import { InfoCircleOutlined } from "@ant-design/icons";
import { Col, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import "./CycleAlertInfo.css";

function CycleAlertInfoModal(props) {
  const { cycleAlertInfoModal, setCycleAlertInfoModal } = props;

  const data = [
    {
      title: "Metro PCS:",
      format: "##########@mymetropcs.com",
    },
    {
      title: "T-Mobile:",
      format: "##########@tmomail.net",
    },
    {
      title: "Verizon Wireless:",
      format: "##########@vtext.com",
    },
    { title: "AT&T:", format: "##########@txt.att.net" },
    {
      title: "Sprint PCS:",
      format: "##########@messaging.sprintpcs.com",
    },
    {
      title: "Nextel:",
      format: "##########@messaging.nextel.com",
    },
    {
      title: "Cricket:",
      format: "##########@sms.mycricket.com",
    },
    {
      title: "US Cellular:",
      format: "##########@email.uscc.net",
    },

    {
      title: "Cingular (GSM):",
      format: "##########@cingularme.com",
    },
    {
      title: "Cingular (TDMA):",
      format: "##########@mmode.com",
    },
  ];

  const dataUI = data.map((info,index) => {
    return (
      <Row className="cycler_alert_info_row my-own-button" key={"format-key-" + index}>
        <Col span={6} className="cycler_alert_info_key">
          {info.title}
        </Col>
        <Col span={18} className="cycler_alert_info_format">
          {info.format}
        </Col>
      </Row>
    );
  });

  return (
    <div>
      <Modal
        centered
        open={cycleAlertInfoModal}
        title={null}
        onCancel={() => setCycleAlertInfoModal(false)}
        onOk={() => setCycleAlertInfoModal(false)}
        closable={true}
        cancelButtonProps={{ style: { display: "none" } }}
        className="app-setting-dialog-style"
        destroyOnClose={true}
        width={700}
      >
        <div>
          <div>
            <div className="cycler_alert_info_title">
              <Row>
                <Col span={1}>
                  <InfoCircleOutlined />
                </Col>
                <Col span={12}>{"About Cycle Stop Alert"}</Col>
              </Row>
            </div>
            <div className="cycler_alert_info_content">
              <div>
                {
                  "Please enter your email and mobile number. If using mobile number, use following format according to your carrier."
                }
              </div>
              <div>{dataUI}</div>
            </div>
          </div>
        </div>
      </Modal>
    </div>
  );
}

export default CycleAlertInfoModal;
