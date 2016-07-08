package br.com.alura.server;

import java.io.PrintStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class CommandC2CallDataBase implements Callable<String> {
    private PrintStream out;

    public CommandC2CallDataBase(PrintStream out) {
        this.out = out;
    }

    @Override
    public String call() throws Exception {
        System.out.println("Executing command C2 - Calling DataBase");
        Thread.sleep(15000);
        
        int number = ThreadLocalRandom.current().nextInt(0, 100) + 1;
        this.out.println("Command C2 executed successfully! DataBase called.");
        
        return Integer.toString(number);
    }
}