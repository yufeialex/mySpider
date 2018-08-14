package com.yufei.spiderService;

import com.google.gson.Gson;
import com.yufei.entity.SecondHand;
import com.yufei.util.FileOperation;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2017/11/22.
 */
public class LianjiaService {

    private static Lock lock = new ReentrantLock();
    private static Logger logger = LoggerFactory.getLogger(LianjiaService.class);

    // 计算信息的url地址
    public static final String CAL_URL_BASE = "https://bj.lianjia.com/tools/calccost?house_code=";
    // 评论信息的url地址
    public static final String COMMENT_URL_BASE = "https://bj.lianjia.com/ershoufang/showcomment?isContent=1&page=1&order=0&id=";

    public static List<String> listHouses(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    //.data("query", "Java")
                    .userAgent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; MALC)")
                    //.cookie("auth", "token")
                    .timeout(30000)
                    //.post()
                    .get();

            //得到videos这个div内的所有东西
            Element sellListContent = doc.getElementsByClass("sellListContent").get(0);
            Elements children = sellListContent.children();
            ArrayList<String> ret = new ArrayList<>();
            for (Element video : children) {
                Element a = video.getElementsByClass("img").get(0);
                String href = a.attr("href");
                ret.add(href);
            }
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * 详情页有3个ajax请求：
     * https://bj.lianjia.com/tools/calccost?house_code=101102884386，计算信息，包含了主要的有用信息
     * https://bj.lianjia.com/ershoufang/showcomment?isContent=1&page=1&order=0&id=101102884386&_=1527409698970，评论信息，包含了能否转商的信息
     * https://bj.lianjia.com/api/newhouserecommend?type=2&id=101102884386，推荐信息，不关注
     * 详情页的连接https://bj.lianjia.com/ershoufang/101102884386.html
     * 101102884386是houseCode，
     * 静态页面与动态请求都有的信息，从静态页面取；因为有时候动态请求会返回空
     * @param detailUrl
     * @return
     */
    public static SecondHand getHouse(String detailUrl) {
        try {
//            if(1 == 1) {
//                throw new RuntimeException("cc");
//            }
            String substring = detailUrl.substring(detailUrl.lastIndexOf("/") + 1, detailUrl.length());
            String houseCode = substring.substring(0, substring.lastIndexOf("."));
            Connection conn = Jsoup.connect(detailUrl)
                    .userAgent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; MALC)")
                    .timeout(3000);
            Thread.sleep(5000);
            Document doc = conn.get();

            // ---------------------以下是在静态页面获得的信息-----------------------------
            // 总价和单价,price标签
            Elements price = doc.getElementsByClass("price");
            String tt = price.get(0).getElementsByClass("total").get(0).text();
            Double total_price = Double.valueOf(tt);
            String unit_price = price.get(0).getElementsByClass("unitPriceValue").get(0).text();
            // 首付不能从这里获取
//            String dp = price.get(0).getElementById("tax-text").getElementsByClass("taxtext").get(0).text();
//            String down_payments = dp.substring(2, dp.indexOf("."));

            // 年份，houseInfo标签
            String yearInfo = doc.getElementsByClass("houseInfo").get(0).getElementsByClass("area")
                    .get(0).getElementsByClass("subInfo").get(0).text();
            String building_year = yearInfo.substring(0, yearInfo.indexOf("年"));
            // 区域，aroundInfo标签
            String building_name = doc.getElementsByClass("communityName").get(0)
                    .getElementsByTag("a").get(0).text();
            Elements areaName = doc.getElementsByClass("areaName");
            String district = areaName.get(0).getElementsByClass("info")
                    .get(0).getElementsByTag("a").get(0).text();
            String business_district = areaName.get(0).getElementsByClass("info")
                    .get(0).getElementsByTag("a").get(1).text();
            String district_describe = areaName.get(0).getElementsByTag("span").get(1).text();
            

            // 基本信息
            Elements introContent = doc.getElementsByClass("introContent");
            Elements content = introContent.get(0).getElementsByClass("base").get(0).getElementsByClass("content")
                    .get(0).getElementsByTag("li");

            String house_layout = content.get(0).text();
            String tFloor = content.get(1).text();
            String house_area_raw = content.get(2).text();
            String house_area = house_area_raw.substring(4, house_area_raw.indexOf("㎡"));
            String house_type = content.get(3).text();
            String inside_area_raw = content.get(4).text();
            String inside_area = "暂无数据";
            Double usage_rate = 0d;
            if(!inside_area_raw.contains("暂无数据")) {
                inside_area = inside_area_raw.substring(4, inside_area_raw.indexOf("㎡"));
                usage_rate = Double.valueOf(inside_area) / Double.valueOf(house_area);
            }
            String building_type = content.get(5).text();
            String orientation = content.get(6).text();
            String building_construct = content.get(7).text();

            String decoration = content.get(8).text();
            String staircase = content.get(9).text();
            String heating = content.get(10).text();
            String elevator = content.get(11).text();
            String property_right_year = content.get(12).text();

            Elements trade = introContent.get(0).getElementsByClass("transaction").get(0).getElementsByClass("content")
                    .get(0).getElementsByTag("li");
            String listing_date = trade.get(0).text();
            String trading_type = trade.get(1).text();
            String last_trade_time = trade.get(2).text();
            String house_usage = trade.get(3).text();
            String holding_year = trade.get(4).text();
            String property_right_own = trade.get(5).text();
            String mortgage = trade.get(6).text();


            // ---------------------静态页面信息到此为止-----------------------------

            //创建httpclient实例
            CloseableHttpClient httpClient = HttpClients.createDefault();

            // ---------------------以下是在评论请求获得的信息-----------------------------
            // 评论信息请求
            Map mapResult = getMapResult(httpClient, COMMENT_URL_BASE + houseCode);
            StringBuilder sb = new StringBuilder();
            String comment = null;
            if (mapResult != null) {
                List<Map> agentList = (List<Map>) mapResult.get("agentList");
                String totalPageNum = (String)mapResult.get("totalPageNum");
                agentList.forEach(item -> sb.append(item.get("comment")));
                for(int i = 2; i <= Integer.valueOf(totalPageNum); i++) {
                    Map mapResult2 = getMapResult(httpClient, "https://bj.lianjia.com/ershoufang/showcomment?isContent=1&page=" + i + "&order=0&id=" + houseCode);
                    List<Map> agentList1 = (List<Map>) mapResult2.get("agentList");
                    agentList1.forEach(item -> sb.append(item.get("comment")));
                }
                comment = sb.toString();
            } else {
                System.out.println("评论信息接口返回为空：" + detailUrl);
            }
            // ---------------------评论请求到此为止-----------------------------

            // ---------------------以下是在计算请求获得的信息-----------------------------
            // 主要信息请求
            Map mapResult1 = getMapResult(httpClient, CAL_URL_BASE + houseCode);

            if(mapResult1 != null) {
                Map payment = (Map) mapResult1.get("payment");
                Double cost_house = (Double) payment.get("cost_house");
                Double cost_tax = (Double) payment.get("cost_tax");
                Double cost_agency = (Double) payment.get("cost_jingjiren");
                Double down_payments = cost_house + cost_agency + cost_tax;

                Map selectors = (Map) mapResult1.get("selectors");
                List<Map> uniqueTypes = (List<Map>) selectors.get("is_unique");
                final String[] is_unique = {""};
                uniqueTypes.forEach(item -> {
                    if ((Boolean) item.get("selected")) {
                        is_unique[0] = (String) item.get("name");
                    }
                });

                Map params = (Map) mapResult1.get("params");
                Double evaluation_price = (Double) params.get("pinggujia");

                Double dp_rate = down_payments / total_price;
                Double dpe_rate = down_payments / evaluation_price;
                Double evaluation_rate = evaluation_price / total_price;

                SecondHand secondHand1 = new SecondHand(detailUrl, total_price, unit_price, down_payments, building_name, building_year, district,
                        business_district, district_describe, house_layout, tFloor, house_area, house_type, inside_area,
                        building_type, orientation, building_construct, decoration, staircase, heating, elevator,
                        property_right_year, listing_date, trading_type, last_trade_time, house_usage, holding_year,
                        property_right_own, mortgage, usage_rate, dp_rate, comment, cost_house, cost_agency, cost_tax,
                        is_unique[0], evaluation_price, evaluation_rate, dpe_rate);

                return secondHand1;
            }
            // ---------------------计算请求到此为止-----------------------------

            System.out.println("主要信息接口返回为空：" + detailUrl);

           
            httpClient.close();
            SecondHand secondHand2 = new SecondHand(detailUrl, total_price, unit_price, 0d, building_name, building_year, district,
                    business_district, district_describe, house_layout, tFloor, house_area, house_type, inside_area,
                    building_type, orientation, building_construct, decoration, staircase, heating, elevator,
                    property_right_year, listing_date, trading_type, last_trade_time, house_usage, holding_year,
                    property_right_own, mortgage, usage_rate, 0d, comment, 0d, 0d, 0d,
                    "", 0d, 0d, 0d);
            return secondHand2;
        } catch (Exception e) {
            // 开发时候打印堆栈
            logger.error("[{}] 出错啦：", detailUrl, e);
            System.out.println(e.toString() + detailUrl);
            // 运行时候不需要打印堆栈
//            System.out.println(e.getMessage() + detailUrl);
//            lock.lock();
//            try {
//                method1("D:\\AAA.txt","\n"+ detailUrl + "\n" + e.toString() + "\n\n\n " );
//            } catch (Exception e1) {
//                System.out.println(e1);
//            }
//            lock.unlock();
            return null;
        }
}

    public static void method1(String file, String conent) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.write(conent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得ajax请求返回信息的map对象
     * @param httpClient httpClient实例
     * @param linkAddr ajax请求的地址
     * @return ajax请求返回信息转换所得的map对象
     * @throws IOException
     */
    private static Map getMapResult(CloseableHttpClient httpClient, String linkAddr) throws IOException {
        //创建httpget实例
        HttpGet httpGet = new HttpGet(linkAddr);  //系統有限制
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
        httpGet.addHeader("Content-Type", "application/json");//3设置请求头参数和参数类型


        //执行http get 请求
        CloseableHttpResponse response = null;
        response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();//获取返回实体
        String str = EntityUtils.toString(entity, "utf-8");//获取网页内容，指定编码
        Gson gson = new Gson();
        response.close();
        return (Map) gson.fromJson(str, HashMap.class).get("data");
    }


}
