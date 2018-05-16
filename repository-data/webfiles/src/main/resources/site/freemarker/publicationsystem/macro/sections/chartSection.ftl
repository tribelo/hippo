<#ftl output_format="HTML">

<#macro chartSection section>
<div id="${section.uniqueId}" style="width:100%; height:400px;"></div>

<script>
    $(function () {
        var myChart = Highcharts.chart('${section.uniqueId}', <#outputformat "plainText">${section.chart}</#outputformat>);
    });
</script>

<!--<div id="container1" style="width:100%; height:400px;"></div>-->

<!--<script>
        $(function() {

    Highcharts.setOptions({
        lang: {
            thousandsSep: ","
        }
    });

  var chart = new Highcharts.Chart({

    chart: {
      renderTo: 'container1',
      type: 'scatter',
    },
    title: {
      text: 'Summary Hospital-level Mortality Indicator (SHMI) - Deaths associated with hospitalisation, England, January 2015 - December 2015'
    },

    xAxis: {
      title: {
        enabled: true,
        align: 'middle',
        style: {"color":"#000000","fontWeight":"bold"},
        text: 'Expected number of deaths'
      },
      labels: {
        style: {"color":"#000000"},
        format: '{value:,.0f}'
			},
      gridLineColor: "#888888",
      gridLineDashStyle: 'shortdash',
      gridLineWidth: 1,
      lineColor: "#000000",
      lineWidth: 1,
      max: 5000,
      min: 0,
      tickColor: "#000000",
      tickInterval: 500,
      tickLength: 5,
      tickPosition: 'outside',
      tickWidth: 1,
      startOnTick: true,
      endOnTick: false,
      showLastLabel: true
    },

    yAxis: {
      title: {
        enabled: true,
        align: 'middle',
        style: {"color":"#000000","fontWeight":"bold"},
        text: 'SHMI Value'
      },
      labels: {
        style: {"color":"#000000"},
        format: '{value:.1f}'
      },
      gridLineColor: "#888888",
      gridLineDashStyle: 'shortdash',
      gridLineWidth: 1,
      lineColor: "#000000",
      lineWidth: 1,
      max: 1.4,
      min: 0.6,
      tickColor: "#000000",
      tickInterval: 0.1,
      tickLength: 5,
			tickPosition: 'outside',
      tickWidth: 1,
      startOnTick: false,
      endOnTick: true,
      showLastLabel: true
    },

    plotOptions: {
      scatter: {
        color: '#000000'				// not sure of the affect of this
        },
      line: {
        color: '#ff0000',
        marker: {
          enabled: false
        }
      }
    },

    series: [{
      type: 'line',
      dashStyle: 'solid',
      name: 'lower limit',
      tooltip: {
           pointFormat: '<br>Expected number of deaths: {point.x}</br><br>SHMI Value: {point.y}</br>',
           headerFormat: '<small><b>{series.name}:</b></small>',
           },
      data: [
{ "x": 50, "y": 0.745460769 },
{ "x": 100, "y": 0.803477512 },
{ "x": 150, "y": 0.829217192 },
{ "x": 200, "y": 0.844239839 },
{ "x": 250, "y": 0.854219609 },
{ "x": 300, "y": 0.861380262 },
{ "x": 350, "y": 0.86679011 },
{ "x": 400, "y": 0.871031928 },
{ "x": 450, "y": 0.874452805 },
{ "x": 500, "y": 0.877273266 },
{ "x": 550, "y": 0.879640573 },
{ "x": 600, "y": 0.881657032 },
{ "x": 650, "y": 0.88339605 },
{ "x": 700, "y": 0.884911732 },
{ "x": 750, "y": 0.88624487 },
{ "x": 800, "y": 0.887426825 },
{ "x": 850, "y": 0.888482119 },
{ "x": 900, "y": 0.889430212 },
{ "x": 950, "y": 0.89028675 },
{ "x": 1000, "y": 0.891064459 },
{ "x": 1050, "y": 0.8917738 },
{ "x": 1100, "y": 0.892423454 },
{ "x": 1150, "y": 0.893020685 },
{ "x": 1200, "y": 0.89357162 },
{ "x": 1250, "y": 0.894081463 },
{ "x": 1300, "y": 0.894554664 },
{ "x": 1350, "y": 0.894995051 },
{ "x": 1400, "y": 0.895405933 },
{ "x": 1450, "y": 0.895790189 },
{ "x": 1500, "y": 0.896150335 },
{ "x": 1550, "y": 0.896488576 },
{ "x": 1600, "y": 0.896806858 },
{ "x": 1650, "y": 0.897106902 },
{ "x": 1700, "y": 0.897390235 },
{ "x": 1750, "y": 0.897658219 },
{ "x": 1800, "y": 0.897912071 },
{ "x": 1850, "y": 0.898152883 },
{ "x": 1900, "y": 0.898381636 },
{ "x": 1950, "y": 0.898599215 },
{ "x": 2000, "y": 0.898806423 },
{ "x": 2050, "y": 0.899003983 },
{ "x": 2100, "y": 0.899192556 },
{ "x": 2150, "y": 0.899372743 },
{ "x": 2200, "y": 0.899545091 },
{ "x": 2250, "y": 0.899710102 },
{ "x": 2300, "y": 0.899868236 },
{ "x": 2350, "y": 0.900019913 },
{ "x": 2400, "y": 0.900165523 },
{ "x": 2450, "y": 0.900305423 },
{ "x": 2500, "y": 0.900439942 },
{ "x": 2550, "y": 0.900569386 },
{ "x": 2600, "y": 0.900694036 },
{ "x": 2650, "y": 0.900814155 },
{ "x": 2700, "y": 0.900929986 },
{ "x": 2750, "y": 0.901041753 },
{ "x": 2800, "y": 0.901149669 },
{ "x": 2850, "y": 0.901253928 },
{ "x": 2900, "y": 0.901354713 },
{ "x": 2950, "y": 0.901452196 },
{ "x": 3000, "y": 0.901546536 },
{ "x": 3050, "y": 0.901637883 },
{ "x": 3100, "y": 0.901726378 },
{ "x": 3150, "y": 0.901812151 },
{ "x": 3200, "y": 0.901895327 },
{ "x": 3250, "y": 0.901976023 },
{ "x": 3300, "y": 0.902054346 },
{ "x": 3350, "y": 0.902130402 },
{ "x": 3400, "y": 0.902204286 },
{ "x": 3450, "y": 0.90227609 },
{ "x": 3500, "y": 0.902345902 },
{ "x": 3550, "y": 0.902413803 },
{ "x": 3600, "y": 0.90247987 },
{ "x": 3650, "y": 0.902544178 },
{ "x": 3700, "y": 0.902606794 },
{ "x": 3750, "y": 0.902667786 },
{ "x": 3800, "y": 0.902727215 },
{ "x": 3850, "y": 0.902785141 },
{ "x": 3900, "y": 0.902841621 },
{ "x": 3950, "y": 0.902896707 },
{ "x": 4000, "y": 0.902950451 },
{ "x": 4050, "y": 0.903002901 },
{ "x": 4100, "y": 0.903054104 },
{ "x": 4150, "y": 0.903104103 },
{ "x": 4200, "y": 0.903152941 },
{ "x": 4250, "y": 0.903200657 },
{ "x": 4300, "y": 0.90324729 },
{ "x": 4350, "y": 0.903292876 },
{ "x": 4400, "y": 0.90333745 },
{ "x": 4450, "y": 0.903381046 },
{ "x": 4500, "y": 0.903423694 },
{ "x": 4550, "y": 0.903465427 },
{ "x": 4600, "y": 0.903506272 },
{ "x": 4650, "y": 0.903546259 },
{ "x": 4700, "y": 0.903585413 },
{ "x": 4750, "y": 0.903623761 },
{ "x": 4800, "y": 0.903661327 },
{ "x": 4850, "y": 0.903698135 },
{ "x": 4900, "y": 0.903734208 },
{ "x": 4950, "y": 0.903769567 },
{ "x": 5000, "y": 0.903804234 }
						]
    }, {
      type: 'line',
      dashStyle: 'solid',
      name: 'upper limit',
      tooltip: {
           pointFormat: '<br>Expected number of deaths: {point.x}</br><br>SHMI Value: {point.y}</br>',
           headerFormat: '<small><b>{series.name}:</b></small>',
           },
      data: [
{ "x": 50, "y": 1.341452215 },
{ "x": 100, "y": 1.244589904 },
{ "x": 150, "y": 1.205956666 },
{ "x": 200, "y": 1.184497526 },
{ "x": 250, "y": 1.170659148 },
{ "x": 300, "y": 1.160927461 },
{ "x": 350, "y": 1.15368183 },
{ "x": 400, "y": 1.148063541 },
{ "x": 450, "y": 1.143572293 },
{ "x": 500, "y": 1.139895673 },
{ "x": 550, "y": 1.136827963 },
{ "x": 600, "y": 1.134227896 },
{ "x": 650, "y": 1.1319951 },
{ "x": 700, "y": 1.130056212 },
{ "x": 750, "y": 1.12835632 },
{ "x": 800, "y": 1.126853473 },
{ "x": 850, "y": 1.125515053 },
{ "x": 900, "y": 1.124315304 },
{ "x": 950, "y": 1.12323361 },
{ "x": 1000, "y": 1.122253267 },
{ "x": 1050, "y": 1.121360596 },
{ "x": 1100, "y": 1.120544284 },
{ "x": 1150, "y": 1.11979489 },
{ "x": 1200, "y": 1.119104476 },
{ "x": 1250, "y": 1.118466316 },
{ "x": 1300, "y": 1.11787467 },
{ "x": 1350, "y": 1.117324615 },
{ "x": 1400, "y": 1.116811899 },
{ "x": 1450, "y": 1.116332833 },
{ "x": 1500, "y": 1.115884201 },
{ "x": 1550, "y": 1.115463182 },
{ "x": 1600, "y": 1.115067297 },
{ "x": 1650, "y": 1.114694355 },
{ "x": 1700, "y": 1.114342413 },
{ "x": 1750, "y": 1.114009741 },
{ "x": 1800, "y": 1.113694795 },
{ "x": 1850, "y": 1.113396193 },
{ "x": 1900, "y": 1.113112691 },
{ "x": 1950, "y": 1.112843171 },
{ "x": 2000, "y": 1.11258662 },
{ "x": 2050, "y": 1.112342124 },
{ "x": 2100, "y": 1.112108851 },
{ "x": 2150, "y": 1.111886043 },
{ "x": 2200, "y": 1.111673011 },
{ "x": 2250, "y": 1.111469125 },
{ "x": 2300, "y": 1.111273807 },
{ "x": 2350, "y": 1.111086527 },
{ "x": 2400, "y": 1.110906799 },
{ "x": 2450, "y": 1.110734174 },
{ "x": 2500, "y": 1.110568238 },
{ "x": 2550, "y": 1.11040861 },
{ "x": 2600, "y": 1.110254936 },
{ "x": 2650, "y": 1.110106889 },
{ "x": 2700, "y": 1.109964166 },
{ "x": 2750, "y": 1.109826483 },
{ "x": 2800, "y": 1.109693578 },
{ "x": 2850, "y": 1.109565206 },
{ "x": 2900, "y": 1.10944114 },
{ "x": 2950, "y": 1.109321165 },
{ "x": 3000, "y": 1.109205083 },
{ "x": 3050, "y": 1.109092706 },
{ "x": 3100, "y": 1.108983861 },
{ "x": 3150, "y": 1.108878383 },
{ "x": 3200, "y": 1.108776118 },
{ "x": 3250, "y": 1.108676921 },
{ "x": 3300, "y": 1.108580657 },
{ "x": 3350, "y": 1.108487197 },
{ "x": 3400, "y": 1.10839642 },
{ "x": 3450, "y": 1.108308211 },
{ "x": 3500, "y": 1.108222465 },
{ "x": 3550, "y": 1.108139078 },
{ "x": 3600, "y": 1.108057955 },
{ "x": 3650, "y": 1.107979005 },
{ "x": 3700, "y": 1.107902141 },
{ "x": 3750, "y": 1.107827282 },
{ "x": 3800, "y": 1.107754351 },
{ "x": 3850, "y": 1.107683273 },
{ "x": 3900, "y": 1.107613979 },
{ "x": 3950, "y": 1.107546403 },
{ "x": 4000, "y": 1.107480481 },
{ "x": 4050, "y": 1.107416154 },
{ "x": 4100, "y": 1.107353364 },
{ "x": 4150, "y": 1.107292057 },
{ "x": 4200, "y": 1.10723218 },
{ "x": 4250, "y": 1.107173685 },
{ "x": 4300, "y": 1.107116524 },
{ "x": 4350, "y": 1.107060652 },
{ "x": 4400, "y": 1.107006025 },
{ "x": 4450, "y": 1.106952603 },
{ "x": 4500, "y": 1.106900346 },
{ "x": 4550, "y": 1.106849217 },
{ "x": 4600, "y": 1.106799179 },
{ "x": 4650, "y": 1.106750197 },
{ "x": 4700, "y": 1.106702239 },
{ "x": 4750, "y": 1.106655273 },
{ "x": 4800, "y": 1.106609268 },
{ "x": 4850, "y": 1.106564195 },
{ "x": 4900, "y": 1.106520027 },
{ "x": 4950, "y": 1.106476735 },
{ "x": 5000, "y": 1.106434295 }
						]
    }, {
      type: 'scatter',
      name: 'Organisation',
//      allowPointSelect: true,
      marker: { fillColor: '#0000FF', radius: 3, symbol: 'circle' },
      tooltip: {
           pointFormat: '<br>{point.org}</br><br>SHMI Value: {point.y}</br><br>Expected number of deaths: {point.x}</br>',
           headerFormat: '<small><b>{series.name}</b></small>',
           },
      data: [
{ "org": "R1F - ISLE OF WIGHT NHS TRUST", "x": 830.6115, "y": 0.9944 },
{ "org": "R1H - BARTS HEALTH NHS TRUST", "x": 4246.4576, "y": 0.9003 },
{ "org": "R1K - LONDON NORTH WEST HEALTHCARE NHS TRUST", "x": 2761.7753, "y": 0.903 },
{ "org": "RA2 - ROYAL SURREY COUNTY HOSPITAL NHS FOUNDATION TRUST", "x": 1528.2909, "y": 0.8637 },
{ "org": "RA3 - WESTON AREA HEALTH NHS TRUST", "x": 878.8538, "y": 1.1674 },
{ "org": "RA4 - YEOVIL DISTRICT HOSPITAL NHS FOUNDATION TRUST", "x": 944.6292, "y": 1.0184 },
{ "org": "RA7 - UNIVERSITY HOSPITALS BRISTOL NHS FOUNDATION TRUST", "x": 1760.7501, "y": 0.9774 },
{ "org": "RA9 - TORBAY AND SOUTH DEVON NHS FOUNDATION TRUST", "x": 2243.5662, "y": 0.8219 },
{ "org": "RAE - BRADFORD TEACHING HOSPITALS NHS FOUNDATION TRUST", "x": 1854.2204, "y": 0.9465 },
{ "org": "RAJ - SOUTHEND UNIVERSITY HOSPITAL NHS FOUNDATION TRUST", "x": 2019.8495, "y": 1.1516 },
{ "org": "RAL - ROYAL FREE LONDON NHS FOUNDATION TRUST", "x": 3187.8794, "y": 0.8852 },
{ "org": "RAP - NORTH MIDDLESEX UNIVERSITY HOSPITAL NHS TRUST", "x": 1474.5803, "y": 0.9494 },
{ "org": "RAS - THE HILLINGDON HOSPITALS NHS FOUNDATION TRUST", "x": 1125.4802, "y": 0.8805 },
{ "org": "RAX - KINGSTON HOSPITAL NHS FOUNDATION TRUST", "x": 1361.3054, "y": 0.9263 },
{ "org": "RBA - TAUNTON AND SOMERSET NHS FOUNDATION TRUST", "x": 1675.0603, "y": 1 },
{ "org": "RBD - DORSET COUNTY HOSPITAL NHS FOUNDATION TRUST", "x": 1007.5317, "y": 1.1493 },
{ "org": "RBK - WALSALL HEALTHCARE NHS TRUST", "x": 1502.9075, "y": 1.046 },
{ "org": "RBL - WIRRAL UNIVERSITY TEACHING HOSPITAL NHS FOUNDATION TRUST", "x": 2310.366, "y": 0.9834 },
{ "org": "RBN - ST HELENS AND KNOWSLEY HOSPITAL SERVICES NHS TRUST", "x": 2153.239, "y": 1.0338 },
{ "org": "RBT - MID CHESHIRE HOSPITALS NHS FOUNDATION TRUST", "x": 1491.1944, "y": 0.9684 },
{ "org": "RBZ - NORTHERN DEVON HEALTHCARE NHS TRUST", "x": 1233.5477, "y": 1.0085 },
{ "org": "RC1 - BEDFORD HOSPITAL NHS TRUST", "x": 1176.3889, "y": 0.9937 },
{ "org": "RC9 - LUTON AND DUNSTABLE UNIVERSITY HOSPITAL NHS FOUNDATION TRUST", "x": 1534.6017, "y": 1.1032 },
{ "org": "RCB - YORK TEACHING HOSPITAL NHS FOUNDATION TRUST", "x": 3201.407, "y": 0.9949 },
{ "org": "RCD - HARROGATE AND DISTRICT NHS FOUNDATION TRUST", "x": 980.6529, "y": 0.9392 },
{ "org": "RCF - AIREDALE NHS FOUNDATION TRUST", "x": 1091.0562, "y": 0.9074 },
{ "org": "RCX - THE QUEEN ELIZABETH HOSPITAL, KING'S LYNN, NHS FOUNDATION TRUST", "x": 1657.9817, "y": 0.9107 },
{ "org": "RD1 - ROYAL UNITED HOSPITALS BATH NHS FOUNDATION TRUST", "x": 2035.7773, "y": 0.9741 },
{ "org": "RD3 - POOLE HOSPITAL NHS FOUNDATION TRUST", "x": 1706.3226, "y": 0.9172 },
{ "org": "RD8 - MILTON KEYNES UNIVERSITY HOSPITAL NHS FOUNDATION TRUST", "x": 1206.5009, "y": 1.0261 },
{ "org": "RDD - BASILDON AND THURROCK UNIVERSITY HOSPITALS NHS FOUNDATION TRUST", "x": 2175.9406, "y": 0.9421 },
{ "org": "RDE - COLCHESTER HOSPITAL UNIVERSITY NHS FOUNDATION TRUST", "x": 2061.9251, "y": 1.0694 },
{ "org": "RDU - FRIMLEY HEALTH NHS FOUNDATION TRUST", "x": 3709.5878, "y": 0.9341 },
{ "org": "RDZ - THE ROYAL BOURNEMOUTH AND CHRISTCHURCH HOSPITALS NHS FOUNDATION TRUST", "x": 2108.0455, "y": 0.9843 },
{ "org": "RE9 - SOUTH TYNESIDE NHS FOUNDATION TRUST", "x": 1010.701, "y": 1.1556 },
{ "org": "REF - ROYAL CORNWALL HOSPITALS NHS TRUST", "x": 2239.0289, "y": 1.0857 },
{ "org": "REM - AINTREE UNIVERSITY HOSPITAL NHS FOUNDATION TRUST", "x": 1909.5469, "y": 1.0364 },
{ "org": "RF4 - BARKING, HAVERING AND REDBRIDGE UNIVERSITY HOSPITALS NHS TRUST", "x": 3271.5938, "y": 0.9598 },
{ "org": "RFF - BARNSLEY HOSPITAL NHS FOUNDATION TRUST", "x": 1457.8961, "y": 0.9857 },
{ "org": "RFR - THE ROTHERHAM NHS FOUNDATION TRUST", "x": 1413.1714, "y": 1.0501 },
{ "org": "RFS - CHESTERFIELD ROYAL HOSPITAL NHS FOUNDATION TRUST", "x": 1821.3989, "y": 0.9844 },
{ "org": "RGN - PETERBOROUGH AND STAMFORD HOSPITALS NHS FOUNDATION TRUST", "x": 1813.4803, "y": 1.0455 },
{ "org": "RGP - JAMES PAGET UNIVERSITY HOSPITALS NHS FOUNDATION TRUST", "x": 1312.5635, "y": 1.0895 },
{ "org": "RGQ - IPSWICH HOSPITAL NHS TRUST", "x": 2013.1192, "y": 0.9811 },
{ "org": "RGR - WEST SUFFOLK NHS FOUNDATION TRUST", "x": 1552.8135, "y": 0.8765 },
{ "org": "RGT - CAMBRIDGE UNIVERSITY HOSPITALS NHS FOUNDATION TRUST", "x": 2721.4045, "y": 0.8363 },
{ "org": "RH8 - ROYAL DEVON AND EXETER NHS FOUNDATION TRUST", "x": 1954.198, "y": 0.9815 },
{ "org": "RHM - UNIVERSITY HOSPITAL SOUTHAMPTON NHS FOUNDATION TRUST", "x": 2860.351, "y": 0.9509 },
{ "org": "RHQ - SHEFFIELD TEACHING HOSPITALS NHS FOUNDATION TRUST", "x": 3779.9939, "y": 0.9362 },
{ "org": "RHU - PORTSMOUTH HOSPITALS NHS TRUST", "x": 2850.3511, "y": 1.0711 },
{ "org": "RHW - ROYAL BERKSHIRE NHS FOUNDATION TRUST", "x": 2138.1084, "y": 0.9457 },
{ "org": "RJ1 - GUY'S AND ST THOMAS' NHS FOUNDATION TRUST", "x": 1981.499, "y": 0.7424 },
{ "org": "RJ2 - LEWISHAM AND GREENWICH NHS TRUST", "x": 2367.5164, "y": 1.0129 },
{ "org": "RJ6 - CROYDON HEALTH SERVICES NHS TRUST", "x": 1310.6448, "y": 0.9858 },
{ "org": "RJ7 - ST GEORGE'S UNIVERSITY HOSPITALS NHS FOUNDATION TRUST", "x": 2328.7129, "y": 0.9057 },
{ "org": "RJC - SOUTH WARWICKSHIRE NHS FOUNDATION TRUST", "x": 1085.4923, "y": 1.0548 },
{ "org": "RJE - UNIVERSITY HOSPITALS OF NORTH MIDLANDS NHS TRUST", "x": 4238.1553, "y": 1.033 },
{ "org": "RJF - BURTON HOSPITALS NHS FOUNDATION TRUST", "x": 1458.6026, "y": 0.9653 },
{ "org": "RJL - NORTHERN LINCOLNSHIRE AND GOOLE NHS FOUNDATION TRUST", "x": 2157.1445, "y": 1.0755 },
{ "org": "RJN - EAST CHESHIRE NHS TRUST", "x": 888.2222, "y": 1.0279 },
{ "org": "RJR - COUNTESS OF CHESTER HOSPITAL NHS FOUNDATION TRUST", "x": 1409.7091, "y": 1.0527 },
{ "org": "RJZ - KING'S COLLEGE HOSPITAL NHS FOUNDATION TRUST", "x": 3410.3128, "y": 0.8709 },
{ "org": "RK5 - SHERWOOD FOREST HOSPITALS NHS FOUNDATION TRUST", "x": 1953.6944, "y": 0.9909 },
{ "org": "RK9 - PLYMOUTH HOSPITALS NHS TRUST", "x": 2396.6439, "y": 1.0047 },
{ "org": "RKB - UNIVERSITY HOSPITALS COVENTRY AND WARWICKSHIRE NHS TRUST", "x": 2676.2994, "y": 1.0627 },
{ "org": "RKE - THE WHITTINGTON HOSPITAL NHS TRUST", "x": 783.4544, "y": 0.6688 },
{ "org": "RL4 - THE ROYAL WOLVERHAMPTON NHS TRUST", "x": 2424.2207, "y": 1.0395 },
{ "org": "RLN - CITY HOSPITALS SUNDERLAND NHS FOUNDATION TRUST", "x": 2100.329, "y": 0.976 },
{ "org": "RLQ - WYE VALLEY NHS TRUST", "x": 986.1064, "y": 1.152 },
{ "org": "RLT - GEORGE ELIOT HOSPITAL NHS TRUST", "x": 927.3026, "y": 1.1506 },
{ "org": "RM1 - NORFOLK AND NORWICH UNIVERSITY HOSPITALS NHS FOUNDATION TRUST", "x": 3296.8726, "y": 1.0401 },
{ "org": "RM2 - UNIVERSITY HOSPITAL OF SOUTH MANCHESTER NHS FOUNDATION TRUST", "x": 1945.1951, "y": 0.9793 },
{ "org": "RM3 - SALFORD ROYAL NHS FOUNDATION TRUST", "x": 1929.2217, "y": 0.8962 },
{ "org": "RMC - BOLTON NHS FOUNDATION TRUST", "x": 1688.0904, "y": 1.045 },
{ "org": "RMP - TAMESIDE HOSPITAL NHS FOUNDATION TRUST", "x": 1233.8004, "y": 1.1493 },
{ "org": "RN3 - GREAT WESTERN HOSPITALS NHS FOUNDATION TRUST", "x": 1880.3222, "y": 0.9466 },
{ "org": "RN5 - HAMPSHIRE HOSPITALS NHS FOUNDATION TRUST", "x": 1799.0206, "y": 1.1067 },
{ "org": "RN7 - DARTFORD AND GRAVESHAM NHS TRUST", "x": 1444.6105, "y": 1.0695 },
{ "org": "RNA - THE DUDLEY GROUP NHS FOUNDATION TRUST", "x": 2363.7158, "y": 0.998 },
{ "org": "RNL - NORTH CUMBRIA UNIVERSITY HOSPITALS NHS TRUST", "x": 1790.2801, "y": 1.0121 },
{ "org": "RNQ - KETTERING GENERAL HOSPITAL NHS FOUNDATION TRUST", "x": 1475.3056, "y": 1.0757 },
{ "org": "RNS - NORTHAMPTON GENERAL HOSPITAL NHS TRUST", "x": 1837.04, "y": 0.9842 },
{ "org": "RNZ - SALISBURY NHS FOUNDATION TRUST", "x": 1022.0168, "y": 1.1184 },
{ "org": "RP5 - DONCASTER AND BASSETLAW HOSPITALS NHS FOUNDATION TRUST", "x": 2691.8179, "y": 1.0004 },
{ "org": "RPA - MEDWAY NHS FOUNDATION TRUST", "x": 1716.3004, "y": 1.1274 },
{ "org": "RQ6 - ROYAL LIVERPOOL AND BROADGREEN UNIVERSITY HOSPITALS NHS TRUST", "x": 1934.6402, "y": 1.0229 },
{ "org": "RQ8 - MID ESSEX HOSPITAL SERVICES NHS TRUST", "x": 1781.0535, "y": 1.0814 },
{ "org": "RQM - CHELSEA AND WESTMINSTER HOSPITAL NHS FOUNDATION TRUST", "x": 1706.7067, "y": 0.8777 },
{ "org": "RQQ - HINCHINGBROOKE HEALTH CARE NHS TRUST", "x": 769.2867, "y": 1.0828 },
{ "org": "RQW - THE PRINCESS ALEXANDRA HOSPITAL NHS TRUST", "x": 1548.1202, "y": 0.9941 },
{ "org": "RQX - HOMERTON UNIVERSITY HOSPITAL NHS FOUNDATION TRUST", "x": 631.5981, "y": 0.9421 },
{ "org": "RR1 - HEART OF ENGLAND NHS FOUNDATION TRUST", "x": 4566.2584, "y": 0.9759 },
{ "org": "RR7 - GATESHEAD HEALTH NHS FOUNDATION TRUST", "x": 1503.1897, "y": 0.9646 },
{ "org": "RR8 - LEEDS TEACHING HOSPITALS NHS TRUST", "x": 4096.2774, "y": 1.0065 },
{ "org": "RRF - WRIGHTINGTON, WIGAN AND LEIGH NHS FOUNDATION TRUST", "x": 1508.0523, "y": 1.1153 },
{ "org": "RRK - UNIVERSITY HOSPITALS BIRMINGHAM NHS FOUNDATION TRUST", "x": 2643.8005, "y": 1.0269 },
{ "org": "RRV - UNIVERSITY COLLEGE LONDON HOSPITALS NHS FOUNDATION TRUST", "x": 1505.3752, "y": 0.7586 },
{ "org": "RTD - THE NEWCASTLE UPON TYNE HOSPITALS NHS FOUNDATION TRUST", "x": 2802.0209, "y": 0.9875 },
{ "org": "RTE - GLOUCESTERSHIRE HOSPITALS NHS FOUNDATION TRUST", "x": 2621.7667, "y": 1.1069 },
{ "org": "RTF - NORTHUMBRIA HEALTHCARE NHS FOUNDATION TRUST", "x": 2964.7743, "y": 1.0372 },
{ "org": "RTG - DERBY TEACHING HOSPITALS NHS FOUNDATION TRUST", "x": 3109.7697, "y": 1.0242 },
{ "org": "RTH - OXFORD UNIVERSITY HOSPITALS NHS FOUNDATION TRUST", "x": 3162.7921, "y": 0.9858 },
{ "org": "RTK - ASHFORD AND ST PETER'S HOSPITALS NHS FOUNDATION TRUST", "x": 1747.9839, "y": 0.9377 },
{ "org": "RTP - SURREY AND SUSSEX HEALTHCARE NHS TRUST", "x": 1869.0468, "y": 0.9518 },
{ "org": "RTR - SOUTH TEES HOSPITALS NHS FOUNDATION TRUST", "x": 2543.4471, "y": 1.1009 },
{ "org": "RTX - UNIVERSITY HOSPITALS OF MORECAMBE BAY NHS FOUNDATION TRUST", "x": 1866.0919, "y": 0.9914 },
{ "org": "RVJ - NORTH BRISTOL NHS TRUST", "x": 2108.177, "y": 0.9017 },
{ "org": "RVR - EPSOM AND ST HELIER UNIVERSITY HOSPITALS NHS TRUST", "x": 2050.3344, "y": 0.9574 },
{ "org": "RVV - EAST KENT HOSPITALS UNIVERSITY NHS FOUNDATION TRUST", "x": 4057.4561, "y": 0.9989 },
{ "org": "RVW - NORTH TEES AND HARTLEPOOL NHS FOUNDATION TRUST", "x": 1744.162, "y": 1.1731 },
{ "org": "RVY - SOUTHPORT AND ORMSKIRK HOSPITAL NHS TRUST", "x": 1157.6091, "y": 1.0789 },
{ "org": "RW3 - CENTRAL MANCHESTER UNIVERSITY HOSPITALS NHS FOUNDATION TRUST", "x": 1700.2735, "y": 1.0045 },
{ "org": "RW6 - PENNINE ACUTE HOSPITALS NHS TRUST", "x": 3715.3869, "y": 1.0796 },
{ "org": "RWA - HULL AND EAST YORKSHIRE HOSPITALS NHS TRUST", "x": 3108.7842, "y": 1.1223 },
{ "org": "RWD - UNITED LINCOLNSHIRE HOSPITALS NHS TRUST", "x": 3235.3045, "y": 1.1099 },
{ "org": "RWE - UNIVERSITY HOSPITALS OF LEICESTER NHS TRUST", "x": 4578.0319, "y": 0.9816 },
{ "org": "RWF - MAIDSTONE AND TUNBRIDGE WELLS NHS TRUST", "x": 2128.1915, "y": 1.0568 },
{ "org": "RWG - WEST HERTFORDSHIRE HOSPITALS NHS TRUST", "x": 2145.4672, "y": 0.9378 },
{ "org": "RWH - EAST AND NORTH HERTFORDSHIRE NHS TRUST", "x": 2158.887, "y": 1.0969 },
{ "org": "RWJ - STOCKPORT NHS FOUNDATION TRUST", "x": 1976.8328, "y": 0.9748 },
{ "org": "RWP - WORCESTERSHIRE ACUTE HOSPITALS NHS TRUST", "x": 2536.1774, "y": 1.1257 },
{ "org": "RWW - WARRINGTON AND HALTON HOSPITALS NHS FOUNDATION TRUST", "x": 1372.7914, "y": 1.1065 },
{ "org": "RWY - CALDERDALE AND HUDDERSFIELD NHS FOUNDATION TRUST", "x": 2087.0436, "y": 1.138 },
{ "org": "RX1 - NOTTINGHAM UNIVERSITY HOSPITALS NHS TRUST", "x": 4322.6218, "y": 1.0535 },
{ "org": "RXC - EAST SUSSEX HEALTHCARE NHS TRUST", "x": 2317.705, "y": 1.1352 },
{ "org": "RXF - MID YORKSHIRE HOSPITALS NHS TRUST", "x": 3098.2216, "y": 0.9473 },
{ "org": "RXH - BRIGHTON AND SUSSEX UNIVERSITY HOSPITALS NHS TRUST", "x": 2461.6877, "y": 0.9583 },
{ "org": "RXK - SANDWELL AND WEST BIRMINGHAM HOSPITALS NHS TRUST", "x": 2201.1161, "y": 0.9927 },
{ "org": "RXL - BLACKPOOL TEACHING HOSPITALS NHS FOUNDATION TRUST", "x": 2069.5094, "y": 1.15 },
{ "org": "RXN - LANCASHIRE TEACHING HOSPITALS NHS FOUNDATION TRUST", "x": 2234.6329, "y": 0.9948 },
{ "org": "RXP - COUNTY DURHAM AND DARLINGTON NHS FOUNDATION TRUST", "x": 2957.0435, "y": 1.023 },
{ "org": "RXQ - BUCKINGHAMSHIRE HEALTHCARE NHS TRUST", "x": 1611.2461, "y": 0.9868 },
{ "org": "RXR - EAST LANCASHIRE HOSPITALS NHS TRUST", "x": 2469.2723, "y": 1.0635 },
{ "org": "RXW - SHREWSBURY AND TELFORD HOSPITAL NHS TRUST", "x": 2553.2235, "y": 1.0265 },
{ "org": "RYJ - IMPERIAL COLLEGE HEALTHCARE NHS TRUST", "x": 3058.2235, "y": 0.758 },
{ "org": "RYR - WESTERN SUSSEX HOSPITALS NHS FOUNDATION TRUST", "x": 3070.9843, "y": 1.0127 }
      ],
    }]
  });
});

    </script>-->
</#macro>
