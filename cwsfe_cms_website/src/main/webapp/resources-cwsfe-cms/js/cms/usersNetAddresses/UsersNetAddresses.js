require(['jquery', 'knockout', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($, ko) {

    function UserNetAddressesModel() {
        var self = this;
        self.userId = null;
        self.netAddress = ko.observable();
    }

    var userNetAddressesModel = new UserNetAddressesModel();

    $(document).ready(function () {

        ko.applyBindings(userNetAddressesModel);

        $('#netAddresses').dataTable({
            'iTabIndex': -1,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'usersNetAddressesList',
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'userName'},
                {'bSortable': false, mData: 'inetAddress'},
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '<button class="button red tiny" name="removeNetAddressButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>';
                    }
                }
            ]
        });

        $('#userName').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: 'usersDropList',
                    data: {
                        limit: 5,
                        term: request.term
                    },
                    success: function (data) {
                        response($.map(data.data, function (item) {
                            return {
                                value: item.userName,
                                id: item.id
                            };
                        }));
                    }
                });
            },
            minLength: 0,
            select: function (event, ui) {
                userNetAddressesModel.userId = ui.item.id;
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

    });

    $('#addNetAddressButton').click(function () {
        addNetAddress();
    });

    $('body').on('click', 'button[name="removeNetAddressButton"]', function () {
        removeNetAddress($(this).val());
    });


    function addNetAddress() {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addNetAddress',
            data: "userId=" + userNetAddressesModel.userId + "&inetAddress=" + userNetAddressesModel.netAddress(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#netAddresses").dataTable().fnDraw();
                    $("#userName").val();
                    userNetAddressesModel.netAddress('');
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

    function removeNetAddress(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteNetAddress',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#netAddresses").dataTable().fnDraw();
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
