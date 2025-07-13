-- data/db/init.sql

-- 데이터베이스 생성 및 사용
CREATE DATABASE IF NOT EXISTS login_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE login_db;

-- 사용자 생성 및 권한 부여
CREATE USER IF NOT EXISTS 'user'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON login_db.* TO 'user'@'%';
FLUSH PRIVILEGES;

-- 사용자 테이블
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    auth VARCHAR(100),
    point INT DEFAULT 0,
    ranks VARCHAR(20) DEFAULT 'normal',
    `role` VARCHAR(20) DEFAULT 'guest',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 게시글 테이블
CREATE TABLE posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    file_name VARCHAR(255),
    encrypted_file_name VARCHAR(255),
    encrypted_file_data LONGTEXT,
    user_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);