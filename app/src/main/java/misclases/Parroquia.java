package misclases;

public class Parroquia {
    String codigo;
    String canton;
    String provincia;
    String nombre;
    String estado;

    public  Parroquia(String codigo,String canton,String provincia, String nombre, String estado){
        this.codigo = codigo;
        this.canton = canton;
        this.provincia = provincia;
        this.nombre = nombre;
        this.estado = estado;
    }

    public String getCodigo()
    {
        return this.codigo;
    }
    public void setCodigo(String codigo)
    {
        this.codigo=codigo;
    }

    public String getNombre()
    {
        return this.nombre;
    }
    public void setNombre(String nombre)
    {
        this.nombre=nombre;
    }

    public String getEstado()
    {
        return this.estado;
    }
    public void setEstado(String estado)
    {
        this.estado=estado;
    }

    public  String getCanton(){return this.canton;}
    public  void setCanton(String canton){this.nombre=canton;}

    public String getProvincia(){return this.provincia;}
    public void setProvincia(String provincia){this.provincia=provincia;}
}
