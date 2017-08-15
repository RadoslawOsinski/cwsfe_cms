package eu.com.cwsfe.cms.web.mvc;

import org.springframework.validation.BindingResult;

/**
 * Created by Radosław Osiński
 */
public abstract class JsonController {

    public static final String CWSFE_CMS_RESOURCE_BUNDLE_PATH = "cwsfe_cms_i18n";

    protected BasicResponse prepareErrorResponse(BindingResult result) {
        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setStatus(BasicResponse.JSON_STATUS_FAIL);
        for (int i = 0; i < result.getAllErrors().size(); i++) {
            basicResponse.getErrorMessages().add(result.getAllErrors().get(i).getCode());
        }
        return basicResponse;
    }

    protected BasicResponse getSuccess() {
        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setStatus(BasicResponse.JSON_STATUS_SUCCESS);
        return basicResponse;
    }

    protected BasicResponse getErrorMessage(String message) {
        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setStatus(BasicResponse.JSON_STATUS_FAIL);
        basicResponse.getErrorMessages().add(message);
        return basicResponse;
    }

    protected BasicResponse getErrorMessages(String[] messages) {
        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setStatus(BasicResponse.JSON_STATUS_FAIL);
        for (String message : messages) {
            basicResponse.getErrorMessages().add(message);
        }
        return basicResponse;
    }

    protected BasicResponse getErrorResponse(String[] messages) {
        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setStatus(BasicResponse.JSON_STATUS_FAIL);
        for (String message : messages) {
            basicResponse.getErrorMessages().add(message);
        }
        return basicResponse;
    }

}
