// @flow strict

import { Input } from "antd";
import React from "react";
import "./InputPassword.css";
function InputPassword(props) {
  const { userDetail, setUserDetail, onPressEnter } = props;
  const { wrongList, setWrongList } = props;
  return (
    <div className="input-password">
      <h3 className="input-title">Password</h3>
      <Input.Password
        placeholder={"Password"}
        value={userDetail.password === undefined ? "" : userDetail.password}
        onChange={(e) =>
          setUserDetail({ ...userDetail, password: e.target.value })
        }
        onPressEnter={onPressEnter}
        className={
          wrongList.includes("password") === true
            ? "phone-input-password-wrong"
            : "phone-input-password-correct"
        }
      />
    </div>
  );
}

export default InputPassword;
