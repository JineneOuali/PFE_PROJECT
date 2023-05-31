package com.example.PFE_PROJECT.Controlleur;


import com.example.PFE_PROJECT.dao.IRevue;
import com.example.PFE_PROJECT.dao.IRevueHistory;
import com.example.PFE_PROJECT.models.Revue;
import com.example.PFE_PROJECT.models.RevueHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/revuesHistory")
public class RevueHistoryController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private IRevueHistory irevueHistory;
    @Autowired
    private IRevue irevue;

    @GetMapping("/all/{id}")
    public Collection<RevueHistory> listRevueHistory(@PathVariable Long id) {
        Revue revue = irevue.findOne(id);
        Collection<RevueHistory> RH=revue.getRevuesHistory();
        return RH;
    }
    /*@PutMapping("/update/{Id}")
    public RevueHistory update(@RequestBody RevueHistory revueH, @PathVariable Long Id) {
        Revue revue = irevue.findOne(Id);
        revueH.setRevues(revue);
        return irevueHistory.saveAndFlush(revueH);
    }*/

    @PutMapping("/update/{Id}")
    public RevueHistory update(@RequestBody RevueHistory revueH, @PathVariable Long Id) {
        Revue optionalReview = irevue.findOne(Id);
        String oldStartTime = optionalReview.getStartTime();
        // update the review in the revue table
        optionalReview.setStartTime(revueH.getStartTime());
        irevue.saveAndFlush(optionalReview);

        // save history to ReviewHistory table
        RevueHistory history = new RevueHistory();
        history.setRevues(optionalReview);
        history.setStartTime(oldStartTime);
        history.setStartTime(revueH.getStartTime());
        history.setDescription(optionalReview.getDescription());
        history.setTitre(optionalReview.getTitre());
        history.setStatus(optionalReview.getStatus());
        history.setTempsPasse(optionalReview.getTempsPasse());
       return irevueHistory.saveAndFlush(history);

    }
    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteRevueHistory (@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            irevueHistory.delete(Id);
            message.put("etat","revue deleted");
            return message;
        }

        catch (Exception e) {
            message.put("etat","revue not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public RevueHistory getonerevueHistory(@PathVariable Long id) {
        return irevueHistory.findOne(id);
    }
    @PostMapping("/save")
    public RevueHistory saveRevueHistory(@RequestBody RevueHistory r){
        return irevueHistory.save(r);
    }
}
