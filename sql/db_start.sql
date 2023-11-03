/*
SQLyog Ultimate v10.00 Beta1
MySQL - 5.7.25-log : Database - db_start
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db_start` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `db_start`;

/*Table structure for table `address_book` */

DROP TABLE IF EXISTS `address_book`;

CREATE TABLE `address_book` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `consignee` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '收货人',
  `sex` tinyint(4) NOT NULL COMMENT '性别 0 女 1 男',
  `phone` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '手机号',
  `province_code` varchar(12) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '省级区划编号',
  `province_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '省级名称',
  `city_code` varchar(12) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '市级区划编号',
  `city_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '市级名称',
  `district_code` varchar(12) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '区级区划编号',
  `district_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '区级名称',
  `detail` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '详细地址',
  `label` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '标签',
  `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT '默认 0 否 1是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_user` bigint(20) NOT NULL COMMENT '修改人',
  `is_deleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='地址管理';

/*Data for the table `address_book` */

insert  into `address_book`(`id`,`user_id`,`consignee`,`sex`,`phone`,`province_code`,`province_name`,`city_code`,`city_name`,`district_code`,`district_name`,`detail`,`label`,`is_default`,`create_time`,`update_time`,`create_user`,`update_user`,`is_deleted`) values (1417414526093082626,1417012167126876162,'小明',1,'13812345678',NULL,NULL,NULL,NULL,NULL,NULL,'昌平区金燕龙办公楼','公司',1,'2021-07-20 17:22:12','2021-07-20 17:26:33',1417012167126876162,1417012167126876162,0),(1417414926166769666,1417012167126876162,'小李',1,'13512345678',NULL,NULL,NULL,NULL,NULL,NULL,'测试','家',0,'2021-07-20 17:23:47','2021-07-20 17:23:47',1417012167126876162,1417012167126876162,0),(1719717053423550466,1718929869313593345,'师',1,'18838563945',NULL,NULL,NULL,NULL,NULL,NULL,'河南省洛阳市偃师市','家',0,'2023-11-01 22:04:31','2023-11-01 22:04:31',1718929869313593345,1718929869313593345,0),(1719717645290176513,1718929869313593345,'丁',0,'18524606523',NULL,NULL,NULL,NULL,NULL,NULL,'浙江杭州萧山金色钱塘','公司',1,'2023-11-01 22:06:52','2023-11-01 22:09:32',1718929869313593345,1718929869313593345,0);

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `type` int(11) DEFAULT NULL COMMENT '类型   1 菜品分类 2 套餐分类',
  `name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '分类名称',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '顺序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_user` bigint(20) NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_category_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='菜品及套餐分类';

/*Data for the table `category` */

insert  into `category`(`id`,`type`,`name`,`sort`,`create_time`,`update_time`,`create_user`,`update_user`) values (1413341197421846529,1,'饮品',11,'2021-07-09 11:36:15','2021-07-09 14:39:15',1,1),(1413384954989060097,1,'主食',12,'2021-07-09 14:30:07','2021-07-09 14:39:19',1,1),(1719936680577622018,1,'大补汤',11,'2023-11-02 12:37:14','2023-11-02 12:41:44',1,1),(1719937780584177666,1,'凉菜',5,'2023-11-02 12:41:36','2023-11-02 12:41:36',1,1),(1719957072495202305,1,'小炒',2,'2023-11-02 13:58:16','2023-11-02 13:58:16',1,1),(1719957390872236033,2,'超值套餐',1,'2023-11-02 13:59:32','2023-11-02 13:59:32',1,1),(1719957571227308034,1,'特色菜品',3,'2023-11-02 14:00:15','2023-11-02 14:00:15',1,1);

/*Table structure for table `dish` */

DROP TABLE IF EXISTS `dish`;

CREATE TABLE `dish` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '菜品名称',
  `category_id` bigint(20) NOT NULL COMMENT '菜品分类id',
  `price` decimal(10,2) DEFAULT NULL COMMENT '菜品价格',
  `code` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '商品码',
  `image` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '图片',
  `description` varchar(400) COLLATE utf8_bin DEFAULT NULL COMMENT '描述信息',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '0 停售 1 起售',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '顺序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_user` bigint(20) NOT NULL COMMENT '修改人',
  `is_deleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_dish_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='菜品管理';

/*Data for the table `dish` */

insert  into `dish`(`id`,`name`,`category_id`,`price`,`code`,`image`,`description`,`status`,`sort`,`create_time`,`update_time`,`create_user`,`update_user`,`is_deleted`) values (1719936195749634050,'雪花',1413341197421846529,'800.00','','1aab7f81-3435-430d-8d73-6032697f3bc4.jpg','',1,0,'2023-11-02 12:35:19','2023-11-02 12:35:19',1,1,0),(1719936321561976833,'农夫山泉',1413341197421846529,'300.00','','bf745960-550f-4a0b-9e66-ea346eba2b21.jpg','',1,0,'2023-11-02 12:35:49','2023-11-02 12:35:49',1,1,0),(1719936448288677890,'冰红茶',1413341197421846529,'600.00','','cbe94ef0-d435-47d6-af45-ad02879d1b9f.jpg','',1,0,'2023-11-02 12:36:19','2023-11-02 12:36:19',1,1,0),(1719936536041906178,'百事可乐',1413341197421846529,'600.00','','a2b17319-ed60-4ac1-ac11-1b6d571ec001.jpg','',1,0,'2023-11-02 12:36:40','2023-11-02 12:36:40',1,1,0),(1719936826279354370,'菌菇汤',1719936680577622018,'1200.00','','c2681799-c7be-475f-a5e0-84f0a5b203f0.jpg','',1,0,'2023-11-02 12:37:49','2023-11-02 12:37:49',1,1,0),(1719937675785297922,'米饭',1413384954989060097,'400.00','','ee4dcb86-76bf-457e-a146-44a53bc5b66a.jpg','',1,0,'2023-11-02 12:41:11','2023-11-02 12:41:11',1,1,0),(1719937964290498561,'黑木耳',1719937780584177666,'1200.00','','6e6c5d42-c893-4604-8e27-2650e514c877.jpg','',1,0,'2023-11-02 12:42:20','2023-11-02 12:42:20',1,1,0),(1719938079243788290,'海苔花生',1719937780584177666,'1600.00','','7d6986ad-8094-40e1-b3b6-41a6c717522b.jpg','',1,0,'2023-11-02 12:42:48','2023-11-02 12:42:48',1,1,0),(1719956958124920833,'香菜拌皮蛋',1719937780584177666,'1600.00','','50214e19-2586-4d95-be3b-0d60acd816df.jpg','',1,0,'2023-11-02 13:57:49','2023-11-02 13:57:49',1,1,0),(1719957269606518785,'海鲜炒面',1719957072495202305,'1200.00','','0141ad07-1a43-4f97-9f06-ba88f96ab117.jpg','',1,0,'2023-11-02 13:59:03','2023-11-02 13:59:03',1,1,0),(1719957778824384513,'干锅花菜',1719957571227308034,'1600.00','','61687032-975e-4f41-8766-2ac33dedaa3f.jpg','',1,0,'2023-11-02 14:01:04','2023-11-02 14:01:04',1,1,0),(1719957927948668929,'酸辣土豆丝',1719957571227308034,'2200.00','','efd9694b-0066-4dc7-9725-89dd67880cd6.jpg','',1,0,'2023-11-02 14:01:40','2023-11-02 14:01:40',1,1,0);

/*Table structure for table `dish_flavor` */

DROP TABLE IF EXISTS `dish_flavor`;

CREATE TABLE `dish_flavor` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `dish_id` bigint(20) NOT NULL COMMENT '菜品',
  `name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '口味名称',
  `value` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '口味数据list',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_user` bigint(20) NOT NULL COMMENT '修改人',
  `is_deleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='菜品口味关系表';

/*Data for the table `dish_flavor` */

insert  into `dish_flavor`(`id`,`dish_id`,`name`,`value`,`create_time`,`update_time`,`create_user`,`update_user`,`is_deleted`) values (1397849417888346113,1397849417854791681,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:37:27','2021-05-27 09:37:27',1,1,0),(1397849936421761025,1397849936404983809,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 09:39:30','2021-05-27 09:39:30',1,1,0),(1397849936438538241,1397849936404983809,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:39:30','2021-05-27 09:39:30',1,1,0),(1397850630734262274,1397850630700707841,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 09:42:16','2021-05-27 09:42:16',1,1,0),(1397850630755233794,1397850630700707841,'辣度','[\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:42:16','2021-05-27 09:42:16',1,1,0),(1397853423486414850,1397853423461249026,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 09:53:22','2021-05-27 09:53:22',1,1,0),(1397854133632413697,1397854133603053569,'温度','[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]','2021-05-27 09:56:11','2021-05-27 09:56:11',1,1,0),(1397855742303186946,1397855742273826817,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 10:02:35','2021-05-27 10:02:35',1,1,0),(1397855906497605633,1397855906468245506,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 10:03:14','2021-05-27 10:03:14',1,1,0),(1397856190573621250,1397856190540066818,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 10:04:21','2021-05-27 10:04:21',1,1,0),(1397859056709316609,1397859056684150785,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 10:15:45','2021-05-27 10:15:45',1,1,0),(1397859277837217794,1397859277812051969,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 10:16:37','2021-05-27 10:16:37',1,1,0),(1397859487502086146,1397859487476920321,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 10:17:27','2021-05-27 10:17:27',1,1,0),(1397859757061615618,1397859757036449794,'甜味','[\"无糖\",\"少糖\",\"半躺\",\"多糖\",\"全糖\"]','2021-05-27 10:18:32','2021-05-27 10:18:32',1,1,0),(1397861135754506242,1397861135733534722,'甜味','[\"无糖\",\"少糖\",\"半躺\",\"多糖\",\"全糖\"]','2021-05-27 10:24:00','2021-05-27 10:24:00',1,1,0),(1397861370035744769,1397861370010578945,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-27 10:24:56','2021-05-27 10:24:56',1,1,0),(1397861898467717121,1397861898438356993,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-27 10:27:02','2021-05-27 10:27:02',1,1,0),(1398089545865015297,1398089545676271617,'温度','[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]','2021-05-28 01:31:38','2021-05-28 01:31:38',1,1,0),(1398089782323097601,1398089782285348866,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:32:34','2021-05-28 01:32:34',1,1,0),(1398090003262255106,1398090003228700673,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-28 01:33:27','2021-05-28 01:33:27',1,1,0),(1398090264554811394,1398090264517062657,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-28 01:34:29','2021-05-28 01:34:29',1,1,0),(1398090455399837698,1398090455324340225,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:35:14','2021-05-28 01:35:14',1,1,0),(1398090685449023490,1398090685419663362,'温度','[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]','2021-05-28 01:36:09','2021-05-28 01:36:09',1,1,0),(1398090825358422017,1398090825329061889,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-28 01:36:43','2021-05-28 01:36:43',1,1,0),(1398091007051476993,1398091007017922561,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:37:26','2021-05-28 01:37:26',1,1,0),(1398091296164851713,1398091296131297281,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:38:35','2021-05-28 01:38:35',1,1,0),(1398091546531246081,1398091546480914433,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2021-05-28 01:39:35','2021-05-28 01:39:35',1,1,0),(1398091729809747969,1398091729788776450,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:40:18','2021-05-28 01:40:18',1,1,0),(1398091889499484161,1398091889449152513,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:40:56','2021-05-28 01:40:56',1,1,0),(1398092095179763713,1398092095142014978,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:41:45','2021-05-28 01:41:45',1,1,0),(1398092283877306370,1398092283847946241,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:42:30','2021-05-28 01:42:30',1,1,0),(1398094018939236354,1398094018893099009,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:49:24','2021-05-28 01:49:24',1,1,0),(1398094391494094850,1398094391456346113,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-05-28 01:50:53','2021-05-28 01:50:53',1,1,0),(1399574026165727233,1399305325713600514,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2021-06-01 03:50:25','2021-06-01 03:50:25',1399309715396669441,1399309715396669441,0),(1719957778891493378,1719957778824384513,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2023-11-02 14:01:04','2023-11-02 14:01:04',1,1,0),(1719957778891493379,1719957778824384513,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]','2023-11-02 14:01:04','2023-11-02 14:01:04',1,1,0),(1719957928015777793,1719957927948668929,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]','2023-11-02 14:01:40','2023-11-02 14:01:40',1,1,0);

/*Table structure for table `employee` */

DROP TABLE IF EXISTS `employee`;

CREATE TABLE `employee` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '姓名',
  `username` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `password` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '密码',
  `phone` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '手机号',
  `sex` varchar(2) COLLATE utf8_bin NOT NULL COMMENT '性别',
  `id_number` varchar(18) COLLATE utf8_bin NOT NULL COMMENT '身份证号',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态 0:禁用，1:正常',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_user` bigint(20) NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='员工信息';

/*Data for the table `employee` */

insert  into `employee`(`id`,`name`,`username`,`password`,`phone`,`sex`,`id_number`,`status`,`create_time`,`update_time`,`create_user`,`update_user`) values (1,'管理员','admin','e10adc3949ba59abbe56e057f20f883e','13812312312','1','110101199001010047',1,'2021-05-06 17:20:07','2021-05-10 02:24:09',1,1),(1717613436306677762,'里斯','lisi','4297f44b13955235245b2497399d7a93','15254521624','0','421035121202123621',1,'2023-10-27 02:45:30','2023-10-27 02:45:30',1,1),(1717768638166941697,'王五噶','wangwu','4297f44b13955235245b2497399d7a93','18835463545','1','412021515125413413',1,'2023-10-27 13:02:13','2023-10-27 16:56:08',1,1),(1717829858966319106,'小王','test1','4297f44b13955235245b2497399d7a93','18838563912','1','410381200005273691',1,'2023-10-27 17:05:31','2023-10-27 17:05:31',1,1),(1717830141729517569,'笑话','test01','4297f44b13955235245b2497399d7a93','18838563912','1','410381200005273691',1,'2023-10-27 17:06:36','2023-10-27 17:35:14',1,1),(1717838061816983553,'张海天','test002','4297f44b13955235245b2497399d7a93','18535624513','1','412012201221249542',1,'2023-10-27 17:38:04','2023-10-27 17:38:04',1717613436306677762,1717613436306677762),(1717838296710590465,'王满天','test003','4297f44b13955235245b2497399d7a93','18535624513','1','412012201221249542',1,'2023-10-27 17:39:00','2023-10-27 17:39:00',1717613436306677762,1717613436306677762);

/*Table structure for table `order_detail` */

DROP TABLE IF EXISTS `order_detail`;

CREATE TABLE `order_detail` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '名字',
  `image` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '图片',
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `dish_id` bigint(20) DEFAULT NULL COMMENT '菜品id',
  `setmeal_id` bigint(20) DEFAULT NULL COMMENT '套餐id',
  `dish_flavor` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '口味',
  `number` int(11) NOT NULL DEFAULT '1' COMMENT '数量',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单明细表';

/*Data for the table `order_detail` */

insert  into `order_detail`(`id`,`name`,`image`,`order_id`,`dish_id`,`setmeal_id`,`dish_flavor`,`number`,`amount`) values (1719999481597849601,'海鲜炒面','0141ad07-1a43-4f97-9f06-ba88f96ab117.jpg',1719999357396119554,1719957269606518785,NULL,NULL,2,'12.00'),(1719999481664958466,'16元组合套装','c8d143d4-ba85-401f-b9bf-90ad5c5efb77.jpg',1719999357396119554,NULL,1719958247135203329,NULL,1,'16.00'),(1719999481732067329,'冰红茶','cbe94ef0-d435-47d6-af45-ad02879d1b9f.jpg',1719999357396119554,1719936448288677890,NULL,NULL,1,'6.00'),(1720075642239676418,'雪花','1aab7f81-3435-430d-8d73-6032697f3bc4.jpg',1720075642105458689,1719936195749634050,NULL,NULL,2,'8.00'),(1720112151810502657,'16元组合套装','c8d143d4-ba85-401f-b9bf-90ad5c5efb77.jpg',1720112151680479234,NULL,1719958247135203329,NULL,1,'16.00'),(1720112151810502658,'酸辣土豆丝','efd9694b-0066-4dc7-9725-89dd67880cd6.jpg',1720112151680479234,1719957927948668929,NULL,'中辣',1,'22.00');

/*Table structure for table `orders` */

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `number` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '订单号',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '订单状态 1待付款，2待派送，3已派送，4已完成，5已取消',
  `user_id` bigint(20) NOT NULL COMMENT '下单用户',
  `address_book_id` bigint(20) NOT NULL COMMENT '地址id',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `checkout_time` datetime NOT NULL COMMENT '结账时间',
  `pay_method` int(11) NOT NULL DEFAULT '1' COMMENT '支付方式 1微信,2支付宝',
  `amount` decimal(10,2) NOT NULL COMMENT '实收金额',
  `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `phone` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '手机号',
  `address` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '地址信息',
  `user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名',
  `consignee` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '收获人的名字',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单表';

/*Data for the table `orders` */

insert  into `orders`(`id`,`number`,`status`,`user_id`,`address_book_id`,`order_time`,`checkout_time`,`pay_method`,`amount`,`remark`,`phone`,`address`,`user_name`,`consignee`) values (1719999459577753602,'1719999357396119554',4,1718929869313593345,1719717645290176513,'2023-11-02 16:46:20','2023-11-02 16:46:20',1,'46.00','','18524606523','null, 浙江杭州萧山金色钱塘','启航用户','丁'),(1720075642172567553,'1720075642105458689',2,1718929869313593345,1719717645290176513,'2023-11-02 21:49:25','2023-11-02 21:49:25',1,'16.00','','18524606523','null, 浙江杭州萧山金色钱塘','启航用户','丁'),(1720112151760171009,'1720112151680479234',2,1718929869313593345,1719717645290176513,'2023-11-03 00:14:30','2023-11-03 00:14:30',1,'38.00','','18524606523','null, 浙江杭州萧山金色钱塘','启航用户','丁');

/*Table structure for table `setmeal` */

DROP TABLE IF EXISTS `setmeal`;

CREATE TABLE `setmeal` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `category_id` bigint(20) NOT NULL COMMENT '菜品分类id',
  `name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '套餐名称',
  `price` decimal(10,2) NOT NULL COMMENT '套餐价格',
  `status` int(11) DEFAULT NULL COMMENT '状态 0:停用 1:启用',
  `code` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '编码',
  `description` varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '描述信息',
  `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '图片',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_user` bigint(20) NOT NULL COMMENT '修改人',
  `is_deleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_setmeal_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='套餐';

/*Data for the table `setmeal` */

insert  into `setmeal`(`id`,`category_id`,`name`,`price`,`status`,`code`,`description`,`image`,`create_time`,`update_time`,`create_user`,`update_user`,`is_deleted`) values (1719958247135203329,1719957390872236033,'16元组合套装','1600.00',1,'','','c8d143d4-ba85-401f-b9bf-90ad5c5efb77.jpg','2023-11-02 14:02:56','2023-11-02 14:02:56',1,1,0);

/*Table structure for table `setmeal_dish` */

DROP TABLE IF EXISTS `setmeal_dish`;

CREATE TABLE `setmeal_dish` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `setmeal_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '套餐id ',
  `dish_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '菜品id',
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '菜品名称 （冗余字段）',
  `price` decimal(10,2) DEFAULT NULL COMMENT '菜品原价（冗余字段）',
  `copies` int(11) NOT NULL COMMENT '份数',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_user` bigint(20) NOT NULL COMMENT '修改人',
  `is_deleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='套餐菜品关系';

/*Data for the table `setmeal_dish` */

insert  into `setmeal_dish`(`id`,`setmeal_id`,`dish_id`,`name`,`price`,`copies`,`sort`,`create_time`,`update_time`,`create_user`,`update_user`,`is_deleted`) values (1719958247202312194,'1719958247135203329','1719956958124920833','香菜拌皮蛋','1600.00',1,0,'2023-11-02 14:02:56','2023-11-02 14:02:56',1,1,0),(1719958247202312195,'1719958247135203329','1719937675785297922','米饭','400.00',1,0,'2023-11-02 14:02:56','2023-11-02 14:02:56',1,1,0),(1719958247202312196,'1719958247135203329','1719957927948668929','酸辣土豆丝','2200.00',1,0,'2023-11-02 14:02:56','2023-11-02 14:02:56',1,1,0);

/*Table structure for table `shopping_cart` */

DROP TABLE IF EXISTS `shopping_cart`;

CREATE TABLE `shopping_cart` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `image` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '图片',
  `user_id` bigint(20) NOT NULL COMMENT '主键',
  `dish_id` bigint(20) DEFAULT NULL COMMENT '菜品id',
  `setmeal_id` bigint(20) DEFAULT NULL COMMENT '套餐id',
  `dish_flavor` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '口味',
  `number` int(11) NOT NULL DEFAULT '1' COMMENT '数量',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='购物车';

/*Data for the table `shopping_cart` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `phone` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '手机号',
  `sex` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  `id_number` varchar(18) COLLATE utf8_bin DEFAULT NULL COMMENT '身份证号',
  `avatar` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '头像',
  `status` int(11) DEFAULT '0' COMMENT '状态 0:禁用，1:正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户信息';

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`phone`,`sex`,`id_number`,`avatar`,`status`) values (1718929869313593345,'启航用户','18838563912',NULL,NULL,NULL,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
