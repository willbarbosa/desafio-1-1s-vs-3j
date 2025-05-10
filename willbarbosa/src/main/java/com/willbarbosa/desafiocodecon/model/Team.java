package com.willbarbosa.desafiocodecon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    private String name;
    private boolean leader;
    private List<Project> projects;
}
