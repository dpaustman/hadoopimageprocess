package org.orion.mipr;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageWritable implements Writable {

    private BufferedImage image;
    private String fileName;
    private String format;

    public ImageWritable() {
    }

    public ImageWritable(BufferedImage image){
        this();
        this.image = image;
    }

    public ImageWritable(BufferedImage image, String filename, String format){
        this(image);
        fileName = filename;
        this.format = format;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        // Write image type
        Text.writeString(out, format);
        // Write image file name
        Text.writeString(out, fileName);

        // Write image
        // Convert image to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, this.getFormat(), baos);
        baos.flush();
        byte[] bytes = baos.toByteArray();
        baos.close();
        // Write byte array size
        out.writeInt(bytes.length);
        // Write image bytes
        out.write(bytes);
    }

    @Override
    public void readFields(DataInput in) throws IOException{
        // Read image type
        format = Text.readString(in);
        // Read image file name
        fileName = Text.readString(in);

        // Read image byte array size
        int bArraySize = in.readInt();
        // Read image byte array
        byte[] bArray = new byte[bArraySize];
        in.readFully(bArray);
        // Read image from byte array
        image = ImageIO.read(new ByteArrayInputStream(bArray));
    }

    public String getFormat() {
        return format;
    }

    public void setFileName(String fName) {
        fileName = fName;
    }

    public void setFormat(String fFormat) {
        format = fFormat;
    }

    public void setImage(BufferedImage bi) {
        image = bi;
    }

    public BufferedImage getImage(){
        return image;
    }

    public String getFileName() {
        return fileName;
    }
}
