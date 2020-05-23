<#macro login path isRegisterForm>
    <form action="${path}" method="post">
        <div class="form-group col-md-6">
            <label> User Name </label>
            <input type="text" name="username" value="<#if user??>${user.username}</#if>"
                   class="form-control ${(usernameError??)?string('is-invalid', '')}"
                   placeholder="Enter username"/>
            <#if usernameError??>
                <div class="invalid-feedback">
                    ${usernameError}
                </div>
            </#if>
        </div>
        <div class="form-group col-md-6">
            <label> Password </label>
            <input type="password" name="password"
                   class="form-control ${(passwordError??)?string('is-invalid', '')}"
                   aria-describedby="passwordHelp" placeholder="Enter password"/>
            <#if passwordError??>
                <div class="invalid-feedback">
                    ${passwordError}
                </div>
            </#if>
            <small id="passwordHelp" class="form-text text-muted">
                We'll never share your password with anyone else.
            </small>
        </div>
        <#if isRegisterForm>
            <div class="form-group col-md-6">
                <label> Confirm password </label>
                <input type="password" name="confirmPassword"
                       class="form-control ${(confirmPasswordError??)?string('is-invalid', '')}"
                       placeholder="Retype password"/>
                <#if confirmPasswordError??>
                    <div class="invalid-feedback">
                        ${confirmPasswordError}
                    </div>
                </#if>
            </div>
            <div class="form-group col-md-6">
                <label> Email </label>
                <input type="email" name="email" value="<#if user??>${user.email}</#if>"
                       class="form-control ${(emailError??)?string('is-invalid', '')}"
                       placeholder="example@ua.com"/>
                <#if emailError??>
                    <div class="invalid-feedback">
                        ${emailError}
                    </div>
                </#if>
            </div>
            <#--<div class="form-group col-sm-6">
                <div class="g-recaptcha" data-sitekey="6Leut5wUAAAAACXNnW7QWAA_f-m5H2z9bmtK2wfR"></div>
                <#if captchaError??>
                    <div class="alert alert-danger col-md-14" role="alert">
                        ${captchaError}
                    </div>
                </#if>
            </div>-->
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#if !isRegisterForm>
            <a href="/registration">Add new user </a>
        </#if>
        <button type="submit" class="btn btn-primary ml-2"><#if isRegisterForm>Create<#else>Sign in</#if></button>
    </form>
</#macro>

<#macro logout>
    <#include "security.ftl">
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary"><#if user??>Sign Out<#else>Log In</#if></button>
    </form>
</#macro>