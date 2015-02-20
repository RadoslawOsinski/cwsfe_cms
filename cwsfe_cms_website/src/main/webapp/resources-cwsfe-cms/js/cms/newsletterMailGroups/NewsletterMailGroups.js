require(['jquery', 'jqueryUi', 'cmsLayout', 'dataTable'], function ($) {

    $(document).ready(function () {

        $('#newsletterMailGroupsList').dataTable({
            'iTabIndex': -1,
            'bAutoWidth': true,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'newsletterMailGroupsList',
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
                {'bSortable': false, mData: 'newsletterMailGroupName'},
                {
                    'bSortable': false, mData: 'id',
                    "fnRender": function (o) {
                        return '' +
                            '<form method="GET" action="newsletterMailGroups/' + o.aData.id + '">' +
                            '<button class="button green tiny" type="submit" tabindex="-1">Select</button>' +
                            '<button class="button red tiny" type="button" name="removeNewsletterMailGroupButton" value="' + o.aData.id + '" tabindex="-1">Delete</button>' +
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

    function searchNewsletterMailGroup() {
        $("#newsletterMailGroupsList").dataTable().fnDraw();
    }

    $('#addNewsletterMailGroupButton').click(function() {
        addNewsletterMailGroup();
    });

    $('body').on('click', 'button[name="removeNewsletterMailGroupButton"]', function() {
        removeNewsletterMailGroup($(this).val());
    });

    function addNewsletterMailGroup() {
        var newsletterMailGroupName = $('#newsletterMailGroupName').val();
        var languageId = $('#languageId').val();
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'addNewsletterMailGroup',
            data: "name=" + newsletterMailGroupName + "&languageId=" + languageId,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsletterMailGroupsList").dataTable().fnDraw();
                    $("#addNewBlogPostForm").trigger('reset');
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

    function removeNewsletterMailGroup(idValue) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'deleteNewsletterMailGroup',
            data: "id=" + idValue,
            success: function (response) {
                if (response.status === 'SUCCESS') {
                    $("#newsletterMailGroupsList").dataTable().fnDraw();
                    $("#addNewNewsletterMailGroupForm").trigger('reset');
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
