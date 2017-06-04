package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.DaoTestsConfiguration;
import eu.com.cwsfe.cms.model.Keystore;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * @author Radoslaw Osinski
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Rollback
@ContextConfiguration(classes = {DaoTestsConfiguration.class, KeystoresDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class KeystoresDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private KeystoresRepository dao;

    @Test
    public void add() throws Exception {
        //given
        String name = "name";
        Keystore keystore = new Keystore();
        keystore.setName(name);
        keystore.setContent(getTestInputStream());

        //when
        Integer addedId = dao.add(keystore);

        //then
        Keystore result = dao.getByName(name);
        assertNotNull(result);
        assertEquals(addedId, result.getId());
        assertEquals(name, result.getName());
        assertTrue(IOUtils.contentEquals(getTestInputStream(), result.getContent()));
    }

    @Test
    public void delete() throws Exception {
        //given
        String name = "name";
        Keystore keystore = new Keystore();
        keystore.setName(name);
        keystore.setContent(getTestInputStream());
        keystore.setId(dao.add(keystore));

        //when
        dao.delete(keystore);

        //then
        Keystore result = dao.getByName(name);
        assertNull(result);
    }

    @Test
    public void getByName() throws Exception {
        //given
        String name = "name";
        Keystore keystore = new Keystore();
        keystore.setName(name);
        keystore.setContent(getTestInputStream());
        Integer addedId = dao.add(keystore);

        //when
        Keystore result = dao.getByName(name);

        //then
        assertNotNull(result);
        assertEquals(addedId, result.getId());
        assertEquals(name, result.getName());
        assertTrue(IOUtils.contentEquals(getTestInputStream(), result.getContent()));
    }

    private FileInputStream getTestInputStream() throws FileNotFoundException {
        return new FileInputStream(new File("build.gradle"));
    }
}
