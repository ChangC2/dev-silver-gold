// @flow strict

import { Input } from "antd";
import React from "react";
import "./InputNormal.css";
function InputNormal(props) {
  const { title, placeholder, field, userDetail, setUserDetail } = props;
  const { wrongList, setWrongList } = props;
  return (
    <div className="input-normal">
      <h3 className="input-title">{title}</h3>
      <Input
        placeholder={placeholder === undefined ? "" : placeholder}
        value={userDetail[field] === undefined ? "" : userDetail[field]}
        style={{
          border:
            wrongList.includes(field) === true
              ? "1px solid var(--redColor)"
              : "1px solid var(--greyColor)",
        }}
        onChange={(e) => {
          let temp = userDetail;
          temp[field] = e.target.value;
          setUserDetail({ ...temp });
        }}
      />
    </div>
  );
}

export default InputNormal;
