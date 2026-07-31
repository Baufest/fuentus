---
name: codeql-java-remediation
description: 'Triar y remediar alertas de GitHub Code Scanning (CodeQL) en Java y Spring Boot. Usar cuando aparezcan hallazgos SAST o CWEs como inyección SQL, command injection, path traversal, XSS, deserialización insegura, log injection, SSRF, XXE, zip slip, criptografía débil, generación aleatoria insegura o secretos hardcodeados en código Java/Maven.'
argument-hint: 'Número de alerta de code scanning, CWE o archivo Java afectado'
---

# Remediación de alertas CodeQL en Java

## Cuándo usar

- Hay una alerta de GitHub Code Scanning / CodeQL sobre código Java.
- Se necesita decidir si un hallazgo SAST es verdadero o falso positivo.
- Hay que aplicar el patrón de corrección seguro para un CWE en Java 8 / Spring Boot 2.7.

## Procedimiento

1. **Recuperar la alerta**
   - Con MCP de GitHub: `list_code_scanning_alerts` (filtra por `state: open`, `severity`) y luego
     `get_code_scanning_alert` con el `alertNumber` para obtener regla, mensaje, ruta y línea.
   - Sin MCP: pedir al usuario la regla (`java/sql-injection`, `java/path-injection`, ...), archivo y línea.

2. **Reproducir el flujo de datos**
   - Abrir el archivo y localizar el **sink** exacto.
   - Retroceder hasta el **source**: `@RequestParam`, `@PathVariable`, `@RequestBody`, `HttpServletRequest`,
     nombres de archivo subidos (`MultipartFile#getOriginalFilename`), cabeceras, contenido de CSV/JSON importado
     (`parsers/`, `processors/`), o filas de base de datos que originalmente vinieron del usuario.
   - Si no existe camino real source→sink, es candidato a falso positivo: documenta el corte
     (validación previa, valor constante, enum, tipo no string).

3. **Elegir el patrón de corrección**
   - Consultar [patrones de remediación por CWE](./references/remediation-patterns.md).
   - Regla de oro: **corregir en el sink** (parametrizar, codificar, canonicalizar) y, si aplica,
     **validar en el borde** con allow-list. La sanitización con `replace()`/regex es el último recurso.

4. **Aplicar el cambio mínimo**
   - Solo el método/clase afectado; sin refactors colaterales.
   - Compatible con Java 8 (sin `var`, `List.of`, `Path.of`, text blocks ni APIs de Java 9+).
   - Preferir APIs del framework: Spring Data JPA con parámetros nombrados, `NamedParameterJdbcTemplate`,
     `Files.newInputStream`, `HtmlUtils.htmlEscape`, `SecureRandom`.

5. **Verificar**
   - Seguir [triaje y verificación](./references/triage-and-verification.md).
   - `mvn -f backend/pom.xml -q clean verify`.
   - Añadir test unitario con entrada maliciosa cuando el fix sea verificable (`' OR '1'='1`, `../../etc/passwd`,
     `<script>alert(1)</script>`, `%0d%0a`).

6. **Reportar**
   - Alerta, CWE, ubicación, flujo, veredicto, fix, verificación y riesgo residual.
   - Si el veredicto es falso positivo, redactar la justificación para el dismiss (no ejecutar el dismiss sin permiso).

## Reglas duras

- Nunca "resolver" una alerta añadiendo supresiones o desactivando la query.
- Nunca cambiar el comportamiento funcional para silenciar el análisis.
- Nunca registrar (`log`) datos sensibles ni entradas sin sanear al corregir log injection.
- Nunca hacer push, abrir PRs ni cerrar alertas en GitHub sin autorización explícita.

## Referencias

- [Patrones de remediación por CWE](./references/remediation-patterns.md)
- [Triaje, verificación y falsos positivos](./references/triage-and-verification.md)
