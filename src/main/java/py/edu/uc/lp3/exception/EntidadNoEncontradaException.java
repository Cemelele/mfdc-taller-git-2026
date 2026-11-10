package py.edu.uc.lp3.exception;

public class EntidadNoEncontradaException extends EntidadException {
    public EntidadNoEncontradaException(Long id) {
        super("No se encontro la entidad con ID: " + id);
    }
}