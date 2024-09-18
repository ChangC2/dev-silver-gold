const axios = require("axios").default;

export function apiCallForGetCards(callback) {
  const url =
    "https://api.trello.com/1/lists/65bc0fc3e32a922e74cfbc4a/cards?key=0fb86384c9bad803405ee107c506aed5&token=ATTAd64466d57780ab1868fe1ff3f998256bee5e04766de4e540570d7ae7a0b3d1377312BFE7";
  axios
    .get(url)
    .then((res) => {
      if (res != null && res.status == 200) {
        callback(res.data);
      }
    })
    .catch((error) => {
      callback(null);
    });
}

export function apiCallForUpdateCard(name, callback) {
  const url =
    "https://api.trello.com/1/cards/65bc11131570bff095aee403?name=" +
    encodeURIComponent(name) +
    "&key=0fb86384c9bad803405ee107c506aed5&token=ATTAd64466d57780ab1868fe1ff3f998256bee5e04766de4e540570d7ae7a0b3d1377312BFE7";
  axios
    .put(url)
    .then((res) => {
      if (res != null && res.status == 200) {
        callback(true);
      }
    })
    .catch((error) => {
      callback(null);
    });
}

export function apiCallForUpdateLabel(color, callback) {
  const url =
    "https://api.trello.com/1/labels/64d62c7a53ef33a7bd2f27de?key=0fb86384c9bad803405ee107c506aed5&token=ATTAd64466d57780ab1868fe1ff3f998256bee5e04766de4e540570d7ae7a0b3d1377312BFE7&color=" + color;
  axios
    .put(url)
    .then((res) => {
      if (res != null && res.status == 200) {
        callback(true);
      }
    })
    .catch((error) => {
      callback(null);
    });
}
