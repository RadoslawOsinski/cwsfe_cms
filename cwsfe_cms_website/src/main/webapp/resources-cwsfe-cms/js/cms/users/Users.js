require(['jquery', 'knockout', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($, ko) {

    function UsersViewModel() {
        var self = this;
        self.userName = ko.observable();
        self.passwordHash = ko.observable();

        self.userNameIsRequiredStyle= ko.computed(function() {
            return self.userName() == null || self.userName() === '' ? 'error' : 'invisible';
        });
        self.passwordIsRequiredStyle= ko.computed(function() {
            return self.passwordHash() == null || self.passwordHash() === '' ? 'error' : 'invisible';
        });
        self.addUserFormIsValid = ko.computed(function() {
            return self.userName() != null && self.userName() !== '' &&
                self.passwordHash() != null && self.passwordHash() !== '';
        });
    }

    var usersViewModel = new UsersViewModel();

    $(document).ready(function () {
        ko.applyBindings(usersViewModel);

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
                {'bSortable': false, mData: '#'},
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

    $('#addUserButton').click(function() {
        addUser();
    });

    function addUser() {
        var userName = $('#userName').val();
        var passwordHash = $('#passwordHash').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addUser',
            data: "userName=" + userName + "&passwordHash=" + passwordHash,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#usersList").dataTable().fnDraw();
                    usersViewModel.userName(null);
                    usersViewModel.passwordHash(null);
                } else {
                    //todo validate alert
                }
            },
            error: function (response) {
                console.log('BUG: ' + response);
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

    function lockUser(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'lockUser',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#usersList").dataTable().fnDraw();
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

    function unlockUser(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'unlockUser',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#usersList").dataTable().fnDraw();
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
