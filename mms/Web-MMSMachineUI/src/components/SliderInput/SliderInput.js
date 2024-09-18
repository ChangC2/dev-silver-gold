import React from 'react'
import { Slider } from 'antd'
import './SliderInput.css'
function SliderInput(props) {
    const { initValue, field, updateValue, title } = props;
    return (
        <div className="slider-input-container">
            <div className="slider-input-title">
                {title}
            </div>
            <div >
                <Slider
                    className="slider-input-value"
                    min={5}
                    max={500}
                    onChange={(e) => updateValue(field, e)}
                    value={parseInt(initValue[field])}
                />
            </div>
        </div>
    )
}

export default SliderInput
