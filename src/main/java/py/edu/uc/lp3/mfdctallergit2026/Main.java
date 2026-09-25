package minecraft;

import java.util.ArrayList;
import java.util.List;

/**
 * Programa de prueba: muestra herencia y polimorfismo en acción.
 */
public class Main {

    public static void main(String[] args) {

        Jugador steve = new Jugador("Steve");
        steve.recoger("Silla de montar");

        // Una sola lista guarda TODAS las entidades: eso lo permite la herencia.
        List<Entidad> mundo = new ArrayList<>();
        mundo.add(steve);
        mundo.add(new Zombie());
        mundo.add(new Esqueleto());
        mundo.add(new Creeper());
        mundo.add(new Enderman());
        mundo.add(new Cerdo());

        Aldeano herrero = new Aldeano("Herrero");
        herrero.agregarOferta("5 esmeraldas -> pico de hierro");
        mundo.add(herrero);

        System.out.println("=== Metodos heredados de Entidad ===");
        for (Entidad e : mundo) {
            e.rotar(90);
            e.avanzar(2);
        }

        System.out.println("\n=== Comportamiento especifico (polimorfismo) ===");
        for (Entidad e : mundo) {
            if (e instanceof Hostil hostil) {
                // misma llamada, cada mob ataca distinto
                hostil.accionAtaque(steve);
            } else if (e instanceof NoHostil pasivo) {
                pasivo.interactuar(steve);
            }
        }

        System.out.println("\n=== Estado final ===");
        for (Entidad e : mundo) {
            System.out.println(e);
        }
    }
}
