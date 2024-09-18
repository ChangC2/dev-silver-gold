d3.barchart = function (Container) {
	var margin = {
			top: 20,
			right: 90,
			bottom: 30,
			left: 60
		},
		width = 980 - margin.left - margin.right,
		height = 500 - margin.top - margin.bottom;
	var x0 = d3.scale.ordinal()
		.rangeRoundBands([0, width], .1);

	var x1 = d3.scale.ordinal();

	var y = d3.scale.linear()
		.range([height, 0]);

	var color = d3.scale.category10();

	var xAxis = d3.svg.axis()
		.scale(x0)
		.orient("bottom");

	var yAxis = d3.svg.axis()
		.scale(y)
		.orient("left")
		.tickFormat(d3.format(".2s"));

	// var Container = "#chart_div";

	// barchart.Container = function (value) {
	// 	if (!arguments.length)
	// 		return Container;
	// 	Container = value;
	// 	return barchart;
	// };

	var svg = d3.select('#chart_div').append("svg")
		.attr('width', width + margin.right + margin.left)
		.attr('height', height + margin.top + margin.bottom)
		.append("g")
		.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

	var tip = d3.tip()
		.attr("class", "d3-tip")
		.offset([-10, 0])
		.html(function (d) {
			return "No. of repairs: " + d.value;
		});



	d3.csv("my.csv", function (error, data) {
        console.log("d3.csv called");
		if (error) throw error;
		fullData = data;
		data = d3.nest()
			.key(function (d) {
				return d.module_category;
			})
			.rollup(function (values) {
				var counts = {},
					keys = ['actual', 'predicted']
				keys.forEach(function (key) {
					counts[key] = d3.sum(values, function (d) {
						return d[key];
					})
				})
				return counts
			})
			.entries(data);
		//make the x axis
		svg.append("g")
			.attr("class", "x axis")
			.attr("transform", "translate(0," + height + ")")
			.call(xAxis);
		//make teh y axis
		svg.append("g")
			.attr("class", "y axis")
			.call(yAxis)
			.append("text")
			.attr("transform", "rotate(-90)")
			.attr("y", 0 - margin.left)
			.attr("x", 0 - (height / 2))
			.attr("dy", "1em")
			.style("text-anchor", "middle")
			.text("Number of Repairs");

		makeModuleGraph(data)

		var legend = svg.selectAll(".legend")
			.data(['actual', 'predicted'])
			.enter().append("g")
			.attr("class", "legend")
			.attr("transform", function (d, i) {
				return "translate(0," + i * 20 + ")";
			});

		legend.append("rect")
			.attr("x", width - 18)
			.attr("width", 18)
			.attr("height", 18)
			.style("fill", function (d) {
				return color(d);
			});

		legend.append("text")
			.attr("x", width - 24)
			.attr("y", 9)
			.attr("dy", ".35em")
			.style("text-anchor", "end")
			.text(function (d) {
				return d;
			});
	});


	function makeModuleGraph(data) {
        console.log("makeModuleGraph called");
		var yval = [];
		data.forEach(function (d) {
			yval.push(d.values.actual);
			yval.push(d.values.predicted);
		});
		x0.domain(data.map(function (d) {
			return d.key;
		}));
		x1.domain(['actual', 'predicted']).rangeRoundBands([0, x0.rangeBand()]);

		y.domain([0, d3.max(yval)]);

		svg.call(tip);

		svg.selectAll("g .x")
			.attr("class", "x axis")
			.attr("transform", "translate(0," + height + ")")
			.call(xAxis);

		svg.selectAll("g .y")
			.attr("class", "y axis")
			.call(yAxis);

		var module = svg.selectAll(".module")
			.data(data)
			.enter().append("g")
			.attr("class", "module")
			.attr("transform", function (d) {
				return "translate(" + x0(d.key) + ",0)";
			});

		module.selectAll("rect")
			.data(function (d) {
				var ary = [];
				ary.push({
					name: "actual",
					value: d.values.actual,
					key: d.key
				});
				ary.push({
					name: "predicted",
					value: d.values.predicted,
					key: d.key
				});
				return ary;
			})
			.enter().append("rect")
			.attr("width", x1.rangeBand())
			.attr("x", function (d) {
				return x1(d.name);
			})
			.attr("y", function (d) {
				return y(d.value);
			})
			.attr("height", function (d) {
				return height - y(d.value);
			})
			.style("fill", function (d) {
				return color(d.name);
			}).on("click", function (d) {
				makeComponentCategoryGraph(d); //make the graph for category
			});

	}

	function makeComponentCategoryGraph(d) {
        console.log("makeComponentCategoryGraph called");
		var filtered = fullData.filter(function (k) {
			if (d.key == k.module_category) {
				return true;
			} else {
				return false;
			}
		})
		var data = d3.nest()
			.key(function (d) {
				return d.component_category;
			})
			.rollup(function (values) {
				var counts = {},
					keys = ['actual', 'predicted']
				keys.forEach(function (key) {
					counts[key] = d3.sum(values, function (d) {
						return d[key];
					})
				})
				return counts
			})
			.entries(filtered);
		var yval = [];
		data.forEach(function (d) {
			yval.push(d.values.actual);
			yval.push(d.values.predicted);
		});
		x0.domain(data.map(function (d) {
			return d.key;
		}));
		x1.domain(['actual', 'predicted']).rangeRoundBands([0, x0.rangeBand()]);

		y.domain([0, d3.max(yval)]);

		svg.call(tip);

		svg.selectAll("g .x")
			.attr("class", "x axis")
			.attr("transform", "translate(0," + height + ")")
			.call(xAxis);

		svg.selectAll("g .y")
			.attr("class", "y axis")
			.call(yAxis);
		svg.selectAll(".module").remove(); //remove alll the bar graphs
		var module = svg.selectAll(".module")
			.data(data)
			.enter().append("g")
			.attr("class", "module")
			.attr("transform", function (d) {
				return "translate(" + x0(d.key) + ",0)";
			});

		module.selectAll("rect")
			.data(function (d) {
				var ary = [];
				ary.push({
					name: "actual",
					value: d.values.actual,
					key: d.key
				});
				ary.push({
					name: "predicted",
					value: d.values.predicted,
					key: d.key
				});
				return ary;
			})
			.enter().append("rect")
			.attr("width", x1.rangeBand())
			.attr("x", function (d) {
				return x1(d.name);
			})
			.attr("y", function (d) {
				return y(d.value);
			})
			.attr("height", function (d) {
				return height - y(d.value);
			})
			.style("fill", function (d) {
				return color(d.name);
			})
	}

}
