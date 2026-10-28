package py.edu.uc.lp3.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class EntidadPersistida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Positive(message = "La vida debe ser positiva")
    private int vida;

    @PositiveOrZero(message = "La velocidad no puede ser negativa")
    private double velocidad;

    private String tipo;
    private String dropItem;
    private String imagen;
    private String sonido;

    private double posX;
    private double posY;
    private double posZ;

    public EntidadPersistida() {
    }

    public EntidadPersistida(String nombre, int vida, double velocidad, String tipo) {
        this.nombre = nombre;
        this.vida = vida;
        this.velocidad = velocidad;
        this.tipo = tipo;
        this.posX = 0.0;
        this.posY = 0.0;
        this.posZ = 0.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDropItem() {
        return dropItem;
    }

    public void setDropItem(String dropItem) {
        this.dropItem = dropItem;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getSonido() {
        return sonido;
    }

    public void setSonido(String sonido) {
        this.sonido = sonido;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }
}
