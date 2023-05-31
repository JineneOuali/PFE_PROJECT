package com.example.PFE_PROJECT.dao;




import com.example.PFE_PROJECT.models.DocumentS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDocumentS extends JpaRepository<DocumentS,Long> {
}
