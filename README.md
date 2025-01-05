# Hopfield Neural Network
2023

## Overview
The Hopfield network is a recurrent neural network that functions as an associative memory system. In this project, the network is designed to recognize distorted letter patterns.

### Core Concepts

- Recurrent Nature: The Hopfield network has a single layer of neurons. The output of this layer is fed back as its input, making the network recurrent.
- Input Representation: Letters are represented as arrays of values `1` and `-1`. Reshaping this array into rows forms a visual representation of the letter. For example, a 5x5 letter pattern is stored as a 25-element array.
- Storage Capacity: A Hopfield network with N neurons can store approximately 0.15 · N patterns. For N = 25, this translates to roughly 3 patterns.

### Training Algorithm

1. Weight Matrix Calculation:
   The weights are computed using the formula:

   W = (1 / N) * Σ<sub>i=1</sub><sup>P</sup> (X<sub>i</sub> · X<sub>i</sub><sup>T</sup>)
   
   Where:
   - W is the weight matrix.
   - P is the number of training patterns.
   - X<sub>i</sub> is an input vector, and X<sub>i</sub><sup>T</sup> is its transpose.

3. Self-Connection Removal:
   After computing W, diagonal values are set to zero to eliminate self-connections, as neurons should not influence themselves.

### Pattern Recognition Process

1. Input Distorted Pattern:
   The network accepts a distorted pattern X<sub>in</sub> as input.

2. Iterative Updates:
   The network iterates through the following steps:
   
   X<sub>out</sub> = sgn(W · X<sub>in</sub>)
   
   X<sub>in</sub> = X<sub>out</sub>
   
   Here, the `sgn` function binarizes the output to `1` or `-1`.

3. Stopping Condition:
   The process repeats until the network reaches a stable state or a predefined maximum number of iterations is exceeded.

## Project Setup

1. Prerequisites

Before starting, make sure you have the following installed:

- IntelliJ IDEA (Community or Ultimate Edition)
- Java Development Kit (JDK), preferably JDK 8 or higher
- (Optional) Git, if you plan to clone the repository from a remote source

2. Importing the Project

Open IntelliJ IDEA: Launch IntelliJ IDEA on your computer.

Clone the Repository:

- Open a terminal and run the following command to clone the repository:

`git clone https://github.com/nemyrovskaa/HopfieldNetwork_AI.git`

OR

Use IntelliJ's built-in Git integration:

- Go to File > New > Project from Version Control.
- Enter the repository URL and choose a destination folder.
- If you already have the project folder, go to File > Open in IntelliJ IDEA. Navigate to the folder containing the project and click OK.

3. Building and Running the Project

- Find the file containing the main method named Main.java.
- Right-click the file with the main method.
- Select Run 'Main'.
- IntelliJ will compile the project and execute the main method.
- The output will be displayed in the Run window at the bottom of the IDE.

