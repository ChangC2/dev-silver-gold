import React, { useState, useEffect } from "react";
import { message } from "antd";
import { CameraOutlined } from "@ant-design/icons";
import Urls, {
  postRequest,
  MACHINE_IMAGE_BASE_URL,
} from "../../../../../services/common/urls";
import "./maintenanceImageUploader.css";
import { useSelector } from "react-redux";
import lang from "../../../../../services/lang";

function MaintenanceImageUploader(props) {
  const { langIndex } = useSelector((state) => state.app);
  const { machine_picture, updateImage } = props;
  const [imageUrl, setImageUrl] = useState("");
  useEffect(() => {
    setImageUrl(machine_picture);
  }, [machine_picture]);

  const onDropImage = (e) => {
    const files = Array.from(e.target.files);
    var formData = new FormData();

    formData.append("file", files[0]);
    postRequest(Urls.UPLOAD_MAINTENANCE_IMAGE, formData, (response) => {
      if (response.status === true) {
        setImageUrl(response.url);
        updateImage(response.url);
      } else {
        message.error(lang(langIndex, "msg_something_wrong"));
      }
    });
  };

  return (
    <div className="machine-manage-avatar-container">
      <div className="machine-image-container">
        <img 
        alt=""
        src={
           imageUrl.includes("http")
           ?  imageUrl
           : MACHINE_IMAGE_BASE_URL + imageUrl
          } 
          className={"machine-manage-dialog-avatar-style"} />
      </div>
      <div className="machine-manage-avatar-add-button-container">
        <label htmlFor="upload_machine_image">
          <CameraOutlined
            className="machine-manage-avatar-add-button-style"
            size={50}
          />
          <input
            type="file"
            id="upload_machine_image"
            name="upload_machine_image"
            style={{ display: "none" }}
            accept="image/x-png,image/gif,image/jpeg"
            onChange={onDropImage}
          />
        </label>
      </div>
    </div>
  );
}

export default MaintenanceImageUploader;
