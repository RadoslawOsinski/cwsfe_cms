var tests = Object.keys(window.__karma__.files).filter(function (file) {
    return /Spec\.js$/.test(file);
});

requirejs.config({
    // Karma serves files from '/base'
    baseUrl: 'src/main/webapp',

    paths: {
        jquery: 'resources-cwsfe-cms/js/jquery/jquery-2.1.1.min',
        jqueryUi: '/resources-cwsfe-cms/js/jqueryui/jquery-ui.min',
        foundation: '/resources-cwsfe-cms/js/foundation/foundation',
        foundationTabs: '/resources-cwsfe-cms/js/foundation/foundation.tab',
        foundationOffCanvas: '/resources-cwsfe-cms/js/foundation/foundation.offcanvas',
        foundationReveal: '/resources-cwsfe-cms/js/foundation/foundation.reveal',
        knockout: '/resources-cwsfe-cms/js/knockout/knockout-3.3.0',
        cmsLayout: '/resources-cwsfe-cms/js/cms/layout/CmsLayout',
        dataTable: '/resources-cwsfe-cms/js/datatables/jquery.dataTables.min',
        dataTableFoundation: '/resources-cwsfe-cms/js/datatables/dataTables.foundation'
    },
    shim: {
        'jqueryUi': ['jquery'],
        'dataTable': ['jquery'],
        'foundation': ['jquery'],
        'foundationTabs': ['foundation'],
        'foundationOffCanvas': ['foundation'],
        'foundationReveal': ['foundation'],
        'dataTableFoundation': ['dataTable']
    },

    // ask Require.js to load these files (all our tests)
    deps: tests,

    // start test run, once Require.js is done
    callback: window.__karma__.start
});