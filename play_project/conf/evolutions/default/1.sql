-- ebiznes schema

-- !Ups

CREATE TABLE `People`
(
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `age`  int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
);

CREATE TABLE `Category`
(
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
);

CREATE TABLE `Product`
(
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `price` float NOT NULL,
  `categoryID` bigint NOT NULL,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_Product_Category` (`categoryID`),
  CONSTRAINT `fk_Product_Category` FOREIGN KEY (`categoryID`) REFERENCES `Category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `User`
(
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(500) NOT NULL,
  `password` varchar(500) NOT NULL,
  `lastname` varchar(45)  NOT NULL,
  `firstname` varchar(45)  NOT NULL,
  `phone` varchar(20)  DEFAULT NULL,
  `country` varchar(20)  DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
);

CREATE TABLE `Order`
(
  `id` bigint NOT NULL AUTO_INCREMENT,
  `userId` bigint NOT NULL,
  `address` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Order_User_idx` (`userID`),
  CONSTRAINT `fk_Order_User` FOREIGN KEY (`userID`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `OrderDetail`
(
  `id` bigint NOT NULL AUTO_INCREMENT,
  `orderID` bigint NOT NULL,
  `productID` bigint NOT NULL,
  `quantity` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_OrderDetail_Order_idx` (`orderID`),
  KEY `fk_OrderDetail_Product_idx` (`productID`),
  CONSTRAINT `fk_OrderDetail_Order` FOREIGN KEY (`orderID`) REFERENCES `Order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_OrderDetail_Product` FOREIGN KEY (`productID`) REFERENCES `Product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);



-- !Downs

DROP TABLE if exists People;
DROP TABLE if exists Category;
DROP TABLE if exists Product;
DROP TABLE if exists User;
DROP TABLE if exists `Order`;
DROP TABLE if exists OrderDetail;