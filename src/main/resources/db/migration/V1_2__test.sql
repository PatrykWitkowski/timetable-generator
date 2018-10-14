insert into timetables(timetable_id, user_id, name, semester, semester_start_date, semester_end_date, quality, generation_date, timetable_type)
values (1, 1, 'test_timetable', 1, '2018-10-01', '2019-01-30', 1, NOW(), 'INVALID');

insert into enrollment_groups(enrollment_id, user_id, name, ects_sum, semester)
values (1, 1, 'zapisowa_1', 30, 1);

insert into classes(class_id, enrollment_id, name, ects)
values (1, 1, 'analiza', 12);

insert into courses(course_id, class_id, timetable_id, group_code, course_start_time, course_end_time, course_day, courses_place, lecturer, even_week, free_places)
values (1, 1, 1, 'CODE_1', '08:20:00', '10:00:00', 'MONDAY', 'w dupie', 'debil', 0, 1);
