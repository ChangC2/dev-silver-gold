// @flow strict

import React from "react";
import "./TextButton.css";

function TextButton(props) {
  return (
    <div className="text-button custom-button" onClick={props.onClick} style={props.style}>
      {props.children}
    </div>
  );
}

export default TextButton;
