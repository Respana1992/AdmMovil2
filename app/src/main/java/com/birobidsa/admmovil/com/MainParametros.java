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
import android.widget.EditText;

import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.Usuario;

public class MainParametros extends Activity {
	String usuario,nombreUsuario,tipoUsuario,scriptSQL,selectSQL;
	Constantes constante;
	SQLiteDatabase db;
    BDTablas dbcon;
    Cursor c;
    Dialogos misDialogos = new Dialogos();
    EditText ETIPWS,ETUSER,ETCLAVE,ETIMPRESORA,ETIPWSB,TXTAPI, TXTRUC, TXTSUCURSAL;
    Usuario objetoUsuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_parametros);
		
		getActionBar().setTitle("Parámetros");
		
		//Recibo los parametros de la actividad MainLogin el usuario y el nombre de quien inicia sesi�n
		usuario = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");
		
		ETIPWS = (EditText)findViewById(R.id.txtIPWS);
		ETUSER = (EditText)findViewById(R.id.txtUserVen);
		ETCLAVE = (EditText)findViewById(R.id.txtClaveVen);
		ETIMPRESORA = (EditText)findViewById(R.id.txtNombreImpresora);
		ETIPWSB = findViewById(R.id.txtIPWSB);
		TXTAPI = findViewById(R.id.txtApiBirobid);
		TXTRUC = findViewById(R.id.txtRuc);
		TXTSUCURSAL = findViewById(R.id.txtSucursal);

		constante = new Constantes();
		try{
			dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
			db = dbcon.getWritableDatabase();
		}catch(Exception e){
			misDialogos.miDialogoToastLargo(this, "Excepción generada al conectarse a la Base de datos: Clase: MainLogin --> " + e.getMessage());
			return;
		}
		//Tomo el dato del WS
		selectSQL = "SELECT IP_WEBSERVICE,IMPRESORA, IP_WEBSERVICE_BIROBID, IP_API, RUC, SUCURSAL  FROM PARAMETROS";
		if(db!=null)
		{
			c = db.rawQuery(selectSQL, null);
			if(c.moveToFirst())
			{
				constante.IPWS = c.getString(0);
				this.ETIMPRESORA.setText(c.getString(1));
				constante.IPWSB = c.getString(2);
				constante.IPAPI = c.getString(3);
				constante.RUC = c.getString(4);
				constante.SUCURSAL = c.getString(5);
			}
			else
			{
				this.ETIMPRESORA.setText("BlueTooth Printer");
			}
				
		}
		this.ETIPWS.setText(constante.IPWS);
		this.ETIPWSB.setText(constante.IPWSB);
		this.TXTAPI.setText(constante.IPAPI);
		this.TXTRUC.setText(constante.RUC);
		this.TXTSUCURSAL.setText(constante.SUCURSAL);
		//Tomo los datos del vendedor
		selectSQL = "SELECT codigo,clave FROM USUARIO where codigo='"+ usuario +"'";
		if(db!=null)
		{
			c = db.rawQuery(selectSQL, null);
			if(c.moveToFirst())
			{
				this.ETUSER.setText(c.getString(0));
				if(c.getString(1) == null)
					this.ETCLAVE.setText("");
				else
					this.ETCLAVE.setText(c.getString(1));
			}
		}
	}
	
	@SuppressLint({ "DefaultLocale", "NewApi" })
	public void grabar()
	{
		if(this.ETIPWS.getText().toString().trim().isEmpty() || this.ETIPWS.getText().toString().trim()==null)
		{
			misDialogos.dialogoAceptar(this,"Debe ingresar una dirección IP para la configuración del WS","Parámetros - "+ constante.tituloVentana);
			ETIPWS.requestFocus();
		}
		else if(this.ETUSER.getText().toString().trim().isEmpty() || this.ETUSER.getText().toString().trim()==null)
		{
			misDialogos.dialogoAceptar(this,"Debe ingresar un usuario para el ingreso al sistema","Parámetros - "+ constante.tituloVentana);
			ETUSER.requestFocus();
		}
		else if(this.ETIPWSB.getText().toString().trim().isEmpty() || this.ETIPWSB.getText().toString().trim()==null)
		{
			misDialogos.dialogoAceptar(this,"Debe ingresar una dirección IP para la configuración del WS BIROBID","Parámetros - "+ constante.tituloVentana);
			ETIPWSB.requestFocus();
		}
		else if(this.ETCLAVE.getText().toString().trim().isEmpty() || this.ETCLAVE.getText().toString().trim()==null)
		{
			misDialogos.dialogoAceptar(this,"Debe ingresar una clave para el ingreso al sistema","Parámetros - "+ constante.tituloVentana);
			ETCLAVE.requestFocus();
		}
		else if(this.TXTAPI.getText().toString().trim().isEmpty() || this.TXTAPI.getText().toString().trim()==null)
		{
			misDialogos.dialogoAceptar(this,"Debe ingresar una ip para la api de ubicacion","Parámetros - "+ constante.tituloVentana);
			TXTAPI.requestFocus();
		}
		else if(this.TXTRUC.getText().toString().trim().isEmpty() || this.TXTRUC.getText().toString().trim()==null)
		{
			misDialogos.dialogoAceptar(this,"Debe ingresar el R.U.C.","Parámetros - "+ constante.tituloVentana);
			TXTRUC.requestFocus();
		}
		else if(this.TXTSUCURSAL.getText().toString().trim().isEmpty() || this.TXTSUCURSAL.getText().toString().trim()==null)
		{
			misDialogos.dialogoAceptar(this,"Debe ingresar la sucursal","Parámetros - "+ constante.tituloVentana);
			TXTSUCURSAL.requestFocus();
		}
		else
		{
			//Actualiza los parametros
			scriptSQL="update PARAMETROS set IP_WEBSERVICE='"+ this.ETIPWS.getText().toString().trim() +"'";
			db.execSQL(scriptSQL);
			
			scriptSQL="update PARAMETROS set IMPRESORA='"+ this.ETIMPRESORA.getText().toString().trim() +"'";
			db.execSQL(scriptSQL);

			scriptSQL="update PARAMETROS set IP_WEBSERVICE_BIROBID ='"+ this.ETIPWSB.getText().toString().trim() +"'";
			db.execSQL(scriptSQL);

			scriptSQL="update PARAMETROS set IP_API ='"+ this.TXTAPI.getText().toString().trim() +"'";
			db.execSQL(scriptSQL);

			scriptSQL="update PARAMETROS set RUC ='"+ this.TXTRUC.getText().toString().trim() +"'";
			db.execSQL(scriptSQL);

			scriptSQL="update PARAMETROS set SUCURSAL ='"+ this.TXTSUCURSAL.getText().toString().trim() +"'";
			db.execSQL(scriptSQL);
			
			//Actualiza el usuario
			objetoUsuario = new Usuario(this.ETUSER.getText().toString().trim().toUpperCase(),
										this.ETUSER.getText().toString().trim().toUpperCase(),
										this.ETCLAVE.getText().toString().trim().toLowerCase(),
										"","","","","A","S","N","N","N",0,"N");
			
			scriptSQL = "UPDATE USUARIO SET codigo = '"+ objetoUsuario.getNombre() +"'," +
											"nombre='"+ objetoUsuario.getNombre() +"'," +
											"clave='"+ objetoUsuario.getClave() +"'," +
											"estado='"+ objetoUsuario.getEstado() +"'" +
											/*"crea_cliente='"+ objetoUsuario.getCreaCliente() +"'," +
											"coloca_descuento='"+ objetoUsuario.getColocaDescuento() +"', "+
											"edita_precio='"+ objetoUsuario.getEditaPrecio() +"', "+
											"es_autoventa='"+ objetoUsuario.getEsAutoVenta() +"', "+
											"bodega='"+ objetoUsuario.getBodega() +"', "+
											"ventop='"+ objetoUsuario.getVentop() +"' "+*/
						" where tipo = 'VEN'";
			db.execSQL(scriptSQL);
				
			scriptSQL = "update USUARIO_SECUENCIALES set codigo = '"+objetoUsuario.getCodigo()+"'";
			db.execSQL(scriptSQL);
			
			//Finaliza proceso
			misDialogos.dialogoAceptar(this,"Los parámetros han sido actualizados, por favor vuelva a iniciar sesión con el usuario que acaba de configurar","Parámetros - "+ constante.tituloVentana);
			
			finishAffinity();
			Intent intent = new Intent(this,MainLogin.class);
			startActivity(intent);
		}
	}
	
	//Este metodo es para inflar el menu y mostrar las opciones del menu en al action bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_parametros, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.accion_guardar:
			this.grabar();
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
			intent.putExtra("user", this.usuario);
			intent.putExtra("nombreUser", this.nombreUsuario);
			intent.putExtra("tipoUser", this.tipoUsuario);
			startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}