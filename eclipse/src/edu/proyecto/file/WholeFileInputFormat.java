package edu.proyecto.file;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

public class WholeFileInputFormat extends FileInputFormat<KeyImage, BytesWritable> {

	
        private WholeFileRecordReader reader;
        
        public WholeFileInputFormat(){
            reader = new WholeFileRecordReader();
        }
        
        @Override
        protected boolean isSplitable(JobContext context, Path filename) {
            return false;
        }

        @Override
        public RecordReader<KeyImage, BytesWritable> createRecordReader(
                        InputSplit inputSplit, TaskAttemptContext context) throws IOException,
                        InterruptedException {                
                reader.initialize(inputSplit, context);
                return reader;
        }
}