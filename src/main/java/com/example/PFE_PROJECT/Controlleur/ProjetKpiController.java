package com.example.PFE_PROJECT.Controlleur;

import com.example.PFE_PROJECT.dao.*;
import com.example.PFE_PROJECT.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/projetkpi")
public class ProjetKpiController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);
    @Autowired
    private IProjetKpi iProjetKpi;
    @Autowired
    private IProjet iProjet;
    @Autowired
    private IKpi iKpi;
/*    @PostMapping("/save/{projetId}/{kpiId}")
    public ProjetKpi saveProjetKpi(@PathVariable Long projetId,
                                   @PathVariable Long kpiId,
                                   @RequestBody Map<String, String> requestBody){
        String objectif = requestBody.get("objectif");
        ProjetKpi projetKpi = new ProjetKpi();
        projetKpi.setObjectif(objectif);
        Kpi kpi = iKpi.findOne(kpiId);
        Projet projet = iProjet.findOne(projetId);
        projetKpi.setKpi(kpi);
        projetKpi.setProjet(projet);
        return iProjetKpi.save(projetKpi);
    }*/
    @PostMapping("/save/{projetId}/{kpiId}")
    public ProjetKpi saveProjetKpi(@PathVariable Long projetId,
                                   @PathVariable Long kpiId,
                                   @RequestBody Map<String, String> requestBody){
        String objectif = requestBody.get("objectif");
        ProjetKpi projetKpi = new ProjetKpi();
        projetKpi.setObjectif(objectif);
        Kpi kpi = iKpi.findOne(kpiId);
        Projet projet = iProjet.findOne(projetId);
        projetKpi.setKpi(kpi);
        projetKpi.setProjet(projet);
        return iProjetKpi.save(projetKpi);
    }
}
