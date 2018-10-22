(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('CustomerProductOrServiceDetailController', CustomerProductOrServiceDetailController);

    CustomerProductOrServiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CustomerProductOrService'];

    function CustomerProductOrServiceDetailController($scope, $rootScope, $stateParams, previousState, entity, CustomerProductOrService) {
        var vm = this;

        vm.customerProductOrService = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('truckwashApp:customerProductOrServiceUpdate', function(event, result) {
            vm.customerProductOrService = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
