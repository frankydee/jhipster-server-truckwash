(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('SoldItemDetailController', SoldItemDetailController);

    SoldItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SoldItem'];

    function SoldItemDetailController($scope, $rootScope, $stateParams, previousState, entity, SoldItem) {
        var vm = this;

        vm.soldItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('truckwashApp:soldItemUpdate', function(event, result) {
            vm.soldItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
