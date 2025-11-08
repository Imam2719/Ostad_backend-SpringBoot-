package com.example.Ostad_SpringBoot.Module_12.Repositories;


import com.example.Ostad_SpringBoot.Module_12.Model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    // 1) find contacts whose firstName contains search term (case-insensitive)
    List<Contact> findByFirstNameContainingIgnoreCase(String term);

    // 2) count total inactive contacts
    long countByIsActiveFalse();

    // 3) retrieve projection (firstName + email) for contacts by category (case-insensitive)
    List<ContactNameEmailProjection> findByCategoryIgnoreCase(String category);

    // 4) retrieve all active contacts with pagination
    Page<Contact> findByIsActiveTrue(Pageable pageable);

    // 5) delete contacts whose phoneNumber starts with prefix (JPQL)
    @Modifying
    @Transactional
    @Query("DELETE FROM Contact c WHERE c.phoneNumber LIKE concat(:prefix, '%')")
    int deleteByPhoneNumberPrefix(@Param("prefix") String prefix);

    // 6) find by phone number (for possible lookup)
    Contact findByPhoneNumber(String phoneNumber);
}
