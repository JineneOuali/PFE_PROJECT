package com.example.PFE_PROJECT.dao;



import com.example.PFE_PROJECT.models.LiberationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILiberationHistory extends JpaRepository<LiberationHistory,Long> {
}
