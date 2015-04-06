require(['jquery', 'knockout', 'formAlerts', 'jqueryUi', 'cmsLayout', 'dataTable', 'foundation', 'foundationTabs'], function ($, ko, formAlertsModule) {

    function SinglePostViewModel() {
        var self = this;
        self.languageId = ko.observable();
        self.postTextCode = ko.observable($('#postTextCode').val());
        self.status = ko.observable($('#status').val());
        self.postTitle = ko.observable();
        self.postShortcut = ko.observable();
        self.postDescription = ko.observable();
        self.i18nDataStatus = ko.observable();
        self.imageTitle = ko.observable($('#title').val());
        self.blogKeywordsAssignment = ko.observableArray();
        self.codeId = ko.observable(null);
        self.code = ko.observable('');
        self.basicInfoAlerts = new formAlertsModule.formAlerts();
        self.i18nContentAlerts = new formAlertsModule.formAlerts();
        self.commentsAlerts = new formAlertsModule.formAlerts();
        self.imagesAlerts = new formAlertsModule.formAlerts();
        self.blogPostCodeAlerts = new formAlertsModule.formAlerts();

        self.postTextCodeIsRequiredStyle = ko.computed(function () {
            return self.postTextCode() == null || self.postTextCode() === '' ? 'error' : 'invisible';
        });
        self.postTitleIsRequiredStyle = ko.computed(function () {
            if (self.languageId() == null) {
                return 'invisible';
            }
            return self.postTitle() == null || self.postTitle() === '' ? 'error' : 'invisible';
        });
        self.i18nLanguageIsRequiredStyle = ko.computed(function () {
            return self.languageId() == null ? 'error' : 'invisible';
        });
        self.imageTitleIsRequiredStyle = ko.computed(function () {
            return self.imageTitle() == null || self.imageTitle() === '' ? 'error' : 'invisible';
        });
        self.codeIdIsRequiredStyle = ko.computed(function () {
            return self.codeId() == null || self.codeId() === '' ? 'error' : 'invisible';
        });

        self.editPostFormIsValid = ko.computed(function () {
            return self.postTextCode() != null && self.postTextCode() !== '' &&
                self.status() != null && self.status() !== '';
        });
        self.addImageFormIsValid = ko.computed(function () {
            return self.imageTitle() != null && self.imageTitle() !== '';
        });

        self.isPostI18nVisible = ko.computed(function () {
            return self.languageId() != null;
        });
        self.savePostI18nFormIsValid = ko.computed(function () {
            return self.postTitle() != null && self.postTitle() !== '';
        });
        self.addCodeFormIsValid = ko.computed(function () {
            return self.codeId() != null && self.codeId() !== '';
        });

        self.initializeSinglePostViewModel = function () {
            self.languageId(null);
        };

        self.initializeI18nData = function () {
            $.ajax({
                url: $('#blogPostId').val() + '/' + self.languageId(),
                async: true,
                success: function (response) {
                    if (response != null && 'SUCCESS' === response.status) {
                        self.postTitle(response.data.postTitle);
                        self.postShortcut(response.data.postShortcut);
                        self.postDescription(response.data.postDescription);
                        self.i18nDataStatus(response.data.status);
                    }
                }
            });
        };

        self.initializeBlogKeywordsAssignment = function () {
            $.ajax({
                url: '../blogPostKeywordAssignment',
                data: {
                    blogPostId: $('#blogPostId').val()
                },
                async: true,
                success: function (response) {
                    self.blogKeywordsAssignment(response.aaData);
                }
            });
        };

        self.changeBlogKeywordAssignment = function (item) {
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: '../postCategoryUpdate',
                data: {
                    postId: $('#blogPostId').val(),
                    id: item.id,
                    assigned: item.assigned
                },
                async: true,
                success: function (response) {
                }
            });
            return true;
        };
    }

    var singlePostViewModel = new SinglePostViewModel();

    $(document).ready(function () {
        ko.applyBindings(singlePostViewModel);
        singlePostViewModel.initializeBlogKeywordsAssignment();

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
                    'bSortable': false, mData: 'image', 'fnRender': function (o) {
                    return '<img src="../blogPostImages/?imageId=' + o.aData.id + '" height="200" width="480"/>';
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

        $('#saveBlogPostButton').click(function () {
            saveBlogPost();
        });
        $('#updatePostI18nButton').click(function () {
            updatePostI18n();
        });
        $('#addBlogPostCodeButton').click(function () {
            addBlogPostCode();
        });

        var $body = $('body');
        $body.on('click', '#addBlogPostCodeButton', function () {
            singlePostViewModel.initializeSinglePostViewModel();
        });
        $body.on('click', 'button[name="removeBlogPostCodeButton"]', function () {
            removeBlogPostCode($(this).val());
        });
        $body.on('click', 'button[name="removeBlogPostImageButton"]', function () {
            removeBlogPostImage($(this).val());
        });

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

        $('.ui-autocomplete').addClass('f-dropdown');
    });

    $('#resetBasicInfoForm').click(function () {
        singlePostViewModel.postTextCode($('#postTextCode').val());
        singlePostViewModel.status($('#status').val());
        singlePostViewModel.basicInfoAlerts.cleanAllMessages();
    });

    $('#revertPostI18nButton').click(function () {
        singlePostViewModel.initializeI18nData();
    });

    $('#resetImagesFormButton').click(function () {
        singlePostViewModel.imageTitle($('#title').val());
        singlePostViewModel.imagesAlerts.cleanAllMessages();
    });

    function saveBlogPost() {
        var id = $('#blogPostId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'updatePostBasicInfo',
            data: "postTextCode=" + singlePostViewModel.postTextCode() + "&status=" + singlePostViewModel.status() + "&id=" + id,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        singlePostViewModel.basicInfoAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                singlePostViewModel.basicInfoAlerts.addMessage(response, 'error');
            }
        });
    }

    function updatePostI18n() {
        singlePostViewModel.i18nContentAlerts.cleanAllMessages();
        var blogPostId = $('#blogPostId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'updateBlogPostI18nContent',
            data: "postTitle=" + singlePostViewModel.postTitle() + "&postShortcut=" + singlePostViewModel.postShortcut() +
            "&postDescription=" + singlePostViewModel.postDescription() + "&status=" + singlePostViewModel.i18nDataStatus() +
            "&languageId=" + singlePostViewModel.languageId() + "&postId=" + blogPostId,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        singlePostViewModel.i18nContentAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                singlePostViewModel.i18nContentAlerts.addMessage(response, 'error');
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
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        singlePostViewModel.imagesAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                singlePostViewModel.imagesAlerts.addMessage(response, 'error');
            }
        });
    }

    function addBlogPostCode() {
        singlePostViewModel.blogPostCodeAlerts.cleanAllMessages();
        var blogPostId = $('#blogPostId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addBlogPostCode',
            data: "blogPostId=" + blogPostId + "&codeId=" + singlePostViewModel.codeId() + "&code=" + singlePostViewModel.code() +
                "&status=NEW",
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#blogPostCodesList").dataTable().fnDraw();
                    singlePostViewModel.codeId(null);
                    singlePostViewModel.code('');
                    $("#addBlogPostCodeForm").trigger('reset');
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        singlePostViewModel.blogPostCodeAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                singlePostViewModel.blogPostCodeAlerts.addMessage(response, 'error');
            }
        });
    }

    function removeBlogPostCode(codeId) {
        var blogPostId = $('#blogPostId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteBlogPostCode',
            data: 'blogPostId=' + blogPostId + '&codeId=' + codeId,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $('#blogPostCodesList').dataTable().fnDraw();
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        singlePostViewModel.blogPostCodeAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                singlePostViewModel.blogPostCodeAlerts.addMessage(response, 'error');
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
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        singlePostViewModel.commentsAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                singlePostViewModel.commentsAlerts.addMessage(response, 'error');
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
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        singlePostViewModel.commentsAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                singlePostViewModel.commentsAlerts.addMessage(response, 'error');
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
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        singlePostViewModel.commentsAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                singlePostViewModel.commentsAlerts.addMessage(response, 'error');
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
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        singlePostViewModel.commentsAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                singlePostViewModel.commentsAlerts.addMessage(response, 'error');
            }
        });
    }

});
