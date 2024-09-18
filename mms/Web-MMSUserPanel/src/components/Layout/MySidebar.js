import {
  AlertOutlined,
  ApiOutlined,
  ClusterOutlined,
  DashboardOutlined,
} from "@ant-design/icons";
import { Button, Drawer, Layout, Menu, Tooltip } from "antd";
import SubMenu from "antd/lib/menu/SubMenu";
import { useMemo, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import collapseIcon from "../../assets/icons/ic_collapse.svg";
import logo_expanded from "../../assets/images/logo.png";
import logo_collapsed from "../../assets/images/logo_collapsed.png";
import {
  MENU_DIVIDER,
  PAGETYPE,
  sizePad,
} from "../../services/common/constants";
import lang from "../../services/lang";
import { onSelectMenu } from "../../services/redux/reducers/app";

import { IconWidget } from "components/IconWidget/IconWidget";
import { useHistory } from "react-router-dom";
import FeatureModal from "./FeatureModal/FeatureModal";
import "./MySidebar.css";
import { USER_SITE } from "services/common/urls";

const { Sider } = Layout;
function MySidebar(props) {
  const dispatch = useDispatch();
  const history = useHistory();
  const appData = useSelector((x) => x.app);
  const { langIndex } = appData;
  const { isMenuCollapsed, setIsMenuCollapsed, screenSize } = props;
  const { security_level } = props;
  const { customerIdList } = props;
  const { customerInfoList } = props;
  const { customDashboardList } = props;
  const { setAutoScroll } = props;
  const [isShowFeatureModal, setIsShowFeatureModal] = useState(false);
  const [arrow, setArrow] = useState("Show");

  const mergedArrow = useMemo(() => {
    if (arrow === "Hide") {
      return false;
    }
    if (arrow === "Show") {
      return true;
    }
    return {
      pointAtCenter: true,
    };
  }, [arrow]);

  const menuList = customerIdList.sort().map((customer_id) => {
    const customerInfo = customerInfoList[customer_id];
    const groupInfo = customerInfo["groupInfo"];

    const subMenu = groupInfo.map((group) => {
      return (
        <Menu.Item
          key={
            PAGETYPE.cnc + MENU_DIVIDER + customer_id + MENU_DIVIDER + group.id
          }
        >
          {group.name}
        </Menu.Item>
      );
    });

    const cwSubMenu =
      customDashboardList[customer_id] == null ||
        customDashboardList[customer_id] == undefined ||
        customDashboardList[customer_id].length == 0
        ? ""
        : customDashboardList[customer_id].map((cd) => {
          return (
            <Menu.Item
              key={
                PAGETYPE.cw +
                MENU_DIVIDER +
                customer_id +
                MENU_DIVIDER +
                cd.id
              }
            >
              {cd.name}
            </Menu.Item>
          );
        });

    return customer_id === "hennig" ? (
      <SubMenu
        key={PAGETYPE.submenu + MENU_DIVIDER + customer_id}
        icon={
          customerInfoList[customer_id]["logo"] === null ||
            customerInfoList[customer_id]["logo"] === "" ? (
            <Tooltip
              placement="right"
              title={customerInfoList[customer_id].name}
              arrow={mergedArrow}
            >
              <DashboardOutlined />
            </Tooltip>
          ) : (
            <Tooltip
              placement="right"
              title={customerInfoList[customer_id].name}
              arrow={mergedArrow}
            >
              <img
                className="sider-customer-logo"
                style={{ cursor: "pointer" }}
                src={customerInfoList[customer_id]["logo"]}
                alt=""
              />
            </Tooltip>
          )
        }
        title={isMenuCollapsed ? "" : customerInfoList[customer_id].name}
      >
        <Menu.Item
          key={PAGETYPE.hennig_iot + MENU_DIVIDER + customer_id}
          icon={<AlertOutlined />}
        >
          Iot
        </Menu.Item>

        <Menu.Item
          key={PAGETYPE.hennig_alarm + MENU_DIVIDER + customer_id}
          icon={<ClusterOutlined />}
        >
          Alarms
        </Menu.Item>
      </SubMenu>
    ) : (
      <SubMenu
        key={PAGETYPE.submenu + MENU_DIVIDER + customer_id}
        icon={
          customerInfoList[customer_id]["logo"] === null ||
            customerInfoList[customer_id]["logo"] === "" ? (
            <Tooltip
              placement="right"
              title={customerInfoList[customer_id].name}
              arrow={mergedArrow}
            >
              <DashboardOutlined />
            </Tooltip>
          ) : (
            <Tooltip
              placement="right"
              title={customerInfoList[customer_id].name}
              arrow={mergedArrow}
            >
              <img
                className="sider-customer-logo"
                style={{ cursor: "pointer" }}
                src={customerInfoList[customer_id]["logo"]}
                alt=""
              />
            </Tooltip>
          )
        }
        title={isMenuCollapsed ? "" : customerInfoList[customer_id].name}
      >
        {
          <>
            <Menu.Item
              key={PAGETYPE.ud + MENU_DIVIDER + customer_id}
              icon={<DashboardOutlined />}
            >
              Utilization Dashboard
            </Menu.Item>
            <SubMenu
              key={PAGETYPE.cnc + MENU_DIVIDER + customer_id}
              icon={<DashboardOutlined />}
              title={"Stations"}
            >
              <Menu.Item
                key={
                  PAGETYPE.cnc +
                  MENU_DIVIDER +
                  customer_id +
                  MENU_DIVIDER +
                  "all"
                }
              >
                {lang(langIndex, "All")}
              </Menu.Item>
              {subMenu}
            </SubMenu>
            {customer_id === "faxon" && (
              <SubMenu
                key={PAGETYPE.cd + MENU_DIVIDER + customer_id}
                icon={<DashboardOutlined />}
                // title={lang(langIndex, "sidebar_menu_dashboard")}
                title={"Custom Dashboards - Slytek"}
              >
                <Menu.Item
                  key={
                    PAGETYPE.cd +
                    MENU_DIVIDER +
                    customer_id +
                    MENU_DIVIDER +
                    "Cleaning Station"
                  }
                >
                  {"Cleaning Station"}
                </Menu.Item>
                <Menu.Item
                  key={
                    PAGETYPE.cd +
                    MENU_DIVIDER +
                    customer_id +
                    MENU_DIVIDER +
                    "Cleaning Station 2"
                  }
                >
                  {"Cleaning Station 2"}
                </Menu.Item>
                <Menu.Item
                  key={
                    PAGETYPE.cd +
                    MENU_DIVIDER +
                    customer_id +
                    MENU_DIVIDER +
                    "Blast Booth"
                  }
                >
                  {"Blast Station"}
                </Menu.Item>
                <Menu.Item
                  key={
                    PAGETYPE.cd +
                    MENU_DIVIDER +
                    customer_id +
                    MENU_DIVIDER +
                    "Phosphate Line 1"
                  }
                >
                  {"Phosphate Line 1"}
                </Menu.Item>

                <Menu.Item
                  key={
                    PAGETYPE.cd +
                    MENU_DIVIDER +
                    customer_id +
                    MENU_DIVIDER +
                    "Phosphate Line 2"
                  }
                >
                  {"Phosphate Line 2"}
                </Menu.Item>

                <Menu.Item
                  key={
                    PAGETYPE.cd +
                    MENU_DIVIDER +
                    customer_id +
                    MENU_DIVIDER +
                    "Paint Booth"
                  }
                >
                  {"Paint"}
                </Menu.Item>
                <Menu.Item
                  key={
                    PAGETYPE.cd +
                    MENU_DIVIDER +
                    customer_id +
                    MENU_DIVIDER +
                    "Assembly Blu_137"
                  }
                >
                  {"Assembly Blu/137"}
                </Menu.Item>

                <Menu.Item
                  key={
                    PAGETYPE.cd +
                    MENU_DIVIDER +
                    customer_id +
                    MENU_DIVIDER +
                    "Assembly Blu_137_2"
                  }
                >
                  {"Assembly Blu/137-2"}
                </Menu.Item>

                <Menu.Item
                  key={
                    PAGETYPE.cd +
                    MENU_DIVIDER +
                    customer_id +
                    MENU_DIVIDER +
                    "Frag Cell"
                  }
                >
                  {"Frag Cell"}
                </Menu.Item>

                <Menu.Item
                  key={
                    PAGETYPE.cd +
                    MENU_DIVIDER +
                    customer_id +
                    MENU_DIVIDER +
                    "Assembly Blu_136"
                  }
                >
                  {"Assembly Blu/136"}
                </Menu.Item>

                <Menu.Item
                  key={
                    PAGETYPE.cd +
                    MENU_DIVIDER +
                    customer_id +
                    MENU_DIVIDER +
                    "Assembly Blu_136_2"
                  }
                >
                  {"Assembly Blu/136-2"}
                </Menu.Item>

                <Menu.Item
                  key={
                    PAGETYPE.cd +
                    MENU_DIVIDER +
                    customer_id +
                    MENU_DIVIDER +
                    "Assembly Blu_136_3"
                  }
                >
                  {"Assembly Blu/136-3"}
                </Menu.Item>

                <Menu.Item
                  key={
                    PAGETYPE.cd +
                    MENU_DIVIDER +
                    customer_id +
                    MENU_DIVIDER +
                    "Assembly Blu_136_4"
                  }
                >
                  {"Assembly Blu/136-4"}
                </Menu.Item>

                <Menu.Item
                  key={
                    PAGETYPE.cd +
                    MENU_DIVIDER +
                    customer_id +
                    MENU_DIVIDER +
                    "Assembly Sub"
                  }
                >
                  {"Assembly Sub"}
                </Menu.Item>

                <Menu.Item
                  key={
                    PAGETYPE.cd +
                    MENU_DIVIDER +
                    customer_id +
                    MENU_DIVIDER +
                    "Quality Control"
                  }
                >
                  {"Quality Station"}
                </Menu.Item>
              </SubMenu>
            )}

            <SubMenu
              key={PAGETYPE.cw + MENU_DIVIDER + customer_id}
              icon={<DashboardOutlined />}
              // title={lang(langIndex, "sidebar_menu_dashboard")}
              title="Custom Dashboards - User"
            >
              <Menu.Item
                key={
                  PAGETYPE.cw +
                  MENU_DIVIDER +
                  customer_id +
                  MENU_DIVIDER +
                  "all"
                }
              >
                All
              </Menu.Item>
              {cwSubMenu}
            </SubMenu>

            <Menu.Item
              key={PAGETYPE.tms + MENU_DIVIDER + customer_id}
              icon={<ClusterOutlined />}
            >
              TOS
            </Menu.Item>

            <Menu.Item
              key={PAGETYPE.tms_alarm + MENU_DIVIDER + customer_id}
              icon={<ClusterOutlined />}
            >
              TOS Alarms
            </Menu.Item>

            <Menu.Item
              key={PAGETYPE.iiot + MENU_DIVIDER + customer_id}
              icon={<AlertOutlined />}
            >
              IIot
            </Menu.Item>

            {security_level == 4 && (
              <Menu.Item
                key={PAGETYPE.cnc_maintenance + MENU_DIVIDER + customer_id}
                icon={<ApiOutlined />}
              >
                {lang(langIndex, "sidebar_menu_maintanence")}
              </Menu.Item>
            )}
          </>
        }
      </SubMenu>
    );
  });

  const onCustomerClicked = (item) => {
    var keyList = item.key.split(MENU_DIVIDER);

    onSelectMenu(keyList[1], keyList[0], keyList[2], dispatch);

    if (appData.isAutoScroll == true) setAutoScroll(false);

    if (keyList[0] == PAGETYPE.tms && appData.page_type != PAGETYPE.tms) {
      history.push(`${USER_SITE}/pages/tms`);
    }

    if (
      keyList[0] == PAGETYPE.tms_alarm &&
      appData.page_type != PAGETYPE.tms_alarm
    ) {
      history.push(`${USER_SITE}/pages/tms_alarm`);
    }

    if (keyList[0] == PAGETYPE.iiot && appData.page_type != PAGETYPE.iiot) {
      history.push(`${USER_SITE}/pages/iiot`);
    }
    if (keyList[0] == PAGETYPE.cnc && appData.page_type != PAGETYPE.cnc) {
      history.push(`${USER_SITE}/pages/machine`);
    }
    if (
      keyList[0] == PAGETYPE.cnc_maintenance &&
      appData.page_type != PAGETYPE.cnc_maintenance
    ) {
      history.push(`${USER_SITE}/pages/maintenance`);
    }
    if (keyList[0] == PAGETYPE.cd && keyList.length == 3) {
      history.push(`${USER_SITE}/pages/machine/${keyList[1]}/${keyList[2]}`);
    }

    if (keyList[0] == PAGETYPE.cw && keyList.length == 3) {
      history.push(`${USER_SITE}/pages/cw/${keyList[1]}/${keyList[2]}`);
    }

    if (
      keyList[0] == PAGETYPE.ud &&
      appData.page_type != PAGETYPE.ud
    ) {
      history.push(`${USER_SITE}/pages/ud`);
    }

    if (
      keyList[0] == PAGETYPE.hennig_alarm &&
      appData.page_type != PAGETYPE.hennig_alarm
    ) {
      history.push(`${USER_SITE}/pages/hennig_alarm`);
    }

    if (
      keyList[0] == PAGETYPE.hennig_iot &&
      appData.page_type != PAGETYPE.hennig_iot
    ) {
      history.push(`${USER_SITE}/pages/hennig_iot`);
    }
    if (screenSize.width < sizePad) setIsMenuCollapsed(true);
  };

  return screenSize.width >= sizePad ? (
    // desktop version
    <div>
      <Sider
        style={{ height: screenSize.height }}
        collapsible
        collapsed={isMenuCollapsed}
        onCollapse={() => setIsMenuCollapsed(!isMenuCollapsed)}
        className={isMenuCollapsed ? "menu-collapsed-style" : "custom-sidebar"}
      >
        <div
          className="logo"
          style={{ textAlign: "center", paddingTop: 60, paddingBottom: 20 }}
        >
          <img
            alt="logo"
            src={isMenuCollapsed ? logo_collapsed : logo_expanded}
            style={{ height: 32 }}
          />

          <div
            className="side-collapse"
            onClick={() => {
              props.setIsMenuCollapsed(!props.isMenuCollapsed);
            }}
          >
            <IconWidget Icon={collapseIcon} />
          </div>
        </div>

        <div
          className="content-scrollbar-style menu-container"
          style={{ maxHeight: screenSize.height - 250 }}
        >
          <Menu
            className="c-menu"
            theme="dark"
            // defaultSelectedKeys={[
            //   appData.page_type == PAGETYPE.cnc
            //     ? appData.page_type +
            //     MENU_DIVIDER +
            //     appData.customer_id +
            //     MENU_DIVIDER +
            //     appData.group_id
            //     : appData.page_type + MENU_DIVIDER + appData.customer_id,
            //   ,
            // ]}
            // defaultOpenKeys={[
            //   PAGETYPE.submenu + MENU_DIVIDER + appData.customer_id,
            //   appData.page_type + MENU_DIVIDER + appData.customer_id,
            // ]}
            onClick={onCustomerClicked}
            mode="inline"
          >
            {menuList}
          </Menu>
        </div>

        <FeatureModal
          isShowFeatureModal={isShowFeatureModal}
          setIsShowFeatureModal={setIsShowFeatureModal}
          customerInfoList={customerInfoList}
          customerIdList={customerIdList}
        />
        {!isMenuCollapsed && (
          <div
            className="sidebar-bottom-button-container"
            style={{ marginTop: 30 }}
          >
            <Button
              ghost
              onClick={() => {
                setIsShowFeatureModal(true);
              }}
              className="sidebar-bottom-button"
            >
              {lang(langIndex, "feature_Request_Form")}
            </Button>
          </div>
        )}
      </Sider>
    </div>
  ) : (
    // mobile version
    <Drawer
      title={
        <div className="logo" style={{ paddingTop: 60, paddingBottom: 20 }}>
          <img
            alt="logo"
            src={isMenuCollapsed ? logo_collapsed : logo_expanded}
            style={{ height: 32 }}
          />
        </div>
      }
      closable={true}
      onClose={() => setIsMenuCollapsed(true)}
      visible={!isMenuCollapsed}
      className={"menu-collapsed-style"}
      placement={"left"}
    >
      <Menu
        theme="dark"
        defaultSelectedKeys={[
          appData.page_type == PAGETYPE.cnc
            ? appData.page_type +
            MENU_DIVIDER +
            appData.customer_id +
            MENU_DIVIDER +
            appData.group_id
            : appData.page_type + MENU_DIVIDER + appData.customer_id,
        ]}
        defaultOpenKeys={[
          PAGETYPE.submenu + MENU_DIVIDER + appData.customer_id,
          appData.page_type + MENU_DIVIDER + appData.customer_id,
        ]}
        onClick={onCustomerClicked}
        mode="inline"
      >
        {menuList}
      </Menu>
      <FeatureModal
        isShowFeatureModal={isShowFeatureModal}
        setIsShowFeatureModal={setIsShowFeatureModal}
        customerInfoList={customerInfoList}
        customerIdList={customerIdList}
      />
      <div
        className="sidebar-bottom-button-container"
        style={{ marginTop: 30 }}
      >
        <Button
          ghost
          onClick={() => {
            setIsMenuCollapsed(true);
            setIsShowFeatureModal(true);
          }}
          className="sidebar-bottom-button"
        >
          {lang(langIndex, "feature_Request_Form")}
        </Button>
      </div>
    </Drawer>
  );
}

export default MySidebar;
