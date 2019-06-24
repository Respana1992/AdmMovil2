package misclases;

public class Usuario {
	private String codigo;
	private String nombre;
	private String clave;
	private String supervisor;
	private String direccion;
	private String cedula;
	private String telefono;
	private String estado;
	private String crea_cliente;
	private String coloca_descuento;
	private String edita_precio;
	private String es_autoventa;
	private int bodega;
	private String ventop;
	
	public Usuario()
	{
	}

	public Usuario(String codigo,String nombre,String clave,String supervisor,String direccion,String cedula,
				   String telefono,String estado)
	{
		this.codigo=codigo;
		this.nombre=nombre;
		this.clave=clave;
		this.supervisor=supervisor;
		this.direccion=direccion;
		this.cedula=cedula;
		this.telefono=telefono;
		this.estado=estado;
	}
	
	public Usuario(String codigo,String nombre,String clave,String supervisor,String direccion,String cedula,
			   String telefono,String estado,String creacli,String colocades,String ediprecio,String esautovta,
			   int bodega,String ventop)
	{
		this.codigo=codigo;
		this.nombre=nombre;
		this.clave=clave;
		this.supervisor=supervisor;
		this.direccion=direccion;
		this.cedula=cedula;
		this.telefono=telefono;
		this.estado=estado;
		this.crea_cliente=creacli;
		this.coloca_descuento=colocades;
		this.edita_precio=ediprecio;
		this.es_autoventa=esautovta;
		this.bodega=bodega;
		this.ventop=ventop;
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
	
	
	public String getClave()
	{
		return this.clave;
	}
	public void setClave(String clave)
	{
		this.clave=clave;
	}
	
	
	public String getSupervisor()
	{
		return this.supervisor;
	}
	public void setSupervisor(String supervisor)
	{
		this.supervisor=supervisor;
	}
	
	
	public String getDireccion()
	{
		return this.direccion;
	}
	public void setDireccion(String direccion)
	{
		this.direccion=direccion;
	}
	
	
	public String getCedula()
	{
		return this.cedula;
	}
	public void setCedula(String cedula)
	{
		this.cedula=cedula;
	}
	
	
	public String getTelefono()
	{
		return this.telefono;
	}
	public void setTelefono(String telefono)
	{
		this.telefono=telefono;
	}
	
	
	public String getEstado()
	{
		return this.estado;
	}
	public void setEstado(String estado)
	{
		this.estado=estado;
	}
	
	
	public String getCreaCliente()
	{
		return this.crea_cliente;
	}
	public void setCreaCliente(String creacli)
	{
		this.crea_cliente=creacli;
	}
	
	
	public String getColocaDescuento()
	{
		return this.coloca_descuento;
	}
	public void setColocaDescuento(String colocades)
	{
		this.coloca_descuento=colocades;
	}
	
	
	public String getEditaPrecio()
	{
		return this.edita_precio;
	}
	public void setEditaPrecio(String ediprecio)
	{
		this.edita_precio=ediprecio;
	}
	
	
	public String getEsAutoVenta()
	{
		return this.es_autoventa;
	}
	public void setEsAutoVenta(String esautovta)
	{
		this.es_autoventa=esautovta;
	}
	
	public int getBodega()
	{
		return this.bodega;
	}
	public void setBodega(int bodega)
	{
		this.bodega=bodega;
	}
	
	public String getVentop()
	{
		return this.ventop;
	}
	public void setVentop(String ventop)
	{
		this.ventop=ventop;
	}
}
