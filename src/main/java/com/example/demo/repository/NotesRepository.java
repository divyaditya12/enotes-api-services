package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Notes;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Integer> {

    List<Notes> findByCreatedByAndIsDeletedTrue(Integer id);

}
