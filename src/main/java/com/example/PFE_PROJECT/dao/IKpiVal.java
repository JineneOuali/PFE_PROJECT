package com.example.PFE_PROJECT.dao;



import com.example.PFE_PROJECT.models.KpiVal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface IKpiVal extends JpaRepository<KpiVal,Long> {
    Collection<KpiVal> findByKpiIdAndProjetId(Long kpiId, Long projetId);
}
