package minecraft;

import java.util.ArrayList;
import java.util.List;

public class Aldeano extends NoHostil {

    private String profesion;
    private final List<String> ofertas;

    public Aldeano(String profesion) {
        super("Aldeano", 20, new Hitbox(0.6, 1.95), "Nada");
        this.profesion = profesion;
        this.ofertas = new ArrayList<>();
    }

    public void agregarOferta(String oferta) {
        ofertas.add(oferta);
    }

    @Override
    public void interactuar(Jugador jugador) {
        reproducirSonido("hmm");
        System.out.println("El aldeano (" + profesion + ") abre el menu de comercio con " + jugador.getNombre() + ":");
        if (ofertas.isEmpty()) {
            System.out.println("  - sin ofertas disponibles");
        } else {
            for (String oferta : ofertas) {
                System.out.println("  - " + oferta);
            }
        }
    }

    public String getProfesion() { return profesion; }
}
