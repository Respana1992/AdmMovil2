package com.birobidsa.admmovil.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import procesos.CONEXIONIONTERNET;
import procesos.UbicacionCliente;
import procesos.VARIOS;

public class MainLogin extends Activity {
	private EditText EUsuario,EClave;
	private TextView TVlblInformacion,TVlblVersion;
	private String txtUsuario,txtClave,codigoUser,nombreUser,datosBase;
	private Constantes constante;
	private Dialogos misDialogos;
	private BDTablas dbcon;
	private SQLiteDatabase db;
	private Cursor c;
	private VARIOS varios;
	boolean vercon;
	CONEXIONIONTERNET conint;
	UbicacionCliente ubicacion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_login);
		
		getActionBar().setTitle("Inicio de Sesión - ADM MÓVIL");
		
		constante = new Constantes();
		misDialogos = new Dialogos();
		varios = new VARIOS();
		conint = new CONEXIONIONTERNET(getApplicationContext());
		codigoUser = "";
		nombreUser = "";
		ubicacion = new UbicacionCliente(this);


		try{
			dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
			db = dbcon.getWritableDatabase();
		}catch(Exception e){
			misDialogos.miDialogoToastLargo(this, "Excepción generada al conectarse a la Base de datos: Clase: MainLogin --> " + e.getMessage());
			return;
		}
		
		if(db!=null)
		{
			//Para eliminar la base de datos, lo uso solo en desarrollo si es necesario
			//this.deleteDatabase("ADMMOVIL.db");
			datosBase = "SELECT codigo,nombre FROM usuario where tipo='VEN'";
			c = db.rawQuery(datosBase, null);
			if(c.moveToFirst())
			{
				codigoUser = c.getString(0);
				nombreUser = c.getString(1);
			}
		}
		TVlblInformacion = (TextView)findViewById(R.id.lblInformacion);
		TVlblInformacion.setText("Fecha de trabajo: " + varios.dameFechaSistema() + " - " + codigoUser+"-"+nombreUser);
		TVlblVersion = (TextView)findViewById(R.id.lblVersion);
		TVlblVersion.setText("Versión 2.43");




	}





	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_login, menu);
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
	
	@SuppressLint("DefaultLocale")
	public void ingreso(View v)
	{
		EUsuario = (EditText)findViewById(R.id.txtUsuario);
		EClave = (EditText)findViewById(R.id.txtClave);
		
		txtUsuario = EUsuario.getText().toString().toUpperCase().trim();
		txtClave = EClave.getText().toString().toLowerCase().trim();
		
		if(txtUsuario.length()==0)
		{
			misDialogos.dialogoAceptar(this,"Por favor ingrese su usuario","Login - "+ constante.tituloVentana);
			EUsuario.requestFocus();
		}
		else if(txtClave.length()==0)
		{
			misDialogos.dialogoAceptar(this,"Por favor ingrese su clave","Login - "+ constante.tituloVentana);
			EClave.requestFocus();
		}
		else
		{
			String selectUsuario;

			selectUsuario = "SELECT codigo,clave,nombre,tipo FROM Usuario WHERE codigo='"+ txtUsuario +"' AND clave='"+ txtClave +"'";
			if(db!=null)
			{
				c = db.rawQuery(selectUsuario, null);
				if(c.moveToFirst())
				{
					db.close();
					//Llamo a otra actividad y le paso dos parametros: el usuario y el nombre de quien inicia sesi�n
					Intent intent = new Intent(this,MainMenuPrincipal.class);
					intent.putExtra("user", this.txtUsuario);
					intent.putExtra("nombreUser", c.getString(2));
					intent.putExtra("tipoUser", c.getString(3));
					startActivity(intent);
				}
				else
				{
					misDialogos.dialogoAceptar(this,"Los datos ingresados no son los correctos","Login - "+ constante.tituloVentana);
					EUsuario.setText("");
					EClave.setText("");
					EUsuario.requestFocus();
				}
			}
		}
	}
}