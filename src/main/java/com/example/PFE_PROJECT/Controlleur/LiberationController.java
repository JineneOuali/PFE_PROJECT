package com.example.PFE_PROJECT.Controlleur;
import com.example.PFE_PROJECT.dao.*;
import com.example.PFE_PROJECT.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/liberations")
public class LiberationController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private ILiberation iliberation;
    @Autowired
    private ILiberationHistory iliberationHistory;
    @Autowired
    private IDocumentS idocumentS;
    @Autowired
    private ICoordinateur icoordinateur;
    @Autowired
    private IResponsableP iResponsableP;
    @Autowired
    private IProjet iProjet;
    @GetMapping("/all")
    public List<Liberation> listLiberation() {
        return iliberation.findAll();
    }
    @PutMapping("/update/{Id}")
    public Liberation update(@RequestBody Liberation liberation, @PathVariable Long Id) {
        liberation.setId(Id);
        return iliberation.saveAndFlush(liberation);
    }
    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteLiberation (@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            iliberation.delete(Id);
            message.put("etat","Liberation deleted");
            return message;
        }

        catch (Exception e) {
            message.put("etat","Liberation not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public Liberation getoneliberation(@PathVariable Long id) {
        return iliberation.findOne(id);
    }
    @PostMapping("/save")
    public Liberation saveLiberation(@RequestBody Liberation l){
        return iliberation.save(l);
    }
    @PostMapping("/save/{iddocS}")
    public Liberation saveLiberationbyDocS(@RequestBody Liberation l, @PathVariable Long iddocS){
        DocumentS docS=idocumentS.findOne(iddocS);
        l.setDocumentS(docS);
        l.setStatus("todo");
        return iliberation.save(l);
    }
    @PutMapping("/liberationdone/{Id}")
    public Liberation liberationdone(@RequestBody Liberation liberation, @PathVariable Long Id) {
        Liberation l = iliberation.findOne(Id);
        l.setStatus("done");
        l.setTempsPasse(liberation.getTempsPasse());
        int tempsPasse=0;
        int tempsPasseH=0;
        if (("done".equals(l.getStatus())) && !("".equals(l.getTempsPasse())) ){
            String tempsPasseString = l.getTempsPasse();
            int liberationTempsPasse = convertStringToMinutes(tempsPasseString);
            System.out.println("Revue " + l.getId() + " : " + liberationTempsPasse + " minutes");
            tempsPasse += liberationTempsPasse;
            for (LiberationHistory historique : l.getLiberationsHistory()) {
                String historiqueTempsPasseString = historique.getTempsPasse();
                if ("notok".equals(historique.getStatus())) {
                    int historiqueTempsPasse = convertStringToMinutes(historiqueTempsPasseString);
                    System.out.println("LiberationH " + l.getId() + " : " + historiqueTempsPasse + " minutes");
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
        return iliberation.saveAndFlush(l);
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
    @PutMapping("/liberationnotok/{Id}")
    public Liberation liberationnotok(@RequestBody Map<String, String> requestBody,@PathVariable Long Id) {

        Liberation optionalLiberation = iliberation.findOne(Id);
        optionalLiberation.setStatus("notok");
        Set<Projet> projets = optionalLiberation.getDocumentS().getActivite().getProcessus().getProjets();
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

        LiberationHistory history = new LiberationHistory();
        history.setLiberations(optionalLiberation);
        history.setStartTime(optionalLiberation.getStartTime());
        history.setDescription(optionalLiberation.getDescription());
        history.setTitre(optionalLiberation.getTitre());
        history.setStatus("notok");
        history.setTempsPasse(requestBody.get("tempsPasse"));
        history.setCause(requestBody.get("cause"));
        iliberationHistory.saveAndFlush(history);
        return optionalLiberation;
    }
    @PutMapping("/planifierLiberation/{Id}")
    public Liberation planifierLiberation(@RequestBody Liberation liberation,@PathVariable Long Id) {

        Liberation optionalLiberation = iliberation.findOne(Id);
        optionalLiberation.setStartTime(liberation.getStartTime());
        optionalLiberation.setStatus("todo");
        Set<Projet> projets = optionalLiberation.getDocumentS().getActivite().getProcessus().getProjets();
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

        iliberation.saveAndFlush(optionalLiberation);
        return optionalLiberation;
    }
    @GetMapping("/getliberationbydocumentS/{id}")
    public Collection<Liberation> getliberationbydocumentS(@PathVariable Long id) {
        DocumentS docS = idocumentS.findOne(id);
        Collection<Liberation> lLiberation=docS.getLiberations();
        return lLiberation;
    }
    @GetMapping("/getliberationbycoordinatur/{id}")
    public Set<Liberation> getliberationbycoordinatur(@PathVariable Long id) {
        Coordinateur c = icoordinateur.findOne(id);
        Set<Projet> listProjet = c.getProjets();
        Set<Liberation> setLiberation = new HashSet<>();
        for (Projet lp : listProjet) {
            Set<Processus> listProcessus = lp.getProcessuses();
            for (Processus lproc : listProcessus) {
                Collection<Activite> listActivite = lproc.getActivites();
                for (Activite lact : listActivite) {
                    Collection<DocumentS> listDocS = lact.getDocumentsS();
                    for (DocumentS ldocs : listDocS) {
                        Collection<Liberation> listLiberation = ldocs.getLiberations();
                        setLiberation.addAll(listLiberation);
                    }
                }
            }
        }
        return setLiberation;
    }
    @GetMapping("/getliberationbyproject")
    public Set<Liberation> getliberationbyproject(@RequestParam(value = "projectId", required = false) Long projectId) {
        if (projectId == null) {
            return new HashSet<>();
        }
        Projet project = iProjet.findOne(projectId);
        Set<Liberation> setLiberation = new HashSet<>();
        Set<Processus> listProcessus = project.getProcessuses();
        for (Processus lproc : listProcessus) {
            Collection<Activite> listActivite = lproc.getActivites();
            for (Activite lact : listActivite) {
                Collection<DocumentS> listDocS = lact.getDocumentsS();
                for (DocumentS ldocs : listDocS) {
                    Collection<Liberation> listLiberation = ldocs.getLiberations();
                    setLiberation.addAll(listLiberation);
                }
            }
        }
        return setLiberation;
    }
    @GetMapping("/getliberationbyresP/{id}")
    public Set<Liberation> getliberationbyresP(@PathVariable Long id) {
        ResponsableP c = iResponsableP.findOne(id);
        Set<Projet> listProjet = c.getProjets();
        Set<Liberation> setLiberation = new HashSet<>();
        for (Projet lp : listProjet) {
            Set<Processus> listProcessus = lp.getProcessuses();
            for (Processus lproc : listProcessus) {
                Collection<Activite> listActivite = lproc.getActivites();
                for (Activite lact : listActivite) {
                    Collection<DocumentS> listDocS = lact.getDocumentsS();
                    for (DocumentS ldocs : listDocS) {
                        Collection<Liberation> listLiberation = ldocs.getLiberations();
                        setLiberation.addAll(listLiberation);
                    }
                }
            }
        }
        return setLiberation;
    }
    @PostMapping("/getliberationsRefus")
    public Map<String, Integer> getliberationsRefus(@RequestBody Map<String, Long> requestBody) throws ParseException {
        Map<String, Integer> liberationsRefus = new HashMap<>();
        Long projectId = requestBody.get("projectId");
        Projet project = iProjet.findOne(projectId);
        Set<Processus> listProcessus = project.getProcessuses();
        for (Processus lproc : listProcessus) {
            Collection<Activite> listActivite = lproc.getActivites();
            for (Activite lact : listActivite) {
                Collection<DocumentS> listDocS = lact.getDocumentsS();
                for (DocumentS ldocs : listDocS) {
                    Collection<Liberation> listLiberation = ldocs.getLiberations();
                    for (Liberation llib : listLiberation) {
                        if (llib.getStatus().equals("done")){
                            Collection<LiberationHistory> listLiberationH = llib.getLiberationsHistory();
                            int count = 0;
                            for (LiberationHistory llibH : listLiberationH) {
                                if (llibH.getStatus().equals("notok")){
                                    count++;
                                }
                            }
                            liberationsRefus.put(llib.getTitre(), count);
                        }
                    }
                }
            }
        }
        return liberationsRefus;
    }

}
