package com.birobidsa.admmovil.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import clasesAdapter.PedidosEnviadosAdapter;
import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.Pedidos;
import procesos.MENSAJEEXCEPCIONES;
import procesos.VARIOS;

@SuppressLint("NewApi")
public class MainVerPedidosEnviados extends Activity {
	int posicion, numpedido,secpedido,secautopedido,bodega;
	double subtotalpedido,descuentopedido,ivapedido,netopedido;
	String scriptSQL,codUser,nombreUsuario,tipoUsuario,tipo,cliente,nomcliente,vendedor,observacion,docfac;
	ArrayList<Pedidos> alPedido = new ArrayList<Pedidos>();
	Pedidos pedido = new Pedidos();
	MENSAJEEXCEPCIONES mex = new MENSAJEEXCEPCIONES();
	PedidosEnviadosAdapter dpAdapter;
	BDTablas dbcon;
	SQLiteDatabase db;
	Cursor c;
	ListView lvPedidos;
	Constantes constante = new Constantes();
	Dialogos dialogo = new Dialogos();
	VARIOS varios;
	Intent intent;
	Gson gson;
	AlertDialog.Builder builder;
	AlertDialog alert;
	EditText EBusquedaPedido;
	static TextView tvDisplayDate;
	
	
	
	
	String linea="";
	Dialogos mensaje = new Dialogos();
	String impresora="BlueTooth Printer";
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_ver_pedidos_enviados);
		
		codUser = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");
		
		this.inicializaVariblesDeControl();

		try{
			dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
			db = dbcon.getWritableDatabase();
		}catch(Exception e){
			dialogo.miDialogoToastLargo(getApplicationContext(), "Error al conectarse a la BD: Clase MainVerPedidosEnviados --> " + e.getMessage());
			return;
		}

		varios = new VARIOS(getApplicationContext(), db);
		tvDisplayDate.setText(varios.dameFechaSistema());
		
		this.buscaPedidos();
		getActionBar().setTitle("Pedidos: " + alPedido.size());
		
		//Evento click del listview
		lvPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
				//Entra cuando el listview NO est� en selecci�n m�tiple.
				//Cuando el listview es selecci�n m�ltiple el listview tienes como setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
				//Y cuando esta en selecci�n m�ltiple la funcion getChoiceMode() es = a 2
				//por eso cuando la funcion getChoiceMode() sea diferente a dos, entra a ejecutar la acci�n del listview no seleccionable
				//esto es para que cuando quiera hacer la selecci�n m�ltiple no aparezca la pantalla de estas opciones, solo cuando haga click corto y no haga selecci�n m�ltiple
				if(lvPedidos.getChoiceMode()!=2)
				{
					posicion = i;
					secautopedido = alPedido.get(i).getSecauto();
					numpedido = alPedido.get(i).getNumero();
					secpedido = alPedido.get(i).getSecuencial();
					bodega = alPedido.get(i).getBodega();
					cliente = alPedido.get(i).getCliente();
					nomcliente = alPedido.get(i).getNombreCliente();
					tipo = alPedido.get(i).getTipo();
					vendedor = alPedido.get(i).getUsuario();
					observacion = alPedido.get(i).getObservacion();
					subtotalpedido = alPedido.get(i).getSubtotal();
					descuentopedido = alPedido.get(i).getDescuento();
					ivapedido = alPedido.get(i).getIva();
					netopedido = alPedido.get(i).getNeto();
					docfac = alPedido.get(i).getDocFac(); 
					
					String[] opc = new String[] {"Ver Detalles","Activar para envio","Eliminar","Imprimir"};
	                AlertDialog opciones = new AlertDialog.Builder(MainVerPedidosEnviados.this)
	        				.setTitle("Opciones del pedido")
	        				.setItems(opc,
	        						new DialogInterface.OnClickListener() {
	        							public void onClick(DialogInterface dialog,int selected) {
	        								try{
	        									if (selected == 0) {
	            							        intent = new Intent(MainVerPedidosEnviados.this,MainVerDetallesPedidos.class);
	            									intent.putExtra("user", codUser);
	            									intent.putExtra("nombreUser", nombreUsuario);
	            									intent.putExtra("tipoUser", tipoUsuario);
	            									intent.putExtra("secautoPedido", String.valueOf(secautopedido));
	            									intent.putExtra("numPedido", String.valueOf(numpedido));
	            									intent.putExtra("secPedido", String.valueOf(secpedido));
	            									intent.putExtra("bodPedido", String.valueOf(bodega));
	            									intent.putExtra("tipoPedido", tipo);
	            									intent.putExtra("clientePedido", cliente);
	            									intent.putExtra("nombreclientePedido", nomcliente);
	            									intent.putExtra("vendedorPedido", vendedor);
	            									intent.putExtra("observacionPedido", observacion);
	            									intent.putExtra("subtotalPedido", String.valueOf(subtotalpedido));
	            									intent.putExtra("descuentoPedido", String.valueOf(descuentopedido));
	            									intent.putExtra("ivaPedido", String.valueOf(ivapedido));
	            									intent.putExtra("netoPedido", String.valueOf(netopedido));
	            									intent.putExtra("esDoc", docfac);
	            									startActivity(intent);
	            								} else if (selected == 1) {
	            									actualizaPedido(numpedido,secpedido,codUser);
	            								}else if (selected == 2) {
	            									eliminaPedido(posicion);
	            								}else if (selected == 3) {
	            									imprimePedido(numpedido);
	            								}
	        								}catch(Exception e){
	        									Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
	        								}
	        							}
	        						}).create();
	        		opciones.show();
				}
			}
		});
		
		//Implemento evento clickLargo del ListView
		lvPedidos.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int pos, long id) {
				//pongo mi lista en modo de selecci�n m�ltiple
			    //con esto, al hacer click en los elementos se seleccionar�n autom�ticamente
				lvPedidos.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
				return true;
			}
		});
	}
	
	@SuppressWarnings("static-access")
	public void buscaPedidos()
	{
		try{
			if(db!=null)
			{
				scriptSQL = "select ped.secauto,ped.tipo,ped.bodega,ped.numero,ped.secuencial,ped.cliente,ped.usuario,ped.subtotal,ped.descuento," +
							"ped.iva,ped.neto,ped.operador,ped.observacion,ped.fecha_ingreso,ped.hora_ingreso,ped.estado,cli.nombre,ped.docfac "+
							"from cabecerapedidos ped,cliente cli where ped.cliente = cli.codigo and ped.usuario = '"+codUser+"'and ped.estado='P' " + 
							"and ped.fecha_ingreso = '" + this.tvDisplayDate.getText().toString().trim() + "' order by ped.fecha_ingreso desc";
				try{
					c = db.rawQuery(scriptSQL, null);
				}catch(Exception e){
					dialogo.miDialogoToastLargo(getApplicationContext(), "Error al ejecutar el script de pedido: Clase MainVerPedidos --> " + e.getMessage());
					return;
				}
				
				if(c.moveToFirst())
				{
					do{
						alPedido.add(new Pedidos(c.getInt(0),c.getString(1),c.getInt(2),c.getInt(3),c.getInt(4),c.getString(5),
								 				 c.getString(6),c.getDouble(7),c.getDouble(8),c.getDouble(9),c.getDouble(10),
								 				 c.getString(11),c.getString(12),c.getString(13),c.getString(14),c.getString(15),
								 				 c.getString(16),c.getString(17),"",""));
					}while(c.moveToNext());
					
					dpAdapter = new PedidosEnviadosAdapter(this, alPedido);
					//dpAdapter = new PedidosAdapter(this, alPedido);
					lvPedidos.setAdapter(dpAdapter);
				}
				else
				{
					dialogo.miDialogoToastCorto(getApplicationContext(), "No hay pedidos que mostrar");
				}
			}
			else
			{
				dialogo.miDialogoToastCorto(getApplicationContext(), "No se pudo abrir la conexión a la base de datos");
			}
		}catch(Exception e)
		{
			dialogo.miDialogoToastLargo(getApplicationContext(), "Error al abrir la BD despues de la conexión: Clase MainVerPedidos --> " + e.getMessage());
			return;
		}
	}
	
	public void eliminaPedido(final int i)
	{
		builder = new Builder(MainVerPedidosEnviados.this);
		builder.setMessage("¿Está seguro de querer eliminar el pedido?")
				.setTitle("Eliminación del pedido")
				.setPositiveButton("Si", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Cuando el usuario escoje SI
						mex = pedido.eliminaPedido(db, secpedido, vendedor);
						if(mex.getEstado())
						{
							alPedido.remove(i);
							dpAdapter = new PedidosEnviadosAdapter(MainVerPedidosEnviados.this, alPedido);
							lvPedidos.setAdapter(dpAdapter);
							dpAdapter.notifyDataSetChanged();
							dialogo.miDialogoToastCorto(getApplicationContext(), "El pedido # " + numpedido + " ha sido eliminado con éxito");
							getActionBar().setTitle("Total de pedidos: " + alPedido.size());
						}
						else
						{
							dialogo.miDialogoToastLargo(getApplicationContext(), mex.getMensaje());
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
	
	public void actualizaPedido(int numpedido,int secuencial,String vendedor)
	{
		mex = pedido.restableceEstadoPedido(db, secuencial, vendedor);
		if(mex.getEstado())
		{ 
			alPedido.clear();
			this.buscaPedidos();
			dialogo.miDialogoToastCorto(getApplicationContext(), "El estado del pedido # " + numpedido + " ha sido actualizado para un próximo envío");
			getActionBar().setTitle("Total de pedidos: " + alPedido.size());
		}
		else
		{
			dialogo.miDialogoToastLargo(getApplicationContext(), mex.getMensaje());
		}
	}
	
	public void inicializaVariblesDeControl()
	{
		lvPedidos = (ListView)findViewById(R.id.lvPedidos);
		tvDisplayDate = (TextView) findViewById(R.id.lblDate);
		lvPedidos.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
	}
	
	public void verPedidosSeleccionados(String accion)
	{
		SparseBooleanArray seleccionados = lvPedidos.getCheckedItemPositions();
		if(seleccionados==null || seleccionados.size()==0){
            //Si no hab�a elementos seleccionados...
            Toast.makeText(this,"No hay elementos seleccionados",Toast.LENGTH_SHORT).show();
        }else{
            //si los hab�a, miro sus valores
            //Esto es para ir creando un mensaje largo que mostrar� al final
            StringBuilder resultado=new StringBuilder();
            if(accion.equals("ACT"))
            {
            	resultado.append("Se activaron para envío los siguientes pedidos:\n");
            }
            else if(accion.equals("ELI"))
            {
            	resultado.append("Se eliminaron los siguientes pedidos:\n");
            }
            //Recorro mi "array" de elementos seleccionados
            final int size=seleccionados.size();
            for (int i=0; i<size; i++) {
                //Si valueAt(i) es true, es que estaba seleccionado
                if (seleccionados.valueAt(i)) {
                    //en keyAt(i) obtengo su posici�n
                    resultado.append("El pedido número: "+alPedido.get(seleccionados.keyAt(i)).getNumero()+"\n");
                    
                    if(accion.equals("ACT"))
                    {
                    	pedido.restableceEstadoPedido(db, alPedido.get(seleccionados.keyAt(i)).getSecuencial(), codUser);
                    }
                    else if(accion.equals("ELI"))
                    {
                    	mex = pedido.eliminaPedido(db, alPedido.get(seleccionados.keyAt(i)).getSecuencial(), codUser);
                    }
                }
            }
            Toast.makeText(this,resultado.toString(),Toast.LENGTH_LONG).show();
        }
		inicializaVariblesDeControl();
		alPedido.clear();
		this.buscaPedidos();
		getActionBar().setTitle("Pedidos: " + alPedido.size());
	}
	
	public void imprimePedido(final int numpedido)
	{
		builder = new Builder(MainVerPedidosEnviados.this);
		builder.setMessage("¿Está seguro de querer imprimir el pedido?")
				.setTitle("Impresión del pedido")
				.setPositiveButton("Si", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Cuando el usuario escoje SI
						//mensaje.miDialogoToastCorto(getApplicationContext(), "SI");
						creaReporte(codUser,numpedido);
						try {
							impresora = varios.getNombreImpresora();
							findBT();
				            //openBT();
				            sendData();
				            closeBT();
				            linea="";
				            
				        } catch (IOException ex) {
				            ex.printStackTrace();
				        }
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//mensaje.miDialogoToastCorto(getApplicationContext(), "NO");
					}
				});
				alert = builder.create();
				alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_ver_pedidos_enviados, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case R.id.accion_buscar:
				alPedido.clear();
				this.buscaPedidos();
				return true;
				
			case R.id.accion_activar_para_envio:
				this.verPedidosSeleccionados("ACT");
				return true;
				
			case R.id.accion_eliminar:
				this.verPedidosSeleccionados("ELI");
				return true;
			case R.id.cerrar_sesion:
				finishAffinity();
				intent = new Intent(this,MainLogin.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// Esto es para cuando de clic en el bo�n de atr�s no regrese a la pantalla de login
	        finish();
	        Intent intent = new Intent(this,MainMenuPedido.class);
			intent.putExtra("user", codUser);
			intent.putExtra("nombreUser", nombreUsuario);
			intent.putExtra("tipoUser", tipoUsuario);
			startActivity(intent);
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void muestraDialogFecha(View v) {
	     SELECCIONAFECHA newFragment = new SELECCIONAFECHA();
	     newFragment.show(getFragmentManager(), "datePicker");
	 }
	
	public static class SELECCIONAFECHA extends DialogFragment implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// 	Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
			tvDisplayDate.setText(new StringBuilder().
								  append(day).append("/").
								  append(month + 1).append("/").
								  append(year).append(" "));
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
			    	mensaje.miDialogoToastCorto(getApplicationContext(), "No hay ning�n adaptador bluetooth disponible");
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
			     //mensaje.miDialogoToastCorto(getApplicationContext(), "Bluetooth device found.");
			     //mensaje.miDialogoToastCorto(getApplicationContext(), "Dispositivo Bluetooth '"+ impresora.trim() +"' encontrado");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
			
			// tries to open a connection to the bluetooth printer device
			void openBT() throws IOException {
			    try {
			        // Standard SerialPortService ID
			        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
			        mmSocket = null;
			        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
			        //mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
			        mmSocket.connect();
			        mmOutputStream = mmSocket.getOutputStream();
			        mmInputStream = mmSocket.getInputStream();
			        beginListenForData();
			        mensaje.miDialogoToastCorto(getApplicationContext(), "Bluetooth conectado");
			    } catch (Exception e) {
			    	//Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			        e.printStackTrace();
			    }
			}

			/*
			 * after opening a connection to bluetooth printer device,
			 * we have to listen and check if a data were sent to be printed.
			 */
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
			        //mensaje.miDialogoToastCorto(getApplicationContext(), "Bluetooth Closed");
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
			}
}