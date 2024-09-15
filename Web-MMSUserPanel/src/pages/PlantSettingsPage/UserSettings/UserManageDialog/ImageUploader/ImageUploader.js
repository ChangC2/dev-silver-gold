import { CameraOutlined } from "@ant-design/icons";
import { message } from "antd";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import Urls, {
  BASE_URL,
  postRequest,
} from "../../../../../services/common/urls";
import lang from "../../../../../services/lang";
import "./ImageUploader.css";
//

function ImageUploader(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { user_picture, updateImage, isLogo } = props;
  const [imageUrl, setImageUrl] = useState("");
  useEffect(() => {
    setImageUrl(user_picture);
  }, [user_picture]);
  const onDropImage = (e) => {
    const files = Array.from(e.target.files);
    var formData = new FormData();
    formData.append("file", files[0]);
    postRequest(Urls.UPLOAD_USER_IMAGE, formData, (response) => {
      if (response.status === true) {
        setImageUrl(response.url);
        updateImage(response.url);
      } else {
        message.error(lang(langIndex, "msg_something_wrong"));
      }
    });
  };

  return (
    <form className="user-manage-avatar-container">
      <div className="user-image-container">
        <img
          src={imageUrl}
          className={
            isLogo === true
              ? "logo-image-style"
              : "user-manage-dialog-avatar-style"
          }
        />
      </div>
      <div className="user-manage-avatar-add-button-container">
        <label htmlFor="upload_image">
          <CameraOutlined
            className="user-manage-avatar-add-button-style"
            size={50}
          />
          <input
            type="file"
            id="upload_image"
            name="upload_image"
            style={{ display: "none" }}
            accept="image/x-png,image/gif,image/jpeg"
            onChange={onDropImage}
          />
        </label>
      </div>
    </form>
  );
}

export default ImageUploader;
