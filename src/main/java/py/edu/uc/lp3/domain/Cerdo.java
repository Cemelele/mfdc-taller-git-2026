package py.edu.uc.lp3.domain;

public class Cerdo extends NoHostil implements Electrificable {

    private boolean ensillado;

    public Cerdo() {
        super("Cerdo", 10, new Hitbox(0.9, 0.9), "Carne de cerdo",
                "texturas/cerdo.png", "oink", 0.2);
        this.ensillado = false;
    }

    @Override
    public String interactuar(Jugador jugador) {
        if (jugador.getInventario().contains("Silla de montar") && !ensillado) {
            ensillado = true;
            return "El cerdo ahora esta ensillado y " + jugador.getNombre() + " puede montarlo.";
        }
        reproducirSonido("oink");
        return "El cerdo gruñe y sigue caminando.";
    }

    /** El rayo no lo mata: lo convierte en Piglin Zombificado. */
    @Override
    public String recibirRayo() {
        return "El cerdo se transforma en Piglin Zombificado." + reproducirSonido("zombificar");
    }

    public boolean estaEnsillado() { return ensillado; }
}
