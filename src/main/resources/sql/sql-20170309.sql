CREATE DATABASE `sy_ext`;

CREATE TABLE `tbl_user` (
  `id` bigint(20) NOT NULL COMMENT '唯一ID',
  `number` varchar(16) NOT NULL COMMENT '编号',
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `photo` varchar(128) NOT NULL COMMENT '照片',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';

CREATE TABLE `tbl_fingerprint` (
  `id` bigint(20) NOT NULL COMMENT '唯一ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `finger` varchar(16) NOT NULL COMMENT '手指名称',
  `template` varchar(3072) NOT NULL COMMENT '指纹模板',
  `enroll_time` datetime DEFAULT NULL COMMENT '登记时间',
  `identify_time` datetime DEFAULT NULL COMMENT '(最后)辨识时间',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='指纹';

CREATE TABLE `tbl_counter` (
  `id` bigint(20) NOT NULL COMMENT '唯一ID',
  `number` varchar(16) NOT NULL COMMENT '编号',
  `name` varchar(32) NOT NULL COMMENT '名称',
  `mac` varchar(16) NOT NULL COMMENT 'MAC地址',
  `ip` varchar(16) NOT NULL COMMENT 'IP地址',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='柜台';

CREATE TABLE `tbl_session` (
  `id` bigint(20) NOT NULL COMMENT '唯一ID',
  `counter_id` bigint(20) NOT NULL COMMENT '柜台ID',
  `queue` varchar(32) NOT NULL COMMENT '消息队列(名称)',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态',
  `online_time` datetime DEFAULT NULL COMMENT '上线时间',
  `offline_time` datetime DEFAULT NULL COMMENT '下线时间',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会话';

CREATE TABLE `tbl_message` (
  `id` bigint(20) NOT NULL COMMENT '唯一ID',
  `counter_id` bigint(20) NOT NULL COMMENT '柜台ID',
  `session_id` bigint(20) NOT NULL COMMENT '会话ID',
  `type` varchar(32) NOT NULL COMMENT '类型',
  `extra` varchar(128) NOT NULL COMMENT '附加信息',
  `send_time` datetime(3) DEFAULT NULL COMMENT '发送时间',
  `ack_time` datetime(3) DEFAULT NULL COMMENT '确认时间',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息';

CREATE TABLE `tbl_setting` (
  `id` bigint(20) NOT NULL COMMENT '唯一ID',
  `parent` bigint(20) NOT NULL COMMENT '父ID',
  `key` varchar(32) NOT NULL COMMENT '配置键',
  `value` varchar(32) DEFAULT NULL COMMENT '配置值',
  `reg_exp` varchar(64) DEFAULT NULL COMMENT '校验正则',
  `description` varchar(32) NOT NULL COMMENT '描述',
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设置';

CREATE TABLE `tbl_resource` (
  `id` bigint(20) NOT NULL COMMENT '唯一ID',
  `type` varchar(16) NOT NULL COMMENT '类型',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `path` varchar(128) NOT NULL COMMENT '路径',
  `md5` varchar(32) NOT NULL COMMENT 'MD5',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源';

CREATE TABLE `tbl_log` (
  `id` bigint(20) NOT NULL COMMENT '唯一ID',
  `platform` varchar(16) NOT NULL COMMENT '客户端平台',
  `version` varchar(16) NOT NULL COMMENT '客户端版本',
  `primary_category` varchar(64) NOT NULL COMMENT '一级类别',
  `secondary_category` varchar(64) NOT NULL COMMENT '二级类别',
  `content` varchar(512) NOT NULL COMMENT '内容',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime NOT NULL COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日志';