package ws;
import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class CONSUMEWEBSERVICEADM extends AsyncTask<String,Void,String>{

	// Namespace definido en el servicio web (Es el dominio donde se encuentra publicado el ws)
    private String NAMESPACE;
    // Fichero de definicion del servcio web (La URL donde se encuentra publicado el ws)
    private String URL;
	// namespace + metodo
	private String SOAP_ACTION;
	// Metodo que queremos ejecutar en el servicio web
    private String METHOD;
    
	public CONSUMEWEBSERVICEADM(String namespace,String url,String soap,String metodo)
	{
		this.NAMESPACE=namespace;
    	this.URL=url;
    	this.SOAP_ACTION=soap;
    	this.METHOD=metodo;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String respuesta = null;
		SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
		if(params.length==0)
		{
			userRequest.addProperty("nombre",null); //Paso de parametros al ws
		}
		else
		{
			for(int i=0; i<params.length; i++)
			{
				userRequest.addProperty("param"+i, params[i]); //Paso de parametros al ws
			}
		}

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
		envelope.setOutputSoapObject(userRequest); //Se envuelve la peticion soap

		try{
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope); //Llamada
			respuesta = envelope.getResponse().toString();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return respuesta;
	}
	
	
	
	protected void onPostExecute(String result)
    {
		try {
			List<String> list = new ArrayList<String>();
			
			//obtenemos un array de JSONObjet con todos los elementos anidados en el JSON
			JSONArray jsonArray = new JSONArray(result);
			
			for(int i=0;i<jsonArray.length();i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				list.add(jsonObject.getString("codigo"));
				list.add(jsonObject.getString("razonsocial"));
				list.add(jsonObject.getString("negocio"));
				list.add(jsonObject.getString("representa"));
				list.add(jsonObject.getString("ruc"));
				list.add(jsonObject.getString("direccion"));
				list.add(jsonObject.getString("telefonos"));
				list.add(jsonObject.getString("email"));
				list.add(jsonObject.getString("tipo"));
				list.add(jsonObject.getString("provincia"));
				list.add(jsonObject.getString("canton"));
				list.add(jsonObject.getString("parroquia"));
				list.add(jsonObject.getString("sector"));
				list.add(jsonObject.getString("ruta"));
				list.add(jsonObject.getString("cupo"));
				list.add(jsonObject.getString("grupo"));
				list.add(jsonObject.getString("orden"));
				list.add(jsonObject.getString("codfre"));
				list.add(jsonObject.getString("credito"));
				list.add(jsonObject.getString("dia"));
				list.add(jsonObject.getString("fecdesde"));
				list.add(jsonObject.getString("fecultcom"));
				list.add(jsonObject.getString("estado"));
				list.add(jsonObject.getString("tdcredito"));
				list.add(jsonObject.getString("diascredit"));
				list.add(jsonObject.getString("vendedor"));
				list.add(jsonObject.getString("formapag"));
				list.add(jsonObject.getString("iva"));
				list.add(jsonObject.getString("confinal"));
				list.add(jsonObject.getString("clase"));
				list.add(jsonObject.getString("observacion"));
				list.add(jsonObject.getString("tiponego"));
				list.add(jsonObject.getString("ejex"));
				list.add(jsonObject.getString("ejey"));
				list.add(jsonObject.getString("vendedoraux"));
			}
			//TPrueba.setText(String.valueOf(list.size()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
        super.onPostExecute(String.valueOf(result));
    }
	
	
	
	
	
}