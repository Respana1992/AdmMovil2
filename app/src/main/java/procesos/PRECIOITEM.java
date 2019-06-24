package procesos;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import conexionBase.BDTablas;
import misclases.Item;

public class PRECIOITEM {
	String scriptSQL="";
	BDTablas dbcon;
	SQLiteDatabase db;
	Cursor cur;
	Context contexto;
	
	public PRECIOITEM()
	{
	}
	
	public PRECIOITEM(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public PRECIOITEM(Context cont, SQLiteDatabase db)
	{
		this.contexto=cont;
		this.db = db;
	}
	
	public int precioPorTipoCliente(String tipoCli)
	{
		scriptSQL = "select PRECIO from CLIPRECIO where TIPO='"+ tipoCli +"'";
		cur = db.rawQuery(scriptSQL, null);
		if(cur.moveToFirst())
		{
			//retorna el precio q me da la tabla
			return cur.getInt(0);
		}
		else
		{
			//retorna precio 1
			return 1;
		}
	}
	
	public ArrayList<Item> getPrecioClienteXItem(String cliente,String fecha)
	{
		ArrayList<Item> alItemXCli = new ArrayList<Item>();
		Item i;
		String[] tmp;
		String fechatabla;
		tmp = fecha.split("/");
		if(Integer.parseInt(tmp[1]) < 10)
			tmp[1]= "0"+tmp[1];
		fechatabla = tmp[2]+""+tmp[1]+""+tmp[0];
		
		scriptSQL = "SELECT CLIENTE,ITEM,PRECIO FROM ITEMXCLIENTE WHERE " + 
				"(substr(FECHADESDE,7,4) || substr(FECHADESDE,4,2) ||substr(FECHADESDE,1,2))<='"+ fechatabla +"' " +
				"AND (substr(FECHAHASTA,7,4) || substr(FECHAHASTA,4,2) ||substr(FECHAHASTA,1,2))>='"+ fechatabla +"' " +
				"and CLIENTE='"+ cliente +"'";
		
		cur = db.rawQuery(scriptSQL, null);
		if(cur.moveToFirst())
		{
			do{
				i = new Item(cur.getString(1),cur.getDouble(2));
				alItemXCli.add(i);
			}while(cur.moveToNext());
		}
		return alItemXCli;
	}
	
	public double getPrecioItem(int numPrecio,String item)
	{
		scriptSQL = "select PRECIO"+ String.valueOf(numPrecio) +" from ITEM where item='"+ item +"'";
		cur = db.rawQuery(scriptSQL, null);
		if(cur.moveToFirst())
		{
			//retorna el precio q me da la tabla
			return cur.getDouble(0);
		}
		else
		{
			//retorna precio 1
			return 1;
		}
	}
}
