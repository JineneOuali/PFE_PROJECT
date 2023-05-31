package com.example.PFE_PROJECT.Controlleur;


import com.example.PFE_PROJECT.dao.*;
import com.example.PFE_PROJECT.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/directions")
public class DirectionController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private IDirection idirection;
    @GetMapping("/all")
    public List<Direction> listDirection() {
        return idirection.findAll();
    }
    @PutMapping("/update/{Id}")
    public Direction update(@RequestBody Direction direction, @PathVariable Long Id) {
        direction.setId(Id);
        return idirection.saveAndFlush(direction);
    }
    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteDirection(@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            idirection.delete(Id);
            message.put("etat","direction deleted");
            return message;
        }

        catch (Exception e) {
            message.put("etat","direction not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public Direction getonedirection(@PathVariable Long id) {
        return idirection.findOne(id);
    }
    @PostMapping("/save")
    public Direction saveDirection(@RequestBody Direction d){
        return idirection.save(d);
    }
}
