---
applyTo: "**/pom.xml"
description: "Reglas de seguridad para dependencias Maven del backend (Java 8 / Spring Boot 2.7)"
---

# Seguridad de dependencias Maven

- Toda dependencia nueva o actualizada debe ser **compatible con Java 8 y Spring Boot 2.7.x**.
- Preferir versiones gestionadas por el BOM `spring-boot-starter-parent`; no fijar versiones que lo contradigan
  salvo que sea para corregir una CVE, y dejarlo indicado con un comentario corto.
- No agregar dependencias solo para sanitizar entrada si el framework ya ofrece una API segura.
- Al resolver una alerta de Dependabot: subir a la **versión parcheada mínima** de la rama compatible, no a la última mayor.
- Revisar el impacto de dependencias transitivas antes de excluirlas (`<exclusions>`); una exclusión puede
  reintroducir una versión vulnerable desde otro path.
- Nunca incluir credenciales, tokens ni URLs de repositorios privados con secretos embebidos en el POM.
- Tras cambiar dependencias: `mvn -f backend/pom.xml -q clean verify` y revisar `mvn dependency:tree` si hay conflicto de versiones.
