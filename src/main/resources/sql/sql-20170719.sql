CREATE DATABASE IF NOT EXISTS `sy_ext` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `sy_ext`;

--
-- Table structure for table `tbl_user`
--
DROP TABLE IF EXISTS `tbl_user`;
CREATE TABLE `tbl_user` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `number` varchar(16) NOT NULL COMMENT '编号',
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `photo` varchar(128) DEFAULT NULL COMMENT '照片',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_number` (`number`),
  FULLTEXT KEY `idx_keyword` (`number`,`name`) WITH PARSER `ngram`
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';

--
-- Table structure for table `tbl_fingerprint`
--
DROP TABLE IF EXISTS `tbl_fingerprint`;
CREATE TABLE `tbl_fingerprint` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `uid` int(11) NOT NULL COMMENT 'UID',
  `finger` tinyint(4) NOT NULL COMMENT '手指',
  `template` varchar(3072) NOT NULL COMMENT '模板',
  `enroll_time` datetime DEFAULT NULL COMMENT '登记时间',
  `identify_time` datetime DEFAULT NULL COMMENT '最后辨识时间',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='指纹';

--
-- Table structure for table `tbl_counter`
--
DROP TABLE IF EXISTS `tbl_counter`;
CREATE TABLE `tbl_counter` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `number` varchar(16) NOT NULL COMMENT '编号',
  `name` varchar(32) NOT NULL COMMENT '名称',
  `mac` varchar(32) NOT NULL COMMENT 'MAC地址',
  `ip` varchar(16) NOT NULL COMMENT 'IP地址',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_number` (`number`),
  KEY `idx_mac_ip` (`mac`,`ip`),
  FULLTEXT KEY `idx_keyword` (`number`,`name`,`ip`) WITH PARSER `ngram`
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='窗口';

--
-- Table structure for table `tbl_session`
--
DROP TABLE IF EXISTS `tbl_session`;
CREATE TABLE `tbl_session` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `counter_id` bigint(20) NOT NULL COMMENT '窗口ID',
  `token` varchar(32) NOT NULL COMMENT 'Token',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态',
  `online_time` datetime DEFAULT NULL COMMENT '上线时间',
  `offline_time` datetime DEFAULT NULL COMMENT '下线时间',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_counter_id` (`counter_id`),
  KEY `idx_token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会话';

--
-- Table structure for table `tbl_message`
--
DROP TABLE IF EXISTS `tbl_message`;
CREATE TABLE `tbl_message` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `counter_id` bigint(20) NOT NULL COMMENT '窗口ID',
  `session_id` bigint(20) NOT NULL COMMENT '会话ID',
  `uid` bigint(20) NOT NULL COMMENT 'UID',
  `direction` tinyint(4) NOT NULL COMMENT '方向',
  `type` varchar(32) NOT NULL COMMENT '类型',
  `body` varchar(3072) NOT NULL COMMENT '消息体',
  `retry` tinyint(4) NOT NULL DEFAULT '1' COMMENT '重试次数',
  `send_time` datetime(3) DEFAULT NULL COMMENT '发送时间',
  `ack_time` datetime(3) DEFAULT NULL COMMENT '确认时间',
  `receive_time` datetime(3) DEFAULT NULL COMMENT '接收时间',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_uid` (`uid`),
  KEY `idx_counter_id_session_id` (`counter_id`,`session_id`),
  FULLTEXT KEY `idx_keyword` (`body`) WITH PARSER `ngram`
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息';

--
-- Table structure for table `tbl_resource`
--
DROP TABLE IF EXISTS `tbl_resource`;
CREATE TABLE `tbl_resource` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `type` varchar(16) NOT NULL COMMENT '类型',
  `tag` varchar(64) NOT NULL COMMENT '标签',
  `filename` varchar(64) NOT NULL COMMENT '文件名',
  `md5` varchar(32) NOT NULL COMMENT 'MD5',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_type_tag` (`type`,`tag`),
  FULLTEXT KEY `idx_keyword` (`tag`) WITH PARSER `ngram`
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源';

--
-- Table structure for table `tbl_setting`
--
DROP TABLE IF EXISTS `tbl_setting`;
CREATE TABLE `tbl_setting` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `parent_id` bigint(20) NOT NULL COMMENT '父ID',
  `key` varchar(32) NOT NULL COMMENT '键',
  `value` varchar(128) DEFAULT NULL COMMENT '值',
  `reg_exp` varchar(64) DEFAULT NULL COMMENT '校验正则',
  `description` varchar(64) NOT NULL COMMENT '描述',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设置';

--
-- Table structure for table `tbl_log`
--
DROP TABLE IF EXISTS `tbl_log`;
CREATE TABLE `tbl_log` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `type` varchar(32) NOT NULL COMMENT '类型',
  `content` varchar(512) NOT NULL COMMENT '内容',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  FULLTEXT KEY `idx_keyword` (`content`) WITH PARSER `ngram`
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日志';