-- =====================================================
-- 宠物领养管理系统 - 数据库初始化脚本
-- 使用方法：在 MySQL 中先创建数据库 pet_adoption，再执行此脚本
-- CREATE DATABASE IF NOT EXISTS pet_adoption DEFAULT CHARACTER SET utf8mb4;
-- USE pet_adoption;
-- =====================================================

-- ----------------------------
-- 1. 用户表
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `address` VARCHAR(255) DEFAULT NULL COMMENT '地址',
    `role` TINYINT NOT NULL DEFAULT 0 COMMENT '0=普通用户, 1=管理员',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0=正常, 1=禁用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- 2. 机构/收容所表
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` VARCHAR(100) NOT NULL COMMENT '机构名称',
    `address` VARCHAR(255) DEFAULT NULL COMMENT '地址',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `description` TEXT DEFAULT NULL COMMENT '简介',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构表';

-- ----------------------------
-- 3. 宠物分类表
-- ----------------------------
DROP TABLE IF EXISTS `pet_category`;
CREATE TABLE `pet_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名(猫/狗/其他)',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物分类表';

-- ----------------------------
-- 4. 宠物表
-- ----------------------------
DROP TABLE IF EXISTS `pet`;
CREATE TABLE `pet` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` VARCHAR(50) NOT NULL COMMENT '宠物名字',
    `category_id` BIGINT DEFAULT NULL COMMENT '品种分类ID',
    `breed` VARCHAR(50) DEFAULT NULL COMMENT '品种(如英短/金毛)',
    `age` VARCHAR(20) DEFAULT NULL COMMENT '年龄描述(如3个月/2岁)',
    `gender` TINYINT DEFAULT 0 COMMENT '0=母, 1=公',
    `size` VARCHAR(20) DEFAULT NULL COMMENT '体型:小型/中型/大型',
    `health_status` VARCHAR(255) DEFAULT NULL COMMENT '健康状况',
    `description` TEXT DEFAULT NULL COMMENT '详细描述/性格',
    `org_id` BIGINT DEFAULT NULL COMMENT '所在机构ID',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0=待领养, 1=已领养, 2=已下架',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category_id`),
    KEY `idx_status` (`status`),
    KEY `idx_org` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物表';

-- ----------------------------
-- 5. 宠物照片表
-- ----------------------------
DROP TABLE IF EXISTS `pet_image`;
CREATE TABLE `pet_image` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `pet_id` BIGINT NOT NULL COMMENT '关联宠物ID',
    `image_url` VARCHAR(500) NOT NULL COMMENT '图片路径',
    `sort_order` INT DEFAULT 0 COMMENT '排序号(轮播顺序)',
    PRIMARY KEY (`id`),
    KEY `idx_pet_id` (`pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物照片表';

-- ----------------------------
-- 6. 领养申请表
-- ----------------------------
DROP TABLE IF EXISTS `adoption_application`;
CREATE TABLE `adoption_application` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '申请人ID',
    `pet_id` BIGINT NOT NULL COMMENT '意向宠物ID',
    `occupation` VARCHAR(100) DEFAULT NULL COMMENT '职业',
    `housing_type` VARCHAR(50) DEFAULT NULL COMMENT '住房类型(自有/租房)',
    `has_experience` TINYINT DEFAULT 0 COMMENT '有无饲养经验(0=无,1=有)',
    `reason` TEXT DEFAULT NULL COMMENT '申请理由',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0=待审核,1=已通过,2=已驳回,3=已领养',
    `audit_comment` VARCHAR(500) DEFAULT NULL COMMENT '审核意见',
    `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_pet_id` (`pet_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='领养申请表';

-- ----------------------------
-- 7. 领养协议表
-- ----------------------------
DROP TABLE IF EXISTS `adoption_agreement`;
CREATE TABLE `adoption_agreement` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `application_id` BIGINT NOT NULL COMMENT '关联申请表ID',
    `user_id` BIGINT NOT NULL COMMENT '领养人ID',
    `pet_id` BIGINT NOT NULL COMMENT '宠物ID',
    `content` TEXT DEFAULT NULL COMMENT '协议正文',
    `user_sign` TINYINT NOT NULL DEFAULT 0 COMMENT '0=未签,1=已签',
    `sign_time` DATETIME DEFAULT NULL COMMENT '签署时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    PRIMARY KEY (`id`),
    KEY `idx_application` (`application_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='领养协议表';

-- ----------------------------
-- 8. 收藏表
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `pet_id` BIGINT NOT NULL COMMENT '宠物ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_pet` (`user_id`, `pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- ----------------------------
-- 9. 评论表
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '评论人ID',
    `pet_id` BIGINT DEFAULT NULL COMMENT '被评论宠物ID',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `score` TINYINT DEFAULT 5 COMMENT '评分1-5',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_pet_id` (`pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- ----------------------------
-- 10. 科普文章表
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT DEFAULT NULL COMMENT '内容',
    `cover_image` VARCHAR(500) DEFAULT NULL COMMENT '封面图',
    `like_count` INT NOT NULL DEFAULT 0 COMMENT '点赞数',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览量',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科普文章表';

-- ----------------------------
-- 11. 通知表
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '接收用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '通知标题',
    `content` TEXT DEFAULT NULL COMMENT '通知内容',
    `is_read` TINYINT NOT NULL DEFAULT 0 COMMENT '0=未读, 1=已读',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '通知时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_read` (`user_id`, `is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- ----------------------------
-- 12. 公告表
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `title` VARCHAR(200) NOT NULL COMMENT '公告标题',
    `content` TEXT DEFAULT NULL COMMENT '公告内容',
    `is_top` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶(0=否,1=是)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- =====================================================
-- 测试数据
-- =====================================================

-- 管理员账号：admin / 123456（BCrypt 加密后的密码）
INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `role`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', '13800000000', 1),
('zhangsan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张三', '13800000001', 0),
('lisi', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李四', '13800000002', 0);

-- 分类数据
INSERT INTO `pet_category` (`name`) VALUES ('猫'), ('狗'), ('其他');

-- 机构数据
INSERT INTO `organization` (`name`, `address`, `phone`, `description`) VALUES
('阳光动物救助中心', '南宁市西乡塘区大学东路100号', '0771-1234567', '成立于2015年，致力于流浪动物救助与领养推广，已帮助超过2000只小动物找到新家。'),
('爱心宠物之家', '南宁市青秀区民族大道200号', '0771-9876543', '由宠物医生创办的专业救助机构，配有医疗室和隔离区，确保每一只待领养宠物的健康。');

-- 宠物数据
INSERT INTO `pet` (`name`, `category_id`, `breed`, `age`, `gender`, `size`, `health_status`, `description`, `org_id`, `status`) VALUES
('小橘', 1, '橘猫', '6个月', 0, '小型', '已驱虫、已疫苗、已绝育', '性格温顺亲人，喜欢蹭人撒娇，会用猫砂盆，适合新手饲养。', 1, 0),
('花花', 1, '三花猫', '1岁', 0, '小型', '已驱虫、已疫苗', '活泼好动，喜欢玩耍逗猫棒，但有点胆小需要耐心陪伴。', 1, 0),
('旺财', 2, '金毛', '2岁', 1, '大型', '已驱虫、已疫苗、已绝育', '性格非常温顺，对人友好，会基本指令（坐下、握手），适合有庭院的家庭。', 2, 0),
('小黑', 2, '中华田园犬', '8个月', 1, '中型', '已驱虫、已疫苗', '聪明机警，忠诚护主，适合看家护院，需要每天遛弯。', 1, 0),
('小白', 3, '荷兰兔', '3个月', 0, '小型', '健康活泼', '毛色纯白，性格安静，饲养简单，适合学生或上班族。', 2, 0),
('豆豆', 1, '英短蓝猫', '1岁', 1, '中型', '已驱虫、已疫苗、已绝育', '品相优良，圆圆的脸很可爱，性格独立但也会撒娇。', 2, 0),
('大黄', 2, '拉布拉多', '3岁', 1, '大型', '已驱虫、已疫苗、已绝育', '导盲犬退役犬，性格极其温顺，受过专业训练，适合有小孩的家庭。', 1, 0),
('糯米', 1, '布偶猫', '10个月', 0, '中型', '已驱虫、已疫苗、已绝育', '颜值超高的布偶猫，性格粘人，喜欢被抱，毛发需要定期打理。', 2, 0);

-- 公告数据
INSERT INTO `announcement` (`title`, `content`, `is_top`) VALUES
('欢迎来到宠物领养平台', '本平台致力于为流浪动物寻找温暖的家。领养代替购买，给它们一个家！', 1),
('本周六开放日通知', '本周六（5月24日）上午9:00-12:00，阳光动物救助中心将举办线下领养开放日，欢迎前来参观。', 0);

-- 科普文章
INSERT INTO `article` (`title`, `content`, `like_count`, `view_count`) VALUES
('新手养猫必备清单', '养猫之前需要准备猫粮、猫砂盆、猫抓板、食盆水盆、猫窝等必备物品。建议先带猫咪做一次全面体检，按时打疫苗和驱虫。', 15, 230),
('狗狗日常护理指南', '每天遛狗2-3次，每次不少于30分钟；每周梳毛1-2次；每月洗澡1-2次（不要过于频繁）；定期修剪指甲和清理耳朵。', 8, 156),
('领养代替购买的意义', '每年有数以万计的流浪动物等待被领养。领养不仅给它们一个家，也减轻了救助机构的压力。每一只被领养的小动物，都是一个生命的新开始。', 42, 520);
