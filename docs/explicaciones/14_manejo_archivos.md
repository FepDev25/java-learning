# 14 · Archivos: rutas, contenido y recursos

## Contenido

- [1. Una ruta no es un archivo abierto](#1-una-ruta-no-es-un-archivo-abierto)
- [2. Operaciones con rutas clásicas: consultar y manipular rutas](#2-operaciones-con-rutas-clásicas-consultar-y-manipular-rutas)
- [3. Escritura de texto: caracteres y codificación](#3-escritura-de-texto-caracteres-y-codificación)
- [4. Lectura de texto: cargar todo o recorrer por partes](#4-lectura-de-texto-cargar-todo-o-recorrer-por-partes)
- [5. Copia de bytes: binarios y fin de lectura](#5-copia-de-bytes-binarios-y-fin-de-lectura)
- [6. Operaciones con Path y Files y el ejemplo de recorrido del sistema de archivos: rutas y árboles reales](#6-operaciones-con-path-y-files-y-el-ejemplo-de-recorrido-del-sistema-de-archivos-rutas-y-árboles-reales)
- [7. Configuración como pares de texto](#7-configuración-como-pares-de-texto)
- [8. Persistir un grafo de objetos](#8-persistir-un-grafo-de-objetos)
- [Preguntas de repaso](#preguntas-de-repaso)

## 1. Una ruta no es un archivo abierto

`File` y `Path` representan ubicaciones. Construir una de esas referencias no crea necesariamente la implementación ni abre su contenido. Las operaciones reales pueden fallar por permisos, inexistencia, archivos ocupados o disco lleno, y deben tratar esos resultados.

`java.io` incluye streams clásicos de bytes y caracteres. `java.nio.file` —NIO.2— incluye `Path`, `Files` y opciones para leer, escribir y recorrer. Los streams de I/O transportan contenido; `java.util.stream.Stream` describe procesamiento de elementos.

### Ruta preparada frente a creación real

```java
String tmp = System.getProperty("java.io.tmpdir");
File archivo = new File(tmp + "/apuntes_felipe.txt");
```

La referencia combina un directorio y un nombre, pero no materializa contenido. Las operaciones de existencia o creación se ejecutan después. Este mismo principio aplica a `Path.of`: representar la ubicación y abrirla son pasos diferentes.

## 2. Operaciones con rutas clásicas: consultar y manipular rutas

El ejemplo de operaciones con rutas clásicas construye una ruta temporal con `new File`, consulta existencia y después llama `createNewFile()`. Este devuelve falso si ya existía; no significa que haya creado un archivo nuevo de todas formas.

`isFile`, `isDirectory`, `length`, `canRead` y `canWrite` consultan atributos. `mkdir` crea un nivel y `mkdirs` puede crear padres. `listFiles` devuelve referencias a hijos y puede devolver nulo si no puede listar. `renameTo` y `delete` devuelven booleanos que hay que interpretar; imprimir un mensaje no garantiza éxito si no se comprueba el resultado.

La función recursiva elimina primero descendientes y luego el directorio. Es un recorrido en postorden: un directorio debe quedar vacío antes de borrarlo. A diferencia del Composite del capítulo 13, estas operaciones sí actúan en el sistema de archivos.

### Consultar el resultado de una creación

```java
boolean creado = archivo.createNewFile();
System.out.println("\n== Después de createNewFile() ==");
System.out.println("Creado        : " + creado);        // false si ya existía
System.out.println("Existe        : " + archivo.exists());
```

La operación puede devolver falso porque la implementación ya existe; la consulta posterior de existencia permite interpretar esa situación. Un error de acceso puede lanzar una excepción, por lo que verdadero/falso no describe todas las formas de fallo.

## 3. Escritura de texto: caracteres y codificación

Texto y bytes se conectan mediante una codificación. El módulo utiliza UTF-8 explícito en sus lectores y escritores de texto para preservar letras como «Computación». Escribir con una codificación y leer con otra puede producir caracteres incorrectos.

Fragmento del ejemplo de escritura de texto:

```java
try (BufferedWriter bw = new BufferedWriter(
        new FileWriter(ruta1, StandardCharsets.UTF_8))) {
    bw.write("Estudiante: Felipe");
    bw.newLine();
    bw.write("Carrera   : Ciencias de la Computación");
    bw.newLine();
    bw.write("Ciudad    : Quito, Ecuador");
    bw.newLine();
    bw.write("Semestre  : 5");
}
```

El `FileWriter` convierte caracteres; `BufferedWriter` acumula antes de transferir y reduce llamadas pequeñas. `try-with-resources` cierra al salir, incluso ante excepción, y el cierre vacía el buffer. Declarar varios recursos los cierra en orden inverso a su creación. `flush` envía lo pendiente, pero no cierra ni garantiza por sí mismo durabilidad física del disco.

El modo normal reemplaza el contenido. El constructor con `true` agrega al final; no inserta automáticamente una línea nueva. Como el bloque anterior termina justo tras `"Semestre : 5"`, el `"-- anexo --"` añadido queda pegado a esa línea antes del siguiente salto. El resultado deriva de las llamadas de escritura, no de la intención de separar un anexo.

`PrintWriter` facilita `println` y `printf`; ciertas fallas se consultan con `checkError` en vez de propagarse como las de un writer ordinario. `Files.writeString` escribe todo un texto y `Files.write(List<String>)` escribe líneas, administrando internamente apertura y cierre. Son cómodos para contenido pequeño disponible en memoria.

## 4. Lectura de texto: cargar todo o recorrer por partes

`BufferedReader.readLine()` devuelve una línea sin terminador o nulo al final. Una línea vacía es `""`, no nulo. `Scanner` divide contenido en tokens; el ejemplo configura comas y saltos como separadores y consume cinco campos por registro.

El contenido es un CSV simplificado: separar por comas no maneja campos entre comillas con comas dentro ni registros incompletos. `hasNext` solo garantiza un siguiente token, no otros cuatro. Los datos preparados por el ejemplo sí cumplen su formato.

| Lectura | Qué carga | Cuándo encaja |
| --- | --- | --- |
| `Files.readString` | Un `String` completo | Archivo pequeño que se procesa completo |
| `Files.readAllLines` | Lista completa de líneas | Archivo pequeño que recorrerás varias veces |
| `BufferedReader` | Lectura incremental | Procesar líneas sin cargar todo |
| `Files.lines` | Stream perezoso de líneas | Combinar lectura incremental con Stream |

En el ejemplo de lectura de texto:

```java
try (Stream<String> stream = Files.lines(ruta, StandardCharsets.UTF_8)) {
    stream
        .map(l -> l.split(","))
        .filter(p -> Double.parseDouble(p[4]) >= 9.0)
        .forEach(p -> System.out.printf("  %-20s → %s%n", p[3], p[4]));
}
```

Cada línea se transforma en campos, se interpreta su nota y se filtra. Algoritmos 9.5, Bases de Datos 9.1 e IA 9.7 pasan. El stream **debe cerrarse** porque posee un recurso de archivo, incluso al utilizar una terminal de cortocircuito. `collect(toList())` posterior volvería a materializar todo y se perdería la ventaja de memoria incremental.

## 5. Copia de bytes: binarios y fin de lectura

Los archivos binarios no deben decodificarse como texto para copiarlos. El ejemplo genera 1024 bytes y contrasta copia byte a byte, bloques, streams con buffer y `Files.copy`.

```java
byte[] buffer = new byte[8192];
try (FileInputStream fis = new FileInputStream(origen.toFile());
     FileOutputStream fos = new FileOutputStream(destino.toFile())) {
    int leidos;
    while ((leidos = fis.read(buffer)) != -1)
        fos.write(buffer, 0, leidos);
}
```

`read` devuelve cantidad válida, que puede ser menor que el buffer. Escribir las 8192 posiciones siempre copiaría bytes sobrantes, especialmente al final. `read()` individual retorna `int` para representar valores de byte de 0 a 255 y reservar −1 como fin; guardarlo inmediatamente en `byte` perdería esa distinción.

El contenido preparado usa casts a byte y algunos valores se ven negativos en Java, pero sus patrones de bits siguen siendo correctos. Los tiempos de un archivo tan pequeño no son un benchmark estable. `BufferedInputStream` y `BufferedOutputStream` ilustran Decorator: añaden buffering conservando la interfaz del stream envuelto.

## 6. Operaciones con Path y Files y el ejemplo de recorrido del sistema de archivos: rutas y árboles reales

`Path.of(TMP,"cs","apunte.txt")` combina segmentos; `getFileName`, `getParent`, `getRoot`, `subpath` y `toAbsolutePath` consultan o derivan rutas. Derivar una ruta no verifica que exista. `Files.createDirectories` crea padres, y `readAttributes` obtiene tamaño y tiempos mediante `BasicFileAttributes`.

`Files.copy` conserva el origen y crea destino; `Files.move` cambia la ubicación. `REPLACE_EXISTING` permite sustituir destino. NIO.2 ofrece excepciones específicas en lugar de solo false, pero no hace todas las operaciones atómicas automáticamente. El código no solicita `ATOMIC_MOVE`, cuya disponibilidad también depende del sistema.

`Files.walk` retorna un stream del árbol empezando por la raíz. `Files.find` permite filtrar con ruta y atributos y limitar profundidad. Ambos se cierran con `try-with-resources`. El ejemplo de recorrido del sistema de archivos crea ocho archivos de maqueta, lista `.java`, suma bytes y agrupa extensiones.

Para borrar, ordena rutas al revés y elimina descendientes antes que padres. Su `catch` ignora errores y la suma trata lecturas de tamaño fallidas como cero; el mensaje de limpieza o el total pueden ocultar operaciones que fallaron. La indentación del ejemplo resta uno al conteo relativo, por lo que la raíz y sus hijos inmediatos pueden aparecer con la misma sangría: no es una medida exacta del nivel del árbol.

### Recorrer y agregar tamaños

```java
// Versión reducida del cálculo de tamaño de un árbol.
try (Stream<Path> rutas = Files.walk(BASE)) {
    long total = rutas
            .filter(Files::isRegularFile)
            .mapToLong(ruta -> {
                try {
                    return Files.size(ruta);
                } catch (IOException e) {
                    return 0L;
                }
            })
            .sum();
    System.out.println("Tamaño: " + total + " bytes");
}
```

La raíz y sus directorios también circulan por `walk`, pero el filtro limita la suma a archivos regulares. El mapeo extrae bytes y `sum` agrega. Convertir un fallo de lectura en cero hace que el cálculo continúe, pero el total puede subestimar el contenido; esa política debe distinguirse de una medición íntegra confirmada.

### Cambiar ubicación y examinar un árbol

```java
Files.copy(p, copia, StandardCopyOption.REPLACE_EXISTING);
System.out.println("  Copiado a: " + copia.getFileName());

Files.move(copia, movido, StandardCopyOption.REPLACE_EXISTING);
System.out.println("  Movido a : " + movido.getFileName());
```

Copiar conserva el ejemplo original y crea otra ubicación con su contenido. Mover traslada la copia; el origen de esa segunda operación ya no queda en su ruta anterior. `REPLACE_EXISTING` define qué ocurre con un destino existente, pero no promete una operación atómica entre sistemas de archivos.

```java
try (Stream<Path> stream = Files.find(BASE, 10,
        (p, attr) -> attr.isRegularFile() && attr.size() > 10)) {
    stream
        .map(p -> "  " + BASE.relativize(p) + " (" + p.toFile().length() + " bytes)")
        .forEach(System.out::println);
}
```

La búsqueda limita profundidad y recibe atributos para evaluar si cada entrada es archivo regular y supera diez bytes. La terminal consume el stream dentro del bloque que administra su cierre. La condición consulta características de disco, no solo nombres de una maqueta en memoria.

## 7. Configuración como pares de texto

`Properties` maneja claves y valores de texto. `setProperty` escribe, `getProperty` lee y su variante con segundo argumento aporta un valor por defecto. `store(Writer,comentario)` persiste y `load(Reader)` restaura; el ejemplo usa readers y writers UTF-8.

La variante `load(InputStream)` utiliza reglas de codificación diferentes, por lo que no conviene mezclar formas sin conocerlas. La salida `store` puede incluir cabecera y orden variable; no es una impresión determinista del objeto. El ejemplo ordena claves cuando necesita listarlas con consistencia.

`System.getProperties()` consulta propiedades de la JVM. El código del ejemplo de configuración mediante propiedades solo **imprime** cómo cargar desde classpath: no incluye una carga real de `src/main/resources/config.properties`. La demostración real del classpath está en el capítulo 1.

### Guardar y recuperar una propiedad de texto

```java
Properties config = new Properties();
config.setProperty("app.nombre",     "GestorNotas");
config.setProperty("app.version",    "1.0.0");
```

Los valores se almacenan como cadenas, incluso cuando describen números o versiones. Leer una propiedad numérica requeriría después interpretación y validación de ese texto.

```java
Properties cargado = new Properties();
try (Reader r = new InputStreamReader(
        new FileInputStream(ruta.toFile()), StandardCharsets.UTF_8)) {
    cargado.load(r);
}
```

El reader convierte bytes UTF-8 a caracteres y `load` interpreta las asignaciones de configuración. El objeto cargado es otro contenedor con los valores restaurados, no una referencia al objeto originalmente almacenado.

## 8. Persistir un grafo de objetos

`Estudiante` implementa `Serializable`, una interfaz de marcado. `ObjectOutputStream.writeObject` guarda el objeto y las referencias serializables alcanzables; `ObjectInputStream.readObject` reconstruye y retorna `Object`, que el ejemplo convierte a estudiante.

`transient` excluye token y promedio cacheado. Al recuperar, token queda nulo y el cache decimal en cero; no se vuelve a ejecutar el constructor del estudiante serializable para regenerarlos. `static` tampoco es estado de cada instancia serializada. Los nombres y materias se recuperan, pero las referencias originales y recuperadas no son el mismo objeto de memoria.

`serialVersionUID = 1L` establece una identidad de versión para compatibilidad. Coincidir en UID no hace compatible cualquier cambio arbitrario de estructura; cambiarlo tampoco convierte datos automáticamente. `@Serial` ayuda a identificar declaraciones especiales de serialización, no sustituye a `Serializable`.

El ejemplo de serialización de objetos cierra correctamente los streams del estudiante individual, pero abre el stream de lectura de la lista en una expresión sin `try-with-resources`; ese recurso no se cierra explícitamente. El cast de lista es no comprobado por borrado de tipos. La lectura necesita un bloque de cierre explícito para administrar ese recurso. La serialización nativa es un formato ligado a clases Java; no equivale a JSON y la deserialización de fuentes no confiables requiere restricciones específicas.

### Un estado que se conserva y otro que se omite

```java
private transient String token;
private transient double promedioCacheado;
```

Ambos campos pertenecen a la instancia, pero no se incluyen en su estado serializado por esta regla. Recuperar el objeto deja token nulo y el decimal en cero; una regeneración necesita código adicional.

```java
try (ObjectOutputStream oos = new ObjectOutputStream(
        new BufferedOutputStream(new FileOutputStream(archivo.toFile())))) {
    oos.writeObject(felipe);
}
```

El stream de objeto convierte el grafo serializable a bytes y el buffer los envía a la implementación. El cierre de los envoltorios administra también la salida inferior. Serializar no equivale a llamar a `toString`: se conserva una representación binaria de estado.

```java
Estudiante recuperado;
try (ObjectInputStream ois = new ObjectInputStream(
        new BufferedInputStream(new FileInputStream(archivo.toFile())))) {
    recuperado = (Estudiante) ois.readObject();
}
```

`readObject` recupera un objeto y retorna una referencia de tipo amplio. El cast exige que el dato realmente corresponda a estudiante. La clase y su compatibilidad de serialización deben estar disponibles, y el objeto recuperado ocupa una identidad nueva en memoria.

## Preguntas de repaso

Los ejemplos usan nombres fijos dentro de `java.io.tmpdir`, escriben y después borran. Un directorio temporal aislado permite evitar colisiones con otros archivos de esos nombres.

El mismo directorio aislado puede utilizarse para los demás ejemplos. Sus limpiezas normales eliminan muchos resultados; no esperes encontrar siempre la implementación después de finalizar.

1. ¿Crear `Path` abre un archivo? **No.**
2. ¿Por qué escribir solo `leidos` bytes? **Porque el último bloque puede ser menor que el buffer.**
3. ¿Hay que cerrar `Files.lines`? **Sí; su stream mantiene recursos de I/O.**
4. ¿`transient` reconstruye el token? **No; lo omite y queda con valor inicial por defecto al deserializar.**
