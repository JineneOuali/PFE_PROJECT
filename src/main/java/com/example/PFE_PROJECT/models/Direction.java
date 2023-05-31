package com.example.PFE_PROJECT.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@Entity
public class Direction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String titre;

    @OneToMany(mappedBy="direction")
    @JsonIgnore
    private Collection<Coordinateur> coordinateurs;

    @OneToMany(mappedBy="directions", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JsonIgnore
    private Set<Projet> projets;

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

    public Collection<Coordinateur> getCoordinateurs() {
        return coordinateurs;
    }

    public void setCoordinateurs(Collection<Coordinateur> coordinateurs) {
        this.coordinateurs = coordinateurs;
    }

    public Set<Projet> getProjets() {
        return projets;
    }

    public void setProjets(Set<Projet> projets) {
        this.projets = projets;
    }
}
