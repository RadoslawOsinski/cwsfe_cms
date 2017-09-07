$(window).load(function () {

    $('#loading-block').fadeOut(function () {
        const arrowSelector = $('.arrow-up, .arrow-down, .arrow-right');
        if (arrowSelector.length) {
            arrowSelector.each(function () {
                let t = $(this);
                let e = t.find('.perc span');
                let v = parseInt(e.attr('data-value'), 10);
                e.data('counter', '');
                e.html('0');
                let $counter = e;
                e.animateNumber(v, 20);
            });
        }
    });

});
