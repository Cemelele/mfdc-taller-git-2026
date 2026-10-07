package py.edu.uc.lp3.domain;

public class Cerdo extends NoHostil {

    private boolean ensillado;

    public Cerdo() {
        super("Cerdo", 10, new Hitbox(0.9, 0.9), "Carne de cerdo");
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

    public boolean estaEnsillado() { return ensillado; }
}
