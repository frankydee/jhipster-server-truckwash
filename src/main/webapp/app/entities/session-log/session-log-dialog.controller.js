(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('SessionLogDialogController', SessionLogDialogController);

    SessionLogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SessionLog'];

    function SessionLogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SessionLog) {
        var vm = this;

        vm.sessionLog = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sessionLog.id !== null) {
                SessionLog.update(vm.sessionLog, onSaveSuccess, onSaveError);
            } else {
                SessionLog.save(vm.sessionLog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('truckwashApp:sessionLogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
