package com.example.PFE_PROJECT.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Coordinateur extends User{

    private String adresse;
    private String telephone;

    @ManyToOne
    @JoinColumn(name="DIRECTION_ID")
    private  Direction direction;

    @ManyToMany(mappedBy = "coordinateurs")
    @JsonIgnore
    Set<Projet> projets;

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Set<Projet> getProjets() {
        return projets;
    }

    public void setProjets(Set<Projet> projets) {
        this.projets = projets;
    }
}
