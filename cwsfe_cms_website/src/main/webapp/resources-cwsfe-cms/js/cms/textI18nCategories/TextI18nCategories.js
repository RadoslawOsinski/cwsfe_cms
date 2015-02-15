require(['jquery', 'cmsLayout', 'dataTable'], function ($) {

    $(document).ready(function () {

        $('#cmsTextI18nCategoriesList').dataTable({
            'iTabIndex': -1,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'cmsTextI18nCategoriesList',
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'category'},
                {
                    'bSortable': false, mData: 'status',
                    'fnRender': function (o) {
                        if (o.aData.status === 'NEW') {
                            return '<span class="highlight yellow">New</span>';
                        } else if (o.aData.status === 'DELETED') {
                            return '<span class="highlight red">Deleted</span>';
                        }
                        return '<span class="highlight red">?</span>';
                    }
                },
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '<button class="button red tiny" name="removeCmsTextI18nCategoryButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>';
                    }
                }
            ]
        });
    });

    $('#addCmsTextI18nCategoryButton').click(function() {
        addCmsTextI18nCategory();
    });

    $('body').on('click', 'button[name="removeCmsTextI18nCategoryButton"]', function() {
        removeCmsTextI18nCategory($(this).val());
    });

    function addCmsTextI18nCategory() {
        var category = $('#category').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addCmsTextI18nCategory',
            data: "category=" + category,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#cmsTextI18nCategoriesList").dataTable().fnDraw();
                    $('#category').val('');
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

    function removeCmsTextI18nCategory(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteCmsTextI18nCategory',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#cmsTextI18nCategoriesList").dataTable().fnDraw();
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