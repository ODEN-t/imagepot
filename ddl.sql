CREATE SEQUENCE user_id_seq
    INCREMENT BY 1
    MAXVALUE 1000
    START WITH 1
    NO CYCLE;

CREATE TABLE mywork.pot_user(
    user_id integer DEFAULT nextval('user_id_seq') PRIMARY KEY,
    user_name varchar(20) NOT NULL,
    user_email varchar(255) NOT NULL UNIQUE,
    user_password varchar(60) NOT NULL,
    user_icon bytea,
    user_role varchar(10) NOT NULL CHECK(user_role = 'ROLE_USER' or user_role = 'ROLE_ADMIN') DEFAULT 'ROLE_USER',
    user_password_updated_at timestamp DEFAULT current_timestamp,
    user_signin_miss_times integer DEFAULT 0,
    user_unlock boolean DEFAULT true,
    user_enabled boolean DEFAULT true,
    user_updated_at timestamp DEFAULT current_timestamp,
    user_created_at timestamp DEFAULT current_timestamp
);

CREATE TABLE mywork.pot_image(
    image_id char(18),
    image_path varchar(2048) NOT NULL,
    user_id integer NOT NULL REFERENCES mywork.pot_user(user_id),
    image_title varchar(20) DEFAULT NULL,
    image_extension varchar(5) NOT NULL,
    image_posted_at timestamp DEFAULT current_timestamp,
    image_deleted_at timestamp DEFAULT NULL,
    image_restored_at timestamp DEFAULT NULL,
    image_is_deleted DEFAULT false,
    PRIMARY KEY(image_id, user_id)
);

-- 削除(依存関係から上から順に)
DELETE from mywork.pot_image a where a.user_id = 1;
DELETE from mywork.pot_user a where a.user_id = 1;
DROP TABLE mywork.pot_image;
DROP TABLE mywork.pot_user;
DROP SEQUENCE user_id_seq;


-- 初期insert pot_user
insert into mywork.pot_user (user_email, user_password, user_name, user_icon, user_role) values (
    'monstersinc2311@gmail.com', '$2a$10$TqJfagRv7dqeL8yip5DbwOE.jcIMQ0K1EHhltD01JFC2zgon4DAza', '☆Michael Wazowski★', pg_read_binary_file('/testimage/monster/mike.jpg'), 'ROLE_ADMIN'
);
insert into mywork.pot_user (user_email, user_password, user_name, user_role) values (
    'eren@gmail.com', 'password', 'Eren Yeager', 'ROLE_ADMIN'
);


-- 初期insert pot_image
insert into mywork.pot_image (image_id, image_path, user_id, image_title, image_extension, image_is_deleted) values (
    to_char(current_timestamp, 'yyyymmddhh24mmssms'), 'https://imagepot-app.s3-ap-northeast-1.amazonaws.com/album1/321_main_visual_name_1-min.jpg', 1, 'モンスターズ・インク', 'jpg', false
);

-- バイナリデータ読み込み
select pg_read_binary_file('/testimage/monster/mike.jpg');
-- ファイル情報取得
select * from pg_ls_dir('/testimage/monster/640-min.jpg');