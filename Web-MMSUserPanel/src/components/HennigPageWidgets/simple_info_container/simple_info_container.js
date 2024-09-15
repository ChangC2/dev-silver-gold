import React from "react";
import "./simple_info_container.css";

function SimpleInfoContainer(props) {
  const { title, value, valueSize, height, isStatusWorking } = props;
  return (
    <div className="simple-info-container" style={{ height: height }}>
      {valueSize == undefined ? (
        <div>
          <div className="simple-info-container-title">{title}&nbsp;:</div>
          <div className="simple-info-container-value">{value}</div>
        </div>
      ) : (
        <div>
          <div className="simple-info-container-title">{title}&nbsp;:</div>
          <div
            className="simple-info-container-value"
            style={{
              fontSize: valueSize,
              color: isStatusWorking
                ? "rgb(4, 202, 14)"
                : isStatusWorking != undefined
                ? "red"
                : "white",
            }}
          >
            {value === NaN || value === "NaN" ? 0.0 : value}
          </div>
        </div>
      )}
    </div>
  );
}
export default SimpleInfoContainer;
