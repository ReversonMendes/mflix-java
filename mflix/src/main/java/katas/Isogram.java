package katas;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Isogram {

    public static void main(String[] args) {
        System.out.println(isIsogram("tem"));
    }

    public static boolean  isIsogram(String str) {
        str = str.toLowerCase();
        List<Character> asList = str.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        return asList.stream().sequential().allMatch(new HashSet<>()::add);

        //melhor assim
        //return str.length() == str.toLowerCase().chars().distinct().count();
    }
}
