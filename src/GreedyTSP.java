import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class GreedyTSP
{

    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );

    static int MAXINPUTSIZE  = 20;

    static int MININPUTSIZE  =  3;
    static long MAXVALUE = 2000;
    static long MINVALUE = -2000;
    static long numberOfTrials = 10;

    static String ResultsFolderPath = "/home/marie/Results/"; // pathname to results folder

    static FileWriter resultsFile;

    static PrintWriter resultsWriter;

    
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
        int count = 0;
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
                        count = count + element;
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
        System.out.println("\n Tour cost:" + count);
    }

    public static void main()
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


        runFullExperiment("Test3-Exp1-ThrowAway.txt");

        runFullExperiment("Test3-Exp2.txt");

        runFullExperiment("Test3-Exp3.txt");

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
}
