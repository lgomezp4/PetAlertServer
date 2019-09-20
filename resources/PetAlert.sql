/**
 * Author:  Luis Gómez
 * Created: 02/05/2019
 * Project: Pet Alert  
 */

CREATE USER 'dam1905'@'localhost' IDENTIFIED BY 'Ew5kaer9!';

CREATE DATABASE dam1905
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

GRANT ALL PRIVILEGES ON dam1905.* TO 'dam1905'@'localhost';  

USE dam1905;

CREATE TABLE users (
    id INTEGER PRIMARY KEY auto_increment,
    name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(150) NOT NULL,
    rol VARCHAR(50) DEFAULT 'user',
    token VARCHAR(100) DEFAULT NULL,
    expiration TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sourceIP VARCHAR(15) DEFAULT NULL,
    mail VARCHAR(50) NOT NULL UNIQUE,
    population VARCHAR(50) DEFAULT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT CHK_rol CHECK (rol IN('user', 'admin'))    
) ENGINE=InnoDb;

CREATE TABLE messages (
    id INTEGER PRIMARY KEY auto_increment,
    title VARCHAR(50) NOT NULL,
    content VARCHAR(500) NOT NULL,
    sender_active BOOLEAN NOT NULL DEFAULT TRUE,
    receiver_active BOOLEAN NOT NULL DEFAULT TRUE,
    send_date LONG DEFAULT NULL,
    receipt_date LONG DEFAULT NULL,
    sender_id INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    CONSTRAINT idSender foreign key (sender_id) references users (id) on delete restrict on update cascade,
    CONSTRAINT idReceiver foreign key (receiver_id) references users (id) on delete restrict on update cascade
) ENGINE=InnoDb;

CREATE TABLE coordinates (
    id INTEGER PRIMARY KEY auto_increment,
    latitude DECIMAL(10, 7) NOT NULL,
    longitude DECIMAL(10, 7) NOT NULL    
) ENGINE=InnoDb;

CREATE TABLE animals (
    id INTEGER PRIMARY KEY auto_increment,
    chip_num BIGINT DEFAULT NULL, 
    name VARCHAR(50) DEFAULT NULL,
    kind VARCHAR(50) NOT NULL,
    hair_color VARCHAR(50) NOT NULL,
    race VARCHAR(50) DEFAULT NULL,
    half_blood BOOLEAN DEFAULT FALSE,
    age INTEGER DEFAULT NULL,
    sex VARCHAR(1) NOT NULL,
    image MEDIUMBLOB DEFAULT NULL,
    CONSTRAINT CHK_kind CHECK (kind IN('dog', 'cat', 'other')),
    CONSTRAINT CHK_sex CHECK (sex IN('M', 'F'))  
) ENGINE=InnoDb;

CREATE TABLE descriptions (
    id INTEGER PRIMARY KEY auto_increment,
    title VARCHAR(100) NOT NULL,
    lost_day_hour LONG DEFAULT NULL,
    description VARCHAR(500) NOT NULL,
    phone VARCHAR(50) DEFAULT NULL    
) ENGINE=InnoDb;

CREATE TABLE alerts (
    id INTEGER PRIMARY KEY auto_increment,
    creation_date LONG DEFAULT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    report_number INTEGER DEFAULT 0,
    user_id INTEGER NOT NULL,
    animal_id INTEGER NOT NULL,
    desc_id INTEGER NOT NULL,
    coord_id INTEGER NOT NULL,
    CONSTRAINT idUser foreign key (user_id) references users (id) on delete restrict on update cascade,
    CONSTRAINT idAnimal foreign key (animal_id) references animals (id) on delete restrict on update cascade,
    CONSTRAINT idDesc foreign key (desc_id) references descriptions (id) on delete restrict on update cascade,
    CONSTRAINT idCoord foreign key (coord_id) references coordinates (id) on delete restrict on update cascade
) ENGINE=InnoDb;