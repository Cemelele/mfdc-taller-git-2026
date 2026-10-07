package py.edu.uc.lp3.domain;

import java.util.ArrayList;
import java.util.List;

public class Aldeano extends NoHostil {

    private String profesion;
    private final List<String> ofertas;

    /** Constructor simple: un aldeano sin profesion explicita es Granjero. */
    public Aldeano() {
        this("Granjero");
    }

    /** Constructor sobrecargado: otra lista de argumentos (la profesion). */
    public Aldeano(String profesion) {
        super("Aldeano", 20, new Hitbox(0.6, 1.95), "Nada");
        if (profesion == null || profesion.isBlank()) {
            throw new IllegalArgumentException("El aldeano necesita una profesion");
        }
        this.profesion = profesion;
        this.ofertas = new ArrayList<>();
    }

    public String agregarOferta(String oferta) {
        if (oferta == null || oferta.isBlank()) {
            throw new IllegalArgumentException("No se puede agregar una oferta vacia");
        }
        ofertas.add(oferta);
        return "El aldeano (" + profesion + ") agrega la oferta: " + oferta + ".";
    }

    @Override
    public String interactuar(Jugador jugador) {
        reproducirSonido("hmm");
        StringBuilder resultado = new StringBuilder("El aldeano (" + profesion
                + ") abre el menu de comercio con " + jugador.getNombre() + ": ");
        if (ofertas.isEmpty()) {
            resultado.append("sin ofertas disponibles");
        } else {
            resultado.append(String.join(", ", ofertas));
        }
        return resultado.toString();
    }

    public String getProfesion() { return profesion; }
    public List<String> getOfertas() { return List.copyOf(ofertas); }
}
