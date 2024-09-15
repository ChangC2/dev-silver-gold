import { Col, Image, Row, Switch } from "antd";
import Modal from "antd/lib/modal/Modal";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import Urls, { MOBILE_URL, postRequest } from "../../../../../src/services/common/urls";
import lang from "../../../../../src/services/lang";
import "./InstallConfigModal.css";

function InstallConfigModal(props) {
  const { langIndex } = useSelector((x) => x.app);
  const {
    isShowInstallConfigModal,
    setIsShowInstallConfigModal,
    customer_id,
    machineInfo,
  } = props;

  const [installConfig, setInstallConfig] = useState({
    machine_name: "",
    serial_number: "",
    cycle_signal: "",
    cycle_interlock_interface: "",
    cycle_interlock_on: "",
    cycle_interlock_open: "",
    images: [],
  });

  useEffect(() => {
    const url = Urls.GET_MACHINE_INSTALL_CONFIG;

    const param = {
      customer_id: customer_id,
      machine_id: machineInfo.machine_id,
      timezone: 0,
    };
    postRequest(url, param, (res) => {
      if (res.status == true) {
        setInstallConfig({
          ...res.data.machine_details,
          images: res.data.images,
        });
      }
    });
  }, []);

  const imageUI = installConfig.images.map((image, index) => {
    return (
      <Col key={"format-key-" + index} style={{ marginLeft: 10 }}>
        <Image height={150} src={MOBILE_URL + image.image} />
      </Col>
    );
  });

  const setInstallConfigs = (field, value) => {
    var newSetting = { ...installConfig };
    newSetting[field] = value;
    setInstallConfig({ ...newSetting });
  };

  return (
    <div>
      <Modal
        centered
        visible={isShowInstallConfigModal}
        title={null}
        onCancel={() => setIsShowInstallConfigModal(false)}
        onOk={() => setIsShowInstallConfigModal(false)}
        closable={true}
        className="install-config-dialog-style"
        destroyOnClose={true}
        width={1000}
        cancelButtonProps={{
          style: {
            display: "none",
          },
        }}
      >
        <div>
          <div>
            <Row>
              <Col span={12} className="install-config-group-container">
                <div className="install-config-group-title">
                  {lang(langIndex, "machine_name")}
                </div>
                {installConfig.machine_name}
              </Col>
              <Col span={12} className="install-config-group-container">
                <div className="install-config-group-title">
                  {lang(langIndex, "serial_number")}
                </div>
                {installConfig.serial_number}
              </Col>
            </Row>
          </div>
          <div className="install-config-group-container">
            <div className="install-config-group-title">
              {lang(langIndex, "cycle_setting")}
            </div>
            <div className="install-config-group-details">
              <Row gutter={[16, 16]}>
                <Col span={8}>
                  <div className="install-config-sub-group-title">
                    {lang(langIndex, "cycle_signal")}
                  </div>
                  <div className="install-config-sub-group-container">
                    {installConfig.cycle_signal}
                  </div>
                </Col>
                <Col span={8}>
                  <div className="install-config-sub-group-title">
                    {lang(langIndex, "cycle_interlocked")}
                  </div>
                  <Row justify={"space-between"}>
                    <Col>
                      <div className="app-setting-radial-input-container">
                        <div>{lang(langIndex, "on_off")}</div>
                      </div>
                    </Col>
                    <Col>
                      <Switch
                        className="app-setting-radial-input"
                        checked={
                          installConfig.cycle_interlock_on == 1 ? true : false
                        }
                      />
                    </Col>
                  </Row>
                  <Row justify={"space-between"}>
                    <Col>
                      <div className="app-setting-radial-input-container">
                        <div>{lang(langIndex, "normally_closed_open")}</div>
                      </div>
                    </Col>
                    <Col>
                      <Switch
                        className="app-setting-radial-input"
                        checked={
                          installConfig.cycle_interlock_open == 1 ? true : false
                        }
                      />
                    </Col>
                  </Row>
                </Col>
                <Col span={8}>
                  <div className="install-config-sub-group-title">
                    {lang(langIndex, "cycle_interlock_interface")}
                  </div>
                  <div
                    className="install-config-sub-group-container"
                    style={{ whiteSpace: "pre-line" }}
                  >
                    {installConfig.cycle_interlock_interface}
                  </div>
                </Col>
              </Row>
            </div>
          </div>
          <div className="install-config-group-container">
            <div className="install-config-group-title">
              {lang(langIndex, "pictures")}
            </div>
            <Row>{imageUI}</Row>
          </div>
        </div>
      </Modal>
    </div>
  );
}

export default InstallConfigModal;
