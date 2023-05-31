package com.example.PFE_PROJECT.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Projet implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String titre;
    private String status;
    private String start;
    private String end;
    @ManyToOne
    @JoinColumn(name="DIRECTION_ID")
    private Direction directions;

    @ManyToOne
    @JoinColumn(name="RESPONSABLEPROJET_ID")
    private ResponsableP responsablesP;

    @OneToMany(mappedBy = "projet")
    @JsonIgnore
    private Set<KpiVal> kpisVal = new HashSet<>();
    /*@OneToMany(mappedBy = "projet")
    @JsonIgnore
    private Set<ProjetKpi> projetsKpi = new HashSet<>();*/

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "processus_projets",
            joinColumns = @JoinColumn(name = "projet_id"),
            inverseJoinColumns = @JoinColumn(name = "processus_id"))
    private Set<Processus> processuses=new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "projets_coordinateurs",
            joinColumns = @JoinColumn(name = "projet_id"),
            inverseJoinColumns = @JoinColumn(name = "coordinateur_id"))
    private Set<Coordinateur> coordinateurs=new HashSet<>();


    @OneToMany(mappedBy="projet")
    @JsonIgnore
    private Collection<Alert> alerts;

    @OneToMany(mappedBy="projet")
    @JsonIgnore
    private Collection<Audit> audits;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "kpi_projets",
            joinColumns = @JoinColumn(name = "projet_id"),
            inverseJoinColumns = @JoinColumn(name = "kpi_id"))
    private Set<Kpi> kpis=new HashSet<>();

    public void updateStatus(Collection<Audit> audits, Collection<Alert> alerts, Collection<Revue> revues, Collection<Liberation> liberations) {
        String auditsCompleted = "";
        String revuesCompleted = "";
        String liberationsCompleted = "";
        boolean hasAlerts = !alerts.isEmpty();

        for (Audit audit : audits) {
            if (audit.getStatus().equals("done")) {
                auditsCompleted = "done";
            }
            else {
                auditsCompleted = "todo";
            }
        }

        for (Revue revue : revues) {
            if (revue.getStatus().equals("done")) {
                revuesCompleted = "done";
            }
            else if (revue.getStatus().equals("todo")) {
                auditsCompleted = "todo";
            }
            else {
                auditsCompleted = "notok";
            }
        }

        for (Liberation liberation : liberations) {
            if (liberation.getStatus().equals("done")) {
                liberationsCompleted = "done";
            }
            else if (liberation.getStatus().equals("todo")) {
                liberationsCompleted = "todo";
            }
            else {
                liberationsCompleted = "notok";
            }
        }



        String projectStatus;
        if (revuesCompleted=="done" && auditsCompleted=="done" && !hasAlerts && liberationsCompleted=="done") {
            projectStatus = "Completed";
        } else if (hasAlerts || revuesCompleted=="notok" || liberationsCompleted=="notok") {
            projectStatus = "At risk";
        } else if (revuesCompleted=="todo" || liberationsCompleted=="todo" || auditsCompleted=="todo"){
            projectStatus = "In progress";
        }
        else {
            projectStatus = "In progress";
        }


        // Set the overall project status
        this.setStatus(projectStatus);
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Set<Processus> getProcessuses() {
        return processuses;
    }

    public void setProcessuses(Set<Processus> processuses) {
        this.processuses = processuses;
    }

    public Set<Coordinateur> getCoordinateurs() {
        return coordinateurs;
    }

    public void setCoordinateurs(Set<Coordinateur> coordinateurs) {
        this.coordinateurs = coordinateurs;
    }

    public Direction getDirections() {
        return directions;
    }

    public ResponsableP getResponsablesP() {
        return responsablesP;
    }

    public void setResponsablesP(ResponsableP responsablesP) {
        this.responsablesP = responsablesP;
    }

    public void setDirections(Direction directions) {
        this.directions = directions;
    }

    public Collection<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(Collection<Alert> alerts) {
        this.alerts = alerts;
    }

    public Collection<Audit> getAudits() {
        return audits;
    }

    public void setAudits(Collection<Audit> audits) {
        this.audits = audits;
       // updateStatus(this.audits);
    }

    public Set<Kpi> getKpis() {
        return kpis;
    }

    public void setKpis(Set<Kpi> kpis) {
        this.kpis = kpis;
    }

    public Set<KpiVal> getKpisVal() {
        return kpisVal;
    }

    public void setKpisVal(Set<KpiVal> kpisVal) {
        this.kpisVal = kpisVal;
    }
}

