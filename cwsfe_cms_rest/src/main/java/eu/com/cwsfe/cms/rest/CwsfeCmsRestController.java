//package eu.com.cwsfe.cms.rest;
//
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.Serializable;
//
///**
// * Created by Radosław Osiński
// */
//todo mappers http://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
//@RestController
//public class CwsfeCmsRestController {
//
//    @RequestMapping(value = "/rest/version", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public Version getCmsVersion() {
//        return new Version("1.5");
//    }
//
//    private class Version implements Serializable {
//
//        private static final long serialVersionUID = 2279976969953624087L;
//
//        private String version;
//
//        private Version(String version) {
//            this.version = version;
//        }
//
//        public String getVersion() {
//            return version;
//        }
//
//        public void setVersion(String version) {
//            this.version = version;
//        }
//    }
//}
