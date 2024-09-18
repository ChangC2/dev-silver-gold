import React, { useEffect, useRef } from "react";
import logo from "../../../assets/images/logo.png";
import MachineItem from "./MachineItem_Grid/MachineItem";
import MachineItemTile from "./MachineItem_Tile/MachineItemTile";
import { Row } from "antd";
import {
  sizePad,
  sizeMobile,
  VIEWMODE,
} from "../../../services/common/constants";
function MachineItemContainer(props) {
  const {
    machineList,
    viewMode,
    customerInfo,
    customer_id,
    group_id,
    onClickMachine,
    operatorList,
  } = props;
  const { screenSize } = props;

  let validMachineList = [];
  if (customerInfo != undefined) {
    const group = customerInfo.groupInfo.find((x) => x.id == group_id);
    if (group != undefined) {
      const avail_machine_ids = group.machine_list.split(",");
      validMachineList = machineList.filter((x) =>
        avail_machine_ids.includes(x.machine_id)
      );
    } else {
      validMachineList = machineList;
    }
  }

  const machineUIList = validMachineList.map((item) => {
    if (viewMode == VIEWMODE.listView) {
      return (
        <MachineItem
          key={machineList.indexOf(item)}
          machineInfo={item}
          customer_id={customer_id}
          customerInfo={customerInfo}
          onClickMachine={onClickMachine}
          operatorList={operatorList}
          screenSize={screenSize}
        />
      );
    } else {
      return (
        <MachineItemTile
          key={machineList.indexOf(item)}
          machineInfo={item}
          customer_id={customer_id}
          customerInfo={customerInfo}
          onClickMachine={onClickMachine}
          operatorList={operatorList}
          screenSize={screenSize}
        />
      );
    }
  });

  return (
    <div className="content-page">
      <div
        style={{
          minHeight:
            screenSize.height -
            100 -
            (screenSize.width >= sizePad
              ? 80
              : screenSize.width >= sizeMobile
              ? 60
              : 50),
        }}
      >
        <Row justify={"start"} align={"top"} gutter={16}>
          {machineUIList}
        </Row>
      </div>
    </div>
  );
}

export default MachineItemContainer;
