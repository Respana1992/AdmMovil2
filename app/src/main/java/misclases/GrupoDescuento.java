package misclases;

public class GrupoDescuento {
	String codigo;
	String nombre;
	
	public GrupoDescuento(String codigo, String nombre)
	{
		this.codigo=codigo;
		this.nombre=nombre;
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
}
