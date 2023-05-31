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

import java.util.HashMap;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/responsablesprojet")
public class ResponsablePController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private StorageService storage;
    @Autowired
    private IResponsableP iresponsableP;
    @GetMapping("/all")
    public List<ResponsableP> listResponsableP() {
        return iresponsableP.findAll();
    }
    @PutMapping("/update/{Id}")
    public ResponsableP update(@RequestBody ResponsableP responsableP, @PathVariable Long Id) {
        responsableP.setId(Id);
        ResponsableP c = iresponsableP.findOne(Id);
        responsableP.setPassword(c.getPassword());
        responsableP.setImage(c.getImage());
        responsableP.setUsername(c.getUsername());
        responsableP.setAdresse(c.getAdresse());

        responsableP.setValide(true);
        responsableP.setRole(c.getRole());

        return iresponsableP.saveAndFlush(responsableP);
    }
    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteResponsableP(@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            iresponsableP.delete(Id);
            message.put("etat","responsable projet deleted");
            return message;
        }

        catch (Exception e) {
            message.put("etat","responsable Projet not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public ResponsableP getoneresponsableP(@PathVariable Long id) {
        return iresponsableP.findOne(id);
    }
    @PostMapping("/save")
    public ResponsableP saveResponsableP(@RequestBody ResponsableP c){
        return iresponsableP.save(c);
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
    public ResponseEntity<?> register(ResponsableP user, @RequestParam("file") MultipartFile file )
    {


        user.setValide(false);

        user.setPassword(hash(user.getPassword()));
        storage.store(file);
        user.setImage(file.getOriginalFilename());
        user.setRole("ResponsableProjet");

        iresponsableP.save(user);

        return ResponseEntity.ok(new UserTokenState(null, 0, user));

    }
    @PutMapping("/validerResponsableP/{Id}")
    public ResponsableP validerResponsableP(@PathVariable Long Id) {
        ResponsableP responsableP = iresponsableP.findOne(Id);
        responsableP.setValide(true);
        return iresponsableP.saveAndFlush(responsableP);
    }
    @PutMapping("/modifimage/{id}")
    public ResponsableP modif(@RequestParam("file") MultipartFile file, @PathVariable Long id){
        ResponsableP c = iresponsableP.findOne(id);
        // c.setId(id);
        // int i = (int) new Date().getTime();
        // System.out.println("Integer : " + i);
        storage.store(file);
        c.setImage(file.getOriginalFilename());
        return iresponsableP.saveAndFlush(c) ;
    }

    @PutMapping("/updatepassword/{Id}")
    public ResponsableP modif(String newpassword, @PathVariable Long Id) {
        ResponsableP c=iresponsableP.findOne(Id);
        c.setPassword(hash(newpassword));
        return iresponsableP.saveAndFlush(c);
    }
}
