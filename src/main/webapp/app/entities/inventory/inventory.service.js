(function() {
    'use strict';
    angular
        .module('truckwashApp')
        .factory('Inventory', Inventory);

    Inventory.$inject = ['$resource', 'DateUtils'];

    function Inventory ($resource, DateUtils) {
        var resourceUrl =  'api/inventories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.readingTime = DateUtils.convertLocalDateFromServer(data.readingTime);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.readingTime = DateUtils.convertLocalDateToServer(copy.readingTime);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.readingTime = DateUtils.convertLocalDateToServer(copy.readingTime);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
