import React, { useState, useEffect } from "react";
import "./SensorImageUploader.css";
import { message } from "antd";
import { CameraOutlined } from "@ant-design/icons";
import Urls, {
  postRequest,
} from "../../../../services/common/urls";
import { useSelector } from "react-redux";
import lang from "../../../../services/lang";

function SensorImageUploader(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { initImage, onUpdateImage } = props;
  const [imageUrl, setImageUrl] = useState("");
  useEffect(() => {
    if (initImage != undefined && initImage != "") setImageUrl(initImage);
  }, [initImage]);

  const onDropImage = (e) => {
    const files = Array.from(e.target.files);
    var formData = new FormData();
    formData.append("file", files[0]);
    postRequest(Urls.UPLOAD_SENSOR_IMAGE, formData, (response) => {
      if (response.status == true) {
        onUpdateImage(
          response.url
        );
      } else {
        message.error(lang(langIndex, "msg_something_wrong"));
      }
    });
  };

  return (
    <div className="sensor-image-dlg-container">
      <div className="sensor-image-dlg-image-container">
        <img
          src={imageUrl}
          className={"sensor-image-dlg-image-style"}
          style={{ display: imageUrl == "" ? "none" : "block" }}
        />
      </div>
      <div className="sensor-image-dlg-add-button-container">
        <label htmlFor="sensor_image_uploader">
          <CameraOutlined
            className="sensor-image-dlg-add-button-style"
            size={50}
          />
          <input
            type="file"
            id="sensor_image_uploader"
            name="sensor_image_uploader"
            style={{ display: "none" }}
            accept="image/x-png,image/gif,image/jpeg"
            onChange={onDropImage}
          />
        </label>
      </div>
    </div>
  );
}

export default SensorImageUploader;
