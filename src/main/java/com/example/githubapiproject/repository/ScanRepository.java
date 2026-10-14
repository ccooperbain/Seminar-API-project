package com.example.githubapiproject.repository;

import com.example.githubapiproject.model.Scan ;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScanRepository extends MongoRepository<Scan,String> {

}
