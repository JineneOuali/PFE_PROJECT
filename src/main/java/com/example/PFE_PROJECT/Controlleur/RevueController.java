package com.example.PFE_PROJECT.Controlleur;

import com.example.PFE_PROJECT.dao.*;
import com.example.PFE_PROJECT.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.sql.Array;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/revues")
public class RevueController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private IRevue irevue;
    @Autowired
    private IRevueHistory irevueHistory;
    @Autowired
    private ICoordinateur icoordinateur;
    @Autowired
    private IDocumentS idocumentS;
    @Autowired
    private IResponsableP iResponsableP;
    @Autowired
    private IProjet iProjet;
    @GetMapping("/all")
    public List<Revue> listRevue() {
        return irevue.findAll();
    }
    @PutMapping("/update/{Id}")
    public Revue update(@RequestBody Revue revue, @PathVariable Long Id) {
        revue.setId(Id);
        return irevue.saveAndFlush(revue);
    }
/*@PutMapping("/update/{Id}")
public Revue update(@RequestBody Revue revue, @PathVariable Long Id) {
        Revue optionalReview = irevue.findOne(Id);
        String oldStartTime = optionalReview.getStartTime();
        // update the review in the revue table
        optionalReview.setStartTime(revue.getStartTime());
        irevue.saveAndFlush(optionalReview);

        // create new ReviewHistory object and set fields
        RevueHistory history = new RevueHistory();
        history.setRevues(optionalReview);
        history.setStartTime(revue.getStartTime());
        history.setDescription(optionalReview.getDescription());
        history.setTitre(optionalReview.getTitre());
        history.setStatus(optionalReview.getStatus());
        history.setTempsPasse(optionalReview.getTempsPasse());



        // save ReviewHistory object to database
        irevueHistory.saveAndFlush(history);
        return optionalReview;
}*/


    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteRevue (@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            irevue.delete(Id);
            message.put("etat","revue deleted");
            return message;
        }

        catch (Exception e) {
            message.put("etat","revue not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public Revue getonerevue(@PathVariable Long id) {
        return irevue.findOne(id);
    }
    @PostMapping("/save")
    public Revue saveRevue(@RequestBody Revue r){
        return irevue.save(r);
    }
    @PostMapping("/save/{iddocS}")
    public Revue saveRevuebyDocS(@RequestBody Revue r, @PathVariable Long iddocS){
        DocumentS docS=idocumentS.findOne(iddocS);
        r.setDocumentS(docS);
        r.setStatus("todo");
        return irevue.save(r);
    }
    @PutMapping("/revuedone/{Id}")
    public Revue revuedone(@RequestBody Revue revue, @PathVariable Long Id) {
        Revue l = irevue.findOne(Id);
        l.setStatus("done");
        l.setTempsPasse(revue.getTempsPasse());
        int tempsPasse=0;
        int tempsPasseH=0;
        if (("done".equals(l.getStatus())) && !("".equals(l.getTempsPasse())) ){
            String tempsPasseString = l.getTempsPasse();
            int revueTempsPasse = convertStringToMinutes(tempsPasseString);
            System.out.println("Revue " + l.getId() + " : " + revueTempsPasse + " minutes");
            tempsPasse += revueTempsPasse;
            for (RevueHistory historique : l.getRevuesHistory()) {
                String historiqueTempsPasseString = historique.getTempsPasse();
                if ("notok".equals(historique.getStatus())) {
                    int historiqueTempsPasse = convertStringToMinutes(historiqueTempsPasseString);
                    System.out.println("RevueH " + l.getId() + " : " + historiqueTempsPasse + " minutes");
                    tempsPasseH += historiqueTempsPasse;
                }
            }
        }
        l.setTempsPasseTotal(convertMinutesToString(tempsPasse+tempsPasseH));
        Set<Projet> projets = l.getDocumentS().getActivite().getProcessus().getProjets();
        Projet project = projets.iterator().next();


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
        return irevue.saveAndFlush(l);
    }
    private int convertStringToMinutes(String timeString) {
        String[] parts = timeString.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }
    private String convertMinutesToString(int minutes) {
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;
        return String.format("%02d:%02d", hours, remainingMinutes);
    }
    @PutMapping("/revuenotok/{Id}")
    public Revue revuenotok(@RequestBody Map<String, String> requestBody,@PathVariable Long Id) {

        Revue optionalRevue = irevue.findOne(Id);
        optionalRevue.setStatus("notok");
        Set<Projet> projets = optionalRevue.getDocumentS().getActivite().getProcessus().getProjets();
        Projet project = projets.iterator().next();


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

        RevueHistory history = new RevueHistory();
        history.setRevues(optionalRevue);
        history.setStartTime(optionalRevue.getStartTime());
        history.setDescription(optionalRevue.getDescription());
        history.setTitre(optionalRevue.getTitre());
        history.setStatus("notok");
        history.setTempsPasse(requestBody.get("tempsPasse"));
        history.setCause(requestBody.get("cause"));
        irevueHistory.saveAndFlush(history);
        return optionalRevue;
    }
    @PutMapping("/planifierRevue/{Id}")
    public Revue planifierRevue(@RequestBody Revue revue,@PathVariable Long Id) {

        Revue optionalRevue = irevue.findOne(Id);
        optionalRevue.setStartTime(revue.getStartTime());
        optionalRevue.setStatus("todo");
        Set<Projet> projets = optionalRevue.getDocumentS().getActivite().getProcessus().getProjets();
        Projet project = projets.iterator().next();


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

        irevue.saveAndFlush(optionalRevue);
        return optionalRevue;
    }
    @GetMapping("/getrevuebycoordinatur/{id}")
    public Set<Revue> getrevuebycoordinatur(@PathVariable Long id) {
        Coordinateur c = icoordinateur.findOne(id);
        Set<Projet> listProjet = c.getProjets();
        Set<Revue> setRevue = new HashSet<>();
        for (Projet lp : listProjet) {
            Set<Processus> listProcessus = lp.getProcessuses();
            for (Processus lproc : listProcessus) {
                Collection<Activite> listActivite = lproc.getActivites();
                for (Activite lact : listActivite) {
                    Collection<DocumentS> listDocS = lact.getDocumentsS();
                    for (DocumentS ldocs : listDocS) {
                        Collection<Revue> listRevue = ldocs.getRevues();
                        setRevue.addAll(listRevue);
                    }
                }
            }
        }
        return setRevue;
    }

    @GetMapping("/getrevuebyresP/{id}")
    public Set<Revue> getrevuebyresP(@PathVariable Long id) {
        ResponsableP c = iResponsableP.findOne(id);
        Set<Projet> listProjet = c.getProjets();
        Set<Revue> setRevue = new HashSet<>();
        for (Projet lp : listProjet) {
            Set<Processus> listProcessus = lp.getProcessuses();
            for (Processus lproc : listProcessus) {
                Collection<Activite> listActivite = lproc.getActivites();
                for (Activite lact : listActivite) {
                    Collection<DocumentS> listDocS = lact.getDocumentsS();
                    for (DocumentS ldocs : listDocS) {
                        Collection<Revue> listRevue = ldocs.getRevues();
                        setRevue.addAll(listRevue);
                    }
                }
            }
        }
        return setRevue;
    }

    @GetMapping("/getrevuebyproject")
    public Set<Revue> getrevuebyproject(@RequestParam(value = "projectId", required = false) Long projectId) {
        if (projectId == null) {
            return new HashSet<>();
        }
        Projet project = iProjet.findOne(projectId);
        Set<Revue> setRevue = new HashSet<>();
        Set<Processus> listProcessus = project.getProcessuses();
        for (Processus lproc : listProcessus) {
            Collection<Activite> listActivite = lproc.getActivites();
            for (Activite lact : listActivite) {
                Collection<DocumentS> listDocS = lact.getDocumentsS();
                for (DocumentS ldocs : listDocS) {
                    Collection<Revue> listRevue = ldocs.getRevues();
                    setRevue.addAll(listRevue);
                }
            }
        }
        return setRevue;
    }

    @GetMapping("/getrevuebydocumentS/{id}")
    public Collection<Revue> getrevuebydocumentS(@PathVariable Long id) {
        DocumentS docS = idocumentS.findOne(id);
        Collection<Revue> lR=docS.getRevues();
        return lR;
    }

}
