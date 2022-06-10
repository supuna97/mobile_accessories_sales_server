/*
 Navicat Premium Data Transfer

 Source Server         : Local
 Source Server Type    : MySQL
 Source Server Version : 50712
 Source Host           : localhost:3306
 Source Schema         : sales_db

 Target Server Type    : H2
 Target Server Version : 50712
 File Encoding         : 65001

 Date: 08/02/2021 12:26:57
*/
SET MODE MySQL;
-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `stock_request_detail`;
DROP TABLE IF EXISTS `stock_request`;
DROP TABLE IF EXISTS `vehicle`;
DROP TABLE IF EXISTS `stock`;
DROP TABLE IF EXISTS `branch`;
DROP TABLE IF EXISTS `product`;

CREATE TABLE `product`
(
    `id`         varchar(255) NOT NULL PRIMARY KEY,
    `name`       varchar(255) NOT NULL,
    `status`     int(10)      NOT NULL DEFAULT 1,
    `created_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------
-- Table structure for branch
-- ----------------------------
CREATE TABLE `branch`
(
    `id`         varchar(255) NOT NULL PRIMARY KEY,
    `name`       varchar(255) NOT NULL,
    `address`    varchar(255)          DEFAULT NULL,
    `tel`        varchar(255)          DEFAULT NULL,
    `type`       int(10)      NOT NULL DEFAULT 2,
    `status`     int(10)      NOT NULL DEFAULT 1,
    `created_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------
-- Table structure for stock
-- ----------------------------
CREATE TABLE `stock`
(
    `id`          varchar(255)   NOT NULL PRIMARY KEY,
    `description` varchar(255)            DEFAULT NULL,
    `qty`         int(255)       NOT NULL DEFAULT 0,
    `price`       decimal(10, 2) NOT NULL DEFAULT 0.0,
    `branch_id`   varchar(255)   NOT NULL,
    `product_id`  varchar(255)   NOT NULL,
    `created_at`  timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX bra_ind (branch_id),
    FOREIGN KEY (branch_id)
        REFERENCES branch (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    INDEX pro_ind (product_id),
    FOREIGN KEY (product_id)
        REFERENCES product (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ----------------------------
-- Table structure for vehicle
-- ----------------------------
CREATE TABLE `vehicle`
(
    `id`         varchar(255) NOT NULL PRIMARY KEY,
    `reg_no`     varchar(255) NOT NULL,
    `driver_id`  varchar(255)          DEFAULT NULL,
    `branch_id`  varchar(255) NOT NULL,
    `created_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX veh_bra_ind (branch_id),
    FOREIGN KEY (branch_id)
        REFERENCES branch (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ----------------------------
-- Table structure for stock_request
-- ----------------------------
CREATE TABLE `stock_request`
(
    `id`            varchar(255) NOT NULL PRIMARY KEY,
    `status`        int(10)      NOT NULL DEFAULT 0,
    `by_branch_id`  varchar(255) NOT NULL,
    `for_branch_id` varchar(255) NOT NULL,
    `vehicle_id`    varchar(255)          DEFAULT NULL,
    `created_at`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    INDEX str_by_bra_ind (by_branch_id),
    FOREIGN KEY (by_branch_id)
        REFERENCES branch (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    INDEX str_for_bra_ind (for_branch_id),
    FOREIGN KEY (for_branch_id)
        REFERENCES branch (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    INDEX str_veh_ind (vehicle_id),
    FOREIGN KEY (vehicle_id)
        REFERENCES vehicle (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ----------------------------
-- Table structure for stock_request_detail
-- ----------------------------
CREATE TABLE `stock_request_detail`
(
    `id`               varchar(255) NOT NULL PRIMARY KEY,
    `qty`              int(255)     NOT NULL DEFAULT 0,
    `stock_request_id` varchar(255) NOT NULL,
    `product_id`       varchar(255) NOT NULL,
    INDEX strd_str_ind (stock_request_id),
    FOREIGN KEY (stock_request_id)
        REFERENCES stock_request (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    INDEX strd_pro_ind (product_id),
    FOREIGN KEY (product_id)
        REFERENCES product (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ----------------------------
-- Table structure for user
-- ----------------------------
CREATE TABLE `user`
(
    `id`        varchar(255) NOT NULL PRIMARY KEY,
    `username`  varchar(255) NOT NULL,
    `password`  varchar(255) NOT NULL,
    `user_role` int(255)     NOT NULL,
    `branch_id` varchar(255),
    INDEX user_bra_ind (branch_id),
    FOREIGN KEY (branch_id)
        REFERENCES branch (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product`
VALUES ('12cbc2ca-69d8-11eb-8f8a-a81e849e9ba1', 'Apple', 1, '2021-02-08 11:38:16');
INSERT INTO `product`
VALUES ('12cbc2ca-69d8-11eb-8f8a-a81e849e9ba3', 'Orange', 1, '2021-02-08 11:38:16');
INSERT INTO `product`
VALUES ('12cbc2ca-69d8-11eb-8f8a-a81e849e9ba4', 'Banana', 1, '2021-02-08 12:06:47');
INSERT INTO `product`
VALUES ('12cbc2ca-69d8-11eb-8f8a-a81e849e9ba2', 'Papaya', 1, '2021-02-08 12:16:47');

-- ----------------------------
-- Records of branch
-- ----------------------------
INSERT INTO `branch` (`id`, `name`, `address`, `tel`, `type`, `status`, `created_at`)
VALUES ('323432', 'Colombo Branch', 'Colombo', '0774935895', 2, 1, '2021-02-17 11:38:44');
INSERT INTO `branch` (`id`, `name`, `address`, `tel`, `type`, `status`, `created_at`)
VALUES ('43242324', 'Galle Branch', 'Galle', '0776288969', 2, 1, '2021-02-17 11:53:30');

-- ----------------------------
-- Records of stock
-- ----------------------------
INSERT INTO `stock` (`id`, `description`, `qty`, `price`, `branch_id`, `product_id`, `created_at`)
VALUES ('643344fregt4t', 'Test', 10, 350000.00, '43242324',
        '12cbc2ca-69d8-11eb-8f8a-a81e849e9ba2', '2021-02-18 09:01:03');

INSERT INTO `stock` (`id`, `description`, `qty`, `price`, `branch_id`, `product_id`, `created_at`)
        VALUES ('643344fregt5t', 'Test', 10, 350000.00, '43242324',
        '12cbc2ca-69d8-11eb-8f8a-a81e849e9ba2', '2021-02-18 09:01:03');

-- ----------------------------
-- Records of vehicle
-- ----------------------------
INSERT INTO `vehicle` (`id`, `reg_no`, `driver_id`, `branch_id`, `created_at`)
VALUES ('43432', 'HF-1232', 'D001', '43242324', '2021-02-19 20:55:52');

-- ----------------------------
-- Records of stock_request
-- ----------------------------
INSERT INTO `stock_request` (`id`, `status`, `by_branch_id`, `for_branch_id`, `vehicle_id`, `created_at`, `updated_at`)
VALUES ('fer324324', 1, '323432', '43242324', '43432',
        '2021-02-19 20:51:39', null);

 INSERT INTO `stock_request` (`id`, `status`, `by_branch_id`, `for_branch_id`, `vehicle_id`, `created_at`, `updated_at`)
VALUES ('fer324355', 1, '323432', '43242324', '43432',
        '2021-02-19 20:51:39', null);       

-- ----------------------------
-- Records of stock_request_detail
-- ----------------------------
INSERT INTO `stock_request_detail` (`id`, `qty`, `stock_request_id`, `product_id`)
VALUES ('1212', 10, 'fer324324', '12cbc2ca-69d8-11eb-8f8a-a81e849e9ba1');
INSERT INTO `stock_request_detail` (`id`, `qty`, `stock_request_id`, `product_id`)
VALUES ('4213', 30, 'fer324324', '12cbc2ca-69d8-11eb-8f8a-a81e849e9ba2');

INSERT INTO `stock_request_detail` (`id`, `qty`, `stock_request_id`, `product_id`)
VALUES ('6000', 30, 'fer324355', '12cbc2ca-69d8-11eb-8f8a-a81e849e9ba2');

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` (`id`, `username`, `password`, `user_role`, `branch_id`)
VALUES ('232323', 'super', '$2a$10$D0or8FRf8ANxQ.0jjPxk9euW9tOIdc9yFpTQj5dh8kmKu.LnrDbRq', 1, '43242324');

SELECT o.id AS order_id, o.status AS status, o.total_amount AS total_amount, o.created_at AS order_date, 
                c.name AS customer_name, c.mobile AS customer_mobile, u.username AS sales_agent_name 
                FROM orders o INNER JOIN customer c on o.customer_id=c.id
                INNER JOIN user u on o.sales_rep_id=u.id

                INSERT INTO `user` (`id`, `username`, `password`, `user_role`, `branch_id`)
VALUES ('232345', 'kamal', '$2a$10$D0or8FRf8ANxQ.0jjPxk9euW9tOIdc9yFpTQj5dh8kmKu.LnrDbRq', 3, '43242324');
