package katas;


public class Kata
{
    public static void main(String[] args) {
        System.out.println(reverseWords("I like eating"));
    }

    public static String numberToString(int num) {
        // Return a string of the number here!
        return String.valueOf(num);
    }

    public static String reverseWords(final String original)
    {
        // Have at it
        String[] array = original.split(" ");

        if(array.length == 0)
            return original;

        int i = 0;
        for(String s : array){
            array[i] = new StringBuilder(s).reverse().toString();
            i++;
        }

//        String[] arrayReverse = Arrays.copyOf(array, array.length);
//
//        int i;
//        int w = 0;
//        for (i = array.length -1; i >= 0 ; i--) {
//
//            arrayReverse[w] = array[i];
//            w++;
//        }
//        return String.join(" ", arrayReverse);

        //Melhor solução
        //write your code here...
//        List Words = Arrays.asList(str.split(" "));
//        Collections.reverse(Words);
//        return String.join(" ", Words);


        return String.join(" ", array);
    }
}
