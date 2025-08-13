package com.bassagou.traitement_de_csv.service;

import org.apache.spark.sql.*;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.spark.sql.functions.*;

@Service
public class SparkIncidentService {
private final SparkSession sparkSession;

    public SparkIncidentService() {
        this.sparkSession = SparkSession
                .builder()
                .master("local[*]")
                .appName("IncidentsAnalysis")
                .getOrCreate();
    }

    public List<Row> countIncidentsByService(String csvPath) {
        Dataset<Row> df = readCsv(csvPath);

        return df.groupBy("service")
                .agg(count("*").alias("nb_incidents"))
                .orderBy(desc("nb_incidents"))
                .collectAsList();
    }

    public List<Row> topYearsWithMostIncidents(String csvPath) {
        Dataset<Row> df = readCsv(csvPath);

        df = df.withColumn("parsed_date",
                to_date(col("date"), "yyyy-MM-dd")
        ).withColumn("year", year(col("parsed_date")));

        return df.groupBy("year")
                .agg(count("*").alias("nb_incidents"))
                .orderBy(desc("nb_incidents"))
                .limit(2)
                .collectAsList();
    }
    private Dataset<Row> readCsv(String path) {
        return sparkSession.read()
                .option("header", true)
                .option("inferSchema", true)
                .csv(path);
    }

}
