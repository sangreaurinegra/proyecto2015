package edu.proyecto.file;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class WholeFileRecordReader extends
        RecordReader<KeyImage, BytesWritable> {

    private FileSplit imagen;
    private Configuration conf;

    private final BytesWritable currValue = new BytesWritable();
    private final KeyImage key = new KeyImage();
    private boolean fileProcessed = false;
    
    private TaskAttemptContext context;

    @Override
    public void initialize(InputSplit archivoImagen, TaskAttemptContext context)
            throws IOException, InterruptedException {
        this.imagen = (FileSplit) archivoImagen;
        this.conf = context.getConfiguration();
        this.context = context;

    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (fileProcessed) {
            return false;
        }
        
        System.out.println("Pido archivo y el path es: " + this.imagen.getPath());
        //parsear Scene.main.mdl-modoFernet-IdUsuario.IdJob
        String nombreImg = this.imagen.getPath().getName();
       
        this.key.setNombreImg(new Text(nombreImg));

        int fileLength = (int) imagen.getLength();
        byte[] result = new byte[fileLength];

        FileSystem fs = FileSystem.get(conf);
        FSDataInputStream in = null;
        try {
            in = fs.open(imagen.getPath());
            IOUtils.readFully(in, result, 0, fileLength);
            currValue.set(result, 0, fileLength);

        } finally {
            IOUtils.closeStream(in);
        }
        this.fileProcessed = true;
        return true;
    }

    @Override
    public KeyImage getCurrentKey() throws IOException,
            InterruptedException {
        return this.key;
    }

    @Override
    public BytesWritable getCurrentValue() throws IOException,
            InterruptedException {
        return currValue;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return context.getProgress();
    }

    @Override
    public void close() throws IOException {
        // nothing to close
    }
}
