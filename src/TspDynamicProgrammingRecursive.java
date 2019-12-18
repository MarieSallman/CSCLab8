import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class TspDynamicProgrammingRecursive {


    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );

    static int MAXINPUTSIZE  = 20;

    static int MININPUTSIZE  =  3;
    static long MAXVALUE = 2000;
    static long MINVALUE = -2000;
    static long numberOfTrials = 10;

    static String ResultsFolderPath = "/home/marie/Results/"; // pathname to results folder

    static FileWriter resultsFile;

    static PrintWriter resultsWriter;

    private final int N;
    private final int START_NODE;
    private final int FINISHED_STATE;

    private double[][] distance;
    private double minTourCost = Double.POSITIVE_INFINITY;

    private List<Integer> tour = new ArrayList<>();
    private boolean ranSolver = false;

    public TspDynamicProgrammingRecursive(double[][] distance) {
        this(0, distance);
    }

    public TspDynamicProgrammingRecursive(int startNode, double[][] distance) {

        this.distance = distance;
        N = distance.length;
        START_NODE = startNode;

        // Validate inputs.
        if (N <= 2) throw new IllegalStateException("TSP on 0, 1 or 2 nodes doesn't make sense.");
        if (N != distance[0].length)
            throw new IllegalArgumentException("Matrix must be square (N x N)");
        if (START_NODE < 0 || START_NODE >= N)
            throw new IllegalArgumentException("Starting node must be: 0 <= startNode < N");
        if (N > 32)
            throw new IllegalArgumentException(
                    "Matrix too large! A matrix that size for the DP TSP problem with a time complexity of"
                            + "O(n^2*2^n) requires way too much computation for any modern home computer to handle");

        // The finished state is when the finished state mask has all bits are set to
        // one (meaning all the nodes have been visited).
        FINISHED_STATE = (1 << N) - 1;
    }

    // Returns the optimal tour for the traveling salesman problem.
    public List<Integer> getTour() {
        if (!ranSolver) solve();
        return tour;
    }

    // Returns the minimal tour cost.
    public double getTourCost() {
        if (!ranSolver) solve();
        return minTourCost;
    }

    public void solve() {

        // Run the solver
        int state = 1 << START_NODE;
        Double[][] memo = new Double[N][1 << N];
        Integer[][] prev = new Integer[N][1 << N];
        minTourCost = tsp(START_NODE, state, memo, prev);
        System.out.println("THis is the" + minTourCost);

        // Regenerate path
        int index = START_NODE;
        while (true) {
            tour.add(index);
            Integer nextIndex = prev[index][state];
            if (nextIndex == null) break;
            int nextState = state | (1 << nextIndex);

            state = nextState;
            index = nextIndex;
        }
        tour.add(START_NODE);
        ranSolver = true;
    }

    private double tsp(int i, int state, Double[][] memo, Integer[][] prev) {
        double count = 0;
        // Done this tour. Return cost of going back to start node.
        if (state == FINISHED_STATE) return distance[i][START_NODE];

        // Return cached answer if already computed.
        if (memo[i][state] != null) return memo[i][state];

        double minCost = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int next = 0; next < N; next++) {

            // Skip if the next node has already been visited.
            if ((state & (1 << next)) != 0) continue;

            int nextState = state | (1 << next);
            double newCost = distance[i][next] + tsp(next, nextState, memo, prev);

            if (newCost < minCost) {
                minCost = newCost;
                index = next;
            }
        }
        //System.out.println(count);
        prev[i][state] = index;
        return memo[i][state] = minCost;
    }



    // Example usage:
    public static void main(String[] args) {

        // Create adjacency matrix
        int n = 6;
        double[][] distanceMatrix = new double[n][n];
        for (double[] row : distanceMatrix) java.util.Arrays.fill(row, 10000);
        distanceMatrix[1][4] = distanceMatrix[4][1] = 2;
        distanceMatrix[4][2] = distanceMatrix[2][4] = 4;
        distanceMatrix[2][3] = distanceMatrix[3][2] = 6;
        distanceMatrix[3][0] = distanceMatrix[0][3] = 8;
        distanceMatrix[0][5] = distanceMatrix[5][0] = 10;
        distanceMatrix[5][1] = distanceMatrix[1][5] = 12;

        int vert = 9;
        int edgemax = 200;
        int myArr[][] = GenerateRandomCostMatrix(vert, edgemax);

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

        double[][] doubleArray = new double[myArr.length][myArr.length];
        for(int i=0; i<myArr.length; i++) {
            for (int j = 0; j < i; j++) {
                doubleArray[i][j] = myArr[i][j];
            }
        }




        // Run the solver
        //TspDynamicProgrammingRecursive solver = new TspDynamicProgrammingRecursive(distanceMatrix);
        TspDynamicProgrammingRecursive solver = new TspDynamicProgrammingRecursive(doubleArray);

        // Prints: [0, 3, 2, 4, 1, 5, 0]
        System.out.println("Tour: " + solver.getTour());

        // Print: 42.0
        System.out.println("Tour cost: " + solver.getTourCost());

        programs(vert, edgemax);

        //runFullExperiment("Test-Exp1-ThrowAway.txt");

        //runFullExperiment("Test-Exp2.txt");

        //runFullExperiment("Test-Exp3.txt");
    }

    static void runFullExperiment(String resultsFileName){

        try {

            resultsFile = new FileWriter(ResultsFolderPath + resultsFileName);

            resultsWriter = new PrintWriter(resultsFile);

        } catch(Exception e) {

            System.out.println("*****!!!!!  Had a problem opening the results file "+ResultsFolderPath+resultsFileName);

            return; // not very foolproof... but we do expect to be able to create/open the file...

        }



        ThreadCpuStopWatch BatchStopwatch = new ThreadCpuStopWatch(); // for timing an entire set of trials

        ThreadCpuStopWatch TrialStopwatch = new ThreadCpuStopWatch(); // for timing an individual trial



        resultsWriter.println("#InputSize    AverageTime"); // # marks a comment in gnuplot data

        resultsWriter.flush();

        for(int inputSize=MININPUTSIZE;inputSize<=MAXINPUTSIZE; inputSize++) {

            // progress message...

            System.out.println("Running test for input size "+inputSize+" ... ");



            /* repeat for desired number of trials (for a specific size of input)... */

            long batchElapsedTime = 0;

            // generate a list of randomly spaced integers in ascending sorted order to use as test input

            // In this case we're generating one list to use for the entire set of trials (of a given input size)

            // but we will randomly generate the search key for each trial






            // instead of timing each individual trial, we will time the entire set of trials (for a given input size)

            // and divide by the number of trials -- this reduces the impact of the amount of time it takes to call the

            // stopwatch methods themselves

            //BatchStopwatch.start(); // comment this line if timing trials individually



            // run the tirals

            for (long trial = 0; trial < 7; trial++) {

                //long[] testingList = createRandomIntegerList(inputSize);

                TrialStopwatch.start(); // *** uncomment this line if timing trials individually

                int vert = inputSize;
                int edgemax = 200;
                int myArr[][] = GenerateRandomCostMatrix(vert, edgemax);


                double[][] doubleArray = new double[myArr.length][myArr.length];
                for(int i=0; i<myArr.length; i++) {
                    for (int j = 0; j < i; j++) {
                        doubleArray[i][j] = myArr[i][j];
                    }
                }


                // Run the solver
                //TspDynamicProgrammingRecursive solver = new TspDynamicProgrammingRecursive(distanceMatrix);
                TspDynamicProgrammingRecursive solver = new TspDynamicProgrammingRecursive(doubleArray);

                // Prints: [0, 3, 2, 4, 1, 5, 0]
                //System.out.println("Tour: " + solver.getTour());
                solver.getTour();

                batchElapsedTime = batchElapsedTime + TrialStopwatch.elapsedTime(); // *** uncomment this line if timing trials individually

            }

            //batchElapsedTime = BatchStopwatch.elapsedTime(); // *** comment this line if timing trials individually

            double averageTimePerTrialInBatch = (double) batchElapsedTime / (double)numberOfTrials; // calculate the average time per trial in this batch



            /* print data for this size of input */

            resultsWriter.printf("%12d  %15.2f \n",inputSize, averageTimePerTrialInBatch); // might as well make the columns look nice

            resultsWriter.flush();

            System.out.println(" ....done.");

        }

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

    public static void programs(int vert, int edgemax){
        int myArr2[][] = GenerateRandomEuclideanCostMatrix(vert, edgemax);

        for(
                int i = 0;
                i<myArr2.length;i++)


        //Used to print out the generated array
        {
            for (int j = 0; j < myArr2[i].length; j++) {

                System.out.printf("%-5d", myArr2[i][j]);
            }
            System.out.println();
        }

        double[][] doubleArray = new double[myArr2.length][myArr2.length];
        for(int i=0; i<myArr2.length; i++) {
            for (int j = 0; j < i; j++) {
                doubleArray[i][j] = myArr2[i][j];
            }
        }

        TspDynamicProgrammingRecursive solver = new TspDynamicProgrammingRecursive(doubleArray);

        // Prints: [0, 3, 2, 4, 1, 5, 0]
        System.out.println("Tour: " + solver.getTour());

        // Print: 42.0
        System.out.println("Tour cost: " + solver.getTourCost());

        int myArr3[][] = GenerateRandomCircularGraphCostMatrix(vert, edgemax);

        for(
                int i = 0;
                i<myArr3.length;i++)


        //Used to print out the generated array
        {
            for (int j = 0; j < myArr3[i].length; j++) {

                System.out.printf("%-5d", myArr3[i][j]);
            }
            System.out.println();
        }

        double[][] doubleArray2 = new double[myArr3.length][myArr3.length];
        for(int i=0; i<myArr3.length; i++) {
            for (int j = 0; j < i; j++) {
                doubleArray2[i][j] = myArr3[i][j];
            }
        }

        TspDynamicProgrammingRecursive solver2 = new TspDynamicProgrammingRecursive(doubleArray2);

        // Prints: [0, 3, 2, 4, 1, 5, 0]
        System.out.println("Tour: " + solver2.getTour());

        // Print: 42.0
        System.out.println("Tour cost: " + solver2.getTourCost());
    }
}
