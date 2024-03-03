package com.thanhtd.zola.dao;

import com.thanhtd.zola.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.deleted = false")
    List<User> findAll();

    @Query("SELECT u FROM User u WHERE u.deleted = false AND u.phone = :phone")
    User findByPhone(@Param("phone") String phone);

    @Query("SELECT u FROM User u WHERE u.deleted = false AND u.userId = :userId")
    User findByUserId(@Param("userId") long userId);
}
