import { Button, Col, message, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import React, { useEffect, useState } from "react";
import "./GoodBadModal.css";
import { useSelector } from "react-redux";
import TextInput from "components/TextInput/TextInput";

function GoodBadModal(props) {
  const authData = useSelector((x) => x.authService);
  const {
    showModal,
    setShowModal,
    goodParts,
    setGoodParts,
    badParts,
    setBadParts,
  } = props;
  const [gParts, setGParts] = useState("0");
  const [bParts, setBParts] = useState("0");

  useEffect(() => {
    setGParts(goodParts);
    setBParts(badParts);
  }, [goodParts, badParts]);

  const onCancel = () => {
    setShowModal(false);
  };

   const onOK = () => {
     setShowModal(false);
     setGoodParts(gParts);
     setBadParts(bParts);
   };

  return (
    <div>
      <Modal
        centered
        open={showModal}
        className="good-bad-dialog-style"
        title={null}
        onCancel={() => onCancel()}
        closable={false}
        maskClosable={false}
        destroyOnClose={true}
        header={null}
        footer={null}
      >
        <div>
          <Row align="middle" className="good-bad-dialog-top">
            <Col>
              <span className="good-bad-dialog-title">{"Good/Bad Parts"}</span>
            </Col>
          </Row>
          <Row className="good-bad-dialog-content">
            <Col span={12}>
              <TextInput
                value={gParts}
                setValue={setGParts}
                title={"Good Parts"}
              />
            </Col>

            <Col span={12}>
              <TextInput
                value={bParts}
                setValue={setBParts}
                title={"Bad Parts"}
              />
            </Col>

            <Col span={12}>
              <Button
                className="good-bad-dialog-button"
                style={{ marginRight: "5px" }}
                onClick={onCancel}
                type="primary"
              >
                {"Cancel"}
              </Button>
            </Col>
            <Col span={12}>
              <Button
                className="good-bad-dialog-button"
                style={{ marginLeft: "5px" }}
                onClick={onOK}
                type="primary"
              >
                {"Ok"}
              </Button>
            </Col>
          </Row>
        </div>
      </Modal>
    </div>
  );
}

export default GoodBadModal;
