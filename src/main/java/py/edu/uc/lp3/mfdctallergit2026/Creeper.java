package minecraft;

public class Creeper extends Hostil {

    private final double radioExplosion;
    private boolean cargado;

    public Creeper() {
        super("Creeper", 20, new Hitbox(0.6, 1.7), "Polvora", 16);
        this.radioExplosion = 3.0;
        this.cargado = false;
    }

    /** El Creeper no golpea: se acerca, sisea y explota. */
    @Override
    public void accionAtaque(Jugador objetivo) {
        reproducirSonido("fuse");
        System.out.println("Creeper sisea y explota en un radio de " + radioExplosion + " bloques.");
        int danio = cargado ? 25 : 15;
        objetivo.recibirDanio(danio);
        morir(); // la explosión lo destruye a él tambien
    }

    public void recibirRayo() {
        this.cargado = true;
        System.out.println("Creeper cargado por un rayo.");
    }
}
