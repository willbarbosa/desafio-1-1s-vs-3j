package com.willbarbosa.desafiocodecon.dto;

import java.math.BigDecimal;

public record TeamInsightsDTO(String team, int total_members, long leaders, long completed_projects, BigDecimal active_percentage) {}
