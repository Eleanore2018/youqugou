app.controller("typeTemplateController", function ($controller, $scope, specificationService, brandService, typeTemplateService) {

    // 继承baseController
    $controller("baseController", {$scope: $scope});

    $scope.searchEntity = {};
    $scope.search = function (currentPage, itemsPerPage) {
        typeTemplateService.searchTypeTemplate(currentPage, itemsPerPage, $scope.searchEntity).success(function (response) {
            $scope.paginationConf.totalItems = response.total;
            $scope.pageTypeTemplateList = response.rows;
        })
    }

    $scope.selectTypeTemplateById = function (id) {
        typeTemplateService.selectTypeTemplateById(id).success(function (response) {
            $scope.entity = response;
            $scope.entity.brandIds = JSON.parse($scope.entity.brandIds);
            $scope.entity.specIds = JSON.parse($scope.entity.specIds);
            $scope.entity.customAttributeItems = JSON.parse($scope.entity.customAttributeItems);
        })
    }

    // 用于回显品牌集合
    $scope.brandList = {data: []};
    $scope.selectAllBrandMap = function () {
        brandService.selectAllBrandMap().success(function (response) {
            $scope.brandList = {data: response};
        })
    }
    // 用于回显规格集合
    $scope.specList = {data: []};
    $scope.selectAllSpecificationMap = function () {
        specificationService.selectAllSpecificationMap().success(function (response) {
            $scope.specList = {data: response}
        })
    }

    $scope.addOption = function () {
        $scope.entity.customAttributeItems.push({});
    }

    $scope.deleteOption = function (index) {
        $scope.entity.customAttributeItems.splice(index, 1);
    }

    $scope.saveTypeTemplate = function () {
        var method;
        if ($scope.entity.id != null) {
            method = typeTemplateService.updateTypeTemplate($scope.entity)
        } else {
            method = typeTemplateService.insertTypeTemplate($scope.entity)
        }
        method.success(function (response) {
            if (response.flag) {
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        })
    }

    $scope.ids = [];
    $scope.selectIds = function (event, id) {
        if (event.target.checked) {
            $scope.ids.push(id);
        } else {
            var index = $scope.ids.indexOf(id);
            $scope.ids.splice(index,1);
        }
    }

    $scope.deleteTypeTemplateByIds = function () {
        typeTemplateService.deleteTypeTemplateByIds($scope.ids).success(function (response) {
            if (response.flag) {
                $scope.reloadList();
                $scope.ids = [];
            } else {
                alert(response.message);
            }
        })
    }
});