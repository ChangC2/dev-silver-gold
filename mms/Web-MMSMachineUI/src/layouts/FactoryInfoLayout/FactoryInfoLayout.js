import { Col, Row, message } from "antd";
import { useState } from "react";
import { useSelector } from "react-redux";
import {
  isValid,
  userData
} from "services/global";
import editIcon from "../../assets/icons/ic_edit.png";
import AccountIDModal from "../AccountIDModal/AccountIDModal";
import "./FactoryInfoLayout.css";

const FactoryInfoLayout = (props) => {
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const {customer_id, customer_details} = appDataStore;
  const [showAccountID, setShowAccountID] = useState(false);

  const onClickEditCustomerID = () =>{
    if (!isValid(userData.id)){
      message.error("Please login with User Id");
      return;
    }
    setShowAccountID(true);
  }

  return (
    <Row align="middle" className="factory-info-layout">
      <AccountIDModal
        showModal={showAccountID}
        setShowModal={setShowAccountID}
      />
      {isValid(customer_id) ? (
        <Col span={16} className="factory-info">
          <img
            className="factory-info-photo"
            src={customer_details["logo"]}
            alt="user_picture"
          />
          {customer_id}
        </Col>
      ) : (
        <Col span={16} className="factory-info">
          {"Factory ID: UnAttended"}
        </Col>
      )}
      <Col span={8} style={{ textAlign: "right" }}>
        <img
          className="factory-info-edit"
          src={editIcon}
          onClick={() => onClickEditCustomerID()}
          alt="edit"
        />
      </Col>
    </Row>
  );
};

export default FactoryInfoLayout;
