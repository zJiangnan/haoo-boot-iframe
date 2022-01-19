package com.haoo.iframe.template.thread;

import java.util.concurrent.Callable;

public class CallableThread implements Callable<String> {

    private String str;

    public CallableThread(String str) {
        this.str = str;
    }

    @Override
    public String call() throws Exception {
        String threadName = Thread.currentThread().getName();
        Thread thread = new Thread(new RunnableThread(threadName));
        thread.start();
        // 获取子线程的返回值：Thread的join方法来阻塞主线程，直到子线程返回
        thread.join();
        return threadName + " :你好," + str;
    }

}
