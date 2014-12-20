package eu.com.cwsfe.cms.application.mvc;

class BusinessException extends Exception {

    private static final long serialVersionUID = -7116604121332345434L;

    private final String message;

    public BusinessException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
