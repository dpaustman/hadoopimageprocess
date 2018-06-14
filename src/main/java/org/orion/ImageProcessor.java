package org.orion;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.orion.mipr.ImageInputFormat;
import org.orion.mipr.ImageOutputFormat;
import org.orion.mipr.ImageWritable;

import java.io.IOException;

public class ImageProcessor extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new ImageProcessor(), args);
        System.exit(res);
    }

    private static void removeDir(String pathToDirectory, Configuration conf) throws IOException {
        Path pathToRemove = new Path(pathToDirectory);
        FileSystem fileSystem = FileSystem.get(conf);
        if (fileSystem.exists(pathToRemove)) {
            fileSystem.delete(pathToRemove, true);
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = new Configuration();

        conf.set("yarn.resourcemanager.scheduler.address", "192.168.205.11:8030");
        conf.set("yarn.resourcemanager.address", "192.168.205.11:8032");
        conf.set("mapreduce.framework.name", "yarn");
        conf.set("fs.defaultFS", "hdfs://192.168.205.11:54310");
        conf.set("mapreduce.job.jar","./build/libs/hadoopimageprocess-1.0-SNAPSHOT.jar");

        // Clean up output directory
        removeDir(args[1], conf);
        conf.setStrings("encryption.outdir", args[1]);

        Job job = Job.getInstance(conf, "encryption");
        job.setJarByClass(ImageProcessor.class);
        job.setInputFormatClass(ImageInputFormat.class);
        job.setOutputFormatClass(ImageOutputFormat.class);
        job.setMapperClass(ImageMapper.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(ImageWritable.class);
        // In our case we don't need Reduce phase
        job.setNumReduceTasks(0);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        return job.waitForCompletion(true) ? 0 : 1;
    }
}
