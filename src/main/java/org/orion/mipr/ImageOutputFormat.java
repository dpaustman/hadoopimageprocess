package org.orion.mipr;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class ImageOutputFormat extends FileOutputFormat<NullWritable, ImageWritable> {

    @Override
    public RecordWriter<NullWritable, ImageWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new ImageRecordWriter(taskAttemptContext);
    }
}
