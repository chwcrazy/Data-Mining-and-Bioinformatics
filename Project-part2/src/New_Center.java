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

public class New_Center {

	public double run(String[] args) throws IOException, InterruptedException {
		Configuration conf = new Configuration();
		conf.set("hadoop.job.ugi", "hadoop,hadoop");
		FileSystem fs = FileSystem.get(URI.create(args[2] + "/part-r-00000"),
				conf);
		FSDataInputStream in = null;
		ArrayList<String> oldcen = new ArrayList<String>();
		ArrayList<String> newcen = new ArrayList<String>();
		String outp = "";
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			in = fs.open(new Path(args[2] + "/part-r-00000"));
			Scanner inp = new Scanner(in);
			String line;
			while (inp.hasNext()) {
				line = inp.nextLine();
				if (line.replaceAll("\\s+", "").equals(""))
					continue;
				String[] tmp = line.split("\\s+");
				int len = tmp.length;
				oldcen.add(tmp[0]);
				newcen.add(tmp[len - 1]);
				outp += tmp[len - 1] + "\n";
			}
			inp.close();
		} finally {
			IOUtils.closeStream(in);
		}

		double sumdist = 0.0;
		for (int i = 0; i < oldcen.size(); i++) {
			String[] startCenter = oldcen.get(i).split(",");
			String[] finalCenter = newcen.get(i).split(",");
			for (int j = 0; j < startCenter.length; j++)
				sumdist += Math.pow(
						Double.parseDouble(startCenter[j]) - Double.parseDouble(finalCenter[j]), 2);
		}
		fs.delete(new Path(args[0] + "/centroid.txt"), true);
		OutputStream out2 = fs.create(new Path(args[0] + "/centroid.txt"));
		IOUtils.copyBytes(new ByteArrayInputStream(outp.getBytes()), out2,
				4096, true);
		return sumdist;
	}
}
