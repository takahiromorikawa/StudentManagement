CREATE TABLE IF NOT EXISTS students (
    ID BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    age INT DEFAULT NULL,
    name_kana VARCHAR(50) DEFAULT NULL,
    nickname VARCHAR(50) DEFAULT NULL,
    mailaddress VARCHAR(50) DEFAULT NULL,
    live VARCHAR(50) DEFAULT NULL,
    sex VARCHAR(50) DEFAULT NULL,
    remark VARCHAR(20) DEFAULT NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS students_courses (
    id_bigint BIGINT NOT NULL AUTO_INCREMENT,
    students_ID BIGINT NOT NULL,
    course_name VARCHAR(50) NOT NULL,
    start DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    endplan DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_bigint),
    FOREIGN KEY (students_ID) REFERENCES students(ID)
);

CREATE TABLE course_status (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_course_id BIGINT NOT NULL UNIQUE,
    course_status VARCHAR(255) NOT NULL
);

INSERT INTO students (name, age, name_kana, nickname, mailaddress, live, sex, ID, remark, is_deleted) VALUES
('五条悟', 25, 'ゴジョウサトル', 'さとる', 'gojyou@example.com', '山梨', '女', 3, '', 0),
('三木正人', 50, 'ミキマサト', 'マート', 'miki@example.com', '三ノ宮', '男', 8, '', 0),
('山田太郎', 10, 'ヤマダタロウ', '太郎', 'tarou@example.com', '埼玉', '男', 9, NULL, 0);

INSERT INTO students_courses (start, endplan, course_name, students_ID, id_bigint) VALUES
('2026-04-18 16:08:16', '2026-04-18 16:08:16', 'デザインコース', 3, 1),
('2026-04-18 16:25:55', '2026-04-18 16:25:55', 'AWSコース', 8, 2),
('2026-04-18 18:44:19', '2026-04-18 18:44:19', 'お絵描きコース', 9, 3);

-- 初期データ投入後に採番位置を調整
ALTER TABLE students ALTER COLUMN ID RESTART WITH 10;
ALTER TABLE students_courses ALTER COLUMN id_bigint RESTART WITH 4;
