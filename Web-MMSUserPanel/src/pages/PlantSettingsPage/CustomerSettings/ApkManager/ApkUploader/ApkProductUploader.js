import { DownloadOutlined } from "@ant-design/icons";
import { useEffect, useState } from "react";
import { MOBILE_URL } from "../../../../../services/common/urls";
import "./ApkUploader.css";
function ApkProductUploader(props) {
  const [apkUrl, setApkUrl] = useState(MOBILE_URL + "Terminals/appMMS.apk");

  useEffect(() => {
   
  }, []);

  return (
    <div>
      <a className="apk-download-button" href={apkUrl} download>
        <DownloadOutlined className="apk-uploader-icon" size={50} />
        <span className="apk-uploader-title">Download</span>
      </a>
    </div>
  );
}

export default ApkProductUploader;
