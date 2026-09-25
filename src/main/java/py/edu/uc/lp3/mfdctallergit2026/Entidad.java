package minecraft;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase general: representa cualquier entidad del juego.
 * Es abstracta porque "una entidad" en abstracto no existe en el mundo:
 * siempre es un Zombie, un Cerdo, el Jugador, etc.
 */
public abstract class Entidad {

    // ---- Atributos comunes (protected: los ven las subclases) ----
    protected int vida;
    protected String nombre;
    protected List<String> sonidos;
    protected Hitbox hitboxFisico;
    protected String drop;

    // Posición y orientación, para que avanzar/rotar hagan algo real
    protected double x;
    protected double y;
    protected double z;
    protected double rotacion; // en grados
    protected boolean enElAire;

    public Entidad(String nombre, int vida, Hitbox hitboxFisico, String drop) {
        this.nombre = nombre;
        this.vida = vida;
        this.hitboxFisico = hitboxFisico;
        this.drop = drop;
        this.sonidos = new ArrayList<>();
        this.rotacion = 0;
        this.enElAire = false;
    }

    // ---- Métodos comunes ----
    public void avanzar(double distancia) {
        double rad = Math.toRadians(rotacion);
        this.x += distancia * Math.cos(rad);
        this.z += distancia * Math.sin(rad);
        System.out.println(nombre + " avanza " + distancia + " bloques.");
    }

    public void rotar(double grados) {
        this.rotacion = (this.rotacion + grados) % 360;
        System.out.println(nombre + " rota hacia " + this.rotacion + "°.");
    }

    public void saltar() {
        if (enElAire) {
            System.out.println(nombre + " ya está en el aire.");
            return;
        }
        enElAire = true;
        this.y += 1.25;
        System.out.println(nombre + " salta.");
    }

    public void recibirDanio(int puntos) {
        this.vida = Math.max(0, this.vida - puntos);
        reproducirSonido("hurt");
        if (estaMuerto()) {
            morir();
        }
    }

    public boolean estaMuerto() {
        return vida <= 0;
    }

    public void morir() {
        System.out.println(nombre + " muere y suelta: " + drop);
    }

    public void reproducirSonido(String sonido) {
        if (!sonidos.contains(sonido)) {
            sonidos.add(sonido);
        }
        System.out.println("[sonido] " + nombre + ": " + sonido);
    }

    // ---- Getters / setters básicos ----
    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public String getDrop() { return drop; }
    public Hitbox getHitboxFisico() { return hitboxFisico; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + nombre + ", vida=" + vida + ")";
    }
}
