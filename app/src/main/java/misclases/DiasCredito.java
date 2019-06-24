package misclases;

public class DiasCredito {
	String codigo;
	String nombre;
	String dia;
	
	public DiasCredito(String codigo, String nombre, String dia)
	{
		this.codigo=codigo;
		this.nombre=nombre;
		this.dia=dia;
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
	
	
	public String getDia()
	{
		return this.dia;
	}
	public void setDia(String dia)
	{
		this.dia=dia;
	}
}
