require(['jquery', 'knockout', 'formAlerts', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($, ko, formAlertsModule) {

    function TranslationViewModel() {
        var self = this;
        self.searchLanguageId = null;
        self.searchLanguage = ko.observable();
        self.searchCategoryId = null;
        self.searchCategory = ko.observable();
        self.key = ko.observable();
        self.text = ko.observable();

        self.searchLanguageIsRequiredStyle = ko.computed(function () {
            return self.searchLanguage() === null || self.searchLanguage() === '' ? 'error' : 'invisible';
        });
        self.searchCategoryIsRequiredStyle = ko.computed(function () {
            return self.searchCategory() === null || self.searchCategory() === '' ? 'error' : 'invisible';
        });
        self.keyIsRequiredStyle = ko.computed(function () {
            return self.key() === null || self.key() === '' ? 'error' : 'invisible';
        });
        self.textIsRequiredStyle = ko.computed(function () {
            return self.text() === null || self.text() === '' ? 'error' : 'invisible';
        });
        self.addTranslationFormIsValid = ko.computed(function () {
            return self.searchLanguage() !== null && self.searchLanguage() !== '' &&
                self.searchCategory() !== null && self.searchCategory() !== '' &&
                self.key() !== null && self.key() !== '' &&
                self.text() !== null && self.text() !== '';
        });
    }

    var viewModel = {
        translationViewModel: new TranslationViewModel(),
        formAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);

        $('#cmsTextI18nList').dataTable({
            'iTabIndex': -1,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'cmsTextI18nList',
            aoColumns: [
                {'bSortable': false, mData: 'orderNumber'},
                {'bSortable': false, mData: 'language'},
                {'bSortable': false, mData: 'category'},
                {'bSortable': false, mData: 'key'},
                {'bSortable': false, mData: 'text'},
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '<button class="button red tiny" name="removeCmsTextI18nButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>';
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
                viewModel.translationViewModel.searchLanguage(ui.item.value);
                viewModel.translationViewModel.searchLanguageId = ui.item.id;
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('#searchCategory').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: 'cmsTextI18nCategoryDropList',
                    data: {
                        limit: 5,
                        term: request.term
                    },
                    success: function (data) {
                        response($.map(data.data, function (item) {
                            return {
                                value: item.category,
                                id: item.id
                            };
                        }));
                    }
                });
            },
            minLength: 0,
            select: function (event, ui) {
                viewModel.translationViewModel.searchCategory(ui.item.value);
                viewModel.translationViewModel.searchCategoryId = ui.item.id;
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('.ui-autocomplete').addClass('f-dropdown');
    });

    $('#addCmsTextI18nButton').click(function () {
        addCmsTextI18n();
    });

    $('body').on('click', 'button[name="removeCmsTextI18nButton"]', function () {
        removeCmsTextI18n($(this).val());
    });

    $('#resetAddTranslation').click(function () {
        viewModel.translationViewModel.searchLanguageId = null;
        viewModel.translationViewModel.searchLanguage(null);
        viewModel.translationViewModel.searchCategoryId = null;
        viewModel.translationViewModel.searchCategory(null);
        viewModel.translationViewModel.key(null);
        viewModel.translationViewModel.text(null);
        viewModel.formAlerts.cleanAllMessages();
    });

    function addCmsTextI18n() {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addCmsTextI18n',
            data: "langId=" + viewModel.translationViewModel.searchLanguageId + "&i18nCategory=" + viewModel.translationViewModel.searchCategoryId + "&i18NKey=" + viewModel.translationViewModel.key() + "&i18NText=" + viewModel.translationViewModel.text(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#cmsTextI18nList").dataTable().fnDraw();
                    viewModel.translationViewModel.searchLanguageId = null;
                    viewModel.translationViewModel.searchLanguage(null);
                    viewModel.translationViewModel.searchCategoryId = null;
                    viewModel.translationViewModel.searchCategory(null);
                    viewModel.translationViewModel.key(null);
                    viewModel.translationViewModel.text(null);
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

    function removeCmsTextI18n(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteCmsTextI18n',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#cmsTextI18nList").dataTable().fnDraw();
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
