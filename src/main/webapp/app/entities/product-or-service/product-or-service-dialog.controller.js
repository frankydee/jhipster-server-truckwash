(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('ProductOrServiceDialogController', ProductOrServiceDialogController);

    ProductOrServiceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProductOrService'];

    function ProductOrServiceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProductOrService) {
        var vm = this;

        vm.productOrService = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.productOrService.id !== null) {
                ProductOrService.update(vm.productOrService, onSaveSuccess, onSaveError);
            } else {
                ProductOrService.save(vm.productOrService, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('truckwashApp:productOrServiceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
