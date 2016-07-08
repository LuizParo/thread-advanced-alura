package br.com.alura.server;

import java.util.concurrent.BlockingQueue;

public class ConsumerTask implements Runnable {
    private BlockingQueue<String> commandQueue;

    public ConsumerTask(BlockingQueue<String> commandQueue) {
        this.commandQueue = commandQueue;
    }

    @Override
    public void run() {
        try {
            String command = null;
            while((command = this.commandQueue.take()) != null) {
                System.out.println("Thread " + Thread.currentThread().getName() + " consuming command " + command);
                Thread.sleep(20000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}