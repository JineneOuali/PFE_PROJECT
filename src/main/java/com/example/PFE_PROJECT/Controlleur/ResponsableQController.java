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
@RequestMapping(value = "/users/responsablesqualite")
public class ResponsableQController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private StorageService storage;
    @Autowired
    private IResponsableQ iresponsableQ;
    @GetMapping("/all")
    public List<ResponsableQ> listResponsableQ() {
        return iresponsableQ.findAll();
    }
    @PutMapping("/update/{Id}")
    public ResponsableQ update(@RequestBody ResponsableQ responsableQ, @PathVariable Long Id) {
        responsableQ.setId(Id);
        ResponsableQ c = iresponsableQ.findOne(Id);
        responsableQ.setPassword(c.getPassword());
        responsableQ.setImage(c.getImage());
        responsableQ.setUsername(c.getUsername());
        responsableQ.setAdresse(c.getAdresse());

        responsableQ.setValide(true);
        responsableQ.setRole(c.getRole());

        return iresponsableQ.saveAndFlush(responsableQ);
    }
    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteResponsableQ(@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            iresponsableQ.delete(Id);
            message.put("etat","responsable QUALITE deleted");
            return message;
        }

        catch (Exception e) {
            message.put("etat","responsable QUALITE not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public ResponsableQ getoneresponsableQ(@PathVariable Long id) {
        return iresponsableQ.findOne(id);
    }
    @PostMapping("/save")
    public ResponsableQ saveResponsableQ(@RequestBody ResponsableQ c){
        return iresponsableQ.save(c);
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
    public ResponseEntity<?> register(ResponsableQ user, @RequestParam("file") MultipartFile file )
    {


        user.setValide(false);

        user.setPassword(hash(user.getPassword()));
        storage.store(file);
        user.setImage(file.getOriginalFilename());
        user.setRole("ResponsableQualite");

        iresponsableQ.save(user);

        return ResponseEntity.ok(new UserTokenState(null, 0, user));

    }
    @PutMapping("/validerResponsableQ/{Id}")
    public ResponsableQ validerResponsableQ(@PathVariable Long Id) {
        ResponsableQ responsableQ = iresponsableQ.findOne(Id);
        responsableQ.setValide(true);
        return iresponsableQ.saveAndFlush(responsableQ);
    }
    @PutMapping("/modifimage/{id}")
    public ResponsableQ modif(@RequestParam("file") MultipartFile file, @PathVariable Long id){
        ResponsableQ c = iresponsableQ.findOne(id);
        // c.setId(id);
        // int i = (int) new Date().getTime();
        // System.out.println("Integer : " + i);
        storage.store(file);
        c.setImage(file.getOriginalFilename());
        return iresponsableQ.saveAndFlush(c) ;
    }

    @PutMapping("/updatepassword/{Id}")
    public ResponsableQ modif(String newpassword, @PathVariable Long Id) {
        ResponsableQ c=iresponsableQ.findOne(Id);
        c.setPassword(hash(newpassword));
        return iresponsableQ.saveAndFlush(c);
    }
}
