require(['jquery', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($) {

    $(document).ready(function () {

        $("#tabs").tabs();

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

        $('#blogPostImagesList').dataTable({
            'iTabIndex': -1,
            'bAutoWidth': true,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'blogPostImagesList',
            'fnServerParams': function (aoData) {
                aoData.push(
                    {'name': "blogPostId", 'value': $('#blogPostId').val()}
                );
            },
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'title'},
                {
                    'bSortable': false, mData: 'image',
                    "fnRender": function (o) {
                        return '<img src="../blogPostImages/?imageId=' + o.aData.image + '" height="200" width="480"/>';
                    }
                },
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '' +
                            '<form method="GET" action="#">' +
                            '<button class="button red tiny" onclick="removeBlogPostImage(' + o.aData.id + ');return false;" tabindex="-1">Delete</button>' +
                            '</form>'
                            ;
                    }
                }
            ]
        });

        $('#blogPostCodesList').dataTable({
            'iTabIndex': -1,
            'bAutoWidth': true,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'blogPostCodesList',
            'fnServerParams': function (aoData) {
                aoData.push(
                    {'name': "blogPostId", 'value': $('#blogPostId').val()}
                );
            },
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'codeId'},
                {'bSortable': false, mData: 'code'},
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '' +
                            '<form method="GET" action="#">' +
                            '<button class="button red tiny" onclick="removeBlogPostCode(' + $('#blogPostId').val() + ',\'' + o.aData.id + '\');return false;" tabindex="-1">Delete</button>' +
                            '</form>'
                            ;
                    }
                }
            ]
        });

    });

    function saveBlogPost() {
        var id = $('#blogPostId').val();
        var postTextCode = $('#postTextCode').val();
        var status = $('#status').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'updatePostBasicInfo',
            data: "postTextCode=" + postTextCode + "&status=" + status + "&id=" + id,
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

    function removeBlogPostImage(id) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteBlogPostImage',
            data: 'id=' + id,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $('#blogPostImagesList').dataTable().fnDraw();
                } else {
                    var errorInfo = "";
                    for (var i = 0; i < response.result.length; i++) {
                        errorInfo += "<br>" + (i + 1) + ". " + response.result[i].error;
                    }
                    $('#blogPostImagesListTableValidation').html("<p>Please correct following errors: " + errorInfo + "</p>").show('slow');
                }
            },
            error: function (response) {
                console.log('BUG: ' + response);
            }
        });
    }

    function addBlogPostCode() {
        var blogPostId = $('#blogPostId').val();
        var codeId = $('#codeId').val();
        var code = $('#code').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addBlogPostCode',
            data: "blogPostId=" + blogPostId + "&codeId=" + codeId + "&code=" + code,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#blogPostCodesList").dataTable().fnDraw();
                    $("#addBlogPostCodeForm").trigger('reset');
                } else {
                    var errorInfo = "";
                    for (var i = 0; i < response.result.length; i++) {
                        errorInfo += "<br>" + (i + 1) + ". " + response.result[i].error;
                    }
                    $('#addBlogPostCodeFormValidation').html("<p>Please correct following errors: " + errorInfo + "</p>").show('slow');
                }
            },
            error: function (response) {
                console.log('BUG: ' + response);
            }
        });
    }

    function removeBlogPostCode(blogPostId, codeId) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteBlogPostCode',
            data: 'blogPostId=' + blogPostId + '&codeId=' + codeId,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $('#blogPostCodesList').dataTable().fnDraw();
                } else {
                    var errorInfo = "";
                    for (var i = 0; i < response.result.length; i++) {
                        errorInfo += "<br>" + (i + 1) + ". " + response.result[i].error;
                    }
                    $('#blogPostCodesTableValidation').html("<p>Please correct following errors: " + errorInfo + "</p>").show('slow');
                }
            },
            error: function (response) {
                console.log('BUG: ' + response);
            }
        });
    }

});
