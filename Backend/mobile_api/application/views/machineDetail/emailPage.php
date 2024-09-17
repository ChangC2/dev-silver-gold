<!DOCTYPE html>

<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>SLYTRACKR</title>

    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css">
    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.css">
    <!-- Material Design Bootstrap -->
    <link href="<?php echo base_url(); ?>assets/css/mdb.min.css" rel="stylesheet">

    <link href="<?php echo base_url(); ?>assets/css/style_detailPage.css?v=1.15" rel="stylesheet">
</head>

<body>

    <div class="container">
        <div class="row">
            <!-- Header -->
            <div class="row">
                <div class="col-xs-3">
                    <a class="center-header" style="font-size: 14px; font-weight: bold;"><?php echo $status_data->machine_id; ?></a>
                </div>
                <div class="col-xs-6">

                    <img class='center-image img img-thumbnail' id='title_logo' src="<?php echo $userdata['logo']; ?>">";

                </div>
                <div class="col-xs-3">
                    <a class="center-header" id="txtCurrentDate" style="font-size: 14px; font-weight: bold;"><?php echo $userdata['fullname']; ?></a>
                </div>
            </div>

            <!-- Key Performance Indicators -->
            <div class="row KeyPerformance" id="key_indicator_container">
                <div class="row" style="margin:2px;">
                    <!-- OEE Gauge -->
                    <div class="col-xs-4" style="min-height:250px">
                        <div style="display: flex; justify-content: center;">
                            <canvas style="height:250px" id="gauge_oee"></canvas>
                        </div>
                        <div style="text-align:center; margin-top:-195px;">
                            <a style="font-weight:900; font-size:50px; color:green; z-index:3; position:relative" id="gauge_oee_text"></a>
                        </div>
                        <div style="text-align:center; margin-top:10px;">
                            <a style="font-weight:900; font-size:30px; color:green; z-index:3; position:relative">OEE</a>
                        </div>
                    </div>

                    <!-- Small Gauge -->
                    <div class="col-xs-8" style="margin-top:30px">
                        <div class="row">
                            <!-- AVA Gauge -->
                            <div class="col-xs-4" style="min-height:180px">
                                <div style="display: flex; justify-content: center;">
                                    <canvas style="height:180px" id="gauge_ava"></canvas>
                                </div>
                                <div style="text-align:center; margin-top:-135px;">
                                    <a style="font-weight:600; font-size:30px; color:green; z-index:3; position:relative;" id="gauge_ava_text"></a>
                                </div>
                                <div style="text-align:center; margin-top:7px;">
                                    <a style="font-weight:600; font-size:20px; color:green; z-index:3; position:relative">AVA</a>
                                </div>
                            </div>
                            <!-- QUA Gauge -->
                            <div class="col-xs-4" style="min-height:180px">
                                <div style="display: flex; justify-content: center;">
                                    <canvas style="height:180px" id="gauge_qua"></canvas>
                                </div>
                                <div style="text-align:center; margin-top:-135px;">
                                    <a style="font-weight:600; font-size:30px; color:green; z-index:3; position:relative" id="gauge_qua_text"></a>
                                </div>
                                <div style="text-align:center; margin-top:7px;">
                                    <a style="font-weight:600; font-size:20px; color:green; z-index:3; position:relative">QUA</a>
                                </div>
                            </div>
                            <!-- PER Gauge -->
                            <div class="col-xs-4" style="min-height:180px">
                                <div style="display: flex; justify-content: center;">
                                    <canvas style="height:180px" id="gauge_per"></canvas>
                                </div>
                                <div style="text-align:center; margin-top:-135px;">
                                    <a style="font-weight:600; font-size:30px; color:green; z-index:3; position:relative" id="gauge_per_text"></a>
                                </div>
                                <div style="text-align:center; margin-top:7px;">
                                    <a style="font-weight:600; font-size:20px; color:green; z-index:3; position:relative">PER</a>
                                </div>
                            </div>


                        </div>
                    </div>
                </div>
            </div>

            <!-- Pie Chart and Aux -->
            <div class="row">
                <!-- Pie Chart -->
                <div class="col-xs-7" style="padding:2%; width:320; padding-top:20px">
                    <div class="relative">
                        <div id="pie_chart_detail" style="margin-top:-0px"></div>
                    </div>
                </div>

                <!-- Aux Data -->
                <div class="col-xs-5 center-AuxText" style="padding-top:50px">
                    <div class="row" style="margin:0px; padding:0px; margin-top:1px">
                        <div class="col-xs-7" style=""><strong class="AuxText" style="color:green"><?php echo $status_data->aux1 != "" ? $status_data->aux1 . ':' : " - "; ?></strong> </div>
                        <div class="col-xs-5">
                            <strong class="AuxText" style="color:green" id="status_aux1"></strong>
                        </div>
                    </div>
                    <div class="row" style="margin:0px; padding:0px; margin-top:1px">
                        <div class="col-xs-7" style=""><strong class="AuxText" style="color:green"><?php echo $status_data->aux2 != "" ? $status_data->aux2 . ':' : " - "; ?></strong></div>
                        <div class="col-xs-5">
                            <strong class="AuxText" style="color:green" id="status_aux2"></strong>
                        </div>
                    </div>
                    <div class="row" style="margin:0px; padding:0px; margin-top:1px; margin-bottom:1px">
                        <div class="col-xs-7" style=""><strong class="AuxText" style="color:green"><?php echo $status_data->aux3 != "" ? $status_data->aux3 . ':' : " - "; ?></strong></div>
                        <div class="col-xs-5">
                            <strong class="AuxText" style="color:green" id="status_aux3"></strong>
                        </div>
                    </div>
                </div>
            </div>


            <!-- Gantt Chart -->
            <div class="row" style="padding:2%; padding-top:10px">
                <div style="text-align:center; color:white;" id="gantt_chart_detail">
                </div>
            </div>

            <!-- Bar Chart -->
            <div class="row" style="padding:2%; padding-top:10px">
                <div id="chart_div" style="height: 500px; display: block; padding: 0px 0px 100px 0px"></div>
            </div>
        </div>
    </div>


    <script type="text/javascript" src="<?php echo base_url(); ?>assets/js/jquery-3.4.0.min.js"></script>

    <!-- Bootstrap tooltips -->
    <script type="text/javascript" src="<?php echo base_url(); ?>assets/js/popper.min.js"></script>
    <!-- Bootstrap core JavaScript -->
    <script type="text/javascript" src="<?php echo base_url(); ?>assets/js/bootstrap.min.js"></script>
    <!-- MDB core JavaScript -->
    <script type="text/javascript" src="<?php echo base_url(); ?>assets/js/mdb.min.js"></script>
    <script src="<?php echo base_url(); ?>d3_assets/d3.v3.min.js"></script>
    <script src="<?php echo base_url(); ?>d3_assets/gantt-chart-d3.js?v=1.03"></script>
    <script src="<?php echo base_url(); ?>gauge_assets/Gauge.min.js"></script>
    <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.20.1/moment.js"></script>
    <!-- Bar Chart -->
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

    <!-- For modal dialog -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.css" />
    <script type="text/javascript">
        google.charts.load('current', {
            'packages': ['corechart']
        });

        function DrawBarChart(hstList) {
            google.charts.setOnLoadCallback(drawVisualization);

            function drawVisualization() {
                var dataList = [];
                dataList.push(['Day', 'In Cycle', 'Not In Cycle', 'In Cycle Trend']);
                for (var i = 0; i < hstList.length && i < 30; i++) {
                    var idle = 0;
                    idle += parseFloat(hstList[i]['uncat']);
                    idle += parseFloat(hstList[i]['r1t']);
                    idle += parseFloat(hstList[i]['r2t']);
                    idle += parseFloat(hstList[i]['r3t']);
                    idle += parseFloat(hstList[i]['r4t']);
                    idle += parseFloat(hstList[i]['r5t']);
                    idle += parseFloat(hstList[i]['r6t']);
                    idle += parseFloat(hstList[i]['r7t']);
                    idle += parseFloat(hstList[i]['r8t']);

                    var incycle = parseFloat(hstList[i]['inCycle']);
                    var day = hstList[i]['date'];

                    var item = [day, incycle / 3600, idle / 3600, incycle / 3600];
                    dataList.push(item);
                }
                // console.log("HST DATA");
                // console.log(dataList);

                // Some raw data (not necessarily accurate)
                var data = google.visualization.arrayToDataTable(dataList);

                var options = {
                    title: 'Utilization History',
                    vAxis: {
                        title: 'Utilization %'
                    },
                    hAxis: {
                        title: 'Day'
                    },
                    seriesType: 'bars',
                    series: {
                        2: {
                            type: 'line'
                        }
                    },
                    chartArea: {
                        width: '92%'
                    },
                    legend: {
                        position: 'bottom'
                    },
                    width: '100%',
                    colors: ['#10ce79', '#ce101d', '#10ce79']
                };

                var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
                chart.draw(data, options);
            }
        }
    </script>
    <script>
        var historyData = {};
        var statusData = <?php echo json_encode($status_data); ?>;
        var hstData = {};
        var ganttChart;
        var diff_time = 0;
        d = new Date();
        localTime = d.getTime();
        localOffset = d.getTimezoneOffset() * 60000;

        String.prototype.toHHMMSS = function() {
            var sec_num = parseInt(this, 10); // don't forget the second param
            var hours = Math.floor(sec_num / 3600);
            var minutes = Math.floor((sec_num - (hours * 3600)) / 60);
            var seconds = sec_num - (hours * 3600) - (minutes * 60);

            if (hours < 10) {
                hours = "0" + hours;
            }
            if (minutes < 10) {
                minutes = "0" + minutes;
            }
            if (seconds < 10) {
                seconds = "0" + seconds;
            }
            var res = "";

            if (hours > 0)
                res = hours + 'hour(s) ' + minutes + 'minute(s) ' + seconds;
            else if (minutes > 0)
                res = minutes + 'minute(s) ' + seconds;
            else
                res = seconds;

            return res + "second(s)";
        }

        var run_mode = 1;
        var realtimeInterval = null;

        getDetailData("10/18/2019");
        // realtimeInterval = setInterval(function() {
        //     if (run_mode == 1)
        //         getDetailData();
        // }, 5000);

        function nowDate() {
            var date = new Date();
            // console.log('localoffset=' + localOffset);
            // console.log("original=" + date.getTime());
            date.setTime(date.getTime() + localOffset + diff_time * 1000);
            // console.log("calced=" + date.getTime());

            yyyy = date.getFullYear();
            mm = date.getMonth() + 1;
            dd = date.getDate();
            return mm + "/" + dd + "/" + yyyy;
        }


        function getDetailData(sel_date = "now") {

            $.ajax({
                method: "POST",
                url: "<?php echo base_url(); ?>api/get_machine_detail_data_today",
                data: JSON.stringify({
                    machine_id: statusData['machine_id'],
                    sel_date: sel_date
                }),
                dataType: "json",
                success: function(msg) {
                    hstData = msg[0];
                    if(hstData == undefined) return;
                    DrawCharts(hstData);
                    //DrawGauge_one(item.id, item.Utilization);
                    $('#status_aux1').html(hstData.aux1data);
                    $('#status_aux2').html(hstData.aux2data);
                    $('#status_aux3').html(hstData.aux3data);
                    $('#txtCurrentDate').html(hstData.date);

                    $("#machine_cam_container").height($("#key_indicator_container").height());
                    //console.log('machine_container: '+ $("#machine_cam_container").height());
                    //console.log('key_indicator_container: '+ $("#key_indicator_container").height());
                    var parentHeight = $("#machine_cam_container").height();
                    var machineHeight = $('#machine_picture').height();

                    $('#machine_picture').height();
                    $('#cameraContainer').height(parentHeight - machineHeight - 20);
                    // console.log('cameraContainer: ' + $("#cameraContainer").height());
                    // console.log('machine_picture: ' + $('#machine_picture').height());
                },
                error: function(err) {
                    console.log(err);
                }
            });

            $.ajax({
                method: "GET",
                url: "<?php echo base_url(); ?>api/get_timezone",
                dataType: "json",
                success: function(info) {
                    //console.log(info);
                    diff_time = parseFloat(info[0].timezone) * 3600;
                }
            });

            $.ajax({
                method: "POST",
                url: "<?php echo base_url(); ?>api/get_ganttData",
                data: JSON.stringify({
                    machine_id: statusData['machine_id'],
                    sel_date: sel_date
                }),
                dataType: "json",
                success: function(msg) {
                    // console.log(msg);
                    drawGanttChart_one(msg);
                },
                error: function(err) {
                    console.log(err);
                }
            });

            $.ajax({
                method: "POST",
                url: "<?php echo base_url(); ?>api/get_allHstData",
                dataType: "json",
                success: function(msg) {
                    historyData = msg;
                    DrawBarChart(msg);
                },
                error: function(err) {
                    console.log(err);
                }
            });
        }

        $(document).ready(function() {

            $('#machine_picture').attr('src', "<?php echo base_url(); ?>images/machine/" + statusData['machine_picture_url']);
            var operatorName = (typeof(statusData.Operator) != "undefined" && statusData.Operator !== null) ? statusData.Operator : "";
            var operator_picture_url = (typeof(statusData.operator_picture_url) != "undefined" && statusData.operator_picture_url !== null && statusData.operator_picture_url !== "") ? statusData.operator_picture_url : "blank.jpg";
            operator_picture_url = "<?php echo base_url(); ?>images/photo/" + operator_picture_url;
            $('#operator_picture').attr('src', operator_picture_url);
            $('#operator_name').html(operatorName);
        });

        function DrawCharts(data) {
            if(data == undefined) return;

            if (data.oee != undefined)
                DrawGauge_one('gauge_oee', data.oee);
            if (data.availability != undefined)
                DrawGauge_one('gauge_ava', data.availability);
            if (data.quality != undefined)
                DrawGauge_one('gauge_qua', data.quality);
            if (data.performance != undefined)
                DrawGauge_one('gauge_per', data.performance);
            if (data.oee != undefined && data.quality != undefined)
                DrawProgressBar('progress_bar', (parseInt(data.oee) + parseInt(data.quality)) / 2);
            drawPieChart(data);
        }

        var gaugeArray = [];

        function DrawGauge_one(gauge_id, value) {
            //console.log(gaugeArray[gauge_id]);
            if (gaugeArray[gauge_id] == undefined) {
                var opts = {
                    angle: -0.5,
                    lineWidth: 0.15,
                    pointer: {
                        length: 0.6,
                        strokeWidth: 0.02,
                        color: '#000000'
                    },
                    limitMax: 'true',
                    //percentColors: [[0.0, "#a9d70b" ], [0.50, "#f9c802"], [1.0, "#ff0000"]], // !!!!
                    percentColors: [
                        [0.0, "#ff0000"],
                        [0.399, "#ff0000"],
                        [0.4, "#ff0000"],
                        [0.599, "#FFFF00"],
                        [0.749, "#FFFF00"],
                        [0.75, "#5DB501"],
                        [0.1, "#5DB501"]
                    ], // !!!!
                    strokeColor: '#E0E0E0',
                    generateGradient: true
                };
                var target = document.getElementById(gauge_id); // your canvas element
                var gauge = new Gauge(target).setOptions(opts); // create sexy gauge!

                gauge.maxValue = 100; // set max gauge value
                gauge.setMinValue(0); // Prefer setter over gauge.minValue = 0
                gauge.animationSpeed = 11; // set animation speed (32 is default value)
                gauge.set(value); // set actual value
                gaugeArray[gauge_id] = gauge;
            } else {
                gaugeArray[gauge_id].set(value);
            }
            $('#' + gauge_id + '_text').html(value + "%");
        }

        function DrawProgressBar(id, value) {
            $('#' + id).width(value + '%');
            $('#' + id + '_text').text(value + '%');
        }
        var plotString = "";

        function drawPieChart(value) {
            var pie_chart_value_array = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
            var pie_chart_label_array = ['In Cycle', 'Uncategorized', 'Offline', statusData['r1'], statusData['r2'], statusData['r3'], statusData['r4'], statusData['r5'], statusData['r6'], statusData['r7'], statusData['r8']];
            var pie_chart_color_array = ['#4c9d2f', '#d8292f', '#8a8a8a', '#00008B', '#B8860B', '#006400', '#9400D3', '#FFD700', '#ADFF2F', '#FF69B4', '#ADD8E6'];

            pie_chart_value_array[0] += value['inCycle'];
            pie_chart_value_array[1] += value['uncat'];
            pie_chart_value_array[2] += value['offline'];
            pie_chart_value_array[3] += value['r1t'];
            pie_chart_value_array[4] += value['r2t'];
            pie_chart_value_array[5] += value['r3t'];
            pie_chart_value_array[6] += value['r4t'];
            pie_chart_value_array[7] += value['r5t'];
            pie_chart_value_array[8] += value['r6t'];
            pie_chart_value_array[9] += value['r7t'];
            pie_chart_value_array[10] += value['r8t'];

            var newPlotString = JSON.stringify(pie_chart_value_array);
            if (newPlotString == plotString) return;
            //console.log(plotString +  " : " + newPlotString);
            plotString = newPlotString;
            // Removing zero values
            pie_chart_label_array = jQuery.grep(pie_chart_label_array, function(value, index) {
                return pie_chart_value_array[index] != 0;
            });
            pie_chart_color_array = jQuery.grep(pie_chart_color_array, function(value, index) {
                return pie_chart_value_array[index] != 0;
            });
            pie_chart_value_array = jQuery.grep(pie_chart_value_array, function(value, index) {
                return value != 0;
            });

            // Convert second to hour
            var sum = 0;
            for (i = 0; i < pie_chart_value_array.length; i++) {
                pie_chart_value_array[i] = pie_chart_value_array[i] / 3600;
                pie_chart_value_array[i] = pie_chart_value_array[i].toFixed(2);
                //console.log(pie_chart_value_array[i]);
                sum += parseFloat(pie_chart_value_array[i]);
            }
            //console.log(pie_chart_value_array);

            //console.log(sum);

            var digits = 1;
            var rounded_values = [];
            for (var i = 0; i < pie_chart_value_array.length; i += 1) {
                rounded_values.push(Math.round(pie_chart_value_array[i] / sum * 100 * 10 * digits) / 10 * digits + '%');
            }

            var text = rounded_values.map((v, i) => `${pie_chart_label_array[i]}<br>${pie_chart_value_array[i]} hr(s)<br>${rounded_values[i]}`);

            var data = [{
                values: pie_chart_value_array,
                labels: pie_chart_label_array,
                type: 'pie',
                sort: false,
                marker: {
                    colors: pie_chart_color_array
                },
                //text: text,
                hovertext: text,
                hoverinfo: 'text',
                //textinfo: 'none'
            }];

            var layout = {
                paper_bgcolor: "transparent",
                margin: {
                    l: 40,
                    r: 0,
                    b: 0,
                    t: 0,
                },
                showlegend: true,
                height: 210,
                legend: {
                    font: 8
                },
            };

            //console.log(pie_chart_value_array);
            if (plot == undefined) {
                var plot = Plotly.newPlot('pie_chart_detail', data, layout, {
                    displayModeBar: false
                });
            } else {
                plot.data = data;
            }
        }
        var plot;

        function SetStatusLabel(status) {
            var color = "#EFEF32";
            $('#gantt_detail_status').removeClass('statusLabel_green');
            $('#gantt_detail_status').removeClass('statusLabel_red');
            $('#gantt_detail_status').removeClass('statusLabel_yellow');

            $('#nowStatus').css('color', 'white');
            switch (status) {
                case "In Cycle":
                case "In Cycle - Monitoring":
                    $('#gantt_detail_status').addClass('statusLabel_green');
                    color = '#5DB501';
                    break;
                case "Idle - Uncategorized":
                case "Uncategorized":
                    $('#gantt_detail_status').addClass('statusLabel_red');
                    color = '#FF0000';
                    break;
                case "Offline":
                    color = '#888B86';
                    break;
                default:
                    $('#gantt_detail_status').addClass('statusLabel_yellow');
                    color = "#EFEF32";
                    $('#nowStatus').css('color', 'black');
                    break;
            }
            $('#nowStatus').html(status);
            $('#nowStatus').css('background', color);

        }

        function drawGanttChart_one(data) {
            var tasks = [];
            if(data.length == 0) return;
            //console.log(id);
            var updated_data = [];
            for (i = 0; i < data.length - 1; i++) {
                var connected = false;
                if (data[i].status == data[i + 1].status) {
                    data[i + 1].start = data[i].start;
                    connected = true;
                }
                if (connected == false)
                    updated_data.push(data[i]);
            }
            updated_data.push(data[data.length - 1]);
            //var diff_time = parseFloat(info[0].timezone) * 3600;
            //console.log("diff_time= " + diff_time + ",  localoffset=" + localOffset);
            for (i = 0; i < updated_data.length; i++) {
                var item = {
                    //"startDate": new Date((parseInt(updated_data[i].start) + diff_time) * 1000 + localOffset),
                    //"endDate": new Date((parseInt(updated_data[i].end) + diff_time) * 1000 + localOffset),
                    "startDate": new Date((parseInt(updated_data[i].start)) * 1000 + localOffset),
                    "endDate": new Date((parseInt(updated_data[i].end)) * 1000 + localOffset),
                    "color": updated_data[i].color,
                    "taskName": updated_data[i].machine_id,
                    "status": updated_data[i].status
                };
                tasks.push(item);
            }

            tasks.sort(function(a, b) {
                if (a.endDate > b.endDate) {
                    return 1;
                }
                if (a.endDate < b.endDate) {
                    return -1;
                }
                return 0;
            })

            $('#gantt_detail_status').html(tasks[tasks.length - 1].status);
            SetStatusLabel(tasks[tasks.length - 1].status);
            $('#gantt_detail_start').html(moment(tasks[tasks.length - 1].startDate).format('YYYY-MM-DD HH:mm:ss'));
            $('#gantt_detail_duration').html((((tasks[tasks.length - 1].endDate - tasks[tasks.length - 1].startDate) / 1000) + "").toHHMMSS());

            //console.log(tasks);
            if (updated_data.length == 0) {
                var item = {
                    "startDate": "nbsp;",
                    "endDate": "",
                    "color": "#555",
                    "taskName": "",
                    "status": ""
                };
                tasks.push(item);
            }

            var taskNames = ["ONE"];

            tasks.sort(function(a, b) {
                return a.endDate - b.endDate;
            });
            var maxDate = tasks[tasks.length - 1].endDate;
            tasks.sort(function(a, b) {
                return a.startDate - b.startDate;
            });
            var minDate = tasks[0].startDate;

            var format = "%H:%M";
            //console.log($("#chartContainer_" + id).width());
            var margin = {
                top: 0,
                right: 10,
                bottom: 30,
                left: 10
            };
            if (ganttChart == undefined) {
                var gantt = d3.gantt("#gantt_chart_detail", $("#gantt_chart_detail").width())
                    .Container("#gantt_chart_detail")
                    .taskTypes(taskNames)
                    .tickFormat(format)
                    .margin(margin);
                gantt(tasks);
                ganttChart = gantt;
            } else {
                ganttChart.Container("#gantt_chart_detail").taskTypes(taskNames).tickFormat(format).redraw(tasks);
                //return;
            }
        }
    </script>
</body>

</html>