import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class GreedyTSP
{
    private int numberOfNodes;
    private Stack<Integer> stack;
    public GreedyTSP()
    {
        stack = new Stack<Integer>();
    }
    public void tsp(int adjacencyMatrix[][])
    {
        numberOfNodes = adjacencyMatrix[1].length - 1;
        int[] visited = new int[numberOfNodes + 1];
        visited[1] = 1;
        stack.push(1);
        int element, dst = 0, i;
        int min = Integer.MAX_VALUE;
        boolean minFlag = false;
        System.out.print("0" + "\t" + "1" +"\t");
        while (!stack.isEmpty())
        {
            element = stack.peek();
            i = 1;
            min = Integer.MAX_VALUE;
            while (i <= numberOfNodes)
            {
                if (adjacencyMatrix[element][i] > 1 && visited[i] == 0)
                {
                    if (min > adjacencyMatrix[element][i])
                    {
                        min = adjacencyMatrix[element][i];
                        dst = i;
                        minFlag = true;
                    }
                }
                i++;
            }
            if (minFlag)
            {
                visited[dst] = 1;
                stack.push(dst);
                System.out.print(dst + "\t");
                minFlag = false;

                continue;
            }

            stack.pop();
        }
    }

    public static void main(String... arg)
    {
        int number_of_nodes;
        Scanner scanner = null;
        try
        {


            int vert = 9;
            int edgemax = 200;
            int myArr[][] = GenerateRandomCostMatrix(vert, edgemax);
            int myArr2[][] = GenerateRandomEuclideanCostMatrix(vert,edgemax);
            int myArr3[][] = GenerateRandomCircularGraphCostMatrix(vert, edgemax);

            System.out.println("the citys are visited as follows 2");
            GreedyTSP tspNearestNeighbour2 = new GreedyTSP();
            tspNearestNeighbour2.tsp(myArr2);

            System.out.println("\n the citys are visited as follows 3");
            GreedyTSP tspNearestNeighbour3 = new GreedyTSP();
            tspNearestNeighbour3.tsp(myArr3);

            System.out.println("\n");

            for(
                    int i = 0;
                    i<myArr.length;i++)


            //Used to print out the generated array
            {
                for (int j = 0; j < myArr[i].length; j++) {

                    System.out.printf("%-5d", myArr[i][j]);
                }
                System.out.println();
            }

            System.out.println("the citys are visited as follows");
            GreedyTSP tspNearestNeighbour = new GreedyTSP();
            tspNearestNeighbour.tsp(myArr);
        } catch (InputMismatchException inputMismatch)
        {
            System.out.println("Wrong Input format");
        }
        //scanner.close();
    }

    final static int infinity = 0;
    public static int[][] GenerateRandomCostMatrix(int V, int E) {
        int[][] a = new int[V][V];
        int e = E;

        for(int i = 0; i < V; i++) {
            for(int j = i; j < V; j++) {
                if(i == j) {
                    a[i][j] = 0;
                }
                else {
                    if(Math.random() < 0.999 && e >= 0) {
                        int temp = (int)Math.ceil(Math.random()*e);
                        a[i][j] = temp;
                        a[j][i] = temp;
                        e--;
                    }
                    else {
                        a[i][j] = infinity;
                        a[j][i] = infinity;
                    }
                }
            }
        }
        return a;
    }

    public static int[][] GenerateRandomEuclideanCostMatrix(int V, int E) {
        int[][] a = new int[V][V];
        int e = E;

        for(int i = 0; i < V; i++) {
            for(int j = i; j < V; j++) {
                if(i == j) {
                    a[i][j] = 0;
                }
                else {
                    if(Math.random() < 0.999 && e >= 0) {

                        int x1 = (int)(Math.random() * ((e - 1) + 1)) + 1;
                        int x2 = (int)(Math.random() * ((e - 1) + 1)) + 1;
                        int y1 = (int)(Math.random() * ((e - 1) + 1)) + 1;
                        int y2 = (int)(Math.random() * ((e - 1) + 1)) + 1;



                        double distance = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));


                        int temp = (int)distance;
                        a[i][j] = temp;
                        a[j][i] = temp;
                        e--;
                    }
                    else {
                        a[i][j] = infinity;
                        a[j][i] = infinity;
                    }
                }
            }
        }
        return a;
    }

    public static int[][] GenerateRandomCircularGraphCostMatrix(int V, int E) {
        int[][] a = new int[V][V];
        int e = E;
        final int NUM_POINTS = e;
        final double RADIUS = 100d;

        final Point[] points = new Point[NUM_POINTS];

        for (int i = 0; i < NUM_POINTS; ++i)
        {
            final double angle = Math.toRadians(((double) i / NUM_POINTS) * 360d);

            points[i] = new Point(
                    Math.cos(angle) * RADIUS,
                    Math.sin(angle) * RADIUS
            );
        }



        Random rand = new Random();

        for (int s = 0; s < points.length; s++) {
            int randomIndexToSwap = rand.nextInt(points.length);
            Point temp = points[randomIndexToSwap];
            points[randomIndexToSwap] = points[s];
            points[s] = temp;
        }






        for(int i = 0; i < V; i++) {
            for(int j = i; j < V; j++) {
                if(i == j) {
                    a[i][j] = 0;
                }
                else {
                    if(Math.random() < 0.999 && e >= 0) {





                        Point firstSet = points[j];
                        Point secondSet = points[j+1];
                        int x1 = firstSet.x;
                        int y1 = firstSet.y;
                        int x2 = secondSet.x;
                        int y2 = secondSet.y;



                        double distance = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));



                        int temp = (int)distance;
                        a[i][j] = temp;
                        a[j][i] = temp;
                        e--;
                    }
                    else {
                        a[i][j] = infinity;
                        a[j][i] = infinity;
                    }
                }
            }
        }
        return a;
    }
}
