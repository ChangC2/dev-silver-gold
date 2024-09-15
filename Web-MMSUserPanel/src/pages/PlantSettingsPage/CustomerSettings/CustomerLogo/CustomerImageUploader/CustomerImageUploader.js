import { CameraOutlined } from "@ant-design/icons";
import { message } from "antd";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import Urls, {
  postRequest,
} from "../../../../../services/common/urls";
import lang from "../../../../../services/lang";
import "./CustomerImageUploader.css";
function CustomerImageUploader(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { user_picture, updateImage } = props;
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
    <div className="customer-logo-container">
      <img src={imageUrl} className="customer-logo-style" />
      <label htmlFor="upload_customer_image">
        <CameraOutlined className="customer-logo-add-button-style" size={50} />
        <input
          type="file"
          id="upload_customer_image"
          name="upload_customer_image"
          style={{ display: "none" }}
          accept="image/x-png,image/gif,image/jpeg"
          onChange={onDropImage}
        />
      </label>
    </div>
  );
}

export default CustomerImageUploader;
