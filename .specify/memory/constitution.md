# Constitución del Proyecto Backend

## Principios Fundamentales

### I. Stack Tecnológico Innegociable
Este proyecto se desarrolla exclusivamente con Spring Boot 3 y Java 17.  
Toda nueva funcionalidad debe implementarse siguiendo convenciones del ecosistema Spring (inyección de dependencias, separación por capas, configuración por perfiles).  
No se aceptan librerías o patrones que contradigan este stack sin una justificación técnica documentada.

### II. Seguridad por Defecto
La API debe estar protegida con autenticación básica (HTTP Basic) desde el inicio.  
Ningún endpoint sensible puede exponerse sin control de acceso.  
Las credenciales, secretos y configuraciones de seguridad deben manejarse por variables de entorno o perfiles, nunca hardcodeadas.

### III. Persistencia Consistente con PostgreSQL
La base de datos oficial del proyecto es PostgreSQL.  
Todos los cambios de modelo deben reflejarse de forma controlada mediante migraciones versionadas (por ejemplo, Flyway o Liquibase).  
Se prohíbe depender de motores embebidos en producción; las pruebas deben simular el comportamiento real de PostgreSQL.

### IV. Entorno Reproducible con Docker
El proyecto debe poder levantarse en local con Docker y Docker Compose para garantizar paridad de entornos.  
Debe existir como mínimo un servicio para la API y otro para PostgreSQL, con red, volúmenes y variables documentadas.  
Cualquier miembro del equipo debe poder ejecutar el sistema con un único flujo estándar.

### V. API Contratada y Documentada
La API REST debe mantenerse documentada con Swagger/OpenAPI de manera actualizada.  
Cada endpoint nuevo o modificado requiere su contrato, ejemplos de request/response y códigos de error esperados.  
No se considera completa una historia sin documentación de API verificable.

## Restricciones Técnicas y de Calidad

- Arquitectura base por capas: `controller`, `service`, `repository`, `domain`/`dto`.
- Validación obligatoria de entrada en todos los endpoints públicos.
- Manejo centralizado de errores con respuestas consistentes.
- Configuración por perfiles (`dev`, `test`, `prod`) con valores seguros por defecto.
- Logs estructurados suficientes para diagnóstico, sin exponer datos sensibles.
- Convención de versionado de API y compatibilidad explícita ante cambios breaking.

## Flujo de Desarrollo y Puertas de Calidad

- Cada cambio funcional debe incluir pruebas automáticas (unitarias y/o integración según alcance).
- Todo cambio de seguridad, base de datos o contrato API requiere revisión técnica obligatoria.
- Antes de integrar, el proyecto debe compilar, pasar pruebas y levantar correctamente con Docker.
- La documentación Swagger debe ser validada manualmente en cada feature relevante.
- No se aprueban PRs con deuda crítica de seguridad, ausencia de pruebas o configuraciones no reproducibles.

## Gobernanza

Esta constitución prevalece sobre prácticas ad hoc del equipo.  
Toda excepción debe documentarse con alcance, riesgos y fecha de revisión.  
Las enmiendas a esta constitución requieren acuerdo del equipo técnico y actualización explícita de versión.

**Version**: 1.0.0 | **Ratified**: 2026-02-25 | **Last Amended**: 2026-02-25
