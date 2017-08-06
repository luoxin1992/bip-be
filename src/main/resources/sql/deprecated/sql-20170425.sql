ALTER TABLE `tbl_log` DROP COLUMN `platform`;
ALTER TABLE `tbl_log` DROP COLUMN `version`;
ALTER TABLE `tbl_log` DROP COLUMN `primary_category`;
ALTER TABLE `tbl_log` DROP COLUMN `secondary_category`;
ALTER TABLE `tbl_log` ADD COLUMN `type` varchar(32) NOT NULL COMMENT '类型' AFTER `id`;

ALTER TABLE `tbl_fingerprint` ADD COLUMN `uuid` varchar(16) NOT NULL COMMENT 'UUID' AFTER `user_id`;
ALTER TABLE `tbl_fingerprint` CHANGE COLUMN `finger` `finger` varchar(16) NOT NULL COMMENT '手指名称';
ALTER TABLE `tbl_fingerprint` ADD UNIQUE INDEX `idx_uuid` (`uuid`);
ALTER TABLE `tbl_session` CHANGE COLUMN `queue` `token` varchar(16) NOT NULL COMMENT 'Token';
ALTER TABLE `tbl_resource` CHANGE COLUMN `path` `filename` varchar(64) NOT NULL COMMENT '文件名';

ALTER TABLE `tbl_message` ADD COLUMN `reply_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '回复消息ID' AFTER `id`;
ALTER TABLE `tbl_message` CHANGE COLUMN `counter_id` `counter_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '窗口ID';
ALTER TABLE `tbl_message` CHANGE COLUMN `session_id` `session_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '会话ID';
ALTER TABLE `tbl_message` DROP COLUMN `extra`;
ALTER TABLE `tbl_message` ADD COLUMN `content` varchar(3072) NOT NULL COMMENT '消息内容' AFTER `type`;
ALTER TABLE `tbl_message` CHANGE COLUMN `send_time` `send_time` datetime NULL COMMENT '发送时间';
ALTER TABLE `tbl_message` CHANGE COLUMN `ack_time` `ack_time` datetime NULL COMMENT '确认时间';
ALTER TABLE `tbl_message` DROP INDEX `idx_counter_id`;
ALTER TABLE `tbl_message` DROP INDEX `idx_session_id`;
ALTER TABLE `tbl_message` ADD INDEX `idx_reply_id` (`reply_id`);
ALTER TABLE `tbl_message` ADD INDEX `idx_type` (`type`);
ALTER TABLE `tbl_message` ADD FULLTEXT INDEX `idx_keyword` (`body`) WITH PARSER `ngram`;

