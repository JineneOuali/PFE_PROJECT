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
@RequestMapping(value = "/users/projet")
public class ProjetController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private IProjet iProjet;
    @Autowired
    private ICoordinateur icoordinateur;

    @Autowired
    private IAudit iaudit;
    @Autowired
    private IRevue irevue;
    @Autowired
    private IResponsableP iResponsableP;

    @GetMapping("/all")
    public List<Projet> listProjet() {
        return iProjet.findAll();
    }

    @PutMapping("/update/{Id}")
    public Projet update(@RequestBody Projet projet, @PathVariable Long Id) {
        projet.setId(Id);
        return iProjet.saveAndFlush(projet);
    }
/*    @PutMapping("/{projectId}/updateStatus")
    public void updateProjectStatus(@PathVariable Long projectId) {
        Projet projet = iProjet.findOne(projectId);

        Collection<Audit> audits = iaudit.findByProjetId(projectId);
        projet.setAudits(audits); // update the audits of the project
        projet.updateStatus(audits); // update the status of the project based on the audits

        iProjet.save(projet);
    }*/


    @DeleteMapping("/delete/{Id}")
    public HashMap<String, String> deleteProjet(@PathVariable(value = "Id") Long Id) {

        HashMap message = new HashMap();
        try {
            iProjet.delete(Id);
            message.put("etat", "Projet deleted");
            return message;
        } catch (Exception e) {
            message.put("etat", "Projet not deleted");
            return message;
        }
    }

    @GetMapping("/getone/{id}")
    public Projet getoneprojet(@PathVariable Long id) {
        return iProjet.findOne(id);
    }

    @PostMapping("/save")
    public Projet saveProjet(@RequestBody Projet p) {
        return iProjet.save(p);
    }

    @PutMapping("/affecterCoordinateur/{Id}/{Idcoo}")
    public Projet affecterCoordinateur(@PathVariable Long Id, @PathVariable Long Idcoo) {
        Projet projet = iProjet.findOne(Id);
        Coordinateur coordinateur = icoordinateur.findOne(Idcoo);
        Set<Coordinateur> coordinateurs = new HashSet<>();
        coordinateurs.add(coordinateur);
        projet.setCoordinateurs(coordinateurs);
        return iProjet.saveAndFlush(projet);
    }

    @PostMapping("/getrevuebyperiode/{id}")
    public Set<Revue> getrevuebyperiode(@PathVariable Long id, @RequestBody Map<String, String> requestBody) throws ParseException {
        ResponsableP c = iResponsableP.findOne(id);
        Set<Projet> listProjet = c.getProjets();
        Set<Revue> setRevue = new HashSet<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(requestBody.get("startDate"));
        Date endDate = sdf.parse(requestBody.get("endDate"));
        for (Projet lp : listProjet) {
            Set<Processus> listProcessus = lp.getProcessuses();
            for (Processus lproc : listProcessus) {
                Collection<Activite> listActivite = lproc.getActivites();
                for (Activite lact : listActivite) {
                    Collection<DocumentS> listDocS = lact.getDocumentsS();
                    for (DocumentS ldocs : listDocS) {
                        Collection<Revue> listRevue = ldocs.getRevues();
                        for (Revue revue : listRevue) {
                            Date revueStartDate = sdf.parse(revue.getStartTime());
                            if (revue.getStatus().equals("done") && (revueStartDate.equals(startDate) || revueStartDate.equals(endDate) || (revueStartDate.after(startDate) && revueStartDate.before(endDate)))) {
                                setRevue.add(revue);
                            }
                        }
                    }
                }
            }
        }
        return setRevue;
    }
    @PostMapping("/getliberationbyperiode/{id}")
    public Set<Liberation> getliberationbyperiode(@PathVariable Long id, @RequestBody Map<String, String> requestBody) throws ParseException {
        ResponsableP c = iResponsableP.findOne(id);
        Set<Projet> listProjet = c.getProjets();
        Set<Liberation> setLiberation = new HashSet<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate1 = sdf.parse(requestBody.get("startDate1"));
        Date endDate1 = sdf.parse(requestBody.get("endDate1"));
        for (Projet lp : listProjet) {
            Set<Processus> listProcessus = lp.getProcessuses();
            for (Processus lproc : listProcessus) {
                Collection<Activite> listActivite = lproc.getActivites();
                for (Activite lact : listActivite) {
                    Collection<DocumentS> listDocS = lact.getDocumentsS();
                    for (DocumentS ldocs : listDocS) {
                        Collection<Liberation> listLiberation = ldocs.getLiberations();
                        for (Liberation liberation : listLiberation) {
                            Date liberationStartDate = sdf.parse(liberation.getStartTime());
                            if (liberation.getStatus().equals("done") && (liberationStartDate.equals(startDate1) || liberationStartDate.equals(endDate1) || (liberationStartDate.after(startDate1) && liberationStartDate.before(endDate1)))) {
                                setLiberation.add(liberation);
                            }
                        }
                    }
                }
            }
        }
        return setLiberation;
    }
    /*@GetMapping("/gettempstotal/{id}")
    public String gettempstotal(@PathVariable Long id) throws ParseException {
        Revue revue = irevue.findOne(id);
        int tempsPasse=0;
        int tempsPasseH=0;

                            if ("done".equals(revue.getStatus())) {
                                String tempsPasseString = revue.getTempsPasse();
                                    int revueTempsPasse = convertStringToMinutes(tempsPasseString);
                                    System.out.println("Revue " + revue.getId() + " : " + revueTempsPasse + " minutes");
                                    tempsPasse += revueTempsPasse;
                                    for (RevueHistory historique : revue.getRevuesHistory()) {
                                        String historiqueTempsPasseString = historique.getTempsPasse();
                                        if ("notok".equals(historique.getStatus())) {
                                            int historiqueTempsPasse = convertStringToMinutes(historiqueTempsPasseString);
                                            System.out.println("RevueH " + revue.getId() + " : " + historiqueTempsPasse + " minutes");
                                            tempsPasseH += historiqueTempsPasse;
                                        }
                                    }

                            }


        return convertMinutesToString(tempsPasse+tempsPasseH);
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
    }*/

    /*@GetMapping("/getrevuebycoordinatur/{id}")
    public Set<Revue> getrevuebyperiode(@PathVariable Long id, @RequestBody Map<String, String> requestBody) throws ParseException {
        Coordinateur c = icoordinateur.findOne(id);
        Set<Projet> listProjet = c.getProjets();
        Set<Revue> setRevue = new HashSet<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(requestBody.get("startDate"));
        Date endDate = sdf.parse(requestBody.get("endDate"));
        for (Projet lp : listProjet) {
            Set<Processus> listProcessus = lp.getProcessuses();
            for (Processus lproc : listProcessus) {
                Collection<Activite> listActivite = lproc.getActivites();
                for (Activite lact : listActivite) {
                    Collection<DocumentS> listDocS = lact.getDocumentsS();
                    for (DocumentS ldocs : listDocS) {
                        Collection<Revue> listRevue = ldocs.getRevues();
                        for (Revue revue : listRevue) {
                            Date revueStartDate = sdf.parse(revue.getStartTime());
                            if (revue.getStatus().equals("done") && (revueStartDate.equals(startDate) || revueStartDate.equals(endDate) || (revueStartDate.after(startDate) && revueStartDate.before(endDate)))) {
                                int tempsPasse = convertStringToMinutes(revue.getTempsPasse());
                                for (RevueHistory historique : revue.getRevuesHistory()) {
                                    Date revueHStartDate = sdf.parse(historique.getStartTime());
                                    if (historique.getStatus().equals("notok")&& (revueHStartDate.equals(startDate) || revueHStartDate.equals(endDate) || (revueHStartDate.after(startDate) && revueHStartDate.before(endDate)))) {
                                        tempsPasse += convertStringToMinutes(historique.getTempsPasse());
                                    }
                                }
                                System.out.println("Revue " + revue.getId() + " : " + revue.getTempsPasse() + " minutes");
                                setRevue.add(revue);
                            }
                        }
                    }
                }
            }
        }
        return setRevue;
    }
    private int convertStringToMinutes(String timeString) {
        String[] parts = timeString.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }*/
/*    @GetMapping("/getrevuebycoordinatur/{id}")
    public List<Revue> getrevuebycoordinatur(@PathVariable Long id, @RequestBody Map<String, String> requestBody) throws ParseException {
        Coordinateur c = icoordinateur.findOne(id);
        Set<Projet> listProjet = c.getProjets();
        List<Revue> revues = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDateString = requestBody.get("startDate");
        String endDateString = requestBody.get("endDate");
        Date startDate = sdf.parse(startDateString);
        Date endDate = sdf.parse(endDateString);

        for (Projet lp : listProjet) {
            Set<Processus> listProcessus = lp.getProcessuses();
            for (Processus lproc : listProcessus) {
                Collection<Activite> listActivite = lproc.getActivites();
                for (Activite lact : listActivite) {
                    Collection<DocumentS> listDocS = lact.getDocumentsS();
                    for (DocumentS ldocs : listDocS) {
                        Collection<Revue> listRevue = ldocs.getRevues();
                        for (Revue revue : listRevue) {
                            Date revueStartDate = sdf.parse(revue.getStartTime());
                            if (revue.getStatus().equals("done") && (revueStartDate.equals(startDate) || revueStartDate.equals(endDate) || (revueStartDate.after(startDate) && revueStartDate.before(endDate)))) {
                                revues.add(revue);
                                int tempsPasse = convertStringToMinutes(revue.getTempsPasse());
                                for (RevueHistory historique : revue.getRevuesHistory()) {
                                    Date revueHStartDate = sdf.parse(historique.getStartTime());
                                    if (historique.getStatus().equals("notok")&& (revueHStartDate.equals(startDate) || revueHStartDate.equals(endDate) || (revueHStartDate.after(startDate) && revueHStartDate.before(endDate)))) {
                                        tempsPasse += convertStringToMinutes(historique.getTempsPasse());
                                    }
                                    System.out.println("Revue " + revue.getId() + " : " + tempsPasse + " minutes");
                                }
                                //revue.setTempsPasse(convertMinutesToString(tempsPasse));

                            }

                        }
                    }
                }
            }
        }
        return revues;
    }

    private int convertStringToMinutes(String timeString) {
        String[] parts = timeString.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }*/



}
