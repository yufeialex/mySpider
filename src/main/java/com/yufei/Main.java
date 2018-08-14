package com.yufei;

import com.yufei.dao.SecondHandDao;
import com.yufei.entity.SecondHand;
import com.yufei.spiderService.consumer.Consumer;
import com.yufei.spiderService.provider.Provider;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-dao.xml");
        SecondHandDao movieDao = context.getBean(SecondHandDao.class);
        //内存缓冲区
        BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>(100);
        //生产者
        Provider p1 = new Provider(queue, movieDao);
//		Provider p2 = new Provider(queue, movieDao);
        //消费者
        Consumer c1 = new Consumer(queue, movieDao);
        Consumer c2 = new Consumer(queue, movieDao);
        Consumer c3 = new Consumer(queue, movieDao);
        Consumer c4 = new Consumer(queue, movieDao);
        Consumer c5 = new Consumer(queue, movieDao);
        Consumer c6 = new Consumer(queue, movieDao);

        //创建线程池运行,这是一个缓存的线程池，可以创建无穷大的线程，没有任务的时候不创建线程。空闲线程存活时间为60s（默认值）
        ExecutorService cachePool = Executors.newCachedThreadPool();
        cachePool.execute(p1);
//		cachePool.execute(p2);
        cachePool.execute(c1);
        cachePool.execute(c2);
        cachePool.execute(c3);
        cachePool.execute(c4);
        cachePool.execute(c5);
        cachePool.execute(c6);

        cachePool.shutdown();
        System.out.println("主线程结束");
    }
}
