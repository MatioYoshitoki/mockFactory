/**
 * Created by matioyoshitoki on 2020/2/3.
 */
function checkToken() {
    let token = $.cookie("token");
    if (token!=null){
        $.ajax({
            type: 'post',
            url: "/user/tokenCheck",
            dataType: 'json',
            cache: false,
            withCredential: true,
            success: function (result) {
                if (result.code!=10000){
                    alert(result.msg);
                    $(location).attr('href', '/index');
                }else {
                    $("#avatar").attr('src', result.data.avatar);
                }

                return;
            },
            error: function () {
                alert("服务器开小差了......");
                // $(location).attr('href', '/index');
                return;
            }
        });
    }else {
        $(location).attr('href', '/index');
    }
}

function getManifestList() {
    let token = $.cookie("token");
    
    if (token!=null){
        $('#loadBoard').show();
        $.ajax({
            type: 'get',
            url: "/mock/getManifestList",
            dataType: 'json',
            cache: false,
            withCredential: true,
            success: function (result) {
                if (result.code==10000){
                    let manifestList = result.data;
                    let i=0;

                    for (let i=0;i<manifestList.length;i++){
                        let outBox;
                        let lineCount=5;
                        
                        if (i==0){
                            outBox=$('#firstFlexBox');
                        }else {
                            outBox=$('<div class="flexBox"></div>');
                            $('#scrollBox').append(outBox);
                            lineCount=6;
                        }

                        for (let j=0;j<lineCount;j++){

                            if (manifestList[i]==null){
                                break;
                            }
                            let manifestName=manifestList[i].manifestName;
                            let manifestID=manifestList[i].manifestID;
                            $('#'+manifestID).remove();
                            outBox.append(
                                '<div id="'+manifestID+'" class="addBox">'+
                                '<div class="boxBoard">'+
                                '<img class="excelImage" src="image/excel.png" alt="" title="">'+
                                '<div class="fourDColor">'+manifestName+'</div>'+
                                '</div>'+
                                '</div>'
                            );

                            $("#"+manifestID).on("click",function(){
                                $(location).attr('href', '/interfaceList?manifestID='+manifestID+"&manifestName="+manifestName);
                            });

                            if (j<(lineCount-1)){
                                i++;
                            }

                        }
                    }
                }
                $("#loadBoard").slideUp(300, function () {
                    $("#loadBoard").hide();
                });

                return;
            },
            error: function () {
                $('#loadBoard').hide();
                return;
            }
        });
    }else {
        $(location).attr('href', '/index');
    }
}
function getInterfaceList(manifestID) {
    let token = $.cookie("token");
    let param = {"manifestID":manifestID};
    if (token!=null){
        $.ajax({
            type: 'get',
            url: "/mock/getInterfaceList",
            dataType: 'json',
            cache: false,
            data: param,
            withCredential: true,
            success: function (result) {
                if (result.code==10000){

                    let data = result.data;
                    $(function(){

                        $("#J_TbData").empty();

                        for( var i = 0; i < data.length; i++ ) {
                            //动态创建一个tr行标签,并且转换成jQuery对象
                            var $trTemp = $("<tr></tr>");

                            //往行里面追加 td单元格
                            $trTemp.append("<td>"+ (i+1) +"</td>");
                            $trTemp.append("<td>"+data[i].interfaceName+"</td>>");
                            $trTemp.append("<td>"+ data[i].interfaceType +"</td>");
                            $trTemp.append("<td>"+ data[i].requestMap +"</td>");
                            $trTemp.append("<td><a href='detail?manifestID="+manifestID+"&interfaceID="+data[i].interfaceID+"&interfaceName="+data[i].interfaceName+"'>详情</a></td>")
                            // $("#J_TbData").append($trTemp);
                            $trTemp.appendTo("#J_TbData");
                        }
                    });
                }

                return;
            },
            error: function () {
                alert("服务器开小差了......");
                $(location).attr('href', '/index');
                return;
            }
        });
    }else {
        $(location).attr('href', '/index');
    }
}


function getInterfaceDetail(manifestID, interfaceID) {
    let token = $.cookie("token");
    let param = {"manifestID":manifestID,"interfaceID":interfaceID};
    if (token!=null){
        $.ajax({
            type: 'get',
            url: "/mock/getInterfaceDetail",
            dataType: 'json',
            cache: false,
            data: param,
            withCredential: true,
            success: function (result) {
                if (result.code==10000){


                    $(function(){
                        let data = result.data.interfaceParamsList;
                        $("#J_TbData").empty();

                        for( var i = 0; i < data.length; i++ ) {
                            //动态创建一个tr行标签,并且转换成jQuery对象
                            var $trTemp = $("<tr></tr>");

                            //往行里面追加 td单元格
                            $trTemp.append("<td>"+ (i+1) +"</td>");
                            $trTemp.append("<td>"+data[i].paramName+"</td>>");
                            $trTemp.append("<td>"+ data[i].paramType +"</td>");
                            $trTemp.append("<td>"+ data[i].checkNull +"</td>");
                            // $("#J_TbData").append($trTemp);
                            $trTemp.appendTo("#J_TbData");
                        }
                    });

                    $(function(){
                        let data = result.data.returnList;
                        $("#J_TbData_2").empty();

                        for( var i = 0; i < data.length; i++ ) {
                            //动态创建一个tr行标签,并且转换成jQuery对象
                            var $trTemp = $("<tr></tr>");

                            //往行里面追加 td单元格
                            $trTemp.append("<td>"+ (i+1) +"</td>");
                            $trTemp.append("<td>"+data[i].viewMessage+"</td>>");
                            $trTemp.append("<td id='viewContent_"+i+"'></td>");
                            // $("#J_TbData").append($trTemp);
                            $trTemp.appendTo("#J_TbData_2");
                            $("#viewContent_"+i).JSONView(jQuery.parseJSON(data[i].viewContent));
                        }
                    });
                }

                return;
            },
            error: function () {
                alert("服务器开小差了......");
                $(location).attr('href', '/index');
                return;
            }
        });
    }else {
        $(location).attr('href', '/index');
    }
}

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg); //获取url中"?"符后的字符串并正则匹配
    var context = "";
    if (r != null)
        context = r[2];
    reg = null;
    r = null;
    return context == null || context == "" || context == "undefined" ? "" : context;
}

function showLoginWindow() {
    $("#tipWindow").show();
    $("#fog").show();
}


function showRegisterWindow() {
    let token = $.cookie("token");
    $("#registerWindow").show();
    $("#fog").show();
}

function login() {
    let param = {
        'uID':$("#userID").val(),
        'pwd':$("#password").val()
    }
    $.ajax({
        type: 'post',
        url: "/user/login",
        data: param,
        dataType: 'json',
        cache: false,
        withCredential: true,
        success: function (result) {
            if (result.code==10000){

                $.cookie("token", result.data.token, { expires: 7 });
                localStorage.setItem("showName", result.data.showName);
                localStorage.setItem("userID", result.data.userID);
                localStorage.setItem("avatar", result.data.avatar);

                $(location).attr('href', '/homePage');
            }else {
                alert(result.msg);
            }

            return;
        },
        error: function () {
            alert("服务器开小差了......");
            return;
        }
    });
}

function send() {
    let param = {
        'phoneNo':$("#phoneNo").val()
    };
    $.ajax({
        type: 'get',
        url: "/message/send",
        data: param,
        dataType: 'json',
        cache: false,
        withCredential: false,
        success: function (result) {
            if (result.code==10000){
                alert("发送成功!");
            }else {
                alert(result.msg);
            }
            return;
        },
        error: function () {
            alert("服务器开小差了......");
            return;
        }
    });
}

function register() {
    let param = {
        'phoneNo':$("#phoneNo").val(),
        'checkCode':$("#checkCode").val(),
        'showName':$("#showName").val(),
        'pwd':$("#pwd").val(),
        'pwdRep':$("#pwdRep").val()
    };
    $.ajax({
        type: 'post',
        url: "/user/register",
        data: param,
        dataType: 'json',
        cache: false,
        withCredential: false,
        success: function (result) {
            if (result.code==10000){
                $.cookie("token", result.data.token, { expires: 7 });
                localStorage.setItem("showName", result.data.showName);
                localStorage.setItem("userID", result.data.userID);
                localStorage.setItem("avatar", result.data.avatar);

                $(location).attr('href', '/homePage');
            }else {
                alert(result.msg);
            }
            return;
        },
        error: function () {
            alert("服务器开小差了......");
            return;
        }
    });
}
function initUpload() {
    $("#addBox").html("");
    new FileUpload("#addBox", {
        callback: function (files) {
            //回调函数，可以传递给后台等等

            let file = files[0];
            console.log(file.name);
            localStorage.setItem("uploadFile", null);
            console.log(file.name);

            let fileType = file.name.substr(file.name.indexOf(".") + 1);

            switch (fileType) {
                case "png":
                    $("#fileImage").attr("src", "image/image.png");
                    break;
                case "jpg":
                    $("#fileImage").attr("src", "image/image.png");
                    break;
                case "xls":
                    break;
                case "xlsx":
                    break;
                case "txt":
                    $("#fileImage").attr("src", "image/txt.png");
                    break;
                case "doc":
                    $("#fileImage").attr("src", "image/word.png");
                    break;
                case "docx":
                    $("#fileImage").attr("src", "image/word.png");
                    break;
                case "pdf":
                    $("#fileImage").attr("src", "image/pdf.png");
                    break;
                default:
                    $("#fileImage").attr("src", "image/unknow.png");
                    break;
            }
            $("#fileName").text(file.name);
            $("#tipWindow").show();
            $("#fog").show();


            $("#submit").one("click", function () {

                var reader = new FileReader();
                reader.readAsDataURL(file); //读出 base64
                reader.onloadend = function () {
                    // 图片的base64值
                    var data_64 = reader.result.substring(reader.result.indexOf(",") + 1);
                    console.log(data_64)
                    var formData = {
                        "file": data_64,
                        "port": $("#tipPort").val(),
                        "fileName": file.name,
                        "manifestName": $("#tipName").val()
                    };
                    $.ajax({
                        type: 'post',
                        url: "/mock/getByFile", //上传文件的请求路径必须是绝对路劲
                        data: formData,
                        dataType: 'json',
                        cache: false,
                        success: function (data) {
                            if (data.code == 10000) {
                                refresh();
                                initUpload();
                            }
                            closeFog();
                            return;
                        },
                        error: function () {
                            alert("上传失败");
                            closeFog();
                            initUpload()
                            return;
                        }
                    });
                };
            });
//
        }
    })
}

let fileInput = null;
function fileWindow() {
    
    if (!fileInput) {
        //创建临时input元素
        fileInput = document.createElement('input');
        //设置input type为文件类型
        fileInput.type = 'file';
        //设置文件name
        fileInput.name = 'ime-images';
        //允许上传多个文件
        fileInput.multiple = true;
        fileInput.onchange=fileUpload;
    }
    //触发点击input点击事件，弹出选择文件对话框
    fileInput.click();
}

function fileUpload() {
    //回调函数，可以传递给后台等等

    let file = fileInput.files[0];
    console.log(file.name);
    localStorage.setItem("uploadFile", null);
    console.log(file.name);

    let fileType = file.name.substr(file.name.indexOf(".") + 1);

    switch (fileType) {
        case "png":
            $("#fileImage").attr("src", "image/image.png");
            break;
        case "jpg":
            $("#fileImage").attr("src", "image/image.png");
            break;
        case "xls":
            break;
        case "xlsx":
            break;
        case "txt":
            $("#fileImage").attr("src", "image/txt.png");
            break;
        case "doc":
            $("#fileImage").attr("src", "image/word.png");
            break;
        case "docx":
            $("#fileImage").attr("src", "image/word.png");
            break;
        case "pdf":
            $("#fileImage").attr("src", "image/pdf.png");
            break;
        default:
            $("#fileImage").attr("src", "image/unknow.png");
            break;
    }
    $("#fileName").text(file.name);
    $("#tipWindow").show();
    $("#fog").show();


    $("#submit").one("click", function () {

        var reader = new FileReader();
        reader.readAsDataURL(file); //读出 base64
        reader.onloadend = function () {
            // 图片的base64值
            var data_64 = reader.result.substring(reader.result.indexOf(",") + 1);
            console.log(data_64)
            var formData = {
                "file": data_64,
                "port": $("#tipPort").val(),
                "fileName": file.name,
                "manifestName": $("#tipName").val()
            };
            $.ajax({
                type: 'post',
                url: "/mock/getByFile", //上传文件的请求路径必须是绝对路劲
                data: formData,
                dataType: 'json',
                cache: false,
                success: function (data) {
                    if (data.code == 10000) {
                        refresh();
                        initUpload();

                    }
                    closeFog();
                    fileInput=null;
                    return;
                },
                error: function () {
                    alert("上传失败");
                    closeFog();
                    initUpload()
                    fileInput=null;
                    return;
                }
            });
        };
    });
//
}