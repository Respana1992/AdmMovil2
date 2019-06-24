package misclases;

public class Deuda {
	int secuencial;
	String bodega;
	String cliente;
	String tipo;
	int numero;
	String serie;
	String secinv;
	double iva;
	double monto;
	double credito;
	double saldo;
	String fechaemi;
	String fechaven;
	String vendedor;
	String observacion;
	
	public Deuda(int secuencial,String bodega,String cliente,String tipo,int numero,String serie,String secinv,double iva,
				 double monto,double credito,double saldo,String fechaemi,String fechaven,String vendedor,String observacion)
	{
		this.secuencial=secuencial;
		this.bodega=bodega;
		this.cliente=cliente;
		this.tipo=tipo;
		this.numero=numero;
		this.serie=serie;
		this.secinv=secinv;
		this.iva=iva;
		this.monto=monto;
		this.credito=credito;
		this.saldo=saldo;
		this.fechaemi=fechaemi;
		this.fechaven=fechaven;
		this.vendedor=vendedor;
		this.observacion=observacion;
	}

	public Deuda(int numero, double saldo, String fechaven)
	{
		this.numero=numero;

		this.saldo=saldo;

		this.fechaven=fechaven;

	}

	public Deuda(int numero, double saldo, String fechaven, String tipo)
	{
		this.numero=numero;

		this.saldo=saldo;

		this.fechaven=fechaven;

		this.tipo = tipo;

	}
	
	public int getSecuencial()
	{
		return this.secuencial;
	}
	public void setSecuencial(int secuencial)
	{
		this.secuencial=secuencial;
	}
	
	public String getBodega()
	{
		return this.bodega;
	}
	public void setBodega(String bodega)
	{
		this.bodega=bodega;
	}
	
	public String getCliente()
	{
		return this.cliente;
	}
	public void setCliente(String cliente)
	{
		this.cliente=cliente;
	}
	
	public String getTipo()
	{
		return this.tipo;
	}
	public void setTipo(String tipo)
	{
		this.tipo=tipo;
	}
	
	public int getNumero()
	{
		return this.numero;
	}
	public void setNumero(int numero)
	{
		this.numero=numero;
	}
	
	public String getSerie()
	{
		return this.serie;
	}
	public void setSerie(String serie)
	{
		this.serie=serie;
	}
	
	public String getSecinv()
	{
		return this.secinv;
	}
	public void setSecinv(String secinv)
	{
		this.secinv=secinv;
	}
	
	public double getIva()
	{
		return this.iva;
	}
	public void setIva(double iva)
	{
		this.iva=iva;
	}
	
	public double getMonto()
	{
		return this.monto;
	}
	public void setMonto(double monto)
	{
		this.monto=monto;
	}
	
	public double getCredito()
	{
		return this.credito;
	}
	public void setCredito(double credito)
	{
		this.credito=credito;
	}
	
	public double getSaldo()
	{
		return this.saldo;
	}
	public void setSaldo(double saldo)
	{
		this.saldo=saldo;
	}
	
	public String getFechaEmi()
	{
		return this.fechaemi;
	}
	public void setFechaEmi(String fechaemi)
	{
		this.fechaemi=fechaemi;
	}
	
	public String getFechaVen()
	{
		return this.fechaven;
	}
	public void setFechaVen(String fechaven)
	{
		this.fechaven=fechaven;
	}
	
	public String getVendedor()
	{
		return this.vendedor;
	}
	public void setVendedor(String vendedor)
	{
		this.vendedor=vendedor;
	}
	
	public String getObservacion()
	{
		return this.observacion;
	}
	public void setObservacion(String observacion)
	{
		this.observacion=observacion;
	}
}