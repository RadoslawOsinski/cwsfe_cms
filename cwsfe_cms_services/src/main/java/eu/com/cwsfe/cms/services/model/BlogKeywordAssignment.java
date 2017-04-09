package eu.com.cwsfe.cms.services.model;

public class BlogKeywordAssignment extends BlogKeyword {

    private static final long serialVersionUID = -8674722987638917725L;

    /**
     * Is keyword assigned to post
     */
    private boolean assigned;

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
}
