require(['jquery', 'knockout', 'foundation', 'foundationTabs', 'foundationOffCanvas'], function ($) {

    function getLoadTime() {
        const now = new Date().getTime();
        // Get the performance object window .
        const performance = window.performance || window.mozPerformance || window.msPerformance || window.webkitPerformance || {};
        const timing = performance.timing || {};
        if (timing) {
            const load_time = now - timing.navigationStart;
            $('#loadTimeValue').html(load_time);
        }
    }

    function handleTabSelection() {
        //check current label
        $(this).attr('aria-selected', 'true');
        const tabLabel = $(this).parent();
        tabLabel.addClass('active');

        //uncheck other labels
        const otherTabLabels = tabLabel.siblings("li");
        $.each(otherTabLabels, function (i, tab) {
            $(tab).removeClass('active');
            $(tab).children().filter('a[role="tab"]').attr('aria-selected', 'false');
        });

        const controlledSection = $('#' + $(this).attr('aria-controls'));
        //hide other sections
        controlledSection.siblings().removeClass('active');
        controlledSection.siblings().attr('aria-hidden', 'true');

        //show current section
        controlledSection.addClass('active');
        controlledSection.attr('aria-hidden', 'false');
    }

    //show menu
    $(document).ready(function () {
        $(document).foundation();

        getLoadTime();

        $('.side-nav li.withSubMenu').click(function () {
            $(this).siblings().find("ul").hide();
            $(this).find("ul").toggle();
        });

        $('a[role="tab"]').click(handleTabSelection);

    });

});
