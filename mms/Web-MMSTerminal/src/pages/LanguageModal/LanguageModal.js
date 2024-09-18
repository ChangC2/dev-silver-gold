import { GlobalOutlined } from "@ant-design/icons";
import { Col, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import "./LanguageModal.css";

function LanguageModal(props) {
  const { isShowLanguageModal, setIsShowLanguageModal } = props;


  const onCancel = () => {
    setIsShowLanguageModal(false);
  };


  return (
    <div>
      <Modal
        centered
        open={isShowLanguageModal}
        className="language-dialog-style"
        title={null}
        onCancel={() => onCancel()}
        closable={true}
        maskClosable={false}
        destroyOnClose={true}
        width={700}
        okButtonProps={{
          style: {
            display: "none",
          },
        }}
      >
        <div>
          <div>
            <div className="lanuage-modal-title">
              <Row>
                <Col span={1}>
                  <GlobalOutlined />
                </Col>
                <Col span={12}>Change Language</Col>
              </Row>
            </div>
            <div className="cycler_alert_info_content"></div>
          </div>
        </div>
      </Modal>
    </div>
  );
}

export default LanguageModal;
