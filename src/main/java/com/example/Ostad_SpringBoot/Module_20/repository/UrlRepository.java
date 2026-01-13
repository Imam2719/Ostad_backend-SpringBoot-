package com.example.Ostad_SpringBoot.Module_20.repository;

import com.example.Ostad_SpringBoot.Module_20.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByShortUrl(String shortUrl);

    boolean existsByShortUrl(String shortUrl);

    boolean existsByOriginalUrl(String originalUrl);

    Optional<Url> findByOriginalUrl(String originalUrl);
}