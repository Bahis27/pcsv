package com.testtask.csvp.repositories;

import com.testtask.csvp.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public List<User> findAllByMsisdnIn(Set<String> msisdns);
}

