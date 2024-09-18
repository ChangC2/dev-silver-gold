import React, { useState, useEffect } from 'react';
import { Modal, DatePicker, Spin, message } from 'antd';
import './ExportCSVDialog.css'
import { getHennigCSVData } from "../../../../services/common/hennig_apis";
import { ExportToCsv } from 'export-to-csv';
import { useSelector } from 'react-redux';
import lang from '../../../../services/lang';

function ExportCSVDialog(props) {
    const {langIndex} = useSelector(x=>x.app);
    const { isShowingExportModal, setShowingExportModal, sensor_id } = props;
    const [isReading, setIsReading] = useState(false);
    const [fromDate, setFromDate] = useState();
    const [toDate, setToDate] = useState();
    const onClickExport = () => {
        if (isReading) {
            message.warn("Your request is on processing...");
            return;
        }
        if (fromDate === undefined || toDate === undefined || fromDate.diff(toDate) > 0) {
            message.warn("Please specify the valid date");
            return;
        }


        setIsReading(true);


        getHennigCSVData(
          sensor_id,
          fromDate.format("YYYY-MM-DD 00:00:00"),
          toDate.format("YYYY-MM-DD 23:59:59"),
          (res) => {
            setIsReading(false);
            if (res == null || res["sensor_values"] == null) {
              message.error(lang(langIndex, "msg_something_wrong"));
              return;
            }
            const options = {
              filename: `HennigData(${fromDate.format(
                "MM/DD/YYYY"
              )}-${toDate.format("MM/DD/YYYY")})`,
              fieldSeparator: ",",
              quoteStrings: '"',
              decimalSeparator: ".",
              showLabels: true,
              showTitle: false,
              title: "csv data",
              useTextFile: false,
              useBom: true,
              useKeysAsHeaders: true,
              // headers: ['Column 1', 'Column 2', etc...] <-- Won't work with useKeysAsHeaders present!
            };
            const csvExporter = new ExportToCsv(options);
            csvExporter.generateCsv(res["sensor_values"]);

            message.success("Success");
            setShowingExportModal(false);
          }
        );
    }
    return (
      <div>
        <Modal
          title={"Export CSV Modal"}
          visible={isShowingExportModal}
          onOk={onClickExport}
          onCancel={() => setShowingExportModal(false)}
          destroyOnClose={true}
          className="export-hennig-csv-modal"
        >
          <div>
            <div className="hennig-detail-one-hennig-name">
              <div className="hennig-detail-one-hennig-info-title">
                From Date:
              </div>
              <div className="hennig-detail-one-hennig-info-value">
                <DatePicker
                  className="detail-page-one-date"
                  format="MM/DD/YYYY"
                  onChange={(value) => setFromDate(value)}
                />
              </div>
            </div>
            <div className="hennig-detail-one-hennig-name">
              <div className="hennig-detail-one-hennig-info-title">
                To Date:
              </div>
              <div className="hennig-detail-one-hennig-info-value">
                <DatePicker
                  className="detail-page-one-date"
                  format="MM/DD/YYYY"
                  onChange={(value) => setToDate(value)}
                />
              </div>
            </div>
            {isReading && (
              <Spin spinning={isReading} size="large" style={{ marginTop: 10 }}>
                <div
                  style={{
                    width: "100%",
                    textAlign: "center",
                    fontSize: 20,
                    color: "#eeeeee",
                    background: "transparent",
                  }}
                >
                  Reading Hennig data...
                </div>
              </Spin>
            )}
          </div>
        </Modal>
      </div>
    );
}

export default ExportCSVDialog;