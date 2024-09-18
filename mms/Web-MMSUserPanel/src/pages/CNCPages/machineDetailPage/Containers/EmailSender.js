import React, { useState, useEffect } from "react";
import { SendOutlined } from "@ant-design/icons";
import { message, Modal } from "antd";
import Urls, { postRequest } from "../../../../services/common/urls";
import "../MachineDetailPage.css";

const { confirm } = Modal;

function EmailSender(props) {
  const { hstInfo, ganttInfo, machineInfo, customerInfo, customer_id } = props;
  const [emailList, setEmailList] = useState([]);
  const [setIsLoading] = useState(true);

  useEffect(() => {
    const url = Urls.GET_TABLE;
    const param = {
      table: customer_id + "_info",
      where: "",
    };
    postRequest(url, param, (res) => {
      var data = res.data[0];
      if (data === undefined) {
        // setSettingModalVisible(false);
      }
      var _mailStringList = data["emails"].split(";");
      var _emailList = _mailStringList
        .filter((v) => v !== "")
        .map((str) => {
          var tmp = str.split(":");
          var _email = { email: tmp[0] };

          if (tmp.length > 1) {
            _email["enabled"] = tmp[1] === "1";
          } else {
            _email["enabled"] = true;
          }
          return _email;
        });
      setEmailList([..._emailList]);

      setIsLoading(false);
    });
  }, []);

  const onClickSend = () => {
    if (emailList.length === 0) {
      message.warn("Warning! Please check the emails...");
      return;
    }

    if (ganttInfo.length === 0) {
      message.warn("Warning! There's no info to send.");
      return;
    }

    showConfirm();
  };
  const replaceAll = (string, search, replace) => {
    return string.split(search).join(replace);
  };
  const sendEmail = () => {
    var url = Urls.SEND_EMAIL;
    var currentDate = hstInfo.date;
    currentDate = replaceAll(currentDate, "/", "_");

    var param = {
      time: currentDate,
      mails: emailList.filter((e) => e.enabled === true).map((e) => e.email),
      m_status: machineInfo,
      m_hst: hstInfo,
      m_gantt: ganttInfo,
      m_info: customerInfo,
    };
    postRequest(url, param, (res) => {
      message.success("Email has been sent.");
    });
  };
  const showConfirm = () => {
    var emailString = emailList
      .filter((e) => e.enabled === true)
      .map((e) => e.email)
      .join("," + " \n");
    confirm({
      className: "confirm-dialog",
      title: "Current machine info will be sent to",
      content: emailString,
      onOk() {
        sendEmail();
        // onRemoveItem(index);
        return new Promise((resolve, reject) => {
          setTimeout(resolve, 200);
        }).catch(() => {});
      },
      onCancel() {
        return false;
      },
    });
  };
  return (
    <div>
      <div className="sendEmailContainer">
        <SendOutlined className="sendEmail" onClick={onClickSend} />
      </div>
    </div>
  );
}

export default EmailSender;
