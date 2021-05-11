CREATE SEQUENCE user_seq
    INCREMENT BY 1
    MAXVALUE 1000
    START WITH 1
    NO CYCLE;


CREATE TABLE pot_user(
    user_id bigint DEFAULT nextval('user_seq') PRIMARY KEY,
    name varchar(20) NOT NULL,
    email varchar(255) NOT NULL UNIQUE,
    password varchar(60) NOT NULL,
    icon bytea,
    role varchar(10) NOT NULL CHECK(role = 'ROLE_USER' or role = 'ROLE_ADMIN') DEFAULT 'ROLE_USER',
    password_updated_at timestamp DEFAULT current_timestamp,
    signin_miss_times integer DEFAULT 0,
    unlock boolean DEFAULT true,
    enabled boolean DEFAULT true,
    updated_at timestamp DEFAULT current_timestamp,
    created_at timestamp DEFAULT current_timestamp
);

CREATE TABLE pot_image(
    user_id integer NOT NULL REFERENCES pot_user(user_id),
    image_id char(18),
    path varchar(2048) NOT NULL,
    title varchar(20) DEFAULT NULL,
    extension varchar(5) NOT NULL,
    posted_at timestamp DEFAULT current_timestamp,
    deleted_at timestamp DEFAULT NULL,
    restored_at timestamp DEFAULT NULL,
    is_deleted boolean DEFAULT false,
    PRIMARY KEY(image_id, user_id)
);

-- 削除(依存関係から上から順に)
DELETE from pot_image a where a.user_id = 1;
DELETE from pot_user a where a.user_id = 1;
DROP TABLE pot_image;
DROP TABLE pot_user;
DROP SEQUENCE user_seq;


-- 初期insert pot_user
insert into pot_user (email, password, name, icon, role) values (
    'monstersinc2311@gmail.com', '$2a$10$TqJfagRv7dqeL8yip5DbwOE.jcIMQ0K1EHhltD01JFC2zgon4DAza', '☆Michael Wazowski★', null, 'ROLE_ADMIN'
);
insert into pot_user (email, password, name, role) values (
    'eren@gmail.com', 'password', 'Eren Yeager', 'ROLE_USER'
);


-- 初期insert pot_image
insert into mywork.pot_image (image_id, image_path, user_id, image_title, image_extension, image_is_deleted) values (
    to_char(current_timestamp, 'yyyymmddhh24mmssms'), 'https://imagepot-app.s3-ap-northeast-1.amazonaws.com/album1/321_main_visual_name_1-min.jpg', 1, 'モンスターズ・インク', 'jpg', false
);

-- バイナリデータ読み込み
select pg_read_binary_file('/testimage/monster/mike.jpg');
-- ファイル情報取得
select * from pg_ls_dir('/testimage/monster/640-min.jpg');