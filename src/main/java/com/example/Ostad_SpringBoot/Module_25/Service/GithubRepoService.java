package com.example.Ostad_SpringBoot.Module_25.Service;

import com.example.Ostad_SpringBoot.Module_25.entity.Contributor;
import com.example.Ostad_SpringBoot.Module_25.entity.SubTopic;
import com.example.Ostad_SpringBoot.Module_25.entity.Topic;
import com.example.Ostad_SpringBoot.Module_25.repository.ContributorRepository;
import com.example.Ostad_SpringBoot.Module_25.repository.SubTopicRepository;
import com.example.Ostad_SpringBoot.Module_25.repository.TopicRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GithubRepoService {

    private final ContributorRepository contributorRepository;
    private final TopicRepository topicRepository;
    private final SubTopicRepository subTopicRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${github.token}")
    private String githubToken;

    @Value("${github.repo.owner}")
    private String repoOwner;

    @Value("${github.repo.name}")
    private String repoName;

    // ─────────────────────────────────────────────────────────
    // Initial load on startup + scheduled daily refresh
    // ─────────────────────────────────────────────────────────

    @PostConstruct
    public void initOnStartup() {
        log.info("Module 25: Initial GitHub data sync on startup...");
        syncAll();
    }

    @Scheduled(cron = "${github.sync.cron}")
    public void scheduledSync() {
        log.info("Module 25: Scheduled GitHub data sync triggered.");
        syncAll();
    }

    private void syncAll() {
        try {
            syncContributors();
            syncReadme();
        } catch (Exception e) {
            log.error("Module 25: Error during GitHub sync: {}", e.getMessage(), e);
        }
    }

    // ─────────────────────────────────────────────────────────
    // Fetch contributors from GitHub API
    // ─────────────────────────────────────────────────────────

    @Transactional
    public void syncContributors() {
        String url = "https://api.github.com/repos/" + repoOwner + "/" + repoName + "/contributors";
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, buildRequest(), String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode nodes = objectMapper.readTree(response.getBody());
                contributorRepository.deleteAll();
                for (JsonNode node : nodes) {
                    Contributor c = new Contributor();
                    c.setLogin(node.path("login").asText());
                    c.setAvatarUrl(node.path("avatar_url").asText());
                    c.setHtmlUrl(node.path("html_url").asText());
                    c.setContributions(node.path("contributions").asInt());
                    contributorRepository.save(c);
                }
                log.info("Module 25: Synced {} contributors.", nodes.size());
            }
        } catch (Exception e) {
            log.error("Module 25: Failed to sync contributors: {}", e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────
    // Fetch and parse README from GitHub
    // ─────────────────────────────────────────────────────────

    @Transactional
    public void syncReadme() {
        String rawUrl = "https://raw.githubusercontent.com/" + repoOwner + "/" + repoName + "/main/README.md";
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    rawUrl, HttpMethod.GET, buildRequest(), String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                parseAndStoreReadme(response.getBody());
            }
        } catch (Exception e) {
            log.error("Module 25: Failed to sync README: {}", e.getMessage());
        }
    }

    /**
     * Parses the README markdown.
     *
     * Expected structure:
     *   ## Topic Name        <- H2 = topic
     *   ### Sub Topic Name   <- H3 = subtopic
     *   content here...      <- lines below H3 until next heading
     *
     * All existing topics/subtopics are wiped and re-saved on each sync.
     */
    private void parseAndStoreReadme(String markdown) {
        topicRepository.deleteAll();   // cascade deletes subtopics too

        String[] lines = markdown.split("\\r?\\n");

        Topic currentTopic = null;
        SubTopic currentSubTopic = null;
        StringBuilder contentBuffer = new StringBuilder();

        int topicOrder = 0;
        int subTopicOrder = 0;

        for (String line : lines) {
            if (line.startsWith("## ")) {
                // Save previous subtopic content if any
                saveSubTopicContent(currentSubTopic, contentBuffer);
                contentBuffer.setLength(0);
                currentSubTopic = null;

                // Save & start new topic
                if (currentTopic != null) {
                    topicRepository.save(currentTopic);
                }
                currentTopic = new Topic();
                currentTopic.setTopicName(line.substring(3).trim());
                currentTopic.setTopicOrder(++topicOrder);
                currentTopic.setSubTopics(new ArrayList<>());
                subTopicOrder = 0;

            } else if (line.startsWith("### ") && currentTopic != null) {
                // Save previous subtopic content
                saveSubTopicContent(currentSubTopic, contentBuffer);
                contentBuffer.setLength(0);

                // New subtopic
                currentSubTopic = new SubTopic();
                currentSubTopic.setSubTopicName(line.substring(4).trim());
                currentSubTopic.setSubTopicOrder(++subTopicOrder);
                currentSubTopic.setContent("");
                currentSubTopic.setTopic(currentTopic);
                currentTopic.getSubTopics().add(currentSubTopic);

            } else if (currentSubTopic != null) {
                // Accumulate content lines
                contentBuffer.append(line).append("\n");
            }
        }

        // Flush last subtopic + topic
        saveSubTopicContent(currentSubTopic, contentBuffer);
        if (currentTopic != null) {
            topicRepository.save(currentTopic);
        }

        log.info("Module 25: README parsed. {} topics stored.", topicOrder);
    }

    private void saveSubTopicContent(SubTopic subTopic, StringBuilder buffer) {
        if (subTopic != null) {
            subTopic.setContent(buffer.toString().trim());
        }
    }

    // ─────────────────────────────────────────────────────────
    // Public methods called by controller
    // ─────────────────────────────────────────────────────────

    public List<Contributor> getContributors() {
        return contributorRepository.findAll();
    }

    public List<Map<String, Object>> getIndices() {
        List<Topic> topics = topicRepository.findAllByOrderByTopicOrderAsc();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Topic topic : topics) {
            Map<String, Object> topicMap = new LinkedHashMap<>();
            topicMap.put("topicName", topic.getTopicName());
            topicMap.put("order", topic.getTopicOrder());

            List<Map<String, Object>> subList = new ArrayList<>();
            for (SubTopic st : topic.getSubTopics()) {
                Map<String, Object> stMap = new LinkedHashMap<>();
                stMap.put("subTopicName", st.getSubTopicName());
                stMap.put("order", st.getSubTopicOrder());
                subList.add(stMap);
            }
            subList.sort(Comparator.comparingInt(m -> (int) m.get("order")));
            topicMap.put("subTopics", subList);
            result.add(topicMap);
        }
        return result;
    }

    public Map<String, Object> getBlog(String topicName, String subTopicName) {
        Topic topic = topicRepository.findByTopicNameIgnoreCase(topicName);
        if (topic == null) {
            return Map.of("error", "Topic not found: " + topicName);
        }

        Optional<SubTopic> subTopicOpt = subTopicRepository
                .findByTopicAndSubTopicNameIgnoreCase(topic, subTopicName);

        if (subTopicOpt.isEmpty()) {
            return Map.of("error", "SubTopic not found: " + subTopicName);
        }

        SubTopic st = subTopicOpt.get();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("topicName", topic.getTopicName());
        response.put("subTopicName", st.getSubTopicName());
        response.put("content", st.getContent());
        return response;
    }

    // ─────────────────────────────────────────────────────────
    // Helper: build HTTP request with auth headers
    // ─────────────────────────────────────────────────────────

    private HttpEntity<Void> buildRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github+json");
        headers.set("X-GitHub-Api-Version", "2022-11-28");
        if (githubToken != null && !githubToken.isBlank()
                && !githubToken.equals("YOUR_GITHUB_PERSONAL_ACCESS_TOKEN_HERE")) {
            headers.set("Authorization", "Bearer " + githubToken);
        }
        return new HttpEntity<>(headers);
    }
}