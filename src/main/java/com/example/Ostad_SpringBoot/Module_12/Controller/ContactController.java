package com.example.Ostad_SpringBoot.Module_12.Controller;


import com.example.Ostad_SpringBoot.Module_12.Exception.ResourceNotFoundException;
import com.example.Ostad_SpringBoot.Module_12.Model.Contact;
import com.example.Ostad_SpringBoot.Module_12.Repositories.ContactNameEmailProjection;
import com.example.Ostad_SpringBoot.Module_12.Service.ContactService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    // 1. POST /api/contacts -> create a new contact
    @PostMapping
    public ResponseEntity<Contact> createContact(@Valid @RequestBody Contact contact) {
        Contact saved = service.save(contact);
        return ResponseEntity.created(URI.create("/api/contacts/" + saved.getId())).body(saved);
    }

    // 2. GET /api/contacts/{id} -> get by id
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getById(@PathVariable Long id) {
        Contact c = service.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with id " + id));
        return ResponseEntity.ok(c);
    }

    // 3. GET /api/contacts/search/name?term=... -> search by firstName containing term
    @GetMapping("/search/name")
    public ResponseEntity<List<Contact>> searchByName(@RequestParam String term) {
        return ResponseEntity.ok(service.searchByFirstName(term));
    }

    // 4. GET /api/contacts/count-inactive -> returns total count inactive
    @GetMapping("/count-inactive")
    public ResponseEntity<Long> countInactive() {
        return ResponseEntity.ok(service.countInactive());
    }

    // 5. GET /api/contacts/search/category?name=Family -> returns projection (firstName + email)
    @GetMapping("/search/category")
    public ResponseEntity<List<ContactNameEmailProjection>> searchByCategory(@RequestParam("name") String category) {
        return ResponseEntity.ok(service.findByCategory(category));
    }

    // 6. GET /api/contacts/active/page?page=0&size=5&sort=lastName,asc
    @GetMapping("/active/page")
    public ResponseEntity<Page<Contact>> activeContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        // sort param example: sort=lastName,asc
        Sort.Direction dir = Sort.Direction.fromString(sort[1]);
        Sort s = Sort.by(dir, sort[0]);
        Pageable pageable = PageRequest.of(page, size, s);
        return ResponseEntity.ok(service.activeContacts(pageable));
    }

    // 7. DELETE /api/contacts/delete-by-prefix?prefix=000 -> deletes contacts with phone number starting with prefix
    @DeleteMapping("/delete-by-prefix")
    public ResponseEntity<String> deleteByPrefix(@RequestParam String prefix) {
        int deleted = service.deleteByPhonePrefix(prefix);
        return ResponseEntity.ok("Deleted " + deleted + " contact(s) with phone prefix '" + prefix + "'");
    }

    // 8. POST /api/contacts/mass -> bulk insert (accepts array of contact objects)
    @PostMapping("/mass")
    public ResponseEntity<List<Contact>> massInsert(@Valid @RequestBody List<Contact> contacts) {
        List<Contact> saved = service.saveAll(contacts);
        return ResponseEntity.ok(saved);
    }
}
