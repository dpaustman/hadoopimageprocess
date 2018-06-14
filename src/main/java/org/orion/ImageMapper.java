package org.orion;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.orion.mipr.ImageWritable;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageMapper extends Mapper<NullWritable, ImageWritable, NullWritable, ImageWritable> {

    @Override
    protected void map(NullWritable key, ImageWritable value, Context context) throws IOException, InterruptedException {
        BufferedImage bufferedImage = value.getImage();
        if (bufferedImage != null) {
            BufferedImage processedImage;
            try {
                processedImage = processImage(bufferedImage);
            } catch (Exception e) {
                throw new RuntimeException("encrypt process ended unsuccessfully", e);
            }
            ImageWritable biw = new ImageWritable(processedImage, "e_" + value.getFileName(), value.getFormat());
            context.write(NullWritable.get(), biw);
        }
    }

    //Do some processing here
    private BufferedImage processImage(BufferedImage origImg) {
        return origImg;
    }

}
