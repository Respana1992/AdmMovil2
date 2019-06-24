package conexionBase;

public class CREDENCIALESBASE {
	private String base;
	private int version;
	
	public CREDENCIALESBASE()
	{
	}
	
	public CREDENCIALESBASE(String base,int version)
	{
		this.base = base;
		this.version = version;
	}
	
	public String getBase()
	{
		return this.base;
	}
	public void setBase(String base)
	{
		this.base = base;
	}
	
	public int getVersion()
	{
		return this.version;
	}
	public void setVersion(int version)
	{
		this.version = version;
	}
}
