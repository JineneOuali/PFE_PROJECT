package com.example.PFE_PROJECT.Controlleur;

import com.example.PFE_PROJECT.dao.*;
import com.example.PFE_PROJECT.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/kpiVals")
public class KpiValController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private IKpiVal ikpival;
    @Autowired
    private IKpi ikpi;

    @Autowired
    private IProjet iProjet;



    @GetMapping("/all")
    public List<KpiVal> listKpiVal() {
        return ikpival.findAll();
    }
/*    @PutMapping("/update/{Id}")
    public KpiVal update(@RequestBody KpiVal kpival, @PathVariable Long Id) {
       // kpival.setId(Id);
        //return ikpival.saveAndFlush(kpival);
        Kpi kpi = ikpi.findOne(Id);
        kpival.setKpi(kpi);
        return ikpival.saveAndFlush(kpival);
    }*/
@PutMapping("/update/{kpiValId}")
public KpiVal update(@RequestBody KpiVal newKpiVal, @PathVariable Long kpiValId) {
    // Get the KpiVal entity
    KpiVal oldKpiVal = ikpival.findOne(kpiValId);

    // Create a new KpiVal entity with the updated values
    KpiVal newKpiValEntity = new KpiVal();
    newKpiValEntity.setValeur(newKpiVal.getValeur());
    newKpiValEntity.setCreationDate(newKpiVal.getCreationDate());
    newKpiValEntity.setKpi(oldKpiVal.getKpi());

    // Add the new KpiVal entity to the Kpi entity's collection of KpiVal entities
    oldKpiVal.getKpi().getKpiVals().add(newKpiValEntity);

    // Save the new KpiVal entity
    ikpival.saveAndFlush(newKpiValEntity);

    return newKpiValEntity;
}
    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteKpiVal(@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            ikpival.delete(Id);
            message.put("etat","KpiVAL deleted");
            return message;
        }

        catch (Exception e) {
            message.put("etat","KpiVAL not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public KpiVal getonekpiVal(@PathVariable Long id) {
        return ikpival.findOne(id);
    }
    @PostMapping("/save")
    public KpiVal saveKpiVal(@RequestBody KpiVal k){
        return ikpival.save(k);
    }
    @PostMapping("/save/{projetId}/{kpiId}")
    public KpiVal saveProjetKpi(@PathVariable Long projetId,
                                   @PathVariable Long kpiId,
                                   @RequestBody Map<String, String> requestBody){
        String objectif = requestBody.get("objectif");
        String valeur = requestBody.get("valeur");
        String creationDate = requestBody.get("creationDate");
        KpiVal KpiVal = new KpiVal();
        KpiVal.setObjectif(objectif);
        KpiVal.setValeur(valeur);
        KpiVal.setCreationDate(creationDate);
        Kpi kpi = ikpi.findOne(kpiId);
        Projet projet = iProjet.findOne(projetId);
        kpi.setObjectif(objectif);
        KpiVal.setKpi(kpi);
        KpiVal.setProjet(projet);
        return ikpival.save(KpiVal);
    }
    @GetMapping("/all/{projetId}/{kpiId}")
    public Collection<KpiVal> listKpiVal(@PathVariable Long projetId, @PathVariable Long kpiId) {
        Collection<KpiVal> kpiVals = ikpival.findByKpiIdAndProjetId(kpiId, projetId);
        return kpiVals;
    }
}
