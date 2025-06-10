package ui;

import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void displayWelcome() {
        System.out.println("=================================");
        System.out.println("   EXPLODING KITTENS");
        System.out.println("==================================\n");
    }
}
