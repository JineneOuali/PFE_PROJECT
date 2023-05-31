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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/admins")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);
    @Autowired
    private StorageService storage;
    @Autowired
    private IAdmin iadmin;


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
    public ResponseEntity<?> register(Admin user, @RequestParam("file") MultipartFile file )
    {





        user.setValide(true);

        user.setPassword(hash(user.getPassword()));
        storage.store(file);
        user.setImage(file.getOriginalFilename());
        user.setRole("Admin");

        iadmin.save(user);

        return ResponseEntity.ok(new UserTokenState(null, 0, user));

    }
}

