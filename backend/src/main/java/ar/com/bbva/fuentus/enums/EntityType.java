package ar.com.bbva.fuentus.enums;

public enum EntityType {
    NUCLEUS_SERVICES("nucleus_services", "Nucleus"),
    CHIMERA_SCA("chimera_sca", "ChimeraSca"),
    CHIMERA_SAST("chimera_sast", "ChimeraSast"),
    APPS("apps", "Apps"),
    RFO("rfo", "Rfo"),
    PRODUCTIVIDAD("productividad", "Productividad"),
    VELOCIDAD("velocidad", "Velocidad");

    private final String tableName;
    private final String entityName;

    EntityType(String tableName, String entityName) {
        this.tableName = tableName;
        this.entityName = entityName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getEntityName() {
        return entityName;
    }
}