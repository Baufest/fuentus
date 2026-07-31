# Patrones de remediación por CWE (Java 8 / Spring Boot 2.7)

Todos los ejemplos son compatibles con Java 8. `❌` = vulnerable, `✅` = corregido.

## CWE-89 — Inyección SQL (`java/sql-injection`)

❌
```java
String sql = "SELECT * FROM apps WHERE name = '" + name + "'";
entityManager.createNativeQuery(sql).getResultList();
```

✅ Spring Data JPA con parámetros nombrados:
```java
@Query(value = "SELECT * FROM apps WHERE name = :name", nativeQuery = true)
List<App> findByName(@Param("name") String name);
```

✅ `EntityManager`:
```java
entityManager.createNativeQuery("SELECT * FROM apps WHERE name = ?1", App.class)
        .setParameter(1, name)
        .getResultList();
```

✅ JDBC:
```java
try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM apps WHERE name = ?")) {
    ps.setString(1, name);
    ...
}
```

**Identificadores dinámicos** (nombre de tabla, columna, `ORDER BY`) no se pueden parametrizar: valida contra
una **allow-list** cerrada.
```java
private static final Set<String> SORTABLE =
        new HashSet<>(Arrays.asList("name", "created_at", "status"));

if (!SORTABLE.contains(sortColumn)) {
    throw new IllegalArgumentException("Columna de orden no permitida");
}
```

## CWE-22 / CWE-23 — Path traversal (`java/path-injection`)

❌ `new File(baseDir, request.getParameter("file"))`

✅
```java
Path base = Paths.get(baseDir).toRealPath();
Path target = base.resolve(fileName).normalize();
if (!target.startsWith(base)) {
    throw new SecurityException("Ruta fuera del directorio permitido");
}
```
Para archivos subidos, **descarta** `MultipartFile#getOriginalFilename()` y genera el nombre en servidor
(`UUID.randomUUID()` + extensión validada contra allow-list).

## CWE-22 (Zip Slip) — Extracción de archivos comprimidos

✅ Validar cada entrada antes de escribir, igual que arriba, y rechazar entradas con `..`, rutas absolutas o
enlaces. Limitar además tamaño total y número de entradas (evita zip bomb, CWE-409).

## CWE-78 — Inyección de comandos (`java/command-line-injection`)

❌ `Runtime.getRuntime().exec("sh -c " + cmd)`

✅ Evitar el shell; usar `ProcessBuilder` con argumentos separados y valores validados:
```java
ProcessBuilder pb = new ProcessBuilder("git", "log", "--oneline", validatedRef);
pb.redirectErrorStream(true);
```
Nunca concatenar entrada de usuario en la cadena del comando ni pasarla a `sh -c` / `cmd.exe /c`.

## CWE-79 — XSS reflejado/almacenado (`java/xss`)

- Preferir `@RestController` devolviendo DTOs serializados por Jackson (`Content-Type: application/json`)
  en lugar de construir HTML.
- Si hay que emitir HTML: `org.springframework.web.util.HtmlUtils.htmlEscape(value)`.
- En el frontend React, no usar `dangerouslySetInnerHTML` con datos del backend.
- Nunca reflejar entrada del usuario en respuestas `text/html` sin codificar.

## CWE-117 — Log injection (`java/log-injection`)

❌ `log.info("Importando archivo " + fileName);`

✅ Neutralizar saltos de línea y usar logging parametrizado:
```java
String safeName = fileName.replaceAll("[\\r\\n]", "_");
log.info("Importando archivo {}", safeName);
```
Nunca loguear contraseñas, tokens, cookies ni el cuerpo completo de la petición.

## CWE-502 — Deserialización insegura (`java/unsafe-deserialization`)

- No usar `ObjectInputStream` sobre datos externos.
- Jackson: mantener el default typing **desactivado**; nunca `enableDefaultTyping()` /
  `activateDefaultTyping(LaissezFaireSubTypeValidator...)`.
- Deserializar siempre a un DTO concreto, no a `Object` ni `Map` genérico cuando hay polimorfismo.
- YAML: usar `new Yaml(new SafeConstructor())`.

## CWE-611 — XXE (`java/xxe`)

```java
DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
dbf.setXIncludeAware(false);
dbf.setExpandEntityReferences(false);
```
Aplicar equivalentes a `SAXParserFactory`, `XMLInputFactory` (`IS_SUPPORTING_EXTERNAL_ENTITIES=false`,
`SUPPORT_DTD=false`) y `TransformerFactory` (`ACCESS_EXTERNAL_DTD`/`ACCESS_EXTERNAL_STYLESHEET` a `""`).

## CWE-918 — SSRF (`java/ssrf`)

- Validar el host destino contra una **allow-list** de dominios; no basta con bloquear `localhost`.
- Resolver el DNS y rechazar IPs privadas/loopback/link-local antes de conectar.
- Deshabilitar redirecciones automáticas o revalidar el destino tras cada redirección.
- Fijar timeouts de conexión y lectura.

## CWE-327 / CWE-328 / CWE-330 — Cripto débil y aleatoriedad

| Inseguro | Reemplazo |
|---|---|
| `MD5`, `SHA-1` para integridad | `SHA-256` / `SHA-512` |
| Hash de contraseñas con SHA-* | `BCryptPasswordEncoder` (Spring Security) |
| `DES`, `RC4`, `AES/ECB` | `AES/GCM/NoPadding` con IV aleatorio de 12 bytes, nunca reutilizado |
| `new Random()` para tokens/IDs | `new SecureRandom()` |
| `Math.random()` en contexto de seguridad | `SecureRandom` |

## CWE-798 — Credenciales hardcodeadas (`java/hardcoded-credential-api-call`)

- Mover a `application.properties` vía variables de entorno: `spring.datasource.password=${DB_PASSWORD}`.
- Nunca commitear el valor real; el fix debe eliminar el literal del código **y** recomendar rotación
  del secreto expuesto (el histórico de git sigue conteniéndolo).

## CWE-352 — CSRF

- No desactivar `csrf()` en `HttpSecurity` "para que funcione el frontend". Si la API es puramente stateless
  con token Bearer, documenta esa condición junto al `csrf().disable()`.

## CWE-942 / CWE-346 — CORS permisivo

En `config/CorsConfig.java`: nunca `allowedOrigins("*")` combinado con `allowCredentials(true)`.
Usar orígenes explícitos por entorno y métodos/cabeceras acotados.

## CWE-209 — Fuga de información en errores

- No devolver `e.getMessage()`, stack traces ni SQL al cliente.
- Usar `@ControllerAdvice` con mensajes genéricos y loguear el detalle en servidor con un id de correlación.

## CWE-601 — Redirección abierta

Validar el destino contra allow-list o permitir solo rutas relativas que empiecen por `/` y no por `//`.
