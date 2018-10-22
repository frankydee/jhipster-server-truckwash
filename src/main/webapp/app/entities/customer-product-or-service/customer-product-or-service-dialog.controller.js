(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('CustomerProductOrServiceDialogController', CustomerProductOrServiceDialogController);

    CustomerProductOrServiceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CustomerProductOrService'];

    function CustomerProductOrServiceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CustomerProductOrService) {
        var vm = this;

        vm.customerProductOrService = entity;
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
            if (vm.customerProductOrService.id !== null) {
                CustomerProductOrService.update(vm.customerProductOrService, onSaveSuccess, onSaveError);
            } else {
                CustomerProductOrService.save(vm.customerProductOrService, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('truckwashApp:customerProductOrServiceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
