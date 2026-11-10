package py.edu.uc.lp3.exception;

public class TipoInvalidoException extends EntidadException {
    public TipoInvalidoException(String tipo) {
        super("Tipo invalido: " + tipo);
    }
}
