# Proyecto Integrador 01 - Módulo Básicos

## Sistema de Gestión de Empleados

### Descripción General

Desarrollar una aplicación de consola para gestionar información de empleados de una empresa. El sistema debe permitir registrar empleados, calcular salarios, gestionar bonificaciones, y generar reportes básicos.

### Objetivos de Aprendizaje

- Aplicar fundamentos de Java: tipos de datos, variables y constantes
- Utilizar operadores aritméticos, lógicos y relacionales
- Implementar estructuras de control (condicionales y bucles)
- Manipular cadenas de texto y realizar validaciones
- Gestionar entrada/salida de datos por consola
- Trabajar con tipos wrapper y conversiones
- Manejar fechas básicas
- Utilizar variables de entorno y propiedades del sistema

### Requerimientos Funcionales

#### 1. Menú Principal

Implementar un menú interactivo con las siguientes opciones:

- Registrar nuevo empleado
- Buscar empleado por ID
- Calcular salario neto
- Aplicar bonificación
- Generar reporte de empleados
- Mostrar información del sistema
- Salir

#### 2. Registro de Empleados

Solicitar y validar:

- ID del empleado (número entero positivo)
- Nombre completo (mínimo 3 caracteres)
- Puesto (opciones: GERENTE, SUPERVISOR, OPERARIO, ADMINISTRATIVO)
- Salario base (número decimal positivo)
- Fecha de ingreso (formato: dd/MM/yyyy)
- Años de experiencia (número entero)

Validaciones requeridas:

- ID único (no puede existir dos empleados con el mismo ID)
- Nombre no puede contener números
- Salario base debe ser mayor a 0
- Fecha de ingreso no puede ser futura

#### 3. Cálculo de Salario Neto

Calcular el salario neto aplicando las siguientes reglas:

Descuentos:

- Seguro social: 9.75% del salario base
- Impuesto sobre la renta:
  - Salario <= 1000: 0%
  - Salario > 1000 y <= 3000: 10%
  - Salario > 3000 y <= 6000: 15%
  - Salario > 6000: 20%

Mostrar desglose completo:

- Salario base
- Descuento por seguro social
- Descuento por impuestos
- Salario neto

#### 4. Sistema de Bonificaciones

Aplicar bonificación según:

Por antigüedad:

- Menos de 1 año: 0%
- 1 a 3 años: 5%
- 3 a 5 años: 10%
- 5 a 10 años: 15%
- Más de 10 años: 20%

Por puesto:

- GERENTE: 500 adicional
- SUPERVISOR: 300 adicional
- OPERARIO: 150 adicional
- ADMINISTRATIVO: 200 adicional

Calcular bonificación total: (salario_base * porcentaje_antiguedad) + bonificacion_puesto

#### 5. Búsqueda de Empleados

Permitir búsqueda por:

- ID exacto
- Nombre (búsqueda parcial, case-insensitive)
- Puesto

Mostrar todos los datos del empleado si se encuentra, mensaje de error si no existe.

#### 6. Reporte de Empleados

Generar reporte mostrando:

- Total de empleados registrados
- Promedio de salarios
- Empleado con mayor salario
- Empleado con menor salario
- Distribución por puesto (contador de empleados por cada puesto)
- Costo total en nómina (suma de todos los salarios netos)

#### 7. Información del Sistema

Mostrar:

- Versión de Java (System.getProperty)
- Sistema operativo
- Usuario actual
- Directorio de trabajo
- Memoria disponible de la JVM
- Fecha y hora actual del sistema

### Requerimientos Técnicos

#### Conceptos del Módulo a Aplicar

1. **Fundamentos**:
   - Usar tipos primitivos (int, double, boolean, long)
   - Declarar constantes para valores fijos (tasas de impuestos, descuentos)
   - Aplicar nomenclatura adecuada (camelCase para variables)

2. **Operadores**:
   - Aritméticos para cálculos de salarios y bonificaciones
   - Relacionales y lógicos para validaciones
   - De asignación compuesta (+=, -=, etc.)

3. **Strings**:
   - Métodos: length(), charAt(), substring(), toLowerCase(), toUpperCase()
   - Concatenación y comparación (equals(), equalsIgnoreCase())
   - StringBuilder para construcción de reportes
   - Validación de formatos

4. **Condicionales**:
   - If-else para validaciones y cálculos
   - Switch para menú principal y tipo de puesto
   - Operador ternario para asignaciones condicionales simples

5. **Bucles**:
   - While para el menú principal
   - For para iteración de empleados en reportes
   - Do-while para validaciones de entrada
   - Break y continue según sea necesario

6. **Wrapper y Autoboxing**:
   - Integer, Double para conversión de tipos
   - Métodos: parseInt(), parseDouble(), valueOf()
   - Validar conversiones con try-catch básico

7. **Entrada/Salida**:
   - Scanner para lectura de datos
   - System.out.printf() para formateo de salidas
   - Validación de tipos de entrada

8. **Instanceof**:
   - Verificar tipos en variables de tipo Object (si aplica)

9. **Paso por Valor/Referencia**:
   - Comprender comportamiento al pasar datos primitivos vs String

10. **Date**:
    - SimpleDateFormat para parseo y formateo de fechas
    - Date para almacenar fecha de ingreso
    - Cálculo de diferencia entre fechas para antigüedad

11. **Entorno y Sistema**:
    - System.getProperty() para información del sistema
    - Runtime.getRuntime() para información de memoria

### Estructura de Datos Sugerida

Como no se usan arreglos en este módulo, mantener máximo 5 empleados con variables individuales:

```bash
int empleado1Id, empleado2Id, empleado3Id, empleado4Id, empleado5Id;
String empleado1Nombre, empleado2Nombre, ...;
double empleado1Salario, empleado2Salario, ...;
// etc.
```

O usar variables simples para demostración de un solo empleado a la vez.

### Casos de Prueba

#### Caso 1: Registro Válido

- Input: ID=1001, Nombre="Juan Pérez", Puesto=OPERARIO, Salario=2500, Fecha="15/03/2020", Experiencia=4
- Output: Empleado registrado exitosamente

#### Caso 2: Cálculo de Salario Neto

- Input: Salario base = 3500
- Expected:
  - Seguro social: 341.25
  - Impuesto: 525.00 (15%)
  - Salario neto: 2633.75

#### Caso 3: Bonificación

- Input: Salario=2500, Antigüedad=4 años, Puesto=OPERARIO
- Expected:
  - Bonificación por antigüedad: 250 (10%)
  - Bonificación por puesto: 150
  - Total bonificación: 400

#### Caso 4: Validación de Entrada

- Input: Salario base = -1000
- Output: Error - El salario debe ser positivo

#### Caso 5: Búsqueda Inexistente

- Input: ID=9999
- Output: Empleado no encontrado

### Criterios de Evaluación

#### Funcionalidad (40%)

- Todas las opciones del menú funcionan correctamente
- Cálculos matemáticos son precisos
- Validaciones implementadas correctamente

#### Uso de Conceptos (40%)

- Aplicación correcta de tipos de datos y operadores
- Uso adecuado de estructuras de control
- Manipulación correcta de Strings
- Implementación de entrada/salida con validaciones
- Uso de constantes para valores fijos

#### Calidad del Código (20%)

- Código legible y bien organizado
- Nomenclatura consistente
- Comentarios en secciones complejas
- Manejo básico de errores

### Entregables

1. Código fuente completo (.java)
2. Documento con casos de prueba ejecutados
3. Capturas de pantalla de la ejecución

### Tiempo Estimado

8-12 horas de desarrollo

### Extensiones Opcionales

1. Permitir actualización de datos de empleados existentes
2. Implementar sistema de horas extras
3. Agregar descuentos adicionales (préstamos, adelantos)
4. Crear calculadora de prestaciones (vacaciones, aguinaldo)
5. Implementar sistema de asistencias con cálculo de días trabajados
