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

    // Constructs a board from an n x n array; tiles[i][j] is the tile at row i and column j, with 0
    // denoting the blank tile.
    public Board(int[][] tiles) {
        this.tiles = tiles;
        this.n = tiles.length;

        // Computing the hamming distance, Manhattan distance, and blankPos and storing their
        // values into the instance variable.
        // set the count variable to 1 (Index for the 1-D array)
        int count = 1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // The blank tile is denoted by the blank tile. Thus, after finding the location of
                // the blank tile, computing its row-major order and storing the value in the
                // instance variable.
                if (tiles[i][j] == 0) {
                    this.blankPos = (n * i + j + 1);
                } else if ((tiles[i][j] != count && tiles[i][j] != 0)) {
                    // As long as the tile at (i,j) is not equal to the count(basically the index of
                    // row major order) and the value of tile is not equal to 0 then increment
                    // hamming distance by 1.
                    this.hamming++;
                    // Value of the tile at (i,j) which is also the row major index k
                    int x = tiles[i][j];
                    // value of i of the goal position of the tile
                    int p = (x - 1) / n;
                    // value of j of the goal position of the tile
                    int q = (x - 1) % n;
                    // calculate the manhattan distance which is the sum of the
                    // difference between the x coordinate and y coordinate.
                    this.manhattan += (Math.abs(i - p) + Math.abs(j - q));
                }
                // increment count by 1.
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
        // Creating an array aList which stores all elements of tiles excluding
        // the blank tile.
        int[] aList = new int[n * n - 1];
        // set count to 0.(variable to keep track of the index of the 1-d array)
        int count = 0;
        // for all the tiles in tiles...
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // if the tile is blank, then continue.
                if (tileAt(i, j) == 0) {
                    continue;
                }
                // else copy the elements of the tiles at index count in the aList.
                aList[count] = this.tileAt(i, j);
                // increment count by 1.
                count++;
            }
        }
        // counting the inversions by using the count function from the Inversions library in the
        // 1-D aList list.
        long inversions = Inversions.count(aList);
        // if the n is odd and no of inversion is even, then Board is solvable.
        if (n % 2 != 0 && inversions % 2 == 0) {
            return true;
        } else if (n % 2 == 0 && (inversions + ((blankPos - 1) / n)) % 2 != 0) {
            // else if the size of the board is even and the no of inversions +
            // (row of the blank tile) is odd then Board is solvable.
            return true;
        }
        // otherwise, the board is not solvable. Thus return false.
        return false;

    }

    // Returns an iterable object containing the neighboring boards of this board.
    public Iterable<Board> neighbors() {
        // Creating the linked list q.
        LinkedQueue<Board> q = new LinkedQueue<Board>();

        int p = (blankPos - 1) / n;  // row of the blank tile
        int r = (blankPos - 1) % n; // column of teh blank tile

        // There could be four neighbour - left, right , up, and, down.

        // down
        if (p < (n - 1)) {

            // Create a clone of this.tiles and call it down.
            int[][] down = this.cloneTiles();
            // exchange the blank tile and down[p+1][r].
            int temp = down[p][r];
            down[p][r] = down[p + 1][r];
            down[p + 1][r] = temp;
            // Create a new Board object objectDown after the swap with down as the 2D array.
            Board objectDown = new Board(down);
            // Enqueue it into the linked list q created above.
            q.enqueue(objectDown);
        }

        // up
        if (p > 0) {
            // Creating a clone of tiles and call it up.
            int[][] up = this.cloneTiles();
            // exchange the blank tile with the tile-up[p-1][r].
            int temp = up[p][r];
            up[p][r] = up[p - 1][r];
            up[p - 1][r] = temp;
            // Creating a board object after the swap with up as argument.
            Board objectUp = new Board(up);
            // Enqueuing the Board object objectUp into the liked list q.
            q.enqueue(objectUp);
        }

        // right
        if (r < (n - 1)) {
            // Create a clone of this.tiles and call it right.
            int[][] right = this.cloneTiles();
            // exchange the blank tile with the tile right[p][r].
            int temp = right[p][r];
            right[p][r] = right[p][r + 1];
            right[p][r + 1] = temp;
            // Create a new board object objectRight with right as the argument.
            Board objectRight = new Board(right);
            // Enqueue it into tht
            q.enqueue(objectRight);

        }
        // left
        if (r > 0) {
            // Create a clone of this.tile and call it left.
            int[][] left = this.cloneTiles();
            // Exchange the blank tile and left[p][r-1]
            int temp = left[p][r];
            left[p][r] = left[p][r - 1];
            left[p][r - 1] = temp;
            // Create a new Board object objectLeft with left as an argument.
            Board objectLeft = new Board(left);
            // Enqueue the newly created object into the linked list q.
            q.enqueue(objectLeft);

        }
        // returning the liked list of the iterable Board objects.
        return q;
    }

    // Returns true if this board is the same as other, and false otherwise.
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
        // Checking if the size of the other board object is equal to that of the
        // tiles.

        Board o = (Board) other;

        // if the size of this and o is not same, then return false.
        if (this.n != o.n) {
            return false;
        }
        // if the size of this and o is same, then check the elements of this and o. If the any
        // element is different, then return false.
        if (this.n == o.n) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (this.tiles[i][j] != o.tiles[i][j]) {
                        return false;
                    }

                }
            }
        }
        // otherwise, return true.
        return true;

    }

    // Returns a string representation of this board.
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
