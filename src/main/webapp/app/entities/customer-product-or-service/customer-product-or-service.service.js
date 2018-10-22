(function() {
    'use strict';
    angular
        .module('truckwashApp')
        .factory('CustomerProductOrService', CustomerProductOrService);

    CustomerProductOrService.$inject = ['$resource'];

    function CustomerProductOrService ($resource) {
        var resourceUrl =  'api/customer-product-or-services/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
