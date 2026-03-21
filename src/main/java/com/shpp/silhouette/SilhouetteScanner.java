package com.shpp.silhouette;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The program displays the number of silhouettes of large objects
 * in the picture on the console using breadth-first search
 * <p>
 * !if a stack overflow error occurs,
 * the following should be specified in the VM parameters
 * when starting the application: -Xss256m.
 */
public class SilhouetteScanner {

	// Threshold for the percentage difference in color to consider two colors as different
	// 1% - 100% (default - 50%)
	private static final int PERCENTAGE_COLOR_DIFFERENCE = 45;

	// Scale used to determine if a silhouette is considered garbage and should be deleted
	// <1% - 100% (default - 1%)
	private static final double PERCENTAGE_GARBAGE_SCALE = 1;

	// Size of the erosion mask used in the image processing operation.
	private static final int EROSION_MASK_SIZE = 10;

	// List storing the sizes of all identified silhouettes
	private static final ArrayList<Integer> silhouettes = new ArrayList<>();

	// Background color of the image, determined during program execution
	private static Color BACKGROUND_COLOR;

	public static void main(String[] args) {
		SilhouetteScanner obj = new SilhouetteScanner();
		// Path to the directory containing the image file
		String source = "src/main/resources/assets/";

		int result = obj.program(source + (args.length != 0 ? args[0] : "test.jpg"));
		System.out.println(result);
	}

	public int program(String path) {
		try {
			silhouettes.clear();
			// Read the image file into a BufferedImage object
			BufferedImage image = ImageIO.read(new File(path));

			// Determine the background color of the image
			BACKGROUND_COLOR = getBackgroundColor(image);
			int[][] erosionImage = erosion(image);
			countSilhouettes(erosionImage);
			deleteGarbage(erosionImage);
			showImage(image, erosionImage);
			return silhouettes.size();
		} catch (IOException e) {
			throw new RuntimeException("no such file: " + path);
		}
	}

	private void showImage(BufferedImage image, int[][] erosionImage) {
		BinaryImageDisplay display = new BinaryImageDisplay(erosionImage);
		JFrame frame = new JFrame("Binary Image Display");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(display);
		frame.pack();
		frame.setSize(image.getWidth(), image.getHeight());
		frame.setVisible(true);
	}

	/**
	 * Determines the background color of the given BufferedImage.
	 *
	 * @param image The BufferedImage to analyze.
	 * @return The dominant background color.
	 */
	private Color getBackgroundColor(BufferedImage image) {
		// Initialize arrays to store color counters and colors
		int[] counters = new int[2];
		Color[] colors = new Color[2];

		// Set the first color as the color of the pixel at (0, 0)
		colors[0] = new Color(image.getRGB(0, 0));
		Color temp;

		// Iterate over each pixel in the image
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				// Get the color of the current pixel
				temp = new Color(image.getRGB(x, y));

				// Calculate the difference between the current pixel's color and the first color
				if (difference(temp, colors[0]) < PERCENTAGE_COLOR_DIFFERENCE) {
					// If the difference is below the threshold, update the first color and increment its counter
					colors[0] = getAverageColor(colors[0], temp);
					counters[0]++;
				} else {
					// If the difference is above the threshold, update the second color and increment its counter
					if (counters[1] != 0)
						colors[1] = getAverageColor(colors[1], temp);
					else
						colors[1] = temp;

					counters[1]++;
				}
			}
		}
		// Return the color with the higher counter as the background color
		return Math.max(counters[0], counters[1]) == counters[0] ? colors[0] : colors[1];
	}

	/**
	 * Calculates the average color between two input colors.
	 *
	 * @param color1 The first Color object.
	 * @param color2 The second Color object.
	 * @return The average color between color1 and color2.
	 */
	private Color getAverageColor(Color color1, Color color2) {
		// Create and return a new Color object with the calculated average components
		return new Color((color1.getRed() + color2.getRed()) / 2,
				(color1.getGreen() + color2.getGreen()) / 2,
				(color1.getBlue() + color2.getBlue()) / 2);
	}

	/**
	 * Counts silhouettes in the given BufferedImage.
	 *
	 * @param image The BufferedImage to count silhouettes in.
	 */
	private void countSilhouettes(int[][] image) {
		int width = image[0].length;
		int height = image.length;

		// Create a boolean array to mark visited pixels
		boolean[][] visited = new boolean[height][width];

		// Iterate over all pixels in the image
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Check if the current pixel is not visited and is part of a silhouette
				if (!visited[y][x] && image[y][x] == 1)
					silhouettes.add(bfs(image, visited, new int[]{y, x}));
			}
		}
	}

	/**
	 * Performs breadth-first search (BFS) traversal on the image grid starting from the specified first pixel.
	 *
	 * @param image      The image grid.
	 * @param visited    A boolean array to track visited pixels.
	 * @param firstPixel The coordinates of the first pixel to start the BFS traversal.
	 */
	private int bfs(int[][] image, boolean[][] visited, int[] firstPixel) {
		int width = image[0].length;
		int height = image.length;
		int pixelsCounter = 0;
		Queue<int[]> queue = new ArrayDeque<>();
		queue.add(firstPixel);

		while (!queue.isEmpty()) {
			int[] pixel = queue.poll();
			int x = pixel[0];
			int y = pixel[1];

			// Check if the current pixel is out of bounds, visited, or belongs to background (value 0)
			if (x < 0 || x >= height || y < 0 || y >= width
					|| visited[x][y]
					|| image[x][y] == 0) {
				continue; // Skip this pixel and continue to the next iteration of the loop
			}

			visited[x][y] = true; // Mark the current pixel as visited
			pixelsCounter++; // Increment the depth counter

			// Add adjacent pixels to the queue for further traversal
			queue.add(new int[]{x, y + 1}); // Right
			queue.add(new int[]{x + 1, y}); // Down
			queue.add(new int[]{x, y - 1}); // Left
			queue.add(new int[]{x - 1, y}); // Up
		}

		return pixelsCounter;
	}

	/**
	 * Performs erosion on the image using the specified mask size.
	 *
	 * @param image The image on which erosion is performed.
	 * @return An array representing the eroded image.
	 */
	private int[][] erosion(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] erosionImage = new int[height][width];

		// Arrays to store the indices of shifts in the vertical and horizontal directions
		int[] ii = new int[EROSION_MASK_SIZE];
		int[] jj = new int[EROSION_MASK_SIZE];

		// Initialize the arrays of shift indices
		for (int i = 0; i < EROSION_MASK_SIZE; i++) {
			ii[i] = i - (EROSION_MASK_SIZE / 2);
			jj[i] = i - (EROSION_MASK_SIZE / 2);
		}

		// Iterate over each pixel in the image
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				boolean match = true; // Flag to determine if the pixel matches the mask
				// Check if each pixel matches the mask
				for (int i : ii) {
					for (int j : jj) {
						int imageY = y + i; // Y-coordinate of the image, accounting for the shift
						int imageX = x + j; // X-coordinate of the image, accounting for the shift

						// Check if the pixel is within the bounds of the image and matches the mask condition
						if (imageY >= 0 && imageY < height && imageX >= 0 && imageX < width
								&& !isSilhouette(image, imageX, imageY)) {
							match = false; // If any pixel does not match the conditions, set match to false
							break; // Break the loop as there is no need to check other pixels
						}
					}
					if (!match)
						break; // If match is already false, break the loop
				}

				// Set the pixel value based on whether it matches the mask
				erosionImage[y][x] = match ? 1 : 0;
			}
		}

		return erosionImage;
	}

	/**
	 * Determines whether a pixel in the given image is part of a silhouette.
	 *
	 * @param image The BufferedImage containing the pixel.
	 * @param x     The x-coordinate of the pixel.
	 * @param y     The y-coordinate of the pixel.
	 * @return True if the pixel is part of a silhouette, false otherwise.
	 */
	private boolean isSilhouette(BufferedImage image, int x, int y) {
		// Calculate the difference between the color of the pixel and the predefined background color
		double colorDifference = difference(new Color(image.getRGB(x, y)), BACKGROUND_COLOR);

		// Compare the difference to the threshold percentage
		return colorDifference > PERCENTAGE_COLOR_DIFFERENCE;
	}

	/**
	 * Calculates the difference between two colors using a formula based on perceptual color difference.
	 *
	 * @param firstColor  The first Color object.
	 * @param secondColor The second Color object.
	 * @return The calculated difference between the two colors as a percentage.
	 */
	private double difference(Color firstColor, Color secondColor) {
		// Maximum color difference value
		final double MAX_DELTA = 764.8339663572415;
		// Maximum channel value for a color component
		final int maxChannelValue = 255;

		// Calculate differences in red, green, and blue components
		int deltaR = firstColor.getRed() - secondColor.getRed();
		int deltaG = firstColor.getGreen() - secondColor.getGreen();
		int deltaB = firstColor.getBlue() - secondColor.getBlue();

		// Calculate the average red component between the two colors
		double redAverage = 0.5 * (firstColor.getRed() + secondColor.getRed());

		// Calculate the perceptual color difference using the provided formula
		double delta = Math.sqrt((2 + (redAverage / maxChannelValue + 1))
				* Math.pow(deltaR, 2)
				+ (4 * Math.pow(deltaG, 2))
				+ (2 + ((maxChannelValue - redAverage) / maxChannelValue + 1))
				* Math.pow(deltaB, 2));

		// Calculate and return the difference as a percentage of the maximum perceptual color difference
		return delta / MAX_DELTA * 100;
	}

	/**
	 * Deletes silhouettes that are considered garbage based on their size relative to the total image size.
	 *
	 * @param image The BufferedImage containing silhouettes.
	 */
	private void deleteGarbage(int[][] image) {
		// Calculate the total size of the image
		int imageSize = image.length * image[0].length;

		// Variable to store the ratio of silhouette size to image size
		double k;

		// Iterate through the list of silhouettes
		for (int i = 0; i < silhouettes.size(); i++) {
			// Get the size of the current silhouette
			Integer silhouette = silhouettes.get(i);

			// Calculate the ratio of silhouette size to image size in percentage
			k = (double) silhouette / imageSize * 100;

			// If the ratio is less than or equal to the predefined garbage scale percentage
			if (k <= PERCENTAGE_GARBAGE_SCALE) {
				// Remove the silhouette from the list
				silhouettes.remove(silhouette);

				// Decrement the loop counter to handle the removal
				i--;
			}
		}
	}
}