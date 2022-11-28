import dsa.Inversions;
import dsa.LinkedQueue;
import stdlib.In;
import stdlib.StdOut;

// A data type to represent a board in the 8-puzzle game or its generalizations.
public class Board {
    private int[][] tiles; // Tiles in the board
    private int n; // size of the board
    private int hamming; // Hamming distance
    private int manhattan; // Manhattan distance
    private int blankPos; // Position of the blankPos in the row major order

    public Board(int[][] tiles) {
        this.tiles = tiles;
        this.n = tiles.length;
        int count = 1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    this.blankPos = (n * i + j + 1);
                } else if ((tiles[i][j] != count && tiles[i][j] != 0)) {
                    this.hamming++;
                    int x = tiles[i][j];
                    int p = (x - 1) / n;
                    int q = (x - 1) % n;
                    this.manhattan += (Math.abs(i - p) + Math.abs(j - q));
                }
                count++;
            }
        }
    }

    // Returns the size of this board.
    public int size() {
        return this.n;
    }

    // Returns the tile at row i and column j of this board.
    public int tileAt(int i, int j) {
        return this.tiles[i][j];
    }

    // Returns Hamming distance between this board and the goal board.
    public int hamming() {
        return this.hamming;
    }

    // Returns the Manhattan distance between this board and the goal board.
    public int manhattan() {
        return this.manhattan;
    }

    // Returns true if this board is the goal board, and false otherwise.
    public boolean isGoal() {
        return this.hamming == 0;
    }

    // Returns true if this board is solvable, and false otherwise.
    public boolean isSolvable() {
        int[] aList = new int[n * n - 1];
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tileAt(i, j) == 0) {
                    continue;
                }
                aList[count] = this.tileAt(i, j);
                count++;
            }
        }
        long inversions = Inversions.count(aList);
        if (n % 2 != 0 && inversions % 2 == 0) {
            return true;
        } else if (n % 2 == 0 && (inversions + ((blankPos - 1) / n)) % 2 != 0) {
            return true;
        }
        return false;

    }

    public Iterable<Board> neighbors() {
        // Creating the linked list q.
        LinkedQueue<Board> q = new LinkedQueue<Board>();

        int p = (blankPos - 1) / n; 
        int r = (blankPos - 1) % n;
        // down
        if (p < (n - 1)) {
            int[][] down = this.cloneTiles();
            int temp = down[p][r];
            down[p][r] = down[p + 1][r];
            down[p + 1][r] = temp;
            Board objectDown = new Board(down);
            q.enqueue(objectDown);
        }

        if (p > 0) {
            int[][] up = this.cloneTiles();
            int temp = up[p][r];
            up[p][r] = up[p - 1][r];
            up[p - 1][r] = temp;
            Board objectUp = new Board(up);
            q.enqueue(objectUp);
        }

        if (r < (n - 1)) {
            int[][] right = this.cloneTiles();
            int temp = right[p][r];
            right[p][r] = right[p][r + 1];
            right[p][r + 1] = temp;
            Board objectRight = new Board(right);
            q.enqueue(objectRight);

        }
        // left
        if (r > 0) {
            int[][] left = this.cloneTiles();
            int temp = left[p][r];
            left[p][r] = left[p][r - 1];
            left[p][r - 1] = temp;
            Board objectLeft = new Board(left);
            q.enqueue(objectLeft);
        }
        return q;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Board o = (Board) other;
        if (this.n != o.n) {
            return false;
        }
        if (this.n == o.n) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (this.tiles[i][j] != o.tiles[i][j]) {
                        return false;
                    }

                }
            }
        }
        return true;

    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2s", tiles[i][j] == 0 ? " " : tiles[i][j]));
                if (j < n - 1) {
                    s.append(" ");
                }
            }
            if (i < n - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    // Returns a defensive copy of tiles[][].
    private int[][] cloneTiles() {
        int[][] clone = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                clone[i][j] = tiles[i][j];
            }
        }
        return clone;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.printf("The board (%d-puzzle):\n%s\n", n, board);
        String f = "Hamming = %d, Manhattan = %d, Goal? %s, Solvable? %s\n";
        StdOut.printf(f, board.hamming(), board.manhattan(), board.isGoal(), board.isSolvable());
        StdOut.println("Neighboring boards:");
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
            StdOut.println("----------");
        }
    }
}
