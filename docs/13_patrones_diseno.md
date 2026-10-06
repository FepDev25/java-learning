# P13 - Patrones de Diseño

## Descripción General

Estudio de patrones de diseño clásicos implementados con Java puro: **Singleton**,
**Factory Method / Abstract Factory**, **Decorator**, **Composite** y **Observer**.
Cada ejemplo está ambientado en casos de uso reales del estudiante.

## Información del Proyecto

- **Artifact ID:** p13_patrones_diseno
- **Group ID:** com.cultodeportivo
- **Versión:** 1.0-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven

## Contenido del Módulo

### 1. `singleton`

- `ConfiguracionApp.java` - Dos variantes: lazy initialization y **Enum Singleton**
  (más robusta, thread-safe y serializable).
- `EjemploSingleton.java` - Demuestra que todas las referencias apuntan a la misma instancia.

### 2. `factory`

- `Notificacion.java` - Producto abstracto.
- `NotificacionFactory.java` - Factory con *template method* (`enviarNotificacion`)
  y *factory method* (`crearNotificacion`).
- `EmailFactory.java`, `SmsFactory.java`, `PushFactory.java` - Fábricas concretas.
- `producto/NotificacionEmail.java`, `NotificacionSMS.java`, `NotificacionPush.java`.
- `EjemploFactory.java` - El cliente solo conoce la abstracción (principio abierto/cerrado).

### 3. `decorator`

- `Formateador.java` - Componente común.
- `TextoSimple.java` - Componente concreto (texto base).
- `TextoDecorador.java` - Decorador abstracto que envuelve un `Formateador`.
- `decorador/MayusculaDecorador.java`, `InvertirDecorador.java`,
  `ReemplazarEspaciosDecorador.java`, `CorcheteDecorador.java`.
- `EjemploDecorador.java` - Encadenamiento de decoradores (el orden importa).

### 4. `composite`

- `Componente.java` - Componente abstracto (hoja y rama).
- `Archivo.java` - Hoja (sin hijos).
- `Directorio.java` - Rama (contiene archivos y directorios); delega recursivamente.
- `EjemploComposite.java` - Árbol de directorios con `mostrar`, `buscar` y `remove`.

### 5. `observer`

- `Observador.java` - Interfaz `@FunctionalInterface` del suscriptor.
- `Observable.java` - Sujeto (publicador) que mantiene la lista de observadores.
- `SistemaNotas.java` - Sujeto concreto; notifica con un `record EventoNota`.
- `EjemploObserver.java` - Varios observadores (app, correo, log, riesgo académico)
  reaccionan cuando se publica una nota.

## Conceptos Clave Aprendidos

| Patrón | Problema que resuelve | Clave |
|--------|-----------------------|-------|
| Singleton | Una sola instancia global | Constructor privado / enum |
| Factory | Crear objetos sin acoplar al concreto | Abstracción de creación (OCP) |
| Decorator | Añadir comportamiento sin herencia | Envolver objetos con misma interfaz |
| Composite | Tratar hojas y ramas por igual | Interfaz común + recursión |
| Observer | Notificar cambios a múltiples interesados | Sujeto + lista de suscriptores (pub/sub) |

## Ejecución de Ejemplos

```bash
cd p13_patrones_diseno
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.<paquete>.<Clase>"
```

Por ejemplo:

```bash
mvn exec:java -Dexec.mainClass="com.cultodeportivo.factory.EjemploFactory"
mvn exec:java -Dexec.mainClass="com.cultodeportivo.composite.EjemploComposite"
```

## Estructura de Paquetes

```text
com.cultodeportivo
├── composite/
├── decorator/
│   └── decorador/
├── factory/
│   └── producto/
├── observer/
└── singleton/
```

## Notas Técnicas

- El **Enum Singleton** (Joshua Bloch, *Effective Java*) es la forma más robusta.
- En Decorator, los decoradores comparten la interfaz del componente → anidables.
- En el JDK, los streams de I/O (`BufferedInputStream`, `DataInputStream`) son decoradores.
- `Observador` es funcional → cada suscriptor puede expresarse como lambda.

## Referencias

- [Refactoring Guru - Design Patterns](https://refactoring.guru/design-patterns)
- [Effective Java - Joshua Bloch](https://www.oreilly.com/library/view/effective-java/9780134686097/)
