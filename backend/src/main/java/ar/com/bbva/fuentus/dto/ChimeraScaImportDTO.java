package ar.com.bbva.fuentus.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChimeraScaImportDTO {

    @CsvBindByName(column = "project_id")
    private String projectId;

    @CsvBindByName(column = "app_id")
    private Long appId;

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "uuaa")
    private String uuaa;

    @CsvBindByName(column = "low")
    private Long low;

    @CsvBindByName(column = "medium")
    private Long medium;

    @CsvBindByName(column = "high")
    private Long high;

    @CsvBindByName(column = "critical")
    private Long critical;

    @Override
    public String toString() {
        return "ChimeraScaImportDTO{" +
               "projectId='" + projectId + '\'' +
               ", appId=" + appId +
               ", name='" + name + '\'' +
               ", uuaa='" + uuaa + '\'' +
               ", low=" + low +
               ", medium=" + medium +
               ", high=" + high +
               ", critical=" + critical +
               '}';
    }
}