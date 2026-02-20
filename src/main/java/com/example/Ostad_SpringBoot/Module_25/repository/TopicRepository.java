package com.example.Ostad_SpringBoot.Module_25.repository;

import com.example.Ostad_SpringBoot.Module_25.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findAllByOrderByTopicOrderAsc();
    Topic findByTopicNameIgnoreCase(String topicName);
}