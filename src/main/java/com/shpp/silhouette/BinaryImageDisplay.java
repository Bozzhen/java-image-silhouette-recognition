package com.shpp.silhouette;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class BinaryImageDisplay extends JPanel {
	private final int[][] binaryImage;

	public BinaryImageDisplay(int[][] binaryImage) {
		this.binaryImage = binaryImage;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int pixelSize = 1;

		for (int y = 0; y < binaryImage.length; y++) {
			for (int x = 0; x < binaryImage[y].length; x++) {
				Color color = binaryImage[y][x] == 1 ? Color.WHITE : Color.BLACK;
				g.setColor(color);
				g.fillRect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
			}
		}
	}
}
