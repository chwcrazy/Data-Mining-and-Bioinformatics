package cse601;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class done {

	private ArrayList<String> center;
	private ArrayList<String> data;

	public void run(String args[]) throws IOException {
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

		data = new ArrayList<String>();
		String dataset = "hdfs://localhost:9000/user/haoweich/data.txt"; // center file
		FileSystem fs1 = FileSystem.get(URI.create(dataset), conf1);
		FSDataInputStream in1 = null;
		try {

			in1 = fs1.open(new Path(dataset));
			String ss;
			Scanner sss = new Scanner(in1);
			while (sss.hasNext()) {
				ss = sss.nextLine();
				data.add(ss);
			}
			sss.close();
		} finally {
			IOUtils.closeStream(in);
		}

		String ans = "";

		for (int k = 0; k < data.size(); k++)// calculate the Euclidean distance between xi and the K centroids
		{

			String[] row = data.get(k).split(",");

			double _min = Double.MAX_VALUE;
			int pos = 0;

			for (int i = 0; i < center.size(); i++) {
				String[] c = center.get(i).split(",");
				double distance = 0;
				for (int j = 0; j < c.length; j++)
					distance += (double) Math.pow(
							(Double.parseDouble(row[j]) - Double
									.parseDouble(c[j])), 2);
				if (_min > distance) {
					_min = distance;
					pos = i;
				}
			}
			
			ans +=  String.valueOf(k) + "," + String.valueOf(pos) + "," + data.get(k) + "\n";
		}
		
		FileSystem filesystem = FileSystem.get(URI.create(args[0] + "/result.txt"), conf1);
		OutputStream out2 = filesystem.create(new Path(args[0] + "/result.txt"));
		IOUtils.copyBytes(new ByteArrayInputStream(ans.getBytes()), out2,
				4096, true);

	}

}
