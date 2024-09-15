import React from 'react'
import GaugeChart from 'react-gauge-chart/dist/GaugeChart';
import { Row, Col } from 'antd';
import './OEEContainer.css'
import OneLineChart from './OneLineChart/OneLineChart';
import IndicatorItem from 'pages/CNCPages/machineDetailPage/Containers/IndicatorItem';
function OEEContainer(props) {
    const { oeeData } = props;
    // var avgData = oeeData.array.forEach(element => {
    // });
    var oee = 0;
    var availability = 0;
    var quality = 0;
    var performance = 0;
    var utilization = 0;
    for (var i = 0; i < oeeData.length; i++) {
        oee += parseInt(oeeData[i]['oee']);
        availability += parseInt(oeeData[i]['availability']);
        quality += parseInt(oeeData[i]['quality']);
        performance += parseInt(oeeData[i]['performance']);
        utilization += parseInt(oeeData[i]["Utilization"]);
    }
    oee = (oee / (oeeData.length) / 100000.0).toFixed(2);
    availability = (availability / (oeeData.length) / 100.0).toFixed(2);
    quality = (quality / (oeeData.length) / 100.0).toFixed(2);
    performance = (performance / (oeeData.length) / 100.0).toFixed(2);
    utilization = (utilization / (oeeData.length) / 100.0).toFixed(2);
    
    return (
        <div>
            <div className="pdf-gauge-container-style">
                <Row>
                    <Col span={2}></Col>
                    <Col span={5}>
                        <IndicatorItem
                            id={"pdf-gauge_availability"}
                            value={parseFloat(availability)}
                            title={"Availability"}
                            textColor={"black"}
                            isForReport={true}
                        />
                    </Col>
                    <Col span={5}>
                        <IndicatorItem
                            id={"pdf-gauge_quality"}
                            value={parseFloat(quality)}
                            title={"Quality"}
                            textColor={"black"}
                            isForReport={true}
                        />
                    </Col>
                    <Col span={5}>
                        <IndicatorItem
                            id={"pdf-gauge_performance"}
                            value={parseFloat(performance)}
                            title={"Performance"}
                            textColor={"black"}
                            isForReport={true}
                        />
                    </Col>
                    <Col span={5}>
                        <IndicatorItem
                            id={"pdf-gauge_oee"}
                            value={parseFloat(oee)}
                            title={"OEE"}
                            textColor={"black"}
                            isForReport={true}
                        />
                    </Col>

                </Row>
            </div>

            <div className="pdf-line-chart-container-style">
                <OneLineChart
                    oeeData={oeeData}
                />
            </div>
            
        </div>
    )
}

export default OEEContainer
