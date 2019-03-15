package com.training.springcore.service;

import com.training.springcore.config.Monitored;
import com.training.springcore.model.Captor;
import com.training.springcore.model.PowerSource;
import com.training.springcore.repository.CaptorDao;
import com.training.springcore.service.measure.FixedMeasureService;
import com.training.springcore.service.measure.RealMeasureService;
import com.training.springcore.service.measure.SimulatedMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CaptorServiceImpl implements CaptorService{
    private FixedMeasureService serviceFixed;
    private SimulatedMeasureService serviceSimulated;
    private RealMeasureService serviceReal;

    private CaptorDao captorDao;

    @Autowired
    public CaptorServiceImpl(FixedMeasureService serviceFixed,SimulatedMeasureService serviceSimulated, RealMeasureService serviceReal, CaptorDao captorDao ){
        this.serviceFixed = serviceFixed;
        this.serviceSimulated = serviceSimulated;
        this.serviceReal = serviceReal ;
        this.captorDao = captorDao;
    }



    @Monitored
    public Set<Captor> findBySite(String siteId) {
            return captorDao.findBySiteId(siteId).stream().collect(Collectors.toSet());
    }


}
