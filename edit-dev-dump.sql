/*
SQLyog Community v11.52 (64 bit)
MySQL - 5.6.25 : Database - edit-dev
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`edit-dev` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `edit-dev`;

/*Table structure for table `items` */

DROP TABLE IF EXISTS `items`;

CREATE TABLE `items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) COLLATE utf8_bin NOT NULL,
  `locationX` double DEFAULT NULL,
  `locationY` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `items` */

insert  into `items`(`id`,`title`,`locationX`,`locationY`) values (2,'Item 1',10,20),(3,'Item 2',10,20),(4,'Item 3',10,20),(5,'Item 4',10,20),(6,'Item 5',10,20),(7,'Item 6',10,20),(8,'Item 7',10,20),(9,'Item 8',10,20),(10,'Item 9',10,20),(11,'Item 10',10,20),(12,'Item 11',10,20),(13,'Item 12',10,20),(14,'Item 13',10,20),(15,'Item 14',10,20),(16,'Item 15',10,20),(17,'Item 16',10,20);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
