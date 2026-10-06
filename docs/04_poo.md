# P04 - Programación Orientada a Objetos

## Descripción General

Módulo central de Programación Orientada a Objetos (POO): clases, objetos,
encapsulamiento, paquetes, sobrecarga, herencia, clases abstractas, interfaces,
genéricos y excepciones personalizadas.

## Información del Proyecto

- **Artifact ID:** p04_poo
- **Group ID:** com.cultodeportivo
- **Versión:** 1.0-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven

## Contenido del Módulo

### 1. `p01_poo` — Clases, objetos y enumeraciones

- `Automovil.java` - Clase con atributos, constructores sobrecargados, getters/setters,
  `equals`/`hashCode`, `Comparable` y relaciones con `Rueda`, `Motor`, `Persona`.
- `Rueda.java` - Objeto que compone a `Automovil` (relación "tiene-un").
- `Motor.java` - Sobrecarga de constructores.
- `Persona.java` - Dueño del automóvil.
- `Estanque.java` - Atributo estático/constante compartida.
- `Color.java`, `TipoAutomovil.java`, `TipoMotor.java` - Enumeraciones (`enum`).
- `pruebas/EjemploAutomovil.java`, `EjemploAutomovilArreglo.java`,
  `EjemploAutomovilEnum.java`, `EjemploAutomovilRelacionesObjetos.java` - Ejemplos de uso.

### 2. `p02_paquetes` — Paquetes y visibilidad

- `hogar/Persona.java`, `hogar/Gato.java` (package-private), `hogar/ColorPelo.java`, `hogar/Main.java`.
- `jardin/Perro.java`, `jardin/Main.java` - Acceso entre paquetes y modificadores de visibilidad.

### 3. `p03_ejemplo_facturas` — Caso práctico

- `Producto.java`, `ItemFactura.java`, `Factura.java`, `Cliente.java`, `Main.java`
  - Modelo de facturación con composición de objetos.

### 4. `p04_sobrecarga` — Sobrecarga de métodos

- `statico/Calculadora.java` + `Main.java` - Sobrecarga de métodos estáticos.
- `varargs/Calculadora.java` + `Main.java` - Uso de argumentos variables (`...`).

### 5. `p05_herencia` — Herencia

- `Persona.java` (base), `Alumno.java`, `AlumnoInternacional.java`,
  `AlumnoInternacionalDiplomatico.java`, `Profesor.java`.
- `pruebas/EjemploHerencia.java`, `EjemploHerenciaConstructores.java`,
  `EjemploHerenciaToString.java` - Cadena de constructores, `super`, `toString`.

### 6. `p06_clases_abstractas` — Clases abstractas

- `EjemploForm.java` - Ejemplo principal de formularios.
- `elementos/ElementoForm.java` (abstracta), `InputForm.java`, `SelectForm.java`,
  `TextareaForm.java`, `select/Opcion.java`.
- `validador/Validador.java` (abstracta) y validadores: `EmailValidador.java`,
  `LargoValidador.java`, `NoNuloValidador.java`, `NumeroValidador.java`,
  `RequeridoValidador.java`, `mensaje/MensajeFormateable.java` (interfaz).

### 7. `p07_interfaces` — Interfaces

- `interfaces/Imprimible.java`, `Hoja.java` (abstracta), `Libro.java`, `Pagina.java`,
  `Curriculo.java`, `Informe.java`, `Persona.java`, `Genero.java`, `Main.java`.
- `interfaces_repositorio/` - Repositorio en memoria:
  `repositorio/CrudRepositorio.java`, `OrdenableRepositorio.java`,
  `PaginableRepositorio.java`, `Direccion.java`, `ClienteListRepositorio.java`,
  `modelo/Cliente.java`, `Main.java`.
- `interfaces_repositorio_herencia_interfaces/` - Una interfaz que hereda de varias:
  `OrdenablePaginableCrudRepositorio.java`, `ContableRepositorio.java`, etc.

### 8. `p08_genericos` — Genéricos y excepciones

- `clases_genericas/` - `Camion<T>` (genérica e `Iterable`), `EjemploGenericos.java`,
  modelos `Animal`, `Automovil`, `Maquinaria`.
- `metodos_genericos/` - `EjemploGenericos.java` con métodos genéricos, comodines
  (`? extends`), restricciones múltiples (`<T extends Cliente & Comparable<T>>`).
- `interfaces_repositorio_genericos_y_excepciones/` - Repositorio genérico
  (`AbstractaListRepositorio<T extends BaseEntity>`) con excepciones personalizadas:
  `AccesoDatoException`, `EscrituraAccesoDatoException`,
  `LecturaAccesoDatoException`, `RegistroDuplicadoAccesoDatoException`.
  Modelos: `BaseEntity`, `Cliente`, `ClientePremium`, `Producto`.

## Conceptos Clave Aprendidos

1. **Clases y objetos:** atributos, métodos, constructores y `this`.
2. **Encapsulamiento:** modificadores de acceso y getters/setters.
3. **Paquetes:** organización del código y visibilidad package-private.
4. **Sobrecarga:** métodos con el mismo nombre y distintos parámetros; `varargs`.
5. **Herencia:** `extends`, `super`, sobrescritura y cadenas de constructores.
6. **Clases abstractas:** métodos abstractos y plantillas.
7. **Interfaces:** contratos, `default`/`static`, herencia múltiple de interfaces.
8. **Genéricos:** clases y métodos genéricos, comodines y restricciones de tipo.
9. **Excepciones:** jerarquía propia y `throws`/`catch`.

## Ejecución de Ejemplos

```bash
cd p04_poo
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.p01_poo.pruebas.EjemploAutomovil"
```

Otros ejemplos: `com.cultodeportivo.p03_ejemplo_facturas.Main`,
`com.cultodeportivo.p06_clases_abstractas.EjemploForm`,
`com.cultodeportivo.p08_genericos.clases_genericas.EjemploGenericos`.

## Estructura de Paquetes

```text
com.cultodeportivo
├── p01_poo/
├── p02_paquetes/{hogar, jardin}
├── p03_ejemplo_facturas/
├── p04_sobrecarga/{statico, varargs}
├── p05_herencia/{, pruebas}
├── p06_clases_abstractas/{, elementos, validador}
├── p07_interfaces/{interfaces, interfaces_repositorio, interfaces_repositorio_herencia_interfaces}
└── p08_genericos/{clases_genericas, metodos_genericos, interfaces_repositorio_genericos_y_excepciones}
```

## Notas Técnicas

- Las enumeraciones son tipos de datos que agrupan constantes con nombre.
- `Comparator` y `Comparable` se usan para ordenar objetos (listas y `TreeSet`/`TreeMap`).
- Los repositorios genéricos aplican el principio de inversión de dependencias (DIP) de SOLID.

## Referencias

- [Object-Oriented Programming Concepts - Oracle](https://docs.oracle.com/javase/tutorial/java/concepts/)
- [Generics - Oracle](https://docs.oracle.com/javase/tutorial/java/generics/)
