package br.com.alura.server;

import java.io.PrintStream;

public class CommandC1 implements Runnable {
    private PrintStream out;

    public CommandC1(PrintStream out) {
        this.out = out;
    }

    @Override
    public void run() {
        System.out.println("Executing command C1");

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.out.println("Command C1 executed successfully!");
    }
}