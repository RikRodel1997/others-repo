import java.util.*;
import java.io.*;

class Solution {
    public static void main(String[] argh) {
        Scanner scnr = new Scanner(System.in);
        int n = scnr.nextInt();

        Map<String, Integer> mp = new HashMap<>();

        for (int i = 0; i < n; i++) {
            String name = scnr.next();
            int phone = scnr.nextInt();
            mp.put(name, phone);
        }

        while (scnr.hasNext()) {
            String query = scnr.next();

            if (mp.containsKey(query)) {
                System.out.printf("%s=%d%n", query, mp.get(query));
            } else {
                System.out.println("Not found");
            }

        }

        scnr.close();
    }
}