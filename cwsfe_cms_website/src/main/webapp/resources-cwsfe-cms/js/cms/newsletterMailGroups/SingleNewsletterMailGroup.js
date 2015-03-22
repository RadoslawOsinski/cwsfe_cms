require(['jquery', 'knockout', 'formAlerts', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($, ko, formAlertsModule) {

    function SingleNewsletterMailGroupViewModel() {
        var self = this;
        self.newsletterMailGroupName = ko.observable($('#newsletterMailGroupName').val());
        self.languageId = $('#languageId').val();
        self.language = ko.observable($('#language').val());
        self.newsletterMailAddress = ko.observable($('#newsletterMailAddress').val());

        self.newsletterMailGroupNameIsRequiredStyle= ko.computed(function() {
            return self.newsletterMailGroupName() == null || self.newsletterMailGroupName() === '' ? 'error' : 'invisible';
        });
        self.languageIsRequiredStyle= ko.computed(function() {
            return self.language() == null || self.language() === '' ? 'error' : 'invisible';
        });
        self.newsletterMailAddressIsRequiredStyle= ko.computed(function() {
            return self.newsletterMailAddress() == null || self.newsletterMailAddress() === '' ? 'error' : 'invisible';
        });
        self.saveNewsletterMailGroupFormIsValid = ko.computed(function() {
            return self.newsletterMailGroupName() != null && self.newsletterMailGroupName() !== '' &&
                self.language() != null && self.language() !== '';
        });
        self.addNewsletterMailAddressFormIsValid = ko.computed(function() {
            return self.newsletterMailAddress() != null && self.newsletterMailAddress() !== '';
        });
    }

    var viewModel = {
        singleNewsletterMailGroupViewModel: new SingleNewsletterMailGroupViewModel(),
        editMailGroupAlerts: new formAlertsModule.formAlerts(),
        newMailAddressAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);
        
        $('#newsletterMailAddressesList').dataTable({
            'iTabIndex': -1,
            'bAutoWidth': true,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'newsletterMailAddressesList',
            'fnServerParams': function (aoData) {
                aoData.push({'name': "mailGroupId", 'value': $('#mailGroupId').val()});
                aoData.push({'name': "searchMail", 'value': $('#searchMail').val()});
            },
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'email'},
                {
                    'bSortable': false, mData: 'status',
                    'fnRender': function (o) {
                        if (o.aData.status === 'ACTIVE') {
                            return '<span class="highlight green">Active</span>';
                        } else if (o.aData.status === 'INACTIVE') {
                            return '<span class="highlight yellow">Inactive</span>';
                        } else if (o.aData.status === 'ERROR') {
                            return '<span class="highlight red">Error</span>';
                        } else if (o.aData.status === 'DELETED') {
                            return '<span class="highlight red">Deleted</span>';
                        }
                        return '<span class="highlight red">?</span>';
                    }
                },
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '' +
                            '<button class="button green tiny" name="activateNewsletterMailAddressButton" value="' + o.aData.id + '" tabindex="-1">Activate</button>' +
                            '<button class="button blue tiny" name="deactivateNewsletterMailAddressButton" value="' + o.aData.id + '" tabindex="-1">Deactivate</button>' +
                            '<button class="button red tiny" name="removeNewsletterMailAddressButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>'
                            ;
                    }
                }
            ]
        });

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
                $('#languageId').val(ui.item.id);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('.ui-autocomplete').addClass('f-dropdown');
    });

    function searchMailInNewsletterMailGroup() {
        $("#newsletterMailAddressesList").dataTable().fnDraw();
    }

    $('#searchMailInNewsletterMailGroupButton').click(function() {
        searchMailInNewsletterMailGroup();
    });

    $('#saveNewsletterMailGroupButton').click(function() {
        updateNewsletterMailGroup();
    });

    $('#addNewsletterMailAddressButton').click(function() {
        addNewsletterMailAddress();
    });

    var $body = $('body');
    $body.on('click', 'button[name="activateNewsletterMailAddressButton"]', function() {
        activateNewsletterMailAddress($(this).val());
    });

    $body.on('click', 'button[name="deactivateNewsletterMailAddressButton"]', function() {
        deactivateNewsletterMailAddress($(this).val());
    });

    $body.on('click', 'button[name="removeNewsletterMailAddressButton"]', function() {
        removeNewsletterMailAddress($(this).val());
    });

    $('#resetSaveNewsletterMailGroupFormIsValid').click(function() {
        viewModel.singleNewsletterMailGroupViewModel.newsletterMailGroupName($('#newsletterMailGroupName').val());
        viewModel.singleNewsletterMailGroupViewModel.languageId = $('#languageId').val();
        viewModel.singleNewsletterMailGroupViewModel.language($('#language').val());
        viewModel.editMailGroupAlerts.cleanAllMessages();
    });

     $('#resetAddNewsletterMailAddress').click(function() {
        viewModel.singleNewsletterMailGroupViewModel.newsletterMailAddress(null);
        viewModel.newMailAddressAlerts.cleanAllMessages();
    });

    function updateNewsletterMailGroup() {
        viewModel.editMailGroupAlerts.cleanAllMessages();
        var mailGroupId = $('#mailGroupId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'updateNewsletterMailGroup',
            data: "id=" + mailGroupId + "&name=" + viewModel.singleNewsletterMailGroupViewModel.newsletterMailGroupName() + "&languageId=" + viewModel.singleNewsletterMailGroupViewModel.languageId,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    viewModel.singleNewsletterMailGroupViewModel.newsletterMailGroupName($('#newsletterMailGroupName').val());
                    viewModel.singleNewsletterMailGroupViewModel.languageId = $('#languageId').val();
                    viewModel.singleNewsletterMailGroupViewModel.language($('#language').val());
                    viewModel.editMailGroupAlerts.cleanAllMessages();
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        viewModel.editMailGroupAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.editMailGroupAlerts.addMessage(response, 'error');
            }
        });
    }

    function addNewsletterMailAddress() {
        viewModel.newMailAddressAlerts.cleanAllMessages();
        var mailGroupId = $('#mailGroupId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addNewsletterMailAddresses',
            data: "mailGroupId=" + mailGroupId + "&email=" + viewModel.singleNewsletterMailGroupViewModel.newsletterMailAddress(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsletterMailAddressesList").dataTable().fnDraw();
                    viewModel.singleNewsletterMailGroupViewModel.newsletterMailAddress(null);
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        viewModel.newMailAddressAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.newMailAddressAlerts.addMessage(response, 'error');
            }
        });
    }

    function removeNewsletterMailAddress(id) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteNewsletterMailAddress',
            data: 'id=' + id,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $('#newsletterMailAddressesList').dataTable().fnDraw();
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        viewModel.editMailGroupAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.editMailGroupAlerts.addMessage(response, 'error');
            }
        });
    }

    function activateNewsletterMailAddress(id) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'activateNewsletterMailAddress',
            data: 'id=' + id,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $('#newsletterMailAddressesList').dataTable().fnDraw();
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        viewModel.editMailGroupAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.editMailGroupAlerts.addMessage(response, 'error');
            }
        });
    }

    function deactivateNewsletterMailAddress(id) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deactivateNewsletterMailAddress',
            data: 'id=' + id,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $('#newsletterMailAddressesList').dataTable().fnDraw();
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        viewModel.editMailGroupAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.editMailGroupAlerts.addMessage(response, 'error');
            }
        });
    }

});