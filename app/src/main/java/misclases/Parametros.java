package misclases;

public class Parametros {
	private String nombrecia;
	private int iva;
	private int bodfac;
	private int faclin;
	private String paginaweb;
	private String eprecio;
	private String enotaventa;
	private String cartera;
	
	public Parametros()	{}
	
	public Parametros(String cia,int iva,int bodfac,int faclin,String paginaweb,String eprecio,String enotaventa,String cartera)
	{
		this.nombrecia = cia;
        this.iva = iva;
        this.bodfac = bodfac;
        this.faclin = faclin;
        this.paginaweb=paginaweb;
        this.eprecio = eprecio;
        this.enotaventa=enotaventa;
        this.cartera=cartera;
	}
	
	public String getNombreCia()
	{
		return this.nombrecia;
	}
	public void setNombreCia(String cia)
	{
		this.nombrecia=cia;
	}
	
	
	public int getIva()
	{
		return this.iva;
	}
	public void setIva(int iva)
	{
		this.iva=iva;
	}
	
	
	public int getBodegaFac()
	{
		return this.bodfac;
	}
	public void setBodegaFac(int bodfac)
	{
		this.bodfac=bodfac;
	}
	
	
	public int getNumeroLineaPed()
	{
		return this.faclin;
	}
	public void setNumeroLineaPed(int faclin)
	{
		this.faclin=faclin;
	}
	
	public String getPaginaWeb()
	{
		return this.paginaweb;
	}
	public void setPaginaWeb(String paginaweb)
	{
		this.paginaweb=paginaweb;
	}
	
	public String getEprecio()
	{
		return this.eprecio;
	}
	public void setEprecio(String eprecio)
	{
		this.eprecio=eprecio;
	}
	
	public String getEnotaventa()
	{
		return this.enotaventa;
	}
	public void setEnotaventa(String enotaventa)
	{
		this.enotaventa=enotaventa;
	}

	public String getCartera()
	{
		return this.cartera;
	}
	public void setCartera(String cartera)
	{
		this.cartera=cartera;
	}
}