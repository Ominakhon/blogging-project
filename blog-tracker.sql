use `blog-tracker`;

DROP VIEW IF EXISTS `post_likes_count`;
DROP VIEW IF EXISTS `post_comments_count`;

DROP TABLE IF EXISTS `like`;
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `category_post`;
DROP TABLE IF EXISTS `tag_post`;
DROP TABLE IF EXISTS `tag`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `post`;
DROP TABLE IF EXISTS `user`;


-- Users Table
create table `user`(
	`id` int auto_increment not null,
	`photo` longblob,
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


-- Posts Table
CREATE TABLE `post` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
	`photo` longblob,
    `title` VARCHAR(255) NOT NULL,
    `status` enum('DRAFT', 'PUBLISHED'),
    `content` TEXT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);

-- Post Categories Table
create table `category`(
	`id` int not null unique AUTO_INCREMENT,
	`title` varchar(50),
    primary key(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

create table `category_post` (
	`post_id` int not null,
    `category_id` int not null,
    primary key(`post_id`, `category_id`),
    foreign key(`post_id`) references `post` (`id`),
    foreign key(`category_id`) references `category` (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

create table `tag`(
	`id` int not null unique AUTO_INCREMENT,
	`title` varchar(50),
    primary key(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

create table `tag_post` (
	`post_id` int not null unique,
    `tag_id` int not null unique,
    primary key(`post_id`, `tag_id`),
    foreign key(`post_id`) references `post` (`id`),
    foreign key(`tag_id`) references `tag` (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- Comments Table
CREATE TABLE `comment` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `post_id` INT NOT NULL,
    `user_id` INT NOT NULL,
    `content` TEXT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`post_id`) REFERENCES `post`(`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);

-- Post Likes Table
CREATE TABLE `like` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `post_id` INT NOT NULL,
    `user_id` INT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (`post_id`, `user_id`),
    FOREIGN KEY (`post_id`) REFERENCES `post`(`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);


-- Post Like Count View
CREATE VIEW `post_likes_count` AS
SELECT
    `post_id`,
    COUNT(*) AS `like_count`
FROM
    `like`
GROUP BY
    `post_id`;

-- Post comment count view
create view `post_comments_count` as
select
	`post_id`,
    count(*) as `comment_count`
from
	`comment`
group by
	`post_id`;
