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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import clasesAdapter.ClientesAdapter;
import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.Clientes;
import misclases.DetallesPedidos;

@SuppressLint("NewApi")
public class MainVerClientes extends Activity {
	int textlength;
	String codigo,nombre,negocio,tipocli,observacion,codUser,nombreUsuario,tipoUsuario,txtCliente,selectClientes,numidcli, telfcli, celcli;
	ArrayList<Clientes> alClientes = new ArrayList<Clientes>();
	ArrayList<Clientes> alClientesBusqueda = new ArrayList<Clientes>();
	ArrayList<DetallesPedidos> alDetPedidos = new ArrayList<DetallesPedidos>();
	ListView lista;
	EditText EClientes;
	RadioGroup RGBusqueda;
	RadioButton RBCodigo,RBNombre, RBCedula;
	ClientesAdapter cAdapter;
	Intent intent;
	BDTablas dbcon;
	SQLiteDatabase db;
	Cursor c;
	Gson gson;
	Bundle bundle;
	Type type;
	LinearLayout llBusqueda;
	Constantes constante;
	Dialogos misDialogos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_ver_clientes);
		
		getActionBar().setTitle("Búsqueda de Clientes");
		//Recibo los datos del cliente para que no se pierda el valor al quere regresar a la actividad de pedidos
		codigo = getIntent().getStringExtra("codigoC");
		nombre = getIntent().getStringExtra("nombreC");
		negocio = getIntent().getStringExtra("negocioC");
		tipocli = getIntent().getStringExtra("tipoC");
		numidcli = getIntent().getStringExtra("numidC");
		telfcli = getIntent().getStringExtra("telfC");
		celcli = getIntent().getStringExtra("celC");

		
		observacion = getIntent().getStringExtra("observacionPed");
		codUser = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");
				
		//Recibo un json llamado "datosDetalles" con los detalles de los pedidos y lo almaceno al ArrayList
		//para que no se pierdan los datos
		bundle = this.getIntent().getExtras();
		gson = new Gson();
		String historial = bundle.getString("datosDetalles");
		type = new TypeToken<ArrayList<DetallesPedidos>>(){}.getType();
		alDetPedidos = gson.fromJson(historial, type);
		
		RGBusqueda = (RadioGroup)findViewById(R.id.rgrBuscarPor);
		RBCodigo = (RadioButton)findViewById(R.id.rbtCodigo);
		RBNombre = (RadioButton)findViewById(R.id.rbtNombre);
        RBCedula = (RadioButton)findViewById(R.id.rbtCedula);
		EClientes = (EditText)findViewById(R.id.txtBusquedaClientes);
		llBusqueda = (LinearLayout)findViewById(R.id.llBusqueda);
		txtCliente = "";
		
		constante = new Constantes();
		try{
			dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
			db = dbcon.getWritableDatabase();
		}catch(Exception e){
			misDialogos.miDialogoToastLargo(this, "Excepción generada al conectarse a la Base de datos: Clase: MainVerClientes --> " + e.getMessage());
			return;
		}
		if(db!=null)
		{
			selectClientes = "select codigo,nombre,negocio,tipo_cliente, numero_identificacion, telefono from CLIENTE where (usuario = '"+codUser+"' or vendedor_aux='"+codUser+"')";
			c = db.rawQuery(selectClientes, null);
			if(c.moveToFirst())
			{
				do
				{
					alClientes.add(new Clientes(c.getString(0),c.getString(1),c.getString(2),c.getString(3), c.getString(4), c.getString(5)));
				}while(c.moveToNext());
			}
		}
		
		lista = (ListView)findViewById(R.id.lvClientes);
		cAdapter = new ClientesAdapter(this, alClientes);
		lista.setAdapter(cAdapter);
		
		//Evento para cuando se da click en el ListView
		lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				codigo = cAdapter.getItem(i).getCodigo();
				nombre = cAdapter.getItem(i).getNombre();
				negocio = cAdapter.getItem(i).getNegocio();
				tipocli = cAdapter.getItem(i).getTipoCliente();
				celcli =  cAdapter.getItem(i).getNumeroIdentificacion();

				
				//Los almaceno en un ArrayList para luego convertirlo en Json
				gson = new Gson();
				String detallesJson = gson.toJson(alDetPedidos);
				
				//Esto sirve para cerrar la actividad y en caso de que vuelva atras no se muestre de nuevo esta actividad
				finish();
				
				intent = new Intent(getApplicationContext(),MainPedidos.class);
				intent.putExtra("codigoCli", codigo);
				intent.putExtra("nombreCli", nombre);
				intent.putExtra("direccionCli", negocio);
				intent.putExtra("tipoCli", tipocli);
				intent.putExtra("cedC", celcli);
				intent.putExtra("observacionPed", observacion);
				intent.putExtra("user", codUser);
				intent.putExtra("nombreUser", nombreUsuario);
				intent.putExtra("tipoUser", tipoUsuario);
				//Les paso los detalles almacenados en un Json
				intent.putExtra("datosDetalles", detallesJson);
				startActivity(intent);
			}
		});
		
		//Evento del RadioGroup para limpiar ListView
		RGBusqueda.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// Cuando hay un cambio de seleccion del radio button
				lista = (ListView)findViewById(R.id.lvClientes);
				cAdapter = new ClientesAdapter(getApplicationContext(), alClientes);
				lista.setAdapter(cAdapter);
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
					selectClientes = "SELECT codigo,nombre,negocio,tipo_cliente, numero_identificacion, telefono FROM cliente where nombre like '%"+txtCliente+"%' and (usuario = '"+codUser+"' or vendedor_aux='"+codUser+"')";
				}
				else if (RBCedula.isChecked())
				{
					selectClientes = "SELECT codigo,nombre,negocio,tipo_cliente, numero_identificacion, telefono FROM cliente where numero_identificacion like '%"+txtCliente+"%' and (usuario = '"+codUser+"' or vendedor_aux='"+codUser+"')";

				}
                else
                {
					selectClientes = "SELECT codigo,nombre,negocio,tipo_cliente, numero_identificacion, telefono FROM cliente where codigo like '%"+txtCliente+"%' and (usuario = '"+codUser+"' or vendedor_aux='"+codUser+"')";
                }
						
				alClientesBusqueda.clear();
				c = db.rawQuery(selectClientes, null);
				if(c.moveToFirst())
				{
					do{
						alClientesBusqueda.add(new Clientes(c.getString(0),c.getString(1),c.getString(2),c.getString(3), c.getString(4), c.getString(5)));
					}while(c.moveToNext());
				}
				lista = (ListView)findViewById(R.id.lvClientes);
				cAdapter = new ClientesAdapter(getApplicationContext(), alClientesBusqueda);
				lista.setAdapter(cAdapter);
			}
		});
	}
	
	//Este metodo es para inflar el menu y mostrar las opciones del menu en al action bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_ver_clientes, menu);
		return true;
	}
	
	//este metodo sirve para controlar las acciones de cada opci�n del men�
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