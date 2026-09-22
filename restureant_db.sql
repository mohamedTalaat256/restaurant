-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: restaurant_db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `acc_accounts`
--

DROP TABLE IF EXISTS `acc_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `acc_accounts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `allow_transaction` bit(1) NOT NULL,
  `balance` decimal(38,2) NOT NULL,
  `code` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `status` bit(1) NOT NULL,
  `type` enum('ASSET','EQUITY','EXPENSE','LIABILITY','REVENUE') COLLATE utf8mb4_general_ci NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK1ls48fnh1qt0ubh957x1v5an0` (`code`),
  KEY `FK8kdrqno7kpt4e1o17vrwttj5i` (`parent_id`),
  CONSTRAINT `FK8kdrqno7kpt4e1o17vrwttj5i` FOREIGN KEY (`parent_id`) REFERENCES `acc_accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acc_accounts`
--

LOCK TABLES `acc_accounts` WRITE;
/*!40000 ALTER TABLE `acc_accounts` DISABLE KEYS */;
INSERT INTO `acc_accounts` VALUES (1,_binary '\0',0.00,'1000','currentAssets',_binary '','ASSET','2026-08-11 11:56:38.000000',NULL),(2,_binary '',820.00,'1100','cashAndBank',_binary '','ASSET','2026-09-10 07:27:50.139649',1),(3,_binary '',190.00,'1200','foodAndBeverageInventory',_binary '','ASSET','2026-08-24 08:31:14.621391',1),(4,_binary '\0',0.00,'2000','currentLiabilities',_binary '','LIABILITY','2026-08-11 11:56:38.000000',NULL),(5,_binary '',0.00,'2100','accountsPayableSuppliers',_binary '','LIABILITY','2026-08-11 11:56:38.000000',4),(6,_binary '\0',0.00,'4000','restaurantRevenue',_binary '','REVENUE','2026-08-11 11:56:38.000000',NULL),(7,_binary '',1005.00,'4100','restaurantSalesRevenue',_binary '','REVENUE','2026-09-10 07:27:50.139649',6),(9,_binary '\0',0.00,'5000','cost of Goods Sold (COGS)',_binary '','EXPENSE','2026-08-11 11:56:38.000000',NULL),(10,_binary '',0.00,'5100','foodCost',_binary '','EXPENSE','2026-08-11 11:56:38.000000',9),(11,_binary '',0.00,'5200','beverageCost',_binary '','EXPENSE','2026-08-11 11:56:38.000000',9),(12,_binary '\0',0.00,'6000','operatingExpenses',_binary '','EXPENSE','2026-08-11 11:56:38.000000',NULL),(13,_binary '',0.00,'6100','salariesAndWages',_binary '','EXPENSE','2026-08-11 11:56:38.000000',12),(14,_binary '',0.00,'6200','restaurantRent',_binary '','EXPENSE','2026-08-11 11:56:38.000000',12),(15,_binary '',0.00,'6300','utilitiesElectricityWater',_binary '','EXPENSE','2026-08-11 11:56:38.000000',12),(16,_binary '',0.00,'6400','marketingAndAdvertising',_binary '','EXPENSE','2026-08-11 11:56:38.000000',12),(17,_binary '',0.00,'6500','kitchenAndDiningSupplies',_binary '','EXPENSE','2026-08-11 11:56:38.000000',12),(18,_binary '\0',0.00,'3000','equity',_binary '','EQUITY','2026-08-11 12:28:38.000000',NULL),(19,_binary '',0.00,'3100','retainedEarnings',_binary '','EQUITY','2026-08-11 12:28:38.000000',18),(20,_binary '',5.00,'21000001','مورد - الحاج اسماعيل',_binary '','LIABILITY','2026-08-11 09:51:55.563243',5);
/*!40000 ALTER TABLE `acc_accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acc_cost_centers`
--

DROP TABLE IF EXISTS `acc_cost_centers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `acc_cost_centers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `status` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhbqf33o0a5v0w47uisec18tv4` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acc_cost_centers`
--

LOCK TABLES `acc_cost_centers` WRITE;
/*!40000 ALTER TABLE `acc_cost_centers` DISABLE KEYS */;
INSERT INTO `acc_cost_centers` VALUES (1,'A','الفرع الرئيسي',_binary '');
/*!40000 ALTER TABLE `acc_cost_centers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acc_fiscal_periods`
--

DROP TABLE IF EXISTS `acc_fiscal_periods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `acc_fiscal_periods` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `end_date` date NOT NULL,
  `locked` bit(1) NOT NULL,
  `locked_at` datetime(6) DEFAULT NULL,
  `locked_by_username` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fiscal_month` int NOT NULL,
  `start_date` date NOT NULL,
  `fiscal_year` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKtr6dg1ttxi2k0s09xtf8wboj6` (`fiscal_year`,`fiscal_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acc_fiscal_periods`
--

LOCK TABLES `acc_fiscal_periods` WRITE;
/*!40000 ALTER TABLE `acc_fiscal_periods` DISABLE KEYS */;
/*!40000 ALTER TABLE `acc_fiscal_periods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acc_journal_entries`
--

DROP TABLE IF EXISTS `acc_journal_entries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `acc_journal_entries` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `entry_date` datetime(6) NOT NULL,
  `entry_number` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `reference` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `source` enum('MANUAL','SYSTEM') COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhueuyftuku2gf7ou10mtv3k2v` (`entry_number`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acc_journal_entries`
--

LOCK TABLES `acc_journal_entries` WRITE;
/*!40000 ALTER TABLE `acc_journal_entries` DISABLE KEYS */;
INSERT INTO `acc_journal_entries` VALUES (1,'phrase_accounting_purchase_cash 1','2026-08-11 09:35:14.634953','JV-2026-00001','1','SYSTEM'),(2,'phrase_accounting_purchase_credit 1','2026-08-11 09:51:55.479823','JV-2026-00002','1','SYSTEM'),(3,'phrase_accounting_supplier_payment 1','2026-08-11 09:51:55.563243','JV-2026-00003','1','SYSTEM'),(4,'phrase_accounting_purchase_cash 234','2026-08-24 07:14:16.302254','JV-2026-00004','234','SYSTEM'),(5,'إثبات شراء نقدي للمواد الخام للفاتورة رقم:  963','2026-08-24 08:20:14.674851','JV-2026-00005','963','SYSTEM'),(6,'إثبات شراء نقدي للمواد الخام للفاتورة رقم:  99','2026-08-24 08:31:14.621391','JV-2026-00006','99','SYSTEM'),(7,'إثبات مبيعات نقدية للفاتورة رقم:  ORD-20260702-0002','2026-09-09 09:32:43.471763','JV-2026-00007','ORD-20260702-0002','SYSTEM'),(8,'إثبات مبيعات نقدية للفاتورة رقم:  ORD-20260702-0003','2026-09-09 09:40:12.820760','JV-2026-00008','ORD-20260702-0003','SYSTEM'),(9,'إثبات مبيعات نقدية للفاتورة رقم:  ORD-20260713-0005','2026-09-09 09:40:23.148858','JV-2026-00009','ORD-20260713-0005','SYSTEM'),(10,'إثبات مبيعات نقدية للفاتورة رقم:  ORD-20260728-0007','2026-09-09 09:41:17.537941','JV-2026-00010','ORD-20260728-0007','SYSTEM'),(11,'إثبات مبيعات نقدية للفاتورة رقم:  ORD-20260909-0001','2026-09-09 09:54:25.130066','JV-2026-00011','ORD-20260909-0001','SYSTEM'),(12,'إثبات مبيعات نقدية للفاتورة رقم:  ORD-20260909-0001','2026-09-09 10:06:43.857725','JV-2026-00012','ORD-20260909-0001','SYSTEM'),(13,'إثبات مبيعات نقدية للفاتورة رقم:  ORD-20260910-0002','2026-09-10 07:27:42.374065','JV-2026-00013','ORD-20260910-0002','SYSTEM'),(14,'إثبات مبيعات نقدية للفاتورة رقم:  ORD-20260910-0003','2026-09-10 07:27:45.501090','JV-2026-00014','ORD-20260910-0003','SYSTEM'),(15,'إثبات مبيعات نقدية للفاتورة رقم:  ORD-20260910-0004','2026-09-10 07:27:48.322363','JV-2026-00015','ORD-20260910-0004','SYSTEM'),(16,'إثبات مبيعات نقدية للفاتورة رقم:  ORD-20260910-0005','2026-09-10 07:27:50.139649','JV-2026-00016','ORD-20260910-0005','SYSTEM');
/*!40000 ALTER TABLE `acc_journal_entries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acc_journal_items`
--

DROP TABLE IF EXISTS `acc_journal_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `acc_journal_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `credit` decimal(19,4) NOT NULL,
  `debit` decimal(19,4) NOT NULL,
  `account_id` bigint NOT NULL,
  `cost_center_id` bigint DEFAULT NULL,
  `journal_entry_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb773qsn2vp89o2w9mrm7vbnlw` (`account_id`),
  KEY `FKdcnjpe6k0kyhjjl5ws268i3ev` (`cost_center_id`),
  KEY `FK47t4vc2rb9qudr8xop1287obu` (`journal_entry_id`),
  CONSTRAINT `FK47t4vc2rb9qudr8xop1287obu` FOREIGN KEY (`journal_entry_id`) REFERENCES `acc_journal_entries` (`id`),
  CONSTRAINT `FKb773qsn2vp89o2w9mrm7vbnlw` FOREIGN KEY (`account_id`) REFERENCES `acc_accounts` (`id`),
  CONSTRAINT `FKdcnjpe6k0kyhjjl5ws268i3ev` FOREIGN KEY (`cost_center_id`) REFERENCES `acc_cost_centers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acc_journal_items`
--

LOCK TABLES `acc_journal_items` WRITE;
/*!40000 ALTER TABLE `acc_journal_items` DISABLE KEYS */;
INSERT INTO `acc_journal_items` VALUES (1,0.0000,10.0000,3,NULL,1),(2,10.0000,0.0000,2,NULL,1),(3,0.0000,10.0000,3,NULL,2),(4,10.0000,0.0000,20,NULL,2),(5,0.0000,5.0000,20,NULL,3),(6,5.0000,0.0000,2,NULL,3),(7,0.0000,80.0000,3,NULL,4),(8,80.0000,0.0000,2,NULL,4),(9,0.0000,30.0000,3,NULL,5),(10,30.0000,0.0000,2,NULL,5),(11,0.0000,60.0000,3,NULL,6),(12,60.0000,0.0000,2,NULL,6),(13,0.0000,210.0000,2,NULL,7),(14,210.0000,0.0000,7,NULL,7),(15,0.0000,175.0000,2,NULL,8),(16,175.0000,0.0000,7,NULL,8),(17,0.0000,100.0000,2,NULL,9),(18,100.0000,0.0000,7,NULL,9),(19,0.0000,50.0000,2,NULL,10),(20,50.0000,0.0000,7,NULL,10),(21,0.0000,50.0000,2,NULL,11),(22,50.0000,0.0000,7,NULL,11),(23,0.0000,50.0000,2,NULL,12),(24,50.0000,0.0000,7,NULL,12),(25,0.0000,200.0000,2,NULL,13),(26,200.0000,0.0000,7,NULL,13),(27,0.0000,50.0000,2,NULL,14),(28,50.0000,0.0000,7,NULL,14),(29,0.0000,100.0000,2,NULL,15),(30,100.0000,0.0000,7,NULL,15),(31,0.0000,20.0000,2,NULL,16),(32,20.0000,0.0000,7,NULL,16);
/*!40000 ALTER TABLE `acc_journal_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application_settings`
--

DROP TABLE IF EXISTS `application_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `application_settings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `application_direction` enum('LTR','RTL') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `application_title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `closing_time` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `date_format` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `discount_percentage` double DEFAULT NULL,
  `discount_type` enum('FIXED_AMOUNT','PERCENTAGE') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `footer_text` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `icon` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `logo` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `opening_time` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `powered_by_text` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `service_charge_type` enum('FIXED_AMOUNT','PERCENTAGE') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `store_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `tax_number` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tax_percentage` double DEFAULT NULL,
  `timezone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `currency_id` bigint DEFAULT NULL,
  `language_code` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkd8al9lyj3ic3xykxxl1n9thq` (`currency_id`),
  KEY `FKicghgfe64mkygmy1oe6wycg13` (`language_code`),
  CONSTRAINT `FKicghgfe64mkygmy1oe6wycg13` FOREIGN KEY (`language_code`) REFERENCES `languages` (`language_code`),
  CONSTRAINT `FKkd8al9lyj3ic3xykxxl1n9thq` FOREIGN KEY (`currency_id`) REFERENCES `currencies` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_settings`
--

LOCK TABLES `application_settings` WRITE;
/*!40000 ALTER TABLE `application_settings` DISABLE KEYS */;
INSERT INTO `application_settings` VALUES (1,'12 شارع البحر بجوار بنك مصر - طنطا','RTL','أهل الكرم','05:00','werwer',5,'PERCENTAGE','نورتنا و شرفتنا','/files/settings/f4bf6f10-4a39-44a2-9e6c-fc62a54d3072.ico','/files/settings/b8ec7f61-32c8-41b7-ab12-5e2438c12b3c.png','09:00','234234234','sdfsdfdsf','PERCENTAGE','أهل الكرم','234234',14,'werwer',1,'ar');
/*!40000 ALTER TABLE `application_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cash_counters`
--

DROP TABLE IF EXISTS `cash_counters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cash_counters` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `number` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cash_counters`
--

LOCK TABLES `cash_counters` WRITE;
/*!40000 ALTER TABLE `cash_counters` DISABLE KEYS */;
INSERT INTO `cash_counters` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `cash_counters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cash_registers`
--

DROP TABLE IF EXISTS `cash_registers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cash_registers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `closing_balance` double DEFAULT NULL,
  `closing_note` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `closing_time` datetime(6) DEFAULT NULL,
  `opening_balance` double NOT NULL,
  `opening_note` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `opening_time` datetime(6) NOT NULL,
  `status` bit(1) NOT NULL,
  `cash_counter_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKppikjrajljhu3j73fy5rkh6hv` (`cash_counter_id`),
  KEY `FKjsgmchr83413apfdvs19tbohv` (`user_id`),
  CONSTRAINT `FKjsgmchr83413apfdvs19tbohv` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKppikjrajljhu3j73fy5rkh6hv` FOREIGN KEY (`cash_counter_id`) REFERENCES `cash_counters` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cash_registers`
--

LOCK TABLES `cash_registers` WRITE;
/*!40000 ALTER TABLE `cash_registers` DISABLE KEYS */;
INSERT INTO `cash_registers` VALUES (1,0,'dfgfdg','2026-06-19 22:07:17.112080',0,'اول وردية و الرصيد صفر','2026-06-19 21:12:01.784555',_binary '\0',1,1),(2,50,'dfgfdg','2026-07-28 10:31:48.505204',0,'اول وردية و الرصيد صفر','2026-06-19 22:07:48.047390',_binary '\0',1,1),(3,NULL,NULL,NULL,0,'اول وردية و الرصيد صفر','2026-07-28 10:32:01.495033',_binary '',1,1);
/*!40000 ALTER TABLE `cash_registers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `currencies`
--

DROP TABLE IF EXISTS `currencies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `currencies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `exchange_rate_to_usd` double NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `symbol` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `currencies`
--

LOCK TABLES `currencies` WRITE;
/*!40000 ALTER TABLE `currencies` DISABLE KEYS */;
INSERT INTO `currencies` VALUES (1,'EGP',0.019,'جنيه مصري','ج.م');
/*!40000 ALTER TABLE `currencies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_types`
--

DROP TABLE IF EXISTS `customer_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_types` (
  `type` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ordering` int NOT NULL,
  `status` bit(1) NOT NULL,
  PRIMARY KEY (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_types`
--

LOCK TABLES `customer_types` WRITE;
/*!40000 ALTER TABLE `customer_types` DISABLE KEYS */;
INSERT INTO `customer_types` VALUES ('ONLINE_CUSTOMER','عميل اونلاين',1,_binary ''),('TAKEAWAY_CUSTOMER','تيك اواي',3,_binary ''),('THIRD_PARTY_CUSTOMER','عمبل طرف ثالث',2,_binary ''),('WALK_IN_CUSTOMER','عميل مسجل بالنظام',0,_binary '');
/*!40000 ALTER TABLE `customer_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `allow_credit` bit(1) NOT NULL,
  `credit_limit` decimal(19,2) DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `favorite_delivery_address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `status` bit(1) NOT NULL,
  `account_id` bigint DEFAULT NULL,
  `customer_type` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4njtl3pvfduamug24b9qmpy0x` (`account_id`),
  KEY `FKmgblfdq45n8rbe7vwqdrbf9h8` (`customer_type`),
  CONSTRAINT `FK10so5fr8uc88x8dt3v5ex80m3` FOREIGN KEY (`account_id`) REFERENCES `acc_accounts` (`id`),
  CONSTRAINT `FKmgblfdq45n8rbe7vwqdrbf9h8` FOREIGN KEY (`customer_type`) REFERENCES `customer_types` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'الجيزة',_binary '\0',0.00,'mohamed@gmail.com','كافية عايدة','بشمهندس محمد','123456','0985356445456',_binary '',NULL,'ONLINE_CUSTOMER');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery_details`
--

DROP TABLE IF EXISTS `delivery_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  `vehicle_number` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `vehicle_type` enum('BICYCLE','CAR','MOTORCYCLE','ON_FOOT','SCOOTER','VAN') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKkk9jm4pw1mjwsv2u4815tcihu` (`user_id`),
  KEY `FKr1xfocwhpqov0tcgwcphxjpkb` (`created_by`),
  KEY `FK2c5gv41hcqj1vbcxmqhme9e14` (`deleted_by`),
  KEY `FKbrtxa1911e39w0g9tddv1ijmb` (`updated_by`),
  CONSTRAINT `FK2c5gv41hcqj1vbcxmqhme9e14` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FK8ejfiy60ajsnafw729kyiwqws` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKbrtxa1911e39w0g9tddv1ijmb` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKr1xfocwhpqov0tcgwcphxjpkb` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery_details`
--

LOCK TABLES `delivery_details` WRITE;
/*!40000 ALTER TABLE `delivery_details` DISABLE KEYS */;
INSERT INTO `delivery_details` VALUES (1,NULL,NULL,'2026-09-21 06:29:03.191656','0109854687',_binary '','98746','SCOOTER',1,NULL,1,7);
/*!40000 ALTER TABLE `delivery_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `floors`
--

DROP TABLE IF EXISTS `floors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `floors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `floors`
--

LOCK TABLES `floors` WRITE;
/*!40000 ALTER TABLE `floors` DISABLE KEYS */;
INSERT INTO `floors` VALUES (1,'الدور الارضي'),(2,'الدور الثاني'),(3,'VIP');
/*!40000 ALTER TABLE `floors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingredients`
--

DROP TABLE IF EXISTS `ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingredients` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `min_stock_quantity` double NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `status` bit(1) NOT NULL,
  `stock_quantity` double NOT NULL,
  `uom_id` bigint DEFAULT NULL,
  `average_cost` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnh9oxckxnf8q0ivos3l0b845b` (`uom_id`),
  CONSTRAINT `FKnh9oxckxnf8q0ivos3l0b845b` FOREIGN KEY (`uom_id`) REFERENCES `unit_of_measurements` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredients`
--

LOCK TABLES `ingredients` WRITE;
/*!40000 ALTER TABLE `ingredients` DISABLE KEYS */;
INSERT INTO `ingredients` VALUES (1,5,'زيت',_binary '',11,1,4.545454545454546),(2,2,'ملح',_binary '',0,1,0),(3,5,'دقيق',_binary '',0,1,0),(4,0,'سمنة',_binary '',2,1,45),(5,0,'زيت زيتون',_binary '',0,1,0),(6,0,'فلفل',_binary '',1,1,30),(7,0,'طماطم',_binary '',0,1,0),(8,0,'جزر',_binary '',0,1,0),(9,0,'عيش توست',_binary '',0,1,0);
/*!40000 ALTER TABLE `ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_categories`
--

DROP TABLE IF EXISTS `item_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `image` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_offer` bit(1) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `offer_end_date` date DEFAULT NULL,
  `offer_start_date` date DEFAULT NULL,
  `position` int DEFAULT NULL,
  `status` bit(1) NOT NULL,
  `created_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqpla966nm1l9y9axw1cte3pss` (`created_by`),
  KEY `FKb49d017rjk9kpuwq2bduy3rx0` (`deleted_by`),
  KEY `FK2xwgiccgb41hwq1bbq03rnjik` (`updated_by`),
  CONSTRAINT `FK2xwgiccgb41hwq1bbq03rnjik` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKb49d017rjk9kpuwq2bduy3rx0` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKqpla966nm1l9y9axw1cte3pss` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_categories`
--

LOCK TABLES `item_categories` WRITE;
/*!40000 ALTER TABLE `item_categories` DISABLE KEYS */;
INSERT INTO `item_categories` VALUES (1,'2026-06-09 06:00:34.809151',NULL,'2026-06-09 06:00:34.809151','/files/food/7f78f329-78a0-40eb-8385-f2f57960c159.webp',_binary '\0','عصائر',NULL,NULL,1,_binary '',1,NULL,1),(2,'2026-06-09 06:06:08.156545',NULL,'2026-06-09 06:06:08.156545','/files/food/4c418fa4-078d-4ca7-bd01-d6a71a65885d.png',_binary '\0','مشروبات ساخنة',NULL,NULL,1,_binary '',1,NULL,1),(3,'2026-06-09 11:52:24.758766',NULL,'2026-06-09 11:52:24.758766','/files/food/9fd569a0-7210-46fc-b274-05350fd3399a.jpg',_binary '\0','شاورما',NULL,NULL,1,_binary '',1,NULL,1),(4,'2026-06-13 12:43:37.634093',NULL,'2026-06-13 12:43:37.634093','/files/food/946438af-dcc5-45c2-a090-b2a170c44ecf.jpg',_binary '\0','ساندوتشات',NULL,NULL,2,_binary '',1,NULL,1);
/*!40000 ALTER TABLE `item_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_food_add_ons`
--

DROP TABLE IF EXISTS `item_food_add_ons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_food_add_ons` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `price` double NOT NULL,
  `status` bit(1) NOT NULL,
  `created_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK99iuetbwmnnke5049cq5ayvqo` (`created_by`),
  KEY `FK9pn07ua2svdn16nnxf06uohmg` (`deleted_by`),
  KEY `FKcg9nl5egy1dkj822hv4tquefl` (`updated_by`),
  CONSTRAINT `FK99iuetbwmnnke5049cq5ayvqo` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FK9pn07ua2svdn16nnxf06uohmg` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKcg9nl5egy1dkj822hv4tquefl` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_food_add_ons`
--

LOCK TABLES `item_food_add_ons` WRITE;
/*!40000 ALTER TABLE `item_food_add_ons` DISABLE KEYS */;
INSERT INTO `item_food_add_ons` VALUES (1,'2026-06-09 11:48:08.404022',NULL,'2026-06-09 11:48:08.404022','كاتشب',10,_binary '',1,NULL,1),(2,'2026-06-09 11:48:18.026905',NULL,'2026-06-09 11:48:18.026905','تومية',15,_binary '',1,NULL,1),(3,'2026-06-09 11:48:37.851580',NULL,'2026-06-09 11:48:37.851580','صوص شوكالاته',10,_binary '',1,NULL,1),(4,'2026-06-13 15:44:43.343393',NULL,'2026-06-13 15:44:43.343393','مخلل',5,_binary '',1,NULL,1);
/*!40000 ALTER TABLE `item_food_add_ons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_food_add_ons_associations`
--

DROP TABLE IF EXISTS `item_food_add_ons_associations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_food_add_ons_associations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `item_food_id` bigint DEFAULT NULL,
  `item_food_add_ons_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmqcsvnomtxf1sumuy8ik43mc3` (`created_by`),
  KEY `FK2ca71ctspt4cxk27hrcyxd335` (`deleted_by`),
  KEY `FKl8nk5sb689kwwjctrvo1517ws` (`updated_by`),
  KEY `FK7qd7msk971orlxyhcociv5rgx` (`item_food_id`),
  KEY `FK29ju01hf3iwlxxx0qc74fjtvd` (`item_food_add_ons_id`),
  CONSTRAINT `FK29ju01hf3iwlxxx0qc74fjtvd` FOREIGN KEY (`item_food_add_ons_id`) REFERENCES `item_food_add_ons` (`id`),
  CONSTRAINT `FK2ca71ctspt4cxk27hrcyxd335` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FK7qd7msk971orlxyhcociv5rgx` FOREIGN KEY (`item_food_id`) REFERENCES `item_foods` (`id`),
  CONSTRAINT `FKl8nk5sb689kwwjctrvo1517ws` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKmqcsvnomtxf1sumuy8ik43mc3` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_food_add_ons_associations`
--

LOCK TABLES `item_food_add_ons_associations` WRITE;
/*!40000 ALTER TABLE `item_food_add_ons_associations` DISABLE KEYS */;
INSERT INTO `item_food_add_ons_associations` VALUES (1,'2026-06-09 12:08:28.866505',NULL,'2026-06-09 12:08:28.866505',1,NULL,1,3,2),(2,'2026-06-09 12:12:37.467405',NULL,'2026-06-09 12:12:37.467405',1,NULL,1,4,2),(4,'2026-06-13 12:25:46.419742',NULL,'2026-06-13 12:25:46.419742',1,NULL,1,3,1),(5,'2026-06-13 12:44:41.924429',NULL,'2026-06-13 12:44:41.924429',1,NULL,1,5,1),(6,'2026-06-13 15:44:54.762987',NULL,'2026-06-13 15:44:54.762987',1,NULL,1,4,4);
/*!40000 ALTER TABLE `item_food_add_ons_associations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_food_variants`
--

DROP TABLE IF EXISTS `item_food_variants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_food_variants` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `price` double NOT NULL,
  `created_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `item_food_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2jmbgfugv581sttqywrcp2fix` (`created_by`),
  KEY `FK755jfrlps3lucbe33b2t4gvoe` (`deleted_by`),
  KEY `FKep26s7rkht6qdd68a1iedobnm` (`updated_by`),
  KEY `FK3qwblig71mjdr75n059r32w2k` (`item_food_id`),
  CONSTRAINT `FK2jmbgfugv581sttqywrcp2fix` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FK3qwblig71mjdr75n059r32w2k` FOREIGN KEY (`item_food_id`) REFERENCES `item_foods` (`id`),
  CONSTRAINT `FK755jfrlps3lucbe33b2t4gvoe` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKep26s7rkht6qdd68a1iedobnm` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_food_variants`
--

LOCK TABLES `item_food_variants` WRITE;
/*!40000 ALTER TABLE `item_food_variants` DISABLE KEYS */;
INSERT INTO `item_food_variants` VALUES (1,'2026-06-09 12:01:58.614510',NULL,'2026-06-09 12:01:58.614510','صغير',50,1,NULL,1,3),(2,'2026-06-09 12:03:28.749077',NULL,'2026-06-09 12:03:50.796349','كبير',100,1,NULL,1,3),(3,'2026-06-09 12:03:45.576159',NULL,'2026-06-09 12:03:45.576159','وسط',75,1,NULL,1,3),(6,'2026-06-13 12:45:50.190612',NULL,'2026-06-13 12:45:50.190612','صغير',50,1,NULL,1,5),(7,'2026-06-13 12:45:56.440965',NULL,'2026-06-13 12:45:56.440965','وسط',75,1,NULL,1,5),(8,'2026-06-13 12:46:01.460837',NULL,'2026-06-13 12:46:01.460837','كبير',100,1,NULL,1,5),(9,'2026-06-19 17:42:33.013341',NULL,'2026-06-19 17:42:33.013341','كبير',100,1,NULL,1,4),(10,'2026-06-19 17:42:39.873341',NULL,'2026-06-19 17:42:39.873341','وسط',75,1,NULL,1,4),(11,'2026-06-19 17:42:46.830087',NULL,'2026-06-19 17:42:46.830087','صغير',50,1,NULL,1,4),(12,'2026-06-19 17:43:26.005207',NULL,'2026-06-19 17:43:26.005207','عادي',20,1,NULL,1,2),(13,'2026-06-19 17:43:39.853602',NULL,'2026-06-19 17:43:39.853602','نعناع',25,1,NULL,1,2),(14,'2026-06-19 17:46:07.061997',NULL,'2026-06-19 17:46:07.061997','وسط',35,1,NULL,1,1);
/*!40000 ALTER TABLE `item_food_variants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_foods`
--

DROP TABLE IF EXISTS `item_foods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_foods` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `component` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cooked_time` int DEFAULT NULL,
  `descrip` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `image` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_custom_qty` bit(1) DEFAULT NULL,
  `is_group` bit(1) DEFAULT NULL,
  `is_special` bit(1) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `note` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `offer_end_date` date DEFAULT NULL,
  `offer_is_available` bit(1) DEFAULT NULL,
  `offer_rate` double DEFAULT NULL,
  `offer_start_date` date DEFAULT NULL,
  `position` int DEFAULT NULL,
  `product_vat` double DEFAULT NULL,
  `status` bit(1) NOT NULL,
  `tax0` double DEFAULT NULL,
  `tax1` double DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `kitchen_id` bigint DEFAULT NULL,
  `menu_type_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKl8kqkp9t3j05b7ua9xcehpoud` (`created_by`),
  KEY `FKgf2oqrj3up5xfhtmllpcs16va` (`deleted_by`),
  KEY `FK7kf5b58l5k58ufrbtqjsx2sab` (`updated_by`),
  KEY `FKc3nhkntln92ay2x7povx20o2x` (`category_id`),
  KEY `FK31t6l5vvmdjy5qr1pgwldf9a1` (`kitchen_id`),
  KEY `FKic89ib8vig98fhfdm5by0te64` (`menu_type_id`),
  CONSTRAINT `FK31t6l5vvmdjy5qr1pgwldf9a1` FOREIGN KEY (`kitchen_id`) REFERENCES `kitchens` (`id`),
  CONSTRAINT `FK7kf5b58l5k58ufrbtqjsx2sab` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKc3nhkntln92ay2x7povx20o2x` FOREIGN KEY (`category_id`) REFERENCES `item_categories` (`id`),
  CONSTRAINT `FKgf2oqrj3up5xfhtmllpcs16va` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKic89ib8vig98fhfdm5by0te64` FOREIGN KEY (`menu_type_id`) REFERENCES `menu_types` (`id`),
  CONSTRAINT `FKl8kqkp9t3j05b7ua9xcehpoud` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_foods`
--

LOCK TABLES `item_foods` WRITE;
/*!40000 ALTER TABLE `item_foods` DISABLE KEYS */;
INSERT INTO `item_foods` VALUES (1,'2026-06-09 06:51:16.567923',NULL,'2026-07-02 13:03:04.123277','',NULL,'عصير ليمون','/files/food/87e9aeeb-8521-4097-aa23-f38157a4968e.png',_binary '\0',_binary '\0',_binary '\0','عصير ليمون','',NULL,_binary '\0',NULL,NULL,NULL,NULL,_binary '',NULL,NULL,1,NULL,1,1,2,NULL),(2,'2026-06-09 06:54:48.048148',NULL,'2026-07-02 13:03:14.887647','',NULL,'','/files/food/fc761834-9766-4d56-97ac-365f76401607.png',_binary '\0',_binary '\0',_binary '\0','شاي','',NULL,_binary '\0',NULL,NULL,NULL,NULL,_binary '',NULL,NULL,1,NULL,1,2,2,2),(3,'2026-06-09 11:52:48.205608',NULL,'2026-06-09 11:52:56.640486','',NULL,'','/files/food/bde42644-a089-4183-8b04-80c55337a96f.jpg',_binary '\0',_binary '\0',_binary '\0','شاورما فراخ','',NULL,_binary '\0',NULL,NULL,NULL,NULL,_binary '',NULL,NULL,1,NULL,1,3,1,NULL),(4,'2026-06-09 12:12:17.815547',NULL,'2026-06-09 12:12:48.589205','',NULL,'','/files/food/ead14c21-aa04-411f-8798-7a118c9e8006.jpg',_binary '\0',_binary '\0',_binary '\0','شاورمة  لحمة','',NULL,_binary '\0',NULL,NULL,NULL,NULL,_binary '',NULL,NULL,1,NULL,1,3,1,NULL),(5,'2026-06-13 12:44:34.737523',NULL,'2026-06-13 12:44:34.737523','',10,'برجر بيف','/files/food/3d856adc-3d58-43e1-82ec-f85fdd75ec59.jpg',_binary '\0',_binary '\0',_binary '\0','برجر بيف','',NULL,_binary '\0',NULL,NULL,1,NULL,_binary '',NULL,NULL,1,NULL,1,4,1,3);
/*!40000 ALTER TABLE `item_foods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kitchen_order_items`
--

DROP TABLE IF EXISTS `kitchen_order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kitchen_order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `notes` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` enum('ACCEPTED','PENDING','PREPARING','READY','REJECTED','SERVED') COLLATE utf8mb4_general_ci NOT NULL,
  `kitchen_order_id` bigint NOT NULL,
  `order_item_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk4ldsq7th90qtvu15hnbcfc06` (`kitchen_order_id`),
  KEY `FKegu27qmpshet0hlf7bw67v0ob` (`order_item_id`),
  CONSTRAINT `FKegu27qmpshet0hlf7bw67v0ob` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`),
  CONSTRAINT `FKk4ldsq7th90qtvu15hnbcfc06` FOREIGN KEY (`kitchen_order_id`) REFERENCES `kitchen_orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kitchen_order_items`
--

LOCK TABLES `kitchen_order_items` WRITE;
/*!40000 ALTER TABLE `kitchen_order_items` DISABLE KEYS */;
INSERT INTO `kitchen_order_items` VALUES (1,NULL,'SERVED',1,1),(2,NULL,'SERVED',2,5),(3,NULL,'PENDING',3,6),(4,NULL,'PENDING',3,7);
/*!40000 ALTER TABLE `kitchen_order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kitchen_orders`
--

DROP TABLE IF EXISTS `kitchen_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kitchen_orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `accepted_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `notes` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `prepared_at` datetime(6) DEFAULT NULL,
  `ready_at` datetime(6) DEFAULT NULL,
  `rejected_at` datetime(6) DEFAULT NULL,
  `status` enum('ACCEPTED','PENDING','PREPARING','READY','REJECTED') COLLATE utf8mb4_general_ci NOT NULL,
  `kitchen_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5m7gffyc5s1cqgfpew6nuqxwf` (`kitchen_id`),
  KEY `FKjnaitjo15d1kbek78hjb0vfpy` (`order_id`),
  CONSTRAINT `FK5m7gffyc5s1cqgfpew6nuqxwf` FOREIGN KEY (`kitchen_id`) REFERENCES `kitchens` (`id`),
  CONSTRAINT `FKjnaitjo15d1kbek78hjb0vfpy` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kitchen_orders`
--

LOCK TABLES `kitchen_orders` WRITE;
/*!40000 ALTER TABLE `kitchen_orders` DISABLE KEYS */;
INSERT INTO `kitchen_orders` VALUES (1,'2026-09-09 09:55:10.785598','2026-09-09 09:55:04.378227',NULL,'2026-09-09 09:55:20.751425','2026-09-09 09:55:23.164879',NULL,'READY',1,1),(2,'2026-09-10 07:15:23.047462','2026-09-10 07:04:32.671758',NULL,'2026-09-10 07:15:50.189655','2026-09-10 07:15:52.045313',NULL,'READY',2,5),(3,NULL,'2026-09-14 09:47:01.811932',NULL,NULL,NULL,NULL,'PENDING',1,6);
/*!40000 ALTER TABLE `kitchen_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kitchens`
--

DROP TABLE IF EXISTS `kitchens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kitchens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ip_address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `port` int DEFAULT NULL,
  `status` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kitchens`
--

LOCK TABLES `kitchens` WRITE;
/*!40000 ALTER TABLE `kitchens` DISABLE KEYS */;
INSERT INTO `kitchens` VALUES (1,'192.168.1.26','المطبخ الرئيسي',4200,_binary ''),(2,'192.168.1.26','المشروبات',4200,_binary '');
/*!40000 ALTER TABLE `kitchens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `language_translations`
--

DROP TABLE IF EXISTS `language_translations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `language_translations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `translation_key` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `translation_value` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `language_code` varchar(10) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsu33ocnknwpxxoeir7d53l6nu` (`language_code`),
  CONSTRAINT `FKsu33ocnknwpxxoeir7d53l6nu` FOREIGN KEY (`language_code`) REFERENCES `languages` (`language_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1506 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `language_translations`
--

LOCK TABLES `language_translations` WRITE;
/*!40000 ALTER TABLE `language_translations` DISABLE KEYS */;
INSERT INTO `language_translations` VALUES (1,'label_welcome','مرحبًا بك في نظام إدارة المطاعم','ar'),(2,'label_sign_in','تسجيل الدخول إلى حسابك','ar'),(3,'label_id','المعرف','ar'),(4,'label_error','خطأ','ar'),(5,'label_success','نجاح','ar'),(6,'label_warning','تحذير','ar'),(7,'label_info','معلومات','ar'),(8,'label_inactive','غير نشط','ar'),(9,'label_clear','مسح','ar'),(10,'label_select_image','اختر صورة','ar'),(11,'label_successful','ناجح','ar'),(12,'label_failed','فشل','ar'),(13,'label_edit','تعديل','ar'),(14,'label_delete','حذف','ar'),(15,'label_add','إضافة','ar'),(16,'label_pos','نقطة البيع','ar'),(17,'label_manage_users','إدارة المستخدمين','ar'),(18,'label_user_details','تفاصيل المستخدم','ar'),(19,'label_users','المستخدمون','ar'),(20,'label_user','المستخدم','ar'),(21,'label_email','البريد الإلكتروني','ar'),(22,'label_password','كلمة المرور','ar'),(23,'label_first_name','الاسم الأول','ar'),(24,'label_last_name','اسم العائلة','ar'),(25,'label_phone','الهاتف','ar'),(26,'label_image','الصورة','ar'),(27,'label_about','حول','ar'),(28,'label_is_admin','مشرف','ar'),(29,'label_is_monitor','مراقب','ar'),(30,'label_counter','عداد','ar'),(31,'label_roles','الأدوار','ar'),(32,'label_manage_roles','إدارة الأدوار','ar'),(33,'label_add_role','إضافة دور','ar'),(34,'label_create_role','إنشاء دور','ar'),(35,'label_create_system_role','إنشاء دور نظام','ar'),(36,'label_edit_system_role','تعديل دور نظام','ar'),(37,'label_edit_role','تعديل دور','ar'),(38,'label_delete_role','حذف دور','ar'),(39,'label_view_role','عرض دور','ar'),(40,'label_role_details','تفاصيل الدور','ar'),(41,'label_itemmanage','إدارة العناصر','ar'),(42,'label_units','الوحدات','ar'),(43,'label_purchase','فاتورة مشتريات','ar'),(44,'label_setting','إعدادات','ar'),(45,'label_ordermanage','إدارة الطلبات','ar'),(46,'label_accounts','الحسابات','ar'),(47,'label_hrm','إدارة الموارد البشرية','ar'),(48,'label_report','التقارير','ar'),(49,'label_category','الفئة','ar'),(50,'label_supplier','المورد','ar'),(51,'label_production','الإنتاج','ar'),(52,'label_reservation','الحجوزات','ar'),(53,'label_parent_menu','القائمة الرئيسية','ar'),(54,'label_can_create','يمكن الإنشاء','ar'),(55,'label_can_edit','يمكن التعديل','ar'),(56,'label_can_delete','يمكن الحذف','ar'),(57,'label_can_view','يمكن العرض','ar'),(58,'label_can_read','يمكن القراءة','ar'),(59,'label_add_user','إضافة مستخدم','ar'),(60,'label_edit_user','تعديل مستخدم','ar'),(61,'label_delete_user','حذف مستخدم','ar'),(62,'label_view_user','عرض مستخدم','ar'),(63,'label_export','تصدير','ar'),(64,'label_search','بحث','ar'),(65,'label_actions','إجراءات','ar'),(66,'label_save','حفظ','ar'),(67,'label_cancel','إلغاء','ar'),(68,'label_confirm','تأكيد','ar'),(69,'label_yes','نعم','ar'),(70,'label_no','لا','ar'),(71,'label_active','نشط','ar'),(72,'label_status','الحالة','ar'),(73,'label_confrim','تأكيد','ar'),(74,'label_role','الدور','ar'),(75,'label_admin','مشرف','ar'),(76,'label_customer','عميل','ar'),(77,'label_name','الاسم','ar'),(78,'label_description','الوصف','ar'),(79,'label_price','السعر','ar'),(80,'label_login','تسجيل الدخول','ar'),(81,'label_logout','تسجيل الخروج','ar'),(82,'label_dashboard','لوحة التحكم','ar'),(83,'label_profile','الملف الشخصي','ar'),(84,'label_users_and_permissions','المستخدمون والصلاحيات','ar'),(85,'confirm_delete_role','هل أنت متأكد أنك تريد حذف هذا الدور؟','ar'),(86,'msg_role_created','تم إنشاء الدور بنجاح','ar'),(87,'msg_role_updated','تم تحديث الدور بنجاح','ar'),(88,'msg_role_deleted','تم حذف الدور بنجاح','ar'),(89,'label_modules','الوحدات','ar'),(90,'msg_permission_updated','تم تحديث الصلاحيات بنجاح','ar'),(91,'label_settings','الإعدادات','ar'),(92,'label_purchases','المشتريات','ar'),(93,'label_sales','المبيعات','ar'),(94,'label_reservations','الحجوزات','ar'),(95,'label_units_of_measurment','وحدات القياس','ar'),(96,'confirm_delete_uom','هل أنت متأكد أنك تريد حذف وحدة القياس هذه؟','ar'),(97,'label_add_uom','إضافة وحدة قياس','ar'),(98,'label_manage_uoms','إدارة وحدات القياس','ar'),(99,'label_uom_details','تفاصيل وحدة القياس','ar'),(100,'label_short_name','الاسم المختصر','ar'),(101,'msg_uom_deleted','تم حذف وحدة القياس بنجاح','ar'),(102,'msg_uom_updated','تم تحديث وحدة القياس بنجاح','ar'),(103,'msg_uom_created','تم إنشاء وحدة القياس بنجاح','ar'),(104,'msg_uom_fetched','تم جلب وحدات القياس بنجاح','ar'),(105,'msg_uom_added','تم إضافة وحدة القياس بنجاح','ar'),(106,'label_floors','الطوابق','ar'),(107,'label_floor','الطابق','ar'),(108,'label_add_floor','إضافة طابق','ar'),(109,'label_manage_floors','إدارة الطوابق','ar'),(110,'label_floor_details','تفاصيل الطابق','ar'),(111,'confirm_delete_floor','هل أنت متأكد أنك تريد حذف هذا الطابق؟','ar'),(112,'msg_floor_added','تم إضافة الطابق بنجاح','ar'),(113,'msg_floor_fetched','تم جلب الطوابق بنجاح','ar'),(114,'msg_floor_created','تم إنشاء الطابق بنجاح','ar'),(115,'msg_floor_updated','تم تحديث الطابق بنجاح','ar'),(116,'msg_floor_deleted','تم حذف الطابق بنجاح','ar'),(117,'label_tables','الطاولات','ar'),(118,'label_table','الطاولة','ar'),(119,'label_add_table','إضافة طاولة','ar'),(120,'label_manage_tables','إدارة الطاولات','ar'),(121,'label_table_details','تفاصيل الطاولة','ar'),(122,'label_capacity','السعة','ar'),(123,'label_icon','الأيقونة','ar'),(124,'confirm_delete_table','هل أنت متأكد أنك تريد حذف هذه الطاولة؟','ar'),(125,'label_kitchens','المطابخ','ar'),(126,'label_kitchen','المطبخ','ar'),(127,'label_add_kitchen','إضافة مطبخ','ar'),(128,'label_manage_kitchens','إدارة المطابخ','ar'),(129,'label_kitchen_details','تفاصيل المطبخ','ar'),(130,'label_ip_address','عنوان IP','ar'),(131,'label_port','المنفذ','ar'),(132,'confirm_delete_kitchen','هل أنت متأكد أنك تريد حذف هذا المطبخ؟','ar'),(133,'msg_kitchen_added','تم إضافة المطبخ بنجاح','ar'),(134,'msg_kitchen_fetched','تم جلب المطابخ بنجاح','ar'),(135,'msg_kitchen_created','تم إنشاء المطبخ بنجاح','ar'),(136,'msg_kitchen_updated','تم تحديث المطبخ بنجاح','ar'),(137,'msg_kitchen_deleted','تم حذف المطبخ بنجاح','ar'),(138,'msg_table_added','تم إضافة الطاولة بنجاح','ar'),(139,'msg_table_fetched','تم جلب الطاولات بنجاح','ar'),(140,'msg_table_created','تم إنشاء الطاولة بنجاح','ar'),(141,'msg_table_updated','تم تحديث الطاولة بنجاح','ar'),(142,'msg_table_deleted','تم حذف الطاولة بنجاح','ar'),(143,'label_item_categories','فئات العناصر','ar'),(144,'label_item_category','فئة العنصر','ar'),(145,'label_add_item_category','إضافة فئة عنصر','ar'),(146,'label_manage_item_categories','إدارة فئات العناصر','ar'),(147,'label_item_category_details','تفاصيل فئة العنصر','ar'),(148,'label_position','الموضع','ar'),(149,'label_is_offer','عرض','ar'),(150,'label_offer_start_date','تاريخ بدء العرض','ar'),(151,'label_offer_end_date','تاريخ انتهاء العرض','ar'),(152,'confirm_delete_item_category','هل أنت متأكد أنك تريد حذف فئة العنصر هذه؟','ar'),(153,'label_menu_types','أنواع القوائم','ar'),(154,'label_menu_type','نوع القائمة','ar'),(155,'label_add_menu_type','إضافة نوع قائمة','ar'),(156,'label_manage_menu_types','إدارة أنواع القوائم','ar'),(157,'label_menu_type_details','تفاصيل نوع القائمة','ar'),(158,'confirm_delete_menu_type','هل أنت متأكد أنك تريد حذف نوع القائمة هذا؟','ar'),(159,'label_item_management','إدارة العناصر','ar'),(160,'label_food_management','إدارة الطعام','ar'),(161,'label_item_foods','أطعمة العناصر','ar'),(162,'label_item_food','عنصر الطعام','ar'),(163,'label_add_item_food','إضافة طعام عنصر','ar'),(164,'label_manage_item_foods','إدارة أطعمة العناصر','ar'),(165,'label_item_food_details','تفاصيل طعام العنصر','ar'),(166,'label_component','المكون','ar'),(167,'label_note','ملاحظة','ar'),(168,'label_is_group','مجموعة','ar'),(169,'label_cooked_time','وقت الطهي','ar'),(170,'label_offer_is_available','العرض متاح','ar'),(171,'label_offer_rate','معدل العرض','ar'),(172,'label_is_custom_qty','كمية مخصصة','ar'),(173,'label_is_special','خاص','ar'),(174,'label_product_vat','ضريبة المنتج','ar'),(175,'label_tax0','ضريبة 0','ar'),(176,'label_tax1','ضريبة 1','ar'),(177,'confirm_delete_item_food','هل أنت متأكد أنك تريد حذف عنصر الطعام هذا؟','ar'),(178,'msg_item_food_added','تم إضافة طعام العنصر بنجاح','ar'),(179,'msg_item_food_fetched','تم جلب أطعمة العناصر بنجاح','ar'),(180,'msg_item_food_created','تم إنشاء طعام العنصر بنجاح','ar'),(181,'msg_item_food_updated','تم تحديث طعام العنصر بنجاح','ar'),(182,'msg_item_food_deleted','تم حذف طعام العنصر بنجاح','ar'),(183,'msg_item_category_added','تم إضافة فئة العنصر بنجاح','ar'),(184,'msg_item_category_fetched','تم جلب فئات العناصر بنجاح','ar'),(185,'msg_item_category_created','تم إنشاء فئة العنصر بنجاح','ar'),(186,'msg_item_category_updated','تم تحديث فئة العنصر بنجاح','ar'),(187,'msg_item_category_deleted','تم حذف فئة العنصر بنجاح','ar'),(188,'msg_menu_type_added','تم إضافة نوع القائمة بنجاح','ar'),(189,'msg_menu_type_fetched','تم جلب أنواع القوائم بنجاح','ar'),(190,'msg_menu_type_created','تم إنشاء نوع القائمة بنجاح','ar'),(191,'msg_menu_type_updated','تم تحديث نوع القائمة بنجاح','ar'),(192,'msg_menu_type_deleted','تم حذف نوع القائمة بنجاح','ar'),(193,'label_manage_item_food_add_ons','إدارة الإضافات لطعام العنصر','ar'),(194,'label_item_food_add_on','إضافة طعام العنصر','ar'),(195,'label_add_item_food_add_ons','إضافة إضافة طعام العنصر','ar'),(196,'label_item_food_add_on_details','تفاصيل إضافة طعام العنصر','ar'),(197,'confirm_delete_item_food_add_on','هل أنت متأكد أنك تريد حذف إضافة طعام العنصر هذه؟','ar'),(198,'msg_item_food_add_ons_added','تم إضافة إضافة طعام العنصر بنجاح','ar'),(199,'msg_item_food_add_ons_fetched','تم جلب إضافات طعام العنصر بنجاح','ar'),(200,'msg_item_food_add_ons_created','تم إنشاء إضافة طعام العنصر بنجاح','ar'),(201,'msg_item_food_add_ons_updated','تم تحديث إضافة طعام العنصر بنجاح','ar'),(202,'msg_item_food_add_ons_deleted','تم حذف إضافة طعام العنصر بنجاح','ar'),(203,'label_variants','الأنواع','ar'),(204,'label_manage_item_food_variants','إدارة أنواع طعام العنصر','ar'),(205,'label_add_item_food_variant','إضافة نوع طعام العنصر','ar'),(206,'label_item_food_variant','نوع طعام العنصر','ar'),(207,'label_item_food_variant_details','تفاصيل نوع طعام العنصر','ar'),(208,'confirm_delete_item_food_variant','هل أنت متأكد أنك تريد حذف نوع طعام العنصر هذا؟','ar'),(209,'msg_item_food_variant_added','تم إضافة نوع طعام العنصر بنجاح','ar'),(210,'msg_item_food_variant_created','تم إنشاء نوع طعام العنصر بنجاح','ar'),(211,'msg_item_food_variant_fetched','تم جلب أنواع طعام العنصر بنجاح','ar'),(212,'msg_item_food_variant_updated','تم تحديث نوع طعام العنصر بنجاح','ar'),(213,'msg_item_food_variant_deleted','تم حذف نوع طعام العنصر بنجاح','ar'),(214,'label_add_ons','الإضافات','ar'),(215,'label_item_food_add_ons','إضافات طعام العنصر','ar'),(216,'label_item_food_variants','أنواع طعام العنصر','ar'),(217,'label_item_food_add_ons_association','ربط الإضافات','ar'),(218,'label_manage_item_food_add_ons_associations','إدارة روابط الإضافات','ar'),(219,'label_add_item_food_add_ons_association','إضافة رابط الإضافات','ar'),(220,'label_item_food_add_ons_association_details','تفاصيل رابط الإضافات','ar'),(221,'confirm_delete_item_food_add_ons_association','هل أنت متأكد أنك تريد حذف رابط الإضافات هذا؟','ar'),(222,'msg_item_food_add_ons_association_added','تم إضافة رابط الإضافات بنجاح','ar'),(223,'msg_item_food_add_ons_association_fetched','تم جلب روابط الإضافات بنجاح','ar'),(224,'msg_item_food_add_ons_association_created','تم إنشاء رابط الإضافات بنجاح','ar'),(225,'msg_item_food_add_ons_association_updated','تم تحديث رابط الإضافات بنجاح','ar'),(226,'msg_item_food_add_ons_association_deleted','تم حذف رابط الإضافات بنجاح','ar'),(227,'label_application_settings','إعدادات التطبيق','ar'),(228,'label_restaurant_settings','إعدادات المطعم','ar'),(229,'label_currencies','العملات','ar'),(230,'label_currency','العملة','ar'),(231,'label_add_currency','إضافة عملة','ar'),(232,'label_manage_currencies','إدارة العملات','ar'),(233,'label_currency_details','تفاصيل العملة','ar'),(234,'label_symbol','الرمز','ar'),(235,'label_exchange_rate_to_usd','سعر الصرف مقابل الدولار الأمريكي','ar'),(236,'confirm_delete_currency','هل أنت متأكد أنك تريد حذف هذه العملة؟','ar'),(237,'msg_currency_created','تم إنشاء العملة بنجاح','ar'),(238,'msg_currency_updated','تم تحديث العملة بنجاح','ar'),(239,'msg_currency_deleted','تم حذف العملة بنجاح','ar'),(240,'label_languages','اللغات','ar'),(241,'label_language','اللغة','ar'),(242,'label_add_language','إضافة لغة','ar'),(243,'label_manage_languages','إدارة اللغات','ar'),(244,'label_language_details','تفاصيل اللغة','ar'),(245,'label_is_default','هل هي اللغة الافتراضية','ar'),(246,'confirm_delete_language','هل أنت متأكد أنك تريد حذف هذه اللغة؟','ar'),(247,'msg_language_created','تم إنشاء اللغة بنجاح','ar'),(248,'msg_language_updated','تم تحديث اللغة بنجاح','ar'),(249,'msg_language_deleted','تم حذف اللغة بنجاح','ar'),(250,'label_language_translations','ترجمات اللغة','ar'),(251,'label_language_translation','ترجمة اللغة','ar'),(252,'label_add_language_translation','إضافة ترجمة لغة','ar'),(253,'label_manage_language_translations','إدارة ترجمات اللغة','ar'),(254,'label_language_translation_details','تفاصيل ترجمة اللغة','ar'),(255,'label_key','المفتاح','ar'),(256,'label_value','القيمة','ar'),(257,'confirm_delete_language_translation','هل أنت متأكد أنك تريد حذف هذه الترجمة؟','ar'),(258,'msg_language_translation_created','تم إنشاء ترجمة اللغة بنجاح','ar'),(259,'msg_language_translation_updated','تم تحديث ترجمة اللغة بنجاح','ar'),(260,'msg_language_translation_deleted','تم حذف ترجمة اللغة بنجاح','ar'),(261,'label_application_title','عنوان التطبيق','ar'),(262,'label_store_name','اسم المتجر','ar'),(263,'label_address','العنوان','ar'),(264,'label_opening_time','وقت الفتح','ar'),(265,'label_closing_time','وقت الإغلاق','ar'),(266,'label_discount_type','نوع الخصم','ar'),(267,'label_discount_percentage','نسبة الخصم','ar'),(268,'label_service_charge_type','نوع رسوم الخدمة','ar'),(269,'label_tax_percentage','نسبة الضريبة','ar'),(270,'label_tax_number','رقم الضريبة','ar'),(271,'label_date_format','تنسيق التاريخ','ar'),(272,'label_timezone','المنطقة الزمنية','ar'),(273,'label_application_direction','اتجاه التطبيق','ar'),(274,'label_powered_by_text','نص التشغيل بواسطة','ar'),(275,'label_footer_text','نص التذييل','ar'),(276,'label_logo','الشعار','ar'),(277,'msg_application_settings_updated_successfully','تم تحديث إعدادات التطبيق بنجاح','ar'),(278,'error_max_length_is','الحد الأقصى للطول هو {{max}} حرفًا','ar'),(279,'error_min_length_is','الحد الأدنى للطول هو {{min}} حرفًا','ar'),(280,'error_max_value_is','القيمة القصوى هي {{max}}','ar'),(281,'error_min_value_is','القيمة الدنيا هي {{min}}','ar'),(282,'error_enter_valid_email','يرجى إدخال عنوان بريد إلكتروني صالح','ar'),(283,'error_required','هذا الحقل مطلوب','ar'),(284,'error_pattern','تنسيق غير صالح','ar'),(285,'error_invalid','قيمة غير صالحة','ar'),(286,'msg_login_success','تم تسجيل الدخول بنجاح','ar'),(287,'msg_login_failed','فشل تسجيل الدخول','ar'),(288,'msg_authenticated_user_could_not_be_loaded','تعذر تحميل المستخدم المصادق عليه','ar'),(289,'msg_invalid_email_or_password','البريد الإلكتروني أو كلمة المرور غير صالحة','ar'),(290,'msg_unauthorized','الوصول غير مصرح به','ar'),(291,'msg_forbidden','الوصول ممنوع','ar'),(292,'msg_not_found','المورد غير موجود','ar'),(293,'msg_user_created','تم إنشاء المستخدم بنجاح','ar'),(294,'msg_user_updated','تم تحديث المستخدم بنجاح','ar'),(295,'msg_user_deleted','تم حذف المستخدم بنجاح','ar'),(296,'confirm_delete_user','هل أنت متأكد أنك تريد حذف هذا المستخدم؟','ar'),(297,'label_payment_methods','طرق الدفع','ar'),(298,'label_payment_method','طريقة الدفع','ar'),(299,'label_add_payment_method','إضافة طريقة دفع','ar'),(300,'label_manage_payment_methods','إدارة طرق الدفع','ar'),(301,'label_payment_method_details','تفاصيل طريقة الدفع','ar'),(302,'confirm_delete_payment_method','هل أنت متأكد أنك تريد حذف طريقة الدفع هذه؟','ar'),(303,'msg_payment_method_created','تم إنشاء طريقة الدفع بنجاح','ar'),(304,'msg_payment_method_updated','تم تحديث طريقة الدفع بنجاح','ar'),(305,'msg_payment_method_deleted','تم حذف طريقة الدفع بنجاح','ar'),(306,'label_customer_types','أنواع العملاء','ar'),(307,'label_customer_type','نوع العميل','ar'),(308,'label_add_customer_type','إضافة نوع عميل','ar'),(309,'label_manage_customer_types','إدارة أنواع العملاء','ar'),(310,'label_customer_type_details','تفاصيل نوع العميل','ar'),(311,'label_type','النوع','ar'),(312,'label_ordering','الترتيب','ar'),(313,'confirm_delete_customer_type','هل أنت متأكد أنك تريد حذف هذا النوع من العملاء؟','ar'),(314,'msg_customer_type_created','تم إنشاء نوع العميل بنجاح','ar'),(315,'msg_customer_type_updated','تم تحديث نوع العميل بنجاح','ar'),(316,'msg_customer_type_deleted','تم حذف نوع العميل بنجاح','ar'),(317,'label_customers','العملاء','ar'),(318,'label_add_customer','إضافة عميل','ar'),(319,'label_manage_customers','إدارة العملاء','ar'),(320,'label_customer_details','تفاصيل العميل','ar'),(321,'label_favorite_delivery_address','عنوان التسليم المفضل','ar'),(322,'label_select_customer_type','اختر نوع العميل','ar'),(323,'confirm_delete_customer','هل أنت متأكد أنك تريد حذف هذا العميل؟','ar'),(324,'msg_customer_created','تم إنشاء العميل بنجاح','ar'),(325,'msg_customer_updated','تم تحديث العميل بنجاح','ar'),(326,'msg_customer_deleted','تم حذف العميل بنجاح','ar'),(327,'label_third_party_customers','عملاء الطرف الثالث','ar'),(328,'label_third_party_customer','عميل الطرف الثالث','ar'),(329,'label_add_third_party_customer','إضافة عميل طرف ثالث','ar'),(330,'label_manage_third_party_customers','إدارة عملاء الطرف الثالث','ar'),(331,'label_third_party_customer_details','تفاصيل عميل الطرف الثالث','ar'),(332,'label_commission_percentage','نسبة العمولة','ar'),(333,'confirm_delete_third_party_customer','هل أنت متأكد أنك تريد حذف هذا العميل من الطرف الثالث؟','ar'),(334,'msg_third_party_customer_created','تم إنشاء عميل الطرف الثالث بنجاح','ar'),(335,'msg_third_party_customer_updated','تم تحديث عميل الطرف الثالث بنجاح','ar'),(336,'msg_third_party_customer_deleted','تم حذف عميل الطرف الثالث بنجاح','ar'),(337,'label_customers_management','إدارة العملاء','ar'),(338,'label_allow_transaction','السماح بالمعاملة','ar'),(339,'label_parent_account','الحساب الرئيسي','ar'),(340,'acc_ASSET','الأصول','ar'),(341,'acc_LIABILITY','الخصوم','ar'),(342,'acc_REVENUE','الإيرادات','ar'),(343,'acc_EQUITY','حقوق الملكية','ar'),(344,'acc_INCOME','الدخل','ar'),(345,'acc_EXPENSE','المصروفات','ar'),(346,'label_account_types','أنواع الحسابات','ar'),(347,'label_customer_account_report','تقرير حساب العميل','ar'),(348,'label_select_customer','اختر العميل','ar'),(349,'label_amount','المبلغ','ar'),(350,'title_operations','العمليات','ar'),(351,'label_ingredients','المكونات','ar'),(352,'label_suppliers','الموردين','ar'),(353,'label_manage_suppliers','إدارة الموردين','ar'),(354,'label_add_supplier','إضافة مورد','ar'),(355,'label_supplier_details','تفاصيل المورد','ar'),(356,'confirm_delete_supplier','هل أنت متأكد أنك تريد حذف هذا المورد؟','ar'),(357,'msg_supplier_created','تم إنشاء المورد بنجاح','ar'),(358,'msg_supplier_updated','تم تحديث المورد بنجاح','ar'),(359,'msg_supplier_deleted','تم حذف المورد بنجاح','ar'),(360,'label_ingredient','المكون','ar'),(361,'label_manage_ingredients','إدارة المكونات','ar'),(362,'label_add_ingredient','إضافة مكون','ar'),(363,'label_ingredient_details','تفاصيل المكون','ar'),(364,'label_uom','وحدة القياس','ar'),(365,'label_stock_quantity','كمية المخزون','ar'),(366,'label_min_stock_quantity','الحد الأدنى للكمية','ar'),(367,'confirm_delete_ingredient','هل أنت متأكد أنك تريد حذف هذا المكون؟','ar'),(368,'msg_ingredient_created','تم إنشاء المكون بنجاح','ar'),(369,'msg_ingredient_updated','تم تحديث المكون بنجاح','ar'),(370,'msg_ingredient_deleted','تم حذف المكون بنجاح','ar'),(371,'label_manage_purchases','إدارة المشتريات','ar'),(372,'label_add_purchase','إضافة مشتريات','ar'),(373,'label_edit_purchase','تعديل المشتريات','ar'),(374,'label_purchase_details','تفاصيل المشتريات','ar'),(375,'label_invoice_number','رقم الفاتورة','ar'),(376,'label_purchase_date','تاريخ الشراء','ar'),(377,'label_expiry_date','تاريخ الانتهاء','ar'),(378,'label_production_date','تاريخ الإنتاج','ar'),(379,'label_draft','مسودة','ar'),(380,'label_approved','معتمد','ar'),(381,'label_voided','ملغي','ar'),(382,'label_approve','اعتماد','ar'),(383,'label_void','إلغاء','ar'),(384,'label_total_amount','إجمالي المبلغ','ar'),(385,'label_paid_amount','المبلغ المدفوع','ar'),(386,'label_purchase_items','عناصر المشتريات','ar'),(387,'label_add_item','إضافة عنصر','ar'),(388,'label_quantity','الكمية','ar'),(389,'confirm_delete_purchase','هل أنت متأكد أنك تريد حذف هذه المشتريات؟','ar'),(390,'confirm_void_purchase','هل أنت متأكد أنك تريد إلغاء هذه المشتريات؟','ar'),(391,'confirm_approve_purchase','هل أنت متأكد أنك تريد اعتماد هذه المشتريات؟ لا يمكن التراجع عن هذا الإجراء.','ar'),(392,'msg_purchase_created','تم إنشاء المشتريات بنجاح','ar'),(393,'msg_purchase_updated','تم تحديث المشتريات بنجاح','ar'),(394,'msg_purchase_deleted','تم حذف المشتريات بنجاح','ar'),(395,'label_item','العنصر','ar'),(396,'label_qti','الكمية','ar'),(397,'label_supplier_account_report','تقرير حساب المورد','ar'),(398,'label_select_supplier','اختر المورد','ar'),(399,'label_balance','الرصيد','ar'),(400,'label_total','الإجمالي','ar'),(401,'label_order_items','عناصر الطلب','ar'),(402,'label_offer','العرض','ar'),(403,'label_add_another','إضافة آخر','ar'),(404,'label_add_and_close','إضافة وإغلاق','ar'),(405,'msg_select_variant','يرجى اختيار نسخة','ar'),(406,'label_place_order','تقديم الطلب','ar'),(407,'label_quick_order','طلب سريع','ar'),(408,'label_waiter','النادل','ar'),(409,'label_select_waiter','اختر النادل','ar'),(410,'label_select_table','اختر الطاولة','ar'),(411,'label_select_kitchen','اختر المطبخ','ar'),(412,'msg_fill_required_fields','يرجى ملء جميع الحقول المطلوبة','ar'),(413,'label_seats','المقاعد','ar'),(414,'label_select_third_party_customer','اختر عميل الطرف الثالث','ar'),(415,'label_vat','ضريبة القيمة المضافة','ar'),(416,'label_sub_total','المجموع الفرعي','ar'),(417,'label_select_payment_method','اختر طريقة الدفع','ar'),(418,'label_on_going_orders','الطلبات الجارية','ar'),(419,'label_kitchen_status','حالة المطبخ','ar'),(420,'label_today_orders','طلبات اليوم','ar'),(421,'label_cash_counters','عدادات النقد','ar'),(422,'label_cash_counter','عداد النقد','ar'),(423,'label_add_cash_counter','إضافة عداد نقد','ar'),(424,'label_manage_cash_counters','إدارة عدادات النقد','ar'),(425,'label_cash_counter_details','تفاصيل عداد النقد','ar'),(426,'confirm_delete_cash_counter','هل أنت متأكد أنك تريد حذف هذا العداد النقدي؟','ar'),(427,'label_number','الرقم','ar'),(428,'label_cash_registers','سجلات النقد','ar'),(429,'label_cash_register','سجل النقد','ar'),(430,'label_open_cash_register','فتح سجل النقد','ar'),(431,'label_close_cash_register','إغلاق سجل النقد','ar'),(432,'label_opening_balance','الرصيد الافتتاحي','ar'),(433,'label_closing_balance','الرصيد الختامي','ar'),(434,'label_opening_note','ملاحظة الافتتاح','ar'),(435,'label_closing_note','ملاحظة الختام','ar'),(436,'label_open','مفتوح','ar'),(437,'label_closed','مغلق','ar'),(438,'label_close','إغلاق','ar'),(439,'msg_cash_register_fetched','تم جلب سجل النقد بنجاح','ar'),(440,'msg_no_open_cash_register','لم يتم العثور على سجل نقد مفتوح','ar'),(441,'msg_cash_register_opened','تم فتح سجل النقد بنجاح','ar'),(442,'msg_cash_register_closed','تم إغلاق سجل النقد بنجاح','ar'),(443,'label_orders_management','إدارة الطلبات','ar'),(444,'label_allow_credit','السماح بالائتمان','ar'),(445,'msg_parent_account_code_not_provided','لم يتم تقديم رمز الحساب الرئيسي','ar'),(446,'label_account_code','رمز الحساب','ar'),(447,'label_account_name','اسم الحساب','ar'),(448,'label_manage_accounts','إدارة الحسابات','ar'),(449,'label_view_as_tree','عرض كهيكل شجري','ar'),(450,'label_view_as_flat','عرض كقائمة مسطحة','ar'),(451,'label_account_nature','طبيعة الحساب','ar'),(452,'label_account_balance','رصيد الحساب','ar'),(453,'label_account_status','حالة الحساب','ar'),(454,'label_sub_account','الحساب الفرعي / المعاملة','ar'),(455,'label_main_account','الحساب الرئيسي / الملخص','ar'),(456,'phrase_accounting_cash_sale','إثبات مبيعات نقدية للفاتورة رقم: ','ar'),(457,'phrase_accounting_credit_sale','إثبات مبيعات ائتمانية للفاتورة رقم: ','ar'),(458,'phrase_accounting_purchase_cash','إثبات شراء نقدي للمواد الخام للفاتورة رقم: ','ar'),(459,'phrase_accounting_purchase_credit','إثبات شراء ائتماني للمواد الخام للفاتورة رقم: ','ar'),(460,'phrase_accounting_supplier_payment','دفع للمورد بموجب سند الدفع رقم: ','ar'),(461,'phrase_accounting_salary_payment','دفع رواتب الموظفين لشهر: ','ar'),(462,'phrase_accounting_customer_collection','تحصيل من العميل بموجب سند الإيصال رقم: ','ar'),(463,'error_accounting_parent_cannot_transact','لا يمكن إجراء معاملات على الحساب الرئيسي: ','ar'),(464,'error_accounting_account_not_found','لم يتم العثور على الحساب المالي بالمعرف: {0}','ar'),(465,'error_accounting_unbalanced_entry','خطأ محاسبي: إدخال غير متوازن! إجمالي المدين: {0} وإجمالي الدائن: {1}','ar'),(466,'error_accounting_missing_code','الحساب المالي غير معرف في النظام بالرمز: {0}','ar'),(467,'label_remaining','المتبقي','ar'),(468,'label_cash','نقدي','ar'),(469,'label_entry_number','رقم الإدخال','ar'),(470,'label_reference','المرجع','ar'),(471,'label_cost_centers','مراكز التكلفة','ar'),(472,'label_cost_center','مركز التكلفة','ar'),(473,'label_add_cost_center','إضافة مركز تكلفة','ar'),(474,'label_manage_cost_centers','إدارة مراكز التكلفة','ar'),(475,'label_cost_center_details','تفاصيل مركز التكلفة','ar'),(476,'confirm_delete_cost_center','هل أنت متأكد أنك تريد حذف هذا المركز التكلفة؟','ar'),(477,'label_code','الرمز','ar'),(478,'label_fiscal_periods','الفترات المالية','ar'),(479,'label_manage_fiscal_periods','إدارة الفترات المالية','ar'),(480,'label_year','السنة','ar'),(481,'label_month','الشهر','ar'),(482,'label_start_date','تاريخ البدء','ar'),(483,'label_end_date','تاريخ الانتهاء','ar'),(484,'label_locked','مغلق','ar'),(485,'label_locked_by','تم القفل بواسطة','ar'),(486,'label_locked_at','تم القفل في','ar'),(487,'label_lock_period','قفل الفترة','ar'),(488,'label_unlock_period','فتح الفترة','ar'),(489,'label_select_year','اختر السنة','ar'),(490,'confirm_lock_period','هل أنت متأكد أنك تريد قفل هذه الفترة؟','ar'),(491,'confirm_unlock_period','هل أنت متأكد أنك تريد فتح هذه الفترة؟','ar'),(492,'label_manual_journal_entry','إدخال قيود يدوية','ar'),(493,'label_journal_items','بنود اليومية','ar'),(494,'label_entry_date','تاريخ الإدخال','ar'),(495,'label_add_line','إضافة سطر','ar'),(496,'label_account','الحساب','ar'),(497,'label_debit','مدين','ar'),(498,'label_credit','دائن','ar'),(499,'label_select_account','اختر الحساب','ar'),(500,'label_select_cost_center','اختر مركز التكلفة','ar'),(501,'label_balanced','متوازن','ar'),(502,'label_not_balanced','غير متوازن','ar'),(503,'label_reset','إعادة تعيين','ar'),(504,'label_submit','إرسال','ar'),(505,'label_general_ledger','دفتر الأستاذ العام','ar'),(506,'label_account_type','نوع الحساب','ar'),(507,'label_running_balance','الرصيد الجاري','ar'),(508,'label_date','التاريخ','ar'),(509,'label_generate','توليد','ar'),(510,'label_trial_balance','ميزان المراجعة','ar'),(511,'label_profit_loss','الأرباح والخسائر','ar'),(512,'label_revenue','الإيرادات','ar'),(513,'label_total_revenue','إجمالي الإيرادات','ar'),(514,'label_cost_of_goods_sold','تكلفة البضائع المباعة','ar'),(515,'label_total_cogs','إجمالي تكلفة البضائع المباعة','ar'),(516,'label_gross_profit','إجمالي الربح','ar'),(517,'label_operating_expenses','المصروفات التشغيلية','ar'),(518,'label_total_operating_expenses','إجمالي المصروفات التشغيلية','ar'),(519,'label_net_profit','صافي الربح','ar'),(520,'label_balance_sheet','الميزانية العمومية','ar'),(521,'label_as_of_date','حتى تاريخ','ar'),(522,'label_assets','الأصول','ar'),(523,'label_total_assets','إجمالي الأصول','ar'),(524,'label_liabilities','الخصوم','ar'),(525,'label_total_liabilities','إجمالي الخصوم','ar'),(526,'label_equity','حقوق الملكية','ar'),(527,'label_total_equity','إجمالي حقوق الملكية','ar'),(528,'label_total_liabilities_and_equity','إجمالي الخصوم وحقوق الملكية','ar'),(529,'label_year_end_closing','إغلاق نهاية السنة','ar'),(530,'label_fiscal_year','السنة المالية','ar'),(531,'label_perform_closing','تنفيذ إغلاق نهاية السنة','ar'),(532,'label_closing_entry_number','رقم قيد الإغلاق','ar'),(533,'label_closing_date','تاريخ الإغلاق','ar'),(534,'label_total_expenses','إجمالي المصروفات','ar'),(535,'label_net_income','صافي الدخل','ar'),(536,'label_accounts_closed','الحسابات المغلقة','ar'),(537,'confirm_year_end_closing','هل أنت متأكد أنك تريد تنفيذ إغلاق نهاية السنة؟ لا يمكن التراجع عن هذا الإجراء.','ar'),(538,'label_financial_reports','التقارير المالية','ar'),(539,'label_journal_entries','قيود اليومية','ar'),(540,'label_order_number','رقم الطلب','ar'),(541,'label_order_type','نوع الطلب','ar'),(542,'label_order_info','معلومات الطلب','ar'),(543,'label_orders','الطلبات','ar'),(544,'label_created_at','تاريخ الإنشاء','ar'),(545,'label_complete','إكمال','ar'),(546,'label_checkout','الدفع','ar'),(547,'label_filter_by_status','تصفية حسب الحالة','ar'),(548,'label_all','الكل','ar'),(549,'label_merge_orders','دمج الطلبات','ar'),(550,'label_split_order','تقسيم الطلب','ar'),(551,'label_split','تقسيم','ar'),(552,'label_tracking','التتبع','ar'),(553,'label_kitchen_progress','تقدم المطبخ','ar'),(554,'label_kitchen_orders','طلبات المطبخ','ar'),(555,'label_items_ready','العناصر الجاهزة','ar'),(556,'label_no_kitchen_data','لا تتوفر بيانات مطبخ','ar'),(557,'label_no_tracking_data','لا تتوفر بيانات تتبع','ar'),(558,'label_auto_refresh_5s','يتحدث تلقائياً كل 5 ثوانٍ','ar'),(559,'label_variant','النوع','ar'),(560,'label_change','الباقي','ar'),(561,'label_view','عرض','ar'),(562,'label_refresh','تحديث','ar'),(563,'label_kitchen_dashboard','لوحة المطبخ','ar'),(564,'label_pending','في الانتظار','ar'),(565,'label_accepted','مقبول','ar'),(566,'label_preparing','قيد التحضير','ar'),(567,'label_ready','جاهز','ar'),(568,'label_no_orders','لا توجد طلبات','ar'),(569,'label_accept','قبول','ar'),(570,'label_reject','رفض','ar'),(571,'label_prepare','تحضير','ar'),(572,'label_mark_ready','تحديد كجاهز','ar'),(573,'label_item_ready','العنصر جاهز','ar'),(574,'label_item_served','تم تقديم العنصر','ar'),(575,'label_group','مجموعة','ar'),(576,'label_add_group','إضافة مجموعة','ar'),(577,'msg_split_order_hint','عيّن كميات كل عنصر لكل مجموعة. المجموعات التي تحتوي على أصفار فقط سيتم تجاهلها.','ar'),(578,'confirm_complete_order','هل أنت متأكد أنك تريد تحديد هذا الطلب كمكتمل؟','ar'),(579,'confirm_delete_order','هل أنت متأكد أنك تريد حذف هذا الطلب؟','ar'),(580,'confirm_merge_orders','هل أنت متأكد أنك تريد دمج الطلبات المحددة؟','ar'),(581,'confirm_remove_item','هل أنت متأكد أنك تريد إزالة هذا العنصر من الطلب؟','ar'),(582,'label_delivery_module','التوصيل','ar'),(583,'label_deliveries','التوصيلات','ar'),(584,'label_delivery','توصيل','ar'),(585,'label_deliveries_management','إدارة التوصيلات','ar'),(586,'label_drivers_management','إدارة السائقين','ar'),(587,'label_drivers','السائقون','ar'),(588,'label_driver','السائق','ar'),(589,'label_create_delivery','إنشاء توصيل','ar'),(590,'label_delivery_info','معلومات التوصيل','ar'),(591,'label_driver_info','معلومات السائق','ar'),(592,'label_driver_profile','ملف السائق','ar'),(593,'label_delivery_fee','رسوم التوصيل','ar'),(594,'label_estimated_delivery_time','وقت التوصيل المتوقع','ar'),(595,'label_actual_delivery_time','وقت التوصيل الفعلي','ar'),(596,'label_assign_driver','تعيين سائق','ar'),(597,'label_reassign_driver','إعادة تعيين سائق','ar'),(598,'label_complete_delivery','إتمام التوصيل','ar'),(599,'label_cancel_delivery','إلغاء التوصيل','ar'),(600,'label_cancel_reason','سبب الإلغاء','ar'),(601,'label_reassign_reason_placeholder','سبب إعادة التعيين (مطلوب)','ar'),(602,'label_reason_required','السبب مطلوب','ar'),(603,'label_vehicle_type','نوع المركبة','ar'),(604,'label_vehicle_plate','لوحة المركبة','ar'),(605,'label_assigned_at','وقت التعيين','ar'),(606,'label_accepted_at','وقت القبول','ar'),(607,'label_picked_up_at','وقت الاستلام','ar'),(608,'label_delivered_at','وقت التسليم','ar'),(609,'label_updated_at','آخر تحديث','ar'),(610,'label_no_driver_assigned','لم يتم تعيين سائق بعد','ar'),(611,'label_no_available_drivers','لا يوجد سائقون متاحون حالياً','ar'),(612,'label_no_deliveries','لا توجد توصيلات','ar'),(613,'label_no_drivers','لا يوجد سائقون','ar'),(614,'label_no_active_delivery','لا توجد توصيلة نشطة','ar'),(615,'label_no_delivery_history','لا يوجد سجل توصيلات','ar'),(616,'label_select_driver','اختر سائقاً','ar'),(617,'label_status_timeline','مراحل الحالة','ar'),(618,'label_status_created','تم الإنشاء','ar'),(619,'label_status_assigned','تم تعيين السائق','ar'),(620,'label_status_accepted','قبل السائق','ar'),(621,'label_status_arrived','وصل إلى المطعم','ar'),(622,'label_status_picked_up','استلم الطلب','ar'),(623,'label_status_on_the_way','في الطريق','ar'),(624,'label_status_delivered','تم التسليم','ar'),(625,'label_status_cancelled','ملغي','ar'),(626,'label_status_failed','فشل','ar'),(627,'label_live_tracking','التتبع المباشر','ar'),(628,'label_auto_refresh_10s','يتحدث تلقائياً كل 10 ثوانٍ','ar'),(629,'label_open_in_google_maps','فتح في خرائط Google','ar'),(630,'label_latitude','خط العرض','ar'),(631,'label_longitude','خط الطول','ar'),(632,'label_speed','السرعة','ar'),(633,'label_heading','الاتجاه','ar'),(634,'label_gps_trail','مسار GPS','ar'),(635,'label_points','نقاط','ar'),(636,'label_is_online','متصل','ar'),(637,'label_change_status','تغيير الحالة','ar'),(638,'label_current_delivery','التوصيلة النشطة الحالية','ar'),(639,'label_delivery_history','سجل التوصيلات','ar'),(640,'label_view_delivery','عرض التوصيلة','ar'),(641,'label_order_id','رقم الطلب','ar'),(642,'label_loading','جارٍ التحميل...','ar'),(643,'label_optional','اختياري','ar'),(644,'label_reason','السبب','ar'),(645,'confirm_complete_delivery','هل أنت متأكد أنك تريد تحديد هذه التوصيلة كمكتملة؟','ar'),(646,'msg_order_not_delivery_type','هذا الطلب ليس طلب توصيل','ar'),(647,'msg_delivery_already_exists','يوجد بالفعل توصيل لهذا الطلب','ar'),(648,'msg_driver_not_available','السائق غير متاح للتعيين','ar'),(649,'msg_driver_has_active_delivery','السائق لديه بالفعل توصيلة نشطة','ar'),(650,'msg_delivery_in_terminal_state','هذه التوصيلة مغلقة بالفعل','ar'),(651,'msg_order_not_ready_for_pickup','الطلب غير جاهز للاستلام بعد','ar'),(652,'msg_driver_busy_cannot_change_status','لا يمكن تغيير الحالة أثناء وجود توصيلة نشطة','ar'),(653,'msg_no_active_delivery','لا توجد توصيلة نشطة للسائق','ar'),(655,'msg_driver_not_found','السائق غير موجود','ar'),(656,'currentAssets','الأصول المتداولة','ar'),(657,'cashAndBank','النقدية والبنوك','ar'),(658,'foodAndBeverageInventory','مخزون الأغذية والمشروبات','ar'),(659,'currentLiabilities','الالتزامات المتداولة','ar'),(660,'accountsPayableSuppliers','حسابات الموردين','ar'),(661,'restaurantRevenue','إيرادات المطعم','ar'),(662,'foodSalesRevenue','إيرادات مبيعات الأغذية','ar'),(663,'beverageSalesRevenue','إيرادات مبيعات المشروبات','ar'),(664,'costOfGoodsSoldCogs','تكلفة البضاعة المباعة','ar'),(665,'foodCost','تكلفة الأغذية','ar'),(666,'beverageCost','تكلفة المشروبات','ar'),(667,'operatingExpenses','المصروفات التشغيلية','ar'),(668,'salariesAndWages','الرواتب والأجور','ar'),(669,'restaurantRent','إيجار المطعم','ar'),(670,'utilitiesElectricityWater','المرافق (كهرباء / مياه)','ar'),(671,'marketingAndAdvertising','التسويق والإعلان','ar'),(672,'kitchenAndDiningSupplies','مستلزمات المطبخ وصالة الطعام','ar'),(673,'equity','حقوق الملكية','ar'),(674,'retainedEarnings','الأرباح المحتجزة','ar'),(678,'msg_purchase_fetched','تم جلب كل الفواتير','ar'),(679,'msg_purchase_approved','تم اعتماد فاتورة الشراء','ar'),(680,'msg_purchase_voided','تم إلغاء فاتورة الشراء','ar'),(681,'error_cannot_be_approved_from_status','لا يمكن قبولها من الحالة : ','ar'),(682,'error_only_approved_purchases_can_be_voided','يمكن إلغاء المشتريات الموافق عليها فقط','ar'),(683,'error_insufficient_stock_to_reverse_ingredient','لا يمكن عكس المخزون للعنصر المحدد. الكمية الحالية أقل من الكمية المطلوبة للعكس.','ar'),(684,'error_current_stock','المخزون الحالي','ar'),(685,'error_required_reversal','الكمية المطلوبة للعكس','ar'),(686,'error_purchase_not_found','الفاتورة غير موجودة برقم: ','ar'),(687,'error_supplier_not_found','المورد غير موجود برقم: ','ar'),(688,'error_duplicate_invoice_number','رقم الفاتورة موجود مسبقاً لهذا المورد','ar'),(689,'error_voided_purchase_cannot_be_modified','لا يمكن تعديل فاتورة ملغاة.','ar'),(690,'error_approved_purchase_cannot_be_deleted','لا يمكن حذف فاتورة معتمدة. قم بإلغائها أولاً.','ar'),(691,'msg_application_setting_updated','تم تحديث الاعدادات','ar'),(692,'msg_logout_success','تم تسجيل الخروج بنجاح','ar'),(693,'label_confirm_logout_message','هل أنت متأكد أنك تريد تسجيل الخروج؟','ar'),(694,'label_confirm_logout','تأكيد تسجيل الخروج','ar'),(695,'label_welcome','Welcome to Restaurant Management System','en'),(696,'label_sign_in','Sign in to your account','en'),(697,'label_id','ID','en'),(698,'label_error','Error','en'),(699,'label_success','Success','en'),(700,'label_warning','Warning','en'),(701,'label_info','Info','en'),(702,'label_inactive','Inactive','en'),(703,'label_clear','Clear','en'),(704,'label_select_image','Select Image','en'),(705,'label_successful','Successful','en'),(706,'label_failed','Failed','en'),(707,'label_edit','Edit','en'),(708,'label_delete','Delete','en'),(709,'label_add','Add','en'),(710,'label_pos','POS','en'),(711,'label_manage_users','Manage Users','en'),(712,'label_user_details','User Details','en'),(713,'label_users','Users','en'),(714,'label_user','User','en'),(715,'label_email','Email','en'),(716,'label_password','Password','en'),(717,'label_first_name','First Name','en'),(718,'label_last_name','Last Name','en'),(719,'label_phone','Phone','en'),(720,'label_image','Image','en'),(721,'label_about','About','en'),(722,'label_is_admin','Is Admin','en'),(723,'label_is_monitor','Is Monitor','en'),(724,'label_counter','Counter','en'),(725,'label_roles','Roles','en'),(726,'label_manage_roles','Manage Roles','en'),(727,'label_add_role','Add Role','en'),(728,'label_create_role','Create Role','en'),(729,'label_create_system_role','Create System Role','en'),(730,'label_edit_system_role','Edit System Role','en'),(731,'label_edit_role','Edit Role','en'),(732,'label_delete_role','Delete Role','en'),(733,'label_view_role','View Role','en'),(734,'label_role_details','Role Details','en'),(735,'label_itemmanage','Item Manage','en'),(736,'label_units','Units','en'),(737,'label_purchase','Purchase','en'),(738,'label_setting','Setting','en'),(739,'label_ordermanage','Order Manage','en'),(740,'label_accounts','Accounts','en'),(741,'label_hrm','HRM','en'),(742,'label_report','Report','en'),(743,'label_category','Category','en'),(744,'label_supplier','Supplier','en'),(745,'label_production','Production','en'),(746,'label_reservation','Reservation','en'),(747,'label_parent_menu','Parent Menu','en'),(748,'label_can_create','Can Create','en'),(749,'label_can_edit','Can Edit','en'),(750,'label_can_delete','Can Delete','en'),(751,'label_can_view','Can View','en'),(752,'label_can_read','Can Read','en'),(753,'label_add_user','Add User','en'),(754,'label_edit_user','Edit User','en'),(755,'label_delete_user','Delete User','en'),(756,'label_view_user','View User','en'),(757,'label_export','Export','en'),(758,'label_search','Search','en'),(759,'label_actions','Actions','en'),(760,'label_save','Save','en'),(761,'label_cancel','Cancel','en'),(762,'label_confirm','Confirm','en'),(763,'label_yes','Yes','en'),(764,'label_no','No','en'),(765,'label_active','Active','en'),(766,'label_status','Status','en'),(767,'label_confrim','Confirm','en'),(768,'label_role','Role','en'),(769,'label_admin','Admin','en'),(770,'label_customer','Customer','en'),(771,'label_name','Name','en'),(772,'label_description','Description','en'),(773,'label_price','Price','en'),(774,'label_login','Login','en'),(775,'label_logout','Logout','en'),(776,'label_dashboard','Dashboard','en'),(777,'label_profile','Profile','en'),(778,'label_users_and_permissions','Users & Permissions','en'),(779,'confirm_delete_role','Are you sure you want to delete this role?','en'),(780,'msg_role_created','Role created successfully','en'),(781,'msg_role_updated','Role updated successfully','en'),(782,'msg_role_deleted','Role deleted successfully','en'),(783,'label_modules','Modules','en'),(784,'msg_permission_updated','Permission updated successfully','en'),(785,'label_settings','Settings','en'),(786,'label_purchases','Purchases','en'),(787,'label_sales','Sales','en'),(788,'label_reservations','Reservations','en'),(789,'label_units_of_measurment','Units of Measurement','en'),(790,'confirm_delete_uom','Are you sure you want to delete this unit of measurement?','en'),(791,'label_add_uom','Add Unit of Measurement','en'),(792,'label_manage_uoms','Manage Units of Measurement','en'),(793,'label_uom_details','Unit of Measurement Details','en'),(794,'label_short_name','Short Name','en'),(795,'msg_uom_deleted','Unit of Measurement deleted successfully','en'),(796,'msg_uom_updated','Unit of Measurement updated successfully','en'),(797,'msg_uom_created','Unit of Measurement created successfully','en'),(798,'msg_uom_fetched','Units of Measurement fetched successfully','en'),(799,'msg_uom_added','Unit of Measurement added successfully','en'),(800,'label_floors','Floors','en'),(801,'label_floor','Floor','en'),(802,'label_add_floor','Add Floor','en'),(803,'label_manage_floors','Manage Floors','en'),(804,'label_floor_details','Floor Details','en'),(805,'confirm_delete_floor','Are you sure you want to delete this floor?','en'),(806,'msg_floor_added','Floor added successfully','en'),(807,'msg_floor_fetched','Floors fetched successfully','en'),(808,'msg_floor_created','Floor created successfully','en'),(809,'msg_floor_updated','Floor updated successfully','en'),(810,'msg_floor_deleted','Floor deleted successfully','en'),(811,'label_tables','Tables','en'),(812,'label_table','Table','en'),(813,'label_add_table','Add Table','en'),(814,'label_manage_tables','Manage Tables','en'),(815,'label_table_details','Table Details','en'),(816,'label_capacity','Capacity','en'),(817,'label_icon','Icon','en'),(818,'confirm_delete_table','Are you sure you want to delete this table?','en'),(819,'label_kitchens','Kitchens','en'),(820,'label_kitchen','Kitchen','en'),(821,'label_add_kitchen','Add Kitchen','en'),(822,'label_manage_kitchens','Manage Kitchens','en'),(823,'label_kitchen_details','Kitchen Details','en'),(824,'label_ip_address','IP Address','en'),(825,'label_port','Port','en'),(826,'confirm_delete_kitchen','Are you sure you want to delete this kitchen?','en'),(827,'msg_kitchen_added','Kitchen added successfully','en'),(828,'msg_kitchen_fetched','Kitchens fetched successfully','en'),(829,'msg_kitchen_created','Kitchen created successfully','en'),(830,'msg_kitchen_updated','Kitchen updated successfully','en'),(831,'msg_kitchen_deleted','Kitchen deleted successfully','en'),(832,'msg_table_added','Table added successfully','en'),(833,'msg_table_fetched','Tables fetched successfully','en'),(834,'msg_table_created','Table created successfully','en'),(835,'msg_table_updated','Table updated successfully','en'),(836,'msg_table_deleted','Table deleted successfully','en'),(837,'label_item_categories','Item Categories','en'),(838,'label_item_category','Item Category','en'),(839,'label_add_item_category','Add Item Category','en'),(840,'label_manage_item_categories','Manage Item Categories','en'),(841,'label_item_category_details','Item Category Details','en'),(842,'label_position','Position','en'),(843,'label_is_offer','Is Offer','en'),(844,'label_offer_start_date','Offer Start Date','en'),(845,'label_offer_end_date','Offer End Date','en'),(846,'confirm_delete_item_category','Are you sure you want to delete this item category?','en'),(847,'label_menu_types','Menu Types','en'),(848,'label_menu_type','Menu Type','en'),(849,'label_add_menu_type','Add Menu Type','en'),(850,'label_manage_menu_types','Manage Menu Types','en'),(851,'label_menu_type_details','Menu Type Details','en'),(852,'confirm_delete_menu_type','Are you sure you want to delete this menu type?','en'),(853,'label_item_management','Item Management','en'),(854,'label_food_management','Food Management','en'),(855,'label_item_foods','Item Foods','en'),(856,'label_item_food','Item Food','en'),(857,'label_add_item_food','Add Item Food','en'),(858,'label_manage_item_foods','Manage Item Foods','en'),(859,'label_item_food_details','Item Food Details','en'),(860,'label_component','Component','en'),(861,'label_note','Note','en'),(862,'label_is_group','Is Group','en'),(863,'label_cooked_time','Cooked Time','en'),(864,'label_offer_is_available','Offer Available','en'),(865,'label_offer_rate','Offer Rate','en'),(866,'label_is_custom_qty','Custom Quantity','en'),(867,'label_is_special','Is Special','en'),(868,'label_product_vat','Product VAT','en'),(869,'label_tax0','Tax 0','en'),(870,'label_tax1','Tax 1','en'),(871,'confirm_delete_item_food','Are you sure you want to delete this item food?','en'),(872,'msg_item_food_added','Item food added successfully','en'),(873,'msg_item_food_fetched','Item foods fetched successfully','en'),(874,'msg_item_food_created','Item food created successfully','en'),(875,'msg_item_food_updated','Item food updated successfully','en'),(876,'msg_item_food_deleted','Item food deleted successfully','en'),(877,'msg_item_category_added','Item category added successfully','en'),(878,'msg_item_category_fetched','Item categories fetched successfully','en'),(879,'msg_item_category_created','Item category created successfully','en'),(880,'msg_item_category_updated','Item category updated successfully','en'),(881,'msg_item_category_deleted','Item category deleted successfully','en'),(882,'msg_menu_type_added','Menu type added successfully','en'),(883,'msg_menu_type_fetched','Menu types fetched successfully','en'),(884,'msg_menu_type_created','Menu type created successfully','en'),(885,'msg_menu_type_updated','Menu type updated successfully','en'),(886,'msg_menu_type_deleted','Menu type deleted successfully','en'),(887,'label_manage_item_food_add_ons','Manage Item Food Add-Ons','en'),(888,'label_item_food_add_on','Item Food Add-On','en'),(889,'label_add_item_food_add_ons','Add Item Food Add-On','en'),(890,'label_item_food_add_on_details','Item Food Add-On Details','en'),(891,'confirm_delete_item_food_add_on','Are you sure you want to delete this item food add-on?','en'),(892,'msg_item_food_add_ons_added','Item food add-on added successfully','en'),(893,'msg_item_food_add_ons_fetched','Item food add-ons fetched successfully','en'),(894,'msg_item_food_add_ons_created','Item food add-on created successfully','en'),(895,'msg_item_food_add_ons_updated','Item food add-on updated successfully','en'),(896,'msg_item_food_add_ons_deleted','Item food add-on deleted successfully','en'),(897,'label_variants','Variants','en'),(898,'label_manage_item_food_variants','Manage Item Food Variants','en'),(899,'label_add_item_food_variant','Add Item Food Variant','en'),(900,'label_item_food_variant','Item Food Variant','en'),(901,'label_item_food_variant_details','Item Food Variant Details','en'),(902,'confirm_delete_item_food_variant','Are you sure you want to delete this item food variant?','en'),(903,'msg_item_food_variant_added','Item food variant added successfully','en'),(904,'msg_item_food_variant_created','Item food variant created successfully','en'),(905,'msg_item_food_variant_fetched','Item food variants fetched successfully','en'),(906,'msg_item_food_variant_updated','Item food variant updated successfully','en'),(907,'msg_item_food_variant_deleted','Item food variant deleted successfully','en'),(908,'label_add_ons','Add-Ons','en'),(909,'label_item_food_add_ons','Item Food Add-Ons','en'),(910,'label_item_food_variants','Item Food Variants','en'),(911,'label_item_food_add_ons_association','Add-Ons Association','en'),(912,'label_manage_item_food_add_ons_associations','Manage Add-Ons Associations','en'),(913,'label_add_item_food_add_ons_association','Add Add-Ons Association','en'),(914,'label_item_food_add_ons_association_details','Add-Ons Association Details','en'),(915,'confirm_delete_item_food_add_ons_association','Are you sure you want to delete this add-ons association?','en'),(916,'msg_item_food_add_ons_association_added','Add-ons association added successfully','en'),(917,'msg_item_food_add_ons_association_fetched','Add-ons associations fetched successfully','en'),(918,'msg_item_food_add_ons_association_created','Add-ons association created successfully','en'),(919,'msg_item_food_add_ons_association_updated','Add-ons association updated successfully','en'),(920,'msg_item_food_add_ons_association_deleted','Add-ons association deleted successfully','en'),(921,'label_application_settings','Application Settings','en'),(922,'label_restaurant_settings','Restaurant Settings','en'),(923,'label_currencies','Currencies','en'),(924,'label_currency','Currency','en'),(925,'label_add_currency','Add Currency','en'),(926,'label_manage_currencies','Manage Currencies','en'),(927,'label_currency_details','Currency Details','en'),(928,'label_symbol','Symbol','en'),(929,'label_exchange_rate_to_usd','Exchange Rate to USD','en'),(930,'confirm_delete_currency','Are you sure you want to delete this currency?','en'),(931,'msg_currency_created','Currency created successfully','en'),(932,'msg_currency_updated','Currency updated successfully','en'),(933,'msg_currency_deleted','Currency deleted successfully','en'),(934,'label_languages','Languages','en'),(935,'label_language','Language','en'),(936,'label_add_language','Add Language','en'),(937,'label_manage_languages','Manage Languages','en'),(938,'label_language_details','Language Details','en'),(939,'label_is_default','Is Default','en'),(940,'confirm_delete_language','Are you sure you want to delete this language?','en'),(941,'msg_language_created','Language created successfully','en'),(942,'msg_language_updated','Language updated successfully','en'),(943,'msg_language_deleted','Language deleted successfully','en'),(944,'label_language_translations','Language Translations','en'),(945,'label_language_translation','Language Translation','en'),(946,'label_add_language_translation','Add Language Translation','en'),(947,'label_manage_language_translations','Manage Language Translations','en'),(948,'label_language_translation_details','Language Translation Details','en'),(949,'label_key','Key','en'),(950,'label_value','Value','en'),(951,'confirm_delete_language_translation','Are you sure you want to delete this language translation?','en'),(952,'msg_language_translation_created','Language translation created successfully','en'),(953,'msg_language_translation_updated','Language translation updated successfully','en'),(954,'msg_language_translation_deleted','Language translation deleted successfully','en'),(955,'label_application_title','Application Title','en'),(956,'label_store_name','Store Name','en'),(957,'label_address','Address','en'),(958,'label_opening_time','Opening Time','en'),(959,'label_closing_time','Closing Time','en'),(960,'label_discount_type','Discount Type','en'),(961,'label_discount_percentage','Discount Percentage','en'),(962,'label_service_charge_type','Service Charge Type','en'),(963,'label_tax_percentage','Tax Percentage','en'),(964,'label_tax_number','Tax Number','en'),(965,'label_date_format','Date Format','en'),(966,'label_timezone','Timezone','en'),(967,'label_application_direction','Application Direction','en'),(968,'label_powered_by_text','Powered By Text','en'),(969,'label_footer_text','Footer Text','en'),(970,'label_logo','Logo','en'),(971,'msg_application_settings_updated_successfully','Application settings updated successfully','en'),(972,'error_max_length_is','Maximum length is {{max}} characters','en'),(973,'error_min_length_is','Minimum length is {{min}} characters','en'),(974,'error_max_value_is','Maximum value is {{max}}','en'),(975,'error_min_value_is','Minimum value is {{min}}','en'),(976,'error_enter_valid_email','Please enter a valid email address','en'),(977,'error_required','This field is required','en'),(978,'error_pattern','Invalid format','en'),(979,'error_invalid','Invalid value','en'),(980,'msg_login_success','Login successful','en'),(981,'msg_login_failed','Login failed','en'),(982,'msg_authenticated_user_could_not_be_loaded','Authenticated user could not be loaded','en'),(983,'msg_invalid_email_or_password','Invalid email or password','en'),(984,'msg_unauthorized','Unauthorized access','en'),(985,'msg_forbidden','Forbidden access','en'),(986,'msg_not_found','Resource not found','en'),(987,'msg_user_created','User created successfully','en'),(988,'msg_user_updated','User updated successfully','en'),(989,'msg_user_deleted','User deleted successfully','en'),(990,'confirm_delete_user','Are you sure you want to delete this user?','en'),(991,'label_payment_methods','Payment Methods','en'),(992,'label_payment_method','Payment Method','en'),(993,'label_add_payment_method','Add Payment Method','en'),(994,'label_manage_payment_methods','Manage Payment Methods','en'),(995,'label_payment_method_details','Payment Method Details','en'),(996,'confirm_delete_payment_method','Are you sure you want to delete this payment method?','en'),(997,'msg_payment_method_created','Payment method created successfully','en'),(998,'msg_payment_method_updated','Payment method updated successfully','en'),(999,'msg_payment_method_deleted','Payment method deleted successfully','en'),(1000,'label_customer_types','Customer Types','en'),(1001,'label_customer_type','Customer Type','en'),(1002,'label_add_customer_type','Add Customer Type','en'),(1003,'label_manage_customer_types','Manage Customer Types','en'),(1004,'label_customer_type_details','Customer Type Details','en'),(1005,'label_type','Type','en'),(1006,'label_ordering','Ordering','en'),(1007,'confirm_delete_customer_type','Are you sure you want to delete this customer type?','en'),(1008,'msg_customer_type_created','Customer type created successfully','en'),(1009,'msg_customer_type_updated','Customer type updated successfully','en'),(1010,'msg_customer_type_deleted','Customer type deleted successfully','en'),(1011,'label_customers','Customers','en'),(1012,'label_add_customer','Add Customer','en'),(1013,'label_manage_customers','Manage Customers','en'),(1014,'label_customer_details','Customer Details','en'),(1015,'label_favorite_delivery_address','Favorite Delivery Address','en'),(1016,'label_select_customer_type','Select Customer Type','en'),(1017,'confirm_delete_customer','Are you sure you want to delete this customer?','en'),(1018,'msg_customer_created','Customer created successfully','en'),(1019,'msg_customer_updated','Customer updated successfully','en'),(1020,'msg_customer_deleted','Customer deleted successfully','en'),(1021,'label_third_party_customers','Third Party Customers','en'),(1022,'label_third_party_customer','Third Party Customer','en'),(1023,'label_add_third_party_customer','Add Third Party Customer','en'),(1024,'label_manage_third_party_customers','Manage Third Party Customers','en'),(1025,'label_third_party_customer_details','Third Party Customer Details','en'),(1026,'label_commission_percentage','Commission Percentage','en'),(1027,'confirm_delete_third_party_customer','Are you sure you want to delete this third party customer?','en'),(1028,'msg_third_party_customer_created','Third party customer created successfully','en'),(1029,'msg_third_party_customer_updated','Third party customer updated successfully','en'),(1030,'msg_third_party_customer_deleted','Third party customer deleted successfully','en'),(1031,'label_customers_management','Customers Management','en'),(1032,'label_allow_transaction','Allow Transaction','en'),(1033,'label_parent_account','Parent Account','en'),(1034,'acc_ASSET','Asset','en'),(1035,'acc_LIABILITY','Liability','en'),(1036,'acc_REVENUE','Revenue','en'),(1037,'acc_EQUITY','Equity','en'),(1038,'acc_INCOME','Income','en'),(1039,'acc_EXPENSE','Expense','en'),(1040,'label_account_types','Account Types','en'),(1041,'label_customer_account_report','Customer Account Report','en'),(1042,'label_select_customer','Select Customer','en'),(1043,'label_amount','Amount','en'),(1044,'title_operations','Operations','en'),(1045,'label_ingredients','Ingredients','en'),(1046,'label_suppliers','Suppliers','en'),(1047,'label_manage_suppliers','Manage Suppliers','en'),(1048,'label_add_supplier','Add Supplier','en'),(1049,'label_supplier_details','Supplier Details','en'),(1050,'confirm_delete_supplier','Are you sure you want to delete this supplier?','en'),(1051,'msg_supplier_created','Supplier created successfully','en'),(1052,'msg_supplier_updated','Supplier updated successfully','en'),(1053,'msg_supplier_deleted','Supplier deleted successfully','en'),(1054,'label_ingredient','Ingredient','en'),(1055,'label_manage_ingredients','Manage Ingredients','en'),(1056,'label_add_ingredient','Add Ingredient','en'),(1057,'label_ingredient_details','Ingredient Details','en'),(1058,'label_uom','Unit of Measurement','en'),(1059,'label_stock_quantity','Stock Quantity','en'),(1060,'label_min_stock_quantity','Min Stock Quantity','en'),(1061,'confirm_delete_ingredient','Are you sure you want to delete this ingredient?','en'),(1062,'msg_ingredient_created','Ingredient created successfully','en'),(1063,'msg_ingredient_updated','Ingredient updated successfully','en'),(1064,'msg_ingredient_deleted','Ingredient deleted successfully','en'),(1065,'label_manage_purchases','Manage Purchases','en'),(1066,'label_add_purchase','Add Purchase','en'),(1067,'label_edit_purchase','Edit Purchase','en'),(1068,'label_purchase_details','Purchase Details','en'),(1069,'label_invoice_number','Invoice Number','en'),(1070,'label_purchase_date','Purchase Date','en'),(1071,'label_expiry_date','Expiry Date','en'),(1072,'label_production_date','Production Date','en'),(1073,'label_draft','Draft','en'),(1074,'label_approved','Approved','en'),(1075,'label_voided','Voided','en'),(1076,'label_approve','Approve','en'),(1077,'label_void','Void','en'),(1078,'label_total_amount','Total Amount','en'),(1079,'label_paid_amount','Paid Amount','en'),(1080,'label_purchase_items','Purchase Items','en'),(1081,'label_add_item','Add Item','en'),(1082,'label_quantity','Quantity','en'),(1083,'confirm_delete_purchase','Are you sure you want to delete this purchase?','en'),(1084,'confirm_void_purchase','Are you sure you want to void this purchase?','en'),(1085,'confirm_approve_purchase','Are you sure you want to approve this purchase? This action cannot be undone.','en'),(1086,'msg_purchase_created','Purchase created successfully','en'),(1087,'msg_purchase_updated','Purchase updated successfully','en'),(1088,'msg_purchase_deleted','Purchase deleted successfully','en'),(1089,'label_item','Item','en'),(1090,'label_qti','QTI','en'),(1091,'label_supplier_account_report','Supplier Account Report','en'),(1092,'label_select_supplier','Select Supplier','en'),(1093,'label_balance','Balance','en'),(1094,'label_total','Total','en'),(1095,'label_order_items','Order Items','en'),(1096,'label_offer','Offer','en'),(1097,'label_add_another','Add Another','en'),(1098,'label_add_and_close','Add & Close','en'),(1099,'msg_select_variant','Please select a variant','en'),(1100,'label_place_order','Place Order','en'),(1101,'label_quick_order','Quick Order','en'),(1102,'label_waiter','Waiter','en'),(1103,'label_select_waiter','Select Waiter','en'),(1104,'label_select_table','Select Table','en'),(1105,'label_select_kitchen','Select Kitchen','en'),(1106,'msg_fill_required_fields','Please fill all required fields','en'),(1107,'label_seats','Seats','en'),(1108,'label_select_third_party_customer','Select Third Party Customer','en'),(1109,'label_vat','VAT','en'),(1110,'label_sub_total','Sub Total','en'),(1111,'label_select_payment_method','Select Payment Method','en'),(1112,'label_on_going_orders','Ongoing Orders','en'),(1113,'label_kitchen_status','Kitchen Status','en'),(1114,'label_today_orders','Today\'s Orders','en'),(1115,'label_cash_counters','Cash Counters','en'),(1116,'label_cash_counter','Cash Counter','en'),(1117,'label_add_cash_counter','Add Cash Counter','en'),(1118,'label_manage_cash_counters','Manage Cash Counters','en'),(1119,'label_cash_counter_details','Cash Counter Details','en'),(1120,'confirm_delete_cash_counter','Are you sure you want to delete this cash counter?','en'),(1121,'label_number','Number','en'),(1122,'label_cash_registers','Cash Registers','en'),(1123,'label_cash_register','Cash Register','en'),(1124,'label_open_cash_register','Open Cash Register','en'),(1125,'label_close_cash_register','Close Cash Register','en'),(1126,'label_opening_balance','Opening Balance','en'),(1127,'label_closing_balance','Closing Balance','en'),(1128,'label_opening_note','Opening Note','en'),(1129,'label_closing_note','Closing Note','en'),(1130,'label_open','Open','en'),(1131,'label_closed','Closed','en'),(1132,'label_close','Close','en'),(1133,'msg_cash_register_fetched','Cash register fetched successfully','en'),(1134,'msg_no_open_cash_register','No open cash register found','en'),(1135,'msg_cash_register_opened','Cash register opened successfully','en'),(1136,'msg_cash_register_closed','Cash register closed successfully','en'),(1137,'label_orders_management','Orders Management','en'),(1138,'label_allow_credit','Allow Credit','en'),(1139,'msg_parent_account_code_not_provided','Parent account code not provided','en'),(1140,'label_account_code','Account Code','en'),(1141,'label_account_name','Account Name','en'),(1142,'label_manage_accounts','Manage Accounts','en'),(1143,'label_view_as_tree','View as Tree','en'),(1144,'label_view_as_flat','View as Flat','en'),(1145,'label_account_nature','Account Nature','en'),(1146,'label_account_balance','Account Balance','en'),(1147,'label_account_status','Account Status','en'),(1148,'label_sub_account','Sub Account / Transaction','en'),(1149,'label_main_account','Main Account / Summary','en'),(1150,'phrase_accounting_cash_sale','Proof of cash sales for invoice number: ','en'),(1151,'phrase_accounting_credit_sale','Proof of credit sales for invoice number: ','en'),(1152,'phrase_accounting_purchase_cash','Proof of cash purchase of raw materials for invoice number: ','en'),(1153,'phrase_accounting_purchase_credit','Proof of credit purchase of raw materials for invoice number: ','en'),(1154,'phrase_accounting_supplier_payment','Payment to supplier under payment voucher number: ','en'),(1155,'phrase_accounting_salary_payment','Payment of employee salaries for month: ','en'),(1156,'phrase_accounting_customer_collection','Collection from customer under receipt voucher number: ','en'),(1157,'error_accounting_parent_cannot_transact','Cannot transact on a parent account: ','en'),(1158,'error_accounting_account_not_found','Financial account not found with ID: {0}','en'),(1159,'error_accounting_unbalanced_entry','Accounting error: Unbalanced entry! Total debit: {0} and total credit: {1}','en'),(1160,'error_accounting_missing_code','Financial account not defined in the system with code: {0}','en'),(1161,'label_remaining','Remaining','en'),(1162,'label_cash','Cash','en'),(1163,'label_entry_number','Entry Number','en'),(1164,'label_reference','Reference','en'),(1165,'label_cost_centers','Cost Centers','en'),(1166,'label_cost_center','Cost Center','en'),(1167,'label_add_cost_center','Add Cost Center','en'),(1168,'label_manage_cost_centers','Manage Cost Centers','en'),(1169,'label_cost_center_details','Cost Center Details','en'),(1170,'confirm_delete_cost_center','Are you sure you want to delete this cost center?','en'),(1171,'label_code','Code','en'),(1172,'label_fiscal_periods','Fiscal Periods','en'),(1173,'label_manage_fiscal_periods','Manage Fiscal Periods','en'),(1174,'label_year','Year','en'),(1175,'label_month','Month','en'),(1176,'label_start_date','Start Date','en'),(1177,'label_end_date','End Date','en'),(1178,'label_locked','Locked','en'),(1179,'label_locked_by','Locked By','en'),(1180,'label_locked_at','Locked At','en'),(1181,'label_lock_period','Lock Period','en'),(1182,'label_unlock_period','Unlock Period','en'),(1183,'label_select_year','Select Year','en'),(1184,'confirm_lock_period','Are you sure you want to lock this period?','en'),(1185,'confirm_unlock_period','Are you sure you want to unlock this period?','en'),(1186,'label_manual_journal_entry','Manual Journal Entry','en'),(1187,'label_journal_items','Journal Items','en'),(1188,'label_entry_date','Entry Date','en'),(1189,'label_add_line','Add Line','en'),(1190,'label_account','Account','en'),(1191,'label_debit','Debit','en'),(1192,'label_credit','Credit','en'),(1193,'label_select_account','Select Account','en'),(1194,'label_select_cost_center','Select Cost Center','en'),(1195,'label_balanced','Balanced','en'),(1196,'label_not_balanced','Not Balanced','en'),(1197,'label_reset','Reset','en'),(1198,'label_submit','Submit','en'),(1199,'label_general_ledger','General Ledger','en'),(1200,'label_account_type','Account Type','en'),(1201,'label_running_balance','Running Balance','en'),(1202,'label_date','Date','en'),(1203,'label_generate','Generate','en'),(1204,'label_trial_balance','Trial Balance','en'),(1205,'label_profit_loss','Profit & Loss','en'),(1206,'label_revenue','Revenue','en'),(1207,'label_total_revenue','Total Revenue','en'),(1208,'label_cost_of_goods_sold','Cost of Goods Sold','en'),(1209,'label_total_cogs','Total COGS','en'),(1210,'label_gross_profit','Gross Profit','en'),(1211,'label_operating_expenses','Operating Expenses','en'),(1212,'label_total_operating_expenses','Total Operating Expenses','en'),(1213,'label_net_profit','Net Profit','en'),(1214,'label_balance_sheet','Balance Sheet','en'),(1215,'label_as_of_date','As of Date','en'),(1216,'label_assets','Assets','en'),(1217,'label_total_assets','Total Assets','en'),(1218,'label_liabilities','Liabilities','en'),(1219,'label_total_liabilities','Total Liabilities','en'),(1220,'label_equity','Equity','en'),(1221,'label_total_equity','Total Equity','en'),(1222,'label_total_liabilities_and_equity','Total Liabilities & Equity','en'),(1223,'label_year_end_closing','Year-End Closing','en'),(1224,'label_fiscal_year','Fiscal Year','en'),(1225,'label_perform_closing','Perform Year-End Closing','en'),(1226,'label_closing_entry_number','Closing Entry Number','en'),(1227,'label_closing_date','Closing Date','en'),(1228,'label_total_expenses','Total Expenses','en'),(1229,'label_net_income','Net Income','en'),(1230,'label_accounts_closed','Accounts Closed','en'),(1231,'confirm_year_end_closing','Are you sure you want to perform year-end closing? This action cannot be undone.','en'),(1232,'label_financial_reports','Financial Reports','en'),(1233,'label_journal_entries','Journal Entries','en'),(1234,'label_enter_financial_document','Enter Financial Document (Cash / Receipt / Settlement)','en'),(1235,'label_transaction_type','Transaction Type','en'),(1236,'label_source_account','Source Account','en'),(1237,'label_destination_account','Destination Account','en'),(1238,'enum_SUPPLIER_PAYMENT','Supplier Payment','en'),(1239,'enum_CUSTOMER_COLLECTION','Customer Collection','en'),(1240,'enum_OPERATIONAL_EXPENSE','Operational Expense','en'),(1241,'enum_EMPLOYEE_ADVANCE','Employee Advance','en'),(1242,'enum_SALARY_PAYMENT','Salary Payment','en'),(1243,'enum_CAPITAL_INJECTION','Capital Injection','en'),(1244,'enum_INTERNAL_TRANSFER','Internal Transfer','en'),(1245,'label_financial_document_details','Financial Document Details','en'),(1246,'label_transaction_date','Transaction Date','en'),(1247,'label_select_transaction_type','Select Transaction Type','en'),(1248,'label_select_source_account','Select Source Account','en'),(1249,'label_select_destination_account','Select Destination Account','en'),(1250,'label_order_number','Order #','en'),(1251,'label_order_type','Order Type','en'),(1252,'label_order_info','Order Info','en'),(1253,'label_orders','Orders','en'),(1254,'label_created_at','Created At','en'),(1255,'label_complete','Complete','en'),(1256,'label_checkout','Checkout','en'),(1257,'label_filter_by_status','Filter by Status','en'),(1258,'label_all','All','en'),(1259,'label_merge_orders','Merge Orders','en'),(1260,'label_split_order','Split Order','en'),(1261,'label_split','Split','en'),(1262,'label_tracking','Tracking','en'),(1263,'label_kitchen_progress','Kitchen Progress','en'),(1264,'label_kitchen_orders','Kitchen Orders','en'),(1265,'label_items_ready','Items Ready','en'),(1266,'label_no_kitchen_data','No kitchen data available','en'),(1267,'label_no_tracking_data','No tracking data available','en'),(1268,'label_auto_refresh_5s','Auto-refreshes every 5s','en'),(1269,'label_variant','Variant','en'),(1270,'label_change','Change','en'),(1271,'label_view','View','en'),(1272,'label_refresh','Refresh','en'),(1273,'label_kitchen_dashboard','Kitchen Dashboard','en'),(1274,'label_pending','Pending','en'),(1275,'label_accepted','Accepted','en'),(1276,'label_preparing','Preparing','en'),(1277,'label_ready','Ready','en'),(1278,'label_no_orders','No orders','en'),(1279,'label_accept','Accept','en'),(1280,'label_reject','Reject','en'),(1281,'label_prepare','Prepare','en'),(1282,'label_mark_ready','Mark Ready','en'),(1283,'label_item_ready','Item Ready','en'),(1284,'label_item_served','Item Served','en'),(1285,'label_group','Group','en'),(1286,'label_add_group','Add Group','en'),(1287,'msg_split_order_hint','Assign quantities of each item to each group. Groups with all-zero quantities will be ignored.','en'),(1288,'confirm_complete_order','Are you sure you want to mark this order as complete?','en'),(1289,'confirm_delete_order','Are you sure you want to delete this order?','en'),(1290,'confirm_merge_orders','Are you sure you want to merge the selected orders?','en'),(1291,'confirm_remove_item','Are you sure you want to remove this item from the order?','en'),(1292,'label_delivery_module','Delivery','en'),(1293,'label_deliveries','Deliveries','en'),(1294,'label_delivery','Delivery','en'),(1295,'label_deliveries_management','Deliveries Management','en'),(1296,'label_drivers_management','Drivers Management','en'),(1297,'label_drivers','Drivers','en'),(1298,'label_driver','Driver','en'),(1299,'label_create_delivery','Create Delivery','en'),(1300,'label_delivery_info','Delivery Info','en'),(1301,'label_driver_info','Driver Info','en'),(1302,'label_driver_profile','Driver Profile','en'),(1303,'label_delivery_fee','Delivery Fee','en'),(1304,'label_estimated_delivery_time','Estimated Delivery Time','en'),(1305,'label_actual_delivery_time','Actual Delivery Time','en'),(1306,'label_assign_driver','Assign Driver','en'),(1307,'label_reassign_driver','Reassign Driver','en'),(1308,'label_complete_delivery','Complete Delivery','en'),(1309,'label_cancel_delivery','Cancel Delivery','en'),(1310,'label_cancel_reason','Cancellation Reason','en'),(1311,'label_reassign_reason_placeholder','Reason for reassignment (required)','en'),(1312,'label_reason_required','Reason is required','en'),(1313,'label_vehicle_type','Vehicle Type','en'),(1314,'label_vehicle_plate','Vehicle Plate','en'),(1315,'label_assigned_at','Assigned At','en'),(1316,'label_accepted_at','Accepted At','en'),(1317,'label_picked_up_at','Picked Up At','en'),(1318,'label_delivered_at','Delivered At','en'),(1319,'label_updated_at','Updated At','en'),(1320,'label_no_driver_assigned','No driver assigned yet','en'),(1321,'label_no_available_drivers','No available drivers at this time','en'),(1322,'label_no_deliveries','No deliveries found','en'),(1323,'label_no_drivers','No drivers found','en'),(1324,'label_no_active_delivery','No active delivery','en'),(1325,'label_no_delivery_history','No delivery history','en'),(1326,'label_select_driver','Select Driver','en'),(1327,'label_status_timeline','Status Timeline','en'),(1328,'label_status_created','Created','en'),(1329,'label_status_assigned','Driver Assigned','en'),(1330,'label_status_accepted','Driver Accepted','en'),(1331,'label_status_arrived','Arrived at Restaurant','en'),(1332,'label_status_picked_up','Picked Up','en'),(1333,'label_status_on_the_way','On the Way','en'),(1334,'label_status_delivered','Delivered','en'),(1335,'label_status_cancelled','Cancelled','en'),(1336,'label_status_failed','Failed','en'),(1337,'label_live_tracking','Live Tracking','en'),(1338,'label_auto_refresh_10s','Auto-refreshes every 10s','en'),(1339,'label_open_in_google_maps','Open in Google Maps','en'),(1340,'label_latitude','Latitude','en'),(1341,'label_longitude','Longitude','en'),(1342,'label_speed','Speed','en'),(1343,'label_heading','Heading','en'),(1344,'label_gps_trail','GPS Trail','en'),(1345,'label_points','points','en'),(1346,'label_is_online','Is Online','en'),(1347,'label_change_status','Change Status','en'),(1348,'label_current_delivery','Current Active Delivery','en'),(1349,'label_delivery_history','Delivery History','en'),(1350,'label_view_delivery','View Delivery','en'),(1351,'label_order_id','Order ID','en'),(1352,'label_loading','Loading...','en'),(1353,'label_optional','Optional','en'),(1354,'label_reason','Reason','en'),(1355,'confirm_complete_delivery','Are you sure you want to mark this delivery as completed?','en'),(1356,'msg_order_not_delivery_type','This order is not a delivery order','en'),(1357,'msg_delivery_already_exists','A delivery already exists for this order','en'),(1358,'msg_driver_not_available','Driver is not available for assignment','en'),(1359,'msg_driver_has_active_delivery','Driver already has an active delivery','en'),(1360,'msg_delivery_in_terminal_state','This delivery is already closed','en'),(1361,'msg_order_not_ready_for_pickup','The order is not ready for pickup yet','en'),(1362,'msg_driver_busy_cannot_change_status','Cannot change status while driver has an active delivery','en'),(1363,'msg_no_active_delivery','Driver has no active delivery','en'),(1364,'msg_delivery_not_found','Delivery not found','en'),(1365,'msg_driver_not_found','Driver not found','en'),(1366,'restaurantSalesRevenue','ايرادات مبيعات المطعم','ar'),(1367,'msg_order_checked_out','تم دفع تكلفة الطلب بنجاح','ar'),(1368,'label_cash_payment','دفع نقدي','ar'),(1369,'PENDING','معلق','ar'),(1370,'ACCEPTED','تم القبول','ar'),(1371,'REJECTED','تم الرفض','ar'),(1372,'PREPARING','قيد التحضير','ar'),(1373,'READY','جاهز','ar'),(1374,'SERVED','تم التقديم','ar'),(1375,'NEW','جديد','ar'),(1376,'CONFIRMED','تم التأكيد','ar'),(1377,'IN_PROGRESS','جاري التقديم','ar'),(1379,'COMPLETED','مكتمل','ar'),(1380,'CHECKED_OUT','مدفوع','ar'),(1381,'CANCELLED','ملغي','ar'),(1382,'MERGED','مدمج','ar'),(1383,'SPLIT','مقسم','ar'),(1384,'PLACE_ORDER','طلب مقدم','ar'),(1385,'QUICK_ORDER','طلب سريع','ar'),(1386,'CASH','نقدي','ar'),(1387,'DEBIT','اّجل','ar'),(1388,'label_showing','عرض','ar'),(1389,'label_to','إلي','ar'),(1390,'label_of','من','ar'),(1391,'msg_order_created','تم انشاء الطلب','ar'),(1392,'msg_kitchen_order_accepted','تم قبول الطلب في المطبخ','ar'),(1393,'last_24_hours','اّخر 24 ساعة','ar'),(1394,'last_week','اّخر اسبوع','ar'),(1395,'label_notes','ملاحظات','ar'),(1396,'label_pos_invoice','فاتورة نقطة البيع','ar'),(1397,'label_print_pos','طباعة فاتورة نقطة البيع','ar'),(1398,'msg_delivery_firstname_required','الاسم الأول مطلوب','ar'),(1399,'msg_delivery_firstname_required','First name is required','en'),(1400,'msg_delivery_lastname_required','اسم العائلة مطلوب','ar'),(1401,'msg_delivery_lastname_required','Last name is required','en'),(1402,'msg_delivery_email_required','البريد الإلكتروني مطلوب','ar'),(1403,'msg_delivery_email_required','Email is required','en'),(1404,'msg_delivery_email_invalid','البريد الإلكتروني غير صالح','ar'),(1405,'msg_delivery_email_invalid','Email is invalid','en'),(1406,'msg_delivery_password_min','يجب أن تكون كلمة المرور 6 أحرف على الأقل','ar'),(1407,'msg_delivery_password_min','Password must be at least 6 characters','en'),(1408,'msg_delivery_phone_required','رقم الهاتف مطلوب','ar'),(1409,'msg_delivery_phone_required','Phone number is required','en'),(1410,'msg_delivery_email_exists','البريد الإلكتروني مستخدم بالفعل','ar'),(1411,'msg_delivery_email_exists','Email already exists','en'),(1412,'msg_delivery_password_required','كلمة المرور مطلوبة','ar'),(1413,'msg_delivery_password_required','Password is required','en'),(1414,'msg_delivery_not_found','لم يتم العثور على بيانات التوصيل','ar'),(1415,'msg_delivery_not_found','Delivery details not found','en'),(1416,'msg_delivery_role_not_found','لم يتم العثور على دور التوصيل','ar'),(1417,'msg_delivery_role_not_found','Delivery role not found','en'),(1419,'msg_delivery_created','Delivery person created successfully','en'),(1420,'msg_delivery_fetched','تم جلب بيانات التوصيل بنجاح','ar'),(1421,'msg_delivery_fetched','Delivery details fetched successfully','en'),(1422,'msg_deliveries_fetched','تم جلب قائمة التوصيل بنجاح','ar'),(1423,'msg_deliveries_fetched','Deliveries fetched successfully','en'),(1425,'msg_delivery_updated','Delivery details updated successfully','en'),(1426,'msg_delivery_status_updated','تم تحديث حالة التوصيل بنجاح','ar'),(1427,'msg_delivery_status_updated','Delivery status updated successfully','en'),(1429,'msg_delivery_deleted','Delivery person deleted successfully','en'),(1430,'label_delivery_persons','مندوبو التوصيل','ar'),(1431,'label_manage_delivery_persons','إدارة مندوبي التوصيل','ar'),(1432,'label_add_delivery_person','إضافة مندوب توصيل','ar'),(1433,'label_delivery_person_details','تفاصيل مندوب التوصيل','ar'),(1434,'label_no_delivery_persons','لم يتم العثور على مندوبي توصيل','ar'),(1435,'label_firstname','الاسم الأول','ar'),(1436,'label_lastname','اسم العائلة','ar'),(1437,'label_vehicle_number','رقم المركبة','ar'),(1438,'label_select_vehicle_type','اختر نوع المركبة','ar'),(1439,'label_password_keep_current','اتركه فارغاً للاحتفاظ بكلمة المرور الحالية','ar'),(1440,'label_vehicle_motorcycle','دراجة نارية','ar'),(1441,'label_vehicle_bicycle','دراجة هوائية','ar'),(1442,'label_vehicle_car','سيارة','ar'),(1443,'label_vehicle_van','شاحنة صغيرة','ar'),(1444,'label_vehicle_scooter','سكوتر','ar'),(1445,'label_vehicle_on_foot','سيراً على الأقدام','ar'),(1446,'confirm_delete_delivery','هل أنت متأكد من حذف مندوب التوصيل هذا؟ سيتم أيضاً حذف حساب المستخدم المرتبط به.','ar'),(1447,'msg_delivery_syncs_user_account','يتم مزامنة الاسم الأول واسم العائلة والبريد الإلكتروني والحالة مع حساب المستخدم.','ar'),(1448,'msg_delivery_created','تم إنشاء مندوب التوصيل بنجاح','ar'),(1449,'msg_delivery_updated','تم تحديث بيانات مندوب التوصيل بنجاح','ar'),(1450,'msg_delivery_deleted','تم حذف مندوب التوصيل بنجاح','ar'),(1452,'label_delivery_persons','Delivery Persons','en'),(1453,'label_manage_delivery_persons','Manage Delivery Persons','en'),(1454,'label_add_delivery_person','Add Delivery Person','en'),(1455,'label_delivery_person_details','Delivery Person Details','en'),(1456,'label_no_delivery_persons','No delivery persons found','en'),(1457,'label_firstname','First Name','en'),(1458,'label_lastname','Last Name','en'),(1459,'label_vehicle_number','Vehicle Number','en'),(1460,'label_select_vehicle_type','Select Vehicle Type','en'),(1461,'label_password_keep_current','Leave blank to keep current','en'),(1462,'label_vehicle_motorcycle','Motorcycle','en'),(1463,'label_vehicle_bicycle','Bicycle','en'),(1464,'label_vehicle_car','Car','en'),(1465,'label_vehicle_van','Van','en'),(1466,'label_vehicle_scooter','Scooter','en'),(1467,'label_vehicle_on_foot','On Foot','en'),(1468,'confirm_delete_delivery','Are you sure you want to delete this delivery person? The linked user account will also be removed.','en'),(1469,'msg_delivery_syncs_user_account','First name, last name, email and status are synced to the user account.','en'),(1470,'msg_delivery_created','Delivery person created successfully','en'),(1471,'msg_delivery_updated','Delivery person updated successfully','en'),(1472,'msg_delivery_deleted','Delivery person deleted successfully','en'),(1473,'msg_delivery_email_exists','A user with this email already exists','en'),(1474,'label_delivery_person','عامل التوصيل','ar'),(1475,'label_select_delivery_person','اختر عامل التوصيل','ar'),(1476,'label_delivery_cost','تكلفة التوصيل','ar'),(1477,'label_delivery_address','عنوان التوصيل','ar'),(1478,'label_delivery_order','طلب توصيل','ar'),(1479,'label_delivery_person','Delivery Person','en'),(1480,'label_select_delivery_person','Select Delivery Person','en'),(1481,'label_delivery_cost','Delivery Cost','en'),(1482,'label_delivery_address','Delivery Address','en'),(1483,'label_delivery_order','Delivery Order','en'),(1484,'label_customer_info','Customer Info','en'),(1485,'label_customer_phone','Phone','en'),(1486,'label_customer_email','Email','en'),(1487,'label_customer_address','Address','en'),(1488,'label_customer_info','معلومات العميل','ar'),(1489,'label_customer_phone','الهاتف','ar'),(1490,'label_customer_email','البريد الإلكتروني','ar'),(1491,'label_customer_address','العنوان','ar'),(1492,'DELIVERY_ORDER','طلب توصيل','ar'),(1499,'label_vehicle_motorcycle','Motorcycle','en'),(1500,'label_vehicle_bicycle','Bicycle','en'),(1501,'label_vehicle_car','Car','en'),(1502,'label_vehicle_van','Van','en'),(1503,'label_vehicle_scooter','Scooter','en'),(1504,'label_vehicle_on_foot','On Foot','en'),(1505,'label_delivery_orders','طلبات دليفري','ar');
/*!40000 ALTER TABLE `language_translations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `languages`
--

DROP TABLE IF EXISTS `languages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `languages` (
  `language_code` varchar(10) COLLATE utf8mb4_general_ci NOT NULL,
  `is_default` bit(1) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`language_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `languages`
--

LOCK TABLES `languages` WRITE;
/*!40000 ALTER TABLE `languages` DISABLE KEYS */;
INSERT INTO `languages` VALUES ('ar',_binary '\0','العربية'),('en',_binary '\0','English');
/*!40000 ALTER TABLE `languages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_items`
--

DROP TABLE IF EXISTS `menu_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_report` bit(1) DEFAULT NULL,
  `page_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `module_id` bigint DEFAULT NULL,
  `parent_menu_id` bigint DEFAULT NULL,
  `is_page` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfrgdq53ghir6drbve15ph2i1j` (`module_id`),
  KEY `FK5ooodaxc9xyofpuwjpc5no8ea` (`parent_menu_id`),
  CONSTRAINT `FK5ooodaxc9xyofpuwjpc5no8ea` FOREIGN KEY (`parent_menu_id`) REFERENCES `menu_items` (`id`),
  CONSTRAINT `FKfrgdq53ghir6drbve15ph2i1j` FOREIGN KEY (`module_id`) REFERENCES `modules` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_items`
--

LOCK TABLES `menu_items` WRITE;
/*!40000 ALTER TABLE `menu_items` DISABLE KEYS */;
INSERT INTO `menu_items` VALUES (1,NULL,'accounts-list','accounts',1,NULL,_binary ''),(4,NULL,'users-list','users',2,NULL,_binary ''),(8,NULL,'roles-list','roles',2,NULL,_binary ''),(9,NULL,'settings','settings',3,NULL,_binary ''),(10,NULL,'units-of-measurment','units_of_measurment',3,NULL,_binary ''),(11,NULL,'floors','floors',3,NULL,_binary ''),(12,NULL,'tables','tables',3,NULL,_binary ''),(13,NULL,'item-category','item_categories',5,NULL,_binary ''),(14,NULL,'menu-type','menu_types',5,NULL,_binary ''),(15,NULL,'item-food','item_foods',5,NULL,_binary ''),(16,NULL,'kitchens','kitchens',3,NULL,_binary ''),(17,NULL,'item-food-variant','item_food_variants',5,NULL,_binary ''),(18,NULL,'item-food-add-ons','item_food_add_ons',5,NULL,_binary ''),(19,NULL,'item-food-add-ons-association','item_food_add_ons_association',5,NULL,_binary ''),(20,NULL,'languages','languages',3,NULL,_binary ''),(21,NULL,'language-translation','language_translation',3,NULL,_binary ''),(22,NULL,'currencies','currencies',3,NULL,_binary ''),(23,NULL,'application-settings','application_settings',3,NULL,_binary ''),(25,NULL,'customer-types','customer_types',3,NULL,_binary ''),(26,NULL,'customers','customers',3,NULL,_binary ''),(27,NULL,'third-party-customers','third_party_customers',3,NULL,_binary ''),(28,NULL,'suppliers','suppliers',6,NULL,_binary ''),(29,NULL,'ingredients','ingredients',6,NULL,_binary ''),(30,NULL,'purchases','purchases',6,NULL,_binary ''),(31,NULL,'counters','counters',7,NULL,_binary ''),(32,NULL,'cash-register','cash_registers',7,NULL,_binary ''),(33,NULL,'orders','orders',7,NULL,_binary ''),(34,NULL,'kitchen-dashboard','kitchen_dashboard',7,NULL,_binary ''),(35,NULL,'delivery','delivery',8,NULL,_binary '');
/*!40000 ALTER TABLE `menu_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_types`
--

DROP TABLE IF EXISTS `menu_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu_types` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `image` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `status` bit(1) NOT NULL,
  `created_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkbxfiy05540om8gladm6v59lm` (`created_by`),
  KEY `FK7yorif2cqd55cqvpa1br8kqka` (`deleted_by`),
  KEY `FK5ch4ma989cqdec3rb7cvj8mt5` (`updated_by`),
  CONSTRAINT `FK5ch4ma989cqdec3rb7cvj8mt5` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FK7yorif2cqd55cqvpa1br8kqka` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKkbxfiy05540om8gladm6v59lm` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_types`
--

LOCK TABLES `menu_types` WRITE;
/*!40000 ALTER TABLE `menu_types` DISABLE KEYS */;
INSERT INTO `menu_types` VALUES (1,'2026-06-09 05:57:21.314093',NULL,'2026-06-09 05:57:21.314093','/files/food/edca02c3-4b77-43a3-8813-daa2bec1d417.avif','حفلة',_binary '',1,NULL,1),(2,'2026-06-09 06:07:59.292506',NULL,'2026-06-09 06:07:59.292506','/files/food/ec17dbd5-6688-4e0d-bf3d-9f3a1d48b0de.jpg','افطار',_binary '',1,NULL,1),(3,'2026-06-13 12:43:53.101894',NULL,'2026-06-13 12:43:53.101894',NULL,'غذاء',_binary '',1,NULL,1);
/*!40000 ALTER TABLE `menu_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `module_permissions`
--

DROP TABLE IF EXISTS `module_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `module_permissions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `can_create` bit(1) DEFAULT NULL,
  `can_delete` bit(1) DEFAULT NULL,
  `can_read` bit(1) DEFAULT NULL,
  `can_update` bit(1) DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `module_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2rpkv4pfo8hgbvxpict42plxu` (`created_by`),
  KEY `FK8a9wpobtrkp9ox4gtp2tontb4` (`deleted_by`),
  KEY `FKequt2mfp4wb925e86kxbqf3gl` (`updated_by`),
  KEY `FKpq780008uwkt096jhxpijtl5n` (`module_id`),
  KEY `FKsa6gqqhie9unq1prtdqrlrvn2` (`user_id`),
  CONSTRAINT `FK2rpkv4pfo8hgbvxpict42plxu` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FK8a9wpobtrkp9ox4gtp2tontb4` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKequt2mfp4wb925e86kxbqf3gl` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKpq780008uwkt096jhxpijtl5n` FOREIGN KEY (`module_id`) REFERENCES `modules` (`id`),
  CONSTRAINT `FKsa6gqqhie9unq1prtdqrlrvn2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `module_permissions`
--

LOCK TABLES `module_permissions` WRITE;
/*!40000 ALTER TABLE `module_permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `module_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `modules`
--

DROP TABLE IF EXISTS `modules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `modules` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `icon` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `status` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modules`
--

LOCK TABLES `modules` WRITE;
/*!40000 ALTER TABLE `modules` DISABLE KEYS */;
INSERT INTO `modules` VALUES (1,'Accounts module','pi-money-bill','accounts',_binary ''),(2,'Users And Permissions','pi-users','users_and_permissions',_binary ''),(3,'Settings module','pi-setting','settings',_binary ''),(4,'Reservations module','pi-time','reservations',_binary ''),(5,'Food Management module','pi-crown','item_management',_binary ''),(6,'Purchase','pi-cart','purchase',_binary ''),(7,'Order Management','pi-file','orders',_binary ''),(8,'Delivery','pi-car','delivery',_binary '');
/*!40000 ALTER TABLE `modules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_delivery_addresses`
--

DROP TABLE IF EXISTS `order_delivery_addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_delivery_addresses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `additional_notes` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `apartment_number` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `building_number` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `city` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `district` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `floor_number` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `label` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `latitude` decimal(10,7) DEFAULT NULL,
  `longitude` decimal(10,7) DEFAULT NULL,
  `street_address` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlqay0fhocy1ahfgu43ql2ryld` (`order_id`),
  CONSTRAINT `FK7bblpr3u8c8vnsv10ohs38ucy` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_delivery_addresses`
--

LOCK TABLES `order_delivery_addresses` WRITE;
/*!40000 ALTER TABLE `order_delivery_addresses` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_delivery_addresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item_add_ons`
--

DROP TABLE IF EXISTS `order_item_add_ons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item_add_ons` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `add_on_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `price` double NOT NULL,
  `add_on_id` bigint NOT NULL,
  `order_item_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpusylh66nlqy5oxw7kv6an22u` (`add_on_id`),
  KEY `FKn9endrcalhjkgpv7a7tki6p1c` (`order_item_id`),
  CONSTRAINT `FKn9endrcalhjkgpv7a7tki6p1c` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`),
  CONSTRAINT `FKpusylh66nlqy5oxw7kv6an22u` FOREIGN KEY (`add_on_id`) REFERENCES `item_food_add_ons` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item_add_ons`
--

LOCK TABLES `order_item_add_ons` WRITE;
/*!40000 ALTER TABLE `order_item_add_ons` DISABLE KEYS */;
INSERT INTO `order_item_add_ons` VALUES (1,'كاتشب',10,1,6),(2,'تومية',15,2,7),(3,'مخلل',5,4,7);
/*!40000 ALTER TABLE `order_item_add_ons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `add_ons_price` double NOT NULL,
  `item_food_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `notes` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `price` double NOT NULL,
  `quantity` int NOT NULL,
  `total_price` double NOT NULL,
  `variant_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `item_food_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `variant_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn6dl46i7jxo2un67geoo8kwyy` (`item_food_id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  KEY `FK49k13tducjbmb2hpu13442971` (`variant_id`),
  CONSTRAINT `FK49k13tducjbmb2hpu13442971` FOREIGN KEY (`variant_id`) REFERENCES `item_food_variants` (`id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKn6dl46i7jxo2un67geoo8kwyy` FOREIGN KEY (`item_food_id`) REFERENCES `item_foods` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,0,'شاورما فراخ',NULL,50,1,50,'صغير',3,1,1),(2,0,'شاورمة  لحمة',NULL,100,2,200,'كبير',4,2,9),(3,0,'برجر بيف',NULL,50,1,50,'صغير',5,3,6),(4,0,'شاورمة  لحمة',NULL,100,1,100,'كبير',4,4,9),(5,0,'شاي',NULL,20,1,20,'عادي',2,5,12),(6,10,'برجر بيف',NULL,60,2,140,'صغير',5,6,6),(7,20,'شاورمة  لحمة',NULL,120,2,280,'كبير',4,6,9),(32,0,'شاورما فراخ',NULL,50,2,100,'صغير',3,21,1);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `completed_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `customer_type` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `notes` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `order_number` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `order_type` enum('PLACE_ORDER','QUICK_ORDER','DELIVERY_ORDER') COLLATE utf8mb4_general_ci NOT NULL,
  `parent_order_id` bigint DEFAULT NULL,
  `status` enum('CANCELLED','CHECKED_OUT','COMPLETED','CONFIRMED','IN_PROGRESS','MERGED','NEW','READY','SPLIT') COLLATE utf8mb4_general_ci NOT NULL,
  `total_amount` double NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cash_register_id` bigint DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `table_id` bigint DEFAULT NULL,
  `third_party_customer_id` bigint DEFAULT NULL,
  `waiter_id` bigint DEFAULT NULL,
  `delivered_at` datetime(6) DEFAULT NULL,
  `delivery_address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `delivery_cost` double DEFAULT NULL,
  `delivery_person_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnthkiu7pgmnqnu86i2jyoe2v7` (`order_number`),
  KEY `FKeoe6j0o3sechqo9rw4bhu7556` (`cash_register_id`),
  KEY `FKpxtb8awmi0dk6smoh2vp1litg` (`customer_id`),
  KEY `FKrkhrp1dape261t3x3spj7l5ny` (`table_id`),
  KEY `FKlr11ybwy3jy7hw9eiau30jcg9` (`third_party_customer_id`),
  KEY `FKtjfxa8chebq9a07kocso7l5o1` (`waiter_id`),
  KEY `FK5ux3vi13eppqkmqu507gh74j` (`delivery_person_id`),
  CONSTRAINT `FK5ux3vi13eppqkmqu507gh74j` FOREIGN KEY (`delivery_person_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKeoe6j0o3sechqo9rw4bhu7556` FOREIGN KEY (`cash_register_id`) REFERENCES `cash_registers` (`id`),
  CONSTRAINT `FKlr11ybwy3jy7hw9eiau30jcg9` FOREIGN KEY (`third_party_customer_id`) REFERENCES `customers` (`id`),
  CONSTRAINT `FKpxtb8awmi0dk6smoh2vp1litg` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
  CONSTRAINT `FKrkhrp1dape261t3x3spj7l5ny` FOREIGN KEY (`table_id`) REFERENCES `tables` (`id`),
  CONSTRAINT `FKtjfxa8chebq9a07kocso7l5o1` FOREIGN KEY (`waiter_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2026-09-09 10:06:37.246192','2026-09-09 09:55:04.368532','TAKEAWAY_CUSTOMER',NULL,'ORD-20260909-0001','PLACE_ORDER',NULL,'CHECKED_OUT',50,'2026-09-09 10:06:43.870651',3,NULL,1,NULL,1,NULL,NULL,NULL,NULL),(2,'2026-09-10 07:27:30.192193','2026-09-10 07:00:43.031850','TAKEAWAY_CUSTOMER',NULL,'ORD-20260910-0002','QUICK_ORDER',NULL,'CHECKED_OUT',200,'2026-09-10 07:27:42.607501',3,NULL,1,NULL,1,NULL,NULL,NULL,NULL),(3,'2026-09-10 07:27:32.541905','2026-09-10 07:01:33.656814','TAKEAWAY_CUSTOMER',NULL,'ORD-20260910-0003','QUICK_ORDER',NULL,'CHECKED_OUT',50,'2026-09-10 07:27:45.524312',3,NULL,1,NULL,1,NULL,NULL,NULL,NULL),(4,'2026-09-10 07:27:35.010723','2026-09-10 07:01:44.377753','TAKEAWAY_CUSTOMER',NULL,'ORD-20260910-0004','QUICK_ORDER',NULL,'CHECKED_OUT',100,'2026-09-10 07:27:48.339001',3,NULL,1,NULL,1,NULL,NULL,NULL,NULL),(5,'2026-09-10 07:27:38.190992','2026-09-10 07:04:32.652923','TAKEAWAY_CUSTOMER',NULL,'ORD-20260910-0005','PLACE_ORDER',NULL,'CHECKED_OUT',20,'2026-09-10 07:27:50.172015',3,NULL,1,NULL,1,NULL,NULL,NULL,NULL),(6,NULL,'2026-09-14 09:47:01.795146','TAKEAWAY_CUSTOMER',NULL,'ORD-20260914-0006','PLACE_ORDER',NULL,'NEW',420,'2026-09-14 09:47:01.816923',3,NULL,1,NULL,1,NULL,NULL,NULL,NULL),(21,NULL,'2026-09-21 07:52:50.113384','ONLINE_CUSTOMER',NULL,'ORD-20260921-0021','DELIVERY_ORDER',NULL,'NEW',100,'2026-09-21 08:33:16.101863',3,1,NULL,NULL,NULL,NULL,'كافية عايدة',20,7);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `change_amount` double NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `paid_amount` double NOT NULL,
  `payment_method` enum('CARD','CASH','MIXED') COLLATE utf8mb4_general_ci NOT NULL,
  `remaining_amount` double NOT NULL,
  `status` enum('PAID','PARTIALLY_PAID','PENDING','REFUNDED') COLLATE utf8mb4_general_ci NOT NULL,
  `total_amount` double NOT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK81gagumt0r8y3rmudcgpbk42l` (`order_id`),
  CONSTRAINT `FK81gagumt0r8y3rmudcgpbk42l` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,0,'2026-07-02 13:06:17.356531',130,'CASH',0,'PAID',130,1);
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_items`
--

DROP TABLE IF EXISTS `purchase_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `price` double NOT NULL,
  `quantity` double NOT NULL,
  `created_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `ingredient_id` bigint DEFAULT NULL,
  `purchase_id` bigint DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `production_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK634od11gvyjmj9e4o5jnxi7a9` (`created_by`),
  KEY `FKe0ycsejxno3bcsxesqkuliisq` (`deleted_by`),
  KEY `FK5mdv7kx675xet1w30965kqw9c` (`updated_by`),
  KEY `FKtirdm77a2eq3e7wv28okmmlnm` (`ingredient_id`),
  KEY `FKhcski0jcuja0o3vhb7o15yqvi` (`purchase_id`),
  CONSTRAINT `FK5mdv7kx675xet1w30965kqw9c` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FK634od11gvyjmj9e4o5jnxi7a9` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKe0ycsejxno3bcsxesqkuliisq` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKhcski0jcuja0o3vhb7o15yqvi` FOREIGN KEY (`purchase_id`) REFERENCES `purchases` (`id`),
  CONSTRAINT `FKtirdm77a2eq3e7wv28okmmlnm` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_items`
--

LOCK TABLES `purchase_items` WRITE;
/*!40000 ALTER TABLE `purchase_items` DISABLE KEYS */;
INSERT INTO `purchase_items` VALUES (18,'2026-08-11 09:51:55.612961',NULL,'2026-08-11 09:51:55.612961',10,1,1,NULL,1,1,18,NULL,NULL),(19,'2026-08-24 07:06:41.822168',NULL,'2026-08-24 07:06:41.822168',50,1,1,NULL,1,1,19,'2026-09-29','2026-08-23'),(20,'2026-08-24 07:06:41.822168',NULL,'2026-08-24 07:06:41.822168',30,1,1,NULL,1,6,19,'2026-08-30','2026-08-23'),(21,'2026-08-24 07:25:47.972525',NULL,'2026-08-24 07:25:47.972525',30,1,1,NULL,1,4,20,'2026-08-30','2026-08-23'),(22,'2026-08-24 08:31:01.312527',NULL,'2026-08-24 08:31:01.312527',60,1,1,NULL,1,4,21,'2026-08-30','2026-08-23'),(23,'2026-09-14 09:10:51.574272',NULL,'2026-09-14 09:10:51.574272',50,1,1,NULL,1,4,22,NULL,NULL);
/*!40000 ALTER TABLE `purchase_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchases`
--

DROP TABLE IF EXISTS `purchases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchases` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `invoice_number` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `note` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `paid_amount` double NOT NULL,
  `purchase_date` date NOT NULL,
  `total_amount` double NOT NULL,
  `created_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `supplier_id` bigint DEFAULT NULL,
  `payment_method` tinyint DEFAULT NULL,
  `status` enum('APPROVED','DRAFT','VOIDED') COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_supplier_invoice` (`supplier_id`,`invoice_number`),
  KEY `FKal92jfnl4o3ueqvq8t6ivfdml` (`created_by`),
  KEY `FK6ycjfmeshq8sa9hokxn8vo7uh` (`deleted_by`),
  KEY `FK3l0lyflx704ef5kesbtky257g` (`updated_by`),
  CONSTRAINT `FK3l0lyflx704ef5kesbtky257g` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FK6ycjfmeshq8sa9hokxn8vo7uh` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FK9ho3w23v5du4x0hrp6rqs1wmh` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`),
  CONSTRAINT `FKal92jfnl4o3ueqvq8t6ivfdml` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  CONSTRAINT `purchases_chk_1` CHECK ((`payment_method` between 0 and 1))
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchases`
--

LOCK TABLES `purchases` WRITE;
/*!40000 ALTER TABLE `purchases` DISABLE KEYS */;
INSERT INTO `purchases` VALUES (18,'2026-08-11 09:51:55.612961',NULL,'2026-08-11 09:51:55.612961',NULL,'1','',5,'2026-08-10',10,1,NULL,1,1,1,'APPROVED'),(19,'2026-08-24 07:06:41.553682',NULL,'2026-08-24 07:14:16.347531',NULL,'234','تم الدفع نقدي',80,'2026-08-23',80,1,NULL,1,1,0,'APPROVED'),(20,'2026-08-24 07:25:47.741622',NULL,'2026-08-24 08:20:14.836085',NULL,'963','',30,'2026-08-23',30,1,NULL,1,1,0,'APPROVED'),(21,'2026-08-24 08:20:46.589492',NULL,'2026-08-24 08:31:14.676734',NULL,'99','',60,'2026-08-23',60,1,NULL,1,1,0,'APPROVED'),(22,'2026-09-14 09:10:51.514337',NULL,'2026-09-14 09:10:51.514337',NULL,'121221','',50,'2026-09-14',50,1,NULL,1,1,0,'DRAFT');
/*!40000 ALTER TABLE `purchases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permissions`
--

DROP TABLE IF EXISTS `role_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_permissions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `can_create` bit(1) DEFAULT NULL,
  `can_delete` bit(1) DEFAULT NULL,
  `can_edit` bit(1) DEFAULT NULL,
  `can_read` bit(1) DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `menu_id` bigint DEFAULT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK85wlhl8dq1ktvxxnyyf6b4v91` (`created_by`),
  KEY `FK2jnr0kibf99rhnajro9jqjgo` (`deleted_by`),
  KEY `FK9se21y2575tfqh2yj9qslwo9c` (`updated_by`),
  KEY `FKgf9qdbmvr9a0sghaset4nc4to` (`menu_id`),
  KEY `FKn5fotdgk8d1xvo8nav9uv3muc` (`role_id`),
  CONSTRAINT `FK2jnr0kibf99rhnajro9jqjgo` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FK85wlhl8dq1ktvxxnyyf6b4v91` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FK9se21y2575tfqh2yj9qslwo9c` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKgf9qdbmvr9a0sghaset4nc4to` FOREIGN KEY (`menu_id`) REFERENCES `menu_items` (`id`),
  CONSTRAINT `FKn5fotdgk8d1xvo8nav9uv3muc` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permissions`
--

LOCK TABLES `role_permissions` WRITE;
/*!40000 ALTER TABLE `role_permissions` DISABLE KEYS */;
INSERT INTO `role_permissions` VALUES (3,NULL,NULL,NULL,_binary '',_binary '',_binary '',_binary '',1,NULL,NULL,1,1),(7,NULL,NULL,'2026-06-11 10:48:44.038729',_binary '',_binary '',_binary '',_binary '',1,NULL,1,4,1),(11,NULL,NULL,'2026-06-08 07:13:59.581123',_binary '',_binary '\0',_binary '',_binary '',1,NULL,1,1,3),(12,'2026-06-08 07:12:12.999785',NULL,'2026-06-08 07:13:56.364906',_binary '\0',_binary '\0',_binary '\0',_binary '\0',1,NULL,1,4,3),(13,'2026-06-08 07:12:15.149573',NULL,'2026-06-08 07:13:57.546648',_binary '\0',_binary '\0',_binary '\0',_binary '\0',1,NULL,1,8,3),(14,'2026-06-08 07:12:49.131231',NULL,'2026-06-08 07:13:18.944474',_binary '',_binary '',_binary '',_binary '',1,NULL,1,8,1),(15,'2026-06-08 07:12:49.808835',NULL,'2026-06-08 07:12:49.808835',_binary '\0',_binary '\0',_binary '\0',_binary '',1,NULL,1,8,1),(16,'2026-06-08 07:12:50.426837',NULL,'2026-06-08 07:12:50.426837',_binary '\0',_binary '\0',_binary '',_binary '\0',1,NULL,1,8,1),(17,'2026-06-08 07:12:50.943052',NULL,'2026-06-08 07:12:50.943052',_binary '\0',_binary '',_binary '\0',_binary '\0',1,NULL,1,8,1),(18,'2026-06-08 07:13:40.267746',NULL,'2026-06-08 07:13:40.267746',_binary '\0',_binary '\0',_binary '\0',_binary '',1,NULL,1,1,2),(33,'2026-06-09 09:20:45.959340',NULL,'2026-06-09 10:11:53.710882',_binary '',_binary '',_binary '',_binary '',1,NULL,1,13,1),(34,'2026-06-09 10:15:12.954776',NULL,'2026-06-09 10:15:15.910804',_binary '',_binary '',_binary '',_binary '',1,NULL,1,15,1),(35,'2026-06-09 10:17:28.840752',NULL,'2026-06-09 10:17:32.888089',_binary '',_binary '',_binary '',_binary '',1,NULL,1,14,1),(36,'2026-06-09 10:38:04.882352',NULL,'2026-06-09 10:38:08.516693',_binary '',_binary '',_binary '',_binary '',1,NULL,1,10,1),(37,'2026-06-09 10:38:07.581514',NULL,'2026-06-17 21:47:31.715033',_binary '',_binary '',_binary '',_binary '',1,NULL,1,11,1),(38,'2026-06-09 10:38:10.183630',NULL,'2026-06-09 10:38:15.878693',_binary '',_binary '',_binary '',_binary '',1,NULL,1,12,1),(39,'2026-06-09 10:38:10.666768',NULL,'2026-06-09 10:38:17.063550',_binary '',_binary '',_binary '',_binary '',1,NULL,1,16,1),(40,'2026-06-09 11:18:35.294898',NULL,'2026-06-09 11:18:38.898681',_binary '',_binary '',_binary '',_binary '',1,NULL,1,9,1),(41,'2026-06-09 11:45:59.666159',NULL,'2026-06-13 12:35:02.831175',_binary '',_binary '',_binary '',_binary '',1,NULL,1,17,1),(42,'2026-06-09 11:46:03.572848',NULL,'2026-06-13 12:35:03.232227',_binary '',_binary '',_binary '',_binary '',1,NULL,1,18,1),(43,'2026-06-09 11:46:04.076795',NULL,'2026-06-13 12:35:03.666728',_binary '',_binary '',_binary '',_binary '',1,NULL,1,19,1),(44,'2026-06-11 10:38:35.420770',NULL,'2026-06-11 10:38:38.069497',_binary '',_binary '',_binary '',_binary '',1,NULL,1,20,1),(45,'2026-06-11 10:38:38.672151',NULL,'2026-06-11 10:38:43.602660',_binary '',_binary '',_binary '',_binary '',1,NULL,1,21,1),(46,'2026-06-11 10:38:39.319472',NULL,'2026-06-11 10:38:44.001696',_binary '',_binary '',_binary '',_binary '',1,NULL,1,22,1),(47,'2026-06-11 10:38:39.874953',NULL,'2026-06-11 10:38:44.625482',_binary '',_binary '',_binary '',_binary '',1,NULL,1,23,1),(49,'2026-06-11 15:15:45.742551',NULL,'2026-06-11 15:15:52.724032',_binary '',_binary '',_binary '',_binary '',1,NULL,1,25,1),(50,'2026-06-11 15:15:46.304449',NULL,'2026-06-11 15:15:53.107642',_binary '',_binary '',_binary '',_binary '',1,NULL,1,26,1),(51,'2026-06-11 15:15:46.892039',NULL,'2026-06-11 15:15:53.556394',_binary '',_binary '',_binary '',_binary '',1,NULL,1,27,1),(52,'2026-06-17 21:31:03.410436',NULL,'2026-06-17 21:31:10.894947',_binary '',_binary '',_binary '',_binary '',1,NULL,1,28,1),(53,'2026-06-17 21:31:04.367203',NULL,'2026-06-17 21:31:10.448826',_binary '',_binary '',_binary '',_binary '',1,NULL,1,29,1),(54,'2026-06-17 21:31:05.805304',NULL,'2026-06-17 21:31:10.027129',_binary '',_binary '',_binary '',_binary '',1,NULL,1,30,1),(55,'2026-06-19 20:59:50.362321',NULL,'2026-06-19 20:59:54.119109',_binary '',_binary '',_binary '',_binary '',1,NULL,1,31,1),(56,'2026-06-19 21:22:01.398235',NULL,'2026-06-19 22:05:35.531328',_binary '',_binary '',_binary '',_binary '',1,NULL,1,32,1),(57,'2026-07-13 06:43:48.280807',NULL,'2026-07-13 06:43:52.513657',_binary '',_binary '',_binary '',_binary '',1,NULL,1,33,1),(58,'2026-07-13 06:48:36.967964',NULL,'2026-07-13 06:50:16.117064',_binary '',_binary '',_binary '',_binary '',1,NULL,1,34,1),(59,'2026-09-21 06:22:37.237413',NULL,'2026-09-21 06:22:40.468840',_binary '',_binary '',_binary '',_binary '',1,NULL,1,35,1);
/*!40000 ALTER TABLE `role_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `status` bit(1) DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`),
  KEY `FKq6ium4se7bjk3mfbj3qm1gvy` (`created_by`),
  KEY `FKgmr3wlthm6g7ohu0afmpk2jpe` (`deleted_by`),
  KEY `FKf0p4aw14esgr0ukams27qfl3m` (`updated_by`),
  CONSTRAINT `FKf0p4aw14esgr0ukams27qfl3m` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKgmr3wlthm6g7ohu0afmpk2jpe` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKq6ium4se7bjk3mfbj3qm1gvy` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,NULL,NULL,NULL,'admin role','ADMIN',_binary '',1,1,1),(2,'2026-06-08 04:19:59.727090',NULL,'2026-06-08 04:19:59.727090','Waiter role','WAITER',_binary '',1,NULL,1),(3,'2026-06-08 04:22:50.483932',NULL,'2026-06-08 04:22:50.483932','Accountant role','ACCOUNTANT',_binary '',1,NULL,1),(4,'2026-09-21 05:55:41.205629',NULL,'2026-09-21 05:55:41.205629','Delivery Role','DELIVERY',_binary '',1,NULL,1);
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier_ledger`
--

DROP TABLE IF EXISTS `supplier_ledger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier_ledger` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `credit` double NOT NULL,
  `debit` double NOT NULL,
  `reference_id` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `running_balance` double NOT NULL,
  `transaction_date` date NOT NULL,
  `transaction_type` enum('PURCHASE','PURCHASE_EDIT','VOIDED') COLLATE utf8mb4_general_ci NOT NULL,
  `supplier_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm0hkff7y45lx6jf6melr38xws` (`supplier_id`),
  CONSTRAINT `FKm0hkff7y45lx6jf6melr38xws` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier_ledger`
--

LOCK TABLES `supplier_ledger` WRITE;
/*!40000 ALTER TABLE `supplier_ledger` DISABLE KEYS */;
INSERT INTO `supplier_ledger` VALUES (1,0,0,'234',0,'2026-08-24','PURCHASE',1),(2,0,0,'963',0,'2026-08-24','PURCHASE',1),(3,0,0,'99',0,'2026-08-24','PURCHASE',1);
/*!40000 ALTER TABLE `supplier_ledger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` bit(1) NOT NULL,
  `created_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `account_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8mmtdyputbwgoumfisqbgafrh` (`account_id`),
  KEY `FKgu047dtwm26rhj7oayva2vq2j` (`created_by`),
  KEY `FK6yc6gvoo2ow0gotu8xab9wl7d` (`deleted_by`),
  KEY `FK64l4218ejnuika4iynq69b6b` (`updated_by`),
  CONSTRAINT `FK55vrkvhhkoemvgv2ru6s0ckma` FOREIGN KEY (`account_id`) REFERENCES `acc_accounts` (`id`),
  CONSTRAINT `FK64l4218ejnuika4iynq69b6b` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FK6yc6gvoo2ow0gotu8xab9wl7d` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKgu047dtwm26rhj7oayva2vq2j` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
INSERT INTO `suppliers` VALUES (1,'2026-08-11 09:32:49.350858',NULL,'2026-08-11 09:33:10.867917','','qwe@gmail.com','الحاج اسماعيل','',_binary '',1,NULL,1,20);
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tables`
--

DROP TABLE IF EXISTS `tables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tables` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `capacity` int NOT NULL,
  `icon` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `status` bit(1) NOT NULL,
  `floor_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6yqyqnlvrju0arcto25k939r6` (`floor_id`),
  CONSTRAINT `FK6yqyqnlvrju0arcto25k939r6` FOREIGN KEY (`floor_id`) REFERENCES `floors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tables`
--

LOCK TABLES `tables` WRITE;
/*!40000 ALTER TABLE `tables` DISABLE KEYS */;
INSERT INTO `tables` VALUES (1,4,'','1',_binary '',1),(2,3,'','2',_binary '',1),(3,3,'','3',_binary '',1);
/*!40000 ALTER TABLE `tables` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `third_party_customers`
--

DROP TABLE IF EXISTS `third_party_customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `third_party_customers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `commission_percentage` double NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `third_party_customers`
--

LOCK TABLES `third_party_customers` WRITE;
/*!40000 ALTER TABLE `third_party_customers` DISABLE KEYS */;
INSERT INTO `third_party_customers` VALUES (1,'HORAS',0,'horas@gmail.com','شركة حورس','0234564654');
/*!40000 ALTER TABLE `third_party_customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unit_of_measurements`
--

DROP TABLE IF EXISTS `unit_of_measurements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `unit_of_measurements` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `short_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unit_of_measurements`
--

LOCK TABLES `unit_of_measurements` WRITE;
/*!40000 ALTER TABLE `unit_of_measurements` DISABLE KEYS */;
INSERT INTO `unit_of_measurements` VALUES (1,'كيلو','KG',_binary ''),(3,'جرام','g',_binary ''),(4,'لتر','لتر',_binary ''),(5,'صفيحة','صفيحة',_binary '');
/*!40000 ALTER TABLE `unit_of_measurements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `about` varchar(2000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `counter` int DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `firstname` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `image` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ip_address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_admin` bit(1) DEFAULT NULL,
  `last_login` datetime(6) DEFAULT NULL,
  `last_logout` datetime(6) DEFAULT NULL,
  `lastname` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `status` bit(1) DEFAULT NULL,
  `waiter_kitchen_token` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  KEY `FKibk1e3kaxy5sfyeekp8hbhnim` (`created_by`),
  KEY `FKtd2l28q3oe9v164nps61hxt1f` (`deleted_by`),
  KEY `FKci7xr690rvyv3bnfappbyh8x0` (`updated_by`),
  CONSTRAINT `FKci7xr690rvyv3bnfappbyh8x0` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKibk1e3kaxy5sfyeekp8hbhnim` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKtd2l28q3oe9v164nps61hxt1f` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,NULL,NULL,'2026-09-21 05:01:18.224765','admin',NULL,'admin@gmail.com','Mohamed','/files/users/87f060e2-8c9c-483f-9683-ef1214f3ffa6.webp','0:0:0:0:0:0:0:1',NULL,'2026-09-21 05:01:18.195643',NULL,'Talaat','$2a$10$OyNjkZwCPdLFqvn67/mMQudseWQxpc3WIhX/nuoKGgMHeat0ECwdy',_binary '',NULL,1,NULL,1),(2,'2026-06-05 17:21:40.904549',NULL,'2026-06-08 07:33:45.935378','Second Admin',NULL,'ahmed@gmail.com','Ahmed','/files/users/e3f8d420-ce8e-4a11-9a80-90e8f3382e33.jpg',NULL,NULL,NULL,NULL,'Hassan','$2a$10$kl5XYkG.w38wuOcAmGCkEOD1WtmkVLpHm5Bvs0swb.tA4FI2QHxHG',_binary '',NULL,1,NULL,1),(3,'2026-06-05 17:24:26.574257',NULL,'2026-06-06 14:51:56.841999','Mostafa',NULL,'mostafa@gmail.com','Mostafa','/files/users/f44a7a1f-6a7e-4401-b381-49838c987124.webp',NULL,NULL,NULL,NULL,'Mohamed','$2a$10$3pJu9GDpPNQDJ1OADY067eQlOk8roPfvHO5/sRO2Hk5uLjHM2z02q',_binary '',NULL,1,NULL,1),(4,'2026-06-05 17:26:18.023422',NULL,'2026-06-08 07:24:05.246700','waiter',NULL,'asd@gmail.com','Moiewr','/files/users/faf23c72-3560-4c65-9386-78a85ca9cb4f.jpg',NULL,NULL,NULL,NULL,'wer','$2a$10$hc.WIO/QuoNSLOc/AGlYy.iGqv6py/6.a0fsEBXpVVni34Jo4d7VG',_binary '\0',NULL,1,NULL,1),(7,'2026-09-21 05:58:08.779494',NULL,'2026-09-21 06:29:03.191656','دليفري',NULL,'hassan@gmail.com','حسن','/files/users/cf340476-5f1c-4eb0-bcf1-3174595caf6d.jpg',NULL,NULL,NULL,NULL,'محمود','$2a$10$AHno0F1U2zGCDY.yL02hUOXQbSc0UekoE7y.qnjkOyaMunRiPYdOC',_binary '',NULL,1,NULL,1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `role_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsvc7fhrhxplsbs5cca2anie4k` (`user_id`,`role_id`),
  UNIQUE KEY `UKq3r1u8cne2rw2hkr899xuh7vj` (`user_id`,`role_id`),
  KEY `FKj5srd0rhvia9w675owp1n5v90` (`created_by`),
  KEY `FKlewabm7sdba978q6txbgj8p3r` (`deleted_by`),
  KEY `FKal0co82v09qy59blbquo33qm6` (`updated_by`),
  KEY `FK86ydimwn2k7ra6clfya3eqyqu` (`role_id`),
  CONSTRAINT `FK3pnc797k9enha0td21jk7mjvt` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK86ydimwn2k7ra6clfya3eqyqu` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKal0co82v09qy59blbquo33qm6` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKj5srd0rhvia9w675owp1n5v90` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKlewabm7sdba978q6txbgj8p3r` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_roles`
--

LOCK TABLES `users_roles` WRITE;
/*!40000 ALTER TABLE `users_roles` DISABLE KEYS */;
INSERT INTO `users_roles` VALUES (5,NULL,NULL,NULL,1,NULL,1,1,1),(7,'2026-06-08 07:24:05.246700',NULL,'2026-06-08 07:24:05.246700',1,NULL,1,2,4),(13,'2026-06-08 07:33:45.910844',NULL,'2026-06-08 07:33:45.910844',1,NULL,1,1,2),(16,'2026-09-21 05:58:08.779494',NULL,'2026-09-21 05:58:08.779494',1,NULL,1,4,7);
/*!40000 ALTER TABLE `users_roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-22 13:52:04
