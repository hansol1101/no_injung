-- Users 테이블 마이그레이션 스크립트
-- email 필드 제거 및 birth_date 필드 추가

-- 1. email 컬럼 제거 (기존 인덱스도 함께 제거됨)
ALTER TABLE users DROP COLUMN IF EXISTS email;

-- 2. birth_date 컬럼 추가 (없다면)
ALTER TABLE users ADD COLUMN IF NOT EXISTS birth_date DATE;

-- 3. role 컬럼 추가 (없다면)
ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(20) NOT NULL DEFAULT 'USER';

-- 4. 기존 email 인덱스 제거 (이미 위에서 제거됨)
-- ALTER TABLE users DROP INDEX IF EXISTS idx_email;

-- 5. nickname 인덱스 확인 및 추가
CREATE INDEX IF NOT EXISTS idx_nickname ON users(nickname); 