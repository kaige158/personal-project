@echo off
chcp 65001 >nul
title 宠物领养管理系统

echo ========================================
echo   宠物领养管理系统 - 一键启动
echo ========================================
echo.

:: 检查 MySQL 连接
echo [1/3] 检查 MySQL 连接...
mysql -u root -p12345 -e "SELECT 1" 2>nul
if %errorlevel% neq 0 (
    echo [错误] 无法连接 MySQL，请确认 MySQL 服务是否启动！
    pause
    exit /b 1
)
echo MySQL 连接正常。

:: 初始化数据库（首次运行自动建库建表）
echo [2/3] 初始化数据库...
mysql -u root -p12345 --default-character-set=utf8mb4 -e "CREATE DATABASE IF NOT EXISTS pet_adoption DEFAULT CHARACTER SET utf8mb4" 2>nul
mysql -u root -p12345 --default-character-set=utf8mb4 pet_adoption < src\main\resources\db\init.sql 2>nul
echo 数据库初始化完成。

:: 启动项目
echo [3/3] 启动项目...
echo.
echo 项目启动中，请勿关闭此窗口...
echo 启动成功后访问: http://localhost:8080
echo ========================================
echo.

mvn spring-boot:run

pause
