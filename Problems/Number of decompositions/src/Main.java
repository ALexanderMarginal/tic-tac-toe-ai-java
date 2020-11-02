import java.util.ArrayList;
import java.util.Scanner;

class Main {
    private static ArrayList<String> list = new ArrayList<>();

    public static void partition(int n) {
        StringBuilder stringBuilder = new StringBuilder();
        partition(n, n, stringBuilder);
    }

    public static void partition(int n, int max, StringBuilder stringBuilder) {
        if (n == 0) {
            list.add(stringBuilder.toString());
            return;
        }

        for (int i = Math.min(max, n); i >= 1; i--) {
            partition(n - i, i, new StringBuilder(stringBuilder.toString()).append(stringBuilder.length() == 0 ? "" : " ").append(i));
        }
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        partition(n);
        for (int i = list.size() - 1; i >= 0; i--) {
            System.out.println(list.get(i));
        }
    }
}