/*
 * Created on 08-Jan-2006
 *
 * This source code is released under the GNU Lesser General Public License Version 3, 29 June 2007
 * see http://www.gnu.org/licenses/lgpl.html or the plain text version of the LGPL included with this project
 *
 * It comes with no warranty whatsoever
 *
 */
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;

/**
 * Run the traveling salesman solver
 *
 * The genetic algorithm will run untill it is aborted, and prints the 
 * current best solution from time to time
 *
 * Edit the source to switch to brute force algorithm, change the random seed or the number of cities 
 * Some parameters are hardcoded in the Environment class
 * This should be improved in future versions. The current state of the code is merely a proof of concept,
 * little attention has been paid to proper engineering. 
 *
 * @author Bjoern Guenzel - http://blog.blinker.net
 */
public class TravelingSalesmanSolver {

    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );

    static int MAXINPUTSIZE  = 10;

    static int MININPUTSIZE  =  3;
    static long MAXVALUE = 2000;
    static long MINVALUE = -2000;
    static long numberOfTrials = 10;

    static String ResultsFolderPath = "/home/marie/Results/"; // pathname to results folder

    static FileWriter resultsFile;

    static PrintWriter resultsWriter;

    public final static int RANDOM_SEED = 1234;

    public final static boolean RUN_BRUTE_FORCE = true;
    //public final static boolean RUN_GENETIC_ALGORITHM = true;

    public static Random random = new Random(RANDOM_SEED);

    /**
     * @param args
     */
    public static void main(String[] args) {
        //TODO interpret arguments, for example random seed and number of cities, and brute force or genetic algorithm could be
        TravelingSalesman salesman = new TravelingSalesman(3, random);

        salesman.printCosts();

        //uncomment for brute force:

        //System.out.println("*** running brute force algorithm ***");
        //TravelingSalesmanBruteForce bruteForce = new TravelingSalesmanBruteForce(salesman);
        //bruteForce.run();

        int myArr2[][] = GenerateRandomEuclideanCostMatrix(10, 200);


        TravelingSalesman salesman2 = new TravelingSalesman(myArr2);
        salesman2.printCosts();

        TravelingSalesmanBruteForce bruteForce2 = new TravelingSalesmanBruteForce(salesman2);
        bruteForce2.run();

        //runFullExperiment("Test2-Exp1-ThrowAway.txt");

        //runFullExperiment("Test2-Exp2.txt");

        //runFullExperiment("Test2-Exp3.txt");



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

                TravelingSalesman salesman = new TravelingSalesman(inputSize, random);

                salesman.printCosts();

                TravelingSalesmanBruteForce bruteForce = new TravelingSalesmanBruteForce(salesman);
                bruteForce.run();


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

}