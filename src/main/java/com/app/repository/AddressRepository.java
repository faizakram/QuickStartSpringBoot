package com.app.repository;

import com.app.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    // Find addresses by city
    List<Address> findByCity(String city);

    // Find addresses by state
    List<Address> findByState(String state);

    // Find addresses by employee name
    List<Address> findByEmployee_Name(String employeeName);
}
