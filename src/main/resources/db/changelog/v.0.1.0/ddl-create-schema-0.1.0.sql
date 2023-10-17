--liquibase formatted sql
--changeset elina:v.0.1.0

    CREATE SCHEMA IF NOT EXISTS notification_bot;

--rollback drop schema notification_bot cascade;

