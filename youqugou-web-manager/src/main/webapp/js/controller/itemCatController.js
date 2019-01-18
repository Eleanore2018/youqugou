//控制层
app.controller('itemCatController', function ($scope, $controller, itemCatService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    $scope.searchEntity = {parentId: 0};
    $scope.search = function (currentPage, itemsPerPage) {
        itemCatService.searchItemCat(currentPage, itemsPerPage, $scope.searchEntity).success(function (response) {
            $scope.paginationConf.totalItems = response.total;
            $scope.pageItemCatList = response.rows;
        })
    }

    /*//读取列表数据绑定到表单中
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    */

    /*//分页
    $scope.findPage = function (page, rows) {
        itemCatService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }*/

    // !!! 找了半个多小时才发现漏了这个
    // select2要求的config格式
    $scope.grandParentList = {data: []};

    // 初始化祖父分类数组
    $scope.selectItemCatsMapByParentId = function () {
        itemCatService.selectItemCatsMapByParentId(0).success(function (response) {
            $scope.grandParentList.data = response;
        })
    }

    $scope.entity = {};
    $scope.parentList = {data: []};
    // 监听grandParentId的变化
    $scope.$watch("grandParentId", function (newValue, oldValue) {
        if (!newValue > 0) {
            // 表示用户想要添加一级分类
            $scope.entity.parentId == 0;
        } else {
            itemCatService.selectItemCatsMapByParentId(newValue).success(function (response) {
                $scope.parentList.data = response;
                $scope.$watch("entity1.id", function () {
                    $scope.parentId = parentId;
                });
            })
        }
    });

    // 监听parentId的变化
    $scope.$watch("parentId", function (newValue, oldValue) {
        // 小于等于0时,按照表示用户想要添加二级分类
        if (newValue <= 0) {
            $scope.entity.parentId = $scope.grandParentId;
        } else {
            $scope.entity.parentId = newValue;
        }
    });

    // 查询模板数据
    $scope.typeTemplateList = {data: []};
    $scope.selectAllTypeTemplatesMap = function () {
        typeTemplateService.selectAllTypeTemplatesMap().success(function (response) {
            $scope.typeTemplateList.data = response;
        })
    }

    // 保存新建/修改的itemCat
    $scope.saveItemCat = function () {
        var method;
        if ($scope.entity.id != null) {
            method = itemCatService.updateItemCat($scope.entity); // 修改
        } else {
            method = itemCatService.insertItemCat($scope.entity); // 增加
        }
        method.success(function (response) {
            if (response.flag) {
                //重新查询
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        })
    }

    // 找了好几个小时没发现问题,由于之前设置了监听器所以将之前的值覆盖了,所以要先定义一个变量
    // 等到监听器生效之后再给$scope.parentId赋值
    // 定义一个参数,没有值时为空,有值时表示parentId的默认值
    var parentId = null;

    //查询实体
    $scope.selectItemCatById = function (id) {
        itemCatService.selectItemCatById(id).success(
            function (response) {
                // 根据传入的id回显数据
                $scope.entity = response;
                // 如果传入的id父id不为0,则说明不是一级目录
                if ($scope.entity.parentId != 0) {
                    itemCatService.selectItemCatById($scope.entity.parentId).success(function (response) {
                        $scope.entity1 = response;
                        // 如果此类目的父id不为0,则说明entity是3级目录
                        if ($scope.entity1.parentId != 0) {
                            // 将此实体的parentId赋值给grandParentId
                            $scope.grandParentId = $scope.entity1.parentId;
                            // 将此实体的id赋值给parentId
                            parentId = $scope.entity1.id;
                        } else {
                            // 此实体父id为0,则说明entity是2级目录
                            $scope.grandParentId = $scope.entity1.id;
                            parentId = 0;
                        }
                    })
                } else {
                    // 如果父id为0,则说明entity是1级目录,没有父目录
                    $scope.grandParentId = 0;
                }
            }
        );
    }

    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        itemCatService.dele($scope.selectIds).success(
            function (response) {
                if (response.flag) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    // 定义一个变量记录当前是第几级分类
    $scope.grade = 1;

    $scope.setGrade = function (value) {
        $scope.grade = value;
    }

    $scope.selectList = function (p_entity) {

        if ($scope.grade == 1) {
            $scope.entity_1 = null;
            $scope.entity_2 = null;
        }
        if ($scope.grade == 2) {
            $scope.entity_1 = p_entity;
            $scope.entity_2 = null;
        }
        if ($scope.grade == 3) {
            $scope.entity_2 = p_entity;
        }

        // 根据父ID查询分类
        $scope.searchEntity.parentId = p_entity.id;
        $scope.reloadList();
    }

});