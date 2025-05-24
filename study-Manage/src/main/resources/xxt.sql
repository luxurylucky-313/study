/*
 Navicat Premium Dump SQL

 Source Server         : MySQL8.0.27
 Source Server Type    : MySQL
 Source Server Version : 80027 (8.0.27)
 Source Host           : localhost:3306
 Source Schema         : xxt

 Target Server Type    : MySQL
 Target Server Version : 80027 (8.0.27)
 File Encoding         : 65001

 Date: 24/05/2025 14:32:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `teacher_id` int NOT NULL COMMENT '关联的教师ID',
  `course_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程名称',
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级名称',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程图片路径',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '课程描述',
  `resource_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程资源文件URL',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1153474563 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (1, 1, '计算机科学导论', '计算机科学2025级1班', '2025-03-26 10:00:00', '2025-06-30 18:00:00', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/course1.jpg', '介绍计算机科学的基本概念和发展历史1', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/张雅婷软件项目管理.docx');
INSERT INTO `course` VALUES (2, 1, '数据结构与算法', '计算机科学2025级2班', '2025-03-27 10:00:00', '2025-07-01 18:00:00', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/course1.jpg', '学习基础的数据结构和算法设计', NULL);
INSERT INTO `course` VALUES (3, 1, '操作系统原理', '计算机科学2025级3班', '2025-03-28 10:00:00', '2025-07-02 18:00:00', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/course1.jpg', '深入探讨操作系统的内部工作原理', NULL);
INSERT INTO `course` VALUES (4, 1, '数据库系统概论', '计算机科学2025级4班', '2025-03-29 10:00:00', '2025-07-03 18:00:00', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/course1.jpg', '讲解数据库系统的设计与实现', NULL);
INSERT INTO `course` VALUES (5, 0, '软件工程基础', '计算机科学2025级5班', '2025-03-30 10:00:00', '2025-07-04 18:00:00', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/course1.jpg', '介绍软件工程的基本概念和方法', NULL);
INSERT INTO `course` VALUES (6, 0, '人工智能基础', '计算机科学2025级6班', '2025-03-31 10:00:00', '2025-07-05 18:00:00', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/course1.jpg', '探索人工智能领域的基本理论和技术', NULL);
INSERT INTO `course` VALUES (7, 0, '计算机网络', '计算机科学2025级7班', '2025-04-01 10:00:00', '2025-07-06 18:00:00', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/course1.jpg', '学习计算机网络的基础知识', NULL);
INSERT INTO `course` VALUES (8, 0, '编译原理', '计算机科学2025级8班', '2025-04-02 10:00:00', '2025-07-07 18:00:00', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/course1.jpg', '研究编译器的设计与实现', NULL);
INSERT INTO `course` VALUES (9, 0, '信息安全', '计算机科学2025级9班', '2025-04-03 10:00:00', '2025-07-08 18:00:00', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/course1.jpg', '介绍信息安全的基本概念和技术', NULL);
INSERT INTO `course` VALUES (10, 0, 'Web开发技术', '计算机科学2025级10班', '2025-04-04 10:00:00', '2025-07-09 18:00:00', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/course1.jpg', '学习现代Web开发的相关技术', NULL);
INSERT INTO `course` VALUES (11, 0, '移动应用开发', '计算机科学2025级11班', '2025-04-05 10:00:00', '2025-07-10 18:00:00', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/course1.jpg', '掌握移动应用开发的基础技能', NULL);
INSERT INTO `course` VALUES (12, 0, '云计算与大数据', '计算机科学2025级12班', '2025-04-06 10:00:00', '2025-07-11 18:00:00', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/course1.jpg', '探讨云计算和大数据处理的方法', NULL);
INSERT INTO `course` VALUES (13, 0, '游戏开发基础', '计算机科学2025级13班', '2025-04-07 10:00:00', '2025-07-12 18:00:00', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/course1.jpg', '介绍游戏开发的基本流程和技术', NULL);
INSERT INTO `course` VALUES (14, 0, '人机交互', '计算机科学2025级14班', '2025-04-08 10:00:00', '2025-07-13 18:00:00', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/course1.jpg', '学习人机交互的设计原则和实践', NULL);
INSERT INTO `course` VALUES (15, 0, '计算机图形学', '计算机科学2025级15班', '2025-04-09 10:00:00', '2025-07-14 18:00:00', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/course1.jpg', '探索计算机生成图像的技术', NULL);

-- ----------------------------
-- Table structure for homework
-- ----------------------------
DROP TABLE IF EXISTS `homework`;
CREATE TABLE `homework`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '作业ID',
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级名称',
  `course_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程名称',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '作业信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of homework
-- ----------------------------
INSERT INTO `homework` VALUES (1, '计算机科学2025级1班', '计算机科学导论', '2025-04-01 10:00:00', '2025-04-15 23:59:59');
INSERT INTO `homework` VALUES (2, '计算机科学2025级2班', '数据结构与算法', '2025-04-02 10:00:00', '2025-04-16 23:59:59');
INSERT INTO `homework` VALUES (3, '计算机科学2025级3班', '操作系统原理', '2025-04-03 10:00:00', '2025-04-17 23:59:59');
INSERT INTO `homework` VALUES (4, '计算机科学2025级4班', '数据库系统概论', '2025-04-04 10:00:00', '2025-04-18 23:59:59');
INSERT INTO `homework` VALUES (5, '计算机科学2025级5班', '软件工程基础', '2025-04-05 10:00:00', '2025-04-19 23:59:59');
INSERT INTO `homework` VALUES (6, '计算机科学2025级6班', '人工智能基础', '2025-04-06 10:00:00', '2025-04-20 23:59:59');
INSERT INTO `homework` VALUES (7, '计算机科学2025级7班', '计算机网络', '2025-04-07 10:00:00', '2025-04-21 23:59:59');
INSERT INTO `homework` VALUES (8, '计算机科学2025级8班', '编译原理', '2025-04-08 10:00:00', '2025-04-22 23:59:59');
INSERT INTO `homework` VALUES (9, '计算机科学2025级9班', '信息安全', '2025-04-09 10:00:00', '2025-04-23 23:59:59');
INSERT INTO `homework` VALUES (10, '计算机科学2025级10班', 'Web开发技术', '2025-04-10 10:00:00', '2025-04-24 23:59:59');
INSERT INTO `homework` VALUES (11, '计算机科学2025级11班', '移动应用开发', '2025-04-11 10:00:00', '2025-04-25 23:59:59');
INSERT INTO `homework` VALUES (12, '计算机科学2025级12班', '云计算与大数据', '2025-04-12 10:00:00', '2025-04-26 23:59:59');
INSERT INTO `homework` VALUES (13, '计算机科学2025级13班', '游戏开发基础', '2025-04-13 10:00:00', '2025-04-27 23:59:59');
INSERT INTO `homework` VALUES (14, '计算机科学2025级14班', '人机交互', '2025-04-14 10:00:00', '2025-04-28 23:59:59');
INSERT INTO `homework` VALUES (15, '计算机科学2025级15班', '计算机图形学', '2025-04-15 10:00:00', '2025-04-29 23:59:59');

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '教师ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '教师姓名',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像路径',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '教师信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (1, '张雅婷', '女', 'https://luxurylucky.oss-cn-hangzhou.aliyuncs.com/head2.jpg', '133', '123', '123456');

SET FOREIGN_KEY_CHECKS = 1;
