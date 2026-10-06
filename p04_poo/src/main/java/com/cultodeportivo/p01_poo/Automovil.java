package com.cultodeportivo.p01_poo;

public class Automovil implements Comparable<Automovil>{

    // Atributos de la clase Automovil
    private int id;
    private String fabricante;
    private String modelo;
    private Color color = Color.GRIS;
    private Motor motor;
    private Estanque estanque;
    private Persona conductor;
    private Rueda[] ruedas;
    private int indiceRuedas;
    private TipoAutomovil tipo;

    // Atributos estáticos de la clase Automovil private
    private static Color colorPatente = Color.NARANJO;
    private static int capacidadEstanqueEstatico = 30;
    private static int ultimoId;

    // Constantes de la clase Automovil
    public static final Integer VELOCIDAD_MAX_CARRETERA = 120;
    public static final int VELOCIDAD_MAX_CIUDAD = 60;

    // Constantes de colores
    public static final String COLOR_ROJO = "Rojo";
    public static final String COLOR_AMARILLO = "Amarillo";
    public static final String COLOR_AZUL = "Azul";
    public static final String COLOR_BLANCO = "Blanco";
    public static final String COLOR_GRIS = "Gris Oscuro";
    public static final String COLOR_VERDE = "Verde";

    // Constructores de la clase Automovil
    public Automovil() {
        this.id = ++ultimoId; // Incrementa y asigna el ID único
        this.ruedas = new Rueda[5]; // Inicializa el arreglo de ruedas con capacidad para 5 ruedas
    }

    // Sobrecarga de constructores

    public Automovil(String fabricante, String modelo) {
        this(); // Llama al constructor por defecto para inicializar ID y ruedas
        this.fabricante = fabricante;
        this.modelo = modelo;
    }

    public Automovil(String fabricante, String modelo, Color color){
        this(fabricante, modelo);
        this.color = color;
    }

    public Automovil(String fabricante, String modelo, Color color, Motor motor) {
        this(fabricante, modelo, color);
        this.motor = motor;
    }

    public Automovil(String fabricante, String modelo, Color color, Motor motor, Estanque estanque) {
        this(fabricante, modelo, color, motor);
        this.estanque = estanque;
    }

    public Automovil(String fabricante, String modelo, Color color, Motor motor, Estanque estanque, Persona conductor, Rueda[] ruedas) {
        this(fabricante, modelo, color, motor, estanque);
        this.conductor = conductor;
        this.ruedas = ruedas;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public static Color getColorPatente(){
        return colorPatente;
    }

    public static void setColorPatente(Color colorPatente){
        Automovil.colorPatente = colorPatente;
    }

    public static int getCapacidadEstanqueEstatico() {
        return capacidadEstanqueEstatico;
    }

    public static void setCapacidadEstanqueEstatico(int capacidadEstanqueEstatico) {
        Automovil.capacidadEstanqueEstatico = capacidadEstanqueEstatico;
    }

    public TipoAutomovil getTipo() {
        return tipo;
    }

    public void setTipo(TipoAutomovil tipo) {
        this.tipo = tipo;
    }

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    public Estanque getEstanque() {
        if(estanque == null){
            this.estanque = new Estanque();
        }
        return estanque;
    }

    public void setEstanque(Estanque estanque) {
        this.estanque = estanque;
    }

    public Persona getConductor() {
        return conductor;
    }

    public void setConductor(Persona conductor) {
        this.conductor = conductor;
    }

    public Rueda[] getRuedas() {
        return ruedas;
    }

    public void setRuedas(Rueda[] ruedas) {
        this.ruedas = ruedas;
    }

    // Método para agregar una rueda al automóvil
    public Automovil addRueda(Rueda rueda){
        if(indiceRuedas < this.ruedas.length) {
            this.ruedas[indiceRuedas++] = rueda;
        }
        return this;
    }

    // Otros métodos de la clase Automovil
    public String verDetalle() {
        String detalle =  "auto.id = " + this.id +
                "\nauto.fabricante = " + (this.getFabricante() != null ? this.getFabricante() : "No asginado") +
                "\nauto.modelo = " + (this.getModelo() != null ? this.getModelo() : "No asginado");

        if(this.getTipo() != null) {
            detalle += "\nauto.tipo = " + (this.getTipo().getDescripcion() != null ? this.getTipo().getDescripcion() : "No asginado");
        } else {
            detalle += "\nauto.tipo = No asginado";
        }

        detalle += "\nauto.color = " + (this.color != null ? this.color : "No asginado") +
                "\nauto.patenteColor = " + (colorPatente != null ? colorPatente : "No asginado");

        detalle += "\nauto.cilindrada = " + (this.motor != null ? this.motor.getCilindrada() : "No asginado");

        detalle += "\nConductor: " + (this.conductor != null ? this.getConductor() : "No asginado");

        if(getRuedas() != null) {
            detalle += "\nRuedas del automóvil:";
            for (Rueda r : this.getRuedas()) {
                if(r != null) {
                    detalle += "\n" + (r.getFabricante() != null ? r.getFabricante() : "No asginado")
                            + ", aro: " + r.getAro()
                            + ", ancho: " + r.getAncho();
                } else {
                    detalle += "\nNo asginado";
                }
            }
        } else {
            detalle += "\nRuedas del automóvil: No asginado";
        }

        return detalle;
    }

    public String acelerar(int rpm) {
        return "el auto " + this.fabricante + " acelerando a " + rpm + "rpm";
    }

    public String frenar() {
        return this.fabricante + " " + this.modelo + " frenando!";
    }

    public String acelerarFrenar(int rpm) {
        String acelerar = this.acelerar(rpm);
        String frenar = this.frenar();
        return acelerar + "\n" + frenar;
    }

    public float calcularConsumo(int km, float porcentajeBencina) {
        return km / (this.getEstanque().getCapacidad() * porcentajeBencina);
    }

    public float calcularConsumo(int km, int porcentajeBencina) {
        return km / (this.getEstanque().getCapacidad() * (porcentajeBencina / 100f));
    }

    public static float calcularConsumoEstatico(int km, int porcentajeBencina) {
        return km / (Automovil.capacidadEstanqueEstatico * (porcentajeBencina / 100f));
    }

    // Métodos equals y hashCode, sirven para comparar objetos y usarlos en colecciones
    @Override
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (fabricante != null ? fabricante.hashCode() : 0);
        hash = 31 * hash + (modelo != null ? modelo.hashCode() : 0);
        return hash;
    }

    // Método toString para representar el objeto como una cadena
    @Override
    public String toString() {
        return this.id + " : " + fabricante + " " + modelo;
    }

    // Método compareTo para comparar automóviles por fabricante
    @Override
    public int compareTo(Automovil a) {
        return this.fabricante.compareTo(a.fabricante);
    }
}

