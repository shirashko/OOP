package ascii_art;

import ascii_art.exceptions.EmptyCharacterSetException;
import ascii_art.exceptions.InvalidCommandException;
import image_char_matching.SubImgCharMatcher;

import java.util.TreeSet;

/**
 * Manages a set of ASCII characters for use in ASCII art generation.
 * It interfaces with a {@link SubImgCharMatcher} to update the set of characters
 * used for matching sub-images to characters.
 * @author Shir Rashkovits and Yoav Dolev
 */
public class AsciiCharacterSetManager {
    // Range of printable ASCII characters
    private static final int PRINTABLE_ASCII_START = 32; // First printable ASCII character (space)
    private static final int PRINTABLE_ASCII_END_EXCLUSIVE = 127; // Just after the last printable
    // ASCII character (tilde `~`)

    // Error messages
    private static final String EMPTY_CHAR_SET_ERROR = "Did not execute. Charset is empty.";
    private static final String REMOVE_CHAR_FORMAT_ERROR = "Did not remove due to incorrect format.";
    private static final String ADD_CHAR_FORMAT_ERROR = "Did not add due to incorrect format.";

    // Special keywords
    private static final String ALL_KEYWORD = "all";
    private static final String SPACE_KEYWORD = "space";

    // Special characters
    private static final char SPACE_CHAR = ' ';

    // Default characters to initialize the set with digits
    private static final char[] DEFAULT_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};


    private final TreeSet<Character> sortedChars; // For sorted printing and containing efficient chars
    // checking
                                                // Also useful for checking if charSet is empty
    private final SubImgCharMatcher subImgCharMatcher; // Matcher for associating characters with sub-images

    /**
     * Constructs an AsciiCharacterSetManager with a specific {@link SubImgCharMatcher}.
     * Initializes the character set with default characters.
     *
     */
    public AsciiCharacterSetManager() {
        this.subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHARS);
        this.sortedChars = new TreeSet<>();
        // Initialize with default characters
        for (char c : DEFAULT_CHARS) {
            addChar(c);
        }
    }

    /**
     * Adds characters to the set based on a command string.
     * The command string should be of the format "add <characters>" where <characters> can be:
     * - A single character
     * - The special keyword "space"
     * - A range of characters in the format "a-p"
     * - The special keyword "all"
     * @param commandArray The command string split into an array of words.
     * @throws InvalidCommandException when the command is not recognized or the format is incorrect.
     */
    public void addCharsByCommand(String[] commandArray) throws InvalidCommandException {
        String charsToAdd = commandArray[1];

        // Handle adding a single character or special keyword "SPACE"
        if (charsToAdd.length() == 1) {
            addChar(charsToAdd.charAt(0));
        } else if (SPACE_KEYWORD.equals(charsToAdd)) {
            addChar(SPACE_CHAR);
        } else if (charsToAdd.matches(".-.")) { // Using regex to check if the input is of the format 'a-p'
            char start = charsToAdd.charAt(0);
            char end = charsToAdd.charAt(2);
            addCharsRange(start, end);
        } else if (ALL_KEYWORD.equals(charsToAdd)) {
            addAllAsciiCharacters();
        } else {
            throw new InvalidCommandException(ADD_CHAR_FORMAT_ERROR);
        }
    }

    /**
     * Removes characters from the set based on a command string.
     * The command string should be of the format "remove <characters>" where <characters> can be:
     * - A single character
     * - The special keyword "space"
     * - A range of characters in the format "a-p"
     * - The special keyword "all"
     * @param commandArray The command string split into an array of words.
     * @throws InvalidCommandException when the command is not recognized or the format is incorrect.
     */
    public void removeCharsByCommand(String[] commandArray) throws InvalidCommandException {
        String charsToRemove = commandArray[1];

        // Handle removing a single character or special keyword "SPACE"
        if (charsToRemove.length() == 1) {
            removeChar(charsToRemove.charAt(0));
        } else if (SPACE_KEYWORD.equals(charsToRemove)) {
            removeChar(SPACE_CHAR);
        } else if (ALL_KEYWORD.equals(charsToRemove)) {
            removeAllAsciiCharacters();
        } else if (charsToRemove.matches(".-.")) { // Using regex to check if the input is of the format 'a-p'
            char start = charsToRemove.charAt(0);
            char end = charsToRemove.charAt(2);
            removeCharsRange(start, end);
        } else {
            throw new InvalidCommandException(REMOVE_CHAR_FORMAT_ERROR);
        }
    }

    /**
     * Prints the current set of characters used for ASCII art generation to the console in sorted order by
     * ASCII value.
     * The characters are printed as a space-separated string.
     */
    public void printChars() {
        for (Character c : this.sortedChars) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    /**
     * Validates that the character set is not empty.
     *
     * @throws EmptyCharacterSetException If the character set is empty and no characters are
     * available for ASCII art generation.
     */
    public void validateCharacterSet() throws EmptyCharacterSetException {
        if (sortedChars.isEmpty()) {
            throw new EmptyCharacterSetException(EMPTY_CHAR_SET_ERROR);
        }
    }

    /**
     * Returns the SubImgCharMatcher associated with this manager.
     * @return The SubImgCharMatcher.
     */
    public SubImgCharMatcher getSubImgCharMatcher() {
        return this.subImgCharMatcher;
    }

    /*
     * Adds a character to the set and updates the SubImgCharMatcher.
     *
     * @param c The character to add.
     */
    private void addChar(char c) {
        boolean charExistInCharsSet = this.sortedChars.add(c);
        if (charExistInCharsSet) {
            this.subImgCharMatcher.addChar(c); // Update SubImgCharMatcher accordingly
        }
    }

    /*
     * Removes a character from the set and updates the SubImgCharMatcher.
     *
     * @param c The character to remove.
     */
    private void removeChar(char c) {
        boolean charExistInCharsSet = this.sortedChars.remove(c);
        if (charExistInCharsSet) { // Update SubImgCharMatcher accordingly
            this.subImgCharMatcher.removeChar(c);
        }
    }

    /*
     * Adds a range of characters to the set and updates the SubImgCharMatcher.
     *
     * @param start The start of the character range.
     * @param end   The end of the character range.
     */
    private void addCharsRange(char start, char end) {
        if (start > end) {
            // Swap to ensure start is less than or equal to end
            char temp = start;
            start = end;
            end = temp;
        }
        for (char c = start; c <= end; c++) {
                addChar(c);
            }
    }

    /*
     * Removes a range of characters from the set and updates the SubImgCharMatcher.
     *
     * @param start The start of the character range.
     * @param end   The end of the character range.
     */
    private void removeCharsRange(char start, char end) {
        if (start > end) {
            // Swap to ensure start is less than or equal to end
            char temp = start;
            start = end;
            end = temp;
        }
        for (char c = start; c <= end; c++) {
                removeChar(c);
        }
    }

    /*
     * Adds all printable ASCII characters to the set and updates the SubImgCharMatcher.
     */
    private void addAllAsciiCharacters() {
        for (char c = PRINTABLE_ASCII_START; c < PRINTABLE_ASCII_END_EXCLUSIVE; c++) {
                addChar(c);
            }
    }

    /*
     * Removes all characters from the set and updates the SubImgCharMatcher accordingly.
     */
    private void removeAllAsciiCharacters() {
        for (Character c : new TreeSet<>(this.sortedChars)) {
            removeChar(c); // Leverage removeChar to handle SubImgCharMatcher update
        }
    }
}
