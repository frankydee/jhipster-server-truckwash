(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .controller('LogEntryDialogController', LogEntryDialogController);

    LogEntryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LogEntry'];

    function LogEntryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LogEntry) {
        var vm = this;

        vm.logEntry = entity;
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
            if (vm.logEntry.id !== null) {
                LogEntry.update(vm.logEntry, onSaveSuccess, onSaveError);
            } else {
                LogEntry.save(vm.logEntry, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('truckwashApp:logEntryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.time = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
