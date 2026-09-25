package minecraft;

import java.util.ArrayList;
import java.util.List;

/**
 * Jugador hereda de Entidad y agrega inventario y experiencia.
 */
public class Jugador extends Entidad {

    private final List<String> inventario;
    private int experiencia;

    public Jugador(String nombre) {
        super(nombre, 20, new Hitbox(0.6, 1.8), "items del inventario");
        this.inventario = new ArrayList<>();
        this.experiencia = 0;
    }

    public void recoger(String item) {
        inventario.add(item);
        System.out.println(nombre + " recoge " + item + ".");
    }

    public void ganarExperiencia(int puntos) {
        this.experiencia += puntos;
        System.out.println(nombre + " gana " + puntos + " XP (total: " + experiencia + ").");
    }

    public List<String> getInventario() { return inventario; }
    public int getExperiencia() { return experiencia; }
}
