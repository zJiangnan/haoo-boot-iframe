package com.haoo.iframe.template.thread;

public class ExtendsThread extends Thread {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ":ExtendsThread");
    }
}
