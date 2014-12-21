require(['jquery', 'cmsLayout', 'dataTable'], function ($) {

    $(document).ready(function () {

        $('#foldersList').dataTable({
            'iTabIndex': -1,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'foldersList',
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'folderName'},
                {'bSortable': false, mData: 'orderNumber'},
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '<button class="button red tiny" name="deleteFolderButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>';
                    }
                }
            ]
        });

    });

    $('#addFolderButton').click(function() {
        addFolder();
    });

    $('body').on('click', 'button[name="deleteFolderButton"]', function() {
        deleteFolder($(this).val());
    });

    function addFolder() {
        var folderName = $('#folderName').val();
        var orderNumber = $('#orderNumber').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addFolder',
            data: "folderName=" + folderName + "&orderNumber=" + orderNumber,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#foldersList").dataTable().fnDraw();
                    $('#folderName').val('');
                    $('#orderNumber').val('');
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

    function deleteFolder(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteFolder',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#foldersList").dataTable().fnDraw();
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
