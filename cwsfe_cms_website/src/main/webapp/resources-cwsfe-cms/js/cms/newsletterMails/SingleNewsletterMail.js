require(['jquery', 'knockout', 'formAlerts', 'jqueryUi', 'cmsLayout', 'foundationReveal'], function ($, ko, formAlertsModule) {

    function SingleNewsletterMailViewModel() {
        var self = this;
        self.recipientGroupId = $('#recipientGroupId').val();
        self.newsletterMailGroupName = ko.observable($('#recipientGroup').val());
        self.name = ko.observable($('#newsletterName').val());
        self.subject = ko.observable($('#newsletterSubject').val());
        self.testEmail = ko.observable();

        self.newsletterMailGroupNameIsRequiredStyle= ko.computed(function() {
            return self.newsletterMailGroupName() === null || self.newsletterMailGroupName() === '' ? 'error' : 'invisible';
        });
        self.nameIsRequiredStyle= ko.computed(function() {
            return self.name() === null || self.name() === '' ? 'error' : 'invisible';
        });
        self.subjectIsRequiredStyle= ko.computed(function() {
            return self.subject() === null || self.subject() === '' ? 'error' : 'invisible';
        });
         self.testEmailIsRequiredStyle= ko.computed(function() {
            return self.testEmail() === null || self.testEmail() === '' ? 'error' : 'invisible';
        });
        self.updateNewsletterMailFormIsValid = ko.computed(function() {
            return self.newsletterMailGroupName() !== null && self.newsletterMailGroupName() !== '' &&
                self.name() !== null && self.name() !== '' &&
                self.subject() !== null && self.subject() !== '';
        });
        self.newsletterTestSendFormIsValid = ko.computed(function() {
            return self.testEmail() !== null && self.testEmail() !== '';
        });
    }

    var viewModel = {
        singleNewsletterMailViewModel: new SingleNewsletterMailViewModel(),
        updateNewsletterMailAlerts: new formAlertsModule.formAlerts(),
        newsletterTestSendAlerts: new formAlertsModule.formAlerts()
    };

    $('#resetUpdateNewsletterMail').click(function() {
        viewModel.singleNewsletterMailViewModel.recipientGroupId = $('#recipientGroupId').val();
        viewModel.singleNewsletterMailViewModel.newsletterMailGroupName($('#recipientGroup').val());
        viewModel.singleNewsletterMailViewModel.name($('#newsletterName').val());
        viewModel.singleNewsletterMailViewModel.subject($('#newsletterSubject').val());
        viewModel.updateNewsletterMailAlerts.cleanAllMessages();
    });

    $('#resetNewsletterTestSend').click(function() {
        viewModel.singleNewsletterMailViewModel.testEmail(null);
        viewModel.newsletterTestSendAlerts.cleanAllMessages();
    });

    function newsletterTestSend() {
        viewModel.newsletterTestSendAlerts.cleanAllMessages();
        var newsletterMailId = $('#newsletterMailId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'newsletterTestSend',
            data: "id=" + newsletterMailId + "&email=" + viewModel.singleNewsletterMailViewModel.testEmail(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    viewModel.singleNewsletterMailViewModel.testEmail(null);
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        viewModel.newsletterTestSendAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.newsletterTestSendAlerts.addMessage(response, 'error');
            }
        });
    }

    function confirmNewsletterSend() {
        $('#confirmSendNewsletterModal').foundation('reveal','open');
    }

    function newsletterSend() {
        viewModel.updateNewsletterMailAlerts.cleanAllMessages();
        var newsletterMailId = $('#newsletterMailId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'newsletterSend',
            data: "id=" + newsletterMailId,
            async: false,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        viewModel.updateNewsletterMailAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.updateNewsletterMailAlerts.addMessage(response, 'error');
            }
        });
    }

    $(document).ready(function () {
        ko.applyBindings(viewModel);

        $('#recipientGroup').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: '../newsletterMailGroupsDropList',
                    data: {
                        limit: 5,
                        term: request.term
                    },
                    success: function (data) {
                        response($.map(data.data, function (item) {
                            return {
                                value: item.newsletterMailGroupName,
                                id: item.id
                            };
                        }));
                    }
                });
            },
            minLength: 0,
            select: function (event, ui) {
                $('#recipientGroupId').val(ui.item.id);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('#confirmSendButton').click(function() {
            confirmNewsletterSend();
        });

        $('#newsletterTestSendButton').click(function() {
            newsletterTestSend();
        });

        $('#confirmSendNewsletterButton').click(function() {
            newsletterSend();
            $('#confirmSendNewsletterModal').foundation('reveal', 'close');
        });

        $('#cancelSendNewsletterButton').click(function() {
            $('#confirmSendNewsletterModal').foundation('reveal', 'close');
        });

        $('.ui-autocomplete').addClass('f-dropdown');
    });

});
