create table users(
   id varchar(50) primary key,
   password varchar(255) not null,
   nickname varchar(50),
   role varchar(50) default 'USER',
   created_at timestamp default current_timestamp
);

create table post(
   id varchar(50) primary key,
   title varchar(255) not null,
   content text not null,
   user_id varchar(50) not null,
   created_at timestamp default current_timestamp,
   foreign key(user_id) references users(id)
);


create table comment(
   id varchar(50) primary key,
   post_id varchar(50) not null,
   user_id varchar(50) not null,
   content TEXT not null,
   parent_id varchar(50),
   created_at timestamp default current_timestamp,
   foreign key(user_id) references users(id),
   foreign key(post_id) references post(id),
   FOREIGN KEY (parent_id) REFERENCES comment(id)
);