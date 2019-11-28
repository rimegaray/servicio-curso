package com.everis.springboot.app.models.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.everis.springboot.app.models.dao.CourseDao;
import com.everis.springboot.app.models.documents.Course;
import com.everis.springboot.app.models.documents.Person;
import com.everis.springboot.app.models.documents.Student;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CourseServiceImpl implements CourseService{

		
	@Autowired
	private WebClient client;
	
	@Autowired
	private CourseDao courseDao;
	
	
	
	@Override
	public Flux<Course> findAllCourses() {
		return courseDao.findAll();
	}

	@Override
	public Mono<Course> findByIdCourse(String id) {
		return courseDao.findById(id);
	}

	@Override
	public Mono<Void> deleteCourse(Course teacher) {
		return courseDao.delete(teacher);
	}

	@Override
	public Mono<Course> saveCourse(Course teacher) {
		return courseDao.save(teacher);
	}
	
	@Override
	public Mono<Course> updateCourse(Course course, String id) {
		return courseDao.findById(id).flatMap(s -> {
			s.setName(course.getName());
			s.setMax(course.getMax());
			s.setMin(course.getMin());
			s.setState(course.getState());
			s.setIdTeacher(course.getIdTeacher());
			s.setIdStudents(s.getIdStudents());
			s.setIdPersons(course.getIdPersons());
			return courseDao.save(s);
		});
	}

	@Override
	public Mono<Course> findCourseByIdTeacher(String idTeacher) {
		return courseDao.findAll().filter(s->s.getIdTeacher().equals(idTeacher)).next();
	}

	@Override
	public Flux<Student> findStudents(String idCourse) {
		return courseDao.findById(idCourse).flux()
				.flatMap(c->{
						return Flux.fromIterable(c.getIdStudents()).flatMap(id->{
							return client.get().uri("/{id}",id)
							.accept(MediaType.APPLICATION_JSON_UTF8)
							.retrieve()
							.bodyToMono(Student.class);
						});
				});
	}

	

}
