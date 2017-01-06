require(['jquery', 'knockout', 'jqueryUi', 'cmsLayout', 'foundation', 'foundationTabs'], function ($, ko) {

    $('#saveUserButton').click(function () {
        saveUser();
    });

    function saveUser() {
        var username = $('#username').val();
        var status = $('#status').val();
        var id = $('#cmsUserId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'updateUserBasicInfo',
            data: "username=" + username + "&status=" + status + "&id=" + id,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#basicInfoFormValidation").html("<p>Success</p>").show('slow');
                } else {
                    var errorInfo = "";
                    for (var i = 0; i < response.result.length; i++) {
                        errorInfo += "<br>" + (i + 1) + ". " + response.result[i].error;
                    }
                    $('#basicInfoFormValidation').html("<p>Please correct following errors: " + errorInfo + "</p>").show('slow');
                }
            },
            error: function (response) {
                console.log('BUG: ' + response);
            }
        });
    }

});
