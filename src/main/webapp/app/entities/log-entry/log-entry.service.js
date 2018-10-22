(function() {
    'use strict';
    angular
        .module('truckwashApp')
        .factory('LogEntry', LogEntry);

    LogEntry.$inject = ['$resource', 'DateUtils'];

    function LogEntry ($resource, DateUtils) {
        var resourceUrl =  'api/log-entries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.time = DateUtils.convertLocalDateFromServer(data.time);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.time = DateUtils.convertLocalDateToServer(copy.time);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.time = DateUtils.convertLocalDateToServer(copy.time);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
