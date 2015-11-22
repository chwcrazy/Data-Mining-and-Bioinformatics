package cse601;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class KMeans {
	
	public static void main(String[] args) throws Exception
	{
		long startTime = System.currentTimeMillis();   //get the start time

		Init_Center centerInitial = new Init_Center();
		centerInitial.run(args,10);//init center
		int times=0;
		double s = 1e9, shold = 0.00001;
		do {
			Configuration conf = new Configuration();
			conf.set("fs.default.name", "hdfs://localhost:9000");
			@SuppressWarnings("deprecation")
			Job job = new Job(conf,"KMeans");//set KMeans's MapReduce job
			job.setJarByClass(KMeans.class);//set job class
			job.setOutputKeyClass(Text.class);//set the output key format：Text
			job.setOutputValueClass(Text.class);//set value output value format：Text
			job.setMapperClass(K_Mapper.class);//set Mapper class
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);//set Reducer class
			job.setReducerClass(K_Reducer.class);
			FileSystem fs = FileSystem.get(conf);
			fs.delete(new Path(args[2]),true);//args[2] is output dir，fs.delete is to delete the existing output
			FileInputFormat.addInputPath(job, new Path(args[0] + "/data.txt"));
			FileOutputFormat.setOutputPath(job, new Path(args[2]));
			job.waitForCompletion(true);//run job, and wait the job complete
			if(job.waitForCompletion(true))//last mapreduce result
			{
				//compare last two centroids, if the distance is smaller than threshold, then stop, 
				//else put the last center as new center.
				New_Center newCenter = new New_Center();
				s = newCenter.run(args);
				System.out.println("s = " + s);
				times++;
			}
		} while(s > shold);// when the distance is smaller than threshold, then stop.
		System.out.println("Iterator: " + times);//Iterator times		
		
		done rst = new done();
		rst.run(args);
		long endTime = System.currentTimeMillis(); //get the end time
		System.out.println("Run Time： "+(endTime-startTime)+"ms");
		
	}

}
