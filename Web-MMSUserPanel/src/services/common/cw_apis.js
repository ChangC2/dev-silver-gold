import Urls, { postRequest } from "./urls";

import * as actionType from "../redux/actionType";

export function getCustomDashboards(dispatch, callback) {
  const url = Urls.Get_Custom_Dashboards;
  postRequest(url, {}, (res) => {
    callback(true);
    let data = "";
    if (res !== null && res.status === true) {
      data = res.data;
    }
    dispatch({
      type: actionType.cwUpdateDashboards,
      data: data,
    });
  });
}

export function addCustomDashboard(
  customer_id,
  name,
  order,
  dispatch,
  callback
) {
  const url = Urls.Add_Custom_Dashboard;
  postRequest(
    url,
    { customer_id: customer_id, name: name, order: order },
    (res) => {
      callback(true);
      let data = "";
      if (res !== null && res.status === true) {
        data = res.data;
      }
      dispatch({
        type: actionType.cwUpdateDashboards,
        data: data,
      });
    }
  );
}

export function updateCustomDashboard(id, name, order, dispatch, callback) {
  const url = Urls.Update_Custom_Dashboard;
  postRequest(url, { id: id, name: name, order: order }, (res) => {
    callback(true);
    let data = "";
    if (res !== null && res.status === true) {
      data = res.data;
    }
    dispatch({
      type: actionType.cwUpdateDashboards,
      data: data,
    });
  });
}

export function deleteCustomDashboard(id, dispatch, callback) {
  const url = Urls.Delete_Custom_Dashboard;
  postRequest(url, { id: id }, (res) => {
    callback(true);
    let data = "";
    if (res !== null && res.status === true) {
      data = res.data;
    }
    dispatch({
      type: actionType.cwUpdateDashboards,
      data: data,
    });
  });
}

export function AddCustomeWidgetFilters(param, dispatch, callback) {
  const url = Urls.ADD_CUSTOME_WIDGET;
  postRequest(url, param, (res) => {
    let data = "";
    if (res !== null && res.status === true) {
      data = res.data;
      callback(true);
    } else {
      callback(false);
    }
    dispatch({
      type: actionType.cwUpdateWidgets,
      data: data,
    });
  });
}

export function UpdateCustomeWidgetFilters(param, dispatch, callback) {
  const url = Urls.EDIT_CUSTOME_WIDGET;
  postRequest(url, param, (res) => {
    let data = "";
    if (res !== null && res.status === true) {
      data = res.data;
      callback(true);
    } else {
      callback(false);
    }
    dispatch({
      type: actionType.cwUpdateWidgets,
      data: data,
    });
  });
}

export function GetCustomeWidget(customerId, dashboardId, dispatch, callback) {
  const url = Urls.GET_CUSTOME_WIDGET;
  const param = {
    customer_id: customerId,
    dashboard_id: dashboardId,
  };
  postRequest(url, param, (res) => {
    let data = [];
    if (res !== null && res.status === true) {
      data = res.data;
      callback(true);
    } else {
      callback(false);
    }
    dispatch({
      type: actionType.cwUpdateWidgets,
      data: data,
    });
  });
}

export function DeleteCustomeWidgetFilters(param, dispatch, callback) {
  const url = Urls.Delete_Custom_WIDGET;
  postRequest(url, param, (res) => {
    let data = "";
    if (res !== null && res.status === true) {
      data = res.data;
      callback(true);
    } else {
      callback(false);
    }
    dispatch({
      type: actionType.cwUpdateWidgets,
      data: data,
    });
  });
}

export function getCustomDashboardReport(
  widgetDatas,
  logoData,
  customWidgets,
  callback
) {
  const url = Urls.GET_CUSTOM_DASHBOARD_REPORT;
  const param = {
    widgetDatas: widgetDatas,
    logoData: logoData,
    customWidgets: customWidgets,
  };

  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function GetCustomeWidgetFilters(customerId, callback) {
  const url = Urls.GET_CUSTOME_WIDGET_FILTERS;
  const param = {
    customer_id: customerId,
  };

  postRequest(url, param, (res) => {
    if (res.status === true) {
      callback(res.data);
    } else {
      callback(null);
    }
  });
}
