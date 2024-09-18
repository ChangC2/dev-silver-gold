import { Select, Spin } from "antd";
import { useEffect } from "react";
import { connect, useSelector } from "react-redux";
import AllDashBoards from "./AllDashBoards";

import "./CustomDashboard.css";
import CustomWidgetsPage from "./CustomWidgetsPage";

const { Option } = Select;

const CustomDashboard = (props) => {
  const appData = useSelector((state) => state.app);

  const { customer_id, dashboard_id } = props.match.params;

  let isPageLoading = false;

  useEffect(() => {}, []);

  return (
    <div style={{ margin: 15 }}>
      {isPageLoading && (
        <div
          style={{ paddingTop: 100, textAlign: "center" }}
        >
          <Spin tip="Loading ..." size="large" />
        </div>
      )}
      {!isPageLoading && (
        <div>
          {dashboard_id === "all" && (
            <AllDashBoards customer_id={customer_id} />
          )}
          {dashboard_id !== "all" && dashboard_id !== "" && (
            <CustomWidgetsPage
              customer_id={customer_id}
              dashboard_id={dashboard_id}
            />
          )}
        </div>
      )}
    </div>
  );
};

const mapStateToProps = (state, props) => ({
  cwService: state.cwService,
  app: state.app,
});

const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});

export default connect(mapStateToProps, mapDispatchToProps)(CustomDashboard);
