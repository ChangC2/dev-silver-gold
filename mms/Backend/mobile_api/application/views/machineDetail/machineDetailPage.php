<div style="margin-left: 15px; text-align:center; width:100%" class="row">
	<div class="col col-sm-12 col-md-2 ">
		<div style="display:flex; flex-direction:row; width:100%; ">
			<img id="operator_picture_url" class="img img-responsive img-circle" style="height: 100px; width:100px" align="middle">
		</div>
		<div style="text-align:center; width:100px">
			<h4 style="color:white" id="Operator"></h4 style="color:white">
		</div>
		<br>
		<br>
		<div>
			<img id="machine_picture_url" class="img img-responsive" align="middle" />
		</div>
		<br>
		<br>
		<div style="display:flex; flex-direction:row; width:100%; ">
			<h5><strong style="color:white">STATUS: &nbsp;&nbsp;</strong></h5>
			<h5 style="color:white" id="status"></h5>
			<div id="status_image">

			</div>
		</div>
		<div class="row">
			<div class="col col-sm-12 " style="display:flex; flex-direction:row; width:100%; ">
				<h5><strong style="color:white"><?php echo $data->aux1; ?>: &nbsp;&nbsp;</strong></h5>
				<h5 style="color:white" id="status_aux1"></h5>
			</div>
			<div class="col col-sm-12 " style="display:flex; flex-direction:row; width:100%; ">
				<h5><strong style="color:white"><?php echo $data->aux2; ?>: &nbsp;&nbsp;</strong></h5>
				<h5 style="color:white" id="status_aux2"></h5>
			</div>
			<div class="col col-sm-12 " style="display:flex; flex-direction:row; width:100%; ">
				<h5><strong style="color:white"><?php echo $data->aux3; ?>: &nbsp;&nbsp;</strong></h5>
				<h5 style="color:white" id="status_aux3"></h5>
			</div>
		</div>

		<!-- <div class="row" style="margin-top:30px">
			<div class="row" style="text-align:left">
				<button type="button" style="width:100%; color:white; background:transparent">Real Time</button>
			</div>

			<div class="row" style="text-align:left; margin-top:20px">
				<label style="color:white; text-align:left">Start Time:</label>
				<input style="color:white; font-weight:bold; background:transparent" type="datetime-local" id="tmrStart" name="meeting-time">
			</div>
			<div class="row" style="text-align:left; margin-top:20px">
				<label style="color:white; text-align:left">End Time:</label>
				<input style="color:white; font-weight:bold; background:transparent" type="datetime-local" id="tmrEnd" name="meeting-time">
			</div>
			<div class="row" style="text-align:right; margin-top:10px">
				<button type="button" style=" color:white; background:transparent" id="btnShowHistory">Show</button>
			</div>
		</div> -->

	</div>
	<style>
		div.relative {
			position: relative;
		}

		div#pie_chart_detail {
			position: absolute;
			left: 50%;
			transform: translateX(-50%);
			-webkit-transform: translateX(-50%);
		}
	</style>
	<div class="col col-sm-12 col-md-10  machine_info " id="chart_container">
		<div class="row" style="margin-bottom:20px" id="titleContainer">
			<a class="text-white" style="font-size: 30px; font-weight: bold; color:white" id="title"></a><br>
			<a class="text-white" style="font-size: 24px; font-weight: bold; color:white" id="title_time"></a>
		</div>
		<div id="gantt_chart_detail"></div>
		<div class="relative">
			<div id="pie_chart_detail" style="margin-top:-150px"></div>
		</div>
	</div>
</div>

<script>
	var machine_data = <?php echo json_encode($data); ?>;
	console.log(machine_data);
</script>