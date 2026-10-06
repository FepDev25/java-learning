# Proyecto Integrador - Módulo POO

## Sistema de Gestión Empresarial Integral

### Descripción General

Desarrollar un sistema empresarial completo que integre gestión de empleados, productos, clientes, ventas y reportes. El sistema debe aplicar todos los conceptos de POO: clases, herencia, interfaces, clases abstractas, genéricos, paquetes y patrones de diseño.

### Objetivos de Aprendizaje

- Diseñar jerarquía de clases con herencia multinivel
- Implementar y usar interfaces múltiples
- Crear clases abstractas y métodos abstractos
- Aplicar genéricos en clases y métodos
- Organizar código en paquetes apropiados
- Implementar patrón repositorio
- Aplicar sobrecarga de métodos y constructores
- Usar enumeraciones
- Implementar equals, hashCode, toString, Comparable

### Arquitectura del Proyecto

#### Estructura de Paquetes

```bash
com.empresa.gestion
├── modelo
│   ├── personas
│   │   ├── Persona (clase abstracta)
│   │   ├── Empleado (extiende Persona)
│   │   ├── Cliente (extiende Persona)
│   │   ├── Gerente (extiende Empleado)
│   │   ├── Vendedor (extiende Empleado)
│   │   └── Tecnico (extiende Empleado)
│   ├── productos
│   │   ├── Producto (clase abstracta)
│   │   ├── ProductoFisico (extiende Producto)
│   │   ├── ProductoDigital (extiende Producto)
│   │   └── ProductoServicio (extiende Producto)
│   ├── ventas
│   │   ├── Venta
│   │   ├── ItemVenta
│   │   └── Factura
│   └── enums
│       ├── TipoDocumento
│       ├── EstadoVenta
│       ├── CategoriaProducto
│       └── DepartamentoEmpleado
├── interfaces
│   ├── IValidable
│   ├── ICalculable
│   ├── IImprimible
│   ├── IExportable
│   ├── IAuditable
│   └── IDescuentable
├── repositorio
│   ├── IRepositorio<T> (interfaz genérica)
│   ├── RepositorioBase<T> (clase abstracta genérica)
│   ├── RepositorioEmpleado
│   ├── RepositorioCliente
│   ├── RepositorioProducto
│   └── RepositorioVenta
├── servicios
│   ├── ServicioVentas
│   ├── ServicioNomina
│   ├── ServicioInventario
│   └── ServicioReportes
├── excepciones
│   ├── GestionException (base)
│   ├── EmpleadoNoEncontradoException
│   ├── ProductoNoDisponibleException
│   ├── VentaInvalidaException
│   └── SaldoInsuficienteException
└── util
    ├── Validador
    ├── Formateador
    └── Generador
```

### Requerimientos Funcionales

#### 1. Módulo de Personas

**Clase Abstracta: Persona**
Atributos:

- id (generado automáticamente)
- nombre
- apellido
- tipoDocumento (enum)
- numeroDocumento
- fechaNacimiento
- email
- telefono
- direccion
- fechaRegistro

Métodos abstractos:

- String obtenerInformacionCompleta()
- double calcularBeneficio()

Métodos concretos:

- int calcularEdad()
- boolean esmayor

DeEdad()

- String getNombreCompleto()

Interfaces implementadas:

- IValidable (validar datos)
- IAuditable (registrar cambios)
- Comparable Persona (comparar por apellido y nombre)

**Clase: Empleado extends Persona**
Atributos adicionales:

- numeroEmpleado
- departamento (enum)
- salarioBase
- comisiones
- bonificaciones
- fechaContratacion
- jefeDirecto (Empleado)
- subordinados (List Empleado)

Métodos:

- double calcularSalarioTotal()
- double calcularAntiguedad()
- void agregarSubordinado(Empleado e)
- int contarSubordinados()
- @Override double calcularBeneficio() // retorna salario
- @Override String obtenerInformacionCompleta()

**Clase: Gerente extends Empleado**
Atributos adicionales:

- presupuestoAsignado
- departamentosACargo List DepartamentoEmpleado
- bonoGerencial

Métodos:

- @Override double calcularSalarioTotal() // incluye bono gerencial
- double calcularRendimiento()

**Clase: Vendedor extends Empleado**
Atributos adicionales:

- metaVentas
- ventasRealizadas
- porcentajeComision

Métodos:

- @Override double calcularSalarioTotal() // incluye comisiones
- double calcularComisionTotal()
- boolean cumplioMeta()

**Clase: Tecnico extends Empleado**
Atributos adicionales:

- especialidad
- certificaciones (List String)
- horasExtra

Métodos:

- @Override double calcularSalarioTotal() // incluye pago horas extra
- void agregarCertificacion(String cert)

**Clase: Cliente extends Persona**
Atributos adicionales:

- numeroCliente
- saldoDisponible
- limiteCredito
- puntosFidelidad
- historialCompras (List Venta)

Métodos:

- void realizarCompra(Venta venta)
- void abonarSaldo(double monto)
- boolean tieneSaldoSuficiente(double monto)
- @Override double calcularBeneficio() // retorna puntos de fidelidad
- int calcularNivelFidelidad() // BRONCE, PLATA, ORO, PLATINO

#### 2. Módulo de Productos

**Clase Abstracta: Producto**
Atributos:

- codigo (generado)
- nombre
- descripcion
- categoria (enum)
- precioBase
- descuento
- activo

Métodos abstractos:

- double calcularPrecioFinal()
- String obtenerDetalles()
- boolean validarDisponibilidad()

Métodos concretos:

- void aplicarDescuento(double porcentaje)
- double obtenerPrecioConDescuento()

Interfaces:

- IDescuentable
- IValidable
- Comparable Producto (por precio)

**Clase: ProductoFisico extends Producto**
Atributos adicionales:

- stock
- peso
- dimensiones
- requiereRefrigeracion
- ubicacionAlmacen

Métodos:

- @Override boolean validarDisponibilidad() // stock > 0
- @Override double calcularPrecioFinal() // incluye costo envío
- void reducirStock(int cantidad)
- void incrementarStock(int cantidad)
- boolean hayStockSuficiente(int cantidad)

**Clase: ProductoDigital extends Producto**
Atributos adicionales:

- url
- tamañoMB
- licencias
- licenciasDisponibles
- versionActual

Métodos:

- @Override boolean validarDisponibilidad() // licencias disponibles
- @Override double calcularPrecioFinal() // sin costo envío
- void generarLicencia()
- void revocarLicencia(String licencia)

**Clase: ProductoServicio extends Producto**
Atributos adicionales:

- duracionHoras
- tecnicoAsignado (Tecnico)
- requiereReservacion
- horariosDisponibles (List String)

Métodos:

- @Override boolean validarDisponibilidad() // técnico disponible
- @Override double calcularPrecioFinal() // precio por hora * duración
- void asignarTecnico(Tecnico t)

#### 3. Módulo de Ventas

**Clase: ItemVenta**
Atributos:

- producto (Producto)
- cantidad
- precioUnitario
- descuentoAplicado
- subtotal

Métodos:

- double calcularSubtotal()
- void aplicarDescuento(double porcentaje)

Implementa: ICalculable

**Clase: Venta**
Atributos:

- numeroVenta (generado)
- cliente (Cliente)
- vendedor (Vendedor)
- items (List ItemVenta)
- fecha
- estado (enum: PENDIENTE, PROCESADA, CANCELADA, DEVUELTA)
- metodoPago
- descuentoGlobal

Métodos:

- void agregarItem(Producto p, int cantidad)
- void eliminarItem(int index)
- double calcularSubtotal()
- double calcularImpuestos()
- double calcularDescuentos()
- double calcularTotal()
- void procesarVenta()
- void cancelarVenta()
- boolean validarVenta()

Implementa: ICalculable, IValidable, IImprimible

**Clase: Factura**
Atributos:

- numeroFactura
- venta (Venta)
- fechaEmision
- rfc
- razonSocial

Métodos:

- String generarFactura()
- void imprimirFactura()
- String exportarPDF()

Implementa: IImprimible, IExportable

#### 4. Enumeraciones

**TipoDocumento:**

- DNI, PASAPORTE, CEDULA, RUC, RFC

**EstadoVenta:**

- PENDIENTE, PROCESADA, ENVIADA, ENTREGADA, CANCELADA, DEVUELTA

**CategoriaProducto:**

- ELECTRONICA, ROPA, ALIMENTOS, HOGAR, DEPORTES, LIBROS, JUGUETES

**DepartamentoEmpleado:**

- VENTAS, TECNOLOGIA, RECURSOS_HUMANOS, CONTABILIDAD, OPERACIONES

#### 5. Interfaces

**IValidable:**

```java
public interface IValidable {
    boolean validar();
    List<String> obtenerErroresValidacion();
}
```

**ICalculable:**

```java
public interface ICalculable {
    double calcularTotal();
    Map<String, Double> obtenerDesglose();
}
```

**IImprimible:**

```java
public interface IImprimible {
    String generarReporte();
    void imprimir();
}
```

**IExportable:**

```java
public interface IExportable {
    String exportarCSV();
    String exportarJSON();
    String exportarXML();
}
```

**IAuditable:**

```java
public interface IAuditable {
    void registrarCreacion(String usuario);
    void registrarModificacion(String usuario);
    String obtenerHistorialCambios();
}
```

**IDescuentable:**

```java
public interface IDescuentable {
    void aplicarDescuento(double porcentaje);
    double obtenerDescuento();
    double calcularPrecioConDescuento();
}
```

#### 6. Patrón Repositorio con Genéricos

Interfaz: IRepositorio T

```java
public interface IRepositorio<T> {
    void agregar(T entidad);
    T buscarPorId(int id);
    List<T> listarTodos();
    void actualizar(T entidad);
    void eliminar(int id);
    int contarTodos();
    boolean existe(int id);
}
```

Clase Abstracta: RepositorioBase T

```java
public abstract class RepositorioBase<T> implements IRepositorio<T> {
    protected List<T> datos;

    public RepositorioBase() {
        datos = new ArrayList<>();
    }

    @Override
    public void agregar(T entidad) {
        datos.add(entidad);
    }

    @Override
    public List<T> listarTodos() {
        return new ArrayList<>(datos);
    }

    @Override
    public int contarTodos() {
        return datos.size();
    }

    // Métodos abstractos para implementaciones específicas
    public abstract T buscarPorId(int id);
    public abstract List<T> buscarPorCriterio(String criterio, String valor);
    public abstract List<T> ordenarPor(String campo, boolean ascendente);
}
```

**Implementaciones:**

- RepositorioEmpleado extends RepositorioBase Empleado
- RepositorioCliente extends RepositorioBase Cliente
- RepositorioProducto extends RepositorioBase Producto
- RepositorioVenta extends RepositorioBase Venta

Cada repositorio implementa búsquedas específicas:

- RepositorioEmpleado: buscarPorDepartamento(), buscarPorRangoSalario()
- RepositorioCliente: buscarPorNivel(), buscarConDeuda()
- RepositorioProducto: buscarPorCategoria(), buscarDisponibles()
- RepositorioVenta: buscarPorFecha(), buscarPorCliente()

#### 7. Servicios de Negocio

**ServicioVentas:**

- realizarVenta(Cliente, Vendedor, List ItemVenta)
- cancelarVenta(int numeroVenta)
- devolverProducto(int numeroVenta, int itemIndex)
- aplicarDescuentoGlobal(Venta, double porcentaje)
- generarFactura(Venta)

**ServicioNomina:**

- calcularNominaTotal()
- calcularNominaPorDepartamento(DepartamentoEmpleado)
- procesarPagoEmpleado(Empleado)
- calcularBonificaciones()
- generarReciboNomina(Empleado)

**ServicioInventario:**

- consultarStock(Producto)
- reabastecer(ProductoFisico, int cantidad)
- marcarComoAgotado(Producto)
- generarAlertaStockBajo()
- calcularValorInventario()

**ServicioReportes:**

- reporteVentasPorPeriodo(Date inicio, Date fin)
- reporteTopProductos(int cantidad)
- reporteDesempeñoVendedores()
- reporteClientesFrecuentes()
- reporteProductosMasVendidos()

#### 8. Excepciones Personalizadas

**GestionException (base):**

```java
public class GestionException extends Exception {
    private String codigo;
    private String detalles;

    public GestionException(String mensaje, String codigo) {
        super(mensaje);
        this.codigo = codigo;
    }
}
```

Excepciones específicas:

- EmpleadoNoEncontradoException
- ProductoNoDisponibleException
- VentaInvalidaException
- SaldoInsuficienteException
- StockInsuficienteException

### Funcionalidades del Sistema

#### Menú Principal

1. Gestión de Empleados
2. Gestión de Clientes
3. Gestión de Productos
4. Gestión de Ventas
5. Reportes y Estadísticas
6. Configuración
7. Salir

#### Gestión de Empleados

- Registrar nuevo empleado (Gerente/Vendedor/Técnico)
- Buscar empleado
- Listar empleados por departamento
- Calcular nómina
- Asignar jefe directo
- Ver jerarquía organizacional
- Generar recibo de pago

#### Gestión de Clientes

- Registrar nuevo cliente
- Buscar cliente
- Consultar historial de compras
- Gestionar saldo y crédito
- Consultar puntos de fidelidad
- Listar clientes por nivel

#### Gestión de Productos

- Registrar producto (Físico/Digital/Servicio)
- Buscar producto
- Actualizar stock
- Aplicar descuentos
- Listar por categoría
- Ver productos con stock bajo

#### Gestión de Ventas

- Nueva venta
- Buscar venta
- Cancelar venta
- Procesar devolución
- Generar factura
- Ver ventas por período

#### Reportes

- Top 10 productos más vendidos
- Ventas por vendedor
- Clientes frecuentes
- Valor total de inventario
- Nómina por departamento
- Comisiones de vendedores

### Casos de Uso Principales

#### Caso de Uso 1: Registrar Venta

1. Seleccionar cliente (crear si no existe)
2. Seleccionar vendedor
3. Agregar productos (validar disponibilidad)
4. Calcular subtotal
5. Aplicar descuentos (si aplica)
6. Calcular impuestos
7. Validar saldo del cliente
8. Procesar venta
9. Actualizar stock
10. Registrar comisión del vendedor
11. Actualizar puntos del cliente
12. Generar factura

#### Caso de Uso 2: Calcular Nómina

1. Seleccionar período
2. Listar empleados activos
3. Para cada empleado:
   - Calcular salario base
   - Agregar comisiones (Vendedor)
   - Agregar bonos (Gerente)
   - Calcular horas extra (Técnico)
   - Calcular descuentos
4. Generar recibo
5. Exportar nómina completa

### Criterios Técnicos de Evaluación

#### POO Básica (15%)

- Correcto uso de clases y objetos
- Encapsulación (getters/setters apropiados)
- Constructores sobrecargados
- Métodos de instancia y estáticos

#### Herencia (15%)

- Jerarquía de clases bien diseñada
- Uso apropiado de super()
- @Override de métodos
- Especialización correcta

#### Clases Abstractas (10%)

- Definición de métodos abstractos
- Implementación en clases concretas
- Combinación con métodos concretos

#### Interfaces (15%)

- Definición de interfaces apropiadas
- Implementación múltiple
- Métodos default (si aplica)
- Constantes en interfaces

#### Genéricos (15%)

- Patrón repositorio genérico
- Type bounds donde sea necesario
- Métodos genéricos
- Colecciones genéricas

#### Paquetes y Organización (10%)

- Estructura de paquetes lógica
- Imports correctos
- Visibilidad apropiada (public/private/protected)

#### Excepciones (10%)

- Excepciones personalizadas
- Manejo con try-catch
- Propagación apropiada
- Mensajes descriptivos

#### Calidad General (10%)

- Código limpio y legible
- Nomenclatura consistente
- Comentarios Javadoc
- Principios SOLID aplicados

### Entregables

1. Código fuente completo organizado en paquetes
2. Diagrama de clases UML
3. Documento de arquitectura
4. Manual de usuario
5. Casos de prueba ejecutados
6. Documentación Javadoc generada

### Tiempo Estimado

25-35 horas de desarrollo

### Extensiones Opcionales

1. Implementar persistencia en archivos/base de datos
2. Agregar patrón Observer para notificaciones
3. Implementar patrón Factory para creación de objetos
4. Agregar validación con anotaciones personalizadas
5. Implementar sistema de roles y permisos
6. Crear API REST básica
7. Agregar logs con patrón Singleton
8. Implementar patrón Strategy para cálculo de precios
