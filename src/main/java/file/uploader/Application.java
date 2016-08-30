package file.uploader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        FileUploadClient fUC = new FileUploadClient();
        // Example using the file in the /resources folder, via ClassPathResource
        // The uploadFile client simply needs an absolute path and a filename
        // i.e. a simple string can be used if desired.
        ClassPathResource resourceFileLocation = new ClassPathResource("tempFile.txt");
        String absolutePath = null;
        try {
            absolutePath = resourceFileLocation.getURI().getPath();
            // Trim the filename from the URI - we don't want it.
            absolutePath = absolutePath.replaceAll("tempFile.txt","");
            if (absolutePath.startsWith("/")) {
                absolutePath = absolutePath.replaceFirst("/","");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        fUC.uploadFile(absolutePath, resourceFileLocation.getFilename());
    }




}