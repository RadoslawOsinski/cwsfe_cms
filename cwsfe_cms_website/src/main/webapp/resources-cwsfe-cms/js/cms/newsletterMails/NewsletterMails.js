require(['jquery', 'knockout', 'formAlerts', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($, ko, formAlertsModule) {

    function NewsletterMailsViewModel() {
        var self = this;
        self.recipientGroupId = null;
        self.recipientGroup = ko.observable();
        self.newsletterMailName = ko.observable();
        self.newsletterMailSubject = ko.observable();

        self.recipientGroupIsRequiredStyle = ko.computed(function () {
            return self.recipientGroup() === null || self.recipientGroup() === '' ? 'error' : 'invisible';
        });
        self.newsletterMailNameIsRequiredStyle = ko.computed(function () {
            return self.newsletterMailName() === null || self.newsletterMailName() === '' ? 'error' : 'invisible';
        });
        self.newsletterMailSubjectIsRequiredStyle = ko.computed(function () {
            return self.newsletterMailSubject() === null || self.newsletterMailSubject() === '' ? 'error' : 'invisible';
        });
        self.addNewsletterMailFormIsValid = ko.computed(function () {
            return self.recipientGroup() !== null && self.recipientGroup() !== '' &&
                self.newsletterMailName() !== null && self.newsletterMailName() !== '' &&
                self.newsletterMailSubject() !== null && self.newsletterMailSubject() !== '';
        });
    }

    var viewModel = {
        newsletterMailsViewModel: new NewsletterMailsViewModel(),
        formAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);

        $('#newsletterMailsList').dataTable({
            'iTabIndex': -1,
            'bAutoWidth': true,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'newsletterMailsList',
            'fnServerParams': function (aoData) {
                aoData.push(
                    {'name': "searchName", 'value': $('#searchName').val()}
                );
                aoData.push(
                    {'name': "searchRecipientGroupId", 'value': $('#searchRecipientGroupId').val()}
                );
            },
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'recipientGroupName'},
                {'bSortable': false, mData: 'newsletterMailName'},
                {'bSortable': false, mData: 'newsletterMailSubject'},
                {
                    'bSortable': false, mData: 'newsletterMailStatus',
                    'fnRender': function (o) {
                        if (o.aData.newsletterMailStatus === 'NEW') {
                            return '<span class="highlight yellow">New</span>';
                        } else if (o.aData.newsletterMailStatus === 'PREPARING') {
                            return '<span class="highlight green">Preparing</span>';
                        } else if (o.aData.newsletterMailStatus === 'SEND') {
                            return '<span class="highlight blue">Sended</span>';
                        } else if (o.aData.newsletterMailStatus === 'DELETED') {
                            return '<span class="highlight red">Deleted</span>';
                        }
                        return '<span class="highlight red">?</span>';
                    }
                },
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '' +
                            '<form method="GET" action="newsletterMails/' + o.aData.id + '">' +
                            '<button class="button green tiny" type="submit" tabindex="-1">Select</button>' +
                            '<button class="button red tiny" type="button" name="removeNewsletterMailButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>' +
                            '</form>'
                            ;
                    }
                }
            ]
        });

        $('#searchNewsletterMailButton').click(function () {
            searchNewsletterMail();
        });

        $('#addNewsletterMailButton').click(function () {
            addNewsletterMail();
        });

        $('body').on('click', 'button[name="removeNewsletterMailButton"]', function () {
            removeNewsletterMail($(this).val());
        });

        $('#searchRecipientGroup').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: 'newsletterMailGroupsDropList',
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
                $('#searchRecipientGroupId').val(ui.item.id);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('#recipientGroup').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: 'newsletterMailGroupsDropList',
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
                viewModel.newsletterMailsViewModel.recipientGroup(ui.item.value);
                viewModel.newsletterMailsViewModel.recipientGroupId = ui.item.id;
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('.ui-autocomplete').addClass('f-dropdown');
    });

    function searchNewsletterMail() {
        $("#newsletterMailsList").dataTable().fnDraw();
    }

    $('#resetAddNewsletterMail').click(function () {
        viewModel.newsletterMailsViewModel.recipientGroupId = null;
        viewModel.newsletterMailsViewModel.recipientGroup(null);
        viewModel.newsletterMailsViewModel.newsletterMailName(null);
        viewModel.newsletterMailsViewModel.newsletterMailSubject(null);
        viewModel.formAlerts.cleanAllMessages();
    });

    function addNewsletterMail() {
        viewModel.formAlerts.cleanAllMessages();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addNewsletterMail',
            data: "name=" + viewModel.newsletterMailsViewModel.newsletterMailName() + "&recipientGroupId=" + viewModel.newsletterMailsViewModel.recipientGroupId + "&subject=" + viewModel.newsletterMailsViewModel.newsletterMailSubject(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsletterMailsList").dataTable().fnDraw();
                    viewModel.newsletterMailsViewModel.recipientGroup(null);
                    viewModel.newsletterMailsViewModel.newsletterMailName(null);
                    viewModel.newsletterMailsViewModel.newsletterMailSubject(null);
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

    function removeNewsletterMail(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteNewsletterMail',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsletterMailsList").dataTable().fnDraw();
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
