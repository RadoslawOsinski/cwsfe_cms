package eu.com.cwsfe.cms.web.mvc;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.validation.BindingResult;

/**
 * Created by Radosław Osiński
 */
public abstract class JsonController {

    public static final String JSON_ERROR_MESSAGES = "errorMessages";
    public static final String JSON_RESULT = "result";
    public static final String JSON_STATUS = "status";
    public static final String JSON_STATUS_SUCCESS = "SUCCESS";
    public static final String JSON_STATUS_FAIL = "FAIL";

    public static final String CWSFE_CMS_RESOURCE_BUNDLE_PATH = "cwsfe_cms_i18n";

    protected void prepareErrorResponse(BindingResult result, JSONObject responseDetailsJson) {
        responseDetailsJson.put(JSON_STATUS, JSON_STATUS_FAIL);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < result.getAllErrors().size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("error", result.getAllErrors().get(i).getCode());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put(JSON_RESULT, jsonArray);
    }

    protected void addJsonSuccess(JSONObject responseDetailsJson) {
        responseDetailsJson.put(JSON_STATUS, JSON_STATUS_SUCCESS);
        responseDetailsJson.put(JSON_RESULT, "");
    }

    protected void addErrorMessage(JSONObject responseDetailsJson, String message) {
        String[] messages = new String[1];
        messages[0] = message;
        addErrorMessages(responseDetailsJson, messages);
    }

    protected void addErrorMessages(JSONObject responseDetailsJson, String[] messages) {
        JSONArray jsonArray = (JSONArray) responseDetailsJson.get(JSON_ERROR_MESSAGES);
        if (jsonArray == null) {
            jsonArray = new JSONArray();
        }
        responseDetailsJson.put(JSON_STATUS, JSON_STATUS_FAIL);
        for (String message : messages) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("error", message);
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.remove(JSON_ERROR_MESSAGES);
        responseDetailsJson.put(JSON_ERROR_MESSAGES, jsonArray);
    }

}
