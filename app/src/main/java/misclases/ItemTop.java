package misclases;

public class ItemTop {
	String vendedor;
	String item;
	String operador;
	String fechareg;
	
	public ItemTop(String vendedor,String item,String operador,String fechareg)
	{
		this.vendedor =vendedor;
		this.item =item;
		this.operador=operador;
		this.fechareg=fechareg;
	}
	
	public String getVendedor()
	{
		return this.vendedor;
	}
	public void setVendedor(String vendedor)
	{
		this.vendedor=vendedor;
	}
	
	public String getItem()
	{
		return this.item;
	}
	public void setItem(String item)
	{
		this.item=item;
	}
	
	public String getOperador()
	{
		return this.operador;
	}
	public void setOperador(String operador)
	{
		this.operador=operador;
	}
	
	public String getFechareg()
	{
		return this.fechareg;
	}
	public void setFechareg(String fechareg)
	{
		this.fechareg=fechareg;
	}
}
