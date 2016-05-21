import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

//import org.apache.hadoop.mapred.FileInputFormat;

import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//import org.apache.hadoop.mapred.FileOutputFormat;
//import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Main extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		System.out.println("Entro al main");
		int res = ToolRunner.run(new Main(), args);
        System.exit(res);
	}

	@Override
	public int run(String[] arg0) throws Exception {
		System.out.println("Entro a run");
		
		Configuration conf = new Configuration();
		
	    Job job = Job.getInstance(conf, "word count");
	    
	    //job.setInputFormatClass(WholeFileInputFormat.class);
	    //job.setInputFormatClass();
	    job.setJarByClass(Main.class);
	    job.setMapperClass(MapTest.class);
	    
	    job.setReducerClass(ReduceTest.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(IntWritable.class);
	    FileInputFormat.addInputPath(job, new Path(arg0[0]));
	    FileOutputFormat.setOutputPath(job, new Path(arg0[1]));
		
	    //FileInputFormat.addInputPath(conf, new Path(arg0[0]));
	    
	    job.waitForCompletion(true);
	    
		return 0;
	}

}
