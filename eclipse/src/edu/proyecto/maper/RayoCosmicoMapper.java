package edu.proyecto.maper;

import static org.apache.commons.io.FileUtils.readFileToByteArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class RayoCosmicoMapper extends Mapper<LongWritable, Text, Text, BytesWritable> { //extends Mapper<LongWritable, Text, LongWritable, Text> {

	//public class Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
	public static String NOMBRE_IMAGEN_RAW = "_raw.fits";
	public static String NOMBRE_IMAGEN_SPT = "_spt.fits";
	
	public static String NOMBRE_IMAGEN_RES = "_res.txt";
	
	public static String WORKING_DIR = "/root/workingdir/";//"/home/gabriel/Escritorio/proyecto/repo/proyecto2015/resources/workingdir/";
	
	String localFileRaw = "";
	String localFileSpt = "";
	
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
        
        localFileRaw = getWorkingDir()+nombreImagen+NOMBRE_IMAGEN_RAW;
        
        copiarArchivoLocalmente(context, nombreImagenRaw, localFileRaw);
        
        System.out.println("Map1 finalizo lectura y guardado de "+nombreImagenRaw);
        
        String nombreImagenSpt = rutaNombreImagen+NOMBRE_IMAGEN_SPT;
        System.out.println("Map1 comienza a leer y guardar "+nombreImagenSpt);
        
        localFileSpt = getWorkingDir()+nombreImagen+NOMBRE_IMAGEN_SPT;
        
        copiarArchivoLocalmente(context, nombreImagenSpt, localFileSpt);
      
        System.out.println("Map1 finalizo lectura y guardado de "+nombreImagenSpt);
        
        //"../resources/map1/src/map1.sh"  , , " > salida.out"
        
        Process process = null;
        //"--init-file "+getWorkingDir()+"bash_ini.sh"
        ProcessBuilder processBuilder = new ProcessBuilder("bash",getWorkingDir()+"map1.sh", nombreImagen, "> salida.out" , "2> salida.err" ); // el sh se encarga de generar los nombres de raw y spt para el procesamiento
        
        processBuilder.directory(new File(getWorkingDir()));
		
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
        ProcessBuilder processBuilder2 = new ProcessBuilder("bash",getWorkingDir()+"map2.sh","> salida2.out" , "2> salida2.err" ); // el sh se encarga de generar los nombres de raw y spt para el procesamiento
        
        processBuilder2.directory(new File(getWorkingDir()));
		
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
        
        clean(getWorkingDir(), nombreImagen);
        
        
        String archivoResultado = nombreImagen+NOMBRE_IMAGEN_RES;
        
        byte[] buffer = readFileToByteArray(new File(getWorkingDir()+archivoResultado));
        
        
        // context.write(key, salida); para el reducer
        context.write(new Text(archivoResultado), new BytesWritable(buffer));

	}

	/**
	 * @param context
	 * @param remoteFile
	 * @param localFile
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 */
	private void copiarArchivoLocalmente(Context context, String remoteFile, String localFile)
			throws IOException, IllegalArgumentException, FileNotFoundException {
        
        try (FSDataInputStream fis = FileSystem.get(context.getConfiguration()).open(new Path(remoteFile))) {
            File archivo = new File(localFile);
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
        
	}
	
	/**
	 * Borra los archivos intermedios locales una vez finalizado el procesamiento 
	 */
    private void clean(String workingDir , String nombreImagen){
    	
    	String[] archs = new String[14];
    	archs[0] = workingDir+nombreImagen+"_raw.fits.conv.fits.cro.fits";
    	archs[1] = workingDir+nombreImagen+"_raw.fits.conv.fits.crr.fits";
    	archs[2] = workingDir+nombreImagen+"_raw.fits.conv.fits.cro.fits.cat";
    	archs[3] = workingDir+nombreImagen+"_raw.fits.conv.fits";
    	archs[4] = workingDir+nombreImagen+"_raw.fits.head";
    	archs[5] = workingDir+nombreImagen+"_spt.fits.head";
    	archs[6] = workingDir+"sptdata.txt";
    	archs[7] = workingDir+"rawdata.txt";
    	archs[8] = workingDir+"list_images.txt";
    	archs[9] = workingDir+"default.sex";
    	archs[10] = workingDir+"darks";
    	archs[11] = workingDir+"cdarks";
    	archs[12] = localFileRaw;
    	archs[13] = localFileSpt;
 
    	
    	for (String arch : archs) {
    		File fichero = new File(arch);
    		 if (fichero.delete())
    	            System.out.println("El archivo "+arch+" ha sido borrado ");
    	         else
    	            System.out.println("El archivo "+arch+" no puede ser borrado");
		}
       
    	
    }
	
    public String getWorkingDir(){
    	String ret = System.getenv("WORKING_DIR");
    	if(ret == null){
    		ret = WORKING_DIR;
    	}
    	
    	return ret;
    }
    
}