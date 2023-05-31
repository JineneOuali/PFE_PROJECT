package com.example.PFE_PROJECT.Controlleur;
import com.example.PFE_PROJECT.dao.*;
import com.example.PFE_PROJECT.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/audits")
public class AuditController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private IAudit iaudit;
    @Autowired
    private IProjet iprojet;
    @GetMapping("/all")
    public List<Audit> listAudit() {
        return iaudit.findAll();
    }
    @PutMapping("/update/{Id}")
    public Audit update(@RequestBody Audit audit, @PathVariable Long Id) {
        audit.setId(Id);
        return iaudit.saveAndFlush(audit);
    }
/*    @PutMapping("/{auditId}/updateStatus")
    public void updateAuditStatus(@PathVariable Long auditId, @RequestBody Map<String, String> request) {
        Audit audit = iaudit.findOne(auditId);

        String status = request.get("status");
        audit.setStatus(status);
        iaudit.save(audit);

        Projet project = audit.getProjet();
        Collection<Audit> audits = project.getAudits();
        Collection<Alert> alerts = project.getAlerts();
        project.updateStatus(audits,alerts);
        iprojet.save(project);
    }*/

/*    @PutMapping("/{auditId}/updateStatus")
    public void updateAuditStatus(@PathVariable Long auditId, @RequestBody Audit audit) {
        Audit existingAudit = iaudit.findOne(auditId);

        existingAudit.setStatus(audit.getStatus());
        iaudit.save(existingAudit);

        // update the project status based on the audit status
        Projet project = existingAudit.getProjet();
        Collection<Audit> audits = project.getAudits();
        project.updateStatus(audits);
        iprojet.save(project);
    }*/
    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteAudit(@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            iaudit.delete(Id);
            message.put("etat","Audit deleted");
            return message;
        }

        catch (Exception e) {
            message.put("etat","Audit not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public Audit getoneAudit(@PathVariable Long id) {
        return iaudit.findOne(id);
    }
    @PostMapping("/save")
    public Audit saveAudit(@RequestBody Audit au){
        return iaudit.save(au);
    }

    @PostMapping("/save/{idprojet}")
    public Audit saveauditbyProjet(@RequestBody Audit au, @PathVariable Long idprojet){
        Projet projet=iprojet.findOne(idprojet);
        au.setProjet(projet);
        au.setStatus("todo");
        Projet project = au.getProjet();
        Collection<Audit> audits = project.getAudits();
        Collection<Alert> alerts = project.getAlerts();
        Collection<Revue> revues = new ArrayList<>();
        for (Processus processus : project.getProcessuses()) {
            for (Activite activite : processus.getActivites()) {
                for (DocumentS document : activite.getDocumentsS()) {
                    document.getRevues();
                }
            }
        }
        Collection<Liberation> liberations = new ArrayList<>();
        for (Processus processus : project.getProcessuses()) {
            for (Activite activite : processus.getActivites()) {
                for (DocumentS document : activite.getDocumentsS()) {
                    document.getLiberations();
                }
            }
        }
        project.updateStatus(audits,alerts,revues, liberations);
        iprojet.save(project);
        return iaudit.save(au);
    }
    @PutMapping("/auditdone/{Id}")
    public Audit Auditdone(@PathVariable Long Id) {
        Audit au = iaudit.findOne(Id);
        au.setStatus("done");
        Projet project = au.getProjet();
        Collection<Audit> audits = project.getAudits();
        Collection<Alert> alerts = project.getAlerts();
        Collection<Revue> revues = new ArrayList<>();
        for (Processus processus : project.getProcessuses()) {
            for (Activite activite : processus.getActivites()) {
                for (DocumentS document : activite.getDocumentsS()) {
                    document.getRevues();
                }
            }
        }
        Collection<Liberation> liberations = new ArrayList<>();
        for (Processus processus : project.getProcessuses()) {
            for (Activite activite : processus.getActivites()) {
                for (DocumentS document : activite.getDocumentsS()) {
                    document.getLiberations();
                }
            }
        }
        project.updateStatus(audits,alerts,revues, liberations);
        iprojet.save(project);
        return iaudit.saveAndFlush(au);
    }
    @GetMapping("/getauditsbyprojet/{id}")
    public Collection<Audit> getauditsbyprojet(@PathVariable Long id) {
        Projet projet = iprojet.findOne(id);
        Collection<Audit> lA=projet.getAudits();
        return lA;
    }
}
