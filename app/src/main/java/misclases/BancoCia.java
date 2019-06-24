package misclases;

public class BancoCia {
    String codigo;
    String nombre;
    String numerocuenta;
    String estado;

    public BancoCia(String codigo, String nombre, String estado, String numerocuenta)
    {
        this.codigo=codigo;
        this.nombre=nombre;
        this.estado=estado;
        this.numerocuenta=numerocuenta;
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

    public String getNumeroCuenta()
    {
        return this.numerocuenta;
    }
    public void setNumeroCuenta(String numerocuenta)
    {
        this.numerocuenta=numerocuenta;
    }
}
