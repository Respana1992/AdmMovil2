package misclases;

public class GrupoCli {
	private String codigo;
	private String nombre;
	private String cliente;
	private String ruta;
	private String clase;
	private String vendedor;
	private String tipo;
	private String estado;
	
	public GrupoCli()
	{
		
	}
	
	public GrupoCli(String codigo,String nombre,String cliente,String ruta,String clase,String vendedor,
					String tipo,String estado)
	{
		this.codigo=codigo;
		this.nombre=nombre;
		this.cliente=cliente;
		this.ruta=ruta;
		this.clase=clase;
		this.vendedor=vendedor;
		this.tipo=tipo;
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
	
	
	public String getCliente()
	{
		return this.cliente;
	}
	public void setCliente(String cliente)
	{
		this.cliente=cliente;
	}
	
	
	public String getRuta()
	{
		return this.ruta;
	}
	public void setRuta(String ruta)
	{
		this.ruta=ruta;
	}
	
	
	public String getClase()
	{
		return this.clase;
	}
	public void setClase(String clase)
	{
		this.clase=clase;
	}
	
	
	public String getVendedor()
	{
		return this.vendedor;
	}
	public void setVendedor(String vendedor)
	{
		this.vendedor=vendedor;
	}
	
	
	public String getTipo()
	{
		return this.tipo;
	}
	public void setTipo(String tipo)
	{
		this.tipo=tipo;
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