package dbii;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigReader {
	
	public String readConfigFile(String fileName) {
        String fileContents = "";
        try {
            Path path = Paths.get(getClass().getResource("/resources/" + fileName).toURI());
            fileContents = new String(Files.readAllBytes(path));
        } catch (IOException | URISyntaxException e) {
            System.err.println("Error reading config file: " + e.getMessage());
        }
        return fileContents;
    }
	
    public static void main(String[] args) {
        ConfigReader reader = new ConfigReader();
        String configFileContents = reader.readConfigFile("DBApp.config");
        System.out.println(configFileContents);
    }    
    
}
