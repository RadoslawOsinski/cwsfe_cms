require(['jquery', 'knockout', 'jqueryUi', 'cmsLayout', 'dataTable', 'foundation', 'foundationTabs'], function ($, ko) {

    function SinglePostViewModel() {
        var self = this;
        self.languageId = ko.observable();
        self.i18nData = ko.observable();

        self.isPostI18nVisible = ko.computed(function() {
            return self.languageId() != null;
        });

        self.initializeSinglePostViewModel = function() {
            self.languageId(null);
        };

        self.initializeI18nData = function () {
            $.ajax({
                url: $('#blogPostId').val() + '/' + self.languageId(),
                async: true,
                success: function (response) {
                    if (response != null && 'SUCCESS' === response.status) {
                        self.i18nData({
                            postTitle: response.data.postTitle,
                            postShortcut: response.data.postShortcut,
                            postDescription: response.data.postDescription,
                            status: response.data.status
                        });
                    }
                }
            });
        };
    }

    $(document).ready(function () {

        var singlePostViewModel = new SinglePostViewModel();

        ko.applyBindings(singlePostViewModel);

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
                        return '<button class="button red tiny" name="removeBlogPostImageButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>';
                    }
                }
            ]
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
                singlePostViewModel.languageId(ui.item.id);
                singlePostViewModel.initializeI18nData();
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
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
                        return '<button class="button red tiny" name="removeBlogPostCodeButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>';
                    }
                }
            ]
        });

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
            'sAjaxSource': '../blogPostCommentsList',
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'username'},
                {'bSortable': false, mData: 'comment'},
                {'bSortable': false, mData: 'created'},
                {
                    'bSortable': false, mData: 'status',
                    'fnRender': function (o) {
                        if (o.aData.status === 'N') {
                            return '<span class="highlight yellow">New</span>';
                        } else if (o.aData.status === 'P') {
                            return '<span class="highlight green">Published</span>';
                        } else if (o.aData.status === 'B') {
                            return '<span class="highlight red">Blocked</span>';
                        } else if (o.aData.status === 'S') {
                            return '<span class="highlight red">Spam</span>';
                        } else if (o.aData.status === 'D') {
                            return '<span class="highlight red">Deleted</span>';
                        }
                        return '<span class="highlight red">Deleted</span>';
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

        $('#saveBlogPostButton').click(function() {
            saveBlogPost();
        });
        $('#addBlogPostCodeButton').click(function() {
            addBlogPostCode();
        });

        var $body = $('body');
        $body.on('click', '#addBlogPostCodeButton', function() {
            singlePostViewModel.initializeSinglePostViewModel();
        });
        $body.on('click', 'button[name="removeBlogPostCodeButton"]', function() {
            removeBlogPostCode($(this).val());
        });
        $body.on('click', 'button[name="removeBlogPostImageButton"]', function() {
            removeBlogPostImage($(this).val());
        });

        $body.on('click', 'button[name="publishBlogCommentButton"]', function() {
            publishBlogComment($(this).val());
        });

        $body.on('click', 'button[name="blockBlogCommentButton"]', function() {
            blockBlogComment($(this).val());
        });

        $body.on('click', 'button[name="markAsSpamBlogPostCommentButton"]', function() {
            markAsSpamBlogPostComment($(this).val());
        });

        $body.on('click', 'button[name="deleteBlogCommentButton"]', function() {
            deleteBlogComment($(this).val());
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

    function publishBlogComment(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: '../publishBlogPostComment',
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
            url: '../blockBlogPostComment',
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
            url: '../markAsSpamBlogPostComment',
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
            url: '../deleteBlogPostComment',
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

});
