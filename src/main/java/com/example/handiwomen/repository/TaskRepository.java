package com.example.handiwomen.repository;

import com.example.handiwomen.models.DailyTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<DailyTask,String> {
}
