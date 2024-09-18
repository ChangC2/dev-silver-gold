import React from 'react'
import './simple_info_container.css';


function SimpleInfoContainer(props) {
    const { title, value, valueSize, height } = props;
    return <div className="simple-info-container" style={{height:height}}>
        {
            valueSize == undefined
                ? <div>
                    <div className="simple-info-container-title">{title}:&nbsp;</div>
                    <div className="simple-info-container-value">{value}</div>
                </div >
                :
                <div>
                    <div className="simple-info-container-title">{title}:&nbsp;</div>
                    <div className="simple-info-container-value" style={{ fontSize: valueSize}}>{value}</div>
                </div >
        }
    </div>


}
export default SimpleInfoContainer;
