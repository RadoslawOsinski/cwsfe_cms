require(['jquery', 'knockout', 'formAlerts', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($, ko, formAlertsModule) {

    function NewsViewModel() {
        var self = this;
        self.authorId = null;
        self.author = ko.observable();
        self.newsTypeId = null;
        self.newsType = ko.observable();
        self.newsFolderId = null;
        self.newsFolder = ko.observable();
        self.newsCode = ko.observable();

        self.authorIsRequiredStyle = ko.computed(function () {
            return self.author() === null || self.author() === '' ? 'error' : 'invisible';
        });
        self.newsTypeIsRequiredStyle = ko.computed(function () {
            return self.newsType() === null || self.newsType() === '' ? 'error' : 'invisible';
        });
        self.newsFolderIsRequiredStyle = ko.computed(function () {
            return self.newsFolder() === null || self.newsFolder() === '' ? 'error' : 'invisible';
        });
        self.newsCodeIsRequiredStyle = ko.computed(function () {
            return self.newsCode() === null || self.newsCode() === '' ? 'error' : 'invisible';
        });
        self.addNewsFormIsValid = ko.computed(function () {
            return self.author() !== null && self.author() !== '' &&
                self.newsType() !== null && self.newsType() !== '' &&
                self.newsFolder() !== null && self.newsFolder() !== '' &&
                self.newsCode() !== null && self.newsCode() !== '';
        });
    }

    var viewModel = {
        newsViewModel: new NewsViewModel(),
        formAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);

        $('#newsList').dataTable({
            'iTabIndex': -1,
            'bAutoWidth': true,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'newsList',
            'fnServerParams': function (aoData) {
                aoData.push(
                    {'name': "searchAuthorId", 'value': $('#searchAuthorId').val()},
                    {'name': "searchNewsCode", 'value': $('#searchNewsCode').val()}
                );
            },
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'author'},
                {'bSortable': false, mData: 'newsCode'},
                {'bSortable': false, mData: 'creationDate'},
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '' +
                            '<form method="GET" action="news/' + o.aData.id + '">' +
                            '<button class="button green tiny" type="submit" tabindex="-1">Select</button>' +
                            '<button class="button red tiny" type="button" name="removeNewsButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>' +
                            '</form>'
                            ;
                    }
                }
            ]
        });

        $('#searchAuthor').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: 'authorsDropList',
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
                $('#searchAuthorId').val(ui.item.id);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('#author').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: 'authorsDropList',
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
                viewModel.newsViewModel.authorId = ui.item.id;
                viewModel.newsViewModel.author(ui.item.value);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('#newsType').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: 'news/newsTypesDropList',
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
                viewModel.newsViewModel.newsTypeId = ui.item.id;
                viewModel.newsViewModel.newsType(ui.item.value);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });


        $('#newsFolder').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: 'news/foldersDropList',
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
                viewModel.newsViewModel.newsFolderId = ui.item.id;
                viewModel.newsViewModel.newsFolder(ui.item.value);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('.ui-autocomplete').addClass('f-dropdown');
    });

    function searchNews() {
        $("#newsList").dataTable().fnDraw();
    }

    $('#addNewsButton').click(function () {
        addNews();
    });

    $('body').on('click', 'button[name="removeNewsButton"]', function () {
        removeNews($(this).val());
    });

    $('#resetAddBlogPost').click(function () {
        viewModel.newsViewModel.authorId = null;
        viewModel.newsViewModel.author(null);
        viewModel.newsViewModel.newsTypeId = null;
        viewModel.newsViewModel.newsType(null);
        viewModel.newsViewModel.newsFolderId = null;
        viewModel.newsViewModel.newsFolder(null);
        viewModel.newsViewModel.newsCode(null);
        viewModel.formAlerts.cleanAllMessages();
    });

    function addNews() {
        viewModel.formAlerts.cleanAllMessages();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addNews',
            data: "authorId=" + viewModel.newsViewModel.authorId + "&newsTypeId=" + viewModel.newsViewModel.newsTypeId +
            "&newsFolderId=" + viewModel.newsViewModel.newsFolderId + "&newsCode=" + viewModel.newsViewModel.newsCode(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsList").dataTable().fnDraw();
                    viewModel.newsViewModel.authorId = null;
                    viewModel.newsViewModel.author(null);
                    viewModel.newsViewModel.newsTypeId = null;
                    viewModel.newsViewModel.newsType(null);
                    viewModel.newsViewModel.newsFolderId = null;
                    viewModel.newsViewModel.newsFolder(null);
                    viewModel.newsViewModel.newsCode(null);
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

    function removeNews(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteNews',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsList").dataTable().fnDraw();
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
