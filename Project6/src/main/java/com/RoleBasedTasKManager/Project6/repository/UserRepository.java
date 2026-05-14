package com.RoleBasedTasKManager.Project6.repository;

import com.RoleBasedTasKManager.Project6.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User,Long>
{
    public User findByEmail(String email);
}
