package edu.proyecto.reducer;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import edu.proyecto.file.ZipFileWriter;



public class RayoCosmicoReducer<Key> extends Reducer<Text, BytesWritable, Text, BytesWritable> {

	
	public static String NOMBRE_ARCHIVO_ZIP = "Resultados";
	
	protected ZipFileWriter zipFileWriter = null;
    static final Logger log = Logger.getLogger("log_file");
    Configuration conf = null;

    @Override
    protected void setup(Context context)
            throws IOException, InterruptedException {
        conf = context.getConfiguration();
    }

    @Override
    public void reduce(Text key, Iterable<BytesWritable> values, Context context) {
    	
        System.out.println("Entro al reducer!!");
        System.out.println("Key del reducer: " + key);
        try {

            if (zipFileWriter == null) {
                System.out.println("Creando zip " + NOMBRE_ARCHIVO_ZIP);
                zipFileWriter = new ZipFileWriter(NOMBRE_ARCHIVO_ZIP);//set subjob id
                zipFileWriter.setup(conf);
                zipFileWriter.openZipForWriting();
            
                System.out.println("Guarde Zip " + NOMBRE_ARCHIVO_ZIP);
            } else {
                System.out.println("Existe zipFileWriter " + NOMBRE_ARCHIVO_ZIP);
            }
            
            for (BytesWritable t : values) {
                System.out.println("Agregando a Zip Resultado " + key.toString());
                zipFileWriter.addBinaryFile(key.toString(), t.getBytes(), t.getLength());
            }

            zipFileWriter.getZipOutputStream().flush();
            zipFileWriter.closeZip();
            
            System.out.println("Cerre ZIP");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
	
}