import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Arrays;

public class sorting {

    private static int[] arr;
    private static int[] arrCopy;
    private static int[] mergeArr;
    private static BufferedReader read;
    private static Random randomGenerator;

    private static int size;
    private static int random;

    private static void printArray(String msg) {
        System.out.print(msg + " [" + arr[0]);
        for(int i=1; i<size; i++) {
            System.out.print(", " + arr[i]);
        }
        System.out.println("]");
    }

    public static void exchange(int i, int j){
        int t=arr[i];
        arr[i]=arr[j];
        arr[j]=t;
    }

    public static void insertSort(int left, int right) {
        // insertSort the subarray arr[left, right]
        int i, j;

        for(i=left+1; i<=right; i++) {
            int temp = arr[i];           // store a[i] in temp
            j = i;                       // start shifts at i
            // until one is smaller,
            while(j>left && arr[j-1] >= temp) {
                arr[j] = arr[j-1];        // shift item to right
                --j;                      // go left one position
            }
            arr[j] = temp;              // insert stored item
        }  // end for
    }  // end insertSort()

    public static void insertionSort() {
        insertSort(0, size-1);
    } // end insertionSort()


    public static void maxheapify(int i, int n) {
        // Pre: the left and right subtrees of node i are max heaps.
        // Post: make the tree rooted at node i as max heap of n nodes.
        int max = i;
        int left=2*i+1;
        int right=2*i+2;

        if(left < n && arr[left] > arr[max]) max = left;
        if(right < n && arr[right] > arr[max]) max = right;

        if (max != i) {  // node i is not maximal
            exchange(i, max);
            maxheapify(max, n);
        }
    }

    public static void heapsort(){
        // Build an in-place bottom up max heap
        for (int i=size/2; i>=0; i--) maxheapify(i, size);

        for(int i=size-1;i>0;i--) {
            exchange(0, i);       // move max from heap to position i.
            maxheapify(0, i);     // adjust heap
        }
    }

    private static void mergesort(int low, int high) {
        // sort arr[low, high-1]
        // Check if low is smaller then high, if not then the array is sorted
        if (low < high-1) {
            // Get the index of the element which is in the middle
            int middle = (high + low) / 2;
            // Sort the left side of the array
            mergesort(low, middle);
            // Sort the right side of the array
            mergesort(middle, high);
            // Combine them both
            merge(low, middle, high);
        }
    }

    private static void merge(int low, int middle, int high) {
        // merge arr[low, middle-1] and arr[middle, high-1] into arr[low, high-1]

        // Copy first part into the arrCopy array
        for (int i = low; i < middle; i++) mergeArr[i] = arr[i];

        int i = low;
        int j = middle;
        int k = low;

        // Copy the smallest values from either the left or the right side back        // to the original array
        while (i < middle && j < high)
            if (mergeArr[i] <= arr[j])
                arr[k++] = mergeArr[i++];
            else
                arr[k++] = arr[j++];

        // Copy the rest of the left part of the array into the original array
        while (i < middle) arr[k++] = mergeArr[i++];
    }

    public static void naturalMergesort() {
        int run[], i, j, s, t, m;

        run = new int[size/2];

        // Step 1: identify runs from the input array arr[]
        i = m = 1;
        run[0] = 0;
        while (i < size) {
            if (arr[i-1] > arr[i])
                if (run[m-1]+1 == i) {     // make sure each run has at least two

                    j = i+1;
                    s = 0;
                    while (j < size && arr[j-1] >= arr[j]) j++;     // not stable

                    // reverse arr[i-1, j-1];
                    s = i - 1;
                    t = j - 1;
                    while (s < t) exchange(s++, t--);

                    i = j;
                } else
                    run[m++] = i++;
            else i++;
        }

        // Step 2: merge runs bottom-up into one run                                                                       
        t = 1;
        while (t < m) {
            s = t;
            t = s<<1;
            i = 0;
            while (i+t < m) {
                merge(run[i], run[i+s], run[i+t]);
                i += t;
            }
            if (i+s < m) merge(run[i], run[i+s], size);
        }

    }

    private static void quicksort(int low, int high) {
        int i = low, j = high;

        // Get the pivot element from the middle of the list
        int pivot = arr[(high+low)/2];

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller then the pivot
            // element then get the next element from the left list
            while (arr[i] < pivot) i++;

            // If the current value from the right list is larger then the pivot
            // element then get the next element from the right list
            while (arr[j] > pivot) j--;

            // If we have found a value in the left list which is larger than
            // the pivot element and if we have found a value in the right list
            // which is smaller then the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i < j) {
                exchange(i, j);
                i++;
                j--;
            } else if (i == j) { i++; j--; }
        }

        // Recursion
        if (low < j)
            quicksort(low, j);
        if (i < high)
            quicksort(i, high);
    }

    public static void demo1 (String input) {
        // demonstration of sorting algorithms on random input

        long start, finish;
        System.out.println();

        // Heap sort      
        for (int i=0; i<size; i++) arr[i] = arrCopy[i];
        if (size < 101) printArray("in");
        start = System.currentTimeMillis();
        heapsort();
        finish = System.currentTimeMillis();
        if (size < 101) printArray("out");
        System.out.println("heapsort on " + input + " input: " + (finish-start) + " milliseconds.");

        // Merge sort
        for (int i=0; i<size; i++) arr[i] = arrCopy[i];
        if (size < 101) printArray("in");
        start = System.currentTimeMillis();
        mergesort(0, size);
        finish = System.currentTimeMillis();
        if (size < 101) printArray("out");
        System.out.println("mergesort on " + input + " input: " + (finish-start) + " milliseconds.");

        // Natural Merge sort
        for (int i=0; i<size; i++) arr[i] = arrCopy[i];
        if (size < 101) printArray("in");
        start = System.currentTimeMillis();
        naturalMergesort();
        finish = System.currentTimeMillis();
        if (size < 101) printArray("out");
        System.out.println("natural mergesort on " + input + " input: " + (finish-start) + " milliseconds.");

        // Quick sort
        for (int i=0; i<size; i++) arr[i] = arrCopy[i];
        if (size < 101) printArray("in");
        start = System.currentTimeMillis();
        quicksort(0, size-1);
        finish = System.currentTimeMillis();
        if (size < 101) printArray("out");
        System.out.println("quicksort on " + input + " input: " + (finish-start) + " milliseconds.");
    }

    public static void demo2 (String input) {
        // demonstration of sorting algorithms on nearly sorted input

        long start, finish;

        demo1(input);

        // Insert sort on nearly-sorted array      
        for(int i=0; i<size; i++) arr[i] = arrCopy[i];
        if (size < 101) printArray("in");
        start = System.currentTimeMillis();
        insertionSort();
        finish = System.currentTimeMillis();
        if (size < 101) printArray("out");
        System.out.println("insertsort on " + input + " input: " + (finish-start) + " milliseconds.");
    }

    public static void main(String[] args) {

        read = new BufferedReader(new InputStreamReader(System.in));

        randomGenerator = new Random();

        try {
            System.out.print("Please enter the array size : ");
            size = Integer.parseInt(read.readLine());
        } catch(Exception ex){
            ex.printStackTrace();
        }

        // create array
        arr = new int[size];
        arrCopy = new int[size];
        mergeArr = new int[size];

        // fill array
        random = size*10;
        for(int i=0; i<size; i++)
            arr[i] = arrCopy[i] = randomGenerator.nextInt(random);
        demo1("random");

        // arr[0..size-1] is already sorted. We randomly swap 100 pairs to make it nearly-sorted.
        for (int i = 0; i < 100; i++) {
            int j  = randomGenerator.nextInt(size);
            int k  = randomGenerator.nextInt(size);
            exchange(j, k);
        }
        for(int i=0; i<size; i++) arrCopy[i] = arr[i];
        demo2("nearly sorted");

        for(int i=0; i<size; i++) arrCopy[i] = size-i;
        demo1("reversely sorted");
    }

    private static void mergesort2(int low, int high){
        // sort arr[low, high-1]
        // Check if low is smaller then high, if not then the array is sorted
        if (low < high-1) {
            // Get the index of the element which is in the middle
            int middle = (high + low) / 2;
            // Sort the left side of the array
            if(middle - low < 1000){
                insertSort(low, middle - 1);
            }
            else{
                mergesort2(low, middle);
            }
            // Sort the right side of the array
            if(high - middle < 1000){
                insertSort(middle, high - 1);
            }
            else{
                mergesort2(middle, high);
            }
            merge(low, middle, high);
        }
    }

    private static void quicksort2(int low, int high){
        int i = low, j = high;

        int pivot = arr[(high+low)/2];

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller then the pivot
            // element then get the next element from the left list
            while (arr[i] < pivot) i++;

            // If the current value from the right list is larger then the pivot
            // element then get the next element from the right list
            while (arr[j] > pivot) j--;

            // If we have found a value in the left list which is larger than
            // the pivot element and if we have found a value in the right list
            // which is smaller then the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i < j) {
                exchange(i, j);
                i++;
                j--;
            } else if (i == j) { i++; j--; }
        }

        // Recursion
        if (low < j)
            if(j - low < 1000){
                insertSort(low, j);
            }
            else{
                quicksort2(low, j);
            }
        if (i < high)
            if(high - i < 1000){
                insertSort(i, high);
            }
            else{
                quicksort2(i, high);
            }
    }

}




/*Task 1: It is known that when the size of a sorting problem is small, t
hen insertion sort works best. This idea can be used in merge sort and quick sort: For
a recursive call, at first, check if the size of a subarray is less than 1000, use
insertion sort instead. Suppose merge sort and quick sort with this idea are implemented in
methods called mergesort2 and quicksort2. Please create a method called task1, which compares the
performances of four methods, i.e., mergesort, mergesort2, quicksort, and quicksort2, on the same set of
100 random arrays of size 1,000,000. The method task1 will create these 100 examples and summerize the
running time for each method. Based on the experimental results, draw your conclusions. */