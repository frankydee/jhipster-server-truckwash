(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sold-item', {
            parent: 'entity',
            url: '/sold-item',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'truckwashApp.soldItem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sold-item/sold-items.html',
                    controller: 'SoldItemController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('soldItem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sold-item-detail', {
            parent: 'sold-item',
            url: '/sold-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'truckwashApp.soldItem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sold-item/sold-item-detail.html',
                    controller: 'SoldItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('soldItem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SoldItem', function($stateParams, SoldItem) {
                    return SoldItem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sold-item',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sold-item-detail.edit', {
            parent: 'sold-item-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sold-item/sold-item-dialog.html',
                    controller: 'SoldItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SoldItem', function(SoldItem) {
                            return SoldItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sold-item.new', {
            parent: 'sold-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sold-item/sold-item-dialog.html',
                    controller: 'SoldItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                totaliserStart: null,
                                totaliserEnd: null,
                                quantity: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sold-item', null, { reload: 'sold-item' });
                }, function() {
                    $state.go('sold-item');
                });
            }]
        })
        .state('sold-item.edit', {
            parent: 'sold-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sold-item/sold-item-dialog.html',
                    controller: 'SoldItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SoldItem', function(SoldItem) {
                            return SoldItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sold-item', null, { reload: 'sold-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sold-item.delete', {
            parent: 'sold-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sold-item/sold-item-delete-dialog.html',
                    controller: 'SoldItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SoldItem', function(SoldItem) {
                            return SoldItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sold-item', null, { reload: 'sold-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
