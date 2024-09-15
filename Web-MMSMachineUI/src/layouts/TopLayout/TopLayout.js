import { Row } from "antd";
import { useSelector } from "react-redux";
import "./TopLayout.css";

import logo from "../../assets/images/logo.png";

import UserInfoLayout from "layouts/UserInfoLayout/UserInfoLayout";

const TopLayout = (props) => {
  const { factoryDataStore } = useSelector((x) => x.factoryDataStore);

  return (
    <Row align="middle" className="top-layout">
      <img alt="logo" src={logo} style={{ height: 28 }} />
      <UserInfoLayout />
    </Row>
  );
};

export default TopLayout;
