require(['jquery', 'knockout', 'formAlerts', 'cmsLayout', 'dataTable'], function ($, ko, formAlertsModule) {

    function AuthorsViewModel() {
        var self = this;
        self.firstName = ko.observable();
        self.lastName = ko.observable();

        self.firstNameIsRequiredStyle= ko.computed(function() {
            return self.firstName() === null || self.firstName() === '' ? 'error' : 'invisible';
        });
        self.lastNameIsRequiredStyle= ko.computed(function() {
            return self.lastName() === null || self.lastName() === '' ? 'error' : 'invisible';
        });
        self.addAuthorFormIsValid = ko.computed(function() {
            return self.firstName() !== null && self.firstName() !== '' &&
                self.lastName() !== null && self.lastName() !== '';
        });
    }

    var viewModel = {
        authorsViewModel: new AuthorsViewModel(),
        formAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);

        $('#authorsList').dataTable({
            'iTabIndex': -1,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'authorsList',
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'lastName'},
                {'bSortable': false, mData: 'firstName'},
                {'bSortable': false, mData: 'googlePlusAuthorLink'},
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '<button class="button red tiny" name="removeAuthorButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>';
                    }
                }
            ]
        });

    });

    $('#addAuthorButton').click(function() {
        addAuthor();
    });

    $('body').on('click', 'button[name="removeAuthorButton"]', function() {
        removeAuthor($(this).val());
    });

    $('#resetAuthorButton').click(function() {
        viewModel.authorsViewModel.firstName(null);
        viewModel.authorsViewModel.lastName(null);
        viewModel.formAlerts.cleanAllMessages();
    });

    function addAuthor() {
        viewModel.formAlerts.cleanAllMessages();
        var googlePlusAuthorLink = $('#googlePlusAuthorLink').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addAuthor',
            data: "firstName=" + viewModel.authorsViewModel.firstName() + "&lastName=" + viewModel.authorsViewModel.lastName() + "&googlePlusAuthorLink=" + googlePlusAuthorLink,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#authorsList").dataTable().fnDraw();
                    viewModel.authorsViewModel.firstName('');
                    viewModel.authorsViewModel.lastName('');
                    $('#googlePlusAuthorLink').val('');
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

    function removeAuthor(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteAuthor',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#authorsList").dataTable().fnDraw();
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
