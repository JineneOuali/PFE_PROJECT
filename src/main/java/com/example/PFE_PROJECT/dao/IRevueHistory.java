package com.example.PFE_PROJECT.dao;



import com.example.PFE_PROJECT.models.RevueHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRevueHistory extends JpaRepository<RevueHistory,Long> {
}
