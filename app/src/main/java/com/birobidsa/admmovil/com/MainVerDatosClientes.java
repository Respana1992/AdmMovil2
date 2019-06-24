package com.birobidsa.admmovil.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.util.ArrayList;

import clasesAdapter.VerDatosClientesAdapter;
import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.Clientes;

@SuppressLint("NewApi")
public class MainVerDatosClientes extends Activity {
	String codUser,nombreUsuario,tipoUsuario,scriptSQL,selectClientes,txtCliente;
	ArrayList<Clientes> alClientes = new ArrayList<Clientes>();
	ArrayList<Clientes> alClientesBusqueda = new ArrayList<Clientes>();
	VerDatosClientesAdapter cliAdapter;
	ListView lvClientes;
	Constantes constante = new Constantes();
	Dialogos dialogo = new Dialogos();
	Intent intent;
	BDTablas dbcon;
	SQLiteDatabase db;
	Cursor c;
	Clientes cli;
	LinearLayout llBusqueda;
	EditText EClientes;
	RadioGroup RGBusqueda;
	RadioButton RBCodigo,RBNombre, RBCedula;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_ver_datos_clientes);
		
		codUser = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");
		
		getActionBar().setTitle("Clientes del " + codUser);
		
		lvClientes = (ListView)findViewById(R.id.lvClientes);
		EClientes = (EditText)findViewById(R.id.busquedaCliente);
		llBusqueda = (LinearLayout)findViewById(R.id.llBusqueda);
		
		RGBusqueda = (RadioGroup)findViewById(R.id.rgrBuscarPor);
		RBCodigo = (RadioButton)findViewById(R.id.rbtCodigo);
		RBNombre = (RadioButton)findViewById(R.id.rbtNombre);
		RBCedula = (RadioButton)findViewById(R.id.rbtCedula);
		EClientes = (EditText)findViewById(R.id.txtBusquedaClientes);
		
		try{
			dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
			db = dbcon.getWritableDatabase();
		}catch(Exception e){
			dialogo.miDialogoToastLargo(getApplicationContext(), "Error al conectarse a la BD: Clase MainVerDatosClientes --> " + e.getMessage());
			return;
		}
		
		buscaClientes();
		
		//Evento del RadioGroup para limpiar ListView
		RGBusqueda.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// Cuando hay un cambio de seleccion del radio button
				lvClientes = (ListView)findViewById(R.id.lvClientes);
				cliAdapter = new VerDatosClientesAdapter(getApplicationContext(), alClientes);
				lvClientes.setAdapter(cliAdapter);
				EClientes.setText("");
				/*if(checkedId == R.id.rbtCodigo)*/
			}
		});
		
		
		//Evento del EditText cuando se escribe en el control
		EClientes.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
							
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override //buscador de cliente
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				selectClientes = "";
				txtCliente = EClientes.getText().toString();
						
				if(RBNombre.isChecked())
				{
					selectClientes = "SELECT codigo,nombre,negocio,representa,tipo_identificacion,numero_identificacion,direccion," +
									 "telefono,email,tipo_cliente,provincia,canton,parroquia,sector,ruta,cupo,grupo_descuento,orden," +
									 "frec_visita,credito,dia,fecha_desde,fecha_ingreso,hora_ingreso,fecha_ult_com,td_credito," +
									 "dias_credito,usuario,forma_pago,iva,con_final,clase,observacion,tipo_negocio,ejex,ejey," +
									 "vendedor_aux,fecha_modifica,hora_modifica,usuario_modifica,fecha_elimina,hora_elimina," +
									 "usuario_elimina,estado,cltenuevo,descargado,clavefe FROM cliente where nombre like '%"+txtCliente+"%' and (usuario = '"+codUser+"' or vendedor_aux='"+codUser+"')";
				}
				else if (RBCedula.isChecked()){
					selectClientes = "SELECT codigo,nombre,negocio,representa,tipo_identificacion,numero_identificacion,direccion," +
							"telefono,email,tipo_cliente,provincia,canton,parroquia,sector,ruta,cupo,grupo_descuento,orden," +
							"frec_visita,credito,dia,fecha_desde,fecha_ingreso,hora_ingreso,fecha_ult_com,td_credito," +
							"dias_credito,usuario,forma_pago,iva,con_final,clase,observacion,tipo_negocio,ejex,ejey," +
							"vendedor_aux,fecha_modifica,hora_modifica,usuario_modifica,fecha_elimina,hora_elimina," +
							"usuario_elimina,estado,cltenuevo,descargado,clavefe FROM cliente where numero_identificacion like '%"+txtCliente+"%' and (usuario = '"+codUser+"' or vendedor_aux='"+codUser+"')";
				}
				else
				{
					selectClientes = "SELECT codigo,nombre,negocio,representa,tipo_identificacion,numero_identificacion,direccion," +
									 "telefono,email,tipo_cliente,provincia,canton,parroquia,sector,ruta,cupo,grupo_descuento,orden," +
									 "frec_visita,credito,dia,fecha_desde,fecha_ingreso,hora_ingreso,fecha_ult_com,td_credito," +
									 "dias_credito,usuario,forma_pago,iva,con_final,clase,observacion,tipo_negocio,ejex,ejey," +
									 "vendedor_aux,fecha_modifica,hora_modifica,usuario_modifica,fecha_elimina,hora_elimina," +
									 "usuario_elimina,estado,cltenuevo,descargado,clavefe FROM cliente where nombre like '%"+txtCliente+"%' and (usuario = '"+codUser+"' or vendedor_aux='"+codUser+"')";
				}
								
				alClientesBusqueda.clear();
				c = db.rawQuery(selectClientes, null);
				if(c.moveToFirst())
				{
					do{
						alClientesBusqueda.add(new Clientes(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),
								   							c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),
								   							c.getString(10),c.getString(11),c.getString(12),c.getString(13),c.getString(14),
								   							c.getString(15),c.getString(16),Integer.parseInt(c.getString(17)),c.getString(18),
								   							c.getString(19),Integer.parseInt(c.getString(20)),c.getString(21),c.getString(22),
								   							c.getString(22),c.getString(24),c.getString(25),Integer.parseInt(c.getString(26)),
								   							c.getString(27),c.getString(28),c.getString(29),c.getString(30),c.getString(31),
								   							c.getString(32),c.getString(33),c.getString(34),c.getString(35),c.getString(36),
								   							c.getString(37),c.getString(38),c.getString(39),c.getString(40),c.getString(41),
								   							c.getString(42),c.getString(43),c.getString(44),c.getString(45),c.getString(46)));
					}while(c.moveToNext());
				}
				lvClientes = (ListView)findViewById(R.id.lvClientes);
				cliAdapter = new VerDatosClientesAdapter(getApplicationContext(), alClientesBusqueda);
				lvClientes.setAdapter(cliAdapter);
			}
		});
	}

	private void buscaClientes()
	{
		try{
			if(db!=null)
			{
				scriptSQL = "SELECT codigo,nombre,negocio,representa,tipo_identificacion,numero_identificacion,direccion," +
							"telefono,email,tipo_cliente,provincia,canton,parroquia,sector,ruta,cupo,grupo_descuento,orden," +
						    "frec_visita,credito,dia,fecha_desde,fecha_ingreso,hora_ingreso,fecha_ult_com,td_credito," +
							"dias_credito,usuario,forma_pago,iva,con_final,clase,observacion,tipo_negocio,ejex,ejey," +
						    "vendedor_aux,fecha_modifica,hora_modifica,usuario_modifica,fecha_elimina,hora_elimina," +
							"usuario_elimina,estado,cltenuevo,descargado,clavefe FROM cliente where (usuario = '"+codUser+"' or vendedor_aux='"+codUser+"')";
				try{
					c = db.rawQuery(scriptSQL, null);
				}catch(Exception e){
					dialogo.miDialogoToastLargo(getApplicationContext(), "Error al ejecutar el script de pedido: Clase MainVerDatosClientes --> " + e.getMessage());
					return;
				}
				
				if(c.moveToFirst())
				{
					do{
						cli = new Clientes(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),
										   c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),
										   c.getString(10),c.getString(11),c.getString(12),c.getString(13),c.getString(14),
										   c.getString(15),c.getString(16),Integer.parseInt(c.getString(17)),c.getString(18),
										   c.getString(19),Integer.parseInt(c.getString(20)),c.getString(21),c.getString(22),
										   c.getString(22),c.getString(24),c.getString(25),Integer.parseInt(c.getString(26)),
										   c.getString(27),c.getString(28),c.getString(29),c.getString(30),c.getString(31),
										   c.getString(32),c.getString(33),c.getString(34),c.getString(35),c.getString(36),
										   c.getString(37),c.getString(38),c.getString(39),c.getString(40),c.getString(41),
										   c.getString(42),c.getString(43),c.getString(44),c.getString(45),c.getString(46));
						alClientes.add(cli);
					}while(c.moveToNext());
					
					EClientes = (EditText)findViewById(R.id.busquedaCliente);
					cliAdapter = new VerDatosClientesAdapter(this, alClientes);
					lvClientes.setAdapter(cliAdapter);
				}
				else
				{
					dialogo.miDialogoToastCorto(getApplicationContext(), "No hay clientes que mostrar");
				}
			}
			else
			{
				dialogo.miDialogoToastCorto(getApplicationContext(), "No se pudo abrir la conexión a la base de datos");
			}
		}catch(Exception e)
		{
			dialogo.miDialogoToastLargo(getApplicationContext(), "Error al abrir la BD despues de la conexión: Clase MainVerDatosClientes --> " + e.getMessage());
			return;
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_ver_datos_clientes, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case R.id.accion_buscar:
				if(llBusqueda.getVisibility()==View.GONE)
				{
					llBusqueda.setVisibility(View.VISIBLE);
					EClientes.setVisibility(View.VISIBLE);
				}
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
}
