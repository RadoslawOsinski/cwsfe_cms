require(['jquery', 'knockout', 'jqueryUi', 'cmsLayout', 'foundation', 'foundationTabs'], function ($, ko) {

    $('#saveUserButton').click(function () {
        saveUser();
    });

    function saveUser() {
        const username = $('#username').val();
        const status = $('#status').val();
        const id = $('#cmsUserId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'updateUserBasicInfo',
            data: "userName=" + username + "&status=" + status + "&id=" + id,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#basicInfoFormValidation").html("<p>Success</p>").show('slow');
                } else {
                    let errorInfo = "";
                    for (let i = 0; i < response.errorMessages.length; i++) {
                        errorInfo += "<br>" + (i + 1) + ". " + response.errorMessages[i].error;
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
