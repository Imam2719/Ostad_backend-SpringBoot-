package com.example.Ostad_SpringBoot.Module_12.Service;


import com.example.Ostad_SpringBoot.Module_12.Model.Contact;
import com.example.Ostad_SpringBoot.Module_12.Repositories.ContactNameEmailProjection;
import com.example.Ostad_SpringBoot.Module_12.Repositories.ContactRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository repo;

    public ContactService(ContactRepository repo) {
        this.repo = repo;
    }

    public Contact save(Contact contact) {
        return repo.save(contact);
    }

    public List<Contact> saveAll(List<Contact> contacts) {
        return repo.saveAll(contacts);
    }

    public Optional<Contact> findById(Long id) {
        return repo.findById(id);
    }

    public List<Contact> searchByFirstName(String term) {
        return repo.findByFirstNameContainingIgnoreCase(term);
    }

    public long countInactive() {
        return repo.countByIsActiveFalse();
    }

    public List<ContactNameEmailProjection> findByCategory(String category) {
        return repo.findByCategoryIgnoreCase(category);
    }

    public Page<Contact> activeContacts(Pageable pageable) {
        return repo.findByIsActiveTrue(pageable);
    }

    @Transactional
    public int deleteByPhonePrefix(String prefix) {
        return repo.deleteByPhoneNumberPrefix(prefix);
    }
}
