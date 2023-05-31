package com.example.PFE_PROJECT.Controlleur;

import com.example.PFE_PROJECT.dao.*;
import com.example.PFE_PROJECT.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/processus")
public class ProcessusController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private IProcessus iprocessus;
    @Autowired
    private IActivite iact;

    @GetMapping("/all")
    public List<Processus> listProcessus() {
        return iprocessus.findAll();
    }
    @PutMapping("/update/{Id}")
    public Processus update(@RequestBody Processus processus, @PathVariable Long Id) {
        processus.setId(Id);
        return iprocessus.saveAndFlush(processus);
    }
    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteProcessus(@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            iprocessus.delete(Id);
            message.put("etat","processus deleted");
            return message;
        }

        catch (Exception e) {
            message.put("etat","processus not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public Processus getoneprocessus(@PathVariable Long id) {
        return iprocessus.findOne(id);
    }
    @PostMapping("/save")
    public Processus saveProcessus(@RequestBody Processus p){
        return iprocessus.save(p);
    }
}