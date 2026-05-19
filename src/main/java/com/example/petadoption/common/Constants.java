package com.example.petadoption.common;

/**
 * 系统常量定义
 * 集中管理各种状态码和角色标识，避免在代码里散落"魔法数字"
 */
public class Constants {

    // ===== 用户角色 =====
    /** 普通用户 */
    public static final int ROLE_USER = 0;
    /** 管理员 */
    public static final int ROLE_ADMIN = 1;

    // ===== 用户状态 =====
    public static final int USER_STATUS_NORMAL = 0;
    public static final int USER_STATUS_DISABLED = 1;

    // ===== 宠物状态 =====
    /** 待领养 */
    public static final int PET_STATUS_AVAILABLE = 0;
    /** 已领养 */
    public static final int PET_STATUS_ADOPTED = 1;
    /** 已下架 */
    public static final int PET_STATUS_OFF = 2;

    // ===== 申请状态 =====
    /** 待审核 */
    public static final int APP_STATUS_PENDING = 0;
    /** 已通过 */
    public static final int APP_STATUS_APPROVED = 1;
    /** 已驳回 */
    public static final int APP_STATUS_REJECTED = 2;
    /** 已领养（用户已签署协议） */
    public static final int APP_STATUS_ADOPTED = 3;

    // ===== 宠物性别 =====
    /** 母 */
    public static final int PET_GENDER_FEMALE = 0;
    /** 公 */
    public static final int PET_GENDER_MALE = 1;

    // ===== 通知状态 =====
    public static final int NOTIFICATION_UNREAD = 0;
    public static final int NOTIFICATION_READ = 1;

    // ===== 协议签署状态 =====
    public static final int AGREEMENT_UNSIGNED = 0;
    public static final int AGREEMENT_SIGNED = 1;

    // ===== Session 键名 =====
    /** 存储在 session 中的用户对象 key */
    public static final String SESSION_USER = "loginUser";

    // ===== 分页默认值 =====
    public static final long DEFAULT_PAGE = 1;
    public static final long DEFAULT_SIZE = 10;
}
