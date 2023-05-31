package com.example.PFE_PROJECT.dao;



import com.example.PFE_PROJECT.models.Revue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRevue extends JpaRepository<Revue,Long> {
}
