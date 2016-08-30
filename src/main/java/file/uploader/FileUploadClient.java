package file.uploader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Zac on 8/29/2016.
 */
@Component
public class FileUploadClient {

    private final Logger log = LoggerFactory.getLogger(FileUploadClient.class);

    public String uploadFile(String directory, String filename) {
        try {
            String absolutePath = directory.endsWith("/") ? directory + filename : directory + "/" + filename;
            InputStream in = Files.newInputStream(Paths.get(absolutePath));

            // Prepare the map with the ByteArrayResource - No need to fool around with
            // Multipart files, a ByteArray will do - since that's what the service wants.
            MultiValueMap<String, Object> parts =
                    new LinkedMultiValueMap<String, Object>();
            parts.add("file", new ByteArrayResource(IOUtils.toByteArray(in)));
            parts.add("filename", filename);

            // Use the Spring RestTemplate as the HTTP Client
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<MultiValueMap<String, Object>>(parts, headers);

            // Optional: File upload path on Destination server
            parts.add("destination", "C:\\Uploads");

            ResponseEntity<String> response =
                    restTemplate.exchange("http://localhost:8080/upload/file",
                            HttpMethod.POST, requestEntity, String.class);

            if (response != null && !response.getBody().trim().equals("")) {
                log.info("Response: " + response.getBody());
                return response.getBody();
            }
            return response.getStatusCode().toString();
        } catch (Exception e) {
            log.error("Exception occurred during fileUpload(). Exception: " + e.getMessage());
        }
        return "Error";
    }
}
