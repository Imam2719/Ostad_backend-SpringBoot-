package com.example.Ostad_SpringBoot.Module_25.repository;

import com.example.Ostad_SpringBoot.Module_25.entity.SubTopic;
import com.example.Ostad_SpringBoot.Module_25.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SubTopicRepository extends JpaRepository<SubTopic, Long> {
    Optional<SubTopic> findByTopicAndSubTopicNameIgnoreCase(Topic topic, String subTopicName);
}