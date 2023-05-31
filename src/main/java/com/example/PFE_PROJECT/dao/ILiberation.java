package com.example.PFE_PROJECT.dao;



import com.example.PFE_PROJECT.models.Liberation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILiberation extends JpaRepository<Liberation,Long> {
}
