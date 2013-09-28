-- THIS FILE IS AUTOMATICALLY GENERATED BY A SCRIPT.
-- DO NOT EDIT IT MANUALLY, YOUR CHANGES WILL BE LOST!!!




CREATE SEQUENCE `seq_dpu_record` START WITH 100;
CREATE TABLE `DPU_INSTANCE`
(
-- DPURecord
  `id` INTEGER AUTO_INCREMENT,
  `name` VARCHAR(45),
  `use_dpu_description` SMALLINT,
  `description` VARCHAR(255),
  `tool_tip` VARCHAR(255),
  `configuration` BLOB,
-- DPUInstaceRecord
  `dpu_id` INTEGER,
  PRIMARY KEY (`id`)
);
CREATE INDEX `ix_DPU_INSTANCE_dpu_id` ON `DPU_INSTANCE` (`dpu_id`);

CREATE TABLE `DPU_TEMPLATE`
(
-- DPURecord
  `id` INTEGER AUTO_INCREMENT,
  `name` VARCHAR(45),
  `use_dpu_description` SMALLINT,
  `description` VARCHAR(255),  
  `configuration` BLOB,
  `parent_id` INTEGER,
-- DPUTemplateRecord
  `user_id` INTEGER,
  `visibility` SMALLINT,
  `type` SMALLINT,
  `jar_directory` VARCHAR(255),
  `jar_name` VARCHAR(255),  
  `jar_description` VARCHAR(512),
  PRIMARY KEY (`id`)
);
CREATE INDEX `ix_DPU_TEMPLATE_jar_directory` ON `DPU_TEMPLATE` (`jar_directory`);
CREATE INDEX `ix_DPU_TEMPLATE_parent_id` ON `DPU_TEMPLATE` (`parent_id`);
CREATE INDEX `ix_DPU_TEMPLATE_user_id` ON `DPU_TEMPLATE` (`user_id`);
CREATE INDEX `ix_DPU_TEMPLATE_visibility` ON `DPU_TEMPLATE` (`visibility`);

CREATE SEQUENCE `seq_exec_dataunit_info` START WITH 100;
CREATE TABLE `EXEC_DATAUNIT_INFO`
(
  `id` INTEGER AUTO_INCREMENT,
  `name` VARCHAR(45),
  `idx` INTEGER,
  `type` SMALLINT,
  `is_input` SMALLINT,
  `exec_context_dpu_id` INTEGER,
  PRIMARY KEY (`id`)
);
CREATE INDEX `ix_EXEC_DATAUNIT_INFO_exec_context_dpu_id` ON `EXEC_DATAUNIT_INFO` (`exec_context_dpu_id`);

CREATE SEQUENCE `seq_exec_context_pipeline` START WITH 100;
CREATE TABLE `EXEC_CONTEXT_PIPELINE`
(
  `id` INTEGER AUTO_INCREMENT,
  `directory` VARCHAR(255),
  `dummy` SMALLINT,
  PRIMARY KEY (`id`)
);

CREATE SEQUENCE `seq_exec_context_dpu` START WITH 100;
CREATE TABLE `EXEC_CONTEXT_DPU`
(
  `id` INTEGER AUTO_INCREMENT,
  `exec_context_pipeline_id` INTEGER,
  `dpu_instance_id` INTEGER,
  `state` SMALLINT,
  PRIMARY KEY (`id`)
);
CREATE INDEX `ix_EXEC_CONTEXT_DPU_exec_context_pipeline_id` ON `EXEC_CONTEXT_DPU` (`exec_context_pipeline_id`);
CREATE INDEX `ix_EXEC_CONTEXT_DPU_dpu_instance_id` ON `EXEC_CONTEXT_DPU` (`dpu_instance_id`);

CREATE SEQUENCE `seq_exec_record` START WITH 100;
CREATE TABLE `EXEC_RECORD`
(
  `id` INTEGER AUTO_INCREMENT,
  `r_time` DATETIME,
  `r_type` SMALLINT,
  `dpu_id` INTEGER,
  `execution_id` INTEGER,
  `short_message` TEXT,
  `full_message` TEXT,
  PRIMARY KEY (`id`)
);
CREATE INDEX `ix_EXEC_RECORD_r_time` ON `EXEC_RECORD` (`r_time`);
CREATE INDEX `ix_EXEC_RECORD_r_type` ON `EXEC_RECORD` (`r_type`);
CREATE INDEX `ix_EXEC_RECORD_dpu_id` ON `EXEC_RECORD` (`dpu_id`);
CREATE INDEX `ix_EXEC_RECORD_execution_id` ON `EXEC_RECORD` (`execution_id`);

CREATE SEQUENCE `seq_exec_pipeline` START WITH 100;
CREATE TABLE `EXEC_PIPELINE`
(
  `id` INTEGER AUTO_INCREMENT,
  `status` INTEGER,
  `pipeline_id` INTEGER,
  `debug_mode` SMALLINT,
  `t_start` DATETIME,
  `t_end` DATETIME,
  `context_id` INTEGER NOT NULL,
  `schedule_id` INTEGER,
  `silent_mode` SMALLINT,
  `debugnode_id` INTEGER,
  `stop` SMALLINT,
  `t_last_change` DATETIME,
  PRIMARY KEY (`id`)
);
CREATE INDEX `ix_EXEC_PIPELINE_status` ON `EXEC_PIPELINE` (`status`);
CREATE INDEX `ix_EXEC_PIPELINE_pipeline_id` ON `EXEC_PIPELINE` (`pipeline_id`);
CREATE INDEX `ix_EXEC_PIPELINE_debug_mode` ON `EXEC_PIPELINE` (`debug_mode`);
CREATE INDEX `ix_EXEC_PIPELINE_t_start` ON `EXEC_PIPELINE` (`t_start`);
CREATE INDEX `ix_EXEC_PIPELINE_context_id` ON `EXEC_PIPELINE` (`context_id`);
CREATE INDEX `ix_EXEC_PIPELINE_schedule_id` ON `EXEC_PIPELINE` (`schedule_id`);

CREATE SEQUENCE `seq_exec_schedule` START WITH 100;
CREATE TABLE `EXEC_SCHEDULE`
(
  `id` INTEGER AUTO_INCREMENT,
  `name` VARCHAR(45),
  `description` VARCHAR(255),
  `pipeline_id` INTEGER NOT NULL,
  `user_id` INTEGER,
  `just_once` SMALLINT,
  `enabled` SMALLINT,
  `type` SMALLINT,
  `first_exec` DATETIME,
  `last_exec` DATETIME,
  `time_period` INTEGER,
  `period_unit` SMALLINT,
  `strict_timing` SMALLINT,
  `strict_tolerance` INTEGER,
  PRIMARY KEY (`id`)
);
-- composite index to optimize fetching schedules following pipeline
CREATE INDEX `ix_EXEC_SCHEDULE_pipeline_id_type` ON `EXEC_SCHEDULE` (`pipeline_id`, `type`);
CREATE INDEX `ix_EXEC_SCHEDULE_user_id` ON `EXEC_SCHEDULE` (`user_id`);
CREATE INDEX `ix_EXEC_SCHEDULE_enabled` ON `EXEC_SCHEDULE` (`enabled`);
CREATE INDEX `ix_EXEC_SCHEDULE_type` ON `EXEC_SCHEDULE` (`type`);

CREATE TABLE `EXEC_SCHEDULE_AFTER`
(
  `schedule_id` INTEGER,
  `pipeline_id` INTEGER,
  PRIMARY KEY (`schedule_id`, `pipeline_id`)
);

CREATE SEQUENCE `seq_ppl_model` START WITH 100;
CREATE TABLE `PPL_MODEL`
(
  `id` INTEGER AUTO_INCREMENT,
  `name` VARCHAR(45),
  `description` VARCHAR(255),
  `user_id` INTEGER,
  PRIMARY KEY (`id`)
);
CREATE INDEX `ix_PPL_MODEL_user_id` ON `PPL_MODEL` (`user_id`);

CREATE TABLE `PPL_PPL_CONFLICTS`
(
  `pipeline_id` INTEGER,
  `pipeline_conflict_id` INTEGER,
  PRIMARY KEY (`pipeline_id`, `pipeline_conflict_id`)
);
CREATE INDEX `ix_PPL_PPL_CONFLICTS_pipeline_id` ON `PPL_PPL_CONFLICTS` (`pipeline_id`);

CREATE SEQUENCE `seq_ppl_edge` START WITH 100;
CREATE TABLE `PPL_EDGE`
(
  `id` INTEGER AUTO_INCREMENT,
  `graph_id` INTEGER,
  `node_from_id` INTEGER,
  `node_to_id` INTEGER,
  `data_unit_name` VARCHAR(45),
  PRIMARY KEY (`id`)
);
CREATE INDEX `ix_PPL_EDGE_graph_id` ON `PPL_EDGE` (`graph_id`);
CREATE INDEX `ix_PPL_EDGE_node_from_id` ON `PPL_EDGE` (`node_from_id`);
CREATE INDEX `ix_PPL_EDGE_node_to_id` ON `PPL_EDGE` (`node_to_id`);

CREATE SEQUENCE `seq_ppl_node` START WITH 100;
CREATE TABLE `PPL_NODE`
(
  `id` INTEGER AUTO_INCREMENT,
  `graph_id` INTEGER,
  `instance_id` INTEGER,
  `position_id` INTEGER,
  PRIMARY KEY (`id`)
);
CREATE INDEX `ix_PPL_NODE_graph_id` ON `PPL_NODE` (`graph_id`);
CREATE INDEX `ix_PPL_NODE_instance_id` ON `PPL_NODE` (`instance_id`);

CREATE SEQUENCE `seq_ppl_graph` START WITH 100;
CREATE TABLE `PPL_GRAPH`
(
  `id` INTEGER AUTO_INCREMENT,
  `pipeline_id` INTEGER,
  PRIMARY KEY (`id`),
  UNIQUE (pipeline_id)
);
CREATE INDEX `ix_PPL_GRAPH_pipeline_id` ON `PPL_GRAPH` (`pipeline_id`);

CREATE SEQUENCE `seq_ppl_position` START WITH 100;
CREATE TABLE `PPL_POSITION`
(
  `id` INTEGER AUTO_INCREMENT,
  `pos_x` INTEGER,
  `pos_y` INTEGER,
  PRIMARY KEY (`id`)
);

CREATE SEQUENCE `seq_sch_notification` START WITH 100;
CREATE TABLE `SCH_SCH_NOTIFICATION`
(
  `id` INTEGER AUTO_INCREMENT,
  `schedule_id` INTEGER NOT NULL,
  `type_success` SMALLINT,
  `type_error` SMALLINT,
  PRIMARY KEY (`id`),
  UNIQUE (schedule_id)
);

CREATE TABLE `SCH_USR_NOTIFICATION`
(
  `id` INTEGER AUTO_INCREMENT,
  `user_id` INTEGER NOT NULL,
  `type_success` SMALLINT,
  `type_error` SMALLINT,
  PRIMARY KEY (`id`),
  UNIQUE (user_id)
);
CREATE INDEX `ix_SCH_USR_NOTIFICATION_user_id` ON `SCH_USR_NOTIFICATION` (`user_id`);

CREATE SEQUENCE `seq_sch_email` START WITH 100;
CREATE TABLE `SCH_EMAIL`
(
  `id` INTEGER AUTO_INCREMENT,
  `e_user` VARCHAR(85),
  `e_domain` VARCHAR(45),
  PRIMARY KEY (`id`)
);
CREATE INDEX `ix_SCH_EMAIL_email` ON `SCH_EMAIL` (`e_user`, `e_domain`);

CREATE TABLE `SCH_SCH_NOTIFICATION_EMAIL`
(
  `notification_id` INTEGER,
  `email_id` SMALLINT,
  PRIMARY KEY (`notification_id`, `email_id`)
);

CREATE TABLE `SCH_USR_NOTIFICATION_EMAIL`
(
  `notification_id` INTEGER AUTO_INCREMENT,
  `email_id` SMALLINT,
  PRIMARY KEY (`notification_id`, `email_id`)
);
CREATE INDEX `ix_SCH_USR_NOTIFICATION_EMAIL_email_id` ON `SCH_USR_NOTIFICATION_EMAIL` (`email_id`);

CREATE SEQUENCE `seq_usr_user` START WITH 100;
CREATE TABLE `USR_USER`
(
  `id` INTEGER AUTO_INCREMENT,
  `username` VARCHAR(25) NOT NULL,
  `email_id` INTEGER,
  `u_password` CHAR(132) NOT NULL,
  `full_name` VARCHAR(55),
  PRIMARY KEY (`id`),
  UNIQUE (`username`)
);
CREATE INDEX `ix_USR_USER_email_id` ON `USR_USER` (`email_id`);

CREATE TABLE `USR_USER_ROLE`
(
  `user_id` INTEGER NOT NULL,
  `role_id` INTEGER NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`)
);

CREATE SEQUENCE `seq_rdf_prefix` START WITH 100;
CREATE TABLE `RDF_PREFIX`
(
  `id` INTEGER AUTO_INCREMENT,
  `name` VARCHAR(25) NOT NULL,
  `uri` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`name`)
);

-- CONSTRAINTS #################################################################


-- Table `DPU_INSTANCE`
ALTER TABLE `DPU_INSTANCE`
  ADD CONSTRAINT `DPU_INSTANCE_DPU_TEMPLATE_id_id` FOREIGN KEY (`dpu_id`)
    REFERENCES `DPU_TEMPLATE` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;


-- Table `DPU_TEMPLATE`
ALTER TABLE `DPU_TEMPLATE`
  ADD CONSTRAINT `DPU_TEMPLATE_DPU_TEMPLATE_id_id` FOREIGN KEY (`parent_id`)
    REFERENCES `DPU_TEMPLATE` (`id`)
	ON UPDATE CASCADE ON DELETE SET NULL;


-- Table `EXEC_DATAUNIT_INFO`
ALTER TABLE `EXEC_DATAUNIT_INFO`
  ADD CONSTRAINT `EXEC_DATAUNIT_INFO_EXEC_CONTEXT_DPU_id_id` FOREIGN KEY (`exec_context_dpu_id`)
    REFERENCES `EXEC_CONTEXT_DPU` (`id`)
	ON UPDATE CASCADE ON DELETE SET NULL;


-- Table `EXEC_CONTEXT_DPU`
ALTER TABLE `EXEC_CONTEXT_DPU`
  ADD CONSTRAINT `EXEC_CONTEXT_DPU_EXEC_CONTEXT_PIPELINE_id_id` FOREIGN KEY (`exec_context_pipeline_id`)
    REFERENCES `EXEC_CONTEXT_PIPELINE` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `EXEC_CONTEXT_DPU`
  ADD CONSTRAINT `EXEC_CONTEXT_DPU_DPU_INSTANCE_id_id` FOREIGN KEY (`dpu_instance_id`)
    REFERENCES `DPU_INSTANCE` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;


-- Table `EXEC_RECORD`
ALTER TABLE `EXEC_RECORD`
  ADD CONSTRAINT `EXEC_RECORD_DPU_INSTANCE_id_id` FOREIGN KEY (`dpu_id`)
    REFERENCES `DPU_INSTANCE` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `EXEC_RECORD`
  ADD CONSTRAINT `EXEC_RECORD_EXEC_PIPELINE_id_id` FOREIGN KEY (`execution_id`)
    REFERENCES `EXEC_PIPELINE` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;


-- Table `EXEC_PIPELINE`
ALTER TABLE `EXEC_PIPELINE`
  ADD CONSTRAINT `EXEC_PIPELINE_PPL_MODEL_id_id` FOREIGN KEY (`pipeline_id`)
    REFERENCES `PPL_MODEL` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `EXEC_PIPELINE`
  ADD CONSTRAINT `EXEC_PIPELINE_EXEC_CONTEXT_PIPELINE_id_id` FOREIGN KEY (`context_id`)
    REFERENCES `EXEC_CONTEXT_PIPELINE` (`id`)
	ON UPDATE CASCADE ON DELETE SET NULL;

ALTER TABLE `EXEC_PIPELINE`
  ADD CONSTRAINT `EXEC_PIPELINE_EXEC_SCHEDULE_id_id` FOREIGN KEY (`schedule_id`)
    REFERENCES `EXEC_SCHEDULE` (`id`)
	ON UPDATE CASCADE ON DELETE SET NULL;

ALTER TABLE `EXEC_PIPELINE`
  ADD CONSTRAINT `EXEC_PIPELINE_PPL_NODE_id_id` FOREIGN KEY (`debugnode_id`)
    REFERENCES `PPL_NODE` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;

-- Table `EXEC_SCHEDULE`
ALTER TABLE `EXEC_SCHEDULE`
  ADD CONSTRAINT `EXEC_SCHEDULE_PPL_MODEL_id_id` FOREIGN KEY (`pipeline_id`)
    REFERENCES `PPL_MODEL` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `EXEC_SCHEDULE`
  ADD CONSTRAINT `EXEC_SCHEDULE_USR_USER_id_id` FOREIGN KEY (`user_id`)
    REFERENCES `USR_USER` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;


-- Table `EXEC_SCHEDULE_AFTER`
ALTER TABLE `EXEC_SCHEDULE_AFTER`
  ADD CONSTRAINT `EXEC_SCHEDULE_AFTER_EXEC_SCHEDULE_id_id` FOREIGN KEY (`schedule_id`)
    REFERENCES `EXEC_SCHEDULE` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `EXEC_SCHEDULE_AFTER`
  ADD CONSTRAINT `EXEC_SCHEDULE_AFTER_PPL_MODEL_id_id` FOREIGN KEY (`pipeline_id`)
    REFERENCES `PPL_MODEL` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;


-- Table `PPL_MODEL`
ALTER TABLE `PPL_MODEL`
  ADD CONSTRAINT `PPL_MODEL_USR_USER_id_id` FOREIGN KEY (`user_id`)
    REFERENCES `USR_USER` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;


-- Table `PPL_GRAPH`
ALTER TABLE `PPL_GRAPH`
  ADD CONSTRAINT `PPL_GRAPH_PPL_MODEL_id_id` FOREIGN KEY (`pipeline_id`)
    REFERENCES `PPL_MODEL` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;


-- Table `PPL_NODE`
ALTER TABLE `PPL_NODE`
  ADD CONSTRAINT `PPL_NODE_PPL_GRAPH_id_id` FOREIGN KEY (`graph_id`)
    REFERENCES `PPL_GRAPH` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `PPL_NODE`
  ADD CONSTRAINT `PPL_NODE_DPU_INSTANCE_id_id` FOREIGN KEY (`instance_id`)
    REFERENCES `DPU_INSTANCE` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `PPL_NODE`
  ADD CONSTRAINT `PPL_NODE_PPL_POSITION_id_id` FOREIGN KEY (`position_id`)
    REFERENCES `PPL_POSITION` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;


-- Table `PPL_EDGE`
ALTER TABLE `PPL_EDGE`
  ADD CONSTRAINT `PPL_EDGE_PPL_GRAPH_id_id` FOREIGN KEY (`graph_id`)
    REFERENCES `PPL_GRAPH` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `PPL_EDGE`
  ADD CONSTRAINT `PPL_EDGE_PPL_NODE_FROM_id_id` FOREIGN KEY (`node_from_id`)
    REFERENCES `PPL_NODE` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `PPL_EDGE`
  ADD CONSTRAINT `PPL_EDGE_PPL_NODE_TO_id_id` FOREIGN KEY (`node_to_id`)
    REFERENCES `PPL_NODE` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;


-- Table `SCH_SCH_NOTIFICATION`
ALTER TABLE `SCH_SCH_NOTIFICATION`
  ADD CONSTRAINT `SCH_SCH_NOTIFICATION_EXEC_SCHEDULE_id_id` FOREIGN KEY (`schedule_id`)
    REFERENCES `EXEC_SCHEDULE` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;


-- Table `SCH_SCH_NOTIFICATION_EMAIL`
ALTER TABLE `SCH_SCH_NOTIFICATION_EMAIL`
  ADD CONSTRAINT `SCH_SCH_NOTIFICATION_EMAIL_SCH_SCH_NOTIFICATION_id_id` FOREIGN KEY (`notification_id`)
    REFERENCES `SCH_SCH_NOTIFICATION` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `SCH_SCH_NOTIFICATION_EMAIL`
  ADD CONSTRAINT `SCH_SCH_NOTIFICATION_EMAIL_SCH_EMAIL_id_id` FOREIGN KEY (`email_id`)
    REFERENCES `SCH_EMAIL` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;


-- Table `SCH_USR_NOTIFICATION`
ALTER TABLE `SCH_USR_NOTIFICATION`
  ADD CONSTRAINT `SCH_USR_NOTIFICATION_USR_USER_id_id` FOREIGN KEY (`user_id`)
    REFERENCES `USR_USER` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;


-- Table `SCH_USR_NOTIFICATION_EMAIL`
ALTER TABLE `SCH_USR_NOTIFICATION_EMAIL`
  ADD CONSTRAINT `SCH_USR_NOTIFICATION_EMAIL_SCH_USR_NOTIFICATION_id_id` FOREIGN KEY (`notification_id`)
    REFERENCES `SCH_USR_NOTIFICATION` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `SCH_USR_NOTIFICATION_EMAIL`
  ADD CONSTRAINT `SCH_USR_NOTIFICATION_EMAIL_SCH_EMAIL_id_id` FOREIGN KEY (`email_id`)
    REFERENCES `SCH_EMAIL` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;


-- Table `USR_USER`
ALTER TABLE `USR_USER`
  ADD CONSTRAINT `USR_USER_SCH_EMAIL_id_id` FOREIGN KEY (`email_id`)
    REFERENCES `SCH_EMAIL` (`id`)
	ON UPDATE CASCADE ON DELETE SET NULL;


-- Table `USR_USER_ROLE`
ALTER TABLE `USR_USER_ROLE`
  ADD CONSTRAINT `USR_USER_USR_USER_ROLE_id_id` FOREIGN KEY (`user_id`)
    REFERENCES `USR_USER` (`id`)
	ON UPDATE CASCADE ON DELETE CASCADE;


-- workaround for bug in virtuoso's implementation of cascades on delete
-- see https://github.com/openlink/virtuoso-opensource/issues/56


-- TABLES FOR LOGBACK


CREATE TABLE `LOGGING_EVENT`
(
  timestmp BIGINT NOT NULL,
  formatted_message TEXT NOT NULL,
  logger_name VARCHAR(254) NOT NULL,
  level_string VARCHAR(254) NOT NULL,
  thread_name VARCHAR(254),
  reference_flag SMALLINT,
  arg0 VARCHAR(254),
  arg1 VARCHAR(254),
  arg2 VARCHAR(254),
  arg3 VARCHAR(254),
  caller_filename VARCHAR(254) NOT NULL,
  caller_class VARCHAR(254) NOT NULL,
  caller_method VARCHAR(254) NOT NULL,
  caller_line CHAR(4) NOT NULL,
  event_id BIGINT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (event_id)
);
CREATE INDEX `ix_LOGGING_EVENT_timestmp` ON `LOGGING_EVENT` (`timestmp`);
CREATE INDEX `ix_LOGGING_EVENT_level_string` ON `LOGGING_EVENT` (`level_string`);

CREATE TABLE `LOGGING_EVENT_PROPERTY`
(
  event_id BIGINT NOT NULL,
  mapped_key VARCHAR(254) NOT NULL,
  mapped_value VARCHAR(254),
  PRIMARY KEY (event_id, mapped_key),
  FOREIGN KEY (event_id) REFERENCES `LOGGING_EVENT`(event_id)
);
CREATE INDEX `ix_LOGGING_EVENT_PROPERTY_mapped_key` ON `LOGGING_EVENT_PROPERTY` (`mapped_key`);

CREATE TABLE `LOGGING_EVENT_EXCEPTION`
(
  event_id BIGINT NOT NULL,
  i SMALLINT NOT NULL,
  trace_line VARCHAR(254) NOT NULL,
  PRIMARY KEY(event_id, i),
  FOREIGN KEY (event_id) REFERENCES `LOGGING_EVENT`(event_id)
);
