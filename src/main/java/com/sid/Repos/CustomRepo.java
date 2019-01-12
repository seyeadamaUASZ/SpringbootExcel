package com.sid.Repos;

import com.sid.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomRepo extends JpaRepository<Customer,Long> {
}
