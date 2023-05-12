package katas;

public class PangramChecker {
    public static void main(String[] args) {
        System.out.println(check("bcnoc axklsstjwzymued ihuf vrajpvqgl"));
    }

    public static boolean check(String sentence){
        //code
        boolean check = false;

        sentence = sentence.replaceAll("[^a-zA-Z]", "").toLowerCase();

        String alfabeto = "abcdefghijklmnopqrstuvwxyz";

        for(char letter : alfabeto.toCharArray()){

            check = sentence.indexOf(letter) > -1;

            if(!check){
                break;
            }
        }


        return check;

        //Melhor forma
        //return sentence.toLowerCase().chars().filter(Character::isLetter).distinct().count() == 26;
    }
}
