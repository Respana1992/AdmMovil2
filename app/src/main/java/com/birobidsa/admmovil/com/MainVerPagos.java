package com.birobidsa.admmovil.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import clasesAdapter.PagosAdapter;
import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.Pagos;
import procesos.MENSAJEEXCEPCIONES;
import procesos.VARIOS;

@SuppressLint("NewApi")
public class  MainVerPagos extends Activity {
    int posicion,secpagos,numpagos;
    double monto;
    String scriptSQL,codUser,nombreUsuario,tipoUsuario,cliente,tipopago,fecha,montpago;
    Integer numfact;
    Double montoant;
    ArrayList<Pagos> alPago = new ArrayList<Pagos>();
    Pagos pedido = new Pagos();
    MENSAJEEXCEPCIONES mex = new MENSAJEEXCEPCIONES();
    PagosAdapter dpAdapter;
    BDTablas dbcon;
    SQLiteDatabase db;
    Cursor c;
    ListView lvPagos;
    Constantes constante = new Constantes();
    Dialogos dialogo = new Dialogos();
    VARIOS varios;
    Intent intent;
    Gson gson;
    AlertDialog.Builder builder;
    AlertDialog alert;
    EditText EBusquedaPedido;
    static TextView tvDisplayDate;

    String linea="";
    Dialogos mensaje = new Dialogos();
    String impresora="BlueTooth Printer";

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    //
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ver_pagos_enviados);

        codUser = getIntent().getStringExtra("user");
        nombreUsuario = getIntent().getStringExtra("nombreUser");
        tipoUsuario = getIntent().getStringExtra("tipoUser");

        this.inicializaVariblesDeControl();

        try{
            dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
            db = dbcon.getWritableDatabase();
        }catch(Exception e){
            dialogo.miDialogoToastLargo(getApplicationContext(), "Error al conectarse a la BD: Clase MainVerPedidosEnviados --> " + e.getMessage());
            return;
        }

        varios = new VARIOS(getApplicationContext(), db);

        Date date = new Date();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        fecha = dateFormat.format(date);
        //fecha = dateFormat.format(this.tvDisplayDate.getText().toString().trim());

        tvDisplayDate.setText(fecha);
        this.buscaPagos();
        getActionBar().setTitle("Pagos: " + alPago.size());

        //Evento click del listview
        lvPagos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                if(lvPagos.getChoiceMode()!=2)
                {
                    posicion = i;
                    secpagos = alPago.get(i).getSecpago();
                    numpagos = alPago.get(i).getNumero();
                    cliente = alPago.get(i).getCliente();
                    tipopago = alPago.get(i).getTipoPago();
                    monto = alPago.get(i).getMonto();
                    fecha = alPago.get(i).getFecha();

                    String[] opc = new String[] {"Eliminar","Imprimir"};
                    AlertDialog opciones = new AlertDialog.Builder(MainVerPagos.this)
                            .setTitle("Opciones del pago")
                            .setItems(opc,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int selected) {
                                            try{
                                                if (selected == 0) {
                                                    eliminaPago(posicion);
                                                } else if (selected == 1) {
                                                    imprimePago(numpagos);
                                                }else if (selected == 2) {

                                                }
                                            }catch(Exception e){
                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).create();
                    opciones.show();

                }

            }

            });


        //Implemento evento clickLargo del ListView
        lvPagos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int pos, long id) {
                //pongo mi lista en modo de selecci�n m�ltiple
                //con esto, al hacer click en los elementos se seleccionar�n autom�ticamente
                lvPagos.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                return true;
            }
        });


    }

    public void muestraDialogFecha(View v) {
        MainVerPagos.SELECCIONAFECHA newFragment = new MainVerPagos.SELECCIONAFECHA();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class SELECCIONAFECHA extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // 	Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            tvDisplayDate.setText(new StringBuilder().
                    append(day).append("/").
                    append(month + 1).append("/").
                    append(year).append(" "));
        }
    }


    public void imprimePago(final int numpedido)
    {
        builder = new AlertDialog.Builder(MainVerPagos.this);
        builder.setMessage("¿Está seguro de querer imprimir el pago?")
                .setTitle("Impresión del pago")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cuando el usuario escoje SI
                        creaReporte(codUser,numpedido);
                        try {
                            impresora = varios.getNombreImpresora();
                            findBT();
                            //openBT();
                            sendData();
                            closeBT();
                            linea="";
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //mensaje.miDialogoToastCorto(getApplicationContext(), "NO");
                    }
                });
        alert = builder.create();
        alert.show();
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
            String codigocli,clavefecli,paginaweb,empresa;
            int lineapedido = 0;
            empresa = varios.getNombreCia();
            paginaweb = varios.getPaginaWeb();
            if(c.moveToFirst())
            {
                linea = linea + empresa +"\n";
                linea = linea + "Pago # "+numpedido+"\n";
                linea = linea + "Cliente: "+c.getString(0)+"\n";
                String tipopag = "";
                c1 = db.rawQuery("SELECT detpag.tipopag, detpag.banco, detpag.cuenta, detpag.numchq, detpag.monto FROM detpagos detpag inner join pagos cabpag on detpag.secuencial = cabpag.secuencial where cabpag.secuencial='"+numpedido+"' and cabpag.vendedor='"+vendedor+"'", null);
                if(c1.moveToFirst())
                    {
                        tipopag = c1.getString(0);
                        montpago = String.valueOf(df.format(c1.getDouble(4)));
                        linea = linea + "\n";
                        linea = linea + "Detalles Pago # "+numpedido+"\n";
                        linea = linea + "Tipo - Banco - Cuenta - N. Chq. - Valor";
                        linea = linea + "\n";
                        do{
                            linea = linea + (lineapedido + 1) +") "+ c1.getString(0).trim() + "-\n";
                            linea = linea + c1.getString(1)+" - "+c1.getString(2)+" - "+c1.getString(3)+" - $"+String.valueOf(df.format(c1.getDouble(4)));
                            linea = linea + "\n";
                            linea = linea + "-------------------------------";
                            linea = linea + "\n";
                            lineapedido = lineapedido +1;
                        }while(c1.moveToNext());
                    }
                linea = linea + "\n";
                //linea = linea + "Valor Pago: $"+ String.valueOf(df.format(montpago));
                linea =linea + "Valor Pago: $" + montpago;
                linea = linea + "\n\n";
                linea = linea + "Vendedor: " + vendedor;
                linea = linea + "\n";
                linea = linea + nombreUsuario;
                linea = linea + "\n\n\n";
            }


        } catch (Exception ioe) {
            Toast.makeText(this, "Error al momento de generar el reporte",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressWarnings("static-access")
    public void buscaPagos()
    {
        try{
            if(db!=null)
            {
                scriptSQL = "select pag.secuencial, pag.numero, cli.nombre, detpag.tipopag, pag.monto, pag.fecha" +
                        " from pagos pag,cliente cli, detpagos detpag where pag.cliente = cli.codigo and  pag.secuencial = detpag.secuencial and pag.vendedor = '"+codUser+"'" +
                        " and pag.fecha = '" + fecha + "' order by pag.fecha desc";
                try{
                    c = db.rawQuery(scriptSQL, null);
                }catch(Exception e){
                    dialogo.miDialogoToastLargo(getApplicationContext(), "Error al ejecutar el script de pedido: Clase MainVerPedidos --> " + e.getMessage());
                    return;
                }

                if(c.moveToFirst())
                {
                    do{
                        alPago.add(new Pagos(c.getInt(0), c.getInt(1),c.getString(2),c.getString(3),c.getDouble(4),c.getString(5)));
                    }while(c.moveToNext());

                    dpAdapter = new PagosAdapter(this, alPago);
                    lvPagos.setAdapter(dpAdapter);
                }
                else
                {
                    dialogo.miDialogoToastCorto(getApplicationContext(), "No hay pagos que mostrar");
                }
            }
            else
            {
                dialogo.miDialogoToastCorto(getApplicationContext(), "No se pudo abrir la conexión a la base de datos");
            }
        }catch(Exception e)
        {
            dialogo.miDialogoToastLargo(getApplicationContext(), "Error al abrir la BD despues de la conexión: Clase MainVerPedidos --> " + e.getMessage());
            return;
        }
    }

    public void eliminaPago(final int i)
    {
        builder = new AlertDialog.Builder(MainVerPagos.this);
        builder.setMessage("¿Está seguro de querer eliminar el pedido?")
                .setTitle("Eliminación del pedido")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cuando el usuario escoje SI

                        if(db!=null)
                        {
                            scriptSQL = "select numfact, monto from pagos where estado = 'A' and numero='"+numpagos+"'";
                            try{
                                c = db.rawQuery(scriptSQL, null);
                                if(c.moveToFirst())
                                {
                                    numfact = c.getInt(0);
                                    montoant = c.getDouble(1);
                                    mex = pedido.eliminaPago(db, secpagos, codUser, numfact, montoant);
                                    if(mex.getEstado())
                                    {
                                        alPago.remove(i);
                                        dpAdapter = new PagosAdapter(MainVerPagos.this, alPago);
                                        lvPagos.setAdapter(dpAdapter);
                                        dpAdapter.notifyDataSetChanged();
                                        dialogo.miDialogoToastCorto(getApplicationContext(), "El pago ha sido eliminado con éxito");
                                        getActionBar().setTitle("Total de pagos: " + alPago.size());
                                    }
                                    else
                                    {
                                        dialogo.miDialogoToastLargo(getApplicationContext(), mex.getMensaje());
                                    }
                                }
                                else
                                {
                                    dialogo.miDialogoToastLargo(getApplicationContext(), "No se puede eliminar el pago porque ya fue sincronizado");
                                }
                            }catch(Exception e){
                                dialogo.miDialogoToastLargo(getApplicationContext(), "Error al ejecutar el script de seleccionar numfact y monto" + e.getMessage());
                                return;

                                }

                        }


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alert = builder.create();
        alert.show();
    }

    public void actualizaPago(int numpedido,int secpago,String vendedor)
    {
        mex = pedido.restableceEstadoPago(db, secpago, vendedor);
        if(mex.getEstado())
        {
            alPago.clear();
            this.buscaPagos();
            dialogo.miDialogoToastCorto(getApplicationContext(), "El estado del pago # " + numpedido + " ha sido actualizado para un próximo envío");
            getActionBar().setTitle("Total de pedidos: " + alPago.size());
        }
        else
        {
            dialogo.miDialogoToastLargo(getApplicationContext(), mex.getMensaje());
        }
    }

    public void inicializaVariblesDeControl()
    {
        lvPagos = (ListView)findViewById(R.id.lvPagos);
        tvDisplayDate = (TextView) findViewById(R.id.lblDate);
        lvPagos.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
    }


}
