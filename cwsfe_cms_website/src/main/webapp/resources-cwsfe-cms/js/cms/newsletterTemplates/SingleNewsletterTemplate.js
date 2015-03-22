require(['jquery', 'knockout', 'formAlerts', 'jqueryUi', 'cmsLayout'], function ($, ko, formAlertsModule) {

    function NewsletterTemplateViewModel() {
        var self = this;
        self.languageId = $('#languageId').val();
        self.language = ko.observable($('#language').val());
        self.newsletterTemplateName = ko.observable($('#newsletterTemplateName').val());
        self.newsletterTemplateSubject = ko.observable($('#newsletterTemplateSubject').val());
        self.testEmail = ko.observable();

        self.languageIsRequiredStyle = ko.computed(function() {
            return self.language() == null || self.language() === '' ? 'error' : 'invisible';
        });
        self.newsletterTemplateNameIsRequiredStyle = ko.computed(function() {
            return self.newsletterTemplateName() == null || self.newsletterTemplateName() === '' ? 'error' : 'invisible';
        });
        self.newsletterTemplateSubjectIsRequiredStyle = ko.computed(function() {
            return self.newsletterTemplateSubject() == null || self.newsletterTemplateSubject() === '' ? 'error' : 'invisible';
        });
        self.testEmailIsRequiredStyle = ko.computed(function() {
            return self.testEmail() == null || self.testEmail() === '' ? 'error' : 'invisible';
        });
        self.updateNewsletterTemplateFormIsValid = ko.computed(function() {
            return self.language() != null && self.language() !== '' &&
                self.newsletterTemplateName() != null && self.newsletterTemplateName() !== '' &&
                self.newsletterTemplateSubject() != null && self.newsletterTemplateSubject() !== '';
        });
        self.newsletterTemplateTestSendFormIsValid = ko.computed(function() {
            return self.testEmail() != null && self.testEmail() !== '';
        });
    }

    var viewModel = {
        newsletterTemplateViewModel: new NewsletterTemplateViewModel(),
        updateNewsletterTemplateAlerts: new formAlertsModule.formAlerts(),
        newsletterTemplateTestAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);

        $('#language').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: '../cmsLanguagesDropList',
                    data: {
                        limit: 5,
                        term: request.term
                    },
                    success: function (data) {
                        response($.map(data.data, function (item) {
                            return {
                                value: item.code + " - " + item.name,
                                id: item.id
                            };
                        }));
                    }
                });
            },
            minLength: 0,
            select: function (event, ui) {
                viewModel.newsletterTemplateViewModel.languageId = ui.item.id;
                viewModel.newsletterTemplateViewModel.language(ui.item.value);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('.ui-autocomplete').addClass('f-dropdown');
    });

    $('#newsletterTemplateTestSendButton').click(function() {
        newsletterTemplateTestSend();
    });

    $('#resetNewsletterTemplate').click(function() {
        viewModel.newsletterTemplateViewModel.languageId = $('#languageId').val();
        viewModel.newsletterTemplateViewModel.language($('#language').val());
        viewModel.newsletterTemplateViewModel.newsletterTemplateName($('#newsletterTemplateName').val());
        viewModel.newsletterTemplateViewModel.newsletterTemplateSubject($('#newsletterTemplateSubject').val());
        viewModel.updateNewsletterTemplateAlerts.cleanAllMessages();
    });

    $('#resetNewsletterTemplateTest').click(function() {
        viewModel.newsletterTemplateViewModel.testEmail(null);
        viewModel.newsletterTemplateTestAlerts.cleanAllMessages();
    });

    $('#updateNewsletterTemplate').click(function() {
        updateNewsletterTemplate();
    });

    function updateNewsletterTemplate() {
        viewModel.updateNewsletterTemplateAlerts.cleanAllMessages();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'updateNewsletterTemplate',
            data: "id=" + $('#newsletterTemplateId').val() +
                "&languageId=" + viewModel.newsletterTemplateViewModel.languageId +
                "&name=" + viewModel.newsletterTemplateViewModel.newsletterTemplateName() +
                "&subject=" + viewModel.newsletterTemplateViewModel.newsletterTemplateSubject() +
                "&content=" + $('#newsletterTemplateContent').val(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    viewModel.newsletterTemplateViewModel.languageId = $('#languageId').val();
                    viewModel.newsletterTemplateViewModel.language($('#language').val());
                    viewModel.newsletterTemplateViewModel.newsletterTemplateName($('#newsletterTemplateName').val());
                    viewModel.newsletterTemplateViewModel.newsletterTemplateSubject($('#newsletterTemplateSubject').val());
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        viewModel.updateNewsletterTemplateAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.updateNewsletterTemplateAlerts.addMessage(response, 'error');
            }
        });
    }

    function newsletterTemplateTestSend() {
        var newsletterTemplateId = $('#newsletterTemplateId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'newsletterTemplateTestSend',
            data: "id=" + newsletterTemplateId + "&email=" + viewModel.newsletterTemplateViewModel.testEmail(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    viewModel.newsletterTemplateViewModel.testEmail(null);
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        viewModel.newsletterTemplateTestAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.newsletterTemplateTestAlerts.addMessage(response, 'error');
            }
        });
    }

});
