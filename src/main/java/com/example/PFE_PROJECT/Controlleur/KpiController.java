package com.example.PFE_PROJECT.Controlleur;
import com.example.PFE_PROJECT.dao.*;
import com.example.PFE_PROJECT.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/users/kpis")
public class KpiController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private IKpi ikpi;
    @Autowired
    private IProjet iprojet;
    @Autowired
    private IResponsableP iResponsableP;

    @Autowired
    private IDirection iDirection;

    @GetMapping("/all")
    public List<Kpi> listKpi() {
        return ikpi.findAll();
    }
    @PutMapping("/update/{Id}")
    public Kpi update(@RequestBody Kpi kpi, @PathVariable Long Id) {
        kpi.setId(Id);
        return ikpi.saveAndFlush(kpi);
    }
    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteKpi(@PathVariable(value = "Id") Long Id) {

        HashMap message= new HashMap();
        try{
            ikpi.delete(Id);
            message.put("etat","Kpi deleted");
            return message;
        }

        catch (Exception e) {
            message.put("etat","Kpi not deleted");
            return message;
        }
    }
    @GetMapping("/getone/{id}")
    public Kpi getonekpi(@PathVariable Long id) {
        return ikpi.findOne(id);
    }
    @PostMapping("/save")
    public Kpi saveKpi(@RequestBody Kpi k){
        return ikpi.save(k);
    }

/*    @PostMapping("/save/{idprojet}")
    public Kpi savekpibyProjet(@RequestBody Kpi k, @PathVariable Long idprojet){
        Projet projet=iprojet.findOne(idprojet);
        k.setProjet(projet);
        return ikpi.save(k);
    }*/
    @GetMapping("/getkpisbyprojet/{id}")
    public Set<Kpi> getkpisbyprojet(@PathVariable Long id) {
        Projet projet = iprojet.findOne(id);
        Set<Kpi> lk=projet.getKpis();
        return lk;
    }
    @PostMapping("/getKpisByProjectAndPeriod/{id}")
    public Map<String, Double> getKpisByProjectAndPeriod(@PathVariable Long id, @RequestBody Map<String, String> requestBody,
                                                         @RequestParam(value = "projectId", required = false) Long projectId) throws ParseException {
        Map<String, Double> kpiAverages = new HashMap<>();
        ResponsableP c = iResponsableP.findOne(id);
        List<Kpi> kpiList = new ArrayList<>();
        Set<Projet> projectList;

        if (projectId != null) {
            // If projectId is specified, get the list of KPIs related to the selected project
            Projet project = iprojet.findOne(projectId);
            kpiList = new ArrayList<>(project.getKpis());
            projectList = new HashSet<>();
            projectList.add(project);
        } else {
            // If projectId is not specified, get all projects
            projectList = c.getProjets();
            // Collect all KPIs from all projects
            for (Projet project : projectList) {
                kpiList.addAll(project.getKpis());
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(requestBody.get("startDate2"));
        Date endDate = sdf.parse(requestBody.get("endDate2"));

        for (Kpi kpi : kpiList) {
            Collection<KpiVal> kpiValList = kpi.getKpiVals();
            double sum = 0;
            int count = 0;
            for (KpiVal kpiVal : kpiValList) {
                boolean validProject = true;
                if (projectId != null) {
                    validProject = kpiVal.getProjet().equals(iprojet.findOne(projectId));
                } else {
                    validProject = projectList.contains(kpiVal.getProjet());
                }
                if (!validProject) {
                    continue;
                }
                Date kpiValStartDate = sdf.parse(kpiVal.getCreationDate());
                if (kpiValStartDate.equals(startDate) || kpiValStartDate.equals(endDate) || (kpiValStartDate.after(startDate) && kpiValStartDate.before(endDate))) {
                    sum += Integer.parseInt(kpiVal.getValeur().replaceAll("%", ""));
                    count++;
                }
            }
            if (count > 0) {
                double average = sum / count;
                kpiAverages.put(kpi.getTitre(), average);
            }
        }

        return kpiAverages;
    }

    /*@PostMapping("/getKpisByProjectAndPeriod/{id}")
    public Map<String, Double> getKpisByProjectAndPeriod(@PathVariable Long id, @RequestBody Map<String, String> requestBody, @RequestParam(value = "projectId", required = false) Long projectId) throws ParseException {

        Map<String, Double> kpiAverages = new HashMap<>();
        ResponsableP c = iResponsableP.findOne(id);
        Set<Projet> listProjets = c.getProjets();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate2 = sdf.parse(requestBody.get("startDate2"));
        Date endDate2 = sdf.parse(requestBody.get("endDate2"));
        for (Projet projet : listProjets) {
            if (projectId != null && !projet.getId().equals(projectId)) {
                // Skip projects that don't match the specified ID
                continue;
            }

            Set<Kpi> listKpis = projet.getKpis();
            for (Kpi kpi : listKpis) {
                Collection<KpiVal> listKpiVal = kpi.getKpiVals();
                double sum = 0;
                int count = 0;
                for (KpiVal kpiVal : listKpiVal) {
                    Date kpiValStartDate = sdf.parse(kpiVal.getCreationDate());
                    if (kpiValStartDate.equals(startDate2) || kpiValStartDate.equals(endDate2) || (kpiValStartDate.after(startDate2) && kpiValStartDate.before(endDate2))) {
                        sum += Integer.parseInt(kpiVal.getValeur().replaceAll("%", ""));
                        count++;
                    }
                }
                if (count > 0) {
                    double average = sum / count;
                    kpiAverages.put(kpi.getTitre(), average);
                }
            }
        }

        return kpiAverages;
    }
*/
    /*@PostMapping("/getKpisByProjectAndPeriod/{id}")
    public Set<Kpi> getKpisByProjectAndPeriod(@PathVariable Long id, @RequestBody Map<String, String> requestBody, @RequestParam(value = "projectId", required = false) Long projectId) throws ParseException {
        Set<Kpi> setKpis = new HashSet<>();
        ResponsableP c = iResponsableP.findOne(id);
        Set<Projet> listProjets = c.getProjets();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate2 = sdf.parse(requestBody.get("startDate2"));
        Date endDate2 = sdf.parse(requestBody.get("endDate2"));

        for (Projet projet : listProjets) {
            if (projectId != null && !projet.getId().equals(projectId)) {
                // Skip projects that don't match the specified ID
                continue;
            }

            Set<Kpi> listKpis = projet.getKpis();
            for (Kpi kpi : listKpis) {
                Collection<KpiVal> listKpiVal = kpi.getKpiVals();
                for (KpiVal kpiVal : listKpiVal) {
                    Date kpiValStartDate = sdf.parse(kpiVal.getCreationDate());
                    if (kpiValStartDate.equals(startDate2) || kpiValStartDate.equals(endDate2) || (kpiValStartDate.after(startDate2) && kpiValStartDate.before(endDate2))) {
                        setKpis.add(kpi);
                    }
                }
            }
        }

        return setKpis;
    }*/
    @PostMapping("/getKpisByDirectionProjectAndPeriod")
    public Map<String, Double> getKpisByDirectionProjectAndPeriod(@RequestBody Map<String, String> requestBody,
                                                                  @RequestParam(value = "projectId", required = false) Long projectId,
                                                                  @RequestParam(value = "directionId", required = false) Long directionId) throws ParseException {
        Map<String, Double> kpiAverages = new HashMap<>();
        List<Kpi> kpiList = new ArrayList<>();
        List<Projet> projectList = new ArrayList<>();

        if (projectId != null) {
            // If projectId is specified, get the list of KPIs related to the selected project
            Projet project = iprojet.findOne(projectId);
            kpiList = new ArrayList<>(project.getKpis());
            projectList.add(project);
        } else {
            if (directionId != null) {
                // If directionId is specified, get the list of projects related to the selected direction
                Direction direction = iDirection.findOne(directionId);
                projectList = new ArrayList<>(direction.getProjets());
            } else {
                // If directionId is not specified, get all projects
                projectList = iprojet.findAll();
            }

            // Collect all KPIs from all projects
            for (Projet project : projectList) {
                kpiList.addAll(project.getKpis());
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(requestBody.get("startDate3"));
        Date endDate = sdf.parse(requestBody.get("endDate3"));

        for (Kpi kpi : kpiList) {
            Collection<KpiVal> kpiValList = kpi.getKpiVals();
            double sum = 0;
            int count = 0;
            for (KpiVal kpiVal : kpiValList) {
                boolean validProject = true;
                if (projectId != null) {
                    validProject = kpiVal.getProjet().equals(iprojet.findOne(projectId));
                }
                if (!validProject) {
                    continue;
                }
                Date kpiValStartDate = sdf.parse(kpiVal.getCreationDate());
                if (kpiValStartDate.equals(startDate) || kpiValStartDate.equals(endDate) || (kpiValStartDate.after(startDate) && kpiValStartDate.before(endDate))) {
                    sum += Integer.parseInt(kpiVal.getValeur().replaceAll("%", ""));
                    count++;
                }
            }
            if (count > 0) {
                double average = sum / count;
                kpiAverages.put(kpi.getTitre(), average);
            }
        }

        return kpiAverages;
    }



    @PostMapping("/averagekpibyproject/{id}")
    public Double getAverageKpiByProject(@PathVariable Long id, @RequestBody Map<String, String> requestBody) throws ParseException {
        List<Projet> listProjets = iprojet.findAll();
        Kpi kpi = ikpi.findOne(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate4 = sdf.parse(requestBody.get("startDate4"));
        Date endDate4 = sdf.parse(requestBody.get("endDate4"));
        Double sum = 0.0;
        int count = 0;
        for (Projet projet : listProjets) {
            Set<Kpi> listKpis = projet.getKpis();
            for (Kpi currentKpi : listKpis) {
                if (currentKpi.getId().equals(kpi.getId())) { // Check if current Kpi has the same ID as the target Kpi
                    Collection<KpiVal> listKpiVal = currentKpi.getKpiVals();
                    for (KpiVal kpiVal : listKpiVal) {
                        Date kpiValStartDate = sdf.parse(kpiVal.getCreationDate());
                        if (kpiValStartDate.equals(startDate4) || kpiValStartDate.equals(endDate4) || (kpiValStartDate.after(startDate4) && kpiValStartDate.before(endDate4))) {

                            sum += Integer.parseInt(kpiVal.getValeur().replaceAll("%", ""));

                            count++;
                        }
                    }
                }
            }
        }
        return count > 0 ? (double) sum / count : 0;
    }


    @GetMapping("/getProjectsByDirection/{directionId}")
    public Set<Projet> getProjectsByDirection(@PathVariable Long directionId) {
        Direction direction = iDirection.findOne(directionId);
        return direction.getProjets();
    }


    /*@GetMapping("/activity/kpi/{kpiId}/average")
    public Map<String, Double> getAverageKpiValue(@PathVariable Long kpiId) {
        List<Activity> activities = activitiesRepository.findAll();
        Map<String, Double> activityAverages = new HashMap<>();

        for (Activity activity : activities) {
            double average = activity.getProjects().stream()
                    .flatMap(project -> project.getHistoryList().stream())
                    .filter(kh -> kh.getKpi().getId().equals(kpiId))
                    .mapToInt(KpiHistory::getValue)
                    .average()
                    .orElse(0.0);

            // Convert the average to a percentage
            double percentage = Double.isNaN(average) ? 0.0 : average;

            activityAverages.put(activity.getName(), percentage);
        }

        return activityAverages;
    }*/
}
