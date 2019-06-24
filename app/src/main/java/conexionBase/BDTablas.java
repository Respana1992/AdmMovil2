package conexionBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BDTablas extends SQLiteOpenHelper {
	
	String tablaUsuario = "CREATE TABLE USUARIO(tipo text,codigo text,nombre text,clave text,supervisor text,direccion text,cedula text,telefono text,estado text,crea_cliente text,coloca_descuento text,edita_precio text,es_autoventa text,bodega integer, ventop text)";
	
	String tablaUsuarioSecuenciales = "CREATE TABLE USUARIO_SECUENCIALES(codigo TEXT,secuencial integer)";
	
	String tablaCliente = "create table CLIENTE (" +
			  "codigo text," +
			  "nombre text," +
			  "negocio text," +
			  "representa text," +
			  "tipo_identificacion text," +
			  "numero_identificacion text," +
			  "direccion text," +
			  "telefono text," +
			  "email text," +
			  "tipo_cliente text," +
			  "provincia text," +
			  "canton text," +
			  "parroquia text," +
			  "sector text," +
			  "ruta text," +
			  "cupo text," +
			  "grupo_descuento text," +
			  "orden integer," +
			  "frec_visita text," +
			  "credito text," +
			  "dia integer," +
			  "fecha_desde text," +
			  "fecha_ingreso text," +
			  "hora_ingreso text," +
			  "fecha_ult_com text," +
			  "td_credito text," +
			  "dias_credito integer," +
			  "usuario text," +
			  "forma_pago text," +
			  "iva text," +
			  "con_final text," +
			  "clase text," +
			  "observacion text," +
			  "tipo_negocio text," +
			  "ejex text," +
			  "ejey text," +
			  "vendedor_aux text," +
			  "fecha_modifica text," +
			  "hora_modifica text," +
			  "usuario_modifica text," +
			  "fecha_elimina text," +
			  "hora_elimina text," +
			  "usuario_elimina text," +
			  "estado text," +
			  "cltenuevo text," +
			  "descargado text," +
			  "clavefe text,"+
			"referencia text,"+
			"zona text)";
	
	String tablaUsuarioCliente = "create table USUARIO_CLIENTE (" +
			  "codigo_usuario text," +
			  "codigo_cliente text," +
			  "tipo_usuario text)";
	
	String tablaTipoUsuario = "create table TIPO_USUARIO (" +
			  "codigo text," +
			  "nombre text)";
	
	String tablaTipoNegocio = "create table TIPO_NEGOCIO (" +
			  "codigo text," +
			  "nombre text," +
			  "estado text)";
	
	String tablaTipoCliente = "create table TIPO_CLIENTE (" +
			  "codigo text," +
			  "nombre text," +
			  "estado text)";
	
	String tablaFrecuenciaVisita = "create table FRECUENCIA_VISITA (" +
			  "codigo text," +
			  "nombre text," +
			  "dia integer)";
	
	String tablaRuta = "create table RUTA (" +
			  "codigo text," +
			  "nombre text," +
			  "estado text)";
	
	String tablaItem = "create table ITEM (" +
			  "item text," +
			  "nombre text," +
			  "nombrecorto text," +
			  "categoria text," +
			  "familia text," +
			  "linea text," +
			  "marca text," +
			  "presenta text," +
			  "estado text," +
			  "dispoven text," +
			  "iva text," +
			  "bien text," +
			  "proveedor text," +
			  "factor numeric(10,2)," +
			  "stock numeric(10,2)," +
			  "stockmi numeric(10,2)," +
			  "stockma numeric(10,2)," +
			  "peso real," +
			  "volumen real," +
			  "precio1 numeric(10,2)," +
			  "precio2 numeric(10,2)," +
			  "precio3 numeric(10,2)," +
			  "precio4 numeric(10,2)," +
			  "precio5 numeric(10,2)," +
			  "pvp numeric(10,2)," +
			  "itemr text," +
			  "ultven text," +
			  "ultcom text," +
			  "costop real," +
			  "costou real," +
			  "observa text," +
			  "grupo text," +
			  "combo text," +
			  "regalo text," +
			  "codprov text," +
			  "poruti real," +
			  "porutiventa real," +
			  "codbarra text," +
			  "canfra text," +
			  "stockmay integer," +
			  "porutipre1 real," +
			  "porutipre2 real," +
			  "porutipre3 real," +
			  "porutipre4 real," +
			  "porutipre5 real," +
			  "litros real," +
			  "web text," +
			  "oferta text," +
			  "poferta real," +
			  "novedad text," +
			  "imagen text," +
			  "cantcompra numeric(10,2)," +
			  "solopos text," +
			  "cuentaventa text," +
			  "espt text," +
			  "imagenadicional text," +
			  "tienectaventa text," +
			  "tipoprofal text," +
			  "pordessugerido real," +
			  "nombre_categoria text," +
			  "nombre_familia text," +
			  "nombre_linea text," +
			  "nombre_marca text," +
			  "nombre_presenta text," +
			  "nombre_proveedor text)";
	
	String tablaCabPedido = "create table CABECERAPEDIDOS (" +
			  "secauto integer primary key autoincrement," +
			  "tipo text," +
			  "bodega integer," +
			  "numero integer," +
			  "secuencial integer," +
			  "cliente text," +
			  "usuario text," +
			  "subtotal numeric(10,2)," +
			  "descuento numeric(10,2)," +
			  "iva numeric(10,2)," +
			  "neto numeric(10,2)," +
			  "operador text," +
			  "observacion text," +
			  "fecha_ingreso text," +
			  "hora_ingreso integer," +
			  "estado text," +
			  "docfac text," +
			  "ltd text," +
			  "lng text,"+
	          "estadob text)";
	
	String tablaDetPedido = "create table DETALLESPEDIDOS (" +
			  "linea integer," +
			  "secuencial integer," +
			  "item text," +
			  "nombre_item text," +
			  "cajas integer," +
			  "unidades integer," +
			  "total_unidades integer," +
			  "precio numeric(10,2)," +
			  "subtotal numeric(10,2)," +
			  "pordes numeric(10,2)," +
			  "descuento numeric(10,2)," +
			  "iva numeric(10,2)," +
			  "neto numeric(10,2)," +
			  "tipo_item text," +
			  "forma_venta text," +
			  "estado text," +
			  "fecha_ingreso text," +
			  "hora_ingreso integer," +
			  "usuario text," +
			  "escambio text,"+
	          "estadob text)";
	
	String tablaCondComer = "create table CONDICIONESCOMERCIALES (" +
			  "secuencial integer," +
			  "nombre text," +
			  "fecdesde text," +
			  "fechasta text," +
			  "fechacre text," +
			  "tipo integer," +
			  "aplicacion integer," +
			  "alcance integer," +
			  "cantdesde real," +
			  "canthasta real," +
			  "valordesde numeric(10,2)," +
			  "valorhasta numeric(10,2)," +
			  "modo integer," +
			  "pordes numeric(10,2)," +
			  "item_reg text," +
			  "can_reg integer," +
			  "cliente text," +
			  "item text," +
			  "grupo_cli text," +
			  "grupo_pro text," +
			  "tipoapli text," +
			  "todos text," +
			  "cuenta text," +
			  "cuenta0 text," +
			  "ptoventa text)";
	
	String tablaGrupoCli = "create table GRUPOCLI (" +
			  "codigo text," +
			  "nombre text," +
			  "cliente text," +
			  "ruta text," +
			  "clase text," +
			  "vendedor text," +
			  "tipo text," +
			  "estado text)";

	String tablaGrupoPro = "create table GRUPOPRO (" +
			"codigo text," +
			"nombre text," +
			"item text," +
			"categoria text," +
			"familia text," +
			"linea text," +
			"marca text," +
			"estado text)";

	String tablaParametroBodega = "create table PARAMETROBODEGA (" +
			  "bodega text," +
			  "nopedido integer)";
	
	String tablaParametros = "create table PARAMETROS (" +
			  "NOMBRE_CIA TEXT," +
			  "IVA INTEGER," +
			  "BODFAC INTEGER," +
			  "CODIGO_CLIENTE TEXT," +
			  "CAMPRECIO TEXT," +
			  "NUM_CLIENTE INTEGER, " +
			  "NUM_LINEAS_PED INTEGER, " +
			  "NUM_LINEAS_PAG INTEGER, " +
			  "IP_WEBSERVICE TEXT," +
			  "PAGINAWEB TEXT," +
			  "IMPRESORA TEXT," +
			  "EPRECIO TEXT," +
			  "ESNOTAVENTA TEXT," +
			  "IP_WEBSERVICE_BIROBID TEXT," +
			  "IP_API TEXT,"+
	          "RUC TEXT,"+
	          "SUCURSAL TEXT,"+
	 		  "CARTERA TEXT)";

	String tablaDeudas = "create table DEUDA (" +
					     "SECUENCIAL INTEGER," +
					     "BODEGA TEXT," +
					     "CLIENTE TEXT," +
					     "TIPO TEXT," +
					     "NUMERO INTEGER," +
					     "SERIE TEXT," +
					     "SECINV TEXT," +
					     "IVA NUMERIC(10,4)," +
					     "MONTO NUMERIC(10,4)," +
					     "CREDITO NUMERIC(10,4)," +
					     "SALDO NUMERIC(10,4)," +
					     "FECHAEMI TEXT," +
					     "FECHAVEN TEXT," +
					     "VENDEDOR TEXT," +
					     "OBSERVACION TEXT)";
	
	String tablaCliPrecio = "create table CLIPRECIO (" +
		     "PRECIO INTEGER," +
		     "TIPO TEXT," +
		     "FECHA TEXT," +
		     "OPERADOR TEXT)";
	
	String tablaItemXCliente = "create table if not exists ITEMXCLIENTE (" +
		     "CLIENTE TEXT," +
		     "ITEM TEXT," +
		     "PRECIO NUMERIC(10,4)," +
		     "FECHADESDE TEXT," +
		     "FECHAHASTA TEXT)";
	
	String tablaDiasCredito ="create table if not exists DIASCREDITO (" +
		     "CODIGO TEXT," +
		     "NOMBRE TEXT," +
		     "DIA TEXT)";
	String tablaItemTop="create table if not exists ITEMTOP(" +
			"VENDEDOR TEXT," +
		     "ITEM TEXT," +
		     "OPERADOR TEXT," +
		     "FECHAREG TEXT)";

	String tablaDetPagos ="create table if not exists DETPAGOS (" +
			"SECUENCIAL INTEGER," +
			"TIPO TEXT," +
			"NUMERO INTEGER," +
			"TIPOPAG TEXT," +
			"MONTO NUMERIC(10,6)," +
			"BANCO TEXT," +
			"CUENTA TEXT," +
			"NUMCHQ TEXT," +
			"FECHAVEN TEXT," +
			"INDICE NUMERIC(18,0))"
			;
	String tablaPagos = "create table if not exists PAGOS (" +
			"SECUENCIAL INTEGER," +
			"CLIENTE TEXT,"+
			"TIPO TEXT," +
			"NUMERO INTEGER," +
			"MONTO NUMERIC(10,6)," +
			"OPERADOR TEXT,"+
			"OBSERVACION TEXT,"+
			"FECHA TEXT,"+
			"VENDEDOR TEXT,"+
			"NOGUIA TEXT,"+
			"HORA TEXT,"+
			"NUMFACT INTEGER,"+
			"ESTADO TEXT)"
			;

	String tablaBanco = "create table if not exists BANCO (" +
			"CODIGO TEXT," +
			"NOMBRE TEXT,"+
			"ESTADO TEXT)"
			;

	String tablaMedioPago = "create table if not exists MEDIOPAGO (" +
			"CODIGO TEXT," +
			"NOMBRE TEXT,"+
			"ESTADO TEXT)"
			;


	String tablaSecuencialPago = "create table if not exists SECUENCIALPAGO (" +
			"SECUENCIAL INTEGER," +
			"NUMERO integer,"+
			"ESTADO TEXT)"
			;

	String tablaZona = "create table if not exists ZONA (" +
			"CODIGO TEXT," +
			"NOMBRE TEXT,"+
			"ESTADO TEXT)"
			;
	String tablaBancoCia = "create table if not exists BANCOCIA (" +
			"CODIGO TEXT," +
			"NOMBRE TEXT," +
			"CUENTA TEXT," +
			"ESTADO TEXT)"
			;
	String tablaParroquia = "create table if not exists PARROQUIA (" +
			"CODIGO TEXT," +
			"NOMBRE TEXT," +
			"CANTON TEXT," +
			"PROVINCIA TEXT," +
			"ESTADO TEXT)"
			;


	public BDTablas(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(tablaUsuario);
		db.execSQL(tablaUsuarioSecuenciales);
		db.execSQL(tablaCliente);
		db.execSQL(tablaUsuarioCliente);
		db.execSQL(tablaTipoUsuario);
		db.execSQL(tablaTipoNegocio);
		db.execSQL(tablaTipoCliente);
		db.execSQL(tablaFrecuenciaVisita);
		db.execSQL(tablaRuta);
		db.execSQL(tablaItem);
		db.execSQL(tablaCabPedido);
		db.execSQL(tablaDetPedido);
		db.execSQL(tablaCondComer);
		db.execSQL(tablaGrupoCli);
		db.execSQL(tablaGrupoPro);
		db.execSQL(tablaParametroBodega);
		db.execSQL(tablaParametros);
		db.execSQL(tablaDetPagos);
		db.execSQL(tablaPagos);
		db.execSQL(tablaBanco);
		db.execSQL(tablaMedioPago);
		db.execSQL(tablaSecuencialPago);
		db.execSQL(tablaDeudas);
		db.execSQL(tablaCliPrecio);
		db.execSQL(tablaItemXCliente);
		db.execSQL(tablaDiasCredito);
		db.execSQL(tablaItemTop);
		db.execSQL(tablaZona);
		db.execSQL(tablaParroquia);
		db.execSQL(tablaBancoCia);

		this.insertarDatosPrimeraVez(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(tablaItemXCliente);
		db.execSQL(tablaParroquia);
		//db.execSQL(tablaDetPagos);
		db.execSQL(tablaPagos);
		db.execSQL(tablaMedioPago);
		db.execSQL(tablaBanco);
		db.execSQL(tablaSecuencialPago);
		db.execSQL(tablaBancoCia);
		//db.execSQL(tablaParametros);



		//SQLiteDatabase db = this.getWritableDatabase();

		Cursor c = db.rawQuery("PRAGMA table_info(USUARIO)", null);
		int n = c.getColumnIndexOrThrow("name");
		int aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("edita_precio"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table USUARIO add edita_precio text not null default 'N'");
			}
		}
		
		
		c = db.rawQuery("PRAGMA table_info(USUARIO)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("es_autoventa"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table USUARIO add es_autoventa text not null default 'N'");
			}
		}


		c = db.rawQuery("PRAGMA table_info(CLIENTE)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("referencia"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table CLIENTE add referencia text not null default 'N'");
			}
		}

		c = db.rawQuery("PRAGMA table_info(PAGOS)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("NUMFACT"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table PAGOS add NUMFACT integer not null default 0");
			}
		}

		c = db.rawQuery("PRAGMA table_info(PAGOS)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("ESTADO"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table PAGOS add ESTADO text not null default 'A'");
			}
		}



		c = db.rawQuery("PRAGMA table_info(CLIENTE)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("zona"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table CLIENTE add zona text not null default 'N'");
			}
		}


		c = db.rawQuery("PRAGMA table_info(USUARIO)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("bodega"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table USUARIO add bodega integer not null default '0'");
			}
		}
		
		c = db.rawQuery("PRAGMA table_info(detallespedidos)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("escambio"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table detallespedidos add escambio text not null default 'N'");
			}
		}
		
		c = db.rawQuery("PRAGMA table_info(cliente)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("clavefe"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table cliente add clavefe text not null default ''");
			}
		}
		
		c = db.rawQuery("PRAGMA table_info(PARAMETROS)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("PAGINAWEB"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table PARAMETROS add PAGINAWEB text not null default ''");
			}
		}

		c = db.rawQuery("PRAGMA table_info(PARAMETROS)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("IMPRESORA"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table PARAMETROS add IMPRESORA text not null default 'BlueTooth Printer'");
			}
		}
		
		
		c = db.rawQuery("PRAGMA table_info(PARAMETROS)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("EPRECIO"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table PARAMETROS add EPRECIO text not null default 'N'");

			}
		}

		c = db.rawQuery("PRAGMA table_info(PARAMETROS)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("CARTERA"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table PARAMETROS add CARTERA text not null default 'N'");

			}
		}


		c = db.rawQuery("PRAGMA table_info(PARAMETROS)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("IP_WEBSERVICE_BIROBID"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table PARAMETROS add IP_WEBSERVICE_BIROBID text not null default 'N'");

			}
		}

		c = db.rawQuery("PRAGMA table_info(PARAMETROS)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("PAGINAWEB"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table PARAMETROS add PAGINAWEB text not null default ''");
			}
		}

		c = db.rawQuery("PRAGMA table_info(PARAMETROS)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("NUM_LINEAS_PAG"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table PARAMETROS add NUM_LINEAS_PAG text not null default 'N'");

			}
		}

		c = db.rawQuery("PRAGMA table_info(cabecerapedidos)", null);
		n = c.getColumnIndexOrThrow("name");
		aux=0;
		if(c.moveToFirst())
		{
			do{
				if(c.getString(n).equals("ltd"))
				{
					aux=1;
				}
			}while(c.moveToNext());
			if(aux==0)
			{
				db.execSQL("alter table cabecerapedidos add ltd text");
				db.execSQL("alter table cabecerapedidos add lng text");
			}
		}

		/*if (newVersion > oldVersion) {
			db.execSQL("alter table PAGOS add NUMFACT integer not null default 0");
			db.execSQL("alter table PAGOS add ESTADO text not null default 'A'");

		}*/

	}



	public void insertarDatosPrimeraVez(SQLiteDatabase db)
	{
		String datosBase = "";
		datosBase = "INSERT INTO USUARIO(tipo,codigo,nombre,clave,crea_cliente,coloca_descuento,estado,edita_precio,es_autoventa,ventop) VALUES('ADM','ADMIN','Administrador','admin','S','S','A','N','N','N')";
		db.execSQL(datosBase);

		datosBase = "INSERT INTO USUARIO(tipo,codigo,nombre,clave,crea_cliente,coloca_descuento,estado,edita_precio,es_autoventa,ventop) VALUES('VEN','VEN099','Ventas de oficina','ven099','S','S','A','N','N','N')";
		db.execSQL(datosBase);
		
		datosBase = "INSERT INTO TIPO_USUARIO(codigo,nombre) VALUES('ADMIN','ADMINISTRADOR')";
		db.execSQL(datosBase);
		
		datosBase = "INSERT INTO TIPO_USUARIO(codigo,nombre) VALUES('VEN099','VENDEDOR')";
		db.execSQL(datosBase);
		
		datosBase = "insert into USUARIO_SECUENCIALES(codigo,secuencial) values('VEN099','0')";
		db.execSQL(datosBase);
		
		datosBase = "INSERT INTO PARAMETROBODEGA(bodega,nopedido) VALUES('0','0')";
		db.execSQL(datosBase);

		//datosBase = "insert into parametros(NOMBRE_CIA,IVA,BODFAC,CODIGO_CLIENTE,NUM_CLIENTE,NUM_LINEAS_PED,PAGINAWEB,IMPRESORA,CARTERA) values('Birobid','12','10','CLIMOV','0','100','','BlueTooth Printer')";
		datosBase="insert into parametros(NOMBRE_CIA,IVA,BODFAC,CODIGO_CLIENTE,NUM_CLIENTE,NUM_LINEAS_PED,PAGINAWEB,IMPRESORA,EPRECIO,ESNOTAVENTA,IP_API,IP_WEBSERVICE_BIROBID,CARTERA)" +
				" values('Birobid','12','10','CLIMOV','0','100','','BlueTooth Printer','N','N','181.198.213.18:8089','181.198.213.18:2020','N')";
		db.execSQL(datosBase);
		
		datosBase = "insert into CLIENTE(codigo,nombre,negocio,representa,tipo_identificacion,numero_identificacion,direccion,telefono,"+
					"email,tipo_cliente,provincia,canton,parroquia,sector,ruta,cupo,grupo_descuento,orden,frec_visita,credito,dia,"+
					"fecha_desde,fecha_ingreso,td_credito,dias_credito,usuario,forma_pago,iva,con_final,clase,observacion,tipo_negocio,estado,cltenuevo,descargado) "+
					"values('CLIPRUEBA','JOHN QUISPILLO','NEGOCIO DE PRUEBA','','C','0930760384','CDLA. LOS ALAMOS','0456787856',"+
					"'','','','','','','','','','1','','','1','17/08/2016','17/08/2016','0','0','VEN099','01','S','S','','','','A','S','N')";
		db.execSQL(datosBase);

		datosBase = "insert into CLIENTE(codigo,nombre,negocio,representa,tipo_identificacion,numero_identificacion,direccion,telefono,"+
				"email,tipo_cliente,provincia,canton,parroquia,sector,ruta,cupo,grupo_descuento,orden,frec_visita,credito,dia,"+
				"fecha_desde,fecha_ingreso,td_credito,dias_credito,usuario,forma_pago,iva,con_final,clase,observacion,tipo_negocio,estado,cltenuevo,descargado) "+
				"values('CLIPRUEBA1','JOSE ANTONIO PUCHAY','NEGOCIO DE PRUEBA','PRUEBA','F','9999999999999','PRUEBA','0456787856',"+
				"'','','','','','','','','','1','','','1','17/08/2016','17/08/2016','0','0','VEN099','01','S','S','','','','A','S','N')";
		db.execSQL(datosBase);
		
		datosBase = "insert into CLIENTE(codigo,nombre,negocio,representa,tipo_identificacion,numero_identificacion,direccion,telefono,"+
				"email,tipo_cliente,provincia,canton,parroquia,sector,ruta,cupo,grupo_descuento,orden,frec_visita,credito,dia,"+
				"fecha_desde,fecha_ingreso,td_credito,dias_credito,usuario,forma_pago,iva,con_final,clase,observacion,tipo_negocio,estado,cltenuevo,descargado) "+
				"values('CLIPRUEBA2','MARIA CRISTINA REYES','NEGOCIO DE PRUEBA','PRUEBA','F','9999999999999','PRUEBA','0456787856',"+
				"'','','','','','','','','','1','','','1','17/08/2016','17/08/2016','0','0','VEN099','01','S','S','','','','A','S','N')";
		db.execSQL(datosBase);
		
		datosBase = "insert into CLIENTE(codigo,nombre,negocio,representa,tipo_identificacion,numero_identificacion,direccion,telefono,"+
				"email,tipo_cliente,provincia,canton,parroquia,sector,ruta,cupo,grupo_descuento,orden,frec_visita,credito,dia,"+
				"fecha_desde,fecha_ingreso,td_credito,dias_credito,usuario,forma_pago,iva,con_final,clase,observacion,tipo_negocio,estado,cltenuevo,descargado) "+
				"values('CLIPRUEBA3','ALBERTO MACHADO','NEGOCIO DE PRUEBA','PRUEBA','F','9999999999999','PRUEBA','0456787856',"+
				"'','','','','','','','','','1','','','1','17/08/2016','17/08/2016','0','0','VEN099','01','S','S','','','','A','S','N')";
		db.execSQL(datosBase);
		
		
		datosBase = "insert into CLIENTE(codigo,nombre,negocio,representa,tipo_identificacion,numero_identificacion,direccion,telefono,"+
				"email,tipo_cliente,provincia,canton,parroquia,sector,ruta,cupo,grupo_descuento,orden,frec_visita,credito,dia,"+
				"fecha_desde,fecha_ingreso,td_credito,dias_credito,usuario,forma_pago,iva,con_final,clase,observacion,tipo_negocio,estado,cltenuevo,descargado) "+
				"values('CLIPRUEBA4','MANUEL RENDON','NEGOCIO DE PRUEBA','PRUEBA','F','9999999999999','PRUEBA','0456787856',"+
				"'','','','','','','','','','1','','','1','17/08/2016','17/08/2016','0','0','VEN099','01','S','S','','','','A','S','N')";
		db.execSQL(datosBase);
		
		datosBase = "insert into CLIENTE(codigo,nombre,negocio,representa,tipo_identificacion,numero_identificacion,direccion,telefono,"+
				"email,tipo_cliente,provincia,canton,parroquia,sector,ruta,cupo,grupo_descuento,orden,frec_visita,credito,dia,"+
				"fecha_desde,fecha_ingreso,td_credito,dias_credito,usuario,forma_pago,iva,con_final,clase,observacion,tipo_negocio,estado,cltenuevo,descargado) "+
				"values('CLIPRUEBA5','NARCISA ACOSTA','NEGOCIO DE PRUEBA','PRUEBA','F','9999999999999','PRUEBA','0456787856',"+
				"'','','','','','','','','','1','','','1','17/08/2016','17/08/2016','0','0','VEN099','01','S','S','','','','A','S','N')";
		db.execSQL(datosBase);
		
		datosBase = "insert into CLIENTE(codigo,nombre,negocio,representa,tipo_identificacion,numero_identificacion,direccion,telefono,"+
				"email,tipo_cliente,provincia,canton,parroquia,sector,ruta,cupo,grupo_descuento,orden,frec_visita,credito,dia,"+
				"fecha_desde,fecha_ingreso,td_credito,dias_credito,usuario,forma_pago,iva,con_final,clase,observacion,tipo_negocio,estado,cltenuevo,descargado) "+
				"values('CLIPRUEBA6','JESSICA DELGADO','NEGOCIO DE PRUEBA','PRUEBA','F','9999999999999','PRUEBA','0456787856',"+
				"'','','','','','','','','','1','','','1','17/08/2016','17/08/2016','0','0','VEN099','01','S','S','','','','A','S','N')";
		db.execSQL(datosBase);

		datosBase = "insert into MEDIOPAGO(codigo,nombre,estado) "+
				"values('EFE','EFECTIVO','A')";
		db.execSQL(datosBase);

		datosBase = "insert into MEDIOPAGO(codigo,nombre,estado) "+
				"values('CHQ','CHEQUES','A')";
		db.execSQL(datosBase);

		datosBase = "insert into MEDIOPAGO(codigo,nombre,estado) "+
				"values('DEB','DEBITO BANCO','A')";
		db.execSQL(datosBase);

		datosBase = "insert into MEDIOPAGO(codigo,nombre,estado) "+
				"values('DEP','DEPOSITO','A')";
		db.execSQL(datosBase);

		datosBase = "insert into MEDIOPAGO(codigo,nombre,estado) "+
				"values('TCR','TARJETA DE CRÉDITO','A')";
		db.execSQL(datosBase);

		datosBase = "insert into MEDIOPAGO(codigo,nombre,estado) "+
				"values('TRA','TRASFERENCIA','A')";
		db.execSQL(datosBase);

		datosBase = "insert into SECUENCIALPAGO(secuencial,numero,estado) "+
				"values('0','0','A')";
		db.execSQL(datosBase);


		datosBase = "insert into DEUDA(secuencial,bodega,cliente,tipo,numero,serie,secinv,iva,monto,credito,saldo," +
				" fechaemi,fechaven,vendedor,observacion) "+
				"values('001','1','CLIPRUEBA','EFE','1','1','0','1','100','0','50','16/02/2018','16/02/2018','VEN099','ASDFDFHH')";
		db.execSQL(datosBase);
		
		datosBase = "insert into ITEM(item,nombre,nombrecorto,categoria,estado,dispoven,iva,bien,"+
				"factor,stock,precio1,grupo) "+
				"values('1606','COCOA UNIVER TIRA 40X12X15G','COCOA UNIVER TIRA','','A','S','S','B',"+
				"'1','100','1.136','')";
		db.execSQL(datosBase);
		
		datosBase = "insert into ITEM(item,nombre,nombrecorto,categoria,estado,dispoven,iva,bien,"+
				"factor,stock,precio1,grupo) "+
				"values('00817','SPEED COOLNIGHT MIN ROL48X30M','SPEED COOLNIGHT MIN','','A','S','S','B',"+
				"'1','100','0.904','')";
		db.execSQL(datosBase);
		
		datosBase = "insert into ITEM(item,nombre,nombrecorto,categoria,estado,dispoven,iva,bien,"+
				"factor,stock,precio1,grupo) "+
				"values('1050','CHOCO HUEVITOS 48FD','CHOCO HUEVITOS 48FD','','A','S','N','B',"+
				"'1','100','1.957','')";
		db.execSQL(datosBase);
		
		datosBase = "insert into ITEM(item,nombre,nombrecorto,categoria,estado,dispoven,iva,bien,"+
				"factor,stock,precio1,grupo) "+
				"values('00098','COLG TRIPLE ACCION','COLG TRIPLE ACCION','','A','S','S','B',"+
				"'1','100','1.167','')";
		db.execSQL(datosBase);
		
		datosBase = "insert into ITEM(item,nombre,nombrecorto,categoria,estado,dispoven,iva,bien,"+
				"factor,stock,precio1,grupo) "+
				"values('1221','ALKA-SELTZER','ALKA-SELTZER','','A','S','S','B',"+
				"'1','100','2.084','')";
		db.execSQL(datosBase);
		
		datosBase = "insert into ITEM(item,nombre,nombrecorto,categoria,estado,dispoven,iva,bien,"+
				"factor,stock,precio1,grupo) "+
				"values('1213','MENTOL CHINO','MENTOL CHINO','','A','S','N','B',"+
				"'1','100','6.474','')";
		db.execSQL(datosBase);

		datosBase = "insert into ITEM(item,nombre,nombrecorto,categoria,estado,dispoven,iva,bien,"+
				"factor,stock,precio1,grupo) "+
				"values('21409','ENCEN MINI','ENCEN MINI','','A','S','S','B',"+
				"'1','100','10.262','')";
		db.execSQL(datosBase);
		
		datosBase = "insert into ITEM(item,nombre,nombrecorto,categoria,estado,dispoven,iva,bien,"+
				"factor,stock,precio1,grupo) "+
				"values('01299','COLG TRIPLE ACCION 144U','COLG TRIPLE ACCION 144U','','A','S','S','B',"+
				"'1','100','0.654','')";
		db.execSQL(datosBase);
		
		datosBase = "insert into ITEM(item,nombre,nombrecorto,categoria,estado,dispoven,iva,bien,"+
				"factor,stock,precio1,grupo) "+
				"values('003841','LADY FLORAL','LADY FLORAL','','A','S','N','B',"+
				"'1','100','0.903','')";
		db.execSQL(datosBase);
		
		datosBase = "insert into ITEM(item,nombre,nombrecorto,categoria,estado,dispoven,iva,bien,"+
				"factor,stock,precio1,grupo) "+
				"values('11044','BOLIG FINO NEGRO','BOLIG FINO NEGRO','','A','S','S','B',"+
				"'1','100','6.471','')";
		db.execSQL(datosBase);
		
		datosBase = "insert into ITEM(item,nombre,nombrecorto,categoria,estado,dispoven,iva,bien,"+
				"factor,stock,precio1,grupo) "+
				"values('1375','PAPEL ALUM RPTO','PAPEL ALUM RPTO','','A','S','S','B',"+
				"'1','100','0.731','')";
		db.execSQL(datosBase);
		
		datosBase = "insert into ITEM(item,nombre,nombrecorto,categoria,estado,dispoven,iva,bien,"+
				"factor,stock,precio1,grupo) "+
				"values('11007','SUAV. FRUTOS ROJOS','SUAV. FRUTOS ROJOS','','A','S','N','B',"+
				"'1','100','0.9','')";
		db.execSQL(datosBase);
		
		datosBase ="insert into DIASCREDITO(codigo,nombre,dia) values('99','CONTADO','0')";
		db.execSQL(datosBase);

		datosBase = "insert into CLIENTE(codigo,nombre,negocio,representa,tipo_identificacion,numero_identificacion,direccion,telefono,"+
				"email,tipo_cliente,provincia,canton,parroquia,sector,ruta,cupo,grupo_descuento,orden,frec_visita,credito,dia,"+
				"fecha_desde,fecha_ingreso,td_credito,dias_credito,usuario,forma_pago,iva,con_final,clase,observacion,tipo_negocio,estado,cltenuevo,descargado) "+
				"values('CLIPRUEBA7','JOSELINE CASTILLO','NEGOCIO DE PRUEBA','PRUEBA','C','0930760384','PRUEBA','0456787857',"+
				"'','','','','','','','','','1','','','1','06/02/2018','06/02/2018','0','0','VEN099','01','S','S','','','','A','S','N')";
		db.execSQL(datosBase);

		datosBase = "insert into PAGOS(secuencial,cliente,tipo,numero,monto,operador,observacion,fecha,"+
				"vendedor,noguia,hora,numfact,estado) "+
				"values('4166','C000119','PAG','1203','779.28','ADM','FACT. 15 Y 16','16/02/2018','VEN099','0','19:10:05','','')";
		db.execSQL(datosBase);

		datosBase = "insert into DETPAGOS(secuencial,tipo,numero,tipopag,monto,banco,cuenta,numchq,"+
				"fechaven,indice) "+
				"values('4166','PAG','1203','CHQ','389.64','PIC','3512378404','1529','16/02/2018','30773')";
		db.execSQL(datosBase);

		datosBase = "insert into BANCO(codigo,nombre,estado) "+
				"values('AMA','AMAZONAS','A')";
		db.execSQL(datosBase);

		datosBase = "insert into BANCOCIA(codigo,nombre,cuenta,estado) "+
				"values('PIC','PICHINCHA','2100001675','A')";
		db.execSQL(datosBase);

		datosBase = "insert into ZONA(codigo,nombre,estado) "+
				"values('NOR','NORTE','A')";
		db.execSQL(datosBase);

		datosBase = "insert into PARROQUIA(codigo,canton,provincia,nombre,estado) "+
				"values('P0003','C0002','P0001','TARQUI-GUAYAQUIL-GUAYAS','A')";
		db.execSQL(datosBase);
















	}
}