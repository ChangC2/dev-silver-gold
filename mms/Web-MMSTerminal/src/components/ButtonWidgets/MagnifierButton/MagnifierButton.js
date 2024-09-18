// @flow strict

import { Popover } from "antd";
import React from "react";
import "./MagnifierButton.css";
function MagnifierButton(props) {
  return (
    <div>
      <Popover trigger={"click"} content={props.children} destroyTooltipOnHide={true}>
        <img
          className="magnifier-button-icon custom-button"
          src="/assets/icons/show_detail.png"
          style={{ width: 35, height: 35 }}
          alt="magnifier to check detail"
        />
      </Popover>
    </div>
  );
}

export default MagnifierButton;
