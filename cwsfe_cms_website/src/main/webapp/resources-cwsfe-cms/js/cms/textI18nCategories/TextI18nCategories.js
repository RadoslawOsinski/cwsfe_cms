require(['jquery', 'knockout', 'formAlerts', 'cmsLayout', 'dataTable'], function ($, ko, formAlertsModule) {

    function CategoryViewModel() {
        const self = this;
        self.category = ko.observable();

        self.categoryIsRequiredStyle = ko.computed(function () {
            return self.category() === null || self.category() === '' ? 'error' : 'invisible';
        });
        self.addCategoryFormIsValid = ko.computed(function () {
            return self.category() !== null && self.category() !== '';
        });
    }

    const viewModel = {
        categoryViewModel: new CategoryViewModel(),
        formAlerts: new formAlertsModule.formAlerts()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);

        $('#cmsTextI18nCategoriesList').dataTable({
            'iTabIndex': -1,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'cmsTextI18nCategoriesList',
            aoColumns: [
                {'bSortable': false, mData: 'orderNumber'},
                {'bSortable': false, mData: 'category'},
                {
                    'bSortable': false, mData: 'status',
                    'fnRender': function (o) {
                        if (o.aData.status === 'NEW') {
                            return '<span class="highlight yellow">New</span>';
                        } else if (o.aData.status === 'DELETED') {
                            return '<span class="highlight red">Deleted</span>';
                        }
                        return '<span class="highlight red">?</span>';
                    }
                },
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '<button class="button red tiny" name="removeCmsTextI18nCategoryButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>';
                    }
                }
            ]
        });
    });

    $('#resetAddUser').click(function () {
        viewModel.categoryViewModel.category(null);
        viewModel.formAlerts.cleanAllMessages();
    });

    function addCmsTextI18nCategory() {
        viewModel.formAlerts.cleanAllMessages();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addCmsTextI18nCategory',
            data: "category=" + viewModel.categoryViewModel.category(),
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#cmsTextI18nCategoriesList").dataTable().fnDraw();
                    viewModel.categoryViewModel.category(null);
                } else {
                    for (let i = 0; i < response.errorMessages.length; i++) {
                        viewModel.formAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.formAlerts.addMessage(response, 'error');
            }
        });
    }

    function removeCmsTextI18nCategory(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteCmsTextI18nCategory',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#cmsTextI18nCategoriesList").dataTable().fnDraw();
                } else {
                    for (let i = 0; i < response.errorMessages.length; i++) {
                        viewModel.formAlerts.addWarning(response.errorMessages[i].error);
                    }
                }
            },
            error: function (response) {
                viewModel.formAlerts.addMessage(response, 'error');
            }
        });
    }

    $('#addCmsTextI18nCategoryButton').click(function () {
        addCmsTextI18nCategory();
    });

    $('body').on('click', 'button[name="removeCmsTextI18nCategoryButton"]', function () {
        removeCmsTextI18nCategory($(this).val());
    });

});
