ALTER TABLE `tbl_user` ADD FULLTEXT INDEX `idx_keyword` (`number`, `name`) WITH PARSER `ngram`;
ALTER TABLE `tbl_fingerprint` ADD INDEX `idx_user_id` (`user_id`);
ALTER TABLE `tbl_counter` ADD FULLTEXT INDEX `idx_keyword` (`number`, `name`, `ip`) WITH PARSER `ngram`;
ALTER TABLE `tbl_session` ADD INDEX `idx_counter_id` (`counter_id`);
ALTER TABLE `tbl_message` ADD INDEX `idx_counter_id` (`counter_id`);
ALTER TABLE `tbl_message` ADD INDEX `idx_session_id` (`session_id`);
ALTER TABLE `tbl_message` ADD FULLTEXT INDEX `idx_keyword` (`extra`) WITH PARSER `ngram`;
ALTER TABLE `tbl_setting` ADD INDEX `idx_type_key` (`type`, `key`);
ALTER TABLE `tbl_resource` ADD INDEX `idx_type_name` (`type`, `name`);
ALTER TABLE `tbl_resource` ADD FULLTEXT INDEX `idx_keyword` (`name`) WITH PARSER `ngram`;
ALTER TABLE `tbl_log` ADD INDEX `idx_category` (`primary_category`, `secondary_category`);
ALTER TABLE `tbl_log` ADD FULLTEXT INDEX `idx_keyword` (`content`) WITH PARSER `ngram`;