import java.util.*;

public class maze {

    private static final int right = 0;
    private static final int down = 1;
    private static final int left = 2;
    private static final int up = 3;
    private static Random randomGenerator;  // for random numbers

    public static int Size;

    public static class Point {  // a Point is a position in the maze

        public int x, y, setSize;
        public Point parent;

        // Constructor
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
            setSize = 1;
            this.parent = this;
        }

        public void copy(Point p) {
            this.x = p.x;
            this.y = p.y;
        }
    }

    public static class Edge {
        // an Edge links two neighboring Points:
        // For the grid graph, an edge can be represented by a point and a direction.
        Point point;
        int direction;    // one of right, down, left, up
        boolean used;     // for maze creation
        boolean deleted;  // for maze creation

        // Constructor
        public Edge(Point p, int d) {
            this.point = p;
            this.direction = d;
            this.used = false;
            this.deleted = false;
        }
    }

    // A board is an SizexSize array whose values are Points
    public static Point[][] board;

    // A graph is simply a set of edges: graph[i][d] is the edge
    // where i is the index for a Point and d is the direction
    public static Edge[][] graph;
    public static int N;   // number of points in the graph

    public static void displayInitBoard() {
        System.out.println("\nInitial Configuration:");

        for (int i = 0; i < Size; ++i) {
            //Tick in line with the 't' of start
            System.out.print("    -");

            for (int j = 0; j < Size; ++j){
                if (graph[i * Size + j][up].deleted){
                    System.out.print("   -");
                }
                else {
                    System.out.print("----");
                }
            }
            System.out.println();

            if (i == 0){
                System.out.print("Start");
            }
            else {
                System.out.print("    |");
            }
            for (int j = 0; j < Size; ++j) {
                if (i == Size-1 && j == Size-1) {
                    System.out.print("    End");
                }
                else {
                    if (graph[i * Size + j][right].deleted){
                        System.out.print("    ");
                    }
                    else {
                        System.out.print("   |");
                    }
                }
            }
            System.out.println();
        }
        System.out.print("    -");
        for (int j = 0; j < Size; ++j){
            System.out.print("----");
        }
        System.out.println();
    }

    private static void union(Point p1, Point p2){
        if (find(p1) != find(p2)){
            if (p1.parent.setSize > p2.parent.setSize){
                p2.parent.parent = p1.parent;
                p1.parent.setSize += p2.parent.setSize;
            }
            else {
                p1.parent.parent = p2.parent;
                p2.parent.setSize += p1.parent.setSize;
            }
        }
    }

    public static Point find(Point p){
        if(p.parent != p){
            p.parent = find(p.parent);
        }
        return p.parent;
    }

    public static void generateMaze(){
        int remainingEdges = Size * Size;

        while(remainingEdges > 1){
            int direction = randomGenerator.nextInt(4);
            int col = randomGenerator.nextInt(Size);
            int row = randomGenerator.nextInt(Size);
            int from = col * Size + row;

            int nextCol, nextRow, reverseDirection;

            if (direction == right){
                nextCol = col;
                nextRow = row + 1;
                reverseDirection = left;
            }
            else if (direction == down){
                nextCol = col + 1;
                nextRow = row;
                reverseDirection = up;
            }
            else if (direction == left){
                nextCol = col;
                nextRow = row - 1;
                reverseDirection = right;
            }
            else{
                nextCol = col - 1;
                nextRow = row;
                reverseDirection = down;
            }

            int to = nextCol * Size + nextRow;

            if (!graph[from][direction].used && !graph[from][direction].deleted &&
                    !graph[to][reverseDirection].used && !graph[to][reverseDirection].deleted){

                Point p1 = board[col][row];
                Point p2 = board[nextCol][nextRow];

                if(find(p1) != find(p2)){
                    union(p1, p2);
                    graph[from][direction].deleted = true;
                    graph[to][reverseDirection].deleted = true;
                    remainingEdges--;
                }
                else{
                    graph[from][direction].used = true;
                    graph[to][reverseDirection].used = true;
                }
            }
        }
    }

    /*
        TODO: Uncomment this
     */
    public static void main(String[] args) {

        // Read in the Size of a maze
        Scanner scan = new Scanner(System.in);
        /*try {
            System.out.println("What's the size of your maze? ");
            Size = scan.nextInt();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        scan.close();*/
        Size = 7;


        // Create one dummy edge for all boundary edges.
        Edge dummy = new Edge(new Point(0, 0), 0);
        dummy.used = true;

        // Create board and graph.
        board = new Point[Size][Size];
        N = Size*Size;  // number of points
        graph = new Edge[N][4];

        for (int i = 0; i < Size; ++i)
            for (int j = 0; j < Size; ++j) {
                Point p = new Point(i, j);
                int pindex = i*Size+j;   // Point(i, j)'s index is i*Size + j

                board[i][j] = p;

                graph[pindex][right] = (j < Size-1)? new Edge(p, right): dummy;
                graph[pindex][down] = (i < Size-1)? new Edge(p, down) : dummy;
                graph[pindex][left] = (j > 0)? graph[pindex-1][right] : dummy;
                graph[pindex][up] = (i > 0)? graph[pindex-Size][down] : dummy;

            }

        displayInitBoard();

        // Hint: To randomly pick an edge in the maze, you may
        // randomly pick a point first, then randomly pick
        // a direction to get the edge associated with the point.
        randomGenerator = new Random();
        int i = randomGenerator.nextInt(N);
        System.out.println("\nA random number between 0 and " + (N-1) + ": " + i);

        generateMaze();
        displayInitBoard();
    }
}
