create table
CREATE TABLE `question` (
   `id` int(8) NOT NULL AUTO_INCREMENT COMMENT 'ID',
   `code` varchar(16) DEFAULT NULL COMMENT '问题编号',
   `question` varchar(128) DEFAULT NULL COMMENT '问题',
   `answer` varchar(256) DEFAULT NULL COMMENT '回答',
   `keyword` varchar(64) DEFAULT NULL COMMENT '关键词',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8