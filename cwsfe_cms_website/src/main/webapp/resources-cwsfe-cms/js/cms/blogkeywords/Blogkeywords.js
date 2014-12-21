require(['jquery', 'cmsLayout', 'dataTable'], function ($) {

    $(document).ready(function () {

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

    function addBlogKeyword() {
        var keywordName = $('#keywordName').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addBlogKeyword',
            data: "keywordName=" + keywordName,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#blogKeywordsList").dataTable().fnDraw();
                    $('#keywordName').val('');
                } else {
                    var errorInfo = "";
                    for (i = 0; i < response.result.length; i++) {
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
                    var errorInfo = "";
                    for (i = 0; i < response.result.length; i++) {
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
