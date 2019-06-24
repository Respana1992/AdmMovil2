package misclases;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import procesos.MENSAJEEXCEPCIONES;
import procesos.PRECIOITEM;
import procesos.VARIOS;

public class Pedidos {
	private int secauto;
	private String tipo;
	private int bodega;
	private int numero;
	private int secuencial;
	private String cliente;
	private String usuario; //vendedor
	private double subtotal;
	private double descuento;
	private double iva;
	private double neto;
	private String operador;
	private String observacion;
	private String fecha;
	private String hora;
	private String estado;
	private String nombreCliente;
	private String docfac;
	private VARIOS varios;
	private PRECIOITEM precioItem;
	private String _latitud;
	private String _longitud;
	SQLiteDatabase db;
	
	public Pedidos()
	{
	}
	public Pedidos(int secauto,String tipo,int bodega,int numero,int secuencial,String cliente,String usuario,
				   double subtotal,double descuento,double iva,double neto,String operador,String observacion,
				   String fecha,String hora,String estado,String nombreCli,String docfac, String latitud, String longitud)
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
		this.fecha=fecha;
		this.hora=hora;
		this.estado=estado;
		this.nombreCliente=nombreCli;
		this.docfac=docfac;
		_latitud = latitud;
		_longitud = longitud;
	}

	public ArrayList<DetallesPedidos> agregarDetalle(SQLiteDatabase db,String item,String nombre,int cajas,int unidades,double pordes,double precio,
							   						 int factor,int stock,String aplicaIva,String tipo,String usuario,
							   						 ArrayList<DetallesPedidos> alDetallesPed,int valorIva,String escambio,String precioCajaUnidad)
	{
		int totalUnidadesItem = 0;
		double subtotalItem = 0;
		double descuentoItem = 0;
		double ivaItem = 0;
		double netoItem = 0;
		precioItem = new PRECIOITEM(db);
		
		totalUnidadesItem = (factor * cajas) + unidades; //Calclula el cantfun: total de unidades de las cajas (factor * caja) + las unidades
		if(precioCajaUnidad.equals("S"))
		{
			if(totalUnidadesItem>=factor)
				precio = precioItem.getPrecioItem(2, item);
			else
				precio = precioItem.getPrecioItem(1, item);
		}
		
		subtotalItem = precio * totalUnidadesItem; //Calcula el subtotal
		
		cajas = totalUnidadesItem / factor;
		unidades = totalUnidadesItem % factor;
		
		//Calcula el descuento
		if(pordes==0)	descuentoItem = 0;
		else			descuentoItem = (subtotalItem * pordes) / 100;
		
		//Calcula el iva
		if(aplicaIva.equals("S"))	ivaItem = (subtotalItem - descuentoItem) * ((double)valorIva/100);
		else						ivaItem = 0;
		
		netoItem = (subtotalItem - descuentoItem) + ivaItem; //Caclula el neto
		
		varios = new VARIOS();
		fecha = varios.dameFechaSistema();
		hora = varios.dameHoraSistema();
		alDetallesPed.add(new DetallesPedidos(item,nombre,cajas,unidades,totalUnidadesItem,precio,
											  subtotalItem,pordes,descuentoItem,ivaItem,netoItem,tipo,
											  "V","A",this.fecha,this.hora,usuario,escambio));
		return alDetallesPed;
	}
	
	public DetallesPedidos actualizaDetallesXLinea(DetallesPedidos detped,double descuentoCC, String itemaplicaiva,int valorIva)
	{
		double descuentoTotal = 0;
		double netoTotal = 0;
		double ivaTotal = 0;
		
		descuentoTotal = detped.getDescuento() + descuentoCC;
		detped.setDescuento(descuentoTotal);
		
		//Calcula el iva
		if(itemaplicaiva.equals("S"))	ivaTotal = (detped.subtotal - descuentoTotal) * ((double)valorIva/100);
		else							ivaTotal = 0;
		
		detped.setIva(ivaTotal);
		
		netoTotal = (detped.getSubtotal() - detped.descuento) + detped.getIva();
		detped.setNeto(netoTotal);
		
		DetallesPedidos dp = new DetallesPedidos(detped.getItem(),detped.getNombre(),detped.getCajas(),detped.getUnidades(),
												 detped.getTotalUnidades(),detped.getPrecio(),detped.getSubtotal(),
												 detped.getPordes(),detped.getDescuento(),detped.getIva(),detped.getNeto(),
												 detped.getTipoItem(),detped.getFormaVenta(),detped.getEstado(),detped.getfechaIngreso(),
												 detped.getHoraIngreso(),detped.getUsuario(),detped.getEsCambio());
		return dp;
	}
	
	public void calculaTotales(ArrayList<DetallesPedidos> alDetalles)
	{
	}
	
	@SuppressWarnings({ "unused", "finally" })
	public MENSAJEEXCEPCIONES grabarPedido(SQLiteDatabase db,String usuario,String codigoCliente,String observacion,
								ArrayList<DetallesPedidos> alPedido,Context cont,String autoventa,String DocFac,
										   double latitud, double longitud)
	{
		int secPedido,numPedido,bodega;
		double subtotalPed,descuentoPed,ivaPed,netoPed;
		String fecha,hora;
		String insertSQL;
		MENSAJEEXCEPCIONES me = new MENSAJEEXCEPCIONES();
		varios = new VARIOS(cont, db);
		bodega = varios.getBodegaFac();
		subtotalPed=0;
		descuentoPed=0;
		ivaPed=0;
		netoPed=0;

		db.beginTransaction();
		try{
			if(db!=null)
			{
				if(autoventa.equals("S"))
				{
					bodega = varios.getBodegaAutoventa(usuario);
				}
				else
				{
					bodega = varios.getBodegaFac();
				}
				secPedido = varios.getSecuencialPedidoXUsuario(usuario);
				numPedido = varios.getNumeroPedido();
				for(int i=0; i<alPedido.size(); i++)
				{
					fecha = varios.dameFechaSistema();
					hora = varios.dameHoraSistema();
					insertSQL="insert into DETALLESPEDIDOS(linea,secuencial,item,nombre_item,cajas," +
							"unidades,total_unidades,precio,subtotal,pordes,descuento,iva,neto," +
							"tipo_item,forma_venta,estado,fecha_ingreso,hora_ingreso,usuario,escambio,estadob) "+
							"values('"+(i+1)+"','"+secPedido+"','"+alPedido.get(i).getItem()+"','"+alPedido.get(i).getNombre()+"','" +
							alPedido.get(i).getCajas()+"','"+alPedido.get(i).getUnidades()+"','"+alPedido.get(i).getTotalUnidades()+"','"+alPedido.get(i).getPrecio()+"','"+alPedido.get(i).getSubtotal()+"','" +
							alPedido.get(i).getPordes()+"',"+alPedido.get(i).getDescuento()+",'"+alPedido.get(i).getIva()+"','"+alPedido.get(i).getNeto()+"','"+alPedido.get(i).getTipoItem()+"','"+alPedido.get(i).getFormaVenta()+"','"+
							alPedido.get(i).getEstado()+"','"+fecha+"','"+hora+"','"+alPedido.get(i).getUsuario()+"','"+alPedido.get(i).getEsCambio()+"','A')";
					db.execSQL(insertSQL);

					subtotalPed = subtotalPed + alPedido.get(i).getSubtotal();
					descuentoPed = descuentoPed + alPedido.get(i).getDescuento();
					ivaPed = ivaPed + alPedido.get(i).getIva();
					netoPed = netoPed + alPedido.get(i).getNeto();
					if(autoventa.equals("S"))
					{
						this.rebajaStock(db,alPedido.get(i).getItem(), alPedido.get(i).getTotalUnidades());
					}
				}
				
				fecha = varios.dameFechaSistema();
				hora = varios.dameHoraSistema();
				insertSQL="insert into CABECERAPEDIDOS(tipo,bodega,numero,secuencial,cliente,usuario,subtotal," +
						"descuento,iva,neto,operador,observacion,fecha_ingreso,hora_ingreso,estado,docfac,ltd,lng,estadob) " +
						"values('PED','"+ bodega +"','"+numPedido+"','"+secPedido+"','"+codigoCliente+"','"+usuario+"','"+subtotalPed+"','"+descuentoPed+"','"+ivaPed+"','"+netoPed+"','"+
						usuario +"','"+observacion+"','"+fecha+"','"+hora+"','A','"+DocFac +"','"+  latitud +"','"+ longitud +"','A')";
				db.execSQL(insertSQL);
				db.setTransactionSuccessful();
				me = new MENSAJEEXCEPCIONES("",true,numPedido);
			}
			else
			{
				me = new MENSAJEEXCEPCIONES("Problema al acceder a la BD. Método: grabarPedido; Clase: Pedidos",false,0);
			}
		}catch(Exception e){
			me = new MENSAJEEXCEPCIONES("Excepción. Método: grabarPedido; Clase: Pedidos --> "+e.getMessage(),false,0);
		}finally{
			db.endTransaction();
			return me;
		}
	}
	
	public void rebajaStock(SQLiteDatabase db,String item,int cantidad)
	{
		String scriptSql = "";
		scriptSql = "update item set stock = stock - "+ cantidad +" where item='"+ item +"'";
		db.execSQL(scriptSql);
	}
	
	
	@SuppressWarnings({ "unused", "finally" })
	public MENSAJEEXCEPCIONES modificarPedido(SQLiteDatabase db,String usuario,String codigoCliente,String observacion,
								ArrayList<DetallesPedidos> alPedido,Context cont,int secped,int numped,String autoventa,String docfac)
	{
		int secPedido,numPedido,bodega;
		double subtotalPed,descuentoPed,ivaPed,netoPed;
		String fecha,hora;
		String insertSQL,updateSQL,scriptSQL;
		MENSAJEEXCEPCIONES me = new MENSAJEEXCEPCIONES();
		varios = new VARIOS(cont, db);
		bodega = varios.getBodegaFac();
		subtotalPed=0;
		descuentoPed=0;
		ivaPed=0;
		netoPed=0;
		Cursor c;

		db.beginTransaction();
		try{
			if(db!=null)
			{
				secPedido = secped;
				numPedido = numped;
				
				scriptSQL = "select * from detallespedidos where secuencial='"+ secPedido +"' and usuario='"+ usuario +"'";
				c = db.rawQuery(scriptSQL, null);
				if(c.moveToFirst())
				{
					do{
						scriptSQL = "update ITEM set stock=stock + "+ c.getInt(6) +" where item='"+ c.getString(2)+"'";
						db.execSQL(scriptSQL);
					}while(c.moveToNext());
				}
				
				scriptSQL = "delete from detallespedidos where secuencial='"+ secPedido +"' and usuario='"+ usuario +"'";
				db.execSQL(scriptSQL);
				for(int i=0; i<alPedido.size(); i++)
				{
					fecha = varios.dameFechaSistema();
					hora = varios.dameHoraSistema();
					insertSQL="insert into DETALLESPEDIDOS(linea,secuencial,item,nombre_item,cajas," +
							"unidades,total_unidades,precio,subtotal,pordes,descuento,iva,neto," +
							"tipo_item,forma_venta,estado,fecha_ingreso,hora_ingreso,usuario,escambio) "+
							"values('"+(i+1)+"','"+secPedido+"','"+alPedido.get(i).getItem()+"','"+alPedido.get(i).getNombre()+"','" +
							alPedido.get(i).getCajas()+"','"+alPedido.get(i).getUnidades()+"','"+alPedido.get(i).getTotalUnidades()+"','"+alPedido.get(i).getPrecio()+"','"+alPedido.get(i).getSubtotal()+"','" +
							alPedido.get(i).getPordes()+"',"+alPedido.get(i).getDescuento()+",'"+alPedido.get(i).getIva()+"','"+alPedido.get(i).getNeto()+"','"+alPedido.get(i).getTipoItem()+"','"+alPedido.get(i).getFormaVenta()+"','"+
							alPedido.get(i).getEstado()+"','"+fecha+"','"+hora+"','"+alPedido.get(i).getUsuario()+"','"+alPedido.get(i).getEsCambio()+"')";
					db.execSQL(insertSQL);

					subtotalPed = subtotalPed + alPedido.get(i).getSubtotal();
					descuentoPed = descuentoPed + alPedido.get(i).getDescuento();
					ivaPed = ivaPed + alPedido.get(i).getIva();
					netoPed = netoPed + alPedido.get(i).getNeto();
					
					if(autoventa.equals("S"))
					{
						this.rebajaStock(db,alPedido.get(i).getItem(), alPedido.get(i).getTotalUnidades());
					}
				}
				
				fecha = varios.dameFechaSistema();
				hora = varios.dameHoraSistema();
				
				updateSQL = "update CABECERAPEDIDOS set subtotal='"+ subtotalPed +"'," + 
														"descuento='"+ descuentoPed +"'," + 
														"iva='"+ ivaPed +"'," +
														"neto='"+ netoPed +"'," +
														"docfac='"+ docfac +"' "+
							"where secuencial='"+ secPedido +"' and usuario='"+ usuario +"'";
				db.execSQL(updateSQL);
				db.setTransactionSuccessful();
				me = new MENSAJEEXCEPCIONES("",true,numPedido);
			}
			else
			{
				me = new MENSAJEEXCEPCIONES("Problema al acceder a la BD. Método: grabarPedido; Clase: Pedidos",false,0);
			}
		}catch(Exception e){
			me = new MENSAJEEXCEPCIONES("Excepción. Método: grabarPedido; Clase: Pedidos --> "+e.getMessage(),false,0);
		}finally{
			db.endTransaction();
			return me;
		}
	}
	
	public ArrayList<DetallesPedidos> getDetallesXPedido(SQLiteDatabase db,int secped,String codVendedor)
	{
		String scriptSQL;
		Cursor c;
		ArrayList<DetallesPedidos> dp = new ArrayList<DetallesPedidos>();
		scriptSQL = "SELECT linea,secuencial,item,nombre_item,cajas,unidades,total_unidades,precio,subtotal,pordes," +
					"descuento,iva,neto,tipo_item,forma_venta,estado,fecha_ingreso,hora_ingreso,usuario " +
					"FROM detallespedidos where secuencial='"+secped+"' and usuario='"+codVendedor+"'";
		c = db.rawQuery(scriptSQL, null);
		if(c.moveToFirst())
		{
			do{
				dp.add(new DetallesPedidos(c.getInt(0),c.getInt(1),c.getString(2),c.getString(3),c.getInt(4),
										   c.getInt(5),c.getInt(6),c.getDouble(7),c.getDouble(8),c.getDouble(9),
										   c.getDouble(10),c.getDouble(11),c.getDouble(12),c.getString(13),
										   c.getString(14),c.getString(15),c.getString(16),c.getString(17),
										   c.getString(18)));
				
			}while(c.moveToNext());
		}
		return dp;
	}
	
	@SuppressWarnings("finally")
	public MENSAJEEXCEPCIONES eliminaPedido(SQLiteDatabase db,int secpedido,String vendedor)
	{
		String scriptSQL;
		MENSAJEEXCEPCIONES me = new MENSAJEEXCEPCIONES();
		try{
			scriptSQL = "delete from cabecerapedidos where secuencial = '"+secpedido+"' and usuario = '"+vendedor+"'";
			db.execSQL(scriptSQL);
			
			scriptSQL = "delete from detallespedidos where secuencial = '"+secpedido+"' and usuario = '"+vendedor+"'";
			db.execSQL(scriptSQL);
			me = new MENSAJEEXCEPCIONES("",true);
		}catch(Exception e){
			me = new MENSAJEEXCEPCIONES("Excepción al eliminar el pedido con secuencial "+secpedido+" del vendedor "+vendedor+". Clase: Pedidos; Método: eliminaPedido --> " + e.getMessage(),false);
		}finally{
			return me;
		}
	}
	
	@SuppressWarnings("finally")
	public MENSAJEEXCEPCIONES restableceEstadoPedido(SQLiteDatabase db,int secpedido,String vendedor)
	{
		String scriptSQL;
		MENSAJEEXCEPCIONES me = new MENSAJEEXCEPCIONES();
		try{
			scriptSQL = "update cabecerapedidos set estado = 'A' where secuencial='"+ secpedido +"' and usuario = '"+ vendedor +"'";
			db.execSQL(scriptSQL);
			
			scriptSQL = "update detallespedidos set estado = 'A' where secuencial='"+ secpedido +"' and usuario = '"+ vendedor +"'";
			db.execSQL(scriptSQL);
			me = new MENSAJEEXCEPCIONES("",true);
		}catch(Exception e){
			me = new MENSAJEEXCEPCIONES("Excepción al querer restablecer el pedido con secuencial "+secpedido+" del vendedor "+vendedor+". Clase: Pedidos; Método: eliminaPedido --> " + e.getMessage(),false);
		}finally{
			return me;
		}
	}
	
	
	
	
	
	
	
	public int getSecauto()
	{
		return this.secauto;
	}
	public void setSecauto(int secauto)
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
	
	public String getFecha()
	{
		return this.fecha;
	}
	public void setFecha(String fecha)
	{
		this.fecha=fecha;
	}
	
	public String getHora()
	{
		return this.hora;
	}
	public void setHora(String hora)
	{
		this.hora=hora;
	}
	
	public String getEstado()
	{
		return this.estado;
	}
	public void setEstado(String estado)
	{
		this.estado=estado;
	}
	
	public String getNombreCliente()
	{
		return this.nombreCliente;
	}
	public void setNombreCliente(String nombrecli)
	{
		this.nombreCliente=nombrecli;
	}
	
	public String getDocFac()
	{
		return this.docfac;
	}
	public void setDocFac(String docfac)
	{
		this.docfac=docfac;
	}

	public String get_latitud() {
		return _latitud;
	}

	public void set_latitud(String _latitud) {
		this._latitud = _latitud;
	}

	public String get_longitud() {
		return _longitud;
	}

	public void set_longitud(String _longitud) {
		this._longitud = _longitud;
	}
}