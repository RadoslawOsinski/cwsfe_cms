require(['jquery', 'knockout', 'formAlerts', 'cmsLayout', 'dataTable'], function ($, ko, formAlertsModule) {

    function FolderViewModel() {
        var self = this;
        self.folderName = ko.observable();
        self.orderNumber = ko.observable(0);

        self.folderNameIsRequiredStyle= ko.computed(function() {
            return self.folderName() === null || self.folderName() === '' ? 'error' : 'invisible';
        });
        self.addFolderFormIsValid = ko.computed(function() {
            return self.folderName() !== null && self.folderName() !== '';
        });
    }

    var viewModel = {
        folderViewModel: new FolderViewModel(),
        formAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);

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

    $('#resetAddFolder').click(function() {
        viewModel.folderViewModel.folderName(null);
        viewModel.folderViewModel.orderNumber(0);
        viewModel.formAlerts.cleanAllMessages();
    });

    function addFolder() {
        viewModel.formAlerts.cleanAllMessages();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addFolder',
            data: "folderName=" + viewModel.folderViewModel.folderName() + "&orderNumber=" + viewModel.folderViewModel.orderNumber(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#foldersList").dataTable().fnDraw();
                    viewModel.folderViewModel.folderName(null);
                    viewModel.folderViewModel.orderNumber(0);
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

});
