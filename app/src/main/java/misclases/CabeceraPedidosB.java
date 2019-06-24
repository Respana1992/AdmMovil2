package misclases;

public class CabeceraPedidosB {
	private int secauto;
	private String tipo;
	private int bodega;
	private int numero;
	private int secuencial;
	private String cliente;
	private String usuario; //vendria a ser el vendedor
	private double subtotal;
	private double descuento;
	private double iva;
	private double neto;
	private String operador; //vendra a ser el mismo usuario o vendedor
	private String observacion;
	private String fechaIngreso;
	private String horaIngreso;
	private String estado;
	private String docfac;
	private String ltd;
	private String lng;

	public CabeceraPedidosB(int secauto, String tipo, int bodega, int numero, int secuencial, String cliente, String usuario,
                            double subtotal, double descuento, double iva, double neto, String operador, String observacion,
                            String fechaIngreso, String horaIngreso, String estado, String docfac)
	{
		this.secauto=secauto;
		this.tipo=tipo;
		this.bodega=bodega;
		this.numero=numero;
		this.secuencial=secuencial;
		this.cliente=cliente;
		this.usuario=usuario;
		this.subtotal=subtotal;
		this.descuento=descuento;
		this.iva=iva;
		this.neto=neto;
		this.operador=operador;
		this.observacion=observacion;
		this.fechaIngreso=fechaIngreso;
		this.horaIngreso=horaIngreso;
		this.estado=estado;
		this.docfac=docfac;


	}

	public int getSecAuto()
	{
		return this.secauto;
	}
	public void setSecAuto(int secauto)
	{
		this.secauto=secauto;
	}
	
	
	public String getTipo()
	{
		return this.tipo;
	}
	public void setTipo(String tipo)
	{
		this.tipo=tipo;
	}
	
	
	public int getBodega()
	{
		return this.bodega;
	}
	public void setBodega(int bodega)
	{
		this.bodega=bodega;
	}
	
	
	public int getNumero()
	{
		return this.numero;
	}
	public void setNumero(int numero)
	{
		this.numero=numero;
	}
	
	
	public int getSecuencial()
	{
		return this.secuencial;
	}
	public void setSecuencial(int secuencial)
	{
		this.secuencial=secuencial;
	}
	
	
	public String getCliente()
	{
		return this.cliente;
	}
	public void setCliente(String cliente)
	{
		this.cliente=cliente;
	}
	
	
	public String getUsuario()
	{
		return this.usuario;
	}
	public void setUsuario(String usuario)
	{
		this.usuario=usuario;
	}
	
	
	public double getSubtotal()
	{
		return this.subtotal;
	}
	public void setSubtotal(double subtotal)
	{
		this.subtotal=subtotal;
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
	
	
	public String getOperador()
	{
		return this.operador;
	}
	public void setOperador(String operador)
	{
		this.operador=operador;
	}
	
	
	public String getObservacion()
	{
		return this.observacion;
	}
	public void setObservacion(String observacion)
	{
		this.observacion=observacion;
	}
	
	
	public String getFechaIngreso()
	{
		return this.fechaIngreso;
	}
	public void setFechaIngreso(String fechaIngreso)
	{
		this.fechaIngreso=fechaIngreso;
	}
	
	
	public String getHoraIngreso()
	{
		return this.horaIngreso;
	}
	public void setHoraIngreso(String horaIngreso)
	{
		this.horaIngreso=horaIngreso;
	}
	
	
	public String getEstado()
	{
		return this.estado;
	}
	public void setEstado(String estado)
	{
		this.estado=estado;
	}
	
	public String getDocfac()
	{
		return this.docfac;
	}
	public void setDocfac(String docfac)
	{
		this.docfac=docfac;
	}

	public String getLtd()
	{
		return this.ltd;
	}
	public void setLtd(String ltd)
	{
		this.ltd=ltd;
	}

	public String getLng()
	{
		return this.lng;
	}
	public void setLng(String lng)
	{
		this.estado=lng;
	}
}