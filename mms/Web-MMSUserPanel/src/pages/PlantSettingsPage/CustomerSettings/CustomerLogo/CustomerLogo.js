import React, { useState, useEffect } from "react";

import "./CustomerLogo.css";
import CustomerImageUploader from "./CustomerImageUploader/CustomerImageUploader";
import { useSelector } from "react-redux";
import lang from "../../../../services/lang";
function CustomerLogo(props) {
  const { customerLogo, setCustomerLogo } = props;
  const { langIndex } = useSelector((x) => x.app);
  return (
    <div style={{marginTop: 30}}>
      <div className="app-setting-text-input-title">
        {" "}
        {lang(langIndex, "plant_customerlogo")}
      </div>
      <div className="customerlogo-value-container-style">
        <CustomerImageUploader
          key="SSSSSSSSS"
          user_picture={customerLogo}
          updateImage={setCustomerLogo}
          isLogo={true}
        />
      </div>
    </div>
  );
}

export default CustomerLogo;
