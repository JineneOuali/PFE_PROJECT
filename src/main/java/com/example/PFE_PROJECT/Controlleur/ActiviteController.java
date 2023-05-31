package com.example.PFE_PROJECT.Controlleur;

import com.example.PFE_PROJECT.dao.*;
import com.example.PFE_PROJECT.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/activites")
public class ActiviteController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private IActivite iactivite;
    @Autowired
    private IDocumentE idocE;
    @Autowired
    private IProcessus iprocessus;
    @GetMapping("/all")
    public List<Activite> listActivite() {
        return iactivite.findAll();
    }
/*    @PutMapping("/update/{Id}")
    public Activite update(@RequestBody Activite activite, @PathVariable Long Id) {
        activite.setId(Id);
        return iactivite.saveAndFlush(activite);
    }*/
    @PutMapping("/update/{id}")
    public Activite update(@RequestBody Activite activite, @PathVariable Long id) {
        Activite existingActivite = iactivite.findOne(id);
        if (existingActivite != null) {
            existingActivite.setTitre(activite.getTitre());
            existingActivite.setDescription(activite.getDescription());

            // Check if the activite has an associated processus object
            Processus updatedProcessus = activite.getProcessus();
            if (updatedProcessus != null) {
                // Load the existing processus object from the database
                Processus existingProcessus = iprocessus.findOne(updatedProcessus.getId());
                if (existingProcessus != null) {
                    // Update the processus fields with the new values
                    existingProcessus.setTitre(updatedProcessus.getTitre());
                    existingProcessus.setDescription(updatedProcessus.getDescription());
                    // Set the updated processus on the existing activite object
                    existingActivite.setProcessus(existingProcessus);
                }
            }

            return iactivite.saveAndFlush(existingActivite);
        }
        return null;
    }
    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteActivite(@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            iactivite.delete(Id);
            message.put("etat","activite deleted");
            return message;
        }

        catch (Exception e) {
            message.put("etat","activite not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public Activite getoneactivite(@PathVariable Long id) {
        return iactivite.findOne(id);
    }
    @PostMapping("/save")
    public Activite saveActivite(@RequestBody Activite a){
        return iactivite.save(a);
    }

    @PostMapping("/save/{idpro}")
    public Activite saveActbyProcessus(@RequestBody Activite a, @PathVariable Long idpro){
        Processus processus=iprocessus.findOne(idpro);
        a.setProcessus(processus);
        return iactivite.save(a);
    }
    @GetMapping("/getactivitesbyprocessus/{id}")
    public Collection<Activite> getactivitesbyprocessus(@PathVariable Long id) {
        Processus processus = iprocessus.findOne(id);
        Collection<Activite> lA=processus.getActivites();
        return lA;
    }
    /*@PutMapping("/affecterProcessus/{Id}/{Idpro}")
    public Activite affecterProcessus( @PathVariable Long Id,@PathVariable Long Idpro) {
        Activite activite = iactivite.findOne(Id);
        Processus processus = iprocessus.findOne(Idpro);
        activite.setProcessus(processus);

        return iactivite.saveAndFlush(activite);
    }*/
}

