require(['jquery', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($) {

    $(document).ready(function () {

        $('#cmsTextI18nList').dataTable({
            'iTabIndex': -1,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'cmsTextI18nList',
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'language'},
                {'bSortable': false, mData: 'category'},
                {'bSortable': false, mData: 'key'},
                {'bSortable': false, mData: 'text'},
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '<button class="button red tiny" name="removeCmsTextI18nButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>';
                    }
                }
            ]
        });

        $('#searchLanguage').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: 'cmsLanguagesDropList',
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
                $('#searchLanguageId').val(ui.item.id);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('#searchCategory').autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: 'cmsTextI18nCategoryDropList',
                    data: {
                        limit: 5,
                        term: request.term
                    },
                    success: function (data) {
                        response($.map(data.data, function (item) {
                            return {
                                value: item.category,
                                id: item.id
                            };
                        }));
                    }
                });
            },
            minLength: 0,
            select: function (event, ui) {
                $('#searchCategoryId').val(ui.item.id);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

        $('.ui-autocomplete').addClass('f-dropdown');
    });

    $('#addCmsTextI18nButton').click(function() {
        addCmsTextI18n();
    });

    $('body').on('click', 'button[name="removeCmsTextI18nButton"]', function() {
        removeCmsTextI18n($(this).val());
    });

    function addCmsTextI18n() {
        var searchLanguageId = $('#searchLanguageId').val();
        var searchCategoryId = $('#searchCategoryId').val();
        var key = $('#key').val();
        var text = $('#text').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addCmsTextI18n',
            data: "langId=" + searchLanguageId + "&i18nCategory=" + searchCategoryId + "&i18nKey=" + key + "&i18nText=" + text,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#cmsTextI18nList").dataTable().fnDraw();
                    $('#searchLanguage').val('');
                    $('#searchLanguageId').val('');
                    $('#searchCategory').val('');
                    $('#searchCategoryId').val('');
                    $('#key').val('');
                    $('#text').val('');
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

    function removeCmsTextI18n(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteCmsTextI18n',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#cmsTextI18nList").dataTable().fnDraw();
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
