package BD;

import javax.persistence.*;

/**
 * Created by Gand on 24/02/17.
 */
@Entity
public class Coches {
    private String matricula;
    private String marca;
    private Integer precio;
    private String DNI;

    public Coches() {
    }

    public Coches(String matricula, String marca, Integer precio, String DNI) {
        this.matricula = matricula;
        this.marca = marca;
        this.precio = precio;
        this.DNI = DNI;
    }

    @Id
    @Column(name = "Matricula")
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Basic
    @Column(name = "Marca")
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Basic
    @Column(name = "Precio")
    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    @Basic
    @Column(name = "DNI")
    public String getDNI(){
        return  DNI;
    }
    public void setDNI(String DNI){
        this.DNI = DNI;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coches coches = (Coches) o;

        if (matricula != null ? !matricula.equals(coches.matricula) : coches.matricula != null) return false;
        if (marca != null ? !marca.equals(coches.marca) : coches.marca != null) return false;
        if (precio != null ? !precio.equals(coches.precio) : coches.precio != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = matricula != null ? matricula.hashCode() : 0;
        result = 31 * result + (marca != null ? marca.hashCode() : 0);
        result = 31 * result + (precio != null ? precio.hashCode() : 0);
        return result;
    }
}
