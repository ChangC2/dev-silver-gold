import { DownloadOutlined } from "@ant-design/icons";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { MOBILE_URL } from "../../../../../services/common/urls";
import "./ApkUploader.css";
function ApkBetaUploader(props) {
  const { langIndex } = useSelector((x) => x.app);
  const [apkUrl, setApkUrl] = useState(
    MOBILE_URL + "Terminals/appMMSTest.apk"
  );

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

export default ApkBetaUploader;
