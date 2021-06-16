-- auto-generated definition
create table doctor
(
    id             bigint auto_increment
        primary key,
    description    varchar(255) null,
    email          varchar(255) null,
    img            varchar(255) null,
    name           varchar(255) null,
    phone          varchar(255) null,
    specialization varchar(255) null
);

-- auto-generated definition
create table patient
(
    id         bigint auto_increment
        primary key,
    blood_type varchar(255) null,
    email      varchar(255) null,
    name       varchar(255) null,
    phone      varchar(255) null
);

-- auto-generated definition
create table service_type
(
    id   bigint auto_increment
        primary key,
    type varchar(255) null,
    constraint UK_bwo6rnv35pi6on9run3evunlv
        unique (type)
);

-- auto-generated definition
create table status
(
    id   bigint auto_increment
        primary key,
    name varchar(255) null
);
-- auto-generated definition
create table appointment
(
    id              bigint auto_increment
        primary key,
    date            datetime     null,
    medical_history varchar(255) null,
    prescription    varchar(255) null,
    status          varchar(255) null,
    visit_code      varchar(255) null,
    doctor_id       bigint       null,
    patient_id      bigint       null,
    service_type    varchar(255) null,
    constraint FK4apif2ewfyf14077ichee8g06
        foreign key (patient_id) references patient (id),
    constraint FK7bdq9q0hepelcvm0cbig1sid
        foreign key (service_type) references service_type (type),
    constraint FKoeb98n82eph1dx43v3y2bcmsl
        foreign key (doctor_id) references doctor (id)
);

-- auto-generated definition
create table archive_appointment
(
    id              bigint auto_increment
        primary key,
    date            datetime     null,
    medical_history varchar(255) null,
    prescription    varchar(255) null,
    status          varchar(255) null,
    visit_code      varchar(255) null,
    doctor_id       bigint       null,
    patient_id      bigint       null,
    service_type    varchar(255) null,
    constraint FKg843ejbnb3xtxlcxed3uu6fwr
        foreign key (doctor_id) references doctor (id),
    constraint FKrk0c8i138akd2sv6867umu8dd
        foreign key (patient_id) references patient (id),
    constraint FKrl0jstrrm8dawvafirkagpkma
        foreign key (service_type) references service_type (type)
);

INSERT INTO edentist.doctor (description, email, img, name, phone, specialization)
VALUES ('decr', 'doc@mail.bg','../../assets/images/team/t3.jpg', 'Peter Stoyanov', '0895123123', 'Surgery');

INSERT INTO edentist.service_type (type)
VALUES ('Orthodontic'),
       ('General Dentistry'),
       ('Surgery');

INSERT INTO edentist.status (name)
VALUES ('Approved'),
       ('Declined'),
       ('Pending');
