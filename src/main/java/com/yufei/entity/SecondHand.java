package com.yufei.entity;

/**
 * Created by Administrator on 2018/5/6.
 */
public class SecondHand {
    private Integer id;
    private String link;

    // 静态页面头部信息，8个字段
    private Double total_price;
    private String unit_price;

    private Double down_payments; // 从ajax读取

    private String building_name;
    private String building_year;

    private String district;
    private String business_district;
    private String district_describe;

    // 静态页面主体信息字段，20个
    // 基本属性
    private String house_layout;
    private String tFloor;
    private String house_area;
    private String house_type;
    private String inside_area;
    private String building_type;
    private String orientation;
    private String building_construct;
    private String decoration;
    private String staircase;
    private String heating;
    private String elevator;
    private String property_right_year;

    // 交易属性
    private String listing_date ;
    private String trading_type;
    private String last_trade_time;
    private String house_usage ;
    private String holding_year;
    private String property_right_own;
    private String mortgage;

    // 计算所得信息，依赖于静态页面，2个
    private Double usage_rate;
    private Double dp_rate; // 从ajax计算

    // 评论ajax获得的字段，1个
    private String comment;

    // 计算ajax获得的字段，5个
    private Double cost_house;
    private Double cost_agency;
    private Double cost_tax;
    private String is_unique;
    private Double evaluation_price;

    // 计算所得信息，依赖于计算ajax请求，2个
    private Double evaluation_rate;
    private Double dpe_rate;

    public SecondHand(String link, Double total_price, String unit_price, Double down_payments, String building_name, String building_year, String district, String business_district, String district_describe, String house_layout, String tFloor, String house_area, String house_type, String inside_area, String building_type, String orientation, String building_construct, String decoration, String staircase, String heating, String elevator, String property_right_year, String listing_date, String trading_type, String last_trade_time, String house_usage, String holding_year, String property_right_own, String mortgage, Double usage_rate, Double dp_rate, String comment, Double cost_house, Double cost_agency, Double cost_tax, String is_unique, Double evaluation_price, Double evaluation_rate, Double dpe_rate) {
        this.link = link;
        this.total_price = total_price;
        this.unit_price = unit_price;
        this.down_payments = down_payments;
        this.building_name = building_name;
        this.building_year = building_year;
        this.district = district;
        this.business_district = business_district;
        this.district_describe = district_describe;
        this.house_layout = house_layout;
        this.tFloor = tFloor;
        this.house_area = house_area;
        this.house_type = house_type;
        this.inside_area = inside_area;
        this.building_type = building_type;
        this.orientation = orientation;
        this.building_construct = building_construct;
        this.decoration = decoration;
        this.staircase = staircase;
        this.heating = heating;
        this.elevator = elevator;
        this.property_right_year = property_right_year;
        this.listing_date = listing_date;
        this.trading_type = trading_type;
        this.last_trade_time = last_trade_time;
        this.house_usage = house_usage;
        this.holding_year = holding_year;
        this.property_right_own = property_right_own;
        this.mortgage = mortgage;
        this.usage_rate = usage_rate;
        this.dp_rate = dp_rate;
        this.comment = comment;
        this.cost_house = cost_house;
        this.cost_agency = cost_agency;
        this.cost_tax = cost_tax;
        this.is_unique = is_unique;
        this.evaluation_price = evaluation_price;
        this.evaluation_rate = evaluation_rate;
        this.dpe_rate = dpe_rate;
    }
}
