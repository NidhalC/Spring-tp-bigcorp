package com.training.springcore.controller;

import com.training.springcore.model.*;
import com.training.springcore.repository.CaptorDao;
import com.training.springcore.service.measure.SimulatedMeasureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measures/captors/{id}/last/hours/{nbHours}")
public class MeasureController {
    private SimulatedMeasureService simulatedMeasureService;
    private CaptorDao captorDao;

    public MeasureController(SimulatedMeasureService simulatedMeasureService, CaptorDao captorDao) {
        this.simulatedMeasureService = simulatedMeasureService;
        this.captorDao = captorDao;
    }

    @GetMapping
    private List<MeasureDto> captorRest(@PathVariable String id , @PathVariable Long nbHours ) {
        Captor captor = captorDao.findById(id).orElseThrow(IllegalArgumentException::new);
        if (captor.getPowerSource() == PowerSource.SIMULATED) {
            return simulatedMeasureService.readMeasures(((SimulatedCaptor) captor),
                    Instant.now().minus(Duration.ofHours(nbHours)).truncatedTo(ChronoUnit.MINUTES),
                    Instant.now().truncatedTo(ChronoUnit.MINUTES),
                    MeasureStep.ONE_MINUTE)
                    .stream()
                    .map(m -> new MeasureDto(m.getInstant(),
                            m.getValueInWatt()))
                    .collect(Collectors.toList());
        }
// Pour le moment on ne gère qu'un type
        return new ArrayList<>();

    }
}

