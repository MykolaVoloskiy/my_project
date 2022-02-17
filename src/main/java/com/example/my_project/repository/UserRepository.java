package com.example.my_project.repository;

import com.example.my_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query("select u from User u where u.firstName =:q and u.age in (:ids)")
    User findSomething(@Param("q") String a, @Param("ids") List<Integer> b);

    @Query(value = "select * from users where password=:q", nativeQuery = true)
    User findByNativeQuery(@Param("q") String a);


    boolean existsByEmail(String email);

}
