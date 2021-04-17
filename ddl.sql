CREATE SEQUENCE user_id_seq
    INCREMENT BY 1
    MAXVALUE 1000
    START WITH 1
    NO CYCLE;

CREATE TABLE mywork.pot_user(
    user_id integer DEFAULT nextval('user_id_seq') PRIMARY KEY,
    user_email varchar(255) NOT NULL UNIQUE,
    user_password varchar(60) NOT NULL,
    user_name varchar(20) NOT NULL,
    user_icon bytea,
    user_role varchar(10) NOT NULL CHECK(user_role = 'ROLE_USER' or user_role = 'ROLE_ADMIN') DEFAULT 'ROLE_USER',
    user_created_at timestamp DEFAULT current_timestamp,
    user_updated_at timestamp DEFAULT current_timestamp,
    user_logging_in boolean NOT NULL DEFAULT false
);

create table mywork.pot_image(
    image_id char(18) PRIMARY KEY,
    image_path varchar(2048) NOT NULL,
    user_id integer NOT NULL REFERENCES mywork.pot_user(user_id),
    image_title varchar(20) DEFAULT NULL,
    image_extension varchar(5) NOT NULL,
    image_posted_at timestamp DEFAULT current_timestamp,
    image_deleted_at timestamp DEFAULT NULL,
    image_restored_at timestamp DEFAULT NULL,
    image_record_status integer NOT NULL CHECK(image_record_status = 0 or image_record_status = 1) DEFAULT 1
);

-- 削除(依存関係から上から順に)
DELETE from mywork.pot_image a where a.user_id = 1;
DELETE from mywork.pot_user a where a.user_id = 1;
DROP TABLE mywork.pot_image;
DROP TABLE mywork.pot_user;
DROP SEQUENCE user_id_seq;


-- 初期insert pot_user
insert into mywork.pot_user (user_email, user_password, user_name, user_icon, user_role) values (
    'monstersinc2311@gmail.com', 'password', '☆Michael Wazowski★', pg_read_binary_file('/testimage/monster/mike.jpg'), 'ROLE_ADMIN'
);
insert into mywork.pot_user (user_email, user_password, user_name, user_role) values (
    'aaaaa@gmail.com', 'password1', '田中太郎', 'ROLE_USER'
);


-- 初期insert pot_image
insert into mywork.pot_image (image_id, image_path, user_id, image_title, image_extension, image_record_status) values (
    to_char(current_timestamp, 'yyyymmddhh24mmssms'), '1617672574670_body_120204.jpeg', 2, 'アラジンの画像', 'jpg', 1
);

-- バイナリデータ読み込み
select pg_read_binary_file('/testimage/monster/mike.jpg');
-- ファイル情報取得
select * from pg_ls_dir('/testimage/monster/640-min.jpg');