/*
Navicat MySQL Data Transfer

Source Server         : user
Source Server Version : 50614
Source Host           : localhost:3306
Source Database       : dailymanager

Target Server Type    : MYSQL
Target Server Version : 50614
File Encoding         : 65001

Date: 2019-08-08 19:40:16
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
/*INSERT INTO `resource_staticpage` VALUES ('59332b816c42363c016c423579746975', '40282b816c280feb016c284408600004', '40282b816c42363c016c423703170011');*/
INSERT INTO `resource_staticpage` VALUES ('59332b816c42363c016c4235717842', '40282b816c280feb016c284408600003', '40282b816c42363c016c42370317187914');

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
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbcb2bb0003', '批量添加角色可以访问的url', '/backrole/batchAddRoleUrl');
INSERT INTO `roleurl` VALUES ('40282b816c2d273d016c2daf8cc70001', '添加部门', '/backdepartment/saveDepartment');
INSERT INTO `roleurl` VALUES ('40282b816c2d273d016c2db0452f0002', '修改部门', '/backdepartment/updateDepartment');
INSERT INTO `roleurl` VALUES ('40282b816c2d273d016c2db0d28f0003', '删除部门', '/backdepartment/deleteDepartment/');
INSERT INTO `roleurl` VALUES ('40282b816c2d273d016c2db12ef30004', '查询部门列表', '/backdepartment/getDepartmentList');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2db9fcef0001', '查询模块列表', '/backrole/getUsemoduleList');
/*INSERT INTO `roleurl` VALUES ('40282b816c2d273d016c2db3bdef0006', '查询角色列表', '/backrole/getRoleList');*/
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbbc7900002', '查询全部url列表', '/backrole/getRoleUrlList');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbced8c0004', '根据id批量删除角色相关url', '/backrole/batchDeleteRoleUrl');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d0005', '查询角色对应url列表', '/backrole/getAllRoleUrlByRoleId/');
/*INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d0455', '添加角色', '/backrole/saveRole');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d0258', '修改角色', '/backrole/updateRole');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d4898', '删除角色', '/backrole/deleteRole/');*/
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d2598', '分页查询员工信息', '/backstaff/getStaffListByPage');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d1876', '获取员工状态', '/backstaff/getStatusList');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d1102', '修改员工状态', '/backstaff/updateStaff');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d1489', '修改员工部门', '/backstaff/updateStaffDept');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d1561', '修改职位部门', '/backstaff/updateStaffRole');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbcb2bb0013', '查看考勤', '/kq/getDailyList');
INSERT INTO `roleurl` VALUES ('40282b816c2d273d016c2daf8cc70021', '查看考勤申请', '/sq/getAllUnapprovedSQ');
INSERT INTO `roleurl` VALUES ('40282b816c2d273d016c2db0452f0032', '查看请假申请', '/ol/getolrecord1');
INSERT INTO `roleurl` VALUES ('40282b816c2d273d016c2db0d28f0043', '查看加班申请', '/ol/getolrecord1');
INSERT INTO `roleurl` VALUES ('40282b816c2d273d016c2db12ef30054', '查看行为日志', '/backuser/getOpLogByNameAndPage');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2db9fcef0061', '删除行为日志', '/backuser/deleteOpLog');
INSERT INTO `roleurl` VALUES ('40282b816c2d273d016c2db3bdef0076', '查看模板', '/backrole/getStaticPageList');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbbc7900082', '查看url', '/backrole/getRoleUrlList');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbced8c0094', '查看角色', '/backrole/getRoleList');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb11d0105', '查看角色的url', '/backrole/getAllRoleUrlByRoleId/');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb12d0455', '给角色添加权限 ', '/backrole/batchAddRoleUrl');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb13d0258', '删除角色权限', '/backrole/batchDeleteRoleUrl');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb14d4898', '添加模板规则', '/rule/getrulefatherlist');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb15d2598', '查看当天规则', '/rule/getrulefatherlist');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb15d2876', '查看模板规则', '/rule/getrulefatherlist');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d3102', '删除模板规则', '/rule/delrule');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d4489', '启动模板规则 ', '/rule/rulefatherstopandstart');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d5561', '添加今天规则', '/rule/insertrule');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d6561', '修改当天规则', '/rule/updaterule');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d15698', '查看报表', '/kq/getBB');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d76856', '请假审核', '/ol/olsh');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d2143', '申诉审核', '/sq/sssh');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d08797', '查看申诉图片', '/sq/getimg');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d879524', '加班审核', '/ol/olsh');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d789456', '获取所有部门', '/backdepartment/getDepartmentList');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d23645', '获取角色所管理部门', '/backrole/getRoleDepartmentByRoleId');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d4568', '获取角色', '/backrole/getRoleList');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d15488', '给角色添加需要管理的部门', '/backrole/batchAddRoleDepartment');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d75416', '取消角色所管理的部门', '/backrole/batchDeleteRoleDepartment');
INSERT INTO `roleurl` VALUES ('40282b816c2db5d6016c2dbeb16d982364', '导出考勤记录', '/kq/importDailyList');


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
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d0005', '40282b816c42363c016c423703170002', '40282b816c2d273d016c2daf8cc70001');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d0165', '40282b816c42363c016c423703170002', '40282b816c2d273d016c2db0452f0002');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d5985', '40282b816c42363c016c423703170002', '40282b816c2d273d016c2db0d28f0003');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d0597', '40282b816c42363c016c423703170002', '40282b816c2d273d016c2db12ef30004');
/*INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d0364', '40282b816c42363c016c423703170002', '40282b816c2db5d6016c2dbeb16d0455');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d0157', '40282b816c42363c016c423703170002', '40282b816c2db5d6016c2dbeb16d0258');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d0598', '40282b816c42363c016c423703170002', '40282b816c2db5d6016c2dbeb16d4898');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d0498', '40282b816c42363c016c423703170002', '40282b816c2d273d016c2db3bdef0006');*/
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d1564', '40282b816c42363c016c423703170003', '40282b816c2db5d6016c2dbeb16d2598');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d1682', '40282b816c42363c016c423703170003', '40282b816c2db5d6016c2dbeb16d1876');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d2675', '40282b816c42363c016c423703170003', '40282b816c2db5d6016c2dbeb16d1102');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d1678', '40282b816c42363c016c423703170003', '40282b816c2db5d6016c2dbeb16d1489');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d9843', '40282b816c42363c016c423703170003', '40282b816c2db5d6016c2dbeb16d1561');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d555', '40282b816c42363c016c423703170001', '40282b816c2db5d6016c2dbcb2bb0013');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d3586', '40282b816c42363c016c423703170004', '40282b816c2d273d016c2daf8cc70021');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d3688', '40282b816c42363c016c423703170005', '40282b816c2d273d016c2db0d28f0043');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d6986', '40282b816c42363c016c423703170006', '40282b816c2d273d016c2db0452f0032');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d65786', '40282b816c42363c016c423703170007', '40282b816c2d273d016c2db12ef30054');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d3265', '40282b816c42363c016c423703170007', '40282b816c2db5d6016c2db9fcef0061');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d587', '40282b816c42363c016c423703170008', '40282b816c2d273d016c2db3bdef0076');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d5875', '40282b816c42363c016c423703170008', '40282b816c2db5d6016c2dbbc7900082');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d0258', '40282b816c42363c016c423703170008', '40282b816c2db5d6016c2dbced8c0094');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d84564', '40282b816c42363c016c423703170008', '40282b816c2db5d6016c2dbeb11d0105');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d4755', '40282b816c42363c016c423703170008', '40282b816c2db5d6016c2dbeb12d0455');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d11745', '40282b816c42363c016c423703170008', '40282b816c2db5d6016c2dbeb13d0258');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d5575', '40282b816c42363c016c423703170009', '40282b816c2db5d6016c2dbeb14d4898');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d5755', '40282b816c42363c016c423703170010', '40282b816c2db5d6016c2dbeb15d2598');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d7788', '40282b816c42363c016c423703170010', '40282b816c2db5d6016c2dbeb15d2876');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d132543', '40282b816c42363c016c423703170010', '40282b816c2db5d6016c2dbeb16d3102');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d357486', '40282b816c42363c016c423703170010', '40282b816c2db5d6016c2dbeb16d4489');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d67878', '40282b816c42363c016c423703170010', '40282b816c2db5d6016c2dbeb16d6561');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d39789', '40282b816c42363c016c423703170010', '40282b816c2db5d6016c2dbeb16d5561');
/*INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d7865', '40282b816c42363c016c423703170011', '40282b816c2db5d6016c2dbeb16d15698');*/
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d67852', '40282b816c42363c016c423703170006', '40282b816c2db5d6016c2dbeb16d76856');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d62586', '40282b816c42363c016c423703170005', '40282b816c2db5d6016c2dbeb16d879524');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d2585', '40282b816c42363c016c423703170004', '40282b816c2db5d6016c2dbeb16d2143');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d345867', '40282b816c42363c016c423703170004', '40282b816c2db5d6016c2dbeb16d08797');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d6983', '40282b816c42363c016c42370317187914', '40282b816c2db5d6016c2dbeb16d789456');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d2186', '40282b816c42363c016c42370317187914', '40282b816c2db5d6016c2dbeb16d23645');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d88876', '40282b816c42363c016c42370317187914', '40282b816c2db5d6016c2dbeb16d4568');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d27865', '40282b816c42363c016c42370317187914', '40282b816c2db5d6016c2dbeb16d15488');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbeb16d75876', '40282b816c42363c016c42370317187914', '40282b816c2db5d6016c2dbeb16d75416');
INSERT INTO `staticpage_url` VALUES ('40282b816c2db5d6016c2dbebcfd1478', '40282b816c42363c016c423703170001', '40282b816c2db5d6016c2dbeb16d982364');


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
/*INSERT INTO `static_page` VALUES ('40282b816c42363c016c423703170011', '报表 ', 'ReportForm.html');*/
INSERT INTO `static_page` VALUES ('40282b816c42363c016c42370317187914', '管理部门 ', 'AdministrativeDepartment.html');

-- ----------------------------
-- Table structure for status
-- ----------------------------
DROP TABLE IF EXISTS `status`;
CREATE TABLE `status` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of status
-- ----------------------------
INSERT INTO `status` VALUES ('40282b816c2db5d6016c2dbbc7900012', '请假');
INSERT INTO `status` VALUES ('40282b816c2db5d6016c2dbbc7900022', '在职');
INSERT INTO `status` VALUES ('40282b816c2db5d6016c2dbbc7900032', '离职');
INSERT INTO `status` VALUES ('40282b816c2db5d6016c2dbbc7900042', '暂停状态');
-- ----------------------------

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

-- ----------------------------
-- Table structure for role
-- ----------------------------
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
INSERT INTO `role` VALUES ('40282b816c45f723016c46081cc725676', '副院长');


