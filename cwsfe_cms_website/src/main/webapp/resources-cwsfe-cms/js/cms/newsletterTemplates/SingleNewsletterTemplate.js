require(['jquery', 'jqueryUi', 'cmsLayout'], function ($) {

    $(document).ready(function () {

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

    function newsletterTemplateTestSend() {
        var newsletterTemplateId = $('#newsletterTemplateId').val();
        var testEmail = $('#testEmail').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'newsletterTemplateTestSend',
            data: "id=" + newsletterTemplateId + "&email=" + testEmail,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsletterTemplateTestSendFormValidation").html("<p>Success</p>").show('slow');
                } else {
                    var errorInfo = "";
                    for (var i = 0; i < response.result.length; i++) {
                        errorInfo += "<br>" + (i + 1) + ". " + response.result[i].error;
                    }
                    $('#newsletterTemplateTestSendFormValidation').html("<p>Please correct following errors: " + errorInfo + "</p>").show('slow');
                }
            },
            error: function (response) {
                console.log('BUG: ' + response);
            }
        });
    }

});
