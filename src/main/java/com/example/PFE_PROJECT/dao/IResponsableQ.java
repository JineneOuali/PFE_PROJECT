package com.example.PFE_PROJECT.dao;



import com.example.PFE_PROJECT.models.ResponsableQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IResponsableQ extends JpaRepository<ResponsableQ,Long> {
}
