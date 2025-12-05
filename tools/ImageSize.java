import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageSize {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: java ImageSize <imagePath>");
            System.exit(1);
        }
        File f = new File(args[0]);
        BufferedImage img = ImageIO.read(f);
        if (img == null) {
            System.err.println("No se pudo leer la imagen: " + args[0]);
            System.exit(2);
        }
        System.out.println(img.getWidth() + " " + img.getHeight());
    }
}

