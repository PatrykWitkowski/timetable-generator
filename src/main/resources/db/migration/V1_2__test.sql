insert into timetables(timetable_id, user_id, name, semester, semester_start_date, semester_end_date, quality, generation_date, timetable_type)
values (1, 1, 'test_timetable', 7, '2018-10-01', '2019-01-30', 92.3, NOW(), 'EXCELLENT');
insert into timetables(timetable_id, user_id, name, semester, semester_start_date, semester_end_date, quality, generation_date, timetable_type)
values (2, 1, 'test_timetable_2', 7, '2018-10-01', '2019-01-30', 5, NOW(), 'INVALID');

insert into enrollment_groups(enrollment_id, user_id, name, ects_sum, semester)
values (1, 1, 'zapisowa_1', 20, 2);

insert into classes(class_id, user_id, name, ects, class_type)
values (1, 1, 'Analiza matematyczna', 12, 'LECTURE');
insert into classes(class_id, user_id, name, ects, class_type)
values (2, 1, 'Analiza matematyczna', 4, 'EXERCISE');
insert into classes(class_id, user_id, name, ects, class_type)
values (3, 1, 'Programowanie obiektowe', 4, 'LABORATORY');

insert into enrollment_class(enrollment_id, class_id)
values  (1, 1);
insert into enrollment_class(enrollment_id, class_id)
values  (1, 2);
insert into enrollment_class(enrollment_id, class_id)
values  (1, 3);

insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (1, 1, 1, 'EFG324F', '08:20:00', '10:00:00', 'MONDAY', 'C-14 A1.1', 'WEEKLY', 1);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (2, 2, 1, 'ABC3424', '10:00:00', '13:00:00', 'MONDAY', 'C-14 A1.1', 'ODD', 1);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (3, 2, 1, 'GFD435D', '13:00:00', '15:00:00', 'FRIDAY', 'C-14 A2.1', 'EVEN', 1);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, parity_of_the_week, free_places)
values (4, 3, 1, 'RF33411', '7:30:00', '9:00:00', 'THURSDAY', 'C-14 A2.1', 'WEEKLY', 1);

insert into timetable_course (timetable_id, course_id)
values (1, 1);
insert into timetable_course (timetable_id, course_id)
values (1, 2);
insert into timetable_course (timetable_id, course_id)
values (2, 4);