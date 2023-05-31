package com.example.PFE_PROJECT.Controlleur;
import com.example.PFE_PROJECT.dao.*;
import com.example.PFE_PROJECT.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/alerts")
public class AlertController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private IAlert ialert;
    @Autowired
    private IProjet iprojet;
    @GetMapping("/all")
    public List<Alert> listAlert() {
        return ialert.findAll();
    }
    @PutMapping("/update/{Id}")
    public Alert update(@RequestBody Alert alert, @PathVariable Long Id) {
        alert.setId(Id);
        return ialert.saveAndFlush(alert);
    }
    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteAlert(@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            ialert.delete(Id);
            message.put("etat","Alert deleted");
            return message;
        }

        catch (Exception e) {
            message.put("etat","Alert not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public Alert getoneAlert(@PathVariable Long id) {
        return ialert.findOne(id);
    }
    @PostMapping("/save")
    public Alert saveAlert(@RequestBody Alert al){
        return ialert.save(al);
    }

    @PostMapping("/save/{idprojet}")
    public Alert savealertbyProjet(@RequestBody Alert al, @PathVariable Long idprojet){
        Projet projet=iprojet.findOne(idprojet);
        al.setProjet(projet);
        Collection<Audit> audits = projet.getAudits();
        Collection<Alert> alerts = projet.getAlerts();
        //Collection<Revue> revues = projet.getProcessuses.getActivites.getDocumentsS.getRevues();
        Collection<Revue> revues = new ArrayList<>();
        for (Processus processus : projet.getProcessuses()) {
            for (Activite activite : processus.getActivites()) {
                for (DocumentS document : activite.getDocumentsS()) {
                    document.getRevues();
                }
            }
        }
        Collection<Liberation> liberations = new ArrayList<>();
        for (Processus processus : projet.getProcessuses()) {
            for (Activite activite : processus.getActivites()) {
                for (DocumentS document : activite.getDocumentsS()) {
                    document.getLiberations();
                }
            }
        }
        projet.updateStatus(audits,alerts,revues, liberations);
        iprojet.save(projet);
        return ialert.save(al);
    }
    @GetMapping("/getalertsbyprojet/{id}")
    public Collection<Alert> getalertsbyprojet(@PathVariable Long id) {
        Projet projet = iprojet.findOne(id);
        Collection<Alert> lA=projet.getAlerts();
        return lA;
    }
}
