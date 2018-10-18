insert into timetables(timetable_id, user_id, name, semester, semester_start_date, semester_end_date, quality, generation_date, timetable_type)
values (1, 1, 'test_timetable', 7, '2018-10-01', '2019-01-30', 1, NOW(), 'INVALID');

insert into enrollment_groups(enrollment_id, user_id, name, ects_sum, semester)
values (1, 1, 'zapisowa_1', 30, 1);

insert into classes(class_id, enrollment_id, name, ects, class_type)
values (1, 1, 'Analiza matematyczna', 12, 'LECTURE');
insert into classes(class_id, enrollment_id, name, ects, class_type)
values (2, 1, 'Analiza matematyczna', 4, 'EXERCISE');

insert into lecturers (lecturer_id, name)
values (1, 'Prof. Janusz Zakrzewski');

insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, even_week, free_places)
values (1, 1, 1, 'EFG324F', '08:20:00', '10:00:00', 'MONDAY', 'C-14 A1.1', null, 1);
insert into courses(course_id, class_id, lecturer_id, group_code, course_start_time, course_end_time, course_day, courses_place, even_week, free_places)
values (2, 2, 1, 'EFG324F', '10:00:00', '13:00:00', 'MONDAY', 'C-14 A1.1', 1, 1);

insert into timetable_course (timetable_id, course_id)
values (1, 1);
insert into timetable_course (timetable_id, course_id)
values (1, 2);