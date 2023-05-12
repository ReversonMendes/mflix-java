package katas;

import java.util.Arrays;

public class SquareSum {

    public static void main(String[] args) {

    }

    public static int squareSum(int[] n)
    {
        //Your Code

        return Arrays.stream(n).map(s -> s * s).sum();

    }
}
