package py.edu.uc.lp3.exception;

/**
 * Se lanza cuando una simulacion de explosion apunta a un Creeper que no
 * existe en la base de datos. Extiende la excepcion de "no encontrado"
 * para que el manejador global responda 404 Not Found.
 */
public class CreeperNoEncontradoException extends EntidadNoEncontradaException {

    public CreeperNoEncontradoException(Long id) {
        super("No se encontro el Creeper con ID: " + id);
    }

    public CreeperNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
