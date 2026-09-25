package minecraft;

public class Cerdo extends NoHostil {

    private boolean ensillado;

    public Cerdo() {
        super("Cerdo", 10, new Hitbox(0.9, 0.9), "Carne de cerdo");
        this.ensillado = false;
    }

    @Override
    public void interactuar(Jugador jugador) {
        if (jugador.getInventario().contains("Silla de montar") && !ensillado) {
            ensillado = true;
            System.out.println("El cerdo ahora esta ensillado y " + jugador.getNombre() + " puede montarlo.");
        } else {
            System.out.println("El cerdo gruñe y sigue caminando.");
            reproducirSonido("oink");
        }
    }

    public boolean estaEnsillado() { return ensillado; }
}
