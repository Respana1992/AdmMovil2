package misclases;

public class DetallesPedidos {
	int linea;
	int secuencial;
	String item;
	String nombreItem;
	int cajas;
	int unidades;
	int total_unidades;
	double precio;
	double subtotal;
	double pordes;
	double descuento;
	double iva;
	double neto;
	String tipoItem;
	String formaVta;
	String estado;
	String fechaIngreso;
	String horaIngreso;
	String usuario;
	String escambio;
	
	public DetallesPedidos()
	{
	}
	
	public DetallesPedidos(String item)
	{
		this.item=item;
	}
	
	public DetallesPedidos(int linea,int secuencial,String item,String nombre,int cajas,int unidades,int totalUnidades,double precio,
			   double subtotal,double pordes,double descuento,double iva,double neto,String tipoItem,
			   String formaVta,String estado,String fechaIng,String horaIng,String usuario)
	{
		this.linea=linea;
		this.secuencial=secuencial;
		this.item=item;
		this.nombreItem=nombre;
		this.cajas=cajas;
		this.unidades=unidades;
		this.total_unidades=totalUnidades;
		this.precio=precio;
		this.subtotal=subtotal;
		this.pordes=pordes;
		this.descuento=descuento;
		this.iva = iva;
		this.neto=neto;
		this.tipoItem=tipoItem;
		this.formaVta=formaVta;
		this.estado=estado;
		this.fechaIngreso=fechaIng;
		this.horaIngreso=horaIng;
		this.usuario=usuario;
	}
	
	public DetallesPedidos(int linea,int secuencial,String item,int cajas,int unidades,int totalUnidades,
						   double precio,double subtotal,double pordes,double descuento,double iva, double neto,
						   String tipoItem,String formaVta,String estado,String fechaIngreso,String horaIngreso,
						   String usuario,String escambio)
	{
		this.linea=linea;
		this.secuencial=secuencial;
		this.item=item;
		this.cajas=cajas;
		this.unidades=unidades;
		this.total_unidades=totalUnidades;
		this.precio=precio;
		this.subtotal=subtotal;
		this.pordes=pordes;
		this.descuento=descuento;
		this.iva=iva;
		this.neto=neto;
		this.tipoItem=tipoItem;
		this.formaVta=formaVta;
		this.estado=estado;
		this.fechaIngreso=fechaIngreso;
		this.horaIngreso=horaIngreso;
		this.usuario=usuario;
		this.escambio=escambio;
	}
	
	public DetallesPedidos(String item,String nombre,int cajas,int unidades,int totalUnidades,double precio,
						   double subtotal,double pordes,double descuento,double iva,double neto,String tipoItem,
						   String formaVta,String estado,String fechaIng,String horaIng,String usuario,String escambio)
	{
		this.item=item;
		this.nombreItem=nombre;
		this.cajas=cajas;
		this.unidades=unidades;
		this.total_unidades=totalUnidades;
		this.precio=precio;
		this.subtotal=subtotal;
		this.pordes=pordes;
		this.descuento=descuento;
		this.iva = iva;
		this.neto=neto;
		this.tipoItem=tipoItem;
		this.formaVta=formaVta;
		this.estado=estado;
		this.fechaIngreso=fechaIng;
		this.horaIngreso=horaIng;
		this.usuario=usuario;
		this.escambio=escambio;
	}
	
	
	
	public int getLinea()
	{
		return this.linea;
	}
	public void setLinea(int linea)
	{
		this.linea=linea;
	}
	
	
	public int getSecuencial()
	{
		return this.secuencial;
	}
	public void setSecuencial(int secuencial)
	{
		this.secuencial=secuencial;
	}
	
	
	public String getItem()
	{
		return this.item;
	}
	public void setItem(String item)
	{
		this.item=item;
	}
	
	
	public String getNombre()
	{
		return this.nombreItem;
	}
	public void setNombre(String nombre)
	{
		this.nombreItem=nombre;
	}
	
	
	public int getCajas()
	{
		return this.cajas;
	}
	public void setCajas(int cajas)
	{
		this.cajas=cajas;
	}
	
	
	public int getUnidades()
	{
		return this.unidades;
	}
	public void setUnidades(int unidades)
	{
		this.unidades=unidades;
	}
	
	
	public int getTotalUnidades()
	{
		return this.total_unidades;
	}
	public void setTotalUnidades(int totalunidades)
	{
		this.total_unidades=totalunidades;
	}
	
	
	public double getPrecio()
	{
		return this.precio;
	}
	public void setPrecio(double precio)
	{
		this.precio=precio;
	}
	
	
	public double getSubtotal()
	{
		return this.subtotal;
	}
	public void setSubtotal(double subtotal)
	{
		this.subtotal=subtotal;
	}
	
	
	public double getPordes()
	{
		return this.pordes;
	}
	public void setPordes(double pordes)
	{
		this.pordes=pordes;
	}
	
	
	public double getDescuento()
	{
		return this.descuento;
	}
	public void setDescuento(double descuento)
	{
		this.descuento=descuento;
	}
	
	
	public double getIva()
	{
		return this.iva;
	}
	public void setIva(double iva)
	{
		this.iva=iva;
	}
	
	
	public double getNeto()
	{
		return this.neto;
	}
	public void setNeto(double neto)
	{
		this.neto=neto;
	}
	
	
	public String getTipoItem()
	{
		return this.tipoItem;
	}
	public void setTipoItem(String tipoItem)
	{
		this.tipoItem=tipoItem;
	}
	
	
	public String getFormaVenta()
	{
		return this.formaVta;
	}
	public void setFormaVenta(String formaVenta)
	{
		this.formaVta=formaVenta;
	}
	
	
	public String getEstado()
	{
		return this.estado;
	}
	public void setEstado(String estado)
	{
		this.estado=estado;
	}
	
	
	public String getfechaIngreso()
	{
		return this.fechaIngreso;
	}
	public void setFechaIngreso(String fechaingresa)
	{
		this.fechaIngreso=fechaingresa;
	}
	
	
	public String getHoraIngreso()
	{
		return this.horaIngreso;
	}
	public void setHoraIngreso(String horaingresa)
	{
		this.horaIngreso=horaingresa;
	}
	
	
	public String getUsuario()
	{
		return this.usuario;
	}
	public void setUsuario(String usuario)
	{
		this.usuario=usuario;
	}
	
	public String getEsCambio()
	{
		return this.escambio;
	}
	public void setEsCambio(String escambio)
	{
		this.escambio=escambio;
	}
}