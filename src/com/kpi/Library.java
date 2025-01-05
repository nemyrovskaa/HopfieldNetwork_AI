package com.kpi;

import java.util.HashSet;

/**
 * A Library object.
 * A Library object represents a library with letters for training.
 * It has capacity of 3 letters.
 */
public class Library {
    /** Set of <strong>letters</strong> library contains. */
    private HashSet<Letter> letters;

    /**
     * Initializes a default Library. It is initialized as an empty set.
     */
    public Library() {
        letters = new HashSet<>();
    }

    /**
     * Initializes a Library based on passed argument.
     *
     * @param letters   A set of <strong>letters</strong> that represents the library.
     */
    public Library(HashSet<Letter> letters) {
        this.letters = letters;
    }

    /**
     * Adds a letter to the library.
     *
     * @param letter    A <strong>letter</strong> that will be added to the library.
     */
    public void addLetter(Letter letter) {
        letters.add(letter);
    }

    /**
     * Clear library.
     */
    public void clearAll() {
        letters.clear();
    }

    /**
     * Checks whether library is empty.
     * Returns true if library is empty and false otherwise.
     */
    public boolean empty() {
        return letters.isEmpty();
    }

    /**
     * Sets a new Library based on passed argument.
     *
     * @param letters   A new set of <strong>letters</strong> that represents the library.
     */
    public void set(HashSet<Letter> letters) {
        this.letters = letters;
    }

    /**
     * Returns set of letters.
     * @return set of letters.
     */
    public HashSet<Letter> getLetters() {
        return letters;
    }

    /**
     * Generates a string representation of the entire library, where each Letter object in
     * the collection is converted to its string form and separated by a newline character.
     *
     * @return A String where each Letter in the library is represented in its grid-like format, separated by blank lines.
     */
    @Override
    public String toString() {
        String retStr = "";

        for (Letter letter : letters)
            retStr += letter + "\n";

        return retStr;
    }
}
