package com.training.springcore.controller;

import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

public class MeasureDto {

    private  final Instant instant;
    private  final  Integer valueInWatt;

    public MeasureDto(Instant instant, Integer valueInWatt) {
        this.instant = instant;
        this.valueInWatt = valueInWatt;
    }

    public Instant getInstant() {
        return instant;
    }

    public Integer getValueInWatt() {
        return valueInWatt;
    }
}
