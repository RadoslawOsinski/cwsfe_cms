require(['jquery', 'cmsLayout', 'dataTable'], function ($) {

    $(window).load(function () {

        $('#blogPostCommentsList').dataTable({
            'iTabIndex': -1,
            'bAutoWidth': true,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'blogPostCommentsList',
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'username'},
                {'bSortable': false, mData: 'comment'},
                {'bSortable': false, mData: 'created'},
                {
                    'bSortable': false, mData: 'status',
                    'fnRender': function (o) {
                        if (o.aData.status === 'NEW') {
                            return '<span class="highlight yellow">New</span>';
                        } else if (o.aData.status === 'PUBLISHED') {
                            return '<span class="highlight green">Published</span>';
                        } else if (o.aData.status === 'BLOCKED') {
                            return '<span class="highlight red">Blocked</span>';
                        } else if (o.aData.status === 'SPAM') {
                            return '<span class="highlight red">Spam</span>';
                        } else if (o.aData.status === 'DELETED') {
                            return '<span class="highlight red">Deleted</span>';
                        }
                        return '<span class="highlight red">?</span>';
                    }
                },
                {
                    'bSortable': false, mData: 'id',
                    'fnRender': function (o) {
                        return '' +
                            '<form method="GET">' +
                            '<button class="button red tiny" name="publishBlogCommentButton" value="' + o.aData.id + '" tabindex="-1">Publish</button>' +
                            '<button class="button red tiny" name="blockBlogCommentButton" value="' + o.aData.id + '" tabindex="-1">Reject</button>' +
                            '<button class="button red tiny" name="markAsSpamBlogPostCommentButton" value="' + o.aData.id + '" tabindex="-1">Spam</button>' +
                            '<button class="button red tiny" name="deleteBlogCommentButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>' +
                            '</form>'
                            ;
                    }
                }
            ]
        });

    });

    var $body = $('body');

    function publishBlogComment(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'publishBlogPostComment',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#blogPostCommentsList").dataTable().fnDraw();
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

    function blockBlogComment(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'blockBlogPostComment',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#blogPostCommentsList").dataTable().fnDraw();
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

    function markAsSpamBlogPostComment(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'markAsSpamBlogPostComment',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#blogPostCommentsList").dataTable().fnDraw();
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

    function deleteBlogComment(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteBlogPostComment',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#blogPostCommentsList").dataTable().fnDraw();
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

    $body.on('click', 'button[name="publishBlogCommentButton"]', function () {
        publishBlogComment($(this).val());
    });

    $body.on('click', 'button[name="blockBlogCommentButton"]', function () {
        blockBlogComment($(this).val());
    });

    $body.on('click', 'button[name="markAsSpamBlogPostCommentButton"]', function () {
        markAsSpamBlogPostComment($(this).val());
    });

    $body.on('click', 'button[name="deleteBlogCommentButton"]', function () {
        deleteBlogComment($(this).val());
    });

});
