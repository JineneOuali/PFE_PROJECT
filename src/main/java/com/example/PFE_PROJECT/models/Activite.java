package com.example.PFE_PROJECT.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@Data
@Entity
public class Activite implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String titre;

    @OneToMany(mappedBy="activite")
    private Collection<DocumentS> documentsS;

    @OneToMany(mappedBy="activite")
    private Collection<DocumentE> documentsE;

    @ManyToOne
    @JoinColumn(name="PROCESSUS_ID")
    @JsonIgnore
    private Processus processus;


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


    public Collection<DocumentS> getDocumentsS() {
        return documentsS;
    }

    public void setDocumentsS(Collection<DocumentS> documentsS) {
        this.documentsS = documentsS;
    }


    public Collection<DocumentE> getDocumentsE() {
        return documentsE;
    }

    public void setDocumentsE(Collection<DocumentE> documentsE) {
        this.documentsE = documentsE;
    }


    public Processus getProcessus() {
        return processus;
    }

    public void setProcessus(Processus processus) {
        this.processus = processus;
    }
}
