import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class main {

    public static void main(String[] args) {

        int[] a = {1,2,3};
        int[] b = {1};

        int[] result = arrayDiff(a,b);

        List<Integer> lista = Arrays.asList(1, 2,3);

        System.out.println(result);
    }

    public static int[] arrayDiff(int[] a, int[] b) {
        // Your code here
        Arrays.stream(a).anyMatch(b);


        return a;
    }

}