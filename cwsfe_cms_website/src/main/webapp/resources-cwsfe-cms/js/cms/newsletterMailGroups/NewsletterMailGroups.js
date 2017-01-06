require(['jquery', 'knockout', 'formAlerts', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($, ko, formAlertsModule) {

    function NewsletterMailGroupViewModel() {
        var self = this;
        self.languageId = null;
        self.language = ko.observable();
        self.newsletterMailGroupName = ko.observable();

        self.languageIsRequiredStyle = ko.computed(function () {
            return self.language() === null || self.language() === '' ? 'error' : 'invisible';
        });
        self.newsletterMailGroupNameIsRequiredStyle = ko.computed(function () {
            return self.newsletterMailGroupName() === null || self.newsletterMailGroupName() === '' ? 'error' : 'invisible';
        });
        self.addNewsletterMailGroupFormIsValid = ko.computed(function () {
            return self.language() !== null && self.language() !== '' &&
                self.newsletterMailGroupName() !== null && self.newsletterMailGroupName() !== '';
        });
    }

    var viewModel = {
        newsletterMailGroupViewModel: new NewsletterMailGroupViewModel(),
        formAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);

        $('#newsletterMailGroupsList').dataTable({
            'iTabIndex': -1,
            'bAutoWidth': true,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'newsletterMailGroupsList',
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
                {'bSortable': false, mData: 'newsletterMailGroupName'},
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '' +
                            '<form method="GET" action="newsletterMailGroups/' + o.aData.id + '">' +
                            '<button class="button green tiny" type="submit" tabindex="-1">Select</button>' +
                            '<button class="button red tiny" type="button" name="removeNewsletterMailGroupButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>' +
                            '</form>'
                            ;
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
                viewModel.newsletterMailGroupViewModel.language(ui.item.value);
                viewModel.newsletterMailGroupViewModel.languageId = ui.item.id;
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('.ui-autocomplete').addClass('f-dropdown');
    });

    function searchNewsletterMailGroup() {
        $("#newsletterMailGroupsList").dataTable().fnDraw();
    }

    $('#addNewsletterMailGroupButton').click(function () {
        addNewsletterMailGroup();
    });

    $('body').on('click', 'button[name="removeNewsletterMailGroupButton"]', function () {
        removeNewsletterMailGroup($(this).val());
    });

    $('#resetAddNewsletterMailGroup').click(function () {
        viewModel.newsletterMailGroupViewModel.languageId = null;
        viewModel.newsletterMailGroupViewModel.language(null);
        viewModel.newsletterMailGroupViewModel.newsletterMailGroupName(null);
        viewModel.formAlerts.cleanAllMessages();
    });

    function addNewsletterMailGroup() {
        viewModel.formAlerts.cleanAllMessages();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addNewsletterMailGroup',
            data: "name=" + viewModel.newsletterMailGroupViewModel.newsletterMailGroupName() + "&languageId=" + viewModel.newsletterMailGroupViewModel.languageId,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsletterMailGroupsList").dataTable().fnDraw();
                    viewModel.newsletterMailGroupViewModel.languageId = null;
                    viewModel.newsletterMailGroupViewModel.language(null);
                    viewModel.newsletterMailGroupViewModel.newsletterMailGroupName(null);
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

    function removeNewsletterMailGroup(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteNewsletterMailGroup',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsletterMailGroupsList").dataTable().fnDraw();
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
