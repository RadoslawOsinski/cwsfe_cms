require(['jquery', 'knockout', 'formAlerts', 'cmsLayout', 'dataTable'], function ($, ko, formAlertsModule) {

    function BlogKeywordsViewModel() {
        var self = this;
        self.keywordName = ko.observable();

        self.keywordNameIsRequiredStyle= ko.computed(function() {
            return self.keywordName() === null || self.keywordName() === '' ? 'error' : 'invisible';
        });
        self.addBlogKeywordFormIsValid = ko.computed(function() {
            return self.keywordName() !== null && self.keywordName() !== '';
        });
    }

    var viewModel = {
        blogKeywordsViewModel: new BlogKeywordsViewModel(),
        formAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);

        $('#blogKeywordsList').dataTable({
            'iTabIndex': -1,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'blogKeywordsList',
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'keywordName'},
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '<button class="button red tiny" name="removeBlogKeywordButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>';
                    }
                }
            ]
        });

    });

    $('#addBlogKeywordButton').click(function() {
        addBlogKeyword();
    });

    $('body').on('click', 'button[name="removeBlogKeywordButton"]', function() {
        removeBlogKeyword($(this).val());
    });

    $('#resetAddBlogKeyword').click(function() {
        viewModel.blogKeywordsViewModel.keywordName(null);
        viewModel.formAlerts.cleanAllMessages();
    });

    function addBlogKeyword() {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addBlogKeyword',
            data: "keywordName=" + viewModel.blogKeywordsViewModel.keywordName(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#blogKeywordsList").dataTable().fnDraw();
                    viewModel.blogKeywordsViewModel.keywordName('');
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

    function removeBlogKeyword(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteBlogKeyword',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#blogKeywordsList").dataTable().fnDraw();
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
