# Triaje, verificación y falsos positivos

## 1. Recuperar el contexto de la alerta

Con las herramientas MCP de GitHub (repositorio `Baufest/fuentus`):

| Necesidad | Herramienta |
|---|---|
| Listar alertas abiertas de CodeQL | `list_code_scanning_alerts` (`state: open`, `severity`, `tool_name: CodeQL`) |
| Detalle de una alerta (regla, ruta, línea, flujo) | `get_code_scanning_alert` |
| Vulnerabilidades de dependencias | `list_dependabot_alerts` / `get_dependabot_alert` |
| Secretos expuestos | `list_secret_scanning_alerts` |

Si el MCP no está disponible, pedir: número de alerta, id de regla, archivo y línea.

## 2. Clasificar el hallazgo

Responder estas preguntas **antes** de editar código:

1. ¿Cuál es el *source*? (`@RequestParam`, `@PathVariable`, `@RequestBody`, `HttpServletRequest`,
   `MultipartFile`, cabeceras, CSV/JSON importado en `parsers/` y `processors/`, valor de BD de origen externo)
2. ¿Existe un camino ejecutable real hasta el *sink*?
3. ¿Hay ya una barrera efectiva (allow-list, enum, parseo a tipo no string, validación con `@Valid`)?
4. ¿El endpoint es alcanzable sin autenticación?
5. ¿Cuál es el impacto concreto (lectura de datos, escritura, RCE, DoS)?

| Veredicto | Criterio |
|---|---|
| Verdadero positivo | Camino source→sink alcanzable y sin barrera efectiva |
| Falso positivo | Barrera demostrable, valor constante/enum, o sink no explotable; hay que citar el código que corta el flujo |
| Riesgo aceptado | Explotable solo con privilegios ya elevados o en código de test/herramienta interna; requiere decisión del usuario |

Nunca marcar falso positivo por "parece improbable". Se necesita evidencia en el código.

## 3. Aplicar el fix

- Cambio mínimo, en el archivo y método señalados.
- Java 8: sin `var`, `List.of`, `Map.of`, `Path.of`, `Stream.toList()`, text blocks ni switch expressions.
- Mantener el contrato de la API: si cambia una firma pública o el JSON devuelto, revisar los consumidores en
  `frontend/src/api/*.ts` y avisar al usuario.
- No tocar formato ni imports no relacionados (mantiene el diff revisable).

## 4. Verificar

```powershell
mvn -f backend/pom.xml -q clean verify
```

Si solo se necesita compilar rápido:
```powershell
mvn -f backend/pom.xml -q -DskipTests compile
```

Agregar un test cuando el fix sea observable. Entradas maliciosas de referencia:

| CWE | Entrada de prueba |
|---|---|
| SQLi | `' OR '1'='1`, `x'; DROP TABLE apps;--` |
| Path traversal | `../../etc/passwd`, `..\\..\\windows\\win.ini`, `%2e%2e%2f` |
| XSS | `<script>alert(1)</script>`, `"><img src=x onerror=alert(1)>` |
| Log injection | `usuario\r\nINFO: acceso concedido` |
| Command injection | `foo; rm -rf /`, `foo && whoami`, `` foo `id` `` |
| SSRF | `http://169.254.169.254/latest/meta-data/`, `http://127.0.0.1:8080/` |

Ejemplo de test negativo:
```java
@Test
void rechazaRutaFueraDelDirectorioBase() {
    assertThrows(SecurityException.class, () -> service.leerArchivo("../../etc/passwd"));
}
```

## 5. Reportar

```markdown
### Alerta <id> — <regla> (CWE-<n>) — <severidad>
**Ubicación**: <archivo>#L<línea>
**Flujo**: <source> → <pasos> → <sink>
**Veredicto**: ...
**Causa raíz**: ...
**Fix aplicado**: ...
**Verificación**: mvn -f backend/pom.xml -q clean verify → BUILD SUCCESS
**Riesgo residual / seguimiento**: ...
```

## 6. Cierre de la alerta

- Un fix mergeado en `main` cierra la alerta automáticamente en el siguiente análisis.
- Un dismiss (`false positive` / `won't fix` / `used in tests`) **solo lo ejecuta el usuario**; el agente
  redacta la justificación pero no la aplica.
- Si hay que rotar un secreto expuesto, indicarlo como acción manual fuera del repositorio.
