package com.everis.springboot.app.models.service;

import com.everis.springboot.app.models.documents.Course;
import com.everis.springboot.app.models.documents.Person;
import com.everis.springboot.app.models.documents.Student;
import com.everis.springboot.app.models.documents.Teacher;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CourseService {

	public Mono<Course> findCourseByIdTeacher(String idTeacher);
	
	public Flux<Course> findAllCourses();

	public Mono<Course> findByIdCourse(String id);

	public Mono<Void> deleteCourse(Course course);

	public Mono<Course> saveCourse(Course course);
	
	public Mono<Course> updateCourse(Course course, String id);
	
	public Flux<Student> findStudents(String id);
}
