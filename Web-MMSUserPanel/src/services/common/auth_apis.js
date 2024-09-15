import Urls, { postRequest } from "./urls";
import { message } from "antd";
import * as actionType from "../redux/actionType";

export function Login(customerId, username, password, dispatch, callback) {
  const url = Urls.LOGIN;
  const param = {
    customer_id: customerId,
    username: username,
    password: password,
  };
  postRequest(url, param, (res) => {
    if (res != null && res.data.length > 0) {
      try {
        const info = res.data[0];
        const data = {
          user_id: info["id"],
          username: username,
          customer_id: info["customer_id"],
          security_level: info["security_level"],
          fullname: info["username_full"],
          avatar: info["user_picture"],
          time_format: info["time_format"],
          is_started_auto: info["is_started_auto"],
          start_time: info["start_time"],
        };
        localStorage.setItem("username", username);
        message.success("Success to login");
        getLaborCategories(info["id"], dispatch, (res) => {});
        dispatch({
          type: actionType.actSetAuthInfo,
          data: data,
        });
        callback(true);
      } catch (e) {
        callback(false);
      }
    } else {
      callback(false);
    }
  });
}

export function SignOut(dispatch) {
  dispatch({
    type: actionType.actSignOut,
    data: "",
  });
}

export function AddLaborTracking(param, callback) {
  const url = Urls.Add_Labor_tracking;
  postRequest(url, param, (res) => {
    if (res === null) {
      callback(false);
    }
    callback(res);
  });
}

export function GetLaborSettings(user_id, dispatch, callback) {
  const url = Urls.Get_Labor_Settings;
  const param = {
    user_id: user_id,
  };
  postRequest(url, param, (res) => {
    if (res != null && res.data != null) {
      try {
        const info = res.data;
        const data = {
          time_format: info["time_format"],
          is_started_auto: info["is_started_auto"],
          start_time: info["start_time"],
        };
        dispatch({
          type: actionType.actSetLaborInfo,
          data: data,
        });
        callback(true);
      } catch (e) {
        callback(false);
      }
    } else {
      callback(false);
    }
  });
}

export function UpdateLaborSetting(
  user_id,
  time_format,
  is_started_auto,
  start_time,
  dispatch,
  callback
) {
  const url = Urls.Update_Labor_Setting;
  const param = {
    user_id: user_id,
    time_format: time_format,
    is_started_auto: is_started_auto,
    start_time: start_time,
  };
  postRequest(url, param, (res) => {
    if (res != null && res.status == true) {
      dispatch({
        type: actionType.actSetLaborInfo,
        data: param,
      });
      callback(true);
    } else {
      callback(false);
    }
  });
}

export function getLaborCategories(user_id, dispatch, callback) {
  const url = Urls.Get_Labor_Categories;
  postRequest(url, { user_id: user_id }, (res) => {
    callback(true);
    let data = "";
    if (res !== null && res.status === true) {
      data = res.data;
    }
    dispatch({
      type: actionType.authUpdateLaborCategories,
      data: data,
    });
  });
}

export function addLaborCategory(user_id, name, order, dispatch, callback) {
  const url = Urls.Add_Labor_Category;
  postRequest(url, { user_id: user_id, name: name, order: order }, (res) => {
    callback(true);
    let data = "";
    if (res !== null && res.status === true) {
      data = res.data;
    }
    dispatch({
      type: actionType.authUpdateLaborCategories,
      data: data,
    });
  });
}

export function updateLaborCategory(
  user_id,
  id,
  name,
  order,
  dispatch,
  callback
) {
  const url = Urls.Update_Labor_Category;
  postRequest(
    url,
    { user_id: user_id, id: id, name: name, order: order },
    (res) => {
      callback(true);
      let data = "";
      if (res !== null && res.status === true) {
        data = res.data;
      }
      dispatch({
        type: actionType.authUpdateLaborCategories,
        data: data,
      });
    }
  );
}

export function deleteLaborCategory(user_id, id, dispatch, callback) {
  const url = Urls.Delete_Labor_Category;
  postRequest(url, { user_id: user_id, id: id }, (res) => {
    callback(true);
    let data = "";
    if (res !== null && res.status === true) {
      data = res.data;
    }
    dispatch({
      type: actionType.authUpdateLaborCategories,
      data: data,
    });
  });
}
