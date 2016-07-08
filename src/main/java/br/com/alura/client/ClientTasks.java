package br.com.alura.client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientTasks {
    public static void main(String[] args) throws Exception {
        try(Socket client = new Socket("localhost", 12345)) {
            System.out.println("Connection established");
            
            Thread threadCommand = createCommandThread(client);
            Thread threadServerResponse = createResponseThread(client);
            
            threadCommand.start();
            threadServerResponse.start();
            
            threadCommand.join();
            
            System.out.println("Closing connection with the server");
        }
    }

    private static Thread createResponseThread(Socket client) {
        return new Thread(() -> {
            try {
                try(Scanner response = new Scanner(client.getInputStream())) {
                    while(response.hasNextLine()) {
                        System.out.println(response.nextLine());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }

    private static Thread createCommandThread(Socket client) {
        return new Thread(() -> {
            try {
                try(PrintStream out = new PrintStream(client.getOutputStream())) {
                    try(Scanner scanner = new Scanner(System.in)) {
                        while(scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            if(line.trim().isEmpty()) {
                                break;
                            }
                            out.println(line);
                        }
                    }
                }
            } catch(IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }
}