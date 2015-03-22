require(['jquery', 'knockout', 'formAlerts', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($, ko, formAlertsModule) {

    function NewNewsletterTemplateViewModel() {
        var self = this;
        self.languageId = null;
        self.language = ko.observable();
        self.newsletterTemplateName = ko.observable();

        self.languageIsRequiredStyle= ko.computed(function() {
            return self.language() == null || self.language() === '' ? 'error' : 'invisible';
        });
        self.newsletterTemplateNameIsRequiredStyle= ko.computed(function() {
            return self.newsletterTemplateName() == null || self.newsletterTemplateName() === '' ? 'error' : 'invisible';
        });
        self.newNewsletterTemplateFormIsValid = ko.computed(function() {
            return self.language() != null && self.language() !== '' &&
                self.newsletterTemplateName() != null && self.newsletterTemplateName() !== '';
        });
    }

    var viewModel = {
        newNewsletterTemplateViewModel: new NewNewsletterTemplateViewModel(),
        formAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);

        $('#newsletterTemplatesList').dataTable({
            'iTabIndex': -1,
            'bAutoWidth': true,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'newsletterTemplatesList',
            'fnServerParams': function (aoData) {
                aoData.push(
                    {'name': "searchName", 'value': $('#searchName').val()}
                );
                aoData.push(
                    {'name': "searchLanguageId", 'value': $('#searchLanguageId').val()}
                );
            },
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'language2LetterCode'},
                {'bSortable': false, mData: 'newsletterTemplateName'},
                {'bSortable': false, mData: 'newsletterTemplateSubject'},
                {
                    'bSortable': false, mData: 'newsletterTemplateStatus',
                    'fnRender': function (o) {
                        if (o.aData.newsletterTemplateStatus === 'NEW') {
                            return '<span class="highlight green">Active</span>';
                        } else if (o.aData.newsletterTemplateStatus === 'DELETED') {
                            return '<span class="highlight red">Deleted</span>';
                        }
                        return '<span class="highlight red">?</span>';
                    }
                },
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '' +
                            '<form method="GET" action="newsletterTemplates/' + o.aData.id + '">' +
                            '<button class="button green tiny" type="submit" tabindex="-1">Select</button>' +
                            '<button class="button green tiny" type="button" name="unDeleteNewsletterTemplateButton" value="' + o.aData.id + '" tabindex="-1">Undelete</button>' +
                            '<button class="button red tiny" type="button" name="removeNewsletterTemplateButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>' +
                            '</form>';
                    }
                }
            ]
        });

        $('#searchLanguage').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: 'cmsLanguagesDropList',
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
                $('#searchLanguageId').val(ui.item.id);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('#language').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: 'cmsLanguagesDropList',
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
                viewModel.newNewsletterTemplateViewModel.languageId = ui.item.id;
                viewModel.newNewsletterTemplateViewModel.language(ui.item.value);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('.ui-autocomplete').addClass('f-dropdown');
    });

    function searchNewsletterTemplate() {
        $("#newsletterTemplatesList").dataTable().fnDraw();
    }

    $('#searchNewsletterTemplateButton').click(function() {
        searchNewsletterTemplate();
    });

    $('#addNewsletterTemplateButton').click(function() {
        addNewsletterTemplate();
    });

    var $body = $('body');
    $body.on('click', 'button[name="unDeleteNewsletterTemplateButton"]', function() {
        unDeleteNewsletterTemplate($(this).val());
    });
    $body.on('click', 'button[name="removeNewsletterTemplateButton"]', function() {
        removeNewsletterTemplate($(this).val());
    });

    $('#resetAddNewNewsletterTemplate').click(function() {
        viewModel.newNewsletterTemplateViewModel.languageId = null;
        viewModel.newNewsletterTemplateViewModel.language(null);
        viewModel.newNewsletterTemplateViewModel.newsletterTemplateName(null);
        viewModel.formAlerts.cleanAllMessages();
    });

    function addNewsletterTemplate() {
        var subject = $('#subject').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addNewsletterTemplate',
            data: "name=" + viewModel.newNewsletterTemplateViewModel.newsletterTemplateName() + "&languageId=" + viewModel.newNewsletterTemplateViewModel.languageId + "&subject=" + subject,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsletterTemplatesList").dataTable().fnDraw();
                    viewModel.newNewsletterTemplateViewModel.languageId = null;
                    viewModel.newNewsletterTemplateViewModel.language(null);
                    viewModel.newNewsletterTemplateViewModel.newsletterTemplateName(null);
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        viewModel.formAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.formAlerts.addMessage(response, 'error');
            }
        });
    }

    function removeNewsletterTemplate(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteNewsletterTemplate',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsletterTemplatesList").dataTable().fnDraw();
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        viewModel.formAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.formAlerts.addMessage(response, 'error');
            }
        });
    }

    function unDeleteNewsletterTemplate(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'unDeleteNewsletterTemplate',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsletterTemplatesList").dataTable().fnDraw();
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        viewModel.formAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.formAlerts.addMessage(response, 'error');
            }
        });
    }

});
