require(['jquery', 'cmsLayout', 'dataTable'], function ($) {

    $(document).ready(function() {

        $('#rolesList').dataTable({
            'iTabIndex': -1,
            'sPaginationType': 'full_numbers',
            'bProcessing': true,
            'bServerSide': true,
            'bFilter': false,
            'aLengthMenu': [10, 20, 30],
            'bPaginate': true,
            'bLengthChange': true,
            'sAjaxSource': 'rolesList',
            aoColumns: [
                {'bSortable': false, mData: '#'},
                {'bSortable': false, mData: 'roleCode'},
                {'bSortable': false, mData: 'roleName'}
            ]
        });
    });

});
