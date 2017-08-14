CREATE TABLE IF NOT EXISTS `demo` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `column1` varchar(50) DEFAULT '' NOT NULL COMMENT '列名１',
  `column2` decimal(5,2) NOT NULL COMMENT '列名２',
  `column3` decimal(5,2) NOT NULL COMMENT '列名３',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;