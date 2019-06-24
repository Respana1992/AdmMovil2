package procesos;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import conexionBase.BDTablas;
import misclases.DetallesPedidos;
import misclases.CondiComer;
import misclases.Pedidos;
import dialogos.Dialogos;

public class CONDICIONESCOMERCIALES {
	int cantidad,valorIva;
	double monto,valorDescuento;
	String scriptSql,item;
	String grupocli,grupoitem;
	String fechadoc,aplicaiva,codigoven;
	Pedidos ped = new Pedidos();
	DetallesPedidos dped = new DetallesPedidos();
	Dialogos dialogo = new Dialogos();
	BDTablas dbcon;
	SQLiteDatabase db;
	Cursor cur_cli,cur_item,cur_condicomer,cur_gcli,cur_gitem,cur_itemreg,curivaitem;
	CondiComer cc;
	ArrayList<CondiComer> al_cc = new ArrayList<CondiComer>();
	ArrayList<DetallesPedidos> alItemRegalo = new ArrayList<DetallesPedidos>();
	Context con;
	AlertDialog.Builder builder;
	AlertDialog alert;
	VARIOS varios = new VARIOS();
	String hora="";
	
	public CONDICIONESCOMERCIALES()
	{
	}
	
	public CONDICIONESCOMERCIALES(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public ArrayList<DetallesPedidos> getCondicion(Context contexto,String cliente,ArrayList<DetallesPedidos> alDetalles,int valorIva)
	{ 
		con = contexto;
		valorDescuento = 0;
		grupocli="";
		grupoitem="";
		scriptSql="";
		item="";
		fechadoc="01/01/1900";
		cantidad=0;
		monto=0;
		hora = varios.dameHoraSistema();
		scriptSql = "select grupo_descuento from CLIENTE where codigo = '"+ cliente +"'";
		cur_cli = db.rawQuery(scriptSql, null);
		if(cur_cli.moveToFirst())
			grupocli=cur_cli.getString(0);
		
		for(int i=0; i<alDetalles.size(); i++)
		{
			item = alDetalles.get(i).getItem();
			cantidad = alDetalles.get(i).getTotalUnidades();
			monto = alDetalles.get(i).getSubtotal();
			fechadoc=alDetalles.get(i).getfechaIngreso();
			codigoven=alDetalles.get(i).getUsuario();
			scriptSql = "select grupo from item where item='"+item+"'";
			cur_item = db.rawQuery(scriptSql, null);
			if(cur_item.moveToFirst())
			{
				if(cur_item.getString(0)==null)
					grupoitem = "";
				else
					grupoitem=cur_item.getString(0);
			}
			al_cc.clear();
			this.buscaCondicion(cliente, item, grupocli, grupoitem, cantidad, monto, fechadoc);
			
			valorDescuento = this.validaCondicion(contexto,alDetalles.get(i).getTotalUnidades(),alDetalles.get(i).getSubtotal());

			if(valorDescuento>0)
			{
				scriptSql = "select iva from item where item='"+item+"'";
				curivaitem = db.rawQuery(scriptSql, null);
				if(curivaitem.moveToFirst())
				{
					aplicaiva = curivaitem.getString(0);
				}
				else
				{
					aplicaiva="N";
				}
				
				dped = ped.actualizaDetallesXLinea(alDetalles.get(i), valorDescuento, aplicaiva,valorIva);
				alDetalles.set(i, dped);
			}
		}
		if(alItemRegalo.size()>0)
		{
			for(int x=0; x<alItemRegalo.size();x++)
			{
				alDetalles.add(new DetallesPedidos(alItemRegalo.get(x).getItem(),alItemRegalo.get(x).getNombre(),
						alItemRegalo.get(x).getCajas(),alItemRegalo.get(x).getUnidades(),alItemRegalo.get(x).getTotalUnidades(),
						alItemRegalo.get(x).getPrecio(),alItemRegalo.get(x).getSubtotal(),alItemRegalo.get(x).getPordes(),
						alItemRegalo.get(x).getDescuento(),alItemRegalo.get(x).getIva(),alItemRegalo.get(x).getNeto(),
						alItemRegalo.get(x).getTipoItem(),alItemRegalo.get(x).getFormaVenta(),alItemRegalo.get(x).getEstado(),
						alItemRegalo.get(x).getfechaIngreso(),alItemRegalo.get(x).getHoraIngreso(),alItemRegalo.get(x).getUsuario(),alItemRegalo.get(x).getEsCambio()));
			}
		}
		return alDetalles;
	}

	public void buscaCondicion(String cliente,String item,String grupocli,String grupoitem,int cantidad,double monto,String fechadoc)
	{
		String[] tmp;
		String fechaped;
		tmp = fechadoc.split("/");
		if(Integer.parseInt(tmp[1]) < 10)
			tmp[1]= "0"+tmp[1];
		
		if(Integer.parseInt(tmp[0]) < 10)
			tmp[0]= "0"+tmp[0];
		
		fechaped = tmp[2]+""+tmp[1]+""+tmp[0];
		
		//cliente - item
		if(cliente.isEmpty()==false && item.isEmpty()==false)
		{
			scriptSql = "select secuencial,nombre,fecdesde,fechasta,fechacre,tipo,aplicacion,alcance,cantdesde," +
						"canthasta,valordesde,valorhasta,modo,pordes,item_reg,can_reg,cliente,item,grupo_cli,grupo_pro " +
						"from condicionescomerciales where " +
						"cliente='"+cliente+"' and item='"+item+"' and tipo<>3 " +
						"and '"+cantidad+"'>=cantdesde and '"+cantidad+"'<=canthasta and " +
						"'"+monto+"' >=valordesde and '"+monto+"'<=valorhasta and " +
						"'"+fechaped+"' >= (substr(fecdesde,7,4) || substr(fecdesde,4,2) ||substr(fecdesde,1,2)) and '"+fechaped+"' <= (substr(fechasta,7,4) || substr(fechasta,4,2) ||substr(fechasta,1,2))";
			cur_condicomer = db.rawQuery(scriptSql, null);
	
			if(cur_condicomer.moveToFirst())
			{
				llenaCondicion();
			}
		}
		
		//cliente - grupo item
		if(cliente.isEmpty()==false && grupoitem.isEmpty()==false)
		{
			scriptSql = "select secuencial,nombre,fecdesde,fechasta,fechacre,tipo,aplicacion,alcance,cantdesde," +
						"canthasta,valordesde,valorhasta,modo,pordes,item_reg,can_reg,cliente,item,grupo_cli,grupo_pro " +
						"from condicionescomerciales where " +
						"cliente='"+cliente+"' and grupo_pro='"+grupoitem+"' and tipo<>3 " +
						"and '"+cantidad+"'>=cantdesde and '"+cantidad+"'<=canthasta and " +
						"'"+monto+"' >=valordesde and '"+monto+"'<=valorhasta and " +
						"'"+fechaped+"' >= (substr(fecdesde,7,4) || substr(fecdesde,4,2) ||substr(fecdesde,1,2)) and '"+fechaped+"' <= (substr(fechasta,7,4) || substr(fechasta,4,2) ||substr(fechasta,1,2))";
			cur_condicomer = db.rawQuery(scriptSql, null);
			
			if(cur_condicomer.moveToFirst())
			{	
				llenaCondicion();
			}
		}
		
		//item - grupo cliente
		if(item.isEmpty()==false && grupocli.isEmpty()==false)
		{
			scriptSql = "select secuencial,nombre,fecdesde,fechasta,fechacre,tipo,aplicacion,alcance,cantdesde," +
					"canthasta,valordesde,valorhasta,modo,pordes,item_reg,can_reg,cliente,item,grupo_cli,grupo_pro " +
					"from condicionescomerciales where " +
					"grupo_cli='"+grupocli+"' and item='"+item+"' and tipo<>3 " +
					"and '"+cantidad+"'>=cantdesde and '"+cantidad+"'<=canthasta and " +
					"'"+monto+"' >=valordesde and '"+monto+"'<=valorhasta and " +
					"'"+fechaped+"' >= (substr(fecdesde,7,4) || substr(fecdesde,4,2) ||substr(fecdesde,1,2)) and '"+fechaped+"' <= (substr(fechasta,7,4) || substr(fechasta,4,2) ||substr(fechasta,1,2))";
			
			cur_condicomer = db.rawQuery(scriptSql, null);
			
			if(cur_condicomer.moveToFirst())
			{
				llenaCondicion();
			}
		}
		
		//grupo pro - grupo clie
		if(grupoitem.isEmpty()==false && grupocli.isEmpty()==false)
		{
			scriptSql = "select secuencial,nombre,fecdesde,fechasta,fechacre,tipo,aplicacion,alcance,cantdesde," +
					"canthasta,valordesde,valorhasta,modo,pordes,item_reg,can_reg,cliente,item,grupo_cli,grupo_pro " +
					"from condicionescomerciales where " +
					"grupo_cli='"+grupocli+"' and grupo_pro='"+grupoitem+"' and tipo<>3 " +
					"and '"+cantidad+"'>=cantdesde and '"+cantidad+"'<=canthasta and " +
					"'"+monto+"' >=valordesde and '"+monto+"'<=valorhasta and " +
					"'"+fechaped+"' >= (substr(fecdesde,7,4) || substr(fecdesde,4,2) ||substr(fecdesde,1,2)) and '"+fechaped+"' <= (substr(fechasta,7,4) || substr(fechasta,4,2) ||substr(fechasta,1,2))";
			
			cur_condicomer = db.rawQuery(scriptSql, null);
					
			if(cur_condicomer.moveToFirst())
			{
				llenaCondicion();
			}
		}
	}
		
	public void llenaCondicion()
	{
		if (cur_condicomer.moveToFirst()) {
		    do {
		        // do what you need with the cursor here
		    	cc = new CondiComer(cur_condicomer.getInt(0),cur_condicomer.getString(1),cur_condicomer.getString(2),
						cur_condicomer.getString(3),cur_condicomer.getString(4),cur_condicomer.getInt(5),
						cur_condicomer.getInt(6),cur_condicomer.getInt(7),cur_condicomer.getDouble(8),
						cur_condicomer.getDouble(9),cur_condicomer.getDouble(10),cur_condicomer.getDouble(11),
						cur_condicomer.getInt(12),cur_condicomer.getDouble(13),cur_condicomer.getString(14),
						cur_condicomer.getInt(15),cur_condicomer.getString(16),cur_condicomer.getString(17),
						cur_condicomer.getString(18),cur_condicomer.getString(19));
				al_cc.add(cc);
		    } while (cur_condicomer.moveToNext());
		}
		/*cc = new CondiComer(cur_condicomer.getInt(0),cur_condicomer.getString(1),cur_condicomer.getString(2),
				cur_condicomer.getString(3),cur_condicomer.getString(4),cur_condicomer.getInt(5),
				cur_condicomer.getInt(6),cur_condicomer.getInt(7),cur_condicomer.getDouble(8),
				cur_condicomer.getDouble(9),cur_condicomer.getDouble(10),cur_condicomer.getDouble(11),
				cur_condicomer.getInt(12),cur_condicomer.getDouble(13),cur_condicomer.getString(14),
				cur_condicomer.getInt(15),cur_condicomer.getString(16),cur_condicomer.getString(17),
				cur_condicomer.getString(18),cur_condicomer.getString(19));
		al_cc.add(cc);*/	
	}
	
	public double validaCondicion(Context context,int cantvta,double montovta)
	{
		double valor_descuento=0;
		double valor_calculado=0;
		double valor_pordes = 0;

		if(al_cc!=null && al_cc.size()>0)
		{
			for(int i=0; i<al_cc.size(); i++)
			{
				switch(al_cc.get(i).getModo())
				{
					case 1: //porcentaje de descuento
						if(al_cc.get(i).getTipo()==1) //distribucion - escalonado
						{
							valor_pordes = (al_cc.get(i).getPordes() / 100);
							valor_calculado = (montovta - valor_descuento) * valor_pordes;
							valor_descuento = valor_descuento + valor_calculado;
						}
						else
						{
							valor_calculado = (montovta) * (al_cc.get(i).getPordes() / 100);
							valor_descuento = valor_descuento + valor_calculado;
						}
						break;
					case 2: //item de regalo
						valor_calculado=0;
						this.itemRegalo(context,cantvta, al_cc.get(i).getCantDesde(), al_cc.get(i).getCantHasta(), 
										al_cc.get(i).getItemReg(),al_cc.get(i).getCanReg(), montovta, al_cc.get(i).getValorDesde());
						break;
				}
			}
		}
		return valor_descuento;
	}
	
	public void itemRegalo(Context contexto,int cantvta,double cantdesde,double canthasta,String itemreg,int cantreg,
						   double montovta,double valordesde)
	{
		scriptSql = "select item,nombre,dispoven,iva,bien,factor,stock,precio1 from item where item='"+itemreg+"'";
		cur_itemreg = db.rawQuery(scriptSql, null);
		int cantidad_regalo;
		int caja_reg,unidades_reg,pordes_reg;
		double subtotal_reg,descuento_reg,precio_reg,iva_reg,neto_reg;
		
		cantidad_regalo = 0;
		caja_reg = 0;
		unidades_reg = 0;
		pordes_reg=100;
		subtotal_reg = 0;
		descuento_reg = 0;
		precio_reg = 0;
		iva_reg = 0;
		neto_reg = 0;
		
		if(cantdesde>0)
		{
			cantidad_regalo = (cantvta/(int)cantdesde) * cantreg;
		}
		else
		{
			if(valordesde <=0)	valordesde=1;
			cantidad_regalo = ((int)montovta/(int)valordesde) * cantreg;
		}
		
		if(cur_itemreg.moveToFirst())
		{
			caja_reg = (cantidad_regalo / (int)cur_itemreg.getDouble(5)); //cant. de vta / factor --> para sacar las cajas
			unidades_reg = (cantidad_regalo % (int)cur_itemreg.getDouble(5)); //cant. de vta % factor --> el residuo de ls div para las unidades
			precio_reg=	cur_itemreg.getDouble(7);
			subtotal_reg=precio_reg *cantidad_regalo;
			descuento_reg=subtotal_reg;	
			alItemRegalo.add(new DetallesPedidos(itemreg,cur_itemreg.getString(1),caja_reg,unidades_reg,cantidad_regalo,
						                         precio_reg,subtotal_reg,pordes_reg,descuento_reg,iva_reg,neto_reg,"V",
						                         "R","A",fechadoc,hora,codigoven,"N"));
		}
		else
		{
			dialogo.miDialogoToastCorto(contexto,"No existe el item de regalo "+itemreg+" en el maestro de cliente");
		}
	}
}