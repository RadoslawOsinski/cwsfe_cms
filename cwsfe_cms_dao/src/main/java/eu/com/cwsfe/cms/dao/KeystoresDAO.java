package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.Keystore;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@Repository
public class KeystoresDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeystoresDAO.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public KeystoresDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Keystore mapKeystore(ResultSet resultSet) throws SQLException {
        Keystore keystore = new Keystore();
        keystore.setId(resultSet.getInt("ID"));
        keystore.setName(resultSet.getString("NAME"));
        keystore.setContent(new ByteArrayInputStream(resultSet.getBytes("CONTENT")));
        return keystore;
    }

    public Integer add(Keystore keystore) {
        Object[] dbParams = new Object[3];
        Integer id = jdbcTemplate.queryForObject("SELECT nextval('KEYSTORES_S')", Integer.class);
        dbParams[0] = id;
        dbParams[1] = keystore.getName();
        try {
            dbParams[2] = IOUtils.toByteArray(keystore.getContent());
        } catch (IOException e) {
            LOGGER.error("Problem with converting bytes", e);
        }
        jdbcTemplate.update("INSERT INTO keystores(id, name, content)" +
                " VALUES (?, ?, ?)", dbParams);
        return id;
    }

    public void delete(Keystore keystore) {
        jdbcTemplate.update("DELETE FROM keystores WHERE id = ?", keystore.getId());
    }

    public Keystore getByName(String name) {
        String query =
                "SELECT id, name, content " +
                        " FROM keystores " +
                        "WHERE name = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = name;
        Keystore keystore = null;
        try {
            keystore = jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                    mapKeystore(resultSet));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [{}] with params: {}", query, Arrays.toString(dbParams), e);
        }
        return keystore;
    }
}
