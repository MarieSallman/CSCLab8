import java.util.*;

public class Point {
    final int x;
    final int y;

    Point(final double x, final double y)
    {
        this.x = (int) x; // we're dealing with pixels, so just truncate it
        this.y = (int) y;
    }

    @Override
    public String toString()
    {
        return "{" + x + ", " + y + "}";
    }

    public static void main(String[] args) {
        final int NUM_POINTS = 10;
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

        for (int i = 0; i < points.length; i++) {
            int randomIndexToSwap = rand.nextInt(points.length);
            Point temp = points[randomIndexToSwap];
            points[randomIndexToSwap] = points[i];
            points[i] = temp;
        }
        System.out.println(Arrays.toString(points));

        Point try1 = points[1];
        System.out.println(try1);
        int asdf = try1.x;
        int fdsa = try1.y;
        System.out.println(asdf + " " + fdsa);




    }
}
