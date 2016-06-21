package edu.proyecto.maper;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class RayoCosmicoMapper extends Mapper<Object, Text, Text, IntWritable> {

	@Override
	protected void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		
		System.out.println("MAP");
		
		Process process = new ProcessBuilder("mcell.exe", "-errfile", "errorMcell.txt", "entradaMap.mdl").start();
		
//		
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