-- liquibase formatted sql

--changeset dmitriy:1
create table users
(
    id          bigint AUTO_INCREMENT primary key
 );

-- --changeset dmitriy:2
-- insert into users(login, password, email, role, name, surname, patronymic)
-- values ('admin', '$2a$12$Muy41KAVMBHncdk9d3MHfe7oE9PztyWsYBHQYwc.Lyghc/yZsanXS', '', 'admin', '', '', ''),
--        ('moderator', '$2a$12$kingQJfpRkY4YIj1Wf8jt.jhMUO7CSibe9Bcf/Lj6dX069QPYDA4y', '', 'moderator', '', '', '');
--
--


