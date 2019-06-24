package com.birobidsa.admmovil.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import clasesAdapter.ListViewMenuClienteAdapter;
import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.Clientes;

@SuppressLint("NewApi")
public class MainMenuClientes extends Activity {
	String[] titulo = new String[]{"Nuevo Cliente","Ver Clientes","Cartera de Clientes"};
	int[] imagenes = new int[]{R.drawable.cliente,R.drawable.cliente,R.drawable.carteraclientes};
	String usuario,nombreUsuario,tipoUsuario,creaCliente,scriptSQL,cartera;
	int numreg;
	ListViewMenuClienteAdapter adapter;
	Clientes cliente;
	TextView TUsuario;
	Intent intent;
	BDTablas dbcon;
	SQLiteDatabase db;
	Cursor c;
	Constantes constante = new Constantes();
	Dialogos dialogo = new Dialogos();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu_clientes);
		
		getActionBar().setTitle("Menú de Clientes");
		
		//Recibo los parametros de la actividad MainLogin el usuario y el nombre de quien inicia sesi�n
		usuario = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");
		
		//Tomo el control TextView donde va el nombre del usuario y le asigno la variable usuario
		TUsuario = (TextView)findViewById(R.id.lblNombreUsuario);
		TUsuario.setText("Bienvenido: " + nombreUsuario + "-" + usuario);
		creaCliente = "S";
		cliente = new Clientes();
		
		try{
			dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
			db = dbcon.getWritableDatabase();
		}catch(Exception e){
			dialogo.miDialogoToastLargo(getApplicationContext(), "Error al conectarse a la BD: Clase MainMenuClientes --> " + e.getMessage());
			return;
		}
		
		if(db!=null)
		{
			scriptSQL = "SELECT CARTERA FROM PARAMETROS";
			c = db.rawQuery(scriptSQL,null);
			if(c.moveToFirst())
			{
				cartera = c.getString(0);

			}

			scriptSQL = "SELECT crea_cliente FROM usuario where codigo='"+ usuario +"'";
			c = db.rawQuery(scriptSQL,null);
			if(c.moveToFirst())
			{
				creaCliente = c.getString(0);
			}
			if(creaCliente == null)
			{
				creaCliente = "N";
			}




		}


		
		//Tomo el control ListView
		final ListView listaMenuClientes = (ListView)findViewById(R.id.lvMenuClientes);
		adapter = new ListViewMenuClienteAdapter(this, titulo, imagenes);
		listaMenuClientes.setAdapter(adapter);



		listaMenuClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
				switch (i) {
				case 0:
					if(creaCliente.equals("S"))
					{
						intent = new Intent(getApplicationContext() ,MainCreacionClientes.class);
						intent.putExtra("user", usuario);
						intent.putExtra("nombreUser", nombreUsuario);
						intent.putExtra("tipoUser", tipoUsuario);
						startActivity(intent);
					}
					else
					{
						dialogo.miDialogoToastLargo(getApplicationContext(), "Opción no habilitada para usuario " + usuario);
					}
					break;
				case 1:
					numreg = cliente.getNumClienteXUsuario(getApplicationContext(),db, usuario);
					if(numreg>0)
					{
						//para ver los clientes
						intent = new Intent(getApplicationContext() ,MainVerDatosClientes.class);
						intent.putExtra("user", usuario);
						intent.putExtra("nombreUser", nombreUsuario);
						intent.putExtra("tipoUser", tipoUsuario);
						startActivity(intent);
					}
					else
					{
						dialogo.miDialogoToastCorto(getApplicationContext(), "No hay registros de clientes para mostrar");
					}
					break;
				case 2:
					if(cartera.trim().equals("S")){
						intent = new Intent(getApplicationContext() ,MainCarteraClientes.class);
						intent.putExtra("user", usuario);
						intent.putExtra("nombreUser", nombreUsuario);
						intent.putExtra("tipoUser", tipoUsuario);
						startActivity(intent);
					}else
					{
						dialogo.miDialogoToastCorto(getApplicationContext(), "Opción no disponible comunicarse con el administrador del sistema central");
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
		getMenuInflater().inflate(R.menu.main_menu_clientes, menu);
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
}
