package examples;

import static org.bytedeco.leptonica.global.lept.pixDestroy;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.leptonica.global.lept;
import org.bytedeco.tesseract.TessBaseAPI;

/**
 * Recognize text on the picture. Modified example. Works with BufferedImage instead link to image
 * file.
 * <p>
 * Original example from JavaCPP.
 *
 * @see <a href="https://github.com/bytedeco/javacpp-presets/tree/master/tesseract">
 * JavaCPP Presets for Tesseract/a>
 */
public class BufferedImageExample {

  public static void main(String[] args) {
    BytePointer outText;

    TessBaseAPI api = new TessBaseAPI();

    //String tessdata = System.getenv("TESSDATA_PREFIX");
    String tessdata = "/usr/share/tesseract/tessdata/";

    // Initialize tesseract-ocr with English, without specifying tessdata path
    if (api.Init(tessdata, "eng") != 0) {
      System.err.println("Could not initialize tesseract.");
      System.exit(1);
    }

    //File imgPath = new File("images/test.png");
    File imgPath = new File("images/slack.png");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    BufferedImage img;

    try {
      img = ImageIO.read(imgPath);
      ImageIO.write(img, "png", baos);
    } catch (IOException e) {
      System.err.println("Reading file or writing byte[] failed.");
      e.printStackTrace();
    }

    byte[] imageInByte = baos.toByteArray();

    PIX image = lept.pixReadMemPng(imageInByte, imageInByte.length);

    api.SetImage(image);
    // Get OCR result
    outText = api.GetUTF8Text();
    System.out.println("OCR output:\n" + outText.getString());

    // Destroy used object and release memory
    api.End();
    outText.deallocate();
    pixDestroy(image);
  }
}
