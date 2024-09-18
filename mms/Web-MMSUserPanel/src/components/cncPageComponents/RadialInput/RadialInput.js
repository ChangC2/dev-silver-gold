import React from 'react'
import './RadialInput.css'
import { Switch } from 'antd'
function RadialInput(props) {
    const { initValue, field, updateValue, title } = props;
    return (
        <div className="app-setting-radial-input-container">
            <Switch
                className="app-setting-radial-input"
                checked={initValue[field] == 1 ? true : false}
                onChange={(e) => updateValue(field, e == true ? 1 : 0)}
            />
            <div className="app-setting-radial-input-title">
                {title}
            </div>
        </div>
    )
}

export default RadialInput
