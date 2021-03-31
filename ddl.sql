CREATE SEQUENCE user_id_seq
    INCREMENT BY 1
    MAXVALUE 1000
    START WITH 1
    NO CYCLE;

CREATE TABLE mywork.pot_user(
    user_id integer DEFAULT nextval('user_id_seq') PRIMARY KEY,
    user_address varchar(255) NOT NULL UNIQUE,
    user_password varchar(60) NOT NULL,
    user_name varchar(10) NOT NULL,
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

-- 初期insert pot_user
insert into mywork.pot_user (user_address, user_password, user_name, user_role) values (
    'abcde@gmail.com', '$2a$08$YC.mKbmw6a6GWM9H2icZsuVmIzdhvFtK51FU3rceNR2s4fScR2NDW', '坂本龍馬', 'ROLE_ADMIN'
);

-- 初期insert pot_image
insert into mywork.pot_image (image_id, image_path, user_id, image_title, image_extension, image_record_status) values (
    to_char(current_timestamp, 'yyyymmddhh24mmssms'), 'https://s3-ap-northeast-1.amazonaws.com/imagepot-app/testdataA.jpg', 1, 'テストタイトル', 'jpg', 0
);
