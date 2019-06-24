package procesos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import android.content.ActivityNotFoundException;
import android.os.Environment;

public class ARCHIVOS {
	private static final String APP_FOLDER_NAME = "admmovil.documentos";
	private static final String INVOICES = "Recibos";

	public void leerarchivo()
	{
		// Fichero del que queremos leer
		File fichero = new File("carpeta/miarchivo.txt");
		Scanner s = null;
		String linea="";
		try {
			// Leemos el contenido del fichero
			System.out.println("... Leemos el contenido del fichero ...");
			s = new Scanner(fichero);

			// Leemos linea a linea el fichero
					
			while (s.hasNextLine()) {
				linea = linea + s.nextLine(); 	// Guardamos la linea en un String
				linea = linea + "\n";
				//System.out.println(linea);      // Imprimimos la linea
			}
		} catch (Exception ex) {
			System.out.println("Mensaje: " + ex.getMessage());
		}
	}
	
	public void escribirArchivos()
	{
		try {
        	String rutaArchivo = createDirectoryAndFileName();
    		File file = new File(rutaArchivo);
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
            osw.write("Mi texto inicial \n");
            osw.write("Mi texto final \n");
            osw.flush();
            osw.close();
        } catch (IOException ioe) {
        }
	}
	
	//crea un directorio en la tarjeta del dispositivo (interna o externa)
	private String createDirectoryAndFileName(){
		 
        String FILENAME = "reciboNumero.txt";
        String fullFileName ="";
        //Obtenemos el directorio raiz "/sdcard"
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(extStorageDirectory + File.separator + APP_FOLDER_NAME);
 
        //Creamos la carpeta "com.movalink.pdf" y la subcarpeta "Invoice"
        try {
            if (!pdfDir.exists()) {
                pdfDir.mkdir();
            }
            File pdfSubDir = new File(pdfDir.getPath() + File.separator + INVOICES);
 
            if (!pdfSubDir.exists()) {
                pdfSubDir.mkdir();
            }
 
            fullFileName = Environment.getExternalStorageDirectory() + File.separator + APP_FOLDER_NAME + File.separator + INVOICES + File.separator + FILENAME;
 
            File outputFile = new File(fullFileName);
 
            if (outputFile.exists()) {
                outputFile.delete();
            }
        } catch (ActivityNotFoundException e) {
        }
        return fullFileName;
    }
}
