import { Dropdown, Menu, Popover } from "antd";
import { useDispatch, useSelector } from "react-redux";

import { SignOut } from "services/common/auth_apis";
import { PAGETYPE, sizeMobile, sizePad } from "../../services/common/constants";
import lang from "../../services/lang";
import { SetLanguage, onSelectMenu } from "../../services/redux/reducers/app";
import "./MyHeader.css";

import LaborTrackingPopover from "pages/LaborTracking/LaborTrackingPopover";
import PlantSettingsPopover from "pages/PlantSettingsPage/PlantSettingsPopover";
import { useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import avarIcon from "../../assets/icons/ic_avatar.png";
import collapseIcon from "../../assets/icons/ic_collapse.png";
import fullscreenIcon from "../../assets/icons/ic_fullscreen.png";
import laborIcon from "../../assets/icons/ic_labor.png";
import langIcon from "../../assets/icons/ic_lang.png";
import menuIcon from "../../assets/icons/ic_menu.png";
import settingsIcon from "../../assets/icons/ic_settings.png";
import logoutIcon from "../../assets/icons/logout_w.png";
import NotificationIcon from "./NotificationIcon";
import { USER_SITE } from "services/common/urls";

function MyHeader(props) {
  const dispatch = useDispatch();
  const history = useHistory();

  const { langIndex, customer_id } = useSelector((x) => x.app);
  const {
    screenSize,
    customerInfo,
    setIsFullScreen,
    avatar,
    fullname,
    isMenuCollapsed,
    setIsMenuCollapsed,
    security_level,
  } = props;

  const [category, setCategory] = useState("");
  const [openSettings, setOpenSettings] = useState(false);
  const [description, setDescription] = useState("");
  const [jobID, setJobID] = useState("");

  const app = useSelector((x) => x.app);
  const authData = useSelector((x) => x.authService);

  useEffect(() => {}, []);

  const handleLang = ({ key }) => {
    SetLanguage(key, dispatch);
  };

  const handleSubMenu = ({ key }) => {
    onSelectMenu(customer_id, key, app.group_id, dispatch);
    if (key == PAGETYPE.job_entry && app.page_type != PAGETYPE.job_entry) {
      history.push(`${USER_SITE}/pages/job_entry`);
    }
    if (key == PAGETYPE.report && app.page_type != PAGETYPE.report) {
      history.push(`${USER_SITE}/pages/report`);
    }
    if (
      key == PAGETYPE.plant_setting &&
      app.page_type != PAGETYPE.plant_setting
    ) {
      history.push(`${USER_SITE}/pages/plant_setting`);
    }
  };

  const handlePopoverVisibleChange = () => {
    setOpenSettings(!openSettings);
  };

  const logOut = () => {
    SignOut(dispatch);
    history.push(`${USER_SITE}/login`);
  };

  const onCollapse = () => {
    setIsMenuCollapsed(!isMenuCollapsed);
  };

  const langMenu = (
    <Menu
      onClick={handleLang}
      items={[
        {
          key: "0",
          label: "English",
        },
        {
          key: "1",
          label: "简体中文",
        },
        {
          key: "2",
          label: "Española",
        },
      ]}
    />
  );

  const subMenu = (
    <Menu
      onClick={handleSubMenu}
      items={[
        {
          key: PAGETYPE.job_entry,
          label: lang(langIndex, "breadcrumb_job_entry"),
        },
        {
          key: PAGETYPE.report,
          label: lang(langIndex, "breadcrumb_reports"),
        },
      ]}
    />
  );

  const planSettingsContent = (
    <PlantSettingsPopover
      category={category}
      setCategory={setCategory}
      description={description}
      setDescription={setDescription}
      jobID={jobID}
      setJobID={setJobID}
      setOpenSettings={setOpenSettings}
    />
  );

  const laborTrackingContent = (
    <LaborTrackingPopover
      customer_id={customer_id}
      category={category}
      setCategory={setCategory}
      description={description}
      setDescription={setDescription}
      jobID={jobID}
      setJobID={setJobID}
    />
  );

  return screenSize.width > sizeMobile ? (
    <div className="background">
      <div className="top-customer-bg">
        <span className="txt-top-customer-name">{customerInfo["name"]}</span>
        <img
          className="top-customer-logo"
          style={{ cursor: "pointer" }}
          src={
            avatar === null || avatar === ""
              ? "/assets/icons/ic_logout.svg"
              : customerInfo["logo"]
          }
          alt=""
        />
      </div>

      <div style={{ flex: "1 1 0%" }}></div>

      <div className="top-buttons-bg">
        <div className="txt-top-name">
          <span style={{ marginBottom: 20 }}>{fullname}</span>
        </div>
        <img
          className="user-profile-image"
          style={{ cursor: "pointer" }}
          src={avatar === null || avatar === "" ? avarIcon : avatar}
          alt=""
        />
        {screenSize.width < sizePad && (
          <img className="top-button" src={collapseIcon} onClick={onCollapse} />
        )}
        <Dropdown overlay={subMenu} placement="bottomLeft" arrow>
          <a onClick={(e) => e.preventDefault()}>
            <img className="top-button" src={menuIcon} />
          </a>
        </Dropdown>

        {security_level == 4 && (
          <Popover
            className="labor-tracking-popover"
            placement="bottom"
            visible={openSettings}
            onVisibleChange={handlePopoverVisibleChange}
            title={lang(langIndex, "breadcrumb_plant_settings")}
            content={planSettingsContent}
            trigger="click"
          >
            <img className="top-button" src={settingsIcon} />
          </Popover>
        )}
        {security_level == 4 && (
          <Popover
            className="labor-tracking-popover"
            placement="bottom"
            title="Labor Tracking"
            content={laborTrackingContent}
            trigger="click"
          >
            <img className="top-button" src={laborIcon} />
          </Popover>
        )}

        <Dropdown overlay={langMenu} placement="bottomLeft" arrow>
          <a onClick={(e) => e.preventDefault()}>
            <img className="top-button" src={langIcon} />
          </a>
        </Dropdown>

        <img
          className="top-button"
          src={fullscreenIcon}
          onClick={() => setIsFullScreen(true)}
        />
        <img className="top-button" src={logoutIcon} onClick={logOut} />
        <NotificationIcon />
      </div>
    </div>
  ) : (
    <div className="background">
      <div className="top-buttons-bg">
        <span className="txt-top-name">{fullname}</span>
        <img
          className="user-profile-image"
          style={{ cursor: "pointer" }}
          src={avatar === null || avatar === "" ? avarIcon : avatar}
          alt=""
        />
        <img className="top-button" src={collapseIcon} onClick={onCollapse} />

        <Dropdown overlay={subMenu} placement="bottomLeft" arrow>
          <a onClick={(e) => e.preventDefault()}>
            <img className="top-button" src={menuIcon} />
          </a>
        </Dropdown>

        {security_level == 4 && (
          <Popover
            className="labor-tracking-popover"
            placement="bottom"
            visible={openSettings}
            onVisibleChange={handlePopoverVisibleChange}
            title={lang(langIndex, "breadcrumb_plant_settings")}
            content={planSettingsContent}
            trigger="click"
          >
            <img className="top-button" src={settingsIcon} />
          </Popover>
        )}

        {security_level == 4 && (
          <Popover
            className="labor-tracking-popover"
            placement="bottom"
            title="Labor Tracking"
            content={laborTrackingContent}
            trigger="click"
          >
            <img className="top-button" src={laborIcon} />
          </Popover>
        )}

        <Dropdown overlay={langMenu} placement="bottomLeft" arrow>
          <a onClick={(e) => e.preventDefault()}>
            <img className="top-button" src={langIcon} />
          </a>
        </Dropdown>
        <img
          className="top-button"
          src={fullscreenIcon}
          onClick={() => setIsFullScreen(true)}
        />
        <img className="top-button" src={logoutIcon} onClick={logOut} />
        <NotificationIcon />
      </div>

      <div className="top-customer-bg">
        <span className="txt-top-customer-name">{customerInfo["name"]}</span>
        <img
          className="top-customer-logo"
          style={{ cursor: "pointer" }}
          src={
            avatar === null || avatar === ""
              ? "/assets/icons/ic_logout.svg"
              : customerInfo["logo"]
          }
          alt=""
        />
      </div>
    </div>
  );
}

export default MyHeader;
