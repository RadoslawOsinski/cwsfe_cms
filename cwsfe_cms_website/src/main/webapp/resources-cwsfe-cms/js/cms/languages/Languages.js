require(['jquery', 'cmsLayout', 'dataTable'], function ($) {

    $(document).ready(function () {

        $('#LanguagesList').dataTable({
            'iTabIndex': -1,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'languagesList',
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'code'},
                {'bSortable': false, mData: 'name'},
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '<button class="button red tiny" name="removeLanguageButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>';
                    }
                }
            ]
        });

    });

    $('#addLanguageButton').click(function() {
        addLanguage();
    });

    $('body').on('click', 'button[name="removeLanguageButton"]', function() {
        removeLanguage($(this).val());
    });

    function addLanguage() {
        var code = $('#code').val();
        var name = $('#name').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addLanguage',
            data: "code=" + code + "&name=" + name,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#LanguagesList").dataTable().fnDraw();
                    $('#code').val('');
                    $('#name').val('');
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

    function removeLanguage(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteLanguage',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#LanguagesList").dataTable().fnDraw();
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
