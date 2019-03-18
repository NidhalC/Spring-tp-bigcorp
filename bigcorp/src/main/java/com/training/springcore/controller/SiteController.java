package com.training.springcore.controller;

import com.training.springcore.model.Site;
import com.training.springcore.repository.CaptorDao;
import com.training.springcore.repository.MeasureDao;
import com.training.springcore.repository.SiteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;


@Controller
@RequestMapping("/sites")
@Transactional
public class SiteController {
    private SiteDao siteDao;
    private CaptorDao captorDao;
    private MeasureDao measureDao;

    public SiteController(){

    }
    @Autowired
    public SiteController(SiteDao siteDao, CaptorDao captorDao,MeasureDao measureDao){
        this.siteDao = siteDao;
        this.captorDao = captorDao;
        this.measureDao = measureDao;
    }
    @GetMapping
    public ModelAndView readSite(Model model){
       return new ModelAndView("sites")
               .addObject("sites",siteDao.findAll());
    }

    @GetMapping("/{id}")
    public ModelAndView findById(@PathVariable String id){
        return new ModelAndView("create")
                .addObject("site",
                        siteDao.findById(id).orElseThrow(IllegalArgumentException::new));
    }
    @GetMapping("/create")
    public ModelAndView create(Model model){
        return new ModelAndView("create").addObject("site", new Site());
    }



    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView save(Site site) {
        if (site.getId() == null) {
            return new ModelAndView("create").addObject("site", siteDao.save(site));
        }
        else {
            Site siteToPersist =
                    siteDao.findById(site.getId()).orElseThrow(IllegalArgumentException::new);

            // L'utilisateur ne peut changer que le nom du site sur l'écran
            siteToPersist.setName(site.getName());
            return new ModelAndView("sites").addObject("sites", siteDao.findAll());
        }
    }

    @PostMapping("/{id}/delete")
    public  ModelAndView delete (@PathVariable String id){
        Site site = siteDao.findById(id).orElseThrow(IllegalArgumentException::new);
        site.getCaptors().forEach(c-> measureDao.deleteByCaptorId(c.getId()));
        captorDao.deleteBySiteId(id);
        siteDao.deleteById(id);
        return new ModelAndView("sites").addObject("sites", siteDao.findAll());
    }

    @GetMapping("/{id}/measures")
    public ModelAndView findMeasuresById(@PathVariable String id){
        Site site = siteDao.findById(id).orElseThrow(IllegalAccessError::new);

        // On doit concatèner les id de captor pour que les templates puissent les prendrent en compte
        String captors = site.getCaptors()
                .stream()
                .map(c-> "{ id: '" + c.getId() + "', name: '"+ c.getName()
                        +"'}")
                .collect(Collectors.joining(","));
        return new ModelAndView("site-measures")
                .addObject("site" , site)
                .addObject("captors", captors);
    }


}
