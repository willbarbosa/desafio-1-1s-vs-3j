package com.willbarbosa.desafiocodecon.dto;

import java.util.Map;

public record EvaluationResponse(Map<String, EvaluationDTO> tested_endpoints) { }