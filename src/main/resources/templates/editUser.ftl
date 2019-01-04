<#include "header.ftl">
<link rel="stylesheet" media="all" href="../styles/letter.css">
<#if msg??>
<script>
    alert("${msg!}");
</script>
</#if>
    <div id="main">
        <div class="zg-wrap zu-main clearfix ">
            <div class="container-fluid">
                <div class="row-fluid">
                    <div class="span12">
                        <div class="row-fluid">
                            <div class="span6">
                                <form action="/editUserName/${localUser.userId!}" method="post">
                                    <fieldset>
                                        <legend>用户名修改</legend>
                                        <label>用户名 :</label><input type="text" name="name" value="${localUser.name!}"/>
                                        <button type="submit" class="btn">修改</button>
                                    </fieldset>
                                </form>
                            </div>
                            <div class="span6">
                                <#if proof??>
                                    <legend>身份认证</legend>
                                    <label>已认证：${proof.typeName!}</label><br><br>
                                <#else >
                                    <legend>身份认证</legend>
                                    <form action="/validatorUser/${localUser.userId}" method="post">
                                        <fieldset>
                                            <label>教务处学号</label><input type="text" name="txtUserName"/><br><br>
                                            <label>密码</label><input type="password" name="password"/><br><br>
                                            <label>学生</label><input type="radio" name="proofName" value="学生"/> &nbsp;&nbsp;&nbsp;
                                            <label>教师</label><input type="radio" name="proofName"
                                                                    value="教师"/><br><br>
                                            <label>验证码</label><input type="text" name="txtSecretCode"/> <img
                                                src="/Path/${checkCodePath}"><br><br>
                                            <button type="submit" class="btn">认证</button>
                                        </fieldset>
                                    </form>
                                </#if>
                            </div>
                            <div class="span6">
                                <form action="/editUserPass/${localUser.userId!}" method="post">
                                    <fieldset>
                                        <legend>修改密码</legend>
                                        <label>旧密码</label><input type="password" name="oldPass"/>
                                        <label>新密码</label><input type="password" name="newPass"/>
                                        <button type="submit" class="btn">修改</button>
                                    </fieldset>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


<#include "js.html">
<#include "footer.ftl">

