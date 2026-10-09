# Documentación - Curso Java Master

Documentación técnica del curso de Java, organizada por módulos. Cada módulo es un
proyecto Maven independiente dentro del repositorio `Udemy_Master_Java`.

Las [explicaciones de los capítulos 1 al 15](./explicaciones/README.md) desarrollan
los conceptos mediante fragmentos de código integrados, trazas, resultados y
límites de las implementaciones, con contexto para interpretar cada ejemplo.

## Índice de Módulos

| Módulo | Tema | Documento |
|--------|------|-----------|
| p01 | Fundamentos del lenguaje | [01 - Fundamentos de Java](./01_fundamentos_java.md) |
| p02 | Arreglos | [02 - Arreglos en Java](./02_arreglos.md) |
| p03 | Matrices | [03 - Matrices en Java](./03_matrices.md) |
| p04 | Programación Orientada a Objetos | [04 - POO](./04_poo.md) |
| p05 | API de Colecciones | [05 - API de Colecciones](./05_api_colecciones.md) |
| p06 | Hilos y concurrencia | [06 - Hilos](./06_hilos.md) |
| p07 | Java 8 - Expresiones Lambda | [07 - Java 8 Lambda](./07_java8_lambda.md) |
| p08 | Java 8 - API Stream | [08 - API Stream](./08_api_stream.md) |
| p09 | Optional | [09 - Optional](./09_optional.md) |
| p10 | Java 8 - Fecha y Hora | [10 - Fecha y Hora](./10_java8_date_time.md) |
| p11 | Anotaciones y Reflection | [11 - Anotaciones](./11_anotaciones.md) |
| p12 | Recursividad | [12 - Recursividad](./12_recursividad.md) |
| p13 | Patrones de Diseño | [13 - Patrones de Diseño](./13_patrones_diseno.md) |
| p14 | Manejo de Archivos | [14 - Manejo de Archivos](./14_manejo_archivos.md) |
| p15 | JDBC | [15 - JDBC](./15_jdbc.md) |

> Los proyectos integradores viven en `proyects/` y no forman parte de la numeración
> del curso. Algunos incluyen su propia documentación (por ejemplo,
> `proyects/velocity-exchange-core/docs/`).

## Información General

Todos los módulos comparten la misma configuración:

- **Java:** 21 (LTS)
- **Build tool:** Maven
- **Group ID:** `com.cultodeportivo`
- **Versión:** `1.0-SNAPSHOT`
- **Paquete raíz:** `com.cultodeportivo`

El módulo `p15_jdbc` añade dos dependencias: `org.postgresql:postgresql:42.7.3`
y `com.zaxxer:HikariCP:5.1.0`.

## Cómo compilar y ejecutar

Cada módulo se compila y ejecuta de forma independiente:

```bash
cd p0X_nombre
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.<paquete>.<Clase>"
```

Ejemplo:

```bash
cd p01_basicos
mvn exec:java -Dexec.mainClass="com.cultodeportivo.fundamentos.HolaMundo"
```

La clase `Main` de cada módulo (`com.cultodeportivo.Main`) es solo un punto de
entrada de ejemplo; el contenido del curso está en las clases de cada paquete.

## Convenciones de la documentación

Cada documento incluye:

- Descripción general del módulo.
- Información del proyecto (artifactId, Java, build tool).
- Contenido organizado por paquetes y clases reales.
- Conceptos clave aprendidos.
- Instrucciones de ejecución y estructura de paquetes.
- Notas técnicas y referencias.
