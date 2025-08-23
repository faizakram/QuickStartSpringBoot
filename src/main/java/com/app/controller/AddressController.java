package com.app.controller;

import com.app.model.Address;
import com.app.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;


    // CRUD Operations

    @PostMapping
    public Address createAddress(@RequestBody Address address) {
        return addressService.createAddress(address);
    }

    @GetMapping
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/{id}")
    public Optional<Address> getAddressById(@PathVariable Long id) {
        return addressService.getAddressById(id);
    }

    @PutMapping("/{id}")
    public Address updateAddress(@PathVariable Long id, @RequestBody Address addressDetails) {
        return addressService.updateAddress(id, addressDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
    }

    // FindBy Operations

    @GetMapping("/by-city/{city}")
    public List<Address> getAddressesByCity(@PathVariable String city) {
        return addressService.findByCity(city);
    }

    @GetMapping("/by-state/{state}")
    public List<Address> getAddressesByState(@PathVariable String state) {
        return addressService.findByState(state);
    }

    @GetMapping("/by-employee-name/{employeeName}")
    public List<Address> getAddressesByEmployeeName(@PathVariable String employeeName) {
        return addressService.findByEmployeeName(employeeName);
    }
}

