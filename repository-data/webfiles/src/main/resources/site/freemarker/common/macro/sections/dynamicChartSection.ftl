<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.DynamicChartSection" -->

<#macro dynamicChartSection section size>
    <@fmt.message key="headers.download-chart-data" var="downloadDataFileHeader" />
    <#local linkText>${downloadDataFileHeader} ${section.title}</#local>
    <#local getData="uk.nhs.digital.freemarker.highcharts.RemoteChartDataFromUrl"?new() />
    <#local chartData =  (getData(section.url))! />

    <div id="chart-${section.uniqueId}-block">
        <figure data-chart="highchart">
            <div id="chart-${section.uniqueId}"
                 style="width:100%; height:${size}px;"></div>
            <span class="attachment">
                <#if chartData??>
                <a data-uipath="ps.publication.chart-section.data-file"
                   title="${linkText}"
                   download="${slugify(section.title)}.csv"
                   href="data:text/plain;base64,${chartData.data}"
                   onClick="logGoogleAnalyticsEvent(
                           'Download chart data','Publication','${slugify(section.title)}'
                           );"
                   onKeyUp="logGoogleAnalyticsEvent(
                           'Download chart data','Publication','${slugify(section.title)}'
                           );">${linkText}</a>
                </#if>
            </span>
        </figure>
    </div>
    <script type="text/javascript" data-chartsource="highchart" data-charttype="chart" data-sectionid="${section.uniqueId}">
        <#if chartData??>
        var results = Papa.parse(atob("${chartData.data}"));
        if(!results.errors.length) {
        </#if>
            window.highchartData${section.uniqueId?remove_beginning("-")} = {
                chart: {
                    type: '${section.type?lower_case}',
                    alignTicks: false,
                },
                <#if section.YTitle?has_content>
                yAxis: {
                    title: {
                        text: "${section.YTitle}"
                    },
                    gridLineDashStyle: 'shortdash',
                    gridLineWidth: 1
                },
                </#if>
                <#if section.XTitle?has_content>
                xAxis: {
                    title: {
                        text: "${section.XTitle}"
                    },
                    gridLineDashStyle: 'shortdash',
                    gridLineWidth: 1
                },
                </#if>
                title: {
                    text: "${section.title}"
                },
                series: [<#list section.seriesItem as item>{
                    type: '${item.type}',
                    name: '${item.name}'
                }<#if item?is_last><#else>, </#if></#list>],
                data: {
                    <#if chartData??>
                    csv: atob("${chartData.data}"),
                    <#else>
                    enablePolling: false,
                    csvURL: "${section.url}",
                    </#if>
                    firstRowAsNames: true
                }
            };
        <#if chartData??>
        } else {
            document.getElementById('chart-${section.uniqueId}-block').innerHTML = "<p><strong>The dynamic chart is unavailable at this time. Please try again soon.</strong></p>"
        }
        </#if>
    </script>
</#macro>
