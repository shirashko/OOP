package image_char_matching;

import java.util.*;

/**
 * Manages the mapping of characters to their corresponding brightness levels for ASCII art generation.
 * This class facilitates the selection of characters based on brightness to accurately represent
 * parts of an image in ASCII art.
 * @author Shir Rashkovits and Yoav Dolev
 */
public class SubImgCharMatcher {
    // A tree map that maps normalized brightness levels to sets of characters with the same brightness.
    private final TreeMap<Double, TreeSet<Character>> normalizedCharMap;
    // A map that maps characters to their brightness levels.
    private final HashMap<Character, Double> charsToBrightness; // un-normalized
    private Double minBrightness; // un-normalized
    private Double maxBrightness; // un-normalized


    /**
     * Initializes a new SubImgCharMatcher with a predefined set of characters.
     * Assumed to get at least one character.
     *
     * @param chars An array of characters to be initially added to the matcher.
     */
    public SubImgCharMatcher(char[] chars) {
        // Initialize data structures
        this.normalizedCharMap = new TreeMap<>();
        this.charsToBrightness = new HashMap<>();

        // Initialize min and max brightness for safe initialization (although we assumed to be initialized
        // with at least one character)
        this.minBrightness = Double.MAX_VALUE;
        this.maxBrightness = Double.MIN_VALUE;

        for (char curChar: chars) {
            addChar(curChar);
        }
    }

    /**
     * Adds a character to the matcher with its corresponding brightness level.
     * Updates the mapping and adjusts normalization if necessary.
     *
     * @param c The character to add.
     */
    public void addChar(char c) {
        if (doesCharExist(c)) { // if the character is already in the map, do nothing
            return;
        }
        double brightness = getCharBrightness(c);
        charsToBrightness.put(c, brightness);

        if (brightness < minBrightness || brightness > maxBrightness) {
            // When we get a new min/max, we have to re-normalize each item in the tree map, so we need
            // to rebuild it
            minBrightness = Math.min(brightness, minBrightness);
            maxBrightness = Math.max(brightness, maxBrightness);
            rebuildTreeMapByHashMap();
        } else {
            addCharacterToTreeMap(c, brightness);
        }
    }

    /**
     * Removes a character from the matcher, along with its brightness mapping.
     * Adjusts the tree map if necessary.
     *
     * @param c The character to remove.
     */
    public void removeChar(char c) {
        if (!doesCharExist(c)) { // if the character is not in the map, do nothing
            return;
        }

        double brightness = charsToBrightness.remove(c); // Returns the previous value associated with key
        boolean minOrMaxWereUpdated = updateMinOrMaxInRemovalIfNeeded(c, brightness);
        if (minOrMaxWereUpdated) {
            rebuildTreeMapByHashMap(); // Rebuild without the removed character
        } else {
            removeCharacterFromTreeMap(c, brightness);
        }

        if (normalizedCharMap.isEmpty()) {
            this.minBrightness = Double.MAX_VALUE;
            this.maxBrightness = Double.MIN_VALUE;
        }
    }

    private boolean doesCharExist(char c) {
        return charsToBrightness.containsKey(c);
    }

    /**
     * Finds the character that best matches a given brightness value.
     * This function assumed to be called when the characters set is not empty.
     *
     * @param brightness The target brightness value, normalized to the range [0, 1].
     * @return The character that closest matches the specified brightness, or the character with the
     * smallest ASCII value if more than one character has the same brightness.
     */
    public char getCharByImageBrightness(double brightness) {
        double nearestKey = findNearestBrightnessKey(brightness);
        return normalizedCharMap.get(nearestKey).first();
    }

    /*
     * This method is used to update the min and max brightness if the removed character was the min or max.
     * This method assumes the char hasn't been removed from the tree map yet.
     */
    private boolean updateMinOrMaxInRemovalIfNeeded(char c, double brightness) {
        boolean isMin = (brightness == minBrightness);
        boolean isMax = (brightness == maxBrightness);

        double normalizedBrightness = getNormalizedBrightness(brightness);
        if (isMin || isMax) { // check if it is the only min/max, and if it is, update the min/max
            TreeSet<Character> charsWithSameBrightness = normalizedCharMap.get(normalizedBrightness);
            if (isOnlyCharExistWithSpecifiedBrightness(c, charsWithSameBrightness)) {
                if (isMin) {
                    minBrightness = normalizedCharMap.ceilingKey(Double.MIN_VALUE);
                } else {
                    maxBrightness = normalizedCharMap.floorKey(Double.MAX_VALUE);
                }
            }
            return true;
        }
        return false;
    }

    /*
     * Checks if the specified character is the only character in the set with the specified brightness.
     */
    private static boolean isOnlyCharExistWithSpecifiedBrightness(char c,
                                                                TreeSet<Character> charsWithSameBrightness) {
        return charsWithSameBrightness != null && charsWithSameBrightness.size() == 1 &&
                charsWithSameBrightness.contains(c);
    }

    /*
     * Adds a character to the tree map.
     */
    private void addCharacterToTreeMap(char c, double brightness) {
        double normalizedBrightness = getNormalizedBrightness(brightness);
        // If the brightness is already in the map, add the character to the set.
        // Otherwise, create a new set with the character.
        normalizedCharMap.computeIfAbsent(normalizedBrightness, k -> new TreeSet<>()).add(c);
    }

    /*
     * Removes a character from the tree map.
     * This method assumes that the char has already been removed from the charsToBrightness map.
     */
    private void removeCharacterFromTreeMap(char c, double brightness) {
        double normalizedBrightness = getNormalizedBrightness(brightness);
        TreeSet<Character> chars = normalizedCharMap.get(normalizedBrightness);
        if (chars != null) {
            chars.remove(c);
            if (chars.isEmpty()) {
                normalizedCharMap.remove(normalizedBrightness);
            }
        }
    }

    /*
     * Rebuilds the tree map from the unnormalized brightness map.
     * This method assumes that the charsToBrightness map has been updated, and the chars need to be
     * in the rebuilt tree map are the ones in the charsToBrightness map.
     */
    private void rebuildTreeMapByHashMap() {
        normalizedCharMap.clear();
        charsToBrightness.forEach(this::addCharacterToTreeMap);
    }

    /*
     * Normalizes a brightness value to the range [0, 1].
     * This function is assumed to be called when the min and max brightness are not equal.
     */
    private double getNormalizedBrightness(double brightness) {
        // case of maxBrightness == minBrightness? or not necessary?
        return (brightness - minBrightness) / (maxBrightness - minBrightness);
    }

    /*
     * Finds the nearest brightness key in the tree map to the specified brightness.
     */
    private Double findNearestBrightnessKey(double brightness) {
        Double charWithClosestLowerBrightness = normalizedCharMap.floorKey(brightness);
        Double charWithClosestHigherBrightness = normalizedCharMap.ceilingKey(brightness);

        // If we have both a lower and a higher, we need to find out which is closer
        if (charWithClosestLowerBrightness != null && charWithClosestHigherBrightness != null) {
            return chooseNearestBrightnessFromHighAndLow(brightness, charWithClosestLowerBrightness,
                                                         charWithClosestHigherBrightness);
        } else {
            // If there's no lower or higher, we return whatever we have
            return charWithClosestLowerBrightness != null ? charWithClosestLowerBrightness :
                                                                        charWithClosestHigherBrightness;
        }
    }

    /*
     * Chooses the nearest brightness from the lower and higher brightnesses.
     */
    private double chooseNearestBrightnessFromHighAndLow(double brightness, double lowerBrightness,
                                                         double higherBrightness) {
        double absDiffFromLower = Math.abs(brightness - lowerBrightness);
        double absDiffFromHigher = Math.abs(brightness - higherBrightness);

        if (absDiffFromLower < absDiffFromHigher) {
            return lowerBrightness;
        } else if (absDiffFromLower > absDiffFromHigher) {
            return higherBrightness;
        } else {
            // If the differences are equal, we retrieve the sets for both brightnesses
            TreeSet<Character> lowerChars = normalizedCharMap.get(lowerBrightness);
            TreeSet<Character> higherChars = normalizedCharMap.get(higherBrightness);

            // We choose the one with the smaller ASCII code (the first character in each set)
            char lowerChar = lowerChars.first();
            char higherChar = higherChars.first();

            // Compare the ASCII values of the characters
            return (lowerChar < higherChar) ? lowerBrightness : higherBrightness;
        }
    }


    /*
     * Returns the brightness of a character (the average brightness of its pixels).
     * Called when the character is not in the map.
     */
    private double getCharBrightness(char c) {
        boolean[][] blackAndWhiteCharImage =  CharConverter.convertToBoolArray(c);
        int height = blackAndWhiteCharImage.length;
        int width = blackAndWhiteCharImage[0].length;
        int brightnessSum = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (blackAndWhiteCharImage[row][col]) {
                    brightnessSum++;
                }
            }
        }

        int numberOfPixelsInCharImage = height * width;
        return (double) brightnessSum / numberOfPixelsInCharImage;
    }
}
