# Explicaciones de Java: capítulos 1 al 15

Los capítulos desarrollan los conceptos de los módulos numerados del curso mediante explicaciones, fragmentos de código, trazas y resultados. Cada tema incluye el contexto necesario para interpretar sus ejemplos, sus precondiciones y los límites de las implementaciones.

## Índice de temas

| Capítulo | Tema | Contenido principal |
| --- | --- | --- |
| 01 | [Fundamentos](01_fundamentos_java.md) | Tipos, operadores, decisiones, bucles, texto, parámetros y entorno |
| 02 | [Arreglos](02_arreglos.md) | Índices, búsqueda, ordenamiento, inserción, eliminación y acumulación |
| 03 | [Matrices](03_matrices.md) | Filas, columnas, recorridos, simetría y transposición |
| 04 | [Programación orientada a objetos](04_poo.md) | Encapsulación, relaciones, herencia, contratos, genéricos y excepciones |
| 05 | [Colecciones](05_api_colecciones.md) | Listas, conjuntos, mapas, igualdad y criterios de orden |
| 06 | [Hilos y concurrencia](06_hilos.md) | Tareas, monitores, coordinación, temporizadores y pools |
| 07 | [Lambdas](07_java8_lambda.md) | Interfaces funcionales, composición y referencias de métodos |
| 08 | [Streams](08_api_stream.md) | Transformación, selección, reducción, agrupación y paralelismo |
| 09 | [Optional](09_optional.md) | Presencia, ausencia, recuperación y navegación de valores |
| 10 | [Fecha y hora](10_java8_date_time.md) | Tipos temporales, zonas, duración y períodos |
| 11 | [Anotaciones y reflection](11_anotaciones.md) | Metadatos, retención, inspección y procesamiento |
| 12 | [Recursividad](12_recursividad.md) | Casos base, reducción de problemas y recorridos de árboles |
| 13 | [Patrones de diseño](13_patrones_diseno.md) | Creación controlada, composición y notificación |
| 14 | [Manejo de archivos](14_manejo_archivos.md) | Rutas, caracteres, bytes, recursos y serialización |
| 15 | [JDBC](15_jdbc.md) | SQL, parámetros, cursores, transacciones, pools y DAO |

## Convenciones de los ejemplos

Los ejemplos proceden del código del curso revisado el 8 de octubre de 2026. Los fragmentos seleccionan la operación relevante y pueden omitir declaraciones de paquete, imports, mensajes auxiliares y partes ajenas al concepto. Las versiones reducidas y adaptaciones se identifican cuando simplifican la estructura, cambian datos o añaden una política que no está implementada en el ejemplo original.

Las variables de un fragmento pueden depender del contexto descrito inmediatamente antes o después: una conexión ya obtenida, una lista de cursos o un arreglo con datos cargados. Un extracto no constituye necesariamente un programa ejecutable independiente. Las clases de distintos capítulos pueden compartir nombre y representar modelos diferentes.

Los resultados deterministas se calculan a partir de los datos indicados. La fecha actual, los tiempos, el orden de ejecución concurrente y el estado de una base de datos dependen de condiciones externas. Los nombres, edades y fechas de los ejemplos son datos de práctica independientes entre módulos.

Las limitaciones descritas forman parte del comportamiento de cada implementación. Una corrección mostrada como adaptación no implica que se haya modificado el código del curso. Los comentarios o mensajes que difieren del cálculo ejecutado se interpretan a partir de las instrucciones efectivas.

## Entorno de ejecución

Los módulos son proyectos Maven independientes configurados para Java 21. La mención de Java 8 en ciertos temas corresponde a la introducción de sus APIs. El procesamiento funcional puede coexistir con características posteriores del lenguaje.

Compilar genera bytecode; ejecutar una clase con `main` inicia únicamente ese programa. El classpath debe incluir las clases utilizadas y, en JDBC, el driver de PostgreSQL y HikariCP. La entrada de consola requiere datos compatibles con el formato solicitado, y la entrada gráfica requiere un entorno con ventanas disponibles.

Los ejemplos de archivos crean y eliminan contenido dentro del directorio temporal indicado por `java.io.tmpdir`. JDBC requiere el servidor PostgreSQL y el esquema explicados en su capítulo. La persistencia entre ejecuciones puede alterar conteos, IDs y resultados de consultas.

## Relaciones entre conceptos

Tipos y control de flujo permiten recorrer arreglos y matrices. Los objetos reúnen datos y responsabilidades, mientras que las colecciones aportan estructuras dinámicas. Lambdas expresa comportamiento reutilizable y Streams lo compone sobre secuencias. Optional representa ausencia; las anotaciones describen metadatos que reflection puede consultar. Recursividad resuelve jerarquías y los patrones organizan colaboraciones entre objetos. Archivos y JDBC trasladan datos de memoria a medios persistentes con sus propias reglas de recursos y consistencia.
