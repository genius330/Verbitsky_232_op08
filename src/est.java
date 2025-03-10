import java.util.Random;

public class est {
    public static void main(String[] args) {
        int[] number = new int[15];
        Random random = new Random();

        for (int i = 0; i < number.length; i++) {
            number[i] = random.nextInt(51) + 50;
        }
        int min = number[0];
        int max = number[0];

        for (int num : number) {
            if (num < min) min = num;
            if (num > max) max = num;
        }

        System.out.print("Ваше число: ");
        for (int num : number) {
            System.out.println(num + "");
        }
        System.out.println("Минимальное число: " + min);
        System.out.println("Максимальное число: " + max);
    }
}
