package com.birobidsa.admmovil.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;

import clasesAdapter.BancoAdapter;
import conexionBase.BDTablas;
import constantes.Constantes;
import dialogos.Dialogos;
import misclases.Banco;

@SuppressLint("NewApi")
public class MainBancos extends Activity {

    String selectBancos,codbanco,nombrebanco, montopago,numerod, saldod, nombrepago, fechavend, txtCliente,
            codigoCliente,usuario,nombreUsuario,tipoUsuario,observacion,
            numCuenta,numChq,banco,fechVenChq;
    Spinner txtFormPag;
    Constantes constante;
    SQLiteDatabase db;
    BDTablas dbcon;
    Dialogos misDialogos = new Dialogos();
    Cursor c;
    ArrayList<Banco> albanco = new ArrayList<Banco>();
    ListView lista;
    BancoAdapter cAdapter;
    Gson gson;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ver_bancos);

        getActionBar().setTitle("Búsqueda Bancos");

        codigoCliente = getIntent().getStringExtra("codigoCli");

        numerod = getIntent().getStringExtra("numeroD");
        saldod = getIntent().getStringExtra("saldoD");
        fechavend = getIntent().getStringExtra("fechavenD");


        usuario = getIntent().getStringExtra("user");
        nombreUsuario = getIntent().getStringExtra("nombreUser");
        tipoUsuario = getIntent().getStringExtra("tipoUser");

        nombrepago = getIntent().getStringExtra("nombrepago");

        banco = getIntent().getStringExtra("banco");
        numCuenta = getIntent().getStringExtra("numcuenta");
        numChq = getIntent().getStringExtra("numcheque");
        fechVenChq = getIntent().getStringExtra("fechvenchq");

        observacion = getIntent().getStringExtra("observacion");

        montopago = getIntent().getStringExtra("montopago");

        txtFormPag = (Spinner) findViewById(R.id.txtFormPag);

        constante = new Constantes();
        try{
            dbcon = new BDTablas(this,constante.DATABASE_NAME,null,constante.DATABASE_VERSION);
            db = dbcon.getWritableDatabase();
        }catch(Exception e){
            misDialogos.miDialogoToastLargo(this, "Excepción generada al conectarse a la Base de datos: Clase: MainVerBancos --> " + e.getMessage());
            return;
        }

        if(db!=null)
        {
            selectBancos ="select codigo,nombre  from BANCO ";
            c = db.rawQuery(selectBancos, null);
            if(c.moveToFirst())
            {
                do
                {
                    albanco.add(new Banco(c.getString(0),c.getString(1)));
                }while(c.moveToNext());
            }

            try{
                lista = (ListView)findViewById(R.id.lvBancos);
                cAdapter = new BancoAdapter(this, albanco);
                lista.setAdapter(cAdapter);
            }catch(Exception e){
                misDialogos.miDialogoToastLargo(this, "No existen bancos" + e.getMessage());
                return;
            }


        }

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                banco = cAdapter.getItem(i).getCodigo();

                finish();

                intent = new Intent(getApplicationContext(),MainRegistraPagos.class);

                intent.putExtra("codigoCli", codigoCliente);

                intent.putExtra("numeroD", numerod);
                intent.putExtra("saldoD", saldod);
                intent.putExtra("fechavenD", fechavend);

                intent.putExtra("user", usuario);
                intent.putExtra("nombreUser", nombreUsuario);
                intent.putExtra("tipoUser", tipoUsuario);

                intent.putExtra("nombrepago", nombrepago);

                intent.putExtra("banco", banco);
                intent.putExtra("numcuenta", numCuenta);
                intent.putExtra("numcheque", numChq);
                intent.putExtra("fechvenchq", fechVenChq);

                intent.putExtra("observacion", observacion);

                intent.putExtra("montopago", montopago);



                startActivity(intent);
            }
        });

        }



}
