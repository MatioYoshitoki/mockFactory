$(function () {

    // $("body").click(function () {
    //     $("#sort_by_menu").attr("style","display:none");
    // });

    // $("#sort_by").click(function(){
    //     $("#sort_by_menu").attr("style","display:block");
    // });

    $("#sort_by").off().on("click",function(e){
        $("#sort_by_menu").show(), e.stopPropagation()
    }),


    $("body").on("click",function(){
        $("#sort_by_menu").hide();
    });
    
    $("#fog").on("click",function(){
        closeFog();
        initUpload();
    });
    
    $(".downloadMock").on("click", function () {
        
        var url = "/static/mock/"+getQueryString("manifestID")+"_node.js";
        window.open(encodeURI(url));
    })
    $(".editMock").on("click", function () {

        var url = "/static/mock/"+getQueryString("manifestID")+"_node.js";
        window.open(encodeURI(url));
    })
    $("#history").on("click", function () {
        $("#scrollBox").hide();
        $("#historyScrollBox").show();
    })
    $("#now").on("click", function () {
        $("#historyScrollBox").hide();
        $("#scrollBox").show();

    })

});

function closeFog() {
    $("#fog").fadeOut(500, function(){
        $("#fog").hide();
    });
    $("#tipWindow").fadeOut(300, function () {
        $("#tipWindow").hide();
    });
    $("#registerWindow").fadeOut(300, function () {
        $("#tipWindow").hide();
    });
}


/**
 * 获取检验码
 */
function getCheckCodeTime() {
    curCount = 60;
    //设置button效果，开始计时
    $("#btnSendCode").removeAttr("href");
    $("#btnSendCode").removeAttr("onclick");
    $("#btnSendCode").html("请在" + curCount + "秒内输入验证码");
    InterValObj = window.setInterval(setRemainTime, 1000); //启动计时器，1秒执行一次

}

//timer处理函数
function setRemainTime() {
    if (curCount == 0) {
        window.clearInterval(InterValObj);//停止计时器
        $("#btnSendCode").attr("href","#");//启用按钮
        $("#btnSendCode").attr("onclick","sendCheck()");//启用按钮
        $("#btnSendCode").html("重新发送");
    }
    else {
        curCount--;
        $("#btnSendCode").html("请在" + curCount + "秒内输入验证码");
    }
}