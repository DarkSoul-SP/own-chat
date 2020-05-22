<#import "parts/common.ftl" as c>

<@c.page>
<div style="font-size: 28px">User editor</div>

<form class="user-edit-form" action="/user" method="post">
    <label>Username
        <input style="padding: 8px; margin-bottom: 10px; margin-top: 10px; border-radius: 8px;" type="text" value="${user.username}" name="username"/>
    </label>
    <div>Roles</div>
    <#list roles as role>
    <div>
        <label>
            <input style="margin-right: 10px;" type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}/>
            ${role}
        </label>
    </div>
    </#list>
    <div>Account status</div>
    <div>
        <label>
            <input style="margin-right: 10px;" type="checkbox" name="isActive" ${user.active?string("checked", "unchecked")}/>
            Active
        </label>
    </div>
    <input type="hidden" value="${user.id}" name="userId"/>
    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <button type="submit" style="background-color: black; color: white; width: 80px; border-radius: 8px;">Save</button>
</form>
</@c.page>