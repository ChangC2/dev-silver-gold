/**
 * Modified version of @author Dimitry Kudrayvtsev
 * @version 2.0
 *
 * Ported to d3 v4 by Keyvan Fatehi on October 16th, 2016
 *
 * Modified by Dianne Boyles 01/08/2018
 *  - added tooltips
 *  - added legend
 *  - added brush
 */
var prevPos = [0, 0];
var prev_endDate = null;
var prev_startDate = null;

d3.gantt = function (w, h) {
	var FIT_TIME_DOMAIN_MODE = "fit";
	var FIXED_TIME_DOMAIN_MODE = "fixed";
    const bottom_margin = 110;
    
	var margin = {
		top: 0,
		right: 25,
		bottom: 110,
		left: 150
	};

	var selector = "body";
	var timeDomainStart = d3.timeDay.offset(new Date(), -3);
	var timeDomainEnd = d3.timeHour.offset(new Date(), +3);
	var timeDomainMode = FIT_TIME_DOMAIN_MODE; // fixed or fit
	var taskTypes = [];
	var taskStatus = [];
	//   var height = document.body.clientHeight - margin.top - margin.bottom - 5;
	//   var width = document.body.clientWidth - margin.right - margin.left - 5;
	var height = h - margin.bottom;
	var width = w-margin.right-margin.left;

	var margin2 = {
		top: 0,
		right: 25,
		bottom: 50,
		left: 150
	};

	if (w <= 512) {
		margin.left = 0;
		margin2.left = 0;
	}

	//var height2 = document.body.clientHeight - margin2.top - margin2.bottom;
	var height2 = height + margin.bottom + 5 - margin2.bottom;

	var tickFormat = "%H:%M";

	var keyFunction = function (d) {
		return d.startDate + d.taskName + d.endDate;
	};

	var rectTransform = function (d) {
		//return "translate(" + x(d.startDate) + "," + y(d.taskName) + ")";
		return "translate(" + x(d.startDate) + "," + y(d.taskName) + ")";
	};

	var x, x2, y, y2, xAxis, xAxis2, yAxis, tooltip, svg, focus, context, bars; //, brush, zoom;

	initAxis();

	var initTimeDomain = function (tasks) {
		if (timeDomainMode === FIT_TIME_DOMAIN_MODE) {
			if (tasks == undefined || tasks.length < 1) {
				timeDomainStart = d3.time.day.offset(new Date(), -3);
				timeDomainEnd = d3.time.hour.offset(new Date(), +3);
				return;
			}
			tasks.sort(function (a, b) {
				return a.endDate - b.endDate;
			});
			timeDomainEnd = tasks[tasks.length - 1].endDate;
			tasks.sort(function (a, b) {
				return a.startDate - b.startDate;
			});
			timeDomainStart = tasks[0].startDate;
		}
	};

	var initTimeDomain_redraw = function (tasks) {
		if (timeDomainMode === FIT_TIME_DOMAIN_MODE) {
			if (tasks == undefined || tasks.length < 1) {
				timeDomainStart = d3.time.day.offset(new Date(), -3);
				timeDomainEnd = d3.time.hour.offset(new Date(), +3);
				return;
			}
			tasks.sort(function (a, b) {
				return a.endDate - b.endDate;
			});
			timeDomainEnd = tasks[tasks.length - 1].endDate;
		}
	};

	function initAxis() {
		x = d3
			.scaleTime()
			.domain([timeDomainStart, timeDomainEnd])
			.range([0, width])
			.clamp(true);

		x2 = d3
			.scaleTime()
			.domain([timeDomainStart, timeDomainEnd])
			.range([0, width])
			.clamp(true);

		y = d3
			.scaleBand()
			.domain(taskTypes)
			.rangeRound([0, height - margin.top - margin.bottom], 0.1);

		y2 = d3
			.scaleBand()
			.domain(taskTypes)
			.rangeRound([0, height2 - margin.top - margin.bottom], 0.1);

		xAxis = d3
			.axisBottom()
			.scale(x)
			.tickFormat(d3.timeFormat(tickFormat))
			.tickSize(8)
			.tickPadding(8);

		xAxis2 = d3
			.axisBottom()
			.scale(x2)
			//.tickFormat(d3.timeFormat(tickFormat))
			.tickSize(-25)
			.tickPadding(8);

		yAxis = d3
			.axisLeft()
			.scale(y)
			.tickSize(0);
	}

	function initAxis_redraw() {
		x = d3
			.scaleTime()
			.domain([prev_startDate, prev_endDate])
			.range([0, width])
			.clamp(true);

		x2 = d3
			.scaleTime()
			.domain([prev_startDate, prev_endDate])
			.range([0, width])
			.clamp(true);

		y = d3
			.scaleBand()
			.domain(taskTypes)
			.rangeRound([0, height - margin.top - margin.bottom], 0.1);

		y2 = d3
			.scaleBand()
			.domain(taskTypes)
			.rangeRound([0, height2 - margin.top - margin.bottom], 0.1);

		xAxis = d3
			.axisBottom()
			.scale(x)
			.tickFormat(d3.timeFormat(tickFormat))
			.tickSize(8)
			.tickPadding(8);

		xAxis2 = d3
			.axisBottom()
			.scale(x2)
			//.tickFormat(d3.timeFormat(tickFormat))
			.tickSize(-25)
			.tickPadding(8);

		yAxis = d3
			.axisLeft()
			.scale(y)
			.tickSize(0);
	}

	var brush = d3
		.brushX()
		.extent([
			[0, 0],
			[width, 25]
		]) //height2 - margin.top]])
		.on("brush end", brushed);

	var zoom = d3
		.zoom()
		.scaleExtent([1, Infinity])
		.translateExtent([
			[0, 0],
			[width, height]
		])
		.extent([
			[0, 0],
			[width, height]
		])
		.on("zoom", zoomed);

	function gantt(tasks) {
		initTimeDomain(tasks);
		initAxis();

		svg = d3
			.select(selector)
			.append("svg")
			.attr("class", "chart")
			.attr("width", width + margin.left + margin.right)
			.attr("height", height);

		svg
			.append("defs")
			.append("clipPath")
			.attr("id", "clip")
			.append("rect")
			.attr("width", width)
			.attr("height", height);

		// Focus is the top chart
		focus = svg
			.append("g")
			.attr("class", "focus")
			.attr("width", width + margin.left + margin.right)
			.attr("height", height + margin.top) // + margin.bottom)
			.attr("transform", "translate(" + margin.left + ", " + margin.top + ")");

		// Context is the brush chart at the bottom
		context = svg
			.append("g")
			.attr("class", "context")
			//.attr("width", width + margin.left + margin.right)
			//.attr("height", height2 + margin.top + margin.bottom)
			.attr("transform", "translate(" + margin.left + ", " + (margin2.top - margin.top - margin2.bottom) + ")");

		bars = focus.append("g");

		bars.attr("clip-path", "url(#clip)");

		bars
			.selectAll(".chart")
			.data(tasks, keyFunction)
			.enter()
			.append("rect")
			.attr("class", "pending")
			.style('fill', function (d) {
				return d.color;
			})
			.attr("class", function (d) {
				if (taskStatus[d.status] == null) {
					return "bar taskbar";
				}
				return taskStatus[d.status] + " taskbar";
			})

			.attr("y", 0)
			.attr("transform", rectTransform)
			.attr("height", function (d) {
				return y.bandwidth();
				//       // return y.range()[1];
			})
			.attr("width", function (d) {
				return x(d.endDate) - x(d.startDate);
			});

		focus
			.append("g")
			.attr("class", "axis axis--x")
			.attr(
				"transform",
				"translate(0, " + (height - margin.top - margin.bottom) + ")"
			)
			.transition()
			.call(xAxis);

		focus
			.append("g")
			.attr("class", "axis y--axis")
			.transition()
			.call(yAxis);

		// add legend
		/*		var legend = svg.append("g").attr("class", "legend");

				var taskTypeData = Object.keys(taskStatus);

				legend
				  .selectAll(".chart")
				  .data(taskTypeData)
				  .enter()
				  .append("rect")
				  .attr("x", function(d, i) {
				    //return (width - margin.left) / (taskTypeData.length) * (i+1);
				    return 0;
				  })
				  .attr("y", function(d, i) {
				    //return height + margin.top; 
				    return margin.top + (i*1)*30;
				  })
				  .attr("width", 10)
				  .attr("height", 10)
				  .attr("class", function(d, i) {
				    return taskStatus[d];
				  });

				legend
				  .selectAll(".labels")
				  .data(taskTypeData)
				  .enter()
				  .append("text")
				  .attr("x", function(d, i) {
				    return (
				      //(width - margin.left) / (taskTypeData.length) * (i+1) + 20
				      20
				    );
				  })
				  .attr("y", function(d, i) {
				    //return height + margin.top + 9; 
				    return margin.top + (i*1)*30+9;
				  })
				  .text(function(d, i) {
				    return d;
				  });
		*/
		var tooltip = d3
			.select(selector)
			.append("div")
			.attr("class", "tooltip");

		tooltip.append("div").attr("class", "timeRange");
		tooltip.append("div").attr("class", "timeDuring");
		tooltip.append("div").attr("class", "status");
		tooltip.append("div").attr("class", "operator");

		focus
			.selectAll(".taskbar")
			.on("mouseover", function (d) {
				tooltip
					.select(".timeRange")
					.html(
						moment(d.startDate).format("h:mm A") +
						" - " +
						moment(d.endDate).format("h:mm A")
					);

				if (d.routine == "") {
					tooltip.select(".routine").html("");
				} else {
					tooltip.select(".routine").html("Routine: " + d.routine);
				}
				var duration = d.endDate - d.startDate;
				duration = duration / 1000;
				hours = Math.floor(duration / 3600);
				minutes = Math.floor((duration - (hours * 3600)) / 60);
				seconds = duration - (hours * 3600) - (minutes * 60);

				timeString = hours.toString().padStart(2, '0') + ':' +
					minutes.toString().padStart(2, '0') + ':' +
					seconds.toString().padStart(2, '0');

				tooltip.select(".operator").html("Operator: " + d.operator);
				tooltip.select(".status").html("Status: " + d.show_status);
				tooltip.select(".timeDuring").html("Duration: " + timeString);

				tooltip.style("display", "block");
				tooltip.style("opacity", 0.9);
			})
			.on("mousemove", function (d) {
				tooltip
					.style("top", d3.event.layerY + 10 + "px")
					.style("left", d3.event.layerX - 25 + "px");
			})
			.on("mouseout", function () {
				tooltip.style("display", "none");
				tooltip.style("opacity", 0);
			});

		context
			.append("g")
			.attr("class", "axis axis--x")
			.attr("transform", "translate(0, " + 25 + ")") //.transition()
			.call(xAxis2);


		if (prev_endDate != null) {
			var wid = prevPos[1] - prevPos[0];
			console.log("Width:=" + wid);
			console.log("Start Date :" + prev_startDate + "End Date :" + prev_endDate)
			prevPos[0] = x2(prev_startDate);
			prevPos[1] = prevPos[0] + wid;
		} else {
			prevPos = x2.range();
		}

		context
			.append("g")
			.attr("class", "brush")
			.call(brush)
			.call(brush.move, prevPos);

		// Removed zoom in order to keep tooltips. Zoom rect overlayed the tooltips functionality. Need to see if I can make both work together
		//    svg
		//      .append("rect")
		//      .attr("class", "zoom")
		//      .attr("width", width)
		//      .attr("height", height2 - margin.top)
		//      .attr("transform", "translate(" + margin.left + "," + margin.bottom + ")")
		//      .call(zoom);
		// setTimeout(function(){
		// 	drawBrush();
		// }, 1000);

		return gantt;
	}

	function drawBrush() {

		//console.log(prevPos);

		var s = prevPos || x2.range();
		x.domain(s.map(x2.invert, x2));

		prev_startDate = x.invert(prevPos[0]);
		prev_endDate = x.invert(prevPos[1]);

		focus
			.selectAll(".taskbar")
			.attr("transform", rectTransform)
			.attr("height", function (d) {
				return y.bandwidth();
			})
			.attr("width", function (d) {
				return x(d.endDate) - x(d.startDate);
			});

		focus.select(".axis--x").call(xAxis);

		intervalViewport = prev_endDate.getTime() - prev_startDate.getTime();
		offsetViewport = prev_startDate.getTime() - timeDomainStart.getTime();
	}

	var intervalViewport;
	var offsetViewport;

	function brushed() {

		if (d3.event.sourceEvent && d3.event.sourceEvent.type === "zoom") {
			return; // ignore brush-by-zoom
		}

		var s = d3.event.selection || x2.range();

		//console.log(d3.event.selection);
		prevPos = d3.event.selection;
		if (prevPos != null) {
			prev_startDate = x2.invert(prevPos[0]);
			prev_endDate = x2.invert(prevPos[1]);
		}

		//console.log(prevPos);

		x.domain(s.map(x2.invert, x2));



		focus
			.selectAll(".taskbar")
			.attr("transform", rectTransform)
			.attr("height", function (d) {
				return y.bandwidth();
			})
			.attr("width", function (d) {
				return x(d.endDate) - x(d.startDate);
			});

		focus.select(".axis--x").call(xAxis);

		// intervalViewport = prev_endDate.getTime() - prev_startDate.getTime();
		// offsetViewport = prev_startDate.getTime() - timeDomainStart.getTime();

		//console.log(prev_endDate + " : " + prev_startDate);

		// svg
		// 	.select(".zoom")
		// 	.call(
		// 		zoom.transform,
		// 		d3.zoomIdentity.scale(width / (s[1] - s[0])).translate(-s[0], 0)
		// 	);			

	}




	function zoomed() {
		if (d3.event.sourceEvent && d3.event.sourceEvent.type === "brush") return; // ignore zoom-by-brush
		var t = d3.event.transform;

		x.domain(t.rescaleX(x2).domain());
		focus
			.selectAll(".taskbar")
			.attr("transform", rectTransform)
			.attr("height", function (d) {
				return y.bandwidth();
			})
			.attr("width", function (d) {
				return x(d.endDate) - x(d.startDate);
			});
		focus.select(".axis--x").call(xAxis);
		context.select(".brush").call(brush.move, x.range().map(t.invertX, t));

	}

	gantt.redraw1 = function (tasks) {
		initTimeDomain(tasks);
		initAxis();

		var svg = d3.select("svg");

		var ganttChartGroup = svg.select(".focus");
		var rect = ganttChartGroup.selectAll("rect").data(tasks, keyFunction);

		rect
			.enter()
			.insert("rect", ":first-child")
			.attr("class", function (d) {
				if (taskStatus[d.status] == null) {
					return "bar taskbar";
				}
				return taskStatus[d.status] + " taskbar";
			})
			.style("fill", function (d) {
				return d.color;
			})
			.transition()
			.attr("y", 0)
			.attr("transform", rectTransform)
			.attr("height", function (d) {
				return y.bandwidth();
			})
			.attr("width", function (d) {
				return x(d.endDate) - x(d.startDate);
			});

		rect
			.transition()
			.attr("transform", rectTransform)
			.attr("height", function (d) {
				return y.bandwidth();
			})
			.attr("width", function (d) {
				return x(d.endDate) - x(d.startDate);
			});

		rect.exit().remove();

		svg
			.select(".x")
			.transition()
			.call(xAxis);
		svg
			.select(".y")
			.transition()
			.call(yAxis);
		return gantt;
	}

	gantt.redraw = function (tasks) {
		initTimeDomain(tasks);
		//initAxis();
		initAxis_redraw();


		var svg = d3.select("svg");

		var ganttChartGroup = svg.select(".focus");
		var rect = ganttChartGroup.selectAll("rect").data(tasks, keyFunction);

		rect
			.enter()
			.insert("rect", ":first-child")
			.attr("class", function (d) {
				if (taskStatus[d.status] == null) {
					return "bar taskbar";
				}
				return taskStatus[d.status] + " taskbar";
			})
			.style("fill", function (d) {
				return d.color;
			})
			.transition()
			.attr("y", 0)
			.attr("transform", rectTransform)
			.attr("height", function (d) {
				return y.bandwidth();
			})
			.attr("width", function (d) {
				return x(d.endDate) - x(d.startDate);
			});

		rect
			.transition()
			.attr("transform", rectTransform)
			.attr("height", function (d) {
				return y.bandwidth();
			})
			.attr("width", function (d) {
				return x(d.endDate) - x(d.startDate);
			});

		rect.exit().remove();

		svg
			.select(".x")
			.transition()
			.call(xAxis);
		svg
			.select(".y")
			.transition()
			.call(yAxis);

		//brushed();

		//brushed_force();
		//drawBrush(prevPos[0], prevPos[1]);
		return gantt;
	};

	gantt.margin = function (value) {
		if (!arguments.length) return margin;
		margin = value;
		return gantt;
	};

	gantt.timeDomain = function (value) {
		if (!arguments.length) return [timeDomainStart, timeDomainEnd];
		(timeDomainStart = +value[0]), (timeDomainEnd = +value[1]);
		return gantt;
	};

	/**
	 * @param {string}
	 *                vale The value can be "fit" - the domain fits the data or
	 *                "fixed" - fixed domain.
	 */
	gantt.timeDomainMode = function (value) {
		if (!arguments.length) return timeDomainMode;
		timeDomainMode = value;
		return gantt;
	};

	gantt.taskTypes = function (value) {
		if (!arguments.length) return taskTypes;
		taskTypes = value;
		return gantt;
	};

	gantt.taskStatus = function (value) {
		if (!arguments.length) return taskStatus;
		taskStatus = value;
		return gantt;
	};

	gantt.width = function (value) {
		if (!arguments.length) return width;
		width = +value;
		return gantt;
	};

	gantt.height = function (value) {
		if (!arguments.length) return height;
		height = +value;
		height2 = height + margin.bottom + 5 - margin2.bottom;
		margin2.top = height;
		return gantt;
	};

	gantt.tickFormat = function (value) {
		if (!arguments.length) return tickFormat;
		tickFormat = value;
		return gantt;
	};
	gantt.selector = function (value) {
		if (!arguments.length)
			return selector;
		selector = value;
		return gantt;
	};

	return gantt;
};
