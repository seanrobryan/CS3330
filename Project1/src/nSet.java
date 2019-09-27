import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

/**
 * For complexity analysis, let m be this.max and n be the number of elements in this set.
 */

public class nSet {
    // this class implements the set operations over sets of natural numbers.
    public int Max;        // maximal natural number in any set
    private int n_long;    // the number of long integers: 64*n_long > Max
    private long[] store;  // the store has n_long longs
    private int size;      // remember the size of the current set

    // Constructor: runtime = O(m), with a small constant
    public nSet(int n) {
        this.Max = n;
        if (n <= 0) { this.Max = 1; }
        this.n_long = (n >> 6) + 1; // n_long = n/64+1, number of longs
        this.store = new long[n_long];
        for (int i = 0; i<this.n_long; i++) this.store[i] = 0;
        this.size = 0;   // the empty set: 
    }

    //Constant runtime
    public void add(int x) {
        // add x into the set
        if (x < 0 || x > this.Max) return;
        int i = (x>>6);     // i = x/64
        int j = x - (i<<6); // j = x % 64, i.e., 64i + j = x
        long y = this.store[i];
        if (((y>>>j) & 1) == 1) return;   // if x is present, do nothing
        this.store[i] |= ((long) 1<<j);  // "|" is the bitwise OR operation
        this.size++;
    }


    //Constant runtime
    public boolean find(int x) {
        // decide if x is in the set
        if (x < 0 || x > this.Max) return false;
        int i = (x>>6);     // i = x/64
        int j = x - (i<<6); // j = x % 64, i.e., 64i + j = x
        long y = this.store[i];
        return ((y>>>j) & 1) == 1; // "&" is the bitwise OR operation
    }


    //O(m) linear time, with a small constant
    public void clear () {
        for(int i = 0; i<this.n_long; i++) store[i]=0;
        this.size = 0;
    }


    // Constant runtime
    public int size () {
        return this.size;
    }


    // O(m) linear time
    private void set_size () {
        int counter = 0;
        for(int i = 0; i<this.n_long; i++) {
            for (int j = 0; j < 64; j++) {
                if(((this.store[i]>>>j) & 1) == 1)
                    counter++;
            }
        }
        this.size = counter;
    }

    // Constant runtime
    public void print () {
        // print up to 30 numbers in the current nSet
        System.out.print("{ ");
        int count = 0;
        for(int i=0; i<this.n_long; i++)
            for(int j=0; j<64 ; j++)
                if (((this.store[i] >>> j) & 1) == 1) {
                    System.out.print(((i << 6) + j)+", ");
                    if (++count >= 30) {
                        System.out.println("... }");
                        return;
                    }
                }
        System.out.println("}");
    }


    // O(m) runtime
    public nSet union (nSet X) {
        int maximum = max(this.Max, X.Max);
        nSet A = new nSet(maximum);

        for(int i=0 ;i < n_long; i++) {
            A.store[i] = this.store[i] | X.store[i];
        }

        A.set_size();
        return A;
    }


    //You need to complete the implementation of the following operations:

    public boolean isEmpty () {
        // return true iff the current nSet is empty
        return size == 0;
    }

    public boolean delete (int x) {
        // return false if x isn't in the set;
        // delete the number x from the current set and return true.
            int i = (x>>6);
            int j = x - (i<<6);
            long y = this.store[i];
            if (((y>>>j) & 1) != 1) return false;   // if x is present, do nothing
            this.store[i] ^= ((long) 1<<j);  //
            this.size--;
            return true;
    }


	public nSet intersect (nSet X) {
	   // return a new nSet which is the intersection of the current nSet and X
        nSet intersection = new nSet(Math.min(X.Max, this.Max));

        for(int i = 0; i<Math.max(X.Max, this.Max); i++){
            if(X.find(i) && this.find(i)){
                intersection.add(i);
            }
        }
        return intersection;
	} 
	
    public nSet subtract (nSet X) {
	   // return a new nSet which is the subtraction of the current nSet by X
        nSet difference = new nSet(Math.max(X.Max, this.Max));

        for(int i = 0; i<Math.max(X.Max, this.Max); i++){
            if(X.find(i) && !this.find(i)){
                difference.add(i);
            }
            else if(!X.find(i) && this.find(i)){
                difference.add(i);
            }
        }
        return difference;
	} 
	
    public nSet complement() {
	   // return a new nSet which is the complement of the current nSet
        nSet complementarySet = new nSet(this.Max);
        for (int i = 0; i < this.n_long; i++) {
            complementarySet.store[i] = ~this.store[i];
        }
        complementarySet.size = this.Max + 1 - this.size;
        return complementarySet;
	} 

	public boolean equal(nSet X) {
	   // return true iff X and the current nSet contain the same set of numbers
        return X.store.equals(this.store);
	}
	
	public boolean isSubset(nSet X) {
	   // return true iff X is a subset of the current nSet
        for (int i = 0; i<X.size; i++){
            if(X.store[i] != this.store[i]){
                return false;
            }
        }
        return true;
	}


	// Most of the logic was striped from the print function
	public int[] toArray () {
	   // return an int array which contains all the numbers in the current nSet
        int[] arr = new int[this.size];
        int count = 0;
        for(int i=0; i<this.n_long; i++)
            for(int j=0; j<64 ; j++)
                if (((this.store[i] >>> j) & 1) == 1) {
                    arr[count] = (i << 6 ) + j;
                    count++;
                }
        return arr;
	} 

	// O(m*2^m)
	public void enumerate() {
	   // Enumerate all subsets of the current nSet and print them out.
	   // You may assume the current nSet contains less than 30 numbers.
        int [] arr = this.toArray();
        int n = this.size;

        for(int i = 0; i<(1<<n); i++){
            System.out.print("{");
            for (int j = 0; j < n; j++){
                if((i & (1 << j)) > 0) {
                    System.out.print(arr[j] + " ");
                }
            }
            System.out.print("} ");
        }
	}



    public static void main(String[] args) {
        // testing
        nSet A = new nSet(1000);

        for (int i = 1; i<A.Max; i += i) {
            A.add(i-1);
            A.add(i);
            A.add(i+1);
        }

        for (int i = 0; i<=A.Max; i++) {
            if (A.find(i)) System.out.println("found " + i + " in A");
        }
        
        
        // more testing code
        nSet B = new nSet(1000); // all natural numbers <= 1000, is a power of 2
        // Powers of two will be a whole number when taken as a log_2
        for(int i = 0; i<B.Max; i++){
            if((Math.log(i)/Math.log(2) % 1 == 0)){
                B.add(i);
            }
        }

        nSet C = new nSet(1000); // all odd natural numbers <= 1000
        // The last digit of all odd binaries is 1
        for(int i = 0; i<=C.Max; i++){
            if((i & 1) == 1){
                C.add(i);
            }
        }
        nSet D = C.complement();

        nSet E = D.union(B);

        if (D.equal(E))
            System.out.println("D is equal to E");
        else
            System.out.println("D is not equal to E");

        nSet F = A.intersect(B);

        if (B.equal(F))
            System.out.println("B is equal to F");
        else
            System.out.println("B is not equal to F");

        nSet G = new nSet(1000); // all natural numbers <= 1000, and divisible by 8
        for(int i = 0; i<=G.Max; i+=8){G.add(i);}
                
        nSet H = A.intersect(G);
        if (G.equal(H))
            System.out.println("G is equal to H");
        else
            System.out.println("G is not equal to H");

        nSet I = G.subtract(D);
        if (I.isEmpty())
            System.out.println("I is empty");
        else
            System.out.println("I is not empty");

        nSet J = H.intersect(E);
        
        nSet K = H.complement();
        
        // print out the sizes and members of A, B, C, D, E, F, G, H, I, J, K
        System.out.print(A.size() + " is the size of A = "); A.print();
        System.out.print(B.size() + " is the size of B = "); B.print();
        System.out.print(C.size() + " is the size of C = "); C.print();
        System.out.print(D.size() + " is the size of D = "); D.print();
        System.out.print(E.size() + " is the size of E = "); E.print();
        System.out.print(F.size() + " is the size of F = "); F.print();
        System.out.print(G.size() + " is the size of G = "); G.print();
        System.out.print(H.size() + " is the size of H = "); H.print();
        System.out.print(I.size() + " is the size of I = "); I.print();
        System.out.print(J.size() + " is the size of J = "); J.print();
        System.out.print(K.size() + " is the size of K = "); K.print();
        
        System.out.println("All subsets of H:");
        H.enumerate();

    }
}

/*
found 0 in A
found 1 in A
found 2 in A
found 3 in A
found 4 in A
found 5 in A
found 7 in A
found 8 in A
found 9 in A
found 15 in A
found 16 in A
found 17 in A
found 31 in A
found 32 in A
found 33 in A
found 63 in A
found 64 in A
found 65 in A
found 127 in A
found 128 in A
found 129 in A
found 255 in A
found 256 in A
found 257 in A
found 511 in A
found 512 in A
found 513 in A
D is not equal to E
B is not equal to F
G is not equal to H
I is not empty
27 is the size of A = { 0, 1, 2, 3, 4, 5, 7, 8, 9, 15, 16, 17, 31, 32, 33, 63, 64, 65, 127, 128, 129, 255, 256, 257, 511, 512, 513, }
10 is the size of B = { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, }
500 is the size of C = { 1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, 41, 43, 45, 47, 49, 51, 53, 55, 57, 59, ... }
501 is the size of D = { 0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, ... }
525 is the size of E = { 0, 1, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, ... }
10 is the size of F = { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, }
126 is the size of G = { 0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 80, 88, 96, 104, 112, 120, 128, 136, 144, 152, 160, 168, 176, 184, 192, 200, 208, 216, 224, 232, ... }
8 is the size of H = { 0, 8, 16, 32, 64, 128, 256, 512, }
375 is the size of I = { 2, 4, 6, 10, 12, 14, 18, 20, 22, 26, 28, 30, 34, 36, 38, 42, 44, 46, 50, 52, 54, 58, 60, 62, 66, 68, 70, 74, 76, 78, ... }
8 is the size of J = { 0, 8, 16, 32, 64, 128, 256, 512, }
993 is the size of K = { 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 33, ... }
All subsets of H:
{} {0 } {8 } {0 8 } {16 } {0 16 } {8 16 } {0 8 16 } {32 } {0 32 } {8 32 } {0 8 32 } {16 32 } {0 16 32 } {8 16 32 } {0 8 16 32 } {64 } {0 64 } {8 64 } {0 8 64 } {16 64 } {0 16 64 } {8 16 64 } {0 8 16 64 } {32 64 } {0 32 64 } {8 32 64 } {0 8 32 64 } {16 32 64 } {0 16 32 64 } {8 16 32 64 } {0 8 16 32 64 } {128 } {0 128 } {8 128 } {0 8 128 } {16 128 } {0 16 128 } {8 16 128 } {0 8 16 128 } {32 128 } {0 32 128 } {8 32 128 } {0 8 32 128 } {16 32 128 } {0 16 32 128 } {8 16 32 128 } {0 8 16 32 128 } {64 128 } {0 64 128 } {8 64 128 } {0 8 64 128 } {16 64 128 } {0 16 64 128 } {8 16 64 128 } {0 8 16 64 128 } {32 64 128 } {0 32 64 128 } {8 32 64 128 } {0 8 32 64 128 } {16 32 64 128 } {0 16 32 64 128 } {8 16 32 64 128 } {0 8 16 32 64 128 } {256 } {0 256 } {8 256 } {0 8 256 } {16 256 } {0 16 256 } {8 16 256 } {0 8 16 256 } {32 256 } {0 32 256 } {8 32 256 } {0 8 32 256 } {16 32 256 } {0 16 32 256 } {8 16 32 256 } {0 8 16 32 256 } {64 256 } {0 64 256 } {8 64 256 } {0 8 64 256 } {16 64 256 } {0 16 64 256 } {8 16 64 256 } {0 8 16 64 256 } {32 64 256 } {0 32 64 256 } {8 32 64 256 } {0 8 32 64 256 } {16 32 64 256 } {0 16 32 64 256 } {8 16 32 64 256 } {0 8 16 32 64 256 } {128 256 } {0 128 256 } {8 128 256 } {0 8 128 256 } {16 128 256 } {0 16 128 256 } {8 16 128 256 } {0 8 16 128 256 } {32 128 256 } {0 32 128 256 } {8 32 128 256 } {0 8 32 128 256 } {16 32 128 256 } {0 16 32 128 256 } {8 16 32 128 256 } {0 8 16 32 128 256 } {64 128 256 } {0 64 128 256 } {8 64 128 256 } {0 8 64 128 256 } {16 64 128 256 } {0 16 64 128 256 } {8 16 64 128 256 } {0 8 16 64 128 256 } {32 64 128 256 } {0 32 64 128 256 } {8 32 64 128 256 } {0 8 32 64 128 256 } {16 32 64 128 256 } {0 16 32 64 128 256 } {8 16 32 64 128 256 } {0 8 16 32 64 128 256 } {512 } {0 512 } {8 512 } {0 8 512 } {16 512 } {0 16 512 } {8 16 512 } {0 8 16 512 } {32 512 } {0 32 512 } {8 32 512 } {0 8 32 512 } {16 32 512 } {0 16 32 512 } {8 16 32 512 } {0 8 16 32 512 } {64 512 } {0 64 512 } {8 64 512 } {0 8 64 512 } {16 64 512 } {0 16 64 512 } {8 16 64 512 } {0 8 16 64 512 } {32 64 512 } {0 32 64 512 } {8 32 64 512 } {0 8 32 64 512 } {16 32 64 512 } {0 16 32 64 512 } {8 16 32 64 512 } {0 8 16 32 64 512 } {128 512 } {0 128 512 } {8 128 512 } {0 8 128 512 } {16 128 512 } {0 16 128 512 } {8 16 128 512 } {0 8 16 128 512 } {32 128 512 } {0 32 128 512 } {8 32 128 512 } {0 8 32 128 512 } {16 32 128 512 } {0 16 32 128 512 } {8 16 32 128 512 } {0 8 16 32 128 512 } {64 128 512 } {0 64 128 512 } {8 64 128 512 } {0 8 64 128 512 } {16 64 128 512 } {0 16 64 128 512 } {8 16 64 128 512 } {0 8 16 64 128 512 } {32 64 128 512 } {0 32 64 128 512 } {8 32 64 128 512 } {0 8 32 64 128 512 } {16 32 64 128 512 } {0 16 32 64 128 512 } {8 16 32 64 128 512 } {0 8 16 32 64 128 512 } {256 512 } {0 256 512 } {8 256 512 } {0 8 256 512 } {16 256 512 } {0 16 256 512 } {8 16 256 512 } {0 8 16 256 512 } {32 256 512 } {0 32 256 512 } {8 32 256 512 } {0 8 32 256 512 } {16 32 256 512 } {0 16 32 256 512 } {8 16 32 256 512 } {0 8 16 32 256 512 } {64 256 512 } {0 64 256 512 } {8 64 256 512 } {0 8 64 256 512 } {16 64 256 512 } {0 16 64 256 512 } {8 16 64 256 512 } {0 8 16 64 256 512 } {32 64 256 512 } {0 32 64 256 512 } {8 32 64 256 512 } {0 8 32 64 256 512 } {16 32 64 256 512 } {0 16 32 64 256 512 } {8 16 32 64 256 512 } {0 8 16 32 64 256 512 } {128 256 512 } {0 128 256 512 } {8 128 256 512 } {0 8 128 256 512 } {16 128 256 512 } {0 16 128 256 512 } {8 16 128 256 512 } {0 8 16 128 256 512 } {32 128 256 512 } {0 32 128 256 512 } {8 32 128 256 512 } {0 8 32 128 256 512 } {16 32 128 256 512 } {0 16 32 128 256 512 } {8 16 32 128 256 512 } {0 8 16 32 128 256 512 } {64 128 256 512 } {0 64 128 256 512 } {8 64 128 256 512 } {0 8 64 128 256 512 } {16 64 128 256 512 } {0 16 64 128 256 512 } {8 16 64 128 256 512 } {0 8 16 64 128 256 512 } {32 64 128 256 512 } {0 32 64 128 256 512 } {8 32 64 128 256 512 } {0 8 32 64 128 256 512 } {16 32 64 128 256 512 } {0 16 32 64 128 256 512 } {8 16 32 64 128 256 512 } {0 8 16 32 64 128 256 512 }
 */