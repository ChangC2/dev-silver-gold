import { Col, Row } from "antd";
import "./MaintenanceTopLayout.css";

import { useDispatch, useSelector } from "react-redux";
import { setAppDataStore } from "redux/actions/appActions";
import { appData } from "services/global";
import backIcon from "../../assets/icons/ic_back_arrow_w.png";



const MaintenanceTopLayout = (props) => {
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
    <div className="maintenance-top-layout">
      <Row>
        <Col span={6}>
          <div className="maintenance-top-layout-left">
            <a
              className="maintenance-top-layout-menu-button"
              onClick={() => onClickBack()}
            >
              <img
                className="maintenance-top-layout-menu-icon"
                src={backIcon}
              />
            </a>

            <div className="maintenance-top-layout-title-div">
              <span className="maintenance-top-layout-title">
                {"Maintenance"}
              </span>
            </div>
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default MaintenanceTopLayout;
