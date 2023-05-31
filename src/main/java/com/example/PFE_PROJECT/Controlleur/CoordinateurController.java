package com.example.PFE_PROJECT.Controlleur;

import com.example.PFE_PROJECT.dao.*;
import com.example.PFE_PROJECT.models.*;
import com.example.PFE_PROJECT.utils.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/coordinateurs")
public class CoordinateurController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private StorageService storage;
    @Autowired
    private IDirection idir;
    @Autowired
    private IProjet iprojet;
    @Autowired
    private ICoordinateur icoordinateur;
    @GetMapping("/all")
    public List<Coordinateur> listCoordinateur() {
        return icoordinateur.findAll();
    }
    @PutMapping("/update/{Id}")
    public Coordinateur update(@RequestBody Coordinateur coordinateur, @PathVariable Long Id) {
        coordinateur.setId(Id);
        Coordinateur c = icoordinateur.findOne(Id);
        coordinateur.setPassword(c.getPassword());
        coordinateur.setImage(c.getImage());
        coordinateur.setUsername(c.getUsername());
        coordinateur.setAdresse(c.getAdresse());

        coordinateur.setValide(true);
        coordinateur.setRole(c.getRole());




        return icoordinateur.saveAndFlush(coordinateur);
    }
    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteCoordinateur(@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            icoordinateur.delete(Id);
            message.put("etat","coordinateur deleted");
            return message;
        }

        catch (Exception e) {
            message.put("etat","coordinateur not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public Coordinateur getonecoordinateur(@PathVariable Long id) {
        return icoordinateur.findOne(id);
    }
    @PostMapping("/save")
    public Coordinateur saveCoordinateur(@RequestBody Coordinateur c){
        return icoordinateur.save(c);
    }

    String hash(String password) {


        String hashedPassword = null;
        int i = 0;
        while (i < 5) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            hashedPassword = passwordEncoder.encode(password);
            i++;
        }

        return hashedPassword;
    }

    @RequestMapping("/register")
    public ResponseEntity<?> register(Coordinateur user, @RequestParam("file") MultipartFile file )
    {



        user.setValide(false);

        user.setPassword(hash(user.getPassword()));
        storage.store(file);
        user.setImage(file.getOriginalFilename());
        user.setRole("CoordinateurQualite");

        icoordinateur.save(user);

        return ResponseEntity.ok(new UserTokenState(null, 0, user));

    }
    @PutMapping("/validerCoordinateur/{Id}")
    public Coordinateur validerCoordinateur(@PathVariable Long Id) {
        Coordinateur coordinateur = icoordinateur.findOne(Id);
        coordinateur.setValide(true);
        return icoordinateur.saveAndFlush(coordinateur);
    }
    @PutMapping("/modifimage/{id}")
    public Coordinateur modif(@RequestParam("file") MultipartFile file, @PathVariable Long id){
        Coordinateur c = icoordinateur.findOne(id);
        // c.setId(id);
        // int i = (int) new Date().getTime();
        // System.out.println("Integer : " + i);
        storage.store(file);
        c.setImage(file.getOriginalFilename());
        return icoordinateur.saveAndFlush(c) ;
    }

    @PutMapping("/updatepassword/{Id}")
    public Coordinateur modif(String newpassword, @PathVariable Long Id) {
        Coordinateur c=icoordinateur.findOne(Id);
        c.setPassword(hash(newpassword));
        return icoordinateur.saveAndFlush(c);
    }
    @PutMapping("/affecterDirection/{Id}/{Iddir}")
    public Coordinateur affecterDirection(@PathVariable Long Id, @PathVariable Long Iddir) {
        Coordinateur coordinateur = icoordinateur.findOne(Id);
        Direction direction = idir.findOne(Iddir);
        coordinateur.setDirection(direction);
        return icoordinateur.saveAndFlush(coordinateur);
    }
    @GetMapping("/countCoordinatorsByDirection")
    public List<Object[]> countCoordinatorsByDirection() {
        return icoordinateur.countCoordinatorsByDirection();
    }


/*
@PutMapping("/affecterDirectionEtProjets/{coordinateurId}/{directionId}")
public Coordinateur affecterDirectionEtProjets(@PathVariable Long coordinateurId,@PathVariable Long directionId, @RequestBody Long[] projetIds) {
    Coordinateur coordinateur = icoordinateur.findOne(coordinateurId);
    Direction direction = idir.findOne(directionId);
    List<Projet> projets = iprojet.findAllById(Arrays.asList(projetIds));
    coordinateur.setDirection(direction);
    coordinateur.setProjets(new HashSet<>(projets));
    return icoordinateur.saveAndFlush(coordinateur);
}
*/


}
