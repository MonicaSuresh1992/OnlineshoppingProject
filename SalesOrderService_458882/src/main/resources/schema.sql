DROP TABLE IF EXISTS Order_458882;

CREATE TABLE Order_458882
(
    id INTEGER  PRIMARY KEY AUTO_INCREMENT,
    order_date varchar(200) NOT NULL,
    cust_id int(36) DEFAULT NULL,
    order_desc varchar(500) DEFAULT NULL,
    total_price double(500) DEFAULT NULL
);
