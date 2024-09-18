import React from 'react'
import { Slider } from 'antd'
import './SliderInput.css'
function SliderInput(props) {
    const { initValue, field, updateValue, title } = props;
    return (
        <div className="app-setting-slider-input-container">
            <div className="app-setting-slider-input-title">
                {title}
            </div>
            <div >
                <Slider
                    className="app-setting-slider-input-value"
                    min={10}
                    max={100}
                    onChange={(e) => updateValue(field, e)}
                    value={parseInt(initValue[field])}
                />
            </div>
        </div>
    )
}

export default SliderInput
