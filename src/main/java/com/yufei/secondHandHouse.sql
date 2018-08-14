CREATE TABLE second_hand (
  id INT (11) UNSIGNED NOT NULL auto_increment COMMENT '自增id',
  link VARCHAR (200) DEFAULT '' COMMENT '详情页链接',
  total_price DOUBLE (11, 4) DEFAULT 0 COMMENT '总价',
  unit_price VARCHAR (200) DEFAULT '' COMMENT '单价',
  down_payments VARCHAR (200) DEFAULT '' COMMENT '首付',
  building_name VARCHAR (200) DEFAULT '' COMMENT '小区名',
  building_year VARCHAR (200) DEFAULT '' COMMENT '小区年份',
  district VARCHAR (200) DEFAULT '' COMMENT '所在区',
  business_district VARCHAR (200) DEFAULT '' COMMENT '所在商圈',
  district_describe VARCHAR (200) DEFAULT '' COMMENT '所在地描述',
  house_layout VARCHAR (200) DEFAULT '' COMMENT '户型',
  tFloor VARCHAR (200) DEFAULT '' COMMENT '楼层',
  house_area VARCHAR (200) DEFAULT '' COMMENT '建筑面积',
  house_type VARCHAR (200) DEFAULT '' COMMENT '户型结构',
  inside_area VARCHAR (200) DEFAULT '' COMMENT '使用面积',
  building_type VARCHAR (200) DEFAULT '' COMMENT '建筑类型',
  orientation VARCHAR (200) DEFAULT '' COMMENT '朝向',
  building_construct VARCHAR (200) DEFAULT '' COMMENT '建筑结构',
  decoration VARCHAR (200) DEFAULT '' COMMENT '装修',
  staircase VARCHAR (200) DEFAULT '' COMMENT '梯户',
  heating VARCHAR (200) DEFAULT '' COMMENT '供暖',
  elevator VARCHAR (200) DEFAULT '' COMMENT '电梯',
  property_right_year VARCHAR (200) DEFAULT '' COMMENT '产权年限',
  listing_date VARCHAR (200) DEFAULT '' COMMENT '挂牌日期',
  trading_type VARCHAR (200) DEFAULT '' COMMENT '交易权属',
  last_trade_time VARCHAR (200) DEFAULT '' COMMENT '上次交易日期',
  house_usage VARCHAR (200) DEFAULT '' COMMENT '房屋用途',
  holding_year VARCHAR (200) DEFAULT '' COMMENT '持有年限',
  property_right_own VARCHAR (200) DEFAULT '' COMMENT '产权所属',
  mortgage VARCHAR (200) DEFAULT '' COMMENT '抵押信息',
  usage_rate DOUBLE (11, 4) DEFAULT 0 COMMENT '使用面积占比',
  dp_rate DOUBLE (11, 4) DEFAULT 0 COMMENT '首付占比',
  COMMENT VARCHAR (200) DEFAULT '' COMMENT '评论',
  cost_house DOUBLE (11, 4) DEFAULT 0 COMMENT '净首付',
  cost_agency DOUBLE (11, 4) DEFAULT 0 COMMENT '中介费',
  cost_tax DOUBLE (11, 4) DEFAULT 0 COMMENT '税费',
  is_unique VARCHAR (200) DEFAULT '' COMMENT '是否唯一',
  evaluation_price DOUBLE (11, 4) DEFAULT 0 COMMENT '评估价',
  evaluation_rate DOUBLE (11, 4) DEFAULT 0 COMMENT '评估价占比',
  dpe_rate DOUBLE (11, 4) DEFAULT 0 COMMENT '首付比上评估价',
  PRIMARY KEY (id)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = COMPACT COMMENT = '';

ALTER TABLE `second_hand`
  ADD COLUMN `link`  varchar(255) NULL COMMENT '详情页链接' AFTER `id` ;

ALTER TABLE `second_hand`
  MODIFY COLUMN `down_payments`  double(11,4) NULL DEFAULT 0 COMMENT '首付' AFTER `unit_price`;

