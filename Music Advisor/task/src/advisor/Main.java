package advisor;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    public static boolean showCommands(Scanner scanner) {
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
                return true;
            }
        }
        return false;
    }

    public static boolean authorize(String endpoint) throws IOException, InterruptedException {

        Thread server = new Thread(new ServerExecutor());
        server.start();
        System.out.println("use this link to request the access code:");
        System.out.println(endpoint +
                "?client_id=e7e6267af3a549b986b2fce8371d8ae0&redirect_uri=http://localhost:8080&response_type=code");
        System.out.println("waiting for code...");
        try {
            server.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception occurred");
            System.exit(1);
        }
        System.out.println("code received\n" +
                "making http request for access_token...\n" +
                "response:");
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create("https://accounts.spotify.com/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString("client_id=e7e6267af3a549b986b2fce8371d8ae0" +
                        "&client_secret=1337a9507c054e3c8a4bdee1ea02dced" +
                        "&grant_type=authorization_code" +
                        "&code=" + ServerExecutor.query +
                        "&redirect_uri=http://localhost:8080&response_type=code"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        System.out.println("---SUCCESS---");
        return true;
    }

    public static void main(String[] args) {
        String endpoint = "https://accounts.spotify.com/authorize";
        if (args.length == 2 && args[0].equals("access")) {
            endpoint = args[1];
        }
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        boolean authorized = false;
        while (!exit) {
            while (!authorized && !exit) {
                String command = scanner.nextLine();
                if (command.equals("auth")) {
                    try {
                        authorized = authorize(endpoint);
                    } catch (IOException | InterruptedException e) {
                        System.out.println("An error occurred");
                    }
                } else if (command.equals("exit")) {
                    exit = true;
                } else {
                    System.out.println("Please, provide access for application.");
                }
            }
            if (exit) break;
            exit = showCommands(scanner);
        }
    }
}

class ServerExecutor implements Runnable {

    HttpServer server;
    static String query = null;

    public ServerExecutor() {
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            System.out.println("IOException Occurred here");
            System.exit(1);
        }
        server.createContext("/",
                exchange -> {
                    while (query == null) {
                        query = exchange.getRequestURI().getQuery();
                    }
                    String response;
                    if (query.contains("code")) {
                        response = "Got the code. Return back to your program.";
                    } else {
                        response = "Authorization code not found. Try again.";
                    }
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().close();
                }
        );

    }

    @Override
    public void run() {
        server.start();
        while (true) {
            //System.out.println(query);
            if (query != null && query.contains("code")) {
                query = query.split("=")[1];
                System.out.println(query);
                server.stop(1);
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
