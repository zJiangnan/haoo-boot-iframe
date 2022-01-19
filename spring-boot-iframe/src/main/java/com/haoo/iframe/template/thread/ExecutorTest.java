package com.haoo.iframe.template.thread;

import com.alibaba.fastjson.JSON;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

public class ExecutorTest {

    private static LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<Runnable>(100000);
    private static ArrayBlockingQueue<Runnable> arrayBlockingQueue = new ArrayBlockingQueue<Runnable>(100000);

    public static void main(String[] args) throws Exception {
        //队列
        customPool();
        //回调
        callableThread();
        //常规
        runnableThread();
        //示例
        exampleThread();
    }


    public static String callableThread() throws Exception {
        LinkedBlockingQueue<String> hlsQueue = new LinkedBlockingQueue<>();
        //初始化线程池
        ThreadPoolExecutor customThreadPool = init("thread-pool-");

        String[] strings = {"Java", "Callable", "C#", "Python", "PHP"};

        //回调会按照顺序执行
        for (String str : strings) {
            hlsQueue.put(str);
            // submit 使用
            Future future = customThreadPool.submit((new CallableThread(hlsQueue.take())));
            if (!future.isDone()) {
                System.out.println(JSON.toJSON(future.get()));
            }
        }
        //shutdown之后不会在接受新任务
        customThreadPool.shutdown();
        return "callableThread end";
    }


    public static String runnableThread() {
        //初始化线程池
        ThreadPoolExecutor customThreadPool = init("thread-pool-");

        for (int i = 0; i < 3; i++) {
            // submit 使用
            customThreadPool.execute(() -> {
                Thread thread = new Thread(new RunnableThread(Thread.currentThread().getName()));
                thread.start();
            });
        }

        //shutdown之后不会在接受新任务
        customThreadPool.shutdown();
        return "runnableThread end";
    }


    public static String customPool() throws Exception {
        LinkedBlockingQueue<String> hlsQueue = new LinkedBlockingQueue<>();
        //初始化线程池
        ThreadPoolExecutor customThreadPool = init("thread-pool-");
        String[] strings = {"Java", "Callable", "C#", "Python", "PHP"};

        for (String str : strings) {
            hlsQueue.put(str);
            customThreadPool.execute(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + ":" + hlsQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        //shutdown之后不会在接受新任务
        customThreadPool.shutdown();
        return "customPool end";
    }

    public static String exampleThread() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor customThreadPool = init("thread-pool-");

        customThreadPool.execute(() -> {
            System.out.println("Hello , Java");
        });

        Future<String> future = customThreadPool.submit(() -> {
            System.out.println("Hello , Java");
        }, "return result");

        if (!future.isDone()) {
            System.out.println(JSON.toJSON(future.get()));
        }

        customThreadPool.execute(() -> {
            ExtendsThread thread = new ExtendsThread();
            thread.start();
        });

        customThreadPool.shutdown();
        return "extendsThread end";
    }


    //@PostConstruct初始化线程池
    public static ThreadPoolExecutor init(String threadNamePrefix) {
        ThreadFactory hlsThreadFactory = new CustomizableThreadFactory(threadNamePrefix);
        int corePoolSize = 10;
        int maximumPoolSize = 10;
        long keepAliveTime = 0;

        //学习资料:https://www.jianshu.com/p/f030aa5d7a28
        //[核心线程池大小,最大线程池大小,线程最大空闲时间,时间单位,线程等待队列,线程创建工厂,拒绝策略]
        //[默认拒绝策略-AbortPolicy:丢弃任务并抛出RejectedExecutionException异常]
        //[拒绝策略-DiscardPolicy:丢弃任务，但是不抛出异常。如果线程队列已满，则后续提交的任务都会被丢弃，且是静默丢弃。]
        //[拒绝策略-DiscardOldestPolicy:丢弃队列最前面的任务，然后重新提交被拒绝的任务。]
        //[拒绝策略-CallerRunsPolicy:由调用线程处理该任务]
        ThreadPoolExecutor hlsPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, linkedBlockingQueue, hlsThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        //预设线程池
        ThreadPoolExecutor fixedThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        ThreadPoolExecutor cachedThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        ThreadPoolExecutor scheduledThreadPool = (ThreadPoolExecutor) Executors.newScheduledThreadPool(10);
        ExecutorService singleExecutor = Executors.newSingleThreadExecutor();


        return hlsPool;
    }

    public static TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(10000);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("thread-pool-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

}
