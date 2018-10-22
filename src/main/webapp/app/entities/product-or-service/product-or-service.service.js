(function() {
    'use strict';
    angular
        .module('truckwashApp')
        .factory('ProductOrService', ProductOrService);

    ProductOrService.$inject = ['$resource'];

    function ProductOrService ($resource) {
        var resourceUrl =  'api/product-or-services/:id';

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
