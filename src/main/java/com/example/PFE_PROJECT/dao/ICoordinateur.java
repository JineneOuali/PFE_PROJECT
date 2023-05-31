package com.example.PFE_PROJECT.dao;



import com.example.PFE_PROJECT.models.Coordinateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICoordinateur extends JpaRepository<Coordinateur,Long> {
    @Query("SELECT d.titre AS direction, COUNT(c) AS nbCoordinators FROM Coordinateur c JOIN c.direction d GROUP BY d.titre")
    List<Object[]> countCoordinatorsByDirection();
}
