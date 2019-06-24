package com.birobidsa.admmovil.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

import clasesAdapter.VerDetallesPedidosAdapter;
import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.DetallesPedidos;
import misclases.Pedidos;

@SuppressLint("NewApi")
public class MainVerDetallesPedidos extends Activity {
	int numpedido,secpedido,secautopedido,bodega;
	double subtotalpedido,descuentopedido,ivapedido,netopedido,latitud, longitud;
	String usuario,nombreUsuario,tipoUsuario,tipo,cliente,nomcliente,vendedor,observacion;
	ArrayList<Pedidos> alPedido = new ArrayList<Pedidos>();
	ArrayList<DetallesPedidos> alDetallePedido = new ArrayList<DetallesPedidos>();
	VerDetallesPedidosAdapter dpAdapter;
	TextView TCabecera,TDatosCabecera,TTituloDetalle;
	Pedidos ped;
	Constantes constante;
	Dialogos misDialogos;
	ListView lvDetPed;
	Intent intent;
	Gson gson;
	Bundle bundle;
	Type type;
	BDTablas dbcon;
	SQLiteDatabase db;
	DecimalFormat df;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_ver_detalles_pedidos);
		
		usuario = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");
		secautopedido = Integer.parseInt(getIntent().getStringExtra("secautoPedido"));
		numpedido = Integer.parseInt(getIntent().getStringExtra("numPedido"));
		secpedido = Integer.parseInt(getIntent().getStringExtra("secPedido"));
		bodega = Integer.parseInt(getIntent().getStringExtra("bodPedido"));
		tipo = getIntent().getStringExtra("tipoPedido");
		cliente = getIntent().getStringExtra("clientePedido");
		nomcliente = getIntent().getStringExtra("nombreclientePedido");
		vendedor = getIntent().getStringExtra("vendedorPedido");
		observacion = getIntent().getStringExtra("observacionPedido");
		subtotalpedido = Double.parseDouble(getIntent().getStringExtra("subtotalPedido"));
		descuentopedido = Double.parseDouble(getIntent().getStringExtra("descuentoPedido"));
		ivapedido = Double.parseDouble(getIntent().getStringExtra("ivaPedido"));
		netopedido = Double.parseDouble(getIntent().getStringExtra("netoPedido"));


		latitud = Double.parseDouble(getIntent().getStringExtra("latitud"));
		longitud = Double.parseDouble(getIntent().getStringExtra("longitud"));


		//TextView txtLatitud = (TextView) findViewById(R.id.txtLatLon);

		//txtLatitud.setText( latitud + " ; " + longitud);
		TCabecera = (TextView)findViewById(R.id.tvCabeceraPed);
		TDatosCabecera = (TextView)findViewById(R.id.tvDatoscabecera);
		TTituloDetalle = (TextView)findViewById(R.id.tvTituloDetPed);
		
		//Recibo un json llamado "datosPedido" con los pedidos y lo almaceno al ArrayList
		bundle = this.getIntent().getExtras();
		gson = new Gson();
		String historial = bundle.getString("datosPedido");
		type = new TypeToken<ArrayList<Pedidos>>(){}.getType();
		alPedido = gson.fromJson(historial, type);
		
		lvDetPed = (ListView)findViewById(R.id.lvDetPedidos);
		constante = new Constantes();
		try{
			dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
			db = dbcon.getWritableDatabase();
		}catch(Exception e){
			misDialogos.miDialogoToastLargo(this, "Excepción generada al conectarse a la Base de datos: Clase: MainVerDetallesPedidos; Método: onCreate --> " + e.getMessage());
			return;
		}
		
		if(db!=null)
		{
			ped = new Pedidos();
			alDetallePedido = ped.getDetallesXPedido(db, secpedido, vendedor);
			if(alDetallePedido.size()==0)
			{
				misDialogos.miDialogoToastLargo(this, "No hay detalles para mostrar");
			}
			else
			{
				dpAdapter = new VerDetallesPedidosAdapter(this, alDetallePedido);
				lvDetPed.setAdapter(dpAdapter);
			}
		}
		else
		{
			misDialogos.miDialogoToastLargo(this, "Error al abrir la Base de datos: Clase: MainVerDetallesPedidos; Método: onCreate");
			return;
		}
		getActionBar().setTitle(nomcliente); //Pongo el nombre del cliente en la barra de t�tulo
		
		df= new DecimalFormat("#0.00"); //esto sirve para mostrar solo dos decimales
		TCabecera.setText("Datos del Pedido No. " + numpedido);
		TDatosCabecera.setText("Secuencial: " + secpedido + " - Cod. Cliente: " + cliente +
						  	   "\nNeto: " + df.format(netopedido) + " - Observación: " + observacion +
						       "\nVendedor: " + vendedor + " - " + nombreUsuario +
								"\nLocalización: " + latitud + " ; " + longitud);
		TTituloDetalle.setText("Detalles del Pedido");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_ver_detalles_pedidos, menu);
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

