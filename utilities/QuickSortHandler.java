package frc.trigon.lib.utilities;

import java.util.function.ToDoubleFunction;

/**
 * A class that handles sorting of an array using <a href="https://en.wikipedia.org/wiki/Quicksort">Quick Sort</a>.
 * Quick Sort is a sorting method that partitions the array into smaller sections for faster sorting.
 * The sorting is done by selecting a pivot point and moving all values less than it to the left and all values greater than it to the right.
 * This is done recursively until the array is sorted.
 */
public class QuickSortHandler {
    /**
     * Uses quick sort to sort an array of objects by their double values.
     * Quick Sort is a sorting method that partitions the array into smaller sections for faster sorting.
     * The sorting is done by selecting a pivot point and moving all values less than it to the left and all values greater than it to the right.
     * This is done recursively until the array is sorted.
     *
     * @param array            the array to sort
     * @param toDoubleFunction the function needed to convert an object in the array to a double
     */
    public static <T> void sort(T[] array, ToDoubleFunction<T> toDoubleFunction) {
        quickSortByDoubleValue(array, toDoubleFunction, 0, array.length - 1);
    }

    /**
     * Runs a loop that partitions the given section of an array, sorts by their double value, and calculates a new pivot.
     * This method uses recursion and keeps calling itself until the array is sorted.
     *
     * @param array            the array to sort
     * @param toDoubleFunction the function needed to convert an object in the array to a double
     * @param startIndex       the index of the start of the unsorted section of the array
     * @param endIndex         the index of the end of the unsorted section of the array
     */
    private static <T> void quickSortByDoubleValue(T[] array, ToDoubleFunction<T> toDoubleFunction, int startIndex, int endIndex) {
        if (startIndex < endIndex) {
            final int pivot = partitionArrayAndGetNewPivot(array, toDoubleFunction, startIndex, endIndex);

            quickSortByDoubleValue(array, toDoubleFunction, startIndex, pivot - 1);
            quickSortByDoubleValue(array, toDoubleFunction, pivot + 1, endIndex);
        }
    }

    /**
     * Partitions the given section of an array by their double value and returns the new pivot.
     *
     * @param array            the array to partition and sort
     * @param toDoubleFunction the function needed to convert an object in the array to a double
     * @param startIndex       the start of the current partition of the array to partition
     * @param endIndex         the end of the current partition of the array to partition
     * @return the index of the new pivot in the whole array
     */
    private static <T> int partitionArrayAndGetNewPivot(T[] array, ToDoubleFunction<T> toDoubleFunction, int startIndex, int endIndex) {
        final double[] doubleArray = convertArrayToDoubleArray(array, toDoubleFunction);
        final double pivot = doubleArray[endIndex];

        int i = startIndex;
        for (int j = startIndex; j < endIndex; j++) {
            if (doubleArray[j] < pivot) {
                swapArrayValues(array, i, j);
                i++;
            }
        }
        swapArrayValues(array, i, endIndex);
        return i;
    }

    private static <T> double[] convertArrayToDoubleArray(T[] array, ToDoubleFunction<T> toDoubleFunction) {
        final double[] doubleArray = new double[array.length];
        for (int i = 0; i < array.length; i++)
            doubleArray[i] = toDoubleFunction.applyAsDouble(array[i]);
        return doubleArray;
    }

    /**
     * Swaps the values held by two indices in an array.
     *
     * @param array       the array to swap values in
     * @param firstIndex  the index of the first value to swap
     * @param secondIndex the index of the second value to swap
     */
    private static <T> void swapArrayValues(T[] array, int firstIndex, int secondIndex) {
        final T temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
    }
}
