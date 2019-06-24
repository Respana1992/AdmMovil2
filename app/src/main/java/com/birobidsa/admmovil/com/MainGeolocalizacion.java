package com.birobidsa.admmovil.com;

//import android.annotation.SuppressLint;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.util.ArrayList;

import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.Coordenada;
import misclases.DetallesPedidos;
import procesos.UbicacionCliente;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ws.CoordenadaApi;



//@SuppressLint("NewApi")
public class MainGeolocalizacion extends Activity {
	static String usuario, nombreUsuario, tipoUsuario, selectSQL, temporalcodcliente;
	static boolean visita;
	static TextView latitudv;
	TextView longitudv;
	TextView botonactualizarv;
	EditText ENombreCliente,ECodigoCliente;
	double latitude1 = 0, longitude1 = 0, latitud,  longitud;
	SQLiteDatabase db;
	BDTablas dbcon;
	Cursor c;
	Dialogos misDialogos = new Dialogos();
	Constantes constante = new Constantes();
	UbicacionCliente ubicacion;
	private Retrofit retrofit;
	static private CoordenadaApi coordenadaApi;
	private GoogleApiClient client;
	CheckBox chbVisita;
	Intent intent;
	Gson gson;
	String detallesJson = "";
	String codigoCliente, nombreCliente, negocioCliente,tipoCliente,cedulaCliente;
	private ArrayList<DetallesPedidos> alDetallesPed = new ArrayList<DetallesPedidos>();
	static Dialogos mensaje = new Dialogos();





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_geolocalizacion);

        try{
            dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
            db = dbcon.getWritableDatabase();
        }catch(Exception e){
            misDialogos.miDialogoToastLargo(this, "Excepción generada al conectarse a la Base de datos: Clase: MainLogin --> " + e.getMessage());
            return;
        }

		this.incializaVariablesDeControles();


        selectSQL = "SELECT IP_API  FROM PARAMETROS";
        if(db!=null)
        {
            c = db.rawQuery(selectSQL, null);
            if(c.moveToFirst())
            {
                constante.IPAPI = c.getString(0);

            }

        }




		String BASE_URL =  "http://"+ constante.IPAPI +"/"; /*"http://"+ constante.IPAPI +"/";*///"http://181.198.213.18:8089/";;

		try {
			// Crear conexión al servicio REST
			retrofit = new Retrofit.Builder()
					.baseUrl(BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.build();

			//...
			// Crear conexión a la API de SaludMock
			coordenadaApi = retrofit.create(CoordenadaApi.class);
			getActionBar().setTitle("Menú de Geolocalización");

			//Recibo los parametros de la actividad MainLogin el usuario y el nombre de quien inicia sesi�n
			usuario = getIntent().getStringExtra("user");
			nombreUsuario = getIntent().getStringExtra("nombreUser");
			tipoUsuario = getIntent().getStringExtra("tipoUser");
			latitudv = findViewById(R.id.txtviewlatitud);
			botonactualizarv = (ToggleButton) findViewById(R.id.toggleButton);
			chbVisita = (CheckBox) findViewById(R.id.chbVisita);
			codigoCliente = getIntent().getStringExtra("codigoCli");
			nombreCliente = getIntent().getStringExtra("nombreCli");
			negocioCliente = getIntent().getStringExtra("negocioCli");
			tipoCliente = getIntent().getStringExtra("tipoCli");
			cedulaCliente = getIntent().getStringExtra("cedC");
			temporalcodcliente="";
			visita=false;


			if(codigoCliente != null)
			{
				ECodigoCliente.setEnabled(true);
				ENombreCliente.setText(nombreCliente);
				ECodigoCliente.setText(codigoCliente);
				ENombreCliente.setTextColor(Color.parseColor("#3A3A3A"));
                temporalcodcliente = codigoCliente;
				//Recibo los detalles de los pedidos en un objeto Json y lo paso al ArrayList

			}


			chbVisita.setOnCheckedChangeListener(new myCheckBoxChnageClicker());

			if (ActivityCompat.checkSelfPermission(this,
					Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
					&&  ActivityCompat.checkSelfPermission(this,
					Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
			{
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},
						1000);
			} else {
				botonactualizarv.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								locationStart();
							}
						});
					}
				});
			}

		} catch (Exception e){
			//e.printstacktrace();
			Log.d("ERROR", e.getMessage());
		}

	}


	public void incializaVariablesDeControles()
	{

		latitudv = (TextView)findViewById(R.id.txtLatLon);

		ENombreCliente = (EditText)findViewById(R.id.txtCliente);
		ECodigoCliente = (EditText)findViewById(R.id.txtCodigoCliente);

		chbVisita = (CheckBox)findViewById(R.id.chbVisita);
	}




	public void locationStart() {

		LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Localizacion mLocalizacion = new Localizacion();

		if (ActivityCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},
					1000);
			return;
		}


			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000*30, 0, mLocalizacion);
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000*30, 0, mLocalizacion);



	}


	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == 1000) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				locationStart();
				return;
			}
		}
	}

	public class myCheckBoxChnageClicker implements CheckBox.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
									 boolean isChecked) {
			if (isChecked) {
				if (buttonView == chbVisita) {
					visita = true;
					//showTextNotification("Android");
				}else{
					visita = false;
				}

			}
		}


	}

	public void buscaClientes(View v)
	{
		gson = new Gson();
		detallesJson = gson.toJson(alDetallesPed);

		intent = new Intent(this,MainVerClientesGeolocalizacion.class);
		intent.putExtra("codigoC", this.codigoCliente);
		intent.putExtra("nombreC", this.nombreCliente);
		intent.putExtra("negocioC", this.negocioCliente);
		intent.putExtra("tipoC", this.tipoCliente);
		intent.putExtra("cedC", this.cedulaCliente);
		intent.putExtra("user", this.usuario);
		intent.putExtra("nombreUser", this.nombreUsuario);
		intent.putExtra("tipoUser", tipoUsuario);
		intent.putExtra("datosDetalles", detallesJson);
		intent.putExtra("nombreUser", latitud);
		intent.putExtra("nombreUser", visita);

		startActivity(intent);

	}

	public void GuardarVGC (){
		locationStart();
	}

	/* Aqui empieza la Clase Localizacion */
	static public class Localizacion implements LocationListener {

		@Override
		public void onLocationChanged(Location loc) {
			// Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
			// debido a la deteccion de un cambio de ubicacion


			latitudv.setText(loc.getLatitude() + " ; " + loc.getLongitude());

			//enviar cooredenada al api
			Call<Coordenada> registrarMarcador = coordenadaApi.RegistrarCordenada(
					new Coordenada(
                            usuario,
                            temporalcodcliente,
                            visita,
							String.valueOf(loc.getLatitude()),
							String.valueOf(loc.getLongitude())
                            )
			);

			//mensaje.miDialogoToastCorto("Datos de Geolocalización enviados con exito!");

			registrarMarcador.enqueue(new Callback<Coordenada>( ){
				@Override
				public void onResponse(Call<Coordenada> call, Response<Coordenada> response) {
					// Procesar errores
					if (!response.isSuccessful()) {
						Log.d("ERROR en Respuesta", response.toString());
						return;
					}

				}

				@Override
				public void onFailure(Call<Coordenada> call, Throwable t) {
					Log.d("ERROR", t.getMessage());

				}
			});
		}

		@Override
		public void onProviderDisabled(String provider) {
			// Este metodo se ejecuta cuando el GPS es desactivado
			latitudv.setText("GPS Desactivado");
		}

		@Override
		public void onProviderEnabled(String provider) {
			// Este metodo se ejecuta cuando el GPS es activado
			latitudv.setText("GPS Activado");
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
				case LocationProvider.AVAILABLE:
					Log.d("debug", "LocationProvider.AVAILABLE");
					break;
				case LocationProvider.OUT_OF_SERVICE:
					Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
					break;
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
					break;
			}
		}
	}
	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onStop() {
		super.onStop();

	}


}
