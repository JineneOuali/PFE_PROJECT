package com.example.PFE_PROJECT.dao;



import com.example.PFE_PROJECT.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUser  extends JpaRepository<User,Long> {

    /*@Query("{'username':?0}")*/
    @Query("select u from  User  u where u.username= :username")
    User findByUsername(@Param("username") String username);

    @Query("select u from  User  u where u.email= :email")
    User findUserByEmail(@Param("email") String email);

    @Query("select u from  User  u where u.passwordResetToken= :passwordResetToken")
    User findByPasswordResetToken(@Param("passwordResetToken") String passwordResetToken);

}
