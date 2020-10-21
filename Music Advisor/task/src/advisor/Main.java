package advisor;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        boolean authorized = false;
        while (!exit) {
            while (!authorized && !exit) {
                String command = scanner.nextLine();
                if (command.equals("auth")) {
                    System.out.println("https://accounts.spotify.com/authorize?" +
                            "client_id=e7e6267af3a549b986b2fce8371d8ae0" +
                            "&redirect_uri=http://localhost:8080&response_type=code");
                    System.out.println("---SUCCESS---");
                    authorized = true;
                } else if (command.equals("exit")) {
                    exit = true;
                } else {
                    System.out.println("Please, provide access for application.");
                }
            }
            if (exit)
                break;
            String command = scanner.nextLine().split(" ")[0];
            switch (command) {
                case "new": {
                    System.out.println("---NEW RELEASES---\n" +
                            "Mountains [Sia, Diplo, Labrinth]\n" +
                            "Runaway [Lil Peep]\n" +
                            "The Greatest Show [Panic! At The Disco]\n" +
                            "All Out Life [Slipknot]");
                    break;
                }
                case "featured": {
                    System.out.println("---FEATURED---\n" +
                            "Mellow Morning\n" +
                            "Wake Up and Smell the Coffee\n" +
                            "Monday Motivation\n" +
                            "Songs to Sing in the Shower");
                    break;
                }
                case "categories": {
                    System.out.println("---CATEGORIES---\n" +
                            "Top Lists\n" +
                            "Pop\n" +
                            "Mood\n" +
                            "Latin");
                    break;
                }
                case "playlists": {
                    System.out.println("---MOOD PLAYLISTS---\n" +
                            "Walk Like A Badass  \n" +
                            "Rage Beats  \n" +
                            "Arab Mood Booster  \n" +
                            "Sunday Stroll");
                    break;
                }
                case "exit": {
                    System.out.println("---GOODBYE!---");
                    exit = true;
                    break;
                }
            }
        }
    }
}
