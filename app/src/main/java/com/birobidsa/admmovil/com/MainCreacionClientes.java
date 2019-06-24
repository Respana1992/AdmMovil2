package com.birobidsa.admmovil.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.CEDULAECUATORIANA;
import misclases.Clientes;
import procesos.VARIOS;

@SuppressLint("NewApi")
public class MainCreacionClientes extends Activity {
	String txtNombre,txtNegocio,txtDireccion,txtNumIdentificacion,txtEmail,tipoIdentificacion,insertSQL,tipoUsuario,confinal,telefono,parroquiacli,canton,provincia,
			referencia, observacioncli,zona;
	String tipoNego,tipoCli,grupoDscto,frecVis,ruta,usuario,nombreUsuario,fecha,hora,selectSQL,codCliente,numCliente,diacredito;
	String[] separadorCadena;
	String c_diascredito,c_tdcredito;
	Constantes constante;
	Dialogos misDialogos;
	VARIOS procesos;
	ArrayList<CEDULAECUATORIANA> alCedula = new ArrayList<CEDULAECUATORIANA>();
	int posTipoNego,posTipoCli,posGrupoDes,posFrecVis,posRuta,posCredito,posZona,posParroquia;
	EditText ENombre,ENegocio,EDireccion,ENumIdentificacion,EEmail,EReferencia, EZona, EObservacion, ETelefono;
	Clientes cliente;
	BDTablas dbcon;
	SQLiteDatabase db;
	Cursor c;
	RadioGroup rgrTipoIden;
	//Spinner spnZona;
	RadioButton rbtCedula,rbtRuc,rbtFinal;
	TextView TVTipoNegocio,TVTipoCliente,TVGrupoDescuento,TVFrecuenciaVisita,TVRuta,TVCredito,TVZona,TVParroquia;
	Intent intent;
	ArrayAdapter<String> adpTipoNegocio,adpTipoCliente,adpGrupoDscto,adpFrecVis,adpRuta,adpCredito, adpZona,adpParroquia;
	ArrayList<String> alTipoNego = new ArrayList<String>();
	ArrayList<String> alTipoCliente = new ArrayList<String>();
	ArrayList<String> alGrupoDscto = new ArrayList<String>();
	ArrayList<String> alZona = new ArrayList<String>();
	ArrayList<String> alFrecVis = new ArrayList<String>();
	ArrayList<String> alRuta = new ArrayList<String>();
	ArrayList<String> alCredito = new ArrayList<String>();
	ArrayList<String> alParroquias = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_creacion_clientes);

		getActionBar().setTitle("Creación de Clientes");

		ENombre= (EditText)findViewById(R.id.txtNombre);
		ENegocio= (EditText)findViewById(R.id.txtNegocio);
		EDireccion= (EditText)findViewById(R.id.txtDireccion);
		ENumIdentificacion= (EditText)findViewById(R.id.txtNumeroIdentificacion);
		EEmail= (EditText)findViewById(R.id.txtEmail);
		EReferencia = (EditText)findViewById(R.id.txtReferencia);
		ETelefono = (EditText)findViewById(R.id.txtTelefono);
		EReferencia = (EditText)findViewById(R.id.txtReferencia);
		EObservacion = (EditText)findViewById(R.id.txtObserva);
		rgrTipoIden = (RadioGroup)findViewById(R.id.rgrNumeroIdentificacion);
		rbtCedula = (RadioButton)findViewById(R.id.rbtCedula);
		rbtRuc = (RadioButton)findViewById(R.id.rbtRuc);
		rbtFinal = (RadioButton)findViewById(R.id.rbtConsumidorFinal);


		//Recibo los parametros de la actividad MainLogin el usuario y el nombre de quien inicia sesi�n
		usuario = getIntent().getStringExtra("user");
		nombreUsuario = getIntent().getStringExtra("nombreUser");
		tipoUsuario = getIntent().getStringExtra("tipoUser");

		constante = new Constantes();
		try{
			dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
			db = dbcon.getWritableDatabase();
		}catch(Exception e){
			misDialogos.miDialogoToastLargo(this, "Excepción generada al conectarse a la Base de datos: Clase: MainCreacionClientes --> " + e.getMessage());
			return;
		}

		posTipoNego=0;
		posTipoCli=0;
		posGrupoDes=0;
		posFrecVis=0;
		posRuta=0;
		posZona=0;
		posParroquia=0;
		
		if(db!=null)
		{
			//Carga tipo de negocio
			selectSQL="SELECT codigo,nombre FROM TIPO_NEGOCIO";
			c = db.rawQuery(selectSQL, null);
			if(c.moveToFirst())
			{
				do{
					alTipoNego.add(c.getString(0)+"-"+c.getString(1));
				}while(c.moveToNext());
			}
			adpTipoNegocio = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice,alTipoNego);
			
			//Carga tipo de cliente
			selectSQL="SELECT codigo,nombre FROM TIPO_CLIENTE";
			c = db.rawQuery(selectSQL, null);
			if(c.moveToFirst())
			{
				do{
					alTipoCliente.add(c.getString(0)+"-"+c.getString(1));
				}while(c.moveToNext());
			}
			adpTipoCliente = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice,alTipoCliente);
			
			//Carga de zona
			selectSQL="SELECT codigo,nombre FROM zona";
			c = db.rawQuery(selectSQL, null);
			if(c.moveToFirst())
			{
				do{
					alZona.add(c.getString(0)+"-"+c.getString(1));
				}while(c.moveToNext());
			}
			adpZona = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice,alZona);

			//Carga grupo de descuento o de cliente
			selectSQL="SELECT codigo,nombre FROM grupocli";
			c = db.rawQuery(selectSQL, null);
			if(c.moveToFirst())
			{
				do{
					alGrupoDscto.add(c.getString(0)+"-"+c.getString(1));
				}while(c.moveToNext());
			}
			adpGrupoDscto = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice,alGrupoDscto);


			//Carga frecuencia de visita
			selectSQL="SELECT codigo,nombre FROM FRECUENCIA_VISITA";
			c = db.rawQuery(selectSQL, null);
			if(c.moveToFirst())
			{
				do{
					alFrecVis.add(c.getString(0)+"-"+c.getString(1));
				}while(c.moveToNext());
			}
			adpFrecVis = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice,alFrecVis);
			
			//Carga ruta
			selectSQL="SELECT codigo,nombre FROM RUTA";
			c = db.rawQuery(selectSQL, null);
			if(c.moveToFirst())
			{
				do{
					alRuta.add(c.getString(0)+"-"+c.getString(1));
				}while(c.moveToNext());
			}
			adpRuta = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice,alRuta);

			//Carga Parroquia
			selectSQL="SELECT codigo,nombre FROM PARROQUIA";
			c = db.rawQuery(selectSQL, null);
			if(c.moveToFirst())
			{
				do{
					alParroquias.add(c.getString(0)+"-"+c.getString(1));
				}while(c.moveToNext());
			}
			adpParroquia = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice,alParroquias);
			
			//Carga diascredito
			selectSQL="SELECT codigo,nombre,dia FROM DIASCREDITO";
			c = db.rawQuery(selectSQL, null);
			if(c.moveToFirst())
			{
				do{
					alCredito.add(c.getString(0)+"-"+c.getString(1)+"-"+c.getString(2));
				}while(c.moveToNext());
			}
			adpCredito = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice,alCredito);
			
			selectSQL="select CODIGO_CLIENTE,(NUM_CLIENTE+1) NUM_CLIENTE from parametros";
			c = db.rawQuery(selectSQL, null);
			if(c.moveToFirst())
			{
				codCliente = c.getString(0);
				numCliente = String.valueOf(c.getInt(1));
				codCliente = codCliente + numCliente;
			}
		}
		
		//Opciones de dias de credito
				TVCredito = (TextView)findViewById(R.id.txtCredito);
				TVCredito.setOnClickListener(new AdapterView.OnClickListener() {
					@Override
					public void onClick(View v) {
		                AlertDialog opciones = new AlertDialog.Builder(MainCreacionClientes.this)
		        				.setTitle("Dias de Credito")
		        				.setSingleChoiceItems(adpCredito,posCredito,
		        						new DialogInterface.OnClickListener() {
		        							public void onClick(DialogInterface dialog,int selected) {
		        								try{
		        									posCredito = selected;
		        									TVCredito.setText(adpCredito.getItem(posCredito));
		        									dialog.dismiss();
		        								}catch(Exception e){
		        									Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		        								}
		        							}
		        						}).create();
		        		opciones.show();
					}
				});
		
		//Opciones de tipos de negocio
		TVTipoNegocio = (TextView)findViewById(R.id.txtTipoNegocio);
		TVTipoNegocio.setOnClickListener(new AdapterView.OnClickListener() {
			@Override
			public void onClick(View v) {
                AlertDialog opciones = new AlertDialog.Builder(MainCreacionClientes.this)
        				.setTitle("Tipo de negocio")
        				.setSingleChoiceItems(adpTipoNegocio,posTipoNego,
        						new DialogInterface.OnClickListener() {
        							public void onClick(DialogInterface dialog,int selected) {
        								try{
        									posTipoNego = selected;
        									TVTipoNegocio.setText(adpTipoNegocio.getItem(posTipoNego));
        									dialog.dismiss();
        								}catch(Exception e){
        									Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        								}
        							}
        						}).create();
        		opciones.show();
			}
		});
		
		//Opciones de tipos de clientes
		TVTipoCliente = (TextView)findViewById(R.id.txtTipoCliente);
		TVTipoCliente.setOnClickListener(new AdapterView.OnClickListener() {
			@Override
			public void onClick(View v) {
                AlertDialog opciones = new AlertDialog.Builder(MainCreacionClientes.this)
        				.setTitle("Tipo de cliente")
        				.setSingleChoiceItems(adpTipoCliente,posTipoCli,
        						new DialogInterface.OnClickListener() {
        							public void onClick(DialogInterface dialog,int selected) {
        								try{
        									posTipoCli = selected;
        									TVTipoCliente.setText(adpTipoCliente.getItem(posTipoCli));
        									dialog.dismiss();
        								}catch(Exception e){
        									Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        								}
        							}
        						}).create();
        		opciones.show();
			}
		});
		
		//Opciones de grupos de descuentos
		TVGrupoDescuento = (TextView)findViewById(R.id.txtGrupoDescuento);
		TVGrupoDescuento.setOnClickListener(new AdapterView.OnClickListener() {
			@Override
			public void onClick(View v) {
                AlertDialog opciones = new AlertDialog.Builder(MainCreacionClientes.this)
        				.setTitle("Grupos de descuento")
        				.setSingleChoiceItems(adpGrupoDscto,posGrupoDes,
        						new DialogInterface.OnClickListener() {
        							public void onClick(DialogInterface dialog,int selected) {
        								try{
        									posGrupoDes = selected;
        									TVGrupoDescuento.setText(adpGrupoDscto.getItem(posGrupoDes));
        									dialog.dismiss();
        								}catch(Exception e){
        									Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        								}
        							}
        						}).create();
        		opciones.show();
			}
		});
		
		//Opciones de frecuencia de visita
		TVFrecuenciaVisita = (TextView)findViewById(R.id.txtFrecuenciaVisita);
		TVFrecuenciaVisita.setOnClickListener(new AdapterView.OnClickListener() {
			@Override
			public void onClick(View v) {
                AlertDialog opciones = new AlertDialog.Builder(MainCreacionClientes.this)
        				.setTitle("Frecuencia de visitas")
        				.setSingleChoiceItems(adpFrecVis,posFrecVis,
        						new DialogInterface.OnClickListener() {
        							public void onClick(DialogInterface dialog,int selected) {
        								try{
        									posFrecVis = selected;
        									TVFrecuenciaVisita.setText(adpFrecVis.getItem(posFrecVis));
        									dialog.dismiss();
        								}catch(Exception e){
        									Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        								}
        							}
        						}).create();
        		opciones.show();
			}
		});
		
		//Opciones de rutas
		TVRuta = (TextView)findViewById(R.id.txtRuta);
		TVRuta.setOnClickListener(new AdapterView.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog opciones = new AlertDialog.Builder(MainCreacionClientes.this)
						.setTitle("Rutas")
						.setSingleChoiceItems(adpRuta,posRuta,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int selected) {
										try{
											posRuta = selected;
											TVRuta.setText(adpRuta.getItem(posRuta));
											dialog.dismiss();
										}catch(Exception e){
											Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
										}
									}
								}).create();
				opciones.show();
			}
		});

		//Opciones de zonas
		TVZona = (TextView) findViewById(R.id.txtZona);
		adpZona.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);

		TVZona.setOnClickListener(new AdapterView.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog opciones = new AlertDialog.Builder(MainCreacionClientes.this)
						.setTitle("Zonas")
						.setSingleChoiceItems(adpZona,posZona,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int selected) {
										try{
											posZona = selected;
											TVZona.setText(adpZona.getItem(posZona));
											dialog.dismiss();
										}catch(Exception e){
											Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
										}
									}
								}).create();
				opciones.show();
			}
		});

		//Opciones de parroquias
		TVParroquia = (TextView)findViewById(R.id.txtParroquia);
		TVParroquia.setOnClickListener(new AdapterView.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog opciones = new AlertDialog.Builder(MainCreacionClientes.this)
						.setTitle("Parroquias")
						.setSingleChoiceItems(adpParroquia,posParroquia,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int selected) {
										try{
											posParroquia = selected;
											TVParroquia.setText(adpParroquia.getItem(posParroquia));
											dialog.dismiss();
										}catch(Exception e){
											Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
										}
									}
								}).create();
				opciones.show();
			}
		});

		// Cuando hay un cambio de seleccion del radio button
		rgrTipoIden.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.rbtCedula)
				{
					ENumIdentificacion.setFilters( new InputFilter[] { new InputFilter.LengthFilter(10) } );
					ENumIdentificacion.setText("");
					ENumIdentificacion.setEnabled(true);
					confinal = "N";
				}
				else if(checkedId == R.id.rbtRuc)
				{
					ENumIdentificacion.setFilters( new InputFilter[] { new InputFilter.LengthFilter(13) } );
					ENumIdentificacion.setText("");
					ENumIdentificacion.setEnabled(true);
					confinal = "N";
				}
				else
				{
					ENumIdentificacion.setFilters( new InputFilter[] { new InputFilter.LengthFilter(13) } );
					ENumIdentificacion.setText("9999999999999");
					ENumIdentificacion.setEnabled(false);
					confinal = "S";
				}
			}
		});
	}
	
	@SuppressLint("DefaultLocale")
	public void crear_cliente()
	{
		constante = new Constantes();
		misDialogos = new Dialogos();
		procesos = new VARIOS(getApplicationContext(), db);
		
		txtNombre = ENombre.getText().toString();
		txtNegocio = ENegocio.getText().toString();
		txtDireccion = EDireccion.getText().toString();
		txtNumIdentificacion = ENumIdentificacion.getText().toString();
		txtEmail = EEmail.getText().toString();
		observacioncli = EObservacion.getText().toString();
		referencia = EReferencia.getText().toString();
		zona = TVZona.getText().toString();
		telefono = ETelefono.getText().toString();
		parroquiacli=TVParroquia.getText().toString();
		
		if(txtNombre.length()==0)
		{
			misDialogos.dialogoAceptar(this,"Por favor ingrese un nombre","Clientes - "+ constante.tituloVentana);
			ENombre.requestFocus();
		}
		else if(txtNegocio.length()==0)
		{
			misDialogos.dialogoAceptar(this,"Por favor ingrese un nombre de negocio","Clientes - "+ constante.tituloVentana);
			ENegocio.requestFocus();
		}
		else if(parroquiacli.length()==0)
		{
			misDialogos.dialogoAceptar(this,"Por favor seleccione una parroquia","Clientes - "+ constante.tituloVentana);
			TVParroquia.requestFocus();
		}
		else if(referencia.length()==0)
		{
			misDialogos.dialogoAceptar(this,"Por favor ingrese referencia","Clientes - "+ constante.tituloVentana);
			EReferencia.requestFocus();
		}
		else if(telefono.length()==0)
		{
			misDialogos.dialogoAceptar(this,"Por favor ingrese teléfono","Clientes - "+ constante.tituloVentana);
			ETelefono.requestFocus();
		}
		else if(txtDireccion.length()==0)
		{
			misDialogos.dialogoAceptar(this,"Por favor ingrese una dirección","Clientes - "+ constante.tituloVentana);
			EDireccion.requestFocus();
		}
		else if(txtNumIdentificacion.length()==0)
		{
			misDialogos.dialogoAceptar(this,"Por favor ingrese un número de identificación","Clientes - "+ constante.tituloVentana);
			ENumIdentificacion.requestFocus();
		}
		else if(TVTipoNegocio.getText().length()==0)
		{
			misDialogos.dialogoAceptar(this,"Por favor seleccione un tipo de negocio","Clientes - "+ constante.tituloVentana);
			TVTipoNegocio.requestFocus();
		}
		else if(TVTipoCliente.getText().length()==0)
		{
			misDialogos.dialogoAceptar(this,"Por favor seleccione un tipo de cliente","Clientes - "+ constante.tituloVentana);
			TVTipoCliente.requestFocus();
		}
		else if(TVFrecuenciaVisita.getText().length()==0)
		{
			misDialogos.dialogoAceptar(this,"Por favor seleccione una frecuencia de visita","Clientes - "+ constante.tituloVentana);
			TVFrecuenciaVisita.requestFocus();
		}
		else if(TVRuta.getText().length()==0)
		{
			misDialogos.dialogoAceptar(this,"Por favor seleccione una ruta","Clientes - "+ constante.tituloVentana);
			TVRuta.requestFocus();
		}
		else if(TVCredito.getText().length()==0)
		{
			misDialogos.dialogoAceptar(this,"Por favor seleccione un dia de credito","Clientes - "+ constante.tituloVentana);
			TVCredito.requestFocus();
		}
		else if(TVZona.getText().length()==0)
		{
			misDialogos.dialogoAceptar(this,"Por favor seleccione una zona","Clientes - "+ constante.tituloVentana);
			TVZona.requestFocus();
		}
		else
		{
			if((rbtCedula.isChecked() || rbtRuc.isChecked()) && procesos.existeCedula(txtNumIdentificacion))
			{
				Toast.makeText(getApplicationContext(), "Número de identificación ya se encuentra registrado", Toast.LENGTH_SHORT).show();
				ENumIdentificacion.requestFocus();
			}
			else
			{
				if(rbtCedula.isChecked())
					alCedula = procesos.validaCedulaEcuatoriana(txtNumIdentificacion);
				else if(rbtRuc.isChecked())
					alCedula = procesos.validaRUCEcuatoriana(txtNumIdentificacion);
				else
					alCedula.add(new CEDULAECUATORIANA(true, ""));	
				
				if(alCedula.get(0).getValida()==false)
				{
					Toast.makeText(getApplicationContext(), alCedula.get(0).getMensaje(), Toast.LENGTH_SHORT).show();
					ENumIdentificacion.requestFocus();
				}
				else
				{
					try{
						separadorCadena = this.TVTipoNegocio.getText().toString().split("-");
						this.tipoNego = separadorCadena[0];
						
						separadorCadena = this.TVTipoCliente.getText().toString().split("-");
						this.tipoCli = separadorCadena[0];
						
						separadorCadena = this.TVGrupoDescuento.getText().toString().split("-");
						this.grupoDscto = separadorCadena[0];
						
						separadorCadena = this.TVFrecuenciaVisita.getText().toString().split("-");
						this.frecVis = separadorCadena[0];
						
						separadorCadena = this.TVRuta.getText().toString().split("-");
						this.ruta = separadorCadena[0];
						
						separadorCadena = this.TVCredito.getText().toString().split("-");
						c_tdcredito = separadorCadena[0];
						c_diascredito = separadorCadena[2];

						separadorCadena = this.TVZona.getText().toString().split("-");
						this.zona = separadorCadena[0];

						separadorCadena = this.TVParroquia.getText().toString().split("-");
						this.parroquiacli = separadorCadena[0];
						
						rbtCedula = (RadioButton)findViewById(R.id.rbtCedula);
						rbtRuc = (RadioButton)findViewById(R.id.rbtRuc);
						rbtFinal = (RadioButton)findViewById(R.id.rbtConsumidorFinal);

						if(rbtCedula.isChecked()==true)
						{
							tipoIdentificacion="C";
						}
						else if(rbtRuc.isChecked()==true)
						{
							tipoIdentificacion="R";
						}
						else
						{
							tipoIdentificacion="F";
						}
						
						fecha = procesos.dameFechaSistema();
						hora = procesos.dameHoraSistema();
						cliente=new Clientes(this.codCliente,this.txtNombre.toUpperCase(),this.txtNegocio.toUpperCase(),this.txtDireccion.toUpperCase(),this.tipoNego,this.tipoIdentificacion,this.txtNumIdentificacion,this.txtEmail.toUpperCase(),this.tipoCli,this.grupoDscto,this.frecVis,this.ruta,"A",this.fecha,this.hora,"S",confinal);
						
						if(db!=null)
						{
							try{
								
								insertSQL="insert into CLIENTE(codigo,nombre,negocio,direccion,tipo_negocio,tipo_identificacion,numero_identificacion," +
							            "email,tipo_cliente,grupo_descuento,frec_visita,ruta,estado,fecha_desde,hora_ingreso,usuario,cltenuevo,descargado,"+
										"representa,orden,fecha_ingreso,fecha_ult_com,iva,clase,observacion,credito,td_credito,dias_credito," +
							            "con_final,forma_pago,sector,dia,telefono,referencia,zona,ejex,ejey,vendedor_aux,fecha_modifica,hora_modifica,usuario_modifica,fecha_elimina,hora_elimina,usuario_elimina,cupo,clavefe,fecha_desde) " +
										"values('"+cliente.getCodigo()+"','"+cliente.getNombre()+"','"+cliente.getNegocio()+"','"+cliente.getDireccion()+"','" +
										cliente.getTipoNegocio()+"','"+cliente.getTipoIdentificacion()+"','"+cliente.getNumeroIdentificacion()+"','"+cliente.getEmail()+"','"+cliente.getTipoCliente()+"','" +
										cliente.getGrupoDescuento()+"',"+cliente.getFrecuenciaVisita()+",'"+cliente.getRuta()+"','"+cliente.getEstado()+"','"+cliente.getFechaIngreso()+"','"+cliente.getHoraIngreso()+"','"+this.usuario+"','"+cliente.getClienteNuevo()+"','N','"+
										cliente.getNombre() +"','1','"+cliente.getFechaIngreso()+"','"+cliente.getFechaIngreso()+"','S','A','"+observacioncli+"','N','"+c_tdcredito+"','"+c_diascredito+"','"+cliente.getConFinal()+"','01','S0001','10','"+telefono+"','"+referencia+"','"
										+zona+"','','','','','','','','','','999','8520','"+cliente.getFechaIngreso()+"')";
								db.execSQL(insertSQL);


								selectSQL="SELECT canton,provincia FROM PARROQUIA where codigo='"+parroquiacli+"'";
								c = db.rawQuery(selectSQL, null);
								if(c.moveToFirst())
								{
									canton = c.getString(0);
									provincia = c.getString(1);
								}
								
								insertSQL = "update CLIENTE set canton='"+canton+"'"+",provincia='"+provincia+"'"+",parroquia='"+parroquiacli+"'"+" where codigo='"+cliente.getCodigo()+"'";
								db.execSQL(insertSQL);
								
								insertSQL="INSERT INTO USUARIO_CLIENTE(codigo_cliente,codigo_usuario,tipo_usuario) values('"+cliente.getCodigo()+"','"+this.usuario+"','PRI')";
								db.execSQL(insertSQL);
								
								insertSQL="update parametros set num_cliente = num_cliente+1";
								db.execSQL(insertSQL);
							}catch(Exception e){
								misDialogos.miDialogoToastLargo(getApplicationContext(), "Excepción al momento de insertar el cliente: Función: crear_cliente(); Clase: MainCreacionClientes --> " + e.getMessage());
								return;
							}finally{
								
							}
						}
						misDialogos.miDialogoToastCorto(getApplicationContext(), "Cliente guardado con éxito");
						
						Intent intent = new Intent(this,MainMenuClientes.class);
						intent.putExtra("user", usuario);
						intent.putExtra("nombreUser", nombreUsuario);
						intent.putExtra("tipoUser", tipoUsuario);
						startActivity(intent);
					}catch(Exception e){
						misDialogos.miDialogoToastLargo(getApplicationContext(), "Excepción al momento de crear cliente: Función: crear_cliente(); Clase: MainCreacionClientes --> " + e.getMessage());
						return;
					}
				}	
			}
			
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_creacion_clientes, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case R.id.accion_guardar:
				this.crear_cliente();
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