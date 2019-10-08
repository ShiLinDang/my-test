package com.my.test.demo.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:线程池队列测试
 *
 * @author dsl
 * @date 2019/10/8 14:35
 */
public class ThreadPoolExcutorTest implements Runnable{

    private String name;

    public ThreadPoolExcutorTest(String name){
        this.name = name;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println(name);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(3);
        /**
         * 有界队列:
         * 1.线程池接收到任务后直接 new 一个Thread,立马执行
         * 2.提交的任务数7个超过了核心线程数量2,此时会把剩余的放到队列中排队执行,只能放3个(有界队列最大容量设置为3)
         * 3.通过步骤1,2已经处理了5个任务,队列也放满了,会尝试 new 一个 Thread 救急处理,救急处理之后已经达到线程池最大处理能力6
         * ,当第7个任务到达时实在无法处理,只有执行 reject(拒绝) 操作,抛出异常
         * 总结:当线程池的任务缓存队列已满并且线程池中的线程数目达到 maximumPoolSize，如果还有任务到来就会采取任务拒绝策略。
         *
         * 无界队列:
         * 与有界队列相比，除非系统资源耗尽，否则无界的任务队列不存在任务入队失败的情况。
         * 当有新的任务到来，系统的线程数小于corePoolSize时，则新建线程执行任务。
         * 当达到corePoolSize后，就不会继续增加线程数量，若后续仍有新的任务加入，而没有空闲的线程资源，则任务直接进入队列等待。
         * 若任务创建和处理的速度差异很大，无界队列会保持快速增长，直到耗尽系统内存。
         * 无界队列如果修改了线程池的maximumPoolSize参数（大于corePoolSize的大小），
         * 程序执行结果不受影响。所以对于无界队列，maximumPoolSize的设置设置的再大对于线程的执行是没有影响的。
         */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,3,1L,TimeUnit.SECONDS,workQueue);
        executor.execute(new ThreadPoolExcutorTest("任务1"));
        executor.execute(new ThreadPoolExcutorTest("任务2"));
        executor.execute(new ThreadPoolExcutorTest("任务3"));
        executor.execute(new ThreadPoolExcutorTest("任务4"));
        executor.execute(new ThreadPoolExcutorTest("任务5"));
        executor.execute(new ThreadPoolExcutorTest("任务6"));
        executor.execute(new ThreadPoolExcutorTest("任务7"));
        executor.shutdown();
    }
}
