app.controller('sellerController' ,function($scope, $controller, sellerService){
	
	$controller('baseController',{$scope:$scope});//继承

    $scope.searchEntity = {};
	$scope.search = function (currentPage, itemsPerPage) {
		// console.log($("input[type='radio']:checked").val());
        $scope.searchEntity.status = $("input[type='radio']:checked").val();
		sellerService.searchSeller(currentPage, itemsPerPage, $scope.searchEntity).success(function (response) {
			$scope.paginationConf.totalItems = response.total;
			$scope.pageSellerList = response.rows;
        })
    }

    $scope.selectSellerById = function (sellerId) {
		sellerService.selectSellerById(sellerId).success(function (response) {
			$scope.entity = response;
        })
    }
});	
