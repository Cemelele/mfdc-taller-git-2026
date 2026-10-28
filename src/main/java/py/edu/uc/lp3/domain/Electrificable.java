package py.edu.uc.lp3.domain;

/**
 * Capacidad compartida por entidades de familias distintas: reaccionar
 * cuando un rayo las alcanza.
 *
 * No pertenece a la jerarquia de Entidad: es un rol que una clase decide
 * cumplir. Lo comparten el Creeper (familia Hostil), que se carga, y el
 * Cerdo (familia NoHostil), que se transforma.
 *
 * La interfaz declara un mensaje que devuelve texto y ofrece un default;
 * una implementacion puede reemplazar ese default (el Creeper lo hace).
 */
public interface Electrificable {

    /**
     * Mensaje que describe que le pasa a la entidad al recibir un rayo.
     * Cada clase lo escribe a su modo.
     */
    String recibirRayo();

    /**
     * Aviso previo al efecto. Es un default: sirve para cualquier entidad
     * electrificable, pero una clase puede reemplazarlo con su propio texto.
     */
    default String avisoRayo() {
        return "El cielo se ilumina: algo va a cambiar.";
    }
}
