package com.yufei.spiderService.consumer;

import com.yufei.dao.SecondHandDao;
import com.yufei.entity.SecondHand;
import com.yufei.spiderService.LianjiaService;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 从队列里取出一个详情页的20个movie组成list并查询详情页，添加Movie字段，然后插入数据库
 * Created by Administrator on 2017/11/28.
 */
public class Consumer extends BaseConsumer{

    private SecondHandDao secondHandDao;
    private BlockingQueue<List<String>> queue;
    // 消费上限
    private static final int totalCount = 73;
    // 消费计数器
    private static AtomicInteger count = new AtomicInteger();
    // 多线程间是否启动变量，有强制从主内存中刷新的功能。即时返回线程的状态
    private static volatile boolean isRunning = true;

    public Consumer(BlockingQueue queue, SecondHandDao secondHandDao) {
        this.queue = queue;
        this.secondHandDao = secondHandDao;
    }

    //随机对象
    private static Random r = new Random();

    @Override
    public void run() {
        while (isRunning) {
            try {
                int i;
                //获取数据
                List<String> detailSeed = this.queue.take();
                System.out.println("消费者开始处理：" + detailSeed.get(0));
                synchronized (Consumer.class) {
                    if (isRunning) { // 可能有多个在等待
                        i = count.incrementAndGet(); // 拿到数据并增加计数
                        if (count.get() == totalCount) { // 目标内容已经消费结束
                            isRunning = false; // 不再消费;但是要把当前的消费掉
                        }
                    } else {
                        break; // 同时等待的其他线程就不执行了
                    }
                }
                //进行数据处理。
                for (String movie : detailSeed) { // 循环详情页
                    try {
                        SecondHand secondHand = LianjiaService.getHouse(movie);
                        secondHandDao.insertSecondHand(secondHand);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("当前消费线程：" + Thread.currentThread().getName() + "， 消费成功，消费数据为第 " + i + "个");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("消费者线程结束");
    }
}
