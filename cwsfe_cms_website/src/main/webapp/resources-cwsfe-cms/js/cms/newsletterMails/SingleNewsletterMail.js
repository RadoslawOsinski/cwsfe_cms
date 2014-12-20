require(['jquery', 'jqueryUi', 'cmsLayout'], function ($) {

    $(document).ready(function () {

        $('#recipientGroup').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: '../newsletterMailGroupsDropList',
                    data: {
                        limit: 5,
                        term: request.term
                    },
                    success: function (data) {
                        response($.map(data.data, function (item) {
                            return {
                                value: item.newsletterMailGroupName,
                                id: item.id
                            }
                        }));
                    }
                });
            },
            minLength: 0,
            select: function (event, ui) {
                $('#recipientGroupId').val(ui.item.id);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

    });

    function newsletterTestSend() {
        var newsletterMailId = $('#newsletterMailId').val();
        var testEmail = $('#testEmail').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'newsletterTestSend',
            data: "id=" + newsletterMailId + "&email=" + testEmail,
            success: function (response) {
                if (response.status == 'SUCCESS') {
                    $("#newsletterTestSendFormValidation").html("<p>Success</p>").show('slow');
                } else {
                    var errorInfo = "";
                    for (var i = 0; i < response.result.length; i++) {
                        errorInfo += "<br>" + (i + 1) + ". " + response.result[i].error;
                    }
                    $('#newsletterTestSendFormValidation').html("<p>Please correct following errors: " + errorInfo + "</p>").show('slow');
                }
            },
            error: function (response) {
                console.log('BUG: ' + response);
            }
        });
    }

    function confirmNewsletterSend() {
        apprise('Do you really want to send newsletter?', {
            'animate': true,
            'confirm': true
        }, function (r) {
            if (r) {
                newsletterSend();
            }
        });
    }

    function newsletterSend() {
        var newsletterMailId = $('#newsletterMailId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'newsletterSend',
            data: "id=" + newsletterMailId,
            success: function (response) {
                if (response.status == 'SUCCESS') {
                    $("#newsletterEditFormValidation").html("<p>Success</p>").show('slow');
                } else {
                    var errorInfo = "";
                    for (var i = 0; i < response.result.length; i++) {
                        errorInfo += "<br>" + (i + 1) + ". " + response.result[i].error;
                    }
                    $('#newsletterEditFormValidation').html("<p>Please correct following errors: " + errorInfo + "</p>").show('slow');
                }
            },
            error: function (response) {
                console.log('BUG: ' + response);
            }
        });
    }

});
