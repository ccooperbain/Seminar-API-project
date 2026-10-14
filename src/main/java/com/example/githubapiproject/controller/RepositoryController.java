package com.example.githubapiproject.controller;

import com.example.githubapiproject.model.Repository;
import com.example.githubapiproject.service.RepositoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repositories")
public class RepositoryController {

    private final RepositoryService repositoryService;
    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping
    public List<Repository> getAllRepositories(){
        return repositoryService.getAllRepositories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Repository> getRepositoryById(@PathVariable String id){
        return repositoryService.getRepositoriesById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Repository createRepository(@RequestBody Repository repository){
        return repositoryService.saveRepository(repository);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepository(@PathVariable String id){
        repositoryService.deleteRepository(id);
        return ResponseEntity.noContent().build();
    }


}
