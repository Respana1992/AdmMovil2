package com.birobidsa.admmovil.com;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.birobidsa.admmovil.com.MainGeolocalizacion.Localizacion;
import com.google.gson.Gson;

import java.util.ArrayList;

import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.CabeceraPedidos;
import misclases.Clientes;
import misclases.CondiComer;
import misclases.DetallesPedidos;
import misclases.FrecuenciaVisita;
import misclases.GrupoCli;
import misclases.GrupoPro;
import misclases.Item;
import misclases.ListViewAdapter;
import misclases.Ruta;
import misclases.TipoCliente;
import misclases.TipoNegocio;
import misclases.Usuario;
import procesos.DESCARGARDATOS;

@SuppressLint("NewApi")
public class MainMenuPrincipal extends Activity {
	String[] titulo;
	int[] imagenes;
	String usuario,nombreUsuario,tipoUsuario,selectSQL,cartera;
	TextView TUsuario;
	ListViewAdapter adapter;
	Intent intent;
	Clientes cli;
	Item item;
	CondiComer condicomer;
	GrupoPro gpro;
	GrupoCli gcli;
	Usuario objetoUsuario;
	TipoNegocio tiponego;
	TipoCliente tipocli;
	FrecuenciaVisita frevis;
	Ruta ruta;
	SQLiteDatabase db;
    BDTablas dbcon;
    Cursor cur_cab,cur_det,c;
    Gson gson;
    Dialogos dialogo = new Dialogos();
    ArrayList<CabeceraPedidos> alcabped;
    ArrayList<DetallesPedidos> aldetped;
    AlertDialog.Builder builder;
    AlertDialog alert;
    DESCARGARDATOS descarga;
    Constantes constante;
	private LocationManager locationManager;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu_principal);
		
		getActionBar().setTitle("Menú Principal");


		
		constante = new Constantes();
		try{
			dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
			db = dbcon.getWritableDatabase();
		}catch(Exception e){
			dialogo.miDialogoToastLargo(this, "Excepción generada al conectarse a la Base de datos: Clase: MainMenuPrincipal --> " + e.getMessage());
			return;
		}

		//Recibo los parametros de la actividad MainLogin el usuario y el nombre de quien inicia sesi�n
		usuario = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");

		//Tomo el control TextView donde va el nombre del usuario y le asigno la variable usuario
		TUsuario = (TextView)findViewById(R.id.lblNombreUsuario);
		TUsuario.setText("Bienvenido: " + nombreUsuario + "-" + usuario);
		
		selectSQL = "SELECT IP_WEBSERVICE, IP_API, IP_WEBSERVICE_BIROBID,CARTERA FROM PARAMETROS";
		if(db!=null)
		{
			c = db.rawQuery(selectSQL, null);
			if(c.moveToFirst())
			{
				constante.IPWS = c.getString(0);
				constante.IPAPI = c.getString(1);
				constante.IPWSB = c.getString(2);
				constante.URLWS = "http://"+ constante.IPWS +"/Service1.asmx";
				constante.NAMESPACE = "http://birobid.com/webservice/admmovil";
                cartera = c.getString(3);
			}

		}


		if (ActivityCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&&  ActivityCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},
					1000);
		} else {
            LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Localizacion mmLocalizacion = new Localizacion();

            //MainGeolocalizacion.Localizacion mLocalizacion = new MainGeolocalizacion.Localizacion();
        }

		titulo = new String[]{"Clientes","Pedidos","Pagos","Geolocalización","Carga de Datos","Descarga de Datos","Parámetros"};
		imagenes = new int[]{R.drawable.cliente,R.drawable.pedidos,R.drawable.pagos,R.drawable.geolocalizacion,R.drawable.descargadatos,R.drawable.cargadatos,R.drawable.parametros};

		//Tomo el control ListView
		final ListView listaMenuPrincipal = (ListView)findViewById(R.id.lvMenuPrincipal);
		adapter = new ListViewAdapter(this, titulo, imagenes);
		listaMenuPrincipal.setAdapter(adapter);
		
		listaMenuPrincipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            	switch (i) {
				case 0:
					intent = new Intent(getApplicationContext() ,MainMenuClientes.class);
					intent.putExtra("user", usuario);
					intent.putExtra("nombreUser", nombreUsuario);
					intent.putExtra("tipoUser", tipoUsuario);
					startActivity(intent);
					break;

				case 1:
					//Llamo a la actividad de pedidos
					intent = new Intent(getApplicationContext(),MainMenuPedido.class);
					intent.putExtra("user", usuario);
					intent.putExtra("nombreUser", nombreUsuario);
					intent.putExtra("tipoUser", tipoUsuario);
					startActivity(intent);
					break;
				case 2:
					//Llamo a la actividad de pagos
					if(cartera.trim().equals("S")){
						intent = new Intent(getApplicationContext() ,MainPagos.class);
						intent.putExtra("user", usuario);
						intent.putExtra("nombreUser", nombreUsuario);
						intent.putExtra("tipoUser", tipoUsuario);
						startActivity(intent);
					}else
					{
						dialogo.miDialogoToastCorto(getApplicationContext(), "Opción no disponible comunicarse con el administrador del sistema central");
					}
					break;

				case 3:
					//Llamo a la actividad de geolocalizacion
					intent = new Intent(getApplicationContext(),MainGeolocalizacion.class);
					intent.putExtra("user", usuario);
					intent.putExtra("nombreUser", nombreUsuario);
					intent.putExtra("tipoUser", tipoUsuario);
					startActivity(intent);
					break;

				case 4:
					if(constante.IPWS=="" || constante.IPWS==null)
					{
						dialogo.miDialogoToastCorto(getApplicationContext(), "Debe configurar la IP del Web Service");
					}
					else
					{
						intent = new Intent(getApplicationContext() ,MainCargaDatos.class);
						intent.putExtra("user", usuario);
						intent.putExtra("nombreUser", nombreUsuario);
						intent.putExtra("tipoUser", tipoUsuario);
						startActivity(intent);
					}
					break;
					case 5:
						if(constante.IPWS=="" || constante.IPWS==null)
						{
							dialogo.miDialogoToastCorto(getApplicationContext(), "Debe configurar la IP del Web Service");
						}
						else
						{
							builder = new Builder(MainMenuPrincipal.this);
							builder.setMessage("¿Está seguro de querer descargar la información?")
									.setTitle("Descarga de datos")
									.setPositiveButton("Si", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											//enviaPedidos();
											descarga = new DESCARGARDATOS(getApplicationContext());
											descarga.descargarTodo(db);
											descarga.descargarDatosBoribid(db);
											descarga.descargarClientes(db);
											descarga.descargarPago(db,usuario);
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
						}
						break;
					default:
						break;

					case 6:
						//Llamo a la actividad de parametros
						intent = new Intent(getApplicationContext(),MainParametros.class);
						intent.putExtra("user", usuario);
						intent.putExtra("nombreUser", nombreUsuario);
						intent.putExtra("tipoUser", tipoUsuario);
						startActivity(intent);
						break;
				}
            }
        });
	}


	
	//Este m�todo se ejecuta cuando se da clic en el bot�n de ir hacia atr�s
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	// Esto es para cuando de clic en el bo�n de atr�s no regrese a la pantalla de login
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu_principal, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case R.id.cerrar_sesion:
				finishAffinity();
				intent = new Intent(this,MainLogin.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}