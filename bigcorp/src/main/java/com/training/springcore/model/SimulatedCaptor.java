package com.training.springcore.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("SIMULATED")
public class SimulatedCaptor extends Captor {

    @NotNull
    private Long minPowerInWatt;

    @NotNull
    private Long maxPowerInWatt;

    @AssertTrue(message = "minPowerInWatt should be less than mawPowerInWatt")
    public boolean isValid(){
        return this.minPowerInWatt <= this.maxPowerInWatt;
    }

    public SimulatedCaptor() {
        super();

    }

    public SimulatedCaptor(String name, Site site, Long minPowerInWatt, Long maxPowerInWatt) {
        super(name, site, PowerSource.SIMULATED);
        this.minPowerInWatt = minPowerInWatt;
        this.maxPowerInWatt = maxPowerInWatt;
    }

    public Long getMinPowerInWatt() {
        return minPowerInWatt;
    }

    public void setMinPowerInWatt(Long minPowerInWatt) {
        this.minPowerInWatt = minPowerInWatt;
    }

    public Long getMaxPowerInWatt() {
        return maxPowerInWatt;
    }

    public void setMaxPowerInWatt(Long maxPowerInWatt) {
        this.maxPowerInWatt = maxPowerInWatt;
    }
}
