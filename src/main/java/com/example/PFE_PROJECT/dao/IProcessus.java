package com.example.PFE_PROJECT.dao;



import com.example.PFE_PROJECT.models.Processus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProcessus extends JpaRepository<Processus,Long> {
}
