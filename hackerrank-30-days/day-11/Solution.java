import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        List<List<Integer>> arr = new ArrayList<>();

        IntStream.range(0, 6).forEach(i -> {
            try {
                arr.add(
                        Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                .map(Integer::parseInt)
                                .collect(toList()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        int maxSum = 0;
        int rowIdx = 0;
        for (List<Integer> row : arr) {
            rowIdx++;
            if (rowIdx % 3 == 0) {
                int hgSum = 0;
                List<Integer> hgTop = arr.get(rowIdx - 3);
                List<Integer> hgMid = arr.get(rowIdx - 2);
                List<Integer> hgBot = arr.get(rowIdx - 1);

                for (int i = 0; i < hgTop.size(); i++) {
                    hgSum += hgTop.get(i);
                }

                for (int i = 0; i < hgMid.size(); i++) {
                    hgSum += hgMid.get(i);
                }

                for (int i = 0; i < hgBot.size(); i++) {
                    hgSum += hgBot.get(i);
                }

                if (hgSum > maxSum) {
                    maxSum = hgSum;
                }
            }
        }

        System.out.printf("%d%n", maxSum);

        bufferedReader.close();
    }
}
