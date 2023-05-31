package com.example.PFE_PROJECT.dao;




import com.example.PFE_PROJECT.models.DocumentE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDocumentE extends JpaRepository<DocumentE,Long> {
}
