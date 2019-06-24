package com.birobidsa.admmovil.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

import clasesAdapter.DeudaPAdapter;
import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.Deuda;

@SuppressLint("NewApi")
public class MainVerDeudas extends Activity {
    String usuario,nombreUsuario,tipoUsuario,montopago,scriptSQL,selectSQL,clientecod,numerod,saldod,fechavend,txtCliente,selectClientes,codigoCliente,observacion,nombreCli;
    Constantes constante;
    SQLiteDatabase db;
    BDTablas dbcon;
    Dialogos misDialogos = new Dialogos();
    Cursor c;
    ArrayList<Deuda> aldeuda = new ArrayList<>();
    RadioGroup RGBusqueda;
    RadioButton RBCodigo,RBNombre, RBCedula;
    EditText EClientes;
    LinearLayout llBusqueda;
    ListView lista;
    DeudaPAdapter cAdapter;
    Gson gson;
    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ver_deudas);

        getActionBar().setTitle("Búsqueda de Deudas");

        codigoCliente = getIntent().getStringExtra("codigoCli");

        clientecod = getIntent().getStringExtra("clientecod");
        nombreCli = getIntent().getStringExtra("nombreCliente");

        numerod = getIntent().getStringExtra("numeroD");
        saldod = getIntent().getStringExtra("saldoD");
        fechavend = getIntent().getStringExtra("fechavenD");


        usuario = getIntent().getStringExtra("user");
        nombreUsuario = getIntent().getStringExtra("nombreUser");
        tipoUsuario = getIntent().getStringExtra("tipoUser");

        observacion = getIntent().getStringExtra("observacion");

        montopago = getIntent().getStringExtra("montopago");

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
            selectClientes ="select numero,saldo,fechaven,tipo  from DEUDA where cliente='"+codigoCliente+"' and (saldo > 0)";
            c = db.rawQuery(selectClientes, null);
            if(c.moveToFirst())
            {
                do
                {
                    aldeuda.add(new Deuda(c.getInt(0),c.getDouble(1),c.getString(2),c.getString(3)));
                }while(c.moveToNext());
            }

            try{
                lista = (ListView)findViewById(R.id.lvDeudas);
                cAdapter = new DeudaPAdapter(this, aldeuda);
                lista.setAdapter(cAdapter);
            }catch(Exception e){
                misDialogos.miDialogoToastLargo(this, "No existe deuda para ese cliente" + e.getMessage());
                return;
            }


        }



        //Evento para cuando se da click en el ListView
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                clientecod = cAdapter.getItem(i).getCliente();

                numerod = String.valueOf(cAdapter.getItem(i).getNumero());
                saldod = String.valueOf(cAdapter.getItem(i).getSaldo());
                fechavend = cAdapter.getItem(i).getFechaVen();



                //Los almaceno en un ArrayList para luego convertirlo en Json

                //Esto sirve para cerrar la actividad y en caso de que vuelva atras no se muestre de nuevo esta actividad
                finish();

                intent = new Intent(getApplicationContext(),MainRegistraPagos.class);

                intent.putExtra("codigoCli", codigoCliente);

                intent.putExtra("clientecod", clientecod);

                intent.putExtra("numeroD", numerod);
                intent.putExtra("saldoD", saldod);
                intent.putExtra("fechavenD", fechavend);

                intent.putExtra("user", usuario);
                intent.putExtra("nombreUser", nombreUsuario);
                intent.putExtra("tipoUser", tipoUsuario);

                intent.putExtra("observacion", observacion);
                intent.putExtra("montopago", montopago);
                intent.putExtra("nombreCliente", nombreCli);

                startActivity(intent);
            }
        });

    }


}
