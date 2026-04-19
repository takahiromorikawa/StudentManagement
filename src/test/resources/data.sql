INSERT INTO students (name, age, name_kana, nickname, mailaddress, live, sex, ID, remark, is_deleted) VALUES
                                                                                                          ('五条悟', 25, 'ゴジョウサトル', 'さとる', 'gojyou@example.com', '山梨', '女', 3, '', 0),
                                                                                                          ('三木正人', 50, 'ミキマサト', 'マート', 'miki@example.com', '三ノ宮', '男', 8, '', 0),
                                                                                                          ('山田太郎', 10, 'ヤマダタロウ', '太郎', 'tarou@example.com', '埼玉', '男', 9, NULL, 0);

INSERT INTO students_courses (start, endplan, course_name, students_ID, id_bigint) VALUES
                                                                                       ('2026-04-18 16:08:16', '2026-04-18 16:08:16', 'デザインコース', 3, 1),
                                                                                       ('2026-04-18 16:25:55', '2026-04-18 16:25:55', 'AWSコース', 8, 2),
                                                                                       ('2026-04-18 18:44:19', '2026-04-18 18:44:19', 'お絵描きコース', 9, 3);

