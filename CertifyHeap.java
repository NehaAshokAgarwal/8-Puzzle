import stdlib.StdIn;
import stdlib.StdOut;

public class CertifyHeap {
    // Returns true if a[] represents a max-heap, and false otherwise.
    public static boolean isMaxHeap(Comparable[] a) {
        int n = a.length;
        for (int i = 1; i <= n / 2; i++) {
            if ((2 * i) < n && less(a[i], a[(2 * i)]) || (2 * i + 1) <
                    n && less(a[i], a[(2 * i + 1)])) {
                return false;
            }
        }
        return true;
    }

    // Returns true of v is less than w, and false otherwise.
    private static boolean less(Comparable v, Comparable w) {
        return (v.compareTo(w) < 0);
    }

    // Unit tests the library. [DO NOT EDIT]
    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        StdOut.println(isMaxHeap(a));
    }
}
