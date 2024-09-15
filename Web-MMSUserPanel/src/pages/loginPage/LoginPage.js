import { Button, Input, Spin } from "antd";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import { useDispatch, useSelector } from "react-redux";
import logo from "../../assets/images/logo.png";
import lang from "../../services/lang";
import { Login, SignOut } from "../../services/common/auth_apis";
import "./LoginPage.css";
import { UpdateAppConfig } from "services/redux/reducers/app";
import { USER_SITE } from "services/common/urls";
function LoginPage(props) {
  const dispatch = useDispatch();
  const [customerId, setCustomerId] = useState("");
  const [username, setusername] = useState("");
  const [password, setPassword] = useState("");
  const [isWrong, setIsWrong] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const appData = useSelector((state) => state.app);
  const authData = useSelector((x) => x.authService);
  const { langIndex } = appData;

  useEffect(() => {
    SignOut(dispatch);
    UpdateAppConfig({ customer_id: "" }, dispatch);
  }, []);

  useEffect(() => {
    if (authData != undefined && isLoading) {
      setIsLoading(false);
      props.history.push(`${USER_SITE}/pages`);
    }
  }, [authData]);

  const onClickSigninButton = async () => {
    setIsLoading(true);
    Login(customerId, username, password, dispatch, (res) => {
      if (res == false) {
        setIsLoading(false);
        setIsWrong(true);
      }
    });
  };

  return (
    <div style={{ color: "var(--blackColor)" }} className="login-container">
      <div className="LoginDialog">
        {/* <h4 className="whiteText">LOGIN</h4> */}
        <div className="login-page-logo-container">
          <img className="login-page-logo-image" alt="logo" src={logo} />
          <h4 className="login-page-title">{lang(langIndex, "login_desc1")}</h4>
          <h4 className="login-page-title">{lang(langIndex, "login_desc2")}</h4>
          <h4 className="login-page-title">{lang(langIndex, "login_desc3")}</h4>
          <h4></h4>
          <h4></h4>
        </div>
        <div className="row">
          <Input
            className="login-input"
            placeholder={lang(langIndex, "register_factory_id")}
            type="text"
            value={customerId}
            onChange={(e) => {
              setCustomerId(e.target.value);
              setIsWrong(false);
            }}
          />
        </div>
        <div className="row">
          <Input
            className="login-input"
            placeholder={lang(langIndex, "login_username")}
            type="text"
            value={username}
            onChange={(e) => {
              setusername(e.target.value);
              setIsWrong(false);
            }}
          />
        </div>
        <div className="row">
          <Input
            className="login-input"
            placeholder={lang(langIndex, "login_password")}
            type="password"
            value={password}
            onChange={(e) => {
              setPassword(e.target.value);
              setIsWrong(false);
            }}
            onPressEnter={onClickSigninButton}
          />
        </div>

        <Button
          type="primary"
          className="login-button"
          onClick={onClickSigninButton}
        >
          {isLoading && <Spin style={{ marginLeft: 10, marginRight: 10 }} />}
          Login
        </Button>

        <div style={{ marginTop: 5, textAlign: "center", height: 20 }}>
          {isWrong && (
            <h5
              className="whiteText"
              style={{ fontSize: 12, color: "#ff0000" }}
            >
              {lang(langIndex, "login_error")}
            </h5>
          )}
        </div>

        <div className="login-page-link-text">
          {lang(langIndex, "login_has_keycode")}{" "}
          <Link to={`${USER_SITE}/register`}>
            {lang(langIndex, "login_register_here")}
          </Link>
        </div>

        <div className="login-page-link-text">
          {lang(langIndex, "login_privacy")}
          <a href="https://www.slytrackr.com/legal.html">
            {lang(langIndex, "login_here")}
          </a>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;
