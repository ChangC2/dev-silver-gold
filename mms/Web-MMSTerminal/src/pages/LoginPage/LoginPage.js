// @flow strict

import { Col, Row, message } from "antd";
import { ROOT } from "navigation/CONSTANTS";
import { useAuth } from "navigation/ProvideAuth";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import { setFromUrl } from "redux/actions/userActions";
import "./LoginPage.css";

import NormalButton from "components/ButtonWidgets/NormalButton/NormalButton";
import InputNormal from "components/InputWidgets/InputNormal/InputNormal";
import InputPassword from "components/InputWidgets/InputPassword/InputPassword";
import { setUserDataStore } from "redux/actions/userActions";
import { logoUrl } from "services/CONSTANTS";
import { setUserData, userData, validatePassword } from "services/global";

function LoginPage(props) {
  const history = useHistory();
  const auth = useAuth();
  const dispatch = useDispatch();
  const [isBusy, setIsBusy] = useState(false);
  const { userDataStore } = useSelector((x) => x.userDataStore);
  const [userDetail, setUserDetail] = useState({
    name: "",
    password: "",
  });

  const [wrongList, setWrongList] = useState([]);

  const validateFields = () => {
    const { name, password } = userDetail;
    let tmpWrongList = [];
    if (name === undefined || name === "") {
      tmpWrongList.push("name");
    }
    if (validatePassword(password) === false) tmpWrongList.push("password");
    setWrongList(tmpWrongList);
    return tmpWrongList.length === 0;
  };

  const onClickNext = () => {
    if (validateFields() === false) {
      return;
    }
    onClickLogin();
  };

  useEffect(() => {
    userData.name = "";
    userData.photo = "";
    dispatch(setUserDataStore(userData));
  }, []);

  const onClickLogin = () => {
    setIsBusy(true);
    auth
      .signin(userDetail)
      .then((res) => {
        const { status } = res;
        if (status === undefined || status === false) {
          setIsBusy(false);
          message.error("Login Fail");
        } else {
          setIsBusy(false);
          onSuccessLogin(res);
        }
      })
      .catch((err) => {
        setIsBusy(false);
        message.error("Login Fail");
      });

    setIsBusy(false);
    onSuccessLogin(userDetail);
  };

  const onSuccessLogin = (info) => {
    if (info.name === undefined || info.password === undefined) {
      return;
    }
    setUserData(info);
    dispatch(setUserDataStore(userData));
    const fromUrl = userDataStore.fromUrl;
    setTimeout(() => {
      if (fromUrl === undefined || fromUrl.pathname === undefined) {
        history.replace(ROOT);
      } else {
        dispatch(setFromUrl(undefined));
        history.replace(fromUrl.pathname);
      }
    }, 500);
  };

  return (
    <div className="login-page">
      <Row className="auth-dialog-row">
        <Col className="auth-dialog-widget">
          <div>
            <div className="auth-logo-container">
              <img className="auth-logo" src={logoUrl} alt="logo" />
            </div>
            <div style={{ marginTop: 40 }}>
              <InputNormal
                userDetail={userDetail}
                setUserDetail={setUserDetail}
                placeholder="Enter Name"
                field="name"
                title="Username"
                wrongList={wrongList}
                setWrongList={setWrongList}
              />
            </div>
            <div style={{ marginTop: 20 }}>
              <InputPassword
                userDetail={userDetail}
                setUserDetail={setUserDetail}
                wrongList={wrongList}
                setWrongList={setWrongList}
                onPressEnter={onClickNext}
              />
            </div>
            <div style={{ marginTop: 30 }}>
              <NormalButton
                style={{ backgroundColor: "var(--blueColor)" }}
                onClick={onClickNext}
                isBusy={isBusy}
              >
                Login
              </NormalButton>
            </div>
          </div>
        </Col>
      </Row>
    </div>
  );
}

export default LoginPage;
