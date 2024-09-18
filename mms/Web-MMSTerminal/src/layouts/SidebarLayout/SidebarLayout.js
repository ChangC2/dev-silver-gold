import { Drawer, Layout } from "antd";
import { useDispatch, useSelector } from "react-redux";
import { setAppDataStore } from "redux/actions/appActions";
import { appData } from "services/global";
import SidebarMenu from "../SidebarMenu/SidebarMenu";
import "./SidebarLayout.css";

const { Sider } = Layout;
function SidebarLayout(props) {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { collapsed, width } = appDataStore;

  return (
    <Layout>
      {width > 1024 ? (
        <Layout.Sider
          className="sidebar"
          theme="dark"
          collapsed={collapsed}
          collapsedWidth={0}
          trigger={null}
        >
          <SidebarMenu />
        </Layout.Sider>
      ) : (
        <Drawer
          placement={"left"}
          open={!collapsed}
          className="custom-drawer"
          onClose={() => {
            appData.collapsed = true;
            dispatch(setAppDataStore(appData));
          }}
        >
          <SidebarMenu />
        </Drawer>
      )}
      {props.children}
    </Layout>
  );
}

export default SidebarLayout;
