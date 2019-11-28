package com.everis.springboot.app.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.everis.springboot.app.models.documents.Course;

public interface CourseDao extends ReactiveMongoRepository<Course, String>{

}
