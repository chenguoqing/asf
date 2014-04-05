
CREATE TABLE `asf_execution` (
  `ID_` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `INSTANCE_ID_` bigint(20) NOT NULL COMMENT '流程实例ID',
  `NODE_FULL_ID_` varchar(255)  NOT NULL COMMENT '当前节点ID',
  `NODE_TYPE_` tinyint(4) NOT NULL COMMENT '节点类型',
  `GMT_CREATE` timestamp NOT NULL  COMMENT '创建日期',
  `GMT_MODIFIED` timestamp NOT NULL  COMMENT '更新日期',
  PRIMARY KEY (`ID_`),
  KEY `E_INSTANCE_ID` (`INSTANCE_ID_`)
) ;


CREATE TABLE `asf_instance` (
  `ID_` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '流程实例ID',
  `DEF_ID_` varchar(255) NOT NULL COMMENT '流程定义的名称',
  `DEF_VERSION_` int(11) NOT NULL COMMENT '流程定义的版本',
  `STATUS_` tinyint(4) NOT NULL COMMENT '0-活动 1-挂起 2-结束',
  `VERSION_` int(11) NOT NULL COMMENT 'MVCC版本',
  `GMT_CREATE` timestamp NOT NULL  COMMENT '创建日期',
  `GMT_MODIFIED` timestamp NOT NULL  COMMENT '修改日期',
  PRIMARY KEY (`ID_`)
);

CREATE TABLE `asf_transition` (
  `ID_` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `INSTANCE_ID_` bigint(20) NOT NULL COMMENT '流程实例ID',
  `SOURCE_REF_` varchar(255)  NOT NULL COMMENT 'From节点Full Id',
  `TARGET_REF_` varchar(255)  NOT NULL COMMENT 'To节点Full Id',
  `FLOW_VIRTUAL_` tinyint(4) NOT NULL COMMENT '0 -真实Flow 1 - 虚拟',
  `SOURCE_NODE_TYPE_` tinyint(4) NOT NULL COMMENT 'From节点类型',
  `TARGET_NODE_TYPE_` tinyint(4) NOT NULL COMMENT 'To节点类型',
  `GMT_CREATE` timestamp NOT NULL  COMMENT '创建时间',
  `GMT_MODIFIED` timestamp NOT NULL  COMMENT '修改时间',
  PRIMARY KEY (`ID_`),
  KEY `T_INSTANCE_ID` (`INSTANCE_ID_`)
) ;

CREATE TABLE `asf_variable` (
  `ID_` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '变量ID，自增长',
  `INSTANCE_ID_` bigint(20) unsigned NOT NULL COMMENT '流程实例ID',
  `NAME_` varchar(255)  NOT NULL COMMENT '变量名称',
  `DOUBLE_` double DEFAULT NULL COMMENT 'Double值',
  `LONG_` bigint(20) DEFAULT NULL COMMENT 'Long值',
  `STRING_` varchar(2000)  DEFAULT NULL COMMENT '字符串值',
  `OBJECT_` blob COMMENT '对象值',
  `TYPE_` int(11) NOT NULL COMMENT '值类型 0 -字符串 1 - Long 2 - Double 3 - Object',
  `CLASS_` tinyint(4) NOT NULL COMMENT '变量类别 0 -用户变量 1-系统变量',
  `VERSION_` int(11) NOT NULL COMMENT '版本号(MVCC)',
  `GMT_CREATE` timestamp NOT NULL COMMENT '创建日期',
  `GMT_MODIFIED` timestamp NOT NULL  COMMENT '更新日期',
  PRIMARY KEY (`ID_`),
  UNIQUE KEY `BK_` (`INSTANCE_ID_`,`NAME_`,`CLASS_`),
  KEY `INSTANCE_ID_IDX` (`INSTANCE_ID_`)
);
