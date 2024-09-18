<!DOCTYPE html>
<?php ini_set('default_charset', 'utf-8'); ?>

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
    <link href="<?php echo base_url() ?>assets/css/mdb.min.css" rel="stylesheet">

    <link href="<?php echo base_url() ?>assets/css/style_detailPage.css?v=1.12" rel="stylesheet">

    <style>
        body {
            background: #1d1d1d;
        }

        .axis {
            font: 14px sans-serif;
        }

        .chart {
            font-family: Arial, sans-serif;
            font-size: 12px;
        }

        .axis path,
        .axis line {
            fill: none;
            stroke: #f4f4f4;
            shape-rendering: crispEdges;
        }

        .green-bar {
            fill: #4c9d2f
        }

        .red-bar {
            fill: #d8292f
        }

        .grey-bar {
            fill: #8a8a8a
        }



        .bar {
            fill: #8a8a8a;
        }

        .bar-failed {
            fill: #d8292f;
        }

        .bar-running {
            fill: #4c9d2f;
        }

        .bar-succeeded {
            fill: #4c9d2f;
        }

        .bar-idle {
            fill: #f4c400;
        }

        text {
            fill: #f4f4f4;
        }

        .tooltip {
            background: #132e35;
            color: #f4f4f4;
            border: 1px solid #2c6d7c;
            font-size: 12px;
            font-family: Arial, sans-serif;
            left: 130px;
            padding: 10px;
            position: absolute;
            text-align: left;
            top: 95px;
            z-index: 10;
            display: block;
            opacity: 0.5;
        }



        .legend {
            padding: 5px;
            font: 16px;
            font-family: Arial, sans-serif;
            box-shadow: 2px 2px 1px #888;
        }

        .zoom {
            cursor: move;
            fill: none;
            pointer-events: all;

        }

        rect {
            stroke: transparent;
        }

        @media only screen and (min-width:768px) {
            .machine_info {
                float: left;
                border: 1.5px solid;
                border-top: 0;
                border-bottom: 0;
                border-image: linear-gradient(to bottom, rgba(255, 255, 255, 0) 0%, rgba(226, 226, 226, 1) 48%, rgba(255, 255, 255, 0) 100%);
                border-image-slice: 1;
            }
        }

        @medai only mobile and (max-width:400) {
            .infolayer {
                display: none;
            }
        }
    </style>
</head>

<body>

    <div class="container">
        <div class="row" style="padding:20px">
            <div class="col-sm-12 col-md-9">
                <!-- Header -->
                <div class="row" style="height:70px">
                    <div class="col-sm-12 col-md-1" style="position: relative">
                        <style>
                            #btnBack:hover {
                                color: white;
                            }

                            #btnBack {
                                color: grey;
                                font-size: 20px;
                                font-weight: bold;
                            }
                        </style>

                        <a class="center-header" id="btnBack"><i class="fas fa-arrow-left"></i></a>
                    </div>
                    <div class="col-sm-12 col-md-3" style="position: relative">
                        <a class="center-header" style="font-size: 14px; font-weight: bold; color:#d9d9d9"><?php echo $status_data->machine_id; ?></a>
                    </div>
                    <div class="col-sm-12 col-md-5">
                        <?php
                        if (isset($userdata['logo']) && $userdata['logo'] != "" && $userdata['logo'] != null)
                            echo "<img class='center-image img img-thumbnail' id='title_logo' src=\"" . $userdata['logo'] . "\">";
                        else
                            echo "<a class='text-secondary' style='font-size:30px;font-weight:bold;color:#d9d9d9'>" . $userdata['fullname'] . "</a>"
                        ?>
                    </div>
                    <div class="col-sm-12 col-md-3">
                        <a class="center-header" id="txtCurrentDate" style="font-size: 14px; font-weight: bold; color:#d9d9d9"><?php echo $userdata['fullname']; ?></a>
                    </div>
                </div>

                <!-- Key Performance Indicators -->
                <div class="row " id="key_indicator_container" style="margin-top: 15px;background: #404040;border: 1px solid #8a8a8a; min-height:380px">
                    <div style="margin:2%">
                        <a style="font-size:16px; font-weight:bold; color: white">Key Performance Indicators</a>
                        <hr>
                    </div>
                    <div class="row" style="margin:2%; ">
                        <div class="col col-sm-12 col-md-4" style="min-height:300px">
                            <div style="display: flex; justify-content: center;">
                                <canvas style="height:300px" id="gauge_oee"></canvas>
                            </div>
                            <div style="text-align:center; margin-top:-225px;">
                                <a class="auxTextStyle" style="color:#d9d9d9; font-weight:900; font-size:50px;" id="gauge_oee_text"></a>
                            </div>
                            <div style="text-align:center; margin-top:10px;">
                                <a class="auxTextStyle" style="color:#d9d9d9; font-weight:900; font-size:30px;">OEE</a>
                            </div>
                        </div>
                        <div class="col col-sm-12 col-md-8">
                            <div class="row">
                                <div class="col-sm-12 col-md-4" style="min-height:200px">
                                    <div style="display: flex; justify-content: center;">
                                        <canvas style="height:180px" id="gauge_ava"></canvas>
                                    </div>
                                    <div style="text-align:center; margin-top:-135px;">
                                        <a class="auxTextStyle" style="color:#d9d9d9; font-weight:600; font-size:30px;;" id="gauge_ava_text"></a>
                                    </div>
                                    <div style="text-align:center; margin-top:7px;">
                                        <a class="auxTextStyle" style="color:#d9d9d9; font-weight:600; font-size:20px;">AVA</a>
                                    </div>
                                </div>
                                <div class="col-sm-12 col-md-4" style="min-height:200px">
                                    <div style="display: flex; justify-content: center;">
                                        <canvas style="height:180px" id="gauge_qua"></canvas>
                                    </div>
                                    <div style="text-align:center; margin-top:-135px;">
                                        <a class="auxTextStyle" style="color:#d9d9d9; font-weight:600; font-size:30px;" id="gauge_qua_text"></a>
                                    </div>
                                    <div style="text-align:center; margin-top:7px;">
                                        <a class="auxTextStyle" style="color:#d9d9d9; font-weight:600; font-size:20px;">QUA</a>
                                    </div>
                                </div>
                                <div class="col-sm-12 col-md-4" style="min-height:200px">
                                    <div style="display: flex; justify-content: center;">
                                        <canvas style="height:180px" id="gauge_per"></canvas>
                                    </div>
                                    <div style="text-align:center; margin-top:-135px;">
                                        <a class="auxTextStyle" style="color:#d9d9d9; font-weight:600; font-size:30px;" id="gauge_per_text"></a>
                                    </div>
                                    <div style="text-align:center; margin-top:7px;">
                                        <a class="auxTextStyle" style="color:#d9d9d9; font-weight:600; font-size:20px;">PER</a>
                                    </div>
                                </div>

                                <!-- Progress Bar & Aux DATA-->
                                <div class="col-sm-12" style="margin-top:3%">
                                    <div class="row">
                                        <!-- Progress Bar -->
                                        <div class="col-sm-8">
                                            <div>
                                                <strong style="color:#d9d9d9">Production Rate: </strong>
                                                <a id="progress_bar_text" style="color:#d9d9d9">
                                                </a>
                                            </div>
                                            <div class="progress">
                                                <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" id="progress_bar" style="width:0%; color:black">
                                                </div>
                                            </div>
                                        </div>
                                        <!-- Aux Data -->
                                        <div class="col-sm-4" style="padding-left:0px; padding-right:0px;">
                                            <div class="row" style="margin:0px; padding:0px; margin-top:1px">
                                                <div class="col-sm-8" style="padding-left:0px; padding-right:0px;"><strong style="color:#d9d9d9"><?php echo $status_data->aux1 != "" ? $status_data->aux1 . ':' : " - "; ?></strong> </div>
                                                <div class="col-sm-4">
                                                    <strong style="color:#d9d9d9" id="status_aux1"></strong>
                                                </div>
                                            </div>
                                            <div class="row" style="margin:0px; padding:0px; margin-top:1px">
                                                <div class="col-sm-8" style="padding-left:0px; padding-right:0px;"><strong style="color:#d9d9d9"><?php echo $status_data->aux2 != "" ? $status_data->aux2 . ':' : " - "; ?></strong></div>
                                                <div class="col-sm-4">
                                                    <strong style="color:#d9d9d9" id="status_aux2"></strong>
                                                </div>
                                            </div>
                                            <div class="row" style="margin:0px; padding:0px; margin-top:1px; margin-bottom:1px">
                                                <div class="col-sm-8" style="padding-left:0px; padding-right:0px;"><strong style="color:#d9d9d9"><?php echo $status_data->aux3 != "" ? $status_data->aux3 . ':' : " - "; ?></strong></div>
                                                <div class="col-sm-4">
                                                    <strong style="color:#d9d9d9" id="status_aux3"></strong>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Pie and Gantt charts -->
                <div class="row">
                    <div class="col-sm-12 col-md-5" style="padding:2%; width:320; padding-top:20px">
                        <div>
                            <a style="font-size:16px; font-weight:bold;; color:#d9d9d9">Utilization</a>
                            <hr>
                        </div>
                        <div class="relative">
                            <div id="pie_chart_detail" style="margin-top:-0px"></div>
                        </div>
                    </div>
                    <div class="col-sm-12 col-md-7" style="padding:2%; padding-top:20px">
                        <div>
                            <a style="font-size:16px; font-weight:bold;; color:#d9d9d9">Timeline</a>
                            <hr>

                            <div class="row" style="margin-top:10px">
                                <div class="col-sm-12 col-md-10">
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <a class="ganttDetail-text" style="font-size:14px; color:#d9d9d9">Status:</a><br>
                                            <a class="ganttDetail-text" style="color:#d9d9d9">Start time:</a><br>
                                            <a class="ganttDetail-text" style="color:#d9d9d9">Duration:</a>
                                        </div>
                                        <div class="col-sm-9">
                                            <a class="ganttDetail-text statusLabel_green" style="font-weight:bold; font-size:14px; color:#d9d9d9" id="gantt_detail_status"></a><br>
                                            <a class="ganttDetail-text" style="font-weight:bold; color:#d9d9d9" id="gantt_detail_start"></a><br>
                                            <a class="ganttDetail-text" style="font-weight:bold; color:#d9d9d9" id="gantt_detail_duration"></a>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-12 col-md-6">
                                </div>
                            </div>
                        </div>
                        <div style="text-align:center; color:#d9d9d9;" id="gantt_chart_detail">
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-12 col-md-3" style="padding-left:2%">
                <!-- Operator Image -->
                <div class="KeyPerformance" style="height:70px; margin:0px; padding:3px">
                    <div class="row">
                        <div class="col-sm-5">
                            <div style="display:flex; flex-direction:row; width:100%; padding:1px ">
                                <img id="operator_picture" class="img img-responsive img-circle" style="max-width:100%; height:60px; box-shadow: 3px 3px 2px grey;" />
                            </div>
                        </div>
                        <div class="col-sm-7" style="text-align:left">
                            <strong class="center-header" style="color:white; justify-content:left" id="operator_name"></strong>
                        </div>
                    </div>
                </div>

                <div id="machine_cam_container" style="margin-top:15px; min-height:380px;" class="KeyPerformance__">
                    <!-- Machine Image -->
                    <div class="KeyPerformance">
                        <img id="machine_picture" class="img" style="width:100%" />
                    </div>
                    <!-- Camera -->
                    <div class="KeyPerformance" id="cameraContainer" style="background:black; margin-top:16px; height:175px; text-align:center">
                        <!-- <video id="cameraViewer" style="width:100%; height:100%" loop> -->
                        <!-- <source src="http://techslides.com/demos/sample-videos/small.webm" type="video/webm">
                                <source src="http://techslides.com/demos/sample-videos/small.ogv" type="video/ogg">
                                <source src="http://techslides.com/demos/sample-videos/small.mp4" type="video/mp4">
                                <source src="http://techslides.com/demos/sample-videos/small.3gp" type="video/3gp"> -->
                        <!-- <source src="<?php echo base_url(); ?>assets/ATC.avi" type="video/avi"> -->
                        <!-- <source src="<?php echo base_url(); ?>assets/CNCMachining.mp4" type="video/mp4"> -->
                        <!-- </video> -->
                        <i class="fas fa-video" style="font-size: 50px; margin-top: 50px"></i>
                    </div>
                    <!-- <button id="btnMute" style="background:transparent; color:white; float:right">
                        <i class="fas fa-volume-up"></i>
                    </button> -->
                </div>

                <!-- Chart History -->
                <div class="row" style="padding:2%">
                    <div style="padding-top:15px;">
                        <div>
                            <a style="font-size:16px; font-weight:bold; color:#d9d9d9">Chart History</a>
                            <hr>
                        </div>
                        <div>
                            <select multiple="false" style="width:100%; height:210px; background-color:#d9d9d9" id="historyList">
                                <option value="1"> </option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="modal" id="ex1" style="background:#999999; text-align:center; padding:20px; max-width:90%">
        <video id="cameraViewer1" style="width:90%; padding:0px" loop muted>
            <!-- <source src="http://techslides.com/demos/sample-videos/small.webm" type="video/webm">
            <source src="http://techslides.com/demos/sample-videos/small.ogv" type="video/ogg">
            <source src="http://techslides.com/demos/sample-videos/small.mp4" type="video/mp4">
            <source src="http://techslides.com/demos/sample-videos/small.3gp" type="video/3gp"> -->
            <source src="<?php echo base_url(); ?>assets/CNCMachining.mp4" type="video/mp4">
        </video>
    </div>
    <style>
        iframe {
            width: 100%;
            height: 100%;
            min-height: 400px;
        }
    </style>
    <div class="modal" id="ganttDetail" style="background:white; text-align:center; padding:10px; max-width:90%;">
        <iframe id="ganttPage" width="100%" height="100%" src="<?php echo base_url(); ?>ganttDetail/1/now"> </iframe>
    </div>
    <!-- ganttDetail -->


    <script type="text/javascript" src="<?php echo base_url() ?>assets/js/jquery-3.4.0.min.js"></script>

    <!-- Bootstrap tooltips -->
    <script type="text/javascript" src="<?php echo base_url() ?>assets/js/popper.min.js"></script>
    <!-- Bootstrap core JavaScript -->
    <script type="text/javascript" src="<?php echo base_url() ?>assets/js/bootstrap.min.js"></script>
    <!-- MDB core JavaScript -->
    <script type="text/javascript" src="<?php echo base_url() ?>assets/js/mdb.min.js"></script>
    <script src="<?php echo base_url() ?>d3_assets/d3.v3.min.js"></script>
    <script src="<?php echo base_url() ?>d3_assets/gantt-chart-d3.js?v=1.03"></script>

    <script src="<?php echo base_url() ?>gauge_assets/Gauge.min.js"></script>
    <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.20.1/moment.js"></script>

    <!-- For modal dialog -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.css" />

    <!-- Script for Mute button -->
    <script>
        $("#btnMute").click(function() {

            if ($("#cameraViewer").prop('muted')) {
                $("#cameraViewer").prop('muted', false);
                // $(this).removeClass('mute-video');
                // $(this).addClass('unmute-video');
                $(this).html(`<i class="fas fa-volume-up"></i>`);

            } else {
                $("#cameraViewer").prop('muted', true);
                // $(this).removeClass('unmute-video');
                // $(this).addClass('mute-video');
                $(this).html(`<i class="fas fa-volume-mute"></i>`);
            }
            console.log($("#cameraViewer").prop('muted'))
        });
    </script>

    <script>
        var statusData = <?php echo json_encode($status_data); ?>;
        var hstData = {};
        var ganttChart;
        var diff_time = 0;
        d = new Date();
        localTime = d.getTime();
        localOffset = d.getTimezoneOffset() * 60000;

        function getTimeStartEnd(tmpDate, offset, diff) {

            var sel_date_start = tmpDate;
            if (tmpDate == "now" || tmpDate == "") {
                var today = new Date();
                today.setTime(today.getTime() + offset + diff * 1000);
                sel_date_start = (today.getMonth() + 1) + '/' + today.getDate() + '/' + today.getFullYear();
            }

            var sel_date_end = sel_date_start + " 23:59:59";
            sel_date_start += " 00:00:00";

            sel_date_end = moment(sel_date_end);
            sel_date_start = moment(sel_date_start);
            return {
                "sel_date_start": sel_date_start,
                "sel_date_end": sel_date_end,
            }
        }


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

        getDetailData();
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
        var sel_date = "now";

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
                    // console.log("get_machine_detail_data_today");
                    // console.log(msg);
                    hstData = msg[0];

                    if (hstData == null) return;

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
                    //console.log("get_timezone");
                    //console.log(info);
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
                    //console.log("get_ganttData");
                    //console.log(msg);
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
                    // console.log("get_allHstData");
                    // console.log(msg);

                    SetHistoryList(msg);
                },
                error: function(err) {
                    console.log(err);
                }
            })
        }
        $(document).ready(function() {

            $('#machine_picture').attr('src', "<?php echo base_url(); ?>images/machine/" + statusData['machine_picture_url']);
            var operatorName = (typeof(statusData.Operator) != "undefined" && statusData.Operator !== null) ? statusData.Operator : "";
            var operator_picture_url = (typeof(statusData.operator_picture_url) != "undefined" && statusData.operator_picture_url !== null && statusData.operator_picture_url !== "") ? statusData.operator_picture_url : "blank.jpg";
            operator_picture_url = "<?php echo base_url(); ?>images/photo/" + operator_picture_url;
            $('#operator_picture').attr('src', operator_picture_url);
            $('#operator_name').html(operatorName);

            /*
                        var video = document.getElementById('cameraViewer');
                        if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
                            // Not adding `{ audio: true }` since we only want video now
                            navigator.mediaDevices.getUserMedia({
                                video: true
                            }).then(function(stream) {
                                video.src = window.URL.createObjectURL(stream);
                                video.srcObject = stream;
                                video.play();
                            });
                        }
            */

            $('#cameraViewer').on('click', function() {
                $("#ex1").modal({
                    escapeClose: true,
                    clickClose: true,
                    showClose: false
                });
            });

            $('#gantt_chart_detail').on('click', function() {
                $('#ganttDetail').modal({
                    escapeClose: true,
                    clickClose: true,
                    showClose: false
                });
            });

            $('#btnBack').on('click', function() {
                // window.location.href = "<?php echo base_url(); ?>plant_page/1";
                window.history.back();
            });

            $('#cameraViewer').get(0).play();
            $('#cameraViewer1').get(0).play();
        });

        function DrawCharts(data) {

            if (data == undefined) return;

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
            // var pie_chart_color_array = ['#4c9d2f', '#d8292f', '#8a8a8a', '#00008B', '#B8860B', '#006400', '#9400D3', '#FFD700', '#ADFF2F', '#FF69B4', '#ADD8E6'];
            var pie_chart_color_array = ['#00a648', '#ce101d', '#91969e', '#ff9500', '#0008ff', '#d400ff', '#ffee00', '#ffee00', '#00d5ff', '#FFD700', '#030000'];

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
                font: {
                    color: '#FFF'
                }
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
            switch (status.toLowerCase()) {
                case "in cycle":
                case "in cycle - monitoring":
                    $('#gantt_detail_status').addClass('statusLabel_green');
                    color = '#5DB501';
                    break;
                case "idle - uncategorized":
                case "uncategorized":
                    $('#gantt_detail_status').addClass('statusLabel_red');
                    color = '#FF0000';
                    break;
                case "offline":
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
            console.log(data.length);

            if (data.length == 0) {
                $('#gantt_chart_detail').hide();
                return;
            } else {
                $('#gantt_chart_detail').show();
            }


            //console.log(id);
            var limit = getTimeStartEnd(sel_date, localOffset, diff_time);
            var sel_date_start = limit['sel_date_start'];
            var sel_date_end = limit['sel_date_end'];

            var updated_data = [];
            for (i = 0; i < data.length - 1; i++) {
                var connected = false;
                if (data[i].status.toUpperCase() == data[i + 1].status.toUpperCase()) {
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
                    "startDate": new Date((parseInt(updated_data[i].start) + diff_time) * 1000 + localOffset),
                    "endDate": new Date((parseInt(updated_data[i].end) + diff_time) * 1000 + localOffset),
                    // "startDate": new Date((parseInt(updated_data[i].start)) * 1000 + localOffset),
                    // "endDate": new Date((parseInt(updated_data[i].end)) * 1000 + localOffset),
                    "color": updated_data[i].color,
                    "taskName": updated_data[i].machine_id,
                    "status": updated_data[i].status.toUpperCase()
                };

                if (i == 0) {

                    var start = {
                        "startDate": new Date(sel_date_start.valueOf()),
                        "endDate": new Date(sel_date_start.valueOf() + 1),
                        // "startDate": new Date((parseInt(updated_data[i].start)) * 1000 + localOffset),
                        // "endDate": new Date((parseInt(updated_data[i].end)) * 1000 + localOffset),
                        "color": "#1e1e1e",
                        "taskName": updated_data[i].machine_id,
                        "status": updated_data[i].status.toUpperCase()
                    };

                    var end = {
                        "startDate": new Date(sel_date_end.valueOf() - 1),
                        "endDate": new Date(sel_date_end.valueOf()),
                        // "startDate": new Date((parseInt(updated_data[i].start)) * 1000 + localOffset),
                        // "endDate": new Date((parseInt(updated_data[i].end)) * 1000 + localOffset),
                        "color": "#1e1e1e",
                        "taskName": updated_data[i].machine_id,
                        "status": updated_data[i].status.toUpperCase()
                    };
                    tasks.push(start);
                    tasks.push(end);
                }
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
            // console.log("last one", tasks[tasks.length - 2]);
            // console.log("before last one", tasks[tasks.length - 3]);
            
            $('#gantt_detail_status').html(tasks[tasks.length - 2].status.toUpperCase());
            SetStatusLabel(tasks[tasks.length - 2].status.toUpperCase());
            $('#gantt_detail_start').html(moment(tasks[tasks.length - 2].startDate).format('YYYY-MM-DD HH:mm:ss'));
            $('#gantt_detail_duration').html((((tasks[tasks.length - 2].endDate - tasks[tasks.length - 2].startDate) / 1000) + "").toHHMMSS());

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

        var historyString = "";

        function SetHistoryList(data) {

            historyListString = "<option value='now'>Real Time</option>";


            var today = new Date();
            for (i = 0; i < 30; i++) {
                var current = new Date(today.getTime() - (i * 24 * 60 * 60 * 1000));
                var strDate = (current.getMonth() + 1) + "/" + (current.getDate()) + "/" + (current.getFullYear());
                historyListString += `<option value="` + strDate + `">` + strDate + `</option>`;
            }


            // for (i = 0; i < data.length; i++) {
            //     if (data[i]['date'] >= nowDate()) {
            //         // console.log(data[i]['date'] + ": " + nowDate());
            //         continue;
            //     }
            //     historyListString += `<option value="` + data[i]['date'] + `">` + data[i]['date'] + `</option>`;
            // }


            if (historyString == historyListString) return;

            if (historyString != historyListString) {
                historyString = historyListString;
            }

            $('#historyList').html(historyListString);
            $("#historyList").val($("#historyList option:first").val());

            // var options = $('#historyList option');
            // var arr = options.map(function(_, o) {
            //     return {
            //         t: $(o).text(),
            //         v: o.value
            //     };
            // }).get();
            // arr.sort(function(o1, o2) {
            //     return o1.t < o2.t ? 1 : o1.t > o2.t ? -1 : 0;
            // });
            // options.each(function(i, o) {
            //     //console.log(i);
            //     o.value = arr[i].v;
            //     $(o).text(arr[i].t);
            // });

            $("#historyList").val($("#historyList option:first").val());

        }
        var sel_date = "now";
        $("#historyList")
            .change(function() {
                var str = "";
                var selValue = "now";
                $("select option:selected").each(function() {
                    str = $(this).text();
                    selValue = $(this).val();
                });
                $("#txtCurrentDate").html(str);

                // console.log(selValue);
                $('#ganttPage').attr('src', "<?php echo base_url(); ?>ganttDetail/<?php echo $machine_id; ?>/" + selValue.replace(/\//g, "_"));
                sel_date = selValue;
                if (selValue != "now") {
                    clearInterval(realtimeInterval);
                    getDetailData(selValue);

                } else {
                    getDetailData();
                    realtimeInterval = setInterval(function() {
                        if (run_mode == 1)
                            getDetailData();
                    }, 5000);
                }
            })
            .change();
    </script>
</body>

</html>