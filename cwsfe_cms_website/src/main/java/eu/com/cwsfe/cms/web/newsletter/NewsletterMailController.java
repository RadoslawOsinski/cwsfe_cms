package eu.com.cwsfe.cms.web.newsletter;

import eu.com.cwsfe.cms.dao.NewsletterMailAddressDAO;
import eu.com.cwsfe.cms.dao.NewsletterMailDAO;
import eu.com.cwsfe.cms.dao.NewsletterMailGroupDAO;
import eu.com.cwsfe.cms.model.Breadcrumb;
import eu.com.cwsfe.cms.model.NewsletterMail;
import eu.com.cwsfe.cms.model.NewsletterMailAddress;
import eu.com.cwsfe.cms.model.NewsletterMailGroup;
import eu.com.cwsfe.cms.web.mail.CmsMailSender;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Radoslaw Osinski
 */
@Controller
public class NewsletterMailController extends JsonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsletterMailController.class);

    private final NewsletterMailDAO newsletterMailDAO;
    private final NewsletterMailAddressDAO newsletterMailAddressDAO;
    private final NewsletterMailGroupDAO newsletterMailGroupDAO;
    private final CmsMailSender cmsMailSender;

    @Autowired
    public NewsletterMailController(NewsletterMailAddressDAO newsletterMailAddressDAO, NewsletterMailDAO newsletterMailDAO, CmsMailSender cmsMailSender, NewsletterMailGroupDAO newsletterMailGroupDAO) {
        this.newsletterMailAddressDAO = newsletterMailAddressDAO;
        this.newsletterMailDAO = newsletterMailDAO;
        this.cmsMailSender = cmsMailSender;
        this.newsletterMailGroupDAO = newsletterMailGroupDAO;
    }

    @RequestMapping(value = "/newsletterMails", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/newsletterMails/NewsletterMails";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/newsletterMails/NewsletterMails.js";
    }

    private List<Breadcrumb> getBreadcrumbs(Locale locale) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new Breadcrumb(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/newsletterMails").build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailsManagement")));
        return breadcrumbs;
    }

    private String setSingleNewsletterMailsAdditionalJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/newsletterMails/SingleNewsletterMail.js";
    }

    private List<Breadcrumb> getSingleNewsletterMailsBreadcrumbs(Locale locale, Long id) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new Breadcrumb(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/newsletterMails").build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailsManagement")));
        breadcrumbs.add(new Breadcrumb(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/newsletterMails/" + id).build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CurrentNewsletter")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/newsletterMailsList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listNewsletterMails(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho,
        @RequestParam(required = false) Long searchRecipientGroupId,
        @RequestParam(required = false) String searchName
    ) {
        List<NewsletterMail> dbList = newsletterMailDAO.searchByAjax(iDisplayStart, iDisplayLength, searchName, searchRecipientGroupId);
        Integer dbListDisplayRecordsSize = newsletterMailDAO.searchByAjaxCount(searchName, searchRecipientGroupId);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < dbList.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            final NewsletterMail objects = dbList.get(i);
            formDetailsJson.put("recipientGroupName", newsletterMailGroupDAO.get(objects.getRecipientGroupId()).getName());
            formDetailsJson.put("newsletterMailName", objects.getName());
            formDetailsJson.put("newsletterMailSubject", objects.getSubject());
            formDetailsJson.put("newsletterMailStatus", objects.getStatus());
            formDetailsJson.put("id", objects.getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        responseDetailsJson.put("iTotalRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("iTotalDisplayRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addNewsletterMail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addNewsletterMail(
        @ModelAttribute(value = "newsletterMail") NewsletterMail newsletterMail,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "recipientGroupId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("RecipientGroupMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "name", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailNameMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "subject", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("SubjectMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsletterMail.setMailContent("");
            newsletterMailDAO.add(newsletterMail);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteNewsletterMail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteNewsletterMail(
        @ModelAttribute(value = "newsletterMail") NewsletterMail newsletterMail,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsletterMailDAO.delete(newsletterMail);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/unDeleteNewsletterMail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String unDeleteNewsletterMail(
        @ModelAttribute(value = "newsletterMail") NewsletterMail newsletterMail,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsletterMailDAO.undelete(newsletterMail);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/newsletterMails/{id}", method = RequestMethod.GET)
    public String browseNewsletterMail(ModelMap model, Locale locale, @PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", setSingleNewsletterMailsAdditionalJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getSingleNewsletterMailsBreadcrumbs(locale, id));
        NewsletterMail newsletterMail = newsletterMailDAO.get(id);
        model.addAttribute("newsletterMail", newsletterMail);
        NewsletterMailGroup newsletterMailGroup = newsletterMailGroupDAO.get(newsletterMail.getRecipientGroupId());
        model.addAttribute("newsletterMailGroupName", newsletterMailGroup.getName());
        return "cms/newsletterMails/SingleNewsletterMail";
    }

    @RequestMapping(value = "/newsletterMails/updateNewsletterMail", method = RequestMethod.POST)
    public String updateNewsletterMail(
        @ModelAttribute(value = "newsletterMail") NewsletterMail newsletterMail,
        BindingResult result, ModelMap model, Locale locale, HttpServletRequest httpServletRequest
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "recipientGroupId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("RecipientGroupMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "name", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailNameMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "subject", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("SubjectMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "mailContent", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("ContentMustBeSet"));
        if (!result.hasErrors()) {
            newsletterMail.setMailContent(newsletterMail.getMailContent().trim());
            newsletterMailDAO.update(newsletterMail);
            model.addAttribute("updateSuccessfull", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("Saved"));
        } else {
            StringBuilder errors = new StringBuilder();
            for (int i = 0; i < result.getAllErrors().size(); i++) {
                errors.append(result.getAllErrors().get(i).getCode()).append("<br/>");
            }
            model.addAttribute("updateErrors", errors);
        }
        return browseNewsletterMail(model, locale, newsletterMail.getId(), httpServletRequest);
    }

    @RequestMapping(value = "/newsletterMails/newsletterSend", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String newsletterSend(
        @ModelAttribute(value = "newsletterMail") NewsletterMail newsletterMail,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailMustBeSet"));
        newsletterMail = newsletterMailDAO.get(newsletterMail.getId());
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            sendNewsletterEmail(newsletterMail);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/newsletterMails/newsletterTestSend", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String newsletterTestSend(
        @ModelAttribute(value = "newsletterMail") NewsletterMail newsletterMail,
        @ModelAttribute(value = "newsletterMailAddress") NewsletterMailAddress newsletterMailAddress,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "email", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("EmailIsInvalid"));
        if (!EmailValidator.isValidEmailAddress(newsletterMailAddress.getEmail())) {
            result.rejectValue("email", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("EmailIsInvalid"));
        }
        NewsletterMail newsletterMailFromDb = newsletterMailDAO.get(newsletterMail.getId());
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            sendTestEmail(newsletterMailFromDb, newsletterMailAddress);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    private void sendNewsletterEmail(NewsletterMail newsletterMail) {
        //todo move sending emails to JMS
        MimeMessage mimeMessage = cmsMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessage.setContent(newsletterMail.getMailContent(), "text/html");
            helper.setSubject(newsletterMail.getSubject());
            helper.setReplyTo("info@cwsfe.pl");
            List<NewsletterMailAddress> newsletterMailAddresses = newsletterMailAddressDAO.listByRecipientGroup(newsletterMail.getRecipientGroupId());
            for (NewsletterMailAddress newsletterMailAddress : newsletterMailAddresses) {
                helper.setTo(newsletterMailAddress.getEmail());
                cmsMailSender.send(mimeMessage);
            }
        } catch (MessagingException e) {
            LOGGER.error("Message sending problem", e);
        }
    }

    private void sendTestEmail(NewsletterMail newsletterMail, NewsletterMailAddress newsletterMailAddress) {
        MimeMessage mimeMessage = cmsMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            helper.setTo(newsletterMailAddress.getEmail());
            mimeMessage.setContent(newsletterMail.getMailContent(), "text/html");
            helper.setSubject(newsletterMail.getSubject());
            helper.setReplyTo("info@cwsfe.pl");
        } catch (MessagingException e) {
            LOGGER.error("Problem with sending message", e);
        }
        cmsMailSender.send(mimeMessage);
    }

}
