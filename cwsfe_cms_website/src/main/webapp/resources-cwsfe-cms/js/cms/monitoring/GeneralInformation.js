require(['jquery', 'cmsLayout'], function ($) {

    $(document).ready(function () {

        const refreshMemoryInfo = function () {
            $.ajax({
                url: 'generalMemoryInfo',
                dataType: "json",
                success: function (data) {
                    $('#usedMemoryInMb').html(data.usedMemoryInMb);
                    $('#availableMemoryInMB').html(data.availableMemoryInMB);
                }
            });
        };
        setInterval(refreshMemoryInfo, 3000);

    });

});
