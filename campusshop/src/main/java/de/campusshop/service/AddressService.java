package de.campusshop.service;

import de.campusshop.dto.AddressDto;
import de.campusshop.dto.CreateAddressRequest;
import de.campusshop.model.Address;
import de.campusshop.model.AppUser;
import de.campusshop.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional
    public AddressDto createAddress(CreateAddressRequest request, AppUser currentUser) {

        Address address = new Address();
        address.setUser(currentUser);
        address.setFullName(request.getFullName());
        address.setStreet(request.getStreet());
        address.setZip(request.getZip());
        address.setCity(request.getCity());
        address.setCountry(request.getCountry().toUpperCase());
        address.setDefaultAddress(request.isDefaultAddress());

        // Wenn neue Adresse "default" ist, dann müssen alle anderen default=false werden
        if (request.isDefaultAddress()) {
            unsetDefaultForAll(currentUser);
        }

        Address saved = addressRepository.save(address);
        return toDto(saved);
    }

    public List<AddressDto> getMyAddresses(AppUser currentUser) {
        return addressRepository.findByUserOrderByIsDefaultDescIdAsc(currentUser)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public void deleteAddress(Long addressId, AppUser currentUser) {
        Address address = addressRepository.findByIdAndUser(addressId, currentUser)
                .orElseThrow(() -> new IllegalArgumentException("Adresse existiert nicht oder gehört nicht zu diesem User"));

        addressRepository.delete(address);
    }

    @Transactional
    public AddressDto setDefaultAddress(Long addressId, AppUser currentUser) {
        Address address = addressRepository.findByIdAndUser(addressId, currentUser)
                .orElseThrow(() -> new IllegalArgumentException("Adresse existiert nicht oder gehört nicht zu diesem User"));

        unsetDefaultForAll(currentUser);

        address.setDefaultAddress(true);
        Address saved = addressRepository.save(address);

        return toDto(saved);
    }

    private void unsetDefaultForAll(AppUser currentUser) {
        List<Address> all = addressRepository.findByUserOrderByIsDefaultDescIdAsc(currentUser);
        for (Address a : all) {
            if (a.isDefaultAddress()) {
                a.setDefaultAddress(false);
                addressRepository.save(a);
            }
        }
    }

    private AddressDto toDto(Address a) {
        AddressDto dto = new AddressDto();
        dto.setId(a.getId());
        dto.setFullName(a.getFullName());
        dto.setStreet(a.getStreet());
        dto.setZip(a.getZip());
        dto.setCity(a.getCity());
        dto.setCountry(a.getCountry());
        dto.setDefaultAddress(a.isDefaultAddress());
        return dto;
    }
}
