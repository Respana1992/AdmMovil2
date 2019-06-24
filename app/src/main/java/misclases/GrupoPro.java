package misclases;

public class GrupoPro {
	private String codigo;
	private String nombre;
	private String item;
	private String categoria;
	private String familia;
	private String linea;
	private String marca;
	private String estado;
	
	public GrupoPro()
	{
		
	}
	
	public GrupoPro(String codigo,String nombre,String item,String categoria,String familia,String linea,
					String marca,String estado)
	{
		this.codigo=codigo;
		this.nombre=nombre;
		this.item=item;
		this.categoria=categoria;
		this.familia=familia;
		this.linea=linea;
		this.marca=marca;
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
	
	
	public String getItem()
	{
		return this.item;
	}
	public void setItem(String item)
	{
		this.item=item;
	}
	
	
	public String getCategoria()
	{
		return this.categoria;
	}
	public void setCategoria(String categoria)
	{
		this.categoria=categoria;
	}
	
	
	public String getFamilia()
	{
		return this.familia;
	}
	public void setFamilia(String familia)
	{
		this.familia=familia;
	}
	
	
	public String getLinea()
	{
		return this.linea;
	}
	public void setLinea(String linea)
	{
		this.linea=linea;
	}
	
	
	public String getMarca()
	{
		return this.marca;
	}
	public void setMarca(String marca)
	{
		this.marca=marca;
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