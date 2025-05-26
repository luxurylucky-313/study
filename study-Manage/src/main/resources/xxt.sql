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

 Date: 26/05/2025 18:34:39
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
  `teacher_id` int NOT NULL COMMENT '教师ID',
  `course_id` int NOT NULL COMMENT '关联的课程ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '作业标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '作业描述',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态(0:未发布,1:已发布)',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_teacher_id`(`teacher_id` ASC) USING BTREE,
  INDEX `idx_course_id`(`course_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '作业信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of homework
-- ----------------------------
INSERT INTO `homework` VALUES (1, 1, 4, '作业1：基础练习', '完成课本第1章练习题1-10', '2025-06-01 08:00:00', '2025-06-10 23:59:59', 1, '2025-05-26 14:27:33', '2025-05-26 14:27:33');
INSERT INTO `homework` VALUES (2, 1, 1, '作业2：进阶练习', '完成练习册第2章习题A部分', '2025-06-11 08:00:00', '2025-06-20 23:59:59', 1, '2025-05-26 14:27:33', '2025-05-26 14:27:33');
INSERT INTO `homework` VALUES (3, 1, 5, '作业3：综合测试', '在线平台完成10道题目', '2025-06-21 08:00:00', '2025-06-30 23:59:59', 1, '2025-05-26 14:27:33', '2025-05-26 14:27:33');
INSERT INTO `homework` VALUES (4, 1, 5, '作业4：复习与总结', '整理前三个章节知识点并提交笔记', '2025-07-01 08:00:00', '2025-07-10 23:59:59', 1, '2025-05-26 14:27:33', '2025-05-26 14:27:33');
INSERT INTO `homework` VALUES (5, 1, 5, '作业5：应用实践', '完成实际问题建模练习', '2025-07-11 08:00:00', '2025-07-20 23:59:59', 1, '2025-05-26 14:27:33', '2025-05-26 14:27:33');
INSERT INTO `homework` VALUES (6, 1, 1, '作业6：编程入门', '编写简单程序实现功能', '2025-07-21 08:00:00', '2025-07-30 23:59:59', 1, '2025-05-26 14:27:33', '2025-05-26 14:27:33');
INSERT INTO `homework` VALUES (7, 1, 5, '作业7：项目设计', '设计一个小项目方案并提交文档', '2025-08-01 08:00:00', '2025-08-10 23:59:59', 1, '2025-05-26 14:27:33', '2025-05-26 14:27:33');
INSERT INTO `homework` VALUES (8, 1, 1, '作业8：代码调试', '下载模板代码并修复错误', '2025-08-11 08:00:00', '2025-08-20 23:59:59', 1, '2025-05-26 14:27:33', '2025-05-26 14:27:33');
INSERT INTO `homework` VALUES (9, 1, 5, '作业9：算法练习', '完成5个排序算法实现', '2025-08-21 08:00:00', '2025-08-30 23:59:59', 1, '2025-05-26 14:27:33', '2025-05-26 14:27:33');
INSERT INTO `homework` VALUES (10, 1, 1, '作业10：期末考核', '独立完成最终项目并提交成果', '2025-08-31 08:00:00', '2025-09-10 23:59:59', 1, '2025-05-26 14:27:33', '2025-05-26 14:27:33');
INSERT INTO `homework` VALUES (11, 1, 5, '作业1：基础练习', '完成课本第1章练习题1-10', '2025-06-01 08:00:00', '2025-06-10 23:59:59', 1, '2025-05-26 14:27:43', '2025-05-26 14:27:43');
INSERT INTO `homework` VALUES (12, 1, 1, '作业2：进阶练习', '完成练习册第2章习题A部分', '2025-06-11 08:00:00', '2025-06-20 23:59:59', 1, '2025-05-26 14:27:43', '2025-05-26 14:27:43');
INSERT INTO `homework` VALUES (13, 1, 1, '作业3：综合测试', '在线平台完成10道题目', '2025-06-21 08:00:00', '2025-06-30 23:59:59', 1, '2025-05-26 14:27:43', '2025-05-26 14:27:43');
INSERT INTO `homework` VALUES (14, 1, 1, '作业4：复习与总结', '整理前三个章节知识点并提交笔记', '2025-07-01 08:00:00', '2025-07-10 23:59:59', 1, '2025-05-26 14:27:43', '2025-05-26 14:27:43');
INSERT INTO `homework` VALUES (15, 1, 5, '作业5：应用实践', '完成实际问题建模练习', '2025-07-11 08:00:00', '2025-07-20 23:59:59', 1, '2025-05-26 14:27:43', '2025-05-26 14:27:43');
INSERT INTO `homework` VALUES (16, 1, 4, '作业6：编程入门', '编写简单程序实现功能', '2025-07-21 08:00:00', '2025-07-30 23:59:59', 1, '2025-05-26 14:27:43', '2025-05-26 14:27:43');
INSERT INTO `homework` VALUES (17, 1, 4, '作业7：项目设计', '设计一个小项目方案并提交文档', '2025-08-01 08:00:00', '2025-08-10 23:59:59', 1, '2025-05-26 14:27:43', '2025-05-26 14:27:43');
INSERT INTO `homework` VALUES (18, 1, 2, '作业8：代码调试', '下载模板代码并修复错误', '2025-08-11 08:00:00', '2025-08-20 23:59:59', 1, '2025-05-26 14:27:43', '2025-05-26 14:27:43');
INSERT INTO `homework` VALUES (19, 1, 3, '作业9：算法练习', '完成5个排序算法实现', '2025-08-21 08:00:00', '2025-08-30 23:59:59', 1, '2025-05-26 14:27:43', '2025-05-26 14:27:43');
INSERT INTO `homework` VALUES (20, 1, 3, '作业10：期末考核', '独立完成最终项目并提交成果', '2025-08-31 08:00:00', '2025-09-10 23:59:59', 1, '2025-05-26 14:27:43', '2025-05-26 14:27:43');

-- ----------------------------
-- Table structure for homework_question
-- ----------------------------
DROP TABLE IF EXISTS `homework_question`;
CREATE TABLE `homework_question`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '题目ID',
  `homework_id` int NOT NULL COMMENT '关联的作业ID',
  `type` tinyint NOT NULL COMMENT '题目类型(1:选择题,2:判断题,3:简答题)',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '题目内容',
  `options` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '选择题选项(JSON格式)',
  `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '参考答案',
  `score` int NOT NULL DEFAULT 0 COMMENT '分值',
  `sequence` int NOT NULL DEFAULT 0 COMMENT '题目顺序',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_homework_id`(`homework_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1805533186 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '作业题目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of homework_question
-- ----------------------------
INSERT INTO `homework_question` VALUES (-1835122686, 1, 1, '测试', '{\"A\":\"1\",\"B\":\"2\",\"C\":\"3\",\"D\":\"4\"}', 'A', 5, 1, '2025-05-26 17:06:29', '2025-05-26 17:06:29');

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
