import { Menu } from "antd";
import LanguageModal from "pages/LanguageModal/LanguageModal";
import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setAppDataStore } from "redux/actions/appActions";
import { appData } from "services/global";
import ic_oee_dashboard from "../../assets/icons/ic_menu_dashboard.png";
import ic_menu_install from "../../assets/icons/ic_menu_install.png";
import ic_menu_maintenance from "../../assets/icons/ic_menu_maintenance.png";
import ic_menu_setting from "../../assets/icons/ic_menu_setting.png";
import ic_menu_timereport from "../../assets/icons/ic_menu_timereport.png";
import ic_nav_language from "../../assets/icons/ic_nav_language.png";
import logo from "../../assets/icons/logo.png";
import "./SidebarMenu.css";

const SidebarMenu = (props) => {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { open } = props;
  const { pages } = appDataStore;
  const [isShowLanguageModal, setIsShowLanguageModal] = useState(false);

  const onMenuClick = (info) => {
    open(false);
    if (info.page_index === 5) {
      setIsShowLanguageModal(true);
    } else {
      if (info.page_index < 2) {
        appData.pages = [info.page_index];
        console.log("SidebarMenu:appData=>", appData);
        dispatch(setAppDataStore(appData));
      } else {
        let newPages = pages;
        newPages.push(info.page_index);
        appData.pages = newPages;
        dispatch(setAppDataStore(appData));
      }
    }
  };

  const NAV_MENU_IMTES = [
    {
      page_index: 0,
      title: "OEE Dashboard",
      icon: (
        <img
          style={{ width: "20px", height: "20px", color: "white" }}
          src={ic_oee_dashboard}
        />
      ),
      active: true,
    },
    {
      page_index: 1,
      title: "Process Monitor",
      icon: (
        <img
          style={{ width: "20px", height: "20px", color: "white" }}
          src={ic_menu_timereport}
        />
      ),
      active: false,
    },
    {
      page_index: 2,
      title: "Settings",
      icon: (
        <img
          style={{ width: "20px", height: "20px", color: "white" }}
          src={ic_menu_setting}
        />
      ),
      active: false,
    },
    {
      page_index: 3,
      title: "Install Configuration",
      icon: (
        <img
          style={{ width: "20px", height: "20px", color: "white" }}
          src={ic_menu_install}
        />
      ),
      active: false,
    },
    {
      page_index: 4,
      title: "Maintenance",
      icon: (
        <img
          style={{ width: "20px", height: "20px", color: "white" }}
          src={ic_menu_maintenance}
        />
      ),
      active: false,
    },
    {
      page_index: 5,
      title: "Language",
      icon: (
        <img
          style={{ width: "20px", height: "20px", color: "white" }}
          src={ic_nav_language}
        />
      ),
      active: false,
    },
  ];

  const menuUI = NAV_MENU_IMTES.map((info, index) => {
    return (
      <Menu.Item
        className="c-menu-item"
        key={info.page_index}
        icon={info.icon}
        onClick={() => {
          onMenuClick(info);
        }}
      >
        {info.title}
      </Menu.Item>
    );
  });

  return (
    <div style={{ textAlign: "center" }}>
      <LanguageModal
        isShowLanguageModal={isShowLanguageModal}
        setIsShowLanguageModal={setIsShowLanguageModal}
      />
      <img
        className="drawer-logo"
        alt="logo"
        src={logo}
        style={{ height: 32 }}
      />
      <Menu theme="dark" mode="inline" className="c-menu">
        {menuUI}
      </Menu>
    </div>
  );
};

export default SidebarMenu;
