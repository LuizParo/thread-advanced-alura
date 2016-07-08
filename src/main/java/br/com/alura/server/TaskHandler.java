package br.com.alura.server;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TaskHandler implements Runnable {
    private Socket socket;
    private ExecutorService threadPool;
    private ArrayBlockingQueue<String> commandQueue;
    private ServerTasks server;

    public TaskHandler(Socket socket, ExecutorService threadPool, ArrayBlockingQueue<String> commandQueue, ServerTasks server) {
        this.socket = socket;
        this.threadPool = threadPool;
        this.commandQueue = commandQueue;
        this.server = server;
    }
    
    @Override
    public void run() {
        System.out.println("Distributing task to client " + this.socket);
        try {
            try(Scanner clientInput = new Scanner(this.socket.getInputStream())) {
                try(PrintStream responseStream = new PrintStream(this.socket.getOutputStream())) {
                    while(clientInput.hasNextLine()) {
                        String command = clientInput.nextLine();
                        System.out.println("Receiving command from client: " + command);
                        
                        switch (command) {
                            case "c1": {
                                responseStream.println("Confirming command " + command);
                                CommandC1 c1 = new CommandC1(responseStream);
                                this.threadPool.execute(c1);
                                break;
                            }
                                
                            case "c2": {
                                responseStream.println("Confirming command " + command);
                                
                                CommandC2CallWS commandWS = new CommandC2CallWS(responseStream);
                                CommandC2CallDataBase commandDB = new CommandC2CallDataBase(responseStream);
                                
                                Future<String> futureWS = this.threadPool.submit(commandWS);
                                Future<String> futureDB = this.threadPool.submit(commandDB);
                                
                                new JointFutureFromCommands(responseStream, futureWS, futureDB);
                                
                                break;
                            }
                                
                            case "c3": {
                                this.commandQueue.put(command); // block the current thread
                                responseStream.println("Command " + command + " added to queue!");
                                break;
                            }
                                
                            case "shutdown": {
                                System.out.println("Shuttingdown the server");
                                this.server.close();
                                return;
                            }

                            default: {
                                responseStream.println("Unknown command " + command);
                                break;
                            }
                        }
                    }
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}