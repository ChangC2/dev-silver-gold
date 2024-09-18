import React from 'react'
import './FactoryContainer.css';
import { List, Row, Col, Divider } from 'antd';

function FactoryContainer(props) {
    const { customerInfoList, customerIdList } = props;
    const { selectedFactory, setSelectedFactory } = props;

    const factoryList = customerIdList.map((customer_id) => {
        const customerInfo = customerInfoList[customer_id];
        return (
            <Row key={customer_id}
                align={"middle"}
                className={customer_id == selectedFactory
                    ? "one-factory-info-container selected"
                    : "one-factory-info-container normal"}
                onClick={() => setSelectedFactory(customer_id)}
            >
                <Col span={8}>
                    <img src={customerInfo['logo']}
                        alt="factory-logo"
                        className="factory-logo-img" />
                </Col>
                <Col span={16}>
                    <span>
                        {customerInfo.name}
                    </span>
                </Col>
            </Row>
        );
    });

    return (
        <div>
            <Divider orientation="center" style={{ color: 'white', fontSize: 20 }}>Factories</Divider>
            <div >
                {factoryList}
            </div>
            
        </div>
    )
}

export default FactoryContainer
