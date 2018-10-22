(function() {
    'use strict';

    angular
        .module('truckwashApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('customer-product-or-service', {
            parent: 'entity',
            url: '/customer-product-or-service',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'truckwashApp.customerProductOrService.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-product-or-service/customer-product-or-services.html',
                    controller: 'CustomerProductOrServiceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customerProductOrService');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('customer-product-or-service-detail', {
            parent: 'customer-product-or-service',
            url: '/customer-product-or-service/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'truckwashApp.customerProductOrService.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-product-or-service/customer-product-or-service-detail.html',
                    controller: 'CustomerProductOrServiceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customerProductOrService');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CustomerProductOrService', function($stateParams, CustomerProductOrService) {
                    return CustomerProductOrService.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'customer-product-or-service',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('customer-product-or-service-detail.edit', {
            parent: 'customer-product-or-service-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-product-or-service/customer-product-or-service-dialog.html',
                    controller: 'CustomerProductOrServiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomerProductOrService', function(CustomerProductOrService) {
                            return CustomerProductOrService.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer-product-or-service.new', {
            parent: 'customer-product-or-service',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-product-or-service/customer-product-or-service-dialog.html',
                    controller: 'CustomerProductOrServiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                percentDiscount: null,
                                overridingPricePerUnit: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('customer-product-or-service', null, { reload: 'customer-product-or-service' });
                }, function() {
                    $state.go('customer-product-or-service');
                });
            }]
        })
        .state('customer-product-or-service.edit', {
            parent: 'customer-product-or-service',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-product-or-service/customer-product-or-service-dialog.html',
                    controller: 'CustomerProductOrServiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomerProductOrService', function(CustomerProductOrService) {
                            return CustomerProductOrService.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-product-or-service', null, { reload: 'customer-product-or-service' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer-product-or-service.delete', {
            parent: 'customer-product-or-service',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-product-or-service/customer-product-or-service-delete-dialog.html',
                    controller: 'CustomerProductOrServiceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CustomerProductOrService', function(CustomerProductOrService) {
                            return CustomerProductOrService.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-product-or-service', null, { reload: 'customer-product-or-service' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
