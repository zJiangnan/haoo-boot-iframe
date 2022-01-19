package com.haoo.iframe.template.thread;

public class RunnableThread implements Runnable {

    private String parentName;

    public RunnableThread(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public void run() {
        System.out.println(parentName + "-Runnable 子线程 : " + Thread.currentThread().getName());
    }

}
