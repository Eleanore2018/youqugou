//首页控制器
/**
 * 马超  2019.1.2
 * @param entity
 * @returns {*|void}
 */
app.controller("addressController", function ($scope, addressService) {

    //回显
    $scope.loginInfo = function () {
        addressService.loginInfo().success(
            function (response) {
                $scope.addressList = response;
            }
        )

    }

    // //新增地址
    // $scope.addAddress = function () {
    //     addressService.addAddress($scope.entity).success(
    //         function (response) {
    //             alert(response.message);
    //             location.reload();
    //         }
    //     );
    // }

    //保存
    $scope.save=function(){
        var serviceObject;//服务层对象
        if($scope.entity.id!=null){//如果有ID
            serviceObject=addressService.update( $scope.entity ); //修改
        }else{
            serviceObject=addressService.add( $scope.entity  );//增加
        }
        serviceObject.success(
            function(response){
                if(response.flag){
                    //重新查询
                    location.reload();//重新加载
                }else{
                    alert(response.message);
                }
            }
        );
    }

    $scope.deleteAddress = function (id) {

        addressService.deleteAddress(id).success(
            function (response) {
                alert(response.message)
                location.reload();

            }
        )
    }

    $scope.findOne=function(id){
        addressService.findOne(id).success(function (response) {
            $scope.entity=response;
            $("#c1").val(response.provinceId);
        })
    }

   /* $scope.findList=function(){
        addressService.findList().success(function (response) {
            $scope.list1=response;
        })
    }

// 查询二级分类列表:
    $scope.$watch("entity.provinceId", function (newValue, oldValue) {
        addressService.findList2(newValue).success(function (response) {
            alert(newValue);
            $scope.list2 = response;
        });
    });

// 查询三级分类列表:
    $scope.$watch("entity.cityId", function (newValue, oldValue) {
        addressService.findList3(newValue).success(function (response) {
            $scope.list3 = response;
        });
    });*/



});