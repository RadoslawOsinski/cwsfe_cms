require(['jquery', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($) {

    $(document).ready(function () {

        $('#newsletterMailAddressesList').dataTable({
            'iTabIndex': -1,
            'bAutoWidth': true,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'newsletterMailAddressesList',
            'fnServerParams': function (aoData) {
                aoData.push({'name': "mailGroupId", 'value': $('#mailGroupId').val()});
                aoData.push({'name': "searchMail", 'value': $('#searchMail').val()});
            },
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'email'},
                {
                    'bSortable': false, mData: 'status',
                    'fnRender': function (o) {
                        if (o.aData.status === 'ACTIVE') {
                            return '<span class="highlight green">Active</span>';
                        } else if (o.aData.status === 'INACTIVE') {
                            return '<span class="highlight yellow">Inactive</span>';
                        } else if (o.aData.status === 'ERROR') {
                            return '<span class="highlight red">Error</span>';
                        } else if (o.aData.status === 'DELETED') {
                            return '<span class="highlight red">Deleted</span>';
                        }
                        return '<span class="highlight red">?</span>';
                    }
                },
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '' +
                            '<button class="button green tiny" name="activateNewsletterMailAddressButton" value="' + o.aData.id + '" tabindex="-1">Activate</button>' +
                            '<button class="button blue tiny" name="deactivateNewsletterMailAddressButton" value="' + o.aData.id + '" tabindex="-1">Deactivate</button>' +
                            '<button class="button red tiny" name="removeNewsletterMailAddressButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>'
                            ;
                    }
                }
            ]
        });

        $('#language').autocomplete({
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
                $('#languageId').val(ui.item.id);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

    });

    function searchMailInNewsletterMailGroup() {
        $("#newsletterMailAddressesList").dataTable().fnDraw();
    }

    $('#searchMailInNewsletterMailGroupButton').click(function() {
        searchMailInNewsletterMailGroup();
    });

    $('#saveNewsletterMailGroupButton').click(function() {
        updateNewsletterMailGroup();
    });

    $('#addNewsletterMailAddressButton').click(function() {
        addNewsletterMailAddress();
    });

    var $body = $('body');
    $body.on('click', 'button[name="activateNewsletterMailAddressButton"]', function() {
        activateNewsletterMailAddress($(this).val());
    });

    $body.on('click', 'button[name="deactivateNewsletterMailAddressButton"]', function() {
        deactivateNewsletterMailAddress($(this).val());
    });

    $body.on('click', 'button[name="removeNewsletterMailAddressButton"]', function() {
        removeNewsletterMailAddress($(this).val());
    });

    function updateNewsletterMailGroup() {
        var mailGroupId = $('#mailGroupId').val();
        var languageId = $('#languageId').val();
        var newsletterMailGroupName = $('#newsletterMailGroupName').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'updateNewsletterMailGroup',
            data: "id=" + mailGroupId + "&name=" + newsletterMailGroupName + "&languageId=" + languageId,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#mailGroupEditFormValidation").html("<p>Success</p>").show('slow');
                } else {
                    var errorInfo = "";
                    for (var i = 0; i < response.result.length; i++) {
                        errorInfo += "<br>" + (i + 1) + ". " + response.result[i].error;
                    }
                    $('#mailGroupEditFormValidation').html("<p>Please correct following errors: " + errorInfo + "</p>").show('slow');
                }
            },
            error: function (response) {
                console.log('BUG: ' + response);
            }
        });
    }

    function addNewsletterMailAddress() {
        var mailGroupId = $('#mailGroupId').val();
        var newsletterMailAddress = $('#newsletterMailAddress').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addNewsletterMailAddresses',
            data: "mailGroupId=" + mailGroupId + "&email=" + newsletterMailAddress,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsletterMailAddressesList").dataTable().fnDraw();
                    $("#addNewMailAddressForm").trigger('reset');
                } else {
                    var errorInfo = "";
                    for (var i = 0; i < response.result.length; i++) {
                        errorInfo += "<br>" + (i + 1) + ". " + response.result[i].error;
                    }
                    $('#addNewMailAddressFormValidation').html("<p>Please correct following errors: " + errorInfo + "</p>").show('slow');
                }
            },
            error: function (response) {
                console.log('BUG: ' + response);
            }
        });
    }

    function removeNewsletterMailAddress(id) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteNewsletterMailAddress',
            data: 'id=' + id,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $('#newsletterMailAddressesList').dataTable().fnDraw();
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

    function activateNewsletterMailAddress(id) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'activateNewsletterMailAddress',
            data: 'id=' + id,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $('#newsletterMailAddressesList').dataTable().fnDraw();
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

    function deactivateNewsletterMailAddress(id) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deactivateNewsletterMailAddress',
            data: 'id=' + id,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $('#newsletterMailAddressesList').dataTable().fnDraw();
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