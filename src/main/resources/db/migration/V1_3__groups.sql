insert into enrollment_groups(enrollment_id, user_id, name, ects_sum, semester)
values (2, 1, 'zapisy_test', 30, 4);

insert into classes(class_id, user_id, name, ects, class_type)
values (4, 1, 'Analiza matematyczna', 4, 'LECTURE');
insert into classes(class_id, user_id, name, ects, class_type)
values (5, 1, 'Analiza matematyczna', 4, 'EXERCISE');
insert into classes(class_id, user_id, name, ects, class_type)
values (6, 1, 'Programowanie obiektowe', 3, 'LECTURE');
insert into classes(class_id, user_id, name, ects, class_type)
values (7, 1, 'Programowanie obiektowe', 3, 'LABORATORY');
insert into classes(class_id, user_id, name, ects, class_type)
values (8, 1, 'Bazy danych', 2, 'LECTURE');
insert into classes(class_id, user_id, name, ects, class_type)
values (9, 1, 'Bazy danych', 2, 'LABORATORY');
insert into classes(class_id, user_id, name, ects, class_type)
values (10, 1, 'Bazy danych', 2, 'PROJECT');
insert into classes(class_id, user_id, name, ects, class_type)
values (11, 1, 'Algebra', 2, 'EXERCISE');
insert into classes(class_id, user_id, name, ects, class_type)
values (12, 1, 'Sztuczna inteligencja', 4, 'LECTURE');
insert into classes(class_id, user_id, name, ects, class_type)
values (13, 1, 'Sztuczna inteligencja', 4, 'SEMINAR');

insert into enrollment_class(enrollment_id, class_id)
values  (2, 4);
insert into enrollment_class(enrollment_id, class_id)
values  (2, 5);
insert into enrollment_class(enrollment_id, class_id)
values  (2, 6);
insert into enrollment_class(enrollment_id, class_id)
values  (2, 7);
insert into enrollment_class(enrollment_id, class_id)
values  (2, 8);
insert into enrollment_class(enrollment_id, class_id)
values  (2, 9);
insert into enrollment_class(enrollment_id, class_id)
values  (2, 10);
insert into enrollment_class(enrollment_id, class_id)
values  (2, 11);
insert into enrollment_class(enrollment_id, class_id)
values  (2, 12);
insert into enrollment_class(enrollment_id, class_id)
values  (2, 13);

--Analiza lecture
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (5, 4, 1, 'ANALIZA01', '09:00:00', '11:00:00', 'MONDAY', 'C-14 A1.1', 'WEEKLY', 100);

--Analiza exercise
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (6, 5, 1, 'ANALIZA02', '11:00:00', '13:00:00', 'MONDAY', 'C-14 A1.1', 'ODD', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (7, 5, 1, 'ANALIZA03', '11:00:00', '13:00:00', 'MONDAY', 'C-14 A1.1', 'EVEN', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (8, 5, 1, 'ANALIZA04', '13:00:00', '15:00:00', 'MONDAY', 'C-14 A1.1', 'ODD', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (9, 5, 2, 'ANALIZA05', '11:00:00', '13:00:00', 'WEDNESDAY', 'C-14 A1.1', 'EVEN', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (10, 5, 2, 'ANALIZA06', '11:00:00', '13:00:00', 'WEDNESDAY', 'C-14 A1.1', 'ODD', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (11, 5, 2, 'ANALIZA07', '13:00:00', '15:00:00', 'WEDNESDAY', 'C-14 A1.1', 'EVEN', 20);

--Programowanie lecture
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (12, 6, 3, 'PROG01', '15:00:00', '17:00:00', 'THURSDAY', 'C-14 A1.1', 'WEEKLY', 100);

--Programowanie exercise
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (13, 7, 3, 'PROG02', '13:00:00', '15:00:00', 'THURSDAY', 'C-14 A1.1', 'EVEN', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (14, 7, 3, 'PROG03', '13:00:00', '15:00:00', 'THURSDAY', 'C-14 A1.1', 'ODD', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (15, 7, 3, 'PROG04', '11:00:00', '13:00:00', 'THURSDAY', 'C-14 A1.1', 'EVEN', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (16, 7, 4, 'PROG05', '09:00:00', '11:00:00', 'WEDNESDAY', 'C-14 A1.1', 'ODD', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (17, 7, 4, 'PROG06', '09:00:00', '11:00:00', 'WEDNESDAY', 'C-14 A1.1', 'EVEN', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (18, 7, 4, 'PROG07', '11:00:00', '13:00:00', 'WEDNESDAY', 'C-14 A1.1', 'ODD', 20);

--Bazy danych lecture
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (19, 8, 5, 'BAZY01', '11:00:00', '13:00:00', 'TUESDAY', 'C-14 A1.1', 'WEEKLY', 100);

--Bazy danych exercise
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (20, 9, 5, 'BAZY02', '15:00:00', '17:00:00', 'TUESDAY', 'C-14 A1.1', 'EVEN', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (21, 9, 5, 'BAZY03', '15:00:00', '17:00:00', 'TUESDAY', 'C-14 A1.1', 'ODD', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (22, 9, 5, 'BAZY04', '17:00:00', '19:00:00', 'TUESDAY', 'C-14 A1.1', 'EVEN', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (23, 9, 5, 'BAZY05', '17:00:00', '19:00:00', 'FRIDAY', 'C-14 A1.1', 'ODD', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (24, 9, 5, 'BAZY06', '17:00:00', '19:00:00', 'FRIDAY', 'C-14 A1.1', 'EVEN', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (25, 9, 5, 'BAZY07', '19:00:00', '21:00:00', 'FRIDAY', 'C-14 A1.1', 'ODD', 20);

--Bazy danych project
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (26, 10, 5, 'BAZY08', '17:00:00', '19:00:00', 'TUESDAY', 'C-14 A1.1', 'WEEKLY', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (27, 10, 5, 'BAZY09', '19:00:00', '21:00:00', 'TUESDAY', 'C-14 A1.1', 'WEEKLY', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (28, 10, 6, 'BAZY10', '15:00:00', '17:00:00', 'FRIDAY', 'C-14 A1.1', 'WEEKLY', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (29, 10, 6, 'BAZY11', '13:00:00', '15:00:00', 'FRIDAY', 'C-14 A1.1', 'WEEKLY', 20);

--Algebra exercise
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (30, 11, 7, 'ALGB01', '13:00:00', '15:00:00', 'MONDAY', 'C-14 A1.1', 'EVEN', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (31, 11, 7, 'ALGB02', '13:00:00', '15:00:00', 'MONDAY', 'C-14 A1.1', 'ODD', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (32, 11, 7, 'ALGB03', '15:00:00', '17:00:00', 'MONDAY', 'C-14 A1.1', 'EVEN', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (33, 11, 8, 'ALGB04', '07:30:00', '09:00:00', 'TUESDAY', 'C-14 A1.1', 'ODD', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (34, 11, 8, 'ALGB05', '07:30:00', '09:00:00', 'TUESDAY', 'C-14 A1.1', 'EVEN', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (35, 11, 8, 'ALGB06', '09:00:00', '11:00:00', 'TUESDAY', 'C-14 A1.1', 'ODD', 20);

--Sztuczna inteligencje lecture
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (36, 12, 9, 'SZINT01', '11:00:00', '13:00:00', 'WEDNESDAY', 'C-14 A1.1', 'WEEKLY', 100);

--Sztuczna inteligencje seminar
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (37, 13, 9, 'SZINT02', '15:00:00', '17:00:00', 'THURSDAY', 'C-14 A1.1', 'EVEN', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (38, 13, 9, 'SZINT03', '15:00:00', '17:00:00', 'THURSDAY', 'C-14 A1.1', 'ODD', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (39, 13, 9, 'SZINT04', '17:00:00', '19:00:00', 'THURSDAY', 'C-14 A1.1', 'EVEN', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (40, 13, 10, 'SZINT05', '11:00:00', '13:00:00', 'FRIDAY', 'C-14 A1.1', 'ODD', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (41, 13, 10, 'SZINT06', '11:00:00', '13:00:00', 'FRIDAY', 'C-14 A1.1', 'EVEN', 20);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (42, 13, 10, 'SZINT07', '13:00:00', '15:00:00', 'FRIDAY', 'C-14 A1.1', 'ODD', 20);
