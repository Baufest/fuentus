package ar.com.bbva.fuentus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChimeraSastImportDTO {

    @CsvBindByName(column = "project_id")
    @JsonProperty("projectId")
    private String projectId;
    
    @CsvBindByName(column = "name")
    @JsonProperty("name")
    private String name;
    
    @CsvBindByName(column = "repo_url")
    @JsonProperty("repoUrl")
    private String repoUrl;
    
    @CsvBindByName(column = "uuaa")
    @JsonProperty("uuaa")
    private String uuaa;
    
    @CsvBindByName(column = "country_det_name")
    @JsonProperty("countryDetName")
    private String countryDetName;
    
    @CsvBindByName(column = "application")
    @JsonProperty("application")
    private String application;
    
    @CsvBindByName(column = "last_scan")
    @CsvDate(value = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("lastScan")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastScan;
    
    @CsvBindByName(column = "chimera_url")
    @JsonProperty("chimeraUrl")
    private String chimeraUrl;
    
    @CsvBindByName(column = "branch")
    @JsonProperty("branch")
    private String branch;
    
    @CsvBindByName(column = "analyzer")
    @JsonProperty("analyzer")
    private String analyzer;
    
    @CsvBindByName(column = "arq")
    @JsonProperty("arq")
    private String arq;
    
    @CsvBindByName(column = "language")
    @JsonProperty("language")
    private String language;
    
    @CsvBindByName(column = "stock_flow")
    @JsonProperty("stockFlow")
    private String stockFlow;
    
    @CsvBindByName(column = "assumed_1")
    @JsonProperty("assumed1")
    private Integer assumed1;
    
    @CsvBindByName(column = "assumed_2")
    @JsonProperty("assumed2")
    private Integer assumed2;
    
    @CsvBindByName(column = "high")
    @JsonProperty("high")
    private Integer high;
    
    @CsvBindByName(column = "critical")
    @JsonProperty("critical")
    private Integer critical;
    
    @CsvBindByName(column = "medium")
    @JsonProperty("medium")
    private Integer medium;
    
    @CsvBindByName(column = "low")
    @JsonProperty("low")
    private Integer low;
    
    @CsvBindByName(column = "to_review")
    @JsonProperty("toReview")
    private Integer toReview;
    
    @CsvBindByName(column = "lines")
    @JsonProperty("lines")
    private Integer lines;
}