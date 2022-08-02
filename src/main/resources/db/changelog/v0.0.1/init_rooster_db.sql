CREATE SEQUENCE rooster_list_seq START WITH 1 INCREMENT 1;

CREATE TABLE rooster_list
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('rooster_list_seq'),
    login       VARCHAR,
    fullname    VARCHAR,
    month_count BIGINT NOT NULL DEFAULT 0
);