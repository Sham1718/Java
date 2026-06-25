package com.TaskApi.repository;

import com.TaskApi.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    Optional<Task> findByTitleIgnoreCase(String title);
}
