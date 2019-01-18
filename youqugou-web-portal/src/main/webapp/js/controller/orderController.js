app.controller('orderController',function($scope,$location,orderService,loginService){

    //搜索
    $scope.pageNo=1;
    $scope.pageSize=2;
    $scope.search=function(s){
        orderService.search(s,$scope.pageSize,$scope.pageNo).success(
            function(response){
                $scope.orderList=response.rows;
                //计算总页数
                $scope.totalPages=Math.ceil(response.total*1.0/$scope.pageSize);
                buildPageLabel();//构建分页栏
            }
        );
    };

    //订单状态 1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价  张静
    $scope.status = ["","未支付", "已支付", "未发货", "已发货","交易成功","交易关闭","待评价"];


    //根据订单id,查询指定订单  张静
    $scope.selectOrderItemById = function(){
        var id = $location.search()['id'];
        orderService.selectOrderItemById(id).success(
            function (response) {
                $scope.entity=response;
            }
        )
    }
    //构建分页栏
    buildPageLabel=function(){
        //构建分页栏
        $scope.pageLabel=[];

        var firstPage=1;//开始页码
        var lastPage=$scope.totalPages;//截止页码
        $scope.firstDot=true;//前面有点
        $scope.lastDot=true;//后边有点

        if($scope.totalPages>5){  //如果页码数量大于5

            if($scope.pageNo<=3){//如果当前页码小于等于3 ，显示前5页
                lastPage=5;
                $scope.firstDot=false;//前面没点
            }else if( $scope.pageNo>= $scope.totalPages-2 ){//显示后5页
                firstPage=$scope.totalPages-4;
                $scope.lastDot=false;//后边没点
            }else{  //显示以当前页为中心的5页
                firstPage=$scope.pageNo-2;
                lastPage=parseInt($scope.pageNo)+2;
            }
        }else{
            $scope.firstDot=false;//前面无点
            $scope.lastDot=false;//后边无点
        }


        //构建页码
        for(var i=firstPage;i<=lastPage;i++){
            $scope.pageLabel.push(i);
        }
    }

    //分页查询
    $scope.queryByPage=function(pageNo,s){
        if(pageNo<1 || pageNo>$scope.totalPages){
            return ;
        }
        $scope.pageNo=pageNo;
        $scope.search(s);//查询
    }

    //判断当前页是否为第一页
    $scope.isTopPage=function(){
        if($scope.pageNo==1){
            return true;
        }else{
            return false;
        }
    }

    //判断当前页是否为最后一页
    $scope.isEndPage=function(){
        if($scope.pageNo==$scope.totalPages){
            return true;
        }else{
            return false;
        }
    }

    //获取当前登录人
    $scope.loginInfo = function(){
        loginService.loginInfo().success(function(response){
            $scope.username = response.username;
            $scope.loginTime = response.loginTime;
        });
    }

    //去支付 张静 12月29日
    $scope.toPay=function (id) {
        orderService.toPay(id).success(
            function (response) {
                if (response.flag) {
                    //页面跳转
                    location.href = "pay.html";
                } else {
                    alert(response.message);	//也可以跳转到提示页面
                }
            }
        )
    }

});