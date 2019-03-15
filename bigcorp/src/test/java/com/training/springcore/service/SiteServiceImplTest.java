package com.training.springcore.service;

import com.training.springcore.model.Captor;
import com.training.springcore.model.PowerSource;
import com.training.springcore.model.RealCaptor;
import com.training.springcore.model.Site;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Set;


public class SiteServiceImplTest {

    @Mock
    private SiteService siteService;
    @Mock
    ResourceLoader resourceLoader;

    @Mock
    private CaptorService captorService;

    @InjectMocks
    private SiteServiceImpl siteServiceImpl;

    @Rule
    public OutputCapture output = new OutputCapture();

    private Site site;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
            site = new Site("name");
            site.setId("site1");
    }
    /** Test sur la resourceLoader**/



    @Test
    public void findByIdShouldReturnNullWhenIdIsNull(){
        // Initialisation
        String siteId = null;

        // Appel du SUT
        Site site = siteService.findById(siteId);

        // Vérification
        Assertions.assertThat(site).isNull();
    }

    /** Test sur la méthode de recherche de Site par Id**/

    @Test
    public void findById(){
        // Initialisation
       // String siteId = "site1";
        Set<Captor> expectedCaptors = Collections.singleton(new RealCaptor("Capteur A",site ));
        Mockito.when(captorService.findBySite("site1")).thenReturn(expectedCaptors);

        // Appel du SUT
        Site site = siteService.findById("site1");

        // Vérification
        Assertions.assertThat(site.getId()).isEqualTo("site1");
        Assertions.assertThat(site.getName()).isEqualTo("name");
        Assertions.assertThat(site.getCaptors()).isEqualTo(expectedCaptors);
    }

}