$(window).load(function () {

    $('#loading-block').fadeOut(function () {
        var arrowSelector = $('.arrow-up, .arrow-down, .arrow-right');
        if (arrowSelector.length) {
            arrowSelector.each(function () {
                var t = $(this);
                var e = t.find('.perc span');
                var v = parseInt(e.attr('data-value'), 10);
                e.data('counter', '');
                e.html('0');
                var $counter = e;
                e.animateNumber(v, 20);
            });
        }
    });

});
