create database if not exists `blog-tracker`;
use `blog-tracker`;

drop table if exists `user_like`;
drop table if exists `likes`;
drop table if exists `comments`;
drop table if exists `role`;
drop table if exists `like`;
drop table if exists `category_post`;
drop table if exists `post`;
drop table if exists `user`;
drop table if exists `category`;

create table `user`(
  `id` int auto_increment not null,
    `first_name` varchar(50) not null,
    `last_name` varchar(50) not null,
    `email` varchar(50) not null,
    `username` varchar(50) not null,
    `password` varchar(50) not null,
    `bio` varchar(400),
    `registered` date,
    primary key (`id`),
    unique(`username`),
    unique(`email`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

create table `role`(
  `username` varchar(50) not null,
    `role` varchar(50) not null,
    primary key(`username`, `role`),
    foreign key(`username`) references `user` (`username`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

create table `post`(
  `id` int not null auto_increment,
    `author_id` int not null,
    `title` varchar(50) not null,
    `meta_title` varchar(500) not null,
    `slug` varchar(50) not null,
    `photo` longblob,
    `published` datetime,
    `content` text,
    primary key(`id`),
    foreign key(`author_id`) references `user` (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

create table `comments` (
  `id` int not null auto_increment,
    `comment` varchar(1000),
    `post_id` int not null,
    `user_id` int not null,
    `published_date` date,
     primary key(`id`),
     foreign key(`post_id`) references `post`(`id`),
     foreign key(`user_id`) references `user`(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

create table `category`(
	`id` int not null unique,
	`title` varchar(50),
    primary key(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

create table `category_post` (
	`post_id` int not null unique,
    `category_id` int not null unique,
    primary key(`post_id`, `category_id`),
    foreign key(`post_id`) references `post` (`id`),
    foreign key(`category_id`) references `category` (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


create table `like`(
	`id` int not null auto_increment,
    `post_id` int not null,
    primary key(`id`),
    foreign key(`post_id`) references `post` (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

create table `user_like` (
	`user_id` int not null unique,
	`like_id` int not null unique,
    primary key(`user_id`, `like_id`),
    foreign key(`user_id`) references `user` (`id`),
    foreign key(`like_id`) references `like` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
