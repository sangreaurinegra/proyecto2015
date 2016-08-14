package edu.proyecto;

import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import edu.proyecto.maper.RayoCosmicoMapper;
import edu.proyecto.reducer.RayoCosmicoReducer;

public class Main extends Configured implements Tool {
	
	private final static Logger logger = Logger.getLogger(Main.class.getName());
	
    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Main(), args);
        System.exit(res);
    }

	@Override
	public int run(String[] args) throws Exception {

		logger.info("Inicio");
		
		// algo
		
		for (int i = 0; i < args.length; i++) {
			logger.info("Hadoop - arg[" + i + "] es: " + args[i]);
		}
		//Configuration
		Configuration conf = new Configuration();
		conf.set("mapreduce.map.memory.mb", "1400");
		conf.set("mapreduce.reduce.memory.mb", "2800");
		conf.set("mapreduce.map.java.opts", "-Xmx1120m");
		conf.set("mapreduce.reduce.java.opts", "-Xmx2240m");
		conf.set("yarn.app.mapreduce.am.resource.mb", "2800");
		conf.set("yarn.app.mapreduce.am.command-opts", "-Xmx2240m");
		conf.set("yarn.nodemanager.resource.memory-mb", "5040");
		conf.set("yarn.scheduler.minimum-allocation-mb", "1400");
		conf.set("yarn.scheduler.maximum-allocation-mb", "5040");
		conf.set("mapreduce.task.timeout", "18000000");//5 horas

		Job job = Job.getInstance(conf, "Rayo Cosmico Conf");
		
		job.setJarByClass(Main.class);
	    
//		FileInputFormat.addInputPath(job, new Path(args[0]));
//	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));   
   
        MultipleOutputs.addNamedOutput(job, "controloutput", TextOutputFormat.class, Text.class, Text.class);
        MultipleOutputs.addNamedOutput(job, "error", TextOutputFormat.class, Text.class, Text.class);
	    
//	    Ver pada distribuir a los nodos los archivos TODO como mejora
//	    job.addCacheFile(new Path("wasb:///mcell.exe").toUri());
//        job.addCacheFile(new Path("wasb:///fernet.exe").toUri());
	    
        
        job.setMapperClass(RayoCosmicoMapper.class);
//        TODO map chain deshabilitado probando solo primer mapper.
//        Configuration map1Conf = new Configuration(false);
//        ChainMapper.addMapper(job, RayoCosmicoMapper.class, Text.class, IntWritable.class,
//        		Text.class, IntWritable.class, map1Conf);
//        
//        Configuration map2Conf = new Configuration(false);
//        ChainMapper.addMapper(job, IdemCr.class, Text.class, IntWritable.class,
//        		Text.class, IntWritable.class, map2Conf);
        
        
	    job.setReducerClass(RayoCosmicoReducer.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(IntWritable.class);
	    
	    
	    
	    job.submit();
	    
		return 0;
	}

}
