<#import "parts/common.ftl" as c>

<@c.page>
    <h3>${username}</h3>
    <#if message??>
        <div class="alert alert-${messageType} col-md-6" role="alert">
            ${message}
        </div>
    </#if>
    <form method="post">
        <div class="form-group col-md-6">
            <label> Username </label>
            <input type="text" name="username" class="form-control" placeholder="Enter new username"/>
        </div>
        <div class="form-group col-md-6">
            <label> Password </label>
            <input type="password" name="password" class="form-control" aria-describedby="passwordHelp" placeholder="Enter new password"/>
            <small id="passwordHelp" class="form-text text-muted">We'll never share your password with anyone else.</small>
        </div>
        <div class="form-group col-md-6">
            <label> Email </label>
            <input type="email" name="email" class="form-control" placeholder="example@ua.com" value="${email!''}"/>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary">Save</button>
    </form>
</@c.page>