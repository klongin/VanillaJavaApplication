create table report
(
    id             bigserial
        primary key,
    message_id     varchar(100),
    execution_time timestamp,
    root_element   varchar(100)
);

alter table report
    owner to postgres;

