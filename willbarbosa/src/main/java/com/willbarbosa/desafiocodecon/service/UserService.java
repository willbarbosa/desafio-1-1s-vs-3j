package com.willbarbosa.desafiocodecon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.willbarbosa.desafiocodecon.dto.*;
import com.willbarbosa.desafiocodecon.enums.Action;
import com.willbarbosa.desafiocodecon.model.Project;
import com.willbarbosa.desafiocodecon.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    private final List<User> userStore = new ArrayList<>();

    public void loadUsers(List<User> users) {
        userStore.clear();
        userStore.addAll(users);
    }

    public List<User> getSuperUsers() {
        return userStore.stream()
                .filter(u -> u.isActive() && u.getScore() >= 900)
                .collect(Collectors.toList());
    }

    public List<TopCountriesDTO> getTopCountries() {
        Map<String, Long> countMap = getSuperUsers().stream()
                .collect(Collectors.groupingBy(User::getCountry, Collectors.counting()));

        return countMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> new TopCountriesDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public List<TeamInsightsDTO> getTeamInsights() {
        Map<String, List<User>> grouped = userStore.stream()
                .filter(u -> u.getTeam() != null)
                .collect(Collectors.groupingBy(u -> u.getTeam().getName()));

        return grouped.entrySet().stream().map(entry -> {
            String teamName = entry.getKey();
            List<User> teamMembers = entry.getValue();
            long leaders = teamMembers.stream().filter(u -> u.getTeam().isLeader()).count();
            long completedProjects = teamMembers.stream()
                    .flatMap(u -> u.getTeam().getProjects().stream())
                    .filter(Project::isCompleted)
                    .count();
            BigDecimal activePct = BigDecimal.valueOf(100.0 * teamMembers.stream().filter(User::isActive).count() / teamMembers.size())
                    .setScale(1, RoundingMode.HALF_UP);
            return new TeamInsightsDTO(teamName, teamMembers.size(), leaders, completedProjects, activePct);
        }).collect(Collectors.toList());
    }

    public List<ActiveUsersPerDayDTO> getActiveUsersPerDay(Integer min) {
        Map<String, Long> loginMap = new HashMap<>();

        userStore.stream()
                .flatMap(u -> u.getLogs().stream())
                .filter(log -> log.getAction().equals(Action.login))
                .forEach(log -> loginMap.merge(log.getDate(), 1L, Long::sum));

        return loginMap.entrySet().stream()
                .filter(e -> min == null || e.getValue() >= min)
                .map(e -> new ActiveUsersPerDayDTO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(ActiveUsersPerDayDTO::date))
                .collect(Collectors.toList());
    }

    public EvaluationResponse evaluateEndpoints() {
        Map<String, EvaluationDTO> results = new HashMap<>();

        List<String> endpoints = Arrays.asList("/superusers", "/top-countries", "/team-insights", "/active-users-per-day");

        for (String endpoint : endpoints) {
            long startTime = System.currentTimeMillis();
            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080" + endpoint, String.class);
            long executionTime = System.currentTimeMillis() - startTime;

            int status = response.getStatusCode().value();
            boolean validResponse = status == HttpStatus.OK.value();
            boolean isJsonValid = isValidJson(response.getBody());

            results.put(endpoint, new EvaluationDTO(status, (int) executionTime, validResponse && isJsonValid));
        }

        return new EvaluationResponse(results);
    }

    private boolean isValidJson(String response) {
        try {
            new ObjectMapper().readTree(response);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
