import { Col, Modal, Row } from "antd";
import { confirmable } from "react-confirm";
import Barcode from "react-barcode";
import domtoimage from "dom-to-image";
import { useRef } from "react";
import "./PDFPreviewModal.css";
import { sizeMobile } from "services/common/constants";
import { useEffect } from "react";
import userBlank from "../../../assets/images/user_blank.jpg";

const PDFPreviewModal = (props) => {
  const { show, proceed, userList, screenSize, customerInfo } = props;

  const refs = useRef([]);

  const mainUI = userList.map((user, i) => {
    {
      let oper = "";
      switch (user.plant_securitylevel) {
        case "4":
          oper = "Administrator";
        case "2":
          oper = "Client";
        case "0":
          oper = "Operator";
        default:
          oper = "Operator";
      }
      return (
        <Col span={24} key={`col-${user.id}`} type="flex" align="middle">
          <div
            className="user-pdf-container"
            ref={(el) => (refs.current = [...refs.current, el])}
          >
            <img
              className="user-pdf-factory-logo"
              src={customerInfo.logo}
            />
            <div className="user-pdf-factory-card">EMPLOYEE CARD</div>
            <h4 className="user-pdf-factory-name">{customerInfo.name}</h4>
            {user.user_picture == "" || user.user_picture == null ? (
              <img className="user-pdf-user-logo" src={userBlank} />
            ) : (
              <img
                className="user-pdf-user-logo"
                src={user.user_picture}
              />
            )}
            <div className="user-pdf-user-name">{user.username}</div>
            <div className="user-pdf-user-operator">{oper}</div>
            <Barcode
              displayValue="false"
              width="2px"
              height="50px"
              value={user.id}
            />
            <div className="user-pdf-user-mmswordidcards">SLYMMS</div>
          </div>
        </Col>
      );
    }
  });

  const onClickOK = async () => {
    const userData = [];
    for (let i = 0; i < refs.current.length; i++) {
      let userImage = await domtoimage.toJpeg(refs.current[i], {
        quality: 1,
      });
      userData.push(userImage);
    }
    proceed({ userData: userData });
  };

  const onClickCancel = () => {
    proceed(false);
  };

  return (
    <Modal
      width={1200}
      height={900}
      visible={show}
      closable={false}
      onOk={onClickOK}
      onCancel={onClickCancel}
      okText="Print"
    >
      <Row align="middle" style={{ width: "100%" }}>
        <Col span={6}></Col>
        <Col
          span={12}
          style={{ textAlign: "center", fontSize: 25, fontWeight: "bold" }}
        ></Col>
        <Col span={6}></Col>
      </Row>
      <div style={{ marginTop: 20 }}>
        <Row align="middle">{mainUI}</Row>
      </div>
    </Modal>
  );
};

export default confirmable(PDFPreviewModal);
