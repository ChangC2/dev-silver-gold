import { Col, Row } from "antd";
import "./InstallConfigTopLayout.css";

import backIcon from "../../assets/icons/ic_back_arrow_w.png";

import { useDispatch, useSelector } from "react-redux";
import { setAppDataStore } from "redux/actions/appActions";
import { appData } from "services/global";

const InstallConfigTopLayout = (props) => {

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
    <div className="install-config-top-layout">
      <Row>
        <Col span={6}>
          <div className="install-config-top-layout-left">
            <a
              className="install-config-top-layout-menu-button"
              onClick={() => onClickBack()}
            >
              <img
                className="install-config-top-layout-menu-icon"
                src={backIcon}
              />
            </a>

            <div className="install-config-top-layout-title-div">
              <span className="install-config-top-layout-title">
                {"Install Configuration"}
              </span>
            </div>
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default InstallConfigTopLayout;
