require(['jquery', 'foundation'], function ($) {

    function getLoadTime() {
        var now = new Date().getTime();
        // Get the performance object window .
        var performance = window.performance || window.mozPerformance || window.msPerformance || window.webkitPerformance || {};
        var timing = performance.timing || {};
        if (timing) {
            var load_time = now - timing.navigationStart;
            $('#loadTimeValue').html(load_time);
        }
    }

    //show menu
    $(document).ready(function () {
        $(document).foundation();

        getLoadTime();

        $('.side-nav li.withSubMenu').click(function () {
            $(this).siblings().find("ul").hide();
            $(this).find("ul").toggle();
        });
    });

});
