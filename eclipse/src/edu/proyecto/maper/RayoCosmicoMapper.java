package edu.proyecto.maper;

import static org.apache.commons.io.FileUtils.readFileToByteArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class RayoCosmicoMapper extends Mapper<LongWritable, Text, Text, BytesWritable> { //extends Mapper<LongWritable, Text, LongWritable, Text> {

	//public class Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
	public static String NOMBRE_IMAGEN_RAW = "_raw.fits";
	public static String NOMBRE_IMAGEN_SPT = "_spt.fits";
	
	public static String NOMBRE_IMAGEN_RES = "_res.txt";
	
	public static String WORKING_DIR = "/home/gabriel/Escritorio/proyecto/repo/proyecto2015/resources/entrada/";
	
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		
		System.out.println("MAP1");
			
		String rutaNombreImagen = value.toString(); //"iaa901jxq"; // 
	
		StringTokenizer strTock = new StringTokenizer(rutaNombreImagen, "/");		
 
		String nombreImagen = "";
		 
		 while(strTock.hasMoreTokens()){
			 nombreImagen = strTock.nextToken();
		 }
		 
		 
		
		 System.out.println("nombreImagen "+ nombreImagen);
		
		
//		con el nombre de la imagen se ejecutan con el par de archivos , nombreImagen_raw.fits y nombreImagen_spt.fits 
		//read los archivos al nodo
		
		System.out.println("Entro al Map");
        System.out.println("Imagen: " + rutaNombreImagen);


        String nombreImagenRaw = rutaNombreImagen+NOMBRE_IMAGEN_RAW;
        System.out.println("Map1 comienza a leer y guardar "+nombreImagenRaw);
        
        
        //TODO borrar
//        FileSystem fs = FileSystem.get(context.getConfiguration());
//        fs.createNewFile(new Path("ACA"));
//        fs.close();
        
//        try (FSDataInputStream fis = FileSystem.get(context.getConfiguration()).open(new Path(nombreImagenRaw))) {
//            File archivo = new File(nombreImagenRaw);
//            try (FileOutputStream fos = new FileOutputStream(archivo)) {
//                byte[] buf = new byte[1024];
//                int bytesRead;
//                while ((bytesRead = fis.read(buf)) > 0) {
//                    fos.write(buf, 0, bytesRead);
//                    fos.flush();
//                    context.progress();
//                }
//                fos.close();
//                fis.close();
//            }
//        }
        System.out.println("Map1 finalizo lectura y guardado de "+nombreImagenRaw);
        
        String nombreImagenSpt = rutaNombreImagen+NOMBRE_IMAGEN_SPT;
        System.out.println("Map1 comienza a leer y guardar "+nombreImagenSpt);
        
//        try (FSDataInputStream fis = FileSystem.get(context.getConfiguration()).open(new Path(nombreImagenSpt))) {
//            File archivo = new File(nombreImagenSpt);
//            try (FileOutputStream fos = new FileOutputStream(archivo)) {
//                byte[] buf = new byte[1024];
//                int bytesRead;
//                while ((bytesRead = fis.read(buf)) > 0) {
//                    fos.write(buf, 0, bytesRead);
//                    fos.flush();
//                    context.progress();
//                }
//                fos.close();
//                fis.close();
//            }
//        }
        System.out.println("Map1 finalizo lectura y guardado de "+nombreImagenSpt);
        
        //"../resources/map1/src/map1.sh"  , , " > salida.out"
        
        Process process = null;
        ProcessBuilder processBuilder = new ProcessBuilder("bash",WORKING_DIR+"map1.sh", nombreImagen, "> salida.out" , "2> salida.err" ); // el sh se encarga de generar los nombres de raw y spt para el procesamiento
        
        processBuilder.directory(new File(WORKING_DIR));
		
        process = processBuilder.start();
        
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        System.out.println("Map1 ejecutando "+nombreImagen);
        String res = "";
        while ((line = br.readLine()) != null) {
            res = res.concat(line);
            res = res.concat("\n");
            // TODO evaluar  context.progress();                      
        }
        
        System.out.println(res);
        
        System.out.println("Map 2 inicio");
        context.progress();        
        
        Process process2 = null;
        ProcessBuilder processBuilder2 = new ProcessBuilder("bash",WORKING_DIR+"map2.sh","> salida2.out" , "2> salida2.err" ); // el sh se encarga de generar los nombres de raw y spt para el procesamiento
        
        processBuilder2.directory(new File(WORKING_DIR));
		
        process2 = processBuilder2.start();
        
        InputStream is2 = process2.getInputStream();
        InputStreamReader isr2 = new InputStreamReader(is2);
        BufferedReader br2 = new BufferedReader(isr2);
        String line2;

        System.out.println("Map2 ejecutando "+nombreImagen);
        String res2 = "";
        while ((line2 = br2.readLine()) != null) {
            res2 = res2.concat(line2);
            res2 = res2.concat("\n");
          // TODO evaluar  context.progress();                  
        }
        
        System.out.println(res2);
        
        context.progress(); 
        
        
        String archivoResultado = nombreImagen+NOMBRE_IMAGEN_RES;
        
        byte[] buffer = readFileToByteArray(new File(WORKING_DIR+archivoResultado));
        
        
        // context.write(key, salida); para el reducer
        context.write(new Text(archivoResultado), new BytesWritable(buffer));

	}
	
	// // crearArchivoParaMap2(nombreImagen);
//	private void crearArchivoParaMap2(String nombreImagen) throws IOException{
//		String ruta = WORKING_DIR+"list_images.txt";
//		File archivo = new File(ruta);
//		BufferedWriter bw;
//	        if(archivo.exists()) {
//	        	archivo.delete();
//	            bw = new BufferedWriter(new FileWriter(archivo));
//	            bw.write(nombreImagen);
//	        } else {
//	            bw = new BufferedWriter(new FileWriter(archivo));
//	            bw.write(nombreImagen);
//	        }
//	        bw.close();
//		
//	}
	
}