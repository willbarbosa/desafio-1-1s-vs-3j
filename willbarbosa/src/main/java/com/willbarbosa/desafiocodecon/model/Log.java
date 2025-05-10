package com.willbarbosa.desafiocodecon.model;

import com.willbarbosa.desafiocodecon.enums.Action;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    private String date;
    private Action action;
}