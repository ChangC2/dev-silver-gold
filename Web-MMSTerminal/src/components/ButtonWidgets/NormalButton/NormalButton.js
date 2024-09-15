import React from "react";
import "./NormalButton.css";
import { Spin } from "antd";
function NormalButton(props) {
  return (
    <div>
      <button
        className="green-button custom-button"
        onClick={props.onClick}
        style={props.style}
        ref={props.ref}
      >
        <Spin spinning={props.isBusy === true}>{props.children}</Spin>
      </button>
    </div>
  );
}

export default NormalButton;
