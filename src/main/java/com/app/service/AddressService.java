package com.app.service;

import com.app.model.Address;
import com.app.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    // CRUD Operations

    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }

    public Address updateAddress(Long id, Address addressDetails) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));

        address.setStreet(addressDetails.getStreet());
        address.setCity(addressDetails.getCity());
        address.setState(addressDetails.getState());
        address.setEmployee(addressDetails.getEmployee());

        return addressRepository.save(address);
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }

    // FindBy Operations

    public List<Address> findByCity(String city) {
        return addressRepository.findByCity(city);
    }

    public List<Address> findByState(String state) {
        return addressRepository.findByState(state);
    }

    public List<Address> findByEmployeeName(String employeeName) {
        return addressRepository.findByEmployee_Name(employeeName);
    }
}
