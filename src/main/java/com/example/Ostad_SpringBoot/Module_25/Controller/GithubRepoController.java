package com.example.Ostad_SpringBoot.Module_25.Controller;

import com.example.Ostad_SpringBoot.Module_25.Service.GithubRepoService;
import com.example.Ostad_SpringBoot.Module_25.entity.Contributor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GithubRepoController {

    private final GithubRepoService githubRepoService;

    /**
     * GET /api/v1/read_contributions
     * Returns list of contributors from the JavaInREADME repository.
     */
    @GetMapping("/read_contributions")
    public ResponseEntity<List<Contributor>> readContributions() {
        return ResponseEntity.ok(githubRepoService.getContributors());
    }

    /**
     * GET /api/v1/read_indices
     * Returns all topics and their subtopics (parsed from README).
     */
    @GetMapping("/read_indices")
    public ResponseEntity<List<Map<String, Object>>> readIndices() {
        return ResponseEntity.ok(githubRepoService.getIndices());
    }

    /**
     * GET /api/v1/read_blog?topic_name={x}&sub_topic_name={y}
     * Returns blog content for the given topic and subtopic.
     */
    @GetMapping("/read_blog")
    public ResponseEntity<Map<String, Object>> readBlog(
            @RequestParam("topic_name") String topicName,
            @RequestParam("sub_topic_name") String subTopicName) {

        Map<String, Object> result = githubRepoService.getBlog(topicName, subTopicName);

        if (result.containsKey("error")) {
            return ResponseEntity.status(404).body(result);
        }
        return ResponseEntity.ok(result);
    }
}