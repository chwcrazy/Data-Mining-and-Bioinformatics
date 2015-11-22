package cse601;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class Init_Center {

	public void run(String[] args, int k) throws IOException {
		String data = args[0] + "/" + args[1];// "hdfs://localhost:9000/user/haoweich/iyer.txt";
		String outpath = args[0];
		Configuration conf1 = new Configuration();
		conf1.set("hadoop.job.ugi", "hadoop-user,hadoop-user");
		FileSystem fs = FileSystem.get(URI.create(data), conf1);
		FSDataInputStream in = null;
		ArrayList<String> all = new ArrayList<String>();
		String alldata = "";
		try {
			in = fs.open(new Path(data));
			String ss;
			Scanner sss = new Scanner(in);
			while (sss.hasNext()) {
				ss = sss.nextLine();
				if(ss.replaceAll("\\s+", "").equals("")) continue;
				String[] tmp = ss.split("\\s+");
				String row = "";
				for(int i = 2 ; i < tmp.length ; i++){
					if(i > 2)row += ",";
					row += tmp[i];
				}
				alldata += row + "\n";
				all.add(row);
			}
			sss.close();
		} finally {
			IOUtils.closeStream(in);
		}
		// for(int i = 0 ; i < all.size() ; i++){
		// System.out.println(all.get(i));
		// }
		Random rand = new Random();
		int[] random = new int[k];
		String string = "";
		for (int j = 0; j < k; j++) {
			int tmp = rand.nextInt(all.size());
			boolean flag = true;
			while (flag) {
				flag = false;
				for (int m = 0; m < j; m++) {
					if (tmp == random[m]) {
						tmp = rand.nextInt(all.size()) + 1;
						flag = true;
						break;
					}
				}
			}
			random[j] = tmp;
			string += all.get(random[j]) + "\n";
		}
		FileSystem filesystem = FileSystem.get(URI.create(outpath + "/centroid.txt"), conf1); // get URI's HDFS filesystem
		OutputStream out2 = filesystem.create(new Path(outpath + "/centroid.txt"));
		IOUtils.copyBytes(new ByteArrayInputStream(string.getBytes()), out2,
				4096, true);
		
//		FileSystem files = FileSystem.get(URI.create(outpath + "/data.txt"), conf1); // get URI's HDFS filesystem
		OutputStream out3 = filesystem.create(new Path(outpath + "/data.txt"));
		IOUtils.copyBytes(new ByteArrayInputStream(alldata.getBytes()), out3,
				4096, true);
		// System.out.println("init:\n"+string);
	}

}
