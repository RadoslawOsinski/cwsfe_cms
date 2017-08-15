require(['jquery', 'knockout', 'formAlerts', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($, ko, formAlertsModule) {

    function UsersViewModel() {
        var self = this;
        self.userName = ko.observable();
        self.passwordHash = ko.observable();

        self.userNameIsRequiredStyle = ko.computed(function () {
            return self.userName() === null || self.userName() === '' ? 'error' : 'invisible';
        });
        self.passwordIsRequiredStyle = ko.computed(function () {
            return self.passwordHash() === null || self.passwordHash() === '' ? 'error' : 'invisible';
        });
        self.addUserFormIsValid = ko.computed(function () {
            return self.userName() !== null && self.userName() !== '' &&
                self.passwordHash() !== null && self.passwordHash() !== '';
        });
    }

    var viewModel = {
        usersViewModel: new UsersViewModel(),
        formAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);

        $('#usersList').dataTable({
            'iTabIndex': -1,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'usersList',
            aoColumns: [
                {'bSortable': false, mData: 'orderNumber'},
                {'bSortable': false, mData: 'userName'},
                {
                    'bSortable': false, mData: 'status',
                    'fnRender': function (o) {
                        if (o.aData.status === 'NEW') {
                            return '<span class="highlight green">New</span>';
                        } else if (o.aData.status === 'LOCKED') {
                            return '<span class="highlight blue">Locked</span>';
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
                            '<form method="GET" action="users/' + o.aData.id + '">' +
                            '<button class="button green tiny" type="submit" tabindex="-1">Select</button>' +
                            '<a class="icon awesome thumbs-up" title="Unlock" tabindex="-1" onclick="unlockUser(' + o.aData.id + ');"></a>' +
                            '<a class="icon awesome thumbs-down" title="Lock" tabindex="-1" onclick="lockUser(' + o.aData.id + ');"></a>' +
                            '<a class="icon awesome icon-remove-sign" title="Delete" tabindex="-1" onclick="removeUser(' + o.aData.id + ');"></a>' +
                            '</form>';
                    }
                }
            ]
        });

    });

    $('#resetAddUser').click(function () {
        viewModel.usersViewModel.userName(null);
        viewModel.usersViewModel.passwordHash(null);
        viewModel.formAlerts.cleanAllMessages();
    });

    function addUser() {
        viewModel.formAlerts.cleanAllMessages();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addUser',
            data: "userName=" + viewModel.usersViewModel.userName() + "&passwordHash=" + viewModel.usersViewModel.passwordHash(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#usersList").dataTable().fnDraw();
                    viewModel.usersViewModel.userName(null);
                    viewModel.usersViewModel.passwordHash(null);
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

    function removeUser(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteUser',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#usersList").dataTable().fnDraw();
                }
            },
            error: function () {
            }
        });
    }

    function lockUser(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'lockUser',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#usersList").dataTable().fnDraw();
                }
            },
            error: function () {
            }
        });
    }

    function unlockUser(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'unlockUser',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#usersList").dataTable().fnDraw();
                }
            },
            error: function () {
            }
        });
    }

    $('#addUserButton').click(function () {
        addUser();
    });

});
