<#macro login path isRegisterForm>
    <form action="${path}" method="post">
        <div class="form-group col-md-6">
            <label> User Name </label>
            <input type="text" name="username" class="form-control" aria-describedby="emailHelp" placeholder="Enter username"/>
        </div>
        <div class="form-group col-md-6">
            <label> Password </label>
            <input type="password" name="password" class="form-control" aria-describedby="passwordHelp" placeholder="Enter password"/>
            <small id="passwordHelp" class="form-text text-muted">We'll never share your password with anyone else.</small>
        </div>
        <#if isRegisterForm>
        <div class="form-group col-md-6">
            <label> Email </label>
            <input type="email" name="email" class="form-control" placeholder="example@ua.com"/>
        </div>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#if !isRegisterForm>
            <a href="/registration">Add new user  </a>
        </#if>
        <button type="submit" class="btn btn-primary"><#if isRegisterForm>Create<#else>Sign in</#if></button>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary">Sign Out</button>
    </form>
</#macro>