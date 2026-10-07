package py.edu.uc.lp3.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Jugador hereda de Entidad y agrega inventario y experiencia.
 * No ataca a nadie, asi que su comportamiento es usar su inventario.
 */
public class Jugador extends Entidad {

    private final List<String> inventario;
    private int experiencia;

    /** Constructor simple: el clasico "Steve". */
    public Jugador() {
        this("Steve");
    }

    /** Constructor sobrecargado: nombre propio del jugador. */
    public Jugador(String nombre) {
        super(nombre, 20, new Hitbox(0.6, 1.8), "items del inventario");
        this.inventario = new ArrayList<>();
        this.experiencia = 0;
    }

    @Override
    public String comportamiento(Jugador objetivo) {
        return getNombre() + " no ataca a nadie: mejoraria " + experiencia
                + " de experiencia y tiene " + inventario.size() + " items.";
    }

    public String recoger(String item) {
        if (item == null || item.isBlank()) {
            throw new IllegalArgumentException("No se puede recoger un item vacio");
        }
        inventario.add(item);
        return getNombre() + " recoge " + item + ".";
    }

    public String ganarExperiencia(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("La experiencia no puede ser negativa");
        }
        experiencia += puntos;
        return getNombre() + " gana " + puntos + " de XP (total: " + experiencia + ").";
    }

    public List<String> getInventario() { return List.copyOf(inventario); }
    public int getExperiencia() { return experiencia; }
}
