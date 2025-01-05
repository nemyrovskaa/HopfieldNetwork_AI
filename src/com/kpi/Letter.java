package com.kpi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * A Letter object.
 * A Letter described as 5x5 matrix and is stored in ArrayList.
 */
public class Letter {
    /** The amount of <strong>rows</strong> in letter matrix. */
    private static final int LETTER_ROW = 5;
    /** The amount of <strong>columns</strong> in letter matrix. */
    private static final int LETTER_COLUMN = 5;
    /** The total <strong>size</strong> of letter matrix. */
    private static final int LETTER_SIZE = LETTER_COLUMN * LETTER_ROW;
    /** An array that represents <strong>letter</strong> */
    private ArrayList<Float> letter;
    /** The <strong>empty</strong> flag for letter. */
    private boolean empty;

    /**
     * Initializes a default Letter. It is initialized as an empty letter.
     * All values in array are zero.
     */
    public Letter() {
        Float[] weightsArr = new Float[LETTER_SIZE];
        Arrays.fill(weightsArr, 0.0f);
        letter = new ArrayList<>(Arrays.asList(weightsArr));
        empty = true;
    }

    /**
     * Initializes a Letter based on passed argument.
     *
     * @param letter    An array that represents <strong>letter</strong>.
     */
    public Letter(ArrayList<Float> letter) {
        setLetter(letter);
    }

    /**
     * Returns the letter array of this Letter.
     * @return the letter array of this Letter.
     */
    public ArrayList<Float> getLetter() {
        return letter;
    }

    /**
     * Returns the constant amount of rows in Letter.
     * @return the constant amount of rows in Letter.
     */
    public Integer getRowsNum() {
        return LETTER_ROW;
    }

    /**
     * Returns the constant amount of columns in Letter.
     * @return the constant amount of columns in Letter.
     */
    public Integer getColsNum() {
        return LETTER_COLUMN;
    }

    /**
     * Fills an array representing a letter and checks the input array for size.
     * If argument is an empty letter, sets flag in true and vise-versa.
     *
     * @param letter    An array that represents <strong>letter</strong>.
     */
    public void setLetter(ArrayList<Float> letter) {
        if (letter == null)
            throw new NullPointerException("The letter array is null.");

        if (letter.size() != LETTER_SIZE)
            throw new IllegalArgumentException("Size of letter array must be 25 instead of " + letter.size());

        this.letter = letter;
        this.empty = isEmpty();
    }

    /**
     * Checks whether letter is empty.
     * Returns true if the letter is empty (all values in the array are 0) and false otherwise.
     */
    public boolean isEmpty() {
        for (Float i : letter) {
            if (i != 0)
                return false;
        }
        return true;
    }

    /**
     * Converts the Letter object into a string representation. The string uses "#"
     * to represent 1 and "." for 0.
     *
     * @return Returns a String representing the letter in a grid-like format.
     */
    @Override
    public String toString() {
        String retStr = "";

        for (int i = 0; i < LETTER_SIZE; i++)
            retStr += (letter.get(i) == 1 ? "#" : ".") +
                    ((i+1) % 5 == 0 ? "\n" : " ");

        return retStr;
    }

    /**
     * Compares this Letter object with another object to determine if they are equal.
     * Two Letter objects are considered equal if their internal letter representations are the same.
     *
     * @param o The object to compare with this Letter.
     * @return Returns true if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Letter))
            return false;

        Letter letter1 = (Letter) o;
        return Objects.equals(letter, letter1.letter);
    }

    /**
     * Computes the hash code for the Letter object based on its internal letter representation.
     *
     * @return Returns an int hash code value for the Letter object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(letter);
    }
}
