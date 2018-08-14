package com.yufei;

import com.yufei.dao.SecondHandDao;
import com.yufei.entity.SecondHand;
import com.yufei.spiderService.LianjiaService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2018/5/27.
 */
public class test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-dao.xml");
        SecondHandDao secondHandDao = context.getBean(SecondHandDao.class);
        SecondHand house = LianjiaService.getHouse("https://bj.lianjia.com/ershoufang/101102901942.html");
        if(house != null) {
            System.out.println("aa");
//            secondHandDao.insertSecondHand(house);
        }
    }
}
