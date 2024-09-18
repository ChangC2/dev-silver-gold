<!DOCTYPE html>
<?php ini_set('default_charset', 'utf-8'); ?>

<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>SLYTRACKR</title>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css">
    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.css">
    <!-- Material Design Bootstrap -->
    <link href="<?php echo base_url() ?>assets/css/mdb.min.css" rel="stylesheet">
    <!-- Your custom styles (optional) -->

    <link href="<?php echo base_url() ?>assets/css/style.css" rel="stylesheet">
    <!-- <link href="<?php echo base_url() ?>assets/css/mystyle.css" rel="stylesheet"> -->


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

        @media only mobile and (max-width:400) {
            .infolayer {
                display: none;
            }
        }
    </style>

    <style>
        body {
            -ms-overflow-style: none;
            scrollbar-width: none;
        }

        body::-webkit-scrollbar {
            display: none;
        }
    </style>
</head>

<body>

    <div id="gantt_chart_detail" style="width:95%; height:90%"></div>
    <script>
        var machine_data = <?php echo json_encode($data); ?>;

        // console.log(machine_data);
    </script>



    <!-- JQuery -->
    <script type="text/javascript" src="<?php echo base_url() ?>assets/js/jquery-3.4.0.min.js"></script>
    <!-- Bootstrap tooltips -->
    <script type="text/javascript" src="<?php echo base_url() ?>assets/js/popper.min.js"></script>
    <!-- Bootstrap core JavaScript -->
    <script type="text/javascript" src="<?php echo base_url() ?>assets/js/bootstrap.min.js?v=1.0"></script>
    <!-- MDB core JavaScript -->
    <script type="text/javascript" src="<?php echo base_url() ?>assets/js/mdb.min.js"></script>
    <script type="text/javascript" src="<?php echo base_url() ?>assets/js/addons/datatables.min.js"></script>

    <script src="<?php echo base_url() ?>pagination_assets/jquery.pajinatify.js"></script>
    <!-- <script src="<?php echo base_url() ?>d3_assets/d3.v3.min.js"></script> -->
    <script src="<?php echo base_url() ?>d3_assets/d3.v4.min.js?v=2.0"></script>
    <!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/d3/4.11.0/d3.min.js"></script> -->

    <script src="<?php echo base_url() ?>d3_assets/gantt-chart-d3-custom.js?v=4.06"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/d3-tip/0.8.0-alpha.1/d3-tip.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.20.1/moment.js"></script>

    <script>
        d = new Date();
        localTime = d.getTime();
        localOffset = d.getTimezoneOffset() * 60000;

        var run_mode = 1; // 1: real time mode ,    	2: historical chart view mode
        var sel_date = "<?php echo $sel_date; ?>";
        if (sel_date != "now" || sel_date != "") {
            run_mode = 2;
        }


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



        //console.log(sel_date_end);
        //console.log(sel_date_end.valueOf());
    </script>


    <script>
        getDetail(sel_date);
        if (run_mode == 1) {
            setInterval(function() {
                getDetail(sel_date);
            }, 5000);
        }



        var ganttArray;

        function calcTime(offset) {
            var d = new Date();
            var utc = d.getTime() + (d.getTimezoneOffset() * 60000);
            var nd = new Date(utc + (3600000 * offset));
            return nd;
        }

        var tmp_index = 0;

        function getDetail(sel_date) {
            $.ajax({
                method: "POST",
                url: "<?php echo base_url(); ?>api/get_ganttData",
                data: JSON.stringify({
                    machine_id: machine_data.machine_id,
                    sel_date: sel_date
                }),
                dataType: "json",
                success: function(msg) {
                    // console.log("GanttData");
                    // console.log(msg);
                    //console.log(ganttArray  == JSON.stringify(msg));

                    if (!(ganttArray == JSON.stringify(msg))) {

                        $.ajax({
                            method: "GET",
                            url: "<?php echo base_url(); ?>api/get_timezone",
                            dataType: "json",
                            success: function(info) {
                                // console.log(info);
                                tmp_info = info;
                                $('#gantt_chart_detail').html("");
                                ganttArray = JSON.stringify(msg);
                                drawGanttChart(msg, info);
                            }
                        });
                    }
                },
                error: function(err) {
                    console.log(err);
                }
            });
        }
    </script>

    <script>
        // round value
        function round(value, exp) {
            if (typeof exp === 'undefined' || +exp === 0)
                return Math.round(value);

            value = +value;
            exp = +exp;

            if (isNaN(value) || !(typeof exp === 'number' && exp % 1 === 0))
                return NaN;

            // Shift
            value = value.toString().split('e');
            value = Math.round(+(value[0] + 'e' + (value[1] ? (+value[1] + exp) : exp)));

            // Shift back
            value = value.toString().split('e');
            return +(value[0] + 'e' + (value[1] ? (+value[1] - exp) : -exp));
        }
    </script>

    <script>
        var taskArray = [];
        var ganttData = [];
        //drawPieChart();


        function drawGanttChart(data, info, isredraw = false) {
            //console.log(data);
            if (data.length == 0) return;
            var diff_time = parseFloat(info[0].timezone) * 3600;

            // console.log(sel_date, localOffset, diff_time)
            var limit = getTimeStartEnd(sel_date, localOffset, diff_time);
            var sel_date_start = limit['sel_date_start'];
            var sel_date_end = limit['sel_date_end'];

            // console.log(limit);

            operator = machine_data.Operator;
            var tasks = [];
            var taskNames = ["IN CYCLE", "UNCATEGORIZED", "OFFLINE"];
            //if(data.length == 0) return;
            //console.log(id);

            

            var updated_data = [];
            for (i = 0; i < data.length - 1; i++) {
                var connected = false;

                if (data[i].status.toUpperCase() == data[i + 1].status.toUpperCase()) {
                    if (data[i + 1].start > data[i].start)
                        data[i + 1].start = data[i].start;

                    if (data[i + 1].end < data[i].end)
                        data[i + 1].end = data[i].end;

                    connected = true;
                }
                if (connected == false)
                    updated_data.push(data[i]);
            }
            updated_data.push(data[data.length - 1]);

            //console.log("localoffset:=" + localOffset);
            
            //console.log('last data= ');
            //console.log(updated_data[updated_data.length-1]);

            for (i = 0; i < updated_data.length; i++) {
                updated_data[i].show_status = updated_data[i].status.toUpperCase();

                if (updated_data[i].status.toLowerCase() == "idle - uncategorized") {
                    updated_data[i].show_status = updated_data[i].status;
                    updated_data[i].status = "UNCATEGORIZED";
                }

                if (updated_data[i].status.toLowerCase() == "in cycle - monitoring") {
                    updated_data[i].show_status = updated_data[i].status;
                    updated_data[i].status = "IN CYCLE";
                }


                if (updated_data[i].start > updated_data[i].end) {
                    var tmp = updated_data[i].start;
                    updated_data[i].start = updated_data[i].end;
                    updated_data[i].end = tmp;
                }

                var item = {
                    "startDate": new Date((parseInt(updated_data[i].start) + diff_time) * 1000 + localOffset),
                    "endDate": new Date((parseInt(updated_data[i].end) + diff_time) * 1000 + localOffset),
                    // "startDate": new Date((parseInt(updated_data[i].start)) * 1000 + localOffset),
                    // "endDate": new Date((parseInt(updated_data[i].end)) * 1000 + localOffset),
                    "color": updated_data[i].color,
                    "taskName": updated_data[i].status.toUpperCase(),
                    "status": updated_data[i].status.toUpperCase(),
                    "MachineName": "123",
                    "operator": operator,
                    "show_status": updated_data[i].show_status.toUpperCase()
                };

                if (i == 0) {
                    // console.log('localOffset', localOffset);
                    // console.log('diff_time', diff_time);
                    // console.log("SET DATE", sel_date);
                    // console.log("Start :", new Date(sel_date_start.valueOf()), updated_data[i].start);
                    // console.log("End :", new Date(sel_date_end.valueOf()), updated_data[i].start);
                    // console.log("Real Start :", item['startDate']);
                    // console.log("Real End :", item['endDate']);
                    // console.log("Color:", item['color']);

                    var start = {
                        "startDate": new Date(sel_date_start.valueOf()),
                        "endDate": new Date(sel_date_start.valueOf() + 1),
                        // "startDate": new Date((parseInt(updated_data[i].start)) * 1000 + localOffset),
                        // "endDate": new Date((parseInt(updated_data[i].end)) * 1000 + localOffset),
                        "color": updated_data[i].color,
                        "taskName": updated_data[i].status.toUpperCase(),
                        "status": updated_data[i].status.toUpperCase(),
                        "MachineName": "123",
                        "operator": operator,
                        "show_status": updated_data[i].show_status.toUpperCase()
                    };

                    var end = {
                        "startDate": new Date(sel_date_end.valueOf() - 1),
                        "endDate": new Date(sel_date_end.valueOf()),
                        // "startDate": new Date((parseInt(updated_data[i].start)) * 1000 + localOffset),
                        // "endDate": new Date((parseInt(updated_data[i].end)) * 1000 + localOffset),
                        "color": updated_data[i].color,
                        "taskName": updated_data[i].status.toUpperCase(),
                        "status": updated_data[i].status.toUpperCase(),
                        "MachineName": "123",
                        "operator": operator,
                        "show_status": updated_data[i].show_status.toUpperCase()
                    };
                    tasks.push(start);
                    tasks.push(end);
                }
                tasks.push(item);
                if (jQuery.inArray(updated_data[i].status.toUpperCase(), taskNames) == -1) {
                    taskNames.push(updated_data[i].status.toUpperCase());
                }
            }
            tasks.sort(function(a, b) {
                return a.endDate - b.endDate;
            });
            var maxDate = tasks[tasks.length - 1].endDate;
            tasks.sort(function(a, b) {
                return a.startDate - b.startDate;
            });
            var minDate = tasks[0].startDate;

            var format = "%H:%M";


            var width_gantt = $("#gantt_chart_detail").width() - 50;
            if ($("#gantt_chart_detail").width() < 414) width_gantt = $("#gantt_chart_detail").width();

            var gantt = d3.gantt(width_gantt, $(document).height())
                .height($(document).height())
                .selector("#gantt_chart_detail").taskTypes(taskNames).taskStatus(taskNames).tickFormat(format);
            gantt(tasks);
        }
    </script>

</body>

</html>