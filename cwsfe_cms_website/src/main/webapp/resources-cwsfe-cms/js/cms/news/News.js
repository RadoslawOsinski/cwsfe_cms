require(['jquery', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($) {

    $(document).ready(function () {

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
                $('#authorId').val(ui.item.id);
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
                $('#newsTypeId').val(ui.item.id);
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
                $('#newsFolderId').val(ui.item.id);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('.ui-autocomplete').addClass('f-dropdown');
    });

    function searchNews() {
        $("#newsList").dataTable().fnDraw();
    }

    $('#addNewsButton').click(function() {
        addNews();
    });

    $('body').on('click', 'button[name="removeNewsButton"]', function() {
        removeNews($(this).val());
    });

    function addNews() {
        var authorId = $('#authorId').val();
        var newsTypeId = $('#newsTypeId').val();
        var newsFolderId = $('#newsFolderId').val();
        var newsCode = $('#newsCode').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addNews',
            data: "authorId=" + authorId + "&newsTypeId=" + newsTypeId + "&newsFolderId=" + newsFolderId + "&newsCode=" + newsCode,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsList").dataTable().fnDraw();
                    $("#addNewNewsForm").trigger('reset');
                } else {
                    var errorInfo = "";
                    for (var i = 0; i < response.result.length; i++) {
                        errorInfo += "<br>" + (i + 1) + ". " + response.result[i].error;
                    }
                    $('#formValidation').html("<p>Please correct following errors: " + errorInfo + "</p>").show('slow');
                }
            },
            error: function (response) {
                console.log('BUG: ' + response);
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
                    var errorInfo = "";
                    for (var i = 0; i < response.result.length; i++) {
                        errorInfo += "<br>" + (i + 1) + ". " + response.result[i].error;
                    }
                    $('#tableValidation').html("<p>Please correct following errors: " + errorInfo + "</p>").show('slow');
                }
            },
            error: function (response) {
                console.log('BUG: ' + response);
            }
        });
    }

});
