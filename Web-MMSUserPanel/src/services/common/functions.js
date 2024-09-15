export function getEnumKeyByValue(Enum, val) {
  return Object.keys(Enum).find((key) => Enum[key] === val);
}

const VARIABLE_SPLIT = "|$$$|";
const VARIABLE_VALUE_SPLIT = "/$/";
const VARIABLE_STORAGE_STR = "variableView";
const parseVariableViewString = (variableViewStr) => {
  if (variableViewStr === null) {
    return [];
  }
  const viewItemList = variableViewStr.split(VARIABLE_SPLIT);

  const result = [];
  for (var i = 0; i < viewItemList.length; i++) {
    const oneItem = viewItemList[i];
    const values = oneItem.split(VARIABLE_VALUE_SPLIT);
    result.push({
      customer_id: values[0],
      machine_id: values[1],
      value: values[2],
    });
  }
  return result;
};

export const readVariableViewIndex = (customer_id, machine_id) => {
  const variableViewStr = localStorage.getItem(VARIABLE_STORAGE_STR);
  const variableList = parseVariableViewString(variableViewStr);
  const value = variableList.find(
    (x) => x.customer_id === customer_id && x.machine_id === machine_id
  );
  if (value === undefined) return 0;
  return parseInt(value["value"]);
};

export const writeVariableViewIndex = (customer_id, machine_id, indexValue) => {
  const variableViewStr = localStorage.getItem(VARIABLE_STORAGE_STR);
  
  const existingList = parseVariableViewString(variableViewStr);
  let variableList = [...existingList];
  const value = variableList.find(
    (x) => x.customer_id === customer_id && x.machine_id === machine_id
  );

  if (value === undefined) {
    variableList = [
      ...variableList,
      {
        customer_id: customer_id,
        machine_id: machine_id,
        value: indexValue,
      },
    ];
  } else {
    const index = variableList.indexOf(value);
    variableList[index]["value"] = indexValue;
  }
  const itemStringList = variableList.map(
    (x) =>
      x["customer_id"] +
      VARIABLE_VALUE_SPLIT +
      x["machine_id"] +
      VARIABLE_VALUE_SPLIT +
      x["value"]
  );

  const viewString = itemStringList.join(VARIABLE_SPLIT);
  localStorage.setItem(VARIABLE_STORAGE_STR, viewString);
};

export function parseDate(input, format) {
  format = format || "yyyy-mm-dd"; // default format
  var parts = input.match(/(\d+)/g),
    i = 0,
    fmt = {};
  // extract date-part indexes from the format
  format.replace(/(yyyy|dd|mm)/g, function (part) {
    fmt[part] = i++;
  });
  return new Date(parts[fmt["yyyy"]], parts[fmt["mm"]] - 1, parts[fmt["dd"]]);
}

export function onlyUnique(value, index, self) {
  return self.indexOf(value) === index;
}
