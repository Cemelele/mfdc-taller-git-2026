package py.edu.uc.lp3.domain;

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
    public String accionAtaque(Jugador objetivo) {
        reproducirSonido("fuse");
        int danio = cargado ? 25 : 15;
        String resultado = "El Creeper sisea y explota en un radio de " + radioExplosion
                + " bloques" + (cargado ? ", cargado por un rayo (" + danio + " de dano)" : "")
                + ". " + objetivo.recibirDanio(danio);
        return resultado + ". " + morir(); // la explosion lo destruye a el tambien
    }

    public String recibirRayo() {
        this.cargado = true;
        return "Creeper cargado por un rayo: ahora explota con " + getNombre() + ".";
    }

    public boolean isCargado() { return cargado; }
    public double getRadioExplosion() { return radioExplosion; }
}
