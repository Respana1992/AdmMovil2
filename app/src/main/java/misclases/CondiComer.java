package misclases;

public class CondiComer {
	private int secuencial;
	private String nombre;
	private String fecdesde;
    private String fechasta;
    private String fechacre;
    private int tipo;
    private int aplicacion;
    private int alcance;
    private double cantdesde;
    private double canthasta;
    private double valordesde;
    private double valorhasta;
    private int modo;
    private double pordes;
    private String item_reg;
    private int can_reg;
    private String cliente;
    private String item;
    private String grupo_cli;
    private String grupo_pro;
    private String tipoapli;
    private String todos;
    private String cuenta;
    private String cuenta0;
    private String ptoventa;
    
    public CondiComer()
    {
    }
    
    public CondiComer(int sec,String nombre,String fecdesde,String fechasta,String feccre,int tipo,int apli,int alcance,
    				  double cantdesde,double canthasta,double valordesde,double valorhasta,int modo,double pordes,String itemreg,
    				  int canreg,String cliente,String item,String grupocli,String grupopro,String tipoapli,String todos,
    				  String cuenta,String cuenta0,String ptovta)
    {
    	this.secuencial=sec;
    	this.nombre=nombre;
    	this.fecdesde=fecdesde;
    	this.fechasta=fechasta;
    	this.fechacre=feccre;
    	this.tipo=tipo;
    	this.aplicacion=apli;
    	this.alcance=alcance;
    	this.cantdesde=cantdesde;
    	this.canthasta=canthasta;
    	this.valordesde=valordesde;
    	this.valorhasta=valorhasta;
    	this.modo=modo;
    	this.pordes=pordes;
    	this.item_reg=itemreg;
    	this.can_reg=canreg;
    	this.cliente=cliente;
    	this.item=item;
    	this.grupo_cli=grupocli;
    	this.grupo_pro=grupopro;
    	this.tipoapli=tipoapli;
    	this.todos=todos;
    	this.cuenta=cuenta;
    	this.cuenta0=cuenta0;
    	this.ptoventa=ptovta;
    }
    
    public CondiComer(int sec,String nombre,String fecdesde,String fechasta,String feccre,int tipo,int apli,int alcance,
			  double cantdesde,double canthasta,double valordesde,double valorhasta,int modo,double pordes,String itemreg,
			  int canreg,String cliente,String item,String grupocli,String grupopro)
    {
    	this.secuencial=sec;
    	this.nombre=nombre;
    	this.fecdesde=fecdesde;
    	this.fechasta=fechasta;
    	this.fechacre=feccre;
    	this.tipo=tipo;
    	this.aplicacion=apli;
    	this.alcance=alcance;
    	this.cantdesde=cantdesde;
    	this.canthasta=canthasta;
    	this.valordesde=valordesde;
    	this.valorhasta=valorhasta;
    	this.modo=modo;
    	this.pordes=pordes;
    	this.item_reg=itemreg;
    	this.can_reg=canreg;
    	this.cliente=cliente;
    	this.item=item;
    	this.grupo_cli=grupocli;
    	this.grupo_pro=grupopro;
    }
    
    public int getSecuencial()
    {
    	return this.secuencial;
    }
    public void setSecuencial(int secuencial)
    {
    	this.secuencial=secuencial;
    }
    
    
    public String getNombre()
    {
    	return this.nombre;
    }
    public void setNombre(String nombre)
    {
    	this.nombre=nombre;
    }
    
    
    public String getFecDesde()
    {
    	return this.fecdesde;
    }
    public void setFecDesde(String fecdesde)
    {
    	this.fecdesde=fecdesde;
    }
    
    
    public String getFecHasta()
    {
    	return this.fechasta;
    }
    public void setFecHasta(String fechasta)
    {
    	this.fechasta=fechasta;
    }
    
    
    public String getFechaCre()
    {
    	return this.fechacre;
    }
    public void setFechaCre(String fechaCre)
    {
    	this.fechacre=fechaCre;
    }
    
    
    public int getTipo()
    {
    	return this.tipo;
    }
    public void setTipo(int tipo)
    {
    	this.tipo=tipo;
    }
    
    
    public int getApli()
    {
    	return this.aplicacion;
    }
    public void setApli(int apli)
    {
    	this.aplicacion=apli;
    }
    
    
    public int getAlcance()
    {
    	return this.alcance;
    }
    public void setAlcance(int alcance)
    {
    	this.alcance=alcance;
    }
    
    
    public double getCantDesde()
    {
    	return this.cantdesde;
    }
    public void setCantDesde(double cantdesde)
    {
    	this.cantdesde=cantdesde;
    }
    
    
    public double getCantHasta()
    {
    	return this.canthasta;
    }
    public void setCantHasta(double canthasta)
    {
    	this.canthasta=canthasta;
    }
    
    
    public double getValorDesde()
    {
    	return this.valordesde;
    }
    public void setValorDesde(double valordesde)
    {
    	this.valordesde=valordesde;
    }
    
    
    public double getValorHasta()
    {
    	return this.valorhasta;
    }
    public void setValorHasta(double valorhasta)
    {
    	this.valorhasta=valorhasta;
    }
    
    
    public int getModo()
    {
    	return this.modo;
    }
    public void setModo(int modo)
    {
    	this.modo=modo;
    }
    
    
    public double getPordes()
    {
    	return this.pordes;
    }
    public void setPordes(double pordes)
    {
    	this.pordes=pordes;
    }
    
    
    public String getItemReg()
    {
    	return this.item_reg;
    }
    public void setItemReg(String itemreg)
    {
    	this.item_reg=itemreg;
    }
    
    
    public int getCanReg()
    {
    	return this.can_reg;
    }
    public void setCanReg(int canreg)
    {
    	this.can_reg=canreg;
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
    
    
    public String getGrupoCli()
    {
    	return this.grupo_cli;
    }
    public void setGrupoCli(String grupocli)
    {
    	this.grupo_cli=grupocli;
    }
    
    
    public String getGrupoPro()
    {
    	return this.grupo_pro;
    }
    public void setGrupoPro(String grupopro)
    {
    	this.grupo_pro=grupopro;
    }
    
    
    public String getTipoApli()
    {
    	return this.tipoapli;
    }
    public void setTipoApli(String tipoapli)
    {
    	this.tipoapli=tipoapli;
    }
    
    
    public String getTodos()
    {
    	return this.todos;
    }
    public void setTodos(String todos)
    {
    	this.todos=todos;
    }
    
    
    public String getCuenta()
    {
    	return this.cuenta;
    }
    public void setCuenta(String cuenta)
    {
    	this.cuenta=cuenta;
    }
    
    
    public String getCuenta0()
    {
    	return this.cuenta0;
    }
    public void setCuenta0(String cuenta0)
    {
    	this.cuenta0=cuenta0;
    }
    
    
    public String getPtoVta()
    {
    	return this.ptoventa;
    }
    public void setPtoVta(String ptovta)
    {
    	this.ptoventa=ptovta;
    }
}