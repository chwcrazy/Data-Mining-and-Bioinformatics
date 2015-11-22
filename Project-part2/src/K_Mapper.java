package cse601;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class K_Mapper extends Mapper<LongWritable, Text, Text, Text> {

	private ArrayList<String> center;
	protected void setup(Context context) throws IOException,
			InterruptedException // read centerlist, and save to center[]
	{
		center = new ArrayList<String>();
		String centerlist = "hdfs://localhost:9000/user/haoweich/centroid.txt"; // center file
		Configuration conf1 = new Configuration();
		conf1.set("hadoop.job.ugi", "hadoop-user,hadoop-user");
		FileSystem fs = FileSystem.get(URI.create(centerlist), conf1);
		FSDataInputStream in = null;
		try {

			in = fs.open(new Path(centerlist));
			String ss;
			Scanner sss = new Scanner(in);
			while (sss.hasNext()) {
				ss = sss.nextLine();
				center.add(ss);
			}
			sss.close();
		} finally {
			IOUtils.closeStream(in);
		}

	}

	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		StringTokenizer itr = new StringTokenizer(value.toString());
		while (itr.hasMoreTokens())
		{
			// calculate the distance from first Coordinate to centroid -_min.
			String outValue = new String(itr.nextToken());
			String[] row = outValue.split(",");
			
			double _min = Double.MAX_VALUE;
			int pos = 0;

			for (int i = 0; i < center.size(); i++) {
				String[] c = center.get(i).split(",");
				double distance = 0;
				for (int j = 0; j < c.length; j++)
					distance += (double) Math.pow(
							(Double.parseDouble(row[j]) - Double.parseDouble(c[j])), 2);
				if (_min > distance) {
					_min = distance;
					pos = i;
				}
			}
			context.write(new Text(center.get(pos)), new Text(outValue));
		}
	}

}