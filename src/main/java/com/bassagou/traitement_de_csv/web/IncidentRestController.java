package com.bassagou.traitement_de_csv.web;

import com.bassagou.traitement_de_csv.service.SparkIncidentService;
import org.apache.spark.sql.Row;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/incidents")
public class IncidentRestController {
    private final SparkIncidentService service;

    public IncidentRestController(SparkIncidentService service) {
        this.service = service;
    }

    @GetMapping("/by-service")
    public List<Row> getIncidentsByService() {
        return service.countIncidentsByService("src/main/resources/data/incidents.csv");
    }

    @GetMapping("/top-years")
    public List<Row> getTopYears() {
        return service.topYearsWithMostIncidents("src/main/resources/data/incidents.csv");
    }
}
