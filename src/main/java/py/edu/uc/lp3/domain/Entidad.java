package py.edu.uc.lp3.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase base "general": representa cualquier entidad del juego.
 * Es abstracta porque "una entidad" en abstracto no existe en el mundo:
 * siempre es un Zombie, un Cerdo, el Jugador, etc.
 *
 * Todos los campos son private y el estado solo cambia a traves de los
 * metodos de la propia entidad: desde afuera, o desde un controller, no
 * se puede dejar una entidad en un estado imposible.
 *
 * Los metodos no imprimen: devuelven una String con lo que ocurrio. Asi
 * el controller puede armar el JSON con la respuesta de cada entidad.
 */
public abstract class Entidad {

    private int vida;
    private final String nombre;
    private final Hitbox hitboxFisico;
    private final String drop;
    private final List<String> sonidos;

    // Posicion y orientacion, para que avanzar/rotar hagan algo real
    private double x;
    private double y;
    private double z;
    private double rotacion; // en grados
    private boolean enElAire;

    protected Entidad(String nombre, int vida, Hitbox hitboxFisico, String drop) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("La entidad necesita un nombre");
        }
        if (vida <= 0) {
            throw new IllegalArgumentException("La vida inicial de " + nombre + " debe ser positiva");
        }
        this.nombre = nombre;
        this.vida = vida;
        this.hitboxFisico = Objects.requireNonNull(hitboxFisico, "la entidad necesita un hitbox");
        this.drop = Objects.requireNonNull(drop, "la entidad necesita un drop");
        this.sonidos = new ArrayList<>();
        this.rotacion = 0.0;
        this.enElAire = false;
    }

    // ---- Metodo abstracto: cada entidad se comporta distinto ----

    /**
     * Que hace esta entidad frente al jugador. El padre no puede escribir
     * esta respuesta porque cada tipo de entidad la realiza de otra manera:
     * un Creeper explota, un Aldeano comercian, el Jugador usa su inventario.
     */
    public abstract String comportamiento(Jugador objetivo);

    // ---- Movimientos ----

    /**
     * SOBRECARGA: misma accion "avanzar", otra lista de argumentos.
     * Sin datos, avanza el paso por defecto (1 bloque). La logica no se
     * duplica: esta firma delega en la de un argumento.
     */
    public String avanzar() {
        return avanzar(1.0);
    }

    public String avanzar(double distancia) {
        if (distancia < 0) {
            throw new IllegalArgumentException("La distancia no puede ser negativa");
        }
        if (estaMuerto()) {
            return nombre + " ya murio, no puede avanzar.";
        }
        double rad = Math.toRadians(rotacion);
        x += distancia * Math.cos(rad);
        z += distancia * Math.sin(rad);
        return nombre + " avanza " + distancia + " bloques.";
    }

    public String rotar(double grados) {
        if (estaMuerto()) {
            return nombre + " ya murio, no puede rotar.";
        }
        rotacion = (rotacion + grados) % 360;
        return nombre + " rota hacia " + (int) rotacion + " grados.";
    }

    public String saltar() {
        if (estaMuerto()) {
            return nombre + " ya murio, no puede saltar.";
        }
        if (enElAire) {
            return nombre + " ya esta en el aire.";
        }
        enElAire = true;
        y += 1.25;
        return nombre + " salta.";
    }

    /**
     * Punto de extension del padre: las hijas que necesitan cambiar de
     * posicion (un Enderman que se teletransporta) lo usan en vez de tocar
     * las coordenadas directamente.
     */
    protected String desplazar(double dx, double dy, double dz) {
        x += dx;
        y += dy;
        z += dz;
        return nombre + " cambia de posicion a (" + dosDecimales(x) + ", "
                + dosDecimales(y) + ", " + dosDecimales(z) + ").";
    }

    private static String dosDecimales(double valor) {
        return String.valueOf(Math.round(valor * 100.0) / 100.0);
    }

    // ---- Vida y muerte ----

    /**
     * SOBRECARGA: mismo mensaje "recibirDanio", otra lista de argumentos.
     * Ademas de los puntos, indica quien ataca. Valida el atacante antes
     * de tocar la vida, asi un valor ilegal no deja el objeto a medias.
     */
    public String recibirDanio(int puntos, String atacante) {
        if (atacante == null || atacante.isBlank()) {
            throw new IllegalArgumentException("El atacante no puede estar vacio");
        }
        return atacante + " ataca a " + nombre + ". " + recibirDanio(puntos);
    }

    public String recibirDanio(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("El dano no puede ser negativo");
        }
        if (estaMuerto()) {
            return nombre + " ya murio, no puede recibir dano.";
        }
        vida = Math.max(0, vida - puntos);
        String resultado = nombre + " recibe " + puntos + " de dano" + reproducirSonido("hurt");
        if (estaMuerto()) {
            resultado += ". " + morir();
        }
        return resultado + ".";
    }

    public boolean estaMuerto() {
        return vida <= 0;
    }

    /**
     * Mata a la entidad. Es protected porque solo la jerarquia decide
     * cuando una entidad muere sin recibir dano (por ejemplo, el Creeper
     * al explotar se lleva a si mismo por delante).
     */
    protected String morir() {
        vida = 0;
        return nombre + " muere y suelta: " + drop;
    }

    public String reproducirSonido(String sonido) {
        if (sonido != null && !sonidos.contains(sonido)) {
            sonidos.add(sonido);
        }
        return " y suena: " + sonido;
    }

    // ---- Getters (las listas se devuelven como copia inmutable) ----

    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public String getDrop() { return drop; }
    public Hitbox getHitboxFisico() { return hitboxFisico; }
    public List<String> getSonidos() { return List.copyOf(sonidos); }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public double getRotacion() { return rotacion; }
    public boolean isEnElAire() { return enElAire; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + nombre + ", vida=" + vida + ")";
    }
}
