package br.com.alura.server;

public class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        System.err.println("Exception in thread " + thread.getName() + ": " + exception.getMessage());
    }
}