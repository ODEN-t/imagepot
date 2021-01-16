create table mywork.users(
    user_id varchar(10) not null primary key,
    user_pass varchar(10) not null,
    firstname varchar(20) not null,
    lastname varchar(20) not null,
    create_date timestamp not null,
    last_access_date timestamp,
    rec_status smallint DEFAULT 00,
    passed boolean DEFAULT FALSE
);

insert into users(
    user_id,
    user_pass,
    firstname,
    lastname,
    create_date
) values(
    'testid01',
    'password',
    'テスト',
    '小太郎',
    current_timestamp
);

create table mywork.upload_data(
    data_id integer not null primary key,
    user_id integer not null,
    data bytea not null,
    data_name varchar(255) not null,
    upload_date timestamp not null,
    delete_date timestamp,
    rec_status smallint DEFAULT 00 
);