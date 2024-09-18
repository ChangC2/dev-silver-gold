import React from 'react'
import './DayCheck.css'
function DayCheck(props) {
    const { index, title, checked, setChecked } = props;
    return (
        <div
            className={checked ? "day_check_container day_check_container_checked" : "day_check_container day_check_container_unchecked"}
            onClick={() => setChecked(index, !checked)}
        >
            {title}
        </div>
    )
}

export default DayCheck
