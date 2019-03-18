package com.training.springcore.repository;

import com.training.springcore.model.Captor;
import com.training.springcore.model.Measure;
import com.training.springcore.model.RealCaptor;
import com.training.springcore.model.Site;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan
public class MeasureDaoImplTest {

    @Autowired
    private MeasureDao measureDao;

    @Autowired
    private EntityManager entityManager;

    private Instant instant;

    private Captor captor;


    @Test
    public void findById() {
        Optional<Measure> measure = measureDao.findById(-1L);
        Assertions.assertThat(measure)
                .get()
                .extracting(Measure::getId,
                        Measure::getInstant,
                        Measure::getValueInWatt,
                        m->m.getCaptor().getName(),
                        m->m.getCaptor().getSite().getName())
                .containsExactly(-1L,Instant.parse("2018-08-09T11:00:00.000Z"),1_000_000,"Eolienne","Bigcorp Lyon");

    }
    @Test
    public void findByIdShouldReturnNullWhenIdUnknown() {
        Optional<Measure> measure = measureDao.findById(-1000L);
        Assertions.assertThat(measure).isEmpty();
    }
    @Test
    public void findAll() {
        List<Measure> measures = measureDao.findAll();
        Assertions.assertThat(measures).hasSize(10);
    }
    @Test
    public void create() {
        Captor captor = new RealCaptor("Eolienne", new Site("site"));
        captor.setId("c1");
        Assertions.assertThat(measureDao.findAll()).hasSize(10);
        measureDao.save(new Measure(Instant.now(), 2_333_666, captor));
        Assertions.assertThat(measureDao.findAll()).hasSize(11);
    }
    @Test
    public void update() {
        Optional<Measure> measure = measureDao.findById(-1L);
        Assertions.assertThat(measure)
                .get()
                .extracting("valueInWatt")
                .containsExactly(1_000_000);
        measure.ifPresent(m->{
            m.setValueInWatt(2_333_666);
            measureDao.save(m);

        });

        measure = measureDao.findById(-1L);
        Assertions.assertThat(measure)
                .get()
                .extracting("valueInWatt")
                .containsExactly(2_333_666);
    }
    @Test
    public void delete() {
        Measure newMeasure = measureDao.getOne(-1L);
        Assertions.assertThat(measureDao.findById(newMeasure.getId())).isNotEmpty();

        measureDao.delete(newMeasure);
        Assertions.assertThat(measureDao.findById(newMeasure.getId())).isEmpty();
    }
    @Test
    public void deleteByCaptorId() {
        Assertions.assertThat(measureDao.findAll().stream().filter(m ->
                m.getCaptor().getId().equals("c1"))).hasSize(5);
        measureDao.deleteByCaptorId("c1");
        Assertions.assertThat(measureDao.findAll().stream().filter(m ->
                m.getCaptor().getId().equals("c1"))).isEmpty();
    }

}