(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('product-or-service', {
            parent: 'entity',
            url: '/product-or-service',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'truckwashApp.productOrService.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-or-service/product-or-services.html',
                    controller: 'ProductOrServiceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('productOrService');
                    $translatePartialLoader.addPart('unitOfMeasure');
                    $translatePartialLoader.addPart('productOrServiceType');
                    $translatePartialLoader.addPart('productQuantityMetering');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('product-or-service-detail', {
            parent: 'product-or-service',
            url: '/product-or-service/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'truckwashApp.productOrService.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-or-service/product-or-service-detail.html',
                    controller: 'ProductOrServiceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('productOrService');
                    $translatePartialLoader.addPart('unitOfMeasure');
                    $translatePartialLoader.addPart('productOrServiceType');
                    $translatePartialLoader.addPart('productQuantityMetering');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProductOrService', function($stateParams, ProductOrService) {
                    return ProductOrService.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'product-or-service',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('product-or-service-detail.edit', {
            parent: 'product-or-service-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-or-service/product-or-service-dialog.html',
                    controller: 'ProductOrServiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProductOrService', function(ProductOrService) {
                            return ProductOrService.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('product-or-service.new', {
            parent: 'product-or-service',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-or-service/product-or-service-dialog.html',
                    controller: 'ProductOrServiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                shortDescription: null,
                                longDescription: null,
                                unitOfMeashre: null,
                                pricePerUnit: null,
                                type: null,
                                maxDuration: null,
                                quantityInStock: null,
                                quantityMetering: null,
                                autoMeteringInterval: null,
                                lowLevelAlert: null,
                                active: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('product-or-service', null, { reload: 'product-or-service' });
                }, function() {
                    $state.go('product-or-service');
                });
            }]
        })
        .state('product-or-service.edit', {
            parent: 'product-or-service',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-or-service/product-or-service-dialog.html',
                    controller: 'ProductOrServiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProductOrService', function(ProductOrService) {
                            return ProductOrService.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-or-service', null, { reload: 'product-or-service' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('product-or-service.delete', {
            parent: 'product-or-service',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-or-service/product-or-service-delete-dialog.html',
                    controller: 'ProductOrServiceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProductOrService', function(ProductOrService) {
                            return ProductOrService.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-or-service', null, { reload: 'product-or-service' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
