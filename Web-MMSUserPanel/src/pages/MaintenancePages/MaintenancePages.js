import { Spin } from "antd";
import React, { useEffect, useState } from "react";
import { connect } from "react-redux";
import { GetStatus } from "../../services/common/cnc_apis";
import MaintenancePage from "../CNCPages/maintenancePage/maintenancePage";
import "./MaintenancePages.css";
function MainTenancePages(props) {
  const selCustomerId = props.app.customer_id;

  useEffect(() => {
    GetStatus(selCustomerId, props.dispatch);
  }, [selCustomerId]);

  const machineList = props.cncService.machineInfoList[selCustomerId];
  var isPageLoading = machineList == undefined;

  return (
    <div>
      {isPageLoading && (
        <div
          style={{ paddingTop: 100, textAlign: "center" }}
        >
          <Spin tip="Loading ..." size="large" />
        </div>
      )}

      {!isPageLoading && (
        <MaintenancePage
          selCustomerId={selCustomerId}
          machineList={machineList}
        />
      )}
    </div>
  );
}

const mapStateToProps = (state, props) => ({
  cncService: state.cncService,
  app: state.app,
});
const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});
export default connect(mapStateToProps, mapDispatchToProps)(MainTenancePages);
