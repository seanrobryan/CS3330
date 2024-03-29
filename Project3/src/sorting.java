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

        /* try {
            System.out.print("Please enter the array size : ");
            size = Integer.parseInt(read.readLine());
        } catch(Exception ex){
            ex.printStackTrace();
        } */

        // create array
        arr = new int[size];
        arrCopy = new int[size];
        mergeArr = new int[size];

        // fill array
        /*random = size*10;
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
        demo1("reversely sorted"); */

        try {
            System.out.println("Which task would you like to select? ");
            int task = Integer.parseInt(read.readLine());

            switch(task){
                case 1:
                    task1();
                    break;
                case 2:
                    task2();
                    break;
                case 3:
                    task3();
                    break;
                case 4:
                    task4();
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*

            Start of Task 1

     */

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

        while (i <= j) {
            while (arr[i] < pivot) i++;

            while (arr[j] > pivot) j--;

            if (i < j) {
                exchange(i, j);
                i++;
                j--;
            } else if (i == j) { i++; j--; }
        }

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

    private static void task1(){

        randomGenerator = new Random();

        size = 1000000;

        // create array
        arr = new int[size];
        arrCopy = new int[size];
        mergeArr = new int[size];

        long [] runningTimes = new long[4];
        for(int i = 0; i<4; i++) runningTimes[i] = 0;
        long start, finish;


        for(int i = 0; i < 100; i++){
            // fill array
            random = size*10;
            for(int j=0; j<size; j++) {
                arr[j] = arrCopy[j] = randomGenerator.nextInt(random);
            }

            // Test regular mergesort
            start = System.currentTimeMillis();
            mergesort(0, size);
            finish = System.currentTimeMillis();
            runningTimes[0] += finish - start;

            // Test mergesort2
            for (int j=0; j<size; j++) arr[j] = arrCopy[j];
            start = System.currentTimeMillis();
            mergesort2(0, size);
            finish = System.currentTimeMillis();
            runningTimes[1] += finish - start;

            // Test quicksort
            for (int j=0; j<size; j++) arr[j] = arrCopy[j];
            start = System.currentTimeMillis();
            quicksort(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[2] += finish - start;

            // Test quicksort2
            for (int j=0; j<size; j++) arr[j] = arrCopy[j];
            start = System.currentTimeMillis();
            quicksort2(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[3] += finish - start;
        }

        System.out.println("Total running time for mergesort is: " + runningTimes[0] + " ms");
        System.out.println("Total running time for mergesort2 is: " + runningTimes[1] + " ms");
        System.out.println("Total running time for quicksort is: " + runningTimes[2] + " ms");
        System.out.println("Total running time for quicksort2 is: " + runningTimes[3] + " ms");
    }

    /*

            Start of Task 2

     */

    private static boolean isSorted(int low, int high){
        for(int i = low; i < high; i++){
            if(arr[i] > arr[i + 1]){
                return false;
            }
        }
        return true;
    }

    private static void quicksort3(int low, int high){

            int i = low, j = high;

            int pivot = arr[(high + low) / 2];

            while (i <= j) {
                while (arr[i] < pivot) i++;

                while (arr[j] > pivot) j--;

                if (i < j) {
                    exchange(i, j);
                    i++;
                    j--;
                } else if (i == j) {
                    i++;
                    j--;
                }
            }

        if (!isSorted(low, high)){
            if (low < j) {
                quicksort3(low, j);
            }
            if (i < high) {
                quicksort3(i, high);
            }
        }
    }

    private static void quicksort4(int low, int high) {

            int i = low, j = high;

            int pivot = arr[(high + low) / 2];

            while (i <= j) {
                while (arr[i] < pivot) i++;

                while (arr[j] > pivot) j--;

                if (i < j) {
                    exchange(i, j);
                    i++;
                    j--;
                } else if (i == j) {
                    i++;
                    j--;
                }
            }

        if (!isSorted(low, high)) {
            if (low < j)
                if (j - low < 1000) {
                    insertSort(low, j);
                } else {
                    quicksort4(low, j);
                }
            if (i < high)
                if (high - i < 1000) {
                    insertSort(i, high);
                } else {
                    quicksort4(i, high);
                }
        }
    }

    private static void task2(){

        randomGenerator = new Random();

        size = 1000000;

        // create array
        arr = new int[size];
        arrCopy = new int[size];
        mergeArr = new int[size];

        long [] runningTimes = new long[4];
        for(int i = 0; i < 4; i++) runningTimes[i] = 0;
        long start, finish;


        for(int i = 0; i < 10; i++){
            // fill array
            random = size*10;
            for(int j=0; j<size; j++) {
                arr[j] = arrCopy[j] = randomGenerator.nextInt(random);
            }

            // Test regular quicksort
            start = System.currentTimeMillis();
            quicksort(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[0] += finish - start;

            // Test quicksort2
            for (int j=0; j<size; j++) arr[j] = arrCopy[j];
            start = System.currentTimeMillis();
            quicksort2(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[1] += finish - start;

            // Test quicksort3
            for (int j=0; j<size; j++) arr[j] = arrCopy[j];
            start = System.currentTimeMillis();
            quicksort3(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[2] += finish - start;

            // Test quicksort4
            for (int j=0; j<size; j++) arr[j] = arrCopy[j];
            start = System.currentTimeMillis();
            quicksort4(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[3] += finish - start;
        }

        System.out.println("Total running time for quicksort is: " + runningTimes[0] + " ms");
        System.out.println("Total running time for quicksort2 is: " + runningTimes[1] + " ms");
        System.out.println("Total running time for quicksort3 is: " + runningTimes[2] + " ms");
        System.out.println("Total running time for quicksort4 is: " + runningTimes[3] + " ms");



        size = 10000000;

        // create array
        arr = new int[size];
        arrCopy = new int[size];
        mergeArr = new int[size];

        for(int i = 0; i < 4; i++) runningTimes[i] = 0;


        for(int i = 0; i < 10; i++){
            // fill array
            random = size*10;
            for(int j=0; j<size; j++) {
                arr[j] = arrCopy[j] = j;
            }

            // Test regular quicksort
            start = System.currentTimeMillis();
            quicksort(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[0] += finish - start;

            // Test quicksort2
            for (int j=0; j<size; j++) arr[j] = arrCopy[j];
            start = System.currentTimeMillis();
            quicksort2(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[1] += finish - start;

            // Test quicksort3
            for (int j=0; j<size; j++) arr[j] = arrCopy[j];
            start = System.currentTimeMillis();
            quicksort3(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[2] += finish - start;

            // Test quicksort4
            for (int j=0; j<size; j++) arr[j] = arrCopy[j];
            start = System.currentTimeMillis();
            quicksort4(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[3] += finish - start;
        }

        System.out.println("Total running time for quicksort on a sorted array is: " + runningTimes[0] + " ms");
        System.out.println("Total running time for quicksort2 on a sorted array is: " + runningTimes[1] + " ms");
        System.out.println("Total running time for quicksort3 on a sorted array is: " + runningTimes[2] + " ms");
        System.out.println("Total running time for quicksort4 on a sorted array is: " + runningTimes[3] + " ms");


        for(int i = 0; i < 4; i++) runningTimes[i] = 0;


        for(int i = 0; i < 10; i++){
            // fill array
            random = size*10;
            for(int j=size-1; j >= 0; j--) {
                arr[j] = arrCopy[j] = j;
            }

            // Test regular quicksort
            start = System.currentTimeMillis();
            quicksort(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[0] += finish - start;

            // Test quicksort2
            for (int j=0; j<size; j++) arr[j] = arrCopy[j];
            start = System.currentTimeMillis();
            quicksort2(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[1] += finish - start;

            // Test quicksort3
            for (int j=0; j<size; j++) arr[j] = arrCopy[j];
            start = System.currentTimeMillis();
            quicksort3(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[2] += finish - start;

            // Test quicksort4
            for (int j=0; j<size; j++) arr[j] = arrCopy[j];
            start = System.currentTimeMillis();
            quicksort4(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[3] += finish - start;
        }

        System.out.println("Total running time for quicksort on a reversely sorted array is: " + runningTimes[0] + " ms");
        System.out.println("Total running time for quicksort2 on a reversely sorted array is: " + runningTimes[1] + " ms");
        System.out.println("Total running time for quicksort3 on a reversely unsorted array is: " + runningTimes[2] + " ms");
        System.out.println("Total running time for quicksort4 on a reversely unsorted array is: " + runningTimes[3] + " ms");
    }

    /*

            Start of Task 3
            The methods created will be a variation of quicksort3 because it performed the best,
            or comparably well to the best, of the quicksort algorithms during the testing done in task2.

     */

    private static void quicksort5(int low, int high){
        if (!isSorted(low, high)){

            int i = low, j = high, m = (i + j) / 2;

            int pivot = arr[indexOfMedianOfThree(low, high, m)];

            while (i <= j) {
                while (arr[i] < pivot) i++;

                while (arr[j] > pivot) j--;

                if (i < j) {
                    exchange(i, j);
                    i++;
                    j--;
                } else if (i == j) {
                    i++;
                    j--;
                }
            }

            if (low < j) {
                quicksort(low, j);
            }
            if (i < high) {
                quicksort(i, high);
            }
        }
    }

    private static void quicksort6(int low, int high){
        if (!isSorted(low, high)){

            int i = low, j = high;

            int pivot = arr[indexOfMedianOfNine(low, high)];

            while (i <= j) {
                while (arr[i] < pivot) i++;

                while (arr[j] > pivot) j--;

                if (i < j) {
                    exchange(i, j);
                    i++;
                    j--;
                } else if (i == j) {
                    i++;
                    j--;
                }
            }

            if (low < j) {
                quicksort(low, j);
            }
            if (i < high) {
                quicksort(i, high);
            }
        }
    }

    private static int indexOfMedianOfThree(int x, int y, int z){
        return arr[x] > arr[y] && arr[x] > arr[z] ? (arr[y] > arr[z] ? y : z) : (arr[x] < arr[y] && arr[x] < arr[z] ? (arr[y] < arr[z] ? y : z) : x);
    }

    private static int indexOfMedianOfNine(int x, int y){
        // x  z/9  2z/9  3z/9  4z/9  5z/9  6z/9  7z/9  8z/9  z

        int med1 = indexOfMedianOfThree(x, (2*y)/9, y/3);
        int med2 = indexOfMedianOfThree((4*y)/9, (5*y)/9, (2*y)/3);
        int med3 = indexOfMedianOfThree((7*y)/9, (8*y)/9, y);

        return indexOfMedianOfThree(med1, med2, med3);
    }

    private static void task3(){
        randomGenerator = new Random();

        size = 1000000;

        // create array
        arr = new int[size];
        arrCopy = new int[size];
        mergeArr = new int[size];

        long [] runningTimes = new long[5];
        for(int i = 0; i < 5; i++) runningTimes[i] = 0;
        long start, finish;

        for(int i = 0; i < 100; i++){
            // fill array
            random = size*10;
            for(int j=0; j<size; j++) {
                arr[j] = arrCopy[j] = randomGenerator.nextInt(random);
            }

            // Test regular heapsort
            start = System.currentTimeMillis();
            heapsort();
            finish = System.currentTimeMillis();
            runningTimes[0] += finish - start;

            // Test quicksort
            restoreArray();
            start = System.currentTimeMillis();
            quicksort(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[1] += finish - start;

            // Test quicksort3
            restoreArray();
            start = System.currentTimeMillis();
            quicksort3(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[2] += finish - start;

            // Test quicksort5
            restoreArray();
            start = System.currentTimeMillis();
            quicksort5(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[3] += finish - start;

            // Test quicksort6
            restoreArray();
            start = System.currentTimeMillis();
            quicksort6(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[4] += finish - start;
        }

        System.out.println("Total running time for heapsort is: " + runningTimes[0] + " ms");
        System.out.println("Total running time for quicksort is: " + runningTimes[1] + " ms");
        System.out.println("Total running time for quicksort3 is: " + runningTimes[2] + " ms");
        System.out.println("Total running time for quicksort5 is: " + runningTimes[3] + " ms");
        System.out.println("Total running time for quicksort6 is: " + runningTimes[4] + " ms");


        for(int i = 0; i < 5; i++) runningTimes[i] = 0;

        for(int i = 0; i < 10; i++){
            // fill array
            for(int j=size-1; j >= 0; j--) {
                arr[j] = arrCopy[j] = j;
            }

            // Test regular heapsort
            start = System.currentTimeMillis();
            heapsort();
            finish = System.currentTimeMillis();
            runningTimes[0] += finish - start;

            // Test quicksort
            restoreArray();
            start = System.currentTimeMillis();
            quicksort(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[1] += finish - start;

            // Test quicksort3
            restoreArray();
            start = System.currentTimeMillis();
            quicksort3(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[2] += finish - start;

            // Test quicksort5
            restoreArray();
            start = System.currentTimeMillis();
            quicksort5(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[3] += finish - start;

            // Test quicksort6
            restoreArray();
            start = System.currentTimeMillis();
            quicksort6(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[4] += finish - start;
        }

        System.out.println("Total running time for heapsort on a reversely sorted array is: " + runningTimes[0] + " ms");
        System.out.println("Total running time for quicksort on a reversely sorted array is: " + runningTimes[1] + " ms");
        System.out.println("Total running time for quicksort3 on a reversely sorted array is: " + runningTimes[2] + " ms");
        System.out.println("Total running time for quicksort5 on a reversely sorted array is: " + runningTimes[3] + " ms");
        System.out.println("Total running time for quicksort6 on a reversely sorted array is: " + runningTimes[4] + " ms");

        for(int i = 0; i < 5; i++) runningTimes[i] = 0;

        for(int i = 0; i < 10; i++){
            // fill array
            for(int j = 0; j < size/2; j++){
              arr[j] = (size/2) - j + 1;
            }
            for(int j = size - 1; j >= 0; j--){
                arr[j] = j - (size/2) + 1;
            }

            // Test regular heapsort
            start = System.currentTimeMillis();
            heapsort();
            finish = System.currentTimeMillis();
            runningTimes[0] += finish - start;

            // Test quicksort
            restoreArray();
            start = System.currentTimeMillis();
            quicksort(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[1] += finish - start;

            // Test quicksort3
            restoreArray();
            start = System.currentTimeMillis();
            quicksort3(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[2] += finish - start;

            // Test quicksort5
            restoreArray();
            start = System.currentTimeMillis();
            quicksort5(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[3] += finish - start;

            // Test quicksort6
            restoreArray();
            start = System.currentTimeMillis();
            quicksort6(0, size - 1);
            finish = System.currentTimeMillis();
            runningTimes[4] += finish - start;
        }

        System.out.println("Total running time for heapsort on an organ shaped array is: " + runningTimes[0] + " ms");
        System.out.println("Total running time for quicksort on a an organ shaped array is: " + runningTimes[1] + " ms");
        System.out.println("Total running time for quicksort3 on an organ shaped array is: " + runningTimes[2] + " ms");
        System.out.println("Total running time for quicksort5 on an organ shaped array is: " + runningTimes[3] + " ms");
        System.out.println("Total running time for quicksort6 on an organ shaped array is: " + runningTimes[4] + " ms");
    }

    /*

        Start of Task 4
        quicksort5 performed about as well as the other fast quicksort algorithm, quicksort6, on random arrays but
        performed much better on arrays with a large degree of order at the beginning so this version will be used
        in this task.

    */


    private static void makeKDistanceArr(int k){
        random = size*10;
        for(int j=0; j<size; j++) {
            arr[j] = randomGenerator.nextInt(random);
        }
        quicksort5(0, size - 1);

        for (int i = 0; i < size - k; i+=k) {
            for (int j = (k - 1) / 2; j > 0; j--){
                exchange(randomGenerator.nextInt(k) + i, randomGenerator.nextInt(k) + i);
            }
        }

        for(int j = 0; j < size; j++) arrCopy[j] = arr[j];

    }

    private static void makeKExchangeArr(int k){
        random = size*10;
        for(int j=0; j<size; j++) {
            arr[j] = randomGenerator.nextInt(random);
        }
        quicksort5(0, size - 1);

        while (k > 0){
            exchange(randomGenerator.nextInt(size), randomGenerator.nextInt(size));
           k--;
        }

        for(int i = 0; i < size; i++) arrCopy[i] = arr[i];
    }

    private static void task4(){

        size = 5000000;
        long [] runningTimes = {0, 0, 0, 0, 0};
        long start, finish;
        arr = new int[size];
        arrCopy = new int[size];
        mergeArr = new int[size];

        int [] ks = {10, 20, 40, 80, 160, 320};

        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 100; j++){
                makeKDistanceArr(ks[i]);

                // Test quicksort5
                start = System.currentTimeMillis();
                quicksort5(0, size - 1);
                finish = System.currentTimeMillis();
                runningTimes[0] += finish - start;

                // Test heapsort
                restoreArray();
                start = System.currentTimeMillis();
                heapsort();
                finish = System.currentTimeMillis();
                runningTimes[1] += finish - start;

                // Test mergesort
                restoreArray();
                start = System.currentTimeMillis();
                mergesort(0, size - 1);
                finish = System.currentTimeMillis();
                runningTimes[2] += finish - start;

                // Test natural mergesort
                restoreArray();
                start = System.currentTimeMillis();
                naturalMergesort();
                finish = System.currentTimeMillis();
                runningTimes[3] += finish - start;

                // Test insertion sort
                restoreArray();
                start = System.currentTimeMillis();
                insertionSort();
                finish = System.currentTimeMillis();
                runningTimes[4] += finish - start;
            }

            System.out.println("Total running time for quicksort5 on a k-distance array where k is " + ks[i] + ": " + runningTimes[0] + " ms");
            System.out.println("Total running time for heapsort on a k-distance array where k is " + ks[i] + ": " + runningTimes[1] + " ms");
            System.out.println("Total running time for mergesort on a k-distance array where k is " + ks[i] + ": " + runningTimes[2] + " ms");
            System.out.println("Total running time for natural mergesort on a k-distance array where k is " + ks[i] + ": " + runningTimes[3] + " ms");
            System.out.println("Total running time for insertion sort on a k-distance array where k is " + ks[i] + ": " + runningTimes[4] + " ms");

        }

        for (int i = 0; i < 6; i++){
            ks[i] = 100 * (int) Math.pow(2, i);

            for (int j = 0; j < 100; j++){
                makeKExchangeArr(ks[i]);

                // Test quicksort5
                start = System.currentTimeMillis();
                quicksort5(0, size - 1);
                finish = System.currentTimeMillis();
                runningTimes[0] += finish - start;

                // Test heapsort
                restoreArray();
                start = System.currentTimeMillis();
                heapsort();
                finish = System.currentTimeMillis();
                runningTimes[1] += finish - start;

                // Test mergesort
                restoreArray();
                start = System.currentTimeMillis();
                mergesort(0, size - 1);
                finish = System.currentTimeMillis();
                runningTimes[2] += finish - start;

                // Test natural mergesort
                restoreArray();
                start = System.currentTimeMillis();
                naturalMergesort();
                finish = System.currentTimeMillis();
                runningTimes[3] += finish - start;

                // Test insertion sort
                restoreArray();
                start = System.currentTimeMillis();
                insertionSort();
                finish = System.currentTimeMillis();
                runningTimes[4] += finish - start;
            }
            System.out.println("Total running time for quicksort5 on a k-exchange array where k is " + ks[i] + ": " + runningTimes[0] + " ms");
            System.out.println("Total running time for heapsort on a k-exchange array where k is " + ks[i] + ": " + runningTimes[1] + " ms");
            System.out.println("Total running time for mergesort on a k-exchange array where k is " + ks[i] + ": " + runningTimes[2] + " ms");
            System.out.println("Total running time for natural mergesort on a k-exchange array where k is " + ks[i] + ": " + runningTimes[3] + " ms");
            System.out.println("Total running time for insertion sort on a k-exchange array where k is " + ks[i] + ": " + runningTimes[4] + " ms");
        }
    }

    private static void restoreArray(){
        for (int i=0; i<size; i++) arr[i] = arrCopy[i];
    }
}