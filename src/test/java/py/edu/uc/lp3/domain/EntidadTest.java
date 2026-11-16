package py.edu.uc.lp3.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Prueba los mecanismos del dominio de Minecraft y verifica que la capa de
 * dominio sea pura: muta estado, pero no valida ni lanza excepciones. Las
 * validaciones viven en los services.
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
    void elDominioSoloMutaEstadoYNoValidaSusArgumentos() {
        Entidad zombie = new Zombie();

        assertDoesNotThrow(() -> zombie.recibirDanio(-1));
        assertDoesNotThrow(() -> zombie.recibirDanio(5, "  "));
        assertEquals(16, zombie.getVida());

        assertDoesNotThrow(() -> new Zombie(0));
        assertDoesNotThrow(() -> new Zombie(-10));
        assertDoesNotThrow(() -> new Jugador(" "));
        assertDoesNotThrow(() -> new Aldeano(""));
        assertDoesNotThrow(() -> new Hitbox(0, 0));
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
        Creeper creeperPersonalizado = new Creeper("Boom", 5);

        assertEquals("Steve", porDefecto.getNombre());
        assertEquals("Alex", conNombre.getNombre());
        assertEquals(20, vidaPorDefecto.getVida());
        assertEquals(35, vidaPersonalizada.getVida());
        assertEquals("Granjero", granjero.getProfesion());
        assertEquals("Herrero", herrero.getProfesion());
        assertEquals("Boom", creeperPersonalizado.getNombre());
        assertEquals(5, creeperPersonalizado.getVida());
    }

    // ---- Ocultamiento: el estado no se pisa desde afuera ----

    @Test
    void laVidaNuncaSeVuelveNegativa() {
        Entidad cerdo = new Cerdo();

        cerdo.recibirDanio(6);
        assertEquals(4, cerdo.getVida());

        cerdo.recibirDanio(1000);
        assertTrue(cerdo.estaMuerto());
        assertEquals(0, cerdo.getVida());

        cerdo.recibirDanio(5);
        assertEquals(0, cerdo.getVida());
    }

    // ---- Interfaz: una capacidad compartida por familias distintas ----

    @Test
    void laMismaCapacidadSeImplementaDistintoEnFamiliasDistintas() {
        Electrificable creeper = new Creeper();
        Electrificable cerdo = new Cerdo();

        assertTrue(creeper.recibirRayo().contains("cargado por un rayo"),
                creeper.recibirRayo());
        assertTrue(cerdo.recibirRayo().contains("Piglin Zombificado"),
                cerdo.recibirRayo());
    }

    @Test
    void unaClaseReemplazaElDefaultDeLaInterfazYOtraLoHereda() {
        Electrificable creeper = new Creeper();
        Electrificable cerdo = new Cerdo();

        assertNotEquals(creeper.avisoRayo(), cerdo.avisoRayo());
        assertTrue(creeper.avisoRayo().contains("sisea"), creeper.avisoRayo());
        assertTrue(cerdo.avisoRayo().contains("El cielo se ilumina"), cerdo.avisoRayo());
    }

    // ---- Interfaz UICapable: todos los elementos dibujables ----

    @Test
    void todasLasEntidadesSonDibujablesYPidenSusDatosPorElTipoDeLaInterfaz() {
        UICapable[] dibujables = { new Zombie(), new Creeper(), new Esqueleto(),
                new Enderman(), new Aldeano(), new Cerdo(), new Jugador() };

        for (UICapable elemento : dibujables) {
            assertTrue(elemento.getImagen().endsWith(".png"), elemento.getImagen());
            assertTrue(elemento.getPosicion().startsWith("("), elemento.getPosicion());
            assertFalse(elemento.getSonido().isBlank(), "el sonido no puede venir vacio");
            assertTrue(elemento.getVelocidad() > 0, "la velocidad debe ser positiva");
        }
    }

    @Test
    void laPosicionSeReflejaEnLosDatosDeUI() {
        Entidad zombie = new Zombie();
        zombie.avanzar(2);

        assertTrue(zombie.getPosicion().contains("2.0"), zombie.getPosicion());
    }

    // ---- Creeper como entidad: solo muta su propio estado ----

    @Test
    void elCreeperEnciendeSuMechaYMutaSuEstadoSimple() {
        Creeper creeper = new Creeper();

        assertFalse(creeper.isEncendido());
        assertTrue(creeper.encender().contains("enciende su mecha"));
        assertTrue(creeper.isEncendido());

        creeper.morir();
        assertTrue(creeper.estaMuerto());
        assertEquals(0, creeper.getVida());
    }

    @Test
    void elCreeperSeCargaYSeLePuedeCambiarElRadioSinValidaciones() {
        Creeper creeper = new Creeper();
        creeper.recibirRayo();

        assertTrue(creeper.isCargado());
        creeper.cambiarRadioExplosion(5.0);
        assertEquals(5.0, creeper.getRadioExplosion());
        assertDoesNotThrow(() -> creeper.cambiarRadioExplosion(0));
        assertEquals(0.0, creeper.getRadioExplosion());
    }
}
