require(['jquery', 'knockout', 'formAlerts', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($, ko, formAlertsModule) {

    function UserNetAddressesModel() {
        var self = this;
        self.userId = ko.observable();
        self.netAddress = ko.observable();

        self.userNameIsRequiredStyle = ko.computed(function () {
            return self.userId() === null || self.userId() === '' ? 'error' : 'invisible';
        });
        self.netAddressIsRequiredStyle = ko.computed(function () {
            return self.netAddress() === null || self.netAddress() === '' ? 'error' : 'invisible';
        });
        self.addUserNetAddressFormIsValid = ko.computed(function () {
            return self.userId() !== null && self.userId() !== '' &&
                self.netAddress() !== null && self.netAddress() !== '';
        });
    }

    var viewModel = {
        userNetAddressesModel: new UserNetAddressesModel(),
        formAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {

        ko.applyBindings(viewModel);

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
                {'bSortable': false, mData: 'orderNumber'},
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
                viewModel.userNetAddressesModel.userId(ui.item.id);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('.ui-autocomplete').addClass('f-dropdown');
    });

    $('#resetAddNetAddressButton').click(function () {
        viewModel.userNetAddressesModel.userId(null);
        viewModel.userNetAddressesModel.netAddress(null);
        viewModel.formAlerts.cleanAllMessages();
    });

    function addNetAddress() {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addNetAddress',
            data: "userId=" + viewModel.userNetAddressesModel.userId() + "&inetAddress=" + viewModel.userNetAddressesModel.netAddress(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#netAddresses").dataTable().fnDraw();
                    viewModel.userNetAddressesModel.userId(null);
                    viewModel.userNetAddressesModel.netAddress(null);
                } else {
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        viewModel.formAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.formAlerts.addMessage(response, 'error');
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
                    for (var i = 0; i < response.errorMessages.length; i++) {
                        viewModel.formAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.formAlerts.addMessage(response, 'error');
            }
        });
    }

    $('#addNetAddressButton').click(function () {
        addNetAddress();
    });

    $('body').on('click', 'button[name="removeNetAddressButton"]', function () {
        removeNetAddress($(this).val());
    });

});
