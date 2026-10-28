package py.edu.uc.lp3.domain;

public class Creeper extends Hostil implements Electrificable {

    private final double radioExplosion;
    private boolean cargado;

    public Creeper() {
        super("Creeper", 20, new Hitbox(0.6, 1.7), "Polvora",
                "texturas/creeper.png", "siseo", 0.25, 16);
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

    @Override
    public String recibirRayo() {
        this.cargado = true;
        return "Creeper cargado por un rayo: ahora explota mas fuerte.";
    }

    /** Reemplaza el default de Electrificable con un aviso propio del Creeper. */
    @Override
    public String avisoRayo() {
        return "El Creeper sisea mas fuerte mientras el rayo lo carga.";
    }

    public boolean isCargado() { return cargado; }
    public double getRadioExplosion() { return radioExplosion; }
}
