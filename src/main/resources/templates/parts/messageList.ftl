<#include "security.ftl">
<#import "pager.ftl" as p>

<@p.itemsOnPage page url/>

<div class="card-columns" id="message-list">
<#list page.content as message>
    <div class="card my-3" data-id="${message.id}">
        <#if message.filename??>
            <img src="/img/${message.filename}" class="card-img-top"/>
        </#if>
        <div class="m-2">
             <#if message.text?starts_with("http")>
                <span><a href="${message.text}" target="_blank">${message.text}</span><br/>
             <#else>
                <span>${message.text}</span><br/>
             </#if>
            <i>#${message.tag}</i>
        </div>
        <div class="card-footer text-muted container">
            <div class="row">
                <a class="col align-self-center" href="/user-messages/${message.author.id}"> ${message.authorName}</a>
                <a class="col align-self-center" href="/messages/${message.id}/like">
                    <#if message.meLiked>
                        <i class="fas fa-heart"></i>
                    <#else>
                        <i class="far fa-heart"></i>
                    </#if>
                    ${message.likes}
                </a>
                <#if message.author.id == currentUserId>
                    <a class="col align-self-center" href="/user-messages/${message.author.id}?message=${message.id}">
                            <i class="fas fa-user-edit"></i>
                    </a>
                </#if>
                <#if message.author.id == currentUserId || isAdmin>
                    <a class="col align-self-center" href="/delete-message/${message.author.id}/${message.id}">
                        <i class="fas fa-trash"></i>
                    </a>
                </#if>
            </div>
        </div>
    </div>
<#else>
No message.
</#list>
</div>

<#if itemsCount gt 5>
    <@p.itemsOnPage page url/>
</#if>
<@p.pager page url/>
