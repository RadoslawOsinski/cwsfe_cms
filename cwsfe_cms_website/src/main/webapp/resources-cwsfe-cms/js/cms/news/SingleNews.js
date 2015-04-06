require(['jquery', 'knockout', 'jqueryUi', 'cmsLayout', 'dataTable', 'foundation', 'foundationTabs'], function ($, ko) {

    function SingleNewsViewModel() {
        var self = this;
        self.languageId = ko.observable();
        self.i18nData = ko.observable();

        self.isNewsI18nVisible = ko.computed(function() {
            return self.languageId() !== null;
        });

        self.initializeSingleNewsViewModel = function() {
            self.languageId(null);
        };

        self.initializeI18nData = function () {
            $.ajax({
                url: $('#cmsNewsId').val() + '/' + self.languageId(),
                async: true,
                success: function (response) {
                    if (response !== null && 'SUCCESS' === response.status) {
                        self.i18nData({
                            newsTitle: response.data.newsTitle,
                            newsShortcut: response.data.newsShortcut,
                            newsDescription: response.data.newsDescription,
                            status: response.data.status
                        });
                    }
                }
            });
        };
    }

    $(document).ready(function () {

        var singleNewsViewModel = new SingleNewsViewModel();

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
                $('#newsTypeId').val(ui.item.id);
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
                $('#newsFolderId').val(ui.item.id);
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
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'title'},
                {
                    'bSortable': false, mData: 'image',
                    "fnRender": function (o) {
                        return '<img src="../newsImages/?imageId=' + o.aData.id + '" height="200" width="480"/>';
                    }
                },
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '<button class="button red tiny" name="removeNewsImageButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>';
                    }
                }
            ]
        });

        $('#saveNewsButton').click(function() {
            saveNews();
        });

        var $body = $('body');
        $body.on('click', '#revertNewsI18nButton', function() {
            singleNewsViewModel.initializeSingleNewsViewModel();
        });

        $body.on('click', 'button[name="removeNewsImageButton"]', function() {
            removeNewsImage($(this).val());
        });

        $('.ui-autocomplete').addClass('f-dropdown');
    });

    function saveNews() {
        var newsTypeId = $('#newsTypeId').val();
        var newsFolderId = $('#newsFolderId').val();
        var newsCode = $('#newsCode').val();
        var status = $('#status').val();
        var id = $('#cmsNewsId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'updateNewsBasicInfo',
            data: "newsTypeId=" + newsTypeId + "&newsFolderId=" + newsFolderId + "&newsCode=" + newsCode + "&status=" + status + "&id=" + id,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#basicInfoFormValidation").html("<p>Success</p>").show('slow');
                } else {
                    var errorInfo = "";
                    for (var i = 0; i < response.result.length; i++) {
                        errorInfo += "<br>" + (i + 1) + ". " + response.result[i].error;
                    }
                    $('#basicInfoFormValidation').html("<p>Please correct following errors: " + errorInfo + "</p>").show('slow');
                }
            },
            error: function (response) {
                console.log('BUG: ' + response);
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
                    var errorInfo = "";
                    for (var i = 0; i < response.result.length; i++) {
                        errorInfo += "<br>" + (i + 1) + ". " + response.result[i].error;
                    }
                    $('#cmsNewsImagesListTableValidation').html("<p>Please correct following errors: " + errorInfo + "</p>").show('slow');
                }
            },
            error: function (response) {
                console.log('BUG: ' + response);
            }
        });
    }

});
