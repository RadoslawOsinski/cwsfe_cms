package eu.com.cwsfe.cms.web.mvc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radosław Osiński
 */
public class BasicResponse {

    public static final String JSON_STATUS_SUCCESS = "SUCCESS";
    public static final String JSON_STATUS_FAIL = "FAIL";

    private String status;
    private List<String> errorMessages = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
