import {
  SettingOutlined
} from "@ant-design/icons";
import { Col, Modal, Row } from "antd";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import Urls, { postRequest } from "../../../services/common/urls";
import lang from "../../../services/lang";
import ApkBeta from "./ApkManager/ApkBeta";
import ApkProduct from "./ApkManager/ApkProduct";
import CustomerLogo from "./CustomerLogo/CustomerLogo";
import CustomerName from "./CustomerName/CustomerName";
import "./CustomerSettings.css";

function CustomerSettings(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { showModal, setShowModal, customerInfo, updateCustomerInfo } = props;
  const [customerLogo, setCustomerLogo] = useState("");
  const [customerName, setCustomerName] = useState("");

  const [version1, setVersion1] = useState("");
  const [version2, setVersion2] = useState("");

  useEffect(() => {
    setCustomerLogo(customerInfo.logo);
    setCustomerName(customerInfo.name);
  }, [customerInfo]);

  const onUpdateCustomerInfo = () => {
    if (customerInfo.logo == customerLogo && customerName == customerInfo.name)
      return;
    let newInfo = customerInfo;
    newInfo.logo = customerLogo;
    newInfo.name = customerName;
    setShowModal(false);
    updateCustomerInfo(newInfo);
  };

  useEffect(() => {
    var url = Urls.GET_TABLE;
    var param = {
      table: "tbl_versions",
    };
    postRequest(url, param, (res) => {
      if (res.status === true) {
        setVersion1(res.data[0].version1);
        setVersion2(res.data[0].version2);
      }
    });
  }, []);

  const onCancel = () => {
    setShowModal(false);
  };

  return (
    <div>
      <Modal
        centered
        visible={showModal}
        title={null}
        onCancel={() => onCancel()}
        onOk={() => onUpdateCustomerInfo()}
        okText={lang(langIndex, "plant_update")}
        closable={true}
        className="customer-setting-dialog-style"
        destroyOnClose={true}
        width={700}
      >
        <div className="customer-setting-page-title">
          <SettingOutlined />
          <span style={{marginLeft: 10}} />
          {lang(langIndex, "plant_customersetting")}
        </div>

        <div className="customer-setting-container-style">
          <CustomerName
            customerName={customerName}
            setCustomerName={setCustomerName}
          />
          <CustomerLogo
            customerLogo={customerLogo}
            setCustomerLogo={setCustomerLogo}
          />
          <Row>
            <Col span={12}>
              <ApkBeta
                customerLogo={customerLogo}
                setCustomerLogo={setCustomerLogo}
                version={version1}
              />
            </Col>
            <Col span={12}>
              <ApkProduct
                customerLogo={customerLogo}
                setCustomerLogo={setCustomerLogo}
                version={version2}
              />
            </Col>
          </Row>
        </div>
      </Modal>
    </div>
  );
}

export default CustomerSettings;
