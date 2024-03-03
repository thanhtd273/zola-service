package com.thanhtd.zola.dao;

import com.thanhtd.zola.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role, Long> {
    @Query("SELECT u FROM Role u WHERE u.deleted = false AND u.name = :name")
    Role findByName(@Param("name") String name);

    @Query("SELECT u FROM Role u WHERE u.deleted = false AND u.roleId = :roleId")
    Role findByRoleId(@Param("roleId") Long roleId);
}
