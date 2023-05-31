package com.example.PFE_PROJECT.dao;




import com.example.PFE_PROJECT.models.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface IAudit extends JpaRepository<Audit,Long> {
    Collection<Audit> findByProjetId(Long projectId);
}
