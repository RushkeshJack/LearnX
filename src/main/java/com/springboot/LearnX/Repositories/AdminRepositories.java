package com.springboot.LearnX.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.springboot.LearnX.Entity.Admin;

public interface AdminRepositories extends CrudRepository<Admin, Integer> {

}
