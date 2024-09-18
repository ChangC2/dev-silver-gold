import React, { useState, useEffect } from "react";
import Urls, { BASE_URL, postRequest } from "../../../services/common/urls";
import moment from "moment";
import "./jobAddModal.css";
import {
  Form,
  DatePicker,
  Modal,
  Input,
  Button,
  Row,
  Col,
  message,
  Spin,
} from "antd";
import { useSelector } from "react-redux";
import lang from "../../../services/lang";

const dateFormat = "YYYY-MM-DD HH:mm:ss";

function JobAddModal(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { addEntry, isShowModal, setIsShowModal, maxJobId } = props;

  const [inputData, setInputData] = useState({
    jobID:maxJobId.toString(),
    dueDate: moment(new Date()).format(dateFormat),
    orderDate: moment(new Date()).format(dateFormat),
  });
  const [files, setFiles] = useState([]);
  const [isBusy, setBusy] = useState(false);

  const [form] = Form.useForm();

  const fileSelectedHandler = (e) => {
    setFiles(Array.from(e.target.files));
  };

  const onClickAdd = () => {
    if (inputData.jobID === undefined || inputData.jobID === "") {
      message.error(lang(langIndex, "missing_param"));
      return;
    }

    if (inputData.customer === undefined || inputData.customer === "") {
      message.error(lang(langIndex, "missing_param"));
      return;
    }

    if (inputData.dueDate === undefined || inputData.dueDate === "") {
      inputData.dueDate = moment(new Date()).format(dateFormat);
    }
    if (inputData.orderDate === undefined || inputData.orderDate === "") {
      inputData.orderDate = moment(new Date()).format(dateFormat);
    }

    var formData = new FormData();
    for (var i = 0; i < files.length; i++) {
      formData.append("files[]", files[i]);
    }
    setBusy(true);
    postRequest(Urls.UPLOAD_JOB_FILES, formData, (response) => {
      setBusy(false);
      setIsShowModal(false);
      if (response.status === true) {
        addEntry({ ...inputData, urls: response.urls });
      } else {
        addEntry({ ...inputData });
      }
    });
  };

  const onClickCancel = () => {
    setIsShowModal(false);
  };

  return (
    <div>
      <Modal
        title={lang(langIndex, "jobentry_editdialog")}
        visible={isShowModal}
        onOk={onClickAdd}
        onCancel={onClickCancel}
        destroyOnClose={true}
        className="entry-add-modal"
      >
        <Spin spinning={isBusy}>
          <Form form={form} component={false}>
            <Row>
              <Col span={8}>
                <span>{lang(langIndex, "jobentry_jobid")}</span>
              </Col>
              <Col span={16}>
                <Input
                  style={{ background: "#1e1e1e", color: "#eeeeee" }}
                  placeholder={lang(langIndex, "jobentry_jobid")}
                  value={
                    inputData.jobID === undefined
                      ? maxJobId.toString()
                      : inputData.jobID
                  }
                  onChange={(e) =>
                    setInputData({ ...inputData, jobID: e.target.value })
                  }
                />
              </Col>
            </Row>
            <Row>
              <Col span={8}>
                <span>{lang(langIndex, "jobentry_customerid")}</span>
              </Col>
              <Col span={16}>
                <Input
                  style={{ background: "#1e1e1e", color: "#eeeeee" }}
                  placeholder={lang(langIndex, "jobentry_customerid")}
                  value={
                    inputData.customer === undefined ? "" : inputData.customer
                  }
                  onChange={(e) =>
                    setInputData({ ...inputData, customer: e.target.value })
                  }
                />
              </Col>
            </Row>
            <Row>
              <Col span={8}>
                <span>{lang(langIndex, "jobentry_partnumber")}</span>
              </Col>
              <Col span={16}>
                <Input
                  style={{ background: "#1e1e1e", color: "#eeeeee" }}
                  placeholder={lang(langIndex, "jobentry_partnumber")}
                  value={
                    inputData.partNumber === undefined
                      ? ""
                      : inputData.partNumber
                  }
                  onChange={(e) =>
                    setInputData({ ...inputData, partNumber: e.target.value })
                  }
                />
              </Col>
            </Row>
            <Row>
              <Col span={8}>
                <span>{lang(langIndex, "jobentry_programnumber")}</span>
              </Col>
              <Col span={16}>
                <Input
                  style={{ background: "#1e1e1e", color: "#eeeeee" }}
                  placeholder={lang(langIndex, "jobentry_programnumber")}
                  value={
                    inputData.programNumber === undefined
                      ? ""
                      : inputData.programNumber
                  }
                  onChange={(e) =>
                    setInputData({
                      ...inputData,
                      programNumber: e.target.value,
                    })
                  }
                />
              </Col>
            </Row>
            <Row>
              <Col span={8}>
                <span>{lang(langIndex, "jobentry_description")}</span>
              </Col>
              <Col span={16}>
                <Input
                  style={{ background: "#1e1e1e", color: "#eeeeee" }}
                  placeholder={lang(langIndex, "jobentry_description")}
                  value={
                    inputData.description === undefined
                      ? ""
                      : inputData.description
                  }
                  onChange={(e) =>
                    setInputData({ ...inputData, description: e.target.value })
                  }
                />
              </Col>
            </Row>
            <Row>
              <Col span={8}>
                <span>{lang(langIndex, "jobentry_partspercycle")}</span>
              </Col>
              <Col span={16}>
                <Input
                  style={{ background: "#1e1e1e", color: "#eeeeee" }}
                  placeholder={lang(langIndex, "jobentry_partspercycle")}
                  type={"number"}
                  value={
                    inputData.partsPerCycle === undefined
                      ? ""
                      : inputData.partsPerCycle
                  }
                  onChange={(e) =>
                    setInputData({
                      ...inputData,
                      partsPerCycle: e.target.value,
                    })
                  }
                />
              </Col>
            </Row>
            <Row>
              <Col span={8}>
                <span>{lang(langIndex, "jobentry_targetcycletime")}</span>
              </Col>
              <Col span={16}>
                <Input
                  style={{ background: "#1e1e1e", color: "#eeeeee" }}
                  placeholder={lang(langIndex, "jobentry_targetcycletime")}
                  type={"number"}
                  value={
                    inputData.targetCycleTime === undefined
                      ? ""
                      : inputData.targetCycleTime
                  }
                  onChange={(e) =>
                    setInputData({
                      ...inputData,
                      targetCycleTime: e.target.value,
                    })
                  }
                />
              </Col>
            </Row>
            <Row>
              <Col span={8}>
                <span>{lang(langIndex, "jobentry_qtyrequired")}</span>
              </Col>
              <Col span={16}>
                <Input
                  style={{ background: "#1e1e1e", color: "#eeeeee" }}
                  placeholder={lang(langIndex, "jobentry_qtyrequired")}
                  type={"number"}
                  value={
                    inputData.qtyRequired === undefined
                      ? ""
                      : inputData.qtyRequired
                  }
                  onChange={(e) =>
                    setInputData({ ...inputData, qtyRequired: e.target.value })
                  }
                />
              </Col>
            </Row>
            <Row>
              <Col span={8}>
                <span>{lang(langIndex, "jobentry_qtycompleted")}</span>
              </Col>
              <Col span={16}>
                <Input
                  style={{ background: "#1e1e1e", color: "#eeeeee" }}
                  placeholder={lang(langIndex, "jobentry_qtycompleted")}
                  type={"number"}
                  value={
                    inputData.qtyCompleted === undefined
                      ? ""
                      : inputData.qtyCompleted
                  }
                  onChange={(e) =>
                    setInputData({ ...inputData, qtyCompleted: e.target.value })
                  }
                />
              </Col>
            </Row>
            <Row>
              <Col span={8}>
                <span>{lang(langIndex, "jobentry_orderdate")}</span>
              </Col>
              <Col span={16}>
                {/* <Input style={{ background: "#1e1e1e", color: "#eeeeee" }} placeholder="orderDate : yyyy-MM-dd HH:mm:ss" value={inputData.orderDate === undefined ? "" : inputData.orderDate} onChange={(e) => setInputData({ ...inputData, orderDate: e.target.value })} /> */}
                <DatePicker
                  className="date-time-picker-style"
                  format={dateFormat}
                  value={
                    inputData.orderDate === undefined ||
                    inputData.orderDate === ""
                      ? ""
                      : moment(inputData.orderDate, dateFormat)
                  }
                  onChange={(date, dateStringe) =>
                    setInputData({ ...inputData, orderDate: dateStringe })
                  }
                  showTime={true}
                />
              </Col>
            </Row>
            <Row>
              <Col span={8}>
                <span>{lang(langIndex, "jobentry_duedate")}</span>
              </Col>
              <Col span={16}>
                <DatePicker
                  className="date-time-picker-style"
                  format={dateFormat}
                  // defaultValue={moment(moment.now(), dateFormat)}
                  value={
                    inputData.dueDate === undefined || inputData.dueDate === ""
                      ? ""
                      : moment(inputData.dueDate, dateFormat)
                  }
                  onChange={(date, dateStringe) =>
                    setInputData({ ...inputData, dueDate: dateStringe })
                  }
                  showTime={true}
                />
              </Col>
            </Row>

            <Row>
              <Col span={8}>
                <span>{lang(langIndex, "jobentry_aux1data")}</span>
              </Col>
              <Col span={16}>
                <Input
                  style={{ background: "#1e1e1e", color: "#eeeeee" }}
                  placeholder={lang(langIndex, "jobentry_aux1data")}
                  value={
                    inputData.aux1data === undefined ? "" : inputData.aux1data
                  }
                  onChange={(e) =>
                    setInputData({ ...inputData, aux1data: e.target.value })
                  }
                />
              </Col>
            </Row>

            <Row>
              <Col span={8}>
                <span>{lang(langIndex, "jobentry_aux2data")}</span>
              </Col>
              <Col span={16}>
                <Input
                  style={{ background: "#1e1e1e", color: "#eeeeee" }}
                  placeholder={lang(langIndex, "jobentry_aux2data")}
                  value={
                    inputData.aux2data === undefined ? "" : inputData.aux2data
                  }
                  onChange={(e) =>
                    setInputData({ ...inputData, aux2data: e.target.value })
                  }
                />
              </Col>
            </Row>

            <Row>
              <Col span={8}>
                <span>{lang(langIndex, "jobentry_aux3data")}</span>
              </Col>
              <Col span={16}>
                <Input
                  style={{ background: "#1e1e1e", color: "#eeeeee" }}
                  placeholder={lang(langIndex, "jobentry_aux3data")}
                  value={
                    inputData.aux3data === undefined ? "" : inputData.aux3data
                  }
                  onChange={(e) =>
                    setInputData({ ...inputData, aux3data: e.target.value })
                  }
                />
              </Col>
            </Row>

            <Row>
              <Col span={8}>
                <span>{lang(langIndex, "files")}</span>
              </Col>
              <Col span={16}>
                <form>
                  <input
                    type="file"
                    accept=".png, .jpg, .jpeg, .pdf"
                    multiple
                    onChange={fileSelectedHandler}
                  />
                </form>
              </Col>
            </Row>
          </Form>
        </Spin>
      </Modal>
    </div>
  );
}

export default JobAddModal;
