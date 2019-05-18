
-- ----------------------------
-- Table structure for auth_container_info
-- ----------------------------
CREATE TABLE `auth_video_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `video_name` varchar(100) NOT NULL COMMENT '视频名称',
  `video_url` varchar(100) DEFAULT NULL COMMENT '视频地址',
  `service_creator` varchar(50) NOT NULL COMMENT '创建人',
  `consumer` varchar(50) NOT NULL COMMENT '照片拥有者',
  `account` varchar(50) NOT NULL COMMENT '所属平台用户',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=218 DEFAULT CHARSET=utf8 COMMENT='平台用户视频表';