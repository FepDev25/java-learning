# 13 · Patrones de diseño: reconocer colaboraciones reutilizables

## Contenido

- [1. Un patrón comienza con un problema](#1-un-patrón-comienza-con-un-problema)
- [2. Singleton: instancia compartida, creación controlada](#2-singleton-instancia-compartida-creación-controlada)
- [3. Factory Method: decidir creación desde una abstracción](#3-factory-method-decidir-creación-desde-una-abstracción)
- [4. Decorator: composición de comportamiento](#4-decorator-composición-de-comportamiento)
- [5. Composite: una operación uniforme sobre un árbol](#5-composite-una-operación-uniforme-sobre-un-árbol)
- [6. Observer: el cambio se comunica a interesados](#6-observer-el-cambio-se-comunica-a-interesados)
- [Preguntas de repaso](#preguntas-de-repaso)

## 1. Un patrón comienza con un problema

Un patrón da nombre a una forma de organizar objetos que resuelve un problema recurrente. No es una librería que haya que importar ni una obligación de añadir clases a todo programa. Para entenderlo identifica qué cambia, qué permanece estable y cómo colaboran los participantes.

| Paquete | Problema | Colaboración del módulo |
| --- | --- | --- |
| `singleton` | Compartir una instancia de configuración | Constructor privado y acceso controlado |
| `factory` | Variar creación de notificaciones | Un creador abstracto delega la creación a subclases |
| `decorator` | Añadir transformaciones combinables | Objetos de la misma interfaz se envuelven |
| `composite` | Procesar hojas y ramas como componentes | Una rama delega en sus hijos |
| `observer` | Informar a varios interesados | Un sujeto llama a sus observadores |

## 2. Singleton: instancia compartida, creación controlada

`ConfiguracionApp` tiene constructor privado, referencia estática y acceso con comprobación doble:

```java
public static ConfiguracionApp getInstancia() {
    if (instancia == null) {
        synchronized (ConfiguracionApp.class) {
            if (instancia == null) {
                instancia = new ConfiguracionApp();
            }
        }
    }
    return instancia;
}
```

La primera comprobación evita tomar el monitor en las llamadas habituales posteriores. La segunda comprueba de nuevo después de adquirirlo: otro hilo podría haber creado la instancia mientras este esperaba. La referencia se declara `volatile`, necesaria para publicación segura en esta forma de inicialización.

El ejemplo de configuración compartida obtiene tres referencias, cambia semestre a 6 mediante una y lo lee desde otra. Las tres apuntan al mismo objeto; `cfg1 == cfg2` y `cfg2 == cfg3` dan verdadero. «Crear solo una vez» se refiere al acceso ordinario de esta clase bajo su cargador de clases, no a una garantía universal entre procesos o todos los cargadores.

La seguridad de **creación** no hace seguro todo el estado mutable. `semestre` se lee y escribe sin sincronización ni `volatile`; compartirlo entre hilos sigue requiriendo una política. También hay costos de diseño: estado global facilita acoplamiento y complica aislar pruebas. No implica que una única conexión JDBC deba compartirse entre todas las tareas; el capítulo 15 comparte un pool y presta conexiones.

Los comentarios mencionan un singleton con enum, pero el módulo no implementa esa variante. Una explicación conceptual es que una constante de enum puede servir como instancia controlada por la JVM; no hay una segunda clase del repo que ejecutar para compararla.

### Referencias diferentes al mismo estado

```java
ConfiguracionApp cfg1 = ConfiguracionApp.getInstancia();
ConfiguracionApp cfg2 = ConfiguracionApp.getInstancia();
ConfiguracionApp cfg3 = ConfiguracionApp.getInstancia();
```

Las tres variables reciben el acceso controlado a una instancia. La igualdad de identidad entre ellas se deriva del objeto compartido, no de que universidad o estudiante tengan los mismos textos. Modificar un campo mediante cualquiera de estas referencias afecta al estado observado desde las demás.

## 3. Factory Method: decidir creación desde una abstracción

El cliente quiere enviar email, SMS o push sin repetir preparación y envío para cada tipo. `Notificacion` define el producto común con `preparar`, `enviar` y `getTipo`. Sus subclases establecen destinatario, asunto y extras, y cada una implementa su envío.

En `NotificacionFactory`:

```java
public final Notificacion enviarNotificacion(String destinatario, String asunto) {
    Notificacion n = crearNotificacion(destinatario, asunto);
    System.out.println("--- Procesando " + n.getTipo() + " ---");
    n.preparar();
    n.enviar();
    return n;
}

protected abstract Notificacion crearNotificacion(String destinatario, String asunto);
```

`EmailFactory`, `SmsFactory` y `PushFactory` implementan la creación. La estructura corresponde a **Factory Method**: las subclases eligen un producto concreto. El método final conserva el algoritmo «crear → preparar → enviar», una colaboración de Template Method. Aunque el comentario menciona Abstract Factory, no hay aquí una fábrica de familias de varios productos relacionados.

El ejemplo de creación de notificaciones trabaja con referencias `NotificacionFactory`. El envío no necesita conocer el constructor del producto. La selección dinámica con `switch(canal)` sí enumera las fábricas concretas: añadir un canal requiere actualizar esa selección o su configuración. El principio de extensión no significa que nunca cambie ningún punto de la aplicación.

`NotificacionEmail.enviar` imprime un mensaje con un servidor de ejemplo; SMS y push también imprimen. No envían correo, no contactan una API externa y no verifican un límite real de 160 caracteres. El flujo representa una simulación de responsabilidades.

### La fábrica concreta determina el producto

```java
protected Notificacion crearNotificacion(String destinatario, String asunto) {
    return new NotificacionEmail(destinatario, asunto);
}
```

El tipo de retorno es la abstracción, mientras que la construcción elige una clase concreta. La fábrica de SMS aplica la misma firma y construye otro producto. La secuencia común de preparación y envío puede trabajar con cualquiera de ellos mediante operaciones polimórficas.

## 4. Decorator: composición de comportamiento

`Formateador` declara `String formatear()`. `TextoSimple` devuelve el texto base; `TextoDecorador` mantiene una referencia a otro `Formateador`. Las cuatro clases concretas delegan y transforman: mayúsculas, inversión, reemplazo de espacios o prefijo/sufijo.

En el ejemplo de composición de decoradores:

```java
Formateador cadena = new ReemplazarEspaciosDecorador(
                         new InvertirDecorador(
                             new MayusculaDecorador(base)), "-");
```

La llamada externa entra a reemplazo, que pide el resultado a inversión; inversión lo pide a mayúsculas y esta al texto base. Los resultados vuelven aplicando transformaciones desde dentro hacia fuera. Adaptación con `"Hola Java"`: `"Hola Java" → "HOLA JAVA" → "AVAJ ALOH" → "AVAJ-ALOH"`.

Cada envoltorio implementa la misma interfaz y puede sustituir al original desde el punto de vista del cliente. No se necesita crear una subclase para cada combinación posible. La composición se decide al construir los objetos.

El orden puede importar: si agregas prefijo y sufijo antes de invertir, también se invierten; si los agregas después, permanecen en su orientación. Algunas transformaciones particulares pueden conmutar en ciertos textos, pero no asumas esa propiedad para toda cadena.

`Formateador conFecha = () -> "[2025-03-13] " + base.formatear()` es una implementación inline del contrato, con fecha literal. No consulta el reloj. Los streams de entrada/salida del capítulo 14 usan una idea similar al envolver un stream de archivo con uno que añade buffering.

### Delegación antes de transformar

```java
public String formatear() {
    return new StringBuilder(texto.formatear()).reverse().toString();
}
```

El decorador obtiene primero el resultado del componente envuelto, luego lo invierte y devuelve texto. Si ese componente es otro decorador, su transformación ya forma parte del valor recibido. Cada capa conserva el contrato `formatear` y añade una responsabilidad.

## 5. Composite: una operación uniforme sobre un árbol

`Componente` define nombre, `mostrar(nivel)` y `buscar(nombre)`. `Archivo` es hoja y responde por sí mismo. `Directorio` mantiene `List<Componente>`, admite hojas y ramas y delega a sus hijos. El cliente puede pedir mostrar o buscar sin escribir toda la recursión otra vez.

`Directorio` busca así:

```java
public boolean buscar(String nombre) {
    if (this.nombre.equalsIgnoreCase(nombre)) return true;
    return hijos.stream().anyMatch(h -> h.buscar(nombre));
}
```

Primero compara su nombre. Si no coincide, pregunta a los hijos; `anyMatch` deja de preguntar cuando alguno responde verdadero. Una hoja es el caso base. La lista conserva el orden de inserción para mostrar. `mostrar` indenta por nivel y arma el texto; sus saltos se ajustan según hojas y directorios.

El ejemplo de composición de un árbol construye una maqueta del repositorio, busca varios nombres y elimina `new Archivo("README.md")`. Aunque es otra instancia, `remove` usa `equals`, que compara nombre y clase concreta. Un directorio y un archivo del mismo nombre no son iguales según esa regla.

La búsqueda ignora mayúsculas, pero `equals` para eliminar usa comparación sensible a ellas. `remove` elimina solo de la lista del directorio receptor, no busca recursivamente el componente solicitado. No hay garantía de nombres únicos ni de ausencia de ciclos; `getHijos` expone la lista mutable. El árbol no representa archivos reales y eliminar su nodo no borra nada del disco.

### Hoja que resuelve la operación directamente

```java
public boolean buscar(String nombre) {
    return this.nombre.equalsIgnoreCase(nombre);
}
```

La hoja solo compara su nombre y retorna. No necesita preguntar a hijos porque no los tiene. La rama utiliza exactamente ese resultado en su búsqueda, así el cliente no distingue la recursión interna de la respuesta directa de la hoja.

## 6. Observer: el cambio se comunica a interesados

`Observador` declara `actualizar(Observable,Object)` y permite lambdas. `Observable` mantiene una lista y ofrece registrar, remover y notificar. `SistemaNotas` guarda notas y publica un `record EventoNota`, con materia, estudiante, nota y una consulta de aprobación desde 7.

`SistemaNotas` realiza:

```java
notas.put(estudiante, nota);
System.out.println("\n[SistemaNotas] " + materia + " → " + estudiante + ": " + nota);
notificarObservadores(new EventoNota(materia, estudiante, nota));
```

Cada observador puede reaccionar de forma diferente. El ejemplo de notificación de cambios registra app, email, log y alerta. Felipe 9.1 recibe la reacción de su app; Luis 5.4 activa riesgo académico. Publicar en el sistema de Redes sin observadores solo actualiza ese sistema y no genera esas reacciones.

La notificación es **síncrona**: `publicarNota` llama a cada observador en el mismo hilo. No hay cola de mensajes, broker ni ejecución paralela. Si un observador lanza una excepción, puede impedir que los posteriores reciban el evento. Añadir o remover de la lista mientras se recorre también requiere cuidado. El parámetro `Object` obliga a hacer cast al evento esperado; no se valida automáticamente el tipo.

El texto «Felipe se desuscribe» del ejemplo no ejecuta una baja. Para remover exactamente el observador registrado hay que conservar su referencia. Adaptación:

```java
Observador registro = (sujeto, dato) -> System.out.println(dato);
algoritmos.agregarObservador(registro);
algoritmos.removerObservador(registro);
```

Crear otra lambda con el mismo cuerpo no garantiza la identidad necesaria. El record del evento tiene campos finales y aquí sus datos son inmutables; un record que contenga objetos mutables no vuelve inmutables automáticamente esos objetos.

### Distribución síncrona del evento

```java
protected void notificarObservadores(Object dato) {
    for (Observador o : observadores) {
        o.actualizar(this, dato);
    }
}
```

El bucle llama a cada suscriptor antes de continuar con el siguiente. Todos reciben el mismo dato y una referencia al sujeto emisor. No se inicia un hilo nuevo ni se añade una tarea a un executor; un observador lento prolonga la llamada de publicación.

```java
public record EventoNota(String materia, String estudiante, double nota) {
    public boolean aprobado() { return nota >= 7.0; }
}
```

El evento agrupa datos finales y la consulta `aprobado` deriva un booleano de la nota. Las reacciones usan esos datos sin necesitar acceder al mapa de notas del sujeto. Una nota inferior a 7 activa la condición de alerta del observador correspondiente.

## Preguntas de repaso

1. ¿Qué hace intercambiables a los decoradores? **Implementan la misma interfaz del componente que envuelven.**
2. ¿Por qué Composite necesita recursión? **Las ramas contienen componentes que pueden ser otras ramas.**
3. ¿Un singleton de creación segura protege todos sus campos? **No; el estado mutable requiere su propia coordinación.**
4. ¿Observer es asíncrono en este repo? **No; los observadores se llaman directamente en el hilo publicador.**
