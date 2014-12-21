require(['jquery', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($) {

    $(document).ready(function () {

        $('#newsletterTemplatesList').dataTable({
            'iTabIndex': -1,
            'bAutoWidth': true,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'newsletterTemplatesList',
            'fnServerParams': function (aoData) {
                aoData.push(
                    {'name': "searchName", 'value': $('#searchName').val()}
                );
                aoData.push(
                    {'name': "searchLanguageId", 'value': $('#searchLanguageId').val()}
                );
            },
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'language2LetterCode'},
                {'bSortable': false, mData: 'newsletterTemplateName'},
                {'bSortable': false, mData: 'newsletterTemplateSubject'},
                {
                    'bSortable': false, mData: 'newsletterTemplateStatus',
                    'fnRender': function (o) {
                        if (o.aData.newsletterTemplateStatus === 'N') {
                            return '<span class="highlight green">Active</span>';
                        } else if (o.aData.newsletterTemplateStatus === 'D') {
                            return '<span class="highlight red">Deleted</span>';
                        }
                        return '<span class="highlight red">Deleted</span>';
                    }
                },
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '' +
                            '<form method="GET" action="newsletterTemplates/' + o.aData.id + '">' +
                            '<button class="button green tiny" type="submit" tabindex="-1">Select</button>' +
                            '<a class="icon awesome thumbs-up" title="Undelete" tabindex="-1" onclick="unDeleteNewsletterTemplate(' + o.aData.id + ');"></a>' +
                            '<a class="icon awesome icon-remove-sign" title="Delete" tabindex="-1" onclick="removeNewsletterTemplate(' + o.aData.id + ');return false;"></a>' +
                            '</form>'
                            ;

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

        $('#language').autocomplete({
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
                $('#languageId').val(ui.item.id);
            }
        }).focus(function () {
            $(this).autocomplete("search", "");
        });

    });

    function searchNewsletterTemplate() {
        $("#newsletterTemplatesList").dataTable().fnDraw();
    }

    function addNewsletterTemplate() {
        var newsletterTemplateName = $('#newsletterTemplateName').val();
        var languageId = $('#languageId').val();
        var subject = $('#subject').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addNewsletterTemplate',
            data: "name=" + newsletterTemplateName + "&languageId=" + languageId + "&subject=" + subject,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsletterTemplatesList").dataTable().fnDraw();
                    $("#addNewNewsletterTemplateForm").trigger('reset');
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

    function removeNewsletterTemplate(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteNewsletterTemplate',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsletterTemplatesList").dataTable().fnDraw();
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

    function unDeleteNewsletterTemplate(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'unDeleteNewsletterTemplate',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsletterTemplatesList").dataTable().fnDraw();
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
