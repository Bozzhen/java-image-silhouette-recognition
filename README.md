# java-image-silhouette-recognition
A Java-based image processing tool that uses the Breadth-First Search (BFS) algorithm and custom erosion logic to detect and count silhouettes in images.

# Silhouette Recognition Tool (Java)

A high-performance Java application that identifies and counts objects (silhouettes) in images using the **Breadth-First Search (BFS)** algorithm.

##  Key Features
* **BFS-based Segmentation:** Efficiently explores connected components (pixels) to identify separate objects.
* **Noise Reduction (Erosion):** Implements a custom image erosion algorithm to remove "garbage" pixels and improve accuracy.
* **Perceptual Color Difference:** Uses a professional formula to distinguish objects from the background based on human color perception (Delta E).
* **Automated Background Detection:** Automatically determines the background color to minimize configuration.

##  Tech Stack
* **Language:** Java 11+
* **Libraries:** `javax.imageio`, `java.awt`
* **Concepts:** Algorithms, Data Structures (Queues), Image Processing.

##  How it works
1. The program analyzes the image and identifies the background color.
2. It applies an **Erosion mask** to clean up the image.
3. Using **BFS**, it traverses the pixel grid to find connected silhouettes.
4. Objects smaller than a certain threshold are discarded as noise.
5. The total count of silhouettes is displayed in the console.
