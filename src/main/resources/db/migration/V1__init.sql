CREATE TABLE users (
    user_id bigint NOT NULL AUTO_INCREMENT,
    username varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    type varchar(8),
    first_name varchar(255),
    last_name varchar(255),
    index_number varchar(255),
    personal_number varchar(255),
    gender varchar(10),
    birth_date date,
    email varchar(255),
    address varchar(255),
    actual_semester bigint,
    enrollment_access bit,
    PRIMARY KEY (user_id)
);

CREATE TABLE timetables (
    timetable_id bigint NOT NULL AUTO_INCREMENT,
    user_id bigint NOT NULL,
    name varchar(255) NOT NULL UNIQUE,
    semester bigint,
    quality bigint,
    generation_date date,
    timetable_type varchar(10),
    PRIMARY KEY (timetable_id),
    CONSTRAINT userhasmanytimetables FOREIGN KEY(user_id) REFERENCES users(user_id)
);

CREATE TABLE enrollment_groups (
    enrollment_id bigint NOT NULL AUTO_INCREMENT,
    user_id bigint NOT NULL,
    name varchar(255) NOT NULL UNIQUE,
    ects_sum bigint,
    semester bigint,
    PRIMARY KEY (enrollment_id),
    CONSTRAINT userhasmanyenrollments FOREIGN KEY(user_id) REFERENCES users(user_id)
);

CREATE TABLE classes (
    class_id bigint NOT NULL AUTO_INCREMENT,
    enrollment_id bigint NOT NULL,
    name varchar(255) NOT NULL UNIQUE,
    ects bigint,
    PRIMARY KEY (class_id),
    CONSTRAINT enrolmmenthasmanyclasses FOREIGN KEY(enrollment_id) REFERENCES enrollment_groups(enrollment_id)
);

CREATE TABLE courses (
    course_id bigint NOT NULL AUTO_INCREMENT,
    class_id bigint NOT NULL,
    timetable_id bigint NOT NULL,
    group_code varchar(50),
    courses_date timestamp,
    courses_place varchar(255),
    lecturer varchar(255),
    even_week bit,
    free_places bigint,
    PRIMARY KEY (course_id),
    CONSTRAINT classhasmanycourses FOREIGN KEY(class_id) REFERENCES classes(class_id),
    CONSTRAINT timetableshasmanycourses FOREIGN KEY(timetable_id) REFERENCES timetables(timetable_id)
);