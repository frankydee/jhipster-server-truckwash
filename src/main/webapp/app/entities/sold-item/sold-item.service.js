(function() {
    'use strict';
    angular
        .module('truckwashApp')
        .factory('SoldItem', SoldItem);

    SoldItem.$inject = ['$resource'];

    function SoldItem ($resource) {
        var resourceUrl =  'api/sold-items/:id';

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
