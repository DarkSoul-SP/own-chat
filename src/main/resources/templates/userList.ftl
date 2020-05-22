<#import "parts/common.ftl" as c>

<@c.page>
<div style="padding-bottom: 10px; font-size: 28px;">
    List of users
</div>

<table class="user-list-table">
    <thead style="font-size: 24px;">
    <tr>
        <th>Name</th>
        <th>Roles</th>
        <th>Active</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <#list users as user>
        <tr class="user-list-row-item">
            <td style="padding: 5px">${user.username}</td>
            <td style="padding: 5px"><#list user.roles as role >${role}<#sep>, </#list></td>
            <td style="padding: 5px">${user.active?string('Yes', 'No')}</td>
            <td style="width: 50px;"></td>
            <td style="padding: 5px"><a class="user-list-row-item edit-btn" href="/user/${user.id}">Edit</a></td>
        </tr>
    </#list>
    </tbody>
</table>
</@c.page>