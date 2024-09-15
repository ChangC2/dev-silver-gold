// A wrapper for <Route> that redirects to the login

import { ROUTE_LOGIN } from "navigation/CONSTANTS";
import React from "react";
import { useDispatch } from "react-redux";
import { Redirect, Route } from "react-router-dom";
import { setFromUrl } from "redux/actions/userActions";
import { useAuth } from "./ProvideAuth";

import MainLayout from "layouts/MainLayout/MainLayout";

// screen if you're not yet authenticated.
function PrivateRoute({ children, ...rest }) {
  let auth = useAuth();
  const dispatch = useDispatch();

  if (auth.userData.name !== undefined && auth.userData.name !== "") {
    return (
      <MainLayout>
        <Route {...rest} />
      </MainLayout>
    );
  } else {
    dispatch(setFromUrl(rest.location));
    return (
      <Redirect
        to={{
          pathname: ROUTE_LOGIN,
        }}
      />
    );
  }
}
export default PrivateRoute;
