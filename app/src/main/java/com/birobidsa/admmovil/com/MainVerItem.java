 package com.birobidsa.admmovil.com;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import android.widget.Toast;
import clasesAdapter.ItemAdapter;
import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.Clientes;
import misclases.DetallesPedidos;
import misclases.Item;
import procesos.PRECIOITEM;
import procesos.VARIOS;

@SuppressLint("NewApi")
public class MainVerItem extends Activity {
	double precio,precioItem,poruti,costop;
	int factor,stock,numprecio;
	boolean clienteitem;
	ArrayList<Item> alItem = new ArrayList<Item>();
	ArrayList<Item> alItemBusqueda = new ArrayList<Item>();
	ArrayList<Item> alItemXCliente = new ArrayList<Item>();
	ArrayList<DetallesPedidos> alDetPedidos = new ArrayList<DetallesPedidos>();
	String codigo,nombre,aplicaIva,tipoItem,codigoCliente,nombreCliente,negocioCliente,tipoCliente,observacion,usuario,nombreUsuario,tipoUsuario,selectSql,txtItem;
	String mensaje,fechaSistema,esAutoVenta,esVenTop;
	ListView lista;
	EditText EItem;
	RadioGroup RGBusqueda;
	RadioButton RBCodigo,RBNombre;
	ItemAdapter iAdapter;
	Intent intent;
	Gson gson;
	Bundle bundle;
	Type type;
	BDTablas dbcon;
	SQLiteDatabase db;
	Cursor c;
	LinearLayout llBusqueda;
	PRECIOITEM preitem;
	Dialogos dialogo = new Dialogos();
	Constantes constante;
	Clientes cli;
	VARIOS varios;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_ver_item);
		
		getActionBar().setTitle("Búsqueda de Productos");
		
		//Recibo los datos del cliente para que no se pierda el valor al quere regresar a la actividad de pedidos
		codigoCliente = getIntent().getStringExtra("codigoC");
		nombreCliente = getIntent().getStringExtra("nombreC");
		negocioCliente = getIntent().getStringExtra("negocioC");
		tipoCliente = getIntent().getStringExtra("tipoC");
		
		observacion = getIntent().getStringExtra("observacionPed");
		usuario = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");
		esAutoVenta = getIntent().getStringExtra("esautoventa");
		
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
		EItem = (EditText)findViewById(R.id.txtBusquedaItem);
		llBusqueda = (LinearLayout)findViewById(R.id.llBusqueda);
		txtItem = "";
		numprecio=1;
		precioItem=0;
		poruti=0;
		costop=0;
		clienteitem=false;
		cli = new Clientes();
		varios = new VARIOS();
		
		//Me conecto a la base para consultar y mostrar los item
		constante = new Constantes();
		try{
			dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
			db = dbcon.getWritableDatabase();
		}catch(Exception e){
			dialogo.miDialogoToastLargo(this, "Excepción generada al conectarse a la Base de datos: Clase: MainVerItem --> " + e.getMessage());
			return;
		}
		
		if(db!=null)
		{
			//buscamos si el usuario es top
			selectSql="SELECT ventop FROM usuario where codigo='"+ usuario +"'";
			c = db.rawQuery(selectSql, null);
			if(c.moveToFirst())
			{
				esVenTop = c.getString(0);
			}
			
			preitem = new PRECIOITEM(getApplicationContext(),db);
			fechaSistema = varios.dameFechaSistema();
			clienteitem = cli.getClienteTienePrecioXItem(getApplicationContext(), db, usuario, fechaSistema);
			if(clienteitem)
			{
				alItemXCliente = preitem.getPrecioClienteXItem(codigoCliente, fechaSistema);
			}

			numprecio = preitem.precioPorTipoCliente(tipoCliente);
			
			if(esAutoVenta.equals("S"))
				selectSql = "SELECT item,nombre,factor,stock,precio"+numprecio+",iva,bien,poruti,costop FROM ITEM where stock>0 order by nombre";
			else
				selectSql = "SELECT item,nombre,factor,stock,precio"+numprecio+",iva,bien,poruti,costop FROM ITEM order by nombre";
			
			
			//dialogo.miDialogoToastLargo(getApplicationContext(),"es vendedor top: "+esVenTop);
			if(esVenTop.equals("S"))
			{
				selectSql="";	
				selectSql="SELECT i.item,i.nombre,i.factor,i.stock,i.precio"+numprecio+",i.iva,i.bien,i.poruti,i.costop FROM ITEM i,ITEMTOP t where i.item=t.item and t.vendedor='"+ usuario +"' order by i.nombre";
			}
			
			c = db.rawQuery(selectSql, null);
			if(c.moveToFirst())
			{
				do
				{
					precioItem = c.getDouble(4);
					for(int f=0; f<alItemXCliente.size(); f++)
					{
						if(alItemXCliente.get(f).getItem().equals(c.getString(0)))
						{
							precioItem = alItemXCliente.get(f).getPrecioItemXCliente(); 
						}
					}
					
					alItem.add(new Item(c.getString(0),c.getString(1),c.getInt(2),c.getInt(3),precioItem,c.getString(5),c.getString(6),c.getDouble(7),c.getDouble(8)));
				}while(c.moveToNext());
			}
		}
		//Muestro los item tra�dos de la base en un listView
		lista = (ListView)findViewById(R.id.lvItem);
		iAdapter = new ItemAdapter(this, alItem);
		lista.setAdapter(iAdapter);
		
		//Implemento el evento OnClick del listView, para cuando selecciono un �tem
		lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				//Tomo los valores seleccionados desde el listView
				codigo = iAdapter.getItem(i).getItem();
				nombre = iAdapter.getItem(i).getNombre();
				factor = iAdapter.getItem(i).getFactor();
				stock =  iAdapter.getItem(i).getStock();
				precio = iAdapter.getItem(i).getPrecioPed();
				aplicaIva = iAdapter.getItem(i).getIva();
				tipoItem = iAdapter.getItem(i).getBien();
				poruti = iAdapter.getItem(i).getPoruti();
				costop = iAdapter.getItem(i).getCostoP();
				
				Toast.makeText(getApplicationContext(), "El precio del item es "+precio, Toast.LENGTH_SHORT).show();
				
				if(precio > 0)
				{
					//Los almaceno en un ArrayList para luego convertirlo en Json
					gson = new Gson();
					String detallesJson = gson.toJson(alDetPedidos);
					
					//Esto sirve para cerrar la actividad y en caso de que vuelva atras no se muestre de nuevo esta actividad
					finish();
					
					//Llamo a la actividade de pedidos y les paso los valores
					intent = new Intent(getApplicationContext(),MainPedidos.class);
					intent.putExtra("codigoItem", codigo);
					intent.putExtra("nombreItem", nombre);
					intent.putExtra("factorItem", String.valueOf(factor));
					intent.putExtra("stockItem", String.valueOf(stock));
					intent.putExtra("precioItem", String.valueOf(precio));
					intent.putExtra("aplicaIvaItem", aplicaIva);
					intent.putExtra("tipoItem", tipoItem);
					intent.putExtra("porutiItem", String.valueOf(poruti));
					intent.putExtra("costopItem", String.valueOf(costop));
					intent.putExtra("observacionPed", observacion);
					intent.putExtra("user", usuario);
					intent.putExtra("nombreUser", nombreUsuario);
					intent.putExtra("tipoUser", tipoUsuario);
					//Env�o los datos del cliente para que no se pierda el valor al quere regresar a la actividad de pedidos				
					intent.putExtra("codigoCli", codigoCliente);
					intent.putExtra("nombreCli", nombreCliente);
					intent.putExtra("negocioCli", negocioCliente);
					intent.putExtra("tipoCli", tipoCliente);
					//Les paso los detalles almacenados en un Json
					intent.putExtra("datosDetalles", detallesJson);
					//Inicio la actividad
					startActivity(intent);
				}
				else
				{
					mensaje  = "El item " + codigo + " - " + nombre + " no puede ser seleccionado por no tener precio";
					dialogo.miDialogoToastLargo(getApplicationContext(),mensaje);
				}
			}
		});
		
		//Implementar evento del radio group para cuando el metodo de busqueda
		RGBusqueda.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// Cuando hay un cambio de seleccion del radio button
				lista = (ListView)findViewById(R.id.lvItem);
				iAdapter = new ItemAdapter(getApplicationContext(), alItem);
				lista.setAdapter(iAdapter);
				EItem.setText("");
				/*if(checkedId == R.id.rbtCodigo)*/
			}
		});
		
		//Implementando evento del EdiText para cuando esta escribiendo
		EItem.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				selectSql = "";
				txtItem = EItem.getText().toString();
				
				if(RBNombre.isChecked())
				{
					if(esAutoVenta.equals("S"))
						selectSql = "SELECT item,nombre,factor,stock,precio"+numprecio+",iva,bien,poruti,costop FROM item where stock>0 and nombre like '%"+txtItem+"%' order by nombre";
					else if(esVenTop.equals("S") && esAutoVenta.equals("N"))
						selectSql="SELECT i.item,i.nombre,i.factor,i.stock,i.precio"+numprecio+",i.iva,i.bien,i.poruti,i.costop FROM ITEM i,ITEMTOP t where i.item=t.item and t.vendedor='"+ usuario +"' and i.nombre like '%"+txtItem+"%' order by i.nombre";
					else
						selectSql = "SELECT item,nombre,factor,stock,precio"+numprecio+",iva,bien,poruti,costop FROM item where nombre like '%"+txtItem+"%' order by nombre";
				}
				else
				{
					if(esAutoVenta.equals("S"))
						selectSql = "SELECT item,nombre,factor,stock,precio"+numprecio+",iva,bien,poruti,costop FROM item where stock>0 and item like '%"+txtItem+"%' order by nombre";
					else if(esVenTop.equals("S") && esAutoVenta.equals("N"))
						selectSql="SELECT i.item,i.nombre,i.factor,i.stock,i.precio"+numprecio+",i.iva,i.bien,i.poruti,i.costop FROM ITEM i,ITEMTOP t where i.item=t.item and t.vendedor='"+ usuario +"' and i.item like '%"+txtItem+"%' order by i.nombre";
					else
						selectSql = "SELECT item,nombre,factor,stock,precio"+numprecio+",iva,bien,poruti,costop FROM item where item like '%"+txtItem+"%' order by nombre";
				}
				
				alItemBusqueda.clear();
				c = db.rawQuery(selectSql, null);
				if(c.moveToFirst())
				{
					do{
						precioItem = c.getDouble(4);
						for(int f=0; f<alItemXCliente.size(); f++)
						{
							if(alItemXCliente.get(f).getItem().equals(c.getString(0)))
							{
								precioItem = alItemXCliente.get(f).getPrecioItemXCliente(); 
							}
						}
						alItemBusqueda.add(new Item(c.getString(0),c.getString(1),c.getInt(2),c.getInt(3),precioItem,c.getString(5),c.getString(6),c.getDouble(7),c.getDouble(8)));
					}while(c.moveToNext());
				}
				lista = (ListView)findViewById(R.id.lvItem);
				iAdapter = new ItemAdapter(getApplicationContext(), alItemBusqueda);
				lista.setAdapter(iAdapter);
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
					EItem.setVisibility(View.VISIBLE);
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