create or replace table bank
(
    balance double       not null,
    id      bigint auto_increment
        primary key,
    user_id bigint       null,
    address varchar(255) not null,
    name    varchar(255) not null
);

create or replace table user
(
    balance    double       not null,
    enabled    bit          null,
    bank_id    bigint       null,
    user_id    bigint auto_increment
        primary key,
    created_at varchar(255) null,
    email      varchar(255) null,
    first_name varchar(255) null,
    last_name  varchar(255) null,
    pwd        varchar(255) null,
    username   varchar(255) null,
    constraint FKlhs64uv6rna1fxa30cmjjt0we
        foreign key (bank_id) references bank (id)
);

alter table bank
    add constraint FK2win8j1uec7jclkhq93hv0tda
        foreign key (user_id) references user (user_id);

create or replace table contact
(
    contact_id bigint null,
    id         bigint auto_increment
        primary key,
    user_id    bigint null,
    constraint FKe07k4jcfdophemi6j1lt84b61
        foreign key (user_id) references user (user_id),
    constraint FKqgj7ka9h4nt6snuo4tpj7bh52
        foreign key (contact_id) references user (user_id)
);

create or replace table transaction
(
    amount           double       not null,
    created_at       datetime(6)  null,
    emitter_bank_id  bigint       null,
    emitter_user_id  bigint       null,
    id               bigint auto_increment
        primary key,
    receiver_bank_id bigint       null,
    receiver_user_id bigint       null,
    description      varchar(255) null,
    constraint FK41ksrq308xhdrlteviuiip9xr
        foreign key (emitter_bank_id) references bank (id),
    constraint FK749934s5t25a9a43o3td06k5f
        foreign key (receiver_user_id) references user (user_id),
    constraint FKe1ps4yopu8vr0xwq8637vgxt6
        foreign key (receiver_bank_id) references bank (id),
    constraint FKtmd3qm9xn3q2mnmw08c6bns0f
        foreign key (emitter_user_id) references user (user_id)
);

create or replace table user_role
(
    user_id bigint       not null,
    role    varchar(255) null,
    constraint FK859n2jvi8ivhui0rl0esws6o
        foreign key (user_id) references user (user_id)
);

create or replace table verif_token
(
    expiry_date datetime(6)  null,
    id          bigint       not null
        primary key,
    user_id     bigint       not null,
    token       varchar(255) null,
    constraint UK_ef1j0br2syaec1161w6h6jm16
        unique (user_id),
    constraint FKtl7lsibj0i8mv8rewq29hecgf
        foreign key (user_id) references user (user_id)
);

create or replace table verif_token_seq
(
    next_val bigint null
);

