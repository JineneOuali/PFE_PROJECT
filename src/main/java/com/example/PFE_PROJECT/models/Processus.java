package com.example.PFE_PROJECT.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity

public class Processus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String titre;

    @OneToMany(mappedBy="processus")
    private Collection<Activite> activites;

    @ManyToMany(mappedBy = "processuses")
    @JsonIgnore
    Set<Projet> projets;

    /*@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "activites_processuses",
            joinColumns = @JoinColumn(name = "processus_id"),
            inverseJoinColumns = @JoinColumn(name = "activites_id"))
    private Set<Activite> activites=new HashSet<>();*/


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

    public Collection<Activite> getActivites() {
        return activites;
    }

    public void setActivites(Collection<Activite> activites) {
        this.activites = activites;
    }

    public Set<Projet> getProjets() {
        return projets;
    }

    public void setProjets(Set<Projet> projets) {
        this.projets = projets;
    }
}
