package com.willbarbosa.desafiocodecon.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String name;
    private int age;
    private int score;
    private boolean active;
    private String country;
    private Team team;
    private List<Log> logs;
}
