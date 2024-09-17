</div>
</div>

<div class="modal" id="pageSetting" role="dialog" style="background:#1d1d1d; padding:1% 3%!important; width:90%!important;overflow: initial; width:500px!important">
	<input type="hidden" id="emailCount" value='0'>

	<div class="row" style="width:460px!important">
		<div class="row">
			<div style="text-align:center">
				<h2 class="white-text">Settings</h2>
			</div>
		</div>
		<div style="margin-top:20px!important">
			<div class="row">
				<div class="col-xs-3">
					<a class="white-text">Email: </a>
				</div>
				<div class="col-xs-6">
					<input type="email" id="txtEmail" required>
				</div>

				<div class="col-xs-3">
					<button type="button" class="btn btn-primary" id="btnAddEmail"> Add</button>
				</div>
			</div>
			<hr>
			<div class="row" id="emailList">

			</div>
		</div>
		<div style="margin-top:20px!important">
			<button class="btn btn-dark-green" style="float:left; font-size:14px" id="btnSettingSave">Save</button>
			<button class="btn btn-danger" style="float:right; font-size:14px; margin-right:10px!important" id="btnSettingClose">Cancel</button>
		</div>
	</div>
</div>

<!-- JQuery -->
<script type="text/javascript" src="<?php echo base_url() ?>assets/js/jquery-3.4.0.min.js"></script>
<!-- Bootstrap tooltips -->
<script type="text/javascript" src="<?php echo base_url() ?>assets/js/popper.min.js"></script>
<!-- Bootstrap core JavaScript -->
<script type="text/javascript" src="<?php echo base_url() ?>assets/js/bootstrap.min.js"></script>
<!-- MDB core JavaScript -->
<script type="text/javascript" src="<?php echo base_url() ?>assets/js/mdb.min.js"></script>
<script type="text/javascript" src="<?php echo base_url() ?>assets/js/addons/datatables.min.js"></script>

<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.min.js"></script> -->

<!-- jQuery Modal -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.css" />


<script src="<?php echo base_url() ?>pagination_assets/jquery.pajinatify.js"></script>
<script src="<?php echo base_url() ?>d3_assets/d3.v3.min.js"></script>
<script src="<?php echo base_url() ?>d3_assets/gantt-chart-d3.js?v=2.7"></script>
<script src="<?php echo base_url() ?>gauge_assets/Gauge.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.20.1/moment.js"></script>

<script>
	$('#btnSettingSave').on('click', function() {

		//emailList
		emailListString = "";

		for (i = 0; i < $('#emailCount').val(); i++) {
			try {
				if ($('#email_' + i).text() == "") continue;
				emailListString += $('#email_' + i).text();
				if ($('#email_check_enabled_' + i).is(":checked") == false) {
					emailListString += ":0";
				}
				emailListString += ";";

			} catch (err) {}
		}
		emailListString = emailListString.trim(';');
		//console.log(emailListString);

		$.ajax({
			method: "POST",
			url: "<?php echo base_url(); ?>/api/setEmailSetting",
			data: JSON.stringify({
				emailString: emailListString
			}),
			dataType: "json",
			success: function(msg) {
				closeSettingDialog();
			},
			error: function(err) {
				console.log(err);
			}
		})

	});

	$('#btnAddEmail').on('click', function() {
		mailCount = $('#emailCount').val();
		// console.log(mailCount);
		str_emailList = $('#emailList').html();
		str_emailList += getEmailItemString(mailCount, $('#txtEmail').val(), "checked");
		$('#emailList').html(str_emailList);
		$('#emailCount').val(parseInt(mailCount) + 1);

		$('#txtEmail').val("");
		// console.log($('#emailCount').val());
	});

	$('#btnSettingClose').on('click', function() {
		closeSettingDialog();
	});

	function closeSettingDialog() {
		$.modal.close();
		isSettingOpened = false;
	}

	$('#email_check_enabled_0').on('click', function() {
		// console.log($("#email_check_enabled_0").is(":checked"));
	});

	$('#emailList').on('click', '.btnEmailRemove', function() {
		id = $(this).attr('id');
		id = id.replace('email_delete_', '');
		$('#emailConfig' + id).remove();
	})
	var isSettingOpened = false;
	$('#btnSetting').on('click', function() {
		$.ajax({
			method: "POST",
			url: "<?php echo base_url(); ?>/api/get_customer_detail",
			dataType: "json",
			success: function(msg) {
				makeSettingPage(msg[0].emails.split(';'));

				$('#pageSetting').modal({
					escapeClose: false,
					clickClose: true,
					showClose: false,
				});
				isSettingOpened = true;

			},
			error: function(err) {
				console.log(err);
			}
		})
	});

	function makeSettingPage(data) {

		if (isSettingOpened == true)
			return;
		$('#emailCount').val(data.length);
		//console.log(data);
		$('#emailList').html("");
		str_emailList = "";

		for (i = 0; i < data.length; i++) {
			email = data[i].split(':')[0];
			if (email == "") continue;
			checked = "checked";
			if (data[i].split(':').length != 0 && data[i].split(':')[1] == 0)
				checked = "";
			str_emailList += getEmailItemString(i, email, checked);
		}
		$('#emailList').html(str_emailList);
	}

	function getEmailItemString(id, email, checked) {
		return `<div id="emailConfig` + id + `" class="emailItem">
					<div class="col-xs-6 white-text" id="email_` + id + `">` + email + `</div>
					<div class="col-xs-3">
						<label class="switch" style="height:20px">
							<input type="checkbox" id="email_check_enabled_` + id + `" ` + checked + `>
							<span class="slider round"></span>
						</label>
					</div>
					<div class="col-xs-3">
						<a class="fas fa-trash-alt btnEmailRemove" style="color:#B30101; font-size:20px; text-shadow: -1px 0 #C4C4C4, 0 1px #C4C4C4, 1px 0 #C4C4C4, 0 -1px #C4C4C4;" id="email_delete_` + id + `"></a>					</div>
				</div>`
	}
</script>
<script>
	$(document).ready(function() {
		var container_height = $(window).height() - $('#top-navbar').height();
		$('#main-content').height(container_height - 70);
	});

	$('#chkScroll').click(function() {

		// console.log("Content height:=" + $('#content').height());
		abortTimer();
		if (!($(this).is(':checked'))) {
			return;
		}
		//mycode();	
		makeScroll();
	});

	var scrollSpeed = 50;

	function setSliderSpeed(spd) {
		abortTimer();
		scrollSpeed = spd;
		//$('#labelSpeed').html("Scroll Speed(" + scrollSpeed + "s)");
		$('#chkScroll').prop('checked', false);
		//mycode();
		// console.log("ScrollHeight:="+$('#content').prop("scrollHeight"));
	}

	function makeScroll() {
		abortTimer();
		// console.log(scrollSpeed);
		mycode();
		tid = setInterval(mycode, (110 - scrollSpeed) * 1000);
	}

	//var tid;
	//tid = setInterval(mycode, 1000);
	var tid; // = setTimeout(mycode, scrollSpeed * 1000);
	function mycode() {
		// do some stuff...
		// no need to recall the function (it's an interval, it'll loop forever)
		$("#content").stop();
		// console.log("start scrolling");
		// $("#content").animate({scrollTop:0}, {duration:1000});
		// console.log("scroll to top");
		// $("#content").animate({scrollTop:$('#content').prop("scrollHeight")}, {duration: (100-scrollSpeed-3)*1000});


		$("#content").animate({
			scrollTop: 0
		}, {
			duration: 1000,
			queue: false,
			easing: "swing",
			complete: function() {
				// console.log("complete top. ScrollHeight:= "+$('#content').prop("scrollHeight"));
				$("#content").animate({
					scrollTop: $('#content').prop("scrollHeight") - $('#content').height()
				}, {
					duration: (110 - scrollSpeed) * 1000,
					queue: false,
					easing: "swing",
					complete: function() {
						//console.log("complete");
						if ($('#chkScroll').prop('checked') == true) {
							console.log("start again");
						}
					}
				});
			}
		});


		// if ($('#chkScroll').prop('checked') == true) {
		// 	tid = setTimeout(mycode, (scrollSpeed + 3) * 1000);
		// } 

	}

	function abortTimer() { // to be called when you want to stop the timer
		clearInterval(tid);
		$("#content").stop();
	}
</script>

<script>
	var taskArray = [];
	var ganttArray = [];
	var gaugeArray = [];

	//DrawGauge();
	function DrawGauge() {
		for (i = 0; i < statusList.length; i++) {

			var opts = {
				lines: 12,
				angle: -0.15,
				lineWidth: 0.25,
				pointer: {
					length: 0.6,
					strokeWidth: 0.035,
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
					[0.75, "#00ff00"],
					[0.1, "#00ff00"]
				], // !!!!
				strokeColor: '#E0E0E0',
				generateGradient: true
			};
			var target = document.getElementById('gauge_' + statusList[i].id); // your canvas element
			var gauge = new Gauge(target).setOptions(opts); // create sexy gauge!

			gauge.maxValue = 100; // set max gauge value
			gauge.setMinValue(0); // Prefer setter over gauge.minValue = 0
			gauge.animationSpeed = 11; // set animation speed (32 is default value)
			gauge.set(statusList[i].Utilization); // set actual value
			$('#gauge_text_' + statusList[i].id).html(statusList[i].Utilization);
		}
	}

	function DrawGauge_one(id, util) {

		if (gaugeArray[id] == undefined) {
			var opts = {
				lines: 12,
				angle: -0.15,
				lineWidth: 0.25,
				pointer: {
					length: 0.6,
					strokeWidth: 0.035,
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
					[0.75, "#00ff00"],
					[0.1, "#00ff00"]
				], // !!!!
				strokeColor: '#E0E0E0',
				generateGradient: true
			};
			var target = document.getElementById('gauge_' + id); // your canvas element
			var gauge = new Gauge(target).setOptions(opts); // create sexy gauge!
			gauge.maxValue = 100; // set max gauge value
			gauge.setMinValue(0); // Prefer setter over gauge.minValue = 0
			gauge.animationSpeed = 11; // set animation speed (32 is default value)
			gauge.set(util); // set actual value
			gaugeArray[id] = gauge;
			$('#gauge_text_' + id).html(util + "%");
		} else {
			//console.log(gaugeArray[id]);
			gaugeArray[id].set(util);
			$('#gauge_text_' + id).html(util + "%");
		}
	}

	var diff_time = 0;
	$.ajax({
		method: "GET",
		url: "<?php echo base_url(); ?>api/get_timezone",
		dataType: "json",
		success: function(info) {
			diff_time = parseFloat(info[0].timezone) * 3600;
		}
	});
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

	function drawGanttChart_one(id, data, info, isredraw = false) {

		var tasks = [];
		if (data.length == 0) return;

		var limit = getTimeStartEnd("now", localOffset, diff_time);
		var sel_date_start = limit['sel_date_start'];
		var sel_date_end = limit['sel_date_end'];

		// console.log(data.length);
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
		// var diff_time = parseFloat(info[0].timezone) * 3600;
		for (i = 0; i < updated_data.length; i++) {
			var item = {
				"startDate": new Date((parseInt(updated_data[i].start) + diff_time) * 1000 + localOffset),
				"endDate": new Date((parseInt(updated_data[i].end) + diff_time) * 1000 + localOffset),
				// "startDate": new Date((parseInt(updated_data[i].start)) * 1000 + localOffset),
				// "endDate": new Date((parseInt(updated_data[i].end)) * 1000 + localOffset),
				"color": updated_data[i].color,
				"taskName": updated_data[i].machine_id,
				"status": updated_data[i].status
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
		//console.log(tasks);
		if (updated_data.length == 0) {
			var item = {
				"startDate": "",
				"endDate": "",
				"color": "#555",
				"taskName": "",
				"status": ""
			};
			tasks.push(item);
		}

		var taskStatus = {
			"In Cycle": "incycle",
			"Tooling": "tooling",
			"Idle - Uncategorized": "idleuncategorized",
			"Wait Material": "waitmaterial",
			"Offline": "offline"
		};

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
		if (ganttArray[id] == undefined) {
			var gantt = d3.gantt("#chartContainer_" + id, $("#chartContainer_" + id).width()).Container("#chartContainer_" + id).taskTypes(taskNames).tickFormat(format);
			gantt(tasks);
			ganttArray[id] = gantt;
		} else {
			//var gantt = d3.gantt("#chartContainer_"+id).width($("#chartContainer_"+id).width()).taskTypes(taskNames).tickFormat(format).redraw(tasks);
			//console.log("Width " + i + " : " + $("#chartContainer_" + i).width());
			//ganttArray[id].width($("#chartContainer_" + id).width()).Container("#chartContainer_" + id).redraw(tasks);

			//ganttArray[id].Container("#chartContainer_" + id).width($("#chartContainer_" + id).width()).taskTypes(taskNames).tickFormat(format).redraw(tasks);
			ganttArray[id].Container("#chartContainer_" + id).taskTypes(taskNames).tickFormat(format).redraw(tasks);
			//return;
		}
		//console.log(ganttArray[id].Container);
		taskArray[id] = tasks;

		return tasks[tasks.length - 2].status;
	}
</script>

<script>
	/*$(window).resize(function() {
		$(".chart").remove();
		//drawGanttChart();
	});
	*/

	$(document).ready(function() {
		$('.machineItem').on('click', function() {
			id = $(this).attr('id');
			if (!$('#sidebar').hasClass('active')) {
				$('#sidebar').addClass('active');
				$('#sidebarCollapse').addClass('active');
			}

			setTimeout(function() {
				window.location.href = "<?php echo base_url(); ?>machineDetail/" + id;
			}, 500);

		});

		var myInterval;

		if (page == 1) {
			getContent();
			myInterval = setInterval(function() {
				getContent();
			}, 5000);
		}

		$('#sidebarCollapse').on('click', function() {
			$('#sidebar').toggleClass('active');
			$(this).toggleClass('active');
		});

		$('#sidebar ul li').click(function() {
			$('#sidebar ul li').removeClass("selected");
			$(this).addClass("selected");
		})

		function getContent() {
			//$(".chart").remove();
			//get_content
			$.ajax({
				method: "POST",
				url: "<?php echo base_url(); ?>api/get_content",
				success: function(msg) {
					//console.log(msg);
					ShowPlantStatus(JSON.parse(msg));
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
					makeSettingPage(msg[0].emails.split(';'));
					//$('#title_username').html("<i class='fas fa-user' ></i> "+msg[0].name);
				},
				error: function(err) {
					console.log(err);
				}
			})
		}

		function ShowPlantStatus(data) {
			//plantContainer
			html_str = "";
			for (i = 0; i < data.length; i++) {
				GetOnePlantHtml(data[i], i);
			}
			//$("#plantContainer").html(html_str);
			//DrawCahrt(data);
		}

		function GetOnePlantHtml(item, no) {

			$.ajax({
				method: "POST",
				url: "<?php echo base_url(); ?>api/get_machine_detail_data_today",
				data: JSON.stringify({
					machine_id: item.machine_id
				}),
				dataType: "json",
				success: function(msg) {
					if (msg.length > 0 && msg[0].Utilization != null) {
						DrawGauge_one(item.id, msg[0].Utilization);
					} else {
						DrawGauge_one(item.id, 0);
					}
					//DrawGauge_one(item.id, item.Utilization);
				},
				error: function(err) {
					console.log(err);
				}
			});


			$.ajax({
				method: "POST",
				url: "<?php echo base_url(); ?>api/get_ganttData",
				data: JSON.stringify({
					machine_id: item.machine_id
				}),
				dataType: "json",
				success: function(msg) {
					//console.log(msg);
					var status = drawGanttChart_one(item.id, msg);
					drawStatus(item, status);
				},
				error: function(err) {
					console.log(err);
				}
			});

			machine_name = item.machine_name;
			machine_picture_url = "<?php echo base_url(); ?>images/machine/" + item.machine_picture_url;
			//console.log(item.id+" Updating. image_url:=" + machine_picture_url);


			Operator = (typeof(item.Operator) != "undefined" && item.Operator !== null) ? item.Operator : "";
			operator_picture_url = (typeof(item.operator_picture_url) != "undefined" && item.operator_picture_url !== null && item.operator_picture_url !== "") ? item.operator_picture_url : "blank.jpg";
			operator_picture_url = "<?php echo base_url(); ?>images/photo/" + operator_picture_url;
			Utilization = (typeof(item.Utilization) != "undefined" && item.Utilization !== null) ? item.Utilization : "0";

			$("#machine_name_" + item.id).html(machine_name);
			$("#machine_picture_url_" + item.id).attr("src", machine_picture_url);
			$("#operator_" + item.id).html(Operator);
			$("#operator_picture_url_" + item.id).attr("src", operator_picture_url);
			$("#progress_bar_" + item.id).css("width", Utilization + "%");

		}

		function drawStatus(item, status) {
			statusHtml = "";
			switch (item.status.toLowerCase()) {
				case "in cycle":
					statusHtml += `<img src="<?php echo base_url(); ?>images/status/gruen_39px.png" class="img img-responsive" style="float:center;"  align="middle"/>`;
					break;
				case "in cycle - monitoring":
					statusHtml += `<img src="<?php echo base_url(); ?>images/status/gruen_39px.png" class="img img-responsive" style="float:center;"  align="middle"/>`;
					break;

				case "idle - uncategorized":
					statusHtml += `<img src="<?php echo base_url(); ?>images/status/rot_39px.png" class="img img-responsive" style="float:center;" align="middle"/>`;
					break;
				case "uncategorized":
					statusHtml += `<img src="<?php echo base_url(); ?>images/status/rot_39px.png" class="img img-responsive" style="float:center;" align="middle"/>`;
					break;
				case "offline":
					statusHtml += `<img src="<?php echo base_url(); ?>images/status/grau_39px.png" class="img img-responsive" style="float:center;"  align="middle"/>`;
					break;
				default:
					statusHtml += `<img src="<?php echo base_url(); ?>images/status/gelb_39px.png" class="img img-responsive" style="float:center;"  align="middle"/>`;
					break;
			}
			$('#status_image_' + item.id).html(statusHtml);
			$('#status_' + item.id).html(status);
		}
	});
</script>

</body>

</html>