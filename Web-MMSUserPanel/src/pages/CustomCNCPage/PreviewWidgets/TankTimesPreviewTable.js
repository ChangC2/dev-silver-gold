import { Select, Table } from "antd";
import "./TankTimesPreviewTable.css";
const { Option } = Select;

const daysoftimesList = [1, 2, 3, 4, 5, 6, 7];

const dateFormat = "YYYY-MM-DD HH:mm:ss";


function TankTimesPreviewTable(props) {
  const { columns, dataSource, daysoftimes } = props;

 


  return (
    <div className="tanktimes-table-preview-back">
      <div className="tanktimes-table-preview-title">
        Processing Times ({daysoftimes} Days)
      </div>
      <Table
        className="tanktimes-table-preview-style"
        dataSource={dataSource}
        columns={columns}
      />
    </div>
  );
}

export default TankTimesPreviewTable;
