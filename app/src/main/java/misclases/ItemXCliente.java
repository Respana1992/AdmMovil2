package misclases;

public class ItemXCliente {
	String cliente;
	String item;
	double precio;
	String fechadesde;
	String fechahasta;
	
	public ItemXCliente()
	{
	}
	
	public ItemXCliente(String cliente,String item,double precio,String fechadesde,String fechahasta)
	{
		this.cliente=cliente;
		this.item=item;
		this.precio=precio;
		this.fechadesde=fechadesde;
		this.fechahasta=fechahasta;
	}
	
	
	
	public String getCliente()
	{
		return this.cliente;
	}
	public void setCliente(String cliente)
	{
		this.cliente=cliente;
	}
	
	public String getItem()
	{
		return this.item;
	}
	public void setItem(String item)
	{
		this.item=item;
	}
	
	public double getPrecio()
	{
		return this.precio;
	}
	public void setPrecio(double precio)
	{
		this.precio=precio;
	}
	
	public String getFechaDesde()
	{
		return this.fechadesde;
	}
	public void setFechaDesde(String fechadesde)
	{
		this.fechadesde=fechadesde;
	}
	
	public String getFechaHasta()
	{
		return this.fechahasta;
	}
	public void setFechaHasta(String fechahasta)
	{
		this.fechahasta=fechahasta;
	}
}