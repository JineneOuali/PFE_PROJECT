package com.example.PFE_PROJECT.dao;



import com.example.PFE_PROJECT.models.Activite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IActivite extends JpaRepository<Activite,Long> {
}
