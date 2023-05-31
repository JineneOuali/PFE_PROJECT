package com.example.PFE_PROJECT.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;


@Entity
public class DocumentS {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String titre;
    private String type;
    private boolean docExiste;
    private boolean docAjour;
    private boolean docEnCnf;
    private boolean infoDispo;

    @ManyToOne
    @JoinColumn(name="ACTIVITE_ID")
    private Activite activite;

    @OneToMany(mappedBy="documentS")
    @JsonIgnore
    private Collection<Revue> revues;

    @OneToMany(mappedBy="documentS")
    @JsonIgnore
    private Collection<Liberation> liberations;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public boolean isDocExiste() {
        return docExiste;
    }

    public void setDocExiste(boolean docExiste) {
        this.docExiste = docExiste;
    }

    public boolean isDocAjour() {
        return docAjour;
    }

    public void setDocAjour(boolean docAjour) {
        this.docAjour = docAjour;
    }

    public boolean isDocEnCnf() {
        return docEnCnf;
    }

    public void setDocEnCnf(boolean docEnCnf) {
        this.docEnCnf = docEnCnf;
    }

    public boolean isInfoDispo() {
        return infoDispo;
    }

    public void setInfoDispo(boolean infoDispo) {
        this.infoDispo = infoDispo;
    }

    public Collection<Revue> getRevues() {
        return revues;
    }

    public void setRevues(Collection<Revue> revues) {
        this.revues = revues;
    }

    public Collection<Liberation> getLiberations() {
        return liberations;
    }

    public void setLiberations(Collection<Liberation> liberations) {
        this.liberations = liberations;
    }

    @JsonIgnore
    public Activite getActivite() {
        return activite;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
    }
}
