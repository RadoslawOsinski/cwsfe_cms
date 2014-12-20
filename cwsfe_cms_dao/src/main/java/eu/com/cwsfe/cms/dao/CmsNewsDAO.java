package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.CmsNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CmsNewsDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getTotalNumberNotDeleted() {
        String query = "SELECT count(*) FROM CMS_NEWS WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<Object[]> searchByAjax(
            int iDisplayStart, int iDisplayLength, Integer searchAuthorId, String searchNewsCode
    ) {
        int numberOfSearchParams = 0;
        String additionalQuery = "";
        List<Object> additionalParams = new ArrayList<>(5);
        if ((searchNewsCode != null) && !searchNewsCode.isEmpty()) {
            ++numberOfSearchParams;
            additionalQuery += " and lower(news_code) like lower(?) ";
            additionalParams.add("%" + searchNewsCode + "%");
        }
        if (searchAuthorId != null) {
            ++numberOfSearchParams;
            additionalQuery += " and author_id = ? ";
            additionalParams.add(searchAuthorId);
        }

        numberOfSearchParams++;
        additionalParams.add(iDisplayLength);
        numberOfSearchParams++;
        additionalParams.add(iDisplayStart);
        Object[] dbParams = new Object[numberOfSearchParams];
        for (int i = 0; i < numberOfSearchParams; ++i) {
            dbParams[i] = additionalParams.get(i);
        }
        String query =
                "select " +
                        " cn.id, (first_name || ' ' || last_name) as author, news_type_id, folder_id, creation_date, news_code, cn.status" +
                        " from CMS_NEWS cn left join CMS_AUTHORS ca ON cn.author_id = ca.id " +
                        " where cn.status <> 'D' and ca.status <> 'D' " + additionalQuery +
                        " and 1 = 1" +
                        " order by creation_date desc" +
                        " limit ? offset ?";
        return jdbcTemplate.query(query, dbParams,
                (resultSet, i) -> {
                    Object[] o = new Object[7];
                    o[0] = resultSet.getInt("ID");
                    o[1] = resultSet.getString("AUTHOR");
                    o[2] = resultSet.getInt("NEWS_TYPE_ID");
                    o[3] = resultSet.getInt("FOLDER_ID");
                    o[4] = resultSet.getDate("CREATION_DATE");
                    o[5] = resultSet.getString("NEWS_CODE");
                    o[6] = resultSet.getString("STATUS");
                    return o;
                });
    }

    public int searchByAjaxCount(Integer searchAuthorId, String searchNewsCode) {
        int numberOfSearchParams = 0;
        String additionalQuery = "";
        List<Object> additionalParams = new ArrayList<>(5);
        if ((searchNewsCode != null) && !searchNewsCode.isEmpty()) {
            ++numberOfSearchParams;
            additionalQuery += " and lower(news_code) like lower(?) ";
            additionalParams.add("%" + searchNewsCode + "%");
        }
        if (searchAuthorId != null) {
            ++numberOfSearchParams;
            additionalQuery += " and author_id = ? ";
            additionalParams.add(searchAuthorId);
        }

        Object[] dbParamsForCount = new Object[numberOfSearchParams];
        for (int i = 0; i < numberOfSearchParams; ++i) {
            dbParamsForCount[i] = additionalParams.get(i);
        }
        String query =
                "select count(*) from (" +
                        "select " +
                        " id, author_id, news_type_id, folder_id, creation_date, news_code, status" +
                        " from CMS_NEWS" +
                        " where status <> 'D' " + additionalQuery +
                        " and 1 = 1" +
                        " order by creation_date desc" +
                        ") as results";
        return jdbcTemplate.queryForObject(query, dbParamsForCount, Integer.class);
    }

    public List<CmsNews> listAll() {
        String query =
                "SELECT " +
                        "id, author_id, news_type_id, folder_id, creation_date, news_code, status " +
                        "FROM CMS_NEWS " +
                        "WHERE status <> 'D' " +
                        "ORDER BY creation_date DESC";
        return jdbcTemplate.query(query, (resultSet, rowNum) -> mapCmsNews(resultSet));
    }

    private CmsNews mapCmsNews(ResultSet resultSet) throws SQLException {
        CmsNews cmsNews = new CmsNews();
        cmsNews.setId(resultSet.getLong("ID"));
        cmsNews.setAuthorId(resultSet.getLong("AUTHOR_ID"));
        cmsNews.setNewsTypeId(resultSet.getLong("NEWS_TYPE_ID"));
        cmsNews.setNewsFolderId(resultSet.getLong("FOLDER_ID"));
        cmsNews.setCreationDate(resultSet.getDate("CREATION_DATE"));
        cmsNews.setNewsCode(resultSet.getString("NEWS_CODE"));
        cmsNews.setStatus(resultSet.getString("STATUS"));
        return cmsNews;
    }

    @Cacheable(value="cmsNewsById")
    public CmsNews get(Long id) {
        String query =
                "SELECT " +
                        " id, author_id, news_type_id, folder_id, creation_date, news_code, status" +
                        " FROM CMS_NEWS " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) -> mapCmsNews(resultSet));
    }

    public CmsNews getByFolderName(String folderName) {
        String query =
                "SELECT " +
                        " id, author_id, news_type_id, folder_id, creation_date, news_code, status" +
                        " FROM CMS_NEWS " +
                        "WHERE news_code = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = folderName;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) -> mapCmsNews(resultSet));
    }

    @Cacheable(value="cmsNewsByNewsTypeIdNewsFolderIdNewsCode")
    public CmsNews getByNewsTypeFolderAndNewsCode(Long newsTypeId, Long newsFolderId, String newsCode) {
        String query =
                "SELECT " +
                        " id, author_id, news_type_id, folder_id, creation_date, news_code, status" +
                        " FROM CMS_NEWS " +
                        "WHERE status = 'P' AND news_type_id = ? AND folder_id = ? AND news_code = ?";
        Object[] dbParams = new Object[3];
        dbParams[0] = newsTypeId;
        dbParams[1] = newsFolderId;
        dbParams[2] = newsCode;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) -> mapCmsNews(resultSet));
    }

    public Long add(CmsNews newsPost) {
        Object[] dbParams = new Object[7];
        Long id = jdbcTemplate.queryForObject("select nextval('CMS_NEWS_S')", Long.class);
        dbParams[0] = id;
        dbParams[1] = newsPost.getAuthorId();
        dbParams[2] = newsPost.getNewsTypeId();
        dbParams[3] = newsPost.getNewsFolderId();
        dbParams[4] = newsPost.getCreationDate();
        dbParams[5] = newsPost.getNewsCode();
        dbParams[6] = "N";
        jdbcTemplate.update("INSERT INTO CMS_NEWS" +
                "(id, author_id, news_type_id, folder_id, creation_date, news_code, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)"
                , dbParams);
        return id;
    }

    @CacheEvict(value = {"cmsNewsById", "cmsNewsByNewsTypeIdNewsFolderIdNewsCode"})
    public void update(CmsNews newsPost) {
        Object[] dbParams = new Object[3];
        dbParams[0] = newsPost.getNewsCode();
        dbParams[1] = newsPost.getStatus();
        dbParams[2] = newsPost.getId();
        jdbcTemplate.update("UPDATE CMS_NEWS SET news_code = ?, status = ? WHERE id = ?", dbParams);
    }

    @CacheEvict(value = {"cmsNewsById", "cmsNewsByNewsTypeIdNewsFolderIdNewsCode"})
    public void updatePostBasicInfo(CmsNews newsPost) {
        Object[] dbParams = new Object[5];
        dbParams[0] = newsPost.getNewsTypeId();
        dbParams[1] = newsPost.getNewsFolderId();
        dbParams[2] = newsPost.getNewsCode();
        dbParams[3] = newsPost.getStatus();
        dbParams[4] = newsPost.getId();
        jdbcTemplate.update("UPDATE CMS_NEWS SET news_type_id = ?, folder_id = ?, news_code = ?, status = ? WHERE id = ?", dbParams);
    }

    @CacheEvict(value = {"cmsNewsById", "cmsNewsByNewsTypeIdNewsFolderIdNewsCode"}, allEntries = true)
    public void delete(CmsNews newsPost) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsPost.getId();
        jdbcTemplate.update("update CMS_NEWS set status = 'D' where id = ?", dbParams);
    }

    @CacheEvict(value = {"cmsNewsById", "cmsNewsByNewsTypeIdNewsFolderIdNewsCode"}, allEntries = true)
    public void undelete(CmsNews newsPost) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsPost.getId();
        jdbcTemplate.update("update CMS_NEWS set status = 'H' where id = ?", dbParams);
    }

    @CacheEvict(value = {"cmsNewsById", "cmsNewsByNewsTypeIdNewsFolderIdNewsCode"}, allEntries = true)
    public void publish(CmsNews newsPost) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsPost.getId();
        jdbcTemplate.update("update CMS_NEWS set status = 'P' where id = ?", dbParams);
    }

    public List<Object[]> listByFolderLangWithPagingForProjects(Integer newsFolderId, Long languageId, int newsPerPage, int offset) {
        Object[] dbParams = new Object[5];
        dbParams[0] = newsFolderId;
        dbParams[1] = newsFolderId;
        dbParams[2] = languageId;
        dbParams[3] = newsPerPage;
        dbParams[4] = offset;
        String query =
                "SELECT" +
                        " cn.id, cni18n.id " +
                        " FROM CMS_NEWS cn, CMS_NEWS_I18N_CONTENTS cni18n " +
                        " WHERE " +
                        "  cn.id = cni18n.news_id AND " +
                        "  cn.folder_id IN (SELECT cf2.id FROM CMS_FOLDERS cf2 WHERE (cf2.id = ? OR cf2.parent_id = ?) AND cf2.status = 'N') AND " +
                        "  cni18n.language_id = ? AND " +
                        "  cn.news_type_id = (SELECT id FROM CMS_NEWS_TYPES WHERE status = 'N' AND type = 'Projects') AND " +
                        "  cn.status = 'P' AND cni18n.status = 'P' " +
                        "ORDER BY cn.creation_date DESC " +
                        "LIMIT ? OFFSET ? ";
        return jdbcTemplate.query(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
    }

    public Integer countListByFolderLangWithPagingForProjects(Integer newsFolderId, Long languageId) {
        Object[] dbParams = new Object[3];
        dbParams[0] = newsFolderId;
        dbParams[1] = newsFolderId;
        dbParams[2] = languageId;
        String query =
                "SELECT count(*) FROM (" +
                        " SELECT " +
                        " cn.id, cni18n.id" +
                        " FROM CMS_NEWS cn, CMS_NEWS_I18N_CONTENTS cni18n" +
                        " WHERE" +
                        " cn.id = cni18n.news_id AND" +
                        " cn.folder_id IN (SELECT cf2.id FROM CMS_FOLDERS cf2 WHERE (cf2.id = ? OR cf2.parent_id = ?) AND cf2.status = 'N') AND" +
                        " cni18n.language_id = ? AND" +
                        " cn.news_type_id = (SELECT id FROM CMS_NEWS_TYPES WHERE status = 'N' AND type = 'Projects') AND" +
                        " cn.status = 'P' AND cni18n.status = 'P'" +
                        " ORDER BY cn.creation_date DESC" +
                        ") AS results";
        return jdbcTemplate.queryForObject(query, dbParams, Integer.class);
    }

    public List<Object[]> listByFolderLangWithPagingForProducts(Integer newsFolderId, Long languageId, int newsPerPage, int offset) {
        Object[] dbParams = new Object[5];
        dbParams[0] = newsFolderId;
        dbParams[1] = newsFolderId;
        dbParams[2] = languageId;
        dbParams[3] = newsPerPage;
        dbParams[4] = offset;
        String query =
                "SELECT" +
                        " cn.id, cni18n.id " +
                        " FROM CMS_NEWS cn, CMS_NEWS_I18N_CONTENTS cni18n " +
                        " WHERE " +
                        "  cn.id = cni18n.news_id AND " +
                        "  cn.folder_id IN (SELECT cf2.id FROM CMS_FOLDERS cf2 WHERE (cf2.id = ? OR cf2.parent_id = ?) AND cf2.status = 'N') AND " +
                        "  cni18n.language_id = ? AND " +
                        "  cn.news_type_id = (SELECT id FROM CMS_NEWS_TYPES WHERE status = 'N' AND type = 'Products') AND " +
                        "  cn.status = 'P' AND cni18n.status = 'P' " +
                        "ORDER BY cn.creation_date DESC " +
                        "LIMIT ? OFFSET ? ";
        return jdbcTemplate.query(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
    }

    public Integer countListByFolderLangWithPagingForProducts(Integer newsFolderId, Long languageId) {
        Object[] dbParams = new Object[3];
        dbParams[0] = newsFolderId;
        dbParams[1] = newsFolderId;
        dbParams[2] = languageId;
        String query =
                "SELECT count(*) FROM (" +
                        " SELECT " +
                        " cn.id, cni18n.id" +
                        " FROM CMS_NEWS cn, CMS_NEWS_I18N_CONTENTS cni18n" +
                        " WHERE" +
                        " cn.id = cni18n.news_id AND" +
                        " cn.folder_id IN (SELECT cf2.id FROM CMS_FOLDERS cf2 WHERE (cf2.id = ? OR cf2.parent_id = ?) AND cf2.status = 'N') AND" +
                        " cni18n.language_id = ? AND" +
                        " cn.news_type_id = (SELECT id FROM CMS_NEWS_TYPES WHERE status = 'N' AND type = 'Products') AND" +
                        " cn.status = 'P' AND cni18n.status = 'P'" +
                        " ORDER BY cn.creation_date DESC" +
                        ") AS results";
        return jdbcTemplate.queryForObject(query, dbParams, Integer.class);
    }

    public List<Object[]> listLangWithPagingForProjects(Long languageId, int newsPerPage, int offset) {
        Object[] dbParams = new Object[3];
        dbParams[0] = languageId;
        dbParams[1] = newsPerPage;
        dbParams[2] = offset;
        String query =
                "SELECT" +
                        "    cn.id, cni18n.id" +
                        "    FROM CMS_NEWS cn, CMS_NEWS_I18N_CONTENTS cni18n" +
                        "    WHERE" +
                        "        cn.id = cni18n.news_id AND" +
                        "        cni18n.language_id = ? AND" +
                        "        cn.news_type_id = (SELECT id FROM CMS_NEWS_TYPES WHERE status = 'N' AND type = 'Projects') AND" +
                        "        cn.status = 'P' AND cni18n.status = 'P'" +
                        " ORDER BY cn.creation_date DESC " +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
    }

    public Integer countListLangWithPagingForProjects(Long languageId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = languageId;
        String query =
                "SELECT count(*) FROM (" +
                        "   SELECT" +
                        "       cn.id, cni18n.id" +
                        "   FROM CMS_NEWS cn, CMS_NEWS_I18N_CONTENTS cni18n" +
                        "   WHERE" +
                        "       cn.id = cni18n.news_id AND" +
                        "       cni18n.language_id = ? AND" +
                        "       cn.news_type_id = (SELECT id FROM CMS_NEWS_TYPES WHERE status = 'N' AND type = 'Projects') AND" +
                        "       cn.status = 'P' AND cni18n.status = 'P'" +
                        "   ORDER BY cn.creation_date DESC" +
                        ") AS results";
        return jdbcTemplate.queryForObject(query, dbParams, Integer.class);
    }

    public List<Object[]> listLangWithPagingForProducts(Long languageId, int newsPerPage, int offset) {
        Object[] dbParams = new Object[3];
        dbParams[0] = languageId;
        dbParams[1] = newsPerPage;
        dbParams[2] = offset;
        String query =
                "SELECT" +
                        "    cn.id, cni18n.id" +
                        "    FROM CMS_NEWS cn, CMS_NEWS_I18N_CONTENTS cni18n" +
                        "    WHERE" +
                        "        cn.id = cni18n.news_id AND" +
                        "        cni18n.language_id = ? AND" +
                        "        cn.news_type_id = (SELECT id FROM CMS_NEWS_TYPES WHERE status = 'N' AND type = 'Products') AND" +
                        "        cn.status = 'P' AND cni18n.status = 'P'" +
                        " ORDER BY cn.creation_date DESC " +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
    }

    public Integer countListLangWithPagingForProducts(Long languageId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = languageId;
        String query =
                "SELECT count(*) FROM (" +
                        "   SELECT" +
                        "       cn.id, cni18n.id" +
                        "   FROM CMS_NEWS cn, CMS_NEWS_I18N_CONTENTS cni18n" +
                        "   WHERE" +
                        "       cn.id = cni18n.news_id AND" +
                        "       cni18n.language_id = ? AND" +
                        "       cn.news_type_id = (SELECT id FROM CMS_NEWS_TYPES WHERE status = 'N' AND type = 'Products') AND" +
                        "       cn.status = 'P' AND cni18n.status = 'P'" +
                        "   ORDER BY cn.creation_date DESC" +
                        ") AS results";
        return jdbcTemplate.queryForObject(query, dbParams, Integer.class);
    }

    public List<Object[]> listI18nProjects(Long languageId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = languageId;
        String query =
                "SELECT cn.id, cni.id " +
                        "FROM CMS_NEWS cn, CMS_NEWS_I18N_CONTENTS cni " +
                        "WHERE " +
                        "cn.status = 'P' AND cni.status = 'P' AND " +
                        "cn.id = cni.news_id AND " +
                        "cni.language_id = ? AND " +
                        "cn.news_type_id = (SELECT id FROM CMS_NEWS_TYPES WHERE status = 'N' AND type = 'Projects') " +
                        "ORDER BY creation_date DESC ";
        return jdbcTemplate.query(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
    }

    public List<Object[]> listI18nProducts(Long languageId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = languageId;
        String query =
                "SELECT cn.id, cni.id " +
                        "FROM CMS_NEWS cn, CMS_NEWS_I18N_CONTENTS cni " +
                        "WHERE " +
                        "cn.status = 'P' AND cni.status = 'P' AND " +
                        "cn.id = cni.news_id AND " +
                        "cni.language_id = ? AND " +
                        "cn.news_type_id = (SELECT id FROM CMS_NEWS_TYPES WHERE status = 'N' AND type = 'Products') " +
                        "ORDER BY creation_date DESC ";
        return jdbcTemplate.query(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
    }

}
