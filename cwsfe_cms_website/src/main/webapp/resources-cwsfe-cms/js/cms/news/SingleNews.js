require(['jquery', 'knockout', 'formAlerts', 'jqueryUi', 'cmsLayout', 'dataTable', 'foundation', 'foundationTabs'], function ($, ko, formAlertsModule) {

    function SingleNewsViewModel() {
        var self = this;
        self.languageId = ko.observable(null);
        self.newsTypeId = ko.observable($('#newsTypeId').val());
        self.newsType = ko.observable($('#newsType').val());
        self.newsFolderId = ko.observable($('#newsFolderId').val());
        self.newsFolder = ko.observable($('#newsFolder').val());
        self.newsCode = ko.observable($('#newsCode').val());
        self.newsTitle = ko.observable();
        self.newsShortcut = ko.observable();
        self.newsDescription = ko.observable();
        self.i18nStatus = ko.observable();
        self.imageTitle = ko.observable($('#title').val());
        self.basicInfoAlerts = new formAlertsModule.formAlerts();
        self.i18nContentAlerts = new formAlertsModule.formAlerts();
        self.imageAlerts = new formAlertsModule.formAlerts();

        self.newsTypeIsRequiredStyle = ko.computed(function () {
            return self.newsType() === null || self.newsType() === '' ? 'error' : 'invisible';
        });
        self.newsFolderIsRequiredStyle = ko.computed(function () {
            return self.newsFolder() === null || self.newsFolder() === '' ? 'error' : 'invisible';
        });
        self.newsCodeIsRequiredStyle = ko.computed(function () {
            return self.newsCode() === null || self.newsCode() === '' ? 'error' : 'invisible';
        });
        self.i18nLanguageIsRequiredStyle = ko.computed(function () {
            return self.languageId() === null ? 'error' : 'invisible';
        });
        self.newsTitleIsRequiredStyle = ko.computed(function () {
            if (self.languageId() === null) {
                return 'invisible';
            }
            return self.newsTitle() === null || self.newsTitle() === '' ? 'error' : 'invisible';
        });
        self.imageTitleIsRequiredStyle = ko.computed(function () {
            return self.imageTitle() === null || self.imageTitle() === '' ? 'error' : 'invisible';
        });

        self.saveBasicInfoFormIsValid = ko.computed(function () {
            return self.newsType() !== null && self.newsType() !== '' &&
                self.newsFolder() !== null && self.newsFolder() !== '' &&
                self.newsCode() !== null && self.newsCode() !== '';
        });
        self.saveNewsI18nFormIsValid = ko.computed(function () {
            return self.newsTitle() !== null && self.newsTitle() !== '';
        });

        self.addImageFormIsValid = ko.computed(function () {
            return self.imageTitle() !== null && self.imageTitle() !== '';
        });

        self.isNewsI18nVisible = ko.computed(function () {
            return self.languageId() !== null;
        });

        self.initializeSingleNewsViewModel = function () {
            self.languageId(null);
        };

        self.initializeI18nData = function () {
            $.ajax({
                url: $('#cmsNewsId').val() + '/' + self.languageId(),
                async: true,
                success: function (response) {
                    if (response !== null && 'SUCCESS' === response.status) {
                        self.newsTitle(response.data.newsTitle);
                        self.newsShortcut(response.data.newsShortcut);
                        self.newsDescription(response.data.newsDescription);
                        self.i18nStatus(response.data.status);
                    }
                }
            });
        };
    }

    var singleNewsViewModel = new SingleNewsViewModel();

    $(document).ready(function () {

        ko.applyBindings(singleNewsViewModel);

        $('#author').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: '/authors/authorsDropList',
                    data: {
                        limit: 5,
                        term: request.term
                    },
                    success: function (data) {
                        response($.map(data.data, function (item) {
                            return {
                                value: item.lastName + " " + item.firstName,
                                id: item.id
                            };
                        }));
                    }
                });
            },
            minLength: 0,
            select: function (event, ui) {
                $('#authorId').val(ui.item.id);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('#newsType').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: './newsTypesDropList',
                    data: {
                        limit: 5,
                        term: request.term
                    },
                    success: function (data) {
                        response($.map(data.data, function (item) {
                            return {
                                value: item.type,
                                id: item.id
                            };
                        }));
                    }
                });
            },
            minLength: 0,
            select: function (event, ui) {
                singleNewsViewModel.newsTypeId(ui.item.id);
                singleNewsViewModel.newsType(ui.item.value);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('#newsFolder').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: './foldersDropList',
                    data: {
                        limit: 5,
                        term: request.term
                    },
                    success: function (data) {
                        response($.map(data.data, function (item) {
                            return {
                                value: item.folderName,
                                id: item.id
                            };
                        }));
                    }
                });
            },
            minLength: 0,
            select: function (event, ui) {
                singleNewsViewModel.newsFolderId(ui.item.id);
                singleNewsViewModel.newsFolder(ui.item.value);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('#i18nLanguage').autocomplete({
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
                singleNewsViewModel.languageId(ui.item.id);
                singleNewsViewModel.initializeI18nData();
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('#cmsNewsImagesList').dataTable({
            'iTabIndex': -1,
            'bAutoWidth': true,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'cmsNewsImagesList',
            'fnServerParams': function (aoData) {
                aoData.push(
                    {'name': "cmsNewsId", 'value': $('#cmsNewsId').val()}
                );
            },
            aoColumns: [
                {'bSortable': false, mData: 'orderNumber'},
                {'bSortable': false, mData: 'title'},
                {
                    'bSortable': false, mData: 'image', 'fnRender': function (o) {
                    return '<img src="' + o.aData.url + '" height="200" width="480"/>';
                }
                },
                {
                    'bSortable': false, mData: 'id', 'fnRender': function (o) {
                    return '<button class="button red tiny" name="removeNewsImageButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>';
                }
                }
            ]
        });

        $('#saveNewsButton').click(function () {
            saveNews();
        });

        var $body = $('body');
        $body.on('click', '#revertNewsI18nButton', function () {
            singleNewsViewModel.i18nContentAlerts.cleanAllMessages();
            singleNewsViewModel.initializeSingleNewsViewModel();
        });

        $('#resetBasicInfoForm').click(function () {
            singleNewsViewModel.basicInfoAlerts.cleanAllMessages();
        });

        $body.on('click', 'button[name="removeNewsImageButton"]', function () {
            removeNewsImage($(this).val());
        });

        $('#updateNewsI18nButton').click(function () {
            updateNewsI18n();
        });

        $('.ui-autocomplete').addClass('f-dropdown');
    });

    $('#resetSaveBasicInfoButton').click(function () {
        singleNewsViewModel.newsTypeId($('#newsTypeId').val());
        singleNewsViewModel.newsType($('#newsType').val());
        singleNewsViewModel.newsFolderId($('#newsFolderId').val());
        singleNewsViewModel.newsFolder($('#newsFolder').val());
        singleNewsViewModel.newsCode($('#newsCode').val());
        singleNewsViewModel.basicInfoAlerts.cleanAllMessages();
    });

    $('#resetImagesFormButton').click(function () {
        singleNewsViewModel.imageTitle($('#title').val());
        singleNewsViewModel.imageAlerts.cleanAllMessages();
    });

    function saveNews() {
        singleNewsViewModel.basicInfoAlerts.cleanAllMessages();
        var status = $('#status').val();
        var id = $('#cmsNewsId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'updateNewsBasicInfo',
            data: "newsTypeId=" + singleNewsViewModel.newsTypeId() + "&newsFolderId=" + singleNewsViewModel.newsFolderId() +
            "&newsCode=" + singleNewsViewModel.newsCode() + "&status=" + status + "&id=" + id,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        singleNewsViewModel.basicInfoAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                singleNewsViewModel.basicInfoAlerts.addMessage(response, 'error');
            }
        });
    }

    function updateNewsI18n() {
        singleNewsViewModel.i18nContentAlerts.cleanAllMessages();
        var cmsNewsId = $('#cmsNewsId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'updateNewsI18nContent',
            data: "newsTitle=" + singleNewsViewModel.newsTitle() + "&newsShortcut=" + singleNewsViewModel.newsShortcut() +
            "&newsDescription=" + singleNewsViewModel.newsDescription() + "&status=" + singleNewsViewModel.i18nStatus() +
            "&languageId=" + singleNewsViewModel.languageId() + "&newsId=" + cmsNewsId,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        singleNewsViewModel.i18nContentAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                singleNewsViewModel.i18nContentAlerts.addMessage(response, 'error');
            }
        });
    }

    function removeNewsImage(id) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteCmsNewsImage',
            data: 'id=' + id,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $('#cmsNewsImagesList').dataTable().fnDraw();
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        singleNewsViewModel.imageAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                singleNewsViewModel.imageAlerts.addMessage(response, 'error');
            }
        });
    }

});
