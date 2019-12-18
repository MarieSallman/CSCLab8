import java.util.Arrays;
import java.util.Random;

public class ThreadCpuTest{

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

                        System.out.println("X1 = " + x1 + " X2 = " + x2);
                        System.out.println("Y1 = " + y1 + " Y2 = " + y2);
                        System.out.println("Distance = " + distance);

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

        System.out.println(Arrays.toString(points));

        Random rand = new Random();

        for (int s = 0; s < points.length; s++) {
            int randomIndexToSwap = rand.nextInt(points.length);
            Point temp = points[randomIndexToSwap];
            points[randomIndexToSwap] = points[s];
            points[s] = temp;
        }




        System.out.println(Arrays.toString(points));

        for(int i = 0; i < V; i++) {
            for(int j = i; j < V; j++) {
                if(i == j) {
                    a[i][j] = 0;
                }
                else {
                    if(Math.random() < 0.999 && e >= 0) {





                        Point firstSet = points[i];
                        Point secondSet = points[i+1];
                        System.out.println(firstSet + " " + secondSet);
                        int x1 = firstSet.x;
                        int y1 = firstSet.y;
                        int x2 = secondSet.x;
                        int y2 = secondSet.y;



                        double distance = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));

                        System.out.println("X1 = " + x1 + " X2 = " + x2);
                        System.out.println("Y1 = " + y1 + " Y2 = " + y2);
                        System.out.println("Distance = " + distance);

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

    public static void main(String[] args) {
        int vert = 9;
        int edgemax = 200;
        int myArr[][] = GenerateRandomEuclideanCostMatrix(vert, edgemax);

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


        int myArr2[][] = GenerateRandomCircularGraphCostMatrix(vert, edgemax);

        for(
                int i = 0;
                i<myArr2.length;i++)


        //Used to print out the generated array
        {
            for (int j = 0; j < myArr[i].length; j++) {

                System.out.printf("%-5d", myArr2[i][j]);
            }
            System.out.println();
        }



    }
}