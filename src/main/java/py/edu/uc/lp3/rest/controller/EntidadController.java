package py.edu.uc.lp3.rest.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import py.edu.uc.lp3.domain.Aldeano;
import py.edu.uc.lp3.domain.Cerdo;
import py.edu.uc.lp3.domain.Creeper;
import py.edu.uc.lp3.domain.Enderman;
import py.edu.uc.lp3.domain.Entidad;
import py.edu.uc.lp3.domain.Esqueleto;
import py.edu.uc.lp3.domain.Jugador;
import py.edu.uc.lp3.domain.Zombie;

/**
 * Construye entidades del dominio a partir de los parametros de la URL
 * y las describe usando solo la API de {@link Entidad}, el tipo padre.
 *
 * Los parametros de la URL alimentan constructores del dominio (simples
 * y sobrecargados) y los mensajes sobrecargados recibirDanio y avanzar.
 * Si un valor rompe una regla, la clase lo rechaza con una
 * IllegalArgumentException y el controller la informa como 400.
 *
 *   GET /entidad/zombie
 *   GET /entidad/zombie?vida=30&amp;danio=10&amp;atacante=Creeper
 *   GET /entidad/aldeano?profesion=Herrero
 *   GET /entidad/cerdo?item=Silla%20de%20montar
 *   GET /entidad/esqueleto?danio=-5        (la clase rechaza el dano negativo)
 *   GET /entidades
 */
@RestController
public class EntidadController {

    private static final List<String> TIPOS =
            List.of("creeper", "zombie", "esqueleto", "enderman", "cerdo", "aldeano");

    @GetMapping("/entidad/{tipo}")
    public Map<String, Object> crear(@PathVariable String tipo,
                                     @RequestParam(defaultValue = "") String jugador,
                                     @RequestParam(defaultValue = "0") int danio,
                                     @RequestParam(defaultValue = "") String atacante,
                                     @RequestParam(defaultValue = "") String profesion,
                                     @RequestParam(defaultValue = "") String item,
                                     @RequestParam(defaultValue = "2.0") double distancia,
                                     @RequestParam(defaultValue = "0") int vida) {
        Entidad entidad = crearEntidad(tipo, profesion, vida);
        Jugador objetivo = jugador.isBlank() ? new Jugador() : new Jugador(jugador);
        if (!item.isBlank()) {
            objetivo.recoger(item);
        }
        return describir(entidad, objetivo, danio, atacante, distancia);
    }

    /** Todas las entidades del mundo, cada una mostrando su propio comportamiento. */
    @GetMapping("/entidades")
    public List<Map<String, Object>> todas() {
        Jugador objetivo = new Jugador();
        return TIPOS.stream()
                .map(tipo -> crearEntidad(tipo, "", 0))
                .map(entidad -> describir(entidad, objetivo, 0, "", 2.0))
                .toList();
    }

    /**
     * El constructor que se usa depende de los datos de la URL: ahi se ve
     * la sobrecarga de constructores. Un valor ilegal (vida no positiva,
     * nombre en blanco) lo rechaza la clase, no este metodo.
     */
    private Entidad crearEntidad(String tipo, String profesion, int vida) {
        return switch (tipo.toLowerCase()) {
            case "creeper" -> new Creeper();
            case "zombie" -> vida == 0 ? new Zombie() : new Zombie(vida);
            case "esqueleto" -> new Esqueleto();
            case "enderman" -> new Enderman();
            case "cerdo" -> new Cerdo();
            case "aldeano" -> profesion.isBlank() ? new Aldeano() : new Aldeano(profesion);
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Tipo de entidad desconocido: " + tipo);
        };
    }

    /**
     * Usa solo la API de Entidad (el tipo padre): el mismo codigo sirve
     * para cualquier subclase. El texto de "comportamiento" lo escribe la
     * clase hija sobreescrita, nunca este controller.
     */
    private Map<String, Object> describir(Entidad entidad, Jugador objetivo,
                                          int danio, String atacante, double distancia) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("entidad", entidad);
        json.put("comportamiento", entidad.comportamiento(objetivo));
        json.put("avanzar", entidad.avanzar(distancia));
        json.put("rotar", entidad.rotar(90));
        json.put("saltar", entidad.saltar());
        if (danio != 0) {
            json.put("recibirDanio", atacante.isBlank()
                    ? entidad.recibirDanio(danio)
                    : entidad.recibirDanio(danio, atacante));
        }
        json.put("vidaFinal", entidad.getVida());
        json.put("viva", !entidad.estaMuerto());
        return json;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> argumentoInvalido(IllegalArgumentException e) {
        return Map.of("error", e.getMessage());
    }
}
