# Proyecto Integrador 02 - Módulo Básicos

## Sistema de Análisis y Procesamiento de Texto

### Descripción General

Desarrollar una aplicación de consola que funcione como un analizador avanzado de texto, capaz de procesar cadenas de caracteres, generar estadísticas, realizar transformaciones y validaciones. El sistema debe incluir funcionalidades de cifrado básico, búsqueda de patrones y generación de reportes.

### Objetivos de Aprendizaje

- Dominar manipulación avanzada de Strings
- Aplicar operadores y expresiones complejas
- Implementar algoritmos de búsqueda y transformación
- Utilizar bucles anidados y estructuras de control complejas
- Trabajar con StringBuilder para optimización
- Aplicar validaciones y conversiones de tipos
- Manejar entrada/salida con formateo avanzado

### Requerimientos Funcionales

#### 1. Menú Principal

Implementar menú con las siguientes opciones:

- Ingresar/Modificar texto
- Análisis estadístico del texto
- Transformaciones de texto
- Búsqueda y reemplazo
- Validaciones de formato
- Cifrado y descifrado (César)
- Generador de contraseñas
- Comparar dos textos
- Ver historial de operaciones
- Salir

#### 2. Ingreso y Gestión de Texto

- Permitir ingresar texto (máximo 1000 caracteres)
- Opción para cargar texto predefinido de prueba
- Mostrar texto actual en cualquier momento
- Limpiar texto
- Validar que el texto no esté vacío antes de operaciones

#### 3. Análisis Estadístico

Calcular y mostrar:

- Total de caracteres (incluyendo espacios)
- Total de caracteres sin espacios
- Total de palabras
- Total de líneas (contar saltos de línea)
- Total de vocales (a, e, i, o, u - mayúsculas y minúsculas)
- Total de consonantes
- Total de dígitos
- Total de caracteres especiales (!, ?, ., ,, etc.)
- Palabra más larga y palabra más corta
- Frecuencia de cada vocal
- Promedio de caracteres por palabra
- Promedio de palabras por línea

Formato de salida:

```bash
========== ANÁLISIS ESTADÍSTICO ==========
Texto analizado: "..."
Caracteres totales: 150
Caracteres sin espacios: 125
Palabras: 25
Líneas: 3
Vocales: 45 (a:12, e:15, i:8, o:7, u:3)
Consonantes: 65
Dígitos: 5
Caracteres especiales: 10
Palabra más larga: "programación" (12 letras)
Palabra más corta: "de" (2 letras)
Promedio caracteres/palabra: 5.0
Promedio palabras/línea: 8.3
=========================================
```

#### 4. Transformaciones de Texto

Implementar las siguientes transformaciones:

- Convertir a MAYÚSCULAS
- Convertir a minúsculas
- Capitalizar Primera Letra De Cada Palabra
- Invertir texto completo (espejo)
- Invertir orden de palabras
- Eliminar espacios extra (dejar solo un espacio entre palabras)
- Eliminar todos los espacios
- Alternar mayúsculas/minúsculas (eFeCTo zEbRa)
- Convertir a "leet speak" (a->4, e->3, i->1, o->0, s->5, t->7)
- Duplicar vocales (Hoolaa muundoo)

Después de cada transformación:

- Mostrar texto original
- Mostrar texto transformado
- Preguntar si desea guardar la transformación

#### 5. Búsqueda y Reemplazo

- Buscar palabra/frase (case-sensitive y case-insensitive)
- Mostrar número de ocurrencias
- Mostrar posiciones donde aparece
- Resaltar ocurrencias en el contexto
- Reemplazar todas las ocurrencias
- Reemplazar solo la primera ocurrencia
- Reemplazar solo la última ocurrencia
- Reemplazar ocurrencia específica por número

Ejemplo de salida:

```bash
Buscando "java" en modo case-insensitive...
Se encontraron 3 ocurrencias:
  [1] Posición 5: "...aprender Java es..."
  [2] Posición 45: "...programar en JAVA..."
  [3] Posición 89: "...frameworks de java..."
```

#### 6. Validaciones de Formato

Validar si el texto cumple con formatos específicos:

- Es un email válido (contiene @ y dominio)
- Es un número de teléfono (solo dígitos, longitud 10)
- Es una fecha (formato dd/MM/yyyy o dd-MM-yyyy)
- Es un número entero
- Es un número decimal
- Es una URL válida (comienza con http:// o https://)
- Es un código postal (5 dígitos)
- Contiene solo letras
- Contiene solo números
- Es un palíndromo (se lee igual al derecho y al revés)

Para cada validación mostrar:

- Resultado: VÁLIDO o INVÁLIDO
- Razón del resultado
- Sugerencias de corrección si es inválido

#### 7. Cifrado César

Implementar cifrado y descifrado César:

- Solicitar texto a cifrar
- Solicitar desplazamiento (clave numérica 1-25)
- Cifrar texto (desplazar cada letra en el alfabeto)
- Preservar espacios y caracteres especiales
- Mostrar texto cifrado
- Opción para descifrar proporcionando la clave
- Opción de "fuerza bruta" para descifrar (probar todas las claves 1-25)

Ejemplo:

```bash
Texto original: "HOLA MUNDO"
Clave: 3
Texto cifrado: "KROD PXQGR"
```

#### 8. Generador de Contraseñas

Generar contraseñas aleatorias con opciones:

- Longitud de la contraseña (4-20 caracteres)
- Incluir mayúsculas (A-Z)
- Incluir minúsculas (a-z)
- Incluir números (0-9)
- Incluir símbolos (!@#$%^&*)
- Generar múltiples opciones (generar 5 contraseñas)

Validar que la contraseña generada cumple con:

- Longitud solicitada
- Al menos un carácter de cada tipo seleccionado

Mostrar nivel de seguridad:

- Débil: solo un tipo de caracteres
- Media: dos tipos de caracteres
- Fuerte: tres tipos de caracteres
- Muy fuerte: cuatro tipos de caracteres y longitud >= 12

#### 9. Comparación de Textos

Comparar dos textos mostrando:

- Textos son idénticos (incluyendo mayúsculas)
- Textos son iguales (ignorando mayúsculas)
- Textos son similares (mismo contenido, diferente orden)
- Porcentaje de similitud (caracteres en común)
- Diferencias de longitud
- Palabras únicas en texto 1
- Palabras únicas en texto 2
- Palabras comunes en ambos textos

#### 10. Historial de Operaciones

Mantener registro de las últimas 10 operaciones:

- Tipo de operación
- Fecha y hora
- Texto antes de la operación
- Texto después de la operación (si aplica)

Mostrar historial con formato:

```bash
========== HISTORIAL DE OPERACIONES ==========
[1] 31/01/2026 14:30:25 - ANÁLISIS ESTADÍSTICO
    Texto: "Hola mundo"

[2] 31/01/2026 14:31:10 - TRANSFORMACIÓN: MAYÚSCULAS
    Antes: "hola mundo"
    Después: "HOLA MUNDO"

[3] 31/01/2026 14:32:05 - BÚSQUEDA: "mundo"
    Encontradas: 2 ocurrencias
===============================================
```

### Requerimientos Técnicos

#### Conceptos del Módulo a Aplicar

1. **Strings (Intensivo)**:
   - Todos los métodos principales: length(), charAt(), substring(), indexOf(), lastIndexOf()
   - toLowerCase(), toUpperCase(), trim(), replace(), replaceAll()
   - startsWith(), endsWith(), contains(), equals(), equalsIgnoreCase()
   - split() para separar palabras
   - StringBuilder para construcción eficiente de textos largos
   - String.format() para formateo
   - Comparación de strings (==, equals, compareTo)

2. **Bucles Anidados**:
   - For anidado para análisis carácter por carácter
   - While para menús y validaciones
   - Do-while para repetir operaciones

3. **Condicionales Complejas**:
   - If-else-if encadenados para validaciones
   - Switch-case para menú y tipos de operación
   - Operador ternario para asignaciones condicionales
   - Operadores lógicos complejos (&&, ||, !)

4. **Operadores**:
   - Aritméticos para estadísticas
   - Módulo (%) para cifrado César
   - Incrementales para contadores
   - Relacionales para comparaciones

5. **Tipos de Datos y Wrapper**:
   - char para manipulación de caracteres individuales
   - int, double para estadísticas
   - Integer.parseInt(), Double.parseDouble()
   - Character.isLetter(), isDigit(), isWhitespace()

6. **Date**:
   - Almacenar timestamp de operaciones
   - SimpleDateFormat para formateo

7. **Clase Math**:
   - Math.random() para generador de contraseñas
   - Math.abs(), Math.max(), Math.min() para cálculos

### Casos de Prueba

#### Caso 1: Análisis Estadístico

Input:

```bash
"Java es un lenguaje de programación.
Java tiene 25 años de historia."
```

Expected:

- Palabras: 12
- Vocales: 23
- La palabra "Java" aparece 2 veces

#### Caso 2: Cifrado César

- Input: "ABC", Clave: 1
- Expected: "BCD"
- Input: "XYZ", Clave: 3
- Expected: "ABC"

#### Caso 3: Validación Email

- "<user@example.com>" -> VÁLIDO
- "userexample.com" -> INVÁLIDO (falta @)
- "user@" -> INVÁLIDO (falta dominio)

#### Caso 4: Palíndromo

- "anita lava la tina" (sin espacios) -> VÁLIDO
- "hola mundo" -> INVÁLIDO

#### Caso 5: Transformación Leet Speak

- Input: "HELLO WORLD"
- Expected: "H3LL0 W0RLD"

### Criterios de Evaluación

#### Funcionalidad (40%)

- Todas las opciones del menú implementadas
- Cálculos y análisis correctos
- Transformaciones precisas
- Validaciones efectivas

#### Uso de Conceptos (40%)

- Uso extensivo de métodos de String
- StringBuilder para optimización
- Bucles y condicionales apropiados
- Manejo de caracteres individuales
- Algoritmos de búsqueda implementados

#### Calidad y Eficiencia (20%)

- Código optimizado (StringBuilder vs concatenación)
- Manejo de casos especiales (texto vacío, caracteres especiales)
- Interfaz de usuario clara
- Comentarios en algoritmos complejos

### Entregables

1. Código fuente completo
2. Documento con pruebas de todas las funcionalidades
3. Capturas de pantalla de ejecución

### Tiempo Estimado

10-14 horas de desarrollo

### Extensiones Opcionales

1. Implementar cifrado ROT13
2. Agregar análisis de frecuencia de letras
3. Detectar idioma del texto (español/inglés) basado en patrones
4. Implementar corrector ortográfico básico
5. Agregar funcionalidad de autocompletado de palabras
6. Crear generador de texto Lorem Ipsum personalizado
7. Implementar búsqueda con expresiones regulares básicas
