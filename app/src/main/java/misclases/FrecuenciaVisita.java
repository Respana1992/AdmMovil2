package misclases;

public class FrecuenciaVisita {
	String codigo;
	String nombre;
	int dia;
	
	public FrecuenciaVisita(String codigo, String nombre, int dia)
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
	
	
	public int getDia()
	{
		return this.dia;
	}
	public void setDia(int dia)
	{
		this.dia=dia;
	}
}
