package com.example.PFE_PROJECT.dao;



import com.example.PFE_PROJECT.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdmin extends JpaRepository<Admin,Long> {
}
