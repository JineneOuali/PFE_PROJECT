package com.example.PFE_PROJECT.dao;




import com.example.PFE_PROJECT.models.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDirection extends JpaRepository<Direction,Long> {
}
