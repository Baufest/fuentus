---
applyTo: "backend/src/**/*.java"
description: "Reglas de codificación segura para el backend Java 8 / Spring Boot 2.7 de Fuentus"
---

# Codificación segura — backend Java

## Compatibilidad
- Target Java 8: no usar `var`, records, text blocks, switch expressions, `List.of`/`Map.of`,
  `Path.of`, `Stream.toList()` ni APIs de Java 9+.

## Reglas obligatorias
- **SQL**: solo consultas parametrizadas (`@Query` con `:param`, `setParameter`, `PreparedStatement`).
  Nunca concatenar entrada de usuario en SQL/JPQL. Identificadores dinámicos (columna, `ORDER BY`) solo por allow-list.
- **Rutas de archivo**: canonicalizar con `normalize()` y verificar `target.startsWith(baseDir)`.
  Ignorar `MultipartFile#getOriginalFilename()` como nombre de destino; generarlo en servidor.
- **Logging (Log4j2)**: usar logging parametrizado (`log.info("... {}", valor)`), nunca concatenación.
  Neutralizar `\r\n` en valores de usuario. Nunca loguear credenciales, tokens ni cuerpos completos de request.
- **Errores**: no devolver `e.getMessage()` ni stack traces al cliente; mensaje genérico + detalle en log.
- **Entrada**: validar DTOs en el borde (`@Valid`, tipos concretos, enums) en lugar de sanear strings más abajo.
- **Cripto**: `SHA-256`+ para hashes, `AES/GCM/NoPadding` con IV aleatorio, `SecureRandom` para tokens/IDs.
  Nunca MD5/SHA-1 para seguridad, ni `new Random()` ni `Math.random()`.
- **Secretos**: sin literales de credenciales, URLs con contraseña, tokens o API keys en código ni en
  `application*.properties` versionados; usar `${VAR_ENTORNO}`.
- **XML/JSON**: parsers XML con DTD y entidades externas deshabilitadas; Jackson sin default typing.
- **CORS/CSRF**: sin `allowedOrigins("*")` junto a `allowCredentials(true)`; no desactivar CSRF sin justificación documentada.
- **Serialización**: prohibido `ObjectInputStream` sobre datos externos.

## Al corregir un hallazgo de seguridad
- Cambio mínimo, acotado al método afectado; sin refactors ni reformateos colaterales.
- Preferir la API segura del framework antes que sanitización manual con `replace()`/regex.
- Nunca "resolver" con `@SuppressWarnings` o comentarios de supresión de CodeQL.
- Verificar con `mvn -f backend/pom.xml -q clean verify` y añadir test con entrada maliciosa cuando sea observable.
