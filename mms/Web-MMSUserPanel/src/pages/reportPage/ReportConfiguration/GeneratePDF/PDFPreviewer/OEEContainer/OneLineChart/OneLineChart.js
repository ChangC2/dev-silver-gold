import React from 'react'
import Chart from 'react-google-charts';
import moment from 'moment';

function OneLineChart(props) {
    const { oeeData } = props;
    let chartData = [];
    chartData = oeeData.map(x => [moment(x.date, "MM/DD/YYYY").toDate(), parseInt(x.oee), parseInt(x.performance), parseInt(x.quality), parseInt(x.availability)]);
    chartData = [[{ type: 'date', label: 'Day' }, 'oee', 'performance', 'quality', 'availability'], ...chartData];
    return (
        <div>
            <Chart
                width={'100%'}
                height={'320px'}
                chartType="LineChart"
                loader={<div>Loading Chart</div>}
                data={chartData}
                options={{
                    hAxis: {
                        title: '',
                        slantedText: true
                    },
                    
                    // vAxis: {
                    //     title: 'Popularity',
                    // },
                    // series: {
                    //     1: { curveType: 'function' },
                    // },
                }}
                rootProps={{ 'data-testid': '2' }}
            />
        </div>
    )
}

export default OneLineChart
