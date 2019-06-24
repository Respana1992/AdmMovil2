package com.birobidsa.admmovil.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import clasesAdapter.DetallesPedidosAdapter;
import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.DetallesPedidos;
import misclases.Pedidos;
import procesos.CONDICIONESCOMERCIALES;
import procesos.DESCARGARDATOS;
import procesos.MENSAJEEXCEPCIONES;
import procesos.UbicacionCliente;
import procesos.VARIOS;

@SuppressLint("NewApi")
public class MainPedidos extends Activity {
	private ArrayList<DetallesPedidos> alDetallesPed = new ArrayList<DetallesPedidos>();
	private ListView listaDetPed;
	private DetallesPedidosAdapter dpAdapter;
	double precioItem,subtotalPed,descuentoPed,ivaPed,netoPed,subtotalItem,descuentoItem,ivaItem,netoItem,pordes,poruti,preciominimoeditado,costop;
	int totalUnidadesItem,factorItem,stockItem,cajas,unidades,secPedido,numPedido,posicion,valorIva,numlineaped;
	boolean agrega;
	String usuario = "";
	String nombreUsuario = "";
	String tipoUsuario = "";
	String detallesJson = "";
	String precioCajasUnidades = "N";
	String codigoCliente, nombreCliente, negocioCliente,tipoCliente,codigoItem,nombreItem,aplicaIvaItem,tipoItem,insertSQL, cedulaCliente;
	String fecha,hora,scriptSQL,txtObservacion,creaDescuento,editaPrecio,esautoventa,escambio,esnotaventa;
	EditText ENombreCliente,ECodigoCliente,ECajas,EUnidades,EItem,EPordes,EEditaPrecio;
	TextView TSubtotal,TDescuento,TIva,TNeto,TVendedor,TPordes;
	CheckBox chkEsCambio;
	CheckBox chkNotaventa;
	Bundle bundle;
	Intent intent;
	Gson gson;
	Type type;
	BDTablas dbcon;
	SQLiteDatabase db;
	Cursor c;
	Button btnAcepta;
	Pedidos pedido;
	CONDICIONESCOMERCIALES cc;
	VARIOS varios;
	Dialogos mensaje = new Dialogos();
	MENSAJEEXCEPCIONES mex = new MENSAJEEXCEPCIONES();
	DecimalFormat df;
	AlertDialog.Builder builder;
	AlertDialog alert;
	Constantes constante;
	String rutaArchivo="";
	String linea="";
	String impresora="BlueTooth Printer";
	Cursor TotCondicion;
	String TipoDoc="";
	String estaMarcado="N";
	DESCARGARDATOS descarga;

	// android built in classes for bluetooth operations
	BluetoothAdapter mBluetoothAdapter;
	BluetoothSocket mmSocket;
	BluetoothDevice mmDevice;
	// needed for communication to bluetooth device / network
	OutputStream mmOutputStream;
	InputStream mmInputStream;
	Thread workerThread;
	//
	byte[] readBuffer;
	int readBufferPosition;
	volatile boolean stopWorker;

	UbicacionCliente ubicacion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_pedidos);
		ubicacion = new UbicacionCliente(this);

		this.inicializaVariables();
		this.incializaVariablesDeControles();
		
		getActionBar().setTitle("Creación de Pedidos");
		this.ocultaTeclado();
		creaDescuento="S";
		poruti=0;
		preciominimoeditado=0;
		costop=0;
		agrega = true;
		constante = new Constantes();
		pedido = new Pedidos();
		
		//Tomo los datos enviado desde la otra actividad
		usuario = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");
		txtObservacion = getIntent().getStringExtra("observacionPed");
		
		codigoCliente = getIntent().getStringExtra("codigoCli");
		nombreCliente = getIntent().getStringExtra("nombreCli");
		negocioCliente = getIntent().getStringExtra("negocioCli");
		tipoCliente = getIntent().getStringExtra("tipoCli");
		cedulaCliente = getIntent().getStringExtra("cedC");

		codigoItem = getIntent().getStringExtra("codigoItem");
/*
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ubicacion.obtenerUbicacion();
			}
		});
*/
		if(codigoCliente != null)
		{
			ENombreCliente.setText(nombreCliente);
			ECodigoCliente.setText(codigoCliente);
			ENombreCliente.setTextColor(Color.parseColor("#3A3A3A"));
			
			//Recibo los detalles de los pedidos en un objeto Json y lo paso al ArrayList
			bundle = this.getIntent().getExtras();
			gson = new Gson();
			String historial = bundle.getString("datosDetalles");
			type = new TypeToken<ArrayList<DetallesPedidos>>(){}.getType();
			alDetallesPed = gson.fromJson(historial, type);
		}
		
		if(codigoItem != null)
		{
			nombreItem = getIntent().getStringExtra("nombreItem");
			factorItem = Integer.parseInt(getIntent().getStringExtra("factorItem"));
			stockItem = Integer.parseInt(getIntent().getStringExtra("stockItem"));
			precioItem = Double.parseDouble(getIntent().getStringExtra("precioItem"));
			aplicaIvaItem = getIntent().getStringExtra("aplicaIvaItem");
			tipoItem = getIntent().getStringExtra("tipoItem");
			poruti = Double.parseDouble(getIntent().getStringExtra("porutiItem"));
			costop = Double.parseDouble(getIntent().getStringExtra("costopItem"));
			
			//Recibo los detalles de los pedidos en un objeto Json y lo paso al ArrayList
			bundle = this.getIntent().getExtras();
			gson = new Gson();
			String historial = bundle.getString("datosDetalles");
			type = new TypeToken<ArrayList<DetallesPedidos>>(){}.getType();
			alDetallesPed = gson.fromJson(historial, type);
			
			EItem.setText(nombreItem);
			EEditaPrecio.setText(String.valueOf(precioItem));
			EItem.setTextColor(Color.parseColor("#3A3A3A"));
		}
		
		if(alDetallesPed.size()>0)
		{
			actualizaListViewDetalles();
		}
		
		try{
			dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
			db = dbcon.getWritableDatabase();
		}catch(Exception e){
			mensaje.miDialogoToastLargo(this, "Excepción generada al conectarse a la Base de datos: Clase: MainPedidos --> " + e.getMessage());
			return;
		}
		
		varios = new VARIOS(getApplicationContext(), db);
		valorIva = varios.getValorIva();
		impresora = varios.getNombreImpresora();
		
		//Valida si el vendedor puede agregar descuento
		scriptSQL = "SELECT coloca_descuento FROM usuario where codigo='"+ usuario +"'";
		c = db.rawQuery(scriptSQL, null);
		if(c.moveToFirst())
		{
			creaDescuento = c.getString(0);
		}
		if(creaDescuento == null)
		{
			creaDescuento = "N";
		}
		
		if(creaDescuento.equals("S"))
		{
			TPordes.setVisibility(View.VISIBLE);
			EPordes.setVisibility(View.VISIBLE);
		}
		else
		{
			TPordes.setVisibility(View.GONE);
			EPordes.setVisibility(View.GONE);
		}
		
		//Valida si el vendedor puede cambiar precios
		scriptSQL = "SELECT edita_precio FROM usuario where codigo='"+ usuario +"'";
		c = db.rawQuery(scriptSQL, null);
		if(c.moveToFirst())
		{
			editaPrecio = c.getString(0);
		}
		if(editaPrecio == null)
		{
			editaPrecio = "N";
		}
		
		if(editaPrecio.equals("S"))
		{
			//EEditaPrecio.setVisibility(View.VISIBLE);
			EEditaPrecio.setEnabled(true);
		}
		else
		{
			//EEditaPrecio.setVisibility(View.GONE);
			EEditaPrecio.setEnabled(false);
		}
		
		//Valida si el vendedor es de autoventa
		scriptSQL = "SELECT es_autoventa FROM usuario where codigo='"+ usuario +"'";
		c = db.rawQuery(scriptSQL, null);
		if(c.moveToFirst())
		{
			esautoventa = c.getString(0);
		}
		if(editaPrecio == null)
		{
			esautoventa = "N";
		}
		
		if(esautoventa.equals("S"))
		{
			chkEsCambio.setEnabled(true);
		}
		else
		{
			chkEsCambio.setEnabled(false);
		}
		
		//Busca si hay pedidos como nota de venta
		scriptSQL = "SELECT ESNOTAVENTA FROM parametros";
		c = db.rawQuery(scriptSQL, null);
		if(c.moveToFirst())
		{
			esnotaventa = c.getString(0);
		}
		if(esnotaventa.equals("S"))
		{
			chkNotaventa.setEnabled(true);
		}
		else
		{
			chkNotaventa.setEnabled(false);
		}
		
		//Valida el n�mero de l�neas que puede tener los detalles del pedido
		scriptSQL = "SELECT num_lineas_ped FROM parametros";
		c = db.rawQuery(scriptSQL, null);
		if(c.moveToFirst())
		{
			if(String.valueOf(c.getInt(0))==null || String.valueOf(c.getInt(0)) .equals("") || Integer.parseInt(c.getString(0))==0)
			{
				numlineaped = 10000;
			}
			else
			{
				numlineaped = c.getInt(0);
			}
		}
		else
		{
			numlineaped = 10000;
		}
		
		precioCajasUnidades = varios.esPrecioCajasUnidades();
		
		//Implemento evento clickLargo del ListView
		listaDetPed.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int pos, long id) {
				posicion = pos;
				String[] opc = new String[] {"Eliminar Línea"};
				
				AlertDialog opciones = new AlertDialog.Builder(MainPedidos.this)
        				.setTitle("Opciones del detalle")
        				.setItems(opc,
        						new DialogInterface.OnClickListener() {
        							public void onClick(DialogInterface dialog,int selected) {
        								try{
        									switch(selected)
        									{
        										case 0:
        											eliminaDetalle(posicion);
        											break;
        									}
        								}catch(Exception e){
        									mensaje.miDialogoToastLargo(getApplicationContext(), e.getMessage());
        								}
        							}
        						}).create();
				opciones.show();
				return true;
			}
		});
		
		TVendedor.setText("Vendedor: " + usuario + " - " + nombreUsuario);
	}
	
	public void inicializaVariables()
	{
		aplicaIvaItem="N";
		tipoItem="B";
		factorItem=0;
		stockItem=0;
		precioItem=0;
		subtotalPed=0;
		descuentoPed=0;
		ivaPed=0;
		netoPed=0;
		txtObservacion="";
	}
	
	public void incializaVariablesDeControles()
	{
		TSubtotal = (TextView)findViewById(R.id.txtSubtotal);
		TDescuento = (TextView)findViewById(R.id.txtDescuento);
		TIva = (TextView)findViewById(R.id.txtTotalIva);
		TNeto = (TextView)findViewById(R.id.txtNeto);
		TVendedor = (TextView)findViewById(R.id.txtVendedor);
		TPordes = (TextView)findViewById(R.id.lblPorDes);
		listaDetPed = (ListView)findViewById(R.id.lvDetallesPedidos);
		ENombreCliente = (EditText)findViewById(R.id.txtCliente);
		ECodigoCliente = (EditText)findViewById(R.id.txtCodigoCliente);
		ECajas = (EditText)findViewById(R.id.txtCajas);
		EUnidades = (EditText)findViewById(R.id.txtUnidades);
		EItem = (EditText)findViewById(R.id.txtItem);
		EPordes = (EditText)findViewById(R.id.txtPorDes);
		EEditaPrecio = (EditText)findViewById(R.id.txtEditaPrecio);
		chkEsCambio = (CheckBox)findViewById(R.id.chkEsCambio);
		chkNotaventa = (CheckBox)findViewById(R.id.chkNotaventa);
	}
	
	public void buscaClientes(View v)
	{
		gson = new Gson();
		detallesJson = gson.toJson(alDetallesPed);
		
		intent = new Intent(this,MainVerClientes.class);
		intent.putExtra("codigoC", this.codigoCliente);
		intent.putExtra("nombreC", this.nombreCliente);
		intent.putExtra("negocioC", this.negocioCliente);
		intent.putExtra("tipoC", this.tipoCliente);
		intent.putExtra("observacionPed", this.txtObservacion);

		intent.putExtra("cedC", this.cedulaCliente);
		intent.putExtra("user", this.usuario);
		intent.putExtra("nombreUser", this.nombreUsuario);
		intent.putExtra("tipoUser", tipoUsuario);
		intent.putExtra("datosDetalles", detallesJson);
		startActivity(intent);
	}
	
	public void buscaItem(View v)
	{
		if(this.codigoCliente==null || this.codigoCliente.equals(""))
		{
			mensaje.miDialogoToastLargo(getApplicationContext(),"Seleccione primero un cliente");
		}
		else
		{
			gson = new Gson();
			detallesJson = gson.toJson(alDetallesPed);
			
			intent = new Intent(this,MainVerItem.class);
			intent.putExtra("codigoC", this.codigoCliente);
			intent.putExtra("nombreC", this.nombreCliente);
			intent.putExtra("negocioC", this.negocioCliente);
			intent.putExtra("tipoC", this.tipoCliente);
			intent.putExtra("observacionPed", this.txtObservacion);
            intent.putExtra("cedC", this.cedulaCliente);
			intent.putExtra("user", this.usuario);
			intent.putExtra("nombreUser", this.nombreUsuario);
			intent.putExtra("tipoUser", tipoUsuario);
			intent.putExtra("esautoventa", esautoventa);
			intent.putExtra("datosDetalles", detallesJson);
			startActivity(intent);
		}
	}
	
	public void eliminaDetalle(int pos)
	{
		subtotalPed = subtotalPed - alDetallesPed.get(pos).getSubtotal();
		descuentoPed = descuentoPed - alDetallesPed.get(pos).getDescuento();
		ivaPed = ivaPed - alDetallesPed.get(pos).getIva();
		netoPed = netoPed - alDetallesPed.get(pos).getNeto();
		
		alDetallesPed.remove(pos);
		dpAdapter = new DetallesPedidosAdapter(this, alDetallesPed);
		listaDetPed.setAdapter(dpAdapter);
		dpAdapter.notifyDataSetChanged();
		
		this.calculaValores(subtotalPed, descuentoPed, ivaPed, netoPed);
	}
	
	public void agregar()
	{
		if(chkNotaventa.isChecked())
		{
			estaMarcado="S";
		}
		mensaje.miDialogoToastCorto(getApplicationContext(),"total lineas de pedidos " + numlineaped + " detalles");
		//Toast.makeText(getApplicationContext(), "aqui", Toast.LENGTH_SHORT).show();
		double totalafacturar=0;
		if(alDetallesPed.size()==numlineaped)
		{
			mensaje.miDialogoToastCorto(getApplicationContext(),"Solo puede tener hasta " + numlineaped + " detalles");
		}
		else
		{
			if(codigoItem!=null)
			{
				if(ECajas.getText().toString().isEmpty() && EUnidades.getText().toString().isEmpty())
				{
					mensaje.miDialogoToastCorto(getApplicationContext(),"Por favor ingrese un número de cajas o unidades");
				}
				else
				{
					if(editaPrecio.equals("S"))
					{
						precioItem = Double.parseDouble(EEditaPrecio.getText().toString().trim());
						preciominimoeditado = this.calculaPrecioMinimoEditado(costop, poruti);
					}
					else
					{
						preciominimoeditado = precioItem;
					}
					
					if(precioItem<preciominimoeditado)
					{
						mensaje.miDialogoToastCorto(getApplicationContext(),"El precio editado no puede ser menor al costo mínimo");
					}
					else
					{
						if(ECajas.getText().toString().isEmpty())	 	ECajas.setText("0");
						if(EUnidades.getText().toString().isEmpty())	EUnidades.setText("0");
						if(EPordes.getText().toString().isEmpty())		EPordes.setText("0");
						
						cajas = Integer.parseInt(ECajas.getText().toString());
						unidades = Integer.parseInt(EUnidades.getText().toString());
						pordes = Double.parseDouble(EPordes.getText().toString());
						
						subtotalPed = 0;
						ivaPed = 0;
						netoPed = 0;
						descuentoPed = 0;
						
						if(chkEsCambio.isChecked())
						{
							escambio="S";
							precioItem=0;
						}
						else
							escambio="N";
						
						if(esautoventa.equals("N"))
							agrega=true;
						else
						{
							totalafacturar = (factorItem * cajas) + unidades;
							if(totalafacturar > stockItem)
								agrega=false;
							else
								agrega=true;
						}
						
						if(agrega)
						{
							pedido.agregarDetalle(db,codigoItem,nombreItem,cajas,unidades,pordes,precioItem,factorItem,stockItem,aplicaIvaItem,tipoItem,usuario,alDetallesPed,valorIva,escambio,precioCajasUnidades);
							actualizaListViewDetalles();
							EItem.setText("");
							ECajas.setText("");
							EUnidades.setText("");
							EPordes.setText("");
							EEditaPrecio.setText("");
							codigoItem = null;
							chkEsCambio.setChecked(false);
							this.ocultaTeclado();
						}
						else
						{
							Toast.makeText(getApplicationContext(), "No tiene stock suficiente para el pedido", Toast.LENGTH_LONG).show();
						}
						agrega = true;
					}
				}
			}
			else
			{
				mensaje.miDialogoToastCorto(getApplicationContext(),"Por favor primero seleccione un producto");
			}
		}
		if(estaMarcado.equals("S"))
		{
			chkNotaventa.setChecked(true);
		}
	}
	
	public void grabarPedido()
	{
		if(codigoCliente==null)
		{
			mensaje.miDialogoToastCorto(getApplicationContext(),"Debe seleccionar un cliente");
		}
		else if(alDetallesPed==null || alDetallesPed.size()==0)
		{
			mensaje.miDialogoToastCorto(getApplicationContext(),"No hay pedido para grabar");
		}
		else
		{

			EItem.setText("");
			ECajas.setText("");
			EUnidades.setText("");
			db.beginTransaction();
			try{
				if(chkNotaventa.isChecked())
				{
				  TipoDoc="NVT";	
				}
				else
				{
				  TipoDoc="FAC";
				}
				cc = new CONDICIONESCOMERCIALES(db);			
				alDetallesPed = cc.getCondicion(getApplicationContext(), codigoCliente, alDetallesPed,valorIva);
				if(txtObservacion==null)
					txtObservacion = "";

				mex = pedido.grabarPedido(db, usuario, codigoCliente, txtObservacion, alDetallesPed,getApplicationContext(),
						esautoventa,TipoDoc,ubicacion.get_latitud(),ubicacion.get_longitud());




				if(mex.getEstado())
				{
					scriptSQL="update usuario_secuenciales set secuencial = secuencial+1 where codigo='"+ usuario +"'";
					db.execSQL(scriptSQL);
					
					scriptSQL="update parametrobodega set nopedido=nopedido+1";
					db.execSQL(scriptSQL);
					
					mensaje.miDialogoToastCorto(getApplicationContext(),"Se realizó el pedido No. "+ mex.getNumPed() +" con éxito");
					db.setTransactionSuccessful();
					//Limpio al arraylist de los detalles y actualizo el listview para que limpie los datos
					alDetallesPed.clear();
					dpAdapter = new DetallesPedidosAdapter(this, alDetallesPed);
					listaDetPed.setAdapter(dpAdapter);
					dpAdapter.notifyDataSetChanged();
					limpiaTodosControles();
					creaReporte(usuario,mex.getNumPed());

					/*descarga = new DESCARGARDATOS(getApplicationContext());
					descarga.descargarTodo(db);
					descarga.descargarClientes(db);*/
				}
				else
				{
					mensaje.miDialogoToastCorto(getApplicationContext(),mex.getMensaje());
				}
			}catch(Exception e){
				mensaje.miDialogoToastLargo(getApplicationContext(), mex.formaMensajeExcepcion("Se produjo la excepción al guardar el pedido", "MainPedidos", "grabarPedido()", e.getMessage()));
			}finally{
				db.endTransaction();
				db.close();
			}
		}
	}
	
	private void  actualizaListViewDetalles()
	{
		for(int i=0; i<alDetallesPed.size(); i++)
		{
			subtotalPed = subtotalPed + alDetallesPed.get(i).getSubtotal();
			descuentoPed = descuentoPed + alDetallesPed.get(i).getDescuento();
			ivaPed = ivaPed + alDetallesPed.get(i).getIva();
			netoPed = netoPed + alDetallesPed.get(i).getNeto();
			
			dpAdapter = new DetallesPedidosAdapter(this, alDetallesPed);
			listaDetPed.setAdapter(dpAdapter);
			dpAdapter.notifyDataSetChanged();
		}
		this.calculaValores(subtotalPed, descuentoPed, ivaPed, netoPed);
	}
	
	private void calculaValores(double subtotal,double descuento,double iva,double neto)
	{
		df= new DecimalFormat("#0.00"); //esto sirve para mostrar solo dos decimales
		TSubtotal.setText(String.valueOf(df.format(subtotal)));
		TDescuento.setText(String.valueOf(df.format(descuento)));
		TIva.setText(String.valueOf(df.format(iva)));
		TNeto.setText(String.valueOf(df.format(neto)));
	}
	
	private double calculaPrecioMinimoEditado(double costopItem,double porutilidad)
	{
		double preciomin=0;
		double valorporcentaje=0;
		valorporcentaje = (costopItem * porutilidad) / 100;
		preciomin = costopItem + valorporcentaje;
		return preciomin;
	}
	
	private void limpiaTodosControles()
	{
		ECajas.setText("");
		EUnidades.setText("");
		EPordes.setText("");
		EItem.setText("");
		ENombreCliente.setText("");
		ECodigoCliente.setText("");
		EEditaPrecio.setText("");
		TSubtotal.setText("0.00");
		TDescuento.setText("0.00");
		TIva.setText("0.00");
		TNeto.setText("0.00");
		subtotalPed=0;
		ivaPed=0;
		netoPed=0;
		codigoCliente="";
		poruti=0;
		costop=0;
		chkEsCambio.setChecked(false);
		chkNotaventa.setChecked(false);
	}
	
	public void ocultaTeclado()
	{
		//Este funciona cada vez que inicia la actividad
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		//Lineas para ocultar el teclado virtual (Hide keyboard) despues de alguna accion
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(ENombreCliente.getWindowToken(), 0);
	}

	//Este metodo es para inflar el menu y mostrar las opciones del menu en al action bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_pedidos, menu);
		return true;
	}

	//este metodo sirve para controlar las acciones de cada opci�n del men�
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case R.id.accion_agregar:
				this.agregar();
				return true;
			case R.id.accion_guardar:
				this.grabarPedido();
				return true;
			case R.id.accion_observacion:
				final Dialog dialog = new Dialog(this);
				dialog.setContentView(R.layout.layout_observacion);
				dialog.setTitle("Mi titulo");
				btnAcepta = (Button)dialog.findViewById(R.id.btnAcepta);
				btnAcepta.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						EditText edit=(EditText)dialog.findViewById(R.id.txtObservacion);
						txtObservacion=edit.getText().toString();
		                dialog.dismiss(); //Para que el dialogo se cierre y no consuma memoria
					}
				});
				dialog.show();
				return true;
			case R.id.accion_imprimir:
				this.imprimir();
				return true;
			case R.id.cerrar_sesion:
				/*
				finishAffinity();
				intent = new Intent(this,MainLogin.class);
				startActivity(intent);
				*/
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	//Este m�todo se ejecuta cuando se da clic en el bot�n de ir hacia atr�s
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	// Esto es para cuando de clic en el bo�n de atr�s no regrese a la pantalla de login
            if(alDetallesPed.size()>0)
            {
            	builder = new Builder(MainPedidos.this);
				builder.setMessage("El pedido esta pendiente grabar. ¿Está seguro que desea salir?. Si sale el pedido no se podrá grabar")
						.setTitle("Edición de pedidos")
						.setPositiveButton("Si", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
				            	Intent intent = new Intent(getApplicationContext(),MainMenuPedido.class);
								intent.putExtra("user", usuario);
								intent.putExtra("nombreUser", nombreUsuario);
								intent.putExtra("tipoUser", tipoUsuario);
								startActivity(intent);
							}
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//
							}
						});
				alert = builder.create();
				alert.show();
            	
            	
            	mensaje.miDialogoToastLargo(getApplicationContext(), "Tiene pedido pendiente de grabar");
            	return true;
            }
            else
            {
            	finish();
            	Intent intent = new Intent(this,MainMenuPedido.class);
				intent.putExtra("user", usuario);
				intent.putExtra("nombreUser", nombreUsuario);
				intent.putExtra("tipoUser", tipoUsuario);
				startActivity(intent);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
	
	public void imprimir()
	{
		try {
			//mensaje.miDialogoToastCorto(getApplicationContext(), "Comienza Impresión");
			findBT();

            //openBT();
            sendData();
            closeBT();
			//mensaje.miDialogoToastCorto(getApplicationContext(), "Termina impresión");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	public void creaReporte(String vendedor, int numpedido)
	{
		try {
        	c = db.rawQuery("SELECT c.nombre,p.*,c.clavefe FROM cabecerapedidos p,cliente c where p.numero='"+numpedido+"' and p.usuario='"+vendedor+"' and p.cliente=c.codigo", null);
        	Cursor c1;
        	DecimalFormat df;
        	df= new DecimalFormat("#0.00");
            double subtpedi,ivapedi,despedi,netopedi;
            String codigocli,clavefecli,paginaweb,empresa;
            int lineapedido = 0;
            subtpedi=0;
            ivapedi=0;
            despedi=0;
            netopedi=0;
            codigocli="";
            clavefecli="";
            
            empresa = varios.getNombreCia();
            paginaweb = varios.getPaginaWeb();
            
            if(c.moveToFirst())
			{
            	linea = linea + empresa +"\n";
            	linea = linea + "Pedido # "+numpedido+"\n";
            	linea = linea + "Cliente: "+c.getString(0)+"\n";
            	
            	codigocli = c.getString(6);
            	clavefecli = c.getString(21);
            	subtpedi = c.getDouble(8);
            	despedi = c.getDouble(9);
            	ivapedi = c.getDouble(10);
            	netopedi = c.getDouble(11);

            	c1 = db.rawQuery("SELECT * FROM DETALLESPEDIDOS where secuencial='"+c.getInt(5)+"' and usuario='"+vendedor+"'", null);
                if(c1.moveToFirst())
    			{
                	linea = linea + "\n";
                	linea = linea + "Detalles Pedido # "+numpedido+"\n";
                	linea = linea + "Nombre - Cant - Precio - Neto";
                	linea = linea + "\n";
                	
                	do{
                		linea = linea + (lineapedido + 1) +") "+ c1.getString(3).trim() + "-\n";
                		linea = linea + c1.getInt(6)+" - $"+String.valueOf(df.format(c1.getDouble(7)))+" - $"+String.valueOf(df.format(c1.getDouble(12)));
                		linea = linea + "\n";
                		linea = linea + "-------------------------------";
                		linea = linea + "\n";
                		lineapedido = lineapedido +1;
                	}while(c1.moveToNext());
    			}
			}
            linea = linea + "\n";
            linea = linea + "Subtotal: $"+ String.valueOf(df.format(subtpedi));
            linea = linea + "\n";
            linea = linea + "Descuento: $"+ String.valueOf(df.format(despedi));
            linea = linea + "\n";
            linea = linea + "IVA: $"+ String.valueOf(df.format(ivapedi));
            linea = linea + "\n";
            linea = linea + "Neto: $"+ String.valueOf(df.format(netopedi));

            linea = linea + "\n\n";
            linea = linea + "Descargue su factura electronica despues de 24 horas en:";
            linea = linea + "\n";
            linea = linea + paginaweb;
            linea = linea + "\n";
            linea = linea + "Usuario: " + codigocli;
            linea = linea + "\n";
            linea = linea + "Clave: " + clavefecli;
            linea = linea + "\n\n";
            linea = linea + "Vendedor: " + vendedor;
            linea = linea + "\n";
            linea = linea + nombreUsuario;
            linea = linea + "\n\n\n";
        } catch (Exception ioe) {
            Toast.makeText(this, "Error al momento de generar el reporte",
                    Toast.LENGTH_SHORT).show();
        }
	}
	
	// this will find a bluetooth printer device
	public	void findBT()
	{
		try {




			mensaje.miDialogoToastCorto(getApplicationContext(), "Impresora Bluetooth: "+ impresora.trim());
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		    if(mBluetoothAdapter == null) {
		    	mensaje.miDialogoToastCorto(getApplicationContext(), "No hay ningún adaptador bluetooth disponible");
		    }
		 
		    if(!mBluetoothAdapter.isEnabled()) {
		    	Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		        startActivityForResult(enableBluetooth, 0);
		    }

		     Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		 
		     if(pairedDevices.size() > 0) {
		    	 for (BluetoothDevice device : pairedDevices) {

					 String deviceAlias = device.getName();
					 try {
						 Method method = device.getClass().getMethod("getAliasName");
						 if(method != null) {
							 deviceAlias = (String)method.invoke(device);
							 if(deviceAlias.equals(impresora.trim())){
								 mmDevice = device;
								 Toast.makeText(getApplicationContext(),"Conectado a Impresora"+device.getName(),Toast.LENGTH_LONG).show();
								 openBT();

								 break;
							 }
						 }
					 } catch (NoSuchMethodException e) {
						 e.printStackTrace();
					 } catch (InvocationTargetException e) {
						 e.printStackTrace();
					 } catch (IllegalAccessException e) {
						 e.printStackTrace();
					 }

		    		 // RPP300 is the name of the bluetooth printer device
		             // we got this name from the list of paired devices
					// String nameD = device.getName().compareTo(impresora);


		         }
		     }
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static int numeroRandom(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}
	
	// tries to open a connection to the bluetooth printer device
	void openBT() throws IOException {
	    try {
	        // Standard SerialPortService ID
	        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
	        mmSocket = null;
	        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
	        //mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
			Thread.sleep(numeroRandom(1000, 5000));
			mmSocket.connect();
	        mmOutputStream = mmSocket.getOutputStream();
	        mmInputStream = mmSocket.getInputStream();
	        beginListenForData();
	        mensaje.miDialogoToastCorto(getApplicationContext(), "Bluetooth conectado");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	 /* after opening a connection to bluetooth printer device,
	 * we have to listen and check if a data were sent to be printed.*/
	void beginListenForData() {
	    try {
	        final Handler handler = new Handler();
	         
	        // this is the ASCII code for a newline character
	        final byte delimiter = 10;
		        stopWorker = false;
	        readBufferPosition = 0;
	        readBuffer = new byte[1024];
	         
	        workerThread = new Thread(new Runnable() {
	            public void run() {
	                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
	                    try {
	                        int bytesAvailable = mmInputStream.available();
	                        if (bytesAvailable > 0) {
	                            byte[] packetBytes = new byte[bytesAvailable];
	                            mmInputStream.read(packetBytes);
	 
	                            for (int i = 0; i < bytesAvailable; i++) {
	                                byte b = packetBytes[i];
	                                if (b == delimiter) {
	 
	                                    byte[] encodedBytes = new byte[readBufferPosition];
	                                    System.arraycopy(
	                                        readBuffer, 0,
	                                        encodedBytes, 0,
	                                        encodedBytes.length
	                                    );
	                                    // specify US-ASCII encoding
	                                    @SuppressWarnings("unused")
										final String data = new String(encodedBytes, "US-ASCII");
	                                    readBufferPosition = 0;
	 
	                                    // tell the user data were sent to bluetooth printer device
	                                    handler.post(new Runnable() {
	                                        public void run() {
	                                            //myLabel.setText(data);
	                                        }
	                                    });
	                                } else {
	                                    readBuffer[readBufferPosition++] = b;
	                                }
	                            }
	                        }
	                    } catch (IOException ex) {
	                        stopWorker = true;
	                    }
	                }
	            }
	        });
	        workerThread.start();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	// this will send text data to be printed by the bluetooth printer
	void sendData() throws IOException {
	    try {
	        mmOutputStream.write(linea.getBytes());
	        mensaje.miDialogoToastCorto(getApplicationContext(), "Imprimiendo recibo");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	// close the connection to bluetooth printer.
	void closeBT() throws IOException {
	    try {
	        stopWorker = true;
	        mmOutputStream.close();
	        mmInputStream.close();
	        mmSocket.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}