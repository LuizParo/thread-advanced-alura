package br.com.alura.server;

import java.io.PrintStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class JointFutureFromCommands implements Callable<Void> {
    private PrintStream out;
    private Future<String> futureWS;
    private Future<String> futureDB;

    public JointFutureFromCommands(PrintStream out, Future<String> futureWS, Future<String> futureDB) {
        this.out = out;
        this.futureWS = futureWS;
        this.futureDB = futureDB;
    }

    @Override
    public Void call() {
        String magicNumberOne;
        try {
            magicNumberOne = this.futureWS.get(20, TimeUnit.SECONDS);
            String magicNumberTwo = this.futureDB.get(20, TimeUnit.SECONDS);
            this.out.println("Joinning futures: " + magicNumberOne + ", " + magicNumberTwo);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println("Timeout: Aborting execution!");
            this.futureWS.cancel(true);
            this.futureDB.cancel(true);

            this.out.println("Timeout on execution: " + e.getMessage());
        }
        
        System.out.println("Futures joined!");
        return null;
    }
}