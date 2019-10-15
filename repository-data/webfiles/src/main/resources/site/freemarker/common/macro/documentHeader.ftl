<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

<#macro documentHeader document doctype icon="" title="" summary="" topics="" hasSchemaOrg=true>

    <#assign custom_title = title />
    <!-- checking whether simulating doc in order to avoid console errors from NewsHub and EventHub docs -->
    <#if ! custom_title?has_content && document != "simulating_doc"  >
      <#assign custom_title = document.title />
    </#if>

    <#assign custom_summary = summary />
    <#assign hasDocumentSummary = false />
    <#if ! custom_summary?has_content && document != "simulating_doc"  >
      <#assign hasDocumentSummary = document.summary?? && document.summary.content?has_content />
      <#if hasDocumentSummary >
        <#assign custom_summary = document.summary />
      </#if>
    </#if>

    <#assign hasPageIcon = icon?has_content />
    <#assign hasTopics = topics?has_content />
    <#assign hasBannerControls = document != "simulating_doc" && document.bannercontrols?? && document.bannercontrols?has_content>

    <#assign headerStyle='' />
    <#if hasBannerControls && document.bannercontrols.backgroundcolor?has_content>
      <#assign headerStyle = 'style=background-color:${document.bannercontrols.backgroundcolor}' />
    </#if>

    <#assign fontColor='' />
    <#if hasBannerControls && document.bannercontrols.fontcolor?has_content>
      <#assign headerStyle = '${headerStyle};color:${document.bannercontrols.fontcolor}' />
    </#if>

    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--with-icon" ${headerStyle}>
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column--two-thirds column--reset">
                            <#if hasSchemaOrg>
                            <h1 id="top" class="local-header__title" data-uipath="document.title" itemprop="name" ${headerStyle}>${custom_title}</h1>
                            <#else>
                              <h1 id="top" class="local-header__title" data-uipath="document.title">${custom_title}</h1>
                            </#if>
                            <#if hasDocumentSummary>
                              <div class="article-header__subtitle" data-uipath="website.${doctype}.summary">
                                <@hst.html hippohtml=custom_summary contentRewriter=gaContentRewriter/>
                              </div>
                            <#else>
                              <#if hasSchemaOrg>
                              <div itemprop="description" class="article-header__subtitle" data-uipath="website.${doctype}.summary">
                              <#else>
                              <div class="article-header__subtitle" data-uipath="website.${doctype}.summary">
                              </#if>
                                <p>${custom_summary}</p>
                              </div>
                            </#if>
                        </div>
                        <#if hasPageIcon>
                            <div class="column--one-third column--reset local-header__icon">
                              <#if hasDocumentSummary> 
                                  <#-- ex. Service case - image from HippoGalleryImageSet -->
                                  <@hst.link hippobean=icon.original fullyQualified=true var="image" />
                                  <#if image?ends_with("svg")>
                                      <#assign colour = 'ffcd60'>
                                      <#if hasBannerControls && document.bannercontrols.iconcolor?has_content>
                                        <#assign colour = document.bannercontrols.iconcolor?replace("#","")>
                                      </#if>
                                      <#assign imageUrl = '${image?replace("/binaries", "/svg-magic/binaries")}' />
                                      <#assign imageUrl += "?colour=${colour}" />
                                      <img src="${imageUrl}" alt="${custom_title}" />
                                  <#else>
                                      <img src="${image}" alt="${custom_title}" />
                                  </#if>
                                <#else>
                                  <#-- ex. Events or News case - image from provided path -->
                                  <img src="<@hst.webfile path="${icon}" fullyQualified=true/>" alt="${custom_title}">
                                </#if>
                            </div>
                        </#if>
                    </div>
                    <#if hasTopics>
                      <div class="detail-list-grid">
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key">Topics:</dt>
                                    <dd class="detail-list__value">
                                      <#if hasSchemaOrg>
                                        <span itemprop="keywords" ><#list document.taxonomyTags as tag>${tag} <#sep>, </#list></span>
                                      <#else>
                                        <span><#list document.taxonomyTags as tag>${tag} <#sep>, </#list></span>
                                      </#if>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                      </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>

</#macro>
