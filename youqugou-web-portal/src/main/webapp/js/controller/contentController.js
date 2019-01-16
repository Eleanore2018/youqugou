app.controller("contentController",function($scope,contentService){
	$scope.contentList = [];
	// 根据分类ID查询广告的方法:
	$scope.selectContentsByCategoryId = function(categoryId){
		contentService.selectContentsByCategoryId(categoryId).success(function(response){
			$scope.contentList[categoryId] = response;
		});
	}

	//查询楼层广告
	$scope.contentCategory = [7,8,12];
	$scope.contentFloor = [];
	$scope.findFloorContent = function () {
        console.log($scope.contentCategory);
        contentService.findFloorContent($scope.contentCategory).success(function(response){
        	$scope.contentFloor = response;
		});
    }


    //根据父Id'0'查询出所有的分类
    $scope.findAllItemCat = function (parentId) {
        contentService.findAllItemCat(parentId).success(function(response){
            $scope.itemCatList = response;
        });
    }
	
	// 搜索（传递参数）
	$scope.search=function(){
		location.href="http://localhost:9103/search.html#?keywords="+$scope.keywords;
	}
});