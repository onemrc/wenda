layui.use('element', function(){
    var element = layui.element;

    //一些事件监听
    element.on('tab(demo)', function(data){

    });
});


$('#myFollows').click(function () {
    $.ajax({
        type:"GET",
        url:"recommendFollow",
        dataType:"json",
        success:function (data) {
            console.info(data);
            var recommendFollows = data.data;

            var dataInit = function () {
                for (var i in recommendFollows){
                    console.info(recommendFollows[i].name);
                    console.info(recommendFollows[i].introduction);

                    var row = $("#recommendFollow").clone();
                    row.find("#name").text(recommendFollows[i].name);
                    row.find("#answerCount").text(recommendFollows[i].answerCount+" 回答");
                    row.find("#introduction").text(recommendFollows[i].introduction);
                    row.find("#likeCount").text(recommendFollows[i].likeCount+" 赞同");
                    row.find("#followeeCount").text(recommendFollows[i].followeeCount+" 关注者");
                    row.find("#questionCount").text(recommendFollows[i].questionCount+" 提问");
                    row.attr("id","ready"+i);

                    row.appendTo("#recommendList");
                    $("#ready"+i).removeClass("hidden");
                }

            };

            dataInit();
        }
    })
});

$('#news').click(function () {
    $.ajax({
        type:"GET",
        url:"news",
        dataType:"json",
        success:function (data){
            var datas = data.data;
            var dataInit = function (){
                for (var i in datas){
                    var row = $("#questions").clone();
                    row.find("#commentCount").text(datas[i].commentCount);
                    row.find("#lookCount").text(datas[i].lookCount);
                    row.find("#createTime").text(datas[i].createTime ?String('yyyy.MM.dd'));
                }
            }
        }
    });
});



var addQuestion = function(){
    layui.use('layer', function(){
        layer.open({
            type:1,
            title:'发布问题',
            content:  $('#addQuestion'),
            // content: 'http://sentsin.com'


            area: ['700px', '400px'],
            offset: '100px'
        });
    });
};


var login = function(){
    layui.use('layer', function(){
        layer.open({
            type:1,
            title:'登录',
            content:  $('#login'),
            // content: 'http://sentsin.com'


            area: ['550px', '270px'],
            offset: '100px'
        });
    });
};


layui.use('form', function(){
    var form = layui.form;


//各种基于事件的操作，下面会有进一步介绍


    form.verify({
        username: function(value, item){ //value：表单的值、item：表单的DOM对象
            // if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){
            //   return '用户名不能有特殊字符';
            // }
            // if(/(^\_)|(\__)|(\_+$)/.test(value)){
            //   return '用户名首尾不能出现下划线\'_\'';
            // }
            // if(/^\d+\d+\d$/.test(value)){
            //   return '用户名不能全为数字';
            // }

            if(!new RegExp("/^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/").test(value)){
                if(!new RegExp("^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$").test(value)){
                    return '账号必须是手机号或邮箱号';
                }
            }
        }

        //我们既支持上述函数式的方式，也支持下述数组的形式
        //数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
        ,pass: [
            /^[\S]{6,12}$/
            ,'密码必须6到12位，且不能出现空格'
        ],affirmPass:function(value){
            var pass =  document.getElementById("pass").value;

            if(value != pass){
                return '两次密码输入不一致';
            }
        }



    });

   form.on('submit(reg)',function (data) {
       console.log(data.value);
       console.log(data.elem);
       $.ajax({
           url:"reg",
           success:console.log('ajax成功'),
           error:console.log('ajax失败')
       })
   })
});


