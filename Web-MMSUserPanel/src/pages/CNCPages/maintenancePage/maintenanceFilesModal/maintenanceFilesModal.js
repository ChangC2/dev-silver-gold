import { MinusSquareFilled } from "@ant-design/icons";
import { Col, Modal, Popconfirm, Row, Spin } from "antd";
import { useState } from "react";
import { useSelector } from "react-redux";
import {
  ExecuteQuery
} from "../../../../services/common/cnc_apis";
import Urls, { BASE_URL, postRequest } from "../../../../services/common/urls";
import lang from "../../../../services/lang";
import "./maintenanceFilesModal.css";

const dateFormat = "YYYY-MM-DD HH:mm:ss";

const GuideWidget = (props) => {
  const { langIndex } = useSelector((x) => x.app);
  const { info, onClick, index, onDelete, files, security_level } = props;
  return <Row align={'middle'} justify={'space-between'}>
    <Col>
      <span className="custom-button" onClick={() => onClick(info)}>
        {info}
      </span>
    </Col>
    {security_level !== 0 && (
      <Popconfirm
        title={lang(langIndex, 'jobentry_suretodelete')}
        onConfirm={() => onDelete(files, index)}
      >
        <Col>
          <span className="custom-button">
            {(info !== "") && <MinusSquareFilled />}
          </span>
        </Col>
      </Popconfirm>
    )}
  </Row>;

}

function MaintenanceFilesModal(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { selCustomerId, Id, files, setFiles, updateEntry, isShowFiles, setIsShowFiles, security_level } = props;
  const [isBusy, setBusy] = useState(false);

  const updateMaintenanceEntry = (newFiles) => {
    let query =
      "UPDATE " +
      "`" +
      selCustomerId +
      "_maintenance` " +
      "SET `files`='" +
      newFiles +
      "' " +
      "WHERE `id`=" +
      Id;
    setBusy(true)
    ExecuteQuery(query, (res) => {
      setFiles(newFiles);
      updateEntry({"id": Id, "files": newFiles });
      setBusy(false)
    });
  };

  const onClickGuide = (url) => {
    window.open(BASE_URL + "files/" + url)
  };

  const onClickDelete = (files, index) => {
    var fileList = files.split(':');
    var newFiles = "";
    for (var i = 0; i < fileList.length; i++) {
      if (i !== index) {
        if (newFiles == "") {
          newFiles = fileList[i];
        } else {
          newFiles = newFiles + ":" + fileList[i];
        }
      }
    }
    updateMaintenanceEntry(newFiles);
  };

  const onClickCancel = () => {
    setIsShowFiles(false);
  };

  const GuideListUI = files !== null ?  files.split(':').map((one_guide, index) => {
    return <GuideWidget
      key={`guide-key-${index}`}
      info={one_guide}
      index={index}
      files={files}
      security_level={security_level}
      onDelete={onClickDelete}
      onClick={onClickGuide}
    />
  }) : '';

  const fileSelectedHandler = (e) => {
    var newFiles = Array.from(e.target.files);
    var formData = new FormData();
    for (var i = 0; i < newFiles.length; i++) {
      formData.append("files[]", newFiles[i]);
    }
    setBusy(true)
    postRequest(
      Urls.UPLOAD_JOB_FILES,
      formData,
      (response) => {
        setBusy(false);
        var newFilesStr = files;
        if (newFilesStr == "") {
          newFilesStr = response.urls;
        } else {
          newFilesStr = newFilesStr + ":" + response.urls;
        }
        updateMaintenanceEntry(newFilesStr);
      }
    );
  };


  return (
    <div>
      <Modal
        title={lang(langIndex, "maintenance_entry_files")}
        visible={isShowFiles}
        onCancel={onClickCancel}
        okButtonProps={{
          style: {
            display: "none",
          },
        }}
        destroyOnClose={true}
        className="entry-add-modal"
      >
        <Spin spinning={isBusy}>
          {GuideListUI}
          {security_level !== 0 && (
            <Row style={{ marginTop: 30 }}>
              <Col span={8}>
                <span>{lang(langIndex, "new_files")}</span>
              </Col>
              <Col span={16}>
                <form>
                  <input type="file" accept=".png, .jpg, .jpeg, .pdf" multiple onChange={fileSelectedHandler} />
                </form>
              </Col>
            </Row>
          )}

        </Spin>
      </Modal>
    </div>
  );
}

export default MaintenanceFilesModal;
