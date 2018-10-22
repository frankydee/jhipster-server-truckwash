(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('ProductOrServiceDetailController', ProductOrServiceDetailController);

    ProductOrServiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProductOrService'];

    function ProductOrServiceDetailController($scope, $rootScope, $stateParams, previousState, entity, ProductOrService) {
        var vm = this;

        vm.productOrService = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('truckwashApp:productOrServiceUpdate', function(event, result) {
            vm.productOrService = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
