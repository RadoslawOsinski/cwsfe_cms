package eu.com.cwsfe.cms.web.newsletter;

import eu.com.cwsfe.cms.web.mvc.JsonController;
import eu.com.cwsfe.cms.dao.CmsLanguagesDAO;
import eu.com.cwsfe.cms.dao.NewsletterTemplateDAO;
import eu.com.cwsfe.cms.model.Breadcrumb;
import eu.com.cwsfe.cms.model.NewsletterMailAddress;
import eu.com.cwsfe.cms.model.NewsletterTemplate;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
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
class NewsletterTemplateController extends JsonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsletterTemplateController.class);

    private NewsletterTemplateDAO newsletterTemplateDAO;
    private CmsLanguagesDAO cmsLanguagesDAO;
    private JavaMailSender cmsMailSender;

    @Autowired
    public void setNewsletterTemplateDAO(NewsletterTemplateDAO newsletterTemplateDAO) {
        this.newsletterTemplateDAO = newsletterTemplateDAO;
    }

    @Autowired
    public void setCmsLanguagesDAO(CmsLanguagesDAO cmsLanguagesDAO) {
        this.cmsLanguagesDAO = cmsLanguagesDAO;
    }

    @Lazy
    @Autowired
    public void setCmsMailSender(JavaMailSender cmsMailSender) {
        this.cmsMailSender = cmsMailSender;
    }

    @RequestMapping(value = "/newsletterTemplates", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/newsletterTemplates/NewsletterTemplates";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/newsletterTemplates/NewsletterTemplates.js";
    }

    private List<Breadcrumb> getBreadcrumbs(Locale locale) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new Breadcrumb(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/newsletterTemplates").build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterTemplatesManagement")));
        return breadcrumbs;
    }

    private String setSingleNewsletterTemplatesAdditionalJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/newsletterTemplates/SingleNewsletterTemplate.js";
    }

    private List<Breadcrumb> getSingleNewsletterTemplatesBreadcrumbs(Locale locale, Long id) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new Breadcrumb(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/newsletterTemplates").build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterTemplatesManagement")));
        breadcrumbs.add(new Breadcrumb(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/newsletterTemplates/" + id).build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CurrentNewsletterTemplate")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/newsletterTemplatesList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listNewsletterTemplates(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho,
        @RequestParam(required = false) Long searchLanguageId,
        @RequestParam(required = false) String searchName
    ) {
        List<NewsletterTemplate> dbList = newsletterTemplateDAO.searchByAjax(iDisplayStart, iDisplayLength, searchName, searchLanguageId);
        Integer dbListDisplayRecordsSize = newsletterTemplateDAO.searchByAjaxCount(searchName, searchLanguageId);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < dbList.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            final NewsletterTemplate objects = dbList.get(i);
            formDetailsJson.put("language2LetterCode", cmsLanguagesDAO.getById(objects.getLanguageId()).getCode());
            formDetailsJson.put("newsletterTemplateName", objects.getName());
            formDetailsJson.put("newsletterTemplateSubject", objects.getSubject());
            formDetailsJson.put("newsletterTemplateStatus", objects.getStatus());
            formDetailsJson.put("id", objects.getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        responseDetailsJson.put("iTotalRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("iTotalDisplayRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addNewsletterTemplate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addNewsletterTemplate(
        @ModelAttribute(value = "newsletterTemplate") NewsletterTemplate newsletterTemplate,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "languageId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "name", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterTemplateNameMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsletterTemplateDAO.add(newsletterTemplate);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteNewsletterTemplate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteNewsletterTemplate(
        @ModelAttribute(value = "newsletterTemplate") NewsletterTemplate newsletterTemplate,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterTemplateMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsletterTemplateDAO.delete(newsletterTemplate);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/unDeleteNewsletterTemplate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String unDeleteNewsletterTemplate(
        @ModelAttribute(value = "newsletterTemplate") NewsletterTemplate newsletterTemplate,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterTemplateMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsletterTemplateDAO.undelete(newsletterTemplate);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/newsletterTemplates/{id}", method = RequestMethod.GET)
    public String browseNewsletterTemplate(ModelMap model, Locale locale, @PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", setSingleNewsletterTemplatesAdditionalJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getSingleNewsletterTemplatesBreadcrumbs(locale, id));
        NewsletterTemplate newsletterTemplate = newsletterTemplateDAO.get(id);
        model.addAttribute("newsletterTemplate", newsletterTemplate);
        model.addAttribute("newsletterTemplateLanguageCode", cmsLanguagesDAO.getById(newsletterTemplate.getLanguageId()).getCode());
        return "cms/newsletterTemplates/SingleNewsletterTemplate";
    }

    @RequestMapping(value = "/newsletterTemplates/updateNewsletterTemplate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateNewsletterTemplate(
        @ModelAttribute(value = "newsletterTemplate") NewsletterTemplate newsletterTemplate,
        BindingResult result, ModelMap model, Locale locale, HttpServletRequest httpServletRequest
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterTemplateMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "languageId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "name", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterTemplateNameMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "subject", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("SubjectMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "content", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("ContentMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsletterTemplate.setContent(newsletterTemplate.getContent().trim());
            newsletterTemplateDAO.update(newsletterTemplate);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/newsletterTemplates/newsletterTemplateTestSend", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String newsletterTemplateTestSend(
        @ModelAttribute(value = "newsletterTemplate") NewsletterTemplate newsletterTemplate,
        @ModelAttribute(value = "newsletterMailAddress") NewsletterMailAddress newsletterMailAddress,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterTemplateMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "email", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("EmailIsInvalid"));
        if (!EmailValidator.isValidEmailAddress(newsletterMailAddress.getEmail())) {
            result.rejectValue("email", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("EmailIsInvalid"));
        }
        NewsletterTemplate fullNewsletterTemplate = newsletterTemplateDAO.get(newsletterTemplate.getId());
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            sendTestTemplateEmail(fullNewsletterTemplate, newsletterMailAddress);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    private void sendTestTemplateEmail(NewsletterTemplate newsletterTemplate, NewsletterMailAddress newsletterMailAddress) {
        MimeMessage mimeMessage = cmsMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            helper.setTo(newsletterMailAddress.getEmail());
            mimeMessage.setContent(newsletterTemplate.getContent(), "text/html");
            helper.setSubject(newsletterTemplate.getSubject());
            helper.setReplyTo("info@cwsfe.pl");
        } catch (MessagingException e) {
            LOGGER.error("Problem with sending message", e);
        }
        cmsMailSender.send(mimeMessage);
    }

}
