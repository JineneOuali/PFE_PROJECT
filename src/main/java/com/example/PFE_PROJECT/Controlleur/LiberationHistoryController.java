package com.example.PFE_PROJECT.Controlleur;

import com.example.PFE_PROJECT.dao.*;
import com.example.PFE_PROJECT.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/liberationsHistory")
public class LiberationHistoryController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private ILiberationHistory iliberationHistory;
    @Autowired
    private ILiberation iliberation;

    @GetMapping("/all/{id}")
    public Collection<LiberationHistory> listLiberationHistory(@PathVariable Long id) {
        Liberation liberation = iliberation.findOne(id);
        Collection<LiberationHistory> LH=liberation.getLiberationsHistory();
        return LH;
    }

    @PutMapping("/update/{Id}")
    public LiberationHistory update(@RequestBody LiberationHistory liberationH, @PathVariable Long Id) {
        Liberation optionalLiberation = iliberation.findOne(Id);
        String oldStartTime = optionalLiberation.getStartTime();
        optionalLiberation.setStartTime(liberationH.getStartTime());
        iliberation.saveAndFlush(optionalLiberation);

        LiberationHistory history = new LiberationHistory();
        history.setLiberations(optionalLiberation);
        history.setStartTime(oldStartTime);
        history.setStartTime(liberationH.getStartTime());
        history.setDescription(optionalLiberation.getDescription());
        history.setTitre(optionalLiberation.getTitre());
        history.setStatus(optionalLiberation.getStatus());
        history.setTempsPasse(optionalLiberation.getTempsPasse());
        return iliberationHistory.saveAndFlush(history);
    }
    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteLiberationHistory (@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            iliberationHistory.delete(Id);
            message.put("etat","liberation deleted");
            return message;
        }

        catch (Exception e) {
            message.put("etat","liberation not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public LiberationHistory getoneliberationHistory(@PathVariable Long id) {
        return iliberationHistory.findOne(id);
    }
    @PostMapping("/save")
    public LiberationHistory saveLiberationHistory(@RequestBody LiberationHistory r){
        return iliberationHistory.save(r);
    }
}
