create table mywork.pot_user(
    user_id serial PRIMARY KEY,
    user_login_id varchar(10) NOT NULL UNIQUE,
    user_password varchar(70) NOT NULL,
    user_firstname varchar(30) NOT NULL,
    user_lastname varchar(30) NOT NULL,
    user_nickname varchar(10) NOT NULL,
    user_role varchar(10) NOT NULL CHECK(user_role = 'ROLE_USER' or user_role = 'ROLE_ADMIN') DEFAULT 'ROLE_USER',
    user_created_at timestamp DEFAULT current_timestamp,
    user_updated_at timestamp DEFAULT current_timestamp,
    user_logging_in boolean NOT NULL DEFAULT false
);

create table mywork.pot_image(
    image_id char(5) PRIMARY KEY,
    image_path varchar(2048) NOT NULL,
    user_id serial NOT NULL REFERENCES mywork.pot_user(user_id),
    image_title varchar(20) DEFAULT NULL,
    image_extension varchar(5) NOT NULL,
    image_posted_at timestamp DEFAULT current_timestamp,
    image_deleted_at timestamp DEFAULT NULL,
    image_restored_at timestamp DEFAULT NULL,
    image_record_status smallint NOT NULL CHECK(image_record_status = 00 or image_record_status = 99) DEFAULT 00 
);