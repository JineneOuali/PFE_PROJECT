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
@RequestMapping(value = "/users/documentsS")
public class DocSController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private IDocumentS idocumentS;
    @Autowired
    private IActivite iactivite;
    @GetMapping("/all")
    public List<DocumentS> listDocumentS() {
        return idocumentS.findAll();
    }
    @PutMapping("/update/{Id}")
    public DocumentS update(@RequestBody DocumentS documentS, @PathVariable Long Id) {
        documentS.setId(Id);
        return idocumentS.saveAndFlush(documentS);
    }
    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteDocumentS (@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            idocumentS.delete(Id);
            message.put("etat","document de sortie deleted");
            return message;
        }
        catch (Exception e) {
            message.put("etat","document de sortie not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public DocumentS getonedocumentS(@PathVariable Long id) {
        return idocumentS.findOne(id);
    }
    @PostMapping("/save")
    public DocumentS saveDocument(@RequestBody DocumentS d){
        return idocumentS.save(d);
    }
    @PostMapping("/save/{idact}")
    public DocumentS saveDocEbyActivite(@RequestBody DocumentS d, @PathVariable Long idact){
        Activite activite=iactivite.findOne(idact);
        d.setActivite(activite);
        return idocumentS.save(d);
    }
    @PutMapping("/activerExiste/{Id}")
    public DocumentS activerExiste(@PathVariable Long Id) {
        DocumentS d = idocumentS.findOne(Id);
        d.setDocExiste(true);
        return idocumentS.saveAndFlush(d);
    }
    @PutMapping("/desactiverExiste/{Id}")
    public DocumentS desactiverExiste(@PathVariable Long Id) {
        DocumentS d = idocumentS.findOne(Id);
        d.setDocExiste(false);
        return idocumentS.saveAndFlush(d);
    }
    @PutMapping("/activerAJour/{Id}")
    public DocumentS activerAJour(@PathVariable Long Id) {
        DocumentS d = idocumentS.findOne(Id);
        d.setDocAjour(true);
        return idocumentS.saveAndFlush(d);
    }
    @PutMapping("/desactiverAJour/{Id}")
    public DocumentS desactiverAJour(@PathVariable Long Id) {
        DocumentS d = idocumentS.findOne(Id);
        d.setDocAjour(false);
        return idocumentS.saveAndFlush(d);
    }
    @PutMapping("/activerEnCnf/{Id}")
    public DocumentS activerEnCnf(@PathVariable Long Id) {
        DocumentS d = idocumentS.findOne(Id);
        d.setDocEnCnf(true);
        return idocumentS.saveAndFlush(d);
    }
    @PutMapping("/desactiverEnCnf/{Id}")
    public DocumentS desactiverEnCnf(@PathVariable Long Id) {
        DocumentS d = idocumentS.findOne(Id);
        d.setDocEnCnf(false);
        return idocumentS.saveAndFlush(d);
    }
    @PutMapping("/activerInfoDispo/{Id}")
    public DocumentS activerInfoDispo(@PathVariable Long Id) {
        DocumentS d = idocumentS.findOne(Id);
        d.setInfoDispo(true);
        return idocumentS.saveAndFlush(d);
    }
    @PutMapping("/desactiverInfoDispo/{Id}")
    public DocumentS desactiverInfoDispo(@PathVariable Long Id) {
        DocumentS d = idocumentS.findOne(Id);
        d.setInfoDispo(false);
        return idocumentS.saveAndFlush(d);
    }

}
