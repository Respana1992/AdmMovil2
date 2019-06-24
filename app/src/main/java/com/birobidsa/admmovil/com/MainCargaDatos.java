package com.birobidsa.admmovil.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import clasesAdapter.CargaDatosAdapter;
import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.CliPrecio;
import misclases.Clientes;
import misclases.CondiComer;
import misclases.Deuda;
import misclases.DiasCredito;
import misclases.FrecuenciaVisita;
import misclases.GrupoCli;
import misclases.GrupoPro;
import misclases.Item;
import misclases.ItemTop;
import misclases.ItemXCliente;
import misclases.MedioPago;
import misclases.Parametros;
import misclases.Ruta;
import misclases.TipoCliente;
import misclases.TipoNegocio;
import misclases.Usuario;
import misclases.Zona;
import misclases.Banco;
import misclases.BancoCia;
import misclases.Parroquia;
import procesos.CONEXIONIONTERNET;
import procesos.MENSAJEEXCEPCIONES;

public class MainCargaDatos extends Activity {
	Dialogos mensaje = new Dialogos();
	String usuario,nombreUsuario,tipoUsuario,selectSQL,claveUser,existeUsuario,esautoventa;
	String[] titulo,descripcion;
	int[] imagenes;
	int bodegaven,bodfac;
	Constantes constante;
	SQLiteDatabase db;
	MENSAJEEXCEPCIONES mex = new MENSAJEEXCEPCIONES();
	BDTablas dbcon;
	TextView TUsuario;
	Cursor c;
	CargaDatosAdapter adapter;
	AlertDialog.Builder builder;
	AlertDialog alert;
	Clientes cli;
	Item item;
	MedioPago medpag;
	ItemTop itemt;
	CondiComer condicomer;
	Deuda deuda;
	GrupoPro gpro;
	GrupoCli gcli;
	Banco banco;
	BancoCia bancocia;
	Usuario objetoUsuario;
	Parametros parametros;
	TipoNegocio tiponego;
	TipoCliente tipocli;
	FrecuenciaVisita frevis;
	Parroquia parroquia;
	Ruta ruta;
	Zona zona;
	DiasCredito dia;
	CliPrecio clipre;
	ItemXCliente itemxcli;
	Dialogos dialogo = new Dialogos();
	CONEXIONIONTERNET conint;
	@SuppressWarnings("rawtypes")
	ArrayList alTieneInternet = new ArrayList();
	boolean tieneInternet = true;
	String [] soloip = null;
	Button btnAcepta;
	String txtNumTransfer="";
	int numtransfer=0;
	int bodegaAutoVenta=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_carga_datos);

		titulo = new String[]{"Carga de Clientes","Carga de Productos y Stock","Carga Cartera","Carga Total","Carga de Transferencia","Reiniciar Stock Autoventa"};
		descripcion = new String[]{"Carga la información de los clientes, vendedores, grupos y tipos de cliente y las rutas","Carga la información de los productos, de las condiciones comerciales, grupos de productos y el stock actualizado","Carga la cartera o deuda vigente de los clientes","Carga la información completa","Carga de Transferencia","Reiniciar Stock Autoventa"};
		imagenes = new int[]{R.drawable.cargadatos,R.drawable.cargadatos,R.drawable.cargadatos,R.drawable.cargadatos,R.drawable.cargadatos,R.drawable.cargadatos,R.drawable.cargadatos};

		getActionBar().setTitle("Carga de datos");
		conint = new CONEXIONIONTERNET(getApplicationContext());
		constante = new Constantes();
		try{
			dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
			db = dbcon.getWritableDatabase();
		}catch(Exception e){
			dialogo.miDialogoToastLargo(this, "Excepción generada al conectarse a la Base de datos: Clase: MainCargaDatos --> " + e.getMessage());
			return;
		}

		//Recibo los parametros de la actividad MainLogin el usuario y el nombre de quien inicia sesi�n
		usuario = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");

		//Tomo el control TextView donde va el nombre del usuario y le asigno la variable usuario
		TUsuario = (TextView)findViewById(R.id.lblNombreUsuario);
		TUsuario.setText("Bienvenido: " + nombreUsuario + "-" + usuario);

		if(db!=null)
		{
			selectSQL = "SELECT IP_WEBSERVICE FROM PARAMETROS";
			c = db.rawQuery(selectSQL, null);
			if(c.moveToFirst())
			{
				constante.IPWS = c.getString(0);
				constante.URLWS = "http://"+ constante.IPWS +"/Service1.asmx";
				constante.NAMESPACE = "http://birobid.com/webservice/admmovil";
				soloip = c.getString(0).split(":");
			}

			selectSQL = "SELECT clave FROM usuario where codigo='"+ usuario +"'";
			c = db.rawQuery(selectSQL, null);
			if(c.moveToFirst())
				claveUser = c.getString(0);
			else
				claveUser = "";
		}

		//Tomo el control ListView
		final ListView listaMenuCargaDatos = (ListView)findViewById(R.id.lvMenuCargaDatos);
		adapter = new CargaDatosAdapter(this, titulo, descripcion, imagenes);
		listaMenuCargaDatos.setAdapter(adapter);

		listaMenuCargaDatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				switch (i) {
					case 0:
						builder = new Builder(MainCargaDatos.this);
						builder.setMessage("¿Está seguro de querer cargar la información?")
								.setTitle("Carga de Clientes")
								.setPositiveButton("Si", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										tieneInternet = conint.executeCmd("ping -c 1 -w 1 " + soloip[0], false);
										if(tieneInternet) //Si es q si tiene conexion a internet
										{
											new llamaWSValidaUsuario(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getValidaUsuarioExistente","getValidaUsuarioExistente").execute(usuario,claveUser,"CLI");
										}
										else
										{
											dialogo.dialogoAceptar(MainCargaDatos.this, "Por favor proceda primero a revisar la configuración del Web Service con la dirección " + constante.IPWS + ", de persistir el error revise su conexión a INTERNET","Conexión a Internet");
											dialogo.dialogoAceptar(MainCargaDatos.this, "Problemas al conectarse al SERVIDOR CENTRAL. No tiene acceso a internet o la configuración del Web Service se encuentra mal configurado.","Conexión a Internet");
										}
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
									}
								});
						alert = builder.create();
						alert.show();
						break;

					case 1:
						builder = new Builder(MainCargaDatos.this);
						builder.setMessage("¿Está seguro de querer cargar la información?")
								.setTitle("Carga de Productos")
								.setPositiveButton("Si", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										tieneInternet = conint.executeCmd("ping -c 1 -w 1 " + soloip[0], false);
										if(tieneInternet) //Si es q si tiene conexion a internet
										{
											new llamaWSValidaUsuario(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getValidaUsuarioExistente","getValidaUsuarioExistente").execute(usuario,claveUser,"ITEM");
										}
										else
										{
											dialogo.dialogoAceptar(MainCargaDatos.this, "Por favor proceda primero a revisar la configuración del Web Service con la dirección " + constante.IPWS + ", de persistir el error revise su conexión a INTERNET","Conexión a Internet");
											dialogo.dialogoAceptar(MainCargaDatos.this, "Problemas al conectarse al SERVIDOR CENTRAL. No tiene acceso a internet o la configuración del Web Service se encuentra mal configurado.","Conexión a Internet");
										}
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
									}
								});
						alert = builder.create();
						alert.show();
						break;

					case 2:
						builder = new Builder(MainCargaDatos.this);
						builder.setMessage("¿Está seguro de querer cargar la cartera de los clientes?")
								.setTitle("Carga de Cartera")
								.setPositiveButton("Si", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										tieneInternet = conint.executeCmd("ping -c 1 -w 1 " + soloip[0], false);
										if(tieneInternet) //Si es q si tiene conexion a internet
										{
											new llamaWSValidaUsuario(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getValidaUsuarioExistente","getValidaUsuarioExistente").execute(usuario,claveUser,"CAR");
										}
										else
										{
											dialogo.dialogoAceptar(MainCargaDatos.this, "Por favor proceda primero a revisar la configuración del Web Service con la dirección " + constante.IPWS + ", de persistir el error revise su conexión a INTERNET","Conexión a Internet");
											dialogo.dialogoAceptar(MainCargaDatos.this, "Problemas al conectarse al SERVIDOR CENTRAL. No tiene acceso a internet o la configuración del Web Service se encuentra mal configurado.","Conexión a Internet");
										}
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
									}
								});
						alert = builder.create();
						alert.show();
						break;

					case 3:
						builder = new Builder(MainCargaDatos.this);
						builder.setMessage("�Est� seguro de querer cargar la informaci�n?")
								.setTitle("Carga Total")
								.setPositiveButton("Si", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										tieneInternet = conint.executeCmd("ping -c 1 -w 1 " + soloip[0], false);
										if(tieneInternet) //Si es q si tiene conexion a internet
										{
											new llamaWSValidaUsuario(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getValidaUsuarioExistente","getValidaUsuarioExistente").execute(usuario,claveUser,"TOT");
										}
										else
										{
											dialogo.dialogoAceptar(MainCargaDatos.this, "Por favor proceda primero a revisar la configuración del Web Service con la dirección " + constante.IPWS + ", de persistir el error revise su conexión a INTERNET","Conexión a Internet");
											dialogo.dialogoAceptar(MainCargaDatos.this, "Problemas al conectarse al SERVIDOR CENTRAL. No tiene acceso a internet o la configuración del Web Service se encuentra mal configurado.","Conexión a Internet");
										}
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
									}
								});
						alert = builder.create();
						alert.show();
						break;

					case 4:
						selectSQL = "SELECT es_autoventa FROM usuario where codigo='"+ usuario +"'";
						c = db.rawQuery(selectSQL, null);
						if(c.moveToFirst())
							esautoventa = c.getString(0);
						else
							esautoventa = "N";

						if(esautoventa.equals("S"))
						{
							builder = new Builder(MainCargaDatos.this);
							builder.setMessage("¿Está seguro de querer cargar la información?")
									.setTitle("Carga de Stock AutoVenta")
									.setPositiveButton("Si", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											tieneInternet = conint.executeCmd("ping -c 1 -w 1 " + soloip[0], false);
											if(tieneInternet) //Si es q si tiene conexion a internet
											{
												final Dialog dialog1 = new Dialog(MainCargaDatos.this);
												dialog1.setContentView(R.layout.layout_numtransfer);
												dialog1.setTitle("Número de Transferencia");
												btnAcepta = (Button)dialog1.findViewById(R.id.btnAcepta);
												btnAcepta.setOnClickListener(new OnClickListener() {
													public void onClick(View v) {
														EditText edit=(EditText)dialog1.findViewById(R.id.txtNumTransfer);
														txtNumTransfer=edit.getText().toString();
														dialog1.dismiss(); //Para que el dialogo se cierre y no consuma memoria

														selectSQL = "SELECT bodega FROM usuario where codigo='"+ usuario +"'";
														c = db.rawQuery(selectSQL, null);
														if(c.moveToFirst())
														{
															new llamaWSItemAutoVentaXTransferencia(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getItemSoloAutoVenta","getItemSoloAutoVenta").execute(String.valueOf(c.getInt(0)),txtNumTransfer);
														}
													}
												});
												dialog1.show();
											}
											else
											{
												dialogo.dialogoAceptar(MainCargaDatos.this, "Por favor proceda primero a revisar la configuración del Web Service con la dirección " + constante.IPWS + ", de persistir el error revise su conexión a INTERNET","Conexión a Internet");
												dialogo.dialogoAceptar(MainCargaDatos.this, "Problemas al conectarse al SERVIDOR CENTRAL. No tiene acceso a internet o la configuración del Web Service se encuentra mal configurado.","Conexión a Internet");
											}
										}
									})
									.setNegativeButton("No", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
										}
									});
							alert = builder.create();
							alert.show();
						}
						else
						{
							dialogo.dialogoAceptar(MainCargaDatos.this, "Opción habilitada solo para usuarios de autoventa","Mensaje");
						}
						break;

					case 5:
						selectSQL = "SELECT es_autoventa FROM usuario where codigo='"+ usuario +"'";
						c = db.rawQuery(selectSQL, null);
						if(c.moveToFirst())
							esautoventa = c.getString(0);
						else
							esautoventa = "N";

						if(esautoventa.equals("S"))
						{
							builder = new Builder(MainCargaDatos.this);
							builder.setMessage("¿Está seguro de querer cargar la información?")
									.setTitle("Reiniciar Stock AutoVenta")
									.setPositiveButton("Si", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											tieneInternet = conint.executeCmd("ping -c 1 -w 1 " + soloip[0], false);
											if(tieneInternet) //Si es q si tiene conexion a internet
											{
												selectSQL = "SELECT bodega FROM usuario where codigo='"+ usuario +"'";
												c = db.rawQuery(selectSQL, null);
												if(c.moveToFirst())
												{
													new llamaWSItemAutoVenta(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getItemTodosAutoVenta","getItemTodosAutoVenta").execute(String.valueOf(c.getInt(0))); //si no tiene parametros
												}
											}
											else
											{
												dialogo.dialogoAceptar(MainCargaDatos.this, "Por favor proceda primero a revisar la configuración del Web Service con la dirección " + constante.IPWS + ", de persistir el error revise su conexión a INTERNET","Conexión a Internet");
												dialogo.dialogoAceptar(MainCargaDatos.this, "Problemas al conectarse al SERVIDOR CENTRAL. No tiene acceso a internet o la configuración del Web Service se encuentra mal configurado.","Conexión a Internet");
											}
										}
									})
									.setNegativeButton("No", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
										}
									});
							alert = builder.create();
							alert.show();
						}
						else
						{
							dialogo.dialogoAceptar(MainCargaDatos.this, "Opción habilitada solo para usuarios de autoventa","Mensaje");
						}
						break;
					default:
						break;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_carga_datos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//Este m�todo se ejecuta cuando se da clic en el bot�n de ir hacia atr�s
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// Esto es para cuando de clic en el bo�n de atr�s no regrese a la pantalla de login
			finish();
			Intent intent = new Intent(this,MainMenuPrincipal.class);
			intent.putExtra("user", usuario);
			intent.putExtra("nombreUser", nombreUsuario);
			intent.putExtra("tipoUser", tipoUsuario);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}











	/**************************************************************************************/
	/****** Clases para invocar a los web services ******/
	/**************************************************************************************/

	/**************************************************************************************/
	/****** Registra los clientes traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSValidaUsuario extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSValidaUsuario(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			userRequest.addProperty("codigo", params[0]); //Paso de par�metros al ws
			userRequest.addProperty("clave", params[1]); //Paso de par�metros al ws

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				dialogo.miDialogoToastCorto(getApplicationContext(), e.getMessage());
				e.printStackTrace();
			}
			return respuesta+"-"+params[2]; //Envio la respuesta de WS con el ultimo parametro del metodo para saber que es lo que se tiene que cargar en la otra funcion onPostExecute
		}

		protected void onPostExecute(String result)
		{
			String[] cadenas = result.split("-");//parto la cadena

			//Si el codigo del vendedor con su clave existe en el ADM o servidor central,
			//se procede a realizar las respectivas cargas
			if(cadenas[0].equals("S"))
			{
				if(cadenas[1].equals("CLI"))
				{
					new llamaWSParametros(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getParametros","getParametros").execute(); //si no tiene parametros
					new llamaWSCliente(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getClientesTodosXUsuario","getClientesTodosXUsuario").execute(usuario);
					new llamaWSGrupoCli(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getGrupoCli","getGrupoCli").execute(); //si no tiene parametros
					new llamaWSTipoCliente(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getTipoCliente","getTipoCliente").execute(); //si no tiene parametros
					new llamaWSFrecuenciaVisita(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getFrecuenciaVisita","getFrecuenciaVisita").execute(); //si no tiene parametros
					new llamaWSRuta(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getRuta","getRuta").execute(); //si no tiene parametros
					new llamaWSZona(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getZona","getZona").execute(); //si no tiene parametros
					new llamaWSDiasCredito(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getDiasCredito","getDiasCredito").execute(); //si no tiene parametros
					new llamaWSCliPrecio(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getClientePrecio","getClientePrecio").execute(); //si no tiene parametros
					new llamaWSTipoNegocio(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getTipoNegocio","getTipoNegocio").execute(); //si no tiene parametros
					new llamaWSItemXCliente(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getItemXCliente","getItemXCliente").execute(); //si no tiene parametros
					new llamaWSMedioPago(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getMedioPago","getMedioPago").execute(); //si no tiene parametros
					new llamaWSBanco(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getBancos","getBancos").execute(); //si no tiene parametros
					new llamaWSParroquia(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getParroquias","getParroquias").execute(); //si no tiene parametros
					new llamaWSBancoCia(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getBancoCia","getBancoCia").execute(); //si no tiene parametros
                }
				else if(cadenas[1].equals("ITEM"))
				{
					new llamaWSParametros(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getParametros","getParametros").execute(); //si no tiene parametros
					new llamaWSUsuario(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getVendedores","getVendedores").execute(usuario);
					new llamaWSCondiComer(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getCondicionesComerciales","getCondicionesComerciales").execute(); //si no tiene parametros
					new llamaWSGrupoPro(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getGrupoPro","getGrupoPro").execute(); //si no tiene parametros
					new llamaWSItemTop(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getItemTop","getItemTop").execute(); //si no tiene parametros
					new llamaWSItemXCliente(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getItemXCliente","getItemXCliente").execute(); //si no tiene parametros

					selectSQL = "delete from ITEM where categoria = ''";
					db.execSQL(selectSQL);
				}
				else if(cadenas[1].equals("CAR"))
				{
					new llamaWSDeuda(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getDeudaConSaldo","getDeudaConSaldo").execute(usuario);
				}
				else if(cadenas[1].equals("TOT"))
				{
					new llamaWSParametros(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getParametros","getParametros").execute(); //si no tiene parametros
					new llamaWSUsuario(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getVendedores","getVendedores").execute(usuario); //si no tiene parametros
					new llamaWSCliente(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getClientesTodosXUsuario","getClientesTodosXUsuario").execute(usuario);
					new llamaWSCondiComer(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getCondicionesComerciales","getCondicionesComerciales").execute(); //si no tiene parametros
					new llamaWSGrupoPro(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getGrupoPro","getGrupoPro").execute(); //si no tiene parametros
					new llamaWSGrupoCli(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getGrupoCli","getGrupoCli").execute(); //si no tiene parametros
					new llamaWSTipoNegocio(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getTipoNegocio","getTipoNegocio").execute(); //si no tiene parametros
					new llamaWSTipoCliente(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getTipoCliente","getTipoCliente").execute(); //si no tiene parametros
					new llamaWSFrecuenciaVisita(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getFrecuenciaVisita","getFrecuenciaVisita").execute(); //si no tiene parametros
					new llamaWSRuta(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getRuta","getRuta").execute(); //si no tiene parametros
					new llamaWSDiasCredito(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getDiasCredito","getDiasCredito").execute(); //si no tiene parametros
					new llamaWSDeuda(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getDeudaConSaldo","getDeudaConSaldo").execute(usuario);
					new llamaWSCliPrecio(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getClientePrecio","getClientePrecio").execute(); //si no tiene parametros
					new llamaWSItemTop(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getItemTop","getItemTop").execute(); //si no tiene parametros
					new llamaWSItemXCliente(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getItemXCliente","getItemXCliente").execute(); //si no tiene parametros
					new llamaWSZona(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getZona","getZona").execute(); //si no tiene parametros
					new llamaWSMedioPago(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getMedioPago","getMedioPago").execute(); //si no tiene parametros
					new llamaWSBanco(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getBancos","getBancos").execute(); //si no tiene parametros
					new llamaWSParroquia(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getParroquias","getParroquias").execute(); //si no tiene parametros
					new llamaWSBancoCia(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getBancoCia","getBancoCia").execute(); //si no tiene parametros
					selectSQL = "delete from ITEM where categoria = ''";
					db.execSQL(selectSQL);
				}
			}
			else
			{
				dialogo.dialogoAceptar(MainCargaDatos.this, "IMPOSIBLE realizar la carga de datos. El usuario configurado en parámetros no existe en el Servidor Central", "Carga de datos - "+ constante.tituloVentana);
			}
		}
	}


	private class llamaWSItemAutoVentaXTransferencia extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSItemAutoVentaXTransferencia(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			//userRequest.addProperty("bodega", String.valueOf(bodegaAutoVenta)); //Paso de par�metros al ws
			userRequest.addProperty("bodega", params[0]); //Paso de par�metros al ws
			userRequest.addProperty("transferencia", params[1]); //Paso de par�metros al ws

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				dialogo.miDialogoToastCorto(getApplicationContext(), e.getMessage());
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);
				if(jsonArray.length()>0)
				{
					for(int i=0;i<jsonArray.length();i++)
					{
						JSONObject jsonObject = jsonArray.getJSONObject(i);

						item = new Item(jsonObject.getString("item"),jsonObject.getString("nombre"),jsonObject.getString("nombrecorto"),
								jsonObject.getString("categoria"),jsonObject.getString("familia"),jsonObject.getString("linea"),
								jsonObject.getString("marca"),jsonObject.getString("presenta"),jsonObject.getString("estado"),
								jsonObject.getString("dispoven"),jsonObject.getString("iva"),jsonObject.getString("bien"),
								jsonObject.getString("proveedor"),jsonObject.getInt("factor"),jsonObject.getInt("stock"),
								jsonObject.getDouble("stockmi"),jsonObject.getDouble("stockma"),jsonObject.getDouble("peso"),
								jsonObject.getDouble("volumen"),jsonObject.getDouble("precio1"),jsonObject.getDouble("precio2"),
								jsonObject.getDouble("precio3"),jsonObject.getDouble("precio4"),jsonObject.getDouble("precio5"),
								jsonObject.getDouble("pvp"),jsonObject.getString("itemr"),jsonObject.getString("ultven"),jsonObject.getString("ultcom"),
								jsonObject.getDouble("costop"),jsonObject.getDouble("costou"),jsonObject.getString("observa"),
								jsonObject.getString("grupo"),jsonObject.getString("combo"),jsonObject.getString("regalo"),
								jsonObject.getString("codprov"),jsonObject.getDouble("poruti"),jsonObject.getDouble("porutiventa"),
								jsonObject.getString("codbarra"),jsonObject.getString("canfra"),jsonObject.getInt("stockmay"),
								jsonObject.getDouble("porutipre1"),jsonObject.getDouble("porutipre2"),jsonObject.getDouble("porutipre3"),
								jsonObject.getDouble("porutipre4"),jsonObject.getDouble("porutipre5"),jsonObject.getDouble("litros"),
								jsonObject.getString("web"),jsonObject.getString("oferta"),jsonObject.getDouble("poferta"),jsonObject.getString("novedad"),
								jsonObject.getString("imagen"),jsonObject.getDouble("cantcompra"),jsonObject.getString("solopos"),
								jsonObject.getString("cuentaventa"),jsonObject.getString("espt"),jsonObject.getString("imagenadicional"),
								jsonObject.getString("tienectaventa"),jsonObject.getString("tipoprofal"),jsonObject.getDouble("pordessugerido"),
								jsonObject.getString("nombre_categoria"),jsonObject.getString("nombre_familia"),jsonObject.getString("nombre_linea"),
								jsonObject.getString("nombre_marca"),jsonObject.getString("nombre_presenta"),jsonObject.getString("nombre_proveedor"));
						if(db!=null)
						{
							datosBase="select * from item where item = '"+ item.getItem() +"'";
							c = db.rawQuery(datosBase, null);
							if(c.moveToFirst())
							{
								datosBase="update ITEM set stock=stock+"+item.getStock()+" where item='"+item.getItem()+"'";
							}
							else
							{
								datosBase = "INSERT INTO ITEM(item,nombre,nombrecorto,categoria,familia,linea,marca,presenta,estado,dispoven,"
										+ "iva,bien,proveedor,factor,stock,stockmi,stockma,peso,volumen,precio1,precio2,precio3,precio4,precio5,"
										+ "pvp,itemr,ultven,ultcom,costop,costou,observa,grupo,combo,regalo,codprov,poruti,porutiventa,codbarra,"
										+ "canfra,stockmay,porutipre1,porutipre2,porutipre3,porutipre4,porutipre5,litros,web,oferta,poferta,"
										+ "novedad,imagen,cantcompra,solopos,cuentaventa,espt,imagenadicional,tienectaventa,tipoprofal,pordessugerido,"
										+ "nombre_categoria,nombre_familia,nombre_linea,nombre_marca,nombre_presenta,nombre_proveedor) "
										+ "VALUES('"+item.getItem()+"','"+item.getNombre()+"','"+item.getNombreCorto()+"','"+item.getCategoria()+"',"
										+ "'"+item.getFamilia()+"','"+item.getLinea()+"','"+item.getMarca()+"','"+item.getPresenta()+"',"
										+ "'"+item.getEstado()+"','"+item.getDispoVen()+"','"+item.getIva()+"','"+item.getBien()+"','"+item.getProveedor()+"',"
										+ "'"+item.getFactor()+"','"+item.getStock()+"','"+item.getStockMi()+"','"+item.getStockMa()+"','"+item.getPeso()+"',"
										+ "'"+item.getVolumen()+"','"+item.getPrecio1()+"','"+item.getPrecio2()+"','"+item.getPrecio3()+"',"
										+ "'"+item.getPrecio4()+"','"+item.getPrecio5()+"','"+item.getPVP()+"','"+item.getItemReg()+"','"+item.getUltVen()+"',"
										+ "'"+item.getUltCom()+"','"+item.getCostoP()+"','"+item.getCostoU()+"','"+item.getObserva()+"','"+item.getGrupo()+"',"
										+ "'"+item.getCombo()+"','"+item.getRegalo()+"','"+item.getCodProv()+"','"+item.getPoruti()+"','"+item.getPorutiVenta()+"',"
										+ "'"+item.getCodBarra()+"','"+item.getCanFra()+"','"+item.getStockMay()+"','"+item.getPorutiPre1()+"',"
										+ "'"+item.getPorutiPre2()+"','"+item.getPorutiPre3()+"','"+item.getPorutiPre4()+"','"+item.getPorutiPre5()+"',"
										+ "'"+item.getLitros()+"','"+item.getWeb()+"','"+item.getOferta()+"','"+item.getPOferta()+"','"+item.getNovedad()+"',"
										+ "'"+item.getImagen()+"','"+item.getCantCompra()+"','"+item.getSoloPos()+"','"+item.getCuentaVenta()+"','"+item.getESPT()+"',"
										+ "'"+item.getImagenAdicional()+"','"+item.getTieneCuentaVenta()+"','"+item.getTipoProFal()+"','"+item.getPorDesSugerido()+"',"
										+ "'"+item.getNombreCategoria()+"','"+item.getNombreFamilia()+"','"+item.getNombreLinea()+"','"+item.getNombreMarca()+"',"
										+ "'"+item.getNombrePresenta()+"','"+item.getNombreProveedor()+"')";
							}
							db.execSQL(datosBase);
						}
					}
					dialogo.miDialogoToastCorto(getApplicationContext(), "Se actualizaron los items y el stock de autoventa");
				}
				else
				{
					dialogo.miDialogoToastCorto(getApplicationContext(), "No hay información de item para actualizar");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}







	/**************************************************************************************/
	/****** Registra los clientes traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSCliente extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSCliente(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			userRequest.addProperty("vendedor", params[0]); //Paso de par�metros al ws

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				dialogo.miDialogoToastCorto(getApplicationContext(), e.getMessage());
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";

			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);
				if(jsonArray.length()>0)
				{
					//datosBase = "delete from CLIENTE where (usuario = '"+ usuario +"' or  vendedor_aux='"+ usuario +"')";
					datosBase = "delete from CLIENTE";
					db.execSQL(datosBase);

					for(int i=0;i<jsonArray.length();i++)
					{
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						cli = new Clientes(jsonObject.getString("codigo"),jsonObject.getString("razonsocial"),jsonObject.getString("negocio"),
								jsonObject.getString("representa"),"C",jsonObject.getString("ruc"),jsonObject.getString("direccion"),
								jsonObject.getString("telefonos"),jsonObject.getString("email"),jsonObject.getString("tipo"),
								jsonObject.getString("provincia"),jsonObject.getString("canton"),jsonObject.getString("parroquia"),
								jsonObject.getString("sector"),jsonObject.getString("ruta"),jsonObject.getString("cupo"),
								jsonObject.getString("grupo"),jsonObject.getInt("orden"),jsonObject.getString("codfre"),jsonObject.getString("credito"),
								jsonObject.getInt("dia"),jsonObject.getString("fecdesde")," "," ",jsonObject.getString("fecultcom"),
								jsonObject.getString("tdcredito"),jsonObject.getInt("diascredit"),jsonObject.getString("vendedor"),
								jsonObject.getString("formapag"),jsonObject.getString("iva"),jsonObject.getString("confinal"),
								jsonObject.getString("clase"),jsonObject.getString("observacion"),jsonObject.getString("tiponego"),
								jsonObject.getString("ejex"),jsonObject.getString("ejey"),jsonObject.getString("vendedoraux")," "," "," ",
								" "," "," ",jsonObject.getString("estado"),"N","N",jsonObject.getString("clavefe"));

						if(db!=null)
						{
							datosBase = "INSERT INTO CLIENTE(codigo,nombre,negocio,representa,tipo_identificacion,numero_identificacion,direccion,telefono,"
									+ "email,tipo_cliente,provincia,canton,parroquia,sector,ruta,cupo,grupo_descuento,orden,"
									+ "frec_visita,credito,dia,fecha_desde,fecha_ingreso,hora_ingreso,fecha_ult_com,td_credito,"
									+ "dias_credito,usuario,forma_pago,iva,con_final,clase,observacion,tipo_negocio,ejex,ejey,vendedor_aux,"
									+ "fecha_modifica,hora_modifica,usuario_modifica,fecha_elimina,hora_elimina,usuario_elimina,estado,cltenuevo,descargado,clavefe) "
									+ "VALUES('"+cli.getCodigo().trim()+"','"+cli.getNombre()+"','"+cli.getNegocio()+"','"+cli.getRepresenta()+"',"
									+ "'"+cli.getTipoIdentificacion()+"','"+cli.getNumeroIdentificacion()+"','"+cli.getDireccion()+"',"
									+ "'"+cli.getTelefono()+"','"+cli.getEmail()+"','"+cli.getTipoCliente()+"','"+cli.getProvincia()+"',"
									+ "'"+cli.getCanton()+"','"+cli.getParroquia()+"','"+cli.getSector()+"','"+cli.getRuta()+"','"+cli.getCupo()+"',"
									+ "'"+cli.getGrupoDescuento()+"','"+cli.getOrden()+"','"+cli.getFrecuenciaVisita()+"','"+cli.getCredito()+"',"
									+ "'"+cli.getDia()+"','"+cli.getFechaDesde()+"','"+cli.getFechaIngreso()+"','"+cli.getHoraIngreso()+"',"
									+ "'"+cli.getFechaUltimaCompra()+"','"+cli.getTdCredito()+"','"+cli.getDiasCredito()+"','"+cli.getUsuario()+"',"
									+ "'"+cli.getFormaPago()+"','"+cli.getIva()+"','"+cli.getConFinal()+"','"+cli.getClase()+"','"+cli.getObservacion()+"',"
									+ "'"+cli.getTipoNegocio()+"','"+cli.getEjex()+"','"+cli.getEjey()+"','"+cli.getVendedorAux()+"',"
									+ "'"+cli.getFechaModifica()+"','"+cli.getHoraModifica()+"','"+cli.getUsuarioModifica()+"','"+cli.getFechaElimina()+"',"
									+ "'"+cli.getHoraElimina()+"','"+cli.getUsuarioElimina()+"','"+cli.getEstado()+"','"+cli.getClienteNuevo()+"','"+cli.getDescargado()+"','"+cli.getClavefe()+"')";
							db.execSQL(datosBase);
						}
					}
					dialogo.miDialogoToastCorto(getApplicationContext(), "Se insertaron los clientes");
				}
				else
				{
					dialogo.miDialogoToastCorto(getApplicationContext(), "No hay información de clientes para actualizar");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}


	/**************************************************************************************/
	/****** Registra los item traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSItem extends AsyncTask<String,Void,String>{

		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSItem(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			userRequest.addProperty("nombre",null); //Paso de par�metros al ws

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				mensaje.miDialogoToastLargo(getApplicationContext(), mex.formaMensajeExcepcion("Se produjo la excepción al cargar los items", "llamaWSItem", "doInBackground()", e.getMessage()));
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);
				if(jsonArray.length()>0)
				{
					datosBase = "delete from ITEM";
					db.execSQL(datosBase);
					for(int i=0;i<jsonArray.length();i++)
					{
						JSONObject jsonObject = jsonArray.getJSONObject(i);

						item = new Item(jsonObject.getString("item"),jsonObject.getString("nombre"),jsonObject.getString("nombrecorto"),
								jsonObject.getString("categoria"),jsonObject.getString("familia"),jsonObject.getString("linea"),
								jsonObject.getString("marca"),jsonObject.getString("presenta"),jsonObject.getString("estado"),
								jsonObject.getString("dispoven"),jsonObject.getString("iva"),jsonObject.getString("bien"),
								jsonObject.getString("proveedor"),jsonObject.getInt("factor"),jsonObject.getInt("stock"),
								jsonObject.getDouble("stockmi"),jsonObject.getDouble("stockma"),jsonObject.getDouble("peso"),
								jsonObject.getDouble("volumen"),jsonObject.getDouble("precio1"),jsonObject.getDouble("precio2"),
								jsonObject.getDouble("precio3"),jsonObject.getDouble("precio4"),jsonObject.getDouble("precio5"),
								jsonObject.getDouble("pvp"),jsonObject.getString("itemr"),jsonObject.getString("ultven"),jsonObject.getString("ultcom"),
								jsonObject.getDouble("costop"),jsonObject.getDouble("costou"),jsonObject.getString("observa"),
								jsonObject.getString("grupo"),jsonObject.getString("combo"),jsonObject.getString("regalo"),
								jsonObject.getString("codprov"),jsonObject.getDouble("poruti"),jsonObject.getDouble("porutiventa"),
								jsonObject.getString("codbarra"),jsonObject.getString("canfra"),jsonObject.getInt("stockmay"),
								jsonObject.getDouble("porutipre1"),jsonObject.getDouble("porutipre2"),jsonObject.getDouble("porutipre3"),
								jsonObject.getDouble("porutipre4"),jsonObject.getDouble("porutipre5"),jsonObject.getDouble("litros"),
								jsonObject.getString("web"),jsonObject.getString("oferta"),jsonObject.getDouble("poferta"),jsonObject.getString("novedad"),
								jsonObject.getString("imagen"),jsonObject.getDouble("cantcompra"),jsonObject.getString("solopos"),
								jsonObject.getString("cuentaventa"),jsonObject.getString("espt"),jsonObject.getString("imagenadicional"),
								jsonObject.getString("tienectaventa"),jsonObject.getString("tipoprofal"),jsonObject.getDouble("pordessugerido"),
								jsonObject.getString("nombre_categoria"),jsonObject.getString("nombre_familia"),jsonObject.getString("nombre_linea"),
								jsonObject.getString("nombre_marca"),jsonObject.getString("nombre_presenta"),jsonObject.getString("nombre_proveedor"));
						if(db!=null)
						{
							datosBase = "INSERT INTO ITEM(item,nombre,nombrecorto,categoria,familia,linea,marca,presenta,estado,dispoven,"
									+ "iva,bien,proveedor,factor,stock,stockmi,stockma,peso,volumen,precio1,precio2,precio3,precio4,precio5,"
									+ "pvp,itemr,ultven,ultcom,costop,costou,observa,grupo,combo,regalo,codprov,poruti,porutiventa,codbarra,"
									+ "canfra,stockmay,porutipre1,porutipre2,porutipre3,porutipre4,porutipre5,litros,web,oferta,poferta,"
									+ "novedad,imagen,cantcompra,solopos,cuentaventa,espt,imagenadicional,tienectaventa,tipoprofal,pordessugerido,"
									+ "nombre_categoria,nombre_familia,nombre_linea,nombre_marca,nombre_presenta,nombre_proveedor) "
									+ "VALUES('"+item.getItem()+"','"+item.getNombre()+"','"+item.getNombreCorto()+"','"+item.getCategoria()+"',"
									+ "'"+item.getFamilia()+"','"+item.getLinea()+"','"+item.getMarca()+"','"+item.getPresenta()+"',"
									+ "'"+item.getEstado()+"','"+item.getDispoVen()+"','"+item.getIva()+"','"+item.getBien()+"','"+item.getProveedor()+"',"
									+ "'"+item.getFactor()+"','"+item.getStock()+"','"+item.getStockMi()+"','"+item.getStockMa()+"','"+item.getPeso()+"',"
									+ "'"+item.getVolumen()+"','"+item.getPrecio1()+"','"+item.getPrecio2()+"','"+item.getPrecio3()+"',"
									+ "'"+item.getPrecio4()+"','"+item.getPrecio5()+"','"+item.getPVP()+"','"+item.getItemReg()+"','"+item.getUltVen()+"',"
									+ "'"+item.getUltCom()+"','"+item.getCostoP()+"','"+item.getCostoU()+"','"+item.getObserva()+"','"+item.getGrupo()+"',"
									+ "'"+item.getCombo()+"','"+item.getRegalo()+"','"+item.getCodProv()+"','"+item.getPoruti()+"','"+item.getPorutiVenta()+"',"
									+ "'"+item.getCodBarra()+"','"+item.getCanFra()+"','"+item.getStockMay()+"','"+item.getPorutiPre1()+"',"
									+ "'"+item.getPorutiPre2()+"','"+item.getPorutiPre3()+"','"+item.getPorutiPre4()+"','"+item.getPorutiPre5()+"',"
									+ "'"+item.getLitros()+"','"+item.getWeb()+"','"+item.getOferta()+"','"+item.getPOferta()+"','"+item.getNovedad()+"',"
									+ "'"+item.getImagen()+"','"+item.getCantCompra()+"','"+item.getSoloPos()+"','"+item.getCuentaVenta()+"','"+item.getESPT()+"',"
									+ "'"+item.getImagenAdicional()+"','"+item.getTieneCuentaVenta()+"','"+item.getTipoProFal()+"','"+item.getPorDesSugerido()+"',"
									+ "'"+item.getNombreCategoria()+"','"+item.getNombreFamilia()+"','"+item.getNombreLinea()+"','"+item.getNombreMarca()+"',"
									+ "'"+item.getNombrePresenta()+"','"+item.getNombreProveedor()+"')";
							db.execSQL(datosBase);
						}
					}
					dialogo.miDialogoToastCorto(getApplicationContext(), "Se insertaron los items");
					dialogo.miDialogoToastCorto(getApplicationContext(), "Carga Finalizada");
				}
				else
				{
					dialogo.miDialogoToastCorto(getApplicationContext(), "No hay información de items para actualizar");
				}
			} catch (JSONException e) {
				mensaje.miDialogoToastLargo(getApplicationContext(), mex.formaMensajeExcepcion("Se produjo la excepción al cargar los items", "llamaWSItem", "onPostExecute()", e.getMessage()));
			}
			super.onPostExecute(String.valueOf(result));
		}
	}


	private class llamaWSItemAutoVenta extends AsyncTask<String,Void,String>{

		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSItemAutoVenta(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			userRequest.addProperty("bodega", params[0]);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				mensaje.miDialogoToastLargo(getApplicationContext(), mex.formaMensajeExcepcion("Se produjo la excepción al cargar los items", "llamaWSItemAutoVenta", "doInBackground()", e.getMessage()));
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);
				if(jsonArray.length()>0)
				{
					datosBase = "delete from ITEM";
					db.execSQL(datosBase);
					for(int i=0;i<jsonArray.length();i++)
					{
						JSONObject jsonObject = jsonArray.getJSONObject(i);

						item = new Item(jsonObject.getString("item"),jsonObject.getString("nombre"),jsonObject.getString("nombrecorto"),
								jsonObject.getString("categoria"),jsonObject.getString("familia"),jsonObject.getString("linea"),
								jsonObject.getString("marca"),jsonObject.getString("presenta"),jsonObject.getString("estado"),
								jsonObject.getString("dispoven"),jsonObject.getString("iva"),jsonObject.getString("bien"),
								jsonObject.getString("proveedor"),jsonObject.getInt("factor"),jsonObject.getInt("stock"),
								jsonObject.getDouble("stockmi"),jsonObject.getDouble("stockma"),jsonObject.getDouble("peso"),
								jsonObject.getDouble("volumen"),jsonObject.getDouble("precio1"),jsonObject.getDouble("precio2"),
								jsonObject.getDouble("precio3"),jsonObject.getDouble("precio4"),jsonObject.getDouble("precio5"),
								jsonObject.getDouble("pvp"),jsonObject.getString("itemr"),jsonObject.getString("ultven"),jsonObject.getString("ultcom"),
								jsonObject.getDouble("costop"),jsonObject.getDouble("costou"),jsonObject.getString("observa"),
								jsonObject.getString("grupo"),jsonObject.getString("combo"),jsonObject.getString("regalo"),
								jsonObject.getString("codprov"),jsonObject.getDouble("poruti"),jsonObject.getDouble("porutiventa"),
								jsonObject.getString("codbarra"),jsonObject.getString("canfra"),jsonObject.getInt("stockmay"),
								jsonObject.getDouble("porutipre1"),jsonObject.getDouble("porutipre2"),jsonObject.getDouble("porutipre3"),
								jsonObject.getDouble("porutipre4"),jsonObject.getDouble("porutipre5"),jsonObject.getDouble("litros"),
								jsonObject.getString("web"),jsonObject.getString("oferta"),jsonObject.getDouble("poferta"),jsonObject.getString("novedad"),
								jsonObject.getString("imagen"),jsonObject.getDouble("cantcompra"),jsonObject.getString("solopos"),
								jsonObject.getString("cuentaventa"),jsonObject.getString("espt"),jsonObject.getString("imagenadicional"),
								jsonObject.getString("tienectaventa"),jsonObject.getString("tipoprofal"),jsonObject.getDouble("pordessugerido"),
								jsonObject.getString("nombre_categoria"),jsonObject.getString("nombre_familia"),jsonObject.getString("nombre_linea"),
								jsonObject.getString("nombre_marca"),jsonObject.getString("nombre_presenta"),jsonObject.getString("nombre_proveedor"));
						if(db!=null)
						{
							datosBase = "INSERT INTO ITEM(item,nombre,nombrecorto,categoria,familia,linea,marca,presenta,estado,dispoven,"
									+ "iva,bien,proveedor,factor,stock,stockmi,stockma,peso,volumen,precio1,precio2,precio3,precio4,precio5,"
									+ "pvp,itemr,ultven,ultcom,costop,costou,observa,grupo,combo,regalo,codprov,poruti,porutiventa,codbarra,"
									+ "canfra,stockmay,porutipre1,porutipre2,porutipre3,porutipre4,porutipre5,litros,web,oferta,poferta,"
									+ "novedad,imagen,cantcompra,solopos,cuentaventa,espt,imagenadicional,tienectaventa,tipoprofal,pordessugerido,"
									+ "nombre_categoria,nombre_familia,nombre_linea,nombre_marca,nombre_presenta,nombre_proveedor) "
									+ "VALUES('"+item.getItem()+"','"+item.getNombre()+"','"+item.getNombreCorto()+"','"+item.getCategoria()+"',"
									+ "'"+item.getFamilia()+"','"+item.getLinea()+"','"+item.getMarca()+"','"+item.getPresenta()+"',"
									+ "'"+item.getEstado()+"','"+item.getDispoVen()+"','"+item.getIva()+"','"+item.getBien()+"','"+item.getProveedor()+"',"
									+ "'"+item.getFactor()+"','"+item.getStock()+"','"+item.getStockMi()+"','"+item.getStockMa()+"','"+item.getPeso()+"',"
									+ "'"+item.getVolumen()+"','"+item.getPrecio1()+"','"+item.getPrecio2()+"','"+item.getPrecio3()+"',"
									+ "'"+item.getPrecio4()+"','"+item.getPrecio5()+"','"+item.getPVP()+"','"+item.getItemReg()+"','"+item.getUltVen()+"',"
									+ "'"+item.getUltCom()+"','"+item.getCostoP()+"','"+item.getCostoU()+"','"+item.getObserva()+"','"+item.getGrupo()+"',"
									+ "'"+item.getCombo()+"','"+item.getRegalo()+"','"+item.getCodProv()+"','"+item.getPoruti()+"','"+item.getPorutiVenta()+"',"
									+ "'"+item.getCodBarra()+"','"+item.getCanFra()+"','"+item.getStockMay()+"','"+item.getPorutiPre1()+"',"
									+ "'"+item.getPorutiPre2()+"','"+item.getPorutiPre3()+"','"+item.getPorutiPre4()+"','"+item.getPorutiPre5()+"',"
									+ "'"+item.getLitros()+"','"+item.getWeb()+"','"+item.getOferta()+"','"+item.getPOferta()+"','"+item.getNovedad()+"',"
									+ "'"+item.getImagen()+"','"+item.getCantCompra()+"','"+item.getSoloPos()+"','"+item.getCuentaVenta()+"','"+item.getESPT()+"',"
									+ "'"+item.getImagenAdicional()+"','"+item.getTieneCuentaVenta()+"','"+item.getTipoProFal()+"','"+item.getPorDesSugerido()+"',"
									+ "'"+item.getNombreCategoria()+"','"+item.getNombreFamilia()+"','"+item.getNombreLinea()+"','"+item.getNombreMarca()+"',"
									+ "'"+item.getNombrePresenta()+"','"+item.getNombreProveedor()+"')";
							db.execSQL(datosBase);
						}
					}
					dialogo.miDialogoToastCorto(getApplicationContext(), "Se insertaron los items de autoventa de bodega ");
				}
				else
				{
					dialogo.miDialogoToastCorto(getApplicationContext(), "No hay información de items para actualizar");
				}
			} catch (JSONException e) {
				mensaje.miDialogoToastLargo(getApplicationContext(), mex.formaMensajeExcepcion("Se produjo la excepción al cargar los items", "llamaWSItemAutoVenta", "onPostExecute()", e.getMessage()));
			}
			super.onPostExecute(String.valueOf(result));
		}
	}


	/**************************************************************************************/
	/****** Registra las condiciones comerciales traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSCondiComer extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSCondiComer(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from CONDICIONESCOMERCIALES";
			db.execSQL(datosBase);

			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					condicomer = new CondiComer(jsonObject.getInt("secuencial"),jsonObject.getString("nombre"),
							jsonObject.getString("fecdesde"),jsonObject.getString("fechasta"),
							jsonObject.getString("fechacre"),jsonObject.getInt("tipo"),
							jsonObject.getInt("aplicacion"),jsonObject.getInt("alcance"),
							jsonObject.getDouble("cantdesde"),jsonObject.getDouble("canthasta"),
							jsonObject.getDouble("valordesde"),jsonObject.getDouble("valorhasta"),
							jsonObject.getInt("modo"),jsonObject.getDouble("pordes"),jsonObject.getString("itemreg"),
							jsonObject.getInt("cantreg"),jsonObject.getString("cliente"),jsonObject.getString("item"),
							jsonObject.getString("grupocli"),jsonObject.getString("grupopro"),jsonObject.getString("tipoapl"),
							jsonObject.getString("todos"),jsonObject.getString("cuenta"),jsonObject.getString("cuenta0"),
							jsonObject.getString("ptoventa"));
					if(db!=null)
					{
						datosBase = "INSERT INTO CONDICIONESCOMERCIALES(secuencial,nombre,fecdesde,fechasta,fechacre,tipo,aplicacion,"
								+ "alcance,cantdesde,canthasta,valordesde,valorhasta,modo,pordes,item_reg,can_reg,cliente,item,grupo_cli,"
								+ "grupo_pro,tipoapli,todos,cuenta,cuenta0,ptoventa) "
								+ "VALUES('"+condicomer.getSecuencial()+"','"+condicomer.getNombre()+"','"+condicomer.getFecDesde()+"',"
								+ "'"+condicomer.getFecHasta()+"','"+condicomer.getFechaCre()+"','"+condicomer.getTipo()+"',"
								+ "'"+condicomer.getApli()+"','"+condicomer.getAlcance()+"','"+condicomer.getCantDesde()+"',"
								+ "'"+condicomer.getCantHasta()+"','"+condicomer.getValorDesde()+"','"+condicomer.getValorHasta()+"',"
								+ "'"+condicomer.getModo()+"','"+condicomer.getPordes()+"','"+condicomer.getItemReg()+"',"
								+ "'"+condicomer.getCanReg()+"','"+condicomer.getCliente()+"','"+condicomer.getItem()+"',"
								+ "'"+condicomer.getGrupoCli()+"','"+condicomer.getGrupoPro()+"','"+condicomer.getTipoApli()+"',"
								+ "'"+condicomer.getTodos()+"','"+condicomer.getCuenta()+"','"+condicomer.getCuenta0()+"',"
								+ "'"+condicomer.getPtoVta()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(), "Se insertaron todas las condiciones comerciales");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}


	/**************************************************************************************/
	/****** Registra la cartera traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSDeuda extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSDeuda(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			userRequest.addProperty("codven", params[0]); //Paso de par�metros al ws

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
				dialogo.miDialogoToastCorto(getApplicationContext(), e.getMessage());
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from DEUDA";
			db.execSQL(datosBase);

			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					deuda = new Deuda(jsonObject.getInt("secuencial"),jsonObject.getString("bodega"),
							jsonObject.getString("cliente"),jsonObject.getString("tipo"),
							jsonObject.getInt("numero"),jsonObject.getString("serie"),
							jsonObject.getString("secinv"),jsonObject.getDouble("iva"),
							jsonObject.getDouble("monto"),jsonObject.getDouble("credito"),
							jsonObject.getDouble("saldo"),jsonObject.getString("fechaemi"),
							jsonObject.getString("fechaven"),jsonObject.getString("vendedor"),
							jsonObject.getString("observacion"));
					if(db!=null)
					{
						datosBase = "INSERT INTO DEUDA(secuencial,bodega,cliente,tipo,numero,serie,secinv,iva,monto,credito,saldo," +
								"fechaemi,fechaven,vendedor,observacion) "
								+ "VALUES('"+deuda.getSecuencial()+"','"+deuda.getBodega()+"','"+deuda.getCliente()+"',"
								+ "'"+deuda.getTipo()+"','"+deuda.getNumero()+"','"+deuda.getSerie()+"',"
								+ "'"+deuda.getSecinv()+"','"+deuda.getIva()+"','"+deuda.getMonto()+"',"
								+ "'"+deuda.getCredito()+"','"+deuda.getSaldo()+"','"+deuda.getFechaEmi()+"',"
								+ "'"+deuda.getFechaVen()+"','"+deuda.getVendedor()+"','"+deuda.getObservacion()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(), "Se insertó la cartera de los clientes");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}


	/**************************************************************************************/
	/****** Registra los grupos de productos traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSGrupoPro extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSGrupoPro(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from GRUPOPRO";
			db.execSQL(datosBase);

			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					gpro = new GrupoPro(jsonObject.getString("codigo"),jsonObject.getString("nombre"),
							jsonObject.getString("item"),jsonObject.getString("categoria"),
							jsonObject.getString("familia"),jsonObject.getString("linea"),
							jsonObject.getString("marca"),jsonObject.getString("estado"));
					if(db!=null)
					{
						datosBase = "INSERT INTO GRUPOPRO(codigo,nombre,item,categoria,familia,linea,marca,estado) "
								+ "VALUES('"+gpro.getCodigo()+"','"+gpro.getNombre()+"','"+gpro.getItem()+"',"
								+ "'"+gpro.getCategoria()+"','"+gpro.getFamilia()+"','"+gpro.getLinea()+"',"
								+ "'"+gpro.getMarca()+"','"+gpro.getEstado()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(), "Se insertaron los grupos de productos");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}


	/**************************************************************************************/
	/****** Registra los grupos de clientes traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSGrupoCli extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSGrupoCli(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from GRUPOCLI";
			db.execSQL(datosBase);

			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					gcli = new GrupoCli(jsonObject.getString("codigo"),jsonObject.getString("nombre"),
							jsonObject.getString("cliente"),jsonObject.getString("ruta"),
							jsonObject.getString("clase"),jsonObject.getString("vendedor"),
							jsonObject.getString("tipo"),jsonObject.getString("estado"));
					if(db!=null)
					{
						datosBase = "INSERT INTO GRUPOCLI(codigo,nombre,cliente,ruta,clase,vendedor,tipo,estado) "
								+ "VALUES('"+gcli.getCodigo()+"','"+gcli.getNombre()+"','"+gcli.getCliente()+"',"
								+ "'"+gcli.getRuta()+"','"+gcli.getClase()+"','"+gcli.getVendedor()+"',"
								+ "'"+gcli.getTipo()+"','"+gcli.getEstado()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(), "Se insertaron todos los grupos de clientes");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}


	/**************************************************************************************/
	/****** Registra los usuarios en este caso los vendedores traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSUsuario extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSUsuario(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			userRequest.addProperty("codigo", params[0]); //Paso de par�metros al ws

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					objetoUsuario = new Usuario(jsonObject.getString("codigo"),jsonObject.getString("nombre"),
							jsonObject.getString("clave"),jsonObject.getString("supervisor"),
							jsonObject.getString("direccion"),jsonObject.getString("cedula"),
							jsonObject.getString("telefono"),jsonObject.getString("estado"),
							jsonObject.getString("crea_cliente"),jsonObject.getString("coloca_descuento"),
							jsonObject.getString("editaprecio"),jsonObject.getString("esautoventa"),
							Integer.parseInt(jsonObject.getString("bodega")),jsonObject.getString("ventop"));
					if(db!=null)
					{
						datosBase = "UPDATE USUARIO SET nombre='"+ objetoUsuario.getNombre() +"'," +
								"supervisor='"+ objetoUsuario.getSupervisor() +"'," +
								"direccion='"+ objetoUsuario.getDireccion() +"'," +
								"cedula='"+ objetoUsuario.getCedula() +"'," +
								"telefono='"+ objetoUsuario.getTelefono() +"'," +
								"crea_cliente='"+ objetoUsuario.getCreaCliente() +"'," +
								"coloca_descuento='"+ objetoUsuario.getColocaDescuento() +"', "+
								"edita_precio='"+ objetoUsuario.getEditaPrecio() +"', "+
								"es_autoventa='"+ objetoUsuario.getEsAutoVenta() +"', "+
								"bodega='"+objetoUsuario.getBodega()+"', "+
								"ventop='"+objetoUsuario.getVentop()+"' "+
								"where codigo = '"+ objetoUsuario.getCodigo() +"'";
						db.execSQL(datosBase);
						esautoventa = objetoUsuario.getEsAutoVenta();
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(), "Se actualizó la información del usuario " + usuario);

				if(esautoventa.equals("N"))
				{
					try
					{
						new llamaWSItem(constante.NAMESPACE, constante.URLWS, constante.NAMESPACE+"/getItemTodos","getItemTodos").execute(); //si no tiene parametros
					}catch(Exception e){
						mensaje.miDialogoToastLargo(getApplicationContext(), mex.formaMensajeExcepcion("Se produjo la excepción al cargar los items", "llamaWSItem", "onPostExecute()", e.getMessage()));
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}

	/**************************************************************************************/
	/****** Registra los par�metros del sistema traido del WS ******/
	/**************************************************************************************/
	private class llamaWSParametros extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSParametros(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					parametros = new Parametros(jsonObject.getString("nombrecia"),jsonObject.getInt("iva"),jsonObject.getInt("bodfac"),jsonObject.getInt("faclin"),jsonObject.getString("paginaweb"),jsonObject.getString("eprecio"),jsonObject.getString("enotaventa"),jsonObject.getString("cartera"));
					if(db!=null)
					{
						datosBase = "update PARAMETROS set NOMBRE_CIA='"+ parametros.getNombreCia() +"'," +
								"IVA='"+ parametros.getIva() +"'," +
								"BODFAC='"+ parametros.getBodegaFac() +"'," +
								"NUM_LINEAS_PED='"+ parametros.getNumeroLineaPed() +"'," +
								"PAGINAWEB='"+ parametros.getPaginaWeb() +"'," +
								"EPRECIO='"+ parametros.getEprecio() +"',"+
								"ESNOTAVENTA='"+ parametros.getEnotaventa() +"',"+
								"CARTERA='"+ parametros.getCartera() +"'";

						db.execSQL(datosBase);
					}

					datosBase = "UPDATE PARAMETROBODEGA set bodega='"+parametros.getBodegaFac()+"'";
					db.execSQL(datosBase);
				}
				//dialogo.miDialogoToastCorto(getApplicationContext(), "Se registr� los par�metros del sistema");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}



	/**************************************************************************************/
	/****** Registra los tipos de negocios traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSTipoNegocio extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSTipoNegocio(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from tipo_negocio";
			db.execSQL(datosBase);

			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					tiponego = new TipoNegocio(jsonObject.getString("codigo"),jsonObject.getString("nombre"),jsonObject.getString("estado"));
					if(db!=null)
					{
						datosBase = "INSERT INTO tipo_negocio(codigo,nombre,estado) "
								+ "VALUES('"+tiponego.getCodigo()+"','"+tiponego.getNombre()+"','"+tiponego.getEstado()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(), "Se registró los tipos de negocios");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}





	/**************************************************************************************/
	/****** Registra los tipos de clientes traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSTipoCliente extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSTipoCliente(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from tipo_cliente";
			db.execSQL(datosBase);

			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					tipocli = new TipoCliente(jsonObject.getString("codigo"),jsonObject.getString("nombre"),jsonObject.getString("estado"));
					if(db!=null)
					{
						datosBase = "INSERT INTO tipo_cliente(codigo,nombre,estado) "
								+ "VALUES('"+tipocli.getCodigo()+"','"+tipocli.getNombre()+"','"+tipocli.getEstado()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(), "Se registró los tipos de cliente");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}


	/**************************************************************************************/
	/****** Registra las frecuencias de visitas traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSFrecuenciaVisita extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSFrecuenciaVisita(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from frecuencia_visita";
			db.execSQL(datosBase);

			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					frevis = new FrecuenciaVisita(jsonObject.getString("codigo"),jsonObject.getString("nombre"),jsonObject.getInt("dia"));
					if(db!=null)
					{
						datosBase = "INSERT INTO frecuencia_visita(codigo,nombre,dia) "
								+ "VALUES('"+frevis.getCodigo()+"','"+frevis.getNombre()+"','"+frevis.getDia()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(),"Se registraron las frecuencias de visitas");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}


	/**************************************************************************************/
	/****** Registra las rutas traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSRuta extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSRuta(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from ruta";
			db.execSQL(datosBase);

			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					ruta = new Ruta(jsonObject.getString("codigo"),jsonObject.getString("nombre"),jsonObject.getString("estado"));
					if(db!=null)
					{
						datosBase = "INSERT INTO ruta(codigo,nombre,estado) "
								+ "VALUES('"+ruta.getCodigo()+"','"+ruta.getNombre()+"','"+ruta.getEstado()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(),"Se registraron las rutas");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}


    /**************************************************************************************/
    /****** Registra las zonas traidos del WS ******/
    /**************************************************************************************/
    private class llamaWSZona extends AsyncTask<String,Void,String>{
        private String NAMESPACE;
        private String URL;
        private String SOAP_ACTION;
        private String METHOD;
        String respuesta = null;

        public llamaWSZona(String namespace,String url,String soap,String metodo)
        {
            this.NAMESPACE=namespace;
            this.URL=url;
            this.SOAP_ACTION=soap;
            this.METHOD=metodo;
        }

        @Override
        protected String doInBackground(String... params) {
            SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
            if(params.length==0)
            {
                userRequest.addProperty("nombre",null); //Paso de par�metros al ws
            }
            else
            {
                for(int i=0; i<params.length; i++)
                {
                    userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
                }
            }
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
            envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

            try{
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
                respuesta = envelope.getResponse().toString();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return respuesta;
        }

        protected void onPostExecute(String result)
        {
            String datosBase = "";
            datosBase = "delete from zona";
            db.execSQL(datosBase);
            try {
                //obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
                JSONArray jsonArray = new JSONArray(result);

                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    zona = new Zona(jsonObject.getString("codigo"),jsonObject.getString("nombre"),jsonObject.getString("estado"));
                    if(db!=null)
                    {
                        datosBase = "INSERT INTO zona(codigo,nombre,estado) "
                                + "VALUES('"+zona.getCodigo()+"','"+zona.getNombre()+"','"+zona.getEstado()+"')";
                        db.execSQL(datosBase);
                    }
                }
                dialogo.miDialogoToastCorto(getApplicationContext(),"Se registraron las zonas");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(String.valueOf(result));
        }
    }

	/**************************************************************************************/
	/****** Registra las parroquias traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSParroquia extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSParroquia(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from PARROQUIA";
			db.execSQL(datosBase);

			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					parroquia = new Parroquia(jsonObject.getString("codigo"),jsonObject.getString("canton"),jsonObject.getString("provincia"),jsonObject.getString("nombre"),jsonObject.getString("estado"));
					if(db!=null)
					{
						datosBase = "INSERT INTO PARROQUIA(codigo,canton,provincia,nombre,estado) "
								+ "VALUES('"+parroquia.getCodigo()+"','"+parroquia.getCanton()+"','"+parroquia.getProvincia()+"','"+parroquia.getNombre()+"','"+parroquia.getEstado()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(), "Se registró las parroquias");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}



	/**************************************************************************************/
	/****** Registra los bancos de la CIA traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSBancoCia extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSBancoCia(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from BANCOCIA";
			db.execSQL(datosBase);
			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					bancocia = new BancoCia(jsonObject.getString("codigo"),jsonObject.getString("nombre"),jsonObject.getString("cuenta"),jsonObject.getString("estado"));
					if(db!=null)
					{
						datosBase = "INSERT INTO bancocia(codigo,nombre,cuenta,estado) "
								+ "VALUES('"+bancocia.getCodigo()+"','"+bancocia.getNombre()+"','"+bancocia.getNumeroCuenta()+"','"+bancocia.getEstado()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(),"Se registraron los bancos de la CIA");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}


    /**************************************************************************************/
	/****** Registra los dias de credito traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSDiasCredito extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSDiasCredito(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from DIASCREDITO";
			db.execSQL(datosBase);

			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					dia = new DiasCredito(jsonObject.getString("codigo"),jsonObject.getString("nombre"),jsonObject.getString("dia"));
					if(db!=null)
					{
						datosBase = "INSERT INTO DIASCREDITO(codigo,nombre,dia) "
								+ "VALUES('"+dia.getCodigo()+"','"+dia.getNombre()+"','"+dia.getDia()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(), "Se registr� los dias de credito");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}


	/**************************************************************************************/
	/****** Registra las relaciones de los tipos de clientes con el precio asignado traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSCliPrecio extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSCliPrecio(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from CLIPRECIO";
			db.execSQL(datosBase);

			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					clipre = new CliPrecio(jsonObject.getInt("precio"),jsonObject.getString("tipo"),jsonObject.getString("fecha"),jsonObject.getString("operador"));
					if(db!=null)
					{
						datosBase = "INSERT INTO CLIPRECIO(precio,tipo,fecha,operador) "
								+ "VALUES('"+clipre.getPrecio()+"','"+clipre.getTipo()+"','"+clipre.getFecha()+"','"+clipre.getOperador()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(),"Se registraron las relaciones de precio - cliente");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}


	/**************************************************************************************/
	/****** Registra los itemtop traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSItemTop extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSItemTop(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from ITEMTOP";
			db.execSQL(datosBase);

			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					itemt = new ItemTop(jsonObject.getString("vendedor"),jsonObject.getString("item"),jsonObject.getString("operador"),"");
					if(db!=null)
					{
						datosBase = "INSERT INTO ITEMTOP(vendedor,item,operador,fechareg) "
								+ "VALUES('"+itemt.getVendedor()+"','"+itemt.getItem()+"','"+itemt.getOperador()+"','"+itemt.getFechareg()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(), "Se registró los item top");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}



/**************************************************************************************/
	/****** Registra los dias de credito traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSMedioPago extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSMedioPago(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from MEDIOPAGO";
			db.execSQL(datosBase);

			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					medpag = new MedioPago(jsonObject.getString("codigo"), jsonObject.getString("nombre"), jsonObject.getString("estado"));
					if(db!=null)
					{
						datosBase = "INSERT INTO MEDIOPAGO(codigo,nombre,estado) "
								+ "VALUES('"+medpag.getCodigo()+"','"+medpag.getNombre()+"','"+medpag.getEstado()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(), "Se registro el medio pago");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}

	/**************************************************************************************/
	/****** Registra los bancos traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSBanco extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSBanco(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from BANCO";
			db.execSQL(datosBase);
			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					banco = new Banco(jsonObject.getString("codigo"),jsonObject.getString("nombre"),jsonObject.getString("estado"));
					if(db!=null)
					{
						datosBase = "INSERT INTO BANCO(codigo,nombre,estado) "
								+ "VALUES('"+banco.getCodigo()+"','"+banco.getNombre()+"','"+banco.getEstado()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(),"Se registraron los bancos");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}


	/**************************************************************************************/
	/****** Registra las relaciones de los tipos de clientes con el precio asignado traidos del WS ******/
	/**************************************************************************************/
	private class llamaWSItemXCliente extends AsyncTask<String,Void,String>{
		private String NAMESPACE;
		private String URL;
		private String SOAP_ACTION;
		private String METHOD;
		String respuesta = null;

		public llamaWSItemXCliente(String namespace,String url,String soap,String metodo)
		{
			this.NAMESPACE=namespace;
			this.URL=url;
			this.SOAP_ACTION=soap;
			this.METHOD=metodo;
		}

		@Override
		protected String doInBackground(String... params) {
			SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
			if(params.length==0)
			{
				userRequest.addProperty("nombre",null); //Paso de par�metros al ws
			}
			else
			{
				for(int i=0; i<params.length; i++)
				{
					userRequest.addProperty("param"+i, params[i]); //Paso de par�metros al ws
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
			envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

			try{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
				respuesta = envelope.getResponse().toString();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return respuesta;
		}

		protected void onPostExecute(String result)
		{
			String datosBase = "";
			datosBase = "delete from ITEMXCLIENTE";
			db.execSQL(datosBase);

			try {
				//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
				JSONArray jsonArray = new JSONArray(result);

				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					itemxcli = new ItemXCliente(jsonObject.getString("cliente"),jsonObject.getString("item"),jsonObject.getDouble("precio"),jsonObject.getString("fechadesde"),jsonObject.getString("fechahasta"));
					if(db!=null)
					{
						datosBase = "INSERT INTO ITEMXCLIENTE(cliente,item,precio,fechadesde,fechahasta) "
								+ "VALUES('"+itemxcli.getCliente()+"','"+itemxcli.getItem()+"','"+itemxcli.getPrecio()+"','"+itemxcli.getFechaDesde()+"','"+itemxcli.getFechaHasta()+"')";
						db.execSQL(datosBase);
					}
				}
				dialogo.miDialogoToastCorto(getApplicationContext(),"Se registraron las relaciones de item por cliente");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(String.valueOf(result));
		}
	}










}