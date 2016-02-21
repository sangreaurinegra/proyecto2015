package edu.proyecto.maper;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Main extends Configured implements Tool {
	
	private final static Logger logger = Logger.getLogger(Main.class.getName());
	
    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Main(), args);
        System.exit(res);
    }

	@Override
	public int run(String[] args) throws Exception {

		logger.info("Inicio");
		
		for (int i = 0; i < args.length; i++) {
			//System.out.println("Hadoop - arg[" + i + "] es: " + args[i]);
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


   

		return 0;
	}

}
