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
public class MainVerClientesRP extends Activity {
    String usuario,nombreUsuario,tipoUsuario, codigoCliente, nombreCliente, negocioCliente, tipoCliente, cedulaCliente, txtCliente, selectClientes, observacion,montopago;
    RadioGroup RGBusqueda;
    RadioButton RBCodigo,RBNombre, RBCedula;
    Gson gson;
    Type type;
    Bundle bundle;
    ArrayList<DetallesPedidos> alDetPedidos = new ArrayList<DetallesPedidos>();
    LinearLayout llBusqueda;
    Constantes constante;
    Dialogos misDialogos;
    ListView lista;
    EditText EClientes;
    BDTablas dbcon;
    SQLiteDatabase db;
    ArrayList<Clientes> alClientes = new ArrayList<Clientes>();
    Cursor c;
    ClientesAdapter cAdapter;
    Intent intent;
    ArrayList<Clientes> alClientesBusqueda = new ArrayList<Clientes>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ver_clientes_rp);

        getActionBar().setTitle("Búsqueda de Clientes");

        usuario = getIntent().getStringExtra("user");
        nombreUsuario = getIntent().getStringExtra("nombreUser");
        tipoUsuario = getIntent().getStringExtra("tipoUser");

        codigoCliente = getIntent().getStringExtra("codigoCli");
        nombreCliente = getIntent().getStringExtra("nombreCli");
        negocioCliente = getIntent().getStringExtra("negocioCli");
        tipoCliente = getIntent().getStringExtra("tipoCli");
        cedulaCliente = getIntent().getStringExtra("cedC");
        observacion = getIntent().getStringExtra("observacion");
        montopago =  getIntent().getStringExtra("montopago");

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
            selectClientes = "select codigo,nombre,negocio,tipo_cliente, numero_identificacion, telefono from CLIENTE where (usuario = '"+usuario+"' or vendedor_aux='"+usuario+"')";
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
                codigoCliente = cAdapter.getItem(i).getCodigo();
                nombreCliente = cAdapter.getItem(i).getNombre();
                negocioCliente = cAdapter.getItem(i).getNegocio();
                tipoCliente = cAdapter.getItem(i).getTipoCliente();
                cedulaCliente =  cAdapter.getItem(i).getNumeroIdentificacion();


                //Los almaceno en un ArrayList para luego convertirlo en Json
                gson = new Gson();
                String detallesJson = gson.toJson(alDetPedidos);

                //Esto sirve para cerrar la actividad y en caso de que vuelva atras no se muestre de nuevo esta actividad
                finish();

                intent = new Intent(getApplicationContext(),MainRegistraPagos.class);
                intent.putExtra("codigoCli", codigoCliente);
                intent.putExtra("nombreCli", nombreCliente);
                intent.putExtra("direccionCli", negocioCliente);
                intent.putExtra("tipoCli", tipoCliente);
                intent.putExtra("cedC", cedulaCliente);
                intent.putExtra("user", usuario);
                intent.putExtra("nombreUser", nombreUsuario);
                intent.putExtra("tipoUser", tipoUsuario);
                intent.putExtra("observacion", observacion);
                intent.putExtra("montopago", montopago);
                //Les paso los detalles almacenados en un Json
                intent.putExtra("datosDetalles", detallesJson);
                startActivity(intent);
            }
        });

        RGBusqueda.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
                    selectClientes = "SELECT codigo,nombre,negocio,tipo_cliente, numero_identificacion, telefono FROM cliente where nombre like '%"+txtCliente+"%' and (usuario = '"+usuario+"' or vendedor_aux='"+usuario+"')";
                }
                else if (RBCedula.isChecked())
                {
                    selectClientes = "SELECT codigo,nombre,negocio,tipo_cliente, numero_identificacion, telefono FROM cliente where numero_identificacion like '%"+txtCliente+"%' and (usuario = '"+usuario+"' or vendedor_aux='"+usuario+"')";

                }
                else
                {
                    selectClientes = "SELECT codigo,nombre,negocio,tipo_cliente, numero_identificacion, telefono FROM cliente where codigo like '%"+txtCliente+"%' and (usuario = '"+usuario+"' or vendedor_aux='"+usuario+"')";
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
