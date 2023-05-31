package com.example.PFE_PROJECT.dao;



import com.example.PFE_PROJECT.models.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAlert extends JpaRepository<Alert,Long> {
}
