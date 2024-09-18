import { Col, Row } from "antd";
import "./SettingsTopLayout.css";

import { useDispatch, useSelector } from "react-redux";
import { setAppDataStore } from "redux/actions/appActions";
import { appData } from "services/global";
import backIcon from "../../assets/icons/ic_back_arrow_w.png";

const SettingsTopLayout = (props) => {
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { pages } = appDataStore;
  const dispatch = useDispatch();
  const onClickBack = () => {
    let newPages = pages;
    newPages.splice(-1);
    appData.pages = newPages;
    dispatch(setAppDataStore(appData));
  };

  return (
    <div className="settings-top-layout">
      <Row>
        <Col span={6}>
          <div className="settings-top-layout-left">
            <a
              className="settings-top-layout-menu-button"
              onClick={() => onClickBack()}
            >
              <img className="settings-top-layout-menu-icon" src={backIcon} />
            </a>

            <div className="settings-top-layout-title-div">
              <span className="settings-top-layout-title">{"Settings"}</span>
            </div>
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default SettingsTopLayout;
