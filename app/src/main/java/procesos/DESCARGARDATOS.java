package procesos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import constantes.Constantes;
import dialogos.Dialogos;
import misclases.CabeceraPedidos;
import misclases.CabeceraPedidosB;
import misclases.Clientes;
import misclases.DetallesPagos;
import misclases.DetallesPedidos;
import misclases.Empresa;
import misclases.Pagos;

public class DESCARGARDATOS {
	Constantes constante = new Constantes();
	public Context contexto;
	private String scriptSQL;
	private Gson gson;
	Dialogos dialogo = new Dialogos();
	Cursor c;
	String ruc, sucursal;
	int otroparametro;
	
	public DESCARGARDATOS()
	{
	}
	
	public DESCARGARDATOS(Context con)
	{
		this.contexto=con;
	}
	
	public void descargarTodo(SQLiteDatabase db)
	{
		scriptSQL = "SELECT IP_WEBSERVICE FROM PARAMETROS";
		if(db!=null)
		{
			c = db.rawQuery(scriptSQL, null);
			if(c.moveToFirst())
			{
				constante.IPWS = c.getString(0);
				constante.URLWS = "http://"+ constante.IPWS +"/Service1.asmx";
				constante.NAMESPACE = "http://birobid.com/webservice/admmovil";
			}
		}
		
		String formaJsonCabPed,formaJsonDetPed;
		Cursor cur_cab,cur_det;
		
		ArrayList<CabeceraPedidos> alcabped;
	    ArrayList<DetallesPedidos> aldetped;
	    formaJsonDetPed = "";
		try{
			//Obtiene la cabecera
			scriptSQL="SELECT secauto,tipo,bodega,numero,secuencial,cliente,usuario,subtotal,descuento,iva,neto,operador,observacion,fecha_ingreso,hora_ingreso,estado,docfac FROM cabecerapedidos where estado='A'";
			cur_cab = db.rawQuery(scriptSQL, null);
			if(cur_cab.moveToFirst())
			{
				 alcabped = new ArrayList<CabeceraPedidos>();
				do{
					alcabped.add(new CabeceraPedidos(cur_cab.getInt(0),cur_cab.getString(1),cur_cab.getInt(2),cur_cab.getInt(3),
													 cur_cab.getInt(4),cur_cab.getString(5),cur_cab.getString(6),cur_cab.getDouble(7),
													 cur_cab.getDouble(8),cur_cab.getDouble(9),cur_cab.getDouble(10),cur_cab.getString(11),
													 cur_cab.getString(12),cur_cab.getString(13),cur_cab.getString(14),cur_cab.getString(15),cur_cab.getString(16)));
				}while(cur_cab.moveToNext());
				formaJsonCabPed="";
				gson = new Gson();
				formaJsonCabPed = gson.toJson(alcabped);
				
				//Obtiene los detalles
				scriptSQL="SELECT linea,secuencial,item,cajas,unidades,total_unidades,precio,subtotal,pordes,descuento,iva,neto,tipo_item,forma_venta,estado,fecha_ingreso,hora_ingreso,usuario,escambio FROM detallespedidos where estado='A'";
				cur_det = db.rawQuery(scriptSQL,null);
				if(cur_det.moveToFirst())
				{
					aldetped = new ArrayList<DetallesPedidos>();
					do{
						aldetped.add(new DetallesPedidos(cur_det.getInt(0),cur_det.getInt(1),cur_det.getString(2),cur_det.getInt(3),
														 cur_det.getInt(4),cur_det.getInt(5),cur_det.getDouble(6),cur_det.getDouble(7),
														 cur_det.getDouble(8),cur_det.getDouble(9),cur_det.getDouble(10),cur_det.getDouble(11),
														 cur_det.getString(12),cur_det.getString(13),cur_det.getString(14),cur_det.getString(15),
														 cur_det.getString(16),cur_det.getString(17),cur_det.getString(18)));
					}while(cur_det.moveToNext());
					formaJsonDetPed="";
					gson = new Gson();
					formaJsonDetPed = gson.toJson(aldetped);
				}
				otroparametro = 0;
				new llamaWSPedidos(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/setPedidos","setPedidos").execute(formaJsonCabPed,formaJsonDetPed); //si no tiene parametros
				dialogo.miDialogoToastCorto(contexto, "La información se ha descargado correctamente");
				scriptSQL = "update cabecerapedidos set estado = 'P', estadob = 'A' where estado='A'";
				db.execSQL(scriptSQL);
				scriptSQL = "update detallespedidos set estado = 'P', estadob = 'A' where estado='A'";
				db.execSQL(scriptSQL);
			}
			else
			{
				Toast.makeText(contexto, "No hay pedidos para enviar", Toast.LENGTH_SHORT).show();
			}
		}catch(Exception e){
			Toast.makeText(contexto, "Excepción al querer sincronizar los pedidos: "+e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	public void descargarPago(SQLiteDatabase db,String codven)
	{
		scriptSQL = "SELECT IP_WEBSERVICE FROM PARAMETROS";
		if(db!=null)
		{
			c = db.rawQuery(scriptSQL, null);
			if(c.moveToFirst())
			{
				constante.IPWS = c.getString(0);
				constante.URLWS = "http://"+ constante.IPWS +"/Service1.asmx";
				constante.NAMESPACE = "http://birobid.com/webservice/admmovil";
			}
		}

		String formaJsonCabPag,formaJsonDetPag;
		Cursor cur_cab,cur_det;

		ArrayList<Pagos> alcabpag;
		ArrayList<DetallesPagos> aldetpag;
		formaJsonDetPag = "";

		try{
			//Obtiene la cabecera
			scriptSQL="SELECT secuencial,cliente,tipo,numero,monto,operador,observacion,fecha,vendedor,noguia,hora,numfact,estado FROM PAGOS where estado='A'";
			cur_cab = db.rawQuery(scriptSQL, null);
			if(cur_cab.moveToFirst())
			{
				alcabpag = new ArrayList<Pagos>();
				do{
					alcabpag.add(new Pagos(cur_cab.getInt(0),cur_cab.getString(1),cur_cab.getString(2),cur_cab.getInt(3),
							cur_cab.getDouble(4),cur_cab.getString(5),cur_cab.getString(6),cur_cab.getString(7),
							cur_cab.getString(8),cur_cab.getString(9),cur_cab.getString(10),cur_cab.getInt(11),
							cur_cab.getString(12)));
				}while(cur_cab.moveToNext());
				formaJsonCabPag="";
				gson = new Gson();
				formaJsonCabPag = gson.toJson(alcabpag);

				//Obtiene los detalles
				scriptSQL="SELECT detp.secuencial,detp.tipo,detp.numero,detp.tipopag,detp.monto,detp.banco,detp.cuenta,detp.numchq,detp.fechaven,detp.indice FROM DETPAGOS detp inner join pagos cabp where detp.secuencial = cabp.secuencial and  cabp.estado='A'";
				cur_det = db.rawQuery(scriptSQL,null);
				if(cur_det.moveToFirst())
				{
					aldetpag = new ArrayList<DetallesPagos>();
					do{
						aldetpag.add(new DetallesPagos(cur_det.getInt(0),cur_det.getString(1),cur_det.getInt(2),cur_det.getString(3),
								cur_det.getDouble(4),cur_det.getString(5),cur_det.getString(6),cur_det.getString(7),
								cur_det.getString(8),cur_det.getDouble(9),codven));
					}while(cur_det.moveToNext());
					formaJsonDetPag="";
					gson = new Gson();
					formaJsonDetPag = gson.toJson(aldetpag);
				}
				new llamaWSPagos(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/setPagos","setPagos").execute(formaJsonCabPag,formaJsonDetPag); //si no tiene parametros
				dialogo.miDialogoToastCorto(contexto, "La información se ha descargado correctamente");
				scriptSQL = "update pagos set estado = 'P' where estado='A'";
				db.execSQL(scriptSQL);


			}else
			{
				Toast.makeText(contexto, "No hay pagos para enviar", Toast.LENGTH_SHORT).show();
			}
		}catch(Exception e){
			Toast.makeText(contexto, "Excepción al querer sincronizar los pagos: "+e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}


	public void descargarDatosBoribid(SQLiteDatabase db)
	{
		scriptSQL = "SELECT IP_WEBSERVICE_BIROBID, RUC, SUCURSAL FROM PARAMETROS";
		if(db!=null)
		{
			ruc ="";
			sucursal="";
			c = db.rawQuery(scriptSQL, null);
			if(c.moveToFirst())
			{
				constante.IPWSB = c.getString(0);

				ruc = c.getString(1);

				sucursal = c.getString(2);
				constante.URLWS = "http://"+ constante.IPWSB +"/Service1.asmx";
				constante.NAMESPACE = "http://birobid.com/webservice/admmovil";
			}
		}


		dialogo.miDialogoToastCorto(contexto, "descargar datos B "+constante.IPWSB.toString());
		String formaJsonCabPed,formaJsonDetPed;
		Cursor cur_cab,cur_det;

		ArrayList<CabeceraPedidosB> alcabped;
		ArrayList<DetallesPedidos> aldetped;
		ArrayList<Empresa> alemp;

		formaJsonDetPed = "";
		try{
			//Obtiene la cabecera
			scriptSQL="SELECT secauto,tipo,bodega,numero,secuencial,cliente,usuario,subtotal,descuento,iva,neto,operador,observacion,fecha_ingreso,hora_ingreso,estado,docfac FROM cabecerapedidos WHERE estadob = 'A'";
			cur_cab = db.rawQuery(scriptSQL, null);
			if(cur_cab.moveToFirst())
			{
				alcabped = new ArrayList<CabeceraPedidosB>();
				do{
					alcabped.add(new CabeceraPedidosB(cur_cab.getInt(0),cur_cab.getString(1),cur_cab.getInt(2),cur_cab.getInt(3),
							cur_cab.getInt(4),cur_cab.getString(5),cur_cab.getString(6),cur_cab.getDouble(7),
							cur_cab.getDouble(8),cur_cab.getDouble(9),cur_cab.getDouble(10),cur_cab.getString(11),
							cur_cab.getString(12),cur_cab.getString(13),cur_cab.getString(14),cur_cab.getString(15),cur_cab.getString(16)));
				}while(cur_cab.moveToNext());
				formaJsonCabPed="";
				gson = new Gson();
				formaJsonCabPed = gson.toJson(alcabped);

				//Obtiene los detalles
				scriptSQL="SELECT linea,secuencial,item,cajas,unidades,total_unidades,precio,subtotal,pordes,descuento,iva,neto,tipo_item,forma_venta,estado,fecha_ingreso,hora_ingreso,usuario,escambio FROM detallespedidos WHERE estadob = 'A'";
				cur_det = db.rawQuery(scriptSQL,null);
				if(cur_det.moveToFirst())
				{
					aldetped = new ArrayList<DetallesPedidos>();
					do{
						aldetped.add(new DetallesPedidos(cur_det.getInt(0),cur_det.getInt(1),cur_det.getString(2),cur_det.getInt(3),
								cur_det.getInt(4),cur_det.getInt(5),cur_det.getDouble(6),cur_det.getDouble(7),
								cur_det.getDouble(8),cur_det.getDouble(9),cur_det.getDouble(10),cur_det.getDouble(11),
								cur_det.getString(12),cur_det.getString(13),cur_det.getString(14),cur_det.getString(15),
								cur_det.getString(16),cur_det.getString(17),cur_det.getString(18)));
					}while(cur_det.moveToNext());
					formaJsonDetPed="";
					gson = new Gson();
					formaJsonDetPed = gson.toJson(aldetped);
				}
                String jsonsecundario="";
				alemp = new ArrayList<Empresa>();
				alemp.add(new Empresa(ruc,sucursal));
				gson = new Gson();
				jsonsecundario = gson.toJson(alemp);
				otroparametro=1;
				new llamaWSPedidos(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/setPedidos","setPedidos").execute(formaJsonCabPed,formaJsonDetPed,jsonsecundario); //si no tiene parametros
				dialogo.miDialogoToastCorto(contexto, "La información se ha descargado correctamente");
				scriptSQL = "update cabecerapedidos set estadob = 'P' where estadob = 'A'";
				db.execSQL(scriptSQL);
				scriptSQL = "update detallespedidos set estadob = 'P' where estadob = 'A'";
				db.execSQL(scriptSQL);
			}
			else
			{
				Toast.makeText(contexto, "No hay pedidos para enviar", Toast.LENGTH_SHORT).show();
			}
		}catch(Exception e){
			Toast.makeText(contexto, "Excepción al querer sincronizar los pedidos: "+e.getMessage(), Toast.LENGTH_SHORT).show();
 		}
	}
	
	public void descargarClientes(SQLiteDatabase db)
	{
		scriptSQL = "SELECT IP_WEBSERVICE FROM PARAMETROS";
		if(db!=null)
		{
			c = db.rawQuery(scriptSQL, null);
			if(c.moveToFirst())
			{
				constante.IPWS = c.getString(0);
				constante.URLWS = "http://"+ constante.IPWS +"/Service1.asmx";
				constante.NAMESPACE = "http://birobid.com/webservice/admmovil";
			}
		}


		
		String formaJsonClientes;
		Cursor c;
		
		ArrayList<Clientes> alclientes;
	    formaJsonClientes = "";
		try{
			//Obtiene la cabecera
			scriptSQL="SELECT " + 
					  "codigo,nombre as RAZONSOCIAL,negocio,numero_identificacion as RUC,direccion,telefono as TELEFONOS,tipo_cliente as TIPO,'' as CATEGORIA,provincia,canton,parroquia,sector,ruta,cupo,grupo_descuento as GRUPO,orden,frec_visita as CODFRE,'N' as CREDITO, " +
					  "dia,fecha_desde as FECDESDE,estado,td_credito as TDCREDITO,dias_credito as DIASCREDIT,usuario as VENDEDOR,iva, observacion,tipo_negocio as TIPONEGO,'N' as REGISTRADO,'' as FECHATRABAJO,zona,email,cltenuevo,ejex,ejey,codigo as CODIGOSISTEMA,'' as TIPOCTACLIENTE,'' as FAX,forma_pago as FORMAPAG,referencia FROM cliente where cltenuevo='S' and estado='A'";
			c = db.rawQuery(scriptSQL, null);
			if(c.moveToFirst())
			{
				alclientes = new ArrayList<Clientes>();
				do{
					alclientes.add(new Clientes(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),
												c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getString(10),
												c.getString(11),c.getString(12),c.getString(13),c.getString(14),c.getInt(15),
												c.getString(16),c.getString(17),c.getInt(18),c.getString(19),c.getString(20),c.getString(21),
												c.getInt(22),c.getString(23),c.getString(24),c.getString(25),c.getString(26),c.getString(27),
												c.getString(28),c.getString(29),c.getString(30),c.getString(31),c.getString(32),c.getString(33),
												c.getString(34),c.getString(35),c.getString(36),c.getString(37),c.getString(38)));
				}while(c.moveToNext());
				formaJsonClientes="";
				gson = new Gson();
				formaJsonClientes = gson.toJson(alclientes);
				
				new llamaWSInsertaClientes(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/setClientes","setClientes").execute(formaJsonClientes);
				dialogo.miDialogoToastCorto(contexto, "La información se ha descargado correctamente");
				scriptSQL = "update cliente set descargado='S'";
				db.execSQL(scriptSQL);
			}
			else
			{
				Toast.makeText(contexto, "No hay clientes para enviar", Toast.LENGTH_SHORT).show();
			}
		}catch(Exception e){
			Toast.makeText(contexto, "Excepción al querer sincronizar los clientes: "+e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	
	
	
	
	
	private class llamaWSPedidos extends AsyncTask<String,Void,String>{
	    private String NAMESPACE;
	    private String URL;
		private String SOAP_ACTION;
	    private String METHOD;
	    String respuesta = null;
	    
	    public llamaWSPedidos(String namespace,String url,String soap,String metodo)
	    {
	    	this.NAMESPACE=namespace;
	    	this.URL=url;
	    	this.SOAP_ACTION=soap;
	    	this.METHOD=metodo;
	    }
	    
		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			userRequest.addProperty("cabecera", params[0]); //Paso de par�metros al ws
			userRequest.addProperty("detalles", params[1]); //Paso de par�metros al ws
			if(otroparametro ==1)
			{
				userRequest.addProperty("jsonx", params[2]); //Paso de par�metros al ws
			}
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
	        envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

	        try{
	            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	            androidHttpTransport.debug = true;
	            androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
	            //respuesta = envelope.getResponse().toString();
	        }
	        catch (Exception e){
	        	Toast.makeText(contexto, e.getMessage(), Toast.LENGTH_LONG).show();
	            e.printStackTrace();
	        }
	        return respuesta;
		}
		
		protected void onPostExecute(String result)
	    {
	        super.onPostExecute(String.valueOf(result));
	    }
	}

	private class llamaWSPagos extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSPagos(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			userRequest.addProperty("cabecera", params[0]); //Paso de par�metros al ws
			userRequest.addProperty("detalles", params[1]); //Paso de par�metros al ws

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				//respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				Toast.makeText(contexto, e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			super.onPostExecute(String.valueOf(result));
		}
	}
	
	
	private class llamaWSInsertaClientes extends AsyncTask<String,Void,String>{
	    private String NAMESPACE;
	    private String URL;
		private String SOAP_ACTION;
	    private String METHOD;
	    String respuesta = null;
	    
	    public llamaWSInsertaClientes(String namespace,String url,String soap,String metodo)
	    {
	    	this.NAMESPACE=namespace;
	    	this.URL=url;
	    	this.SOAP_ACTION=soap;
	    	this.METHOD=metodo;
	    }
	    
		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			userRequest.addProperty("datos", params[0]); //Paso de par�metros al ws
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
	        envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

	        try{
	            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	            androidHttpTransport.debug = true;
	            androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
	            //respuesta = envelope.getResponse().toString();
	        }
	        catch (Exception e){
	        	Toast.makeText(contexto, e.getMessage(), Toast.LENGTH_LONG).show();
	            e.printStackTrace();
	        }
	        return respuesta;
		}
		
		protected void onPostExecute(String result)
	    {
	        super.onPostExecute(String.valueOf(result));
	    }
	}
	

}
