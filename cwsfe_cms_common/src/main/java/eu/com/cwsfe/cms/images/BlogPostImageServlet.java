package eu.com.cwsfe.cms.images;

import eu.com.cwsfe.cms.dao.BlogPostImagesDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Radoslaw Osinski
 */
public class BlogPostImageServlet extends CachingImageServlet {

    private static final long serialVersionUID = -7746211225693172326L;

    private static final Logger LOGGER = LogManager.getLogger(BlogPostImageServlet.class);

    @Autowired
    private BlogPostImagesDAO blogPostImagesDAO;

    @Override
    protected Long validateRequestedImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String imageIdString = request.getParameter("imageId");
        Long imageId;
        try {
            imageId = Long.parseLong(imageIdString);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            LOGGER.error("Number format problem: " + imageIdString, e);
            return null;
        }
        return imageId;
    }

    @Override
    protected String getImageFileName(Long imageId) {
        return blogPostImagesDAO.getWithContent(imageId).getFileName();
    }

    @Override
    protected Long getFileSize(Long imageId) {
        return blogPostImagesDAO.getWithContent(imageId).getFileSize();
    }

    @Override
    protected Long getLastModified(Long imageId) {
        return blogPostImagesDAO.getWithContent(imageId).getCreated().getTime();   //todo should be last modified!;
    }

    @Override
    protected String getMimeType(Long imageId) {
        return blogPostImagesDAO.getWithContent(imageId).getMimeType();
    }

    @Override
    protected byte[] getContent(Long imageId) {
        return blogPostImagesDAO.getWithContent(imageId).getContent();
    }

}