import * as actionType from "../actionType";

const initialState = {
  isDetailPage: false,
  customerInfoList: {},
  machineInfoList: {},
  machineDetailList: {},
  utilizationMachineList: {},
};

const reducer = (state = initialState, action) => {
  let _customerInfoList = { ...state.customerInfoList };
  switch (action.type) {
    case actionType.cncChangePage:
      return {
        ...state,
        isDetailPage: action.data,
      };
    case actionType.cncSetAllCustomerInfo:
      return {
        ...state,
        customerInfoList: action.data,
      };
    case actionType.cncUpdateCustomerInfo:
      _customerInfoList[action.data.customer_id] = {
        ...action.data.customer_info,
      };

      return {
        ...state,
        customerInfoList: { ..._customerInfoList },
      };
    case actionType.cncUpdateGroupMachineInfo:
      _customerInfoList[action.data.customer_id] = {
        ..._customerInfoList[action.data.customer_id],
        groupInfo: [...action.data.groupInfo],
      };
      return {
        ...state,
        customerInfoList: { ..._customerInfoList },
      };
    case actionType.cncUpdateMachineListInfo:
      let _machineInfoList = { ...state.machineInfoList };
      _machineInfoList[action.data.customer_id] = [...action.data.machine_info];
      return {
        ...state,
        machineInfoList: { ..._machineInfoList },
      };
    case actionType.cncUpdateMachineDetailInfo:
      let _machineDetailList = { ...state.machineDetailList };
      _machineDetailList[action.data.customer_id] = [...action.data.machines];
      return {
        ...state,
        machineDetailList: { ..._machineDetailList },
      };
    case actionType.utilizationData:
      let _utilizationMachineList = { ...state.utilizationMachineList };
      _utilizationMachineList[action.data.customer_id] = [...action.data.machines];
      return {
        ...state,
        utilizationMachineList: { ..._utilizationMachineList },
      };
    default:
      return state;
  }
};
export default reducer;
