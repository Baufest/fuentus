package ar.com.bbva.fuentus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VelocidadImportDTO {

    @CsvBindByName(column = "service_n2")
    @JsonProperty("service_n2")
    private String serviceN2;

    @CsvBindByName(column = "LT")
    @JsonProperty("LT")
    private String lt;

    @CsvBindByName(column = "CT")
    @JsonProperty("CT")
    private String ct;

    // Columnas de fecha para encontrar la más reciente
    @CsvBindByName(column = "new_date")
    @JsonProperty("new_date")
    private String newDate;

    @CsvBindByName(column = "analyzing_date")
    @JsonProperty("analyzing_date")
    private String analyzingDate;

    @CsvBindByName(column = "ready_date")
    @JsonProperty("ready_date")
    private String readyDate;

    @CsvBindByName(column = "in_progress_date")
    @JsonProperty("in_progress_date")
    private String inProgressDate;

    @CsvBindByName(column = "test_date")
    @JsonProperty("test_date")
    private String testDate;

    @CsvBindByName(column = "ready_to_verify_date")
    @JsonProperty("ready_to_verify_date")
    private String readyToVerifyDate;

    @CsvBindByName(column = "to_rework_date")
    @JsonProperty("to_rework_date")
    private String toReworkDate;

    @CsvBindByName(column = "blocked_date")
    @JsonProperty("blocked_date")
    private String blockedDate;

    @CsvBindByName(column = "accepted_date")
    @JsonProperty("accepted_date")
    private String acceptedDate;

    @CsvBindByName(column = "discarded_date")
    @JsonProperty("discarded_date")
    private String discardedDate;

    @CsvBindByName(column = "ready_to_deploy_date")
    @JsonProperty("ready_to_deploy_date")
    private String readyToDeployDate;

    @CsvBindByName(column = "deployed_date")
    @JsonProperty("deployed_date")
    private String deployedDate;
}
