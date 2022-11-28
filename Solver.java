import dsa.LinkedStack;
import dsa.MinPQ;
import stdlib.In;
import stdlib.StdOut;

// A data type that implements the A* algorithm for solving the 8-puzzle and its generalizations.
public class Solver {
    private int moves;
    private LinkedStack<Board> solution;

    public Solver(Board board) {

        if (board == null) {
            throw new NullPointerException("board is null");
        }

        if (!board.isSolvable()) {
            throw new IllegalArgumentException("board is unsolvable");
        }

        this.solution = new LinkedStack<Board>();

        int countMoves = 0;


        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();

        SearchNode a = new SearchNode(board, 0, null);

        pq.insert(a);

        while (!pq.isEmpty()) {

            SearchNode node = pq.delMin();

            if (node.board.isGoal()) {

                this.moves = node.moves;

                for (SearchNode x = node; x.previous != null; x = x.previous) {
                    solution.push(x.board);
                }

                break; /* Exit the program as the puzzle is solved*/
            } else {


                countMoves += 1;

                Board previousBoard = (node.previous == null) ? null : node.previous.board;
                for (Board v : node.board.neighbors()) {

                    if (!v.equals(previousBoard)) {

                        SearchNode newNode = new SearchNode(v, countMoves, node);

                        pq.insert(newNode);
                    }

                }
            }
        }
    }

    // Returns the minimum number of moves needed to solve the initial board.
    public int moves() {
        return this.moves;
    }

    // Returns a sequence of boards in a shortest solution of the initial board.
    public Iterable<Board> solution() {
        return this.solution;
    }

    // A data type that represents a search node in the game tree. Each node includes a
    // reference to a board, the number of moves to the node from the initial node, and a
    // reference to the previous node.
    private class SearchNode implements Comparable<SearchNode> {
        private Board board; // board represented by this node.
        private int moves; // Number of moves it needed to get this node from the initial node
        private SearchNode previous; // previous search node


        // Constructs a new search node.
        public SearchNode(Board board, int moves, SearchNode previous) {
            // Initialising the instance variable - board, moves, previous.
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }

        // Returns a comparison of this node and other based on the following sum:
        // Manhattan distance of the board in the node + the # of moves to the node
        public int compareTo(SearchNode other) {
            SearchNode o = (SearchNode) other;

            int x = this.board.manhattan() + this.moves;

            int y = o.board.manhattan() + o.moves;
            return x - y;
        }
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
        Board initial = new Board(tiles);
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.printf("Solution (%d moves):\n", solver.moves());
            StdOut.println(initial);
            StdOut.println("----------");
            for (Board board : solver.solution()) {
                StdOut.println(board);
                StdOut.println("----------");
            }
        } else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}