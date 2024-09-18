import axios from "axios";
import { SYSTEM_ERROR } from "./CONSTANTS";

const DOMAIN = "https://api.slymms.com";
const BASE_URL = DOMAIN + "/api";

const urlUserLogin = BASE_URL + "/loginWithUserId";
const urlLoginWithCustomerId = BASE_URL + "/loginWithCustomerId";
const urlGetJobData = BASE_URL + "/getJobData";
const getTankTime = BASE_URL + "/getTankTime";
const postTankTime = BASE_URL + "/postTankTime";
const postPhosTestData = BASE_URL + "/postPhos_test_data";
const getCleaningStation = BASE_URL + "/getCleaningStation";
const postCleaningStation = BASE_URL + "/postCleaningStation";
const getBlastStation = BASE_URL + "/getBlastStation";
const postBlastStation = BASE_URL + "/postBlastStation";
const getTankTemperature = BASE_URL + "/getTankTemperature";
const postPaintStation = BASE_URL + "/postPaintStation";
const getPaintStation = BASE_URL + "/getPaintStation";
const postAssembly1Station = BASE_URL + "/postAssembly1Station";
const getAssembly1Station = BASE_URL + "/getAssembly1Station";
const postBlu136Assembly = BASE_URL + "/postBlu136Assembly";
const getBlu136Assembly = BASE_URL + "/getBlu136Assembly";
const postAssembly3Station = BASE_URL + "/postAssembly3Station";
const getAssembly3Station = BASE_URL + "/getAssembly3Station";
const postQualityStation = BASE_URL + "/postQualityStation";
const getQualityStation = BASE_URL + "/getQualityStation";

export const apiCallPost = (url, formData, errorMessage = "") => {
  const config = {
    headers: {
      "content-type": "multipart/form-data",
    },
  };
  return new Promise((resolve, reject) => {
    try {
      axios
        .post(url, formData, config)
        .then((res) => {
          const { data } = res;
          if (data.status === true) {
            resolve(data);
          } else {
            reject(data.message);
          }
        })
        .catch((err) => {
          reject(errorMessage, err);
        });
    } catch (error) {
      reject(SYSTEM_ERROR);
    }
  });
};

export const apiCallGet = (url, param, errorMessage = "") => {
  return new Promise((resolve, reject) => {
    try {
      axios
        .get(url, { params: param })
        .then((res) => {
          const { data } = res;
          if (data.status === true) {
            resolve(data.data);
          } else {
            reject(data.message);
          }
        })
        .catch((err) => {
          reject(errorMessage, err);
        });
    } catch (error) {
      console.error(errorMessage, error);
      reject(SYSTEM_ERROR);
    }
  });
};

export const apiCallForLogin = (userId, deviceId) => {
  console.log("apiCallForLogin");
  const url = urlUserLogin;
  var formData = new FormData();
  formData.append("userId", userId);
  formData.append("deviceId", deviceId);
  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForLogin")
      .then((res) => {
        resolve(res.data);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForLoginWithCustomerId = (accountId, deviceId) => {
  console.log("apiCallForLoginWithCustomerId");
  const url = urlLoginWithCustomerId;
  var formData = new FormData();
  formData.append("customerId", accountId);
  formData.append("deviceId", deviceId);
  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForLoginWithCustomerId")
      .then((res) => {
        resolve(res.data);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForGetJobData = (customerId, jobId) => {
  console.log("apiCallForGetJobData");
  const url = urlGetJobData;
  var formData = new FormData();
  formData.append("customerId", customerId);
  formData.append("jobId", jobId);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForGetJobData")
      .then((res) => {
        resolve(res.data);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForPostTankTime = (params) => {
  console.log("apiCallForPostTankTime=>", params);
  const url = postTankTime;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("machine_id", params["machine_id"]);
  formData.append("created_at", params["created_at"]);
  formData.append("timestamp", params["timestamp"]);
  formData.append("operator", params["operator"]);
  formData.append("part_id", params["part_id"]);
  formData.append("date", params["date"]);
  formData.append("time", params["time"]);
  formData.append("ttime1", params["ttime1"]);
  formData.append("ttemp1", params["ttemp1"]);
  formData.append("ttime2", params["ttime2"]);
  formData.append("ttemp2", params["ttemp2"]);
  formData.append("ttime3", params["ttime3"]);
  formData.append("ttemp3", params["ttemp3"]);
  formData.append("ttime4", params["ttime4"]);
  formData.append("ttemp4", params["ttemp4"]);
  formData.append("ttime5", params["ttime5"]);
  formData.append("ttemp5", params["ttemp5"]);
  formData.append("ttime6", params["ttime6"]);
  formData.append("ttemp6", params["ttemp6"]);
  formData.append("ttime7", params["ttime7"]);
  formData.append("ttemp7", params["ttemp7"]);
  formData.append("ttime8", params["ttime8"]);
  formData.append("ttemp8", params["ttemp8"]);
  formData.append("toven", params["toven"]);
  formData.append("rm_lot", params["rm_lot"]);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForPostTankTime")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForGetTankTime = (params) => {
  console.log("apiCallForGetTankTime");
  const url = getTankTime;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("part_id", params["part_id"]);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForGetTankTime")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForRecordPhosphateTestInfo = (params) => {
  console.log("apiCallForRecordPhosphateTestInfo");
  const url = postPhosTestData;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("machine_id", params["machine_id"]);
  formData.append("operator", params["operator"]);
  formData.append("part_id", params["part_id"]);
  formData.append("created_at", params["created_at"]);
  formData.append("timestamp", params["timestamp"]);
  formData.append("date", params["date"]);
  formData.append("time", params["time"]);
  formData.append("weight", params["weight"]);
  formData.append("water_break", params["water_break"]);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "postPhos_test_data")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForPostCleaningStation = (params) => {
  console.log("apiCallForPostCleaningStation");
  const url = postCleaningStation;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("machine_id", params["machine_id"]);
  formData.append("created_at", params["created_at"]);
  formData.append("timestamp", params["timestamp"]);
  formData.append("operator", params["operator"]);
  formData.append("part_id", params["part_id"]);
  formData.append("date", params["date"]);
  formData.append("time", params["time"]);
  formData.append("processing_time", params["processing_time"]);
  formData.append("notes", params["notes"]);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForPostCleaningStation")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForGetCleaningStation = (params) => {
  console.log("apiCallForGetCleaningStation");
  const url = getCleaningStation;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("part_id", params["part_id"]);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForGetCleaningStation")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForPostBlastStation = (params) => {
  console.log("apiCallForPostBlastStation");
  const url = postBlastStation;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("machine_id", params["machine_id"]);
  formData.append("created_at", params["created_at"]);
  formData.append("timestamp", params["timestamp"]);
  formData.append("operator", params["operator"]);
  formData.append("part_id", params["part_id"]);
  formData.append("date", params["date"]);
  formData.append("time", params["time"]);
  formData.append("processing_time", params["processing_time"]);
  formData.append("media_wt_before", params["media_wt_before"]);
  formData.append("media_wt_after", params["media_wt_after"]);
  formData.append("media_used", params["media_used"]);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForPostBlastStation")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForGetBlastStation = (params) => {
  console.log("apiCallForGetBlastStation");
  const url = getBlastStation;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("part_id", params["part_id"]);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForGetBlastStation")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForPostPaintStation = (params) => {
  console.log("apiCallForPostPaintStation");
  const url = postPaintStation;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("machine_id", params["machine_id"]);
  formData.append("created_at", params["created_at"]);
  formData.append("timestamp", params["timestamp"]);
  formData.append("operator", params["operator"]);
  formData.append("part_id", params["part_id"]);
  formData.append("date", params["date"]);
  formData.append("time", params["time"]);
  formData.append("processing_time", params["processing_time"]);
  formData.append("bitu_wt_before", params["bitu_wt_before"]);
  formData.append("bitu_wt_after", params["bitu_wt_after"]);
  formData.append("bitu_used", params["bitu_used"]);
  formData.append("ambient_temp", params["ambient_temp"]);
  formData.append("ambient_humidity", params["ambient_humidity"]);
  formData.append("ambient_dewpoint", params["ambient_dewpoint"]);
  formData.append("paintbooth_temp", params["paintbooth_temp"]);
  formData.append("paintbooth_humidity", params["paintbooth_humidity"]);
  formData.append("paintbooth_dewpoint", params["paintbooth_dewpoint"]);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForPostPaintStation")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForGetPaintStation = (params) => {
  console.log("apiCallForGetPaintStation");
  const url = getPaintStation;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("part_id", params["part_id"]);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForGetPaintStation")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForGetTankTemperature = (params) => {
  console.log("apiCallForGetTankTemperature");
  const url = getTankTemperature;
  var formData = new FormData();
  formData.append("customerId", params["customer_id"]);
  formData.append("activeMin", "20");

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForGetTankTemperature")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForPostAssembly1Station = (params) => {
  console.log("apiCallForPostAssembly1Station");
  const url = postAssembly1Station;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("machine_id", params["machine_id"]);
  formData.append("created_at", params["created_at"]);
  formData.append("timestamp", params["timestamp"]);
  formData.append("operator", params["operator"]);
  formData.append("part_id", params["part_id"]);
  formData.append("date", params["date"]);
  formData.append("time", params["time"]);
  formData.append("processing_time", params["processing_time"]);

  formData.append("case_number", params["case_number"]);
  formData.append("serial", params["serial"]);
  formData.append("shipment", params["shipment"]);
  formData.append("empty_wt", params["empty_wt"]);

  formData.append("center_grav", params["center_grav"]);
  formData.append("aft_ass", params["aft_ass"]);
  formData.append("aft_retain_ring", params["aft_retain_ring"]);
  formData.append("shipping_cover", params["shipping_cover"]);

  formData.append("shipping_plug", params["shipping_plug"]);
  formData.append("end_cap", params["end_cap"]);
  formData.append("lugs", params["lugs"]);
  formData.append("retain_ring", params["retain_ring"]);

  formData.append("adapt_ring", params["adapt_ring"]);
  formData.append("impact_ring", params["impact_ring"]);
  formData.append("total_wt", params["total_wt"]);
  formData.append("ass_center_grav", params["ass_center_grav"]);

  // Usage 2
  formData.append("degrease_sol", params["degrease_sol"]);
  formData.append("corrosin_prev_compound", params["corrosin_prev_compound"]);
  formData.append("ship_cover_oring", params["ship_cover_oring"]);
  formData.append("oring_grease", params["oring_grease"]);

  formData.append("protective_end_cap", params["protective_end_cap"]);
  formData.append("end_cap_set_screw", params["end_cap_set_screw"]);
  formData.append("lifting_lug_bolt", params["lifting_lug_bolt"]);
  formData.append("lifting_lug_washer", params["lifting_lug_washer"]);

  formData.append("stencil_ink", params["stencil_ink"]);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForPostAssembly1Station")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForGetAssembly1Station = (params) => {
  console.log("apiCallForPostAssembly1Station");
  const url = getAssembly1Station;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("part_id", params["part_id"]);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForPostAssembly1Station")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};


export const apiCallForPostBlu136Assembly = (params) => {
  console.log("apiCallForPostBlu136Assembly");
  const url = postBlu136Assembly;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("machine_id", params["machine_id"]);
  formData.append("created_at", params["created_at"]);
  formData.append("timestamp", params["timestamp"]);
  formData.append("operator", params["operator"]);
  formData.append("part_id", params["part_id"]);
  formData.append("date", params["date"]);
  formData.append("time", params["time"]);
  formData.append("processing_time", params["processing_time"]);

  formData.append("base_plat11", params["p_base_plat11"]);
  formData.append("20199266_fwd_fuze_line", params["p_20199266_fwd_fuze_line"]);
  formData.append("20199367_aft_fuz_line", params["p_20199367_aft_fuz_line"]);
  formData.append("1265394_fit_chrg_tube", params["p_1265394_fit_chrg_tube"]);
  formData.append("1252629_washer_lock_iternal_tooth2", params["p_1252629_washer_lock_iternal_tooth2"]);
  formData.append("4902493_retainer_fuz_liner_aft", params["p_4902493_retainer_fuz_liner_aft"]);
  formData.append("1123646_nut_fit_charg_tube2", params["p_1123646_nut_fit_charg_tube2"]);
  formData.append("20199361_fwd_chrg_tube", params["p_20199361_fwd_chrg_tube"]);
  formData.append("20199361_030_aft_charg_tube", params["p_20199361_030_aft_charg_tube"]);
  formData.append("4512424_cap_shipping", params["p_4512424_cap_shipping"]);
  formData.append("nas1149f0832p_flat_washer8", params["p_nas1149f0832p_flat_washer8"]);
  formData.append("nas568_41_hex_head_bolt8", params["p_nas568_41_hex_head_bolt8"]);
  formData.append("x20173251_lug_shipping2", params["p_x20173251_lug_shipping2"]);
  formData.append("20199362_charge_tube_plug", params["p_20199362_charge_tube_plug"]);
  formData.append("nasm90725_31_screw_cap_hex_head2", params["p_nasm90725_31_screw_cap_hex_head2"]);
  formData.append("ms35338_45_washer_lock_sprg4", params["p_ms35338_45_washer_lock_sprg4"]);
  formData.append("mil_dtl_450_bituminous", params["p_mil_dtl_450_bituminous"]);
  formData.append("as3582_236_o_ring_small2", params["p_as3582_236_o_ring_small2"]);
  formData.append("923as694_o_ring_rubber", params["p_923as694_o_ring_rubber"]);
  formData.append("ms51964_69_set_screw1", params["p_ms51964_69_set_screw1"]);
  formData.append("a_a_208_ink_marking_stencil", params["p_a_a_208_ink_marking_stencil"]);
  formData.append("mil_prf_63460_gun_oil", params["p_mil_prf_63460_gun_oil"]);
  formData.append("mil_prf_16173_corrision_resistant_grease", params["p_mil_prf_16173_corrision_resistant_grease"]);
  formData.append("sae_as8660_silicone_lubricant", params["p_sae_as8660_silicone_lubricant"]);
  formData.append("mil_prf_680_degreasing_solvent", params["p_mil_prf_680_degreasing_solvent"]);
  formData.append("shipping_plugs2", params["p_shipping_plugs2"]);
  formData.append("job_at", params["p_job_at"]);
  formData.append("screw_lot6", params["p_screw_lot6"]);
  formData.append("threadlock_271_lot", params["p_threadlock_271_lot"]);
  formData.append("set_screw_lot_6", params["p_set_screw_lot_6"]);
  formData.append("ams_s_8802_lot", params["p_ams_s_8802_lot"]);
  formData.append("two_part_polysulfie_sealant", params["p_two_part_polysulfie_sealant"]);

  console.log("apiCallForPostBlu136Assembly=>", formData);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForPostBlu136Assembly")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForGetBlu136Assembly = (params) => {
  console.log("apiCallForPostBlu136Assembly");
  const url = getBlu136Assembly;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("part_id", params["part_id"]);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForPostBlu136Assembly")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForPostAssembly3Station = (params) => {
  console.log("apiCallForPostAssembly3Station");
  const url = postAssembly3Station;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("machine_id", params["machine_id"]);
  formData.append("created_at", params["created_at"]);
  formData.append("timestamp", params["timestamp"]);
  formData.append("operator", params["operator"]);
  formData.append("part_id", params["part_id"]);
  formData.append("date", params["date"]);
  formData.append("time", params["time"]);
  formData.append("processing_time", params["processing_time"]);
  formData.append("mil_d_16791_non_ionic_detergent_lot", params["p_mil_d_16791_non_ionic_detergent_lot"]);
  formData.append("923as829_drive_lok_pins_lot", params["p_923as829_drive_lok_pins_lot"]);
  formData.append("ams_s_8802_polysulfide_lot", params["p_ams_s_8802_polysulfide_lot"]);
  formData.append("4512421_base_plate_lt", params["p_4512421_base_plate_lt"]);
  formData.append("4512422_abs_insert_lot", params["p_4512422_abs_insert_lot"]);
  formData.append("4512423_steel_insert_lot", params["p_4512423_steel_insert_lot"]);


    console.log("apiCallForPostAssembly3Station=>", formData);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForPostAssembly3Station")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForGetAssembly3Station = (params) => {
  console.log("apiCallForPostAssembly3Station");
  const url = getAssembly3Station;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("part_id", params["part_id"]);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForPostAssembly3Station")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};


export const apiCallForPostQualityStation = (params) => {
  console.log("apiCallForPostQualityStation=>", params);
  const url = postQualityStation;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("machine_id", params["machine_id"]);
  formData.append("created_at", params["created_at"]);
  formData.append("timestamp", params["timestamp"]);
  formData.append("operator", params["operator"]);
  formData.append("part_id", params["part_id"]);
  formData.append("date", params["date"]);
  formData.append("time", params["time"]);
  formData.append("processing_time", params["processing_time"]);
  formData.append("comments", params["comments"]);
  formData.append("scrap_code", params["scrap_code"]);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForPostQualityStation")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};

export const apiCallForGetQualityStation = (params) => {
  console.log("apiCallForGetQualityStation");
  const url = getQualityStation;
  var formData = new FormData();
  formData.append("customer_id", params["customer_id"]);
  formData.append("part_id", params["part_id"]);

  return new Promise((resolve, reject) => {
    apiCallPost(url, formData, "apiCallForGetQualityStation")
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
};
