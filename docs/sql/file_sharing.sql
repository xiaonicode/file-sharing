/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.56.10_3306
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 192.168.56.10:3306
 Source Schema         : file_sharing

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 15/08/2022 20:06:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_catalog
-- ----------------------------
DROP TABLE IF EXISTS `tb_catalog`;
CREATE TABLE `tb_catalog`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `catalog_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录名称',
  `catalog_level` tinyint UNSIGNED NOT NULL COMMENT '目录层级',
  `parent_id` bigint UNSIGNED NOT NULL COMMENT '父目录 ID (0 表示为根目录)',
  `permission` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '权限 (1-私有; 2-公开)',
  `is_recycled` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已被回收 (0-否; 1-是)',
  `creator_id` bigint UNSIGNED NOT NULL COMMENT '创建者 ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已被逻辑删除 (0-否; 1-是)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_catalog_name`(`catalog_name` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_creator_id`(`creator_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '目录信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_catalog
-- ----------------------------
INSERT INTO `tb_catalog` VALUES (1, 'root', 1, 0, 1, 1, 1, '2022-08-13 11:04:15', '2022-08-14 11:15:24', 0);
INSERT INTO `tb_catalog` VALUES (2, 'root2', 1, 0, 1, 1, 1, '2022-08-13 18:07:13', '2022-08-14 11:15:24', 0);
INSERT INTO `tb_catalog` VALUES (3, 'sub1', 2, 1, 1, 1, 1, '2022-08-13 18:08:23', '2022-08-14 11:15:24', 0);
INSERT INTO `tb_catalog` VALUES (4, 'sub1_1', 3, 3, 1, 1, 1, '2022-08-13 18:10:29', '2022-08-14 11:15:24', 0);
INSERT INTO `tb_catalog` VALUES (5, 'admin', 2, 0, 1, 0, 2, '2022-08-13 20:54:16', '2022-08-13 22:47:17', 0);

-- ----------------------------
-- Table structure for tb_file_record
-- ----------------------------
DROP TABLE IF EXISTS `tb_file_record`;
CREATE TABLE `tb_file_record`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `catalog_id` bigint UNSIGNED NOT NULL COMMENT '目录 ID',
  `original_filename` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原始文件名称',
  `unique_filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一文件名称',
  `permission` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '权限 (1-私有; 2-公开)',
  `is_recycled` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已被回收 (0-否; 1-是)',
  `creator_id` bigint UNSIGNED NOT NULL COMMENT '创建者 ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已被逻辑删除 (0-否; 1-是)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_catalog_id`(`catalog_id` ASC) USING BTREE,
  INDEX `idx_creator_id`(`creator_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_file_record
-- ----------------------------
INSERT INTO `tb_file_record` VALUES (1, 1, 'a.txt', 'xxx', 1, 1, 1, '2022-08-13 18:11:51', '2022-08-14 11:15:24', 0);
INSERT INTO `tb_file_record` VALUES (2, 2, 'a.txt', 'yyy', 1, 1, 1, '2022-08-13 18:12:21', '2022-08-14 11:15:24', 0);
INSERT INTO `tb_file_record` VALUES (3, 3, 'b.txt', 'zzz', 1, 1, 1, '2022-08-13 18:13:03', '2022-08-14 11:15:24', 0);
INSERT INTO `tb_file_record` VALUES (4, 4, 'c.txt', 'ooo', 1, 1, 1, '2022-08-13 18:13:19', '2022-08-14 11:15:24', 0);
INSERT INTO `tb_file_record` VALUES (5, 0, 'd.txt', 'qqq', 1, 0, 2, '2022-08-13 18:13:53', '2022-08-13 22:47:17', 0);
INSERT INTO `tb_file_record` VALUES (6, 0, 'a.txt', 'f9f6c4cf0cc84f3182f82dfb584c14c4.txt', 1, 0, 1, '2022-08-14 19:58:13', NULL, 0);
INSERT INTO `tb_file_record` VALUES (7, 0, 'c.txt', '18178ce2619840d9b21ddddcda677d23.txt', 1, 0, 1, '2022-08-14 20:01:20', NULL, 0);
INSERT INTO `tb_file_record` VALUES (8, 0, 'd.txt', '08538851f00c48f183a5e24a1734cdbb.txt', 1, 0, 1, '2022-08-14 20:04:51', NULL, 0);
INSERT INTO `tb_file_record` VALUES (9, 0, 'f.txt', 'a651fc99af52442db43e4e2d051a7dc6.txt', 1, 0, 1, '2022-08-14 20:08:17', NULL, 0);
INSERT INTO `tb_file_record` VALUES (10, 0, 'g.txt', '4b7e8861282d4242a66a71c2c0ed6caf.txt', 1, 0, 1, '2022-08-14 20:11:13', NULL, 0);
INSERT INTO `tb_file_record` VALUES (12, 0, 'b.txt', '719ec5fe19204d4da11e42dd34dd4769.txt', 2, 0, 1, '2022-08-14 22:56:11', '2022-08-14 23:10:29', 0);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已被逻辑删除 (0-否; 1-是)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'xiaoni', '$2a$10$YPPBe/t7ng9J0IOlfskhDu76/4yB.ppe1H2JR3FTrQNWeWTMaJHry', '小妮', '2022-08-12 11:11:20', NULL, 0);
INSERT INTO `tb_user` VALUES (2, 'mike', '$2a$10$fMjc2/EZwBy3aIh2NYtcpOMyoQhL6pgpxIx.2ranCfTZ62qkna95.', '麦克', '2022-08-12 11:13:36', NULL, 0);
INSERT INTO `tb_user` VALUES (3, '', '$2a$10$whpRmJIuKd5jfCrbh16hmOqoga8n4Tw0TRXWUF5/rhY5yz/4b9TZy', '麦克', '2022-08-12 11:15:39', NULL, 0);
INSERT INTO `tb_user` VALUES (4, 'tom', '$2a$10$g2DLVcvkBcA7fIxxd.17L.Gua5hsB351ixnwiKRk.mR996/tBSMMS', '汤姆', '2022-08-12 11:27:51', NULL, 0);

SET FOREIGN_KEY_CHECKS = 1;
