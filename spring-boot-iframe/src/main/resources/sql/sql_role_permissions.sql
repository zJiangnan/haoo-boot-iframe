CREATE TABLE `haoo`.`user`  (
  `uid` varchar(100) NOT NULL COMMENT '用户ID',
  `org_id` varchar(100) NOT NULL COMMENT '所属组织',
  `login_name` varchar(50) NOT NULL COMMENT '登录帐号',
  `password` varchar(255) NOT NULL COMMENT '用户密码',
  `user_name` varchar(50) NOT NULL COMMENT '用户姓名',
	`mobile` varchar(50) NULL COMMENT '手机号',
	`email` varchar(50) NULL COMMENT '电子邮箱',
	`created_time` datetime NULL COMMENT '创建时间',
	`modified_time` datetime NULL COMMENT '修改时间',
	`login_times` bigint(0) NULL COMMENT '登录次数',
  PRIMARY KEY (`uid`)
)ENGINE=InnoDB COMMENT='用户表';


CREATE TABLE `haoo`.`role`  (
  `rid` varchar(100) NOT NULL COMMENT '角色ID',
  `parent_rid` varchar(100) NOT NULL COMMENT '父级角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `created_time` datetime NULL COMMENT '创建时间',
	`modified_time` datetime NULL COMMENT '修改时间',
	`description` varchar(255) NULL COMMENT '角色描述',
  PRIMARY KEY (`rid`)
)ENGINE=InnoDB COMMENT='角色表';


CREATE TABLE `haoo`.`permissions`  (
  `pid` varchar(100) NOT NULL COMMENT '权限ID',
  `parent_pid` varchar(100) NOT NULL COMMENT '父权限',
  `permissions_name` varchar(200) NOT NULL COMMENT '权限名称',
	`description` varchar(200) NULL COMMENT '权限描述',
  PRIMARY KEY (`pid`)
)ENGINE=InnoDB COMMENT='权限表';



CREATE TABLE `haoo`.`user_role_relation`  (
  `ur_id` varchar(100) NOT NULL COMMENT '用户角色ID',
  `uid` varchar(100) NOT NULL COMMENT '用户ID',
  `rid` varchar(200) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`ur_id`)
)ENGINE=InnoDB COMMENT='用户角色关系表';



CREATE TABLE `haoo`.`role_permissions_relation`  (
  `rp_id` varchar(100) NOT NULL COMMENT '角色权限关系ID',
  `rid` varchar(100) NOT NULL COMMENT '角色ID',
  `pid` varchar(100) NOT NULL COMMENT '权限ID',
	`permissions_type` varchar(50) NOT NULL COMMENT '权限类型',
  PRIMARY KEY (`rp_id`)
)ENGINE=InnoDB COMMENT='角色权限关系表';


CREATE TABLE `haoo`.`user_permissions_relation`  (
  `up_id` varchar(100) NOT NULL COMMENT '用户权限关系ID',
  `uid` varchar(100) NOT NULL COMMENT '用户ID',
  `pid` varchar(100) NOT NULL COMMENT '权限ID',
	`permissions_type` varchar(50) NOT NULL COMMENT '权限类型',
  PRIMARY KEY (`up_id`)
)ENGINE=InnoDB COMMENT='用户权限关系表';