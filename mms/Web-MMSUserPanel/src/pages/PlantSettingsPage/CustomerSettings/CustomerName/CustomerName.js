import React from "react";
import { Input } from "antd";
import "./CustomerName.css";
import { useSelector } from "react-redux";
import lang from "../../../../services/lang";

function CustomerName(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { customerName, setCustomerName } = props;
  return (
    <div>
      <div className="app-setting-text-input-title">
        {" "}
        {lang(langIndex, "plant_customername")}
      </div>
      <input
        style={{ marginTop: 5 }}
        className={"app-setting-text-input-value"}
        value={customerName}
        onChange={(e) => setCustomerName(e.target.value)}
        type={"string"}
      />
    </div>
  );
}

export default CustomerName;
