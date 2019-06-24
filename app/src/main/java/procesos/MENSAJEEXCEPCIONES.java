package procesos;

public class MENSAJEEXCEPCIONES {
	private String mensaje;
	private boolean estado;
	private int numped;
	
	public MENSAJEEXCEPCIONES()
	{
	}
	
	public MENSAJEEXCEPCIONES(String mensaje,boolean estado)
	{
		this.mensaje=mensaje;
		this.estado=estado;
	}
	
	//Este constructor lo uso para los pedidos, mostrar un mensajer de excepcion en caso de haberlo,
	//mostrar un estado de como se almaceno el pedido, y el numero del pedido almacenado
	public MENSAJEEXCEPCIONES(String mensaje,boolean estado,int numped)
	{
		this.mensaje=mensaje;
		this.estado=estado;
		this.numped=numped;
	}
	
	public String formaMensajeExcepcion(String mensaje,String actividad,String metodo,String mensajeExcepcion)
	{
		return mensaje + ": Actividad: " + actividad + "; Método: " + metodo + " --> Excepción: " + mensajeExcepcion;
	}
	

	public String getMensaje()
	{
		return this.mensaje;
	}
	public void setMensaje(String mensaje)
	{
		this.mensaje=mensaje;
	}
	
	public boolean getEstado()
	{
		return this.estado;
	}
	public void setEstado(boolean estado)
	{
		this.estado=estado;
	}
	
	public int getNumPed()
	{
		return this.numped;
	}
	public void setNumPed(int numped)
	{
		this.numped=numped;
	}
}