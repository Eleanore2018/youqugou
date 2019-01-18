//首页控制器
/**
 * 马超  2019.1.2
 * @param entity
 * @returns {*|void}
 */
app.controller("selfCenterController", function ($scope, selfCenterService,uploadService) {
    $scope.addSelfCenter = function () {
        selfCenterService.addselfCenter($scope.entity).success(
            function (response) {

                alert(response.message);
            }
        );
    }

    $scope.loginInfo=function () {
        selfCenterService.loginInfo().success(
            function (response){
                $scope.userName=response.username;
            }
        )

    }

    /**
     * 上传图片
     */
    $scope.uploadFile = function(){
        // 调用uploadService的方法完成文件的上传
        uploadService.uploadFile().success(function(response){
            if(response.flag){
                alert("上传成功!");
                //alert(response.message);
                // 获得url
                $scope.entity.user.head_pic =  response.message;
            }else{
                alert(response.message);
            }
        });
    }






});