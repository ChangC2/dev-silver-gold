import React from 'react'
import { Switch, Row, Col } from 'antd';
import './OneEmailUI.css'
import { sizePad } from '../../../../services/common/constants';

function OneEmailUI(props) {
    
    const { email, isSelected, onSelectEmail, screenSize } = props;
    return (
        screenSize.width >= sizePad
            ?
            <Row className="email-select-container-style">
                <Col span={18}>
                    <h3 className="email-style">{email}</h3>
                </Col>
                <Col span={6}>
                    <Switch
                        className={isSelected ? "email-select-switch-style-checked" : "email-select-switch-style-unchecked"}
                        size="small"
                        checked={isSelected}
                        onChange={(checked) => onSelectEmail(email, checked)}
                    />
                </Col>
            </Row>
            :
            <Row style={{ marginLeft: 20 }}>
                <Col span={18}>
                    <h3 className="email-style">{email}</h3>
                </Col>
                <Col span={6}>
                    <Switch
                        className={isSelected ? "email-select-switch-style-checked" : "email-select-switch-style-unchecked"}
                        size="small"
                        checked={isSelected}
                        onChange={(checked) => onSelectEmail(email, checked)}
                    />
                </Col>
            </Row>
    )
}

export default OneEmailUI
