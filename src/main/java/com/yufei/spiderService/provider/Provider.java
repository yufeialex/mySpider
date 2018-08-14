package com.yufei.spiderService.provider;

import com.yufei.dao.SecondHandDao;
import com.yufei.spiderService.LianjiaService;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 向队列里面放一个详情页的20个movie组成list
 */
public class Provider implements Runnable {

    //共享缓存区
    private BlockingQueue<List<String>> queue;
    private SecondHandDao movieDao;
    //多线程间是否启动变量，有强制从主内存中刷新的功能。即时返回线程的状态
    private static volatile boolean isRunning1 = true;
    //id生成器
    private static AtomicInteger count1 = new AtomicInteger(0);
    // url地址生成用
    private static AtomicInteger urlNumber = new AtomicInteger(1);
    //随机对象
    private static Random r = new Random();
    // 生产上限
    private static final int totalCount1 = 73;
//    private static final int totalCount1 = 43;
    private String base = "https://bj.lianjia.com/ershoufang/pg";
    private String base1 = "ng1hu1nb1mw1ty1y1y2y3dp1dp2dp3dp4sf1sf5ba40ea20000bp100ep400/";

    public Provider(BlockingQueue queue, SecondHandDao movieDao) {
        this.queue = queue;
        this.movieDao = movieDao;
    }

    @Override
    public void run() {
        while (isRunning1) {
            try {
                int id = 0;
                String listUrl = "";
                synchronized (Provider.class) {
                    //生产主体，生产出详情页的连接并放到队列
                    listUrl = base + urlNumber.getAndIncrement() + base1;
                    System.out.println("生产者构造了待处理的url："+listUrl);
                }

                List<String> detailUrl = LianjiaService.listHouses(listUrl);

                // 这段代码是用于检测什么时候结束生产的
                synchronized (Provider.class) { // 完成生产就立即计数
                    if (isRunning1) { // 可能多个线程等在这里
                        //获取的数据进行累计...
                        id = count1.incrementAndGet();
                        if (count1.get() == totalCount1) { // 生产工作已经结束
                            isRunning1 = false; // 不再生产，但是我是第6个，还是要添加的
                        }
                    } else {
                        break; // 其他等待线程就不再插入了
                    }
                }

                boolean offer = this.queue.offer(detailUrl, 2, TimeUnit.SECONDS);
                if (offer) {
                    System.out.println("生产线程：" + Thread.currentThread().getName() + "把" + listUrl +"放入队列,是第" + id + "个");
                } else {
                    System.out.println("提交缓冲区数据失败....");
                    //do something... 比如重新提交
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("生产者线程结束");
    }

}
