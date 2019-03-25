package view;

import java.util.Scanner;

public class Console implements View {
    @Override
    public void write(String massage) {
        System.out.println(massage);
    }

    @Override
    public String read() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
