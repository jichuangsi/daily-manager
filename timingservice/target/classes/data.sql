/*
Navicat MySQL Data Transfer

Source Server         : user
Source Server Version : 50614
Source Host           : localhost:3306
Source Database       : aaa

Target Server Type    : MYSQL
Target Server Version : 50614
File Encoding         : 65001

Date: 2019-08-05 18:46:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for resource_staticpage
-- ----------------------------
DROP TABLE IF EXISTS `resource_staticpage`;
CREATE TABLE `resource_staticpage` (
  `id` varchar(255) NOT NULL,
  `resource_id` varchar(255) DEFAULT NULL,
  `static_page_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of resource_staticpage
-- ----------------------------
INSERT INTO `resource_staticpage` VALUES ('40282b816c42363c016c422368770001', '40282b816c280feb016c284408600002', '40282b816c42363c016c423703170001');
INSERT INTO `resource_staticpage` VALUES ('40282b816c42363c016c423579770001', '40282b816c280feb016c284408600003', '40282b816c42363c016c423703170002');
INSERT INTO `resource_staticpage` VALUES ('40282b816c42363c016c423579773451', '40282b816c280feb016c284408600003', '40282b816c42363c016c423703170003');
INSERT INTO `resource_staticpage` VALUES ('40282b816c42363c016c423579896001', '40282b816c280feb016c284408600004', '40282b816c42363c016c423703170004');
INSERT INTO `resource_staticpage` VALUES ('40282b816c42363c016c423579786341', '40282b816c280feb016c284408600004', '40282b816c42363c016c423703170005');
INSERT INTO `resource_staticpage` VALUES ('40282b816c593643c01c423579786341', '40282b816c280feb016c284408600004', '40282b816c42363c016c423703170006');
INSERT INTO `resource_staticpage` VALUES ('40282b816c46588c016c423579786341', '40282b816c280feb016c284408600004', '40282b816c42363c016c423703170007');
INSERT INTO `resource_staticpage` VALUES ('59332b816c42363c016c423579786341', '40282b816c280feb016c284408600004', '40282b816c42363c016c423703170008');
INSERT INTO `resource_staticpage` VALUES ('59332b816c42363c016c423579723697', '40282b816c280feb016c284408600004', '40282b816c42363c016c423703170009');
INSERT INTO `resource_staticpage` VALUES ('59332b816c42363c016c423579712397', '40282b816c280feb016c284408600004', '40282b816c42363c016c423703170010');
INSERT INTO `resource_staticpage` VALUES ('59332b816c42363c016c423579746975', '40282b816c280feb016c284408600004', '40282b816c42363c016c423703170011');

-- ----------------------------
-- Table structure for roleurl
-- ----------------------------
DROP TABLE IF EXISTS `roleurl`;
CREATE TABLE `roleurl` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of roleurl
-- ----------------------------
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbcb2bb0003', '批量添加角色可以访问的url', '/backrole/batchAddRoleUrl ');
INSERT INTO `roleurl` VALUES ('40282b816c2d273d016c2daf8cc70001', '添加部门', '/backdepartment/saveDepartment');
INSERT INTO `roleurl` VALUES ('40282b816c2d273d016c2db0452f0002', '修改部门', '/backdepartment/updateDepartment');
INSERT INTO `roleurl` VALUES ('40282b816c2d273d016c2db0d28f0003', '删除部门', '/backdepartment/deleteDepartment/{deptId}');
INSERT INTO `roleurl` VALUES ('40282b816c2d273d016c2db12ef30004', '查询部门列表', '/backdepartment/getDepartmentList');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2db9fcef0001', '查询模块列表', '/backrole/getUsemoduleList ');
INSERT INTO `roleurl` VALUES ('40282b816c2d273d016c2db3bdef0006', '查询角色列表', '/backrole/getRoleList ');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbbc7900002', '查询全部url列表', '/backrole/getRoleUrlList ');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbced8c0004', '根据id批量删除角色相关url', '/backrole/batchDeleteRoleUrl ');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d0005', '查询角色对应url列表', '/backrole/getAllRoleUrlByRoleId/{roleId} ');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d0455', '添加角色', '/backrole/saveRole');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d0258', '修改角色', '/backrole/updateRole');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d4898', '删除角色', '/backrole/deleteRole/');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d2598', '分页查询员工信息', '/backstaff/getStaffListByPage');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d1876', '获取员工状态', '/backstaff/getStatusList');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d1102', '修改员工状态', '/backstaff/updateStaff');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d1489', '修改员工部门', '/backstaff/updateStaffDept');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d1561', '修改职位部门', '/backstaff/updateStaffRole');

-- ----------------------------
-- Table structure for staticpage_url
-- ----------------------------
DROP TABLE IF EXISTS `staticpage_url`;
CREATE TABLE `staticpage_url` (
  `id` varchar(255) NOT NULL,
  `static_page_id` varchar(255) DEFAULT NULL,
  `url_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of staticpage_url
-- ----------------------------
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d0005', '40282b816c42363c016c423703170002', '40282b816c2d273d016c2daf8cc70001');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d0165', '40282b816c42363c016c423703170002', '40282b816c2d273d016c2db0452f0002');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d5985', '40282b816c42363c016c423703170002', '40282b816c2d273d016c2db0d28f0003');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d0597', '40282b816c42363c016c423703170002', '40282b816c2d273d016c2db12ef30004');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d0364', '40282b816c42363c016c423703170002', '40282b816c2db5d6016c2dbeb16d0455');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d0157', '40282b816c42363c016c423703170002', '40282b816c2db5d6016c2dbeb16d0258');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d0598', '40282b816c42363c016c423703170002', '40282b816c2db5d6016c2dbeb16d4898');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d0498', '40282b816c42363c016c423703170002', '40282b816c2d273d016c2db3bdef0006');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d1564', '40282b816c42363c016c423703170003', '40282b816c2db5d6016c2dbeb16d2598');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d1682', '40282b816c42363c016c423703170003', '40282b816c2db5d6016c2dbeb16d1876');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d2675', '40282b816c42363c016c423703170003', '40282b816c2db5d6016c2dbeb16d1102');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d1678', '40282b816c42363c016c423703170003', '40282b816c2db5d6016c2dbeb16d1489');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d9843', '40282b816c42363c016c423703170003', '40282b816c2db5d6016c2dbeb16d1561');

-- ----------------------------
-- Table structure for static_page
-- ----------------------------
DROP TABLE IF EXISTS `static_page`;
CREATE TABLE `static_page` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `static_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of static_page
-- ----------------------------
INSERT INTO `static_page` VALUES ('40282b816c42363c016c423703170001', '考勤列表', 'CheckWorkAttendance.html ');
INSERT INTO `static_page` VALUES ('40282b816c42363c016c423703170002', '部门角色', 'DepartmentRole.html');
INSERT INTO `static_page` VALUES ('40282b816c42363c016c423703170003', '人员管理', 'PersonnelManagement.html');
INSERT INTO `static_page` VALUES ('40282b816c42363c016c423703170004', '申诉列表', 'AppealList.html');
INSERT INTO `static_page` VALUES ('40282b816c42363c016c423703170005', '加班申请', 'OvertimeApplication.html');
INSERT INTO `static_page` VALUES ('40282b816c42363c016c423703170006', '请假申请', 'ApplicationLeave.html');
INSERT INTO `static_page` VALUES ('40282b816c42363c016c423703170007', '行为日志', 'SystemLog.html');
INSERT INTO `static_page` VALUES ('40282b816c42363c016c423703170008', '权限管理', 'AuthorityManagement.html');
INSERT INTO `static_page` VALUES ('40282b816c42363c016c423703170009', '考勤规则', 'RuleSettings.html');
INSERT INTO `static_page` VALUES ('40282b816c42363c016c423703170010', '打卡规则', 'CardingRules.html');
INSERT INTO `static_page` VALUES ('40282b816c42363c016c423703170011', '报表 ', 'ReportForm.html');

-- ----------------------------
-- Table structure for useway
-- ----------------------------
DROP TABLE IF EXISTS `useway`;
CREATE TABLE `useway` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of useway
-- ----------------------------
INSERT INTO `useway` VALUES ('40282b816c280feb016c284408600002', '考勤管理');
INSERT INTO `useway` VALUES ('40282b816c280feb016c284408600003', '部门管理');
INSERT INTO `useway` VALUES ('40282b816c280feb016c284408600004', '管理员管理');

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` varchar(255) NOT NULL,
  `rolename` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('40282b816c427657016c4282d71f0005', '员工');
INSERT INTO `role` VALUES ('40282b816c2878a9016c287b79470002', '院长');
INSERT INTO `role` VALUES ('40282b816c45f723016c46081cc70006', '部长');


DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` varchar(255) NOT NULL,
  `deptdescribe` varchar(255) DEFAULT NULL,
  `deptname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES ('40282b816c45c47b016c45db4b680000', '工程相关事宜', '工程部');
INSERT INTO `department` VALUES ('40282b816c280feb016c284408600000', '管理财务', '财务部');
