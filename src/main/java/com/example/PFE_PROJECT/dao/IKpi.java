package com.example.PFE_PROJECT.dao;



import com.example.PFE_PROJECT.models.Kpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IKpi extends JpaRepository<Kpi,Long> {
}
