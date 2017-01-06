var assert = require('assert');
//var requirejs = require('../../../../../main/webapp/resources-cwsfe-cms/js/requirejs/require');
//var usersNetAddresses = require('../../../../../main/webapp/resources-cwsfe-cms/js/cms/usersNetAddresses/UsersNetAddresses');

//requirejs.config({
//    //baseUrl: '.',
//    //nodeRequire: require,
//    paths: {
//        jquery: '../../../../../main/webapp/resources-cwsfe-cms/js/jquery/jquery-2.1.1.min',
//        jqueryUi: '../../../../../main/webapp/resources-cwsfe-cms/js/jqueryui/jquery-ui.min',
//        foundation: '../../../../../main/webapp/resources-cwsfe-cms/js/foundation/foundation',
//        foundationTabs: '../../../../../main/webapp/resources-cwsfe-cms/js/foundation/foundation.tab',
//        foundationOffCanvas: '../../../../../main/webapp/resources-cwsfe-cms/js/foundation/foundation.offcanvas',
//        foundationReveal: '../../../../../main/webapp/resources-cwsfe-cms/js/foundation/foundation.reveal',
//        knockout: '../../../../../main/webapp/resources-cwsfe-cms/js/knockout/knockout-3.3.0',
//        cmsLayout: '../../../../../main/webapp/resources-cwsfe-cms/js/cms/layout/CmsLayout',
//        dataTable: '../../../../../main/webapp/resources-cwsfe-cms/js/datatables/jquery.dataTables.min',
//        dataTableFoundation: '../../../../../main/webapp/resources-cwsfe-cms/js/datatables/dataTables.foundation',
//        formAlerts: '../../../../../main/webapp/resources-cwsfe-cms/js/cms/layout/FormAlerts'
//    },
//    shim: {
//        'jqueryUi': ['jquery'],
//        'dataTable': ['jquery'],
//        'foundation': ['jquery'],
//        'foundationTabs': ['foundation'],
//        'foundationOffCanvas': ['foundation'],
//        'foundationReveal': ['foundation'],
//        'dataTableFoundation': ['dataTable']
//    }
//});
//
//describe('Net address management', function() {
//    describe('#addNetAddress', function() {
//        usersNetAddresses.addNetAddress();
//    });
//});

describe('Array', function () {
    describe('#indexOf()', function () {
        it('should return -1 when the value is not present', function () {
            assert.equal(-1, [1, 2, 3].indexOf(5));
            assert.equal(-1, [1, 2, 3].indexOf(0));
        })
    })
});
