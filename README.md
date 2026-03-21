# Silhouette Scanner

This project is a Java-based application that counts the number of object silhouettes in a given image using Breadth-First Search (BFS) and image processing techniques. It can handle images with slightly noisy backgrounds and correctly identifies shapes ignoring small "garbage" particles.

## 🚀 Features

- **Breadth-First Search (BFS)** for finding and counting disconnected objects.
- **Image Erosion** to remove small noise or connecting bridges between silhouettes.
- **Perceptual Color Difference Algorithm** to distinguish objects from the background automatically, even with slight shadows or gradients.
- **Automated Background Detection** by evaluating boundary colors.
- **Visual Display** showing the resulting eroded binary image (silhouette map) to help debug and understand how the algorithm sees the objects.
- **Garbage Deletion** ignoring shapes that are too small relative to the image size.

## 📁 Project Structure

- `src/main/java/com/shpp/silhouette/SilhouetteScanner.java` - Main algorithm, image processing, BFS logic.
- `src/main/java/com/shpp/silhouette/BinaryImageDisplay.java` - Helper class to draw a visual output of the binary matrix.
- `src/test/java/com/shpp/silhouette/SilhouetteScannerTest.java` - JUnit 5 tests.
- `src/main/resources/assets/` - Folder where input images should be stored.

## 🛠 Prerequisites

- Java 17 (or compatible version).
- IDE (IntelliJ IDEA recommended).
- *Optional:* If you encounter a `StackOverflowError` with very large images, increase the stack size by adding `-Xss256m` (or more) to your VM options. *Note: With the current BFS Queue approach, stack overflow is highly unlikely.*

## ⚙️ How to Use

1. Clone or download this project.
2. Place the image you want to scan inside the `src/main/resources/assets/` folder.
3. Open `SilhouetteScanner.java` and run the `main` method.
4. You can pass the filename as a program argument (e.g. `test1.png`). If no arguments are passed, it defaults to `test.jpg`.

### Example

Input: An image named `test1.png` with 5 distinct dark shapes on a light background.
Output in the console:
```text
5
```
A new window will also pop up showing the black-and-white mask that the program generated to perform the search.

## 🧪 Testing

The project is fully set up for automated testing using **JUnit 5 parameterized tests**.

1. Navigate to `SilhouetteScannerTest.java`.
2. Provide your actual image names and their expected silhouette counts in the `provideSilhouettes()` method.
3. Run the test file.

## 💡 Best Practices Implemented
- Clean package structure (`src/main/java`, `src/main/resources`, `src/test/java`).
- Iterative BFS instead of Recursion (avoids StackOverflow exceptions).
- Use of parameterized tests for clear and scalable test coverage.
- Separation of logic (image processing vs. visual display component).
