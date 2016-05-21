import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MapTest extends Mapper<Object, Text, Text, IntWritable> {

	@Override
	protected void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		System.out.println("MAP");
		Text word = new Text();
		IntWritable one = new IntWritable(1);
		
		String entrada = value.toString();
		for(char x : entrada.toCharArray()){
			word.set(Character.toString(x));
			context.write(word, one);
		}
	}
}
