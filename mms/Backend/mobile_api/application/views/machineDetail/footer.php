</div>
</div>
<style>
	ul.pie-legend {
		text-align: center;
		margin-top: 2rem;
	}

	ul.pie-legend li {
		display: inline-block;
		margin-right: 10px;
	}

	ul.pie-legend li span {
		padding: 5px 10px;
		color: #fff;
	}

	.modebar {
		display: none;
	}

	rect.scrollbar {
		fill: transparent !important;
		background: transparent;
	}

	text.legendtext {
		fill: white !important;
	}
</style>

<!-- JQuery -->
<script type="text/javascript" src="<?php echo base_url() ?>assets/js/jquery-3.4.0.min.js"></script>
<!-- Bootstrap tooltips -->
<script type="text/javascript" src="<?php echo base_url() ?>assets/js/popper.min.js"></script>
<!-- Bootstrap core JavaScript -->
<script type="text/javascript" src="<?php echo base_url() ?>assets/js/bootstrap.min.js?v=1.0"></script>
<!-- MDB core JavaScript -->
<script type="text/javascript" src="<?php echo base_url() ?>assets/js/mdb.min.js"></script>
<script type="text/javascript" src="<?php echo base_url() ?>assets/js/addons/datatables.min.js"></script>

<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.min.js"></script> -->

<!-- jQuery Modal -->
<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.css" />
 -->


<script src="<?php echo base_url() ?>pagination_assets/jquery.pajinatify.js"></script>
<!-- <script src="<?php echo base_url() ?>d3_assets/d3.v3.min.js"></script> -->
<script src="<?php echo base_url() ?>d3_assets/d3.v4.min.js"></script>
<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/d3/4.11.0/d3.min.js"></script> -->

<script src="<?php echo base_url() ?>d3_assets/gantt-chart-d3-custom.js?v=4.1"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/d3-tip/0.8.0-alpha.1/d3-tip.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.20.1/moment.js"></script>
<script src="https://cdn.plot.ly/plotly-latest.min.js"></script>

<script>
	var run_mode = 1; // 1: real time mode ,    	2: historical chart view mode
</script>


<script>
	$('#btnShowHistory').click(function() {

		$.ajax({
			method: "GET",
			url: "<?php echo base_url(); ?>api/get_timezone",
			dataType: "json",
			success: function(info) {
				var firstTime = new Date($('#tmrStart').val());
				// console.log(firstTime);
				var diff_epoch = firstTime.getTimezoneOffset()*60;
				// console.log(diff_epoch);
				var seconds = firstTime.getTime() / 1000;
				// console.log(seconds);
				seconds = seconds + diff_epoch;
				// console.log(seconds);
				seconds = seconds + info[0].timezone * 60;
				// console.log(seconds);
				var utcTime = new Date(0);
				// console.log(utcTime);
				utcTime.setUTCSeconds(seconds);
				
				// console.log(utcTime);
				//var date2 = calcTime(parseFloat(info[0].timezone));
			}
		});
/*
		run_mode = 2;
		console.log($('#tmrStart').val());
		var tt = new Date($('#tmrStart').val());
		console.log(tt);
		tt.setMinutes(tt.getMinutes() - 7.5 * 60);
		console.log(tt);
		console.log(tt.getTimezoneOffset());
		*/
	})
</script>


<script>
	getDetailData();
	setInterval(function() {
		if (run_mode == 1)
			getDetailData();
	}, 5000);

	function getDetailData() {
		machine_id = machine_data.machine_id;
		$.ajax({
			method: "POST",
			url: "<?php echo base_url(); ?>api/get_machine_detail_data_today",
			data: JSON.stringify({
				machine_id: machine_id
			}),
			dataType: "json",
			success: function(res) {
				//console.log("hst");
				//console.log(res);
				
				if (res.length == 0) {
					$('#pie_chart_detail').html("");
				} else {
					drawPieChart(res);
				}
				ShowAuxData(res);
			},
			error: function(err) {
				console.log(err);
			}
		});
	}

	function ShowAuxData(res) {
		// auxData1 = 0;
		// auxData2 = 0;
		// auxData3 = 0;
		// for (i = 0; i < res.length; i++) {
		// 	auxData1 += parseInt(res[i].aux1data);
		// 	auxData2 += parseInt(res[i].aux2data);
		// 	auxData3 += parseInt(res[i].aux3data);
		// }
		try {
			$('#status_aux1').html(res[0].aux1data);
		} catch {}
		try {
			$('#status_aux2').html(res[0].aux2data);
		} catch {}
		try {
			$('#status_aux3').html(res[0].aux3data);
		} catch {}
	}
</script>


<script>
	getDetail();
	setInterval(function() {
		if (run_mode == 1)
			getDetail();
	}, 5000);

	d = new Date();
	localTime = d.getTime();
	localOffset = d.getTimezoneOffset() * 60000;

	var ganttArray;

	function calcTime(offset) {

		// create Date object for current location
		var d = new Date();

		// convert to msec
		// add local time zone offset
		// get UTC time in msec
		var utc = d.getTime() + (d.getTimezoneOffset() * 60000);

		// create new Date object for different city
		// using supplied offset
		var nd = new Date(utc + (3600000 * offset));

		// return time as a string
		return nd;
	}

	var tmp_index = 0;

	$('#btnAdd').click(function() {
		$('#gantt_chart_detail').html("");
		var tmpArray = JSON.parse(ganttArray);
		var tmp = [];
		for (i = 0; i < tmp_index + 3; i++) {
			tmp.push(tmpArray[i]);
		}
		drawGanttChart1(tmp, tmp_info);
		tmp_index += 3;
	});

	function getDetail() {
		//console.log("I am called");
		$.ajax({
			method: "POST",
			url: "<?php echo base_url(); ?>api/get_machine_detail",
			data: JSON.stringify({
				id: machine_data.id
			}),
			success: function(msg) {
				//console.log(msg);
				Refresh(JSON.parse(msg));
			},
			error: function(err) {
				console.log(err);
			}
		});

		$.ajax({
			method: "POST",
			url: "<?php echo base_url(); ?>api/get_ganttData",
			data: JSON.stringify({
				machine_id: machine_data.machine_id
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

							var weekday = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
							var month = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

							var date2 = calcTime(parseFloat(info[0].timezone));
							$('#title_time').html(weekday[date2.getDay()] + ", " + month[date2.getMonth()] + " " + date2.getDate());
						}
					});
				}
			},
			error: function(err) {
				console.log(err);
			}
		});

		$.ajax({
			method: "POST",
			url: "<?php echo base_url(); ?>/api/get_customer_detail",
			dataType: "json",
			success: function(msg) {
				//console.log(msg);
				//console.log(msg[0].logo);
				$('#title_logo').attr("src", msg[0].logo);
				//$('#title_username').html("<i class='fas fa-user' ></i> "+msg[0].name);
			},
			error: function(err) {
				console.log(err);
			}
		});
	}

	function Refresh(item) {
		//console.log(item.id+" Updating");

		title = item.machine_id;
		machine_picture_url = "<?php echo base_url(); ?>images/machine/" + item.machine_picture_url;
		status = item.status;
		Operator = (typeof(item.Operator) != "undefined" && item.Operator !== null) ? item.Operator : "";
		operator_picture_url = (typeof(item.operator_picture_url) != "undefined" && item.operator_picture_url !== null && item.operator_picture_url !== "") ? item.operator_picture_url : "blank.jpg";
		operator_picture_url = "<?php echo base_url(); ?>images/photo/" + operator_picture_url;
		Utilization = (typeof(item.Utilization) != "undefined" && item.Utilization !== null) ? item.Utilization : "0";

		$("#title").html(title);
		$("#machine_picture_url").attr("src", machine_picture_url);
		$("#Operator").html(Operator);
		$("#operator_picture_url").attr("src", operator_picture_url);
		$("#status").html(item.status);
		//		$("#progress_bar_").css("width", Utilization+"%");
		//		$("#utilization_").html(Utilization+"%");

		statusHtml = "";

		switch (item.status) {
			case "In Cycle":
				statusHtml += `<img src="<?php echo base_url(); ?>images/status/gruen_39px.png" class="img img-responsive" style="float:center;"  align="middle"/>`;
				break;
			case "In Cycle - Monitoring":
				statusHtml += `<img src="<?php echo base_url(); ?>images/status/gruen_39px.png" class="img img-responsive" style="float:center;"  align="middle"/>`;
				break;
			case "Idle - Uncategorized":
				statusHtml += `<img src="<?php echo base_url(); ?>images/status/rot_39px.png" class="img img-responsive" style="float:center;" align="middle"/>`;
				break;
			case "Uncategorized":
				statusHtml += `<img src="<?php echo base_url(); ?>images/status/rot_39px.png" class="img img-responsive" style="float:center;" align="middle"/>`;
				break;
			case "Offline":
				statusHtml += `<img src="<?php echo base_url(); ?>images/status/grau_39px.png" class="img img-responsive" style="float:center;"  align="middle"/>`;
				break;
			default:
				statusHtml += `<img src="<?php echo base_url(); ?>images/status/gelb_39px.png" class="img img-responsive" style="float:center;"  align="middle"/>`;
				break;
		}
		$('#status_image').html(statusHtml);

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

	function drawPieChart(res) {
		var pie_chart_value_array = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
		var pie_chart_label_array = ['In Cycle', 'Uncategorized', 'Offline', machine_data['r1'], machine_data['r2'], machine_data['r3'], machine_data['r4'], machine_data['r5'], machine_data['r6'], machine_data['r7'], machine_data['r8']];
		var pie_chart_color_array = ['#4c9d2f', '#d8292f', '#8a8a8a', '#00008B', '#B8860B', '#006400', '#9400D3', '#FFD700', '#ADFF2F', '#FF69B4', '#ADD8E6'];

		for (i = 0; i < res.length; i++) {
			pie_chart_value_array[0] += round(res[i]['inCycle'] / 3600);
			pie_chart_value_array[1] += round(res[i]['uncat'] / 3600);
			pie_chart_value_array[2] += round(res[i]['offline'] / 3600);			
			pie_chart_value_array[3] += round(res[i]['r1t'] / 3600);
			pie_chart_value_array[4] += round(res[i]['r2t'] / 3600);
			pie_chart_value_array[5] += round(res[i]['r3t'] / 3600);
			pie_chart_value_array[6] += round(res[i]['r4t'] / 3600);
			pie_chart_value_array[7] += round(res[i]['r5t'] / 3600);
			pie_chart_value_array[8] += round(res[i]['r6t'] / 3600);
			pie_chart_value_array[9] += round(res[i]['r7t'] / 3600);
			pie_chart_value_array[10] += round(res[i]['r8t'] / 3600);			
		}

		//console.log(pie_chart_value_array);

		var sum = pie_chart_value_array.reduce(function(pv, cv) {
			return pv + cv;
		}, 0);

		var digits = 1;
		var rounded_values = [];
		for (var i = 0; i < pie_chart_value_array.length; i += 1) {
			rounded_values.push(Math.round(pie_chart_value_array[i] / sum * 100 * 10 * digits) / 10 * digits + '%');
		}

		var data = [{
			values: pie_chart_value_array,
			labels: pie_chart_label_array,
			type: 'pie',
			sort: false,
			marker: {
				colors: pie_chart_color_array
			},
			text: rounded_values,
			textinfo: 'text'
		}];
		var width_pie = $('#chart_container').width();
		if (width_pie >= 800) width_pie = 800;

		var layout = {
			paper_bgcolor: "transparent",
			width: width_pie,
			margin: {
				l: 0,
				r: 0,
				b: 50,
				t: 50,
				pad: 10
			},
			showlegend: true,
		};
		//console.log(width_pie);
		if (width_pie <= 512) {
			var layout = {
				paper_bgcolor: "transparent",
				width: width_pie,
				margin: {
					l: 30,
					r: 30,
					b: 50,
					t: 50,
					pad: 10
				},
				showlegend: false,

			};
		}
		//console.log(pie_chart_value_array);
		Plotly.newPlot('pie_chart_detail', data, layout, {
			displayModeBar: false
		});
	}
	//drawGanttChart();

	function drawGanttChart(data, info, isredraw = false) {
		//console.log(data);
		if (data.length == 0) return;
		operator = machine_data.Operator;
		var tasks = [];
		var taskNames = ["In Cycle", "Uncategorized", "Offline"];
		//if(data.length == 0) return;
		//console.log(id);

		var diff_time = parseFloat(info[0].timezone) * 3600;
		//console.log(new Date((parseInt(data[0].start) + diff_time)*1000));
		//console.log(new Date(parseInt(data[0].start)*1000 ));

		var updated_data = [];
		for (i = 0; i < data.length - 1; i++) {
			var connected = false;
			/*			for(j = i+1; j < data.length; j++)
						{
							if(data[i].end == data[j].start && data[i].status == data[j].status)
							{
								connected = true;
								data[j].start = data[i].start;
							}
						}
						*/
			if (data[i].status == data[i + 1].status) {
				if (data[i + 1].start > data[i].start)
					data[i + 1].start = data[i].start;

				if (data[i + 1].end < data[i].end)
					data[i + 1].end = data[i].end;

				connected = true;
			}
			if (connected == false)
				updated_data.push(data[i]);
		}
		updated_data.push(data[data.length-1]);
		//console.log('last data= ');
		//console.log(updated_data[updated_data.length-1]);
		// console.log("localoffset:="+localOffset);
		for (i = 0; i < updated_data.length; i++) {
			updated_data[i].show_status = updated_data[i].status;

			if (updated_data[i].status == "Idle - Uncategorized") {
				updated_data[i].show_status = updated_data[i].status;
				updated_data[i].status = "Uncategorized";
			}

			if (updated_data[i].status == "In Cycle - Monitoring") {
				updated_data[i].show_status = updated_data[i].status;
				updated_data[i].status = "In Cycle";
			}


			if (updated_data[i].start > updated_data[i].end) {
				var tmp = updated_data[i].start;
				updated_data[i].start = updated_data[i].end;
				updated_data[i].end = tmp;
			}
			
			var item = {
				// "startDate": new Date((parseInt(updated_data[i].start) + diff_time) * 1000 + localOffset),
				// "endDate": new Date((parseInt(updated_data[i].end) + diff_time) * 1000 + localOffset),
				"startDate": new Date((parseInt(updated_data[i].start) + diff_time) * 1000 + localOffset),
				"endDate": new Date((parseInt(updated_data[i].end) + diff_time) * 1000 + localOffset),
				"color": updated_data[i].color,
				"taskName": updated_data[i].status,
				"status": updated_data[i].status,
				"MachineName": "123",
				"operator": operator,
				"show_status": updated_data[i].show_status
			};

			tasks.push(item);
			if (jQuery.inArray(updated_data[i].status, taskNames) == -1) {
				taskNames.push(updated_data[i].status);
			}
		}
		/*
				var tasks = [{
						"startDate": new Date("Sat Dec 09 00:00:00 CST 2017"),
						"endDate": new Date("Sat Dec 09 07:15:45 CST 2017"),
						"taskName": "r1",
						"status": "Uncategorized",
						"operator": operator,
						"MachineName": ""
					}
				];
		*/


		// var taskStatus = {
		// 	"OFFLINE" : "offline",
		// 	"FAILED" : "tooling",
		// 	"RUNNING" : "incycle",
		// 	"IDLE" : "idleuncategorized"
		// };

		//var taskNames = ["NHX6300 3"];
		// for (i = 0; i < tasks.length; i++) {
		// 	tasks[i].taskName = machine_data[tasks[i].taskName];
		// }

		//		var taskNames = ["In Cycle", "Uncategorized", "Offline", machine_data["r1"], machine_data["r2"], machine_data["r3"], machine_data["r4"], machine_data["r5"], machine_data["r6"], machine_data["r7"], machine_data["r8"]];

		tasks.sort(function(a, b) {
			return a.endDate - b.endDate;
		});
		var maxDate = tasks[tasks.length - 1].endDate;
		tasks.sort(function(a, b) {
			return a.startDate - b.startDate;
		});
		var minDate = tasks[0].startDate;

		var format = "%H:%M";


		//console.log("document : "+$(document).innerHeight()+"   titleContainer : "+$('#titleContainer').outerHeight(true)+"   header-title : " + $('#headertitle').outerHeight(true))
		// console.log("container: " + ($("#gantt_chart_detail").width()-50) + "       document: "+$(document).innerWidth()	);
		// if(ganttData == null)
		// {
		var width_gantt = $("#gantt_chart_detail").width() - 110;
		if ($("#gantt_chart_detail").width() < 414) width_gantt = $("#gantt_chart_detail").width();

		var gantt = d3.gantt(width_gantt, $(window).height() / 2)
			.height($(window).height() / 2)
			.selector("#gantt_chart_detail").taskTypes(taskNames).taskStatus(taskNames).tickFormat(format);
		//ganttData = gantt;
		gantt(tasks);
		// }
		// else{

		// 	ganttData.width($("#gantt_chart_detail").width() - 200)
		// 			//.height($(document).height() - $('#titleContainer').outerHeight(true) - $('#headertitle').outerHeight(true) - 350)					
		// 			.selector("#gantt_chart_detail").taskTypes(taskNames).taskStatus(taskNames).tickFormat(format)
		// 			.redraw(tasks);
		// 	//$('.selection').click();
		// }
	}

	var tmp_info;

	function drawGanttChart1(data, info, isredraw = false) {
		if (data.length == 0) return;
		operator = machine_data.Operator;
		var tasks = [];
		var taskNames = ["In Cycle", "Uncategorized", "Offline"];

		var diff_time = parseFloat(info[0].timezone) * 3600;

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

		for (i = 0; i < updated_data.length; i++) {
			if (updated_data[i].status == "Idle - Uncategorized")
				updated_data[i].status = "Uncategorized";

			if (updated_data[i].start > updated_data[i].end) {
				var tmp = updated_data[i].start;
				updated_data[i].start = updated_data[i].end;
				updated_data[i].end = tmp;
			}

			var item = {
				// "startDate": new Date((parseInt(updated_data[i].start) + diff_time) * 1000 + localOffset),
				// "endDate": new Date((parseInt(updated_data[i].end) + diff_time) * 1000 + localOffset),
				"startDate": new Date((parseInt(updated_data[i].start) + diff_time) * 1000 + localOffset),
				"endDate": new Date((parseInt(updated_data[i].end) + diff_time) * 1000 + localOffset),
				"color": updated_data[i].color,
				"taskName": updated_data[i].status,
				"status": updated_data[i].status,
				"MachineName": "123",
				"operator": operator,
			};
			tasks.push(item);
			if (jQuery.inArray(updated_data[i].status, taskNames) == -1) {
				taskNames.push(updated_data[i].status);
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

		// console.log(updated_data);
		//console.log("document : "+$(document).innerHeight()+"   titleContainer : "+$('#titleContainer').outerHeight(true)+"   header-title : " + $('#headertitle').outerHeight(true))
		// console.log("container: " + ($("#gantt_chart_detail").width()-50) + "       document: "+$(document).innerWidth()	);

		var width_gantt = $("#gantt_chart_detail").width() - 110;
		if ($("#gantt_chart_detail").width() < 414) width_gantt = $("#gantt_chart_detail").width();

		var gantt = d3.gantt(width_gantt, $(window).height() / 2)
			.height($(window).height() / 2)
			.selector("#gantt_chart_detail").taskTypes(taskNames).taskStatus(taskNames).tickFormat(format);

		gantt(tasks);

	}

	var mychart = null;
</script>

<!-- <script type="text/javascript" src="https://cloudboost.io/js-sdk/cloudboost.js"></script> -->

<script>
	$('.fa-backward').click(function() {
		window.location.href = "<?php echo base_url(); ?>plant_page";
	});
</script>

</body>

</html>