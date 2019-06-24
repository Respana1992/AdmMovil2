package com.birobidsa.admmovil.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import clasesAdapter.DetallesPagosAdapter;
import clasesAdapter.DeudaPAdapter;
import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.DetallesPagos;
import misclases.DetallesPedidos;
import misclases.Deuda;
import misclases.Pagos;
import procesos.CONDICIONESCOMERCIALES;
import procesos.MENSAJEEXCEPCIONES;
import procesos.VARIOS;

@SuppressLint("NewApi")
public class MainRegistraPagos extends Activity {
    String usuario,nombreUsuario,tipoUsuario, codigoCliente, nombreCliente, negocioCliente, tipoCliente, cedulaCliente,
            numerod,clientecod,saldod,fechavend,codigo,nombre, scriptSQL, nombrepago,insertSQL,montopago,fecha,hora, observacion,
            numCuenta,numChq,banco,fechVenChq,montopago1;
    Integer secpago, numpago, numlineapag;
    Double credanterior,salanterior,resultadocred,resulatadosal,montpago;
    Gson gson;
    String detallesJson = "";
    Intent intent;
    Pagos pago;
    private ArrayList<DetallesPedidos> alDetallesPed = new ArrayList<DetallesPedidos>();
    private ArrayList<DetallesPagos> alDetallesPag = new ArrayList<DetallesPagos>();
    EditText ENombreCliente,txtSaldoPago, txtmontopago, txtFechaPago
            , txtNumCuenta, txtNumChq, txtFechVenChq;
    TextView TVDocDebe,tvNumChq,tvFechVenChq,txtObservacion, txtNumFact, txtFormPag,ECodigoCliente, txtBanco;
    Button btnAcepta;
    Bundle bundle;
    Type type;
    BDTablas dbcon;
    SQLiteDatabase db;
    Constantes constante = new Constantes();
    Dialogos dialogo = new Dialogos();
    Cursor c;
    ArrayList<Deuda> aldeuda = new ArrayList<Deuda>();
    private ListView listaDetPag;
    private DetallesPagosAdapter dpAdapter;
    Deuda deuda;
    DeudaPAdapter deudaAdapter;
    ListView lvDeuda;
    AlertDialog.Builder builder;
    AlertDialog alert;
    Dialogos mensaje = new Dialogos();
    CONDICIONESCOMERCIALES cc;
    MENSAJEEXCEPCIONES mex = new MENSAJEEXCEPCIONES();
    VARIOS procesos;
    LinearLayout datosTipoPago;
    String linea="";
    VARIOS varios;
    String impresora="BlueTooth Printer";
    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pago);

        getActionBar().setTitle("Registro de Pagos");

        this.incializaVariablesDeControles();

        //Recibo los parametros de la actividad MainLogin el usuario y el nombre de quien inicia sesi�n
        usuario = getIntent().getStringExtra("user");
        nombreUsuario = getIntent().getStringExtra("nombreUser");
        tipoUsuario = getIntent().getStringExtra("tipoUser");

        codigoCliente = getIntent().getStringExtra("codigoCli");
        nombreCliente = getIntent().getStringExtra("nombreCli");
        negocioCliente = getIntent().getStringExtra("negocioCli");
        tipoCliente = getIntent().getStringExtra("tipoCli");
        cedulaCliente = getIntent().getStringExtra("cedC");

        clientecod = getIntent().getStringExtra("clientecod");

        numerod = getIntent().getStringExtra("numeroD");
        saldod = getIntent().getStringExtra("saldoD");
        fechavend = getIntent().getStringExtra("vendedorD");

        nombrepago = getIntent().getStringExtra("nombrepago");

        banco = getIntent().getStringExtra("banco");
        numCuenta = getIntent().getStringExtra("numcuenta");
        numChq = getIntent().getStringExtra("numcheque");
        fechVenChq = getIntent().getStringExtra("fechvenchq");

        observacion = getIntent().getStringExtra("observacion");

        montopago1 = txtmontopago.getText().toString();
        montopago = getIntent().getStringExtra("montopago");



        Date date = new Date();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");

        fecha = dateFormat.format(date);
        hora = hourFormat.format(date);

        txtFechaPago.setText(dateFormat.format(date));
        txtFechVenChq.setText(dateFormat.format(date));

        //txtFechaPago.setText(fecha);


        try{
            dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
            db = dbcon.getWritableDatabase();
        }catch(Exception e){
            dialogo.miDialogoToastLargo(getApplicationContext(), "Error al conectarse a la BD: Clase MainRegistroPagos --> " + e.getMessage());
            return;
        }

        varios = new VARIOS(getApplicationContext(), db);

        buscaDeuda(codigoCliente);

        if(codigoCliente != null)
        {
            //ENombreCliente.setText(nombreCliente);
            ECodigoCliente.setText(codigoCliente+"-"+nombreCliente);
            txtNumFact.setText(numerod);
            txtSaldoPago.setText(saldod);
            txtFormPag.setText(nombrepago);
            txtBanco.setText(banco);
            numCuenta = txtNumCuenta.getText().toString();
            numChq = txtNumChq.getText().toString();
            fechVenChq = txtFechVenChq.getText().toString();
            observacion=  txtObservacion.getText().toString();
            bundle = this.getIntent().getExtras();
            gson = new Gson();
            String historial = bundle.getString("datosDetalles");
            type = new TypeToken<ArrayList<DetallesPedidos>>(){}.getType();
            alDetallesPed = gson.fromJson(historial, type);
        }

        String tipoc = "CHQ";
        String tipodeb = "DEB";
        String tipodep = "DEP";
        String tipotcr = "TCR";
        String tipotra = "TRA";

        if(nombrepago!=null){
            if(nombrepago.equals(tipoc)){
                datosTipoPago.setVisibility(View.VISIBLE);
                tvNumChq.setVisibility(View.VISIBLE);
                tvFechVenChq.setVisibility(View.VISIBLE);
                txtNumChq.setVisibility(View.VISIBLE);
                txtFechVenChq.setVisibility(View.VISIBLE);
            }else if(nombrepago.equals(tipodeb)){
                datosTipoPago.setVisibility(View.VISIBLE);
                tvNumChq.setVisibility(View.VISIBLE);
                tvFechVenChq.setVisibility(View.VISIBLE);
                txtNumChq.setVisibility(View.VISIBLE);
                txtFechVenChq.setVisibility(View.VISIBLE);
            }else if(nombrepago.equals(tipodep)){
                datosTipoPago.setVisibility(View.VISIBLE);
                tvNumChq.setVisibility(View.VISIBLE);
                tvFechVenChq.setVisibility(View.VISIBLE);
                txtNumChq.setVisibility(View.VISIBLE);
                txtFechVenChq.setVisibility(View.VISIBLE);
            }else if(nombrepago.equals(tipotcr)){
                datosTipoPago.setVisibility(View.VISIBLE);
                tvNumChq.setVisibility(View.VISIBLE);
                tvFechVenChq.setVisibility(View.VISIBLE);
                txtNumChq.setVisibility(View.VISIBLE);
                txtFechVenChq.setVisibility(View.VISIBLE);
            }else if(nombrepago.equals(tipotra)){
                datosTipoPago.setVisibility(View.VISIBLE);
                tvNumChq.setVisibility(View.VISIBLE);
                tvFechVenChq.setVisibility(View.VISIBLE);
                txtNumChq.setVisibility(View.VISIBLE);
                txtFechVenChq.setVisibility(View.VISIBLE);
            }
        }
    }

    /*private void  actualizaListViewDetalles(){
        for (int i = 0; i < alDetallesPag.size(); i++) {
            dpAdapter = new DetallesPagosAdapter(this, alDetallesPag);
            listaDetPag.setAdapter(dpAdapter);
            dpAdapter.notifyDataSetChanged();
        }
    }*/

    public void buscaDeuda(String cliente)
    {
        scriptSQL = "select * from DEUDA where cliente = '"+ cliente +"'";
        try{
            c = db.rawQuery(scriptSQL, null);
            if(c.moveToFirst())
            {
                ECodigoCliente.setText(codigoCliente + "-" + nombreCliente);
                do{
                    deuda = new Deuda(c.getInt(0),        //secuencial
                            c.getString(1),    //bodega
                            c.getString(2),    //cliente
                            c.getString(3),    //tipo
                            c.getInt(4),      //numero
                            c.getString(5),    //serie
                            c.getString(6),    //secinv
                            c.getDouble(7),    //iva
                            c.getDouble(8),    //monto
                           c.getDouble(9),    //credito
                            c.getDouble(10),   //saldo
                            c.getString(11),   //fechaemi
                            c.getString(12),   //fechaven
                            c.getString(13),   //vendedor
                            c.getString(14));  //observacion
                    //credanterior=String.valueOf(c.getDouble(9));
                    aldeuda.add(deuda);
                    //credanterior = String.valueOf(aldeuda.get(9));
                }while(c.moveToNext());
                credanterior=deuda.getCredito();
                salanterior=deuda.getSaldo();

                TVDocDebe.setText("");
                TVDocDebe.setText("Tiene "+ aldeuda.size() + " documento/s pendiente de pago");
                /*lvDeuda = (ListView)findViewById(R.id.lvClientesDeudas);
                deudaAdapter = new DeudaPAdapter(this, aldeuda);
                lvDeuda.setAdapter(deudaAdapter);*/
            }
            else
            {
                //ECodigoCliente.setText(codigoCliente + " - " + nombreCliente);
                TVDocDebe.setText("No tiene deuda pendiente de pago");
            }
        }catch(Exception e){
            dialogo.miDialogoToastLargo(getApplicationContext(), "Error al ejecutar el script de pedido: Clase MainVerCarteraClientes --> " + e.getMessage());
            return;
        }
    }

    public void incializaVariablesDeControles()
    {
        //ENombreCliente = (EditText)findViewById(R.id.txtCliente);
        ECodigoCliente = (TextView)findViewById(R.id.txtCliente);
        TVDocDebe = (TextView)findViewById(R.id.lblDocDebe);
        txtNumFact = (TextView)findViewById(R.id.txtNumFact);
        txtFormPag = (TextView)findViewById(R.id.txtFormPag);
        //txtFechaPago = (EditText)findViewById(R.id.txtFechaPago);
        txtSaldoPago = (EditText)findViewById(R.id.txtSaldoPago);
        txtmontopago = (EditText)findViewById(R.id.txtMonto);
        txtFechaPago = (EditText)findViewById(R.id.txtFechaPago);
        datosTipoPago = (LinearLayout)findViewById(R.id.datosTipoPago);
        txtBanco = (TextView)findViewById(R.id.txtBanco);
        txtNumCuenta = (EditText)findViewById(R.id.txtNumCuenta);
        txtNumChq = (EditText)findViewById(R.id.txtNumCheque);
        txtFechVenChq = (EditText)findViewById(R.id.txtFechVenChq);
        tvNumChq = (TextView)findViewById(R.id.lblNumCheque);
        tvFechVenChq = (TextView)findViewById(R.id.lblFechVenChq);
        listaDetPag = (ListView)findViewById(R.id.lvDetallesPedidos);
        txtObservacion = (EditText)findViewById(R.id.txtObservacion);

    }

    public void buscaClientes(View v)
    {
        gson = new Gson();
        detallesJson = gson.toJson(alDetallesPed);

        intent = new Intent(this,MainVerClientesRP.class);
        intent.putExtra("codigoC", this.codigoCliente);
        intent.putExtra("nombreC", this.nombreCliente);
        intent.putExtra("negocioC", this.negocioCliente);
        intent.putExtra("tipoC", this.tipoCliente);
        intent.putExtra("cedC", this.cedulaCliente);
        intent.putExtra("user", this.usuario);
        intent.putExtra("nombreUser", this.nombreUsuario);
        intent.putExtra("tipoUser", tipoUsuario);
        intent.putExtra("datosDetalles", detallesJson);
        intent.putExtra("observacion", this.observacion);
        intent.putExtra("montopago", this.montopago);

        startActivity(intent);

    }




   public void buscarDeudas(View v)
   {
      gson = new Gson();

      intent = new Intent(this,MainVerDeudas.class);
      intent.putExtra("codigoCli", this.codigoCliente);
      clientecod = ECodigoCliente.getText().toString();
      //clientecod = codigoCliente;
      intent.putExtra("clientecod", clientecod);
      intent.putExtra("numeroD", this.numerod);
      intent.putExtra("saldoD", this.saldod);
      intent.putExtra("fechavenD", this.fechavend);
      intent.putExtra("user", this.usuario);
      intent.putExtra("nombreUser", this.nombreUsuario);
      intent.putExtra("tipoUser", tipoUsuario);
      intent.putExtra("observacion", this.observacion);
      intent.putExtra("montopago", this.montopago);
      intent.putExtra("nombreCli", this.nombreCliente);

      startActivity(intent);
   }

    public void buscaMedioPago(View v)
    {

        intent = new Intent(this,MainVerFormasPago.class);

        intent.putExtra("codigoCli", this.codigoCliente);

        intent.putExtra("numeroD", this.numerod);
        intent.putExtra("saldoD", this.saldod);
        intent.putExtra("fechavenD", this.fechavend);
        intent.putExtra("user", this.usuario);
        intent.putExtra("nombreUser", this.nombreUsuario);
        intent.putExtra("tipoUser", tipoUsuario);

        intent.putExtra("nombrepago", this.nombrepago);

        intent.putExtra("observacion", this.observacion);
        intent.putExtra("montopago", this.montopago);



        startActivity(intent);
    }

    public void buscaBanco(View v)
    {

        intent = new Intent(this,MainBancos.class);

        intent.putExtra("codigoCli", this.codigoCliente);

        intent.putExtra("numeroD", this.numerod);
        intent.putExtra("saldoD", this.saldod);
        intent.putExtra("fechavenD", this.fechavend);
        intent.putExtra("user", this.usuario);
        intent.putExtra("nombreUser", this.nombreUsuario);
        intent.putExtra("tipoUser", tipoUsuario);

        intent.putExtra("nombrepago", this.nombrepago);

        intent.putExtra("banco", this.banco);
        intent.putExtra("numcuenta", this.numCuenta);
        intent.putExtra("numcheque", this.numChq);
        intent.putExtra("fechvenchq", this.fechVenChq);

        intent.putExtra("observacion", this.observacion);
        intent.putExtra("montopago", this.montopago1);

        startActivity(intent);
    }

    /*public void ocultaTeclado()
    {
        //Este funciona cada vez que inicia la actividad
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Lineas para ocultar el teclado virtual (Hide keyboard) despues de alguna accion
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtBanco.getWindowToken(), 0);
    }*/


    public void grabarPago()
    {
        if(codigoCliente==null)
        {
            mensaje.miDialogoToastCorto(getApplicationContext(),"Debe seleccionar un cliente");
        }
        else if(numerod==null)
        {
            mensaje.miDialogoToastCorto(getApplicationContext(),"Debe seleccionar una factura");
        } else if(nombrepago==null)
        {
            mensaje.miDialogoToastCorto(getApplicationContext(),"Debe seleccionar una forma de pago");
        }else if(this.txtmontopago.getText().toString().length()==0)
        {
            mensaje.miDialogoToastCorto(getApplicationContext(),"Debe ingresar el valor a pagar");
        }
        else
        {
            if(db!=null){
                try {
                    varios = new VARIOS(getApplicationContext(), db);
                    numpago = varios.getNumeroPago();
                    secpago = varios.getSecuencialPago();

                }catch (Exception e){
                    mensaje.miDialogoToastCorto(getApplicationContext(),"error al consultar secuenciales");
                }

                try {
                    scriptSQL = "select credito from DEUDA where numero='"+numerod+"'";

                    c = db.rawQuery(scriptSQL, null);
                    if(c.moveToFirst()) {
                        credanterior = Double.valueOf(c.getInt(0));
                        // numpago = Integer.valueOf(c.getString(1));
                    }

                }catch (Exception e){
                    mensaje.miDialogoToastCorto(getApplicationContext(),"error al consultar secuenciales");
                }

                try {
                    scriptSQL = "select saldo from DEUDA where numero='"+numerod+"'";

                    c = db.rawQuery(scriptSQL, null);
                    if(c.moveToFirst()) {
                        salanterior = Double.valueOf(c.getInt(0));
                        // numpago = Integer.valueOf(c.getString(1));
                    }

                }catch (Exception e){
                    mensaje.miDialogoToastCorto(getApplicationContext(),"error al consultar secuenciales");
                }

                db.beginTransaction();
                montopago = this.txtmontopago.getText().toString();
                fecha = this.txtFechaPago.getText().toString();
                montpago = Double.valueOf(montopago);
                resultadocred = credanterior+montpago;
                resulatadosal = salanterior-montpago;
                if(nombrepago.trim()!="EFE"){
                    banco=this.txtBanco.getText().toString();
                    numCuenta=this.txtNumCuenta.getText().toString();
                    numChq=this.txtNumChq.getText().toString();
                    fechVenChq=this.txtFechVenChq.getText().toString();
                }
                else
                {
                    banco="";
                    numCuenta="";
                    numChq="";
                }
                try{

                    if(montpago<=salanterior)
                    {
                        insertSQL="insert into PAGOS(secuencial,cliente,tipo,numero,monto,operador,observacion," +
                                " fecha,vendedor,noguia,hora,numfact,estado) values('"+secpago+"','"+codigoCliente+"','PAG','"+numpago+"','"+montopago+"','','"+observacion+"','"+fecha+"','"+usuario+"','','"+hora+"','"+numerod+"','A')";
                        db.execSQL(insertSQL);
                        insertSQL="insert into DETPAGOS(secuencial,tipo,numero,tipopag,monto,banco,cuenta,numchq,fechaven)" +
                                " values('"+secpago+"','PAG','"+numpago+"','"+nombrepago+"','"+montopago+"','"+banco+"','"+numCuenta+"','"+numChq+"','"+fechVenChq+"')";
                        db.execSQL(insertSQL);
                        insertSQL = "update DEUDA set credito='"+resultadocred+"' where cliente='"+codigoCliente+"' and numero='"+numerod+"'";
                        db.execSQL(insertSQL);
                        insertSQL = "update DEUDA set saldo='"+resulatadosal+"'where cliente='"+codigoCliente+"' and numero='"+numerod+"'";
                        db.execSQL(insertSQL);
                        insertSQL = "update SECUENCIALPAGO set secuencial='"+secpago+"'";
                       db.execSQL(insertSQL);
                        insertSQL = "update SECUENCIALPAGO set numero='"+numpago+"'";
                        db.execSQL(insertSQL);

                        dialogo.miDialogoToastCorto(getApplicationContext(), "Pago guardado con éxito");
                        db.setTransactionSuccessful();

                        aldeuda.clear();

                        ECodigoCliente.setText("");
                        txtNumFact.setText("");
                        txtSaldoPago.setText("");
                        txtObservacion.setText("");
                        txtFormPag.setText("");
                        txtmontopago.setText("");
                        TVDocDebe.setText("");
                        txtBanco.setText("");
                        txtNumCuenta.setText("");
                        tvNumChq.setText("");
                        creaReporte(usuario,numpago);
                    }else{
                        dialogo.miDialogoToastCorto(getApplicationContext(), "Monto a pagar debe ser menor o igual al saldo de la deuda ");
                    }


                }catch(Exception e){
                    dialogo.miDialogoToastLargo(getApplicationContext(), "Excepción al momento de insertar el pago: Función: registropago(); Clase: MainRegistroPago --> " + e.getMessage());
                    return;
                }finally{
                    db.endTransaction();
                    db.close();
                }

            }

        }


        }

    public void agregarDetPago()
    {

                if(txtFormPag.getText().toString().isEmpty() && txtNumFact.getText().toString().isEmpty())
                {
                    mensaje.miDialogoToastCorto(getApplicationContext(),"Por favor escoja una número de factura y forma de pago");
                }
                else
                {
                    if(Double.valueOf(montopago)<=salanterior)
                    {
                        if(txtBanco.getText().toString().isEmpty())	 	txtBanco.setText("");
                        if(txtNumCuenta.getText().toString().isEmpty())	txtNumCuenta.setText("");
                        if(txtNumChq.getText().toString().isEmpty())		txtNumChq.setText("");
                        if(txtFechVenChq.getText().toString().isEmpty())		txtFechVenChq.setText("");

                        pago.agregarDetalle("PAG",' ',nombrepago,montpago,banco,numCuenta,numChq,fechVenChq,' ', alDetallesPag);

                        txtBanco.setText("");
                        txtNumCuenta.setText("");
                        txtNumChq.setText("");
                        txtFechVenChq.setText("");
                        //this.ocultaTeclado();

                    }
                    else
                    {

                        mensaje.miDialogoToastCorto(getApplicationContext(),"El valor del pago no puede ser menor al saldo");

                    }

                }

        }



    //Este metodo es para inflar el menu y mostrar las opciones del menu en al action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_registro_pagos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.accion_agregar:
                //this.agregarDetPago();
                return true;
            case R.id.accion_guardar:
                this.grabarPago();
                return true;
            case R.id.accion_observacion:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.layout_observacion);
                dialog.setTitle("Mi titulo");
                btnAcepta = (Button)dialog.findViewById(R.id.btnAcepta);
                btnAcepta.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        EditText edit=(EditText)dialog.findViewById(R.id.txtObservacion);
                        observacion=edit.getText().toString();
                        dialog.dismiss(); //Para que el dialogo se cierre y no consuma memoria
                    }
                });
                dialog.show();
                return true;
            case R.id.accion_imprimir:
                this.imprimir();
                return true;
            case R.id.cerrar_sesion:
				/*
				finishAffinity();
				intent = new Intent(this,MainLogin.class);
				startActivity(intent);
				*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Este m�todo se ejecuta cuando se da clic en el bot�n de ir hacia atr�s
   /* public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // Esto es para cuando de clic en el bo�n de atr�s no regrese a la pantalla de login
            if(codigoCliente!=null)
            {
                builder = new AlertDialog.Builder(MainRegistraPagos.this);
                builder.setMessage("El pago esta pendiente de grabar. ¿Está seguro que desea salir?. Si sale el pago no se podrá grabar")
                        .setTitle("Edición de pagos")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent intent = new Intent(getApplicationContext(),MainPagos.class);
                                intent.putExtra("user", usuario);
                                intent.putExtra("nombreUser", nombreUsuario);
                                intent.putExtra("tipoUser", tipoUsuario);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //
                            }
                        });
                alert = builder.create();
                alert.show();


                mensaje.miDialogoToastLargo(getApplicationContext(), "Tiene pago pendiente de grabar");
                return true;
            }
            else
            {
                finish();
                Intent intent = new Intent(this,MainPagos.class);
                intent.putExtra("user", usuario);
                intent.putExtra("nombreUser", nombreUsuario);
                intent.putExtra("tipoUser", tipoUsuario);
                startActivity(intent);
            }
        }
        return super.onKeyDown(keyCode, event);
    }*/

    public void imprimir()
    {
        try {
            //mensaje.miDialogoToastCorto(getApplicationContext(), "Comienza Impresión");
            findBT();

            //openBT();
            sendData();
            closeBT();
            //mensaje.miDialogoToastCorto(getApplicationContext(), "Termina impresión");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // this will find a bluetooth printer device
    public	void findBT()
    {
        try {




            mensaje.miDialogoToastCorto(getApplicationContext(), "Impresora Bluetooth: "+ impresora.trim());
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter == null) {
                mensaje.miDialogoToastCorto(getApplicationContext(), "No hay ningún adaptador bluetooth disponible");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    String deviceAlias = device.getName();
                    try {
                        Method method = device.getClass().getMethod("getAliasName");
                        if(method != null) {
                            deviceAlias = (String)method.invoke(device);
                            if(deviceAlias.equals(impresora.trim())){
                                mmDevice = device;
                                Toast.makeText(getApplicationContext(),"Conectado a Impresora"+device.getName(),Toast.LENGTH_LONG).show();
                                openBT();

                                break;
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    // String nameD = device.getName().compareTo(impresora);


                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static int numeroRandom(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    // tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = null;
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            //mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
            Thread.sleep(numeroRandom(1000, 5000));
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            beginListenForData();
            mensaje.miDialogoToastCorto(getApplicationContext(), "Bluetooth conectado");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* after opening a connection to bluetooth printer device,
     * we have to listen and check if a data were sent to be printed.*/
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;
            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                        try {
                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );
                                        // specify US-ASCII encoding
                                        @SuppressWarnings("unused")
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                //myLabel.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            stopWorker = true;
                        }
                    }
                }
            });
            workerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // this will send text data to be printed by the bluetooth printer
    void sendData() throws IOException {
        try {
            mmOutputStream.write(linea.getBytes());
            mensaje.miDialogoToastCorto(getApplicationContext(), "Imprimiendo recibo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void creaReporte(String vendedor, int numpedido)
    {
        try {
            c = db.rawQuery("SELECT c.nombre,p.*,c.clavefe FROM pagos p,cliente c where p.numero='"+numpedido+"' and p.vendedor='"+vendedor+"' and p.cliente=c.codigo", null);
            Cursor c1;
            DecimalFormat df;
            df= new DecimalFormat("#0.00");
            double subtpedi,ivapedi,despedi,netopedi;
            String codigocli,clavefecli,paginaweb,empresa;
            int lineapedido = 0;
            subtpedi=0;
            ivapedi=0;
            despedi=0;
            netopedi=0;
            codigocli="";
            clavefecli="";

            empresa = varios.getNombreCia();
            paginaweb = varios.getPaginaWeb();

            if(c.moveToFirst())
            {
                linea = linea + empresa +"\n";
                linea = linea + "Pago # "+numpedido+"\n";
                linea = linea + "Cliente: "+c.getString(0)+"\n";

               /* codigocli = c.getString(6);
                clavefecli = c.getString(17);
                subtpedi = c.getDouble(8);
                despedi = c.getDouble(9);
                ivapedi = c.getDouble(10);
                netopedi = c.getDouble(11);*/

                if(nombrepago != "EFE"){
                    c1 = db.rawQuery("SELECT detpag.tipopag, detpag.banco, detpag.cuenta, detpag.numchq, detpag.monto FROM detpagos detpag inner join pagos cabpag on detpag.secuencial = cabpag.secuencial where cabpag.secuencial='"+numpedido+"' and cabpag.vendedor='"+vendedor+"'", null);
                    if(c1.moveToFirst())
                    {
                        linea = linea + "\n";
                        linea = linea + "Detalles Pago # "+numpedido+"\n";
                        linea = linea + "Tipo - Banco - Cuenta - N. Chq. - Valor";
                        linea = linea + "\n";

                        do{
                            linea = linea + (lineapedido + 1) +") "+ c1.getString(0).trim() + "-\n";
                            linea = linea + c1.getString(1) +" - "+c1.getString(2)+" - "+c1.getString(3)+" - $"+String.valueOf(df.format(c1.getDouble(4)));
                            linea = linea + "\n";
                            linea = linea + "-------------------------------";
                            linea = linea + "\n";
                            lineapedido = lineapedido +1;
                        }while(c1.moveToNext());
                    }
                }

            }

            linea = linea + "\n";
            linea = linea + "Valor Pago: $"+ String.valueOf(df.format(montpago));
            linea = linea + "\n";
            linea = linea + "Saldo Anterior: $"+ String.valueOf(df.format(salanterior));
            linea = linea + "\n";
            linea = linea + "Saldo Actual: $"+ String.valueOf(df.format(resulatadosal));

            linea = linea + "\n\n";
            linea = linea + "Vendedor: " + vendedor;
            linea = linea + "\n";
            linea = linea + nombreUsuario;
            linea = linea + "\n\n\n";
        } catch (Exception ioe) {
            Toast.makeText(this, "Error al momento de generar el reporte",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // Esto es para cuando de clic en el bo�n de atr�s no regrese a la pantalla de login
            finish();
            Intent intent = new Intent(this,MainPagos.class);
            intent.putExtra("user", usuario);
            intent.putExtra("nombreUser", nombreUsuario);
            intent.putExtra("tipoUser", tipoUsuario);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

}
