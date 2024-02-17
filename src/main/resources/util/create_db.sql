create or replace table bank
(
    balance double       not null,
    id      bigint auto_increment primary key,
    user_id bigint       null,
    address varchar(255) not null,
    name    varchar(255) not null
);

create or replace table user
(
    balance    double       not null,
    enabled    bit          null,
    bank_id    bigint       null,
    user_id    bigint auto_increment primary key,
    created_at varchar(255) null,
    email      varchar(255) null,
    first_name varchar(255) null,
    last_name  varchar(255) null,
    pwd        varchar(255) null,
    username   varchar(255) null
);


alter table bank
    add constraint fk_bank_user_id foreign key (user_id) references user (user_id);

alter table user
    add constraint fk_user_bank_id foreign key (bank_id) references bank (id);

create or replace table contact
(
    contact_id bigint not null,
    user_id    bigint not null,
    primary key (contact_id, user_id),
    CONSTRAINT fk_contact_user_id FOREIGN KEY (user_id) REFERENCES user (user_id),
    CONSTRAINT fk_contact_contact_id FOREIGN KEY (contact_id) REFERENCES user (user_id)
);

create or replace table transaction
(
    amount           double       not null,
    created_at       datetime(6)  null,
    emitter_bank_id  bigint       null,
    emitter_user_id  bigint       null,
    id               bigint auto_increment primary key,
    receiver_bank_id bigint       null,
    receiver_user_id bigint       null,
    description      varchar(255) null,
    constraint fk_transaction_emitter_bank_id foreign key (emitter_bank_id) references bank (id),
    constraint fk_transaction_receiver_user_id foreign key (receiver_user_id) references user (user_id),
    constraint fk_transaction_receiver_bank_id foreign key (receiver_bank_id) references bank (id),
    constraint fk_transaction_emitter_user_id foreign key (emitter_user_id) references user (user_id)
);

create or replace table user_role
(
    user_id bigint       not null,
    role    varchar(255) null,
    constraint fk_user_role_user_id foreign key (user_id) references user (user_id)
);

create or replace table verif_token
(
    expiry_date datetime(6)  null,
    id          bigint       not null primary key,
    user_id     bigint       not null,
    token       varchar(255) null,
    constraint uk_verif_token_user_id unique (user_id),
    constraint fk_verif_token_user_id foreign key (user_id) references user (user_id)
);

create or replace table verif_token_seq
(
    next_val bigint null
);
