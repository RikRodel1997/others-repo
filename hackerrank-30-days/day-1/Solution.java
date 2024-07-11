import java.io.*;
import java.util.*;

public class Solution {

    
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);

        int i = 4;
        double d = 4.0;
        String s = "HackerRank ";

        Integer iInput = scnr.nextInt();
        Double dInput = scnr.nextDouble();
        scnr.nextLine(); // To consume the newline from nextDouble()

        String sInput = scnr.nextLine();

        System.out.printf("%d\n", iInput + i);
        System.out.printf("%.1f\n", dInput + d);
        System.out.printf("%s\n", s + sInput);
    }

}
