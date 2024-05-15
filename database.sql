CREATE DATABASE shopapp;
USE shopapp;
CREATE TABLE `shopapp`.`users` ( `id` INT NOT NULL AUTO_INCREMENT , `fullname` VARCHAR(100) NOT NULL DEFAULT '' , `phone_number` VARCHAR(10) NOT NULL , `address` VARCHAR(200) NOT NULL DEFAULT '' , `password` VARCHAR(100) NOT NULL , `created_at` DATE NOT NULL , `updated_at` DATE NOT NULL , `is_active` TINYINT NOT NULL DEFAULT '1' , `date_of_birth` DATE NOT NULL  , `fb_account_id` INT NOT NULL DEFAULT '0' , `gg_account_id` INT NOT NULL DEFAULT '0' , PRIMARY KEY (`id`)) ENGINE = InnoDB;
CREATE TABLE tokens(
	id int PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE NOT NULL,
    toke_type varchar(50) NOT null,
    expiration_date DATETIME,
    revoked tinyint(1) NOT null,
    expired tinyint(1) NOT null,
    user_id int,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE roles(
    id int PRIMARY KEY,
    name VARCHAR(20) not null
)
alter TABLE users add column role_id int;
alter TABLE users add FOREIGN KEY (role_id) REFERENCES roles(id);
CREATE TABLE social_accounts(
    id INT PRIMARY key AUTO_INCREMENT,
    provider VARCHAR(20) not null,
    provider_id varchar(50) not null,
    email varchar (150) not null,
    name varchar (150) not null ,
    user_id int,
    FOREIGN key (user_id) REFERENCES users(id)
);





CREATE TABLE product_image(
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,

    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_product_image_product_id FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    image_url VARCHAR(255)

);



CREATE TABLE categories(
    id int PRIMARY key AUTO_INCREMENT,
    name varchar(100)not null 
);
CREATE TABLE products(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name varchar(350),
    price FLOAT NOT null ,
    thumbnail varchar(300) ,
    description LONGTEXT ,
    created_at DATETIME,
    updated_at DATETIME,
    category_id int,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
CREATE TABLE orders(
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id int,
    FOREIGN KEY (user_id) REFERENCES users(id),
    fullname VARCHAR(100) DEFAULT '',
    email VARCHAR(100) DEFAULT '',
    phone_number VARCHAR (10) not null,
    address VARCHAR (200) NOT null,
    note VARCHAR (100) DEFAULT '',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20),
    total_money FLOAT
);
alter TABLE orders add column 'shipping_method' VARCHAR(100);
alter TABLE orders add column 'shipping_address' VARCHAR(200);
alter TABLE orders add column 'shipping_date' DATE;
alter TABLE orders add column 'payment_method' VARCHAR(100);
alter TABLE orders add column 'tracking_number' VARCHAR(100);
-- xoá 1 đơn hàng thêm 1 trường active 
alter TABLE orders add column active TINYINT(1);
alter TABLE orders MODIFI column status ENUM('pending','shipped','processing','delivered','cancelled');
CREATE TABLE order_details(
    id int PRIMARY KEY AUTO_INCREMENT,
    order_id int,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    product_id int,
    FOREIGN KEY (product_id) REFERENCES products(id),
    price FLOAT ,
    number_of_products int,
    total_money float,
    color VARCHAR(20) DEFAULT ''
);