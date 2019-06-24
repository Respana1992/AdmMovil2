package misclases;

public class Ruta {
	String codigo;
	String nombre;
	String estado;
	
	public Ruta(String codigo, String nombre, String estado)
	{
		this.codigo=codigo;
		this.nombre=nombre;
		this.estado=estado;
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
}
