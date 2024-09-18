import { Col, Drawer, Row } from "antd";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import "./TopLayout.css";

import SidebarMenu from "layouts/SidebarMenu/SidebarMenu";
import menuIcon from "../../assets/icons/ic_menu.png";

import TextWithIcon from "components/TextWithIcon/TextWithIcon";
import { STATUS_COLORS } from "services/CONSTANTS";
import connectedIcon from "../../assets/icons/ic_connected.png";
import disconnectedIcon from "../../assets/icons/ic_disconnected.png";

const TopLayout = (props) => {
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { factoryDataStore } = useSelector((x) => x.factoryDataStore);

  console.log("factoryDataStore=>", factoryDataStore);

  const { pages } = appDataStore;
  const [visible, setVisible] = useState(false);
  
  const [plcConnected, setPlcConnected] = useState(false);
  const [serverConnected, setServerConnected] = useState(true);

  const [machine, setMachine] = useState(appDataStore.machineName);
  const [status, setStatus] = useState("Clear Chips");
  const [statusColor, setStatusColor] = useState("#ffffff");

  useEffect(() => {
    let cColor = STATUS_COLORS.find((item) => item["status"] === status);
    setStatusColor(cColor["color"] === undefined ? "#ffffff" : cColor["color"]);
  }, [status]);

  useEffect(() => {
    setMachine(appDataStore.machineName)
  }, [appDataStore.machineName]);

  return (
    <Row
      align="middle"
      className={
        pages[pages.length - 1] === 0 || pages[pages.length - 1] === 1
          ? "top-layout"
          : "display-none"
      }
    >
      <Col span={6}>
        <a className="top-layout-menu-button" onClick={() => setVisible(true)}>
          <img className="top-layout-menu-icon" src={menuIcon} />
        </a>
        <Drawer
          className="custom-drawer"
          placement="left"
          onClick={() => setVisible(false)}
          onClose={() => setVisible(false)}
          open={visible}
        >
          <SidebarMenu open={setVisible} />
        </Drawer>
        <div className="top-layout-machine-status">
          <span className="top-layout-machine">{machine}</span>
          <span className="top-layout-status" style={{ color: statusColor }}>
            {status}
          </span>
        </div>
      </Col>
      <Col span={12} style={{ textAlign: "center" }}>
        {factoryDataStore.customer_details.logo !== undefined && (
          <img className="top-logo" src={factoryDataStore.customer_details.logo} />
        )}
      </Col>

      <Col span={6} style={{ textAlign: "right" }}>
        <TextWithIcon
          text={plcConnected ? "PLC Connnected" : "PLC Disconnected"}
          icon={plcConnected ? connectedIcon : disconnectedIcon}
          marginLeft={"5px"}
          iconBottom={"1px"}
        />
        <TextWithIcon
          text={
            serverConnected ? "Connnected to Server" : "Disonnnected to Server"
          }
          icon={serverConnected ? connectedIcon : disconnectedIcon}
          marginLeft={"5px"}
          iconBottom={"1px"}
        />
      </Col>
    </Row>
  );
};

export default TopLayout;
