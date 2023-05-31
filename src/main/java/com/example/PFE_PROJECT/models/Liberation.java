package com.example.PFE_PROJECT.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
public class Liberation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String titre;
    private String status;
    private String StartTime;
    private String tempsPasse;
    private String tempsPasseTotal;
    private String statusFinal;
    private String causeRefus;
    private String priority;
    @ManyToOne
    @JoinColumn(name="DOCUMENTS_ID")
    private DocumentS documentS;

    @OneToMany(mappedBy="liberations")
    private Collection<LiberationHistory> liberationsHistory;

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

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getTempsPasse() {
        return tempsPasse;
    }

    public void setTempsPasse(String tempsPasse) {
        this.tempsPasse = tempsPasse;
    }

    public String getTempsPasseTotal() {
        return tempsPasseTotal;
    }

    public void setTempsPasseTotal(String tempsPasseTotal) {
        this.tempsPasseTotal = tempsPasseTotal;
    }

    public String getStatusFinal() {
        return statusFinal;
    }

    public void setStatusFinal(String statusFinal) {
        this.statusFinal = statusFinal;
    }

    public String getCauseRefus() {
        return causeRefus;
    }

    public void setCauseRefus(String causeRefus) {
        this.causeRefus = causeRefus;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public DocumentS getDocumentS() {
        return documentS;
    }

    public void setDocumentS(DocumentS documentS) {
        this.documentS = documentS;
    }

    public Collection<LiberationHistory> getLiberationsHistory() {
        return liberationsHistory;
    }

    public void setLiberationsHistory(Collection<LiberationHistory> liberationsHistory) {
        this.liberationsHistory = liberationsHistory;
    }
}
