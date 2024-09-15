const axios = require("axios").default;
export const USER_SITE = "";
// SLYMMS
export const BASE_URL = "https://slymms.com/backend/";
export const MOBILE_URL = "https://api.slymms.com/";
export const PDF_GEN_URL = "https://slymms.com/pdf_generator/";

// New Server
// export const BASE_URL = "https://" + window.location.hostname + "/site_api/";
// export const MOBILE_URL = "https://" + window.location.hostname + "/mobile_api";
// export const PDF_GEN_URL = "https://" + window.location.hostname + "/pdf_generator/";

// Local
// export const BASE_URL = "http://" + window.location.hostname + "/mms_user/site_api/";
// export const MOBILE_URL =
//   "http://" + window.location.hostname + "/mms_user/mobile_api";
// export const PDF_GEN_URL =
//   "http://" + window.location.hostname + "/mms_user/pdf_generator/";

export const MACHINE_IMAGE_BASE_URL = BASE_URL + "images/machine/";
export const MAINTENANCE_IMAGE_BASE_URL = BASE_URL + "images/maintenance/";

const Todo = {
  LOGIN: BASE_URL + "login.php",
  Get_Labor_Settings: BASE_URL + "get_labor_settings.php",
  Update_Labor_Setting: BASE_URL + "update_labor_setting.php",
  Add_Labor_tracking: BASE_URL + "add_labor_tracking.php",

  Get_Labor_Categories: BASE_URL + "get_labor_categories.php",
  Add_Labor_Category: BASE_URL + "add_labor_category.php",
  Update_Labor_Category: BASE_URL + "update_labor_category.php",
  Delete_Labor_Category: BASE_URL + "delete_labor_category.php",

  GET_CUSTOMER_ALL_INFO: BASE_URL + "get_all_customer_info.php",
  GET_TABLE: BASE_URL + "get_table.php",
  TEST_URL: BASE_URL + "test.php",
  GET_MACHINE_GROUP: BASE_URL + "get_machine_group_list.php",
  MANAGE_MACHINE_GROUP: BASE_URL + "manage_machine_group.php",
  GET_MACHINE_LIST: BASE_URL + "get_machine_list.php",
  GET_MACHINE_DATA: BASE_URL + "get_machine_data.php",
  GET_CURRENT_IOT_DATA: BASE_URL + "get_current_iot_data.php",
  GET_MACHINE_CALCULATION: BASE_URL + "get_machine_calculation.php",
  // GET_UTILIZATION_DATA: BASE_URL + "get_utilization_data.php",
  GET_UTILIZATION_DATA: BASE_URL + "get_machine_data.php",
  GET_UTILIZATION_DETAIL_DATA: BASE_URL + "get_utilization_detail_data.php",
  GET_MACHINE_INSTALL_CONFIG: BASE_URL + "get_machine_install_config.php",
  GET_OPERATOR_LIST: BASE_URL + "get_operator_list.php",
  EXECUTE_QUERY: BASE_URL + "execute_query.php",
  GET_PART_ID: BASE_URL + "get_part_id.php",
  GET_CUSTOM_DASHBOARD_TABLE_DATA:
    BASE_URL + "get_custom_dashboard_table_data.php",
  GET_CUSTOM_DASHBOARD_STAGE: BASE_URL + "get_custom_dashboard_stage.php",
  GET_TIME_SAVINGS: BASE_URL + "get_time_savings.php",

  ADD_ENTRY: BASE_URL + "add_entry.php",

  ADD_USER: BASE_URL + "add_user.php",
  UPDATE_USER: BASE_URL + "update_user.php",
  DELETE_USER: BASE_URL + "delete_user.php",
  ADD_MACHINE: BASE_URL + "add_machine.php",
  ADD_MAINTENANCE_ENTRY: BASE_URL + "add_maintenance_entry.php",

  GET_REPORT_PERIOD_DATA: BASE_URL + "get_period_report_data.php",
  GET_HST_FULL_DATA: BASE_URL + "get_hst_full_data_beta.php",
  GET_REPORT_PERIOD_DATA_BY_OPERATOR:
    BASE_URL + "get_period_report_data_by_operator.php",

  GET_HST_PERIOD_DATA: BASE_URL + "get_hst_period_data.php",
  GET_INCYCLE_TIMES: BASE_URL + "get_incycle_times.php",
  GET_APP_SETTING: BASE_URL + "get_app_setting.php",
  GET_APP_SETTING_FORMULA: BASE_URL + "get_app_setting_formula.php",

  UPDATE_APP_SETTING: BASE_URL + "update_app_setting.php",

  UPDATE_GANTT_STATUS: BASE_URL + "update_gantt_status.php",

  UPLOAD_JOB_FILES: BASE_URL + "upload_files.php",
  UPLOAD_JOBENTRY_CSV: BASE_URL + "upload_job_entry_csv.php",

  UPLOAD_FIRMWARE: BASE_URL + "upload_firmware.php",
  UPDATE_FIRMWARE: BASE_URL + "update_firmware.php",
  GET_FIRMWARES: BASE_URL + "get_firmwares.php",

  CREATE_NEW_FACTORY: BASE_URL + "create_new_factory.php",

  SENSOR_GET_LIST: BASE_URL + "get_sensor_list.php",
  SENSOR_GET_INFO_LIST: BASE_URL + "get_sensor_info_list.php",
  SENSOR_GET_SENSOR_DATA: BASE_URL + "get_sensor_data.php",
  SENSOR_GET_SENSOR_PERIOD_DATA: BASE_URL + "get_sensor_period_data.php",
  SENSOR_ADD_SENSOR: BASE_URL + "add_sensor.php",
  SENSOR_UPDATE_SENSOR: BASE_URL + "update_sensor.php",
  SENSOR_DELETE_SENSOR: BASE_URL + "delete_sensor.php",

  HENNIG_GET_LIST: BASE_URL + "get_hennig_list.php",
  HENNIG_GET_INFO_LIST: BASE_URL + "get_hennig_info_list.php",
  HENNIG_GET_HENNIG_DATA: BASE_URL + "get_hennig_data.php",
  HENNIG_GET_HENNIG_CSV_DATA: BASE_URL + "get_hennig_csv_data.php",
  HENNIG_GET_HENNIG_PERIOD_DATA: BASE_URL + "get_hennig_period_data.php",
  HENNIG_ADD_HENNIG: BASE_URL + "add_hennig.php",
  HENNIG_UPDATE_HENNIG: BASE_URL + "update_hennig.php",
  HENNIG_DELETE_HENNIG: BASE_URL + "delete_hennig.php",

  Get_Custom_Dashboards: BASE_URL + "get_custom_dashboards.php",
  Add_Custom_Dashboard: BASE_URL + "add_custom_dashboard.php",
  Update_Custom_Dashboard: BASE_URL + "update_custom_dashboard.php",
  Delete_Custom_Dashboard: BASE_URL + "delete_custom_dashboard.php",

  GET_CUSTOME_WIDGET_FILTERS: BASE_URL + "get_custom_widget_filters.php",
  ADD_CUSTOME_WIDGET: BASE_URL + "add_custom_widget.php",
  EDIT_CUSTOME_WIDGET: BASE_URL + "update_custom_widget.php",
  GET_CUSTOME_WIDGET: BASE_URL + "get_custom_widget.php",
  Delete_Custom_WIDGET: BASE_URL + "delete_custom_widget.php",
  GET_SHIFT_LIST: BASE_URL + "get_shift_list.php",

  GET_ALARMS_FILTERS: BASE_URL + "get_alarms_filters.php",
  GET_ALARMS: BASE_URL + "get_alarms.php",

  UPLOAD_USER_IMAGE: BASE_URL + "upload_user_image.php",
  UPLOAD_SENSOR_IMAGE: BASE_URL + "upload_sensor_image.php",
  UPLOAD_FACTORY_IMAGE: BASE_URL + "upload_factory_image.php",
  UPLOAD_MAINTENANCE_IMAGE: BASE_URL + "upload_maintenance_image.php",
  UPLOAD_MACHINE_IMAGE: BASE_URL + "upload_machine_image.php",

  GET_TANK_REPORT: PDF_GEN_URL + "generate_pdf.php",
  GET_CUSTOM_DASHBOARD_REPORT:
    PDF_GEN_URL + "generate_custom_dashboard_pdf.php",
  GET_USERS_REPORT: PDF_GEN_URL + "generate_users_pdf.php",

  PDF_SENDER: MOBILE_URL + "emailPage/pdf_sender.php",
  CSV_SENDER: MOBILE_URL + "emailPage/csv_sender.php",
  PDF_DOWNLOADER: MOBILE_URL + "emailPage/pdf_downloader.php",
};

export default Todo;

export function postRequest(url, param, callback) {
  axios
    .post(url, param)
    .then((res) => {
      callback(res.data);
    })
    .catch((error) => {
      callback(null);
    });
}
