package com.willbarbosa.desafiocodecon.controller;

import com.willbarbosa.desafiocodecon.dto.*;
import com.willbarbosa.desafiocodecon.model.User;
import com.willbarbosa.desafiocodecon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UsersResponse> users(@RequestBody List<User> users) {
        userService.loadUsers(users);
        return ResponseEntity.ok(new UsersResponse("Arquivo recebido com sucesso", users.size()));
    }

    @GetMapping("/superusers")
    public ResponseEntity<ApiResponse<List<User>>> getSuperUsers() {
        long start = System.currentTimeMillis();
        List<User> users = userService.getSuperUsers();
        long execTime = System.currentTimeMillis() - start;
        return ResponseEntity.ok(new ApiResponse<>(execTime, users));
    }

    @GetMapping("/top-countries")
    public ResponseEntity<ApiResponse<TopCountriesResponse>> getTopCountries() {
        long start = System.currentTimeMillis();
        List<TopCountriesDTO> countries = userService.getTopCountries();
        long execTime = System.currentTimeMillis() - start;
        return ResponseEntity.ok(new ApiResponse<>(execTime, new TopCountriesResponse(countries)));
    }

    @GetMapping("/team-insights")
    public ResponseEntity<ApiResponse<TeamInsightsResponse>> getTeamInsights() {
        long start = System.currentTimeMillis();
        List<TeamInsightsDTO> insights = userService.getTeamInsights();
        long execTime = System.currentTimeMillis() - start;
        return ResponseEntity.ok(new ApiResponse<>(execTime, new TeamInsightsResponse(insights)));
    }

    @GetMapping("/active-users-per-day")
    public ResponseEntity<ApiResponse<ActiveUsersPerDayResponse>> getActiveUsersPerDay(@RequestParam(name = "min", required = false) Integer min) {
        long start = System.currentTimeMillis();
        List<ActiveUsersPerDayDTO> logins = userService.getActiveUsersPerDay(min);
        long execTime = System.currentTimeMillis() - start;
        return ResponseEntity.ok(new ApiResponse<>(execTime, new ActiveUsersPerDayResponse(logins)));
    }

    @GetMapping("/evaluation")
    public ResponseEntity<ApiResponse<EvaluationResponse>> getEvaluation() {
        long start = System.currentTimeMillis();
        EvaluationResponse report = userService.evaluateEndpoints();
        long execTime = System.currentTimeMillis() - start;
        return ResponseEntity.ok(new ApiResponse<>(execTime, report));
    }
}
