package com.example.PFE_PROJECT.dao;




import com.example.PFE_PROJECT.models.ProjetKpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProjetKpi extends JpaRepository<ProjetKpi,Long> {
}
