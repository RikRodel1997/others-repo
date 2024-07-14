import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        int T = scnr.nextInt();
        scnr.nextLine();

        for (int i = 0; i < T; i++) {
            String s = scnr.nextLine();
            String evenChars = "";
            String oddChars = "";

            for (int j = 0; j < s.length(); j++) {
                char c = s.charAt(j);
                if (j % 2 == 0) {
                    evenChars = evenChars + String.valueOf(c);
                } else {
                    oddChars = oddChars + String.valueOf(c);
                }
            }

            System.out.printf("%s %s\n", evenChars, oddChars);
        }
    }

}
