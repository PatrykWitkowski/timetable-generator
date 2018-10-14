insert into timetables(timetable_id, user_id, name, semester, quality, generation_date, timetable_type)
values (1, 1, 'test_timetable', 1, 1, NOW(), 'VALID');

insert into enrollment_groups(enrollment_id, user_id, name, ects_sum, semester)
values (1, 1, 'zapisowa_1', 30, 1);

insert into classes(class_id, enrollment_id, name, ects)
values (1, 1, 'analiza', 12);

insert into courses(course_id, class_id, timetable_id, group_code, courses_date, courses_place, lecturer, even_week, free_places)
values (1, 1, 1, 'CODE_1', '2018-10-14 08:20:11', 'w dupie', 'debil', 0, 1);



