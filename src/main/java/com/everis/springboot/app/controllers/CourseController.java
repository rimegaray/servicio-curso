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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/course")
@Api(value = "course", description = "API para curso", produces = "application/json")
public class CourseController {

	@Autowired
	private CourseService service;

	 @ApiOperation(value = "Listar todos los cursos", 
		      notes = "Retorna todo sobre todas los cursos")
	 @ApiResponses({
	      @ApiResponse(code = 200, message = "Encuentra por lo menos un curso")
	    })
	@GetMapping
	public Flux<Course> findAll() {
		return service.findAllCourses();
	}
	 
	@ApiOperation(value = "Busca curso por id", 
		      notes = "Retorna todo sobre un curso")
	@ApiResponses({
	      @ApiResponse(code = 200, message = "Encuentra por lo menos un curso")
	    })
	@GetMapping("/{id}")
	public Mono<Course> findById(@PathVariable String id) {
		return service.findByIdCourse(id);
	}

	@ApiOperation(value = "Crear un nuevo curso", 
		      notes = "Retorna el curso creado")
	@ApiResponses({
	      @ApiResponse(code = 200, message = "Curso creado")
	    })
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

	@ApiOperation(value = "Actualizar un curso", 
		      notes = "Retorna el curso actualizado")
	@ApiResponses({
	      @ApiResponse(code = 200, message = "Curso actualizado")
	    })
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

	@ApiOperation(value = "Eliminar un curso", 
		      notes = "Retorna void")
	@ApiResponses({
	      @ApiResponse(code = 200, message = "Se elimina el curso")
	    })
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> delete(@PathVariable String id){
		return service.findByIdCourse(id).flatMap(s->service.deleteCourse(s));
	}
	
	@ApiOperation(value = "Agregar un estudiante a un curso abierto", 
		      notes = "Retorna todo sobre el curso")
	@ApiResponses({
	      @ApiResponse(code = 200, message = "se agrego al estudiante")
	    })
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

	@ApiOperation(value = "Agregar familiares a un nuevo curso", 
		      notes = "Retorna todo sobre el curso")
	@ApiResponses({
	      @ApiResponse(code = 200, message = "se agrego al familiar ")
	    })
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
	
	@ApiOperation(value = "Listar todos los cursos que lleva un familiar", 
		      notes = "Retorna todo sobre todas los cursos")
	@ApiResponses({
	      @ApiResponse(code = 200, message = "Encuentra por lo menos un curso")
	    })
	@GetMapping("/coursesPerson/{idPerson}")
	public Flux<Course> findCoursesByIdPerson(@PathVariable String idPerson){
		return service.findAllCourses().filter(c->c.getIdPersons().contains(idPerson));
	}
	
	@ApiOperation(value = "Listar todos los cursos que llva un estudiante", 
		      notes = "Retorna todo sobre todas los cursos")
	@ApiResponses({
	      @ApiResponse(code = 200, message = "Encuentra por lo menos un curso")
	    })
	@GetMapping("/coursesStudent/{idStudent}")
	public Flux<Course> findCoursesByIdStudent(@PathVariable String idStudent){
		return service.findAllCourses().filter(c->c.getIdStudents().contains(idStudent));
	}
	
	@ApiOperation(value = "Listar todos los cursos que dicta un profesor", 
		      notes = "Retorna todo sobre todas los cursos")
	@ApiResponses({
	      @ApiResponse(code = 200, message = "Encuentra por lo menos un curso")
	    })
	@GetMapping("/coursesTeacher/{idTeacher}")
	public Flux<Course> findCoursesByIdTeacher(@PathVariable String idTeacher){
		return service.findAllCourses().filter(c->c.getIdTeacher().contains(idTeacher));
	}
	
	@ApiOperation(value = "Listar todos los cursos abiertos", 
		      notes = "Retorna todo sobre todas los cursos")
	@ApiResponses({
	      @ApiResponse(code = 200, message = "Encuentra por lo menos un curso")
	    })
	@GetMapping("/openCourses")
	public Flux<Course> findOpenCourses(){
		return service.findAllCourses().filter(c->c.getState().equals("abierto"));
	}
	
	@ApiOperation(value = "Listar todos los cursos activos", 
		      notes = "Retorna todo sobre todas los cursos")
	@ApiResponses({
	      @ApiResponse(code = 200, message = "Encuentra por lo menos un curso")
	    })
	@GetMapping("/activeCourses")
	public Flux<Course> findActiveCourses(){
		return service.findAllCourses().filter(c->c.getState().equals("activo"));
	}
	
	@ApiOperation(value = "Listar todos los estudiante de un curso", 
		      notes = "Retorna todo sobre todas los cursos")
	@ApiResponses({
	      @ApiResponse(code = 200, message = "Encuentra por lo menos un curso")
	    })
	@GetMapping("/findStudents/{idCourse}")
	public Flux<Student> findStudents(@PathVariable String idCourse){
		return service.findStudents(idCourse);
	}

}
