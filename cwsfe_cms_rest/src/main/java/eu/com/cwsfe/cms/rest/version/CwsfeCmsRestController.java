package eu.com.cwsfe.cms.rest.version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Created by Radosław Osiński
 */
@RestController
public class CwsfeCmsRestController {

    private static final Logger LOG = LoggerFactory.getLogger(CwsfeCmsRestController.class);

    @RequestMapping(value = "/rest/version", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public Version getCmsVersion(HttpServletRequest request) {
        InputStream in = request.getSession().getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF");
        try {
            Manifest manifest = new Manifest(in);
            Attributes attributes = manifest.getMainAttributes();
            return new Version(attributes.getValue("Specification-Version"));
        } catch (IOException e) {
            LOG.error("Problem with reading manifest", e);
        }
        return new Version("development version!");
    }

    private class Version implements Serializable {

        private static final long serialVersionUID = 2279976969953624087L;

        private String version;

        private Version(String version) {
            this.version = version;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
