import React from 'react'
import './OneEmail.css'
import { CloseOutlined } from "@ant-design/icons";



function OneEmail(props) {
    const { email, deleteEmail } = props;

    return (
      <div className="one-email-container-style">
        <div className="one-email-style">{email}</div>
        <div
          className="one-email-delete-style"
          onClick={() => deleteEmail(email)}
        >
          <CloseOutlined />
        </div>
      </div>
    );
}

export default OneEmail
