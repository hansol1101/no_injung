-- Users 테이블 스키마 (email 필드 제거, birth_date 필드 추가)
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nickname VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    birth_date DATE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    
    INDEX idx_nickname (nickname)
);

-- 기존 테이블이 있다면 email 컬럼 제거
-- ALTER TABLE users DROP COLUMN IF EXISTS email;
-- ALTER TABLE users DROP INDEX IF EXISTS idx_email;

-- birth_date 컬럼 추가 (없다면)
-- ALTER TABLE users ADD COLUMN IF NOT EXISTS birth_date DATE;

-- role 컬럼 추가 (없다면)
-- ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(20) NOT NULL DEFAULT 'USER'; 