require(['jquery', 'knockout', 'formAlerts', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($, ko, formAlertsModule) {

    function BlogPostsViewModel() {
        var self = this;
        self.authorId = null;
        self.author = ko.observable();
        self.postTextCode = ko.observable();

        self.authorIsRequiredStyle = ko.computed(function() {
            return self.author() === null || self.author() === '' ? 'error' : 'invisible';
        });
        self.postTextCodeIsRequiredStyle = ko.computed(function() {
            return self.postTextCode() === null || self.postTextCode() === '' ? 'error' : 'invisible';
        });
        self.addBlogPostFormIsValid = ko.computed(function() {
            return self.author() !== null && self.author() !== '' &&
                self.postTextCode() !== null && self.postTextCode() !== '';
        });
    }

    var viewModel = {
        blogPostsViewModel: new BlogPostsViewModel(),
        formAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);

        $('#blogPostsList').dataTable({
            'iTabIndex': -1,
            'bAutoWidth': true,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'blogPostsList',
            'fnServerParams': function (aoData) {
                aoData.push(
                    {'name': "searchAuthorId", 'value': $('#searchAuthorId').val()},
                    {'name': "searchPostTextCode", 'value': $('#searchPostTextCode').val()}
                );
            },
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'author'},
                {'bSortable': false, mData: 'postTextCode'},
                {'bSortable': false, mData: 'postCreationDate'},
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '' +
                            '<form method="GET" action="blogPosts/' + o.aData.id + '">' +
                            '<button class="button green tiny" type="submit" tabindex="-1">Select</button>' +
                            '</form>' +
                            '<button class="button red tiny" name="removeBlogPostButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>'
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
                viewModel.blogPostsViewModel.authorId = ui.item.id;
                viewModel.blogPostsViewModel.author(ui.item.value);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('.ui-autocomplete').addClass('f-dropdown');
    });

    function searchBlogPosts() {
        $("#blogPostsList").dataTable().fnDraw();
    }

    $('#addBlogPostButton').click(function() {
        addBlogPost();
    });

    $('body').on('click', 'button[name="removeBlogPostButton"]', function() {
        removeBlogPost($(this).val());
    });

    $('#resetAddBlogPost').click(function() {
        viewModel.blogPostsViewModel.authorId = null;
        viewModel.blogPostsViewModel.author(null);
        viewModel.blogPostsViewModel.postTextCode(null);
        viewModel.formAlerts.cleanAllMessages();
    });

    function addBlogPost() {
        viewModel.formAlerts.cleanAllMessages();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addBlogPost',
            data: "postAuthorId=" + viewModel.blogPostsViewModel.authorId + "&postTextCode=" + viewModel.blogPostsViewModel.postTextCode(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#blogPostsList").dataTable().fnDraw();
                    viewModel.blogPostsViewModel.authorId = null;
                    viewModel.blogPostsViewModel.author(null);
                    viewModel.blogPostsViewModel.postTextCode(null);
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

    function removeBlogPost(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteBlogPost',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#blogPostsList").dataTable().fnDraw();
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
