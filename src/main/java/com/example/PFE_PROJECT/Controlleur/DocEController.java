package com.example.PFE_PROJECT.Controlleur;


import com.example.PFE_PROJECT.dao.*;
import com.example.PFE_PROJECT.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/documentsE")
public class DocEController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private IDocumentE idocumentE;
    @Autowired
    private IActivite iactivite;
    @GetMapping("/all")
    public List<DocumentE> listDocumentE() {
        return idocumentE.findAll();
    }
    @PutMapping("/update/{Id}")
    public DocumentE update(@RequestBody DocumentE documentE, @PathVariable Long Id) {
        documentE.setId(Id);
        return idocumentE.saveAndFlush(documentE);
    }
    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteDocumentE (@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            idocumentE.delete(Id);
            message.put("etat","document d'entrée deleted");
            return message;
        }

        catch (Exception e) {
            message.put("etat","document d'entrée not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public DocumentE getonedocumentE(@PathVariable Long id) {
        return idocumentE.findOne(id);
    }
    @PostMapping("/save")
    public DocumentE saveDocument(@RequestBody DocumentE d){
        return idocumentE.save(d);
    }
    @PostMapping("/save/{idact}")
    public DocumentE saveDocEbyActivite(@RequestBody DocumentE d, @PathVariable Long idact){
        Activite activite=iactivite.findOne(idact);
        d.setActivite(activite);
        return idocumentE.save(d);
    }

}
