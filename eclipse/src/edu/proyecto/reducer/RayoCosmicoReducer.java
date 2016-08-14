package edu.proyecto.reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import edu.proyecto.file.ZipFileWriter;



public class RayoCosmicoReducer<Key> extends Reducer<Text,IntWritable,Text,IntWritable> {

//	@Override
//	protected void reduce(Text key, Iterable<IntWritable> values,
//			Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
////		IntWritable result = new IntWritable();
////		int sum = 0;
////		for (IntWritable val : values) {
////		  sum += val.get();
////		}
////		result.set(sum);
////		context.write(key, result);
//		System.out.println("REDUCE");
//		System.out.println("Falta implementar");
//	}

	
	protected ZipFileWriter zipFileWriter = null;
    private HashMap<String, ZipFileWriter> zips = new HashMap<>();
    static final Logger log = Logger.getLogger("log_file");
    Configuration conf = null;

    @Override
    @SuppressWarnings("unchecked")
    protected void setup(Context context)
            throws IOException, InterruptedException {
        conf = context.getConfiguration();
    }

    @Override
    public void reduce(Text key, Iterable<IntWritable> values,
			Reducer<Text, IntWritable, Text, IntWritable>.Context context) {
        System.out.println("Entro al reducer!!");
        System.out.println("Key del reducer: " + key);
        FileSystem fs;
        try {

            zipFileWriter = zips.get(key.toString());
            if (zipFileWriter == null) {
                System.out.println("CREAR " + key.toString());
                zipFileWriter = new ZipFileWriter(key.toString() + "-resultados");//set subjob id
                zipFileWriter.setup(conf);
                zipFileWriter.openZipForWriting();
                zips.put(key.toString(), zipFileWriter);
                System.out.println("Guarde Zip en map " + key.toString());
            } else {
                System.out.println("GET " + key.toString());
            }

            fs = FileSystem.get(context.getConfiguration());
//            for (FernetOutput t : values) {
//                System.out.println("Archivo de salida fernet es: " + t.getFileName().toString());
//                zipFileWriter.addBinaryFile(t.getFileName().toString(), t.getValue().getBytes(), t.getValue().getLength());
//            }

            zipFileWriter.getZipOutputStream().flush();
            zipFileWriter.closeZip();
            System.out.println("Cerre ZIP");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void cleanup(Reducer.Context context)
            throws IOException, InterruptedException {
        Iterator<String> it = zips.keySet().iterator();
        while (it.hasNext()) {
            zips.get(it.next()).closeZip();
        }
    }
	
}