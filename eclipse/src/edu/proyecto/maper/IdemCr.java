package edu.proyecto.maper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import edu.proyecto.file.KeyImage;

public class IdemCr extends Mapper<KeyImage, Text, KeyImage, Text> {

	//public class Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
	public static String NOMBRE_IMAGEN_RAW_HEAD = "img_raw.fits.head";
	public static String NOMBRE_IMAGEN_SPT_HEAD = "img_spt.fits.head";
	
	public static String NOMBRE_IMAGEN_FITS = "img.fits.conv.fits.cro.fits";
	
	@Override
	protected void map(KeyImage key, Text value, Context context)
			throws IOException, InterruptedException {
		
		System.out.println("MAP2");
		
		String nombreImagen = "iaa901jxq"; // key.toString()
		
		
//		con el nombre de la imagen se ejecutan con el par de archivos , nombreImagen_raw.fits y nombreImagen_spt.fits 
		//read los archivos al nodo
		
		System.out.println("Entro al Map");
        System.out.println("Imagen: " + nombreImagen);

        String nombreImagenRaw = nombreImagen+NOMBRE_IMAGEN_RAW_HEAD;
        System.out.println("Map1 comienza a leer y guardar "+nombreImagenRaw);
        
        try (FSDataInputStream fis = FileSystem.get(context.getConfiguration()).open(new Path(nombreImagenRaw))) {
            File archivo = new File(nombreImagenRaw);
            try (FileOutputStream fos = new FileOutputStream(archivo)) {
                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buf)) > 0) {
                    fos.write(buf, 0, bytesRead);
                    fos.flush();
                    context.progress();
                }
                fos.close();
                fis.close();
            }
        }
        System.out.println("Map2 finalizo lectura y guardado de "+nombreImagenRaw);
        
        String nombreImagenSpt = nombreImagen+NOMBRE_IMAGEN_SPT_HEAD;
        System.out.println("Map2 comienza a leer y guardar "+nombreImagenSpt);
        
        try (FSDataInputStream fis = FileSystem.get(context.getConfiguration()).open(new Path(nombreImagenSpt))) {
            File archivo = new File(nombreImagenSpt);
            try (FileOutputStream fos = new FileOutputStream(archivo)) {
                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buf)) > 0) {
                    fos.write(buf, 0, bytesRead);
                    fos.flush();
                    context.progress();
                }
                fos.close();
                fis.close();
            }
        }
        
        System.out.println("Map2 finalizo lectura y guardado de "+nombreImagenRaw);
        
        String nombreImagenfits = nombreImagen+NOMBRE_IMAGEN_FITS;
        System.out.println("Map2 comienza a leer y guardar "+nombreImagenfits);
        
        try (FSDataInputStream fis = FileSystem.get(context.getConfiguration()).open(new Path(nombreImagenfits))) {
            File archivo = new File(nombreImagenfits);
            try (FileOutputStream fos = new FileOutputStream(archivo)) {
                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buf)) > 0) {
                    fos.write(buf, 0, bytesRead);
                    fos.flush();
                    context.progress();
                }
                fos.close();
                fis.close();
            }
        }
        System.out.println("Map2 finalizo lectura y guardado de "+nombreImagenRaw);
        
        
        Process process = new ProcessBuilder("map2.sh", nombreImagen).start(); // el sh se encarga de generar los nombres de raw y spt para el procesamiento
        
		
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        System.out.println("Map2 ejecutando "+nombreImagen);
        String res = "";
        while ((line = br.readLine()) != null) {
            res = res.concat(line);
            context.progress();                     
        }
        
        
//		Text word = new Text();
//		IntWritable one = new IntWritable(1);
//		
//		String entrada = value.toString();
//		for(char x : entrada.toCharArray()){
//			word.set(Character.toString(x));
//			context.write(word, one);
//		}
	}
}