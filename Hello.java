import java.util.List;
import java.util.Scanner;

class Hello {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String wholeLine = s.nextLine();

        String[] lines = wholeLine.split(" ");
        int countHello = 0;
        int countWorld = 0;

        for (int i = 0; i < lines.length; i ++) {
            if (lines[i].equals("Hello")) {
                countHello += 1;
            } else if (lines[i].equals("World")) {
                countWorld += 1;
            } 
        }

        System.out.println(countHello - countWorld);
    }
}