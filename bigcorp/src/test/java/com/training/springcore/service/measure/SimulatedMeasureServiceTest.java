package com.training.springcore.service.measure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.springcore.controller.MeasureDto;
import com.training.springcore.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

@RestClientTest(SimulatedMeasureService.class)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SimulatedMeasureServiceTest.MeasureServiceConfigurationTest.class})
public class SimulatedMeasureServiceTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Configuration
    @ComponentScan({"com.training.springcore.service.measure","com.training.springcore.config.properties"})
    @PropertySource("classpath:application.properties")
    @EnableConfigurationProperties
    public static class MeasureServiceConfigurationTest{ }


    @Autowired
    private SimulatedMeasureService service;
    private Site site;

    @Before
    public void init() {
        site = new Site("name");
        site.setId("site1");

    }

    /**
     * Captor used in tests
     */
    private SimulatedCaptor captor = new SimulatedCaptor("test", site, 500000L , 1000000L);
    /**
     * Start instant used in tests
     */
    Instant start = Instant.parse("2018-09-01T22:00:00Z");
    /**
     * End instant used in tests. We define a one day period
     */
    Instant end = start.plusSeconds(60 * 60 * 24);


    @Test
    public void readMeasuresThrowsExceptionWhenArgIsNull() {
        assertThatThrownBy(() -> service.readMeasures(null, start, end,
                MeasureStep.ONE_DAY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("captor is required");
        assertThatThrownBy(() -> service.readMeasures(captor, null, end,
                MeasureStep.ONE_DAY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("start is required");
        assertThatThrownBy(() -> service.readMeasures(captor, start, null,
                MeasureStep.ONE_DAY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("end is required");
        assertThatThrownBy(() -> service.readMeasures(captor, start, end, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("step is required");
        assertThatThrownBy(() -> service.readMeasures(captor, end, start,
                MeasureStep.ONE_DAY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("start must be before end");
    }

    @Test
    public void readMeasures() throws Exception {
        Instant start = Instant.parse("2018-09-01T22:00:00Z");
        Instant end = Instant.parse("2018-09-01T22:30:00Z");
        List<MeasureDto> expectedMeasures = Arrays.asList(
                new MeasureDto(Instant.parse("2018-09-01T22:00:00Z"), 1234),
                new MeasureDto(Instant.parse("2018-09-01T22:30:00Z"), 4567));
        String expectedJson = objectMapper.writeValueAsString(expectedMeasures);
        String request = "http://localhost:8090/measures?start=2018-09-01T22:00:00Z&" +
                "end=2018-09-01T22:30:00Z&min=500000&max=1000000&step=3600";
        this.server.expect(MockRestRequestMatchers.requestTo(request))
                .andRespond(MockRestResponseCreators.withSuccess(expectedJson,
                        MediaType.APPLICATION_JSON));
        List<Measure> measures = service.readMeasures(captor, start, end,
                MeasureStep.ONE_HOUR);
        assertThat(measures).hasSize(2);
// And we have a value for each hour of the period
        assertThat(measures)
                .extracting("instant", "valueInWatt")
                .containsExactly(
                        tuple(Instant.parse("2018-09-01T22:00:00Z"),1234),
                        tuple(Instant.parse("2018-09-01T22:30:00Z"),4567));
    }
}
