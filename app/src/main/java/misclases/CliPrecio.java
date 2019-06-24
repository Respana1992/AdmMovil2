package misclases;

public class CliPrecio {
	int precio;
	String tipo;
	String fecha;
	String operador;
	
	public CliPrecio()
	{
	}
	
	public CliPrecio(int precio,String tipo,String fecha,String operador)
	{
		this.precio=precio;
		this.tipo=tipo;
		this.fecha=fecha;
		this.operador=operador;
	}
	
	public int getPrecio()
	{
		return this.precio;
	}
	public void setPrecio(int precio)
	{
		this.precio=precio;
	}
	
	
	public String getTipo()
	{
		return this.tipo;
	}
	public void setTipo(String tipo)
	{
		this.tipo=tipo;
	}
	
	
	public String getFecha()
	{
		return this.fecha;
	}
	public void setFecha(String fecha)
	{
		this.fecha=fecha;
	}
	
	
	public String getOperador()
	{
		return this.operador;
	}
	public void setOperador(String operador)
	{
		this.operador=operador;
	}
}
