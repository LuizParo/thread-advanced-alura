package br.com.alura.server;

import java.util.concurrent.ThreadFactory;

public class LocalThreadFactory implements ThreadFactory {
    private ThreadFactory threadFactory;

    public LocalThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    @Override
    public Thread newThread(Runnable task) {
        Thread thread = this.threadFactory.newThread(task);
        thread.setUncaughtExceptionHandler(new ThreadExceptionHandler());
        thread.setDaemon(true);
        
        return thread;
    }
}