package com.birobidsa.admmovil.com;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import clasesAdapter.ItemAdapterStock;
import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.DetallesPedidos;
import misclases.Item;
import procesos.PRECIOITEM;

public class MainInformesStock extends Activity {
	double precio;
	int factor,stock,numprecio;
	ArrayList<Item> alItem = new ArrayList<Item>();
	ArrayList<Item> alItemBusqueda = new ArrayList<Item>();
	ArrayList<DetallesPedidos> alDetPedidos = new ArrayList<DetallesPedidos>();
	String codigo,nombre,aplicaIva,tipoItem,codigoCliente,nombreCliente,negocioCliente,tipoCliente,observacion,usuario,nombreUsuario,tipoUsuario,selectSql,txtItem;
	String mensaje,esVenTop;
	ListView lista;
	EditText EItem;
	RadioGroup RGBusqueda;
	RadioButton RBCodigo,RBNombre;
	ItemAdapterStock iAdapter;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_informes_stock);		
		
		getActionBar().setTitle("Informe de Stock");
		
		//Recibo los datos del cliente para que no se pierda el valor al quere regresar a la actividad de pedidos
		usuario = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");
		
		RGBusqueda = (RadioGroup)findViewById(R.id.rgrBuscarPor);
		RBCodigo = (RadioButton)findViewById(R.id.rbtCodigo);
		RBNombre = (RadioButton)findViewById(R.id.rbtNombre);
		EItem = (EditText)findViewById(R.id.txtBusquedaItem);
		llBusqueda = (LinearLayout)findViewById(R.id.llBusqueda);
		txtItem = "";
		numprecio=1;
		
		//Me conecto a la base para consultar y mostrar los item
		constante = new Constantes();
		try{
			dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
			db = dbcon.getWritableDatabase();
		}catch(Exception e){
			dialogo.miDialogoToastLargo(this, "Excepción generada al conectarse a la Base de datos: Clase: MainInformeStock --> " + e.getMessage());
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
			if(esVenTop.equals("S"))
			{
				selectSql="SELECT i.item,i.nombre,i.factor,i.stock,i.precio1,i.iva,i.bien,i.poruti,i.costop FROM ITEM i,ITEMTOP t where i.item=t.item and t.vendedor='"+ usuario +"' order by i.nombre";
			}
			else
			{
				selectSql = "SELECT item,nombre,factor,stock,precio1,iva,bien,poruti,costop FROM ITEM order by nombre";
			}
			
			c = db.rawQuery(selectSql, null);
			if(c.moveToFirst())
			{
				do
				{
					alItem.add(new Item(c.getString(0),c.getString(1),c.getInt(2),c.getInt(3),c.getDouble(4),c.getString(5),c.getString(6),c.getDouble(7),c.getDouble(8)));
				}while(c.moveToNext());
			}
		}
		//Muestro los item tra�dos de la base en un listView
		lista = (ListView)findViewById(R.id.lvItem);
		iAdapter = new ItemAdapterStock(this, alItem);
		lista.setAdapter(iAdapter);
		
		//Implemento el evento OnClick del listView, para cuando selecciono un �tem
		lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				/*
				//Tomo los valores seleccionados desde el listView
				codigo = iAdapter.getItem(i).getItem();
				nombre = iAdapter.getItem(i).getNombre();
				factor = iAdapter.getItem(i).getFactor();
				stock =  iAdapter.getItem(i).getStock();
				precio = iAdapter.getItem(i).getPrecioPed();
				aplicaIva = iAdapter.getItem(i).getIva();
				tipoItem = iAdapter.getItem(i).getBien();
				
				Toast.makeText(getApplicationContext(), "El precio del item es "+precio, Toast.LENGTH_SHORT).show();
					
				//Esto sirve para cerrar la actividad y en caso de que vuelva atras no se muestre de nuevo esta actividad
				finish();
					
				//Llamo a la actividade de pedidos y les paso los valores
				intent = new Intent(getApplicationContext(),MainPedidos.class);
				intent.putExtra("user", usuario);
				intent.putExtra("nombreUser", nombreUsuario);
				intent.putExtra("tipoUser", tipoUsuario);
				//Inicio la actividad
				startActivity(intent);
				*/
			}
		});
		
		//Implementar evento del radio group para cuando el metodo de busqueda
		RGBusqueda.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// Cuando hay un cambio de seleccion del radio button
				lista = (ListView)findViewById(R.id.lvItem);
				iAdapter = new ItemAdapterStock(getApplicationContext(), alItem);
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
					selectSql = "SELECT item,nombre,factor,stock,precio1,iva,bien,poruti,costop FROM item where nombre like '%"+txtItem+"%' order by nombre";
				}
				else
				{
					selectSql = "SELECT item,nombre,factor,stock,precio1,iva,bien,poruti,costop FROM item where item like '%"+txtItem+"%' order by nombre";
				}
				
				alItemBusqueda.clear();
				c = db.rawQuery(selectSql, null);
				if(c.moveToFirst())
				{
					do{
						alItemBusqueda.add(new Item(c.getString(0),c.getString(1),c.getInt(2),c.getInt(3),c.getDouble(4),c.getString(5),c.getString(6),c.getDouble(7),c.getDouble(8)));
					}while(c.moveToNext());
				}
				lista = (ListView)findViewById(R.id.lvItem);
				iAdapter = new ItemAdapterStock(getApplicationContext(), alItemBusqueda);
				lista.setAdapter(iAdapter);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_informes_stock, menu);
		return true;
	}

	//este metodo sirve para controlar las acciones de cada opci�n del men�
		@SuppressLint("NewApi")
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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// Esto es para cuando de clic en el bo�n de atr�s no regrese a la pantalla de login
	        finish();
	        Intent intent = new Intent(this,MainMenuPedido.class);
			intent.putExtra("user", usuario);
			intent.putExtra("nombreUser", nombreUsuario);
			intent.putExtra("tipoUser", tipoUsuario);
			startActivity(intent);
	    }
	    return super.onKeyDown(keyCode, event);
	}
}