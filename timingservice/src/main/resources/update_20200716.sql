﻿ALTER TABLE `staff` ADD COLUMN `is_delete` VARCHAR(70) DEFAULT '0' COMMENT '0正常1冻结';
ALTER TABLE `user` ADD COLUMN `is_delete` VARCHAR(70) DEFAULT '0'  COMMENT '0正常1冻结';
UPDATE staff SET is_delete='0';
UPDATE `user` SET is_delete='0';
INSERT INTO `roleurl` VALUES ('40542b816c2db5def456789641316', '冻结员工', '/backstaff/frozenStaffInfo/{opendId}');
INSERT INTO `roleurl` VALUES ('40542b816c2db5def945631345613', '恢复员工', '/backstaff/thawStaffInfo/{opendId}');
INSERT INTO `staticpage_url` VALUES ('40282b816cdw29564316456', '40282b816c42363c016c423703170005', '40542b816c2db5def456789641316');
INSERT INTO `staticpage_url` VALUES ('40282b810567561314641216', '40282b816c42363c016c423703170005', '40542b816c2db5def945631345613');