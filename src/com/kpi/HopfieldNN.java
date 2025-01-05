package com.kpi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 *
 */
public class HopfieldNN {
    /** The constant number of neurons in the network, set to 25. */
    private static final int NEURON_NUM = 25;
    /**  An instance of the Library class that contains the set of stored letters for training and recognition. */
    private Library library;
    /** A matrix representing the weights between neurons, used to store the learned associations of the network. */
    private ArrayList<ArrayList<Float>> weights;

    /**
     * Initializes the Hopfield network by creating an empty library and a weight matrix
     * with all elements set to 0.0f.
     */
    public HopfieldNN() {
        library = new Library();

        Float[] weightsArr = new Float[NEURON_NUM];
        Arrays.fill(weightsArr, 0.0f);

        weights = new ArrayList<>();
        for (int i = 0; i < NEURON_NUM; i++) {
            weights.add(new ArrayList<>(Arrays.asList(weightsArr)));
        }
    }

    /**
     * Adds a Letter object to the network's library.
     *
     * @param letter    A <strong>letter</strong> that will be added to the library.
     */
    public void addLetter(Letter letter) {
        library.addLetter(letter);
    }

    /**
     * Trains the network using the letters stored in the library. It calculates the
     * weight matrix using the Hebbian learning rule by updating the weights for each
     * stored letter. The weights are normalized by dividing them by the number of neurons.
     * The diagonal elements of the weight matrix are then set to 0.
     */
    public void train() {
        if(library.empty())
            return;

        for (Letter letter : library.getLetters()) {
            ArrayList<ArrayList<Float>> letterArr = new ArrayList<>();
            letterArr.add(letter.getLetter());
            weights = addMatrices(weights, dotMatrices(transposeMatrix(letterArr), letterArr));
        }
        weights = scalarMult(weights, 1.0f/NEURON_NUM);
        weights = diagonalFill(weights, 0.0f);
    }

    /**
     * Given an input letter, the method predicts the closest matching letter from
     * the network. It runs the input through the network and compares the result
     * to the stored patterns, returning the closest match as a Letter object.
     *
     * @param inputLetter   The Letter object that needs to be predicted (matched to one of the stored letters).
     * @return Returns a Letter object that represents the predicted closest match to the input letter from the network.
     */
    public Letter predict(Letter inputLetter) {
        ArrayList<ArrayList<Float>> inputLetterArr = new ArrayList<>();
        inputLetterArr.add(inputLetter.getLetter());

        ArrayList<ArrayList<Float>> output = predict(inputLetterArr, 100);

        ArrayList<Float> outputLetterArr = new ArrayList<>();
        for (ArrayList<Float> i : output)
            outputLetterArr.addAll(i);

        Letter outputLetter = new Letter(outputLetterArr);

        return outputLetter;
    }

    /**
     * It runs the input through the Hopfield Network for a specified number of
     * iterations (maxIterations). If a stored pattern matches the result, the method
     * returns the output. If no match is found after the specified iterations, it returns null.
     *
     * @param inputLetter   The Letter object that needs to be predicted (matched to one of the stored letters).
     * @param maxIterations The maximum number of iterations to run the prediction process.
     * @return Returns a Letter object that represents the predicted closest match to the input
     * letter from the network. If no match is found within the specified number of iterations, returns null.
     */
    public ArrayList<ArrayList<Float>> predict(ArrayList<ArrayList<Float>> inputLetter, Integer maxIterations) {
        ArrayList<ArrayList<Float>> outputLetter;
        inputLetter = transposeMatrix(inputLetter);

        for (int i = 0; i < maxIterations; i++) {
            outputLetter = signMatrices(dotMatrices(weights, inputLetter));

            for (Letter letter : library.getLetters()) {
                ArrayList<ArrayList<Float>> letterArr = new ArrayList<>();
                letterArr.add(letter.getLetter());

                if (outputLetter.equals(transposeMatrix(letterArr)))
                    return outputLetter;
            }

            inputLetter = outputLetter;
        }
        return null;
    }

    /**
     * Adds <strong>two matrices</strong> element-wise and returns the result.
     * Throws an exception if the matrices have different dimensions.
     *
     * @param matrix1    The first matrix to be added.
     * @param matrix2    The second matrix to be added.
     * @return The result of element-wise sum of the two matrices.
     */
    private ArrayList<ArrayList<Float>> addMatrices(ArrayList<ArrayList<Float>> matrix1,
                                                      ArrayList<ArrayList<Float>> matrix2) {
        int rowsNum = matrix1.size();
        int colsNum = matrix1.get(0).size();

        if (rowsNum != matrix2.size() || colsNum != matrix2.get(0).size())
            throw new IllegalArgumentException("Matrices must have the same dimensions for addition.");

        ArrayList<ArrayList<Float>> resMatrix = new ArrayList<>(rowsNum);
        for (int i = 0; i < rowsNum; i++) {
            ArrayList<Float> row = new ArrayList<>();

            for (int j = 0; j < colsNum; j++) {
                row.add(matrix1.get(i).get(j) + matrix2.get(i).get(j));
            }
            resMatrix.add(row);
        }
        return resMatrix;
    }

    /**
     * Multiplies <strong>two matrices</strong> and returns the resulting matrix.
     * Throws an exception if the matrices cannot be multiplied due to incompatible dimensions.
     *
     * @param matrix1    The first matrix to be multiplied.
     * @param matrix2    The second matrix to be multiplied.
     * @return The result of multiplying the two matrices.
     */
    private ArrayList<ArrayList<Float>> dotMatrices(ArrayList<ArrayList<Float>> matrix1,
                                                            ArrayList<ArrayList<Float>> matrix2) {
        int rows1 = matrix1.size();
        int cols1 = matrix1.get(0).size();
        int rows2 = matrix2.size();
        int cols2 = matrix2.get(0).size();

        if (cols1 != rows2)
            throw new IllegalArgumentException("Matrices dimensions are not suitable for multiplication.");

        ArrayList<ArrayList<Float>> resMatrix = new ArrayList<>();
        Float[] weightsArr = new Float[cols2];
        Arrays.fill(weightsArr, 0.0f);

        for (int i = 0; i < rows1; i++)
            resMatrix.add(new ArrayList<>(Arrays.asList(weightsArr)));

         for (int i = 0; i < rows1; i++)
            for (int j = 0; j < cols2; j++)
                for (int k = 0; k < cols1; k++)
                    resMatrix.get(i).set(j, resMatrix.get(i).get(j) + matrix1.get(i).get(k) * matrix2.get(k).get(j));

        return resMatrix;
    }

    /**
     * Transposes a given <strong>matrix</strong> (rows become columns and vice versa).
     *
     * @param matrix    The matrix to be transposed.
     * @return The transposed matrix.
     */
    private ArrayList<ArrayList<Float>> transposeMatrix(ArrayList<ArrayList<Float>> matrix) {
        int rowsNum = matrix.size();
        int colsNum = matrix.get(0).size();

        ArrayList<ArrayList<Float>> resMatrix = new ArrayList<>();

        for (int j = 0; j < colsNum; j++) {
            ArrayList<Float> row = new ArrayList<>();
            for (int i = 0; i < rowsNum; i++)
                row.add(matrix.get(i).get(j));

            resMatrix.add(row);
        }
        return resMatrix;
    }

    /**
     * Multiplies each element of the <strong>matrix</strong> by a
     * <strong>scalar</strong> value and returns the resulting matrix.
     *
     * @param matrix    The matrix to be multiplied by the scalar.
     * @param scalar    The scalar value to multiply each element of the matrix by.
     * @return The matrix after scalar multiplication.
     */
    private ArrayList<ArrayList<Float>> scalarMult(ArrayList<ArrayList<Float>> matrix, Float scalar) {
        ArrayList<ArrayList<Float>> resMatrix = new ArrayList<>();

        for (int i = 0; i < matrix.size(); i++) {
            ArrayList<Float> row = new ArrayList<>();
            for (int j = 0; j < matrix.get(0).size(); j++) {
                row.add(matrix.get(i).get(j) * scalar);
            }
            resMatrix.add(row);
        }

        return resMatrix;
    }

    /**
     *  Sets all the diagonal elements of a matrix to a specified <strong>value</strong>.
     *
     * @param matrix    The matrix whose diagonal elements will be filled.
     * @param value     The value to set in the diagonal elements of the matrix.
     * @return The matrix with the diagonal elements set to the specified value.
     */
    private ArrayList<ArrayList<Float>> diagonalFill(ArrayList<ArrayList<Float>> matrix, Float value) {
        ArrayList<ArrayList<Float>> resMatrix = new ArrayList<>();
        for (ArrayList<Float> i : matrix)
            resMatrix.add(new ArrayList<>(i));

        for (int i = 0; i < matrix.size(); i++)
                resMatrix.get(i).set(i, value);

        return resMatrix;
    }

    /**
     * Applies a sign function to each element of the <strong>matrix</strong>.
     *
     * @param matrix    The matrix to apply the sign function to.
     * @return The result matrix after sign operation.
     */
    private ArrayList<ArrayList<Float>> signMatrices(ArrayList<ArrayList<Float>> matrix) {
        ArrayList<ArrayList<Float>> resMatrix = new ArrayList<>();

        for (ArrayList<Float> row : matrix) {
            ArrayList<Float> newRow = new ArrayList<>();
            for (Float i : row) {
                newRow.add((i <= 0.0f) ? 1.0f : -1.0f);
            }
            resMatrix.add(newRow);
        }

        return resMatrix;
    }
}
