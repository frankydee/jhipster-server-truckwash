(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('SoldItemDialogController', SoldItemDialogController);

    SoldItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SoldItem'];

    function SoldItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SoldItem) {
        var vm = this;

        vm.soldItem = entity;
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
            if (vm.soldItem.id !== null) {
                SoldItem.update(vm.soldItem, onSaveSuccess, onSaveError);
            } else {
                SoldItem.save(vm.soldItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('truckwashApp:soldItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
