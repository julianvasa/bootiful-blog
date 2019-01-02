DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS hibernate_sequence;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS recipes;
DROP TABLE IF EXISTS tags;

CREATE TABLE categories (
  id int(11),
  name varchar(100),
  image varchar(100),
  count int(11)
);

CREATE TABLE hibernate_sequence (
  next_val bigint(20)
);

CREATE TABLE ingredients (
  id int(11) NOT NULL AUTO_INCREMENT,
  recipe_id int(11) NOT NULL,
  name varchar(300),
  quantity float,
  unit varchar(100),
  PRIMARY KEY (id)
);

CREATE TABLE roles (
  id int(11) NOT NULL AUTO_INCREMENT,
  role varchar(255),
  PRIMARY KEY (id)
);

CREATE TABLE users (
  id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(255) NOT NULL,
  fullname varchar(255) NOT NULL,
  password varchar(60) NOT NULL,
  role int(11),
  PRIMARY KEY (id)
);

CREATE TABLE tags (
  recipe_id int(11),
  value varchar(100),
  start_pos int(11),
  end_pos int(11),
  intro_instruction varchar(30),
  id int(11) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
);

CREATE TABLE recipes (
  id int(11) NOT NULL,
  category_id bigint(20),
  name varchar(100),
  intro varchar(1000),
  instruction varchar(5000),
  image varchar(1000),
  link varchar(1000),
  time varchar(100),
  servings varchar(100),
  calories varchar(100),
  favorite smallint(6),
  rating decimal(10,0),
  posted decimal(10,0),
  video varchar(200),
  userid int(11),
  PRIMARY KEY (id)
  );

