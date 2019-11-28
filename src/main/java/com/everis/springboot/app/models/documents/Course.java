package com.everis.springboot.app.models.documents;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "curso")
public class Course {

	@Id
	private String id;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String idTeacher;
	
	private List<String> idStudents = new ArrayList<>();
	
	private List<String> idPersons = new ArrayList<>();
	
	@NotNull
	private Integer max;
	
	@NotNull
	private Integer min;
	
	@NotNull
	private String state; //cerrado, abierto, activo, completado

	public String getIdTeacher() {
		return idTeacher;
	}

	public void setIdTeacher(String idTeacher) {
		this.idTeacher = idTeacher;
	}

	public List<String> getIdStudents() {
		return idStudents;
	}

	public void setIdStudents(List<String> idStudents) {
		this.idStudents = idStudents;
	}

	public List<String> getIdPersons() {
		return idPersons;
	}

	public void setIdPersons(List<String> idPersons) {
		this.idPersons = idPersons;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	
}
