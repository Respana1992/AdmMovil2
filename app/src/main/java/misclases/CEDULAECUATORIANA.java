package misclases;

public class CEDULAECUATORIANA {
	private boolean valida;
	private String mensaje;
	
	public CEDULAECUATORIANA()
	{
	}
	
	public CEDULAECUATORIANA(boolean valida,String mensaje)
	{
		this.valida=valida;
		this.mensaje=mensaje;
	}
	
	public boolean getValida()
	{
		return this.valida;
	}
	public void setValida(boolean estado)
	{
		this.valida=estado;
	}
	
	public String getMensaje()
	{
		return this.mensaje;
	}
	public void setMensaje(String mensaje)
	{
		this.mensaje=mensaje;
	}

}
