package py.edu.uc.lp3.rest.dto;

import java.util.Locale;

import py.edu.uc.lp3.domain.Creeper;
import py.edu.uc.lp3.domain.Hostil;

/**
 * Contrato HTTP de la familia Hostil. El campo "tipo" es el discriminador
 * explicito del subtipo: el cliente lo manda al crear y lo recibe al leer.
 * Se eligio un DTO con discriminador explicito en vez de exponer las
 * entidades JPA a Jackson, para que las anotaciones de persistencia no
 * dependan del formato de la API.
 */
public final class HostilDTO {

    private HostilDTO() {
    }

    public record HostilRequest(String tipo, String nombre, Integer vida,
                                Double velocidad, Double rangoAggro,
                                Integer danioAtaque, Boolean cargado, Boolean encendido) {
    }

    public record HostilResponse(Long id, String tipo, String nombre, int vida,
                                 double velocidad, double rangoAggro, int danioAtaque,
                                 Double radioExplosion, boolean cargado, boolean encendido,
                                 double x, double y, double z,
                                 String imagen, String sonido) {

        public static HostilResponse desde(Hostil hostil) {
            String tipo = hostil.getClass().getSimpleName().toLowerCase(Locale.ROOT);
            Double radio = hostil instanceof Creeper creeper ? creeper.getRadioExplosion() : null;
            boolean cargado = hostil instanceof Creeper creeper && creeper.isCargado();
            boolean encendido = hostil instanceof Creeper creeper && creeper.isEncendido();
            return new HostilResponse(hostil.getId(), tipo, hostil.getNombre(), hostil.getVida(),
                    hostil.getVelocidad(), hostil.getRangoAggro(), hostil.getDanioAtaque(),
                    radio, cargado, encendido, hostil.getX(), hostil.getY(), hostil.getZ(),
                    hostil.getImagen(), hostil.getSonido());
        }
    }
}
