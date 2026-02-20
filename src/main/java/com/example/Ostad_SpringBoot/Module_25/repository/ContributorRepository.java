package com.example.Ostad_SpringBoot.Module_25.repository;

import com.example.Ostad_SpringBoot.Module_25.entity.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributorRepository extends JpaRepository<Contributor, Long> {
}