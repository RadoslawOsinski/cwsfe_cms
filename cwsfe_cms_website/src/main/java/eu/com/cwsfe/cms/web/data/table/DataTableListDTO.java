package eu.com.cwsfe.cms.web.data.table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radosław Osiński
 */
public class DataTableListDTO<T> {

    private List<T> aaData = new ArrayList<>();
    private String sEcho;
    private Long iTotalDisplayRecords;
    private Long iTotalRecords;

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public List<T> getAaData() {
        return aaData;
    }

    public void setAaData(List<T> aaData) {
        this.aaData = aaData;
    }

    public void setiTotalDisplayRecords(Long iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public Long getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalRecords(Long iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public Long getiTotalRecords() {
        return iTotalRecords;
    }
}
