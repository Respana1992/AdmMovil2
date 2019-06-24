package misclases;

public class Item {
	private String item;
	private String nombre;
	private String nombrecorto;
    private String categoria;
    private String familia;
    private String linea;
    private String marca;
    private String presenta;
    private String estado;
    private String dispoven;
    private String iva;
    private String bien;
    private String proveedor;
    private int factor;
    private int stock;
    private double stockmi;
    private double stockma;
    private double peso;
    private double volumen;
    private double precio1;
    private double precio2;
    private double precio3;
    private double precio4;
    private double precio5;
    private double pvp;
    private String itemr;
    private String ultven;
    private String ultcom;
    private double costop;
    private double costou;
    private String observa;
    private String grupo;
    private String combo;
    private String regalo;
    private String codprov;
    private double poruti;
    private double porutiventa;
    private String codbarra;
    private String canfra;
    private int stockmay;
    private double porutipre1;
    private double porutipre2;
    private double porutipre3;
    private double porutipre4;
    private double porutipre5;
    private double litros;
    private String web;
    private String oferta;
    private double poferta;
    private String novedad;
    private String imagen;
    private double cantcompra;
    private String solopos;
    private String cuentaventa;
    private String espt;
    private String imagenadicional;
    private String tienectaventa;
    private String tipoprofal;
    private double pordessugerido;
    private String nombre_categoria;
    private String nombre_familia;
    private String nombre_linea;
    private String nombre_marca;
    private String nombre_presenta;
    private String nombre_proveedor;
    private double precioped;
    private double precioItemXCliente;
	
	public Item(String item,String nombre,String nombrecorto,String categoria,String familia,String linea,
            String marca,String presenta,String estado,String dispoven,String iva,String bien,
            String proveedor,int factor,int stock,double stockmi,double stockma,double peso,
            double volumen,double precio1,double precio2,double precio3,double precio4,double precio5,
            double pvp,String itemr,String ultven,String ultcom,double costop,double costou,String observa,
            String grupo,String combo,String regalo,String codprov,double poruti,double porutiventa,
            String codbarra,String canfra,int stockmay,double porutipre1,double porutipre2,double porutipre3,
            double porutipre4,double porutipre5,double litros,String web,String oferta,double poferta,
            String novedad,String imagen,double cantcompra,String solopos,String cuentaventa,String espt,
            String imagenadicional,String tienectaventa,String tipoprofal,double pordessugerido,
            String nombre_categoria,String nombre_familia,String nombre_linea,String nombre_marca,
            String nombre_presenta, String nombre_proveedor)
	{
		this.item=item;
        this.nombre=nombre;
        this.nombrecorto=nombrecorto;
        this.categoria=categoria;
        this.familia=familia;
        this.linea=linea;
        this.marca=marca;
        this.presenta=presenta;
        this.estado=estado;
        this.dispoven=dispoven;
        this.iva=iva;
        this.bien=bien;
        this.proveedor=proveedor;
        this.factor=factor;
        this.stock=stock;
        this.stockmi=stockmi;
        this.stockma=stockma;
        this.peso=peso;
        this.volumen=volumen;
        this.precio1=precio1;
        this.precio2=precio2;
        this.precio3=precio3;
        this.precio4=precio4;
        this.precio5=precio5;
        this.pvp=pvp;
        this.itemr=itemr;
        this.ultven=ultven;
        this.ultcom=ultcom;
        this.costop=costop;
        this.costou=costou;
        this.observa=observa;
        this.grupo=grupo;
        this.combo=combo;
        this.regalo=regalo;
        this.codprov=codprov;
        this.poruti=poruti;
        this.porutiventa=porutiventa;
        this.codbarra=codbarra;
        this.canfra=canfra;
        this.stockmay=stockmay;
        this.porutipre1=porutipre1;
        this.porutipre2=porutipre2;
        this.porutipre3=porutipre3;
        this.porutipre4=porutipre4;
        this.porutipre5=porutipre5;
        this.litros=litros;
        this.web=web;
        this.oferta=oferta;
        this.poferta=poferta;
        this.novedad=novedad;
        this.imagen=imagen;
        this.cantcompra=cantcompra;
        this.solopos=solopos;
        this.cuentaventa=cuentaventa;
        this.espt=espt;
        this.imagenadicional=imagenadicional;
        this.tienectaventa=tienectaventa;
        this.tipoprofal=tipoprofal;
        this.pordessugerido=pordessugerido;
        this.nombre_categoria=nombre_categoria;
        this.nombre_familia=nombre_familia;
        this.nombre_linea=nombre_linea;
        this.nombre_marca=nombre_marca;
        this.nombre_presenta=presenta;
        this.nombre_proveedor=proveedor;
	}
	
	public Item(String item,String nombre, int factor, int stock, double precioped, String iva, String tipo,double poruti,double costop)
	{
		this.item=item;
		this.nombre=nombre;
		this.factor=factor;
		this.stock=stock;
		this.precioped=precioped;
		this.iva=iva;
		this.bien=tipo; //indica si el item es de BIEN (B) o de SERVICIO (S)
		this.poruti=poruti;
		this.costop=costop;
	}
	
	public Item(String item,double precio)
	{
		this.item=item;
		this.precioItemXCliente = precio;
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
		return this.nombre;
	}
	public void setNombre(String nombre)
	{
		this.nombre=nombre;
	}
	
	
	public String getNombreCorto()
	{
		return this.nombrecorto;
	}
	public void setCajas(String nombreCorto)
	{
		this.nombrecorto=nombreCorto;
	}
	
	
	public String getCategoria()
	{
		return this.categoria;
	}
	public void setCategoria(String categoria)
	{
		this.categoria=categoria;
	}
	
	
	public String getFamilia()
	{
		return this.familia;
	}
	public void setFamilia(String familia)
	{
		this.familia=familia;
	}
	
	
	public String getLinea()
	{
		return this.linea;
	}
	public void setLinea(String linea)
	{
		this.linea=linea;
	}
	
	
	public String getMarca()
	{
		return this.marca;
	}
	public void setMarca(String marca)
	{
		this.marca=marca;
	}
	
	
	public String getPresenta()
	{
		return this.presenta;
	}
	public void setPresenta(String presenta)
	{
		this.presenta=presenta;
	}
	
	
	public String getEstado()
	{
		return this.estado;
	}
	public void setEstado(String estado)
	{
		this.estado=estado;
	}
	
	
	public String getDispoVen()
	{
		return this.dispoven;
	}
	public void setDispoVen(String dispoven)
	{
		this.dispoven=dispoven;
	}
	
	
	public String getIva()
	{
		return this.iva;
	}
	public void setIva(String iva)
	{
		this.iva=iva;
	}
	
	
	public String getBien()
	{
		return this.bien;
	}
	public void setBien(String bien)
	{
		this.bien=bien;
	}
	
	
	public String getProveedor()
	{
		return this.proveedor;
	}
	public void setProveedor(String proveedor)
	{
		this.proveedor=proveedor;
	}
	
	
	public int getFactor()
	{
		return this.factor;
	}
	public void setFactor(int factor)
	{
		this.factor=factor;
	}
	
	
	public int getStock()
	{
		return this.stock;
	}
	public void setStock(int stock)
	{
		this.stock=stock;
	}
	
	
	public Double getStockMi()
	{
		return this.stockmi;
	}
	public void setStockMi(double stockmi)
	{
		this.stockmi=stockmi;
	}
	
	
	public Double getStockMa()
	{
		return this.stockma;
	}
	public void setStockMa(double stockma)
	{
		this.stockma=stockma;
	}
	
	
	public Double getPeso()
	{
		return this.peso;
	}
	public void setPeso(double peso)
	{
		this.peso=peso;
	}
	
	
	public Double getVolumen()
	{
		return this.volumen;
	}
	public void setVolumen(double volumen)
	{
		this.volumen=volumen;
	}
	
	
	public Double getPrecio1()
	{
		return this.precio1;
	}
	public void setPrecio1(double precio1)
	{
		this.precio1=precio1;
	}
	
	
	public Double getPrecio2()
	{
		return this.precio2;
	}
	public void setPrecio2(double precio2)
	{
		this.precio2=precio2;
	}
	
	
	public Double getPrecio3()
	{
		return this.precio3;
	}
	public void setPrecio3(double precio3)
	{
		this.precio3=precio3;
	}
	
	
	public Double getPrecio4()
	{
		return this.precio4;
	}
	public void setPrecio4(double precio4)
	{
		this.precio4=precio4;
	}
	
	
	public Double getPrecio5()
	{
		return this.precio5;
	}
	public void setPrecio5(double precio5)
	{
		this.precio5=precio5;
	}
	
	
	public Double getPVP()
	{
		return this.pvp;
	}
	public void setPVP(double pvp)
	{
		this.pvp=pvp;
	}
	
	
	public String getItemReg()
	{
		return this.itemr;
	}
	public void setItemReg(String itemreg)
	{
		this.itemr=itemreg;
	}
	
	
	public String getUltVen()
	{
		return this.ultven;
	}
	public void setUltVen(String ultven)
	{
		this.ultven=ultven;
	}
	
	
	public String getUltCom()
	{
		return this.ultcom;
	}
	public void setUltCom(String ultcom)
	{
		this.ultcom=ultcom;
	}
	
	
	public double getCostoP()
	{
		return this.costop;
	}
	public void setCostoP(double costop)
	{
		this.costop=costop;
	}
	
	
	public double getCostoU()
	{
		return this.costou;
	}
	public void setCostoU(double costou)
	{
		this.costou=costou;
	}
	
	
	public String getObserva()
	{
		return this.observa;
	}
	public void setObserva(String observa)
	{
		this.observa=observa;
	}
	
	
	public String getGrupo()
	{
		return this.grupo;
	}
	public void setgrupo(String grupo)
	{
		this.grupo=grupo;
	}
	
	
	public String getCombo()
	{
		return this.combo;
	}
	public void setCombo(String combo)
	{
		this.combo=combo;
	}
	
	
	public String getRegalo()
	{
		return this.regalo;
	}
	public void setRegalo(String regalo)
	{
		this.regalo=regalo;
	}
	
	
	public String getCodProv()
	{
		return this.codprov;
	}
	public void setCodProv(String codprov)
	{
		this.codprov=codprov;
	}
	
	
	public double getPoruti()
	{
		return this.poruti;
	}
	public void setPoruti(double poruti)
	{
		this.poruti=poruti;
	}
	
	
	public double getPorutiVenta()
	{
		return this.porutiventa;
	}
	public void setPorutiVenta(double porutiventa)
	{
		this.porutiventa=porutiventa;
	}
	
	
	public String getCodBarra()
	{
		return this.codbarra;
	}
	public void setCodBarra(String codbarra)
	{
		this.codbarra=codbarra;
	}
	
	
	public String getCanFra()
	{
		return this.canfra;
	}
	public void setCanFra(String canfra)
	{
		this.canfra=canfra;
	}
	
	
	public int getStockMay()
	{
		return this.stockmay;
	}
	public void setStockMay(int stockmay)
	{
		this.stockmay=stockmay;
	}
	
	
	public double getPorutiPre1()
	{
		return this.porutipre1;
	}
	public void setPorutiPre1(double porutipre1)
	{
		this.porutipre1=porutipre1;
	}
	
	
	public double getPorutiPre2()
	{
		return this.porutipre2;
	}
	public void setPorutiPre2(double porutipre2)
	{
		this.porutipre2=porutipre2;
	}
	
	
	public double getPorutiPre3()
	{
		return this.porutipre3;
	}
	public void setPorutiPre3(double porutipre3)
	{
		this.porutipre3=porutipre3;
	}
	
	
	public double getPorutiPre4()
	{
		return this.porutipre4;
	}
	public void setPorutiPre4(double porutipre4)
	{
		this.porutipre4=porutipre4;
	}
	
	
	public double getPorutiPre5()
	{
		return this.porutipre5;
	}
	public void setPorutiPre5(double porutipre5)
	{
		this.porutipre5=porutipre5;
	}
	
	
	public double getLitros()
	{
		return this.litros;
	}
	public void setLitros(double litros)
	{
		this.litros=litros;
	}
	
	
	public String getWeb()
	{
		return this.web;
	}
	public void setWeb(String web)
	{
		this.web=web;
	}
	
	
	public String getOferta()
	{
		return this.oferta;
	}
	public void setOferta(String oferta)
	{
		this.oferta=oferta;
	}
	
	
	public Double getPOferta()
	{
		return this.poferta;
	}
	public void setWeb(double poferta)
	{
		this.poferta=poferta;
	}
	
	
	public String getNovedad()
	{
		return this.novedad;
	}
	public void setNovedad(String novedad)
	{
		this.novedad=novedad;
	}
	
	
	public String getImagen()
	{
		return this.imagen;
	}
	public void setImagen(String imagen)
	{
		this.imagen=imagen;
	}
	
	
	public double getCantCompra()
	{
		return this.cantcompra;
	}
	public void setCantCompra(double cantcompra)
	{
		this.cantcompra=cantcompra;
	}
	
	
	public String getSoloPos()
	{
		return this.solopos;
	}
	public void setSoloPos(String solopos)
	{
		this.solopos=solopos;
	}
	
	
	public String getCuentaVenta()
	{
		return this.cuentaventa;
	}
	public void setCuentaVenta(String cuentavta)
	{
		this.cuentaventa=cuentavta;
	}
	
	
	public String getESPT()
	{
		return this.espt;
	}
	public void setESPT(String espt)
	{
		this.espt=espt;
	}
	
	
	public String getImagenAdicional()
	{
		return this.imagenadicional;
	}
	public void setImagenAdicional(String imgadi)
	{
		this.imagenadicional=imgadi;
	}
	
	
	public String getTieneCuentaVenta()
	{
		return this.tienectaventa;
	}
	public void setTieneCuentaVenta(String tienectavta)
	{
		this.tienectaventa=tienectavta;
	}
	
	
	public String getTipoProFal()
	{
		return this.tipoprofal;
	}
	public void setTipoProFal(String tipoprofal)
	{
		this.tipoprofal=tipoprofal;
	}
	
	
	public double getPorDesSugerido()
	{
		return this.pordessugerido;
	}
	public void setPorDesSugerido(double pordessug)
	{
		this.pordessugerido=pordessug;
	}
	
	
	public String getNombreCategoria()
	{
		return this.nombre_categoria;
	}
	public void setNombreCategoria(String nombrecate)
	{
		this.nombre_categoria=nombrecate;
	}
	
	
	public String getNombreFamilia()
	{
		return this.nombre_familia;
	}
	public void setNombreFamilia(String nombrefami)
	{
		this.nombre_familia=nombrefami;
	}
	
	
	public String getNombreLinea()
	{
		return this.nombre_linea;
	}
	public void setNombreLinea(String nombrelin)
	{
		this.nombre_linea=nombrelin;
	}
	
	
	public String getNombreMarca()
	{
		return this.nombre_marca;
	}
	public void setNombreMarca(String nombremarca)
	{
		this.nombre_marca=nombremarca;
	}
	
	
	public String getNombrePresenta()
	{
		return this.nombre_presenta;
	}
	public void setNombrePresenta(String nombrepres)
	{
		this.nombre_presenta=nombrepres;
	}
	
	
	public String getNombreProveedor()
	{
		return this.nombre_proveedor;
	}
	public void setNombreProveedor(String nombreprov)
	{
		this.nombre_proveedor=nombreprov;
	}
	
	
	public Double getPrecioPed()
	{
		return this.precioped;
	}
	public void setPrecioPed(double precioped)
	{
		this.precioped=precioped;
	}
	
	public Double getPrecioItemXCliente()
	{
		return this.precioItemXCliente;
	}
	public void getPrecioItemXCliente(double precioItemCli)
	{
		this.precioItemXCliente=precioItemCli;
	}
}