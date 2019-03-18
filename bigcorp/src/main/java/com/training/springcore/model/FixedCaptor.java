package com.training.springcore.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("FIXED")
public class FixedCaptor extends Captor {

    @NotNull
    private Long defaultPowerInWatt;

    public FixedCaptor(){
        super();
    }

    public FixedCaptor(String name, Site site, Long defaultPowerInWatt) {
        super(name, site , PowerSource.FIXED);
        this.defaultPowerInWatt = defaultPowerInWatt;
    }

    public Long getDefaultPowerInWatt() {
        return defaultPowerInWatt;
    }

    public void setDefaultPowerInWatt(Long defaultPowerInWatt) {
        this.defaultPowerInWatt = defaultPowerInWatt;
    }
}
