package py.edu.uc.lp3.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Prueba los tres mecanismos del ejercicio sobre el dominio de Minecraft:
 * sobreescritura, sobrecarga de mensajes y constructores simples y
 * sobrecargados. Ademas, que nadie desde afuera deje una entidad invalida.
 */
class EntidadTest {

    // ---- Sobreescritura: misma firma, dos hijas independientes ----

    @Test
    void elMismoMensajeComportamientoSeResuelveDistintoEnCadaClaseHija() {
        Entidad zombie = new Zombie();
        Entidad aldeano = new Aldeano();

        String textoZombie = zombie.comportamiento(new Jugador());
        String textoAldeano = aldeano.comportamiento(new Jugador());

        assertTrue(textoZombie.contains("golpea cuerpo a cuerpo"), textoZombie);
        assertTrue(textoAldeano.contains("menu de comercio"), textoAldeano);
    }

    @Test
    void quienUsaElModeloHablaConElTipoPadre() {
        Entidad[] entidades = { new Zombie(), new Creeper(), new Aldeano(), new Cerdo() };
        for (Entidad entidad : entidades) {
            String texto = entidad.comportamiento(new Jugador());
            assertFalse(texto.isBlank(), "comportamiento no puede venir vacio");
        }
    }

    // ---- Sobrecarga: mismo nombre, otra lista de argumentos ----

    @Test
    void recibirDanioSeSobrecargaConYSinAtacante() {
        Entidad zombie = new Zombie();

        String sinAtacante = zombie.recibirDanio(5);
        String conAtacante = zombie.recibirDanio(5, "Creeper");

        assertTrue(sinAtacante.contains("recibe 5 de dano"), sinAtacante);
        assertTrue(conAtacante.contains("Creeper ataca a Zombie"), conAtacante);
        assertEquals(10, zombie.getVida(), "las dos sobrecargas aplican la misma regla");
    }

    @Test
    void laSobrecargaDeRecibirDanioTambienValidaSusArgumentos() {
        Entidad zombie = new Zombie();
        assertThrows(IllegalArgumentException.class, () -> zombie.recibirDanio(-1));
        assertThrows(IllegalArgumentException.class, () -> zombie.recibirDanio(5, "  "));
    }

    @Test
    void avanzarSeSobrecargaConYSinDistancia() {
        Entidad aldeano = new Aldeano();

        String sinDatos = aldeano.avanzar();
        String conDistancia = aldeano.avanzar(4);

        assertTrue(sinDatos.contains("avanza 1.0 bloques"), sinDatos);
        assertTrue(conDistancia.contains("avanza 4.0 bloques"), conDistancia);
    }

    // ---- Constructores simples y sobrecargados ----

    @Test
    void losConstructoresSobrecargadosDejanUnEstadoLegal() {
        Jugador porDefecto = new Jugador();
        Jugador conNombre = new Jugador("Alex");
        Zombie vidaPorDefecto = new Zombie();
        Zombie vidaPersonalizada = new Zombie(35);
        Aldeano granjero = new Aldeano();
        Aldeano herrero = new Aldeano("Herrero");

        assertEquals("Steve", porDefecto.getNombre());
        assertEquals("Alex", conNombre.getNombre());
        assertEquals(20, vidaPorDefecto.getVida());
        assertEquals(35, vidaPersonalizada.getVida());
        assertEquals("Granjero", granjero.getProfesion());
        assertEquals("Herrero", herrero.getProfesion());
    }

    @Test
    void NingunConstructorDejaVidaNoPositiva() {
        assertThrows(IllegalArgumentException.class, () -> new Zombie(0));
        assertThrows(IllegalArgumentException.class, () -> new Zombie(-10));
        assertThrows(IllegalArgumentException.class, () -> new Jugador(" "));
    }

    // ---- Ocultamiento: el estado no se pisa desde afuera ----

    @Test
    void LaVidaSoloCambiaPorMensajesYNuncaSeVuelveNegativa() {
        Entidad cerdo = new Cerdo();

        cerdo.recibirDanio(6);
        assertEquals(4, cerdo.getVida());

        cerdo.recibirDanio(1000);
        assertTrue(cerdo.estaMuerto());
        assertEquals(0, cerdo.getVida());

        String despuesDeMuir = cerdo.recibirDanio(5);
        assertTrue(despuesDeMuir.contains("ya murio"), despuesDeMuir);
        assertEquals(0, cerdo.getVida());
    }
}
