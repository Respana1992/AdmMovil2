package com.birobidsa.admmovil.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import clasesAdapter.MenuPedidoAdapter;

@SuppressLint("NewApi")
public class MainMenuPedido extends Activity {
	private String[] titulo = new String[]{"Iniciar Pedido","Pedidos por Enviar","Informes de Pedidos Enviados","Informes de Stock"};
	private int[] imagenes = new int[]{R.drawable.pedidos,R.drawable.verpedidos,R.drawable.verpedidos,R.drawable.verpedidos};
	String usuario,nombreUsuario,tipoUsuario;
	TextView TUsuario;
	ListView lvMenuPedido;
	MenuPedidoAdapter adapter;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu_pedido);
		
		getActionBar().setTitle("Menú de Pedidos");
		
		//Recibo los parametros de la actividad MainLogin el usuario y el nombre de quien inicia sesi�n
		usuario = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");
		
		//Tomo el control TextView donde va el nombre del usuario y le asigno la variable usuario
		TUsuario = (TextView)findViewById(R.id.lblNombreUsuario);
		TUsuario.setText("Bienvenido: " + nombreUsuario + "-" + usuario);
				
		//Tomo el control ListView
		lvMenuPedido = (ListView)findViewById(R.id.lvMenuPedidos);
		adapter = new MenuPedidoAdapter(this, titulo, imagenes);
		lvMenuPedido.setAdapter(adapter);
		
		lvMenuPedido.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				switch (i) {
				case 0:
					//Llamo a la actividad de pedidos
					intent = new Intent(getApplicationContext(),MainPedidos.class);
					intent.putExtra("user", usuario);
					intent.putExtra("nombreUser", nombreUsuario);
					intent.putExtra("tipoUser", tipoUsuario);
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(getApplicationContext(),MainVerPedidos.class);
					intent.putExtra("user", usuario);
					intent.putExtra("nombreUser", nombreUsuario);
					intent.putExtra("tipoUser", tipoUsuario);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(getApplicationContext(),MainVerPedidosEnviados.class);
					intent.putExtra("user", usuario);
					intent.putExtra("nombreUser", nombreUsuario);
					intent.putExtra("tipoUser", tipoUsuario);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(getApplicationContext(),MainInformesStock.class);
					intent.putExtra("user", usuario);
					intent.putExtra("nombreUser", nombreUsuario);
					intent.putExtra("tipoUser", tipoUsuario);
					startActivity(intent);
					break;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu_pedido, menu);
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