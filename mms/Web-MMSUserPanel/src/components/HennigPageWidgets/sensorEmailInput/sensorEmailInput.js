import { Select } from 'antd'
import React from 'react'
import './sensorEmailInput.css'
function SensorEmailInput(props) {
    const { optionList, initValue, updateValue, title, field, valueType } = props;

    const handleChange = (value) => {
        updateValue(field, value);
    }
    return (
        <div>
            <Select
                onChange={handleChange} placeholder="Split emails by enter"
                value={initValue[field]}
                mode="tags"
                style={{ width: '100%' }}
                className="sensor-email-input"
            >

            </Select>
        </div>
    )
}

export default SensorEmailInput
