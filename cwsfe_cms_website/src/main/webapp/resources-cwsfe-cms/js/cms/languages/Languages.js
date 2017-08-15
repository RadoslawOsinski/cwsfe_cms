require(['jquery', 'knockout', 'formAlerts', 'cmsLayout', 'dataTable'], function ($, ko, formAlertsModule) {

    function LanguagesViewModel() {
        var self = this;
        self.code = ko.observable();
        self.name = ko.observable();

        self.languageIsRequiredStyle = ko.computed(function () {
            return self.code() === null || self.code() === '' ? 'error' : 'invisible';
        });
        self.languageNameIsRequiredStyle = ko.computed(function () {
            return self.name() === null || self.name() === '' ? 'error' : 'invisible';
        });
        self.addLanguagesFormIsValid = ko.computed(function () {
            return self.code() !== null && self.code() !== '' &&
                self.name() !== null && self.name() !== '';
        });
    }

    var viewModel = {
        languagesViewModel: new LanguagesViewModel(),
        formAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);

        $('#LanguagesList').dataTable({
            'iTabIndex': -1,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'languagesList',
            aoColumns: [
                {'bSortable': false, mData: 'orderNumber'},
                {'bSortable': false, mData: 'code'},
                {'bSortable': false, mData: 'name'},
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '<button class="button red tiny" name="removeLanguageButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>';
                    }
                }
            ]
        });

    });

    $('#resetAddLanguages').click(function () {
        viewModel.languagesViewModel.code(null);
        viewModel.languagesViewModel.name(null);
        viewModel.formAlerts.cleanAllMessages();
    });

    function addLanguage() {
        viewModel.formAlerts.cleanAllMessages();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addLanguage',
            data: "code=" + viewModel.languagesViewModel.code() + "&name=" + viewModel.languagesViewModel.name(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#LanguagesList").dataTable().fnDraw();
                    viewModel.languagesViewModel.code(null);
                    viewModel.languagesViewModel.name(null);
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

    function removeLanguage(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteLanguage',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#LanguagesList").dataTable().fnDraw();
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

    $('#addLanguageButton').click(function () {
        addLanguage();
    });

    $('body').on('click', 'button[name="removeLanguageButton"]', function () {
        removeLanguage($(this).val());
    });

});
