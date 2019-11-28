package com.everis.springboot.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.everis.springboot.app.models.documents.Course;
import com.everis.springboot.app.models.documents.Student;
import com.everis.springboot.app.models.service.CourseService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/course")
public class CourseController {

	@Autowired
	private CourseService service;

	@GetMapping
	public Flux<Course> findAll() {
		return service.findAllCourses();
	}

	@GetMapping("/{id}")
	public Mono<Course> findById(@PathVariable String id) {
		return service.findByIdCourse(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Course> create(@RequestBody Course course) {

		return service.findAllCourses().filter(c -> c.getIdTeacher().equals(course.getIdTeacher())).count()
				.flatMap(num -> {
					if (num >= 2) {
						return Mono.just(new Course());
					} else {
						return service.saveCourse(course);
					}
				});

	}

	@PutMapping("/{id}")
	public Mono<Course> update(@RequestBody Course course, @PathVariable String id){
		
		return service.findByIdCourse(id).flatMap(cc -> {
									  cc.setName(course.getName());
								      cc.setMax(course.getMax());
								      cc.setMin(course.getMin());
								      cc.setState(course.getState());
								      
								      return service.saveCourse(course);
									});
		
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> delete(@PathVariable String id){
		return service.findByIdCourse(id).flatMap(s->service.deleteCourse(s));
	}
	
	@PutMapping("/addStudent/{idCourse}/{idStudent}")
	public Mono<Course> addStudent(@PathVariable String idCourse, @PathVariable String idStudent) {
		return service.findByIdCourse(idCourse).flatMap(c->{
			if(c.getState().equals("abierto")) {
				List<String> list = c.getIdStudents();
				if(!list.contains(idStudent)) {
					list.add(idStudent);
				}
				c.setIdStudents(list);
				return service.saveCourse(c);
			}else {
				return Mono.just(new Course());
			}
			
		});
	}

	@PutMapping("/addPeople/{idCourse}/{idPerson}")
	public Mono<Course> addPeople(@PathVariable String idCourse, @PathVariable String idPerson) {
		return service.findAllCourses().filter(c->c.getIdPersons().contains(idPerson)).count()
				.flatMap(num->{
					if(num>3) {
						return Mono.just(new Course());
					}else {
						return service.findByIdCourse(idCourse).flatMap(c->{
							if(c.getState().equals("activo")) {
								List<String> list = c.getIdPersons();
								if(!list.contains(idPerson)) {
									list.add(idPerson);
								}
								c.setIdPersons(list);
								return service.saveCourse(c);
							}else {
								return Mono.just(new Course());
							}
							
						});
					}
				});
	}
	
	@GetMapping("/coursesPerson/{idPerson}")
	public Flux<Course> findCoursesByIdPerson(@PathVariable String idPerson){
		return service.findAllCourses().filter(c->c.getIdPersons().contains(idPerson));
	}
	
	@GetMapping("/coursesStudent/{idStudent}")
	public Flux<Course> findCoursesByIdStudent(@PathVariable String idStudent){
		return service.findAllCourses().filter(c->c.getIdStudents().contains(idStudent));
	}
	
	@GetMapping("/coursesTeacher/{idTeacher}")
	public Flux<Course> findCoursesByIdTeacher(@PathVariable String idTeacher){
		return service.findAllCourses().filter(c->c.getIdTeacher().contains(idTeacher));
	}
	
	@GetMapping("/openCourses")
	public Flux<Course> findOpenCourses(){
		return service.findAllCourses().filter(c->c.getState().equals("abierto"));
	}
	
	@GetMapping("/activeCourses")
	public Flux<Course> findActiveCourses(){
		return service.findAllCourses().filter(c->c.getState().equals("activo"));
	}
	
	@GetMapping("/findStudents/{idCourse}")
	public Flux<Student> findStudents(@PathVariable String idCourse){
		return service.findStudents(idCourse);
	}

}
