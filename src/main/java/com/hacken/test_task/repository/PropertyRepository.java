package com.hacken.test_task.repository;

import com.hacken.test_task.domain.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("select p from Property as p where p.key = ?1")
    Property getPropertyByKey(String key);
}
