package file.uploader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;


/**
 * Created by Zac on 8/26/2016.
 */
@RestController
@RequestMapping("/upload")
public class FileUploadController {

    private final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    @RequestMapping("/file")
    private ResponseEntity receiveFile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final String path = request.getParameter("destination");
        final Part filePart = request.getPart("file");
        final String fileName = request.getParameter("filename");

        OutputStream out = null;
        InputStream fileContent = null;

        try {
            out = new FileOutputStream(new File(path + File.separator
                    + fileName));
            fileContent = filePart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = fileContent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            log.info("New file " + fileName + " created at " + path);
        } catch (FileNotFoundException fne) {
            log.error("You either did not specify a file to upload or are "
                    + "trying to upload a file to a protected or nonexistent "
                    + "location.");
            log.error("<br/> ERROR: " + fne.getMessage());
            ResponseEntity<String> responseEntity = new ResponseEntity<String>("ERROR: " + fne.getMessage(), HttpStatus.BAD_REQUEST);
            return responseEntity;
        } finally {
            if (out != null) {
                out.close();
            }
            if (fileContent != null) {
                fileContent.close();
            }
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<String>("New file " + fileName + " created at " + path, HttpStatus.OK);
        return responseEntity;
    }


}
