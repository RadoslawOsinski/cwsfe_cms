require(['jquery', 'knockout', 'formAlerts', 'cmsLayout', 'dataTable'], function ($, ko, formAlertsModule) {

    function NewsTypesViewModel() {
        var self = this;
        self.type = ko.observable();

        self.typeIsRequiredStyle= ko.computed(function() {
            return self.type() == null || self.type() === '' ? 'error' : 'invisible';
        });
        self.addNewsTypeFormIsValid = ko.computed(function() {
            return self.type() != null && self.type() !== '';
        });
    }

    var viewModel = {
        newsTypesViewModel: new NewsTypesViewModel(),
        formAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);
        
        $('#newsTypesList').dataTable({
            'iTabIndex': -1,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'newsTypesList',
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'type'},
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '<button class="button red tiny" name="removeNewsTypeButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>';
                    }
                }
            ]
        });

    });

    $('#addNewsTypeButton').click(function() {
        addNewsType();
    });

    $('body').on('click', 'button[name="removeNewsTypeButton"]', function() {
        removeNewsType($(this).val());
    });

    $('#resetAddNewsType').click(function() {
        viewModel.newsTypesViewModel.type(null);
        viewModel.formAlerts.cleanAllMessages();
    });
    
    function addNewsType() {
        viewModel.formAlerts.cleanAllMessages();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addNewsType',
            data: "type=" + viewModel.newsTypesViewModel.type(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsTypesList").dataTable().fnDraw();
                    viewModel.newsTypesViewModel.type(null);
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

    function removeNewsType(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteNewsType',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsTypesList").dataTable().fnDraw();
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
