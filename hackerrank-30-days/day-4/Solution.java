import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        int T = scnr.nextInt();
        for (int i = 0; i < T; i++) {
            int age = scnr.nextInt();
            Person person = new Person(age);
            person.amIOld();
            for (int j = 0; j < 3; j++) {
                person.yearPasses();
            }
            person.amIOld();
            System.out.println();
        }
        scnr.close();

    }

}

class Person {
    int age;

    Person(int initialAge) {
        if (initialAge >= 0) {
            this.age = initialAge;
        } else {
            this.age = 0;
            System.out.println("Age is not valid, setting age to 0.");
        }
    }

    public void yearPasses() {
        age++;
    }

    public void amIOld() {
        if (age < 13) {
            System.out.println("You are young.");
        } else if (age < 18 && age >= 13) {
            System.out.println("You are a teenager.");
        } else {
            System.out.println("You are old.");
        }
    }
}
