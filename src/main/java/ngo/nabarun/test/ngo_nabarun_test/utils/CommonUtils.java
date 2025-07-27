package ngo.nabarun.test.ngo_nabarun_test.utils;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtils {
	private static final Logger logger = LogManager.getLogger(CommonUtils.class);
	
	public static final ObjectMapper objectMapper = new ObjectMapper();

	
	public static File takeScreenshot(WebDriver driver){
		try {
			return ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		}catch (Exception e) {
			try {
	            Robot robot = new Robot();
	            BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	            String tempDir = System.getProperty("java.io.tmpdir");
	            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	            String filename = "Screenshot_" + timestamp + ".png";
	            File outputFile = new File(tempDir + filename);
	            ImageIO.write(screenCapture, "png", outputFile);
	            return outputFile;
	        } catch (AWTException | IOException e1) {
	            logger.error("Error taking screenshot using Robot", e1);
	    		return null;
	        }
		}
	}
		
	 	private static final String INVALID_CHARACTERS = "[<>:\"/\\|?*]";

	    /**
	     * Sanitizes the given string to make it a valid file name by removing or replacing invalid characters.
	     *
	     * @param fileName The input string to sanitize.
	     * @return A sanitized string suitable for use as a file name.
	     */
	    public static String sanitizeFileName(String fileName) {
	        if (fileName == null || fileName.isEmpty()) {
	            return fileName;
	        }
	        fileName = Normalizer.normalize(fileName, Normalizer.Form.NFD);
	        fileName = fileName.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	        fileName = fileName.replaceAll(INVALID_CHARACTERS, "");
	        fileName = fileName.trim();
	        if (fileName.length() > 255) {
	            fileName = fileName.substring(0, 255);
	        }
	        return fileName;
	    }
	    
	    public static String getFileFromResources(String fileName) {
	        URL resource = CommonUtils.class.getClassLoader().getResource(fileName);
	        if (resource == null) {
	            throw new IllegalArgumentException("File not found: " + fileName);
	        }
	        return new File(resource.getFile()).getAbsolutePath();
	    }
	    
		

}

