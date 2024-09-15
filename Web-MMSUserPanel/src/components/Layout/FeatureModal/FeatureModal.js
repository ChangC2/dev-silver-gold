import { Col, message, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import React, { useEffect, useState } from "react";
import "./FeatureModal.css";
import { MailOutlined } from "@ant-design/icons";
import { useSelector } from "react-redux";
import lang from "../../../services/lang";
import { Input } from "antd";
import emailjs from "emailjs-com";

function FeatureModal(props) {
  const authData = useSelector((x) => x.authService);
  const { langIndex } = useSelector((x) => x.app);
  const { isShowFeatureModal, setIsShowFeatureModal , customerInfoList, customerIdList} = props;

  const { fullname } = authData;
  const [featureInfo, setFeatureInfo] = useState("");
  const [isWrong, setIsWrong] = useState(false);

  const onCancel = () =>{
    setFeatureInfo("");
    setIsShowFeatureModal(false);
  }

  const onSendMessage = () => {
    setIsShowFeatureModal(false);
    const customers = customerIdList.reduce((cname, customer_id) => cname + customerInfoList[customer_id].name + ",  ", "");
    var templateParams = {
      message: featureInfo,
      user: "Username: " + fullname,
      customers: "Customers: " + customers,
    };

    emailjs
      .send(
        "service_g3j9iwg",
        "template_b5u3g8r",
        templateParams,
        "user_BF6Jkdu2fck0RSnLCNmUq"
      )
      .then(
        function (response) {
          message.success(lang(langIndex, "msg_success_request"));
          setFeatureInfo("");
        },
        function (error) {
          message.error(error);
        }
      );
  };

  return (
    <div>
      <Modal
        centered
        visible={isShowFeatureModal}
        title={null}
        onCancel={() => onCancel()}
        onOk={() => onSendMessage()}
        closable={true}
        className="app-setting-dialog-style"
        destroyOnClose={true}
        width={700}
      >
        <div>
          <div>
            <div className="cycler_alert_info_title">
              <Row>
                <Col span={1}>
                  <MailOutlined />
                </Col>
                <Col span={12}>{lang(langIndex, "feature_Request_Form")}</Col>
              </Row>
            </div>
            <div className="cycler_alert_info_content">
              <div>{lang(langIndex, "feature_Request_desc")}</div>
              <div>
                <Input
                  placeholder={lang(langIndex, "feature_Request_Info")}
                  type="text"
                  style={{
                    background: "transparent",
                    color: "#cccccc",
                    marginTop: "10px",
                  }}
                  value={featureInfo}
                  onChange={(e) => {
                    setFeatureInfo(e.target.value);
                    setIsWrong(false);
                  }}
                />
              </div>
            </div>
          </div>
        </div>
      </Modal>
    </div>
  );
}

export default FeatureModal;
