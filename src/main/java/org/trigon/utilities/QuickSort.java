package org.trigon.utilities;

import java.util.Arrays;
import java.util.function.ToDoubleFunction;

public class QuickSort {
    /**
     * Uses quick sort to sort an array of objects by their double values.
     * Quick Sort is a sorting method that partitions the array into smaller sections for faster sorting.
     * The sorting is done by selecting a pivot point and moving all values less than it to the left and all values greater than it to the right.
     * This is done recursively until the array is sorted.
     *
     * @param array                  the array to sort
     * @param objectToDoubleFunction the function needed to convert an object in the array to a double
     */
    public static void sort(Object[] array, ToDoubleFunction<Object> objectToDoubleFunction) {
        quickSortByDoubleValue(array, objectToDoubleFunction, 0, array.length - 1);
    }

    /**
     * Runs a loop that partitions the given section of an array, sorts by their double value, and calculates a new pivot.
     * This method uses recursion and keeps calling itself until the array is sorted.
     *
     * @param array                  the array to sort
     * @param objectToDoubleFunction function needed to convert the array to a double
     * @param startIndex             the index of the start of the unsorted section of the array
     * @param endIndex               the index of the end of the unsorted section of the array
     */
    private static void quickSortByDoubleValue(Object[] array, ToDoubleFunction<Object> objectToDoubleFunction, int startIndex, int endIndex) {
        if (startIndex < endIndex) {
            final int pivot = partitionArrayAndGetNewPivot(array, objectToDoubleFunction, startIndex, endIndex);

            quickSortByDoubleValue(array, objectToDoubleFunction, startIndex, pivot - 1);
            quickSortByDoubleValue(array, objectToDoubleFunction, pivot + 1, endIndex);
        }
    }

    /**
     * Partitions the given section of an array by their double value and returns the new pivot.
     *
     * @param array                  the array to partition and sort
     * @param objectToDoubleFunction the function needed to convert an object in the array to a double
     * @param startIndex             the start of the current partition of the array to partition
     * @param endIndex               the end of the current partition of the array to partition
     * @return the index of the new pivot in the whole array
     */
    private static int partitionArrayAndGetNewPivot(Object[] array, ToDoubleFunction<Object> objectToDoubleFunction, int startIndex, int endIndex) {
        final double[] arrayAsDouble = Arrays.stream(array).mapToDouble(objectToDoubleFunction).toArray();
        final double pivot = arrayAsDouble[endIndex];

        int i = startIndex;
        for (int j = startIndex; j < endIndex; j++) {
            if (arrayAsDouble[j] < pivot) {
                swapArrayValues(array, i, j);
                i++;
            }
        }
        swapArrayValues(array, i, endIndex);
        return i;
    }

    /**
     * Swaps the values held by two indices in an array.
     *
     * @param array       the array to swap values in
     * @param firstIndex  the index of the first value to swap
     * @param secondIndex the index of the second value to swap
     */
    private static void swapArrayValues(Object[] array, int firstIndex, int secondIndex) {
        final Object temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
    }
}
