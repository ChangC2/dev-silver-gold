import { useDispatch, useSelector } from "react-redux";
import { apiCallForLogin } from "services/apiCall";

export function useProvideAuth() {
  const { userData } = useSelector((x) => x.userDataStore);
  const dispatch = useDispatch();
  // signin method: It can either return a promise or execute a callback function.
  // You can prefer to keep this in userServices.js
  const signin = (userDetail) => {
    return new Promise((resolve, reject) => {
      apiCallForLogin(userDetail)
        .then((res) => {
          resolve(res);
        })
        .catch((err) => {
          reject(err);
        });
    });
  };

  return {
    userData,
    signin,
  };
}
