package com.example.PFE_PROJECT.dao;



import com.example.PFE_PROJECT.models.ResponsableP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IResponsableP extends JpaRepository<ResponsableP,Long> {
}
