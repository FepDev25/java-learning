# 04 · Programación orientada a objetos

## Contenido

- [Estado, comportamiento y colaboración](#estado-comportamiento-y-colaboración)
- [1. Clase, objeto y encapsulación](#1-clase-objeto-y-encapsulación)
- [2. Visibilidad y nombres](#2-visibilidad-y-nombres)
- [3. Repartir responsabilidades](#3-repartir-responsabilidades)
- [4. Misma operación, distintas firmas](#4-misma-operación-distintas-firmas)
- [5. Especialización y polimorfismo](#5-especialización-y-polimorfismo)
- [6. Definir una base incompleta](#6-definir-una-base-incompleta)
- [7. Contratos que varias clases cumplen](#7-contratos-que-varias-clases-cumplen)
- [8. Reutilizar sin perder el tipo](#8-reutilizar-sin-perder-el-tipo)
- [Preguntas de repaso](#preguntas-de-repaso)

## Estado, comportamiento y colaboración

En los capítulos anteriores, los datos y las instrucciones se recorrían principalmente desde `main`. POO permite reunir estado y comportamiento en objetos que colaboran. Un automóvil conoce sus componentes; una factura calcula su total a partir de sus ítems; un formulario pide a sus validadores que comprueben valores. La pregunta pasa de «¿qué instrucción escribo?» a «¿qué objeto debe hacerse responsable de esto?».

El capítulo desarrolla clases, paquetes, facturas, sobrecarga, herencia, clases abstractas, interfaces y genéricos. Las clases llamadas `Persona`, `Cliente` o `Calculadora` en distintos paquetes son tipos distintos. El paquete y los imports determinan qué tipo corresponde a cada referencia.

## 1. Clase, objeto y encapsulación

La `clase Automovil` define atributos como fabricante, modelo, color y motor. La clase es la definición; cada `new Automovil(...)` crea una instancia con su propio estado. Dos autos pueden tener el mismo fabricante y seguir siendo objetos diferentes.

`private` impide que el cliente acceda directamente a esos campos. Los getters leen y los setters modifican mediante una API. Encapsular no es solo generar getters y setters: también consiste en controlar invariantes. En esta implementación algunos setters aceptan cualquier valor o incluso devuelven arreglos internos, por lo que no se garantiza todavía una protección completa del estado.

Fragmento del ejemplo de modelado de automóviles:

```java
Motor motorSubaru = new Motor(2.0, TipoMotor.GASOLINA);
Automovil subaru = new Automovil("Subaru", "Impreza");
subaru.setMotor(motorSubaru);
subaru.setEstanque(new Estanque());
subaru.setColor(Color.BLANCO);
subaru.setFabricante("Subaru Fab");
```

Primero se crea el motor. Después se crea el auto y se le asigna una referencia al motor; no se copia automáticamente todo su contenido. Los setters posteriores reemplazan parte de su estado. `this.fabricante` dentro de la clase representa el campo del objeto receptor; `fabricante` como parámetro representa la variable local recibida.

### Estado privado y acceso mediante métodos

```java
private int id;
private String fabricante;
private String modelo;
private Color color = Color.GRIS;
private Motor motor;
private Estanque estanque;
private Persona conductor;
private Rueda[] ruedas;
private int indiceRuedas;
```

Cada instancia conserva sus propios campos. Una referencia a `Motor` permite relacionar el auto con un objeto que tiene cilindrada y tipo; el arreglo `Rueda[]` reúne varios componentes. Declarar campos privados evita que el código externo los modifique directamente, pero una API pública todavía debe decidir qué valores admite.

```java
public void setFabricante(String fabricante) {
    this.fabricante = fabricante;
}
```

`this.fabricante` designa el campo del receptor y el otro `fabricante`, el argumento. El setter mostrado asigna sin validar: el acceso controlado permite introducir validación, aunque no la implementa automáticamente.

### Constructores y estado inicial

Un constructor se llama al crear el objeto, se llama como la clase y no declara tipo de retorno. El constructor vacío de `Automovil` asigna `id = ++ultimoId` y crea un arreglo de cinco ruedas. Los demás constructores encadenan con `this(...)`, así no duplican esa inicialización. El color empieza en `Color.GRIS` si no se sustituye.

El contador `ultimoId` es estático: lo comparten los objetos de esa clase, mientras que cada `id` pertenece a una instancia. Es útil para practicar, pero no es una secuencia persistente de una base de datos ni un contador seguro para varios hilos. Además, el setter permite alterar el ID.

`getEstanque()` crea un estanque por defecto si falta; ese getter tiene un efecto sobre el estado. `Estanque()` establece capacidad 40. La distinción importa: no todos los getters son necesariamente operaciones sin cambios.

### Encadenamiento de constructores

```java
public Automovil() {
    this.id = ++ultimoId; // Incrementa y asigna el ID único
    this.ruedas = new Rueda[5]; // Inicializa el arreglo de ruedas con capacidad para 5 ruedas
}
```

El contador compartido avanza antes de asignarse al ID. El arreglo de cinco ruedas empieza con referencias nulas, de modo que crear el contenedor no construye cada rueda.

```java
public Automovil(String fabricante, String modelo) {
    this(); // Llama al constructor por defecto para inicializar ID y ruedas
    this.fabricante = fabricante;
    this.modelo = modelo;
}
```

`this()` reutiliza la inicialización común antes de asignar fabricante y modelo. El objeto es uno solo: llamar otro constructor de la misma clase no crea un segundo automóvil.

### Estado de instancia, `static` y `final`

`color` pertenece a cada auto. `colorPatente` pertenece a la clase: cambiarlo mediante `Automovil.setColorPatente(...)` afecta lo que todos observan. Un método estático no tiene `this`, por eso `calcularConsumoEstatico` usa la capacidad estática, no el estanque de un automóvil particular.

`static final` declara constantes como `VELOCIDAD_MAX_CIUDAD`. `final` en una variable impide reasignarla; si la variable referencia un objeto mutable, eso no vuelve inmutable el objeto. Más adelante, `final` en una clase impide que se herede de ella, y en un método impide su sobrescritura.

### Un dato compartido por la clase

```java
public static void setColorPatente(Color colorPatente){
    Automovil.colorPatente = colorPatente;
}
```

El campo se identifica mediante el nombre de clase porque es estático. Una llamada modifica el dato común; no selecciona una instancia particular. En contraste, `setColor` modifica el campo de un receptor concreto.

### Métodos y sobrecarga

`acelerar` y `frenar` devuelven descripciones. `acelerarFrenar` reutiliza ambos métodos; no hay un motor físico que se esté controlando. Las dos versiones de `calcularConsumo` distinguen porcentaje decimal (`0.6f`) y entero (`60`):

```java
public float calcularConsumo(int km, int porcentajeBencina) {
    return km / (this.getEstanque().getCapacidad() * (porcentajeBencina / 100f));
}
```

`100f` evita una división entera. Con 300 km, capacidad 40 y 60%, el cálculo es `300 / 24 = 12.5` km por litro. No se validan porcentajes ni capacidad; el ejemplo enseña sobrecarga y aritmética, no reglas completas de consumo.

### Enumeraciones y relaciones

`Color`, `TipoMotor` y `TipoAutomovil` restringen valores a opciones conocidas. Un enum también admite atributos, constructor y métodos: `TipoAutomovil.SUV` tiene nombre, descripción y número de puertas. `values()` permite recorrer las constantes, como muestra el ejemplo de enumeraciones de automóviles.

El automóvil **tiene** motor, estanque, conductor y ruedas. No **es** ninguno de ellos. `Motor`, `Estanque`, `Persona` y `Rueda` dan tipos a estas relaciones. El código ilustra asociación y composición de objetos; no impone por sí solo propiedad exclusiva ni ciclo de vida inseparable de cada componente.

`addRueda` agrega hasta la capacidad y devuelve `this`, permitiendo encadenar llamadas sobre el mismo auto. Si está lleno, ignora la nueva rueda. El ejemplo de relaciones entre objetos muestra esas conexiones; el ejemplo de ordenamiento de automóviles reúne objetos en un arreglo y los ordena.

### Una opción de dominio con atributos

```java
SEDAN("Sedan", "Auto mediano", 4),
STATION_WAGON("Station Wagon", "Auto grande", 5),
HATCHBACK("Hatchback", "Auto compacto", 5),
PICKUP("Pickup", "Camioneta", 4),
COUPE("Coupé", "Auto pequeño", 2),
CONVERTIBLE("Convertible", "Auto deportivo", 2),
FURGON("Furgón", "Auto utilitario", 3),
SUV("SUV", "Todo terreno deportivo", 5);
```

Cada constante construye una instancia del enum con los argumentos indicados. `SUV` incluye nombre, descripción y cinco puertas. El conjunto cerrado de constantes evita usar una cadena arbitraria como categoría; los atributos permiten asociar información a cada opción.

```java
public Automovil addRueda(Rueda rueda){
    if(indiceRuedas < this.ruedas.length) {
        this.ruedas[indiceRuedas++] = rueda;
    }
    return this;
}
```

La condición comprueba la capacidad antes de escribir. `indiceRuedas++` utiliza primero el índice disponible y después avanza. El retorno de `this` permite agregar varias ruedas encadenando llamadas sobre el mismo automóvil, sin producir copias.

### Identidad, igualdad, representación y orden

En el ejemplo de modelado de automóviles, los dos Nissan son objetos separados. `==` da falso, mientras que `equals` da verdadero porque compara fabricante y modelo, aunque los motores difieran. `hashCode` utiliza los mismos datos para respetar la regla de que objetos iguales deben tener igual hash. `toString` devuelve ID, fabricante y modelo para imprimir una descripción.

`compareTo` ordena solo por fabricante; dos autos de la misma marca pueden dar cero aunque sus modelos sean diferentes y `equals` sea falso. Esto será relevante en `TreeSet`: orden e igualdad deben diseñarse con cuidado. Además, comparar con fabricante nulo falla. Mutar campos utilizados por hash u orden después de insertar en una colección puede invalidar su organización.

### Igualdad definida por datos de negocio

```java
public boolean equals(Object obj) {

    if(this == obj){
        return true;
    }
    if(!(obj instanceof Automovil)){
        return false;
    }
    Automovil a = (Automovil) obj;
    return (this.fabricante != null && this.modelo != null
            && this.fabricante.equals(a.getFabricante())
            && this.modelo.equals(a.getModelo()));
}
```

La identidad consigo mismo se acepta primero. Después se rechaza un tipo incompatible y se compara fabricante y modelo. Los campos como motor o color no participan; esto explica que dos vehículos con motores distintos puedan resultar iguales bajo esta política.

```java
public int compareTo(Automovil a) {
    return this.fabricante.compareTo(a.fabricante);
}
```

El orden solo considera fabricante. Una marca igual produce cero en comparación aunque los modelos difieran. Igualdad y orden cumplen funciones diferentes, y una estructura ordenada puede usar ese cero para decidir unicidad.

## 2. Visibilidad y nombres

Los paquetes `hogar` y `jardin` organizan tipos y delimitan acceso. Una clase `public` como `hogar.Persona` se puede importar desde `jardin`. `Gato` no declara `public`, así solo es accesible en su propio paquete. Un subpaquete no recibe acceso especial: `a.b` y `a.b.c` son paquetes diferentes.

| Modificador de miembro | Acceso principal |
| --- | --- |
| `private` | Dentro de la clase que lo declara y su contexto anidado |
| Sin modificador | Dentro del mismo paquete |
| `protected` | Mismo paquete y acceso de subclases con las reglas de herencia |
| `public` | Desde código que pueda acceder al tipo |

`Perro.nombre` y `raza` son `protected`; el `Main` de jardín los usa porque pertenece al mismo paquete. No los usa por ser una subclase. `jugar` tiene acceso de paquete y llama a `persona.lanzarPelota()`, que es público. Las constantes de género y `saludar` muestran miembros estáticos de `Persona`.

### Visibilidad dentro de un paquete

```java
// Extracto de una clase con visibilidad de paquete.
class Gato {
    @Override
    public String toString() {
        return "Soy un gato";
    }
}
```

La ausencia de `public` en la clase limita desde dónde puede referenciarse, aunque su método sea público. El método no vuelve pública a la clase. La accesibilidad de un miembro depende también de poder acceder al tipo que lo declara.

```java
protected String nombre;
protected String raza;

String jugar(Persona persona){
    return persona.lanzarPelota();
}
```

Los dos campos protegidos y el método sin modificador permiten el uso mostrado dentro del paquete. `jugar` delega en una operación pública de la persona: colaboración no implica herencia entre ambos tipos.

## 3. Repartir responsabilidades

La `factura` referencia un cliente y un arreglo de ítems. Cada `ItemFactura` conoce cantidad y producto; cada producto conoce precio y nombre. Así se evita que `main` calcule todo manualmente.

```java
public float calcularImporte(){
    return this.cantidad * this.producto.getPrecio();
}
```

Ese método de `ItemFactura` calcula el subtotal. `Factura.calcularTotal` recorre los ítems, omite posiciones nulas y suma esos subtotales. Adaptación de datos: tres unidades a 10 y dos unidades a 5 generan 30 + 10 = 40. `generarDetalle` construye el texto con `StringBuilder`, fecha y subtotales; `toString` delega en él.

La capacidad `MAX_ITEMS` es 12, aunque el `Main` carga cinco ítems. `indiceItems` separa capacidad de cantidad cargada. `addItemFactura` ignora entradas posteriores al límite, sin avisar. El cliente y los productos deben existir; hay pocas validaciones de cantidades y precios. `float` simplifica el curso, pero un dominio monetario que necesite exactitud decimal exige otra representación, por ejemplo `BigDecimal` con reglas de redondeo explícitas.

El `Main` mezcla `nextFloat`, `nextInt` y `nextLine`. Consume el salto pendiente antes de pedir el siguiente nombre, mostrando por qué la entrada necesita una secuencia consistente.

### Agregar ítems y acumular subtotales

```java
public void addItemFactura(ItemFactura item) {
    if (indiceItems < MAX_ITEMS) {
        this.items[indiceItems++] = item;
    }
}
```

El índice cuenta posiciones ocupadas y el límite fija capacidad. El elemento se almacena en el índice actual antes de incrementarlo. El ejemplo ignora una inserción cuando se alcanza el límite, sin devolver un resultado que permita detectarla.

```java
public float calcularTotal() {
    float total = 0.0f;
    for (ItemFactura item : this.items) {
        if (item == null) {
            continue;
        }
        total += item.calcularImporte();
    }
    return total;
}
```

Cada ítem calcula su importe y la factura coordina la suma. Las posiciones todavía nulas se omiten. La separación deja precio y cantidad en el ítem y producto, mientras que la factura solo necesita conocer sus subtotales.

## 4. Misma operación, distintas firmas

Las calculadoras de `varargs` y `statico` declaran múltiples `sumar`. Sobrecargar significa mismo nombre y distintos parámetros: número, tipos u orden. Cambiar solo el retorno no crea una sobrecarga válida.

La variante de instancia necesita `new Calculadora()`. La estática tiene constructor privado y se llama a través de la clase o del import estático mostrado en su `Main`.

```java
public static int sumar(int... argumentos) {
    int total = 0;
    for (int i : argumentos) {
        total += i;
    }
    return total;
}
```

`int...` permite cero o más argumentos y se ve dentro como un arreglo; debe ser el último parámetro. El compilador elige una firma según los tipos de la llamada y sus reglas de conversión. `sumar(10, 5)` usa la firma fija de dos enteros; cuatro enteros requieren varargs. `sumar(10, '@')` puede ampliar el `char` a entero y suma 64. Una llamada sin argumentos puede ser ambigua si existen varargs de `int` y `double`.

`sumar(String, String)` interpreta ambos textos; si el parseo falla devuelve cero. Ese cero no distingue error de una suma legítima igual a cero. La sobrecarga ocurre al compilar; la sobrescritura de herencia se resuelve mediante el objeto real al ejecutar.

### Firmas fijas y cantidad variable de parámetros

```java
// Sobrecargas reducidas de la calculadora estática.
public static int sumar(int a, int b) {
    return a + b;
}

public static double sumar(double a, double b) {
    return a + b;
}

public static int sumar(int... argumentos) {
    int total = 0;
    for (int valor : argumentos) {
        total += valor;
    }
    return total;
}
```

Dos enteros eligen la firma fija de `int`; dos literales decimales ordinarios eligen la de `double`. Cuatro enteros requieren la firma de cantidad variable. Las versiones comparten nombre, pero cada lista de parámetros identifica un contrato distinto. Cambiar únicamente el retorno no permite duplicar una firma.

## 5. Especialización y polimorfismo

La jerarquía es `Persona → Alumno → AlumnoInternacional` y `Persona → Profesor`. Un alumno **es una** persona; un profesor también. El código común de nombre, apellido, edad y correo se concentra en `Persona`. Los campos privados siguen accediéndose mediante sus métodos; una subclase no recibe acceso directo por heredar.

`super(...)` llama al constructor del padre y permite inicializar primero su parte del objeto. `this(...)` encadena constructores de la misma clase. Los constructores no se heredan. Los mensajes impresos por constructores sin argumentos ayudan a ver el orden desde padre hacia hijo.

`AlumnoInternacional` sobrescribe el promedio:

```java
@Override
public double calcularPromedio() {
    return ((super.calcularPromedio() * 3) + notaIdiomas) / 4;
}
```

El promedio del padre resume tres notas; multiplicarlo por tres recupera su suma y permite añadir idiomas con peso igual. `@Override` hace que el compilador compruebe la intención de sobrescribir. La clase es `final`, así no puede tener subclases en el estado actual del módulo.

`imprimir(Persona persona)` recibe alumnos y profesores mediante una referencia amplia. `persona.saludar()` ejecuta la versión del objeto real: ese es el polimorfismo. Para acceder a datos exclusivos, comprueba `instanceof Alumno alumno`; esa forma declara la variable del tipo concreto dentro de la rama segura. El ejemplo de representación de una jerarquía imprime representaciones que amplían las del padre; el ejemplo de jerarquía de personas inspecciona la jerarquía con `getSuperclass`.

### Ampliar comportamiento heredado

```java
public String saludar() {
    String saludar = super.saludar();
    return saludar + " soy un alumno y mi nombre es " + getNombre();
}
```

`super.saludar()` obtiene la parte común definida por el padre. El retorno añade el detalle de alumno sin duplicar el saludo inicial. El tipo de la referencia puede ser `Persona`; cuando el objeto real es alumno, la llamada utiliza esta sobrescritura.

```java
// Reducción del método que recibe distintas especializaciones.
public static void imprimir(Persona persona) {
    System.out.println(persona.saludar());
    if (persona instanceof Alumno alumno) {
        System.out.println("Promedio: " + alumno.calcularPromedio());
    }
}
```

La primera operación utiliza el contrato común; la segunda exige una comprobación de tipo porque calcular promedio es específico de alumno. La variable declarada por el patrón solo está disponible donde se ha demostrado esa pertenencia.

## 6. Definir una base incompleta

`ElementoForm` reúne nombre, valor, validadores y errores, pero declara `dibujarHtml()` abstracto. No se puede instanciar `new ElementoForm(...)` directamente; se requiere una subclase concreta o completar el método mediante una clase anónima.

`InputForm`, `TextareaForm` y `SelectForm` dibujan diferentes fragmentos HTML. Una `List<ElementoForm>` puede contener todos y llamar al mismo contrato; el despacho dinámico elige el dibujo adecuado. La clase anónima del ejemplo de formularios y validación demuestra una implementación puntual sin declarar otro archivo.

La validación se compone: `addValidador` añade objetos y devuelve `this`. Cada validador responde a `esValido(valor)`:

| Validador | Regla implementada |
| --- | --- |
| `RequeridoValidador` | Valor no nulo y longitud mayor que cero |
| `NoNuloValidador` | Valor no nulo; puede estar vacío |
| `NumeroValidador` | Texto interpretable como `Integer` |
| `EmailValidador` | Expresión simple con texto a ambos lados de `@` |
| `LargoValidador` | Longitud entre mínimo y máximo; permite `null` |

Permitir nulo en longitud permite combinarla con «requerido» cuando sea necesario. Un requerido con espacios pasa porque no usa `isBlank`. La regla de correo es didáctica, no valida todos los requisitos de una dirección real; además invoca `matches` y puede fallar con nulo. La interfaz `MensajeFormateable` permite que `LargoValidador` incorpore campo y límites al mensaje.

**Límites actuales:** `esValido()` agrega errores sin limpiar los anteriores; después de corregir un valor puede seguir dando falso por errores acumulados. `SelectForm.dibujarHtml()` asigna el valor de una opción seleccionada al dibujar: validar antes de dibujar puede dar un resultado diferente. El HTML se concatena sin escape de contenido. Estas observaciones explican el comportamiento real sin modificar el ejercicio.

### Una operación abstracta y una concreta

```java
// Contrato mínimo y estado común del elemento de formulario.
public abstract class ElementoForm {
    protected String valor;
    protected String nombre;

    public void setValor(String valor) {
        this.valor = valor;
    }

    public abstract String dibujarHtml();
}
```

El esquema reduce la base a lo necesario para mostrar la relación: la asignación de valor está implementada y el dibujo queda pendiente. Los validadores y la lista de errores se explican mediante las operaciones siguientes.

```java
public String dibujarHtml() {
    return "<input type=\"" + this.tipo
            + "\" name=\"" + this.nombre
            + "\" value=\"" + this.valor + "\">";
}
```

La subclase decide la estructura del campo HTML utilizando el estado heredado y su tipo de entrada. La misma llamada a `dibujarHtml` produce estructuras distintas para un textarea o una selección.

```java
public boolean esValido(String valor) {
    return (valor != null && valor.length() > 0);
}
```

La primera parte del `&&` protege el acceso a `length`: con nulo, la segunda no se evalúa. El criterio admite cadenas de espacios porque solo comprueba longitud positiva.

```java
public boolean esValido(){
    for(Validador v: validadores){
        if(!v.esValido(this.valor)){
            if(v instanceof MensajeFormateable mensajeFormateable) {
                this.errores.add(mensajeFormateable.getMensajeFormateado(nombre));
            } else {
                this.errores.add(String.format(v.getMensaje(), nombre));
            }
        }
    }
    return this.errores.isEmpty();
}
```

Cada fallo agrega un mensaje. El tipo de validador decide si formatea mediante el contrato especializado o mediante una plantilla común. La lista no se limpia al comienzo; un error anterior puede seguir afectando al resultado de una nueva validación.

## 7. Contratos que varias clases cumplen

Una interfaz describe capacidades; una clase puede implementar varias aunque solo extienda una clase. `Curriculo`, `Informe`, `Pagina` y `Libro` trabajan con impresión. `Hoja` aporta contenido común como base abstracta; `Libro` contiene una lista de `Imprimible` y pide a cada página que se imprima.

`Imprimible` tiene `default String imprimir()` y un método estático auxiliar. El comentario dla implementación dice que las clases deben definir `imprimir`, pero el método **default ya proporciona implementación**: sobrescribirlo es opcional. La interfaz no tiene un único método abstracto; no puede usarse como lambda por esa firma default. Sus constantes son implícitamente públicas, estáticas y finales.

### Implementación predeterminada en una interfaz

```java
String TEXTO_DEFECTO = "Imprimiendo un valor por defecto"; // Constante de interfaz

default String imprimir(){
    return TEXTO_DEFECTO;
}
```

La constante está asociada a la interfaz. El método `default` aporta una implementación que una clase puede conservar o sobrescribir. La presencia de ese cuerpo lo diferencia de un método abstracto que exige implementación concreta.

### Repositorios por capacidades

En `interfaces_repositorio`, `CrudRepositorio` define crear, listar, obtener, editar y eliminar clientes; `OrdenableRepositorio` y `PaginableRepositorio` añaden formas de consulta. `ClienteListRepositorio` implementa los tres y usa una lista en memoria: cerrar el proceso pierde los datos.

`porId` devuelve nulo si no encuentra; `editar` intenta usar el resultado y puede fallar si el ID no existe. La variante `interfaces_repositorio_herencia_interfaces` reúne capacidades con `OrdenablePaginableCrudRepositorio` y añade conteo. Una interfaz puede **extender varias interfaces**; una clase implementa el contrato combinado.

La paginación utiliza `subList(desde, hasta)`: incluye `desde` y excluye `hasta`, con índices de lista, no IDs. Es una vista respaldada por la lista original. Ordenar crea `new ArrayList<>(dataSource)`: cambia el orden de esa copia, aunque los objetos contenidos siguen siendo compartidos.

### Vista por intervalo frente a copia de la lista

```java
// Operaciones utilizadas por el repositorio en memoria.
List<Cliente> pagina = dataSource.subList(1, 3);
List<Cliente> copiaOrdenable = new ArrayList<>(dataSource);
```

La página abarca los índices 1 y 2 y sigue respaldada por la lista original. La segunda expresión crea otro contenedor que puede ordenarse sin cambiar el orden de `dataSource`. Ninguna realiza una copia profunda de cada cliente: los objetos contenidos siguen siendo compartidos.

### Un contrato de acceso independiente del almacenamiento

```java
public interface CrudRepositorio {
    List<Cliente> listar();
    Cliente porId(Integer id);
    void crear(Cliente cliente);
    void editar(Cliente cliente);
    void eliminar(Integer id);
}
```

Las firmas describen operaciones sobre clientes sin exponer la lista utilizada para almacenarlos. Una implementación puede cambiar el mecanismo manteniendo el contrato. Los retornos y excepciones todavía deben indicar con claridad qué ocurre si no se encuentra un dato.

## 8. Reutilizar sin perder el tipo

`Camion<T>` permite expresar `Camion<Animal>`, `Camion<Automovil>` o `Camion<Maquinaria>` sin convertir cada lectura desde `Object`. `T` es un parámetro de tipo, no una clase fija llamada T. La clase implementa `Iterable<T>` devolviendo un iterador de su lista, así funciona el `for-each`.

**Límite real:** su `add` comprueba `size() <= max`. Cuando hay exactamente `max` objetos todavía acepta uno más; la capacidad efectiva llega a `max + 1`. Una comprobación estricta usaría `< max` antes de agregar. Es una mejora propuesta, no una corrección ya aplicada a la implementación.

Los métodos de `metodos_genericos/EjemploGenericos` enseñan parámetros y restricciones:

```java
public static <T extends Comparable<T>> T maximo(T a, T b, T c){
    T max = a;
    if(b.compareTo(max) > 0){
        max = b;
    }
    if(c.compareTo(max) > 0){
        max = c;
    }
    return max;
}
```

`<T ...>` antes del retorno declara el parámetro del método. La restricción permite llamar a `compareTo` y usar la misma lógica para números o cadenas. `<T extends Cliente & Comparable<T>>` exige ambas capacidades; aquí `extends` también se utiliza al expresar un límite de interfaz.

`List<ClientePremium>` no es subtipo de `List<Cliente>`, aunque un cliente premium sí sea un cliente. `List<? extends Cliente>` permite leer listas de clientes o subclases como clientes, pero no insertar arbitrariamente un `Cliente`: el subtipo concreto del contenedor no está determinado por esa referencia. Los genéricos no aceptan primitivos directamente; se usa `Integer`.

`fromArrayToList` retorna `Arrays.asList`, una lista de tamaño fijo respaldada por el arreglo: no permite `add` o `remove`, aunque sí reemplazar posiciones. Los `ClientePremium.compareTo` del módulo retornan siempre cero; cumplen la firma, pero no implementan un criterio de orden significativo.

### Contenedor con un parámetro de tipo

```java
// Estructura reducida del contenedor genérico.
public class Camion<T> implements Iterable<T> {
    private List<T> objetos = new ArrayList<>();

    @Override
    public Iterator<T> iterator() {
        return objetos.iterator();
    }
}
```

Al elegir `Camion<Animal>`, la lista y el iterador trabajan con animales; al elegir otra T, conservan la misma lógica. `Iterable<T>` permite que un bucle `for-each` solicite el iterador, sin conocer cómo se guardan los elementos.

```java
public static void imprimirClientes(List<? extends Cliente> clientes){
    clientes.forEach(System.out::println);
}
```

El comodín acepta listas cuyo elemento concreto sea cliente o una subclase. Cada lectura es compatible con `Cliente`; insertar un cliente cualquiera no es seguro porque la lista podría exigir exclusivamente un subtipo más específico.

### Repositorio genérico y excepciones

`CrudRepositorio<T>` sustituye clientes fijos por un parámetro. `AbstractaListRepositorio` restringe `T extends BaseEntity` para poder leer `getId`. Reutiliza almacenamiento, creación, búsqueda, eliminación, paginación y conteo. `ClienteListRepositorio` y `ProductoListRepositorio` completan edición y criterios de orden según sus campos.

`BaseEntity` compara ID y clase concreta; así un producto y un cliente no son iguales solo por coincidir en ID. El contador estático es compartido por los modelos que usan esa base y solo vive en memoria.

La jerarquía de errores parte de `AccesoDatoException extends Exception`, una excepción comprobada: quien invoca debe capturarla o declararla. `LecturaAccesoDatoException` identifica ID inválido o inexistente; `EscrituraAccesoDatoException` identifica una entrada nula; `RegistroDuplicadoAccesoDatoException` especializa la de escritura para duplicados. `throw` lanza un error; `throws` declara que un método puede propagarlo. Captura primero la clase específica y después la general, como hacen los ejemplos.

El beneficio es comunicar el fallo con contexto en vez de devolver silenciosamente nulo. Sigue habiendo límites: `editar` no valida un argumento nulo antes de leer su ID, `listar()` expone la lista interna, y un campo de orden desconocido hace que el comparador devuelva cero. Una firma genérica correcta no sustituye la validación de datos.

### Reutilizar creación con restricciones y fallos explícitos

```java
public void crear(T t) throws EscrituraAccesoDatoException{
    if(t == null){
        throw new EscrituraAccesoDatoException("Error al insertar un objeto null");
    }
    if(this.dataSource.contains(t)){
        throw new RegistroDuplicadoAccesoDatoException("Error el objeto con id "
                + t.getId() + " existe en el repositorio");
    }
    this.dataSource.add(t);
}
```

El tipo T pertenece a la jerarquía con ID. Primero se rechaza la ausencia del objeto, luego se comprueba duplicación mediante igualdad y por último se añade. Las excepciones diferencian entrada nula y registro ya existente; ambas impiden llegar a la inserción.

## Preguntas de repaso

1. ¿Auto y motor se relacionan por herencia? **No; un auto tiene una referencia a un motor.**
2. ¿Qué distingue sobrecarga y sobrescritura? **Firmas diferentes elegidas al compilar frente a la implementación de una misma operación elegida por el objeto real.**
3. ¿Un método default obliga a sobrescribir? **No; ofrece una implementación heredable.**
4. ¿Qué habilita `T extends BaseEntity`? **Usar en todos los T las operaciones de esa base, como `getId`.**
5. ¿Por qué capturar primero una excepción específica? **Una captura del padre ya abarcaría sus subclases.**
