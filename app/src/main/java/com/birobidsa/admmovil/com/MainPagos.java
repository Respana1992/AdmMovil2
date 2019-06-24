package com.birobidsa.admmovil.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import clasesAdapter.MenuPedidoAdapter;
import dialogos.Dialogos;

@SuppressLint("NewApi")
public class MainPagos extends Activity {
	private String[] titulo = new String[]{"Registro Pago","Informe de Pago"};
	private int[] imagenes = new int[]{R.drawable.regpagos,R.drawable.elipagos};
	String usuario,nombreUsuario,tipoUsuario;
	TextView TUsuario;
	ListView lvMenuPago;
	MenuPedidoAdapter adapter;
	Intent intent;
    Dialogos dialogo = new Dialogos();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu_pago);

		getActionBar().setTitle("Menú de Pagos");

		//Recibo los parametros de la actividad MainLogin el usuario y el nombre de quien inicia sesi�n
		usuario = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");

		//Tomo el control TextView donde va el nombre del usuario y le asigno la variable usuario
		TUsuario = (TextView)findViewById(R.id.lblNombreUsuario);
		TUsuario.setText("Bienvenido: " + nombreUsuario + "-" + usuario);

		//Tomo el control ListView
		lvMenuPago = (ListView)findViewById(R.id.lvMenuPagos);
		adapter = new MenuPedidoAdapter(this, titulo, imagenes);
		lvMenuPago.setAdapter(adapter);

		lvMenuPago.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				switch (i) {
					case 0:
						//Llamo a la actividad de pedidos
						intent = new Intent(getApplicationContext(),MainRegistraPagos.class);
						intent.putExtra("user", usuario);
						intent.putExtra("nombreUser", nombreUsuario);
						intent.putExtra("tipoUser", tipoUsuario);
						startActivity(intent);
						break;
					case 1:
						intent = new Intent(getApplicationContext(),MainVerPagos.class);
						intent.putExtra("user", usuario);
						intent.putExtra("nombreUser", nombreUsuario);
						intent.putExtra("tipoUser", tipoUsuario);
						startActivity(intent);
                        //dialogo.miDialogoToastCorto(getApplicationContext(), "Opción aún no disponible");

                        break;
					case 2:
						/*intent = new Intent(getApplicationContext(),MainVerPedidosEnviados.class);
						intent.putExtra("user", usuario);
						intent.putExtra("nombreUser", nombreUsuario);
						intent.putExtra("tipoUser", tipoUsuario);
						startActivity(intent);*/
                        dialogo.miDialogoToastCorto(getApplicationContext(), "Opción aún no disponible");
						break;
					case 3:
						/*intent = new Intent(getApplicationContext(),MainInformesStock.class);
						intent.putExtra("user", usuario);
						intent.putExtra("nombreUser", nombreUsuario);
						intent.putExtra("tipoUser", tipoUsuario);
						startActivity(intent);*/
                        dialogo.miDialogoToastCorto(getApplicationContext(), "Opción aún no disponible");

                        break;
				}
			}
		});
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