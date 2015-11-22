package cse601;

import java.io.IOException;  

import org.apache.hadoop.io.Text;  
import org.apache.hadoop.mapreduce.Reducer;  
  
  
public class K_Reducer extends Reducer<Text, Text, Text, Text> {  
    //calculate the new center 
    public void reduce(Text key,Iterable<Text> value,Context context) throws IOException,InterruptedException  
    {  
        String outVal = "";  
        int count=0;  
        String center="";  
        int length = key.toString().replace(":", "").split(",").length;  
        double[] ave = new double[length];  
        for(int i=0;i<length;i++)  
            ave[i]=0;   
        for(Text val:value)  
        {   
            outVal += val.toString()+" ";  
            String[] tmp = val.toString().split(",");   
            for(int i=0;i<tmp.length;i++)  
                ave[i] += Double.parseDouble(tmp[i]);  
            count ++;  
        }  
        for(int i=0;i<length;i++)  
        {  
            ave[i] = ave[i]/(double)count;
            if(i > 0) center += ",";
            center += ave[i];
        }   
        context.write(key, new Text(outVal+ " " + center));  
    }  
  
} 