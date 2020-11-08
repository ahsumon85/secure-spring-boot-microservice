package com.micro.auth.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micro.auth.server.model.User;

import javax.jws.soap.SOAPBinding;
import java.util.Optional;

public interface UserDetailRepository extends JpaRepository<User,Integer> {


    Optional<User> findByUsername(String name);

}
