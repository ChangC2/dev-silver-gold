import React from 'react'
import './detailPageValueContainer.css'
function DetailPageValueContainer(props) {
    const { title, valueList, unit } = props;
    return (
        <div className="detail-page-value-container">
            <div className='detail-page-value-info'>
                <div className='detail-page-value-container-title'>
                    {title} :
                </div>
                <div className='detail-page-value-container-current'>
                    {valueList.length == 0 ? "-" : valueList[valueList.length - 1] + " " + unit}
                </div>
            </div>

            <div className='detail-page-value-container-max'>
                MAX: {valueList.length == 0 ? "-" : Math.max(...valueList) + " " + unit}
            </div>
            <div className='detail-page-value-container-min'>
                MIN: {valueList.length == 0 ? "-" : Math.min(...valueList) + " " + unit}
            </div>

        </div>
    )
}

export default DetailPageValueContainer
