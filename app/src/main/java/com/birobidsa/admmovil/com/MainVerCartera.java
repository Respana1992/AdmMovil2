package com.birobidsa.admmovil.com;

//import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import clasesAdapter.DeudaAdapter;
import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.Deuda;

//@SuppressLint("NewApi")
public class MainVerCartera extends Activity {
	String codUser,nombreUsuario,tipoUsuario,scriptSQL,codigo,nombre;
	ArrayList<Deuda> aldeuda = new ArrayList<Deuda>();
	Deuda deuda;
	DeudaAdapter deudaAdapter;
	TextView TVClienteDeuda,TVDocDebe;
	ListView lvDeuda;
	BDTablas dbcon;
	SQLiteDatabase db;
	Cursor c;
	Constantes constante = new Constantes();
	Dialogos dialogo = new Dialogos();
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_ver_cartera);
		
		codUser = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");
		codigo = getIntent().getStringExtra("codigoCli");
		nombre = getIntent().getStringExtra("nombreCli");
		
		TVClienteDeuda = (TextView)findViewById(R.id.lblClienteDeuda);
		TVDocDebe = (TextView)findViewById(R.id.lblCliente);
		
		getActionBar().setTitle("Cartera de Clientes");
		
		try{
			dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
			db = dbcon.getWritableDatabase();
		}catch(Exception e){
			dialogo.miDialogoToastLargo(getApplicationContext(), "Error al conectarse a la BD: Clase MainVerCarteraClientes --> " + e.getMessage());
			return;
		}
		buscaDeuda(codigo,nombre);

//        TVClienteDeuda.setOnClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                //Product selectedProduct = arrayList.get(position);
//
//                //Intent intent = new Intent(getApplicationContext(), activity_main_pago.class);
//                Intent intent = new Intent(getApplicationContext(), MainMenuPedido.class);
//                intent.putExtra("codcli", getIntent().getStringExtra("codigoCli"));
//                intent.putExtra("nombre", getIntent().getStringExtra("nombreCli"));
//                //TextView textView = (TextView) view.findViewById(R.id.myText);
//                //TextView textView = (TextView) view.findViewById(R.id.t);
//                //Obtiene el texto dentro del TextView.
//                //String textItemList  = textView.getText().toString();
//                //intent.putExtra("deuda", lvDeuda);
//                startActivity(intent);
//            }
//        });
	}
	
	public void buscaDeuda(String cliente,String nombrecliente)
	{
		scriptSQL = "select * from deuda where cliente = '"+ cliente +"'";
		try{
			c = db.rawQuery(scriptSQL, null);
			if(c.moveToFirst())
			{
				TVClienteDeuda.setText(codigo + " - " + nombre);
				do{
					deuda = new Deuda(c.getInt(0), 		//secuencial
									  c.getString(1),  	//bodega
									  c.getString(2),  	//cliente
									  c.getString(3),	//tipo
									  c.getInt(4),		//numero
									  c.getString(5),	//serie
									  c.getString(6),	//secinv
									  c.getDouble(7),	//iva
									  c.getDouble(8),	//monto
									  c.getDouble(9),	//credito
									  c.getDouble(10),	//saldo
									  c.getString(11),	//fechaemi
									  c.getString(12),	//fechaven
									  c.getString(13),	//vendedor
									  c.getString(14));	//observacion
					aldeuda.add(deuda);
				}while(c.moveToNext());
				
				TVDocDebe.setText("Tiene "+ aldeuda.size() + " documentos pendiente de pago");
				
				lvDeuda = (ListView)findViewById(R.id.lvClientesDeudas);
				deudaAdapter = new DeudaAdapter(this, aldeuda);
				lvDeuda.setAdapter(deudaAdapter);
			}
			else
			{
				TVClienteDeuda.setText(codigo + " - " + nombre);
				TVDocDebe.setText("No tiene deuda pendiente de pago");
			}
		}catch(Exception e){
			dialogo.miDialogoToastLargo(getApplicationContext(), "Error al ejecutar el script de pedido: Clase MainVerCarteraClientes --> " + e.getMessage());
			return;
		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3){
		//Se busca la referencia del TextView en la vista.
		//TextView textView = (TextView) arg1.findViewById(R.id.myText);
		//Obtiene el texto dentro del TextView.
		//String textItemList  = textView.getText().toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_ver_cartera, menu);
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
