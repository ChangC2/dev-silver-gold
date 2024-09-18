import { Modal } from "antd";
import { useSelector } from "react-redux";
import lang from "../../../services/lang";
import ReportDays from "./ReportDays/ReportDays";
import ReportEmails from "./ReportEmails/ReportEmails";
import "./ReportSettings.css";
import { SettingOutlined } from "@ant-design/icons";

function ReportSettings(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { showModal, setShowModal, customerInfo, updateCustomerInfo } = props;

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
        onOk={() => onCancel()}
        okText={lang(langIndex, "plant_reportsetting")}
        closable={true}
        className="customer-setting-dialog-style"
        destroyOnClose={true}
        footer={null}
        width={700}
      >
        <div className="customer-setting-page-title">
          <SettingOutlined />
          <span style={{ marginLeft: 10 }} />
          {lang(langIndex, "plant_reportsetting")}
        </div>

        <div>
          <ReportDays
            customerInfo={customerInfo}
            updateCustomerInfo={updateCustomerInfo}
          />
          <ReportEmails
            customerInfo={customerInfo}
            updateCustomerInfo={updateCustomerInfo}
          />
        </div>
      </Modal>
    </div>
  );
}

export default ReportSettings;
