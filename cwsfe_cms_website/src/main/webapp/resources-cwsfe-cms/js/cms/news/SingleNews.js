require(['jquery', 'jqueryUi', 'cmsLayout', 'dataTable', 'foundationTabs'], function ($) {

    $(document).ready(function () {

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
                        return '<img src="../newsImages/?imageId=' + o.aData.image + '" height="200" width="480"/>';
                    }
                },
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '' +
                            '<form method="GET" action="#">' +
                            '<button class="button red tiny" onclick="removeNewsImage(' + o.aData.id + ');return false;" tabindex="-1">Delete</button>' +
                            '</form>'
                            ;
                    }
                }
            ]
        });

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
