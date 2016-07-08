package br.com.alura.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerTasks {
    private ServerSocket server;
    private ExecutorService threadPool;
    private AtomicBoolean isRunning;
    private ArrayBlockingQueue<String> commandQueue;

    public ServerTasks(ServerSocket server, ExecutorService threadPool) {
        System.out.println("---- Starting Server ----");
        this.server = server;
        this.threadPool = threadPool;
        this.isRunning = new AtomicBoolean(true);
        this.commandQueue = new ArrayBlockingQueue<>(2);
        
        // Starting the queue consumers.
        this.startConsumers();
    }
    
    public void open() {
        while(this.isRunning.get()) {
            try {
                Socket socket = this.server.accept();
                System.out.println("\nReceiving client in the port " + socket.getPort());
                
                TaskHandler handler = new TaskHandler(socket, this.threadPool, this.commandQueue, this);
                this.threadPool.execute(handler);
            } catch(IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    public void close() throws Exception {
        System.out.println("---- Stopping server ----");
        this.isRunning.set(false);
        this.threadPool.shutdown();
        this.server.close();
    }
    
    public boolean isOpen() {
        return this.isRunning.get();
    }
    
    public static void main(String[] args) throws Exception {
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ServerTasks server = new ServerTasks(new ServerSocket(12345), Executors.newCachedThreadPool(new LocalThreadFactory(threadFactory)));
        server.open();
    }
    
    private void startConsumers() {
        int numberOfConsumers = 2;
        for (int i = 0; i < numberOfConsumers; i++) {
            ConsumerTask task = new ConsumerTask(this.commandQueue);
            this.threadPool.execute(task);
        }
    }
}