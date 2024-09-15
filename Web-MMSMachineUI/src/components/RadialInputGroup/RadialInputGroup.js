import React from 'react'
import './RadialInputGroup.css'
import { Switch } from 'antd'
function RadialInputGroup(props) {
    const { initValue, field, updateValue, title } = props;
    return (
        <div className="radial-input-group-container">
            <Switch
                className="radial-input-group"
                checked={initValue[field] === 1 ? true : false}
                onChange={(e) => updateValue(field, e === true ? 1 : 0)}
            />
            <div className="radial-input-group-title">
                {title}
            </div>
        </div>
    )
}

export default RadialInputGroup
