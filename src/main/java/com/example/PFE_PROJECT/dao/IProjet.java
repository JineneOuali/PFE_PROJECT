package com.example.PFE_PROJECT.dao;



import com.example.PFE_PROJECT.models.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IProjet extends JpaRepository<Projet,Long> {

}
