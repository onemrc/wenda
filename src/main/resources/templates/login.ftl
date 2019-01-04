<!DOCTYPE html>
<html lang="zh-CN"
      class="is-AppPromotionBarVisible cssanimations csstransforms csstransitions flexbox no-touchevents no-mobile">
<head>
    <title>牛客 - 与世界分享你的知识、经验和见解</title>
    <link rel="dns-prefetch" href="">
    <link rel="stylesheet" href="styles/login.css">
    <link rel="stylesheet" type="text/css" href="../styles/bootstrap.min.css">
    <#if msg??>
        <script>

        </script>
    </#if>
</head>
<body class="zhi  no-auth">
<#if msg??>
    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert">
            &times;
        </a>
        <strong>${msg}</strong>
    </div>
</#if>

<div class="index-main">
    <div class="index-main-body">
        <div class="index-header">
            <h1 class="logo hide-text"><img src="images/res/nk.png" alt=""></h1>
            <h2 class="subtitle">
                凝聚知识的力量
            </h2>
        </div>
        <div class="desk-front sign-flow clearfix sign-flow-simple">
            <div class="view view-signin" data-za-module="SignInForm" style="display: block;">
                <form action="" id="regloginform" method="post">
                    <input type="hidden" name="_xsrf" value="21aa1c8d254df2899b23ab9afbd62a53">
                    <div class="group-inputs">
                        <div class="email input-wrapper">
                            <input type="text" name="str" aria-label="手机号或邮箱" placeholder="手机号或邮箱" required="">
                        </div>
                        <div class="input-wrapper">
                            <input type="password" name="password" aria-label="密码" placeholder="密码" required="">
                        </div>
                    </div>
                    <input type="hidden" name="next" value="${next!}"/>
                    <div class="button-wrapper command clearfix">
                        <button class="sign-button submit" type="submit" onclick="form=document.getElementById('regloginform');form.action='do_login'">登录</button>
                        <button class="sign-button submit" type="submit" onclick="form=document.getElementById('regloginform');form.action='reg'">注册</button>
                    </div>
                    <div class="signin-misc-wrapper clearfix">
                        <label class="remember-me">
                            <input type="checkbox" name="rememberme" checked="" value="true"> 记住我
                        </label>
                        <a class="unable-login" href="#">无法登录?</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="../scripts/main/jquery.js"></script>
<script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>

