package com.birobidsa.admmovil.com;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import clasesAdapter.DetallesPedidosAdapter;
import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.DetallesPedidos;
import misclases.Pedidos;
import procesos.CONDICIONESCOMERCIALES;
import procesos.MENSAJEEXCEPCIONES;
import procesos.VARIOS;

public class MainEdicionPedidos extends Activity {
	int secpedido,numpedido,valorIva,posicion,factorItem,stockItem,cajas,unidades,numlineaped;
	double precioItem,subtotalPed,descuentoPed,ivaPed,netoPed,subtotalItem,descuentoItem,ivaItem,netoItem,pordes,poruti,preciominimoeditado,costop;;
	String usuario,nombreUsuario,tipoUsuario,scriptSQL,cliente,nomcliente,tipocliente,negociocliente,observacion;
	String codigoItem,nombreItem,aplicaIvaItem,tipoItem,creaDescuento,editaPrecio,escambio,esautoventa,esnotaventa;
	String precioCajasUnidades = "N";
	DetallesPedidosAdapter dpAdapter;
	EditText ENombreCliente,ECodigoCliente,ECajas,EUnidades,EItem,EPordes,EEditaPrecio;
	TextView TSubtotal,TDescuento,TIva,TNeto,TVendedor,TPordes;
	CheckBox chkEsCambio;
	CheckBox chkNotaventa;
	BDTablas dbcon;
	SQLiteDatabase db;
	Cursor c;
	Constantes constante;
	Dialogos mensaje = new Dialogos();
	ListView listaDetPed;
	ArrayList<DetallesPedidos> alDetallesPed = new ArrayList<DetallesPedidos>();
	Pedidos pedido;
	VARIOS varios;
	DecimalFormat df;
	Intent intent;
	Gson gson;
	String detallesJson = "";
	AlertDialog.Builder builder;
	AlertDialog alert;
	Button btnAcepta;
	Bundle bundle;
	Type type;
	MENSAJEEXCEPCIONES mex = new MENSAJEEXCEPCIONES();
	CONDICIONESCOMERCIALES cc;
	String TipoDoc="";
	String docfacPed="FAC";
	String estaMarcado="N";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_edicion_pedidos);
		
		this.inicializaVariables();
		this.incializaVariablesDeControles();
		getActionBar().setTitle("Modificación del pedido " + numpedido);
		this.ocultaTeclado();
		creaDescuento="S";
		poruti=0;
		preciominimoeditado=0;
		costop=0;
		constante = new Constantes();
		pedido = new Pedidos();
		
		usuario = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");		
		observacion = getIntent().getStringExtra("observacionPedido");
		docfacPed = getIntent().getStringExtra("esDoc");
		
		cliente = getIntent().getStringExtra("clientePedido");
		nomcliente = getIntent().getStringExtra("nombreClientePedido");
		negociocliente = getIntent().getStringExtra("negocioCli");
		tipocliente = getIntent().getStringExtra("tipoCli");
		
		codigoItem = getIntent().getStringExtra("codigoItem");
		
		observacion = getIntent().getStringExtra("observacionPed");
		secpedido = Integer.parseInt(getIntent().getStringExtra("secPedido"));
		numpedido = Integer.parseInt(getIntent().getStringExtra("numPedido"));
		
		if(cliente != null)
		{
			ENombreCliente.setText(nomcliente);
			ECodigoCliente.setText(cliente);
			ENombreCliente.setTextColor(Color.parseColor("#3A3A3A"));
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
			mensaje.miDialogoToastLargo(this, "Excepción generada al conectarse a la Base de datos: Clase: MainEdicionPedidos --> " + e.getMessage());
			return;
		}
		
		varios = new VARIOS(getApplicationContext(), db);
		valorIva = varios.getValorIva();
		
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
			EEditaPrecio.setVisibility(View.VISIBLE);
			EEditaPrecio.setEnabled(true);
		}
		else
		{
			EEditaPrecio.setVisibility(View.GONE);
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
		
		if(alDetallesPed.size()==0 || alDetallesPed == null)
		{
			scriptSQL="SELECT D.*,I.factor,I.stock,I.iva aplicaiva,I.bien tipoitem FROM DETALLESPEDIDOS D, ITEM I where D.item=I.item and D.forma_venta='V' and D.secuencial='"+ secpedido +"' and D.usuario='"+ usuario +"'";
			c = db.rawQuery(scriptSQL, null);
			if(c.moveToFirst())
			{
				do{
					pedido.agregarDetalle(db,c.getString(2),c.getString(3),c.getInt(4),c.getInt(5),c.getInt(9),c.getDouble(7),c.getInt(20),c.getInt(21),c.getString(22),c.getString(23),usuario,alDetallesPed,valorIva,c.getString(19),precioCajasUnidades);
					actualizaListViewDetalles();
				}while(c.moveToNext());
				scriptSQL = "select tipo_cliente,negocio from cliente where codigo='"+ cliente +"'";
				c = db.rawQuery(scriptSQL, null);
				if(c.moveToFirst())
				{
					tipocliente = c.getString(0);
					negociocliente = c.getString(1);
				}
			}
		}
		
		//Implemento evento clickLargo del ListView
		listaDetPed.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int pos, long id) {
				posicion = pos;
				String[] opc = new String[] {"Eliminar Línea"};
						
				AlertDialog opciones = new AlertDialog.Builder(MainEdicionPedidos.this)
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
		if(docfacPed!=null)
		{
			if(docfacPed.equals("NVT"))
			{
				chkNotaventa.setChecked(true);			
			}
			else
			{
				chkNotaventa.setChecked(false);
			}
		}		
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
		observacion="";
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
	
	public void buscaItem(View v)
	{
		gson = new Gson();
		detallesJson = gson.toJson(alDetallesPed);
		
		intent = new Intent(this,MainVerItemEdicionPed.class);
		intent.putExtra("codigoC", this.cliente);
		intent.putExtra("nombreC", this.nomcliente);
		intent.putExtra("negocioC", this.negociocliente);
		intent.putExtra("tipoC", this.tipocliente);
		intent.putExtra("observacionPed", this.observacion);
		intent.putExtra("user", this.usuario);
		intent.putExtra("nombreUser", this.nombreUsuario);
		intent.putExtra("tipoUser", tipoUsuario);
		intent.putExtra("secPedido", String.valueOf(secpedido));
		intent.putExtra("numPedido", String.valueOf(numpedido));
		intent.putExtra("esautoventa", esautoventa);
		intent.putExtra("datosDetalles", detallesJson);
		startActivity(intent);
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
					precioItem = Double.parseDouble(EEditaPrecio.getText().toString().trim());
					preciominimoeditado = this.calculaPrecioMinimoEditado(costop, poruti);
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
						pordes = Integer.parseInt(EPordes.getText().toString());
						
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
						
						pedido.agregarDetalle(db,codigoItem,nombreItem,cajas,unidades,pordes,precioItem,factorItem,stockItem,aplicaIvaItem,tipoItem,usuario,alDetallesPed,valorIva,escambio,precioCajasUnidades);
						actualizaListViewDetalles();
						EItem.setText("");
						ECajas.setText("");
						EUnidades.setText("");
						EPordes.setText("");
						EEditaPrecio.setText("");
						codigoItem = null;
						this.ocultaTeclado();
					}
				}
			}
			else
			{
				mensaje.miDialogoToastCorto(getApplicationContext(),"Por favor primero seleccione un producto");
			}
		}
	}
	
	public void modificarPedido()
	{
		if(cliente==null)
		{
			mensaje.miDialogoToastCorto(getApplicationContext(),"Debe seleccionar un cliente");
		}
		else if(alDetallesPed==null || alDetallesPed.size()==0)
		{
			mensaje.miDialogoToastCorto(getApplicationContext(),"No hay pedido para grabar");
		}
		else
		{
			db.beginTransaction();
			try{
				/*scriptSQL="delete from DETALLESPEDIDOS where secuencial='"+ secpedido +"'";
				db.execSQL(scriptSQL);*/
				cc = new CONDICIONESCOMERCIALES(db);
				
				alDetallesPed = cc.getCondicion(getApplicationContext(), cliente, alDetallesPed,valorIva);
				if(observacion==null)
					observacion = "";
				if(chkNotaventa.isChecked())
				{
				  TipoDoc="NVT";	
				}
				else
				{
				  TipoDoc="FAC";
				}

				mex = pedido.modificarPedido(db, usuario, cliente, observacion, alDetallesPed,getApplicationContext(),secpedido,numpedido,esautoventa,TipoDoc);
				if(mex.getEstado())
				{
					mensaje.miDialogoToastCorto(getApplicationContext(),"Se actualizó el pedido No "+ mex.getNumPed() +" con éxito");
					db.setTransactionSuccessful();
					
					//Limpio al arraylist de los detalles y actualizo el listview para que limpie los datos
					alDetallesPed.clear();
					dpAdapter = new DetallesPedidosAdapter(this, alDetallesPed);
					listaDetPed.setAdapter(dpAdapter);
					dpAdapter.notifyDataSetChanged();
					limpiaTodosControles();
					
					intent = new Intent(this,MainVerPedidos.class);
					intent.putExtra("user", this.usuario);
					intent.putExtra("nombreUser", this.nombreUsuario);
					intent.putExtra("tipoUser", tipoUsuario);
					startActivity(intent);
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
	
	private void actualizaListViewDetalles()
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
		poruti=0;
		costop=0;
	}
	
	public void ocultaTeclado()
	{
		//Este funciona cada vez que inicia la actividad
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		//Lineas para ocultar el teclado virtual (Hide keyboard) despues de alguna accion
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(ENombreCliente.getWindowToken(), 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_edicion_pedidos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case R.id.accion_agregar:
				this.agregar();
				return true;
			case R.id.accion_modificar:
				this.modificarPedido();
				return true;
			case R.id.accion_observacion:
				final Dialog dialog = new Dialog(this);
				dialog.setContentView(R.layout.layout_observacion);
				dialog.setTitle("Mi titulo");
				btnAcepta = (Button)dialog.findViewById(R.id.btnAcepta);
				btnAcepta.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						EditText edit=(EditText)dialog.findViewById(R.id.txtObservacion);
						observacion=edit.getText().toString();
		                dialog.dismiss(); //Para que el dialogo se cierre y no consuma memoria
					}
				});
				dialog.show();
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
            	builder = new Builder(MainEdicionPedidos.this);
				builder.setMessage("El pedido esta pendiente de modificar. ¿Está seguro que desea salir?. Si sale el pedido no se podrá modificar")
						.setTitle("Modificación de pedidos")
						.setPositiveButton("Si", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
				            	Intent intent = new Intent(getApplicationContext(),MainVerPedidos.class);
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
            	mensaje.miDialogoToastLargo(getApplicationContext(), "Tiene pedido pendiente de modificar");
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
}
