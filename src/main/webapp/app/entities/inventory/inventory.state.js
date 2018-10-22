(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('inventory', {
            parent: 'entity',
            url: '/inventory',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'truckwashApp.inventory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inventory/inventories.html',
                    controller: 'InventoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inventory');
                    $translatePartialLoader.addPart('productInventoryEntry');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('inventory-detail', {
            parent: 'inventory',
            url: '/inventory/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'truckwashApp.inventory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inventory/inventory-detail.html',
                    controller: 'InventoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inventory');
                    $translatePartialLoader.addPart('productInventoryEntry');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Inventory', function($stateParams, Inventory) {
                    return Inventory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'inventory',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('inventory-detail.edit', {
            parent: 'inventory-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inventory/inventory-dialog.html',
                    controller: 'InventoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Inventory', function(Inventory) {
                            return Inventory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inventory.new', {
            parent: 'inventory',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inventory/inventory-dialog.html',
                    controller: 'InventoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                readingTime: null,
                                senrorReading: null,
                                quantity: null,
                                entryType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('inventory', null, { reload: 'inventory' });
                }, function() {
                    $state.go('inventory');
                });
            }]
        })
        .state('inventory.edit', {
            parent: 'inventory',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inventory/inventory-dialog.html',
                    controller: 'InventoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Inventory', function(Inventory) {
                            return Inventory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('inventory', null, { reload: 'inventory' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inventory.delete', {
            parent: 'inventory',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inventory/inventory-delete-dialog.html',
                    controller: 'InventoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Inventory', function(Inventory) {
                            return Inventory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('inventory', null, { reload: 'inventory' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
