package com.example.PFE_PROJECT.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Kpi implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String description;
    private String frequence;
    private String objectif;
    private String type;

    @OneToMany(mappedBy="kpi")
    private Collection<KpiVal> kpiVals;

    @ManyToMany(mappedBy = "kpis")
    @JsonIgnore
    Set<Projet> projets;

    /*@OneToMany(mappedBy = "kpi")
    @JsonIgnore
    private Set<ProjetKpi> projetsKpi = new HashSet<>();*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrequence() {
        return frequence;
    }

    public void setFrequence(String frequence) {
        this.frequence = frequence;
    }

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Collection<KpiVal> getKpiVals() {
        return kpiVals;
    }

    public void setKpiVals(Collection<KpiVal> kpiVals) {
        this.kpiVals = kpiVals;
    }

    public Set<Projet> getProjets() {
        return projets;
    }

    public void setProjets(Set<Projet> projets) {
        this.projets = projets;
    }
}
